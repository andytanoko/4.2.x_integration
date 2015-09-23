/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserProcedureHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jan 21 2003    Jagadeesh               Created
 * Feb 10 2003    Jagadeesh               Modified: To pass ParamDef Vector - to
 *                                        ProcedureHandlerInfo.
 * Feb 14 2003    Neo Sok Lay             ParamDef DataSource should be Integer
 *                                        instead of String.
 * May 20 2003    Jagadeesh               Modified : paramValue casting to Integer to
 *                                        String as paramValue is instanceOf String.
 *
 * May 20 2003    Jagadeesh               Added : To RaisAlert using AlertRequestNotification.
 * Jul 28 2003    Koh Han Sing            Added SoapProcedure
 * Sep 02 2003    Koh Han Sing            Added new data types : DataHandler,
 *                                        DataHandler[], String[]. New source
 *                                        Attachments.
 * Nov 13 2003    Neo Sok Lay             To check for Null return value during
 *                                        postProcessing, and safely return the default action.
 * Nov 26 2003    Koh Han Sing            Added new data types : byte[],
 *                                        byte[][]
 * Oct 31 2005    Neo Sok Lay             Use ServiceLocator instead of ServiceLookup for jndi lookup services       
 * Feb 15 2007    Neo Sok Lay             getBytes(): Extra byte content returned from.                               
 */

package com.gridnode.gtas.server.userprocedure.handlers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import com.gridnode.gtas.model.userprocedure.IAction;
import com.gridnode.gtas.model.userprocedure.IDataType;
import com.gridnode.gtas.model.userprocedure.IParamDef;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.helpers.FileHelper;
import com.gridnode.gtas.server.document.helpers.IDocumentPathConfig;
import com.gridnode.gtas.server.document.model.Attachment;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.document.model.IAttachment;
import com.gridnode.gtas.server.userprocedure.exceptions.AbortUserProcedureException;
import com.gridnode.gtas.server.userprocedure.exceptions.InvalidParamDefException;
import com.gridnode.gtas.server.userprocedure.exceptions.InvalidReturnDefException;
import com.gridnode.gtas.server.userprocedure.exceptions.UserProcedureException;
import com.gridnode.gtas.server.userprocedure.helpers.AlertUtil;
import com.gridnode.gtas.server.userprocedure.helpers.Logger;
import com.gridnode.pdip.base.userprocedure.exceptions.UserProcedureExecutionException;
import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerHome;
import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerObj;
import com.gridnode.pdip.base.userprocedure.model.IOperator;
import com.gridnode.pdip.base.userprocedure.model.ParamDef;
import com.gridnode.pdip.base.userprocedure.model.ReturnDef;
import com.gridnode.pdip.base.userprocedure.model.UserProcedure;
import com.gridnode.pdip.base.xml.facade.ejb.IXMLServiceLocalHome;
import com.gridnode.pdip.base.xml.facade.ejb.IXMLServiceLocalObj;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This class is essentialy responsible to handle the following services,
 *
 * 1.PreProcessing of UserProcedure:-
 *   a. Get Parameter Value from Appropriate Data Source.
 *   b. Mapping of ParamValues to Class type.
 *
 * 2.Execution of UserProcedure(i.e invoking the Executing Engine).
 *
 * 3.PostProcessing of UserProcedure.
 *
 */

public class UserProcedureHandler
{

  private static final String CLASS_NAME = "UserProcedureHandler";
  public static final String CMDLINE_ARGUMENTS = "ProcedureDef Arguments";
  //private static final String CMDLINE_DELIMITER = "%";
  //private static final String PARAMPARAM_
  //private static Hashtable _gridDocMap = null;
  private static Hashtable classTable = new Hashtable();

  private static IUserProcedureManagerObj userProcedureObj = null;
  private static IXMLServiceLocalObj xmlServiceObj = null;

