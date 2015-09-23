/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActivationRecordRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-15     Andrew Hill         Created
 * 2003-02-18     Andrew Hill         Implement allows() to filter BEs
 * 2003-03-24     Andrew Hill         Modify rendering of dv buttons
 */
package com.gridnode.gtas.client.web.activation;

import java.util.Collection;

import org.w3c.dom.Element;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.IFilter;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.*;

public class ActivationRecordRenderer extends AbstractRenderer implements IFilter
{
  private static final String ABORT_IMG_SRC = "images/actions/abort.gif"; //20030324AH
  private static final String APPROVE_IMG_SRC = "images/actions/approve.gif"; //20030324AH

  private boolean _edit;

  private static final Number[] _commonFields = {
    IGTActivationRecordEntity.GRIDNODE_ID,
    IGTActivationRecordEntity.GRIDNODE_NAME
  };

  private static final Number[] _requestActivationFields = {
    IGTGridNodeActivationEntity.ACTIVATE_REASON,
    IGTGridNodeActivationEntity.REQUESTOR_BE_LIST,
  };

  private static final Number[] _incomingActivationFields = {
    IGTGridNodeActivationEntity.ACTIVATE_REASON,
    IGTGridNodeActivationEntity.APPROVER_BE_LIST,
  };

  private static Object[] _requestorBeColumns = {
    IGTBusinessEntityEntity.ID,
    IGTBusinessEntityEntity.DESCRIPTION,
  };

