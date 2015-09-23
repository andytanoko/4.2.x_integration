package com.gridnode.gtas.server.rnif.helpers;

import java.util.Collection;
import java.util.Iterator;

import com.gridnode.gtas.server.document.helpers.BizRegDelegate;
import com.gridnode.gtas.server.document.model.DocChannelInfo;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.partnerprocess.facade.ejb.IPartnerProcessManagerHome;
import com.gridnode.gtas.server.partnerprocess.facade.ejb.IPartnerProcessManagerObj;
import com.gridnode.gtas.server.partnerprocess.model.BizCertMapping;
import com.gridnode.gtas.server.partnerprocess.model.IProcessMapping;
import com.gridnode.gtas.server.partnerprocess.model.ProcessMapping;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerHome;
import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerObj;
import com.gridnode.pdip.app.mapper.model.MappingFile;
import com.gridnode.pdip.app.rnif.facade.ejb.IRNProcessDefManagerHome;
import com.gridnode.pdip.app.rnif.facade.ejb.IRNProcessDefManagerObj;
import com.gridnode.pdip.app.rnif.model.ProcessAct;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.base.rnif.helper.RNCertInfo;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class ProcessUtil
{

  //  static ProcessDef addProcessDef(ProcessDef def) throws RnifException
  //  {
  //    Long uid;
  //    try
  //    {
  //      IRNProcessDefManagerObj defMgr = getProcessDefMgr();
  //      uid= defMgr.createProcessDef(def);
  //      return defMgr.findProcessDef(uid);
  //    }
  //    catch (Exception e)
  //    {
  //      throw RnifException.processdefNotFoundEx("def", new Exception());
  //    }
  //  }

  //  static ProcessDef getProcessDef(Long defUid)
  //  {
  //    return getProcessDefMgr().findProcessDef(defUid);;
  //  }

  static public ProcessDef getProcessDef(String defName) throws RnifException
  {
    ProcessDef def;
    try
    {
      def= getProcessDefMgr().findProcessDefByName(defName);
    }
    catch (Exception ex)
    {
      throw RnifException.processdefNotFoundEx("ProcessName is " + defName, ex);
    }
    if (def == null)
      throw RnifException.processdefNotFoundEx("ProcessName is " + defName, new Exception());
    return def;
  }

  public static IRNProcessDefManagerObj getProcessDefMgr() throws ServiceLookupException
  {
    return (IRNProcessDefManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
      IRNProcessDefManagerHome.class.getName(),
      IRNProcessDefManagerHome.class,
      new Object[0]);
  }

  public static ProcessAct getSenderCurAct(ProcessDef def, boolean isInitiator, boolean isSignal)
  {
    boolean isRequestAct= isInitiator != isSignal;
    return isRequestAct ? def.getRequestAct() : def.getResponseAct();
  }

  public static IPartnerProcessManagerObj getPartnerProcessManager() throws ServiceLookupException
  {
    return (IPartnerProcessManagerObj) ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IPartnerProcessManagerHome.class.getName(),
      IPartnerProcessManagerHome.class,
      new Object[0]);
  }

  public static IMappingManagerObj getMappingManager() throws ServiceLookupException
  {
    return (IMappingManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
      IMappingManagerHome.class.getName(),
      IMappingManagerHome.class,
      new Object[0]);
  }

  static String getProcessDefName(
    String pipCode,
    String version,
    String partnerId,
    Boolean isInitiator,
    GridDocument gDoc)
    throws FindEntityException
  {
    try
    {
      Number[] fieldIds=
        new Number[] {
          IProcessMapping.PROCESS_INDICATOR_CODE,
          IProcessMapping.PROCESS_VERSION_ID,
          IProcessMapping.PARTNER_ID,
          IProcessMapping.IS_INITIATING_ROLE };
      Object[] values= new Object[] { pipCode, version, partnerId, isInitiator };

      IPartnerProcessManagerObj partnerProcessMgr= getPartnerProcessManager();

      IDataFilter filter= EntityUtil.getEqualFilter(fieldIds, values);
      Collection list= partnerProcessMgr.findProcessMappingByFilter(filter);
      if (list == null || list.isEmpty())
        throw new Exception("FindProcessMappingByFilter " + filter + " return  empty result!");

      Iterator iter= list.iterator();
      ProcessMapping mapping= (ProcessMapping) iter.next();

      if (gDoc != null)
        gDoc.setUdocDocType(mapping.getDocumentType());
      return mapping.getProcessDef();

    }
    catch (Exception ex)
    {
      throw new FindEntityException("Error in finding ProcessDef: "+ex.getMessage(), ex);
    }

  }

  public static DocChannelInfo getPartnerProcessChannelInfo(
    String defName,
    String partnerId,
    Boolean isInitiator)
    throws Exception
  {
    DocChannelInfo channelInfo= null;
    if (partnerId != null)
    {
      Number[] fieldIds=
        new Number[] {
          IProcessMapping.PROCESS_DEF,
          IProcessMapping.PARTNER_ID,
          IProcessMapping.IS_INITIATING_ROLE };
      Object[] values= new Object[] { defName, partnerId, isInitiator };

      IPartnerProcessManagerObj partnerProcessMgr= getPartnerProcessManager();

      IDataFilter filter= EntityUtil.getEqualFilter(fieldIds, values);
      Collection list= partnerProcessMgr.findProcessMappingByFilter(filter);
      if (list == null || list.isEmpty())
        throw new Exception("getPartnerProcessChannelInfo " + filter + " return  empty result!");

      Iterator iter= list.iterator();
      ProcessMapping mapping= (ProcessMapping) iter.next();

      Long channelUid= mapping.getSendChannelUID();
      if (channelUid != null)
      {
        ChannelInfo cInfo= BizRegDelegate.getChannelManager().getChannelInfo(channelUid);
        if (cInfo != null)
        {
          channelInfo= new DocChannelInfo();
          channelInfo.setChannelUid(new Long(cInfo.getUId()));
          channelInfo.setChannelName(cInfo.getName());
          channelInfo.setChannelProtocol(cInfo.getTptProtocolType());
        }
      }
    }
    return channelInfo;
  }

  public static String getMappingFileName(Long mappingFileUid) throws Exception
  {
    if (mappingFileUid == null)
      return null;
    MappingFile mappingFile= getMappingManager().findMappingFile(mappingFileUid);

    String res= null;
    String subDir = null; //040309NSL 
    if (mappingFile != null)
    {
      res= mappingFile.getFilename();
      subDir = mappingFile.getSubPath(); //040309NSL 
      if (res != null)
      {
        res= res.trim();
        subDir = (subDir == null? "" : subDir.trim()); //040309NSL 
        if (res.length() > 0)
          return subDir.concat(res);  //040309NSL 
      }
    }
    return null;
  }

  //SecurityInfo
  public static RNCertInfo getBizCertMappingForPartner(String partnerId) throws Exception
  {
    RNCertInfo certInfo= new RNCertInfo();
    if (partnerId == null || partnerId.trim().length() == 0)
      return certInfo;

    IDataFilter filter= new DataFilterImpl();
    filter.addSingleFilter(null,BizCertMapping.PARTNER_ID, filter.getEqualOperator(),  partnerId, false);
    Collection  mappingCol= getPartnerProcessManager().findBizCertMappingByFilter(filter);
    if (mappingCol == null || mappingCol.isEmpty())
      return certInfo;
    BizCertMapping certMapping = (BizCertMapping)mappingCol.iterator().next();
    certInfo.set_ownEncryptCertificate(certMapping.getOwnCert());
    certInfo.set_ownSignCertificate(certMapping.getOwnCert());
    certInfo.set_partnerEncryptCertificate(certMapping.getPartnerCert());
    certInfo.set_partnerSignCertificate(certMapping.getPartnerCert());
    return certInfo;
  }
  /*
  private static X509Certificate extractX509Certificate(Certificate cert)
  {
    if (cert == null)
      return null;
    Logger.debug("[extractX509Certificate] cert uid is " + cert.getUId());
    X509Certificate X509cert= GridCertUtilities.loadX509CertificateByString(cert.getCertificate());
    return X509cert;
  }*/
}