  static 
  {
    classTable.put(IDataType.DATA_TYPE_STRING, "java.lang.String");
    classTable.put(IDataType.DATA_TYPE_INTEGER, "java.lang.Integer");
    classTable.put(IDataType.DATA_TYPE_LONG, "java.lang.Long");
    classTable.put(IDataType.DATA_TYPE_DOUBLE, "java.lang.Double");
    classTable.put(IDataType.DATA_TYPE_BOOLEAN, "java.lang.Boolean");
    classTable.put(IDataType.DATA_TYPE_DATE, "java.util.Data");
  }

  public UserProcedureHandler()
  {
  }

  public static Object processUserProcedure(
    GridDocument gridDoc,
    UserProcedure userProcedure)
    throws
      InvalidParamDefException,
      UserProcedureException,
      InvalidReturnDefException,
      UserProcedureExecutionException,
      AbortUserProcedureException
  {
    final String MESSAGE_FORMAT = "[" + CLASS_NAME + "][processUserProcedure()";
    try
    {
      Object processedValue = null;
      if (gridDoc != null)
      {
        Vector paramMap = preProcessUserProcedure(gridDoc, userProcedure);
        Logger.debug(MESSAGE_FORMAT + "Param Map ->" + paramMap);
        processedValue = executeUserProcedure(paramMap, userProcedure);
        Logger.debug(MESSAGE_FORMAT + "Return Value ->" + processedValue);
        int retrunValue =
          postProcessUserProcedure(gridDoc, processedValue, userProcedure);
        if (retrunValue == IAction.ABORT.intValue())
        {
          throw new AbortUserProcedureException(
            "UserProcedure return code indicates" + " ABORT process");
        }
      }
      return processedValue;
    }
    catch (InvalidParamDefException inpex)
    {
      throw inpex;
    }
    catch (UserProcedureException upex)
    {
      throw upex;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      throw new UserProcedureException(
        MESSAGE_FORMAT + "Could not Execute UserProcedure\n" + ex.getMessage());
    }

  }

  /**
   * This method is essentially responsible to Pre Process the given UserProcedure
   * entity.
   *
   * Pre-Processing involves the following steps.,
   *
   *  a. Get the Vector of ParamDef Objects from UserProcedure Entity
   *  b. Iterate through each ParamDef, to get parameter value from appropriate
   *     Data Source.
   *  c. Set this derived value to the ParamDef Object
   *  d. Return a Vector of ParamDef Objects whose values are Derived in setp-b
   *
   *  @param gridDoc - GridDocument
   *  @param userProcedure - UserProcedure Entity
   *  @return - Vector of ParamDef Objects.
   *  @throws InvalidParamDefException - thrown when cannot Pre-Process UserProcedure.
   */
  public static Vector preProcessUserProcedure(
    GridDocument gridDoc,
    UserProcedure userProcedure)
    throws InvalidParamDefException
  {
    final String MESSAGE_FORMAT =
      "[" + CLASS_NAME + "][preProcessUserProcedure()]";
    try
    {
      Vector paramVect = userProcedure.getProcedureParamList();
      Vector paramDefVect = new Vector();

      Hashtable paramTable = new Hashtable();
      HashMap paramMap = new HashMap();
      if (paramVect != null)
      {
        Iterator paramIterator = paramVect.iterator();
        int i = 0;
        while (paramIterator.hasNext())
        {
          ParamDef paramDef = (ParamDef) paramIterator.next();
          Object paramValue = getParamValueByDefinition(gridDoc, paramDef);
          //paramTable.put(paramDef.getName(),paramValue);
          Logger.debug(
            "Putting param :"
              + paramDef.getName()
              + " with value "
              + paramValue
              + " into HashMap");
          // paramMap.put(paramDef.getName(),paramValue);
          paramDef.setActualValue(paramValue);
          paramDefVect.add(paramDef);
          //paramMap.put(new Integer(i++),paramValue);
        }
      }
      //String cmdLineArgs = getCmdLineArgsByDelimitedText(paramTable,userProcedure);
      // paramMap.put(CMDLINE_ARGUMENTS,cmdLineArgs);
      return paramVect;
    }
    catch (Exception ex)
    {
      Logger.warn(MESSAGE_FORMAT + "Could Not PreProcess \n" + ex.getMessage());
      throw new InvalidParamDefException(
        MESSAGE_FORMAT + "Could Not PreProcess \n" + ex.getMessage(),
        ex);
    }
  }

