/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: OperationDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-29     Andrew Hill         Created
 * 2002-10-29     Andrew Hill         Modify entry methods to throw GTClientException
 * 2002-11-16     Andrew Hill         processPushOpCon() takes mappingName parameter now
 * 2002-12-11     Andrew Hill         Evicted createRenderingContext() to GTActionBase
 * 2003-01-02     Andrew Hill         Process DivMsgs in prepareOperationContext() method
 *                                    Implement IDivMsgProcessor (mostly untested yet!)
 * 2003-01-03     Andrew Hill         OperationRequest queue processing (mostly untested yet!)
 * 2003-01-24     Andrew Hill         Log some stuff
 * 2003-01-27     Andrew Hill         Inbuilt support for using opReqs to implement command pattern
 * 2003-01-28     Andrew Hill         Handle IOperationContextRemovalListener implementing opReqs
 * 2003-07-16     Andrew Hill         Remove deprecated version of processPushOpCon
 * 2003-07-23     Andrew Hill         Use const for param name for divertTo field
 * 2003-08-26     Andrew Hill         Stated getDivertForward() contract explicitly in the javadoc
 */
package com.gridnode.gtas.client.web.strutsbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.IRequestKeys;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.IRenderingPipeline;
import com.gridnode.gtas.client.web.renderers.RenderingContext;

public abstract class OperationDispatchAction extends GTDispatchActionBase implements IDivMsgProcessor
{
  private static final Log _log = LogFactory.getLog(OperationDispatchAction.class); // 20031209 DDJ

  //Attribute key under which the operationRequestQueue is stored in Operation Context
  private static final String OPERATION_REQUEST_QUEUE =
    "com.gridnode.gtas.client.web.strutsbase.OperationContext.OPERATION_REQUEST_QUEUE";//20030103AH
  
  public static final String DIVERT_TO_PARAM = "divertTo"; //20030723AH

  protected abstract IDocumentRenderer getFormRenderer( ActionContext actionContext,
                                                      RenderingContext rContext,
                                                      boolean edit)
    throws GTClientException;

  protected abstract String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException;

  protected abstract void initialiseActionForm(ActionContext actionContext)
    throws GTClientException;

  protected abstract ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException;


  protected abstract IRenderingPipeline getRenderingPipeline(ActionContext actionContext,
                                                    OperationContext opContext,
                                                    boolean edit)
    throws GTClientException;

  /**
   * @deprecated use prepareView()
   */
  protected final void prepareEntityView(ActionContext actionContext,
                                    OperationContext opCon,
                                    boolean edit)
    throws GTClientException
  {
    prepareView(actionContext,opCon,edit);
  }

  protected void prepareView(ActionContext actionContext,
                                    OperationContext opCon,
                                    boolean edit)
    throws GTClientException
  {
    try
    {
      IRenderingPipeline rPipe = getRenderingPipeline(actionContext,
                                                      opCon,
                                                      edit);
      actionContext.getRequest().setAttribute(IRequestKeys.RENDERERS, rPipe);
    }
    catch(Exception e)
    {
      throw new GTClientException(e);
    }
  }

  protected final OperationContext prepareOperationContext(ActionContext actionContext)
    throws GTClientException
  {
    //20030102AH - changed this method to final
    OperationContext opContext = OperationContext.getOperationContext(actionContext.getRequest());
    if(opContext==null)
    {
      return initialiseOperationContext(opContext, actionContext);
    }
    else
    {
      if(opContext.isReady())
      {
        if(opContext.isDivRet())
        { //20030102AH
          opContext.processBackDivMsgs(actionContext,this);
          opContext.setDivRet(false);
        }
        return opContext;
      }
      else
      {
        opContext = initialiseOperationContext(opContext, actionContext);
        opContext.processForwardDivMsgs(actionContext,this); //20030102AH
        return opContext;
      }
    }
  }

