package com.gridnode.gtas.model.certificate;

import java.util.Hashtable;

import com.gridnode.gtas.model.user.IUserAccountState;

/**
 * This class provides the fieldIDs of the entities in the Certificate module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Jagadeesh
 *
 * @version 2.0
 * @since 2.0
 */

public class CertificateEntityFieldID
{
  private Hashtable _table;
  private static CertificateEntityFieldID _self = null;

  public CertificateEntityFieldID()
  {
    _table = new Hashtable();

    _table.put(ICertificate.ENTITY_NAME,
      new Number[]
      {
        ICertificate.NAME,
        ICertificate.ID,
        ICertificate.UID,
        ICertificate.SERIALNUM,
        ICertificate.ISSUERNAME,
        ICertificate.CERTIFICATE,
        ICertificate.PUBLICKEY,
        ICertificate.PRIVATEKEY,
        ICertificate.REVOKEID,
        ICertificate.IS_MASTER,
        ICertificate.IS_PARTNER,
        ICertificate.CATEGORY,
        ICertificate.IS_IN_KS,
        ICertificate.IS_IN_TS,
        ICertificate.RELATED_CERT_UID,
        ICertificate.START_DATE,
        ICertificate.END_DATE,
        ICertificate.IS_CA,
        ICertificate.REPLACEMENT_CERT_UID,
        ICertificate.CERTIFICATE_SWAPPING
      });
    
    _table.put(ICertificateSwapping.ENTITY_NAME,
      new Number[]
      {
        ICertificateSwapping.UID,
        ICertificateSwapping.SWAP_DATE,
        ICertificateSwapping.SWAP_TIME,
        ICertificateSwapping.ALARM_UID
      });
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new CertificateEntityFieldID();
    }
    return _self._table;
  }


}
