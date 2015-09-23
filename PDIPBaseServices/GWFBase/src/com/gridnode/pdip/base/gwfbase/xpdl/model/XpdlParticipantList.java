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


public class XpdlParticipantList extends AbstractEntity
    implements IXpdlParticipantList
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4139346563199503246L;
		//Attributes 
    protected String _participantId;
    protected Long _participantIndex;
    protected Long _listUId;

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

    public Long getParticipantIndex()
    {
        return _participantIndex;
    }

    public Long getListUId()
    {
        return _listUId;
    }

    // ******************** Setters for attributes ***************************

    public void setParticipantId(String participantId)
    {
        _participantId = participantId;
    }

    public void setParticipantIndex(Long participantIndex)
    {
        _participantIndex = participantIndex;
    }

    public void setListUId(Long listUId)
    {
        _listUId = listUId;
    }

}
