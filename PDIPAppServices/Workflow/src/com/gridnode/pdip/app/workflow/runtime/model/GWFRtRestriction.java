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

import com.gridnode.pdip.framework.db.entity.*;

 public class GWFRtRestriction
  extends AbstractEntity
  implements IGWFRtRestriction{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8176826841366523243L;
	protected Long _restrictionUId;
  protected String _restrictionType;
  protected String _subRestrictionType;
  protected Long _rtProcessUId;
  protected Long _rtActivityUId;
  protected Long _transActivationStateListUId;
  protected String _processDefKey;

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

  public Long getRestrictionUId(){
    return _restrictionUId;
  }
  public String getRestrictionType(){
    return _restrictionType;
  }
  public String getSubRestrictionType(){
    return _subRestrictionType;
  }
  public Long getRtProcessUId(){
    return _rtProcessUId;
  }
  public Long getRtActivityUId(){
    return _rtActivityUId;
  }

  public Long getTransActivationStateListUId(){
    return _transActivationStateListUId;
  }

  public String getProcessDefKey(){
    return _processDefKey;
  }

  // *********************** Setters for attributes **********************

  public void setRestrictionUId(Long restrictionUId){
    _restrictionUId=restrictionUId;
  }
  public void setRestrictionType(String restrictionType){
    _restrictionType=restrictionType;
  }
  public void setSubRestrictionType(String subRestrictionType){
    _subRestrictionType=subRestrictionType;
  }
  public void setRtProcessUId(Long rtProcessUId){
    _rtProcessUId=rtProcessUId;
  }
  public void setRtActivityUId(Long rtActivityUId){
    _rtActivityUId=rtActivityUId;
  }

  public void setTransActivationStateListUId(Long transActivationStateListUId){
    _transActivationStateListUId=transActivationStateListUId;
  }

  public void setProcessDefKey(String processDefKey){
    _processDefKey=processDefKey;
  }


}
