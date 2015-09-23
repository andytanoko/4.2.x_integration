package com.gridnode.pdip.base.rnif.helper;

import com.gridnode.pdip.base.certificate.model.Certificate;

/**
 * <p>Title:  * This software is the proprietary information of GridNode Pte Ltd.
 * <p>Description: Peer Data Integration Platform
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: GridNode Pte Ltd</p>
 * @author unascribed
 * @version 1.0
 */

public class RNCertInfo {
  private Certificate _partnerSignCertificate;
  private Certificate _ownSignCertificate;
  private Certificate _partnerEncryptCertificate;
  private Certificate _ownEncryptCertificate;

  public RNCertInfo()
  {
  }
  public Certificate get_ownEncryptCertificate() {
    return _ownEncryptCertificate;
  }
  public void set_ownEncryptCertificate(Certificate _ownEncryptCertificate) {
    this._ownEncryptCertificate = _ownEncryptCertificate;
  }
  public Certificate get_ownSignCertificate() {
    return _ownSignCertificate;
  }
  public void set_ownSignCertificate(Certificate _ownSignCertificate) {
    this._ownSignCertificate = _ownSignCertificate;
  }
  public Certificate get_partnerEncryptCertificate() {
    return _partnerEncryptCertificate;
  }
  public void set_partnerEncryptCertificate(Certificate _partnerEncryptCertificate) {
    this._partnerEncryptCertificate = _partnerEncryptCertificate;
  }
  public Certificate get_partnerSignCertificate() {
    return _partnerSignCertificate;
  }
  public void set_partnerSignCertificate(Certificate _partnerSignCertificate) {
    this._partnerSignCertificate = _partnerSignCertificate;
  }
}