/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 24 2002    Mahesh              Created
 *
 */
package com.gridnode.pdip.base.gwfbase.bpss.model;


import com.gridnode.pdip.base.gwfbase.baseentity.GWFProcess;
 

public class BpssBinaryCollaboration extends GWFProcess
    implements IBpssBinaryCollaboration
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1004045134438171278L;
		protected String _binaryCollaborationName;
    protected String _timeToPerform;
    protected String _pattern;
    protected String _preCondition;
    protected String _postCondition;
    protected String _beginsWhen;
    protected String _endsWhen;

    // ******************* Methods from AbstractEntity ******************
    public String getEntityName()
    {
        return ENTITY_NAME;
    }

    public String getEntityDescr()
    {
        return getEntityName();
    }

    public Number getKeyId()
    {
        return UID;
    }

    // *********************** Getters for attributes **********************


    public String getBinaryCollaborationName()
    {
        return _binaryCollaborationName;
    }

    public String getPattern()
    {
        return _pattern;
    }

    public String getTimeToPerform()
    {
        return _timeToPerform;
    }

    public String getPreCondition()
    {
        return _preCondition;
    }

    public String getPostCondition()
    {
        return _postCondition;
    }

    public String getBeginsWhen()
    {
        return _beginsWhen;
    }

    public String getEndsWhen()
    {
        return _endsWhen;
    }

    // *********************** Setters for attributes **********************
    public void setBinaryCollaborationName(String binaryCollaborationName)
    {
        _binaryCollaborationName = binaryCollaborationName;
    }

    public void setPattern(String pattern)
    {
        _pattern = pattern;
    }

    public void setTimeToPerform(String timeToPerform)
    {
        _timeToPerform = timeToPerform;
    }

    public void setPreCondition(String preCondition)
    {
        _preCondition = preCondition;
    }

    public void setPostCondition(String postCondition)
    {
        _postCondition = postCondition;
    }

    public void setBeginsWhen(String beginsWhen)
    {
        _beginsWhen = beginsWhen;
    }

    public void setEndsWhen(String endsWhen)
    {
        _endsWhen = endsWhen;
    }

}
