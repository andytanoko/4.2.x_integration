/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: OperationContext.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-11     Andrew Hill         Created
 * 2002-07-03     Andrew Hill         Attribute hashtable now only created first time needed
 * 2002-07-04     Andrew Hill         Attribute keys now Objects not Strings
 * 2002-08-26     Andrew Hill         Calls disposeFormFileElements() in GTActionFormBase on removal
 * 2002-09-23     Andrew Hill         Rename parent/stacked OC to previous & next
 * 2002-10-29     Andrew Hill         Make use of new InvalidOperationContextException
 * 2002-11-06     Andrew Hill         Add method to get OpCon by Id, and remove by id
 * 2002-11-16     Andrew Hill         Use HashMap instead of Hashtable for attributes
 * 2002-11-26     Andrew Hill         popOperationContext() now allows popped context to retain link to previous
 * 2003-01-02     Andrew Hill         Diversion message queues support (mostly untested yet!)
 * 2003-01-28     Andrew Hill         Support for OpCon Removal Listeners
 * 2003-03-12     Andrew Hill         Log saving & removal of opCons
 * 2003-07-23     Andrew Hill         toString()
 */
package com.gridnode.gtas.client.web.strutsbase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;

import com.gridnode.gtas.client.GTClientException;

/**
 * The OperationContext class exists to assist with the storing of information between requests
 * using the session scope. OpCons themselves are not internally threadsafe so dont have multiple
 * threads modifying the same OpCon at the same time. (Such situtaions generally call for multiple
 * independant OpCons)
 * An OperationContext may be used to store information required by a related group of requests,
 * such as would be involved in editing an entity for example. Each Operation Context has a unique
 * identifier (this is a combination of OPCONID_PREFIX and a running number) which is used to
 * identify it in session scope.
 * The OperationContext provides a getter and a setter for an ActionForm. Further support for
 * this is found in the com.gridnode.gtas.client.web.strutsbase.GTRequestProcessor class which
 * when presented with a request for an action where the bean is in session scope will look for the
 * OperationContext and obtain the ActionForm from there if available. See the docs of this class
 * for more details.
 * In addition the OperationContext provides setAttribute() and getAttribute() methods similar
 * to those found in the session and request and which may be used to store information for
 * the operation. Some common key bindings for this may be found in the interface
 * com.gridnode.gtas.client.web.IOperationContextKeys.
 * In order to identify which operation context is associated with a request, it is necessary to
 * have the browser pass the id of the OperationContext as a request parameter, either as
 * part of a GET query string, or when POSTing a form via a field. The name of this parameter
 * may be found in the constant string OperationContext.ID_PARAMETER. When using an input field
 * for this, it should also have an id with the value ID_ATTRIBUTE to assist renderers in locating
 * it.
 * When an operation is cancelled or completed, the associated OperationContext should be removed
 * from session scope. (When this is not done it will remain in the session until the
 * user logs out or the session expires.)
 * Operations may now be 'stacked'. This is done by creating a new OperationContext using the
 * constructor that takes the OperationContext to be stacked. It is currently left up to the caller
 * to replace the session attribute with the new parent OpCon, likewise for popping, however
 * this may be refactored soon.
 * 20020826AH: I have added a call to disposeFormFileElements() when the OpCon is removed. This call
 * only occurs if their is an ActionForm and it is a subclass of GTActionFormBase. This will clean up
 * any temporary files created by uploading and tracked by the ActionForm using the new methods
 * added to GTActionFormBase
 */
public class OperationContext
{
  private static final Log _log = LogFactory.getLog(OperationContext.class); // 20031209 DDJ  

  /**
   * Prefixed to a running number to create the attribute name under which operation contexts
   * are stored in the session scope.
   */
  public static final String OPCONID_PREFIX = "oc";

  /**
   * The parameter in which the operation context id is stored when the form
   * is submitted. In a POST operation this of course corresponds to the name attribute
   * of the hidden input field on the form.
   * When the OperationContext is saved, the id of it will also be stored in request scope
   * bound to this attribute. Note that this will overide any previous such attribute.
   * nb: the value of ID_PARAMETER (operationContextId) had to be hardcoded in several
   * javascripts. Be sure to update them if you need to change the value.
   */
  public static final String ID_PARAMETER = "operationContextId";

  /**
   * The id attribute of the input element that is used to store the operation context id
   * on the form when we are in browserland.
   */
  public static final String ID_ATTRIBUTE = "operation_context";

