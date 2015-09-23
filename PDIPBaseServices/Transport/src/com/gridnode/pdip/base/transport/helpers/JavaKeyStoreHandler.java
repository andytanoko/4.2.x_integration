/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JavaKeyStoreHandler.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 31 March 2003  Qingsong            Initial creation for GTAS 2.0
 * 21 Sep 2005    Lim Soon Hsiung     Upgrade bouncy castle to 1.5 130,
 *   replace DERInputStream with ASN1InputStream
 * 28 Jul 2006    Tam Wei Xiang       Added in method to determine whether a cert
 *                                    is CA cert.  
 * 10 Aug 2006    Tam Wei Xiang       Added method to determine whether the parent cert
 *                                    of a particular cert existed in truststore. 
 * 12 July 2009   Tam Wei Xiang       #560: added getPKIXCertPathBuilder(...)                                  
 */

package com.gridnode.pdip.base.transport.helpers;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertPath;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBoolean;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;

interface CertificateGenerator
{
  public void reset();
  public void setSerialNo(long sn);
  public void setSubjectName(String subject);
  public void setCSRSubjectName(String csrsubject);
  public void setIssuerName(String issuer);
  public void setValidateDate(Date from, Date to);
  public X509Certificate generateCertificate(KeyPair signerkeypair, PublicKey pubkey) throws InvalidKeyException, SignatureException;
  public X509Certificate generateCertificate(KeyPair keypair) throws InvalidKeyException, SignatureException;
  public X509Certificate generateCertificate(KeyPair signerkeypair, byte[] csr) throws InvalidKeyException, SignatureException, IOException, NoSuchProviderException, NoSuchAlgorithmException;
  public byte[] generateCSR(KeyPair keypair) throws InvalidKeyException, SignatureException, NoSuchProviderException, NoSuchAlgorithmException, IOException;
}

public class JavaKeyStoreHandler
{
  final static public int CERT_STATE_VALID = 0;
  final static public int CERT_STATE_INVALID = -1;
  final static public int CERT_STATE_NOTVALIDYET = 1;
  final static public int CERT_STATE_EXPIRE = 2;

  final static public String KEYSTORE_TYPE_JKS = "JKS";
  final static public String KEYSTORE_TYPE_PKCS12 = "pkcs12";
  final static public String PEM_MARKER_CSR_BEGIN = "-----BEGIN CERTIFICATE REQUEST-----";
  final static public String PEM_MARKER_CSR_END = "-----END CERTIFICATE REQUEST-----";

  final static public String PEM_MARKER_CERTIFICATE_BEGIN = "-----BEGIN CERTIFICATE-----";
  final static public String PEM_MARKER_CERTIFICATE_END = "-----END CERTIFICATE-----";

  final static public String PEM_MARKER_KEYPAIR_BEGIN = "-----BEGIN RSA PRIVATE KEY-----";
  final static public String PEM_MARKER_KEYPAIR_END = "-----END RSA PRIVATE KEY-----";

  final static public String PEM_MARKER_PRIVATEKEY_BEGIN = "-----BEGIN PRIVATE KEY-----";
  final static public String PEM_MARKER_PRIVATEKEY_END = "-----END PRIVATE KEY-----";

  protected KeyStore ks = null;
  protected String   filename="", password = getTrustStorePassword("");
  protected String keystoretype = KEYSTORE_TYPE_JKS;
  private String provider;
  static private CertificateGenerator certEngine;
  static private Provider defprovider;
  static
  {
    InitExtUtils();
  }

  static public boolean emptyString(String str)
  {
    if(str == null || str.length() <= 0)
     return true;
    else
     return false;
  }

  static public JavaKeyStoreHandler getTrustStore() throws NoSuchAlgorithmException, IOException, CertificateException, FileNotFoundException, KeyStoreException, NoSuchProviderException
  {
    JavaKeyStoreHandler ts = new JavaKeyStoreHandler();
    ts.open(getTrustStoreName(""), getTrustStorePassword(""));
    return ts;
  }

  static public String getTrustStorePassword(String password)
  {
    if(emptyString(password))
      {
          return System.getProperties().getProperty("javax.net.ssl.trustStorePassword", "changeit");
      }
    else
      return password;
  }

  static public String getTrustStoreName(String filename)
  {
    if(emptyString(filename))
      return getDefaultTrustedStore();
    else
      return filename;
  }

  static public String getJava_home()
  {
    String javaHome = System.getProperty("java.home");
    return javaHome;
  }

  static public String getDefaultTrustedStore()
  {
    Properties prop = System.getProperties();
    String javaHome = getJava_home();
    javaHome += "/lib/security/cacerts";
    return prop.getProperty("javax.net.ssl.trustStore", javaHome);
  }

  static public InputStream getInputStreamFromBytes(byte[] data)
  {
    return new ByteArrayInputStream(data);
  }

  static public boolean writeBytes(String filename, byte[] data) throws FileNotFoundException, IOException
  {
    return writeBytes(new FileOutputStream(filename), data);
  }

  static public boolean writeBytes(OutputStream out, byte[] data) throws IOException
  {
    out.write(data);
    return true;
  }

  static public byte[] loadBytes(InputStream in) throws IOException
  {
    byte[] data = new byte[in.available()];
    in.read(data);
    return data;
  }

  static public byte[] loadBytes(String filename) throws FileNotFoundException, IOException
  {
    return loadBytes(new FileInputStream(filename));
  }

  static public void writeCertificate(String fileName, Certificate cert) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, CertificateException
  {
    writeCertificate(new FileOutputStream(fileName), cert);
  }

  static public void writeCertificate(OutputStream out, Certificate cert) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, CertificateException
  {
    out.write(writeCertificate(cert));
  }

  static public byte[] writeCertificate(Certificate cert) throws java.security.cert.CertificateException
  {
    return cert.getEncoded();
  }

