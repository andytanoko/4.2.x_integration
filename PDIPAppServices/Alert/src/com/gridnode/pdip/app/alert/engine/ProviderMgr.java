/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProviderMgr.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 29 Nov 2002    Srinath	            Creation
 * 16 May 2003    Neo Sok Lay         Value formatting should be done in data
 *                                    providers.
 * 23 Jun 2003    Neo Sok Lay         Moved from helpers package.
 * 03 Mar 2006    Neo Sok Lay         Use generics
 * 03 Apr 2006    Neo Sok Lay         Change ValueDef.setTerm(): init other variables to
 *                                    null before extraction.
 */

package com.gridnode.pdip.app.alert.engine;

import com.gridnode.pdip.app.alert.providers.IProviderList;
import com.gridnode.pdip.app.alert.providers.IDataProvider;

import com.gridnode.pdip.app.user.helpers.UserEntityHandler;
import com.gridnode.pdip.app.user.model.UserAccount;

import com.gridnode.pdip.base.acl.helpers.RoleEntityHandler;
import com.gridnode.pdip.base.acl.helpers.SubjectRoleEntityHandler;

import java.util.*;


public class ProviderMgr
{
  //private HashMap definition = new HashMap();
  private static ProviderMgr instance;
  public static final String SUBJECT_TYPE = "UserAccount";

  private ProviderMgr()
  {
    initProvider();
  }

  private void initProvider()
  {
    //initEntityDef();
  }

  public static synchronized ProviderMgr getInstance()
  {
    if(instance == null)
      instance = new ProviderMgr();

    return instance;
  }

/*
  public HashMap getDefinition()
  {
    return definition;
  }

  private void initEntityDef()
  {
    MetaInfoFactory metaFactory = new MetaInfoFactory();
    EntityMetaInfo[] entityInfo = metaFactory.getAllMetaInfo();
    EntityMetaInfo metaInfo = null;

    int count = entityInfo.length;
    String[] entityNames = new String[count];

    for (int i = 0; i < count; i++)
    {
      entityNames[i] = entityInfo[i].getEntityName();
      if(entityNames[i] != null)
      {
        try
        {
          metaInfo = metaFactory.getMetaInfoFor(entityNames[i]);
    populateEntityFieldName(entityNames[i], metaInfo.getFieldMetaInfo());
        }
        catch (Exception ex)
        {
          AlertLogger.errorLog("ProviderMgr", "initEntityDef", "Error", ex);
        }
      }
    }
  }

  private void populateEntityFieldName(String entityName,
                                       FieldMetaInfo[] infoList)
  {
    String[] fieldNames = new String[infoList.length];

    int count = infoList.length;
    for (int i = 0; i < count; i++)
    {
      fieldNames[i] = infoList[i].getFieldName();
    }

    definition.put(entityName, fieldNames);
  }

*/
  public HashMap<String,String> getValue(Vector<String> fieldDefs, IProviderList providerList) throws Throwable
  {
    String val = null;
    String format = null;
    String term = null;
    HashMap<String,String> map = new HashMap<String,String>();
    ValueDef vd = new ValueDef();
    IDataProvider prv = null;

    int count2 = fieldDefs.size();
    for (int i = 0; i < count2; i++)
    {
      vd.setTerm(fieldDefs.elementAt(i));
//System.out.println("***** ValueDef >> "+vd.toString());
      term = vd.getTerm();
      if (vd.getProvider() != null)
        prv = providerList.getProvider(vd.getProvider());
      if (prv != null)
      {
        format = vd.getFormat();

        val = prv.get(vd.getField(), format);

        /*030516NSL The data provider should handle the formatting
        Object value = new Object();
        value = (Object)prv.get(vd.getField());
//System.out.println("The value of the Field is " + vd.getField());
//System.out.println("value of the object is >>" + value);
        // Value obtained is of type Date
        if(value instanceof Date)
        {
          format = vd.getFormat();
          if(format == null)
            val = (String) prv.get(vd.getField());
          else
          {
            try
            {
              SimpleDateFormat formatter = new SimpleDateFormat(format);
              val = formatter.format(value);
            }
            catch(Exception e)
            {
              val = (String) prv.get(vd.getField()).toString();
            }
          }
        }
        else if(value instanceof Number)  // Value obtained is of type Number
        {
          format = vd.getFormat();
//System.out.println("the format inside Number is >>" + format);
          if(format == null)
            val = (String) prv.get(vd.getField());
          else
          {
            try
            {
              DecimalFormat df = new DecimalFormat(format);
              val = df.format(value);
            }
            catch(Exception e)
            {
              val = (String) prv.get(vd.getField()).toString();
            }
          }
        }
        else
          val = (String) prv.get(vd.getField());  //  By default Value obtained is of type String
        */
        map.put(vd.getTerm(), val);
      }
      else
      {
        val = null; // initialize the value to null

  // Write for User & Role
        //System.out.println("vd.getProvider() >>>>" + term);
        if(term.startsWith("ROLE"))
        {
          StringBuffer valTemp = new StringBuffer();
          Collection<String> coll = getUserIdFromRoleId(term.substring(5));
          for (String userid : coll)
          {
            String email = getMailIdFromUserId(userid);
            if (email != null && email.trim().length() > 0)
            {
              if (valTemp.length() > 0)
                valTemp.append(',');

              valTemp.append(email);

            }
          }
          val = valTemp.toString();
        }
        else if (term.startsWith("USER"))
        {
          //System.out.println("Value is User");
          val = getMailIdFromUserId(term.substring(5));
        }
  //End
        map.put(vd.getTerm(), val);
      }
    }
    return map;
  }



