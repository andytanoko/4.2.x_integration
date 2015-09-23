/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTCertificateEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2008-07-03     Yee Wah, Wong        #38   Created
 */
package com.gridnode.gtas.client.ctrl;


import com.gridnode.gtas.model.certificate.ICertificateSwapping;

import com.gridnode.gtas.client.GTClientException;

public interface IGTCertificateSwappingEntity extends IGTEntity {

	
	public static final Number UID             = ICertificateSwapping.UID;
	public static final Number SWAP_DATE       = ICertificateSwapping.SWAP_DATE;
	public static final Number SWAP_TIME       = ICertificateSwapping.SWAP_TIME;
	public static final Number ALARM_UID       = ICertificateSwapping.ALARM_UID;
	
	
}