  static public byte[] writePEMObject(byte[] data, String beginMarker, String endMarker)
  {
    BASE64Encoder base64 = new BASE64Encoder();
    return (beginMarker + "\r\n" + base64.encode(data) + "\r\n" + endMarker + "\r\n").getBytes();
  }

  static public byte[] loadPEMObject(byte[] data, String beginMarker, String endMarker) throws IOException
  {
    BASE64Decoder base64 = new BASE64Decoder();
    String strdata = new String(data);
    BufferedReader in = new BufferedReader(new StringReader(strdata));
    StringBuffer    buf = new StringBuffer();
    String line = in.readLine();
    if(line.indexOf(beginMarker) >= 0)
    {
        while ((line = in.readLine()) != null)
        {
            if (line.indexOf(endMarker) != -1)
            {
                break;
            }
            buf.append(line.trim());
        }
      return base64.decodeBuffer(buf.toString());
    }
    else
     throw new IOException("loadPEMObject: not valid PEM Object");
  }

  static public byte[] loadPEMCSR(byte[] pemcsr) throws IOException
  {
    return internalloadCSR(loadPEMObject(pemcsr, PEM_MARKER_CSR_BEGIN, PEM_MARKER_CSR_END));
  }

  static public byte[] writePEMCSR(byte[] csr) throws NoSuchAlgorithmException, InvalidKeySpecException
  {
    return  writePEMObject(csr, PEM_MARKER_CSR_BEGIN, PEM_MARKER_CSR_END);
  }

  static public byte[] writePEMPrivateKey(Key akey) throws NoSuchAlgorithmException, InvalidKeySpecException
  {
    return  writePEMObject(writePrivateKey(akey), PEM_MARKER_PRIVATEKEY_BEGIN, PEM_MARKER_PRIVATEKEY_END);
  }

  static public PrivateKey loadPEMPrivateKey(byte[] pemkey) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException
  {
    return interanlloadPrivateKey(loadPEMObject(pemkey, PEM_MARKER_PRIVATEKEY_BEGIN, PEM_MARKER_PRIVATEKEY_END));
  }

  static public KeyPair loadPEMKeyPair(byte[] KeyPair) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException
  {
    return interanlloadKeyPair(loadPEMObject(KeyPair, PEM_MARKER_KEYPAIR_BEGIN, PEM_MARKER_KEYPAIR_END));
  }

  static public X509Certificate loadPEMCertificate(byte[] pemcert) throws java.security.cert.CertificateException, IOException, NoSuchAlgorithmException
  {
    DerInputStream din = new DerInputStream(loadPEMObject(pemcert, PEM_MARKER_CERTIFICATE_BEGIN, PEM_MARKER_CERTIFICATE_END));
    return  internalloadCertificate(din.toByteArray());
  }

  static public byte[] writePEMCertificate(Certificate cert) throws java.security.cert.CertificateException, IOException, InvalidKeySpecException, NoSuchAlgorithmException
  {
    DerOutputStream dOut = new DerOutputStream();
    writeCertificate(dOut, cert);
    return  writePEMObject(dOut.toByteArray(), PEM_MARKER_CERTIFICATE_BEGIN, PEM_MARKER_CERTIFICATE_END);
  }

  static public X509Certificate loadCertificate(InputStream in) throws CertificateException, IOException, NoSuchAlgorithmException
  {
    return loadCertificate(loadBytes(in));
  }

  static public X509Certificate loadCertificate(String filename) throws FileNotFoundException, CertificateException, IOException, NoSuchAlgorithmException
  {
    return loadCertificate(new FileInputStream(filename));
  }

  static protected X509Certificate internalloadCertificate(byte[] certdata) throws CertificateException, IOException
  {
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    X509Certificate cert = (X509Certificate)cf.generateCertificate(getInputStreamFromBytes(certdata));
    return cert;
  }

  static public X509Certificate loadCertificate(byte[] certdata) throws CertificateException, IOException, NoSuchAlgorithmException
  {
    try
    {
      return loadPEMCertificate(certdata);
    }
    catch (IOException ex)
    {
      return internalloadCertificate(certdata);
    }
  }

  static public PrivateKey loadPrivateKey(String fileName) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException
  {
    return loadPrivateKey(new FileInputStream(fileName));
  }

  static public PrivateKey loadPrivateKey(InputStream in) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException
  {
    return loadPrivateKey(loadBytes(in));
  }

  static public PrivateKey loadPrivateKey(byte[] pkcs8Key) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException
  {
    try
    {
      return loadKeyPair(pkcs8Key).getPrivate();
    }
    catch (Exception ex)
    {
    }
    try
    {
      return loadPEMPrivateKey(pkcs8Key);
    }
    catch (IOException ex)
    {
      return interanlloadPrivateKey(pkcs8Key);
    }
  }


  static public KeyPair loadKeyPair(String fileName) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException
  {
    return loadKeyPair(new FileInputStream(fileName));
  }

  static public KeyPair loadKeyPair(InputStream in) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException
  {
    return loadKeyPair(loadBytes(in));
  }

  static public KeyPair loadKeyPair(byte[] KeyPair) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException
  {
    try
    {
      return loadPEMKeyPair(KeyPair);
    }
    catch (IOException ex)
    {
      return interanlloadKeyPair(KeyPair);
    }
  }

