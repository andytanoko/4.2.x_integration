package com.gridnode.gtas.server.backend.openapi.core;

import java.security.*;

/**
 * Title:        Open API
 * Description:  Open Application Programmer's Interface for GridTalk Server
 * Copyright:    Copyright (c) 2001
 * Company:      GridNode Pte Ltd
 * @author Shannon Koh
 * @version 1.0
 */

import java.util.Random;

public class APIAuthenticator
{
  public static final int KEYLENGTH = 128;

  private String username;
  private String password;
  private byte[] authKey;
  private byte[] sessionKey;

  public APIAuthenticator(String user)
  {
    this(user, null);
  }

  public APIAuthenticator(String user, String pass)
  {
    username = user;
    password = pass;
  }

  public String getUserName()
  {
    return username;
  }

  public byte[] getAuthKey()
  {
    if (authKey == null)
    {
      Random random = new Random();
      authKey = new byte[KEYLENGTH];
      random.nextBytes(authKey);
    }

    return authKey;
  }

  public void setPassword(String pass)
  {
    password = pass;
    sessionKey = null;
  }

  public void setAuthKey(byte[] authKeySrc)
  {
    authKey = authKeySrc;
    sessionKey = null;
  }

  public byte[] getSessionKey()
  {
    if (sessionKey == null)
    {
      try
      {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(authKey);
        sessionKey = md.digest(password.getBytes());
      }
      catch (NoSuchAlgorithmException e)
      {
        sessionKey = null;
      }
    }

    return sessionKey;
  }

  public boolean compareSessionKey(byte[] toCompare)
  {
    return MessageDigest.isEqual(sessionKey, toCompare);
  }
}