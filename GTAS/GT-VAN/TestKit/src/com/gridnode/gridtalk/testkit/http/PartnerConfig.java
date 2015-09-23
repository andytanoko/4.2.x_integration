/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 3, 2007    i00107           Created
 */
 package com.gridnode.gridtalk.testkit.http;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * Configuration for partners
 * @author i00107
 */
public class PartnerConfig
{
  private Properties _props = new Properties();
  private boolean _valid = false;
  private StringBuffer _error = new StringBuffer();
  private List<Partner> _partnerList = new ArrayList<Partner>();
  private int _counterVal; 

  /**
   * @param f partner config file
   */
  public PartnerConfig(File f)
  {
    load(f);
  }

  /**
   * @return <b>true</b> if the partner config file is valid
   */
  public boolean isValid()
  {
    return _valid;
  }
  
  /**
   * @return The errors in the partner config file
   */
  public String getError()
  {
    return _error.toString();
  }
  
  /**
   * @return List of partners configured
   */
  public List<Partner> getPartnerList()
  {
    return _partnerList;
    
  }
  public int getCounter()
  {
	  return _counterVal;
  }
  /**
   * @return the corresponding key value
   */

  private void load(File f)
  {
    try
    {
      FileInputStream fis = new FileInputStream(f);
      _props.load(fis);
      validate();
      fis.close();
    }
    catch (Exception ex)
    {
      System.out.println("[PartnerConfig.load()] Error reading properties");
      ex.printStackTrace();
      _error.append("Error reading properties: "+ex.getMessage());
    }
  }

  private void validate()
  {
    if (_props.isEmpty())
    {
      _error.append("No partner configured.<br>");
    }
    else
    {
      
    	
      Set keys = _props.keySet();
      for (Iterator i=keys.iterator(); i.hasNext(); )
      {
        String key = (String)i.next();
        String val = _props.getProperty(key);
        try
        {
          int numTx = Integer.parseInt(val);
          Partner p = new Partner(key, numTx);
          _partnerList.add(p);
          _counterVal = p.getCounter();
          
        }
        catch (NumberFormatException ex)
        {
          _error.append("Num tx must be integer: "+val+"<br>");
          System.out.println("[RnifHubConfig.validate()] Num tx is not an integer: "+val);
          ex.printStackTrace();
        }
        //to sort
        Collections.sort(_partnerList);
      }
    }
    if (_error.length()==0)
    {
      _valid = true;
    }
  }
  
}