  static protected KeyPair interanlloadKeyPair(byte[] KeyPair) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException
  {
        KeySpec                 pubSpec, privSpec;
        ByteArrayInputStream    bIn = new ByteArrayInputStream(KeyPair);
        ASN1InputStream          dIn = new ASN1InputStream(bIn);
        //DerInputStream          din = new DerInputStream(KeyPair);

        ASN1Sequence            seq = (ASN1Sequence)dIn.readObject();
        DERInteger              v = (DERInteger)seq.getObjectAt(0);
        DERInteger              mod = (DERInteger)seq.getObjectAt(1);
        DERInteger              pubExp = (DERInteger)seq.getObjectAt(2);
        DERInteger              privExp = (DERInteger)seq.getObjectAt(3);
        DERInteger              p1 = (DERInteger)seq.getObjectAt(4);
        DERInteger              p2 = (DERInteger)seq.getObjectAt(5);
        DERInteger              exp1 = (DERInteger)seq.getObjectAt(6);
        DERInteger              exp2 = (DERInteger)seq.getObjectAt(7);
        DERInteger              crtCoef = (DERInteger)seq.getObjectAt(8);

        /*
        BigInt              v = din.getDerValue().getInteger();
        BigInt              mod = din.getInteger();
        BigInt              pubExp = din.getInteger();
        BigInt              privExp = din.getInteger();
        BigInt              p1 = din.getInteger();
        BigInt              p2 = din.getInteger();
        BigInt              exp1 = din.getInteger();
        BigInt              exp2 = din.getInteger();
        BigInt              crtCoef = din.getInteger();

        pubSpec = new RSAPublicKeySpec(mod.toBigInteger(), pubExp.toBigInteger());
        privSpec = new RSAPrivateCrtKeySpec(
                mod.toBigInteger(), pubExp.toBigInteger(), privExp.toBigInteger(),
                p1.toBigInteger(), p2.toBigInteger(),
                exp1.toBigInteger(), exp2.toBigInteger(),
                crtCoef.toBigInteger());
*/
        pubSpec = new RSAPublicKeySpec(mod.getValue(), pubExp.getValue());
        privSpec = new RSAPrivateCrtKeySpec(
                mod.getValue(), pubExp.getValue(), privExp.getValue(),
                p1.getValue(), p2.getValue(),
                exp1.getValue(), exp2.getValue(),
                crtCoef.getValue());
        KeyFactory  fact = KeyFactory.getInstance("RSA");
        return new KeyPair(fact.generatePublic(pubSpec),fact.generatePrivate(privSpec));
  }

  static protected PrivateKey interanlloadPrivateKey(byte[] pkcs8Key) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException
  {
    PKCS8EncodedKeySpec pkcs8spec = new PKCS8EncodedKeySpec(pkcs8Key);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    return keyFactory.generatePrivate(pkcs8spec);
  }

  static public void writePrivateKey(String fileName, Key akey) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException
  {
    writePrivateKey(new FileOutputStream(fileName), akey);
  }

  static public void writePrivateKey(OutputStream out, Key akey) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException
  {
    out.write(writePrivateKey(akey));
  }

  static public byte[] writePrivateKey(Key akey) throws NoSuchAlgorithmException, InvalidKeySpecException
  {
    return akey.getEncoded();
  }

  public boolean extratCertificateFromkeyStore(String certFile, String alias) throws NoSuchAlgorithmException, IOException, CertificateException, FileNotFoundException, KeyStoreException, InvalidKeySpecException, NoSuchProviderException
  {
      if(containsAlias(alias))
      {
        writeCertificate(certFile, getCert(alias));
        return true;
      }
      else
        return false;
  }

  public boolean deleteAliasFromkeyStore(String alias)throws NoSuchAlgorithmException, IOException, CertificateException, FileNotFoundException, KeyStoreException, NoSuchProviderException
  {
      if(!containsAlias(alias))
        return false;
      else
      {
        delete(alias);
        return true;
      }
  }

  public boolean renameAlias(String alias, String newalias)throws NoSuchAlgorithmException, IOException, CertificateException, FileNotFoundException, KeyStoreException, NoSuchProviderException, UnrecoverableKeyException
  {
      if(!containsAlias(alias) || containsAlias(newalias))
        return false;
      else
      {
        PrivateKey key = getKey(alias);
        Certificate[] certs = getCertChain(alias);
        delete(alias);
        insert(newalias, key, certs);
        return true;
      }
  }

  public boolean deleteCertificateFromkeyStore(String certFile)throws NoSuchAlgorithmException, IOException, CertificateException, FileNotFoundException, KeyStoreException, NoSuchProviderException
  {
      String alias = getAlias(loadCertificate(certFile));
      if(emptyString(alias))
        return false;
      else
      {
        delete(alias);
        return true;
      }
  }

  static public KeyPair genKeyPair(int len) throws NoSuchAlgorithmException, NoSuchProviderException
  {
    return genKeyPair("RSA", len);
  }

  static public KeyPair genKeyPair(String algorithm, int len) throws NoSuchAlgorithmException, NoSuchProviderException
  {
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
    keyGen.initialize(len, new SecureRandom());
    return keyGen.genKeyPair();
  }

  static public SecretKey genKey(String algorithm, int len) throws NoSuchAlgorithmException, NoSuchProviderException
  {
    KeyGenerator keygen = KeyGenerator.getInstance(algorithm);
    keygen.init(len);
    return keygen.generateKey();
  }

  public byte[] getCSR(String alias) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, SignatureException, InvalidKeyException, IOException, NoSuchProviderException
  {
    if(getCertEngine() == null)
      return null;
    X509Certificate cert = getCert(alias);
    getCertEngine().setCSRSubjectName(cert.getSubjectDN().getName());
    byte[] csr = getCertEngine().generateCSR(getKeyPair(alias));
    getCertEngine().reset();
    return csr;
  }

  static public byte[] loadCSR(byte[] csrdata) throws IOException
  {
    try
    {
      return loadPEMCSR(csrdata);
    }
    catch (IOException ex)
    {
      return internalloadCSR(csrdata);
    }
  }

  static public byte[] loadCSR(InputStream in) throws IOException
  {
    return loadCSR(loadBytes(in));
  }

