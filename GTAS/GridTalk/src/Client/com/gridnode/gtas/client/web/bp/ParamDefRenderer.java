/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ParamDefRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-24     Daniel D'Cotta      Created
 * 2003-09-08     Daniel D'Cotta      Added a IFliter for DataType
 * 2003-09-09     Daniel D'Cotta      Refactored rendering the DataType and
 *                                    DataValue to do more checking
 * 2003-11-28     Koh Han Sing        Added new data type byte[], byte[][]
 */
package com.gridnode.gtas.client.web.bp;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;

import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.web.strutsbase.FakeEnumeratedConstraint;

public class ParamDefRenderer extends AbstractRenderer
{
  private static final Log _log = LogFactory.getLog(ParamDefRenderer.class);

  private static final Number _fields[] =
  {
    IGTParamDefEntity.PARAM_LIST_NAME,
    IGTParamDefEntity.PARAM_LIST_DESCRIPTION,
    IGTParamDefEntity.PARAM_LIST_SOURCE,
//    IGTParamDefEntity.PARAM_LIST_TYPE,  // rendered individually
    IGTParamDefEntity.PARAM_LIST_DATE_FORMAT,
//    IGTParamDefEntity.PARAM_LIST_VALUE, // rendered individually
  };

/*
  Note: Andrew suggested to implement it this way as OptionSource
  currently does handle enumerated constraints

  type=enum
  paramDef.type.string=1
  paramDef.type.integer=2
  paramDef.type.long=3
  paramDef.type.double=4
  paramDef.type.boolean=5
  paramDef.type.date=6
  paramDef.type.object=7
  paramDef.type.datahandler=8
  paramDef.type.datahandlerArray=9
  paramDef.type.stringArray=10
*/

  private static final String[] _userDefinedTypeLabels =
  {
    "paramDef.type.string",
    "paramDef.type.stringArray",
    "paramDef.type.integer",
    "paramDef.type.long",
    "paramDef.type.double",
    "paramDef.type.boolean",
    "paramDef.type.date",
    "paramDef.type.object",
  };

  private static final String[] _userDefinedTypeValues =
  {
    "1",
    "10",
    "2",
    "3",
    "4",
    "5",
    "6",
    "7",
  };

  private static final String[] _gdocTypeLabels =
  {
    "paramDef.type.string",
    "paramDef.type.integer",
    "paramDef.type.long",
    "paramDef.type.double",
    "paramDef.type.boolean",
    "paramDef.type.date",
    "paramDef.type.object",
  };

  private static final String[] _gdocTypeValues =
  {
    "1",
    "2",
    "3",
    "4",
    "5",
    "6",
    "7",
  };

  private static final String[] _udocTypeLabels =
  {
    "paramDef.type.string",
    "paramDef.type.integer",
    "paramDef.type.long",
    "paramDef.type.double",
    "paramDef.type.boolean",
    "paramDef.type.date",
    "paramDef.type.object",
    "paramDef.type.datahandler",
    "paramDef.type.byteArray",
  };

  private static final String[] _udocTypeValues =
  {
    "1",
    "2",
    "3",
    "4",
    "5",
    "6",
    "7",
    "8",
    "11",
  };

  private static final String[] _attachmentsTypeLabels =
  {
    "paramDef.type.stringArray",
    "paramDef.type.datahandlerArray",
    "paramDef.type.byteArrayArray",
  };

  private static final String[] _attachmentsTypeValues =
  {
    "10",
    "9",
    "12",
  };

  private static final IEnumeratedConstraint _userDefinedTypeConstraint
    = new FakeEnumeratedConstraint(_userDefinedTypeLabels, _userDefinedTypeValues);

  private static final IEnumeratedConstraint _gdocTypeConstraint
    = new FakeEnumeratedConstraint(_gdocTypeLabels, _gdocTypeValues);

  private static final IEnumeratedConstraint _udocTypeConstraint
    = new FakeEnumeratedConstraint(_udocTypeLabels, _udocTypeValues);

  private static final IEnumeratedConstraint _attachmentsTypeConstraint
    = new FakeEnumeratedConstraint(_attachmentsTypeLabels, _attachmentsTypeValues);

  private static final IEnumeratedConstraint _emptyTypeConstraint
  = new FakeEnumeratedConstraint(new String[]{}, new String[]{});

  private static IEnumeratedConstraint _gdocValueConstraint = null;

  private static IEnumeratedConstraint _attachmentsValueConstraint = null;

  private boolean _edit;

