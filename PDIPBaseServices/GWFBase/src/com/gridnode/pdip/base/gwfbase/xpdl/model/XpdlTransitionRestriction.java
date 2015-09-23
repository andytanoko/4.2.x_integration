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


import com.gridnode.pdip.base.gwfbase.baseentity.GWFRestriction;
 

public class XpdlTransitionRestriction extends GWFRestriction
    implements IXpdlTransitionRestriction
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -9405589125873551L;
		//Attributes
    protected Boolean _isInlineBlock;
    protected String _blockName;
    protected String _blockDescription;
    protected String _blockIconUrl;
    protected String _blockDocumentationUrl;
    protected String _blockBeginActivityId;
    protected String _blockEndActivityId;
    protected String _joinType;
    protected String _splitType;
    protected Long _transitionRefListUId;
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

    public Boolean getIsInlineBlock()
    {
        return _isInlineBlock;
    }

    public String getBlockName()
    {
        return _blockName;
    }

    public String getBlockDescription()
    {
        return _blockDescription;
    }

    public String getBlockIconUrl()
    {
        return _blockIconUrl;
    }

    public String getBlockDocumentationUrl()
    {
        return _blockDocumentationUrl;
    }

    public String getBlockBeginActivityId()
    {
        return _blockBeginActivityId;
    }

    public String getBlockEndActivityId()
    {
        return _blockEndActivityId;
    }

    public String getJoinType()
    {
        return _joinType;
    }

    public String getSplitType()
    {
        return _splitType;
    }

    public Long getTransitionRefListUId()
    {
        return _transitionRefListUId;
    }

    public Long getListUId()
    {
        return _listUId;
    }

    // ******************** Setters for attributes ***************************

    public void setIsInlineBlock(Boolean isInlineBlock)
    {
        _isInlineBlock = isInlineBlock;
    }

    public void setBlockName(String blockName)
    {
        _blockName = blockName;
    }

    public void setBlockDescription(String blockDescription)
    {
        _blockDescription = blockDescription;
    }

    public void setBlockIconUrl(String blockIconUrl)
    {
        _blockIconUrl = blockIconUrl;
    }

    public void setBlockDocumentationUrl(String blockDocumentationUrl)
    {
        _blockDocumentationUrl = blockDocumentationUrl;
    }

    public void setBlockBeginActivityId(String blockBeginActivityId)
    {
        _blockBeginActivityId = blockBeginActivityId;
    }

    public void setBlockEndActivityId(String blockEndActivityId)
    {
        _blockEndActivityId = blockEndActivityId;
    }

    public void setJoinType(String joinType)
    {
        _joinType = joinType;
    }

    public void setSplitType(String splitType)
    {
        _splitType = splitType;
    }

    public void setTransitionRefListUId(Long transitionRefListUId)
    {
        _transitionRefListUId = transitionRefListUId;
    }

    public void setListUId(Long listUId)
    {
        _listUId = listUId;
    }

}
