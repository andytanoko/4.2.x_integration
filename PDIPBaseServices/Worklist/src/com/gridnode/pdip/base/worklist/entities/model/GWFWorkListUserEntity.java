package com.gridnode.pdip.base.worklist.entities.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

 

public class GWFWorkListUserEntity extends AbstractEntity
implements GWFIWorkListUserEntity {

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8862510290522203021L;
	Long workitem_id;
  String  user_id = new String();


  public void setWorkItemID(Long id){
   this.workitem_id = id;
  }

  public void setUserID(String userid){
   this.user_id = userid;
  }

  public Long getWorkItemID(){
   return workitem_id;
  }

  public String getUserID(){
   return user_id;
  }

  public String getEntityName(){
    return GWFWorkListUserEntity.ENTITY_NAME;
  }


  public String getEntityDescr(){
    return getEntityName();
  }

  public Number getKeyId(){
    return UID;
  }



}

