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


public class XpdlParticipant extends AbstractEntity
    implements IXpdlParticipant
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5639879308694049430L;
		//Attributes
    protected String _participantId;
    protected String _participantName;
    protected String _participantDescription;
    protected String _extendedAttributes;
    protected String _participantTypeId;
    protected String _processId;
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

    public String getParticipantId()
    {
        return _participantId;
    }

    public String getParticipantName()
    {
        return _participantName;
    }

    public String getParticipantDescription()
    {
        return _participantDescription;
    }

    public String getExtendedAttributes()
    {
        return _extendedAttributes;
    }

    public String getParticipantTypeId()
    {
        return _participantTypeId;
    }

    public String getProcessId()
    {
        return _processId;
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

    public void setParticipantId(String participantId)
    {
        _participantId = participantId;
    }

    public void setParticipantName(String participantName)
    {
        _participantName = participantName;
    }

    public void setParticipantDescription(String participantDescription)
    {
        _participantDescription = participantDescription;
    }

    public void setExtendedAttributes(String extendedAttributes)
    {
        _extendedAttributes = extendedAttributes;
    }

    public void setParticipantTypeId(String participantTypeId)
    {
        _participantTypeId = participantTypeId;
    }

    public void setProcessId(String processId)
    {
        _processId = processId;
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