  public ActivationRecordRenderer(RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      IGTActivationRecordEntity record = (IGTActivationRecordEntity)getEntity();
      IGTGridNodeActivationEntity activation = (IGTGridNodeActivationEntity)
                                                record.getFieldValue(IGTActivationRecordEntity.ACTIVATION_DETAILS);
      ActivationRecordAForm form = (ActivationRecordAForm)getActionForm();
      Short currentType = form.getCurrentTypeShort();
      renderCommonFormElements(record.getType(),_edit);
      BindingFieldPropertyRenderer bfpr = renderFields(null, record,_commonFields);
      if(record.isNewEntity())
      { // If its new then we are doing a request activation submission
        if(_edit)
        {
          renderLabel("requestorBeList_create","gridNodeActivation.requestorBeList.create",false);
        }
        renderFields(bfpr, activation, _requestActivationFields, this, form, null); //20030425AH
        removeNode("approverBeList_details",false);
        removeNode("deny",false);
      }
      else
      {
        if(IGTActivationRecordEntity.CURRENT_TYPE_ACTIVATION.equals(currentType))
        { //Its an existing activation request
          Short actDirection = form.getActDirectionShort();
          if(IGTActivationRecordEntity.DIRECTION_OUTGOING.equals(actDirection))
          { // Activation request that we made that hasnt been completed
            // Show its details, and provide an abort button
            // UNTESTED
            if(_edit)
            {
              removeNode("requestorBeList_create",false);
              replaceNodeWithNewElement("span","activateReason_value",false,false);
              replaceNodeWithNewElement("ul","requestorBeList_value",false,false);
              Element ok = getElementById("ok",false); // Convert the ok button to do abortion!
              if(ok != null)
              {
                ok.setAttribute("href","javascript:submitWithMethod('abort',true);");
                //20030324AH... change icon too
                replaceTextCarefully(ok,rContext.getResourceLookup().getMessage("activationRecord.edit.abort"));
                Element okIcon = getElementById("ok_icon",false);
                if(okIcon != null)
                {
                  okIcon.setAttribute("src",ABORT_IMG_SRC);
                }
                //..
              }
              removeNode("deny",false);
            }
            renderFields(bfpr, activation, _requestActivationFields, this, form, null); //20030425AH
            removeNode("approverBeList_details",false);
            renderLabel("heading","activationRecord.edit.heading.activation.outgoing",false);
          }
          else if(IGTActivationRecordEntity.DIRECTION_INCOMING.equals(actDirection))
          { // Incoming activation request
            if(_edit)
            {
              replaceNodeWithNewElement("span","activateReason_value",false,false);
              Element ok = getElementById("ok",false); // Convert the ok button to do approval
              if(ok != null)
              {
                //20030425AH - Modify call appropriately. Following the change to newui we use
                //the onclick for the ok button - and this was overridding the href call!
                ok.setAttribute("onclick","submitWithMethod('approve',true); return false;");
                //20030324AH - Modify icon too
                replaceTextCarefully(ok,rContext.getResourceLookup().getMessage("activationRecord.edit.approve"));
                Element okIcon = getElementById("ok_icon",false);
                if(okIcon != null)
                {
                  okIcon.setAttribute("src",APPROVE_IMG_SRC);
                }
                //...
              }
              //20030324AH - Move deny into common button bar with an icon
              renderLabelCarefully("deny","activationRecord.edit.deny",false);
              Element denyButton = getElementById("deny_button",false);
              Element commonButtons = getElementById("common_buttons",false);
              if( (denyButton != null) && (commonButtons != null) )
              {
                denyButton.getParentNode().removeChild(denyButton);
                insertFirstChild(commonButtons, denyButton);
              }
              //...
            }
            else
            {
              removeNode("approverBeList_details",false);
              removeNode("deny",false);
            }
            renderLabel("approverBeList_create","gridNodeActivation.approverBeList.create",false);
            renderFields(bfpr, activation, _incomingActivationFields, this, form, null); //20030425AH
            removeNode("requestorBeList_details",false);
            renameNode("requestorBeList_details_2","requestorBeList_details",true);
            Collection requestorBeList = activation.getFieldEntities(IGTGridNodeActivationEntity.REQUESTOR_BE_LIST);
            renderFieldElv(rContext,activation,IGTGridNodeActivationEntity.REQUESTOR_BE_LIST,requestorBeList,_requestorBeColumns);
            renderLabel("heading","activationRecord.edit.heading.activation.incoming",false);
          }
          else
          {
            throw new java.lang.IllegalStateException("Unrecognised actDirection value:" + actDirection);
          }
        }
        else
        {
          if(_edit)
          {
            throw new java.lang.IllegalStateException("activationRecords of currentType="
                                                      + currentType
                                                      + " do not support editing");
          }
          if(IGTActivationRecordEntity.CURRENT_TYPE_DEACTIVATION.equals(currentType))
          { //Its an existing activation request
            Short deactDirection = form.getDeactDirectionShort();
            if(IGTActivationRecordEntity.DIRECTION_INCOMING.equals(deactDirection))
            {
              renderLabel("heading","activationRecord.edit.heading.deactivation.incoming",false);
              removeNode("requestorBeList_details",false);
              removeNode("approverBeList_details",false);
            }
            else if(IGTActivationRecordEntity.DIRECTION_OUTGOING.equals(deactDirection))
            {
              renderLabel("heading","activationRecord.edit.heading.deactivation.outgoing",false);
              removeNode("requestorBeList_details",false);
              removeNode("approverBeList_details",false);
            }
            else
            {
              throw new java.lang.IllegalStateException("Unrecognised deactDirection value:" + deactDirection);
            }
          }
          else if(IGTActivationRecordEntity.CURRENT_TYPE_APPROVAL.equals(currentType))
          {
            renderLabel("heading","activationRecord.edit.heading.approval",false);
            removeNode("requestorBeList_details",false);
            renameNode("requestorBeList_details_2","requestorBeList_details",true);
            Collection requestorBeList = activation.getFieldEntities(IGTGridNodeActivationEntity.REQUESTOR_BE_LIST);
            renderFieldElv(rContext,activation,IGTGridNodeActivationEntity.REQUESTOR_BE_LIST,requestorBeList,_requestorBeColumns);

            removeNode("approverBeList_details",false);
            renameNode("approverBeList_details_2","approverBeList_details",true);
            Collection approverBeList = activation.getFieldEntities(IGTGridNodeActivationEntity.APPROVER_BE_LIST);
            renderFieldElv(rContext,activation,IGTGridNodeActivationEntity.APPROVER_BE_LIST,approverBeList,_requestorBeColumns);
          }
          else if(IGTActivationRecordEntity.CURRENT_TYPE_DENIAL.equals(currentType))
          {
            renderLabel("heading","activationRecord.edit.heading.denial",false);
            removeNode("requestorBeList_details",false);
            removeNode("approverBeList_details",false);
          }
          else if(IGTActivationRecordEntity.CURRENT_TYPE_ABORTION.equals(currentType))
          {
            renderLabel("heading","activationRecord.edit.heading.abortion",false);
            removeNode("requestorBeList_details",false);
            removeNode("approverBeList_details",false);
          }
          else
          {
            throw new java.lang.IllegalStateException("Unrecognised currentType value:" + currentType);
          }
          renderFields(bfpr, activation, _requestActivationFields, this, form, null); //20030425AH
        }
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering activationRecord screen",t);
    }
  }

