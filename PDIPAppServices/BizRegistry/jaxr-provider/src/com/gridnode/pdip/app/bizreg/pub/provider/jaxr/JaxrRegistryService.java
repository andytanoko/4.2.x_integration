/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JaxrRegistryService.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2003    Neo Sok Lay         Created
 * Sep 18 2003    Neo Sok Lay         Add method: querySchemes()
 * Sep 23 2003    Neo Sok Lay         Explicity add FindQualifier for 
 *                                    matchAllKeys=true. 
 * Oct 06 2003    Neo Sok Lay         Modify publish(OrganizationInfo) to 
 *                                    findAndSetServiceKeys after publish.
 * Mar 01 2006    Neo Sok Lay         Use generics                                   
 */
package com.gridnode.pdip.app.bizreg.pub.provider.jaxr;

import com.gridnode.pdip.app.bizreg.exceptions.RegistryProviderException;
import com.gridnode.pdip.app.bizreg.helpers.Logger;
import com.gridnode.pdip.app.bizreg.pub.model.*;
import com.gridnode.pdip.app.bizreg.pub.model.ConceptInfo;
import com.gridnode.pdip.app.bizreg.pub.model.OrganizationInfo;
import com.gridnode.pdip.app.bizreg.pub.model.SchemeInfo;
import com.gridnode.pdip.app.bizreg.pub.model.ServiceInfo;
import com.gridnode.pdip.app.bizreg.pub.provider.IRegistryService;

import javax.xml.registry.Connection;
import javax.xml.registry.FindQualifier;
import javax.xml.registry.JAXRException;
import javax.xml.registry.RegistryService;

