/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BackwardCompatibleRNHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 26 2004    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.rnif.helpers;

import com.gridnode.gtas.server.rnif.helpers.RNUnpackagingHandler.RNSecurityInfoFinder;
import com.gridnode.pdip.base.rnif.helper.*;
import com.gridnode.pdip.base.rnif.helper.RNDePackager_20;
import com.gridnode.pdip.base.rnif.helper.RNPackager;
import com.gridnode.pdip.base.rnif.helper.RNPackager_20;
import com.gridnode.pdip.base.rnif.helper.SecurityInfoFinder;
import com.gridnode.pdip.base.rnif.model.RNPackInfo;

import java.io.File;
import java.util.ArrayList;

/**
 * This helper class handles all required routines for
 * sending RNDoc to a GT 1.x partner.
 * 
 * @author Neo Sok Lay
 * @since GT 2.3 I1
 */
public class BackwardCompatibleRNHandler
{
  private static final int UDOC_INDEX = 0;
  private static final int GDOC_INDEX = 1;
  private static final int DUMMY_INDEX = 2;
  
  private static final int PACK_INFO_INDEX = 0;
  private static final int PACK_UDOC_INDEX = 1;
  
  /**
   * Prepare the files for sending
   * 
   * @param packInfo The RNPackInfo to use for packing the RBM
   * @param payloads Array of payload files. Element 0: Udocfile, 1: Gdocfile, 2: null (required), 3~: attachments (optional)
   * @return Array of file payloads to be sent. Element 0: Udocfile (RBM), 1:Gdocfile, 2:null.
   * @throws Exception Error while preparing the files.
   */
  public static File[] prepareFilesToSend(RNPackInfo packInfo, File[] payloads) throws Exception
  {
    // should be just 1 file
    File[] packed = pack(packInfo, getPayloadsForPacking(payloads));

    File[] filesToSend = new File[3];
     
    for (int i=0; i<filesToSend.length; i++)
    {
      switch (i)
      {
        case UDOC_INDEX: 
          filesToSend[UDOC_INDEX] = packed[0];
          break;
        case GDOC_INDEX:
          filesToSend[GDOC_INDEX] = payloads[GDOC_INDEX];
          break;
        case DUMMY_INDEX:
          filesToSend[DUMMY_INDEX] = null;
          break;    
      }
    }
    
    return filesToSend;
  }
  
  /**
   * Re-org the file payloads for packing into RBM
   * 
   * @param files The file payloads. Element 0: udoc file, 1: Gdoc file, 2: null, 3~: attachment files
   * @return Re-orged file payloads. Element 0: null (placeholder for packinfo file), 1: udoc file, 2~: attachment files.
   */
  private static File[] getPayloadsForPacking(File[] files)
  {
    // remove nulls and GdocFile (position 1 & 2)
    ArrayList list = new ArrayList();
    for (int i=0; i<files.length; i++)
    {
      if (files[i] != null && i != GDOC_INDEX)
        list.add(files[i]);  
    }
    
    list.add(PACK_INFO_INDEX, null); //placeholder for packinfo file (not required here)
    
    return (File[])list.toArray(new File[list.size()]);
  }
  
  /**
   * Pack the payloads into RBM
   * 
   * @param packInfo The RNPackInfo for packaging the payloads
   * @param payloads Element 0: not used, 1: udoc, 2~: attachments
   * @return Array of a single packed RBM file.
   * @throws Exception Error packaging the payloads
   */
  public static File[] pack(RNPackInfo packInfo, File[] payloads) throws Exception
  {
    SecurityInfoFinder securityInfoFinder= new RNSecurityInfoFinder();
    RNPackager packager = getPackager(packInfo);
    packager.setSecurityInfoFinder(securityInfoFinder);
    File[][] res= packager.packDoc(packInfo, payloads);
    RNUnpackagingHandler.setMessageDigest(securityInfoFinder.getPackInfo());
    
    return res[0];
  }
 
  private static RNPackager getPackager(RNPackInfo packInfo)
  {
    if ("RNIF2.0".equals(packInfo.getRnifVersion()))
      return new RNPackager_20();
    else
      return new RNPackager_11();
  }
  
  /**
   * Unpack the payloads from RBM
   * 
   * @param payloads The received payloads. Element 0: Udocfile (RBM), 1:Gdocfile, 2:null.
   * @return Array of Array of files: Element (0,0): RNPackInfo file, (0,1): unpacked Udoc file, (0,2~):Attachments, (1,0):audit file
   * @throws Exception Error unpacking the payloads.
   */
  public static File[][] unpack(File[] payloads) throws Exception
  {  
    SecurityInfoFinder securityInfoFinder= new RNSecurityInfoFinder();
    RNDePackager_20 depackager = new RNDePackager_20();
    depackager.setSecurityInfoFinder(securityInfoFinder);
    //at the moment just 1 file, attachments not supported yet
    File[][] res= depackager.unpackDocument(payloads[UDOC_INDEX], new RNPackInfo());
    
    return res;
  }
  
  
}
