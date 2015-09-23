/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SoapProcedureRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-07-30     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.bp;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTFieldMetaInfo;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTProcedureDefFileManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.ctrl.IGTSoapProcedureEntity;
import com.gridnode.gtas.client.ctrl.ITextConstraint;
import com.gridnode.gtas.client.ctrl.MethodDescriptor;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.BindingFieldPropertyRenderer;
import com.gridnode.gtas.client.web.renderers.IColumnObjectAdapter;
import com.gridnode.gtas.client.web.renderers.IComplexOptionSource;
import com.gridnode.gtas.client.web.renderers.IOptionValueRetriever;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;
import com.gridnode.gtas.client.web.renderers.SelectionTableRenderer;
import com.gridnode.gtas.client.web.strutsbase.MessageUtils;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;

public class SoapProcedureRenderer extends AbstractRenderer implements IComplexOptionSource
{
  private static final String METHOD_ICON_SRC = "images/controls/method.gif"; //20030717AH
  
  private class MethodSelectionTableRenderer extends SelectionTableRenderer
  { //@todo: this class is duplicate of inner class in JavaProcedureRenderer. Factor it it
    MethodSelectionTableRenderer(RenderingContext rContext)
    {
      super(rContext);
    }
    
    
    protected void appendBodyCell(RenderingContext rContext,
                                  Element row,
                                  int col,
                                  int rowCount,
                                  Object value,
                                  Object object)
    throws RenderingException
    {
      Element td = createBodyCell(rContext,col,rowCount,object);
      td.setAttribute("valign", "top");
      if(object == null) 
      {
        Node br = _target.createElement("br");
        td.appendChild(br);
      }
      else
      {
System.out.println("appending body cell. object class=" + StaticUtils.getObjectClassName(object));
        Collection methods = (Collection)object;
        Iterator i = methods.iterator();
        while(i.hasNext())
        {
          MethodDescriptor method = (MethodDescriptor)i.next();
          renderMethod(rContext, td, method);
          if(i.hasNext())
          {
            Node br = _target.createElement("br");
            td.appendChild(br);
          }
        }
      }
      row.appendChild(td);
      decorateBodyCell(rContext,td,col,rowCount,object);
    }
    
    private void renderMethod(RenderingContext rContext,
                                  Element parent,
                                  MethodDescriptor method)
      throws RenderingException
    {
      try
      {
        if (method == null) 
          throw new NullPointerException("method is null");
        Element img = _target.createElement("img");
        img.setAttribute("border","0");
        img.setAttribute("style","vertical-align: text-top; margin-right: 3px;");
        img.setAttribute("src",METHOD_ICON_SRC);
        parent.appendChild(img);
        String returnType = method.getReturnType();
        returnType = returnType == null ? "void" : returnType;
        parent.appendChild( mkSpan(returnType + " ","font-style: italic; font-weight: lighter;","method") );
        parent.appendChild( mkSpan(method.getName(),"font-weight: bolder;","method") );
        parent.appendChild( mkSpan("(","color: blue;","method") );
        MethodDescriptor.Parameter[] params = method.getParameters();
        for(int i=0; i < params.length; i++)
        {
          parent.appendChild( mkSpan(params[i].getType(),"font-style: italic; font-weight: lighter;","method") );
          parent.appendChild( mkSpan(" " + params[i].getName(),"font-weight: lighter;","method") );
          if( i+1 != params.length )
          {
            parent.appendChild( mkSpan(", ","color: blue;","method") );
          }
        }
        parent.appendChild( mkSpan(")","color: blue;","method") );        
      }
      catch(Throwable t)
      {
        throw new RenderingException("Error creating element for method " + method,t);
      }
    }
    
    private Element mkSpan(String text, String style, String cssClass)
    {
      Element span = _target.createElement("span");
      if(style != null)
      {
        span.setAttribute("style", style);
      }
      if(cssClass != null)
      {
        span.setAttribute("class", cssClass);
      }
      Text textNode = _target.createTextNode(text == null ? " " : text);
      span.appendChild(textNode);
      return span;
    }
  }
  
  
  private class MethodValueRetriever implements IOptionValueRetriever, IColumnObjectAdapter
  { //20030717AH
    public String getOptionText(Object choice) throws GTClientException
    { //Unused
      return StaticUtils.stringValue(choice);
    }

    public String getOptionValue(Object choice) throws GTClientException
    {
      return ((MethodDescriptor)StaticUtils.getFirst((Collection)choice)).getName();
    }
    
    public String getColumnLabel(int column) throws GTClientException
    {
      return null;
    }

    public Object getColumnValue(Object object, int column) throws GTClientException
    {
      switch( column )
      {
        case 0:
          return object;
        default:
          throw new IllegalArgumentException("Invalid column " + column);
      }
    }

