/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XML4A5Processor.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 17, 2009   Tam Wei Xiang       Created
 */
package com.inovis.userproc.xml.processor;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import com.inovis.userproc.util.FileUtil;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.3
 */
public class XML4A5Processor
{
  private static final String CUSTOMER_ITEM_HEADER = "#Customer Item";
  private static final String SUPPLIER_HEADER = "Supplier";
  private static final String CUSTOMER_SITE_HEADER = "Site";
  private static final String CFG_HEADER = "CFG";
  private static final String DATA_MEASURE_HEADER = "Data Measure";
  
  
  private static final int CUSTOMER_ITEM = 0;
  private static final int CUSTOMER_SITE = 1;
  private static final int DATE = 2;
  private static final int PRODUCT_QUANTITY = 3;
  
  //value for the header
  private static final String SUPPLIER = "INTEL";
  private static final String CFG = "";
  private static final String DATA_MEASURE = "Commit Air";
  
  public static void main(String[] args) throws Exception
  {
    String filepath = "D:/wxtam/My Task/2009/Inovis/Intel_Dell_Project_1_March/4A5/moreSample/UnPackage5310753111_New_in.txt";
    XML4A5Processor processor = new XML4A5Processor();
    processor.postProcess4A5Flat(filepath, "\t");
  }
  
  public XML4A5Processor()
  {
    
  }
  
  public void postProcess4A5Flat(String filepath, String delim) throws Exception
  {
    delim = interpretDelim(delim);
    
    if(! FileUtil.isFileExist(filepath) || FileUtil.isDirectory(filepath))
    {
      throw new Exception("File: "+filepath+" is not exists or is a directory rather than a file !");
    }
    
    File udoc = new File(filepath);
    BufferedInputStream input = null;
    BufferedReader reader = null;
    FileInputStream fileInput = null;
    File tempFile = null;
    FileWriter writer = null;
    
    try
    {
      fileInput = new FileInputStream(udoc);
      input = new BufferedInputStream(fileInput);
      reader = new BufferedReader(new InputStreamReader(input));
      
      boolean isGeneratedHeader = true;
      String s = "";
      String currentProductItem = "";
      ArrayList<String[]> itemScheduleList = new ArrayList<String[]>();
      
      
      while( (s = (reader.readLine())) != null)
      {
        s = s.trim();
        if("".equals(s))
        {
          continue;
        }
        
        //XR814 VGH_INTEL_01  02-04-2009  5670
        String[] productItem = s.split(delim);
        if(productItem.length >= 4)
        {
          String productItemKey = productItem[CUSTOMER_ITEM]+productItem[CUSTOMER_SITE];
          
          if("".equals(currentProductItem))
          {
            currentProductItem = productItemKey;
          }
          if(currentProductItem.equals(productItemKey))
          {
            itemScheduleList.add(productItem); //Group all the same item together (include comparing customer site
          }
          else
          {
            if(tempFile == null)
            {
              tempFile = FileUtil.createTempFile("."+FileUtil.getFileExtension(udoc));
              writer = new FileWriter(tempFile, true);
              System.out.println("Temp file generated at: "+tempFile.getAbsolutePath());
            }
            //Export the productItem list to the destination CSV file
            outputProductItemSchedule(itemScheduleList, writer, isGeneratedHeader, delim);
            
            //refresh all cache
            itemScheduleList = new ArrayList<String[]>();
            itemScheduleList.add(productItem);
            currentProductItem = productItemKey;
            isGeneratedHeader = false;
          }
          
          
        }
        else
        {
          throw new Exception("Expecting at least 4 column from the file: "+udoc.getAbsolutePath());
        }
      }
      if(itemScheduleList.size() > 0)
      {
        if(tempFile == null)
        {
          tempFile = FileUtil.createTempFile("."+FileUtil.getFileExtension(udoc));
          writer = new FileWriter(tempFile, true);
          System.out.println("Temp file generated at: "+tempFile.getAbsolutePath());
        }
        outputProductItemSchedule(itemScheduleList, writer, isGeneratedHeader, delim);
      }
      
      //Replace the udoc with the final output we generated in the temp file
      if(reader != null)
      {
        reader.close();
      }
      if(input != null)
      {
        input.close();
      }
      if(fileInput != null)
      {
        fileInput.close();
      }
      if(writer != null)
      {
        writer.close();
      }
     
      udoc.delete();
      boolean isReplaceSuccess = tempFile.renameTo(udoc);
      if(! isReplaceSuccess)
      {
        throw new Exception("The intermediate file: "+udoc.getAbsolutePath()+" cannot be replaced by final output file: "+tempFile.getAbsolutePath());
      }
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
      throw ex;
    }
    finally
    {
      try
      {
        if(input != null)
        {
          input.close();
        }
        if(reader != null)
        {
          reader.close();
        }
        if(fileInput != null)
        {
          fileInput.close();
        }
        
        if(writer != null)
        {
          writer.close();
        }
      }
      catch(Exception ex1)
      {
        ex1.printStackTrace();
      }
    }
  }
  
