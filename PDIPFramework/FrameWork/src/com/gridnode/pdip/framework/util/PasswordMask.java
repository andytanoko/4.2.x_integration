/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PasswordMask.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 06 2002    Neo Sok Lay         Created
 * May 17 2002    Neo Sok Lay         Moved from AppUser.
 * May 30 2003    Neo Sok Lay         GNDB00013996: Masked password is 13 chars
 *                                    but gets truncated to 12 since password 
 *                                    length is 12. Change the password 
 *                                    encryption algorithm.
 * May 25 2006    Neo Sok Lay         Get specific algo for SecureRandom. Some
 *                                    platforms give different algo from Windows.                                   
 */
package com.gridnode.pdip.framework.util;

import java.security.SecureRandom;
import java.util.Random;

/**
 * This is a utility to mask a password, using a simple random table encryption algorithm.
 * This algorithm works for password lengths between 1 and 61 and mask lengths of at least 2.
 * It is not recommended for mask lengths to be shorter than password lengths.
 *
 * @author Neo Sok Lay
 *
 * @version 4.0
 * @since 2.1
 */
public class PasswordMask implements java.io.Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5537682682922019356L;
	private final static String RAND_STRING = "abcde1fghij3klmno5pqrst7uvwxy9zABCDE2FGHIJ4KLMNO6PQRST8UVWXY0Z";
  private final static int RAND_VARIABLE = RAND_STRING.length();
  private final static String RAND_ALGO = "SHA1PRNG"; 
  
  private int _length;
  private String _password;

  private PasswordMask()
  {
  }

  /**
   * Construct a PasswordMask for a cleartext password.
   *
   * @param password The cleartext password.
   */
  public PasswordMask(String password)
  {
    _length = password.length();
    mask(password);
  }

  /**
   * Construct a PasswordMask for a cleartext password, applying a mask length.
   *
   * @param password The cleartext password
   * @param maskLength The length of the masked password.
   */
  public PasswordMask(String password, int maskLength)
  {
    _length = password.length();
    mask(password, maskLength);
  }

  /**
   * Apply a mask length restriction to the currently masked password.
   *
   * @param maskLength The mask length to apply.
   */
  public void applyMaskLength(int maskLength)
  {
    /*030530NSL
    if (_password.length() > maskLength)
      _password = _password.substring(0, maskLength);
    else if (_password.length() < maskLength)
      _password = _password +
                    hash(_password).substring(0,
                    maskLength-_password.length());
    */
    /*
    SecureRandom  rand = new SecureRandom(_password.getBytes());
    */
    SecureRandom rand = null;
    try
    {
      rand = SecureRandom.getInstance(RAND_ALGO);
      rand.setSeed(_password.getBytes());
    }
    catch (Exception ex)
    {
      //this means, actually, the password would not match with previously masked password using RAND_ALGO, if any
      rand = new SecureRandom(_password.getBytes());
    }
    char[] pwd = new char[maskLength-1];
    randomize(rand, pwd);
    _password = getLengthPart().concat(String.valueOf(pwd));
  }

  /**
   * Fill a char array buffer with randomly generated alphanumeric characters.
   *
   * @param rand The random number generator to use to randomize.
   * @param buff The buff to hold the randomize characters.
   */
  private void randomize(Random rand, char[] buff)
  {
    int randNum;

    for(int i=0; i<buff.length; i++)
    {
      randNum = rand.nextInt(RAND_VARIABLE);
      buff[i] = RAND_STRING.charAt(randNum);
    }
  }

  /**
   * Get the string representation of the length of the cleartext password
   * 
   * @return A string representing the length of the cleartext password
   */
  private String getLengthPart()
  {
    return String.valueOf(RAND_STRING.charAt(_length));
    
    /*030530NSL: use random string
    String str = String.valueOf(_length);
    if (str.length() < 2)
      return "0".concat(str);

    return str.substring(0, 2);
    */
  }

  /**
   * Construct a PasswordMask based on a masked password.
   *
   * @param maskedPassword The masked password.
   * @return The PasswordMask created.
   */
  public static PasswordMask newMaskedPassword(String maskedPassword)
  {
    PasswordMask mask = new PasswordMask();

    mask._password = maskedPassword;
    mask._length = RAND_STRING.indexOf(maskedPassword.charAt(0));

    /*030530NSL: The length is the first char in the masked password
    String length = maskedPassword.substring(0, 2);
    mask._length = Integer.parseInt(length);
    */

    return mask;
  }

  /**
   * Mask the cleartext password. 
   * 
   * @param password The cleartext password to mask
   */  
  private void mask(String password)
  {
    /*030530NSL:
    _password = getLengthPart().concat(hash(hash(password)));
    */
   
    _password = getLengthPart().concat(password);
    applyMaskLength(password.length());
  }

  /**
   * Mask the cleartext password, applying a length restriction.
   * 
   * @param password   The cleartext password to mask
   * @param maskLength The length restriction of the mask to apply. 
   *                    The masked password will be of length <code>maskLength</code> 
   */ 
  private void mask(String password, int maskLength)
  {
    mask(password);
    applyMaskLength(maskLength);
  }

  /*030530NSL: Not required anymore
  private String hash(String str)
  {
    return String.valueOf(str.hashCode());
  }
  */
  
  /**
   * Get the length of the cleartext password.
   */
  public int getLength()
  {
    return _length;
  }

  /**
   * Return a mask string of '*' for the length of the cleartext password.
   */
  public String getMask()
  {
    StringBuffer buff = new StringBuffer();
    for (int i=0; i<_length; i++)
      buff.append('*');
    return buff.toString();
  }

  /**
   * Returns the masked password.
   */
  public String toString()
  {
    return _password;
  }

/*
  public static void main(String[] args)
  {
    String pwd1 = "admin";
    String pwd2 = "password";
//    String pwd1 = "user1";
//    String pwd2 = "user2";

    PasswordMask mask1 = new PasswordMask(pwd1);
    PasswordMask mask2 = new PasswordMask(pwd2);

    System.out.println("***Before applying mask");
    System.out.println("Password1 = "+mask1 + ", length="+mask1._length);
    System.out.println("Password2 = "+mask2+ ", length="+mask2._length);

    mask1.applyMaskLength(12);
    mask2.applyMaskLength(12);
    System.out.println("***after applying mask");
    System.out.println("Password1 = "+mask1+ ", length="+mask1._length);
    System.out.println("Password2 = "+mask2+ ", length="+mask2._length);

    System.out.println("*** mask 3");
    PasswordMask mask3 = PasswordMask.newMaskedPassword("1C4fB");
    mask3.applyMaskLength(12);
    System.out.println("length = "+mask3.getLength());
    System.out.println("password = "+mask3);
    
  }  
*/
}