  /**
   * Gets the actual parameter value from the given definition(varialbe on DataSource
   *  ex: a. USER_DEFINED Data Source, b. Data Source being GridDocument. c.
   *  Data Source being UserDocument.
   *
   * @param gridDoc - GridDocuemt as input.
   * @param paramDef - ParamDef Object
   * @return - Value retrieved from the given DataSource.
   * @throws InvalidParamDefException - thrown when cannot retrieve value from DataSource.
   */
  private static Object getParamValueByDefinition(
    GridDocument gridDoc,
    ParamDef paramDef)
    throws InvalidParamDefException
  {
    final String MESSAGE_FORMAT =
      "[" + CLASS_NAME + "][getParamValueByDefinition()]";
    try
    {
      Object actualParamValue = null;
      SimpleDateFormat dateToFormat = null;

      String paramName = paramDef.getName();
      Integer dataSource = paramDef.getSource();
      Object paramValue = paramDef.getValue();
      int dataType = paramDef.getType();
      String dateFormat = paramDef.getDateFormat();

      Logger.debug(MESSAGE_FORMAT + paramDef);
      Logger.debug(MESSAGE_FORMAT + dateFormat);

      if ((dateFormat != null) && (!dateFormat.equals("")))
        dateToFormat = new SimpleDateFormat(dateFormat);

      paramValue = getParamValueFromDataSource(dataSource, gridDoc, paramDef);

      if (dateToFormat != null)
      {
        Logger.debug(MESSAGE_FORMAT + "Formatting Date");
        paramValue =
          dateToFormat.parse(paramValue.toString(), new ParsePosition(0));
      }
      Logger.debug(MESSAGE_FORMAT + " After getParam " + paramValue);
      return paramValue;
    }
    catch (Exception ex)
    {
      throw new InvalidParamDefException(
        MESSAGE_FORMAT + "Cannot get ParamValue " + ex.getMessage(),
        ex);
    }
  }

  /**
   * This method invokes the XMLServices to retrieve the value from
   * Document File, given the xpath to the value.
   *
   * @param docFilePath - DocumentFile Path.
   * @param xpathValue - Xpath to the value.
   * @return - List of Values retrieved from the given xpath.
   * @throws Exception - thrown when cannot retrieve values or lookup xmlservices.
   */
  private static List getValueByDocFile(String docFilePath, Object xpathValue)
    throws Exception
  {
    try
    {
      if (xmlServiceObj == null)
        xmlServiceObj = getXMLServiceFacade();
      List xpathValues =
        xmlServiceObj.getXPathValues(docFilePath, (String) xpathValue);
      return xpathValues;
      //Verify with hansing - the use of xpaths which returns List.
    }
    catch (Exception ex)
    {
      throw new Exception(
        "[UserProcedureHandler][getValueByDocFile()]"
          + "Cannot Extract Values From XmlService");
    }
  }

  /**
   * This method delegates the execution of UserProcedure to UserProcedureManager
   * facade at Base Services.
   * @param paramDefVect - A vector of ParamDef Objects.
   * @param userProcedure - UserProcedure Entity.
   * @return - Object value returened after execution.
   * @throws UserProcedureException - thrown when cannot lookup services.
   * @throws UserProcedureExecutionException - thrown when executing engine at base
   * services cannot execute.
   */
  public static Object executeUserProcedure(
    Vector paramDefVect,
    UserProcedure userProcedure)
    throws UserProcedureException, UserProcedureExecutionException
  {
    final String MESSAGE_FORMAT =
      "[" + CLASS_NAME + "][executeUserProcedure()]";
    try
    {
      Logger.debug(MESSAGE_FORMAT + "B4 calling Execute");
      if (userProcedureObj == null)
        userProcedureObj = getBaseUserProcedure();
      Object returnValue =
        userProcedureObj.execute(paramDefVect, userProcedure);
      Logger.debug(
        MESSAGE_FORMAT + "After calling Execute ReturnValue = " + returnValue);
      return returnValue;
    }
    catch (UserProcedureExecutionException upex)
    {
      throw upex;
    }
    catch (Exception ex)
    {
      throw new UserProcedureException(
        MESSAGE_FORMAT + "Cannot Execute UserProcedure Service",
        ex);
    }
  }

