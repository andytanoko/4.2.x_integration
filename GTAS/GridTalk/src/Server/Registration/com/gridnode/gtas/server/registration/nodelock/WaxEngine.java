/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: WaxEngine.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 26 2003    Koh Han Sing        Ported from GridForm
 */
package com.gridnode.gtas.server.registration.nodelock;

//import com.gridnode.gridform.GridForm;

import java.security.*;
import java.util.Random;
import java.math.BigInteger;

// Class to provide simple one-private-key based String encryption.
// The metaphor for this is that the string is coated with a layer of wax, so the user cannot
// read it...
// For use by license engine to hide from user which data it is we use to generate the license key.
// The class also provides a wrapper function to generate an MD5 hash from an array of bytes.
public class WaxEngine
{
  // Andrew: Encode a string
  public static String waxOn(String original, String key)
  { // 'Coat the original string with a layer of wax' (encrypt) such that
    // it takes significant effort to decode its contents without knowing the key.
    // This is not a particularly strong form of encryption, but should be sufficient to cause
    // crackers to try and find an easier way of bypassing our licencing system, and will
    // ensure that honest users stay honest.
    Random rnd = new Random();
    char[] inChars = getCharArray(original); // Convert input string to array of char
    char[] keyChars = getCharArray(key);
    // The fullCode array is where we work on the chars being waxed, and the last char in this
    // array is the random value.
    char[] fullCode = new char[inChars.length + 1];
    char rndChar = (char)(rnd.nextInt(32000)); // Used to make sure same input gives a different waxed string each time

    // Iterate through each character in the string to be encrypted and encrypt that
    // character, storing the result in the fullCode array.
    for(int i=0; i<inChars.length;i++)
    {
      fullCode[i] = inChars[i]; // Copy the unencoded char to our working array
      for(int j=0;j<keyChars.length;j++)
      { // Xor it with every char in the key string
        fullCode[i] = (char)(fullCode[i] ^ keyChars[j]);
      }
      // Now xor that with a char from the key based on the position of this
      // char in the array, and also xor with the random char. This will make sure
      // that 1. The same char maps to a different value when it re-occurs in the string,
      // and further 2. the coded version will change each time the algorithm is run
      fullCode[i] = (char)(fullCode[i] ^ keyChars[i % keyChars.length] ^ rndChar);
    }

    // Having encoded the input string, we now append in the last position of the array,
    // our random value. This is not coded, thus we can extract it when un-encrypting and
    // use to 'remove the wax' from our string.
    fullCode[fullCode.length-1] = rndChar; //Now safe to write this value on the end!
    // We call enHex to return us a string of hex digits representing the unicode
    // values in our waxed string. (This is because the wax contains non-displayables).
    // For each char, there will always be 4 hex digits. We return the hex string representing
    // our waxed string.
    return(enHex(fullCode));
  }

  // Andrew: Decode a 'waxed' string
  public static String waxOff(String coded, String key)
  { // Given a string of hex digits representing a waxed string, and a key, we 'disolve the
    // wax coating' and return the original string.
    char[] fullCode = deHex(coded); // Get an array of waxed chars. (Covert back from hex digits)
    char[] keyChars = getCharArray(key); // Cobvert key to array of chars for convienience
    int length = fullCode.length - 1;
    char[] outCode = new char[length]; // Outcode will stroe our result array of chars
    char rndChar = fullCode[length]; // Extract the random value from the array

    for(int i=0; i<length;i++)
    { // Iterate through the array of waxed chars and de-wax each one, storing the
      // decoded char in outCode array.
      // To remove the wax, we must repeat the operations used to wax it. Note that
      // some ops must be performed in reverse order.
      // 1. Remove the position and random value encoding
      fullCode[i] = (char)(fullCode[i] ^ keyChars[i % keyChars.length] ^ rndChar);
      for(int j=0;j<keyChars.length;j++)
      { // 2. Xor by each char in the key
        fullCode[i] = (char)(fullCode[i] ^ keyChars[j]);
      }
      outCode[i] = fullCode[i]; // and store the result in outCode array
    }
    // Convert outCode to a string, and return it. This was the original string that was encoded.
    return new String(outCode);
  }

  // Andrew: Convert char array to a string of hex digits (4 digits per char)
  public static String enHex(char[] hexChars)
  {
    StringBuffer result = new StringBuffer();
    for(int i=0;i<hexChars.length;i++)
    {
      // Use Integer class static method to get the hex representation of the
      // unicode value of this char.
      String temp = Integer.toHexString((int)hexChars[i]);
      // We now append an appropriate number of zeros to the front to
      // ensure we always have 4 hex digits. (There are more elegant ways to do
      // it but this is the quickest to implement.)
      if(temp.length() == 1)
        result.append("000" + temp);
      else
        if(temp.length() == 2)
          result.append("00" + temp);
        else
          if(temp.length() == 3)
            result.append("0" + temp);
          else
            result.append(temp);
    }
    // Return the result as a String
    return new String(result.toString());
  }

  // Andrew: Convert from a string of hex digits (always 4 digits per char)
  //         to an array of chars the hex digits represented
  public static char[] deHex(String fromHex)
  { // Note that if the string we are passed does not have a multiple
    // of 4 as its length, bad things will happen. This is beacuse we expect
    // each char value to be represented by 4 digits of hex. (ie: 0041) etc...
    char[] hexBytes = new char[fromHex.length() / 4];
    int j=-1; // Our position in the result array. (Will add 1 to it before first calculation)
    for(int i=0; i < fromHex.length(); i+=4)
    { // i will point to the start of current 4 digits block of hex in the fromHex string
      j++; // Increment position in the result array. (If first time in loop will now be 0)
      String oneHexByte = fromHex.substring(i,i+4); // Get 4 digits of hex
      hexBytes[j] = (char)Integer.parseInt(oneHexByte,16); // Parse to int and then cast to char and store
    }
    // We now return our array of chars
    return hexBytes;
  }

  // Andrew: Given a string, returns its contents in an array of chars
  public static char[] getCharArray(String str)
  {
    char[] chars = new char[str.length()];
    for(int i=0;i < str.length(); i++)
    {
      chars[i] = str.charAt(i);
    }
    return chars;
  }

  // Andrew: Given an array of bytes, will return a BigInteger containing
  //         the MD5 hash of that data
  public static BigInteger getMD5(byte[] input)
  {
    BigInteger hash;
    try
    {
      MessageDigest md = MessageDigest.getInstance("MD5");
      hash = new BigInteger(md.digest(input));
      return hash;
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
    return null;
  }

  // Jared: Test method should be shifted to unit-test classes.
//  public static void main(String[] args)
//  {
//    String key = "Secret";
//    String garbageIn = "My dog is inside the piano!";
//    String garbageOut = waxOn(garbageIn,key);
//    String decoded = waxOff(garbageOut,key);
//
//    System.out.println(">>" + garbageIn);
//    System.out.println("==" + garbageOut);
//    System.out.println("<<" + decoded);
//    System.out.println("HEX=" + enHex(getCharArray(decoded)));
//    System.out.println("MD5 Hash of the hex digits=" + getMD5(enHex(getCharArray(decoded)).getBytes()).toString());
//
//  }
}