  /**
   * When the OperationContext cleanup filter is implemented it will make use of this parameter
   * tacked onto query strings to identify occasions when an OperationContext is no longer
   * required and remove it from session scope. (See also URLRewriterImpl & IURLRewriter)
   *
   * 20030221AH - WARNING - The value "rmoc" is also used in the jalan() method of the
   * navigationMethods.js file. That file contains JavaScript which cannot access the
   * constant defined here so I hardcoded it.
   * If you want to change the value to something else, be sure to update that
   * file accordingly or the rmocConfirmation prompting will not kick in.
   */
  public static final String REMOVAL_PARAMETER = "rmoc";

  /**
   * Counter used in generating operation context ids.
   */
  private static long idCounter = 0;

  private Map               _attributes;
  private String            _operationContextId;
  private ActionForm        _actionForm;
  private OperationContext  _previousOpCon = null;
  private ActionForward     _resumeForward; // Forward to resume using this OC if stacked
  private boolean           _ready = false;
  private boolean           _divRetFlag = false;
  private OperationContext  _nextOpCon;

  //20030128AH - Forward & back queues were public (accidently). Made private.
  //nb: these arent synced collections! Be careful! You probably shouldnt be playing with same opCon
  //from 2 threads, but there are a few places where it has to happen , and being a web app its quite
  //easy for it to happen when you dont really expect it!
  private ArrayList         _forwardQueue; //20030102AH - Queue for msgs being passed into a diversion
  private ArrayList         _backQueue;   //20030102AH - Queue for msgs being returned from a diversion

  private ArrayList         _removalListeners; //20030128AH

  /**
   * Constructor.
   * Create a new OperationContext with a unique id.
   */
  public OperationContext()
  {
    _operationContextId = newOperationContextId();
  }

  /**
   * Constructor.
   * Create a new OperationContext with a same id as passed one, and the passed one stacked.
   * (Note that it is up to the caller to save the new one over the top of the old one)
   * @param stackOC
   * @param actionForward to resume stacked operation
   */
  public OperationContext(OperationContext previousOpCon, ActionForward resumePrevious)
  {
    if (previousOpCon == null)
      throw new NullPointerException("previousOpCon is null"); //20030723AH
    _operationContextId = previousOpCon.getOperationContextId();
    previousOpCon.setResumeForward(resumePrevious);
    pushOperationContext(previousOpCon);
  }

  public void setReady(boolean ready)
  {
    _ready = ready;
  }

  /**
   * The divRet flag has been added to assist in detecting when one is returning from a diversion
   * - as it is necessary to know when to invoke processing of the divMsg objects on the back queue.
   * When returning from a diversion you should set this flag. The Action which is returned to has
   * the responsibility to clear it. (OperationDispatchAction handles this for you - but if you
   * arent using it then its your responsibility - as is invoking the divMsg processing for that
   * matter!)
   */
  void setDivRet(boolean flag)
  { //20030102AH
    _divRetFlag = flag;
  }

  boolean isDivRet()
  { //20030102AH
    return _divRetFlag;
  }

  /**
   * The ready flag is for use by actions, etc... using this OperationContext.
   * The EntityDispatchAction makes use of it when do OperationContext Stacking to determine if
   * the ActionForm and entity need to be setup in the OpCon or not...
   * (The exact meaning of this flag is thus defined by the actions using it.)
   */
  public boolean isReady()
  {
    return _ready;
  }

  public void setResumeForward(ActionForward resumeForward)
  {
    _resumeForward = resumeForward;
  }

  public ActionForward getResumeForward()
  {
    return _resumeForward;
  }

  public OperationContext getNextContext()
  {
    return _nextOpCon;
  }

  protected void setNextContext(OperationContext nextOpCon)
  {
    _nextOpCon = nextOpCon;
  }

  public OperationContext getPreviousContext()
  {
    return _previousOpCon;
  }

  protected void setPreviousContext(OperationContext previousOpCon)
  {
    _previousOpCon = previousOpCon;
  }

  /**
   * Stacks the specified OpCon under this one, setting its 'previous' reference to
   * point at this one.
   * 20030102AH - Now also copys in the divMsg object refs from the previousOpCons forward queue
   * Does not invoke processing of these messages
   */
  private void pushOperationContext(OperationContext previousOpCon)
  {
    //20030312AH - Logging
    if(previousOpCon == null) throw new NullPointerException("previousOpCon is null");
    Log log = LogFactory.getLog(this.getClass());
    if(log.isInfoEnabled())
    {
        log.info("Pushing opCon id='"
                + this.getOperationContextId()
                + "' onto opCon id='"
                + previousOpCon.getOperationContextId() + "'");
    }
    //..

    _previousOpCon = previousOpCon;
    previousOpCon.setNextContext(this);

    copyForwardDivMsgs(previousOpCon); //20030102AH
  }