  /**
   * Called for new opCons and opCons where isReady()==false
   */
  protected OperationContext initialiseOperationContext( OperationContext opContext,
                                                      ActionContext actionContext)
    throws GTClientException
  {
    if(opContext == null)
    { //In the case of a stacked operation we already have an OpCon object but its
      //contents are not fully initialised. If it is not a stacked OpCon, then we must
      //also create the OpCon object.
      opContext = new OperationContext();
      if(_log.isInfoEnabled())
      { //20030124AH
        _log.info("Created new OperationContext[id=" + opContext.getOperationContextId() + "]");
      }
    }
    OperationContext.saveOperationContext(opContext, actionContext.getRequest());
    if(actionContext.getActionForm() == null)
    {
      if(_log.isInfoEnabled())
      { //20030124AH
        _log.info("Creating new ActionForm instance");
      }
      ActionForm newForm = createActionForm(actionContext);
      actionContext.setActionForm( newForm );
    }
    processPreparedOperationContext(actionContext, opContext);
    initialiseActionForm(actionContext);
    ActionForm form = actionContext.getActionForm();
    opContext.setActionForm(form);
    opContext.setReady(true);
    return opContext;
  }

  /**
   * Subclass may override to do additional population of OperationContext
   * (such as creating entities etc...)
   * @param actionContext
   * @param operationContext
   * @throws GTClientException
   */
  protected void processPreparedOperationContext( ActionContext actionContext,
                                                  OperationContext opContext)
    throws GTClientException
  {
    
  }


  protected void performCancelProcessing(ActionContext actionContext)
    throws GTClientException
  {
    
  }

  protected String performPreDivertProcessing(ActionContext actionContext, String divertMappingName)
    throws GTClientException
  {
    return divertMappingName;
  }

  public ActionForward cancel(ActionMapping mapping, ActionForm actionForm,
                                HttpServletRequest request, HttpServletResponse response)
                                throws IOException, ServletException, GTClientException
  { //20030312AH nb: 'decorated' in tda for dv support
    ActionContext actionContext = new ActionContext(mapping, actionForm, request, response);
    try
    {
      ActionForward forward = getCancelForward(actionContext);
      performCancelProcessing(actionContext);
      forward = completeOperation(actionContext,forward);
      return forward;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error cancelling operation",t);
    }
  }

  protected ActionForward getCancelForward(ActionContext actionContext)
    throws GTClientException
  { //20030312AH nb: 'decorated' in tda for dv support
    return actionContext.getMapping().findForward("listview");
  }

  protected ActionForward getCompleteForward(ActionContext actionContext)
    throws GTClientException
  {
    return actionContext.getMapping().findForward("listview");
    //20030312AH - Nb: I have overridden ('decorated') this in TaskDispatchAction to check if we are in
    //a detail view and return a forward to the action that writes js to close the window.
    //(If not in dv then delegates back here)
  }

  public final ActionForward completeOperation(ActionContext actionContext, ActionForward forward)
    throws GTClientException
  {
    //20030102AH - Changed this to a final method
    try
    {
      ActionForm actionForm = actionContext.getActionForm();
      HttpServletRequest request = actionContext.getRequest();
      OperationContext opCon = OperationContext.getOperationContext(request);
      processOpReqQueue(actionContext, opCon); //20030103AH
      if(opCon != null)
      {
        OperationContext previousOC = opCon.popOperationContext();
        if(previousOC != null)
        {
          previousOC.setDivRet(true); //20030102AH
          processPopOpCon(actionContext, opCon, previousOC);
          OperationContext.saveOperationContext(previousOC, request);
          forward = processSOCForward( previousOC.getResumeForward(), previousOC );
        }
        else
        {
          OperationContext.removeOperationContext(request);
        }
      }
      if(actionForm instanceof GTActionFormBase)
      { //20020827AH - ensure temp file cleaned up on cancel even if OC stacked
        ((GTActionFormBase)actionForm).disposeFormFileElements();
      }
      return forward;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error completing Operation",t);
    }
  }

