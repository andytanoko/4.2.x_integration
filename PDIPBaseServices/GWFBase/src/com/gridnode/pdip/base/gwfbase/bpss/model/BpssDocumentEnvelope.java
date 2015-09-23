/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * June 27 2002	 MAHESH              Created
 */
package com.gridnode.pdip.base.gwfbase.bpss.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
 
public class BpssDocumentEnvelope
extends AbstractEntity
implements IBpssDocumentEnvelope {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5840629613905881178L;
	//Attributes
	protected String _businessDocumentName;
	protected String _businessDocumentIDRef;
	protected Boolean _isPositiveResponse=Boolean.FALSE;
	protected Boolean _isAuthenticated=Boolean.FALSE;
	protected Boolean _isConfidential=Boolean.FALSE;
	protected Boolean _isTamperProof=Boolean.FALSE;


	//Abstract methods of AbstractEntity
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public Number getKeyId() {
		return UID;
	}

	public String getEntityDescr() {
		return ENTITY_NAME+":"+_uId;
	}

	// ******************** Getters for attributes ***************************
	public String getBusinessDocumentName(){
            return _businessDocumentName;
        }
	public String getBusinessDocumentIDRef(){
            return _businessDocumentIDRef;
        }
	public Boolean getIsPositiveResponse(){
            return _isPositiveResponse;
        }
	public Boolean getIsAuthenticated(){
            return _isAuthenticated;
        }
	public Boolean getIsConfidential(){
            return _isConfidential;
        }
	public Boolean getIsTamperProof(){
            return _isTamperProof;
        }
	// ******************** Setters for attributes ***************************

	public void setBusinessDocumentName(String businessDocumentName){
            _businessDocumentName=businessDocumentName;
        }
	public void setBusinessDocumentIDRef(String businessDocumentIDRef){
            _businessDocumentIDRef=businessDocumentIDRef;
        }
	public void setIsPositiveResponse(Boolean isPositiveResponse){
            _isPositiveResponse=isPositiveResponse;
        }
	public void setIsAuthenticated(Boolean isAuthenticated){
            _isAuthenticated=isAuthenticated;
        }
	public void setIsConfidential(Boolean isConfidential){
            _isConfidential=isConfidential;
        }
	public void setIsTamperProof(Boolean isTamperProof){
            _isTamperProof=isTamperProof;
        }
}