  public ParamDefRenderer(RenderingContext rContext,
                          boolean edit)
  {
    super(rContext);
    _edit = edit;

    IGTSession gtSession = StaticWebUtils.getGridTalkSession(rContext.getSession());

    // Initialise enumerated constraints using fields in other entities
    if(_gdocValueConstraint == null)
    {
      _gdocValueConstraint = getEnumConstraintFromEntityFields(gtSession,
                                                               IGTEntity.ENTITY_GRID_DOCUMENT);
    }
    if(_attachmentsValueConstraint == null)
    {
      _attachmentsValueConstraint = getEnumConstraintFromEntityFields(gtSession,
                                                                      IGTEntity.ENTITY_ATTACHMENT);
    }
  }

  protected void render() throws RenderingException
  {
    try
    {

      IGTParamDefEntity paramDef = (IGTParamDefEntity)getEntity();
      ParamDefAForm form = (ParamDefAForm)getActionForm();
      RenderingContext rContext = getRenderingContext();

      renderCommonFormElements(IGTEntity.ENTITY_PARAM_DEF, _edit);
      renderFields(null, paramDef, _fields);

      Integer source = form.getSourceInteger();
      Integer type = form.getTypeInteger();

//    ================================================================================

      // Only show combo box of data types available to data source
      // Note: Andrew suggested to implement it this way as OptionSource
      // currently does handle enumerated constraints
      IEnumeratedConstraint typeEnumConstraint;
      if(IGTParamDefEntity.SOURCE_USER_DEFINED.equals(source))
        typeEnumConstraint = _userDefinedTypeConstraint;
      else if(IGTParamDefEntity.SOURCE_GDOC.equals(source))
        typeEnumConstraint = _gdocTypeConstraint;
      else if(IGTParamDefEntity.SOURCE_UDOC.equals(source))
        typeEnumConstraint = _udocTypeConstraint;
      else if(IGTParamDefEntity.SOURCE_ATTACHMENTS.equals(source))
        typeEnumConstraint = _attachmentsTypeConstraint;
      else
        typeEnumConstraint = _emptyTypeConstraint;

      BindingFieldPropertyRenderer bfpr = new BindingFieldPropertyRenderer(rContext);
      ActionErrors errors = rContext.getActionErrors();
      bfpr.setBindings(paramDef, IGTParamDefEntity.PARAM_LIST_TYPE, errors);
      bfpr.setConstraint(typeEnumConstraint);
      bfpr.render(_target);

//    ================================================================================

      // Only show combo box of date formats when type is date
      if(!IGTParamDefEntity.DATA_TYPE_DATE.equals(type))
        removeNode("dateFormat_details");

//    ================================================================================

      if(IGTParamDefEntity.DATA_TYPE_DATAHANDLER.equals(type)
        || IGTParamDefEntity.DATA_TYPE_DATAHANDLER_ARRAY.equals(type)
        || IGTParamDefEntity.DATA_TYPE_BYTE_ARRAY.equals(type)
        || IGTParamDefEntity.DATA_TYPE_BYTE_ARRAY_ARRAY.equals(type))
      { // Hide the DataValue field
        removeAllChildren("value_details", false);
      }
      else
      {
        bfpr.reset();
        bfpr.setBindings(paramDef, IGTParamDefEntity.PARAM_LIST_VALUE, errors);
        boolean isEnumConstraint = false;
        if(IGTParamDefEntity.SOURCE_GDOC.equals(source))
        {
          isEnumConstraint = true;
          bfpr.setConstraint(_gdocValueConstraint);

          if(IGTParamDefEntity.DATA_TYPE_OBJECT.equals(type))
          {
            bfpr.setMandatory(false);
          }
        }
        else if(IGTParamDefEntity.SOURCE_ATTACHMENTS.equals(source))
        {
          if(IGTParamDefEntity.DATA_TYPE_STRING_ARRAY.equals(type))
          {
            isEnumConstraint = true;
            bfpr.setConstraint(_attachmentsValueConstraint);
          }
        }
        // Check whether to hide the text box or check box
        if(isEnumConstraint)
        {
          removeAllChildren("value_text_box", false);
        }
        else
        {
          removeAllChildren("value_combo_box", false);
        }
        bfpr.render(_target);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering paramDef screen",t);
    }
  }

  private IEnumeratedConstraint getEnumConstraintFromEntityFields(IGTSession gtSession, String entity)
  {
    try
    {
      Collection fmis = EntityFieldNameValueRetriever.buildFmiCollection(
                                gtSession,
                                entity,
                                false);

      String[] labels = new String[fmis.size()];
      String[] values = new String[fmis.size()];
      Iterator i = fmis.iterator();
      for(int x = 0; i.hasNext(); x++)
      {
        IGTFieldMetaInfo fmi = (IGTFieldMetaInfo)i.next();
        labels[x] = fmi.getLabel();
        values[x] = fmi.getFieldId().toString();
      }

      return new FakeEnumeratedConstraint(labels, values);
    }
    catch(Throwable t)
    {
      if(_log.isErrorEnabled())
      {
        _log.error("Error initialising IEnumeratedConstraint for " + entity, t);
      }
      return null;
    }
  }
}