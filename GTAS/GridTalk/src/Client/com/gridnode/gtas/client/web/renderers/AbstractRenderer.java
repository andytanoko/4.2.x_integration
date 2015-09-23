/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-14     Andrew Hill         Created
 * 2002-05-30     Andrew Hill         Refactored to eliminate xmlc library use
 * 2002-06-03     Andrew Hill         Refactored to make render(Document target) final
 * 2002-06-12     Daniel D'Cotta      Initialised ValueElementFactory in
 *                                    render(Document targetDocument) method for all
 *                                    renderers to make use of it
 * 2002-06-16     Andrew Hill         Added renderSelectOptions() method
 * 2002-08-??     Andrew Hill         Enhanced select option handling
 * 2002-08-??     Andrew Hill         Eliminated ValueElement code
 * 2002-08-28     Andrew Hill         appendParameter() methods
 * 2002-09-06     Andrew Hill         Added getNodeType() method (moved from BFPR)
 * 2002-09-21     Andrew Hill         findElement90, appendEventMethod(), appendOnloadMethod()
 * 2002-09-23     Andrew Hill         includeJavaScript() method added
 * 2002-10-09     Andrew Hill         canDelete() support in renderCommonFormElements()
 * 2002-10-28     Andrew Hill         renderLabel with params. Protected some incorrectly public methods
 * 2002-11-18     Andrew Hill         renameNode()
 * 2002-11-20     Andrew Hill         addButtonLink() (NO GOOD - DONT USE! (YET)) <removed20030325ah>
 * 2002-12-17     Andrew Hill         renderScript()
 * 2002-12-26     Andrew Hill         addListviewLinkButton() (Not as ambitious as addButtonLink() was!)
 * 2003-01-13     Andrew Hill         renderSelectedOptions() return boolean
 * 2003-01-30     Andrew Hill         Fix bug in renameNode()
 * 2003-03-03     Andrew Hill         insertFirstChild()
 * 2003-03-12     Andrew Hill         isDetailView()
 * 2003-03-21     Andrew Hill         disableLink()
 * 2003-03-25     Andrew Hill         createButtonLink()
 * 2003-03-28     Andrew Hill         Refactor addListviewButtonLink to use createButtonLink
 * 2003-04-08     Andrew Hill         Use script url in IGlobals
 * 2003-04-23     Andrew Hill         renderFieldError()
 * 2003-04-25     Andrew Hill         Modified addListviewLinkButton to append IS_NOT_ELV_DIVERT flag
 * 2003-05-07     Andrew Hill         removeFields()
 * 2003-05-18     Andrew Hill         renderSelectElements() that takes element as param
 * 2003-06-25     Andrew Hill         sortSelectOptions()
 * 2003-07-10     Andrew Hill         getLayoutElement()
 * 2003-08-26     Andrew Hill         makeLink()
 * 2003-09-15     Daniel D'Cotta      Added i18n support for IEnumeratedConstraint
 *                                    to renderSelectOptions() 
 */
package com.gridnode.gtas.client.web.renderers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.gridnode.gtas.client.ctrl.IEnumeratedConstraint;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.utils.IFilter;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.IOperationContextKeys;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction;
import com.gridnode.gtas.client.web.strutsbase.MessageUtils;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.strutsbase.OperationException;
import com.gridnode.gtas.client.web.strutsbase.TaskDispatchAction;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

/**
 * Provides some methods that may be of use to classes implementing IDocumentRenderer.
 * Such classes may make use of these methods by subclassing this class.
 */
public abstract class AbstractRenderer implements IDocumentRenderer
{
  private static final Log _log = LogFactory.getLog(AbstractRenderer.class); // 20031209 DDJ

  // Reference to target document which is set in the render(Document) method
  protected Document _target;
  private RenderingContext _renderingContext;

  private static int NL_OFFSET = "\n".length();

  public static final int BUTTON_LINK_RAW                         = 0;
  public static final int BUTTON_LINK_URL                         = 1;
  public static final int BUTTON_LINK_SUBMIT_MULTIPLE_ENTITIES    = 2;

  public static final String DISABLED_LINK_STYLECLASS = "disabledlink"; //20030321AH

  private OptionComparator _optionComparator = null; //20030625AH

  /**
   * Constructor. Subclasses should call this constructor from their own in order to
   * ensure that the rendering context is initialised.
   */
  public AbstractRenderer(RenderingContext rContext)
  {
    setRenderingContext(rContext);
  }

  /**
   * Set the RenderingContext used by this renderer.
   * @param rContext
   */
  protected void setRenderingContext(RenderingContext rContext)
  {
    if(rContext == null)
    {
      throw new java.lang.NullPointerException("RenderingContext is null");
    }
    else
    {
      _renderingContext = rContext;
    }
  }

  /**
   * Return the renderingContext used by this renderer.
   * @return RenderingContext
   */
  protected RenderingContext getRenderingContext()
  {
    return _renderingContext;
  }

  /**
   * Invoke rendering to the target document and return resulting document.
   * This method is declared final so subclasses cannot override it. It will store a reference
   * to the target document in _target memeber variable for use by various utility methods and
   * call the render() method with no args. The targetDocument passed will be returned.
   * @param targetDocument
   * @return targetDocument with modifications
   * @throws RenderingException
   */
  public final Document render(Document targetDocument) throws RenderingException
  {
    _target = targetDocument;

    /*// 20020613 DDJ: Initialise ValueElementFactory
    ISimpleResourceLookup rLookup = getRenderingContext().getResourceLookup();
    _valueElementFactory = new ValueElementFactory(_target, rLookup);*/

    render();

    return _target;
  }

  /**
   * Will search the specified node & its children for the 'first' element containing the attribute
   * named by the attribute parameter which contains the value specified in the id parameter.
   * If cannot such an element it will return null.
   * example: Element bob = getElementByAttributeValue("bob","id",_target);
   * @param id value to match
   * @param attribute to check value of
   * @param node tree to search
   * @return Element
   */
  protected final static Element getElementByAttributeValue(String id, String attribute, Node node)
  {
    if(node == null)      throw new java.lang.NullPointerException("node to search is null");
    if(id == null)        throw new java.lang.NullPointerException("value to search for is null");
    if(attribute == null) throw new java.lang.NullPointerException("attribute to search is null");

    return getElementByAttributeValueInternal(id,attribute,node);
  }

  private final static Element getElementByAttributeValueInternal(String id, String attribute, Node node)
  {
    if (node.getNodeType() == Node.ELEMENT_NODE)
    {
      Element elem = (Element)node;
      Attr nodeId = elem.getAttributeNode(attribute);
      if ((nodeId != null) && nodeId.getValue().equalsIgnoreCase(id))
      {
          return (Element)node;
      }
    }
    for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
    {
      Element childElem = getElementByAttributeValueInternal(id, attribute, child);
      if (childElem != null)
      {
          return childElem;
      }
    }
    return null;
  }

  /**
   * Will search the specified node & its children for all elements containing the attribute
   * named by the attribute parameter which contains the value specified in the id parameter.
   * If cannot such an element it will return an array.
   * example: Element[] bobs = getElementsByAttributeValue("bob","id",_target);
   * @param id value to match
   * @param attribute to check value of
   * @param node tree to search
   * @return Element[]
   */
  protected final static Element[] getElementsByAttributeValue(String id, String attribute, Node node)
  {
    if(node == null)      throw new java.lang.NullPointerException("node to search is null");
    if(id == null)        throw new java.lang.NullPointerException("value to search for is null");
    if(attribute == null) throw new java.lang.NullPointerException("attribute to search is null");

    return getElementsByAttributeValueInternal(id,attribute,node);
  }

  private final static Element[] getElementsByAttributeValueInternal(String id, String attribute, Node node)
  {
    ArrayList list = new ArrayList();
    if (node.getNodeType() == Node.ELEMENT_NODE)
    {
      Element elem = (Element)node;
      Attr nodeId = elem.getAttributeNode(attribute);
      if ((nodeId != null) && nodeId.getValue().equalsIgnoreCase(id))
      {
          list.add(node);
      }
    }
    for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
    {
      Element[] childElems = getElementsByAttributeValueInternal(id, attribute, child);
      for (int i=0; i<childElems.length; i++)
      {
          list.add(childElems[i]);
      }
    }
    return (Element[])list.toArray(new Element[list.size()]);
  }

  /**
   * Will remove all the child nodes of node and replace with a single child node containing
   * new text. If the node passed is null will do nothing.
   * (ReplaceWITHText might therefore be a better name!)
   * @param node
   * @param newText
   */
  protected void replaceText(Node node, String newText)
  {
    if(node != null)
    {
      Document doc = node.getOwnerDocument();
      Text newTextNode = doc.createTextNode(newText);
      if(node.hasChildNodes())
      {
        removeAllChildren(node);
      }
      node.appendChild(newTextNode);
    }
  }

  /**
   * 20021210AH
   * Replaces text without damaging non text children. (entity stuff counts as text)
   */
  protected void replaceTextCarefully(Node node, String newText)
  {
    if(node != null)
    {
      NodeList children = node.getChildNodes();
      int numChildren = children.getLength();
      Node[] copyOfChildren = new Node[numChildren];
      for(int i=0; i < numChildren; i++)
      {
        copyOfChildren[i] = children.item(i);
      }
      for(int j=0; j < numChildren; j++)
      {
        Node child = copyOfChildren[j];
        short childType = child.getNodeType();
        switch(childType)
        {
          case Node.TEXT_NODE:
          case Node.ENTITY_NODE:
          case Node.ENTITY_REFERENCE_NODE:
            node.removeChild(child);
            break;

          default:
            // leave in place
            break;
        }
      }
      Text newTextNode = _target.createTextNode(newText);
      node.appendChild(newTextNode);
    }
  }

