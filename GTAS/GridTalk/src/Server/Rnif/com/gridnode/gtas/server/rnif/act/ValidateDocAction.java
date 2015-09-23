package com.gridnode.gtas.server.rnif.act;

import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rnif.helpers.DocumentUtil;
import com.gridnode.gtas.server.rnif.helpers.Logger;
import com.gridnode.gtas.server.rnif.helpers.ProcessUtil;
import com.gridnode.gtas.server.rnif.helpers.ProfileUtil;
import com.gridnode.gtas.server.rnif.helpers.RnifException;
import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.pdip.app.rnif.model.ProcessAct;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.base.rnif.exception.RosettaNetException;
import com.gridnode.pdip.base.rnif.helper.IRnifPathConfig;
import com.gridnode.pdip.base.rnif.helper.XMLUtil;
import com.gridnode.pdip.framework.file.helpers.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ValidateDocAction
{
  static final int FULL_DTD_PATH= 0;
  static final int FULL_DIC_PATH= 1;
  static final int FULL_UDOC_PATH= 2;
  static final int FULL_SCHEMA_PATH= 3;

  static final int PATHARRAY_SIZE= 4;

  static final int MAX_ERRORS= 10;

  public ValidateDocAction()
  {
  }

  public void execute(GridDocument gDoc, ProcessDef def, ProcessAct curAct)
    throws RosettaNetException
  {
    String[] paths= new String[PATHARRAY_SIZE];
    setPaths(gDoc, def, curAct, paths);
    validate(gDoc, def, curAct, paths);
  }

  void setPaths(GridDocument gDoc, ProcessDef def, ProcessAct pAct, String[] fullPaths)
    throws RosettaNetException
  {
    try
    {
      //the validation is done before the GridDocument'd folder field is set. so we need to set it before call the action
      fullPaths[FULL_UDOC_PATH]= DocumentUtil.getFullUDocPath(gDoc);
      boolean dtdexit= false;
      String dtd= getDTD(pAct);
      if (dtd != null)
      {
        File file= FileUtil.getFile(IRnifPathConfig.PATH_DTD, dtd);
        if (file != null)
        {
          fullPaths[FULL_DTD_PATH]= file.getAbsolutePath();
          dtdexit= true;
        }
        else
          Logger.warn("Will not do dtd validation because cannot found dtd:" + dtd);
      }

      String dic= getDic(pAct);
      if (dic != null && dtdexit)
      {
        File file= FileUtil.getFile(IRnifPathConfig.PATH_DICTIONARY, dic);
        if (file != null)
          fullPaths[FULL_DIC_PATH]= file.getAbsolutePath();
        else
          Logger.warn("Will not do dictionary validation because cannot found dictionary:" + dic);
      }

      String schema= getSchema(pAct);
      if (schema != null && dtdexit)
      {
        File file= FileUtil.getFile(IRnifPathConfig.PATH_SCHEMA, schema);
        if (file != null)
          fullPaths[FULL_SCHEMA_PATH]= file.getAbsolutePath();
        else
          Logger.warn("Will not do schema validation because cannot found schema:" + schema);
      }
    }
    catch (Throwable ex)
    {
      throw RosettaNetException.unpSconValErr("Cannot get config file!", ex);
    }

    Logger.debug("  dtd    = " + fullPaths[FULL_DTD_PATH]);
    Logger.debug("  dic    = " + fullPaths[FULL_DIC_PATH]);
    Logger.debug("  schema = " + fullPaths[FULL_SCHEMA_PATH]);

  }

  void validate(GridDocument gDoc, ProcessDef def, ProcessAct pAct, String[] fullPaths)
    throws RosettaNetException
  {
    boolean validateDic= (fullPaths[FULL_DIC_PATH] != null);
    boolean validateDTD=
      (fullPaths[FULL_DTD_PATH] != null && !Boolean.TRUE.equals(pAct.getDisableDTD()));
    boolean validateSchema=
      (fullPaths[FULL_SCHEMA_PATH] != null && !Boolean.TRUE.equals(pAct.getDisableSchema()));

    ArrayList errlist= new ArrayList();

    if (validateDTD)
    {
      // Validate DTD
      XMLUtil.validateDTD(fullPaths[FULL_UDOC_PATH], fullPaths[FULL_DTD_PATH], errlist);
    }

    if (validateDic)
    {
      XMLUtil.validateDictionary(
        fullPaths[FULL_UDOC_PATH],
        fullPaths[FULL_DIC_PATH],
        fullPaths[FULL_DTD_PATH],
        errlist);
    }

    if (validateSchema)
    {
      XMLUtil.validateSchema(fullPaths[FULL_UDOC_PATH], fullPaths[FULL_SCHEMA_PATH], fullPaths[FULL_DTD_PATH], errlist);
    }

    try
    {
      furtherValidate(gDoc, pAct, fullPaths, errlist);
    }
    catch (RosettaNetException gnEx)
    {
      if (RosettaNetException.UNP_SHDR_MNFSTERR.equals(gnEx.getExType()) || RosettaNetException.ACK_DIGEST_DIFF_CODE.equals(gnEx.getExType())) //TWX 20090811 #841 throw err if digest not match
        throw gnEx;
    }

    if (errlist != null && !errlist.isEmpty())
    {
      handleValidationException(errlist);
    }
  }

  void handleValidationException(ArrayList errlist) throws RosettaNetException
  {
    // handle errors
    Iterator i= errlist.iterator();
    StringBuffer errStrBuf= new StringBuffer();
    while (i.hasNext())
    {
      errStrBuf.append(i.next());
      errStrBuf.append("\n");
    }
    throw RosettaNetException.unpSconValErr(errStrBuf.toString());
  }

  String getFileName(Long mappingFileUid) throws RnifException
  {
    try
    {
      return ProcessUtil.getMappingFileName(mappingFileUid);
    }
    catch (Exception ex)
    {
      throw RnifException.fileProcessErr("Error in get ProcessMsgType's actual filename", ex);
    }
  }

  String getDTD(ProcessAct pAct) throws RnifException
  {
    return getFileName(pAct.getMsgType());
  }

  String getDic(ProcessAct pAct) throws RnifException
  {
    return getFileName(pAct.getDictFile());
  }

  String getSchema(ProcessAct pAct) throws RnifException
  {
    return getFileName(pAct.getXMLSchema());
  }

  void furtherValidate(GridDocument gDoc, ProcessAct pAct, String[] fullPaths, List errlist)
    throws RosettaNetException
  {
    if (gDoc.getSenderPartnerId() != null && !"".equals(gDoc.getSenderPartnerId()))
    {
      //only for received document.
      checkManifest(gDoc);
    }
  }

  /**
   * Checks that the number of attachments match the number specified in the manifest.
   */
  private void checkManifest(GridDocument gdoc) throws RosettaNetException
  {
    int specifiedatts= 0;
    int physicalatts= 0;
    RNProfile profile= null;
    try
    {
      profile= new ProfileUtil().getProfileMustExist(gdoc);
    }
    catch (RnifException ex)
    {
      throw RosettaNetException.unpShdrMnftErr(
        "Can not found the RNProfile of GridDoc "
          + gdoc.getEntityDescr()
          + "; with RNProfileUID="
          + gdoc.getRnProfileUid());
    }
    Integer specNum= profile.getNumberOfAttas();
    specifiedatts= specNum == null ? 0 : specNum.intValue();

    Logger.debug("checkManifest: NumberOfAttas defined in serviceheader = " + specifiedatts);

    List attachList= gdoc.getAttachments();
    if (attachList != null)
    {
      physicalatts= attachList.size();
    }

    if (specifiedatts != physicalatts)
    {
      throw RosettaNetException.unpShdrMnftErr(
        "Wrong number of attachments specified in the manifest, "
          + "expected["
          + physicalatts
          + "], found["
          + specifiedatts
          + "]");
    }
  }

  //     if(pAct.getUserProcedureUId() == null)
  //       return;
  //     try
  //      {
  //        triggerProcedure(event.getDocument(), pAct.getUserProcedureUId());
  //      }
  //      catch(ThrowableAlert alert)
  //     {
  //       GNException gnEx = alert.getException();
  //       String  invokeExStr= null;
  //       GNException outEx;
  //       Throwable innerEx = gnEx;
  //       while(true)
  //       {
  //         if(innerEx instanceof GNException)
  //         {
  //           outEx = (GNException) innerEx;
  //           innerEx = outEx.getNestedException();
  //         }else
  //           break;
  //       }
  //
  //       if(innerEx instanceof InvocationTargetException)
  //       {
  //        InvocationTargetException invokeEx = (InvocationTargetException)innerEx;
  //        innerEx = invokeEx.getTargetException();
  //        if(innerEx != null)
  //          invokeExStr = innerEx.toString();
  //       }
  //
  //       String errMsg = gnEx.getMessage() ;
  //       if(invokeExStr != null)
  //         errMsg += ": " + invokeExStr;
  //       errlist.add(errMsg);
  //       Logger.error("Exception in calling user procedure! ", gnEx);
  //       //throw gnEx;
  //      }
  //      catch (Throwable ex) {
  //            String errMsg = ex.getMessage();
  //            errlist.add(errMsg);
  //            Logger.error("error in calling user procedure! ", ex);
  //      }
  //  }
  //
  //   private Hashtable triggerProcedure(
  //    GridDocument srcGDoc,
  //    Integer procedureUId)
  //    throws ThrowableAlert
  //  {
  //    List workflowActionList = new ArrayList();
  //    List gridDocList = new ArrayList();
  //
  //    WFAction wfAction = new WFAction(
  //                              WFAction.ACTION_USERPROC,
  //                              new Long(procedureUId.longValue()));
  //    workflowActionList.add(wfAction);
  //    gridDocList.add(srcGDoc);
  //
  //    Hashtable handlerInfo = new Hashtable();
  //    handlerInfo.put(IWFActionHandler.GRIDDOC_LIST, gridDocList);
  ////    handlerInfo.put(IWFActionHandler.SRC_FOLDER, srcFolder);
  ////    handlerInfo.put(IWFActionHandler.TGT_FOLDER, tgtFolder);
  //    handlerInfo.put(IWFActionHandler.TRANSFORM_GDOC, Boolean.FALSE);
  //    handlerInfo.put(IWFActionHandler.DELETE_SRCDOC, Boolean.FALSE);
  //    handlerInfo.put(IWFActionHandler.WF_ACTION, wfAction);
  //
  //
  //    IWFActionHandler handler =  new UserProcedureHandler();
  //
  //    handler.setHandlerInfo(handlerInfo);
  //    handler.handle();
  //    return handler.getHandlerInfo();
  //
  //  }

}