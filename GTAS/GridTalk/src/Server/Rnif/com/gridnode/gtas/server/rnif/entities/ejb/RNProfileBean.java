

package com.gridnode.gtas.server.rnif.entities.ejb;


import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;

import com.gridnode.gtas.server.rnif.model.RNProfile;

 public class RNProfileBean extends AbstractEntityBean {
 
	//Abstract methods of AbstractEntityBean

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7084311322371482671L;

	public String getEntityName(){
		 return RNProfile.ENTITY_NAME;
 	}
  
  protected boolean isVersionCheckRequired()
  {
    return false;
  }


}
