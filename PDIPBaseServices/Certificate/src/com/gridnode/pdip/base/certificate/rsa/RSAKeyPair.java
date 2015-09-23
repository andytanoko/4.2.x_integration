/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RSAKeyPair.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 13 2002    Neo Sok Lay         Created
 * Jun 29 2009   Tam Wei Xiang        #560: Migrate from RSA J-SAFE/B-SAFE to BouncyCastle Lib
 */
package com.gridnode.pdip.base.certificate.rsa;

import com.gridnode.pdip.base.certificate.exceptions.ILogErrorCodes;
import com.gridnode.pdip.base.certificate.helpers.CertificateLogger;
import com.gridnode.pdip.base.certificate.helpers.GridCertUtilities;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.security.spec.RSAKeyGenParameterSpec;

/**
 * This class provides a wrapper for a JSafe Public and Private Key pair. It
 * first generates the key pair and keeps the generated key pair for use later
 * on.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class RSAKeyPair
{
  public static final int KEY_LENGTH_1024       = 1024;
  public static final int KEY_LENGTH_2048       = 2048;
  public static final int PUBLIC_EXPONENT_65537 = 65537;
  
  private PrivateKey _privateKey;
  private PublicKey _publicKey;
  
  protected boolean _keysGenerated;
  protected BigInteger[] _keyParameters = {
                                     BigInteger.valueOf(KEY_LENGTH_1024),
                                     RSAKeyGenParameterSpec.F4,
                                   };

  /**
   * Creates a RSAKeyPair object. This will generate the JSafe Public and
   * Private Key pair using the default key length and public exponent of
   * 1024-bit and 65537 respectively.
   */
  public RSAKeyPair()
  {
    generate();
  }

  /**
   * Creates a RSAKeyPair object. This will generate the JSafe Public and
   * Private Key pair using a specified key length and public exponent.
   * <p>Note that there is some fixed relationship between the key length and
   * the exponent.
   *
   * @param keyLength Number of bits for the key
   * @param exponent The public exponent.
   */
  public RSAKeyPair(int keyLength, int exponent)
  {
    setKeyParameters(keyLength, exponent);
    generate();
  }

  /**
   * Set the key length and exponent for key pair generation.
   *
   * @param keyLength Number of bits of key.
   * @param exponent Public exponent
   */
  protected void setKeyParameters(int keyLength, int exponent)
  {
    _keyParameters[0] = BigInteger.valueOf(keyLength);
    _keyParameters[1] = BigInteger.valueOf(exponent);
  }

  /**
   * Checks if the Key pair is generated successfully.
   *
   * @return <b>true</b> if call to <code>generate()</code> completed
   * successfully, <b>false</b> otherwise.
   */
  public boolean isKeyPairGenerated()
  {
    return _keysGenerated;
  }

  /**
   * Generate the Key Pair.
   */
  /*
  protected void generate()
  {
    JSAFE_KeyPair keyPairGen   = null;
    _keysGenerated = false;

    try
    {
      keyPairGen = JSAFE_KeyPair.getInstance("RSA", "Java");

      // initialise the JSAFE_KeyPair, passing in the random number generator
      // and the RSA Key Parameters
      keyPairGen.generateInit(null, _keyParameters, getRandomNumGen());

      // Generate the RSA Key Pair
      keyPairGen.generate();

      // generation done, retrieve the public and private keys.
      _publicKey = keyPairGen.getPublicKey();
      _privateKey = keyPairGen.getPrivateKey();
      _keysGenerated = true;
    }
    catch (Exception ex)
    {
      CertificateLogger.error(ILogErrorCodes.RSA_KEY_PAIR_GENERATE, "[RSAKeyPair.generate] Problem generating RSA Key pair: "+ex.getMessage(), ex);
    }
    finally
    {
      if (keyPairGen != null)
        keyPairGen.clearSensitiveData();

      keyPairGen = null;
    }
  }*/
  
  /**
   * Generate the Key Pair.
   */
  protected void generate()
  {
    KeyPairGenerator keyPairGen   = null;
    _keysGenerated = false;

    try
    {
      keyPairGen = KeyPairGenerator.getInstance("RSA", GridCertUtilities.getSecurityProvider());

      // initialise the KeyPairGen, passing in the random number generator
      // and the RSA Key Parameters
      keyPairGen.initialize(new RSAKeyGenParameterSpec(_keyParameters[0].intValue(), _keyParameters[1]), getRandomNumGen());

      // Generate the RSA Key Pair
      KeyPair keyPair = keyPairGen.generateKeyPair();

      // generation done, retrieve the public and private keys.
      _publicKey = keyPair.getPublic();
      _privateKey = keyPair.getPrivate();
      _keysGenerated = true;
    }
    catch (Exception ex)
    {
      CertificateLogger.error(ILogErrorCodes.RSA_KEY_PAIR_GENERATE, "[RSAKeyPair.generate] Problem generating RSA Key pair: "+ex.getMessage(), ex);
    }
    finally
    {
      keyPairGen = null;
    }
  }
  
  /**
   * Get a Random number generator. This uses the SHA-1 algorithm to create
   * a random number generator.
   *
   * @return An instance of a SecureRandom object.
   */
  protected SecureRandom getRandomNumGen()
    throws NoSuchAlgorithmException
  {
    SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
    
    // Note that this way of seeding the random number generator is not secure
    // A more probable way of obtaining a guaranteed unique seed should be
    // deviced.
    random.setSeed(System.currentTimeMillis());

    return random;
  }

  /**
   * Retrieve the generated Public key.
   *
   * @return Generated JSafe Public key.
   */
  public PublicKey getPublicKey()
  {
    return _publicKey;
  }

  /**
   * Retrieve the generated Private key.
   *
   * @return Generated JSafe Private key.
   */
  public PrivateKey getPrivateKey()
  {
    return _privateKey;
  }
}