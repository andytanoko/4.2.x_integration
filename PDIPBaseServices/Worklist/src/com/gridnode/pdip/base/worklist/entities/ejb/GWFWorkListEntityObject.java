package com.gridnode.pdip.base.worklist.entities.ejb;

/**
 * <p>Title: GridFlow</p>
 * <p>Description: GridFlow - Extended Enterprise Businessware</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: GridNode Pte Ltd.</p>
 * @author Jagadeesh
 * @version 1.0
 */
import com.gridnode.pdip.framework.db.ejb.IEntityObject;

/*
 * GWFWorkListEntityHome is WorkList specific Entity Object which publishes
 * business methods for Remote object.
 *
 * Table                 Database                Descriptin.
 * ----                 ---------                ----------
 * 1.worklistvalue       GWFDB                   This table stores worklist items
 *                                               (user specific).
*/

public interface GWFWorkListEntityObject extends IEntityObject {
}
