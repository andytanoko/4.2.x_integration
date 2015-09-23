package com.gridnode.pdip.base.security.mime.smime;
import java.util.Vector;

import com.gridnode.pdip.base.security.mime.SMimeFactory;
import com.gridnode.pdip.base.security.mime.smime.exceptions.GNSMimeException;

/**
 * <p>Title:  * This software is the proprietary information of GridNode Pte Ltd.
 * <p>Description: Peer Data Integration Platform
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: GridNode Pte Ltd</p>
 * @author unascribed
 * @version 1.0
 */

public interface ISMimeBasic
{
  public static final String ACTION_SIGN = "sign";
  public static final String ACTION_ENCRYPT = "encrypt";
  public static final String ACTION_COMPRESS = "compress";

  public SMimeFactory getFactory();

  public void       setPKCS7Encoding(String encoding);
  public void       setDefaultEncoding(String encoding);
  public void       setActionProperty(String action, int scope);
  public int        getActionProperty(String action);
  public void       setActions(Vector actions);
  public Vector     getActions();

  public boolean    hasAction(String action);
  public void       appendAction(String action);
  public void       removeAction(String action);
  public void       clearAllActions();

  public boolean     isSigned();
  public boolean     isEncryted();
  public boolean     isCompressed();

  public String     getDigestAlgorithm();
  public void       setDigestAlgorithm(String digestAlgorithm);
  public byte[]     getMessageDigest() throws GNSMimeException;
  public byte[]     getMessageDigest(byte[] content) throws GNSMimeException;
}