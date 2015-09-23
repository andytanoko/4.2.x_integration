/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultCertificateManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-13     Andrew Hill         Created
 * 2002-10-09     Andrew Hill         "partnerCat" mods
 * 2003-01-20     Andrew Hill         Added vFields to support import & export
 * 2003-01-22     Andrew Hill         Process 2 maps for viewing cert details
 * 2003-04-14     Andrew Hill         Implement doUpdate to rename cert entities
 * 2003-04-15     Andrew Hill         Implement getDeleteEvent, export store methods
 * 2003-07-03     Andrew Hill         getlistPager(isPartner)
 * 2003-07-18     Andrew Hill         Support for multiple deletion events
 * 2004-01-08     Neo Sok Lay         View/Export existing certificate using UID instead of Name.
 * 2004-03-26     Daniel D'Cotta      Added RELATED_CERT_UID
 * 2006-07-26     Tam Wei Xiang       Load the value for cert's serial num,
 *                                    start date, and end date while in import mode
 *                                    (given use has add a cert). See loadField(...),
 * 2006-07-28     Tam Wei Xiang       Add field isCA. Modified method doCreate 
 * 2006-11-24     Regina Zeng         Override serial num field to displayble        
 * 2006-12-08     Tam Wei Xiang       Remove the virtual field serialNum   
 * 2008-08-01	  Wong Yee Wah		 #38  add field swapDate, swapTime (line :116-117)                       
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.FormFileElement;
import com.gridnode.gtas.events.certificate.ChangePrivateCertificatePasswordEvent;
import com.gridnode.gtas.events.certificate.ExportCertificateEvent;
import com.gridnode.gtas.events.certificate.ExportCertificateKeyStoreEvent;
import com.gridnode.gtas.events.certificate.ExportCertificateTrustStoreEvent;
import com.gridnode.gtas.events.certificate.GetCertificateEvent;
import com.gridnode.gtas.events.certificate.GetCertificateListEvent;
import com.gridnode.gtas.events.certificate.ImportCertificateEvent;
import com.gridnode.gtas.events.certificate.RemoveCertificateEvent;
import com.gridnode.gtas.events.certificate.UpdateCertificateEvent;
import com.gridnode.gtas.events.certificate.ViewCertificateEvent;
import com.gridnode.gtas.model.certificate.ICertificate;
import com.gridnode.gtas.model.certificate.IX500Name;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;