import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of a RegistryService using the JAXR registry provider.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class JaxrRegistryService implements IRegistryService
{
  private int _workingCount = 0;
  private Connection _connection;
  private JaxrClient _client;

  /**
   * Constructor for JaxrRegistryService.
   * 
   * @param connection The connection to the JAXR registry provider
   */
  public JaxrRegistryService(
    Connection connection) throws Exception
  {
    _connection = connection;

    RegistryService regServiceConn = connection.getRegistryService();
    _client =
      new JaxrClient(
        regServiceConn.getBusinessQueryManager(),
        regServiceConn.getBusinessLifeCycleManager());
  }

  /**
   * Dynamically sets the security credentials. 
   * 
   * @param username User name
   * @param password Password
   * @throws RegistryProviderException Error setting the security credentials
   */
  public void setCredentials(String username, String password)
    throws RegistryProviderException
  {
    try
    {
      addWorkingCount();
      PasswordAuthentication passwdAuth = new PasswordAuthentication(username, password.toCharArray());

      Set<PasswordAuthentication> creds = new HashSet<PasswordAuthentication>();
      creds.add(passwdAuth);
      _connection.setCredentials(creds);      
    }
    catch (JAXRException e)
    {
      Logger.warn(
        "[JaxrRegistryService.setCredentials] Error set credentials",
        e);
      throw new RegistryProviderException(e.getMessage(), e.getCause());
    }
    finally
    {
      minusWorkingCount();
    }
  }
  
  /**
   * Closes the registry service connection.
   * After the call to this method, clients should not
   * use this instance anymore to perform registry service operations.
   * 
   * @return <b>true</b> if it is ok to close the connection, <b>false</b> otherwise.
   */
  public synchronized boolean close()
  {
    if (isWorking())
      return false;
      
    try
    {
      _connection.close();
      _client = null;
    }
    catch (Exception e)
    {
      Logger.warn(
        "[JaxrRegistryService.close] Error closing service connection",
        e);
    }
    return true;
  }
  
  private synchronized boolean isWorking()
  {
    return _workingCount > 0;
  }
  
  private synchronized void addWorkingCount()
  {
    _workingCount++;
  }
  
  private synchronized void minusWorkingCount()
  {
    _workingCount--;
  }
  
  /**
   * @see com.gridnode.pdip.app.bizreg.pub.provider.IRegistryService#publish(OrganizationInfo)
   */
  public OrganizationInfo publish(OrganizationInfo orgInfo)
    throws RegistryProviderException
  {
    try
    {
      addWorkingCount();
      orgInfo = _client.save(orgInfo);
      
      // the keys of services & bindings may be changed.
      Collection<ServiceInfo> services = _client.findAndSetServiceKeys(orgInfo);
      orgInfo.setServices(new ArrayList<ServiceInfo>(services));
    }
    catch (JAXRException e)
    {
      Logger.warn(
        "[JaxrRegistryService.publish(OrganizationInfo)] Error saving: ",
        e);
      throw new RegistryProviderException(e.getMessage(), e.getCause());
    }
    finally
    {
      minusWorkingCount();
    }
    return orgInfo;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.provider.IRegistryService#publish(ServiceInfo)
   */
  public ServiceInfo publish(ServiceInfo serviceInfo)
    throws RegistryProviderException
  {
    try
    {
      addWorkingCount();
      serviceInfo = _client.save(serviceInfo);
      Collection<BindingInfo> bindingInfos = _client.findAndSetBindingKeys(serviceInfo);
      serviceInfo.setBindings(new HashSet<BindingInfo>(bindingInfos));
    }
    catch (JAXRException e)
    {
      Logger.warn(
        "[JaxrRegistryService.publish(ServiceInfo)] Error saving: ",
        e);
      throw new RegistryProviderException(e.getMessage(), e.getCause());
    }
    finally
    {
      minusWorkingCount();
    }
    return serviceInfo;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.provider.IRegistryService#publish(BindingInfo)
   */
  //  public String publish(BindingInfo bindingInfo)
  //    throws RegistryProviderException
  //  {
  //    return null;
  //  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.provider.IRegistryService#publish(SchemeInfo)
   */
  public SchemeInfo publish(SchemeInfo schemeInfo)
    throws RegistryProviderException
  {
    try
    {
      addWorkingCount();
      schemeInfo = _client.save(schemeInfo);
      //Collection conceptInfos = _client.findAndSetConceptKeys(schemeInfo);
      //schemeInfo.setConcepts(new HashSet(conceptInfos));                   
    }
    catch (JAXRException e)
    {
      Logger.warn("[JaxrRegistryService.publish(SchemeInfo)] Error saving: ", e);
      throw new RegistryProviderException(e.getMessage(), e.getCause());
    }
    finally
    {
      minusWorkingCount();
    }
    return schemeInfo;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.provider.IRegistryService#publish(ConceptInfo)
   */
  public ConceptInfo publish(ConceptInfo conceptInfo)
    throws RegistryProviderException
  {
    try
    {
      addWorkingCount();
      conceptInfo = _client.save(conceptInfo);
    }
    catch (JAXRException e)
    {
      Logger.warn(
        "[JaxrRegistryService.publish(ConceptInfo)] Error saving: ",
        e);
      throw new RegistryProviderException(e.getMessage(), e.getCause());
    }
    finally
    {
      minusWorkingCount();
    }
    return conceptInfo;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.provider.IRegistryService#queryConcept(java.lang.String)
   */
  public ConceptInfo queryConcept(String key) throws RegistryProviderException
  {
    ConceptInfo conceptInfo;
    try
    {
      addWorkingCount();
      conceptInfo = _client.findConceptInfo(key);
    }
    catch (JAXRException e)
    {
      Logger.warn("[JaxrRegistryService.queryConcept] Error finding: ", e);
      throw new RegistryProviderException(e.getMessage(), e.getCause());
    }
    finally
    {
      minusWorkingCount();
    }
    return conceptInfo;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.provider.IRegistryService#queryOrganization(java.lang.String)
   */
  public OrganizationInfo queryOrganization(String key)
    throws RegistryProviderException
  {
    OrganizationInfo orgInfo;
    try
    {
      addWorkingCount();
      orgInfo = _client.findOrganizationInfo(key);
    }
    catch (JAXRException e)
    {
      Logger.warn("[JaxrRegistryService.queryOrganization)] Error finding: ", e);
      throw new RegistryProviderException(e.getMessage(), e.getCause());
    }
    finally
    {
      minusWorkingCount();
    }
    return orgInfo;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.provider.IRegistryService#queryScheme(java.lang.String)
   */
  public SchemeInfo queryScheme(String key) throws RegistryProviderException
  {
    SchemeInfo schemeInfo;
    try
    {
      addWorkingCount();
      schemeInfo = _client.findSchemeInfo(key);
    }
    catch (JAXRException e)
    {
      Logger.warn("[JaxrRegistryService.queryScheme] Error finding: ", e);
      throw new RegistryProviderException(e.getMessage(), e.getCause());
    }
    finally
    {
      minusWorkingCount();
    }
    return schemeInfo;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.provider.IRegistryService#queryService(java.lang.String)
   */
  public ServiceInfo queryService(String key) throws RegistryProviderException
  {
    ServiceInfo serviceInfo;
    try
    {
      addWorkingCount();
      serviceInfo = _client.findServiceInfo(key);
    }
    catch (JAXRException e)
    {
      Logger.warn("[JaxrRegistryService.queryService] Error finding: ", e);
      throw new RegistryProviderException(e.getMessage(), e.getCause());
    }
    finally
    {
      minusWorkingCount();
    }
    return serviceInfo;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.provider.IRegistryService#queryServiceBinding(java.lang.String)
   */
  public BindingInfo queryServiceBinding(String key)
    throws RegistryProviderException
  {
    BindingInfo bindingInfo;
    try
    {
      addWorkingCount();
      bindingInfo = _client.findServiceBindingInfo(key);
    }
    catch (JAXRException e)
    {
      Logger.warn(
        "[JaxrRegistryService.queryServiceBinding] Error finding: ",
        e);
      throw new RegistryProviderException(e.getMessage(), e.getCause());
    }
    finally
    {
      minusWorkingCount();
    }
    return bindingInfo;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.provider.IRegistryService#queryConcepts(java.util.Collection, boolean, java.util.Collection, java.util.Collection, java.util.Collection)
   */
  public Collection<ConceptInfo> queryConcepts(
    Collection<String> namePatterns,
    boolean matchAllKeys,
    Collection<String[]> categories,
    Collection<String[]> identifiers,
    Collection<String[]> links)
    throws RegistryProviderException
  {
    Collection<ConceptInfo> results = new ArrayList<ConceptInfo>();
    try
    {
      addWorkingCount();
      Collection<String> fQual = new ArrayList<String>();
      fQual.add(FindQualifier.EXACT_NAME_MATCH);
      fQual.add(FindQualifier.CASE_SENSITIVE_MATCH);
      fQual.add(matchAllKeys ? FindQualifier.AND_ALL_KEYS : FindQualifier.OR_ALL_KEYS);

      results.addAll(
        _client.findConceptInfos(
          fQual,
          namePatterns,
          categories,
          identifiers,
          links));
    }
    catch (JAXRException e)
    {
      Logger.warn("[JaxrRegistryService.queryConcepts] Error finding: ", e);
      throw new RegistryProviderException(e.getMessage(), e.getCause());
    }
    finally
    {
      minusWorkingCount();
    }
    return results;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.provider.IRegistryService#queryOrganizations(java.util.Collection, boolean, java.util.Collection, java.util.Collection, java.util.Collection, java.util.Collection)
   */
  public Collection<OrganizationInfo> queryOrganizations(
    Collection<String> namePatterns,
    boolean matchAllKeys,
    Collection<String[]> categories,
    Collection<ConceptInfo> specifications,
    Collection<String[]> identifiers,
    Collection<String[]> links)
    throws RegistryProviderException
  {
    Collection<OrganizationInfo> results = new ArrayList<OrganizationInfo>();
    try
    {
      addWorkingCount();
      Collection<String> fQual = new ArrayList<String>();
      fQual.add(matchAllKeys ? FindQualifier.AND_ALL_KEYS : FindQualifier.OR_ALL_KEYS);
      fQual.add(FindQualifier.SORT_BY_NAME_ASC);

      results.addAll(
        _client.findOrganizationInfos(
          fQual,
          namePatterns,
          categories,
          specifications,
          identifiers,
          links));
    }
    catch (JAXRException e)
    {
      Logger.warn(
        "[JaxrRegistryService.queryOrganizations] Error finding: ",
        e);
      throw new RegistryProviderException(e.getMessage(), e.getCause());
    }
    finally
    {
      minusWorkingCount();
    }
    return results;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.provider.IRegistryService#querySchemes(java.util.Collection, boolean, java.util.Collection, java.util.Collection)
   */
  public Collection<SchemeInfo> querySchemes(
    Collection<String> namePatterns,
    boolean matchAllKeys,
    Collection<String[]> categories,
    Collection<String[]> links)
    throws RegistryProviderException
  {
    Collection<SchemeInfo> results = new ArrayList<SchemeInfo>();
    try
    {
      addWorkingCount();
      Collection<String> fQual = new ArrayList<String>();
      fQual.add(FindQualifier.EXACT_NAME_MATCH);
      fQual.add(FindQualifier.CASE_SENSITIVE_MATCH);
      fQual.add(matchAllKeys ? FindQualifier.AND_ALL_KEYS : FindQualifier.OR_ALL_KEYS);

      results.addAll(
        _client.findSchemeInfos(
          fQual,
          namePatterns,
          categories,
          links));
    }
    catch (JAXRException e)
    {
      Logger.warn("[JaxrRegistryService.querySchemes] Error finding: ", e);
      throw new RegistryProviderException(e.getMessage(), e.getCause());
    }
    finally
    {
      minusWorkingCount();
    }
    return results;
  }

}
