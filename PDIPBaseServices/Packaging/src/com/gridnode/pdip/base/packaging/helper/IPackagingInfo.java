/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPackagingInfo
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 16-OCT-2002    Jagadeesh           Created.
 * Jan 17 2002    Goh Kan Mun         Modified - Added Backward Compatible type.
 * Mar 21 2002    Goh Kan Mun         Modified - Change Envelope Type name from
 *                                               BackwardCompatible to FileSplit.
 * Sep 25 2003    Jagadeesh           Modified - Added SOAP Packaging type.
 * Oct 29 2003    Guo Jianyu          Added AS2_ENVELOPE_TYPE
 */


package com.gridnode.pdip.base.packaging.helper;

import java.io.Serializable;

public interface IPackagingInfo extends Serializable
{

  public static final String DEFAULT_ENVELOPE_TYPE = "NONE";

  public static final String RNIF1_ENVELOPE_TYPE = "RNIF1";

  public static final String RNIF2_ENVELOPE_TYPE ="RNIF2";

	public static final String SOAP_ENVELOPE_TYPE = "SOAP";

  public static final String FILE_SPLIT_ENVELOPE_TYPE = "FILESPLIT";

  public static final String AS2_ENVELOPE_TYPE ="AS2";

}