  static public byte[] loadCSR(String filename) throws IOException, FileNotFoundException
  {
    return loadCSR(new FileInputStream(filename));
  }

  static protected byte[] internalloadCSR(byte[] csrdata) throws IOException, FileNotFoundException
  {
    return csrdata;
  }

  public boolean writeCSR(String alias, String CSRFilename) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, SignatureException, InvalidKeyException, IOException, NoSuchProviderException
  {
    return writeBytes(CSRFilename,getCSR(alias));
  }

  public String genSelfSignCertificate(String alias, String issuer, long sn, Date from, Date to) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, SignatureException, InvalidKeyException, IOException, NoSuchProviderException
  {
    if(getCertEngine() == null)
      return null;
    KeyPair kp = genKeyPair(1024);
    getCertEngine().setIssuerName(issuer);
    getCertEngine().setSerialNo(sn);
    getCertEngine().setValidateDate(from, to);
    return insert(alias, getCertEngine().generateCertificate(kp), kp.getPrivate());
  }

  public X509Certificate genSignCertificate(String caalias, byte[] csr) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, SignatureException, InvalidKeyException, IOException, NoSuchProviderException, CertificateException
  {
    if(getCertEngine() == null)
      return null;
    X509Certificate cert = getCert(caalias);
    getCertEngine().setIssuerName(cert.getSubjectDN().getName());
    return getCertEngine().generateCertificate(getKeyPair(caalias), csr);
  }

  public KeyPair getKeyPair(String alias) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException
  {
    return new KeyPair(getCert(alias).getPublicKey(), (PrivateKey)getKey(alias));
  }
  
  //TWX this method only detect the top root CA (self-signed cert).
  //    Intermediate CA cannot be determined.
  static public boolean isCACertificate(Certificate cert)
  {
    X509Certificate X509Cert = (X509Certificate)cert;
    return X509Cert.getIssuerDN().equals(X509Cert.getSubjectDN());
  }
  
  //TWX 28072006 Original coder : Sumedh
  //Can determine top root CA cert, intermediate CA cert
  static public boolean isCACert(Certificate cert)
  	throws IOException
  {
  	X509Certificate X509Cert = (X509Certificate)cert;
  	//get basicConstraint
  	byte[] array =  X509Cert.getExtensionValue("2.5.29.19");
  	if(array == null)
  	{
  		//CA cert doesn't have to have this value. End entity cert must have.
  		return true;
  	}
  	
  	ASN1InputStream mainStream = new ASN1InputStream(array);		
  	DERObject firstDERo = mainStream.readObject();		
  	byte[] firstBytes = ((DEROctetString) firstDERo).getOctets();		
  	ASN1InputStream firstByteStream = new ASN1InputStream(firstBytes);		
  	DERObject firstInnerDERo = firstByteStream.readObject();		
  	DERSequence sequence = (DERSequence) firstInnerDERo;
  	
  	if (sequence.size() == 0) 
  	{			
  		/*			 * In rfc 2459, BasicConstraints ::= SEQUENCE { cA BOOLEAN DEFAULT
  		 * 			 * FALSE, pathLenConstraint INTEGER (0..MAX) OPTIONAL }
  		 * 			 * 			 * By DER encoding, if field value is same as deafault value, the
  		 * 			 * value is NOT included in DER encoding.
  		 * 			 */			
  		return false;		
  	} 
  	else 
  	{			
  		/*			 * By above document, derBoolean must be true if it is present.
  		 * 			 * Anyway I just return derBoolean.isTrue().			 */
  		DERBoolean derBoolean = (DERBoolean) sequence.getObjectAt(0);
  		return derBoolean.isTrue();		
  	}
  }
  
  public String getUniqueAlias(String alias, Certificate cert) throws KeyStoreException, IOException
  {
      if(!emptyString(alias))
      {
        String oalias = getAlias(cert);
        if(!emptyString(oalias))
          return oalias;
        else
          return alias;
      }
      else
        alias = getAlias(cert);
      if(emptyString(alias))
      {
        try
        {
           X509Certificate X509Cert = (X509Certificate)cert;
           String newalias = X509Cert.getSubjectDN().getName();
           if(newalias != null)
           {
             if(newalias.indexOf("O=") >= 0)
              {
               newalias = newalias.substring(newalias.indexOf("O=") + 2);
               if(newalias.indexOf(",") >= 0)
                newalias = newalias.substring(0,newalias.indexOf(","));
              }
           }
           if(newalias.charAt(0) == '\"')
                newalias = newalias.substring(1);
           if(newalias.indexOf(',') > 0)
            newalias = newalias.substring(0, newalias.indexOf(','));
           if(newalias.indexOf(' ') > 0)
            newalias = newalias.substring(0, newalias.indexOf(' '));
           if(newalias.indexOf('.') > 0)
            newalias = newalias.substring(0, newalias.indexOf('.'));
          alias = getUniqueAlias(newalias);
        }
        catch (Throwable ex)
        {
          alias = getUniqueAlias("newgntool1");
        }
      }
      return alias;
  }

  public String getUniqueAlias(Certificate cert) throws KeyStoreException, IOException
  {
    return getUniqueAlias(null,cert);
  }

  public String getUniqueAlias(String alias) throws KeyStoreException
  {
    if(!containsAlias(alias))
      return alias;
    int i = 1;
    while(containsAlias(alias + i))
          i++;
    return alias + i;
  }

  static public String Exception2String(Throwable e)
  {
      ByteArrayOutputStream detailedError = new ByteArrayOutputStream();
      PrintStream outS = new PrintStream(detailedError);
      if(e != null)
        e.printStackTrace(outS);
      outS.flush();
      outS.close();
      return new String(detailedError.toByteArray());
  }

  static public String getCertStateString(int status)
  {
     if(status == CERT_STATE_EXPIRE)
        return "expire";
     else if(status == CERT_STATE_VALID)
        return "valid";
     else if(status == CERT_STATE_NOTVALIDYET)
        return "not valid yet";
     else
        return "Invalid";
  }

