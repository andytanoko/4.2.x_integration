package com.gridnode.pdip.base.worklist.entities.ejb;

/**
 * <p>Title: GridFlow</p>
 * <p>Description: GridFlow - Extended Enterprise Businessware</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: GridNode Pte Ltd.</p>
 * @author Jagadeesh
 * @version 1.0
 */
import com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;

/**
 * GWFWorkListEntityBean is WorkList specific Entity Bean for performing
 * data specific task on WorkListUser table, which include creating,
 * updating,deleting and query on this Entity.
 * Table                 Database                Descriptin.
 * ----                 ---------                ----------
 * 1.worklistvalue       GWFDB                   This table stores worklist items
 *                                               (user specific).
 */


public class GWFWorkListEntityBean extends AbstractEntityBean{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 9039138406322191035L;

		//Abstract methods of AbstractEntityBean
    public String getEntityName(){
        return GWFWorkListValueEntity.ENTITY_NAME;
    }




}
