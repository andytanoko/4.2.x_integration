/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteExceptionRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-07-09     Andrew Hill         Created
 * 2003-07-22     Andrew Hill         Only show stacktrace for application exceptions when rendering delete errors
 */
package com.gridnode.gtas.client.web.renderers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.gridnode.gtas.client.ctrl.DeleteException;
import com.gridnode.gtas.client.ctrl.IGTFailedDeletionEntityReference;
import com.gridnode.gtas.client.ctrl.IGTMheReference;
import com.gridnode.gtas.client.ctrl.ResponseException;
import com.gridnode.gtas.client.web.StaticWebUtils;

public class DeleteExceptionRenderer  extends AbstractRenderer
{
  private DeleteException _deleteEx;
  private Element _insertionPoint;
  private MheReferenceRenderer _mheRenderer;

  public DeleteExceptionRenderer(RenderingContext rContext, DeleteException deleteEx, Element insertionPoint)
  {
    super(rContext);
    if (deleteEx == null)
      throw new NullPointerException("deleteEx is null");
    if (insertionPoint == null)
      throw new NullPointerException("insertionPoint is null");
    _deleteEx = deleteEx;
    _insertionPoint = insertionPoint;
  }
  
  private MheReferenceRenderer getMheReferenceRenderer(RenderingContext rContext)
  {
    if(_mheRenderer == null)
    {
      _mheRenderer = new MheReferenceRenderer(rContext, false);
    }
    return _mheRenderer;
  }

  public void render() throws RenderingException
  {
    try
    {
      if (_target == null)
        throw new NullPointerException("_target is null");
      RenderingContext rContext = getRenderingContext();
               
      IGTMheReference failedDeletions = _deleteEx.getFailedDeletions();
      Iterator failedDeletionsIterator = failedDeletions.getAll().iterator();
      int i = 0;
      while(failedDeletionsIterator.hasNext())
      {
        String fieldName = "failedDeletion_" + i;
        Element div = _target.createElement("div");
        _insertionPoint.appendChild(div);

        IGTFailedDeletionEntityReference failedEntityRef = (IGTFailedDeletionEntityReference)failedDeletionsIterator.next();
        if (failedEntityRef == null)
          throw new NullPointerException("failedEntityRef is null");
        ResponseException exception = failedEntityRef.getFailureException();
        if (exception == null)
          throw new NullPointerException("exception is null");
        IGTMheReference dependantEntities = failedEntityRef.getDependantEntities();
        if( (dependantEntities != null) && (dependantEntities.size() > 0) )
        {
          
          MheReferenceRenderer mheRefRenderer = getMheReferenceRenderer(rContext);
          mheRefRenderer.reset();
          mheRefRenderer.setInsertElement(div);
          mheRefRenderer.setFieldName(fieldName);
          mheRefRenderer.setNoScrollBar(true);
          mheRefRenderer.setSelectableEntities(dependantEntities);
          mheRefRenderer.setLabel("gtas.error." + exception.getErrorCode());
          mheRefRenderer.setLabelParams(exception.getErrorParams());
          mheRefRenderer.setLabelClass("errortext");
          mheRefRenderer.setAppend(true);
          mheRefRenderer.render(_target);
          
          String contentId = fieldName + "_mheReference_content";
          Element insertedContent = getElementById(contentId,false);
          if(insertedContent != null)
          { //@todo refactor so doesnt depend on how mher does its rendering
            Element span = _target.createElement("span");
            span.setAttribute("class", "fieldlabel");
            Text text = _target.createTextNode(failedEntityRef.toString());
            span.appendChild(text);
            span.appendChild(_target.createElement("br")); //20030722AH
            Element sibling = getElementById(fieldName + "_label",false);
            if(sibling != null)
            {
              sibling.getParentNode().insertBefore(span, sibling);
            }
            else
            {
              insertFirstChild(insertedContent, span);
            }
          }
        }
        else
        { //@todo: use an exception renderer to render the contained exceptions
          Element errorSpan = _target.createElement("span");
          Element labelSpan = _target.createElement("span");
          labelSpan.setAttribute("class","fieldlabel");
          div.appendChild( _target.createElement("br") ); //20030722AH
          div.appendChild(labelSpan);
          Element errSpan = _target.createElement("span");
          errSpan.setAttribute("class","errortext");
          errorSpan.appendChild(errSpan);
          div.appendChild(errorSpan);
          String errorMessage = rContext.getResourceLookup().getMessage("gtas.error."
                                    + exception.getErrorCode(),
                                    exception.getErrorParams());
          replaceText(errSpan,errorMessage);
          replaceText(labelSpan,failedEntityRef.toString()); //20030722AH
          labelSpan.appendChild(_target.createElement("br")); //20030722AH            
          
          if(!exception.isAppError())
          { //20030722AH - Only render stacktrace for non app exceptions (ie: unexpected stuff)
            SectionRenderer sr = new SectionRenderer(rContext);
            sr.reset();
            sr.setName(fieldName);
            sr.setInsertionPoint(div);
            sr.setAppend(true);
            sr.setOpen(false);  
            sr.setLabelKey("exception.details");    
            sr.setContentElementName("pre");      
            sr.render(_target);
            
            Element content = sr.getContentNode();
            StringWriter sWriter = new StringWriter(ExceptionRenderer.INIT_SW_SIZE);
            PrintWriter pWriter = new PrintWriter(sWriter,false);
            StaticWebUtils.printStackTrace(exception, pWriter);
            pWriter.flush();
            replaceText(content,sWriter.toString());
          }
          else
          {
            div.appendChild(_target.createElement("br")); //20030722AH
          }
        }
        i++;
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering DeleteException",t);
    }
  }
  
  
}