  /**
   * Makes sure that the OperationContextId is preserved if this is a redirecting forward
   * (which most stacked OC ops will make use of).
   * Subclass may also override to add parameters.
   */
  protected ActionForward processSOCForward(ActionForward forward, OperationContext opCon)
    throws GTClientException
  {
      if(forward == null)
      {
        throw new java.lang.NullPointerException("Null forward");
      }
      else
      {
        if(forward.getRedirect())
        {
          String path = StaticWebUtils.addParameterToURL(forward.getPath(),
                                                         OperationContext.ID_PARAMETER,
                                                         opCon.getOperationContextId());
          forward = new ActionForward(path,true);
        }
      }
      return forward;
  }

  /**
   * Stack the OperationContext to a new one and forward to the mapping identified by
   * the divertTo parameter in the request.
   * 20030716AH - Finalised (for now)
   */
  public final ActionForward divert(ActionMapping mapping, ActionForm actionForm,
                                HttpServletRequest request, HttpServletResponse response)
                                throws IOException, ServletException, GTClientException
  {
    ActionContext actionContext = new ActionContext(mapping, actionForm, request, response);
    String divertMapping = (String)request.getParameter(DIVERT_TO_PARAM);
    try
    {
      divertMapping = performPreDivertProcessing(actionContext, divertMapping);
      OperationContext opCon = OperationContext.getOperationContext(request);
      if(opCon != null)
      {
        ActionForward resumeForward = getResumeForward(actionContext, actionForm);
        OperationContext newOpCon = new OperationContext(opCon, resumeForward);
        newOpCon.setReady(false); //This is actually default, but being explicit here
        processPushOpCon(actionContext, opCon, newOpCon, divertMapping);
        OperationContext.saveOperationContext(newOpCon, request);
        opCon = newOpCon;
      }
      return getDivertForward(actionContext, opCon, mapping, divertMapping);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error diverting to mapping " + divertMapping,t);
    }
  }

 /**
  * NOTE: subclasses that override this method, MUST delegate back to their superclass's getDivertForward
  * for any mappingName they arent planning to take specific action for.
  */
  protected ActionForward getDivertForward(ActionContext actionContext,
                                           OperationContext opCon,
                                           ActionMapping mapping,
                                           String divertMapping)
    throws GTClientException
  {
    if( !StaticUtils.stringNotEmpty(divertMapping) ) //20030723AH - Use staticUtils stringNotEmpty
    {
      throw new GTClientException("No mapping identified to divert to");
    }
    ActionForward forward = mapping.findForward(divertMapping); //20030220AH
    if(forward == null)
    { //20030723AH
      throw new NullPointerException("Forward \"" + divertMapping + "\" not found");
    }
    return processSOCForward( forward, opCon );
  }

  protected abstract ActionForward getResumeForward(ActionContext actionContext, ActionForm actionForm)
    throws GTClientException;

  /**
   * When the Operation is completing, if we are popping a parent Operation then this method is
   * called. Subclasses may override it to do custom processing. (For example setting an
   * attribute in the parent OpCon to return some information such as the uid of an entity...)
   */
  protected final void processPopOpCon( ActionContext actionContext,
                                  OperationContext thisOperation,
                                  OperationContext newOperation)
    throws GTClientException
  {
    
  }

  /**
   * When diverting to another operation and stacking this one then this method is called.
   * Subclass may override to do custom processing at this point. (For example setting an
   * attribute in the child OpCon to pass some information such as the uid of an entity...)
   * @param actionContext
   * @param opContext - OpCon we diverted from
   * @param newOpCon - OpCon we have diverted into
   * @param mappingName - Name of diversion mapping
   */
  protected void processPushOpCon(ActionContext actionContext,
                                  OperationContext opCon,
                                  OperationContext newOpCon,
                                  String mappingName)
    throws GTClientException
  { //20021115, 20030716AH - No longer call deprecated method (removed)
    //hook For subclass to override if it wants
  }

