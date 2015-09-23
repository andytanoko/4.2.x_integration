/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserProcedureManagerBean.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * AUG 01 2002    Jagadeesh              Created
 * Jul 15 2003    Neo Sok Lay             Add methods:
 *                                        getProcedureDefFileKeys(IDataFilter),
 *                                        getUserProcedureKeys(IDataFilter)
 */

package com.gridnode.pdip.base.userprocedure.facade.ejb;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.pdip.base.userprocedure.exceptions.UserProcedureExecutionException;
import com.gridnode.pdip.base.userprocedure.handler.ProcedureHandlerDelegate;
import com.gridnode.pdip.base.userprocedure.helpers.Logger;
import com.gridnode.pdip.base.userprocedure.helpers.ProcedureDefFileEntityHandler;
import com.gridnode.pdip.base.userprocedure.helpers.SoapCallHelper;
import com.gridnode.pdip.base.userprocedure.helpers.UserProcedureEntityHandler;
import com.gridnode.pdip.base.userprocedure.model.*;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.EntityModifiedException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.util.classloader.AbstractMultiClassLoader;

/**
 * This bean manages the ProcedureDefinition.
 *
 * @author Jagadeesh
 *
 * @version 2.0
 * @since 2.0
 */


public class UserProcedureManagerBean  implements SessionBean
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4318547886543668540L;
	private SessionContext _sessionCtx = null;
  private static final String CLASS_NAME = "UserProcedureManagerBean";

  public void setSessionContext(SessionContext sessionCtx)
  {
    _sessionCtx = sessionCtx;
  }

  public void ejbCreate() throws CreateException
  {
  }

  public void ejbRemove()
  {
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

  // ************************ Implementing methods in IProcedureDefManagerObj *****/


/**
 * Creats a new <code>UserProcedure</code> entity.
 *
 * @param userProcedure - The <code>UserProcedure</code> entity.
 *
 * @throws CreateEntityException - Thrown when <code>userProcedure</code> could not
 *                                 be created.
 * @throws SystemException - Thrown when System Level Exception is caused.
 */



  public Long createUserProcedure(UserProcedure userProcedure)
    throws CreateEntityException,SystemException
  {
    try
    {
      Logger.log("[UserProcedureManagerBean.createUserProcedure] Enter");
      if(userProcedure != null)
      {
        if(!checkProcedureDefFileExist(new Long(userProcedure.getProcedureDefFile().getUId())))
            throw new CreateEntityException("Procedure Definition File Dose Not Exists !");
        return new Long(((UserProcedure)UserProcedureEntityHandler.getInstance().createEntity(userProcedure)).getUId());
      }
      else
      {
        Logger.log("[UserProcedureManagerBean.createUserProcedure]"+
        "- UserProcedure Entity is Null ");
        return null;
     }
    }
    catch (CreateException ex)
    {
      Logger.warn("["+CLASS_NAME+"].createUserProcedure] BL Exception", ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("["+CLASS_NAME+"].createUserProcedure] Error", ex);
      throw new SystemException(
        "["+CLASS_NAME+"].createUserProcedure] Error",
        ex);
    }
    finally
    {
      Logger.log("["+CLASS_NAME+"].createUserProcedure] Exit");
    }
  }


  /**
   * To update changes to an existing <code>UserProcedure</code> entity.
   *
   * @param           userProcedure    The modified <code>UserProcedure</code> entity.
   *
   * @exception       Thrown when the update operation fails.
   *
   * @since 2.0
   * @version 2.0
   */
  public void updateUserProcedure(UserProcedure userProcedure)
    throws    UpdateEntityException, SystemException
  {
    try
    {
      Logger.log("[UserProcedureManagerBean.updateUserProcedure] Enter");
      if(!checkProcedureDefFileExist(new Long(userProcedure.getProcedureDefFile().getUId())))
        throw new UpdateEntityException("Procedure Definition File Dose Not Exists !");

      UserProcedureEntityHandler.getInstance().update(userProcedure);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("["+CLASS_NAME+"].updateUserProcedure() BL Exception",ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable t)
    {
      Logger.warn("["+CLASS_NAME+"].updateUserProcedure() Error",t);
      throw new SystemException(
         "["+CLASS_NAME+"].updateUserProcedure() Error", t);
    }
    finally
    {
      Logger.log(CLASS_NAME+" updateUserProcedure Exit ");
    }
  }


  /**
   * To remove an existing <code>UserProcedure</code>  entity.
   *
   * @param           uID  the uId of the <code>UserProcedure</code> entity to be deleted.
   *
   * @exception       Thrown when the delete operation fails.
   *
   * @since 2.0
   * @version 2.0
   */
  public void deleteUserProcedure(Long uID)
    throws DeleteEntityException, SystemException
  {
    try
    {
      Logger.log("[UserProcedureManagerBean.deleteUserProcedure] Enter");
      UserProcedureEntityHandler.getInstance().remove(uID);
    }
    catch (RemoveException ex)
    {
      Logger.warn("["+CLASS_NAME+"].deleteUserProcedure() BL Exception",ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable t)
    {
      Logger.warn("["+CLASS_NAME+"].deleteUserProcedure() Error",t);
      throw new SystemException(
         "["+CLASS_NAME+"].deleteUserProcedure() Error", t);
    }
    finally
    {
      Logger.log(CLASS_NAME+" deleteUserProcedure Exit ");
    }
  }


  /**
   * To retrieve a collection of <code>UserProcedure</code>
   * entity with the specified filter.
   *
   * @param           filter   the filter used to retrieve the <code>UserProcedure</code> entity.
   *
   * @return          A collection of <code>UserProcedure<code> entities.
   *
   * @exception       Thrown when the retrieval operation fails.
   *
   * @since 2.0
   * @version 2.0
   */

  public Collection getUserProcedure(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    try
    {
      Logger.log("[UserProcedureManagerBean.getUserProcedure] Enter");
      Logger.log("["+CLASS_NAME+" getUserProcedure"+
                         "filter: " + ((filter==null)?null:filter.getFilterExpr()));
      return UserProcedureEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);
    }
    catch (ApplicationException ax)
    {
      Logger.warn("["+CLASS_NAME+"].getUserProcedure() BL Exception",ax);
      throw new FindEntityException(ax);
    }
    catch (SystemException sx)
    {
      Logger.warn("["+CLASS_NAME+"].getUserProcedure() SystemException",sx);
      throw sx;
    }
    catch (Throwable ex)
    {
      Logger.warn("["+CLASS_NAME+"].getUserProcedure() Error",ex);
      throw new SystemException(
         "["+CLASS_NAME+"].getUserProcedure() Error", ex);
    }
    finally
    {
      Logger.log(CLASS_NAME+" getUserProcedure() Exit ");
    }
  }

  /**
   * To retrieve a collection of UIDs of the <code>UserProcedure</code> entities with the specified filter.
   * 
   * @param filter The filtering condition.
   * @return Collection of UIDs of the UserProcedure entities that satisfy the filtering
   * condition.
   * @since GT 2.2 I1
   */
  public Collection getUserProcedureKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    try
    {
      Logger.log("[UserProcedureManagerBean.getUserProcedureKeys] Enter");
      Logger.log("[UserProcedureManagerBean.getUserProcedureKeys]"+
                         "filter: " + ((filter==null)?null:filter.getFilterExpr()));
      return UserProcedureEntityHandler.getInstance().getKeyByFilterForReadOnly(filter);
    }
    catch (ApplicationException ax)
    {
      Logger.warn("[UserProcedureManagerBean.getUserProcedureKeys] BL Exception",ax);
      throw new FindEntityException(ax);
    }
    catch (SystemException sx)
    {
      Logger.warn("[UserProcedureManagerBean.getUserProcedureKeys] SystemException",sx);
      throw sx;
    }
    catch (Throwable ex)
    {
      Logger.warn("[UserProcedureManagerBean.getUserProcedureKeys] Error",ex);
      throw new SystemException(
         "[UserProcedureManagerBean.getUserProcedureKeys] Error", ex);
    }
    finally
    {
      Logger.log("[UserProcedureManagerBean.getUserProcedureKeys] Exit ");
    }
  }


  /**
   * To retrieve a <code>UserProcedure</code>
   * with the specified uId.
   *
   * @param           uID   the uId of the <code>UserProcedure</code> entity.
   *
   * @return          The <code>UserProcedure<code> entity.
   *
   * @exception       Thrown when the retrieval operation fails.
   *
   * @since 2.0
   * @version 2.0
   */

  public UserProcedure getUserProcedure(Long uID)
    throws FindEntityException, SystemException
  {
    try
    {
      Logger.log("[UserProcedureManagerBean.getUserProcedure] Enter "+
      "UID "+uID);

      return (UserProcedure) UserProcedureEntityHandler.getInstance().getEntityByKeyForReadOnly(uID);
    }
    catch (ApplicationException ax)
    {
      Logger.warn("["+CLASS_NAME+"].getUserProcedure() BL Exception",ax);
      throw new FindEntityException(ax);
    }
    catch (SystemException sx)
    {
      Logger.warn("["+CLASS_NAME+"].getUserProcedure() SystemException",sx);
      throw sx;
    }
    catch (Throwable ex)
    {
      Logger.warn("["+CLASS_NAME+"].getUserProcedure() Error",ex);
      throw new SystemException(
         "["+CLASS_NAME+"].getUserProcedure() Error", ex);
    }
    finally
    {
      Logger.log(CLASS_NAME+" getUserProcedure(uID) Exit ");
    }
  }




  /**
   * To check if the specified <code>ProcedureDefFile</code> enity exist in the database.
   *
   * @param           procedureDefFileUId   the uId of <code>ProcedureDefFile</code> entity.
   *
   * @return          true if the <code>ProcedureDefFile</code> entity exist, else false.
   *
   * @SystemException Thrown when the retrieval operation fails.
   *
   * @since 2.0
   * @version 2.0
   *
   */


  public boolean checkProcedureDefFileExist(Long uID) throws SystemException
  {
     try
     {
         return (getProcedureDefFileByUId(uID) != null);
     }
    catch (FindEntityException ex)
    {
      Logger.log("["+CLASS_NAME+"].checkProcedureDefFileExist()"+
                "ProcedureDefFile with " + uID + "does not exist!" +ex.getMessage());
      return false;
    }
  }


  /**
   * Retrieves a <code>ProcedureDefFile</code> entity with the specified uId.
   *
   * @param uid  - UId of <code>ProcedureDefFile</code> entity.
   *
   * @return -  The <code>ProcedureDefFile</code> entity.
   *
   * @throws FindEntityException - Thrown when the entity is not Found.
   *
   * @throws SystemException - Thrown when System Level Exception is throwed.
   */
  public ProcedureDefFile getProcedureDefFileByUId(Long uID)
    throws FindEntityException, SystemException
  {
    try
    {
       return (ProcedureDefFile)ProcedureDefFileEntityHandler.getInstance().getEntityByKey(uID);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("["+CLASS_NAME+"].getProcedureDefFileByUId() BL EXCEPTION",ex);
      throw new FindEntityException(ex);
    }
    catch (SystemException ex)
    {
      Logger.warn("["+CLASS_NAME+"].getProcedureDefFileByUId() System EXCEPTION",ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("["+CLASS_NAME+"].getProcedureDefFileByUId() Error",ex);
      throw new SystemException(
        CLASS_NAME+".getProcedureDefFileByUId() Error ", ex);
    }
    finally
    {
      Logger.log("["+CLASS_NAME+".getProcedureDefFileByUId() Exit ");
    }

  }


/***********************  Create,Update,Delete,Getter Methods for ProcedureDefinition File **/


  /**
   * Creats a new <code>ProcedureDefFile</code> entity.
   *
   * @param procedureDefFile - The <code>ProcedureDefFile</code> entity.
   *
   * @throws CreateEntityException - Thrown when <code>ProcedureDefFile</code> could not
   *                                 be created.
   * @throws SystemException - Thrown when System Level Exception is caused.
   */
  public Long createProcedureDefinitionFile(ProcedureDefFile procedureDefFile)
    throws CreateEntityException,SystemException
  {
    try
    {
      Logger.log("[UserProcedureManagerBean.createProcedureDefinitionFile] Enter");
       return new Long( ((ProcedureDefFile)ProcedureDefFileEntityHandler.getInstance().createEntity(procedureDefFile)).getUId());
    }
    catch (CreateException ex)
    {
      Logger.warn("["+CLASS_NAME+"].createProcedureDefinitionFile] BL Exception", ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("["+CLASS_NAME+"].createProcedureDefinitionFile] Error", ex);
      throw new SystemException(
        "["+CLASS_NAME+"].createProcedureDefinitionFile] Error",
        ex);
    }
    finally
    {
      Logger.log("["+CLASS_NAME+"].createProcedureDefinitionFile] Exit");
    }


  }

  /**
   * To remove an existing <code>ProcedureDefFile</code> entity.
   *
   * @param     uID   the uId of the <code>ProcedureDefFile</code>
   * entity to be deleted.
   *
   * @exception       Thrown when the delete operation fails.
   *
   * @since 2.0
   * @version 2.0
   */
  public void deleteProcedureDefinitionFile(Long uID)
    throws DeleteEntityException, SystemException
  {
    try
    {
       Logger.log("[UserProcedureManagerBean.deleteProcedureDefinitionFile] Enter");
       if(checkDepUserProcedure(uID))
         throw new DeleteEntityException("Cannot Delete ProcedureDefiniton File"+
          "Dependent UserProcedure Exists ");

       ProcedureDefFile procDefFile = getProcedureDefinitionFile(uID);
       FileUtil.delete(procDefFile.getFilePath(), procDefFile.getFileName());
       ProcedureDefFileEntityHandler.getInstance().remove(uID);
    }
    catch (RemoveException ex)
    {
      Logger.warn("["+CLASS_NAME+"].deleteProcedureDefinition() BL Exception",ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable t)
    {
      Logger.warn("["+CLASS_NAME+"].deleteProcedureDefinition() Error",t);
      throw new SystemException(
         "["+CLASS_NAME+"].deleteProcedureDefinition() Error", t);
    }
    finally
    {
      Logger.log(CLASS_NAME+" deleteProcedureDefinition Exit ");
    }
  }


  /**
   * To update changes to an existing <code>ProcedureDefFile</code> entity.
   *
   * @param           procedureDefFile    The modified <code>ProcedureDefFile</code> entity.
   *
   * @exception       Thrown when the update operation fails.
   *
   * @since 2.0
   * @version 2.0
   */
  public void updateProcedureDefinitionFile(ProcedureDefFile procedureDefFile)
    throws    UpdateEntityException, SystemException
  {
    try
    {
      Logger.log("[UserProcedureManagerBean.updateProcedureDefinitionFile] Enter");
      ProcedureDefFileEntityHandler.getInstance().update(procedureDefFile);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("["+CLASS_NAME+"].updateProcedureDefinitionFile() BL Exception",ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable t)
    {
      Logger.warn("["+CLASS_NAME+"].updateProcedureDefinitionFile Error",t);
      throw new SystemException(
         "["+CLASS_NAME+"].updateProcedureDefinitionFile Error", t);
    }
    finally
    {
      Logger.log(CLASS_NAME+" updateProcedureDefinitionFile Exit ");
    }
  }

  /**
   * To retrieve a <code>ProcedureDefFile</code> ProcedureDefinitionFile entity
   * with the specified uId.
   *
   * @param           uID   the uId of the <code>ProcedureDefFile</code> entity.
   *
   * @return          The <code>ProcedureDefFile<code> ProcedureDefinitionFile entity.
   *
   * @exception       Thrown when the retrieval operation fails.
   *
   * @since 2.0
   * @version 2.0
   */
  public ProcedureDefFile getProcedureDefinitionFile(Long uID)
    throws    FindEntityException, SystemException
  {

    try
    {
      Logger.log("[UserProcedureManagerBean.getProcedureDefinitionFile] Enter "+
      "UID "+uID);

      return (ProcedureDefFile)ProcedureDefFileEntityHandler.getInstance().getEntityByKeyForReadOnly(uID);
    }
    catch (ApplicationException ax)
    {
      Logger.warn("["+CLASS_NAME+"].getProcedureDefinitionFile() BL Exception",ax);
      throw new FindEntityException(ax);
    }
    catch (SystemException sx)
    {
      Logger.warn("["+CLASS_NAME+"].getProcedureDefinitionFile() SystemException",sx);
      throw sx;
    }
    catch (Throwable ex)
    {
      Logger.warn("["+CLASS_NAME+"].getProcedureDefinitionFile() Error",ex);
      throw new SystemException(
         "["+CLASS_NAME+"].getProcedureDefinitionFile() Error", ex);
    }
    finally
    {
      Logger.log(CLASS_NAME+" getProcedureDefinitionFile(uId) Exit ");
    }
  }

  /**
   * To retrieve a collection of <code>ProcedureDefFile</code>
   * entity with the specified filter.
   *
   * @param           filter   the filter used to retrieve the <code>ProcedureDefFile</code> entity.
   *
   * @return          A collection of <code>ProcedureDefFile<code> entities.
   *
   * @exception       Thrown when the retrieval operation fails.
   *
   * @since 2.0
   * @version 2.0
   */
  public Collection getProcedureDefinitionFile(IDataFilter filter)
    throws    FindEntityException, SystemException
  {
    try
    {
      Logger.log("[UserProcedureManagerBean.getProcedureDefinitionFile] Enter");

      Logger.log("["+CLASS_NAME+" getProcedureDefinitionFile"+
                         "filter: " + ((filter==null)?null:filter.getFilterExpr()));
      return ProcedureDefFileEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);
    }
    catch (ApplicationException ax)
    {
      Logger.warn("["+CLASS_NAME+"].getProcedureDefinitionFile() BL Exception",ax);
      throw new FindEntityException(ax);
    }
    catch (SystemException sx)
    {
      Logger.warn("["+CLASS_NAME+"].getProcedureDefinitionFile() SystemException",sx);
      throw sx;
    }
    catch (Throwable ex)
    {
      Logger.warn("["+CLASS_NAME+"].getProcedureDefinitionFile() Error",ex);
      throw new SystemException(
         "["+CLASS_NAME+"].getProcedureDefinitionFile() Error", ex);
    }
    finally
    {
      Logger.log(CLASS_NAME+" getProcedureDefinitionFile Exit ");
    }

  }

  /**
   * To retrieve a collection of UIDs of the <code>ProcedureDefFile</code> entities with the specified filter.
   * 
   * @param filter The filtering condition
   * @return Collection of UIDs of ProcedureDefFile entities that satisfy the filtering
   * condition.
   * @since GT 2.2 I1
   */
  public Collection getProcedureDefFileKeys(IDataFilter filter)
    throws FindEntityException, SystemException
 {
    try
    {
      Logger.log("[UserProcedureManagerBean.getProcedureDefFileKeys] Enter");

      Logger.log("["+CLASS_NAME+" getProcedureDefFileKeys"+
                         "filter: " + ((filter==null)?null:filter.getFilterExpr()));
      return ProcedureDefFileEntityHandler.getInstance().getKeyByFilterForReadOnly(filter);
    }
    catch (ApplicationException ax)
    {
      Logger.warn("["+CLASS_NAME+"].getProcedureDefFileKeys() BL Exception",ax);
      throw new FindEntityException(ax);
    }
    catch (SystemException sx)
    {
      Logger.warn("["+CLASS_NAME+"].getProcedureDefFileKeys() SystemException",sx);
      throw sx;
    }
    catch (Throwable ex)
    {
      Logger.warn("["+CLASS_NAME+"].getProcedureDefFileKeys() Error",ex);
      throw new SystemException(
         "["+CLASS_NAME+"].getProcedureDefFileKeys() Error", ex);
    }
    finally
    {
      Logger.log(CLASS_NAME+" getProcedureDefFileKeys Exit ");
    }
 }

  /**
   * To check if there is any dependence <code>userProcedure</code> entity
   * to the specified uid of the <code>ProcedureDefFile</code> enity.
   *
   * @param           uid   the <code>ProcedureDefFile</code> entity.
   *
   * @return          true if the dependency exist, else false.
   *
   * @exception       Thrown when the retrieval operation fails.
   *
   * @since 2.0
   * @version 2.0
   *
   */

  public boolean checkDepUserProcedure(Long procDefFileUID) throws Throwable
  {
     Logger.log("["+CLASS_NAME+"] checkDepUserProcedure "+
        "ProcedureDefFile UID "+procDefFileUID);
     Collection c = UserProcedureEntityHandler.getInstance().findByProcedureDefFile(procDefFileUID);
     return (c != null && c.size() != 0);
  }

  public Object execute(Vector paramDefVect, UserProcedure userProcedure)
    throws UserProcedureExecutionException,SystemException
  {
   try
   {
    Logger.log("["+CLASS_NAME+"] In Execute ");
    ProcedureDefFile fileDef = userProcedure.getProcedureDefFile();
    ProcedureDef procedureDef = userProcedure.getProcedureDef();
    if (userProcedure.getProcedureType() == IProcedureType.PROC_TYPE_JAVA)
    {
      procedureDef.setType(IProcedureType.PROC_TYPE_JAVA);
    }
    else if(userProcedure.getProcedureType() == IProcedureType.PROC_TYPE_EXEC)
    {
      procedureDef.setType(IProcedureType.PROC_TYPE_EXEC);
    }
    else if(userProcedure.getProcedureType() == IProcedureType.PROC_TYPE_SOAP)
    {
      procedureDef.setType(IProcedureType.PROC_TYPE_SOAP);
    }

    String fileName = fileDef.getFileName();
    String filePath = fileDef.getFilePath();
    File   actFile = FileUtil.getFile(filePath, fileName);
    Object resultObj = null;
    if(actFile != null)
    {
      String actPath = actFile.getPath();
      filePath = actPath.substring(0, actPath.indexOf(fileName)); //Just Get the Path without filename
      Logger.debug("FileName = " + fileName);
      Logger.debug("FilePath = " + filePath);
      ProcedureHandlerInfo info = new ProcedureHandlerInfo(
                                    procedureDef,
                                    paramDefVect,
                                    filePath,
                                    fileName);

      Logger.debug("["+CLASS_NAME+"] Before Executing Procedure ");

      resultObj = ProcedureHandlerDelegate.execute(info);

      Logger.debug("["+CLASS_NAME+"] After Executing Procedure " + resultObj);
    }
    else
      throw new UserProcedureExecutionException("["+CLASS_NAME+"][execute()]"+
      "Unable to retrieve File with fileName"+filePath+fileName);
    return resultObj;
   }
   catch(Exception ex)
   {
     throw new UserProcedureExecutionException("["+CLASS_NAME+"][execute()]"+
     "Unable To Execute UserProcedure",ex);
   }
  }

  /**
   * Retrieve methods details from WSDL.
   *
   * @param wsdlURL the URL to the WSDL.
   */
  public Collection getMethodDetailsFromWSDL(Long wsdlUid) throws Exception
  {
    Logger.log("["+CLASS_NAME+"] getMethodDetailsFromWSDL "+
      "wsdlUid : "+wsdlUid);

    ProcedureDefFile defFile = getProcedureDefinitionFile(wsdlUid);
    File defFileObj = FileUtil.getFile(defFile.getFilePath(),
                                       defFile.getFileName());
    String wsdlUri = defFileObj.toURL().toString();
    Logger.log("["+CLASS_NAME+"] getMethodDetailsFromWSDL "+
      "wsdlUri : "+wsdlUri);

    ArrayList returnMethods = new ArrayList();
    Collection methods = SoapCallHelper.getMethodDetailsFromWSDL(wsdlUri);
    SoapCallHelper helper = new SoapCallHelper(wsdlUri);
    String serviceName = helper.getServiceNameFromWSDL();
    String serviceURL = helper.getServiceURLFromWSDL();
    Logger.log("["+CLASS_NAME+"] getMethodDetailsFromWSDL "+
      "serviceName : "+serviceName);
    Logger.log("["+CLASS_NAME+"] getMethodDetailsFromWSDL "+
      "serviceURL : "+serviceURL);

    for (Iterator i = methods.iterator(); i.hasNext(); )
    {
      Object[] newMethodDetails = new Object[5];
      Object[] methodDetails = (Object[])i.next();

      newMethodDetails[0] = methodDetails[0];

      Class returnType = (Class)methodDetails[1];
      newMethodDetails[1] = AbstractMultiClassLoader.getTypeName(returnType);

      newMethodDetails[2] = methodDetails[2];

      Class[] classList = (Class[])methodDetails[3];
      String[] newClassList = new String[classList.length];
      for (int j = 0; j < classList.length; j++)
      {
        Class paramClass = (Class)classList[j];
        newClassList[j] = AbstractMultiClassLoader.getTypeName(paramClass);
      }
      newMethodDetails[3] = newClassList;

      newMethodDetails[4] = methodDetails[4];
      returnMethods.add(newMethodDetails);
    }

    return returnMethods;
  }


}