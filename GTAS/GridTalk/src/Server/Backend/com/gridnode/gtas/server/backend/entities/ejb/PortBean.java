/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PortBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 17 2002    Koh Han Sing        Created
 * May 27 2003    Jagadeesh           Added : To support generating sequential no.
 */
package com.gridnode.gtas.server.backend.entities.ejb;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.gridnode.gtas.server.backend.model.Port;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;

/**
 * A PortBean provides persistency services for Port.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class PortBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7543778230109854644L;
	private static final String PADDED_DIGIT= "0";

  public String getEntityName()
  {
    return Port.ENTITY_NAME;
  }

  public String getNextSeqRunningNo()
  {
    Port port = (Port)_entity;
    String seqNo = getSeqRunningNo(port, port.getNextNumber().intValue());
    int nextNum = getNextNumber(port);
    port.setNextNumber(new Integer(nextNum));
//    int currentNo = getNextCurrentNo(port);
//    String seqNo =  getSeqRunningNo(port,currentNo);
//    currentNo+=1;
//    port.setCurrentNumber(new Integer(currentNo));
    return seqNo;
  }

  /**
   * Returns a Sequential Running No, given the startNo,currentNo, and rollover no,
   * from Port Entity.
   *
   * If Padding is Enabled, the Sequential No returened will be padded with leading
   * PADDING_DIGIT (i.e) 0.
   *
   * @param port - Port Entity
   * @return sequentialRunning No as String.
   */

  protected String getSeqRunningNo(Port port,int currentNo)
  {
    String seqRunningNo=null;

    if( port.isPadded().booleanValue() ) //If Pad with numberSequence.
    {
      int fixNoLength = port.getFixedNumberLen().intValue();
      seqRunningNo =  formatSeqRunningNo(currentNo,fixNoLength);
    }
    else
      seqRunningNo = Integer.toString(currentNo);
    return seqRunningNo;
  }

  /**
   * Formats the given no by padding with leading zeros to fixedno length.
   * @param currentNo - Current No to Pad.
   * @param fixNoLength - No of leading zeros.
   * @return String formatted with leading zeros.
   */

  private String formatSeqRunningNo(int currentNo,int fixNoLength)
  {
     DecimalFormat dcmlFormat =
        (DecimalFormat) NumberFormat.getInstance();
     StringBuffer pattern = new StringBuffer();

     for(int i=0;i<fixNoLength;i++)
       pattern.append(PADDED_DIGIT);
     dcmlFormat.applyPattern(pattern.toString());

     String formattedNo = dcmlFormat.format(currentNo);
     return formattedNo;
  }

  private int getNextNumber(Port port)
  {
    int rolloverNo = port.getRolloverNumber().intValue();
    int nextNum = port.getNextNumber().intValue();

    if (nextNum == rolloverNo ) //Check for rollOver.
    {
      nextNum = port.getStartNumber().intValue();
    }
    else
      nextNum++;

    return nextNum ; //return current no.
  }



}