  /**
   * Find the EMail Id for the specified user.
   *
   * @param UId The Id of the user.
   *
   * @return String EMail Id of the User.
   */

   private String getMailIdFromUserId(String UId) throws Throwable
   {
      return UserEntityHandler.getInstance().findByUserId(UId).getEmail();
   }

   /**
    * Find the User Ids from the specified Role
    *
    * @param RoleId   - Id of the Role
    *
    * @return The Ids of the User.
    */

    private Collection<String> getUserIdFromRoleId(String RoleId) throws Throwable
    {
      Long UId = new Long((RoleEntityHandler.getInstance().getRoleByRoleName(RoleId)).getUId());
      Collection coll = SubjectRoleEntityHandler.getInstance().getSubjectUIdsForRole(SUBJECT_TYPE, UId);
      Vector<String> vectUser = new Vector<String>();
      if (coll != null && !coll.isEmpty())
      {
        for (Iterator iter=coll.iterator(); iter.hasNext(); )
        {
          UserAccount ua = (UserAccount)UserEntityHandler.getInstance().getEntityByKeyForReadOnly(new Long(iter.next().toString()));
          if(ua.getId() != null)
            vectUser.add(ua.getId());
        }
      }
      return vectUser;
    }


  /********* Inner Class ********/

  class ValueDef
  {
    String term, provider, field, format;
    public ValueDef()
    {
    }

    void setTerm(String term)
    {
      this.term = term;
      //NSL20060403 init others to null first
      this.format = null;
      this.field = null;
      this.provider = null;
      int m = term.indexOf(".");
      int n = term.indexOf("#");

      if(m != -1)
      {
        provider = term.substring(0, m);
        if(n != -1)
        {
          field = term.substring(m+1, n);
          format = term.substring(n+1);
        }
        else
          field = term.substring(m+1);
      }
    }

    String getProvider()
    {
      return provider;
    }

    String getField()
    {
      return field;
    }

    String getTerm()
    {
      return term;
    }

    String getFormat()
    {
      return format;
    }

    public String toString()
    {
      return getTerm() + "=" + getProvider() + "." + getField() + "#" +
             getFormat();
    }
  }
}