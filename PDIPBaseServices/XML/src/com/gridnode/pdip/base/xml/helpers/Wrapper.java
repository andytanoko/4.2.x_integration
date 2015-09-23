/**
 * PROPRIETARY AND CONFIDENTIALITY NOTICE
 *
 * The code contained herein is confidential information and is the property 
 * of CrimsonLogic eTrade Services Pte Ltd. It contains copyrighted material 
 * protected by law and applicable international treaties. Copying,         
 * reproduction, distribution, transmission, disclosure or use in any manner 
 * is strictly prohibited without the prior written consent of Crimsonlogic 
 * eTrade Services Pte Ltd. Parties infringing upon such rights may be      
 * subject to civil as well as criminal liability. All rights are reserved. 
 *
 * File: SoapMessageRetrieveService.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * ??             Wong Ming Qian       Created
 * 18 OCT 2010    Tam Wei Xiang        #1901 - remove dependency to altova jar 
 */
package com.gridnode.pdip.base.xml.helpers;

import java.io.File;

import com.gridnode.pdip.base.xml.exceptions.MappingException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.util.classloader.JarClassLoader;
import com.gridnode.xml.XMLSplitter;

/* 
 * Created by Ming Qian, this class is to take in file names and class type as arguments, and calls the 
 * respective class (run method) in the mapforce jar file that is uploaded. 
 */

public class Wrapper 
{
  private XMLSplitter _xmlSplitter;
  
  public Wrapper()
  {
  
  }
  
  /*
   * 1. XML to XML transformation without XSLT
   */
  public void createTransformation(String inputFile, String outputFile, String className, String path, String fileName) throws MappingException, Exception
  {
    System.out.println("Start com.wrapper.Wrapper.createTransformation()");
    
    try
    {
      //Input fileInput = new FileInput(inputFile);       
      //Output fileOutput = new FileOutput(outputFile);
      
      File conRuleFile = FileUtil.getFile(path, fileName);
      String tempFileName = conRuleFile.getCanonicalPath();
      int count = tempFileName.lastIndexOf(File.separator);
      String tempFilePath = (count > -1) ? tempFileName.substring(0, count) : tempFileName;

      JarClassLoader jarClassLoader = new JarClassLoader(tempFilePath, fileName, getClass().getClassLoader());
      jarClassLoader.invokeClassMethod(className, "run", null, null, new Class[] {String.class, String.class}, new Object[] {inputFile, outputFile});
    }
    catch (MappingException e)
    {
      System.out.println("Stack trace at com.wrapper.Wrapper.createTransformation()");
      e.printStackTrace();
      System.out.println("End of stack trace at com.wrapper.Wrapper.createTransformation()");
      throw e;
    }
    catch (Exception e)
    {
      System.out.println("Stack trace at com.wrapper.Wrapper.createTransformation()");
      e.printStackTrace();
      System.out.println("End of stack trace at com.wrapper.Wrapper.createTransformation()");
      throw e;
    }
    
    System.out.println("End com.wrapper.Wrapper.createTransformation()");
  }
  
  /*
   * 1. EDI to XML conversion
   */
  public void createConversion(String inputFile1, String inputFile2, String outputFile, String className, String fileName, String path) throws MappingException, Exception
  {
    System.out.println("Start com.wrapper.Wrapper.createConversion()");
    
    try
    {
      //Input fileInput1 = new FileInput(inputFile1);     
      //Input fileInput2 = new FileInput(inputFile2);     
      //Output fileOutput = new FileOutput(outputFile);
      
      File conRuleFile = FileUtil.getFile(path, fileName);
      String tempFileName = conRuleFile.getCanonicalPath();
      int count = tempFileName.lastIndexOf(File.separator);
      String tempFilePath = (count > -1) ? tempFileName.substring(0, count) : tempFileName;
      JarClassLoader jarClassLoader = new JarClassLoader(tempFilePath, fileName, getClass().getClassLoader());
      //jarClassLoader.invokeClassMethod(className, "run", null, null, new Class[] {Input.class, Input.class, Output.class}, new Object[] {fileInput1, fileInput2, fileOutput});
      jarClassLoader.invokeClassMethod(className, "run", null, null, new Class[] {String.class, String.class}, new Object[] {inputFile1, outputFile});
    }
    catch (MappingException e)
    {
      System.out.println("Stack trace at com.wrapper.Wrapper.createConversion()");
      e.printStackTrace();
      System.out.println("End of stack trace at com.wrapper.Wrapper.createConversion()");
      throw e;
    }
    catch (Exception e)
    {
      System.out.println("Stack trace at com.wrapper.Wrapper.createConversion()");
      e.printStackTrace();
      System.out.println("End of stack trace at com.wrapper.Wrapper.createConversion()");
      throw e;
    }
    
    System.out.println("End com.wrapper.Wrapper.createConversion()");
  }  
}