  public static int postProcessUserProcedure(
    GridDocument gdoc,
    Object processedValue,
    UserProcedure userProcedure)
    throws InvalidReturnDefException
  {
    final String MESSAGE_FORMAT =
      "[" + CLASS_NAME + "][postProcessUserProcedure()] ";
    String toReturnClass = null;
    Integer returnType = null;
    try
    {
      Logger.debug(MESSAGE_FORMAT + "Starts");
      boolean isSynchronous = userProcedure.isSynchronous();
      returnType = new Integer(userProcedure.getReturnDataType());
      toReturnClass = (String) classTable.get(returnType);
      Logger.debug(MESSAGE_FORMAT + toReturnClass);

      Long alertId = null;
      if ((toReturnClass == null)
        || (processedValue == null)
        || (!processedValue.getClass().getName().equals(toReturnClass)))
      {
        alertId = userProcedure.getProcedureDefAlert();
        if (alertId != null)
          AlertUtil.raiseUserDefinedAlert(alertId, gdoc, userProcedure);
        //raiseAlert(gdoc,userProcedure,alertId);

        if (!isSynchronous)
          return UserProcedure.CONTINUE;

        return userProcedure.getProcedureDefAction();
      }

      Vector returnList = userProcedure.getProcedureReturnList();
      Logger.debug(MESSAGE_FORMAT + "Return List " + returnList);
      if ((returnList != null) || (returnList.isEmpty()))
      {
        Iterator returnListIterator = returnList.iterator();
        while (returnListIterator.hasNext())
        {
          ReturnDef returnDefinition = (ReturnDef) returnListIterator.next();
          int operatorType = returnDefinition.getOperator();
          Object operand = returnDefinition.getValue();
          Logger.debug(MESSAGE_FORMAT + "Return Operator Type" + operatorType);
          Logger.debug(MESSAGE_FORMAT + "Return Value " + operand);
          Logger.debug(MESSAGE_FORMAT + "Return Class Name " + toReturnClass);
          //operand = AbstractEntity.convert(operand,toReturnClass);
          Logger.debug(MESSAGE_FORMAT + "After Getting Operand " + operand);
          if ((processReturnValue(processedValue,
            operand,
            toReturnClass,
            operatorType)))
          {
            alertId = returnDefinition.getAlert();
            if (alertId != null)
              AlertUtil.raiseUserDefinedAlert(alertId, gdoc, userProcedure);
            //raiseAlert(gdoc,userProcedure,alertId);

            if (isSynchronous)
              return (returnDefinition.getAction());
          }
        }
      }
      Logger.debug(MESSAGE_FORMAT + "Ends");
      if (!isSynchronous)
        return UserProcedure.CONTINUE;
      //Default Definition
      return userProcedure.getProcedureDefAction();
    }
    catch (Exception ex)
    {
      throw new InvalidReturnDefException(
        MESSAGE_FORMAT
          + "Could Not Post Process with"
          + "[Return Class]"
          + toReturnClass
          + "[Return Type]"
          + returnType,
        ex);
    }
  }