  /**
   * Returns a reference to the Operation Request Queue. If create is true the queue will be
   * created if necessary.
   * @param opCon the operation context contating queue to return
   * @param create flag to request creation of the queue if it hasnt yet been instantiated
   * @return opReqQueue
   * @throws GTClientException
   */
  private static List getOpReqQueue(OperationContext opCon, boolean create)
    throws GTClientException
  { //20030103AH
    if(opCon == null) throw new NullPointerException("opCon is null"); //20030416AH
    try
    {
      List orq = null;
      synchronized(opCon)
      { //20030128 - Sync on the opCon - not this action instance
        orq = (List)opCon.getAttribute(OPERATION_REQUEST_QUEUE);
        if( (orq == null) && create )
        { // Create the queue if it is not yet instantiated and the create flag has been saet
          orq = new ArrayList();
          opCon.setAttribute(OPERATION_REQUEST_QUEUE,orq);
        }
      }
      return orq;
    }
    catch(Throwable t)
    { //smile .... it'll never happen!
      throw new GTClientException("Error obtaining reference to Operation Request Queue",t);
    }
  }

  /**
   * Add an object to the Operations Operation Request Queue, for processing upon completion.
   * This will be called by OperationDispatchAction for any forwardDivMsgs implementing the
   * IOpReqDivMsg interface. If that forwardDivMsg is an OpReqDivMsgWrapper the contained object
   * will be automatically extracted (and the wrapper discarded). DivMsgLists, wrapped or not
   * will be exploded. You may also call this method yourself. Dont be calling it while something
   * has an iterator on the opReqQueue though or you will get a visit from the rather nasty
   * Mr ConcurrentModificationException....
   *
   * The OperationRequestQueue is a feature provided by the OperationDispatchAction (and thus
   * available to all its subclasses). It allows a queue of objects to be built up that will
   * be processed when the operation is completed at which time the OperationDispatchAction will
   * invoke processing of these objects by a method which may be overridden by subclasses.
   * The objects on this queue do not have to implement IOpReqDivMsg.
   *
   * When the forwardDivMsg objects sent to this operation are processed, OperationDispatchAction
   * will automatically move any objects implementing IOpReqDivMsg to the OperationQueue - taking
   * them out of he forwardDivMsg queue - calling this method to add them to the Operation Request
   * Queue.
   *
   * @param actionContext
   * @param opReqDivMsg an object implementing IOpReqDivMsg
   * @throws GTClientException
   * @throws NullPointerException if either parameter is null
   */
  protected void addOpReq(ActionContext actionContext, Object opReq)
    throws GTClientException
  { //20030103AH
    try
    {
      if(actionContext == null) throw new NullPointerException("actionContext is null"); //20030416AH
      if(opReq == null) throw new NullPointerException("opReq is null"); //20030416AH
      OperationContext opCon = OperationContext.getOperationContext(actionContext.getRequest());
      List orq = getOpReqQueue(opCon,true); //retrieval also syncs internally on the opCon
      synchronized(orq)
      { //20030127AH - Synchronized on the queue (as the process method uses a failfast ListIterator)
        //Really you shouldnt be playing round with multiple threads on same opCon, but....
        if(opReq instanceof OpReqDivMsgWrapper)
        { // If the real opReq is wrapped then automatically unwrap it
          opReq = ((OpReqDivMsgWrapper)opReq).getOpReq();
        }
        if(opReq instanceof DivMsgList)
        { // If the opReq is a DivMsgList then explode it to add all the opReqs inside
          Iterator i = ((DivMsgList)opReq).iterator();  //20030128AH
          while(i.hasNext()) addSingleOpReq(opCon, orq, i.next()); //20030128AH
        }
        else
        { // Add single opReq
          addSingleOpReq(opCon, orq, opReq);  //20030128AH
        }
      }
    }
    catch(Throwable t)
    { //This is probably a bad omen.....
      throw new GTClientException("Error adding opReqDivMsg to Operation Request Queue",t);
    }
  }

