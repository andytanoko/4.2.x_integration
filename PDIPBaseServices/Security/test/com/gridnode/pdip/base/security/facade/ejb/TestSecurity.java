package com.gridnode.pdip.base.security.facade.ejb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.gridnode.pdip.base.security.helpers.GridCertUtilities;
import com.gridnode.pdip.base.security.helpers.ISecurityInfo;
import com.gridnode.pdip.base.security.helpers.PKCS12Reader;
import com.gridnode.pdip.base.security.helpers.SecurityInfo;

public class TestSecurity
{
  
  public static void main(String[] args) throws Exception
  {
    secure();
    
    //secure2();
    
    //secure3();
    
    //test encrypt and verify using standard GT (see asym.doc)
    //decrypt and verify via BC version... require load sign cert/encrypt cert...
    //Result: it seem like the decryption part is not taking effect, the output file still in encrypted form
    //        causing the signature validation failed!
    //decryptAndVerify();
    
    encryptAndSignViaBC();
    
    decryptAndVerify();
  }
  
  private static void decryptAndVerify() throws Exception
  {
    //RSA IMPL output
    //File f = new File("data/asym/asmy.doc");
    
    //BC IMPL ooutput
    File f = new File("c:/output.txt");
    
    
    File tpCertFile = new File("data/pkcs12/testP12.cer");
    //File ownCertFile = new File("data/pkcs12/testP12.cer");
    File ownCertPrivate = new File("data/pkcs12/inovQATest.p12");
    
    X509Certificate tpCert = GridCertUtilities.loadX509Certificate(tpCertFile.getAbsolutePath());
    PKCS12Reader reader = new PKCS12Reader(ownCertPrivate.getAbsolutePath(),"changeit".toCharArray());
    reader.read();
    PrivateKey privateKey = reader.getPrivateKey();
    byte[] privateKeyInByte = com.gridnode.pdip.base.certificate.helpers.GridCertUtilities.writePrivateKeyToByte(privateKey);
    
    
    SecurityInfo secInfo = new SecurityInfo();
    secInfo.setSenderCertificateFile(tpCertFile.getAbsolutePath());
    secInfo.setOwnPrivateKey(privateKeyInByte);
    secInfo.setPassword("GridNode".toCharArray());
    secInfo.setDigestAlgorithm(ISecurityInfo.DIGEST_ALGORITHM_SHA1);
    
    File encryptedSignedFile = f;
    SecurityServiceBean bean = new SecurityServiceBean();
    
    //Decrypt via BC IMPL
    //byte[] plain = bean.decryptAndVerify(encryptedSignedFile, secInfo);
    
    //Decrypt via RSA IMPL (OK)
    //byte[] plain = bean.decryptAndVerify2(encryptedSignedFile, secInfo);
    
    //com.gridnode.pdip.base.certificate.helpers.GridCertUtilities.writeByteTOFile(new File("c:/decrypted.txt"), plain, plain.length);
  }
  
  private static void encryptAndSignViaBC() throws Exception
  {
    File f = new File("c:/3C3_33_1.xml");
    File tpCertFile = new File("data/pkcs12/inovQA.cer");
    //File ownCertFile = new File("data/pkcs12/testP12.cer");
    File ownCertPrivate = new File("data/pkcs12/testP12.p12");
    
    X509Certificate tpCert = GridCertUtilities.loadX509Certificate(tpCertFile.getAbsolutePath());
    PKCS12Reader reader = new PKCS12Reader(ownCertPrivate.getAbsolutePath(),"changeit".toCharArray());
    reader.read();
    PrivateKey privateKey = reader.getPrivateKey();
    byte[] privateKeyInByte = com.gridnode.pdip.base.certificate.helpers.GridCertUtilities.writePrivateKeyToByte(privateKey);
    
    
    SecurityInfo secInfo = new SecurityInfo();
    //secInfo.setReceipentCertificateFile(tpCertFile.getAbsolutePath());
    secInfo.setReceipentCertificate(tpCert.getEncoded());
    
    secInfo.setOwnPrivateKey(privateKeyInByte);
    secInfo.setPassword("GridNode".toCharArray());
    secInfo.setKeyLength(512);
    secInfo.setSign(true);
    secInfo.setDigestAlgorithm(ISecurityInfo.DIGEST_ALGORITHM_SHA1);
    
    SecurityServiceBean bean = new SecurityServiceBean();
    //byte[] output = bean.encryptAndSign(f, secInfo);
    //byte[] output = bean.encryptAndSign2(f, secInfo);
    
    //com.gridnode.pdip.base.certificate.helpers.GridCertUtilities.writeByteTOFile("c:/output.txt", output, output.length);
  }
  