  private static boolean processReturnValue(
    Object processedValue,
    Object operand,
    String toReturnClass,
    int operatorType)
    throws Exception
  {
    Logger.debug(
      "[UserProcedureHandler][processedValue()] In Process Return Value");
    Object minValue = null;
    Object maxValue = null;
    try
    {
      if (operatorType != IOperator.BETWEEN)
        operand = AbstractEntity.convert(operand, toReturnClass);
      else
      {
        StringTokenizer stk = new StringTokenizer((String) operand, "-");
        minValue = AbstractEntity.convert(stk.nextToken(), toReturnClass);
        maxValue = AbstractEntity.convert(stk.nextElement(), toReturnClass);
        Logger.debug(
          "[UserProcedureHandler][processedValue()] MINVALUE CLASS='"
            + minValue.getClass().getName()
            + "'");

        Logger.debug(
          "[UserProcedureHandler][processedValue()] MAXVALUE CLASS='"
            + maxValue.getClass().getName()
            + "'");
      }

      if (operatorType == IOperator.EQUAL)
        return (processedValue.equals(operand));

      if (operatorType == IOperator.NOT_EQUAL)
        return !(processedValue.equals(operand));

      if (!(processedValue instanceof Comparable))
        return false;

      if (operatorType == IOperator.LESS)
        return (((Comparable) processedValue).compareTo(operand) < 0);

      if (operatorType == IOperator.GREATER)
        return (((Comparable) processedValue).compareTo(operand) > 0);

      if (operatorType == IOperator.LESS_OR_EQUAL)
        return (
          processedValue.equals(operand)
            || (((Comparable) processedValue).compareTo(operand) < 0));

      if (operatorType == IOperator.GREATER_OR_EQUAL)
        return (
          processedValue.equals(operand)
            || (((Comparable) processedValue).compareTo(operand) > 0));

      if (operatorType == IOperator.BETWEEN)
        return (
          (((Comparable) processedValue).compareTo(minValue) > 0)
            || (((Comparable) processedValue).equals(minValue)))
          && ((((Comparable) processedValue).compareTo(maxValue) < 0)
            || (((Comparable) processedValue).equals(maxValue)));

      return false;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      throw new Exception(
        "Invalid ProcessReturn Definition [Operand]="
          + operand
          + "[processedValue]="
          + processedValue);
    }
  }

  private static Object getParamValueFromDataSource(
    Integer dataSource,
    GridDocument gridDoc,
    ParamDef paramDef)
    throws Exception
  {
    Object paramValue = paramDef.getValue();
    if (dataSource.equals(IParamDef.SOURCE_UDOC))
    {
      paramValue = getParamValueFromUDoc(gridDoc, paramValue, paramDef);
    }
    else if (dataSource.equals(IParamDef.SOURCE_GDOC))
    {
      paramValue = getParamValueFromGDoc(gridDoc, paramValue, paramDef);
    }
    else if (dataSource.equals(IParamDef.SOURCE_ATTACHMENTS))
    {
      paramValue = getParamValueFromAttachments(gridDoc, paramValue, paramDef);
    }
    else if (dataSource.equals(IParamDef.SOURCE_USER_DEFINED))
    {
      paramValue = getUserDefineParamValue(paramValue, paramDef);
    }
    Logger.debug(
      "[UserProcedureHandler.getParamValueFromDataSource] Param value extracted :"
        + paramValue.getClass().getName());
    return paramValue;
  }

  private static Object getParamValueFromUDoc(
    GridDocument gridDoc,
    Object paramValue,
    ParamDef paramDef)
    throws Exception
  {
    Logger.debug(
      "[UserProcedureHandler.getParamValueFromUDoc] Extracting param value from user doc ....");
    File docFile = FileHelper.getUdocFile(gridDoc);
    if (paramDef.getType() == (IDataType.DATA_TYPE_DATAHANDLER.intValue()))
    {
      return new DataHandler(new FileDataSource(docFile));
      //return new DataHandler(docFile.toURL());
    }
    else if (paramDef.getType() == (IDataType.DATA_TYPE_BYTE_ARRAY.intValue()))
    {
      return getBytes(docFile);
    }
    else
    {
      String docFilePath = docFile.getAbsolutePath();
      List xpathValue = getValueByDocFile(docFilePath, paramValue);
      return xpathValue.get(0); //First Element is the DefaultValue.
    }
  }

  private static Object getParamValueFromGDoc(
    GridDocument gridDoc,
    Object paramValue,
    ParamDef paramDef)
    throws Exception
  {
    Logger.debug(
      "[UserProcedureHandler.getParamValueFromGDoc] Extracting param value from griddoc ....");
    if ((paramDef.getType() == (IDataType.DATA_TYPE_OBJECT.intValue()))
      && ((paramValue == null) || (paramValue.toString().equals(""))))
    {
      paramValue = gridDoc;
    }
    else
    {
      //20030520 paramValue instanceof String.
      paramValue = gridDoc.getFieldValue(new Integer((String) paramValue));
    }
    return paramValue;
  }