  public boolean allows(Object object, Object context) throws GTClientException
  { //20030218AH - Filtering of BE options in approverBeList and requestorBeList for request
    //and approval.
    Number field = ((BindingFieldPropertyRenderer)context).getFieldId();
    if(object instanceof IGTBusinessEntityEntity)
    {
      IGTBusinessEntityEntity be = (IGTBusinessEntityEntity)object;

      ActivationRecordAForm form = (ActivationRecordAForm)getActionForm();
      Short currentType = form.getCurrentTypeShort();
      if( ( IGTGridNodeActivationEntity.REQUESTOR_BE_LIST.equals(field)
            || IGTGridNodeActivationEntity.APPROVER_BE_LIST.equals(field) )
          && IGTActivationRecordEntity.CURRENT_TYPE_ACTIVATION.equals(currentType) )
      { //If we are making or approving an activation request then the list of requestor BEs
        //or approver BEs that may be selected should only contain our own BEs.
        boolean beIsOwn = ( false == (be.getIsPartner()).booleanValue());
        return beIsOwn;
      }
    }
    //Default behaviour for most cases is simply to return true.
    return true;
  }

  private void renderFieldElv(RenderingContext rContext,
                              IGTEntity entity,
                              Number fieldId,
                              Collection value,
                              Object[] columns)
    throws RenderingException
  {
    try
    {
      IGTSession gtasSession = StaticWebUtils.getGridTalkSession(rContext.getRequest());
      IGTFieldMetaInfo fmi = entity.getFieldMetaInfo(fieldId);
      String fieldName = fmi.getFieldName();
      IEntityConstraint constraint = (IEntityConstraint)fmi.getConstraint();
      String entityType = constraint.getEntityType();
      IGTManager manager = gtasSession.getManager(entityType);
      ColumnEntityAdapter adapter = new ColumnEntityAdapter(columns,manager,entityType);
      ListViewOptionsImpl listOptions = new ListViewOptionsImpl();

      listOptions.setColumnAdapter(adapter);
      listOptions.setCreateURL(null);
      listOptions.setDeleteURL(null);
      listOptions.setAllowsEdit(false);
      listOptions.setAllowsSelection(false);
      listOptions.setHeadingLabelKey(entity.getType() + "." + fieldName);
      listOptions.setViewURL(null);
      listOptions.setUpdateURL(null);
      ElvRenderer elvRenderer = new ElvRenderer(rContext,
                                                fieldName + "_details",
                                                listOptions,
                                                value);
      elvRenderer.setEmbeddedList(true);
      elvRenderer.setAllowsOrdering(false);
      elvRenderer.setIndexModeDefault(true);
      elvRenderer.setTableName(fieldName);
      elvRenderer.render(_target);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Unable to render the elv for fieldId" + fieldId,t);
    }
  }
}