    public int getSize()
    {
      return 1;
    }

  }
  
  private MethodValueRetriever _mvr = new MethodValueRetriever(); 
  
  private boolean _edit;
  private static final Number[] _fields = 
  {
    IGTSoapProcedureEntity.SOAP_METHOD_NAME,
    IGTSoapProcedureEntity.SOAP_USER_NAME,  // 20031205 DDJ
    IGTSoapProcedureEntity.SOAP_PASSWORD,   // 20031205 DDJ
  };

  public SoapProcedureRenderer(RenderingContext rContext,
                          boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext(); 
      IGTSoapProcedureEntity soapProc = (IGTSoapProcedureEntity)getEntity(); 
      SoapProcedureAForm form = (SoapProcedureAForm)getActionForm();
      renderCommonFormElements(soapProc.getType(),_edit);
      
      BindingFieldPropertyRenderer bfpr = new BindingFieldPropertyRenderer(rContext);
      bfpr.setOptionSource(this);
      MethodSelectionTableRenderer mstr = new MethodSelectionTableRenderer(rContext); 
      mstr.setHighlightAlternateRows(true);
      bfpr.setSelectionTableRenderer( mstr );
      bfpr = renderFields(bfpr, soapProc, _fields);
      
      // 20031205 DDJ: Render ConfirmPassword
      if(_edit)
      {
        IGTFieldMetaInfo passwordMetaInfo = soapProc.getFieldMetaInfo(IGTSoapProcedureEntity.SOAP_PASSWORD); 
        
        bfpr.reset();
        bfpr.setBindings("confirmPassword", false);
        bfpr.setLabelKey("soapProcedure.confirmPassword");
        bfpr.setEditable(_edit);
        bfpr.setVisible(passwordMetaInfo.isDisplayable(soapProc.isNewEntity()));
        bfpr.setMandatory(passwordMetaInfo.isMandatory(soapProc.isNewEntity()));
        bfpr.setMaxLength(((ITextConstraint)passwordMetaInfo.getConstraint()).getMaxLength());
        bfpr.setValue(form.getConfirmPassword());
        bfpr.setErrorKey(MessageUtils.getFirstErrorKey(rContext.getActionErrors(), "confirmPassword"));
        bfpr.render(_target);
      }
      else
      {
        // 20040305 DDJ: Hide password
        String userName = soapProc.getFieldString(IGTSoapProcedureEntity.SOAP_USER_NAME);
        if (userName != null && userName.trim().length() > 0)
          renderElementText("password_value", "*****");
        //removeNode("password_details")
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering soapProcedure screen",t);
    }
  }
  
  
  private Long getProcDefFileUid(RenderingContext rContext)
    throws RenderingException
  { //@todo - factor out as its common with JavaProcedureRenderer
    try
    {
      OperationContext opCon = rContext.getOperationContext();
      return (Long)opCon.getAttribute(JavaProcedureDispatchAction.PROC_DEF_FILE_UID_KEY);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Couldnt get procDefFileUid from opCon",t);
    }
  }

  public Collection getMethods(RenderingContext rContext) throws GTClientException
  { 
    //SoapProcedureAForm form = (SoapProcedureAForm)getActionForm();
    Long procDefFileUid = getProcDefFileUid(rContext);
    if(procDefFileUid == null)
    {
      return Collections.EMPTY_LIST;
    }
    else
    {
      IGTSession gtasSession = getEntity().getSession();
      IGTProcedureDefFileManager procDefFileMgr = (IGTProcedureDefFileManager)gtasSession.getManager(IGTManager.MANAGER_PROCEDURE_DEF_FILE);
      Collection groupedMethods = procDefFileMgr.listGroupedMethodsOfWSDL(procDefFileUid);
      return groupedMethods;
    }
  }

  public IColumnObjectAdapter getColumnObjectAdapter(RenderingContext rContext, BindingFieldPropertyRenderer bfpr)
    throws GTClientException
  { //common with JavaProcedureRenderer - factor out somehow
    if( IGTSoapProcedureEntity.SOAP_METHOD_NAME.equals(bfpr.getFieldId()) )
    {
      return _mvr;
    }
    return null;
  }

  public IOptionValueRetriever getOptionValueRetriever(RenderingContext rContext, BindingFieldPropertyRenderer bfpr)
    throws GTClientException
  { 
    if( IGTSoapProcedureEntity.SOAP_METHOD_NAME.equals(bfpr.getFieldId()) )
    {
      return _mvr;
    }
    return null;
  }

  public Collection getOptions(RenderingContext rContext, BindingFieldPropertyRenderer bfpr) throws GTClientException
  { 
    if( IGTSoapProcedureEntity.SOAP_METHOD_NAME.equals(bfpr.getFieldId()) )
    {
      return getMethods(rContext);
    }
    return null;
  }

}