  private static Object getParamValueFromAttachments(
    GridDocument gridDoc,
    Object paramValue,
    ParamDef paramDef)
    throws Exception
  {
    Logger.debug(
      "[UserProcedureHandler.getParamValueFromAttachments] Extracting param value from attachments ....");
    Collection attachments = getDocumentFacade().findAttachments(gridDoc);
    Object result = null;
    if (paramDef.getType() == (IDataType.DATA_TYPE_STRING_ARRAY.intValue()))
    {
      ArrayList fieldValues = new ArrayList();
      for (Iterator i = attachments.iterator(); i.hasNext();)
      {
        Attachment attachment = (Attachment) i.next();
        String fieldValue =
          attachment.getFieldValue(new Integer((String) paramValue)).toString();
        fieldValues.add(fieldValue);
      }
      String[] values = new String[fieldValues.size()];
      for (int j = 0; j < fieldValues.size(); j++)
      {
        values[j] = (String) fieldValues.get(j);
      }
      result = values;
    }
    else if (
      paramDef.getType() == (IDataType.DATA_TYPE_DATAHANDLER_ARRAY.intValue()))
    {
      ArrayList datahandlers = new ArrayList();
      for (Iterator i = attachments.iterator(); i.hasNext();)
      {
        Attachment attachment = (Attachment) i.next();
        String filename =
          attachment.getFieldValue(IAttachment.FILENAME).toString();
        File attachmentFile =
          FileUtil.getFile(IDocumentPathConfig.PATH_ATTACHMENT, filename);
        DataHandler dataHandler =
          new DataHandler(new FileDataSource(attachmentFile));
        //DataHandler dataHandler = new DataHandler(attachmentFile.toURL());
        datahandlers.add(dataHandler);
      }
      DataHandler[] values = new DataHandler[datahandlers.size()];
      for (int j = 0; j < datahandlers.size(); j++)
      {
        values[j] = (DataHandler) datahandlers.get(j);
      }
      result = values;
    }
    else if (paramDef.getType() == (IDataType.DATA_TYPE_BYTE_ARRAY_ARRAY.intValue()))
    {
      ArrayList byteArrayArray = new ArrayList();
      for (Iterator i = attachments.iterator(); i.hasNext(); )
      {
        Attachment attachment = (Attachment)i.next();
        String filename = attachment.getFieldValue(IAttachment.FILENAME).toString();
        File attachmentFile = FileUtil.getFile(IDocumentPathConfig.PATH_ATTACHMENT, filename);
        byte[] byteArray = getBytes(attachmentFile);
        byteArrayArray.add(byteArray);
      }
      byte[][] values = new byte[byteArrayArray.size()][];
      for (int j = 0; j < byteArrayArray.size(); j++)
      {
        values[j] = (byte[])byteArrayArray.get(j);
      }
      result = values;
    }
    return result;
  }