  /**
   * Returns the 'previous' operations context if it is stacked under this one.
   * If none is stacked, returns null.
   * Note that it is up to the caller to save the popped OC over the old one.
   * 20030102AH - Now will also copy this opCons back divMsg objects into previous opCon
   * Does not invoke processing of these messages
   */
  public OperationContext popOperationContext()
  {
    OperationContext previousOpCon = _previousOpCon;
    if(previousOpCon != null)
    {
      //20030312AH - Logging
      Log log = LogFactory.getLog(this.getClass());
      if(log.isInfoEnabled())
      {
        log.info("Popping opCon id='"
                + previousOpCon.getOperationContextId()
                + "' from opCon id='"
                + this.getOperationContextId() + "'");
      }
      //..
      previousOpCon.setNextContext(null);
      previousOpCon.copyBackDivMsgs(this); //20030102AH
    }
    return previousOpCon;
  }

  protected void copyForwardDivMsgs(OperationContext src)
  { //20030102AH
    if(src==null) return;
    if(src._forwardQueue == null) return;
    Iterator iterator = src._forwardQueue.iterator();
    while(iterator.hasNext())
    {
      Object divMsg = iterator.next();
      this.addForwardDivMsg(divMsg);
    }
  }

  protected void copyBackDivMsgs(OperationContext src)
  { //20030102AH
    if(src==null) return;
    if(src._backQueue == null) return;
    Iterator iterator = src._backQueue.iterator();
    while(iterator.hasNext())
    {
      Object divMsg = iterator.next();
      this.addBackDivMsg(divMsg);
    }
  }

  /**
   * Get the id of this OperationContext
   * @return OperationContextId
   */
  public String getOperationContextId()
  {
    return _operationContextId;
  }

  /**
   * Retrieve object from OperationContext
   * @param name under which object is keyed
   * @return Object
   */
  public synchronized Object getAttribute(Object key)
  {
    if(_attributes != null)
      return _attributes.get(key);
    else
      return null;
  }

  /**
   * Store an object in the OperationContext
   * @param name used as key for object
   * @param object
   */
  public synchronized void setAttribute(Object key, Object value)
  {
    if(_attributes == null)
    {
      _attributes = Collections.synchronizedMap(new HashMap());
    }
    _attributes.put(key,value);
  }

  /**
   * Retrieve the ActionForm associated with this OperationContext
   */
  public ActionForm getActionForm()
  {
    return _actionForm;
  }

  /**
   * Set the ActionForm associated with this OperationContext
   * @param ActionForm
   */
  public void setActionForm(ActionForm actionForm)
  {
    _actionForm = actionForm;
  }

  /**
   * Static utility method to retrieve OperationContext from session scope
   * @param request
   * @return OperationContext
   * @throws OperationException
   */
  public static OperationContext getOperationContext(HttpServletRequest request)
    throws OperationException
  {
    String opConId = getOperationContextId(request);
    return getOperationContext(request, opConId);
  }

  public static OperationContext getOperationContext(HttpServletRequest request, String opConId)
    throws OperationException
  {
    if(opConId != null)
    {
      OperationContext context = (OperationContext)request.getSession().getAttribute(opConId);
      if(context == null)
      {
        throw new InvalidOperationContextException(opConId);
      }
      return context;
    }
    return null;
  }

  /**
   * Static utility function to determine the name under which this requests OperationContext
   * is bound in session scope.
   * @param request
   * @return contextId
   */
  public static String getOperationContextId(HttpServletRequest request)
  {
    String id = (String)request.getParameter(ID_PARAMETER);
    if(id == null)
    {
      id = (String)request.getAttribute(ID_PARAMETER); //20020618AH
    }
    return id;
  }

  /**
   * Static utility method to remove the OperationContext associated with this request from the
   * session scope.
   * @param request
   */
  public static void removeOperationContext(HttpServletRequest request)
    throws OperationException
  {
    String opConId = getOperationContextId(request);
    removeOperationContext(request, opConId);
  }