  /**
   * Add individual opReq to queue, and if it implements IOperationContextRemovalListener
   * also add it as a removal listener in the opCon
   */
  private void addSingleOpReq(OperationContext opCon, List orq, Object opReq)
    throws GTClientException
  {  //20030128AH
    if(opReq instanceof IOperationContextRemovalListener)
    {
      opCon.addRemovalListener( (IOperationContextRemovalListener)opReq );
    }
    orq.add(opReq);
  }

  /**
   * Process the contents of the Operation Request queue.
   *
   * Some notes on opReqs and the Operation Request Queue:
   * The contents of this queue are objects (which we call opReqs). These dont have to implement
   * any particular interface or subclass any particular class. - This is for flexibility though
   * it may prove not to be an optimal architectural choice. (We shall see how things evolve and
   * may need to refactor that later). The opReqs basically provide a means of intra-operation
   * 'comminication' (as opposed to the intra-operation communication provided by DivMsgs) allowing
   * us to keep track of objects that we are aware of before the operation is completeted but that
   * need to be processed when the operation is completed rather than at the time we are aware of
   * this need.
   * Example scenario: divMsgs (IOpReqDivMsg) that request some information to
   * be returned to the calling operation. These come in on the forward divMsg queue but we dont
   * have the information yet (operation is only starting at that point) so we need to keep track
   * of them till the completion of the operation - so we put them (or rather
   * OperationDispatchAction puts them) on the Operation Request Queue. When the operation
   * is being completed they are processed and at that time we have the necessary information
   * so can then create a backDivMsg to return which contains the requested information...
   *
   * This method is invoked from the completeOperationMethod().
   * Prior to iterating the list of opReqs it will call the method
   * getCompletionContext() to give subclasses a chance to return an object that will be passed
   * to each invokation of the processOpReq() method and may be used to carry additional data
   * for use in processing. The meaning of this object is not of interest to OperationDispatchAction.
   * 20030127AH: Added support for Executable Op Reqs - these must implement IExecutableOpReq and
   * instead of processOpReq being called, the ExecutableOpReq's execute() method is called
   * though it may return false to indicate further processing requireds in which case it IS passed
   * to subclass (obviously after its execute method is called to return the false first!...)
   * If the queue has not been instantiated then getCompletionContext() will not be called.
   * @param actionContext
   * @param opCon the operation context of the operation that is about to be completed
   * @throws GTClientException
   */
  private void processOpReqQueue( ActionContext actionContext,
                                  OperationContext opCon)
    throws GTClientException
  { //20030103AH
    //20030127AH - Added inbuilt support for implementing command pattern with opReqs
    try
    {
      List orq = getOpReqQueue(opCon,false);
      if(orq != null)
      { // Dont do any processing if there are no opReqs
        // (Also dont bother getting the completionContext object)
        Object completionContext = getCompletionContext(actionContext);
        ListIterator iterator = orq.listIterator(); //20030127AH
        while(iterator.hasNext())
        { //Call processOpReq() for each operation request object in the queue
          Object opReq = iterator.next();
          boolean processed = false;
          if(opReq instanceof IExecutableOpReq)
          { //20030127AH - Inbuild support for using command pattern with opReqs
            try
            {
              processed = ((IExecutableOpReq)opReq).execute(actionContext, this, opCon);
            }
            catch(Throwable t)
            {
              throw new GTClientException("Error executing OpReq:"
                                          + opReq,t);
            }
          }
          if(processed == false)
          { //Check to see if processed completely already
            try
            { //20030127AH - Check each one for errors
              processed = processOpReq(actionContext, opCon, completionContext, opReq);
            }
            catch(Throwable t)
            {
              throw new GTClientException("Error delegating to subclass to process opReq:"
                                          + opReq,t);
            }
          }
          if(processed)
          { //20030127AH
            iterator.remove();
            if(opReq instanceof IOperationContextRemovalListener)
            { //20030128AH - Remove it from removal listeners list since we processed it already
              opCon.removeRemovalListener((IOperationContextRemovalListener)opReq);
            }
          }
        }
      }
    }
    catch(Throwable t)
    { //please..... make the hurting stop! :-(
      throw new GTClientException("Error processing Operation Request Queue",t);
    }
  }

