package com.gridnode.pdip.base.worklist.entities.ejb;

/**
 * <p>Title: GridFlow</p>
 * <p>Description: GridFlow - Extended Enterprise Businessware</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: GridNode Pte Ltd.</p>
 * @author Jagadeesh
 * @version 1.0
 */


import com.gridnode.pdip.base.worklist.entities.model.GWFWorkListUserEntity;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;

public class GWFWorkListUserEntityBean extends AbstractEntityBean{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5707488122798943545L;

		//Abstract methods of AbstractEntityBean
    public String getEntityName(){
        return GWFWorkListUserEntity.ENTITY_NAME;
    }


}