  private static void secure() throws Exception
  {
    //byte[]           input = new byte[] { 0x00, (byte)0xbe, (byte)0xef };
    byte[] input = "haha".getBytes();
    SecureRandom     random = new SecureRandom();

    // create the RSA Key
    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");

    generator.initialize(1024, random);

    KeyPair          pair = generator.generateKeyPair();
    Key              pubKey = pair.getPublic();
    Key              privKey = pair.getPrivate();

    System.out.println("input            : " + toHex(input));

    // create the symmetric key and iv
    Key             sKey = createKeyForAES(256, random);
    IvParameterSpec sIvSpec = createCtrIvForAES(0, random);
    System.out.println("Initialization Vector Spec:"+sIvSpec.toString());

    // symmetric key/iv wrapping step
//    Cipher           xCipher = Cipher.getInstance(
//                               "RSA/NONE/OAEPWithSHA1AndMGF1Padding", "BC");
    Cipher           xCipher = Cipher.getInstance(
                                                  "RSA/NONE/PKCS1Padding", "BC");
    
    
    xCipher.init(Cipher.ENCRYPT_MODE, pubKey, random);

    byte[]          keyBlock = xCipher.doFinal(packKeyAndIv(sKey, null));

    // encryption step
    Cipher          sCipher = Cipher.getInstance("RC4", "BC");

    //sCipher.init(Cipher.ENCRYPT_MODE, sKey, sIvSpec);
    sCipher.init(Cipher.ENCRYPT_MODE, sKey, random);
    System.out.println("Output size required: "+sCipher.getOutputSize(input.length)+". KeyBlock length: "+input.length);
    
    
    byte[] cipherText = sCipher.doFinal(input);

    System.out.println("keyBlock length  : " + keyBlock.length);
    System.out.println("cipherText length: " + cipherText.length);

    // symmetric key/iv unwrapping step
    xCipher.init(Cipher.DECRYPT_MODE, privKey);

    Object[]keyIv = unpackKeyAndIV(xCipher.doFinal(keyBlock));

    // decryption step
//    sCipher.init(Cipher.DECRYPT_MODE, (Key)keyIv[0],
//                                          (IvParameterSpec)keyIv[1]);
    
    sCipher.init(Cipher.DECRYPT_MODE, (Key)keyIv[0]);

    System.out.println("Output size required: "+sCipher.getOutputSize(keyBlock.length)+". KeyBlock length: "+keyBlock.length);
    byte[] plainText = sCipher.doFinal(cipherText);

    System.out.println("plain            : " + new String(plainText)); 
  }
  
  private static void secure3() throws Exception
  {
    //byte[]           input = new byte[] { 0x00, (byte)0xbe, (byte)0xef };
    
    byte[] inputBlock = new byte[2];
    byte[] outputBlock = new byte[5];
    
    byte[] input = "hahadfaaaaaaaaaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbbbbbbvvvvvvvvvvvvvvvcxxxxxxxxxxxxxcxxxx".getBytes();
    System.out.println("Input lenght: "+input.length);
    
    ByteArrayInputStream in = new ByteArrayInputStream(input);
    DataInputStream dataIn = new DataInputStream(in);
    
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    DataOutputStream dataOut = new DataOutputStream(out);
    
    SecureRandom     random = new SecureRandom();

    // create the RSA Key
    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");

    generator.initialize(1024, random);

    KeyPair          pair = generator.generateKeyPair();
    Key              pubKey = pair.getPublic();
    Key              privKey = pair.getPrivate();

    System.out.println("input            : " + toHex(input));

    // create the symmetric key and iv
    Key             sKey = createKeyForAES(256, random);

    // symmetric key/iv wrapping step
    Cipher           xCipher = Cipher.getInstance(
                                                  "RSA/NONE/PKCS1Padding", "BC");
    
    
    xCipher.init(Cipher.ENCRYPT_MODE, pubKey, random);

    byte[]          keyBlock = xCipher.doFinal(packKeyAndIv(sKey, null));

    // encryption step
    Cipher          sCipher = Cipher.getInstance("RC4", "BC");

    //sCipher.init(Cipher.ENCRYPT_MODE, sKey, sIvSpec);
    sCipher.init(Cipher.ENCRYPT_MODE, sKey, random);

    //TODO use output byte array, update, then doFinal
    int bytesRead = 0;
    int partOut = 0;
    while( (bytesRead = dataIn.read(inputBlock)) > -1)
    {
      partOut = sCipher.update(inputBlock, 0, bytesRead, outputBlock, 0);
      dataOut.write(outputBlock, 0 , partOut);
    }
    int finalOut = sCipher.doFinal(outputBlock, 0);
    System.out.println("Final out: "+finalOut);
    
    dataOut.write(outputBlock, 0 ,finalOut);
    dataIn.close();
    dataOut.close();
    
    byte[] cipherText = out.toByteArray(); 

    System.out.println("keyBlock length  : " + keyBlock.length);
    System.out.println("cipherText length: " + cipherText.length);

    // symmetric key/iv unwrapping step
    xCipher.init(Cipher.DECRYPT_MODE, privKey);

    //TODO use update, doFinal
    Object[]keyIv = unpackKeyAndIV(xCipher.doFinal(keyBlock));

    // decryption step
//    sCipher.init(Cipher.DECRYPT_MODE, (Key)keyIv[0],
//                                          (IvParameterSpec)keyIv[1]);
    sCipher.init(Cipher.DECRYPT_MODE, (Key)keyIv[0]);

    byte[] plainText = sCipher.doFinal(cipherText);

    System.out.println("plain            : " + new String(plainText)); 
  }
  