  static public String certToString(X509Certificate  cert)
  {
     StringBuffer buffer = new StringBuffer();
     buffer.append("    Issuer [" + cert.getIssuerDN() + "] \r\n");
     buffer.append("    Subject[" + cert.getSubjectDN() + "] \r\n");
     buffer.append("    Status: " + getCertStateString(getCertState(cert)) +" From: "+ cert.getNotBefore() + " To: " + cert.getNotAfter() + "\r\n");
     return buffer.toString();
  }

  public String aliasToString(String alias)
  {
    StringBuffer buffer = new StringBuffer();
    try
    {
     X509Certificate  cert = getCert(alias);
     buffer.append("  [" + alias + "] S/N[" + cert.getSerialNumber() + "] key[" + hasPrivateKey(alias)+ "] CA["+ isCACertificate(cert) +"]\r\n");
     buffer.append(certToString(cert));
     if(hasPrivateKey(alias))
     {
        try
        {
           PrivateKey akey = (PrivateKey)getKey(alias);
           buffer.append("    Privatekey " +  akey.getAlgorithm() +"\r\n");
        }
        catch (UnrecoverableKeyException ex)
        {
          buffer.append("    Privatekey[load fail]\r\n");
        }
        Certificate[]  certs = getCertChain(alias);
        if(certs.length > 1)
        {
          buffer.append("    Certchain has more than one certificates\r\n");
          for(int i = 0; i < certs.length;i++)
          {
            if(!cert.equals(certs[i]))
            {
              buffer.append(certToString((X509Certificate)certs[i]));
            }
          }
        }
     }
    }
    catch (Exception ex)
    {
      buffer.append("aliasToString error for " +alias + " :"  + Exception2String(ex));
    }
    return buffer.toString();
  }

  public String aliasToString(Vector certs)
  {
    StringBuffer buffer = new StringBuffer();
    for(int i = 0; i < certs.size(); i++)
    {
      buffer.append(aliasToString((String)certs.get(i)) + "\r\n");
    }
    return buffer.toString();
  }

  public String toString()
  {
    StringBuffer buffer = new StringBuffer();

    Vector v1 = getValidCerts();
    Vector v2 = getNotValidYetCerts();
    Vector v3 = getExpireCerts();
    Vector v4 = getInvalidCerts();

    buffer.append("keystore info for [" + filename + "] passwd[" + password + "]\r\n");
    buffer.append("  valid["+ v1.size() + "] expire["+ v3.size() + "] notyet[" + v2.size() +"]\r\n");
    buffer.append(aliasToString(v1) + "\r\n");
    buffer.append(aliasToString(v2) + "\r\n");
    buffer.append(aliasToString(v3) + "\r\n");
    buffer.append(aliasToString(v4) + "\r\n");
    return buffer.toString();
  }

  static public boolean exportJavaKeyStore(String keystorefilename, String keystoreoldpassword, String keystorepassword, String entryname, X509Certificate cert, Certificate[] certchain, PrivateKey key, String password, boolean overwrite) throws KeyStoreException, FileNotFoundException, CertificateException, java.security.cert.CertificateException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, UnrecoverableKeyException, NoSuchProviderException
  {
    JavaKeyStoreHandler jh = new JavaKeyStoreHandler();
    jh.open(password);
    String  insertedEntry = jh.insert(entryname, cert, key, password, certchain);
    if(overwrite)
        jh.write(keystorefilename, keystorepassword);
    else
      {
        JavaKeyStoreHandler jh1 = new JavaKeyStoreHandler();
        jh1.open(keystorefilename, keystoreoldpassword);
        jh1.append(jh);
        jh1.write(keystorefilename, keystorepassword);
      }
    return !emptyString(insertedEntry);
  }


  public JavaKeyStoreHandler()
  {
  }

  public boolean open() throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, NoSuchProviderException
  {
    return open(getFilename(), getPassword());
  }

  public boolean open(String password) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, NoSuchProviderException
  {
    return open(getFilename(), password);
  }

  public boolean open(byte[] data) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, NoSuchProviderException
  {
    return open(data, getPassword());
  }

  public boolean open(String filename, String password) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, NoSuchProviderException
  {
    setFilename(filename);
    if(!emptyString(getFilename()))
    {
      try
      {
        return open(new FileInputStream(getFilename()), password);
      }
      catch (FileNotFoundException ex)
      {
        return openFromStream(null, password);
      }
    }
    else
      return openFromStream(null, password);
  }

  public boolean open(byte[] data, String password) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, NoSuchProviderException
  {
    ByteArrayInputStream in = new ByteArrayInputStream(data);
    return open(in, password);
  }

  public boolean open(InputStream in, String password) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, NoSuchProviderException
  {
    return openFromStream(in, password);
  }

  protected boolean openFromStream(InputStream in, String password) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, NoSuchProviderException
  {
    if(password != null)
      setPassword(password);
    try
    {
      ks = init(in, getPassword(), getKeystoretype(), getProvider());
    }
    catch (IOException ex)
    {
        ks = init(in, getPassword(), getKeystoretype(), null);
    }
    return true;
  }

  static protected  KeyStore init(InputStream in, String password, String keystoretype, String provider) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, NoSuchProviderException
  {
      KeyStore ks;
      if(provider != null)
        ks = KeyStore.getInstance(keystoretype, provider);
      else
        ks = KeyStore.getInstance(keystoretype);

      if(!emptyString(password))
        ks.load(in, password.toCharArray());
      else
        ks.load(in, null);
      return ks;
  }

  public boolean containsAlias(String alias) throws KeyStoreException
  {
    return ks.containsAlias(alias);
  }