  private static Object getUserDefineParamValue(
    Object paramValue,
    ParamDef paramDef)
    throws Exception
  {
    Logger.debug(
      "[UserProcedureHandler.getUserDefineParamValue] Extracting param value from user define ....");
    if (paramDef.getType() == (IDataType.DATA_TYPE_STRING_ARRAY.intValue()))
    {
      ArrayList list = new ArrayList();
      String value = paramValue.toString();
      StringTokenizer st = new StringTokenizer(value, ";", true);
      String preToken = null;
      while (st.hasMoreTokens())
      {
        String token = st.nextToken();
        if (!token.equals(";"))
        {
          list.add(token);
        }
        else if ((preToken == null) || (preToken.equals(";")))
        {
          list.add("");
        }
        preToken = token;
      }
      if (preToken.equals(";"))
      {
        list.add("");
      }

      String[] result = new String[list.size()];
      for (int i = 0; i < list.size(); i++)
      {
        result[i] = list.get(i).toString();
      }
      return result;
    }
    else
    {
      return paramDef.getValue();
    }
  }
  /*
  private static String getCmdLineArgsByDelimitedText(
    Hashtable paramTable,
    UserProcedure userProcedure)
    throws Exception
  {
    String cmdLineExpression = "";
    ProcedureDef procDef = userProcedure.getProcedureDef();
    String arguments = null;
    if (userProcedure.getProcedureType() == IProcedureType.PROC_TYPE_JAVA)
    {
      JavaProcedure javaProcedure = (JavaProcedure) procDef;
      String args = javaProcedure.getArguments();
      if ((javaProcedure.isLocal()) && (args != null))
        cmdLineExpression =
          replaceDelimitedText(args, CMDLINE_DELIMITER, paramTable);
    }
    else if (userProcedure.getProcedureType() == IProcedureType.PROC_TYPE_EXEC)
    {
      ShellExecutable shellExec = (ShellExecutable) procDef;
      String args = shellExec.getArguments();
      if (args != null)
        cmdLineExpression =
          replaceDelimitedText(args, CMDLINE_DELIMITER, paramTable);
    }
    else if (userProcedure.getProcedureType() == IProcedureType.PROC_TYPE_SOAP)
    {
      // No command line expression
    }
    return cmdLineExpression;
  }*/

  public static String replaceDelimitedText(
    String str,
    String delimiter,
    Hashtable table)
  {
    Logger.debug(
      "[" + CLASS_NAME + "][replaceDelimitedText()] String To Process=" + str);
    Logger.log(
      "[" + CLASS_NAME + "][replaceDelimitedText()] Hashtable=" + table);
    if ((str != null) && (!str.equals("")))
    {
      StringTokenizer strToken = new StringTokenizer(str, delimiter);
      StringBuffer strBuf = new StringBuffer(str);
      while (strToken.hasMoreTokens())
      {
        String key = strToken.nextToken().trim();
        int spaceIndex = key.indexOf(" ");
        if (spaceIndex != -1)
          key = key.substring(0, spaceIndex);
        if (table.get(key) == null)
          continue;

        String value = table.get(key).toString();
        int startIndex = str.indexOf(delimiter + key);
        int endIndex = startIndex + 1 + key.length();
        strBuf.replace(startIndex, endIndex, value);
        str = strBuf.toString();
        strToken = new StringTokenizer(str, delimiter);
      }
    }
    Logger.log(
      "[" + CLASS_NAME + "][replaceDelimitedText()] ReturnValue=" + str);
    return str;
  }

  private static IUserProcedureManagerObj getBaseUserProcedure()
    throws Exception
  {
    return(IUserProcedureManagerObj) ServiceLocator
        .instance(ServiceLocator.CLIENT_CONTEXT)
        .getObj(IUserProcedureManagerHome.class.getName(),
                IUserProcedureManagerHome.class,
                new Object[0]);
  }

  private static IXMLServiceLocalObj getXMLServiceFacade() throws Exception
  {
    return (IXMLServiceLocalObj) ServiceLocator
        .instance(ServiceLocator.LOCAL_CONTEXT)
        .getObj(IXMLServiceLocalHome.class.getName(), 
                IXMLServiceLocalHome.class,
                new Object[0]);
  }

  private static IDocumentManagerObj getDocumentFacade() throws Exception
  {
    return (IDocumentManagerObj) ServiceLocator
        .instance(ServiceLocator.CLIENT_CONTEXT)
        .getObj(IDocumentManagerHome.class.getName(),
                IDocumentManagerHome.class,
                new Object[0]);
  }
  /*
  private List createProviders(UserProcedure userProcedure)
  {
    List providers = new ArrayList();
    providers.add(new UserProcData(userProcedure));
    return providers;
  }*/
  private static byte[] getBytes(File file) throws IOException
  {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    FileInputStream fis = new FileInputStream(file);
    byte[] buffer = new byte[1024];
    int size = fis.read(buffer);
    while (size > 0)
    {
      bos.write(buffer, 0, size); //NSL20070215 Must specify size, otherwise will write previous data in buffer for last loop
      size = fis.read(buffer);
    }
    fis.close();
    return bos.toByteArray();
  }

}