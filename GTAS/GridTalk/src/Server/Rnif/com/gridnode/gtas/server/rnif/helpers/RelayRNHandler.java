package com.gridnode.gtas.server.rnif.helpers;

/**
 * <p>Title: GTAS-RNIF Implementation</p>
 * <p>Description: GTAS
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: GridNode Pte Ltd</p>
 * @author not attributable
 * @version 1.0
 */

import com.gridnode.gtas.server.rnif.helpers.RNUnpackagingHandler.RNSecurityInfoFinder;
import com.gridnode.pdip.base.rnif.helper.*;
import com.gridnode.pdip.base.rnif.helper.RNDePackager_20;
import com.gridnode.pdip.base.rnif.helper.RNDePackager_11;
import com.gridnode.pdip.base.rnif.helper.RNPackager;
import com.gridnode.pdip.base.rnif.helper.RNDePackager;

import com.gridnode.pdip.base.rnif.helper.RNPackager_20;
import com.gridnode.pdip.base.rnif.helper.SecurityInfoFinder;
import com.gridnode.pdip.base.rnif.model.RNPackInfo;

import java.io.File;
import java.util.ArrayList;


public class RelayRNHandler
{

  private static final int UDOC_INDEX = 0;
  private static final int GDOC_INDEX = 1;

  private static final int PACK_INFO_INDEX = 0;
  private static final int PACK_UDOC_INDEX = 1;

  private static final int RNPACK_INFO_INDEX = 1;

  /**
   * Prepare files to send to GT2 via GM
   * 
   * @param packInfo
   * @param payloads Payloads to send: Element 0=udoc, Element 1=RNpackinfo, Element 2~=attachments
   * @return Array of files: Element 0=RBM, Element 1=RNpackinfo
   * @throws Exception
   */
  public static File[] prepareFilesToSend(RNPackInfo packInfo, File[] payloads) throws Exception
  {
    // should be just 1 file
    File[] packed = pack(packInfo, getPayloadsForPacking( payloads));

    File[] filesToSend = new File[2];

    for (int i=0; i<filesToSend.length; i++)
    {
      switch (i)
      {
        case UDOC_INDEX:
          filesToSend[UDOC_INDEX] = packed[0];
          break;
        case RNPACK_INFO_INDEX:
          filesToSend[GDOC_INDEX] = payloads[RNPACK_INFO_INDEX];
          break;
      }
    }

    return filesToSend;
  }


  private static File[] getPayloadsForPacking(File[] files)
  {
    // remove nulls and PackInfo
    ArrayList list = new ArrayList();
    for (int i=0; i<files.length; i++)
    {
      if (files[i] != null && i != GDOC_INDEX)
        list.add(files[i]);
    }
    list.add(PACK_INFO_INDEX, null);
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

  /**
   * Unpack the payloads from RBM
   *
   * @param payloads The received payloads.  Element 0=RBM, Element 1=RNpackinfo
   * @return Array of Array of files: Element (0,0): RNPackInfo file, (0,1): unpacked Udoc file, (0,2~):Attachments, (1,0):audit file
   * @throws Exception Error unpacking the payloads.
   */
  public static File[][] unpack(File[] payloads) throws Exception
  {
    SecurityInfoFinder securityInfoFinder= new RNSecurityInfoFinder();

    RNPackInfo packInfo = new RNPackInfo();
    Logger.debug("[RelayRNHandler.unpack()][B4 creating PackInfo]"+payloads[RNPACK_INFO_INDEX].getAbsolutePath());
    packInfo = (RNPackInfo) packInfo.deserialize(payloads[RNPACK_INFO_INDEX].getAbsolutePath());
    Logger.debug("[RelayRNHandler.unpack()][After creating PackInfo]");

    RNDePackager depackager = getDePackager(packInfo);
    depackager.setSecurityInfoFinder(securityInfoFinder);
    //at the moment just 1 file, attachments not supported yet
    Logger.debug("[RelayRNHandler.unpack()][B4 Calling Depackage to Unpackage]");
    File[][] res= depackager.unpackDocument(payloads[UDOC_INDEX], packInfo);
    Logger.debug("[RelayRNHandler.unpack()][After Calling Depackage to Unpackage]"+res[0][0].getAbsolutePath());

    return res;
  }

  private static RNPackager getPackager(RNPackInfo packInfo)
  {
    if ("RNIF2.0".equals(packInfo.getRnifVersion()))
      return new RNPackager_20();
    else
      return new RNPackager_11();
  }

  private static RNDePackager getDePackager(RNPackInfo packInfo)
  {
    if ("RNIF2.0".equals(packInfo.getRnifVersion()))
      return new RNDePackager_20();
    else
      return new RNDePackager_11();
  }

  public RelayRNHandler()
  {
  }

}