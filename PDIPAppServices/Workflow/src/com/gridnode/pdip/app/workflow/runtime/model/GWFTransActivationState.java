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

public class GWFTransActivationState
  extends AbstractEntity
  implements IGWFTransActivationState{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1702074301640688234L;
	protected Long _transitionUId;
  protected Long _rtRestrictionUId;
  protected String _rtRestrictionType;
  protected Boolean _state;
  protected Long _listUId;


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

  //********************************
  public Long getTransitionUId(){
    return _transitionUId;
  }
  public Long getRtRestrictionUId(){
    return _rtRestrictionUId;
  }
  public String getRtRestrictionType(){
    return _rtRestrictionType;
  }
  public Boolean getState(){
    return _state;
  }
  public Long getListUId(){
    return _listUId;
  }


 //**********************************
  public void setTransitionUId(Long transitionUId){
    _transitionUId=transitionUId;
  }
  public void setRtRestrictionUId(Long rtRestrictionUId){
    _rtRestrictionUId=rtRestrictionUId;
  }
  public void setRestrictionType(String rtRestrictionType){
    _rtRestrictionType=rtRestrictionType;
  }
  public void setState(Boolean state){
    _state=state;
  }
  public void setListUId(Long listUId){
    _listUId=listUId;
  }

}
