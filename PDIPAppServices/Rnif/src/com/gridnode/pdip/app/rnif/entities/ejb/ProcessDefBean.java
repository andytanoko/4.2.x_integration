

package com.gridnode.pdip.app.rnif.entities.ejb;


import com.gridnode.pdip.app.rnif.helpers.ProcessDefDAOHelper;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.framework.db.dao.IEntityDAO;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.entity.IEntity;


 public class ProcessDefBean extends AbstractEntityBean {

	//Abstract methods of AbstractEntityBean
 
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2264438101748809816L;

	public String getEntityName(){
		 return ProcessDef.ENTITY_NAME;
 	}
  
  protected IEntityDAO getDAO()
  {
    return ProcessDefDAOHelper.getInstance();
  }

  //called during ejbCreate()
  protected void checkDuplicate(IEntity entity) throws java.lang.Exception
  {
    ProcessDefDAOHelper.getInstance().checkDuplicate(
      (ProcessDef)entity, false); // don't check key
  }
  
  protected boolean isVersionCheckRequired()
  {
    return false;
  }

}