  public boolean append(JavaKeyStoreHandler jks) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, IOException
  {
    Vector alias = jks.getAlias();
    for(int i =0; i< alias.size(); i++)
    {
      String a = (String)alias.get(i);
      if(jks.hasPrivateKey(a))
          insert(a, null, jks.getKey(a), jks.getCertChain(a));
      else
          insert(a,jks.getCert(a));
    }
    return true;
  }

  public Vector getAlias()
  {
    Vector aliasList = new Vector();
    try
    {
    for(Enumeration e = ks.aliases(); e.hasMoreElements() ;)
    {
      String alias = (String)e.nextElement();
      aliasList.add(alias);
    }
    }
    catch (Exception ex)
    {
    }
    return aliasList;
  }

  public String getAlias(Certificate cert) throws KeyStoreException
  {
    return ks.getCertificateAlias(cert);
  }

  public boolean write() throws KeyStoreException, FileNotFoundException, CertificateException, IOException, NoSuchAlgorithmException
  {
    return write(getFilename());
  }

  public boolean write(String filename) throws KeyStoreException, FileNotFoundException, CertificateException, IOException, NoSuchAlgorithmException
  {
    setFilename(filename);
    return write(new FileOutputStream(getFilename()), getPassword());
  }

  public byte[] writeToByteArray() throws KeyStoreException, FileNotFoundException, CertificateException, IOException, NoSuchAlgorithmException
  {
    return writeToByteArray(getPassword());
  }

  public byte[] writeToByteArray(String password) throws KeyStoreException, FileNotFoundException, CertificateException, IOException, NoSuchAlgorithmException
  {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    if(write(out, password))
      return out.toByteArray();
    else
      return null;
  }

  public boolean write(OutputStream out) throws KeyStoreException, FileNotFoundException, CertificateException, IOException, NoSuchAlgorithmException
  {
    return write(out, password);
  }

  public boolean write(OutputStream out, String password) throws KeyStoreException, FileNotFoundException, CertificateException, IOException, NoSuchAlgorithmException
  {
    return writeToStream(out, password);
  }

  protected boolean writeToStream(OutputStream out, String password) throws KeyStoreException, FileNotFoundException, CertificateException, IOException, NoSuchAlgorithmException
  {
    if(password != null)
      setPassword(password);
    if(!emptyString(getPassword()))
      ks.store(out, getPassword().toCharArray());
    else
      ks.store(out, null);
    return true;
  }


  public boolean write(String filename, String password) throws KeyStoreException, FileNotFoundException, CertificateException, IOException, NoSuchAlgorithmException
  {
    setFilename(filename);
    return write(new FileOutputStream(filename), password);
  }

  public boolean delete(String alias) throws KeyStoreException
  {
      if(!ks.containsAlias(alias))
        return false;
      if(emptyString(alias))
        return false;
      ks.deleteEntry(alias);
      return true;
  }

  public Vector getCerts(int state)
  {
    Vector certs = new  Vector();
    Vector alias = getAlias();
    for(int i = 0; i< alias.size(); i++)
    {
      int astate = CERT_STATE_VALID;
      try
      {
        X509Certificate cert = getCert((String)alias.get(i));
        astate = getCertState(cert);
      }
      catch (Exception ex)
      {
        astate = CERT_STATE_INVALID;
      }
      if(astate == state)
        certs.add((String)alias.get(i));
    }
    return certs;
  }

  public Vector getValidCerts()
  {
      return getCerts(CERT_STATE_VALID);
  }

  public Vector getNotValidYetCerts()
  {
      return getCerts(CERT_STATE_NOTVALIDYET);
  }

  public Vector getExpireCerts()
  {
      return getCerts(CERT_STATE_EXPIRE);
  }

  public Vector getInvalidCerts()
  {
      return getCerts(CERT_STATE_INVALID);
  }

  public Vector getCerts() throws KeyStoreException
  {
    Vector certs = new  Vector();
    Vector alias = getAlias();
    for(int i = 0; i< alias.size(); i++)
    {
      certs.add(getCert((String)alias.get(i)));
    }
    return certs;
  }

  public X509Certificate getCert(String alias) throws KeyStoreException
  {
    if(ks.containsAlias(alias))
      return (X509Certificate)ks.getCertificate(alias);
    else
      return null;
  }

  public Certificate[] getCertChain(String alias) throws KeyStoreException
  {
    if(ks.containsAlias(alias))
      return (Certificate[])ks.getCertificateChain(alias);
    else
      return null;
  }

  public int size() throws KeyStoreException
  {
    return ks.size();
  }

  public PrivateKey getKey(String alias, String password) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException
  {
    return (PrivateKey)ks.getKey(alias, password.toCharArray());
  }

  public PrivateKey getKey(String alias) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException
  {
    return getKey(alias, password);
  }

  public boolean hasPrivateKey(String alias) throws KeyStoreException
  {
    return ks.isKeyEntry(alias);
  }

  static public boolean isValidCert(Certificate cert)
  {
    return getCertState(cert) == CERT_STATE_VALID;
  }

  public boolean isValidCert(String alias)
  {
    return getCertState(alias) == CERT_STATE_VALID;
  }

  static public int getCertState(Certificate cert)
  {
    try
    {
      ((X509Certificate)cert).checkValidity();
      return CERT_STATE_VALID;
    }
    catch (CertificateNotYetValidException ex)
    {
      return CERT_STATE_NOTVALIDYET;
    }
    catch (CertificateExpiredException ex1)
    {
      return CERT_STATE_EXPIRE;
    }
  }

  public int getCertState(String alias)
  {
    try
    {
      return getCertState(getCert(alias));
    }
    catch (KeyStoreException ex2)
    {
      return CERT_STATE_INVALID;
    }
  }

   public String insert(Certificate cert) throws KeyStoreException, IOException
   {
      return insert(null, cert, null, null, null);
   }

