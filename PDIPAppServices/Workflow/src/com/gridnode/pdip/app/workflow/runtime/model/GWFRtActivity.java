/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 28 2002    Mahesh              Created
 *
 */
package com.gridnode.pdip.app.workflow.runtime.model;

import java.util.Date;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
 
 public class GWFRtActivity
  extends AbstractEntity
  implements IGWFRtActivity{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1483650486595277803L;
	protected Long _activityUId;
  protected Integer _state;
  protected Integer _priority;
  protected Long _rtProcessUId;
  protected String _activityType;
  protected Long _finishInterval;
  protected Date _startTime;
  protected Date _endTime;
  protected Long _contextUId;
  protected String _engineType;
  protected String _branchName;
  protected String _processDefKey;

  // ******************* Methods from AbstractEntity ******************
  public String getEntityName(){
    return ENTITY_NAME;
  }

  public String getEntityDescr(){
    return getEntityName();
  }

  public Number getKeyId(){
    return UID;
  }


   // *********************** Getters for attributes **********************

  public Long getActivityUId(){
    return _activityUId;
  }
  public Integer getState(){
    return _state;
  }
  public Integer getPriority(){
    return _priority;
  }
  public Long getRtProcessUId(){
    return _rtProcessUId;
  }

  public String getActivityType(){
    return _activityType;
  }
  public Long getFinishInterval(){
    return _finishInterval;
  }
  public Date getStartTime(){
    return _startTime;
  }
  public Date getEndTime(){
    return _endTime;
  }
  public Long getContextUId(){
    return _contextUId;
  }
  public String getEngineType(){
    return _engineType;
  }
  public String getBranchName(){
    return _branchName;
  }
  public String getProcessDefKey(){
    return _processDefKey;
  }


  // *********************** Setters for attributes **********************

  public void setActivityUId(Long activityUId){
    _activityUId=activityUId;
  }

  public void setState(Integer state){
    _state=state;
  }

  public void setPriority(Integer priority){
    _priority=priority;
  }

  public void setRtProcessUId(Long rtProcessUId){
    _rtProcessUId=rtProcessUId;
  }

  public void setActivityType(String activityType){
    _activityType=activityType;
  }
  public void setFinishInterval(Long finishInterval){
    _finishInterval=finishInterval;
  }
  public void setStartTime(Date startTime){
    _startTime=startTime;
  }
  public void setEndTime(Date endTime){
    _endTime=endTime;
  }
  public void setContextUId(Long contextUId){
    _contextUId=contextUId;
  }
  public void setEngineType(String engineType){
    _engineType=engineType;
  }
  public void setBranchName(String branchName){
    _branchName=branchName;
  }
  public void setProcessDefKey(String processDefKey){
    _processDefKey=processDefKey;
  }

}