  protected void replaceMultilineText(Node node, String newText)
  {
    if(node != null)
    {
      Document doc = node.getOwnerDocument();
      Text newTextNode = null;
      if(node.hasChildNodes())
      {
        removeAllChildren(node);
      }
      int index = newText.indexOf("\n");
      while(index != -1)
      {
        String text = newText.substring(0,index);
        newText = newText.substring(index+NL_OFFSET,newText.length());
        index = newText.indexOf("\n");
        node.appendChild(doc.createTextNode(text));
        node.appendChild(doc.createElement("br"));
      }
      newTextNode = doc.createTextNode(newText);
      node.appendChild(newTextNode);
    }
  }

  /**
   * Will remove all the child nodes of the element and replace with a single child node containing
   * new text. If the node passed is null will do nothing.
   * (ReplaceWITHText might therefore be a better name!)
   * @param elementId id attribute value of the element in the target document
   * @param newText text value for text node that will replace children
   * @param checkExists if true an exception will be thrown if the node doesnt exists
   * @throws RenderingException
   */
  protected void replaceText(String elementId, String newText, boolean checkExists)
    throws RenderingException
  {
    replaceText(getElementById(elementId, checkExists), newText);
  }

  protected void replaceMultilineText(String elementId, String newText, boolean checkExists)
    throws RenderingException
  {
    replaceMultilineText(getElementById(elementId, checkExists), newText);
  }

  /**
   * Removes all childNodes of node.
   * If node is null does nothing.
   * @param node
   */
  protected static void removeAllChildren(Node node)
  {
    if(node == null) return;
    while(node.hasChildNodes())
    {
      node.removeChild(node.getFirstChild());
    }
  }

  protected void removeAllChildren(String id, boolean checkExists)
    throws RenderingException
  {
    try
    {
      Node node = getElementById(id,checkExists);
      removeAllChildren(node);
    }
    catch(Exception e)
    {
      throw new RenderingException("Error attempting to remove children of node id=" + id,e);
    }
  }

  /**
   *  Subclasses should override this method to perform rendering to the target document
   *  defined in the member variable _target.
   */
  protected abstract void render() throws RenderingException;

  /**
   * Will search the target document and return the 'first' element whose "id" attribute matches
   * specified value, or null if such an element cannot be found.
   * @param idValue
   * @return Element
   */
  protected Element getElementById(String idValue)
  {
    try
    {
      return getElementById(idValue, false);
    }
    catch(Exception e)
    {
      return null;
    }
  }

  /**
   * Will search the target document and return the 'first' element whose "id" attribute matches
   * specified value. If checkExists is true will throw a RenderingException if element with an
   * id attribute with specified value is not found.
   * @param idValue
   * @return Element
   */
  protected Element getElementById(String idValue, boolean checkExists) throws RenderingException
  {
    if(idValue == null) throw new java.lang.NullPointerException("idValue is null");
    if(idValue.equals("")) return null;
    Element element = getElementByAttributeValueInternal(idValue, "id", _target);

    if(checkExists && (element == null))
    {
      throw new RenderingException("Element with id=" + idValue + " not found");
    }
    return element;
  }

  /**
   * Will search the target document and return all elements whose "id" attribute matches
   * specified value, or empty array if such an element cannot be found.
   * @param idValue
   * @return Element[]
   */
  protected Element[] getElementsById(String idValue)
  {
    try
    {
      return getElementsById(idValue, false);
    }
    catch(Exception e)
    {
      return new Element[0];
    }
  }

  /**
   * Will search the target document and return all elements whose "id" attribute matches
   * specified value. If checkExists is true will throw a RenderingException if element with an
   * id attribute with specified value is not found.
   * @param idValue
   * @return Element
   */
  protected Element[] getElementsById(String idValue, boolean checkExists) throws RenderingException
  {
    if(idValue == null) throw new java.lang.NullPointerException("idValue is null");
    if(idValue.equals("")) return null;
    Element[] elements = getElementsByAttributeValueInternal(idValue, "id", _target);

    if(checkExists && (elements.length == 0))
    {
      throw new RenderingException("Element with id=" + idValue + " not found");
    }
    return elements;
  }

  /**
   * Will search for the element that has an "id" attribute equal to labelElementId and will
   * remove all of its child nodes and replace them with a text node containing the text looked
   * up from the currently set resource lookup object based on labelKey. It is designed for use
   * with any element that can contain a text node - not just the &lt;label&gt; element. Be warned
   * that all the children of the be replaced by a new text node, so if for example you have an
   * &lt;input&gt; element inside your label element you could be in for a nasty surprise.
   * If this cannot be done then will do nothing.
   * @param labelElementId that identifies the label element
   * @param labelKey
   * @return element (text parent) or null if not found
   */
  protected Element renderLabel(String labelElementId, String labelKey)
  {
    try
    {
      return renderLabel(labelElementId,labelKey,false);
    }
    catch(Exception e)
    {
      return null;
    }
  }

  protected Element renderLabel(String labelElementId, String labelKey, boolean checkExists)
    throws RenderingException
  {
    Element element = getElementById(labelElementId, checkExists);
    replaceText(element, getRenderingContext().getResourceLookup().getMessage(labelKey) );
    return element;
  }

  protected Element renderLabelCarefully(String labelElementId, String labelKey, boolean checkExists)
    throws RenderingException
  { //20021210AH
    Element element = getElementById(labelElementId, checkExists);
    replaceTextCarefully(element, getRenderingContext().getResourceLookup().getMessage(labelKey) );
    return element;
  }

  protected Element renderLabelCarefully(Element element, String labelKey)
    throws RenderingException
  { //20030325AH - Syntactical sugar
    if(element == null) return null;
    replaceTextCarefully(element, getRenderingContext().getResourceLookup().getMessage(labelKey) );
    return element; //what was my rationale for returning the element? hmmm
  }

  protected Element renderLabel(String labelElementId, String labelKey, Object[] params, boolean checkExists)
    throws RenderingException
  {
    Element element = getElementById(labelElementId, checkExists);
    String msg = getRenderingContext().getResourceLookup().getMessage(labelKey,params);
    replaceText(element, msg );
    return element;
  }

  /**
   * Will replace the children of the element that has an id attribute of elementId with a single
   * text node contained the passed text.
   * @param elementId
   * @param text
   */
  protected void renderElementText(String elementId, String text)
  {
    Element element = getElementById(elementId);
    replaceText(element,text);
  }

  /**
   * Will set the specified attribute of the element identified by an "id" attribute equal to
   * elementId to the value returned from the current resource lookup object based on the
   * supplied key. If this cannot be done, will do nothing.
   * @param elementId
   * @param attribute
   * @param key
   */
  protected void renderElementAttributeFromKey(String elementId, String attribute, String key)
  {
    renderElementAttribute( elementId,
                            attribute,
                            getRenderingContext().getResourceLookup().getMessage(key));
  }

  /**
   * Will set the value attribute of the element identified by an "id" attribute equal to
   * elementId to the value returned from the current resource lookup object based on the
   * supplied key. If this cannot be done, will do nothing.
   * @param elementId
   * @param value
   */
  protected void renderElementValue(String elementId, String value)
  {
    renderElementAttribute(elementId,"value",value);
  }

  /**
   * Will remove the specified attribute from the element identified by an "id" attribute equal to
   * elementId to the value returned from the current resource lookup object based on the
   * supplied key. If this cannot be done, will do nothing.
   * @param elementId
   * @param attribute
   */
  protected void removeAttribute(String elementId, String attribute)
  {
    if( (elementId == null) || (attribute == null) )
    {
      return;
    }
    else
    {
      try
      {
        Element element = getElementById(elementId);
        if(element != null)
        {
          element.removeAttribute(attribute);
        }
      }
      catch(Exception e)
      {
        
      }
    }
  }

  /**
   * Will set the specified attribute of the element identified by an "id" attribute equal to
   * elementId to the value specified If this cannot be done, will do nothing.
   * @param elementId
   * @param attribute
   * @param key
   */
  protected void renderElementAttribute(String elementId, String attribute, String value)
  {
    if( (elementId == null) || (attribute == null) )
    {
      return;
    }
    else
    {
      if(value == null)
      {
        value = "";
      }
      try
      {
        Element elem = getElementById(elementId);
        if(elem == null) return;
        elem.setAttribute(attribute,value);
      }
      catch(Exception e)
      {
        //@todo: quietly log this failure, but dont interfere with user.
      }
    }
  }

  /**
   * Will import the new node into the document and replace oldNode with it.
   * If newNode is null, oldNode is removed.
   * If removeId is true, the id attribute will be removed from the imported node.
   * @param newNode
   * @param oldNode
   * @param removeId
   * @return newNode
   */
  protected static Node importAndSubstitute(Node newNode, Node oldNode, boolean removeId)
  {
    if(oldNode == null)     throw new NullPointerException("oldNode may not be null");
    if(oldNode == oldNode.getOwnerDocument()) throw new IllegalArgumentException("oldNode == document");

    if(newNode != null)
    {
      if(!newNode.getOwnerDocument().equals(oldNode.getOwnerDocument()))
      {
        newNode = oldNode.getOwnerDocument().importNode(newNode,true);
      }
    }
    if(removeId && nodeIsElement(newNode) )
    {
      ((Element)newNode).removeAttribute("id");
    }

    Node oldNodeParent = oldNode.getParentNode();
    if(oldNodeParent == null) throw new IllegalArgumentException("oldNode must have parent");
    if(newNode == null)
      oldNodeParent.removeChild(oldNode);
    else
      oldNodeParent.replaceChild(newNode, oldNode);

    return newNode;
  }

