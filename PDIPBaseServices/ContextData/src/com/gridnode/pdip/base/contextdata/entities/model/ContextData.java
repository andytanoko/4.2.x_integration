package com.gridnode.pdip.base.contextdata.entities.model;

import com.gridnode.pdip.framework.db.entity.*;
import java.util.*;

public class ContextData
    extends AbstractEntity
    implements IContextData{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3425396312770568029L;
		Long _contextUId;
    HashMap _contextData=new HashMap();

    //Abstract methods of AbstractEntity
    public String getEntityName() {
            return ENTITY_NAME;
    }

    public Number getKeyId() {
            return UID;
    }

    public String getEntityDescr() {
            return ENTITY_NAME+":"+_uId;
    }

    // ******************** Getters for attributes ***************************

    public Long getContextUId(){
        return _contextUId;
    }
    public HashMap getContextData(){
        return _contextData;
    }
    // ******************** Getters for attributes ***************************

    public void setContextUId(Long contextUId){
        _contextUId=contextUId;
    }

    public void setContextData(HashMap contextData){
        _contextData=contextData;
    }

}