/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 21 2002    MAHESH              Created
 */
package com.gridnode.pdip.base.gwfbase.xpdl.model;


import com.gridnode.pdip.framework.db.entity.*;


public class XpdlExternalPackage extends AbstractEntity
    implements IXpdlExternalPackage
{ 

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5297887976738880708L;
		//Attributes
    protected String _href;
    protected String _extendedAttributes;
    protected String _packageId;
    protected String _pkgVersionId;

    //Abstract methods of AbstractEntity
    public String getEntityName()
    {
        return ENTITY_NAME;
    }

    public Number getKeyId()
    {
        return UID;
    }

    public String getEntityDescr()
    {
        return ENTITY_NAME + ":" + _uId;
    }

    // ******************** Getters for attributes ***************************

    public String getHref()
    {
        return _href;
    }

    public String getExtendedAttributes()
    {
        return _extendedAttributes;
    }

    public String getPackageId()
    {
        return _packageId;
    }

    public String getPkgVersionId()
    {
        return _pkgVersionId;
    }

    // ******************** Setters for attributes ***************************

    public void setHref(String href)
    {
        _href = href;
    }

    public void setExtendedAttributes(String extendedAttributes)
    {
        _extendedAttributes = extendedAttributes;
    }

    public void setPackageId(String packageId)
    {
        _packageId = packageId;
    }

    public void setPkgVersionId(String pkgVersionId)
    {
        _pkgVersionId = pkgVersionId;
    }

}