  /**
   * Returns true if the specified node is an element, false if not or is null.
   * @param node
   * @return isElement
   */
  protected static boolean nodeIsElement(Node node)
  {
    if(node == null)
      return false;
    else
      return (node.getNodeType() == Node.ELEMENT_NODE);
  }

  protected static boolean nodeIsType(Node node, short type)
  { //20021218AH
    if(node == null)
      return false;
    else
      return (node.getNodeType() == type);
  }

  /**
   * @deprecated use replaceNodeWithNewElement()
   */
  protected Element replaceWithNewElement(String newType, Node oldNode, boolean removeId)
    throws RenderingException
  {
    return replaceNodeWithNewElement(newType,oldNode,removeId);
  }

  /**
   * Replaces the oldNode with a newly created Element of the given type.
   * If removeId id false, the new element inherits the id of the old one.
   * @param newType
   * @param oldNode
   * @param removeId
   * @return newElement
   */
  protected Element replaceNodeWithNewElement(String newType, Node oldNode, boolean removeId)
    throws RenderingException
  {
    try
    {
      Element newElement = oldNode.getOwnerDocument().createElement(newType);
      if( (!removeId) && nodeIsElement(oldNode) )
      {
        String id = ((Element)oldNode).getAttribute("id");
        if(id != null)
        {
          newElement.setAttribute("id",id);
        }
      }
      return (Element)importAndSubstitute(newElement,oldNode,false);
    }
    catch(Exception e)
    {
      if(e instanceof RenderingException)
        throw (RenderingException)e;
      else
        throw new RenderingException("Error replacing node with new " + newType + " element",e);
    }
  }

  /**
   * Replaces the oldNode identified by passed id with a newly created Element of the given type.
   * If removeId id false, the new element inherits the id of the old one.
   * If checkExists is true and the node isnt found an exception is thrown.
   * @param newType
   * @param oldNode
   * @param removeId
   * @return newElement
   */
  protected Element replaceNodeWithNewElement(String newType,
                                      String nodeId,
                                      boolean checkExists,
                                      boolean removeId)
    throws RenderingException
  {
    try
    {
      Node oldNode = getElementById(nodeId,checkExists);
      if(oldNode == null) return null;
      return replaceWithNewElement(newType, oldNode, removeId);
    }
    catch(Exception e)
    {
      if(e instanceof RenderingException)
        throw (RenderingException)e;
      else
        throw new RenderingException("Error replacing node id=" + nodeId
                                     + " with new " + newType + " element",e);
    }
  }

  /**
   * Replaces the node for the value of the specifed field with a newly created Element
   * of the given type.
   * If removeId id false, the new element inherits the id of the old one.
   * If checkExists is true and the node isnt found an exception is thrown.
   * @param newType
   * @param oldNode
   * @param removeId
   * @return newElement
   */
  protected Node replaceValueNodeWithNewElement(String newType,
                                      Number fieldId,
                                      boolean checkExists,
                                      boolean removeId)
    throws RenderingException
  {
    try
    {
      Node oldNode = getFieldValueNode(fieldId,checkExists);
      if(oldNode == null) return null;
      return replaceWithNewElement(newType, oldNode, removeId);
    }
    catch(Exception e)
    {
      if(e instanceof RenderingException)
        throw (RenderingException)e;
      else
        throw new RenderingException("Error replacing node for field " + fieldId
                                     + " with new " + newType + " element",e);
    }
  }

  /**
   * Will import the new node into the document and replace oldNode with it.
   * If newNode is null, oldNode is removed.
   * The id attribute will not be removed from the imported node.
   * @param newNode
   * @param oldNode
   * @return newNode
   */
  protected static Node importAndSubstitute(Node newNode, Node oldNode)
  {
    return importAndSubstitute(newNode, oldNode, false);
  }

  /**
   * Will render the operationContext id in the hidden field on the form.
   * The id of the inout element is specified by the constant OperationContext.ID_ATTRIBUTE
   * while the name that field should have is specified by the constant.
   * The field must exist on the document being renderer to!
   * OperationContext.ID_PARAMETER
   * @throws OperationException
   */
  protected void renderOperationContext() throws RenderingException
  {
    OperationContext opCon = getRenderingContext().getOperationContext();
    if(opCon != null)
    {
      renderElementAttribute( OperationContext.ID_ATTRIBUTE,
                              "value",opCon.getOperationContextId());
      renderElementAttribute(OperationContext.ID_ATTRIBUTE,
                              "name",OperationContext.ID_PARAMETER);
    }
    else
    {
      throw new RenderingException("OperationContext reference for renderer has not been initialised");
    }
  }

