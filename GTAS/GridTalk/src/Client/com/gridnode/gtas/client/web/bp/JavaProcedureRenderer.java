/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JavaProcedureRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-24     Daniel D'Cotta      Created
 * 2003-07-16     Andrew Hill         ClassName and MethodName to be selected from lists
 */
package com.gridnode.gtas.client.web.bp;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTJavaProcedureEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTProcedureDefFileManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
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
import com.gridnode.gtas.client.web.renderers.StringValueRetriever;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;

public class JavaProcedureRenderer extends AbstractRenderer implements IComplexOptionSource
{
  private static final String METHOD_ICON_SRC = "images/controls/method.gif"; //20030717AH
  
  private class MethodSelectionTableRenderer extends SelectionTableRenderer
  { //20030717AH
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
  
  private MethodValueRetriever _mvr = new MethodValueRetriever(); //20030717AH
  
  private boolean _edit;
  private static final Number[] _fields = //20030716AH - Renamed _fields to meet convention
  {
    IGTJavaProcedureEntity.JAVA_CLASS_NAME,
    /*20030716AH - co (render manually): IGTJavaProcedureEntity.JAVA_METHOD_NAME,*/
    IGTJavaProcedureEntity.JAVA_IS_LOCAL,
    
  };
  
  private static final Number[] _nonLocalJvmFields =
  { //20030716AH
    IGTJavaProcedureEntity.JAVA_JVM_OPTIONS,
    IGTJavaProcedureEntity.JAVA_ARGUMENTS,
  };
  
  private static final Number[] _localJvmFields =
  { //20030716AH
    IGTJavaProcedureEntity.JAVA_METHOD_NAME,
  };

  public JavaProcedureRenderer(RenderingContext rContext,
                          boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext(); //20030716AH
      IGTJavaProcedureEntity javaProc = (IGTJavaProcedureEntity)getEntity(); //20030716AH
      JavaProcedureAForm form = (JavaProcedureAForm)getActionForm();
      renderCommonFormElements(IGTEntity.ENTITY_JAVA_PROCEDURE,_edit);
      
      //20030717AH - Render the options for the classNames
      if(_edit)
      {
        Element classNameSelect = getElementById("className_value",false);
        if(classNameSelect != null)
        {
          StringValueRetriever svr = new StringValueRetriever();
          renderSelectOptions(classNameSelect,getClassNames(rContext),svr,true,"generic.empty");
          sortSelectOptions(classNameSelect, null);
        }
      }
      //..
      
      //20030716AH - co: renderFields(fields);
      BindingFieldPropertyRenderer bfpr = new BindingFieldPropertyRenderer(rContext);
      bfpr.setOptionSource(this);
      bfpr = renderFields(bfpr, javaProc, _fields); //20030716AH
      
      Boolean isLocal = StaticUtils.booleanValue(form.getIsLocal()); //20030716AH
      if(Boolean.TRUE.equals(isLocal)) //20030716AH
      {
        /*if(_edit)
        {
          Element methodNameSelect = getElementById("methodName_value",false);
          if(methodNameSelect != null)
          {
            MethodValueRetriever mvr = new MethodValueRetriever();
            renderSelectOptions(methodNameSelect,getMethods(rContext),mvr,true,"generic.empty");
            sortSelectOptions(methodNameSelect, null);
          }
        }*/
        removeFields(javaProc, _nonLocalJvmFields, null); //20030716AH
        MethodSelectionTableRenderer mstr = new MethodSelectionTableRenderer(rContext); //20030722AH
        mstr.setHighlightAlternateRows(true); //20030722AH
        bfpr.setSelectionTableRenderer( mstr ); //20030722AH
        bfpr = renderFields(bfpr, javaProc, _localJvmFields); //20030716AH
        //renderMethodNameField(rContext, bfpr, form, javaProc); //20030716AH
        //20030716AH - co: removeNode("jvmOptions_details");
        //20030716AH - co: removeNode("arguments_details");
      }
      else
      {
        removeFields(javaProc, _localJvmFields, null); //20030716AH
        bfpr = renderFields(bfpr, javaProc, _nonLocalJvmFields); //20030716AH
        //20030722AH - Remove unnecessary refreshing since not showing method names here
        Element classSelect = getElementById("className_value",false);
        if(classSelect != null)
        {
          classSelect.removeAttribute("onchange");
        }
        //..
        //20030716AH - co: removeNode("methodName_details");
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering javaProcedure screen",t);
    }
  }
  
  
  private Long getProcDefFileUid(RenderingContext rContext)
    throws RenderingException
  { //20030716AH
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
  
  public Collection getClassNames(RenderingContext rContext) throws GTClientException
  { //20030717AH
    Long procDefFileUid = getProcDefFileUid(rContext);
    if(procDefFileUid == null)
    {
      return Collections.EMPTY_LIST;
    }
    else
    {
      IGTSession gtasSession = getEntity().getSession();
      IGTProcedureDefFileManager procDefFileMgr = (IGTProcedureDefFileManager)gtasSession.getManager(IGTManager.MANAGER_PROCEDURE_DEF_FILE);
      Collection classNames = procDefFileMgr.listClassesInJar(procDefFileUid);
      return classNames;
    }
  }

  public Collection getMethods(RenderingContext rContext) throws GTClientException
  { //20030717AH
    JavaProcedureAForm form = (JavaProcedureAForm)getActionForm();
    String className = form.getClassName();
    if( StaticUtils.stringEmpty(className) )
    {
      return Collections.EMPTY_LIST;
    }
    else
    {
      IGTSession gtasSession = getEntity().getSession();
      IGTProcedureDefFileManager procDefFileMgr = (IGTProcedureDefFileManager)gtasSession.getManager(IGTManager.MANAGER_PROCEDURE_DEF_FILE);
      Collection groupedMethods = procDefFileMgr.listGroupedMethodsOfClass(className);
      return groupedMethods;
    }
  }

  public IColumnObjectAdapter getColumnObjectAdapter(RenderingContext rContext, BindingFieldPropertyRenderer bfpr)
    throws GTClientException
  { //20030717AH
    if( IGTJavaProcedureEntity.JAVA_METHOD_NAME.equals(bfpr.getFieldId()) )
    {
      return _mvr;
    }
    return null;
  }

  public IOptionValueRetriever getOptionValueRetriever(RenderingContext rContext, BindingFieldPropertyRenderer bfpr)
    throws GTClientException
  { //20030717AH
    if( IGTJavaProcedureEntity.JAVA_METHOD_NAME.equals(bfpr.getFieldId()) )
    {
      return _mvr;
    }
    return null;
  }

  public Collection getOptions(RenderingContext rContext, BindingFieldPropertyRenderer bfpr) throws GTClientException
  { //20030717AH
    if( IGTJavaProcedureEntity.JAVA_METHOD_NAME.equals(bfpr.getFieldId()) )
    {
      return getMethods(rContext);
    }
    return null;
  }

}