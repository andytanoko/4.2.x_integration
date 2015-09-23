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
 
public class GWFRtProcess
  extends AbstractEntity
  implements IGWFRtProcess{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6543371000233400305L;
	protected Long _processUId;
  protected Integer _state;
  protected String _processType;
  protected Long _parentRtActivityUId;
  protected Integer _maxConcurrency=new Integer(0);
  protected Long _finishInterval;
  protected Date _startTime;
  protected Date _endTime;
  protected String _engineType;
  protected Long _contextUId;
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

  public Long getProcessUId(){
    return _processUId;
  }
  public Integer getState(){
    return _state;
  }
  public String getProcessType(){
    return _processType;
  }
  public Long getParentRtActivityUId(){
    return _parentRtActivityUId;
  }
  public Integer getMaxConcurrency(){
    return _maxConcurrency;
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
  public String getEngineType(){
    return _engineType;
  }
  public Long getContextUId(){
    return _contextUId;
  }
  public String getProcessDefKey(){
    return _processDefKey;
  }

  // *********************** Setters for attributes **********************

  public void setProcessUId(Long processUId){
    _processUId=processUId;
  }
  public void setState(Integer state){
    _state=state;
  }
  public void setProcessType(String processType){
    _processType=processType;
  }
  public void setParentRtActivityUId(Long parentRtActivityUId){
    _parentRtActivityUId=parentRtActivityUId;
  }
  public void setMaxConcurrency(Integer maxConcurrency){
    _maxConcurrency=maxConcurrency;
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
  public void setEngineType(String engineType){
    _engineType=engineType;
  }
  public void setContextUId(Long contextUId){
    _contextUId=contextUId;
  }
  public void setProcessDefKey(String processDefKey){
    _processDefKey=processDefKey;
  }

  //*********************** helper methods

  public boolean isFailed() {
    if(_state!=null){
        int st=_state.intValue();
        return (st==CLOSED_ABNORMALCOMPLETED || st==CLOSED_ABNORMALCOMPLETED_ABORTED || st==CLOSED_ABNORMALCOMPLETED_TERMINATED);
    }
    return true;
  }
}