   public String insert(String alias, Certificate cert) throws KeyStoreException, IOException
   {
      return insert(alias, cert, null, null, null);
   }

   public String insert(Certificate cert, Key key) throws KeyStoreException, IOException
   {
      return insert(null, cert, key, null, null);
   }

   public String insert(Certificate cert, Key key, String password) throws KeyStoreException, IOException
   {
      return insert(null, cert, key, password, null);
   }

   public String insert(String alias, Certificate cert, Key key) throws KeyStoreException, IOException
   {
      return insert(alias, cert, key, null, null);
   }

   public String insert(String alias, Certificate cert, Key key, String password) throws KeyStoreException, IOException
   {
      return insert(alias, cert, key, password, null);
   }

   public String insert(Certificate cert, Key key, Certificate[] certchain) throws KeyStoreException, IOException
   {
      return insert(null, cert, key, null, certchain);
   }

   public String insert(Key key, Certificate[] certchain) throws KeyStoreException, IOException
   {
      return insert(null, null , key, null, certchain);
   }

   public String insert(String alias, Key key, Certificate[] certchain) throws KeyStoreException, IOException
   {
      return insert(alias, null , key, null, certchain);
   }

   public String insert(String alias, Certificate cert, Key key, Certificate[] certchain) throws KeyStoreException, IOException
   {
      return insert(alias, cert, key, null, certchain);
   }

   public String insert(Certificate cert, Key key, String password, Certificate[] certchain) throws KeyStoreException, IOException
   {
      return insert(null, cert, key, password, certchain);
   }

   public String insert(String alias, Certificate cert, Key key, String password, Certificate[] certchain) throws KeyStoreException, IOException
   {
      if(key == null)
        {
            String oldalias = getAlias(cert);
            if(!emptyString(oldalias))
              return oldalias;
            alias = getUniqueAlias(alias, cert);
            ks.setCertificateEntry(alias, cert);
        }
      else
      {
        if(certchain == null || certchain.length <= 0)
          certchain = new Certificate[]{cert};
        if(password != null)
          setPassword(password);
        alias = getUniqueAlias(alias, (Certificate)certchain[0]);
        ks.setKeyEntry(alias, key, getPassword().toCharArray(), certchain);
      }
      return alias;
   }

  public static void main(String[] args) throws Exception
  {
  /*
    JavaKeyStoreHandler javaKeyStoreHandler1 = new JavaKeyStoreHandler();
    javaKeyStoreHandler1.open();

    PKCS12Reader p12reader = new PKCS12Reader("C:\\TomcatRN\\webapps\\rosettanet\\repositoryV2.0\\tpa\\securitydata\\keys\\encryptkey-123456789.p12", "mysecret".toCharArray());
    p12reader.read();

    javaKeyStoreHandler1.insertCert(JavaKeyStoreHelper.convertCertificate(p12reader.getCertificate()));
    byte[] data = javaKeyStoreHandler1.writeToByteArray();
    System.out.println("" + new String(data));
    */
    //Class.forName("com.gridnode.pdip.base.transport.helpers.JavaKeyStoreHandler");
    System.out.println(loadPrivateKey("D:\\bak\\Apache_2.0.44-OpenSSL_0.9.7-Win32-mod_jk-2.0.42-GridTalk1.x_2.x\\conf\\ssl\\server.key").getAlgorithm());
  }

  public void setFilename(String filename) {
    if(filename == null)
      filename = "";
    this.filename = filename;
  }
  public String getFilename() {
    return filename;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    if(password == null)
      password = "";
    this.password = password;
  }
  public KeyStore getKs() {
    return ks;
  }
  public void setKs(KeyStore ks) {
    this.ks = ks;
  }
  public void setKeystoretype(String keystoretype)
  {
    this.keystoretype = keystoretype;
  }

  public String getKeystoretype()
  {
    return keystoretype;
  }

  static boolean InitExtUtils()
  {
    try
    {
      HttpMessageContext context = HttpMessageContext.getInstance();
      String pp = context.getJava_home()+ "/" + "lib/ext/bcprov-jdk14-118.jar";
      if(!new File(pp).exists() && new File("bcprov-jdk14-118.jar").exists())
      {
          FileOutputStream out = new FileOutputStream(pp);
          FileInputStream in = new FileInputStream("bcprov-jdk14-118.jar");
          int length = in.available();
          byte[] buffer = new byte[length];
          in.read(buffer);
          out.write(buffer);
          in.close();
          in.available();
          out.close();
          out = null;
      }
      JavaKeyStoreHandler ha = new JavaKeyStoreHandler();
      ha.setPKCS12Keystore();
      JavaKeyStoreHandler.setCertEngineFromClassName(ha.getClass().getPackage().getName() + ".BCCertificateGenerator");
      return true;
    }
    catch (Exception ex)
    {
       return false;
    }

  }

  public void setPKCS12Keystore() throws ClassNotFoundException, IllegalAccessException,InstantiationException
  {
    setKeystoretype(KEYSTORE_TYPE_PKCS12);
    try
    {
      if(defprovider != null)
        setProvider(defprovider.getName());
      else
        setProviderFromClassName("org.bouncycastle.jce.provider.BouncyCastleProvider");
    }
    catch (IllegalAccessException ex)
    {
      try
      {
        setProviderFromClassName("com.sun.crypto.provider.SunJCE");
      }
      catch (Exception ex1)
      {
        setProvider((String)null);
      }
    }

  }

  public void setProviderFromClassName(String providerClassName) throws ClassNotFoundException, IllegalAccessException,InstantiationException
  {
    Class aClass = Class.forName(providerClassName);
    Object obj = aClass.newInstance();
    setProvider((Provider)obj);
  }

  public void setProvider(Provider provider)
  {
    defprovider = provider;
    Security.addProvider(provider);
    setProvider(provider.getName());
  }