  private static void secure2() throws Exception
  {
    byte[]           input = new byte[] { 0x00, (byte)0xbe, (byte)0xef };
    SecureRandom     random = new SecureRandom();

    // create the RSA Key
    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");

    generator.initialize(1024, random);

    KeyPair          pair = generator.generateKeyPair();
    Key              pubKey = pair.getPublic();
    Key              privKey = pair.getPrivate();

    System.out.println("input            : " + toHex(input));

    // create the symmetric key and iv
    Key             sKey = createKeyForAES(256, random);
    IvParameterSpec sIvSpec = createCtrIvForAES(0, random);

    // symmetric key/iv wrapping step
    Cipher           xCipher = Cipher.getInstance(
                               "RSA/NONE/OAEPWithSHA1AndMGF1Padding", "BC");

    xCipher.init(Cipher.ENCRYPT_MODE, pubKey, random);

    byte[]          keyBlock = xCipher.doFinal(packKeyAndIv(sKey, null));

    // encryption step
    Cipher          sCipher = Cipher.getInstance("AES/CTR/NoPadding", "BC");

    sCipher.init(Cipher.ENCRYPT_MODE, sKey, random);

    byte[] cipherText = sCipher.doFinal(input);

    System.out.println("keyBlock length  : " + keyBlock.length);
    System.out.println("cipherText length: " + cipherText.length);

    // symmetric key/iv unwrapping step
    xCipher.init(Cipher.DECRYPT_MODE, privKey);

    Object[]keyIv = unpackKeyAndIV(xCipher.doFinal(keyBlock));

    // decryption step
    sCipher.init(Cipher.DECRYPT_MODE, (Key)keyIv[0]);
                                          //(IvParameterSpec)keyIv[1]);

    byte[] plainText = sCipher.doFinal(cipherText);

    System.out.println("plain            : " + toHex(plainText));
  }
  
  public static String toHex(byte[] data, int length)
  {
      String digits = "0123456789abcdef";
      StringBuffer  buf = new StringBuffer();
      
      for (int i = 0; i != length; i++)
      {
          int v = data[i] & 0xff;
          
          buf.append(digits.charAt(v >> 4));
          buf.append(digits.charAt(v & 0xf));
      }
      
      return buf.toString();
  }
  
  public static String toHex(byte[] data)
  {
      return toHex(data, data.length);
  }
  
  public static SecretKey createKeyForAES(
                                          int          bitLength,
                                          SecureRandom random)
                                          throws NoSuchAlgorithmException, NoSuchProviderException
                                      {
                                          KeyGenerator generator = KeyGenerator.getInstance("RC4", "BC");
                                          //KeyGenerator generator = KeyGenerator.getInstance("AES", "BC");
                                          
                                          generator.init(256, random);
                                          
                                          return generator.generateKey();
                                      }
  

  public static IvParameterSpec createCtrIvForAES(
                                                  int             messageNumber,
                                                  SecureRandom    random)
                                              {
                                                  byte[]          ivBytes = new byte[16];
                                                  
                                                  // initially randomize
                                                  
                                                  random.nextBytes(ivBytes);
                                                  
                                                  // set the message number bytes
                                                  
                                                  ivBytes[0] = (byte)(messageNumber >> 24);
                                                  ivBytes[1] = (byte)(messageNumber >> 16);
                                                  ivBytes[2] = (byte)(messageNumber >> 8);
                                                  ivBytes[3] = (byte)(messageNumber >> 0);
                                                  
                                                  // set the counter bytes to 1
                                                  
                                                  for (int i = 0; i != 7; i++)
                                                  {
                                                      ivBytes[8 + i] = 0;
                                                  }
                                                  
                                                  ivBytes[15] = 1;
                                                  
                                                  return new IvParameterSpec(ivBytes);
                                              }
 
  private static byte[] packKeyAndIv(
                                     Key          key,
                                     IvParameterSpec ivSpec)
                                     throws IOException
                                 {
                                     ByteArrayOutputStream bOut = new ByteArrayOutputStream();

                                     if(ivSpec != null)
                                     {
                                       bOut.write(ivSpec.getIV());
                                     }
                                     bOut.write(key.getEncoded());

                                     return bOut.toByteArray();
                                 }

                                 private static Object[] unpackKeyAndIV(
                                     byte[]    data)
                                 {
                                     byte[]    keyD = new byte[16];
                                     byte[]    iv = new byte[data.length - 16];

                                     /* // testing
                                     return new Object[] {
                                          new SecretKeySpec(data, 16, data.length - 16, "AES")
                                          //new IvParameterSpec(data, 0, 16)
                                     }; */
                                     
                                     return new Object[] {
                                         new SecretKeySpec(data, "RC4") };
                                         //new IvParameterSpec(data, 0, 16); 
                                 }

  
}
