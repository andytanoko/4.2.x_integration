/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridTalkMappingRuleRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-17     Andrew Hill         Created
 * 2002-10-16     Andrew Hill         Hide transformWithHeader when isHeader
 * 2002-12-03     Andrew Hill         Rearrange hiding rules for some fields
 * 2003-08-26     Andrew Hill         Ugly hack to make mappingFile field work with fbds
 */
package com.gridnode.gtas.client.web.document;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Element;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTGridTalkMappingRuleEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTMappingFileEntity;
import com.gridnode.gtas.client.ctrl.IGTMappingFileManager;
import com.gridnode.gtas.client.ctrl.IGTMappingRuleEntity;
import com.gridnode.gtas.client.ctrl.IGTProcedureDefFileManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.utils.IFilter;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.bp.JavaProcedureDispatchAction;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.BindingFieldPropertyRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;
import com.gridnode.gtas.client.web.renderers.StringValueRetriever;
import com.gridnode.gtas.client.web.strutsbase.FieldDivPath;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.events.mapper.GetJavaBinaryClassesEvent;
import com.gridnode.gtas.events.userprocedure.GetClassesFromJarEvent;
import com.gridnode.gtas.model.mapper.IMappingFile;
import com.gridnode.pdip.app.mapper.model.MappingFile;
import com.gridnode.pdip.framework.rpf.event.IEvent;