class DefaultCertificateManager extends DefaultAbstractManager
  implements IGTCertificateManager
{
  private static String[] _x500NameFields =
  { //20030121AH
    "x500Name.country",
    "x500Name.state",
    "x500Name.organization",
    "x500Name.locality",
    "x500Name.organizationalUnit",
    "x500Name.streetAddress",
    "x500Name.commonName",
    "x500Name.title",
    "x500Name.emailAddress",
    "x500Name.businessCategory",
    "x500Name.telephoneNumber",
    "x500Name.postalCode",
    "x500Name.unknownAttributeType",
  };
  private static Number[] _x500NameFieldIds =
  { //20030121AH
    IGTX500NameEntity.COUNTRY,
    IGTX500NameEntity.STATE,
    IGTX500NameEntity.ORGANIZATION,
    IGTX500NameEntity.LOCALITY,
    IGTX500NameEntity.ORGANIZATIONAL_UNIT,
    IGTX500NameEntity.STREET_ADDRESS,
    IGTX500NameEntity.COMMON_NAME,
    IGTX500NameEntity.TITLE,
    IGTX500NameEntity.EMAIL_ADDRESS,
    IGTX500NameEntity.BUSINESS_CATEGORY,
    IGTX500NameEntity.TELEPHONE_NUMBER,
    IGTX500NameEntity.POSTAL_CODE,
    IGTX500NameEntity.UNKNOWN_ATTRIBUTE_TYPE,
  };

  DefaultCertificateManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_CERTIFICATE, session);
  }

  protected void doUpdate(IGTEntity entity) throws GTClientException
  { //20030415AH
    /**
     * Only certificates name may be updated.
     */
    try
    {
      IGTCertificateEntity cert = (IGTCertificateEntity)entity;
      IGTCertificateSwappingEntity certSwap = (IGTCertificateSwappingEntity)cert.getFieldValue(IGTCertificateEntity.CERTIFICATE_SWAPPING);
      if(cert.isFieldDirty(IGTCertificateEntity.NAME) || cert.isFieldDirty(IGTCertificateEntity.RELATED_CERT_UID))
      {
        //IEvent event = new ChangeCertificateNameEvent(cert.getUidLong(),
        //                                              cert.getFieldString(cert.NAME));
    	
    	 	  
        IEvent event = new UpdateCertificateEvent(
          cert.getUidLong(),
          cert.getFieldString(IGTCertificateEntity.NAME),
          StaticUtils.longValue(cert.getFieldString(IGTCertificateEntity.RELATED_CERT_UID)),
          certSwap.getFieldString(IGTCertificateSwappingEntity.SWAP_DATE),
          certSwap.getFieldString(IGTCertificateSwappingEntity.SWAP_TIME)
        ); // 20040326 DDJ
        
        handleEvent(event);
      }
      else
      {
        //do nothing
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error updating certificate name",t);
    }
  }
  
  //TWX 26072006 refactor name from loadX500NameEntities
  //             To include the serialNum, startDate and endDate
  private Object[] loadX500CertDetail(IGTCertificateEntity cert)
    throws GTClientException
  { //20030117AH
    //20030123AH - Try and do the file handling correctly (good luck)
    try
    {
      IEvent event = null;
      if(cert.isNewEntity())
      {
        FormFileElement[] elements = (FormFileElement[])cert.getFieldValue(IGTCertificateEntity.CERT_FILE);
        //@todo: what to do with the deleted file?
        String certFile = null;
        if( (elements == null) || (elements.length == 0) )
        {
          throw new IncompleteFieldsException("certFile not initialised (no files)");
        }
        //Now we must find the one for upload
        for(int i=0 ; i < elements.length; i++)
        {
          if(elements[i].isToUpload())
          {
            certFile = elements[i].getUploadedFilename();
            break;
          }
        }
        if( certFile == null )
        {
          throw new IncompleteFieldsException("certFile not initialised (toUpload not found)");
        }

        Boolean isPartner = (Boolean)cert.getFieldValue(IGTCertificateEntity.IS_PARTNER);
        if(isPartner == null) throw new IncompleteFieldsException("isPartner not initialised (null)");
        if(isPartner.booleanValue())
        {
          event = new ViewCertificateEvent(certFile,Boolean.TRUE,null);
        }
        else
        {
          String password = cert.getFieldString(IGTCertificateEntity.PASSWORD);
          if("".equals(password) || (password == null) )
          {
            throw new IncompleteFieldsException("password not initialised (null)");
          }
          event = new ViewCertificateEvent(certFile,Boolean.TRUE,password);
        }
      }
      else
      {
        //String certName = cert.getFieldString(cert.NAME);
        //event = new ViewCertificateEvent(certName,Boolean.FALSE);
        //event = new ViewCertificateEvent(certName);
        Long uid = cert.getUidLong();
        event = new ViewCertificateEvent(uid);
      }
      Map fieldMap = (Map)handleEvent(event); //Fire event to GTAS and get response map
      try
      { //20030122AH - Extracting 2 entity maps from returned map!
        Map issuerMap = (Map)fieldMap.get(IX500Name.ISSUERNAMES_ENTITY_NAME);
        Map subjectMap = (Map)fieldMap.get(IX500Name.SUBJECT_ENTITY_NAME);
        
        /* TWX
        IGTX500NameEntity[] results = new IGTX500NameEntity[2];
        results[0] = (IGTX500NameEntity)processX500NameMap(issuerMap);
        results[1] = (IGTX500NameEntity)processX500NameMap(subjectMap);*/
        
        Object[] results = new Object[fieldMap.size()];
        results[0] = (IGTX500NameEntity)processX500NameMap(issuerMap);
        results[1] = (IGTX500NameEntity)processX500NameMap(subjectMap);
        results[2] = (String)fieldMap.get(ICertificate.SERIAL_NUM_FIELD);
        results[3] = (Date)fieldMap.get(ICertificate.START_DATE_FIELD);
        results[4] = (Date)fieldMap.get(ICertificate.END_DATE_FIELD);
        
        return results;
      }
      catch (Throwable t)
      {
        throw new GTClientException("Error processing returned X500NAME fieldmap", t);
      }
    }
    catch(IncompleteFieldsException t)
    {
      throw t;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error retrieving x500Name entity",t);
    }
  }
  
  private IGTX500NameEntity processX500NameMap(Map map) throws GTClientException
  { //20030122AH
    if(map == null)
    {
      throw new NullPointerException("X500NAME map is null");
    }
    AbstractGTEntity x500Name = initEntityObjects(IGTEntity.ENTITY_X500NAME);
    IGTFieldMetaInfo[] fmi = x500Name.getFieldMetaInfo();
    for(int i =0; i < fmi.length; i++)
    {
      Number fieldId = fmi[i].getFieldId();
      Object value = map.get(fieldId);
      x500Name.setNewFieldValue(fieldId, value );
    }
    return (IGTX500NameEntity)x500Name;
  }

  protected void doCreate(IGTEntity entity) throws GTClientException
  { //20030117AH
    try
    {
      IGTCertificateEntity cert = (IGTCertificateEntity)entity;
      String  certName = cert.getFieldString(IGTCertificateEntity.NAME);
      String  certFile = cert.getFieldString(IGTCertificateEntity.CERT_FILE);
      Long relatedCertUid = (Long)cert.getFieldValue(IGTCertificateEntity.RELATED_CERT_UID); // 20040326 DDJ
/*      if ((obj != null) && ((String)obj).equals(""))
        relatedCertUid = null;
      else
        relatedCertUid = (Long)obj;*/
      boolean isPartner = ((Boolean)cert.getFieldValue(IGTCertificateEntity.IS_PARTNER)).booleanValue();
      Boolean isCA = (Boolean)cert.getFieldValue(IGTCertificateEntity.IS_CA); //TWX 20060728
      
      IEvent event = null;
      if(isPartner)
      {
        //event = new ImportCertificateEvent(certName,certFile);
        event = new ImportCertificateEvent(certName,certFile, relatedCertUid, isCA); // 20040326 DDJ
      }
      else
      { //The type of cert used for own certificates requires a password to be used
        String  password = cert.getFieldString(IGTCertificateEntity.PASSWORD);
        //event = new ImportCertificateEvent(certName, certFile, password);
        event = new ImportCertificateEvent(certName, certFile, password, relatedCertUid); // 20040326 DDJ
      }
      handleCreateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error importing certificate",t);
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_CERTIFICATE;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_CERTIFICATE;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetCertificateEvent(uid); //20030121AH
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetCertificateListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030415AH, 20030718AH
    return new RemoveCertificateEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_CERTIFICATE.equals(entityType))
    {
      return new DefaultCertificateEntity();
    }
    else if(IGTEntity.ENTITY_X500NAME.equals(entityType))
    {
      return new DefaultX500NameEntity();
    }
    else if(IGTEntity.ENTITY_CERTIFICATE_SWAPPING.equals(entityType))
    {
      return new DefaultCertificateSwappingEntity();
    }
    else
    {
      throw new GTClientException("Manager " + this
        + " cannot create entity objects for entity type " + entityType);
    }
  }

  protected IGTFieldMetaInfo[] defineVirtualFields(String entityType)
    throws GTClientException
  { //20030120AH
    if(IGTEntity.ENTITY_CERTIFICATE.equals(entityType))
    {
      VirtualSharedFMI[] sharedVfmi = new VirtualSharedFMI[4]; 
      {
        sharedVfmi[0] = new VirtualSharedFMI( "certificate.password",
                                              IGTCertificateEntity.PASSWORD);
        sharedVfmi[0].setCollection(false);
        sharedVfmi[0].setValueClass("java.lang.String");
        sharedVfmi[0].setDisplayableCreate(true);
        sharedVfmi[0].setDisplayableUpdate(false);
        sharedVfmi[0].setEditableCreate(true);
        sharedVfmi[0].setEditableUpdate(false);
        sharedVfmi[0].setMandatoryCreate(true);
        sharedVfmi[0].setMandatoryUpdate(false);
        ITextConstraint constraint = new TextConstraint(0,0);
        sharedVfmi[0].setConstraint(constraint);
      }
      {
        sharedVfmi[1] = new VirtualSharedFMI( "certificate.certFile",
                                              IGTCertificateEntity.CERT_FILE);
        sharedVfmi[1].setMandatoryCreate(true);
        sharedVfmi[1].setCollection(false);
        sharedVfmi[1].setValueClass("java.lang.String");
        Properties detail = new Properties();
        detail.setProperty("file.fixedKey","common.path.temp");
        detail.setProperty("file.downloadable","true");
        IFileConstraint constraint = new FileConstraint(detail);
        sharedVfmi[1].setConstraint(constraint);
      }
      {
        sharedVfmi[2] = new VirtualSharedFMI( "certificate.issuerDetails",
                                              IGTCertificateEntity.ISSUER_DETAILS);
        sharedVfmi[2].setCollection(false);
        sharedVfmi[2].setValueClass("java.lang.Object");
        sharedVfmi[2].setDisplayableCreate(true);
        sharedVfmi[2].setDisplayableUpdate(true);
        sharedVfmi[2].setEditableCreate(false);
        sharedVfmi[2].setEditableUpdate(false);
        sharedVfmi[2].setMandatoryCreate(false);
        sharedVfmi[2].setMandatoryUpdate(false);
        Properties detail = new Properties();
        detail.setProperty("embedded.type",IGTEntity.ENTITY_X500NAME);
        LocalEntityConstraint constraint = new LocalEntityConstraint(detail);
        sharedVfmi[2].setConstraint(constraint);
      }
      {
        sharedVfmi[3] = new VirtualSharedFMI( "certificate.subjectDetails",
                                              IGTCertificateEntity.SUBJECT_DETAILS);
        sharedVfmi[3].setCollection(false);
        sharedVfmi[3].setValueClass("java.lang.Object");
        sharedVfmi[3].setDisplayableCreate(true);
        sharedVfmi[3].setDisplayableUpdate(true);
        sharedVfmi[3].setEditableCreate(false);
        sharedVfmi[3].setEditableUpdate(false);
        sharedVfmi[3].setMandatoryCreate(false);
        sharedVfmi[3].setMandatoryUpdate(false);
        Properties detail = new Properties();
        detail.setProperty("embedded.type",IGTEntity.ENTITY_X500NAME);
        LocalEntityConstraint constraint = new LocalEntityConstraint(detail);
        sharedVfmi[3].setConstraint(constraint);        
      }
      return sharedVfmi;
    }
    else if(IGTEntity.ENTITY_X500NAME.equals(entityType))
    {
      VirtualSharedFMI[] sharedVfmi = new VirtualSharedFMI[_x500NameFields.length];
      for(int i=0; i < sharedVfmi.length; i++)
      {
        String name = _x500NameFields[i];
        Number number = _x500NameFieldIds[i];
        sharedVfmi[i] = StaticCtrlUtils.createReadOnlyTextSharedVFMI(name,number,0,0);
      }
      return sharedVfmi;
    }
    return new IGTFieldMetaInfo[0];
  }

  boolean isVirtual(String entityType)
  {
    if(IGTEntity.ENTITY_X500NAME.equals(entityType))
    {
      return true;
    }
    else
    {
      return super.isVirtual(entityType);
    }
  }

  void initVirtualEntityFields(String entityType,
                        AbstractGTEntity entity,
                        Map fieldMap)
    throws GTClientException
  { //20030120AH
    if(IGTEntity.ENTITY_CERTIFICATE.equals(entityType))
    {
      entity.setNewFieldValue(IGTCertificateEntity.ISSUER_DETAILS, UnloadedFieldToken.getInstance() );
      entity.setNewFieldValue(IGTCertificateEntity.SUBJECT_DETAILS, UnloadedFieldToken.getInstance() );
    }
  }

  protected void loadField(Number fieldId, AbstractGTEntity entity)
    throws GTClientException
  {  //20030122AH
    if(entity instanceof IGTCertificateEntity)
    {
      if( IGTCertificateEntity.ISSUER_DETAILS.equals(fieldId)
          || IGTCertificateEntity.SUBJECT_DETAILS.equals(fieldId) || 
           (entity.isNewEntity() && (IGTCertificateEntity.SERIAL_NUM.equals(fieldId) ||
          		                       IGTCertificateEntity.START_DATE.equals(fieldId) ||
          		                       IGTCertificateEntity.END_DATE.equals(fieldId)
          		                       )
           )
        )
      {
        Object[] certDetail = loadX500CertDetail( (IGTCertificateEntity)entity );
        entity.setNewFieldValue( IGTCertificateEntity.ISSUER_DETAILS, (IGTX500NameEntity)certDetail[0] );
        entity.setNewFieldValue( IGTCertificateEntity.SUBJECT_DETAILS, (IGTX500NameEntity)certDetail[1] );
        
        //TWX 26072006 populate the field serialNum, startDate, and endDate
        if(entity.isNewEntity())
        {
        	entity.setNewFieldValue( IGTCertificateEntity.SERIAL_NUM, (String)certDetail[2]);
        	entity.setNewFieldValue(IGTCertificateEntity.START_DATE, (Date)certDetail[3]);
        	entity.setNewFieldValue(IGTCertificateEntity.END_DATE, (Date)certDetail[4]);
        }
        
        return;
      }
    }
    //If execution reaches this point, the vField was not loaded. Call back to superclass
    //which will throw an appropriate exception
    super.loadField(fieldId, entity);
  }

  public String prepareExportFile(IGTCertificateEntity cert, String baseFilename)
    throws GTClientException
  { //20030124AH
    //Fires event to gtas telling it to create a physical file for us to download.
    //Nb: at this stage support has not been provided by the b-tier for cleaning this file up
    //after use.
    try
    {
      //String certName = cert.getFieldString(cert.NAME);
      Long uid = cert.getUidLong();
      IEvent event = new ExportCertificateEvent(uid, baseFilename);
      Map retval = (Map)handleEvent(event); //A map is returned. It may not be highly efficient
      //but this operation doesnt occur in bulk and using a map is much more extensible than just
      //returning a string - if we did that for sure tomorrow would need to return another bit of
      //info!
      return (String)retval.get(ICertificate.EXPORT_CERTIFICATE_FILENAME);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error preparing export file for certificate:" + cert,t);
    }
  }

  public void changePrivateCertPassword(String oldPassword, String newPassword)
    throws GTClientException
  { //20030414AH
    try
    {
      assertLogin();
      IEvent event = new ChangePrivateCertificatePasswordEvent(oldPassword,newPassword);
      handleEvent(event);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error changing private certificate password",t);
    }
  }

  public void exportKeyStore(Long uid) throws GTClientException
  { //20030415AH
    try
    {
      assertLogin();
      IEvent event = new ExportCertificateKeyStoreEvent(uid);
      handleEvent(event);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error exporting certificate with uid="
                                  + uid + " to key store",t);
    }
  }

  public void exportTrustStore(Long uid) throws GTClientException
  { //20030415AH
    try
    {
      assertLogin();
      IEvent event = new ExportCertificateTrustStoreEvent(uid);
      handleEvent(event);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error exporting certificate with uid="
                                  + uid + " to trust store",t);
    }
  }
  
  public IGTListPager getListPager(Boolean isPartner) throws GTClientException
  { //20030703AH
    try
    {
      if(isPartner == null)
      {
        return getListPager();
      }
      else
      {
        return getListPager(isPartner, IGTCertificateEntity.IS_PARTNER);
        /*
        ***Later we may need to do a bit of extra filtering for gridtalk & gridmaster certs so
        *they only show in the all certs view. When that happens uncomment this and expand the
        *query accordingly replacing XXX as appropriate...
        
        ListPager pager = null;
        
        pager = new ListPager();
        try
        {
          DataFilterImpl filter = new DataFilterImpl();
          filter.addSingleFilter( null,
                                  ICertificate.IS_PARTNER,
                                  filter.getEqualOperator(),
                                  isPartner,
                                  false);
          filter.addSingleFilter( filter.getAndConnector(),
                                  XXX,
                                  filter.getEqualOperator(),
                                  XXX,
                                  false);
          pager.setFilter(filter);
        }
        catch(Throwable t)
        {
          throw new GTClientException("Error constructing IDataFilter instance for use in Certificate ListPager",t);
        }
        try
        {
          pager.setManager(this);
          pager.setSession(_session);
        }
        catch(Throwable t)
        {
          throw new GTClientException("Error initialising Certificate ListPager:" + pager,t);
        }
        return pager;*/
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Unable to obtain certificate ListPage where isPartner=" + isPartner,t);
    }
  }
}