  /**
   * @deprecated
   */
  protected BindingFieldPropertyRenderer renderField(Number fieldId)
    throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      BindingFieldPropertyRenderer bfpr = new BindingFieldPropertyRenderer(rContext);
      IGTEntity entity = getEntity();
      return renderField(bfpr,entity,fieldId);
    }
    catch(RenderingException re)
    {
      throw re;
    }
    catch(Exception e)
    {
      throw new RenderingException("Error preparing to render field " + fieldId + " using BFPR",e);
    }
  }

  protected BindingFieldPropertyRenderer renderField(BindingFieldPropertyRenderer bfpr,
                                                    IGTEntity entity,
                                                     Number fieldId)
    throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      ActionForm form = getActionForm();
      ActionErrors errors = rContext.getActionErrors();
      if(bfpr==null)
      {
        bfpr = new BindingFieldPropertyRenderer(rContext);
      }
      bfpr.reset();
      bfpr.setBindings(entity,fieldId,errors);
      bfpr.render(_target);
      return bfpr;
    }
    catch(Exception e)
    {
      throw new RenderingException("Error rendering field " + fieldId + " using BFPR",e);
    }
  }

  /**
   * @deprecated
   */
  protected BindingFieldPropertyRenderer renderFields(Number[] fieldIds, IFilter filter)
    throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      BindingFieldPropertyRenderer bfpr = new BindingFieldPropertyRenderer(rContext);
      IGTEntity entity = getEntity();
      return renderFields(bfpr,entity,fieldIds,filter);
    }
    catch(RenderingException re)
    {
      throw re;
    }
    catch(Exception e)
    {
      throw new RenderingException("Error preparing to render fields using BFPR",e);
    }
  }

  /**
   * @deprecated
   */
  protected BindingFieldPropertyRenderer renderFields(Number[] fieldIds)
    throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      BindingFieldPropertyRenderer bfpr = new BindingFieldPropertyRenderer(rContext);
      bfpr.setConstraintFilter(bfpr); // Default anyway - just being explicit for readability
      IGTEntity entity = getEntity();
      return renderFields(bfpr,entity,fieldIds);
    }
    catch(RenderingException re)
    {
      throw re;
    }
    catch(Exception e)
    {
      throw new RenderingException("Error preparing to render fields using BFPR",e);
    }
  }

  protected BindingFieldPropertyRenderer renderFields(BindingFieldPropertyRenderer bfpr,
                                IGTEntity entity,
                                Number[] fieldIds)
    throws RenderingException
  { //20030122AH
    return renderFields(bfpr,entity,fieldIds,null,null);
  }

  protected BindingFieldPropertyRenderer renderFields(BindingFieldPropertyRenderer bfpr,
                                IGTEntity entity,
                                Number[] fieldIds,
                                ActionForm form,
                                String idPrefix)
    throws RenderingException
  { //20030122AH
    try
    {
      RenderingContext rContext = getRenderingContext();
      ActionErrors errors = rContext.getActionErrors();
      if(bfpr==null)
      {
        bfpr = new BindingFieldPropertyRenderer(rContext);
      }
      if(form == null)
      {
        form = getActionForm();
      }
      ActionForm oldForm = bfpr.getActionForm();
      bfpr.setActionForm(form);
      String oldIdPrefix = bfpr.getIdPrefix();
      bfpr.setIdPrefix(idPrefix);
      for(int i=0; i < fieldIds.length; i++)
      {
        bfpr.reset();
        bfpr.setBindings(entity,fieldIds[i],errors);
        bfpr.render(_target);
      }
      bfpr.reset();
      bfpr.setActionForm(oldForm);
      bfpr.setIdPrefix(oldIdPrefix);
      return bfpr;
    }
    catch(Exception e)
    {
      throw new RenderingException("Error rendering fields using BFPR",e);
    }
  }

  /**
   * @deprecated Use renderFields(bfpr,entity,fieldIds,filter,form)
   */
  protected BindingFieldPropertyRenderer renderFields(BindingFieldPropertyRenderer bfpr,
                                IGTEntity entity,
                                Number[] fieldIds,
                                IFilter filter)
    throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      ActionForm form = getActionForm();
      ActionErrors errors = rContext.getActionErrors();
      if(bfpr==null)
      {
        bfpr = new BindingFieldPropertyRenderer(rContext);
      }
      bfpr.setConstraintFilter(filter);
      return renderFields(bfpr,entity,fieldIds);
    }
    catch(Exception e)
    {
      throw new RenderingException("Error rendering fields using BFPR",e);
    }
  }

  protected BindingFieldPropertyRenderer renderFields(BindingFieldPropertyRenderer bfpr,
                                IGTEntity entity,
                                Number[] fieldIds,
                                IFilter filter,
                                ActionForm form,
                                String idPrefix)
    throws RenderingException
  { //20030122AH
    try
    {
      RenderingContext rContext = getRenderingContext();
      if(form == null) throw new NullPointerException("form is null"); //20030414AH
      ActionErrors errors = rContext.getActionErrors();
      if(bfpr==null)
      {
        bfpr = new BindingFieldPropertyRenderer(rContext);
      }
      if( filter != null )
      { //20030826AH - Just doesnt seem right to pass null in this case (as it suppresses all option rendering!)
        bfpr.setConstraintFilter(filter);
      }
      return renderFields(bfpr,entity,fieldIds,form, idPrefix);
    }
    catch(Exception e)
    {
      throw new RenderingException("Error rendering fields using BFPR",e);
    }
  }

  /**
   * Will return the node that relates to the specified field value.
   * (Following the convention id="fieldName_value")
   * @param fieldId
   * @param checkExists when this flag is set causes exception to be thrown if node not found.
   * @return Node
   * @throws RenderingException
   */
  protected Node getFieldValueNode(Number fieldId, boolean checkExists)
    throws RenderingException
  {
    try
    {
      return getElementById(getEntity().getFieldName(fieldId) + "_value",checkExists);
    }
    catch(RenderingException re)
    {
      throw re;
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error obtaining node in DOM for field value:" + fieldId,t);
    }
  }

  /**
   * Will return the node that relates to the specified field label.
   * (Following the convention id="fieldName_label")
   * @param fieldId
   * @param checkExists when this flag is set causes exception to be thrown if node not found.
   * @return Node
   * @throws RenderingException
   */
  protected Node getFieldLabelNode(Number fieldId, boolean checkExists)
    throws RenderingException
  {
    try
    {
      return getElementById(getEntity().getFieldName(fieldId) + "_label",checkExists);
    }
    catch(RenderingException re)
    {
      throw re;
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error obtaining node in DOM for field label:" + fieldId,t);
    }
  }

  /**
   * Will return the node that relates to the specified field error.
   * (Following the convention id="fieldName_error")
   * @param fieldId
   * @param checkExists when this flag is set causes exception to be thrown if node not found.
   * @return Node
   * @throws RenderingException
   */
  protected Node getFieldErrorNode(Number fieldId, boolean checkExists)
    throws RenderingException
  {
    try
    {
      return getElementById(getEntity().getFieldName(fieldId) + "_error",checkExists);
    }
    catch(RenderingException re)
    {
      throw re;
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error obtaining node in DOM for field error:" + fieldId,t);
    }
  }

  /**
   * Returns the current operation entity from the operation context using the key
   * IOperationKeys.ENTITY
   * @return IGTEntity
   * @throws OperationException
   */
  protected final IGTEntity getEntity() throws OperationException
  {
    OperationContext opCon = getRenderingContext().getOperationContext();
    if(opCon == null)
    {
      throw new OperationException("OperationContext reference for renderer has not been initialised");
    }
    else
    {
      return (IGTEntity)opCon.getAttribute(IOperationContextKeys.ENTITY);
    }
  }

  /**
   * Returns the current ActionForm instance from the OperationContext
   * @return ActionForm
   * @throws RenderingException (used to throw OperationException - 20030107AH)
   */
  protected ActionForm getActionForm() throws RenderingException
  {
    try
    {
      OperationContext opCon = getRenderingContext().getOperationContext();
      if(opCon == null)
      {
        throw new OperationException("OperationContext reference for renderer has not been initialised");
      }
      else
      {
        return (ActionForm)opCon.getActionForm();
      }
    }
    catch(Throwable t)
    { //20030107AH
      throw new RenderingException("Unable to obtain ActionForm instance from OperationContext",t);
    }
  }

  /**
   * Makes use of the operationContext (which stores a submitURL) to render the url for the
   * specified form to submit to. The URL rewriter specified in the rendering context is applied
   * to this URL.
   * @param formId
   * @throws RenderingException
   */
  protected void renderFormAction(String formElementId) throws RenderingException
  {
    // Render the URL and the text for the links to use when the form is submitted or cancelled
    OperationContext opCon = getRenderingContext().getOperationContext();
    if(opCon != null)
    {
      String submitURL = (String)opCon.getAttribute(IOperationContextKeys.FORM_SUBMIT_URL);
      if(submitURL == null) throw new NullPointerException("submitURL is null"); //20030414AH
      renderElementAttribute(formElementId,
                            "action",
                            getRenderingContext().getUrlRewriter().rewriteURL(submitURL));
    }
    else
    {
      throw new RenderingException("OperationContext reference for renderer has not been initialised");
    }
  }

  /**
   * Render option tags withing the select tag specified by selectId.
   * The options are based on the collection of choices passed and the IOptionValueRetriever
   * passed which is used to determine the displayed text and the submitted value for each
   * choice object in the collection.
   * If choices is null returns silently.
   * All other parameters must be specified or exceptions are thrown.
   * Existing child nodes of the select node are not removed. This rendering is appended.
   * @param selectId
   * @param choices
   * @param optionValueRetriever
   * @param clearExistingChoices
   * @param emptyChoice
   * @throws RenderingException
   */
  protected void renderSelectOptions( Element selectElement,
                                      Collection choices,
                                      IOptionValueRetriever oRetriever,
                                      boolean clearExistingChoices,
                                      String emptyChoice)
    throws RenderingException
  { //20030518AH
    if(choices == null)
    {
      return;
    }
    if(oRetriever == null)
    {
      throw new java.lang.NullPointerException("No IOptionValueRetriver specified");
    }
    try
    {
      RenderingContext rContext = getRenderingContext();
      Document document = selectElement.getOwnerDocument();
      if(clearExistingChoices)
      {
        removeAllChildren(selectElement);
      }
      if(emptyChoice != null)
      {
        Element emptyOption = document.createElement("option");
        replaceText(emptyOption, rContext.getResourceLookup().getMessage(emptyChoice) );
        emptyOption.setAttribute("value","");
        selectElement.appendChild(emptyOption);
      }
      Iterator i = choices.iterator();
      while(i.hasNext())
      {
        Object choice = i.next();
        Element option = document.createElement("option");
        replaceText(option, oRetriever.getOptionText(choice));
        option.setAttribute("value", oRetriever.getOptionValue(choice));
        selectElement.appendChild(option);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering options for select element" ,t);
    }
  }

  /**
   * Render option tags withing the select tag specified by selectId.
   * The options are based on the collection of choices passed and the IOptionValueRetriever
   * passed which is used to determine the displayed text and the submitted value for each
   * choice object in the collection.
   * If choices is null returns silently.
   * All other parameters must be specified or exceptions are thrown.
   * @param selectId
   * @param choices
   * @param optionValueRetriever
   * @param clearExistingChoices
   * @param emptyChoice
   * @throws RenderingException
   */
  protected void renderSelectOptions( String selectId,
                                    Collection choices,
                                    IOptionValueRetriever oRetriever,
                                    boolean clearExistingChoices,
                                    String emptyChoice)
    throws RenderingException
  { //20030518AH - Factored out the good stuff to make a version that takes node as param
    if(selectId == null)
    {
      throw new java.lang.NullPointerException("No selectId specified");
    }
    if(selectId.equals(""))
    {
      throw new java.lang.IllegalArgumentException("selectId may not be an empty string");
    }
    try
    {
      Element selectElement = getElementById(selectId,true);
      assertElementType(selectElement, "select"); //20030717AH
      renderSelectOptions( selectElement, choices, oRetriever, clearExistingChoices, emptyChoice );
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering options for select: " + selectId,t);
    }
  }

  protected void renderSelectOptions( String selectId,
                                    Collection choices,
                                    IOptionValueRetriever oRetriever)
    throws RenderingException
  {
    renderSelectOptions( selectId, choices, oRetriever, false, null);
  }

  /**
   * Render option tags withing the select tag specified by selectId.
   * The options and selected values are taken from the passed constraint.
   * @param selectId
   * @param constraint an instance of an IEnumeratedConstraint
   * @param clearExistingChoices should be set to true to remove any existing option elements
   * @param emptyChoice the key of a label for the epty choice which submits "" if selected or null if not required.
   * @throws RenderingException
   */
  protected void renderSelectOptions( Element selectElement,
                                      IEnumeratedConstraint constraint,
                                      boolean clearExistingChoices,
                                      String emptyChoice)
    throws RenderingException
  {
    if(selectElement == null)
    {
      throw new java.lang.NullPointerException("No selectNode specified");
    }
    if(!"select".equals(selectElement.getNodeName()))
    {
      throw new java.lang.IllegalArgumentException("element is not a select element");
    }
    if(constraint == null)
    {
      throw new java.lang.IllegalArgumentException("constraint is null");
    }
    try
    {
      RenderingContext rContext = getRenderingContext();
      ISimpleResourceLookup rLookup = rContext.getResourceLookup();
      Document document = selectElement.getOwnerDocument();
      if(clearExistingChoices)
      {
        removeAllChildren(selectElement);
      }
      if(emptyChoice != null)
      {
        Element emptyOption = document.createElement("option");
        //replaceText(emptyOption, rLookup.getMessage(emptyChoice)); // 20030915 DDJ
        String i18nStr = constraint.getRequiresI18n() ? rLookup.getMessage(emptyChoice) : emptyChoice;
        replaceText(emptyOption, i18nStr);
        emptyOption.setAttribute("value","");
        selectElement.appendChild(emptyOption);
      }
      int size = constraint.getSize();
      for(int i=0; i < size; i++)
      {
        Element option = document.createElement("option");
        
        //replaceText(option, rLookup.getMessage( constraint.getLabel(i) )); // 20030915 DDJ
        String i18nStr = constraint.getRequiresI18n() ? rLookup.getMessage(constraint.getLabel(i)) : constraint.getLabel(i);
        replaceText(option, i18nStr);
        option.setAttribute("value", constraint.getValue(i) );
        selectElement.appendChild(option);
      }
    }
    catch(Exception e)
    {
      throw new RenderingException("Error rendering options for select",e);
    }
  }


  /**
   * Will remove the node with the specified id from its parent in the document.
   * If the node cannot be found will fail silently.
   * If the node has no parent will fail silently.
   * Other problems will throw RenderingException.
   * @param id of node to be removed from DOM
   * @throws RenderingException
   */
  protected void removeNode(String nodeId)
    throws RenderingException
  {
    removeNode(nodeId,false);
  }

  protected void removeNode(String nodeId, boolean checkExists)
    throws RenderingException
  {
    removeNode(getElementById(nodeId,checkExists),false);
  }

  protected void removeNode(Node node, boolean checkExists)
    throws RenderingException
  {
    if(node != null)
    {
      try
      {
        Node parent = node.getParentNode();
        if(parent != null)
          parent.removeChild(node);
        else
          throw new RenderingException("Unable to remove node as it has no parent node to be removed from");
      }
      catch(RenderingException re)
      {
        throw re;
      }
      catch(Exception e)
      {
        throw new RenderingException("Unable to remove node from its parent",e);
      }
    }
    else
    {
      if(checkExists)
      {
        throw new java.lang.NullPointerException("Node is null");
      }
      else
      {
        if(_log.isDebugEnabled())
        {
          _log.debug("Node is null");
        }
      }
    }
  }

  protected void renderCheckboxSelection(Element element, String value)
    throws RenderingException
  {
    if(element == null) throw new java.lang.NullPointerException("Null element");
    String checkboxValue = element.getAttribute("value");
    boolean selected = checkboxValue == null ? StaticUtils.primitiveBooleanValue(value) : checkboxValue.equals(value);
    renderCheckboxSelection(element, selected);
  }

  protected void renderCheckboxSelection(Element element, boolean selected)
    throws RenderingException
  {
    if(selected)
    {
      element.setAttribute("checked","checked");
    }
    else
    {
      element.removeAttribute("checked");
    }
  }

  protected boolean renderSelectedOptions(Element selectElement, String value)
    throws RenderingException
  {
    return renderSelectedOptions(selectElement, new String[]{value});
  }

  protected boolean renderSelectedOptions(String selectId, String value, boolean checkExists)
    throws RenderingException
  { //20030113AH - I grow tired of renderSelectOptions only taking string and this
    //only taking Element....
    Element selectElement = getElementById(selectId,checkExists);
    return renderSelectedOptions(selectElement,value);
  }

  protected boolean renderSelectedOptions(Element selectElement, String[] values)
    throws RenderingException
  {
    if(selectElement == null) throw new java.lang.NullPointerException("Null element");
    if(values == null) throw new java.lang.NullPointerException("Null value array");
    if(values.length == 0) return false;
    boolean found = false;
    try
    {
      NodeList options = selectElement.getChildNodes();
      int numOptions = options.getLength();
      for(int i=0; i < numOptions; i++)
      {
        Node child = options.item(i);
        if(nodeIsElement(child))
        {
          Element element = (Element)child;
          if(element.getNodeName().equals("option"))
          {
            String value = element.getAttribute("value");
            if( (value!=null) && StaticUtils.arrayContains(values, value ))
            {
              found = true;
              element.setAttribute("selected","selected");
            }
            else
            {
              element.removeAttribute("selected");
            }
          }
        }
      }
      return found; //20030113AH
    }
    catch(Exception e)
    {
      throw new RenderingException("Error rendering selected values to select element options",e);
    }
  }

  protected void renderListItems(Element element, String[] values)
    throws RenderingException
  {
    if(element == null) throw new java.lang.NullPointerException("Element is null");
    if(values == null) throw new java.lang.NullPointerException("Values array is null");
    try
    {
      for(int i=0; i < values.length; i++)
      {
        Element li = element.getOwnerDocument().createElement("li");
        replaceText(li,values[i]);
        element.appendChild(li);
      }
    }
    catch(Exception e)
    {
      throw new RenderingException("Error rendering list items",e);
    }
  }

  /**
   * Renders labels for ok,cancel,edit,heading as well as operationContext and form action.
   * Currently still requires the layout to have the OperationContext hidden field. Just renders
   * its properties.
   */
  protected void renderCommonFormElements(String entityName, boolean edit)
    throws RenderingException
  {
    try
    {
      boolean canEdit = true;
      try
      {
        IGTEntity entity = getEntity();
        canEdit = entity.canEdit();
      }
      catch(Throwable t)
      {
        // No entity. Allow the edit button
      }

      renderFormAction("form");
      renderOperationContext();
      includeJavaScript(IGlobals.JS_ENTITY_FORM_METHODS); //20030317AH, 20030408AH
      if(edit)
      {
        removeNode("edit_button",false); //20030317AH
        if(canEdit)
        {
          renderLabelCarefully("ok", entityName + ".edit.ok",false);
        }
        else
        {
          //removeNode("ok",false);
          removeNode("ok_button",false); //20030317AH
        }
        renderLabelCarefully("heading",entityName + ".edit.heading",false);
      }
      else
      {
        removeNode("ok_button",false); //20030317AH
        renderLabelCarefully("heading",entityName + ".view.heading",false);
        if(canEdit)
        {
          renderLabelCarefully("edit",entityName + ".view.edit",false);
        }
        else
        {
          //removeNode("edit",false);
          removeNode("edit_button",false); //20030317AH
        }
      }
      // 20040730 DDJ: Help button
      renderLabelCarefully("help",  "generic.help",  false);
      renderLabelCarefully("cancel","generic.cancel",false);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering common form elements",t);
    }
  }

  protected void removeIdAttribute(String id, boolean checkExists)
    throws RenderingException
  {
    Node node = getElementById(id,checkExists);
    removeIdAttribute((Element)node);
  }

  protected void removeIdAttribute(Element element)
  {
    if(element == null) return;
    element.removeAttribute("id");
  }

  protected Element getOptionElement(Element selector, String value, boolean checkExists)
    throws RenderingException
  {
    if(selector == null)
    {
      throw new java.lang.NullPointerException("selector element is null");
    }
    if(value == null)
    {
      throw new java.lang.NullPointerException("value is null");
    }
    if(!"select".equals(selector.getNodeName()))
    {
      throw new java.lang.IllegalArgumentException("expecting 'select' tag but was passed a '"
                                                    + selector.getNodeName() + "' tag");
    }
    NodeList children = selector.getChildNodes();
    for(int i=0; i < children.getLength(); i++)
    {
      Node child = children.item(i);
      if(nodeIsElement(child))
      {
        if("option".equals( child.getNodeName() ))
        {
          if(value.equals( ((Element)child).getAttribute("value") ))
          {
            return (Element)child;
          }
        }
      }
    }
    if(checkExists)
    {
      throw new RenderingException("Select element does not have child option element with value attribute of '" + value + "'");
    }
    else
    {
      return null;
    }
  }

  /**
   * Return the text of the node if it is a text node, or concatenate the text of its
   * immediate child nodes.
   * @param node
   * @return text
   * @throws RenderingException
   */
  protected String getNodeText(Node node) throws RenderingException
  {
    if(node == null) return null;
    try
    {
      if(node.getNodeType() == Node.TEXT_NODE)
      {
        return node.getNodeValue();
      }
      else
      {
        StringBuffer buffer = new StringBuffer();
        NodeList children = node.getChildNodes();
        for(int i=0; i < children.getLength(); i++)
        {
          Node child = children.item(i);
          if(child.getNodeType() == Node.TEXT_NODE)
          {
            buffer.append(child.getNodeValue());
          }
        }
        return buffer.toString();
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error getting text for node:" + node,t);
    }
  }

  protected void appendParameter( String elementId,
                                  String attribute,
                                  String parameter,
                                  String value,
                                  boolean checkExists)
    throws RenderingException
  {
    if(elementId == null) throw new NullPointerException("elementId is null"); //20030414AH
    Element element = getElementById(elementId,checkExists);
    if(element != null)
    {
      appendParameter(element,attribute,parameter,value);
    }
  }

  protected void appendParameter( Element element,
                                  String attribute,
                                  String parameter,
                                  String value)
    throws RenderingException
  {
    if(element == null) throw new NullPointerException("element is null"); //20030414AH
    if(attribute == null) throw new NullPointerException("attribute is null"); //20030414AH
    if(parameter == null) throw new NullPointerException("parameter is null"); //20030414AH
    try
    {
      String href = element.getAttribute(attribute);
      href = StaticWebUtils.addParameterToURL(href, parameter,value);
      element.setAttribute("href",href);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error appending parameter '" + parameter
        + "' with value '" + value + "' to contents of attribute '"
        + attribute + " of element:" + element);
    }
  }

  protected int getNodeType(Element element)
  {
    //@todo: refactor to use a static lookup Map
    if(element == null) return INodeTypes.NONE;
    String nodeName = element.getNodeName();
    if(nodeName.equals("input"))    return getInputNodeType(element);
    if(nodeName.equals("select"))   return INodeTypes.OPTION_PARENT;
    if(nodeName.equals("ul"))       return INodeTypes.LI_PARENT;
    if(nodeName.equals("ol"))       return INodeTypes.LI_PARENT;
    if(nodeName.equals("textarea")) return INodeTypes.TEXTAREA;

    if(nodeName.equals("a"))        return INodeTypes.LINK;
    if(nodeName.equals("img"))      return INodeTypes.IMAGE;

    if(nodeName.equals("table"))    return INodeTypes.TABLE;
    if(nodeName.equals("tr"))       return INodeTypes.TABLE_ROW;
    if(nodeName.equals("thead"))    return INodeTypes.TABLE_HEAD;
    if(nodeName.equals("tbody"))    return INodeTypes.TABLE_BODY;
    if(nodeName.equals("tfoot"))    return INodeTypes.TABLE_FOOT;

    if(nodeName.equals("div"))
    {
      String className = element.getAttribute("class");
      if("multiselector".equals(className))
      {
        return INodeTypes.INSERT_MULTISELECTOR;
      }
      if("multifiles".equals(className))
      {
        return INodeTypes.MULTIFILES;
      }
      if("selectionTable".equals(className))
      { //20030717AH
        return INodeTypes.INSERT_SELECTION_TABLE;
      }
      return INodeTypes.TEXT_PARENT;
    }

    if(nodeName.equals("span"))     return INodeTypes.TEXT_PARENT;
    if(nodeName.equals("td"))       return INodeTypes.TEXT_PARENT;
    if(nodeName.equals("th"))       return INodeTypes.TEXT_PARENT;
    if(nodeName.equals("center"))   return INodeTypes.TEXT_PARENT;
    if(nodeName.equals("b"))        return INodeTypes.TEXT_PARENT;
    if(nodeName.equals("em"))       return INodeTypes.TEXT_PARENT;
    if(nodeName.equals("legend"))   return INodeTypes.TEXT_PARENT;
    if(nodeName.equals("h1"))       return INodeTypes.TEXT_PARENT;
    if(nodeName.equals("h2"))       return INodeTypes.TEXT_PARENT;
    if(nodeName.equals("h3"))       return INodeTypes.TEXT_PARENT;
    if(nodeName.equals("h4"))       return INodeTypes.TEXT_PARENT;
    if(nodeName.equals("h5"))       return INodeTypes.TEXT_PARENT;
    if(nodeName.equals("li"))       return INodeTypes.TEXT_PARENT;
    if(nodeName.equals("h6"))       return INodeTypes.TEXT_PARENT;
    if(nodeName.equals("option"))   return INodeTypes.TEXT_PARENT;
    if(nodeName.equals("font"))     return INodeTypes.TEXT_PARENT;
    if(nodeName.equals("p"))        return INodeTypes.TEXT_PARENT;
    if(nodeName.equals("blockquote"))   return INodeTypes.TEXT_PARENT;
    if(nodeName.equals("big"))      return INodeTypes.TEXT_PARENT;
    if(nodeName.equals("caption"))  return INodeTypes.TEXT_PARENT;
    if(nodeName.equals("cite"))     return INodeTypes.TEXT_PARENT;
    if(nodeName.equals("kbd"))      return INodeTypes.TEXT_PARENT;
    if(nodeName.equals("nobr"))     return INodeTypes.TEXT_PARENT;
    if(nodeName.equals("pre"))      return INodeTypes.TEXT_PARENT;
    if(nodeName.equals("samp"))     return INodeTypes.TEXT_PARENT;
    if(nodeName.equals("small"))    return INodeTypes.TEXT_PARENT;
    if(nodeName.equals("strike"))   return INodeTypes.TEXT_PARENT;
    if(nodeName.equals("strong"))   return INodeTypes.TEXT_PARENT;
    if(nodeName.equals("sub"))      return INodeTypes.TEXT_PARENT;
    if(nodeName.equals("sup"))      return INodeTypes.TEXT_PARENT;

    if(nodeName.equals("iframe"))    return INodeTypes.INLINE_FRAME;

    return INodeTypes.OTHER;
  }

  protected int getInputNodeType(Element element)
  {
    String type = element.getAttribute("type");
    if(type.equals("checkbox"))       return INodeTypes.INPUT_CHECKBOX;
    if(type.equals("radio"))          return INodeTypes.INPUT_RADIO;
    if(type == null)                  return INodeTypes.INPUT_TEXT;
    if(type.equals("text"))           return INodeTypes.INPUT_TEXT;
    if(type.equals("password"))       return INodeTypes.INPUT_PASSWORD;
    if(type.equals("hidden"))         return INodeTypes.INPUT_HIDDEN;
    if(type.equals("button"))         return INodeTypes.INPUT_BUTTON;
    if(type.equals("file"))           return INodeTypes.INPUT_FILE;
    if(type.equals("image"))          return INodeTypes.INPUT_IMAGE;
    if(type.equals("reset"))          return INodeTypes.INPUT_RESET;
    if(type.equals("submit"))         return INodeTypes.INPUT_SUBMIT;
    return INodeTypes.OTHER;
  }

  /*protected boolean isMultipleInput(Element element)
  { //20021218AH
    assertElementType(element,"input");
    String name = element.getAttribute("name");
    if(name == null) return false;
    return( checkForMultipleInput(_target,name,0) > 1 );
  }

  private int checkForMultipleInput(Node node, String name, int count)
  { //20021218AH
    //Recursively search the _target dom tree for input elements with matching name attribute
    //Back out if we find more than one with the same name
    //(Thus this method should only ever return 0 , 1, or 2)
    if(count > 1) return count;
    if(node.getNodeType() == Node.ELEMENT_NODE)
    {
      if( "input".equals(node.getNodeName()) )
      {
        Element elem = (Element)node;
        if(name.equals(elem.getAttribute("name")))
        {
          count++;
          if(count > 1) return count;
        }
      }
    }
    for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
    {
      count = checkForMultipleInput(child, name, count);
      if(count > 1) return count;
    }
    return count;
  }*/

  /**
   * Unless the node is either null, or an element of the name specified will throw  an
   * IllegalArgumentException
   * @param node
   * @param name
   * @throws IllegalArgumentException
   */
  protected void assertElementType(Node node, String name)
  {
    if(name == null) throw new NullPointerException("name is null"); //20030414AH
    if(node == null)
    {
      return;
    }
    else if(nodeIsElement(node))
    {
      if(name.equals(node.getNodeName()))
      {
        return;
      }
      throw new java.lang.IllegalArgumentException("Expecting node name '" + name + "' but found '"
                                                  + node.getNodeName() + "'");
    }
    else
    {
      throw new java.lang.IllegalArgumentException("Node is not an element");
    }
  }

  protected Element getChildElement(String elementName,Node node)
    throws RenderingException
  {
    if(node == null) return null;
    if(elementName == null) throw new NullPointerException("elementName is null"); //20030414AH

    NodeList children = node.getChildNodes();
    for(int i = 0; i < children.getLength(); i++)
    {
      Node child = children.item(i);
      if(nodeIsElement(child))
      {
        if(elementName.equals(child.getNodeName()))
        {
          return (Element)child;
        }
      }
    }
    return null;
  }

  protected void appendEventMethod(String elementId, String attribute, String script, boolean checkExists)
    throws RenderingException
  {
    Element element = getElementById(elementId, checkExists);
    if(element != null)
    {
      appendEventMethod(element, attribute, script);
    }
  }

  protected void appendEventMethod(Element element, String attribute, String script)
    throws RenderingException
  {
    if(element == null) throw new NullPointerException("element is null"); //20030414AH
    if(attribute == null) throw new NullPointerException("attribute is null"); //20030414AH
    if(script == null) throw new NullPointerException("script is null"); //20030414AH
    String onXxx = element.getAttribute(attribute);
    if(onXxx == null)
    {
      element.setAttribute(attribute,script);
    }
    else
    {
      if(onXxx.indexOf(script) == -1)
      { // If this method/code is not already being called then append it
        element.setAttribute(attribute,onXxx + " " + script);
      }
    }
  }

  protected void appendOnloadEventMethod(String script)
    throws RenderingException
  {
    if(script == null) throw new NullPointerException("script is null"); //20030414AH
    try
    {
      Element body = findElement(_target,"body",null,null);
      if(body != null)
      {
        appendEventMethod(body,"onload",script);
      }
      else
      {
        throw new RenderingException("'body' tag not found in target document");
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error appending to onload attribute of body tag",t);
    }
  }

  /**
   * Will search the target for a script element with specified source, if it does not find
   * one will create a script element with that source and language="JavaScript" and append it
   * to the head node. If the head node cannot be found an exception is thrown.
   * Nb: The usual scripts have their urls defined in IGlobals as string constants. Use these
   * constants when possible.
   * @param src relative path to script source ie: scripts/selectUtils.js
   * @throws RenderingException
   */
  protected Element includeJavaScript(String srcUrl) throws RenderingException
  { //20020923AH
    try
    {
      Element scriptNode = findElement(_target,"script","src",srcUrl);
      if(scriptNode == null)
      { // Only include if not included already
        scriptNode = _target.createElement("script");
        Node comment = _target.createComment(""); // to stop xerces collapsing it in the output
        scriptNode.appendChild(comment);
        scriptNode.setAttribute("type","text/javascript"); //20021120AH - use type="text/javascript" now instead of language="Javascript"
        scriptNode.setAttribute("src",srcUrl);

        Element head = findElement(_target,"head",null,null);
        if(head == null)
        {
          throw new RenderingException("Could not find head node in target document");
        }
        head.appendChild(scriptNode);
      }
      return scriptNode; //20030221AH - Now returns the created or existing script node
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error including script src=" + srcUrl,t);
    }
  }

  /**
   * Will create a script and comment node to suround the supplied code, which
   * will be appended as a child of the specified parent node, or if this is null to the
   * head node. The code will be in the comment block and newlines and js comment added
   * automatically. There is no check to see if this code already exists in the document.
   * 20030221AH
   * @param parent
   * @param code
   * @return scriptNode
   * @throws RenderingException
   */
  protected Element addJavaScriptNode(Element parent, String code) throws RenderingException
  { //20030221AH
    try
    {
      Element scriptNode = _target.createElement("script");
      Node comment = _target.createComment("\n" + code + "\n//");
      scriptNode.appendChild(comment);
      scriptNode.setAttribute("type","text/javascript");
      if(parent == null)
      {
        parent = findElement(_target,"head",null,null);
        if(parent == null)
        {
          throw new RenderingException("Could not find head node in target document");
        }
      }
      parent.appendChild(scriptNode);
      return scriptNode;
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error adding JavaScript node",t);
    }
  }

  protected Element findElement(Node parent,
                                String nodeName,
                                String attributeName,
                                String attributeValue)
    throws RenderingException
  { //20030414AH - Refactored to include parent node in the search matching
    if(parent == null) parent = _target;
    //20030414AH - StaticUtils.assertNotNull(nodeName,"nodeName");
    if( (nodeName == null) && (attributeName == null) )
    { //20030414AH
      throw new NullPointerException("attributeName must be specified if node name is not");
    }
    try
    {
      Element element = null;
      if( nodeIsElement(parent) )
      {
        element = (Element)parent;
        if( (nodeName == null) || (nodeName.equals(element.getNodeName())))
        {
          if(attributeValue == null)
          {
            return element;
          }
          else
          {
            if(attributeValue.equals( element.getAttribute(attributeName) ) )
            {
              return element;
            }
          }
        }
      }
      NodeList children = parent.getChildNodes();
      for(int i=0; i < children.getLength(); i++)
      {
        Node child = children.item(i);
        element = findElement(child,nodeName,attributeName,attributeValue);
        if(element != null) return element;
      }
      return null;
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error finding element where nodeName='"
                                    + nodeName + "', attributeName='" + attributeName
                                    + "', attributeValue='" + attributeValue + "'",t);
    }
  }

  protected void renameNode(String oldId,String newId,boolean checkExists)
    throws RenderingException
  {
    Element oldNode = getElementById(oldId,checkExists);
    if(checkExists)
    {
      Element test = getElementById(newId,false);
      if(test != null)
      {
        throw new RenderingException("Node with id '" + newId + "' already exists");
      }
    }
    if(oldNode != null)
    { //20030130AH - If we didnt check for it, be sure we dont throw exception if it isnt there!
      oldNode.setAttribute("id",newId);
    }
  }

  protected Element getHeadNode() throws RenderingException
  {
    return findElement(_target,"head",null,null);
  }

  protected Element getBodyNode() throws RenderingException
  {
    return findElement(_target,"body",null,null);
  }

  protected void renderScript(String script) throws RenderingException
  {
    Element head = getHeadNode();
    if(head == null)
    {
      throw new java.lang.NullPointerException("No head node");
    }
    else
    {
      renderScript(head,script);
    }
  }

  protected void renderScript(Node parent, String script) throws RenderingException
  {
    try
    {
      if(parent == null) throw new NullPointerException("parent is null"); //20030414AH
      script = "\n" + script + "\n";
      Comment codeNode   = _target.createComment(script);
      Element scriptNode = _target.createElement("script");
      scriptNode.appendChild(codeNode);
      scriptNode.setAttribute("type","text/javascript");
      parent.appendChild(scriptNode);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering script",t);
    }
  }

  protected void renderTabs(RenderingContext rContext, String name, ITabDef[] tabs)
    throws RenderingException
  { //20021217AH
    try
    {
      TabRenderer tabRenderer = new TabRenderer(rContext);
      tabRenderer.setName(name);
      tabRenderer.setTabs(tabs);
      tabRenderer.render(_target);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering tab panels",t);
    }
  }

  /*protected void renderXParentOptions(Element parent,
                                      IEnumeratedConstraint constraint,
                                      String emptyChoice)
    throws RenderingException
  {
    if(parent == null)
    {
      throw new java.lang.NullPointerException("No parent element specified");
    }
    if(constraint == null)
    {
      throw new java.lang.IllegalArgumentException("constraint is null");
    }
    try
    {
      RenderingContext rContext = getRenderingContext();
      ISimpleResourceLookup rLookup = rContext.getResourceLookup();
      Document document = parent.getOwnerDocument();
      //Get the contents for stamp
      Node[] stamp = getChildArray(parent);
      //Find the input
      Element input = findElement(parent,"input",null,null);
      if(input == null)
      {
        throw new RenderingException("Couldnt find input in node for cloning");
      }
      //Determine the input type
      int inputType = getInputNodeType(input);
      //Find the text node
      Text text = findText(parent,null,1,Integer.MAX_VALUE);
      //Find the text nodes parent
      Node textParent = null;
      if(text != null) textParent = text.getParentNode();

      //Now remove the nodes from the parent (we still have refs in the stampt array etc...)
      removeAllChildren(selectElement);

      if(emptyChoice != null)
      {
        //Element emptyOption = document.createElement("option");
        //replaceText(emptyOption, rLookup.getMessage(emptyChoice) );
        //emptyOption.setAttribute("value","");
        //selectElement.appendChild(emptyOption);
      }
      int size = constraint.getSize();
      for(int i=0; i < size; i++)
      {
        //Element option = document.createElement("option");
        //replaceText(option, rLookup.getMessage( constraint.getLabel(i) ));
        //option.setAttribute("value", constraint.getValue(i) );
        //selectElement.appendChild(option);
      }
    }
    catch(Exception e)
    {
      throw new RenderingException("Error rendering options for xParent",e);
    }
    ldkfjgl;kfsjslkfdj
    not finished yet
  }*/

  /*private appendXOption(Node parent,
                          Node[] stamp,
                          Element input,
                          int inputType,
                          Node textParent,
                          String inputName,
                          String value,
                          String label)
    throws RenderingException
  { //20021218
    //NB: This modifies the stamp!
    try
    {
      input.setAttribute("name",inputName);
      input.setAttribute("id",inputName + "_" + value);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error appendingXOption",t);
    }
    dfkjhsdkjlfhsdkljfhsdf
    not finished yet
  }*/

  protected Node[] getChildArray(Node parent)
  { //20021218AH
    NodeList children = parent.getChildNodes();
    if(children == null) return new Node[0]; //20030321AH
    int length = children.getLength();
    Node[] childArray = new Node[length];
    for(int i = 0; i < length; i++)
    {
      childArray[i] = children.item(i);
    }
    return childArray;
  }

  protected Text findText( Node parent,String instr,int minLen,int maxLen)
    throws RenderingException
  { //20021218AH
    if(parent == null) parent = _target;
    try
    {
      NodeList children = parent.getChildNodes();
      for(int i=0; i < children.getLength(); i++)
      {
        Node child = children.item(i);
        if(nodeIsType(child,Node.TEXT_NODE))
        {
          Text text = (Text)child;
          String contents = text.getData();
          int length = 0;
          if(contents != null)
          {
            length = contents.length();
          }
          boolean instrOk = true;
          if( instr != null )
          {
            if(contents == null)
            {
              instrOk = false;
            }
            else
            {
              instrOk = ( contents.indexOf(instr) != -1 );
            }
          }
          if( instrOk && (length >= minLen) && (length <= maxLen) )
          {
            return text;
          }
        }
        if(child.hasChildNodes())
        {
          Text text = findText(parent,instr,minLen,maxLen);
          if(text != null)
          {
            return text;
          }
        }
      }
      return null;
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error finding text where instr='"
                                    + instr + "', minLen=" + minLen
                                    + ", maxLen=" + maxLen,t);
    }
  }

  protected void addListviewLinkButton( RenderingContext rContext,
                                        String name,
                                        String actionUrl,
                                        String labelKey,
                                        String iconSrc,
                                        String method,
                                        String confirmMsgKey)
    throws RenderingException
  { //20021226AH, 20030328AH - Refactored to use createButtonLink and take icon url
    //20030425AH - Modified to append IS_NOT_ELV_DIVERT flag!
    try
    {
      actionUrl = StaticWebUtils.addParameterToURL(actionUrl, EntityDispatchAction.IS_NOT_ELV_DIVERT, "true"); //20030425AH
      String url = rContext.getUrlRewriter().rewriteURL(actionUrl,false);
      String confirmMsg = confirmMsgKey == null ? "" : rContext.getResourceLookup().getMessage(confirmMsgKey);
      String action = "submitMultipleEntities('" + method + "','" + confirmMsg + "','" + url + "');";
      Element link = createButtonLink(name, labelKey, iconSrc, action, true);
      Node buttonCell = getElementById("button_cell",false);
      if(buttonCell == null) throw new NullPointerException("Couldnt find button_cell element");
      buttonCell.appendChild(link);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error adding link button to listview",t);
    }
  }

  protected void insertFirstChild( Element parent, Node node )
    throws RenderingException
  { //20030303AH
    try
    {
      Node firstNode = parent.getFirstChild();
      if(firstNode == null)
      {
        parent.appendChild(node);
      }
      else
      {
        parent.insertBefore(node,firstNode);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error inserting first child node",t);
    }
  }

  protected boolean isDetailView()
    throws RenderingException
  { //20030312AH - nb: doesnt check the query params - that should have been done in the
    //preceeding action'
    try
    {
      RenderingContext rContext = getRenderingContext();
      HttpServletRequest request = rContext.getRequest();
      if(request != null)
      {
        Boolean idv = (Boolean)request.getAttribute(TaskDispatchAction.IS_DETAIL_VIEW);
        if(Boolean.TRUE.equals(idv))
        {
          return true;
        }
        else
        {
          OperationContext opCon = rContext.getOperationContext();
          if(opCon != null)
          {
            idv = (Boolean)opCon.getAttribute(TaskDispatchAction.IS_DETAIL_VIEW);
            return Boolean.TRUE.equals(idv);
          }
          else
          {
            return false;
          }
        }
      }
      else
      {
        throw new NullPointerException("request in RenderingContext is null");
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Unable to determine if rendering in a detail view window",t);
    }
  }

  protected void disableLink(String linkId, boolean checkExists)
    throws RenderingException
  { //20030321AH
    try
    {
      Element anchor = getElementById(linkId,checkExists);
      disableLink(anchor); //20030325AH
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering link as disabled",t);
    }
  }

  protected void disableLink(Element anchor)
    throws RenderingException
  { //20030325AH
    try
    {
      if(anchor != null)
      {
        Node parent = anchor.getParentNode();
        if(parent == null) throw new java.lang.IllegalStateException("anchor node not in DOM tree");

        Node[] children = getChildArray(anchor);
        Element span = _target.createElement("span");
        for(int i=0; i < children.length; i++)
        {
          span.appendChild(children[i]);
        }
        span.setAttribute("id",anchor.getAttribute("id")); //20030325AH
        span.setAttribute("class",DISABLED_LINK_STYLECLASS);

        parent.insertBefore(span,anchor);
        parent.removeChild(anchor);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering link as disabled",t);
    }
  }

  protected Element createButtonLink(String name,
                                     String labelKey,
                                     String icon,
                                     String action,
                                     boolean isJavaScript)
    throws RenderingException
  {
    try
    {
      if(action == null) throw new NullPointerException("action is null");
      RenderingContext rContext = getRenderingContext();

      Document various = rContext.getDocumentManager().getDocument(IDocumentKeys.VARIOUS, false);
      if(various == null) throw new NullPointerException("document for 'various' is null");

      Element buttonNode = getElementByAttributeValue("button_content","id",various);
      if(buttonNode == null)
      {
        throw new NullPointerException("Couldnt find element 'button_content' in 'various' document");
      }

      buttonNode = (Element)_target.importNode(buttonNode, true); //get clone owned by target

      Element iconNode = getElementByAttributeValue("button_icon","id",buttonNode);
      //(iconNode may be null)
      Element anchorNode = getElementByAttributeValue("button_anchor","id",buttonNode);
      if(anchorNode == null)
      {
        throw new NullPointerException("Couldnt find element 'button_anchor' in button element");
      }

      if(name != null)
      {
        buttonNode.setAttribute("id",name + "_parent");
        anchorNode.setAttribute("id",name);
        if(iconNode != null) iconNode.setAttribute("id",name + "_icon");
      }
      else
      {
        buttonNode.removeAttribute("id");
        anchorNode.removeAttribute("id");
        if(iconNode != null) iconNode.removeAttribute("id");
      }

      if(iconNode != null)
      {
        if(icon != null)
        {
          iconNode.setAttribute("src",icon);
        }
        else
        {
          buttonNode.removeChild(iconNode);
        }
      }

      if(isJavaScript)
      {
        if(!action.endsWith(";")) action += ";";
        anchorNode.setAttribute("href","javascript: void " + action);
      }
      else
      {
        anchorNode.setAttribute("href", rContext.getUrlRewriter().rewriteURL(action) );
      }

      if(labelKey != null)
      {
        renderLabelCarefully(anchorNode, labelKey);
      }

      return buttonNode;
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error creating buttonLink:" + name,t);
    }
  }
  
  protected boolean renderFieldError(ActionErrors actionErrors, String fieldname) throws RenderingException
  { //20030423AH
    try
    {
      if(actionErrors == null) return false;
      ActionError error = MessageUtils.getFirstError(actionErrors, fieldname);
      if(error != null)
      {
        renderLabel(fieldname + "_error",error.getKey(), false);
        return true;
      }
      else
      {
        return false;
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error occured rendering ActionError for field " + fieldname,t);
    }
  }
  
  /*
   * Removes nodes related to fields in the target document.
   * This is useful for removing those
   * fields that are in the layout but that are not wanted in this state. Supports a similar
   * signature to the renderFields method and provides support for the idPrefix notation.
   * Failure to find nodes for a specified field is not considered an error and will
   * not result in an exception being thrown.
   * @param entity The IGTEntity that will provide the metainfo for the fields
   * @param fieldIds An array of numeric field references
   * @param idPrefix A prefix to be applied to node ids using dot notation or null
   * @throws RenderingException
   */
  protected void removeFields(  IGTEntity entity,
                                Number[] fieldIds,
                                String idPrefix)
    throws RenderingException
  { //20030507AH
    try
    {
      RenderingContext rContext = getRenderingContext();
      if(fieldIds == null) throw new NullPointerException("fieldIds is null");
      if(entity == null) throw new NullPointerException("entity is null");
      for(int i=0; i < fieldIds.length; i++)
      {
        try
        {
          String fieldname = entity.getFieldName(fieldIds[i]);
          String idBase = (idPrefix == null) ? fieldname : idPrefix + "." + fieldname;
          Node detailsNode = getElementById(idBase + "_details",false);
          if(detailsNode != null)
          {
            removeNode(detailsNode,false);
          }
          else
          {
            removeNode(idBase + "_value",false);
            removeNode(idBase + "_label",false);
            removeNode(idBase + "_error", false);
          }
        }
        catch(Throwable t)
        {
          throw new RenderingException("Error removing nodes in target for fieldIds["
                                        + i + "]="
                                        + fieldIds[i]
                                        + " belonging to entity "
                                        + entity);
        }
      } 
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error removing field nodes in target document",t);
    }
  }
  
  protected void sortSelectOptions(Element select, Comparator comparator) throws RenderingException
  { //20030625AH
    try
    {
      if(select == null) throw new NullPointerException("select is null");
      if(select.getNodeName().equals("select"))
      {
        NodeList children = select.getChildNodes();
        int numChildren = children.getLength();
        if( numChildren > 0)
        {
          ArrayList options = new ArrayList(numChildren);
          for(int i=0; i < numChildren; i++)
          {
            Node child = children.item(i);
            if( nodeIsElement(child) && child.getNodeName().equals("option") )
            {
              options.add(child);
            }
          }
          if(options.size() > 1)
          {
            if(comparator == null) comparator = getOptionComparator();
            Collections.sort(options, comparator);
            removeAllChildren(select);
            Iterator c = options.iterator();
            while(c.hasNext())
            {
              select.appendChild((Node)c.next());
            }
          }
        }
      }
      else
      {
        throw new IllegalArgumentException("Expecting select but was passed " + select.getNodeName());
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error sorting select option elements",t);
    }
  }
  
  protected OptionComparator getOptionComparator()
  { //20030625AH    
    if(_optionComparator == null)
    {
      RenderingContext rContext = getRenderingContext();
      Locale locale = StaticWebUtils.getLocale(rContext.getRequest());
      _optionComparator = new OptionComparator(locale);
    }
    return _optionComparator;
  }
  
  protected Element getLayoutElement(String documentKey, String id) throws RenderingException
  { //20030710AH
    if (documentKey == null)
      throw new NullPointerException("documentKey is null");
    if (id == null)
      throw new NullPointerException("id is null");
    try
    {      
      RenderingContext rContext = getRenderingContext();
      Document layoutDoc = rContext.getDocumentManager().getDocument(documentKey, false);
      if (layoutDoc == null)
        throw new NullPointerException("Layout document with key " + documentKey + " not found");
      Element element = getElementByAttributeValue(id, "id", layoutDoc);
      return element == null ? null : (Element)_target.importNode(element.cloneNode(true), true);
    }
    catch (Throwable t)
    {
      throw new RenderingException("Error obtaining node with id \""
                                    + id
                                    + "\" from document with key \""
                                    + documentKey
                                    + "\"", t);
    }
  }
  
  protected Element makeLink(Element parent, String href) throws RenderingException
  { //20030826AH
    Element anchor = null;
    if(parent != null)
    {    
      Node[] children = getChildArray(parent);
      if(children.length > 0)
      {
        Document doc = parent.getOwnerDocument();
        anchor = doc.createElement("a");
        if(href != null) anchor.setAttribute("href",href);
        for(int i=0; i < children.length; i++)
        {
          anchor.appendChild(children[i]);
        }
        parent.appendChild(anchor);
      }
    }
    return anchor;
  }
  
  /**
   * Get the sibling element of node that precedes it and has the specified name,
   * returning null if no such sibling is found. If node is null will also
   * return null.
   * @param node node that comes after the one we are looking for
   * @param name Name of the element to look for (if null returns previous element)
   * @return previous if not found will be null
   */
  protected Element getPrevious(Node node, String name)
  { //20050315AH
    if(node == null)
    {
      return null;
    }
    Node previous = node.getPreviousSibling();
    while(previous != null)
    {
      if(nodeIsElement(previous))
      {
        if(name == null)
        {
          return (Element)previous;
        }
        if(name.equals( ((Element)previous).getNodeName() ))
        {
          return (Element)previous;
        }
      }
    }
    return null;
  }
  
  /**
   * Get the sibling element of node that comes after it and has the specified name,
   * returning null if no such sibling is found. If node is null will also
   * return null.
   * @param node node that comes after the one we are looking for
   * @param name Name of the element to look for (if null returns next element)
   * @return next if not found will be null
   */
  protected Element getNext(Node  node, String name)
  { //20050315
    if(node == null)
    {
      return null;
    }
    Node next = node.getNextSibling();
    while(next != null)
    {
      if(nodeIsElement(next))
      {
        if(name == null)
        {
          return (Element)next;
        }
        if(name.equals( ((Element)next).getNodeName() ))
        {
          return (Element)next;
        }
      }
    }
    return null;
  }
  
}