public class GridTalkMappingRuleRenderer extends AbstractRenderer
implements IFilter
{

  protected static final Number[] fields = new Number[]{IGTGridTalkMappingRuleEntity.DESCRIPTION,
                                                        IGTGridTalkMappingRuleEntity.IS_HEADER_TRANSFORMATION,
                                                        IGTGridTalkMappingRuleEntity.IS_TRANSFORM_WITH_HEADER,
                                                        IGTGridTalkMappingRuleEntity.IS_TRANSFORM_WITH_SOURCE,
                                                        IGTGridTalkMappingRuleEntity.NAME,
                                                        IGTGridTalkMappingRuleEntity.SOURCE_DOC_FILE_TYPE,
                                                        IGTGridTalkMappingRuleEntity.SOURCE_DOC_TYPE,
                                                        IGTGridTalkMappingRuleEntity.TARGET_DOC_FILE_TYPE,
                                                        IGTGridTalkMappingRuleEntity.TARGET_DOC_TYPE,};

  protected static final Number[] mrFields = new Number[]{IGTMappingRuleEntity.IS_KEEP_ORIGINAL,
                                                          IGTMappingRuleEntity.IS_TRANSFORM_REF_DOC,
                                                          IGTMappingRuleEntity.MAPPING_FILE,
                                                          IGTMappingRuleEntity.PARAM_NAME,
                                                          IGTMappingRuleEntity.REF_DOC_UID,
                                                          IGTMappingRuleEntity.TYPE,
                                                          IGTMappingRuleEntity.XPATH,
                                                          IGTMappingRuleEntity.MAPPING_CLASS,};
  private boolean _edit;
  //private Collection _documentTypes;
  //private Collection _mappingFiles;

  private Short _type = null;
  
  private static final String MAPPING_FILE_HACK_JS =
  "function mappingFileDivert(mappingName)" + "\n" +
  "{" + "\n" +
    "if( 0 == mappingName.indexOf('field:') )" + "\n" +
    "{" + "\n" +
      "var splitDivPath = mappingName.split(':');" + "\n" +
      "var field = document.getElementById('mappingFile_value');" + "\n" +
      "if(field)" + "\n" +
      "{" + "\n" +
        "if( field.selectedIndex != -1 )" + "\n" +
        "{" + "\n" +
          "var value = field.options[field.selectedIndex].value;" + "\n" +
          "if(value)" + "\n" +
          "{" + "\n" +
            "splitDivPath[2] = 'update';" + "\n" +
            "splitDivPath[3] = value;" + "\n" +
          "}" + "\n" +
          "else" + "\n" +
          "{" + "\n" +
            "splitDivPath[2] = 'create';" + "\n" +
            "splitDivPath[3] = '';" + "\n" +
          "}" + "\n" +
        "}" + "\n" +
        "else" + "\n" +
        "{" + "\n" +
          "splitDivPath[2] = 'create';" + "\n" +
        "}" + "\n" +
        "mappingName = splitDivPath.join(':');" + "\n" +
      "}" + "\n" +
    "}" + "\n" +
    "divertToMapping(mappingName);" + "\n" +
  "}"; //hehe

  public GridTalkMappingRuleRenderer( RenderingContext rContext,
                                      boolean edit/*,
                                      Collection documentTypes,
                                      Collection mappingFiles*/ )
  {
    super(rContext);
    _edit = edit;
    //_documentTypes = documentTypes;
    //_mappingFiles = mappingFiles;
  }

  protected void render() throws RenderingException
  {
    try
    {
      final RenderingContext rContext = getRenderingContext();
      final IGTGridTalkMappingRuleEntity gridTalkMappingRule = (IGTGridTalkMappingRuleEntity)getEntity();
      final IGTMappingRuleEntity mappingRule = (IGTMappingRuleEntity)gridTalkMappingRule.getFieldValue(gridTalkMappingRule.MAPPING_RULE);
      final GridTalkMappingRuleAForm form = (GridTalkMappingRuleAForm)rContext.getOperationContext().getActionForm();
      boolean isHeaderTransform = StaticUtils.primitiveBooleanValue(form.getHeaderTransformation());
      boolean isTransformRefDoc = StaticUtils.primitiveBooleanValue(form.getTransformRefDoc());


      renderCommonFormElements(gridTalkMappingRule.getType(), _edit);

      if(_edit)
      { // Render diversion labels
        /*20030826AH - co: renderLabel("sourceDocType_create","gridTalkMappingRule.sourceDocType.create",false);
        renderLabel("targetDocType_create","gridTalkMappingRule.targetDocType.create",false);
        renderLabel("sourceDocFileType_create","gridTalkMappingRule.sourceDocFileType.create",false);
        renderLabel("targetDocFileType_create","gridTalkMappingRule.targetDocFileType.create",false);
        renderLabel("mappingFile_create","mappingRule.mappingFile.create",false);
        renderLabel("refDocUid_create","mappingRule.refDocUid.create",false);*/
      }

      if(isHeaderTransform)
      { // The type for headerTransforms must be fixed as transform
        // this should really be done in the action! naughty
        form.setType("" + mappingRule.TYPE_MAPPING_TRANSFORM);
        removeNode("transformWithHeader_details",false); //20021016AH

        // These fields now not applicable for header rules, but are for content!
        removeNode("sourceDocType_details",false);
        removeNode("targetDocType_details",false);
        removeNode("sourceDocFileType_details",false);
        removeNode("targetDocFileType_details",false);
      }
      else
      { // Hide fields not applicable to content transforms
        /*removeNode("sourceDocType_details",false);
        removeNode("targetDocType_details",false);
        removeNode("sourceDocFileType_details",false);
        removeNode("targetDocFileType_details",false);
        removeNode("transformWithSource_details",false);20021203AH*/
        removeNode("transformWithSource_details",false);
      }

      Short type = getType();
      
      //added by ming qian
      if (_edit)
      {
        if (mappingRule.TYPE_MAPPING_SPLIT.equals(type)) 
        {
          removeNode("mappingClass_details", false);
        }
        else
        {
          if (form.getMappingFile() == null || form.getMappingFile().equals(""))
          {
           removeNode("mappingClass_details", false);
          }
          else
          {
            Long longUID = new Long(form.getMappingFile());
            
            //if mapping file is a java binary
            if (mappingFileIsJavaBinary(longUID))
            {
              //show mapping class field 
              Element classNameSelect = getElementById("mappingClass_value",false);
             
              if(classNameSelect != null)
              {
                StringValueRetriever svr = new StringValueRetriever();
                
                renderSelectOptions(classNameSelect,getClassNames(rContext, longUID),svr,true,"generic.empty");
                sortSelectOptions(classNameSelect, null);
              }
            }
            else
            {
             //do nothing
              removeNode("mappingClass_details", false);
            }
          }
        }
      }
      //end of added by ming qian

      if(!mappingRule.TYPE_MAPPING_TRANSFORM.equals(type) )
      { // Hide fields that are only applicable to a transformation
        removeNode("transformWithHeader_details",false);
        removeNode("transformWithSource_details",false);
        removeNode("transformRefDoc_details",false);
        removeNode("refDocUid_details",false);
      }
      else
      {
        if(!isTransformRefDoc)
        {
          removeNode("refDocUid_details",false);
        }
      }

      if(!mappingRule.TYPE_MAPPING_SPLIT.equals(type))
      { // Hide fields that are only applicable to a split
        removeNode("xpath_details",false);
        removeNode("paramName_details",false);
        removeNode("keepOriginal_details",false);
      }
      
      /*20030826AH - co:BindingFieldPropertyRenderer bfpr = renderFields(fields,this);
      bfpr = renderFields(bfpr, mappingRule, mrFields, this);*/
      BindingFieldPropertyRenderer bfpr = renderFields(null,gridTalkMappingRule,fields,this,form,null); //20030826AH
      bfpr = renderFields(bfpr, mappingRule, mrFields, this, form, null); //20030826AH
      doNastyMappingFileDiversionHackForWhichYouOughtToBeAshamed(form); //20030826AH

      if(_edit && isHeaderTransform)
      { // The type for headerTransforms must be fixed as transform
        Element typeElement = getElementById("type_value",false);
        if(typeElement != null)
        { // nb: we are assuming it is a select element
          typeElement.setAttribute("disabled","disabled");
        }
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering GridTalkmappingRule screen",t);
    }
  }
  
  public Collection getClassNames(RenderingContext rContext, Long longUID) throws GTClientException
  { 
      IGTSession gtasSession = getEntity().getSession();
      IGTMappingFileManager mappingFileMgr = (IGTMappingFileManager)gtasSession.getManager(IGTManager.MANAGER_MAPPING_FILE);
      Collection classNames = mappingFileMgr.listClassesInJar(longUID);
      return classNames;
  }

  public boolean allows(Object object, Object context) throws GTClientException
  { // Filter a selectable option for a field. Object will contain option object instance
    // (ie: IGTEntity etc...) while context refers to the bfpr
  
    if( (context != null) && (context instanceof BindingFieldPropertyRenderer) )
    {
      Number fieldId = ((BindingFieldPropertyRenderer)context).getFieldId();
      if(fieldId != null)
      {
        if( (object != null) && (object instanceof IGTMappingFileEntity) )
        {
          if(IGTMappingRuleEntity.REF_DOC_UID.equals(fieldId))
          {
            return mappingFileIsType((IGTMappingFileEntity)object,IGTMappingFileEntity.TYPE_REFERENCE_DOC);
          }
          else if(IGTMappingRuleEntity.MAPPING_FILE.equals(fieldId))
          {
            Short type = getType();

            if(type == null)
            {
              return false;
            }
            if(IGTMappingRuleEntity.TYPE_MAPPING_CONVERT.equals(type) && mappingFileIsType((IGTMappingFileEntity)object,IGTMappingFileEntity.TYPE_CONVERSION_RULE))
            {
              return mappingFileIsType((IGTMappingFileEntity)object,IGTMappingFileEntity.TYPE_CONVERSION_RULE);
            }
            //added by ming qian
            else if(IGTMappingRuleEntity.TYPE_MAPPING_CONVERT.equals(type) && mappingFileIsType((IGTMappingFileEntity)object,IGTMappingFileEntity.TYPE_JAVA_BINARY))
            {
              return mappingFileIsType((IGTMappingFileEntity)object,IGTMappingFileEntity.TYPE_JAVA_BINARY);
            }
            //end of added by ming qian
            else if(IGTMappingRuleEntity.TYPE_MAPPING_SPLIT.equals(type))
            {
              return mappingFileIsType((IGTMappingFileEntity)object,IGTMappingFileEntity.TYPE_XSL);
            }
            else if(IGTMappingRuleEntity.TYPE_MAPPING_TRANSFORM.equals(type) && mappingFileIsType((IGTMappingFileEntity)object,IGTMappingFileEntity.TYPE_XSL))
            {
              return mappingFileIsType((IGTMappingFileEntity)object,IGTMappingFileEntity.TYPE_XSL);
            }
            else if(IGTMappingRuleEntity.TYPE_MAPPING_TRANSFORM.equals(type) && mappingFileIsType((IGTMappingFileEntity)object,IGTMappingFileEntity.TYPE_JAVA_BINARY))
            {
              return mappingFileIsType((IGTMappingFileEntity)object,IGTMappingFileEntity.TYPE_JAVA_BINARY);
            }
          }
        }
      }
    }
    return true;
  }

  protected Short getType()
  {
    if(_type == null)
    {
      _type =  StaticUtils.shortValue(((GridTalkMappingRuleAForm)
                  (GridTalkMappingRuleAForm)getRenderingContext(
                  ).getOperationContext().getActionForm()).getType());
    }
    return _type;
  }

  protected boolean mappingFileIsType(IGTMappingFileEntity mappingFile, Short type)
  throws RenderingException
  {
    try
    {
      if(type == null) return false;
      if(mappingFile == null) throw new java.lang.NullPointerException("null mappingFile entity");
      Short mfType = (Short)mappingFile.getFieldValue(mappingFile.TYPE);
      if(mfType == null) throw new java.lang.NullPointerException("mappingFile:" + mappingFile + " has null type field");
      boolean eekwil = mfType.equals(type);
      return eekwil;
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error determining type",t);
    }
  }
  
  //added by ming qian
  public boolean mappingFileIsJavaBinary(Long mappingFileUID) throws GTClientException
  { //20030717AH
    //Long procDefFileUid = getProcDefFileUid(rContext);
    boolean isJavaBinary = true;
    
    IGTSession gtasSession = getEntity().getSession();
    IGTMappingFileManager mappingFileMgr = (IGTMappingFileManager)gtasSession.getManager(IGTManager.MANAGER_MAPPING_FILE);    
    IGTMappingFileEntity mappingFile = mappingFileMgr.getMappingFileByUID(mappingFileUID);
  
    if (mappingFile.getFieldString(mappingFile.TYPE).equals(IGTMappingFileEntity.TYPE_JAVA_BINARY.toString()))
    {
      //mapping file is a java binary
      isJavaBinary = true;
    }
    else
    {
      //mapping file is not a java binary
      isJavaBinary = false;
    }
          
    return isJavaBinary;
  }
  //end of added by ming qian
  
    

  private void doNastyMappingFileDiversionHackForWhichYouOughtToBeAshamed(GridTalkMappingRuleAForm form)
    throws RenderingException
  { //20030826AH - NASTY HACK to make the fbd work for mappingFile
    //@todo: do properly!
    try
    {
      FieldDivPath divPath = new FieldDivPath();
      divPath.setFieldName("mappingRule.mappingFile");
      Element anchor = null;
      Element parent = null;
      if(_edit)
      {
        parent = getElementById("mappingFile_diversion",false);
      }
      else
      {
        parent = getElementById("mappingFile_value",false);
        divPath.setValue(form.getMappingFile());
      }
      if(parent != null)
      {
        anchor = findElement(parent, "a", null, null);
      }
      if(anchor != null)
      { //ugly ugly ugly! shame andrew shame!
        if(_edit)
        {
          addJavaScriptNode(null, MAPPING_FILE_HACK_JS);
          anchor.setAttribute("href", "javascript: mappingFileDivert('" + divPath.getDivPath() + "');");
        }
        else
        {
          anchor.setAttribute("href", "javascript: divertToMapping('" + divPath.getDivPath() + "');");
        }
      }
    }
    catch(Throwable servesYouRight)
    {
      throw new RenderingException("Error correcting mappingFile field diversion",servesYouRight);
    }
  }
}