  /**
   * Output the consolidated group of product item.
   * @param productItemScheduleList same list of product item but with different product schedule
   * @param writer
   * @param isGeneratedHeader indicate whether we should generate the header
   * @param delim
   * @throws Exception
   */
  private void outputProductItemSchedule(ArrayList<String[]> productItemScheduleList, FileWriter writer, boolean isGeneratedHeader,
                                         String delim) throws Exception
  {
    if(productItemScheduleList == null || productItemScheduleList.size() == 0)
    {
      throw new Exception("Expected at least one product item !");
    }
    
    //Output header  # Customer item Supplier  Customer site CFG Data measure
    if(isGeneratedHeader)
    {
      String[] dateHeader = getDateHeader(productItemScheduleList);
      String[] standardHeader = new String[]{CUSTOMER_ITEM_HEADER, SUPPLIER_HEADER, CUSTOMER_SITE_HEADER, CFG_HEADER, DATA_MEASURE_HEADER};
      outputHeader(writer, standardHeader, dateHeader, delim);
      writer.write("\n");
    }
    
    //Output row item data for product item schedule
      String itemScheduleRow = "";
      for(String[] productItemSchedule : productItemScheduleList)
      {
        if("".equals(itemScheduleRow)) 
        {
          itemScheduleRow = productItemSchedule[CUSTOMER_ITEM] + delim + SUPPLIER +delim + productItemSchedule[CUSTOMER_SITE] + delim+ CFG +delim+ DATA_MEASURE; 
        }
        
        
        String productQuantity = productItemSchedule[PRODUCT_QUANTITY];
        if("0".equals(productQuantity.trim()))
        {
            productQuantity = "";
        }
        itemScheduleRow += delim + productQuantity;
  
      }
      writer.write(itemScheduleRow);
      writer.write("\n");
  }
  
  private void outputHeader(FileWriter writer, String[] standardHeader, String[] dateHeader, String delim) throws IOException
  {
    String header = "";
    for(String s : standardHeader)
    {
      header += s+delim;
    }
    
    for(String s: dateHeader)
    {
      header += s + delim;
    }
    
    int index = header.lastIndexOf(delim);
    header = header.substring(0, index);
    writer.write(header);
  }
  
  private String[] getDateHeader(ArrayList<String[]> productItemScheduleList) throws Exception
  {
    if(productItemScheduleList == null || productItemScheduleList.size() == 0)
    {
      throw new Exception("Can't determine the date header");
    }
    String[] dateHeader = new String[productItemScheduleList.size()];
    int i = 0;
    
    for(String[] productItemSchedule : productItemScheduleList)
    {
      dateHeader[i] = productItemSchedule[2];
      i++;
    }
    return dateHeader;
  }
  
  private String interpretDelim(String delim)
  {
//  interpret the delim here
    if("tab".equals(delim))
    {
      delim = "\t";
    }
    else if("comma".equals(delim))
    {
      delim = ",";
    }
    else if("space".equals(delim))
    {
      delim = " ";
    }
    return delim;
  }
}