  /**
   * Static utility method to remove the OperationContext associated with this request from the
   * session scope. 20021106AH - Take id as method param
   * @param request
   */
  public static void removeOperationContext(HttpServletRequest request, String opConId)
    throws OperationException
  {
    if(opConId != null)
    {
      try
      {
        if(_log.isInfoEnabled())
        {
          _log.info("Removing OperationContext[id=" + opConId + "]");
        }
        //...
        try
        {
          OperationContext opCon = getOperationContext(request, opConId);
          opCon.invokeRemovalListeners(request); //20030128AH
          //20020826AH FormFile destroy handling (evil)
          ActionForm form = opCon.getActionForm();
          if(form != null)
          {
            if(form instanceof GTActionFormBase)
            {
              ((GTActionFormBase)form).disposeFormFileElements();
            }
          }
        }
        catch(Throwable t)
        {
          throw new OperationException("Error disposing of FormFileElements in ActionForm in OperationContext id='" + opConId + "'",t);
        }
        //And now remove the OperationContext object from the session
        request.getSession().removeAttribute(opConId);
        request.removeAttribute(ID_PARAMETER); //20020618AH
      }
      catch(Throwable t)
      {
        throw new OperationException("Could not remove OperationContext[id=" + opConId + "]", t);
      }
    }
  }

  public void save(HttpServletRequest request)
  {
    saveOperationContext(this,request);
  }

  /**
   * Static utility method to store an OperationContext in session scope.
   * @param OperationContext
   * @param request
   */
  public static void saveOperationContext(OperationContext opCon, HttpServletRequest request)
  {
    String opConId = opCon.getOperationContextId();
    if(_log.isInfoEnabled())
    {
      _log.info("Saving OperationContext [id=" + opConId + "]");
    }
    //...
    request.getSession().setAttribute(opConId, opCon);
    request.setAttribute(ID_PARAMETER, opConId); //20020618AH
  }

  /**
   * Static utility method to obtain a new operation context id.
   * @return id
   * @todo: ensure this will work clustered (which right now it probably wont...
   */
  protected static synchronized String newOperationContextId()
  {
    idCounter++;
    return(OPCONID_PREFIX + idCounter);
  }

  private void addDivMsg(List q, Object divMsg)
  { //20030102AH
    if(divMsg==null) return; //dont add null msg. (btw: queue will have been instantiated anyhow!)
    if(divMsg instanceof DivMsgList)
    {
      q.addAll((DivMsgList)divMsg);
    }
    else
    {
      q.add(divMsg);
    }
  }

  public synchronized  void addForwardDivMsg(Object divMsg)
  { //20030102AH
    //20030128AH - Synchronized
    if(_forwardQueue == null)  _forwardQueue = new ArrayList();
    addDivMsg(_forwardQueue,divMsg);
  }

  /**
   *
   */
  public synchronized void addBackDivMsg(Object divMsg)
  { //20030102AH
    //20030128AH - Synchronized
    if(_backQueue == null)  _backQueue = new ArrayList();
    addDivMsg(_backQueue,divMsg);
  }

  public void processForwardDivMsgs(Object context, IDivMsgProcessor processor)
    throws GTClientException
  { //20030102AH
    processDivMsgs(_forwardQueue,context,processor);
  }

  public void processBackDivMsgs(Object context, IDivMsgProcessor processor)
    throws GTClientException
  { //20030102AH
    processDivMsgs(_backQueue,context,processor);
  }

  private void processDivMsgs(List q,
                              Object context,
                              IDivMsgProcessor processor)
    throws GTClientException
  { //20030102AH
    try
    {
      //Some preparatory checking - shortcircuit returns if no need to processes anything
      if(processor == null) return; //Please... you are wasting everybodies time! No soup for you!
      int count = (q == null) ? 0 : q.size(); //Get queue size. (Empty queue maybe not instantiated)
      if(count < 1) return; //Nothing to see here. Move along please.
      if( ! ( (q == _forwardQueue) || (q == _backQueue) ) )
      { //Sanity check to make sure Im doing what I think Im doing
        throw new java.lang.IllegalStateException("Internal assertion failure - Bad queue:" + q);
      }
      ListIterator iterator = q.listIterator(); //Use a ListIterator so we can modify list
      while(iterator.hasNext())
      { //Now we invoke processing each message in the queue
        //We first remove this divMsg object from the queue
        Object divMsg = iterator.next();
        iterator.remove();
        //Now declare a reference to hold any divMsg(s) returned by the processing
        Object processedDivMsg = null;
        //The method we call to process the message depends on which queue it is in
        //We lumped processing for both queueus together in this method to reduce cut & paste
        //redundancy but they do need seperate processing.
        if(q == _forwardQueue)
        { //Our queue reference is to the forward queue
          try
          { //So we invoke processing for a forward message object, passing in the context
            //object we were passed. (We dont know what its used for - its probably
            //an ActionContext instance - but thats none of our business!)
            processedDivMsg = processor.processForwardDivMsg(context,divMsg);
          }
          catch(Throwable t)
          { //Catch errors here so we can report which queue had problems
            throw new GTClientException("Error processing forward divMsg",t);
          }
        }
        else if(q == _backQueue)
        { //like forward only its back...
          try
          {
            processedDivMsg = processor.processBackDivMsg(context,divMsg);
          }
          catch(Throwable t)
          {
            throw new GTClientException("Error processing back DivMsg",t);
          }
        }
        //If we were returned any divMsg(s) we now need to add them back to the queue.
        //Usually a processor will return a divMsg it doesnt care about to allow it to continue
        //propagation along the diversionary chain. It may however return an entirely different
        //message object, or by using the DivMsgList collection class, multiple message objects.
        //(Messages added back to the queue are put 'behind' the iterators cursor and are not
        //processed in this run - ListIterator.add())
        if(processedDivMsg != null)
        {
          if(processedDivMsg instanceof DivMsgList)
          { // Add multiple divMsg back to queue
            // If multiple messages returned then add each to the queue (discarding the DivMsgList
            // wrapper object itself)
            Iterator iterater = ((DivMsgList)processedDivMsg).iterator();
            // hehehe. Like my choice of variable names for the iterators? ;->
            while(iterater.hasNext())
            { //Alas, the ListIterator class lacks an AddAll method...
              iterator.add(iterater.next());
            }
          }
          else
          {
            // Add single divMsg message back to the queue
            iterator.add(processedDivMsg);
          }
        }
      }//wend
    }
    catch(Throwable t)
    { //opps...
      throw new GTClientException("Error processing a divMsg queue",t);
    }
  }

