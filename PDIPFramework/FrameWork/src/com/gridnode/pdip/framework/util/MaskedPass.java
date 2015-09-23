package com.gridnode.pdip.framework.util;
public class MaskedPass
{

  public static String encode(String s){
    s=Base64Coder.encodeString(s);
    s=Base64Coder.encodeString(s);
    return Base64Coder.encodeString(s);
  }
  public static String decode(String s){
    s=Base64Coder.decodeString(s);
    s=Base64Coder.decodeString(s);
    return Base64Coder.decodeString(s);
  }
}
