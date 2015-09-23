package com.gridnode.pdip.base.worklist.entities.model;

/**
 * <p>Title: GridFlow</p>
 * <p>Description: GridFlow - Extended Enterprise Businessware</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: GridNode Pte Ltd.</p>
 * @author Jagadeesh
 * @version 1.0
 */

import java.util.Date;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

public class GWFWorkListValueEntity 
extends AbstractEntity implements GWFIWorkListValueEntity{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2735325260858131633L;
	/**
   * FieldId for UId. A Number.
   */

   String iCal_wi_description=new String() ;
   String iCal_comments = new String();
   Boolean iCal_reqst_status = new Boolean(true);
   Date iCal_Creation_DT = new Date();
   String user_id = new String();
   String unassigned = new String();
   String processDefKey=null;
   String activityId=null;
   String performer=null;
   Long rtActivityUId=null;
   Long contextUId=null;


  // ******************* Methods from AbstractEntity ******************
  public String getEntityName(){
    return GWFWorkListValueEntity.ENTITY_NAME;
  }

  public String getEntityDescr(){
    return getEntityName();
  }

  public Number getKeyId(){
    return UID;
  }

  public String getWorkItemDescription(){
   return iCal_wi_description;
  }

  public String getWorkItemComments(){
   return iCal_comments;
  }

  public Boolean isSetStatus(){
   return iCal_reqst_status;
  }

  public Date getCreationDate(){
   return iCal_Creation_DT;
  }

  public String getUserId(){
   return user_id;
  }

  public String getUnassigned(){
   return unassigned;
  }

  public String getProcessDefKey(){
   return processDefKey;
  }

  public String getActivityId(){
   return activityId;
  }

  public Long getRtActivityUId(){
   return rtActivityUId;
  }

  public String getPerformer(){
   return performer;
  }

  public Long getContextUId(){
   return contextUId;
  }

  public void setWorkItemDescription(String iCal_wi_description){
   this.iCal_wi_description = iCal_wi_description;
  }

  public void setWorkItemComments(String Comments){
   this.iCal_comments = Comments;
  }

  public void setWorkItemStatus(Boolean status){
   this.iCal_reqst_status = status;
  }

  public void setCreationDate(Date date){
    this.iCal_Creation_DT = date;
  }

  public void setUserId(String userid){
   this.user_id = userid;
  }

  public void setUnassigned(String assign){
  this.unassigned = assign;
  }

  public void setProcessDefKey(String processDefKey){
   this.processDefKey=processDefKey;
  }

  public void setActivityId(String activityId){
   this.activityId=activityId;
  }

  public void setRtActivityUId(Long rtActivityUId){
   this.rtActivityUId=rtActivityUId;
  }

  public void setPerformer(String performer){
   this.performer=performer;
  }

  public void setContextUId(Long contextUId){
   this.contextUId=contextUId;
  }


}