  public synchronized void addRemovalListener(IOperationContextRemovalListener listener)
  { //20030128AH
    //If it wasnt for the fact that we may have to create the list ourselves we could just
    //sync on the list. As it is we must do both :-( (all to save a few bytes....)
    if(_removalListeners == null)  _removalListeners = new ArrayList(1);
    synchronized(_removalListeners) //yet more time wasting synchronization that we need :-(
    {
      if(!_removalListeners.contains(listener))
      {
        _removalListeners.add(listener);
      }
    }
  }

  public synchronized void removeRemovalListener(IOperationContextRemovalListener listener)
  { //20030128AH
    if(_removalListeners == null)  return;
    synchronized(_removalListeners)
    {
      _removalListeners.remove(listener);
    }
  }

  void invokeRemovalListeners(HttpServletRequest request) throws OperationException
  { //20030128AH
    try
    {
      if(_removalListeners == null) return;
      synchronized(_removalListeners)
      { //Sync so nothing can touch the list while we are iterating it
        ListIterator iterator = _removalListeners.listIterator();
        while(iterator.hasNext())
        {
          IOperationContextRemovalListener listener = (IOperationContextRemovalListener)
                                                      iterator.next();
          try
          {
            listener.onRemoveOpCon(request, this);
            iterator.remove();
          }
          catch(Throwable t)
          {
            throw new OperationException("Error processing IOperationContextRemovalListener:"
              + listener);
          }
        }
      }
    }
    catch(Throwable t)
    {
      throw new OperationException("Error processing operation context removal listeners"
                + " for opCon with id=" + getOperationContextId(), t );
    }
  }

  public static synchronized void logActiveContexts(HttpServletRequest request, Log log)
  { //20030312AH - For testing/debugging purposes
    //20030425AH - Write results to log
    if(log.isInfoEnabled())
    {
      HttpSession session = request.getSession(false);
      if(session == null) return; //No session - no opCons...
      try
      {
        StringBuffer buffer = new StringBuffer("Active opCons: ");
        int count = 0;
        for(int i=0; i <= idCounter; i++)
        {
          String opConId = OPCONID_PREFIX + i;
          try
          {
            OperationContext oc = getOperationContext(request, opConId);
            buffer.append(opConId);
            buffer.append(", ");
            count++;
          }
          catch(Throwable t)
          {
             //swallow deliberately
          }
        }
        buffer.append("(");
        buffer.append(count);
        buffer.append(" opCons in session [");
        buffer.append(session.getId());
        buffer.append("])");
        log.info(buffer);
      }
      catch(Throwable t)
      {
        System.out.println("Error dumping opCons!");
        t.printStackTrace();
        // swallow exception. Not worth crashing for!
      }
    }
  }
  
  public String toString()
  { //20030723AH
    return "OperationContext[id="
            + _operationContextId
            + ", resumeUrl="
            + ( _resumeForward == null ? "null" : "\"" + _resumeForward.getPath() + "\"" )
            + ( _previousOpCon == null ? "]" : ", \nprevious=" + _previousOpCon + "]" );
  }
}