  /**
   * Process a single opReq.
   * This default implementation does nothing.
   * @param actionContext
   * @param opCon the OperationContext whose operation is about to be completed
   * @param completionContext an object whose meaning is determined by subclasses involved
   * @param opReq the object to be processed
   * @return processed return true to have opReq removed from queue (20030127AH)
   * @throws GTClientException
   */
  protected boolean processOpReq(ActionContext actionContext,
                              OperationContext opCon,
                              Object completionContext,
                              Object opReq)
    throws GTClientException
  { //20030103AH
    return true; //Subclass to override
  }

  /**
   * Gives subclasses a chance to return an object that will be passed to each invokation of the
   * processOpReq() method. The meaning of this object is determined by the contract between the
   * subclass and its own subclasses (if any) and is not of interest to OperationDispatchAction.
   * It might (for example) contain such info as whether the operation was a success or not, etc...
   * This method will only be called if there are any opReqs to process.
   * This default implementation simply returns null.
   * @param actionContext
   * @return completionContext an object whose meaning is up to the subclasses involved
   * @throws GTClientException
   */
  protected Object getCompletionContext(ActionContext actionContext)
    throws GTClientException
  { //20030103AH
    return null; //subclass may override
  }

  public Object processForwardDivMsg(Object context, Object divMsg)
    throws GTClientException
  { //20030102AH
    //Subclass overriding this should make very certain that they delegate processing of any
    //divMsg they dont care about to their superclass. For general processing (ie: non framework
    //classes it is recommended to use doProcessForwardDivMsg)
    //context MUST be the ActionContext - you shouldnt be calling this method yourself anyway
    //I dont really want this to be public... but im implementing an interface here - in this case
    //IDivMsgProcessor - and this method is called by the OperationContext class itself, rather
    //than by OperationDispatchAction. (And no. I dont want to change the interface into
    //an abstract class...)
    try
    {
      if(divMsg instanceof IOpReqDivMsg)
      { //20030103AH - Automatically move the IOpReqDivMsg objects to the Operation Request Queue
        addOpReq( (ActionContext)context, divMsg); //Add it to the operation request queue
        return null; //Ensure removal of the opReqDivMsg from the forward queue
      }
      else
      { // Invoke the normal processing in the method which the subclass will override.
        return doProcessForwardDivMsg((ActionContext)context,divMsg);
      }
    }
    catch(Throwable t)
    { //why oh why! oh cruel world - hast thou no pity?
      throw new GTClientException("Error processing forwardDivMsg",t);
    }
  }

  public Object processBackDivMsg(Object context, Object divMsg)
    throws GTClientException
  { //20030102AH
    //Subclass overriding this should make very certain that they delegate processing of any
    //divMsg they dont care about to their superclass. For general processing (ie: non framework
    //classes it is recommended to use doProcessBackDivMsg)
    //context MUST be the ActionContext - you shouldnt be calling this method yourself anyway
    try
    {
      //Invoke normal processing of the divMsg object by calling the method which the subclass
      //will override to process divMsg objects
      return doProcessBackDivMsg((ActionContext)context,divMsg);
    }
    catch(Throwable t)
    { //noooooooooooo!
      throw new GTClientException("Error processing backDivMsg",t);
    }
  }

  protected Object doProcessForwardDivMsg(ActionContext actionContext, Object divMsg)
    throws GTClientException
  { //20030102AH
    return divMsg; //Subclass may override.
    //When subclassing, one should call the superclass impl of this method to handle divMsgs
    //the subclass doesnt understand
    //btw: dont call addForwardDivMsg() from in here unless you like ConcurrentModificationExceptions
  }

  protected Object doProcessBackDivMsg(ActionContext actionContext, Object divMsg)
    throws GTClientException
  { //20030102AH
    return divMsg; //Subclass may override
    //When subclassing, one should call the superclass impl of this method to handle divMsgs
    //the subclass doesnt understand
    //btw: dont call addBackDivMsg() from in here unless you like ConcurrentModificationExceptions
  }
}