  public void setProvider(String provider)
  {
    this.provider = provider;
  }

  public String getProvider()
  {
    return provider;
  }

  static public CertificateGenerator getCertEngine()
  {
    return certEngine;
  }

  static public void setCertEngine(CertificateGenerator certEngine)
  {
    JavaKeyStoreHandler.certEngine = certEngine;
  }

  static public void setCertEngineFromClassName(String certEngineClassName) throws ClassNotFoundException, IllegalAccessException,InstantiationException
  {
    Class aClass = Class.forName(certEngineClassName);
    Object obj = aClass.newInstance();
    setCertEngine((CertificateGenerator)obj);
  }

  static public Provider getDefProvider()
  {
    Provider[] pro =  Security.getProviders();
    if(defprovider != null)
      return defprovider;
    else
      return pro[0];
  }
  
  /**
   * TWX 10082006 Code contribute by Sumedh, refactor by WX.
   * Determine whether the direct Parent cert of a particular cert exits in trustStore
   * @param certInByte
   * @return
   * @throws Exception
   */
  static public boolean isParentCACertsInTrustStore(byte[] certInByte)
  	throws Exception
  {
  	//load by to Java X509Cert
  	X509Certificate targetCert = loadCertificate(certInByte);
  	
  	//TWX: A self sign cert, it can be allowed to export to truststore
  	if(isCACertificate(targetCert))
  	{
  		return true;
  	}
  	
  	//Get keystore
  	FileInputStream in = new FileInputStream(getTrustStoreName(("")));
  	KeyStore ks = init(in,getTrustStorePassword(""), KEYSTORE_TYPE_JKS, null);
  	
  	//All cert from keystore
  	Vector<Certificate> certVector = getCertificateFromKS(ks);
  	
  	//build cert path given the  import cert
  	return isCertPathBuilded(certVector, ks, targetCert);
  }
  
  /**
   * TWX 12072009 #560
   */
  public static PKIXCertPathBuilderResult getPKIXCertPathBuilder(KeyStore ks, X509CertSelector signerConstraint, boolean isEnableRevocationCheck,
                                                                 X509Certificate targetCert, String provider)
    throws Exception
  {
    //  All cert from keystore
    // TODO: Will it be in-efficient?? Shall we implement something like SecurityDB which cache all the cert?? see SecurityDB.getCertificates()
    //       Whenever we import a cert, it will be auto cached in SecurityDB
    
    Vector<Certificate> certVector = getCertificateFromKS(ks);
    Set<TrustAnchor> set = createTrustAnchorSet(certVector); //The target cert related CA certs need to be in trust store.

    PKIXBuilderParameters params = new PKIXBuilderParameters(set, signerConstraint);
    
    //CRLs setting
    params.setRevocationEnabled(isEnableRevocationCheck);
    
    //add target cert (potential cert using for verification) to certVector
    certVector.add(targetCert);
    
    CollectionCertStoreParameters ccsp = new CollectionCertStoreParameters(certVector);   
    CertStore store = CertStore.getInstance("Collection", ccsp, provider);
    
    params.addCertStore(store);
    
    CertPathBuilder cpb = CertPathBuilder.getInstance("PKIX", provider);
    PKIXCertPathBuilderResult result = (PKIXCertPathBuilderResult) cpb.build(params);
    
    return result;
  }
  
  /**
   * Determine whether we can build the cert path for targetCert.
   * @param certVector
   * @param ks
   * @param targetCert
   * @return
   * @throws Exception
   */
  private static boolean isCertPathBuilded(Vector<Certificate> certVector, KeyStore ks, Certificate targetCert)
  	throws Exception
  {
  	X509CertSelector targetConstraints = new X509CertSelector();
  	targetConstraints.setCertificate((X509Certificate)targetCert);
  	
  	Set<TrustAnchor> set = createTrustAnchorSet(certVector);
  	PKIXBuilderParameters params = new PKIXBuilderParameters(set, targetConstraints);
  	
  	//We dun use CRLs
  	params.setRevocationEnabled(false);
  	
  	//add target cert to certVector
  	certVector.add(targetCert);
  	
  	CollectionCertStoreParameters ccsp = new CollectionCertStoreParameters(certVector);		
  	CertStore store = CertStore.getInstance("Collection", ccsp);
  	
  	params.addCertStore(store);
  	
  	CertPathBuilder cpb = CertPathBuilder.getInstance("PKIX");
  	try
  	{
  		PKIXCertPathBuilderResult result = (PKIXCertPathBuilderResult) cpb.build(params);
  		
  		return true;
  	}
  	catch(CertPathBuilderException ex)
  	{
  		ex.printStackTrace();
  		return false;
  	}
  }
  
  private static Vector<Certificate> getCertificateFromKS(KeyStore ks)
  	throws KeyStoreException
  {
  	Vector<Certificate> certificateVector = new Vector<Certificate>();
  	Enumeration<String> aliasEnum = ks.aliases();
  	while(aliasEnum.hasMoreElements())
  	{
  		String alias = aliasEnum.nextElement();
  		if(ks.isCertificateEntry(alias))
  		{
  			certificateVector.add(ks.getCertificate(alias));
  		}
  	}
  	return certificateVector;
  }
  
  /**
   * Get the set of most-trusted CAs
   * @param certificateVector
   * @return
   * @throws IOException
   */
  private static Set<TrustAnchor> createTrustAnchorSet(Vector<Certificate> certificateVector)
  	throws IOException
	{
		HashSet<TrustAnchor> set = new HashSet<TrustAnchor>();
		Iterator<Certificate> it = certificateVector.iterator();
		while (it.hasNext())
		{
			X509Certificate cert = (X509Certificate) it.next();
			if (isCACert(cert))
			{
				TrustAnchor ta = new TrustAnchor(cert, null);
				set.add(ta);
			}
		}
				
		return set;
	}

}