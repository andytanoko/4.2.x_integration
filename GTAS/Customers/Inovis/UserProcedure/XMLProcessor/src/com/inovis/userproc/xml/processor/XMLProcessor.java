/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XMLProcessor.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 12, 2009   Tam Wei Xiang       Created
 * Mar 26, 2009   Tam Wei Xiang       #138: Modify pre-processor to group product item
 *                                          base on group info.
 */
package com.inovis.userproc.xml.processor;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

import com.gridnode.xml.adapters.GNDocument;
import com.gridnode.xml.adapters.GNElement;
import com.gridnode.xml.adapters.GNXMLDocumentUtility;
import com.gridnode.xml.adapters.GNXMLException;
import com.inovis.userproc.util.DocumentUtil;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.3 (GTVAN)
 */
public class XMLProcessor
{
  private static final String PRODUCT_IDENTIFIER_XPATH = "Pip4A4PlanningReleaseForecastNotification/PlanningReleaseForecast/PartnerProductForecast/ProductForecast/ProductIdentification/GlobalProductIdentifier";
  private static final String PARTNER_PRODUCT_FORECAST_XPATH = "Pip4A4PlanningReleaseForecastNotification/PlanningReleaseForecast/PartnerProductForecast/ForecastPartner/PartnerDescription/PhysicalLocation/GlobalLocationIdentifier";
  
  private static final String PRODUCT_SCHEDULE = "ProductSchedule";
  private static final String PRODUCT_FORECAST = "ProductForecast";
  
  private static final String DATE_FORMAT = "MM/dd/yyyy";
  private static final String RN_DATE_FORMAT = "yyyyMMdd'Z'";
  private static final String FORECAST_INTERVAL_DAY = "Day";
  private static final String FORECAST_INTERVAL_WEEK = "Week";
  private static final String FORECAST_QUANTITY_TYPE_CODE = "Discrete Gross Demand";
  
  public static void main(String[] args) throws Exception
  {
    
    XMLProcessor process = new XMLProcessor();
    //String intermediateXML = "F:/ATX/build/atxFTP_build/XMLProcessor/QA/Postprocess/sampleFile/gmIntermediate_5.xml";
    //String originalFlatFile = "F:/ATX/build/atxFTP_build/XMLProcessor/QA/Postprocess/sampleFile/SAMPLE 1_5.txt";
    String delim = "\t";
    //process.addProductSchedule(intermediateXML, originalFlatFile, delim);
    
    /*
    String intermediateXML = "F:/ATX/12_3/ZZ-ISSUE/4A4Split/proposed/qa/gmintermediate.xml";
    String originalFlatFile = "F:/ATX/12_3/ZZ-ISSUE/4A4Split/proposed/qa/SAMPLE 1_5.txt";
    String ref = "F:/ATX/12_3/ZZ-ISSUE/4A4Split/proposed/REF.XML";
    
    process.postprocessRNXML(intermediateXML, originalFlatFile, delim, ref); */
    
    //String s = "";
    //process.preprocessBackendFile(originalFlatFile, "\t", "DET");
    
    /*
    String udocFullPath = "D:/wxtam/My Task/2009/Inovis/Intel_Dell_Project_1_March/ZZ-ISSUE/4A4Split/GMSplitWrongly/problematic4A4/flat/dellWith1Location.txt";
    String originalFlatFile = "";
    process.preprocessBackendFile(udocFullPath, "", "\t", "DET");*/
  }
  
  public XMLProcessor()
  {
    
  }
  
  public String preprocessBackendFile(String udocFullPath, String originalDocFilename, String delim, String header) throws Exception
  {
    System.out.println("Pre-process Backend udoc full path:"+udocFullPath);
    System.out.println("Pre-process Original Doc:"+originalDocFilename+" delim:"+delim+" header:"+header);
    
    File udoc = new File(udocFullPath);
    if(! udoc.exists() || udoc.isDirectory())
    {
      throw new Exception("Can not locate the udoc file given filepath:"+udocFullPath);
    }
    
    //Reading file
    ArrayList<String> udocFileInStr = readBackendFileAsStr(udoc);
    
    if(udocFileInStr == null || udocFileInStr.size() < 2)
    {
      throw new Exception("Expected at least one header and one row item in the document: "+udocFullPath);
    }
    
    delim = interpretDelim(delim);
    
    //remove the header
    udocFileInStr.remove(0);
    
    //#138 group all the product item into the same location
    Hashtable<String, ArrayList<String>> productItemGroupInLocation = groupItemInLocation(udocFileInStr, delim);
    
    //format it to GridMapper accepted format
    outputToFile(udoc, productItemGroupInLocation, delim, header);
    return originalDocFilename;
    
  }
  
  private Hashtable<String, ArrayList<String>> groupItemInLocation(ArrayList<String> udocFileInStr, String delim)
  {
    Hashtable<String, ArrayList<String>> productItemGroupInLocation = new Hashtable<String, ArrayList<String>>();
    if(udocFileInStr != null && udocFileInStr.size()  >0)
    {
      for(String productItemInfo : udocFileInStr)
      {
        String[] productItemArr = productItemInfo.split(delim);
        String locationID = productItemArr[2];
        if(productItemGroupInLocation.containsKey(locationID))
        {
          ArrayList<String> productItemInfoList = productItemGroupInLocation.get(locationID);
          productItemInfoList.add(productItemInfo);
        }
        else
        {
          ArrayList<String> productItemInfoList = new ArrayList<String>();
          productItemInfoList.add(productItemInfo);
          productItemGroupInLocation.put(locationID, productItemInfoList);
        }
      }
    }
    return productItemGroupInLocation;
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
  
  private void outputToFile(File file, Hashtable<String, ArrayList<String>> productItemGroupInLocation, String delim, String header) throws Exception
  {
    FileWriter writer = null;
    try
    {
      writer = new FileWriter(file);
      
      Enumeration<String> locationIDs = productItemGroupInLocation.keys();
      while(locationIDs.hasMoreElements())
      {
        String locationID = locationIDs.nextElement();
        ArrayList<String> docInStr = productItemGroupInLocation.get(locationID);
        
        if(docInStr != null && docInStr.size() > 0)
        {
          for(String s : docInStr)
          {
            writer.write(formatToGM(s, delim, header));
            writer.write("\n");
          }
        }
      }
      
      
    }
    catch(Exception ex)
    {
      throw ex;
    }
    finally
    {
      if(writer != null)
      {
        try
        {
          writer.close();
        }
        catch(Exception ex) {};
      }
    }
  }
  
  /**
   * Format it so that GridMapper can perform transformation to intermediate RN Format
   * @param productInfo The row item in the flat file from customer backend
   * @return
   */
  private String formatToGM(String productInfo, String delim, String header)
  {
    StringTokenizer st = new StringTokenizer(productInfo, delim);
    String customerItem = st.nextToken();
    String supplier = st.nextToken();
    String site = st.nextToken();
    
    return header+"\t"+site+"\t"+customerItem;
  }
  
  public void postProcessRNXML(String intermediateXMLFullPath, String backendDocFilename, String delim, String folder,
                               String refFullPath) throws Exception
  {
    System.out.println("Post-process: Intermediate xml fullpath:"+intermediateXMLFullPath);
    System.out.println("Post-process: Backend doc filename: "+backendDocFilename);
    String backendDocFilePath = DocumentUtil.getUDocFilePath(folder, backendDocFilename);
    System.out.println("Post-process: Backend doc retrieved: "+backendDocFilePath);
    
    postprocessRNXML(intermediateXMLFullPath, backendDocFilePath, delim,refFullPath);
  }
  
  public void postprocessRNXML(String intermediateXMLFullPath, String originalBackendDocFullPath, String delim,
                               String refFullPath) throws Exception
  {
    File udoc = new File(intermediateXMLFullPath);
    if(! udoc.exists() || udoc.isDirectory())
    {
      throw new Exception("Can not locate the intermediate udoc file given filepath:"+intermediateXMLFullPath);
    }
    
    File originalBackendDoc = new File(originalBackendDocFullPath);
    if(! originalBackendDoc.exists() || originalBackendDoc.isDirectory())
    {
      throw new Exception("Can not locate the original udoc file given filepath:"+originalBackendDocFullPath);
    }
    
    delim = interpretDelim(delim);
    
    //Reading file
    ArrayList<String> udocFileInStr = readBackendFileAsStr(originalBackendDoc);
    if(udocFileInStr.size() < 1)
    {
      throw new Exception("Expecting the udoc file: "+intermediateXMLFullPath+" containing at least 2 lines");
    }
    String header = udocFileInStr.get(0);
    udocFileInStr.remove(0);
    String[] productInfo = udocFileInStr.toArray(new String[]{});
    
    //Get the respective product forecast from xml file
    GNDocument intermediateXML = GNXMLDocumentUtility.getDocument(udoc);
    ArrayList<GNElement> partnerProductForecast = getPartnerProductForecast(PARTNER_PRODUCT_FORECAST_XPATH, intermediateXML);
    
    //Get reference file
    GNDocument refFile = GNXMLDocumentUtility.getDocument(refFullPath);
    Hashtable<String, String> siteDunsMapping = getSiteDunsMapping(refFile);
    
    //fill in ProductForecast
    Hashtable<String, ArrayList<String>> produtInfoLocationHT = groupProductInfoInLocation(productInfo, delim, siteDunsMapping); 
    addProductForecast(partnerProductForecast, produtInfoLocationHT, header, delim);
    
    //output the document to the original file, so that the next Partner function acvity can get the file
    GNXMLDocumentUtility.writeToFile(intermediateXML, intermediateXMLFullPath, true, true);
  }
  
  private Hashtable<String, String> getSiteDunsMapping(GNDocument refXML) throws Exception
  {
    Hashtable<String, String> siteDunsMapping = new Hashtable<String, String>();
    List siteIDList = refXML.getNodes("REF/GETDUNS/SiteID");
    if(siteIDList != null && siteIDList.size() > 0)
    {
      for(int i = 0; i < siteIDList.size(); i++)
      {
        GNElement site = (GNElement)siteIDList.get(i);
        GNElement siteCode = site.getChild("SiteCode");
        GNElement dunsPlus4 = site.getChild("DUNSPlus4");
        siteDunsMapping.put(siteCode.getText(), dunsPlus4.getText());
      }
    }
    return siteDunsMapping;
  }
  
  /**
   * Add ProductForecast element into "PartnerProductForecast" element. The ProductForecast element will include 
   * the ProductSchedule which its value is derived from productInfo and header.
   */
  private void addProductForecast(ArrayList<GNElement> partnerProductForecastList, Hashtable<String, ArrayList<String>> productInfoLocationHT, 
                                  String header, String delim) throws Exception
  {
    if(partnerProductForecastList != null && partnerProductForecastList.size() > 0)
    {
      for(GNElement partnerProductForecast : partnerProductForecastList)
      {
        GNElement forecastPartnerEle  = partnerProductForecast.getChild("ForecastPartner");
        
        GNElement partnerDescEle = forecastPartnerEle.getChild("PartnerDescription");
        GNElement phsicalLocationEle = partnerDescEle.getChild("PhysicalLocation");
        GNElement locationEle = phsicalLocationEle.getChild("GlobalLocationIdentifier");
        String locationID = locationEle.getText();
        
        ArrayList<GNElement> productForecastList = new ArrayList<GNElement>();
        ArrayList<String> productInfoList = productInfoLocationHT.get(locationID);
        for(String productInfo : productInfoList)
        {
          String[] productInfos = productInfo.split(delim);
          String customerItem = productInfos[0];
          
          //create productForecast element
          GNElement productForecastEle = createProductForecastEle(customerItem);
          productForecastList.add(productForecastEle);
          
          //add productForecast into partnerProductForecast element
          partnerProductForecast.addElement(productForecastEle);
        }
        
        //add product schedule into the productForecast element
        addProductScheduleForProductForecast(productForecastList, productInfoList.toArray(new String[]{}), header, delim);
      }
    }
  }
  
  /**
   * @param productIdentifier
   * @return
   */
  private GNElement createProductForecastEle(String productIdentifier)
  {
    GNElement productForecast = GNXMLDocumentUtility.newElement("ProductForecast");
    GNElement measureCode = GNXMLDocumentUtility.newElement("GlobalProductUnitOfMeasureCode");
    measureCode.setText("Piece");
    productForecast.addElement(measureCode);
    
    //productForecastIdentifier
    GNElement productForecastIdentifier = GNXMLDocumentUtility.newElement("productForecastIdentifier");
    
    //ForecastIdentifierReference
    GNElement forecastIdRef = GNXMLDocumentUtility.newElement("ForecastIdentifierReference");
    GNElement forecastRefCode = GNXMLDocumentUtility.newElement("GlobalForecastReferenceTypeCode");
    forecastRefCode.setText("Contract number");
    GNElement refIdentifier = GNXMLDocumentUtility.newElement("ProprietaryReferenceIdentifier");
    refIdentifier.setText("1");
    forecastIdRef.addElement(forecastRefCode);
    forecastIdRef.addElement(refIdentifier);
    
    productForecastIdentifier.addElement(forecastIdRef);
    
    //ProductIdentification
    GNElement productIdentification = GNXMLDocumentUtility.newElement("ProductIdentification");
    GNElement globalProductID = GNXMLDocumentUtility.newElement("GlobalProductIdentifier");
    globalProductID.setText("11111111111111");
    
    GNElement partnerProductID = GNXMLDocumentUtility.newElement("PartnerProductIdentification");
    GNElement partnerClassificationCode = GNXMLDocumentUtility.newElement("GlobalPartnerClassificationCode");
    partnerClassificationCode.setText("Manufacturer");
    
    GNElement propietaryProductID = GNXMLDocumentUtility.newElement("ProprietaryProductIdentifier");
    propietaryProductID.setText(productIdentifier);
    
    partnerProductID.addElement(partnerClassificationCode);
    partnerProductID.addElement(propietaryProductID);
    
    productIdentification.addElement(globalProductID);
    productIdentification.addElement(partnerProductID);
    
    productForecast.addElement(productForecastIdentifier);
    productForecast.addElement(productIdentification);
    
    return productForecast;
  }
  
  public void addProductSchedule(String intermediateXMLFullPath, String originalBackendDocFullPath, String delim) throws Exception
  {
    File udoc = new File(intermediateXMLFullPath);
    if(! udoc.exists() || udoc.isDirectory())
    {
      throw new Exception("Can not locate the intermediate udoc file given filepath:"+intermediateXMLFullPath);
    }
    
    File originalBackendDoc = new File(originalBackendDocFullPath);
    if(! originalBackendDoc.exists() || originalBackendDoc.isDirectory())
    {
      throw new Exception("Can not locate the original udoc file given filepath:"+originalBackendDocFullPath);
    }
    
    delim = interpretDelim(delim);
    
    //Reading file
    ArrayList<String> udocFileInStr = readBackendFileAsStr(originalBackendDoc);
    if(udocFileInStr.size() < 1)
    {
      throw new Exception("Expecting the udoc file: "+intermediateXMLFullPath+" containing at least 2 lines");
    }
    String header = udocFileInStr.get(0);
    udocFileInStr.remove(0);
    String[] productInfo = udocFileInStr.toArray(new String[]{});
    
    //Get the respective product forecast from xml file
    GNDocument intermediateXML = GNXMLDocumentUtility.getDocument(udoc);
    ArrayList<GNElement> productForecast = getProductForecast(PRODUCT_IDENTIFIER_XPATH, intermediateXML);
    
    //fill in all those date
    addProductScheduleForProductForecast(productForecast, productInfo, header, delim);
    
    //output the document to the original file, so that the next Partner function acvity can get the file
    GNXMLDocumentUtility.writeToFile(intermediateXML, intermediateXMLFullPath, true, true);
  }
  
  private ArrayList<String> readBackendFileAsStr(File udoc) throws Exception
  {
    BufferedInputStream input = null;
    BufferedReader reader = null;
    FileInputStream fileInput = null;
    try
    {
      fileInput = new FileInputStream(udoc);
      input = new BufferedInputStream(fileInput);
      reader = new BufferedReader(new InputStreamReader(input));
      String s = "";
      ArrayList<String> udocInStr = new ArrayList<String>();
      
      //String header = reader.readLine();
      //udocInStr.add(header);
      while( (s = (reader.readLine())) != null)
      {
        if(s.trim().length() > 0)
        {
          udocInStr.add(s);
        }
      }
      
      return udocInStr;
    }
    catch(Exception ex)
    {
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
      }
      catch(Exception ex1)
      {
        ex1.printStackTrace();
      }
    }
    
  }
  
  /**
   * Group the ProductInfo that fall under the same site .  
   * @param productInfos the list of ProductInfo in the udoc flat file.
   * @param delim The delim that is used in the ProductInfo string
   * @param siteDunsMapping The mapping from site to Duns
   * @return mapping from Duns to the list of ProductInfo
   * @throws Exception throw if we can't find the mapping from Site to Duns through the REF.xml
   */
  private Hashtable<String, ArrayList<String> > groupProductInfoInLocation(String[] productInfos, String delim, 
                                                                           Hashtable<String, String> siteDunsMapping) throws Exception
  
  {
    Hashtable<String, ArrayList<String>> productInfoByLocation = new Hashtable<String, ArrayList<String>>();
    if(productInfos != null && productInfos.length > 0)
    {
      for(String productInfo : productInfos)
      {
        String[] productInfoArr = productInfo.split(delim);
        String locationID = productInfoArr[2];
        
        String duns = siteDunsMapping.get(locationID);
        if(duns == null)
        {
          throw new Exception("We can't find the corresponding duns given site:"+locationID+". Make sure REF.xml is updated.");
        }
        
        if(productInfoByLocation.containsKey(duns))
        {
          ArrayList<String> locationProInfo = productInfoByLocation.get(duns);
          locationProInfo.add(productInfo);
        }
        else
        {
          ArrayList<String> locationProInfo = new ArrayList<String>();
          locationProInfo.add(productInfo);
          productInfoByLocation.put(duns, locationProInfo);
        }
      }
    }
    
    return productInfoByLocation;
  }
  
  private ArrayList<GNElement> getProductForecast(String productIdentifierXpath, GNDocument intermediateRNXML) throws GNXMLException
  {
    ArrayList<GNElement> productForecastEles = new ArrayList<GNElement>();
    List productNodes = intermediateRNXML.getNodes(productIdentifierXpath);
    for(int i = 0; productNodes !=null && i < productNodes.size(); i++)
    {
      GNElement productIdentifier = (GNElement)productNodes.get(i);
      GNElement productIdentification = productIdentifier.getParent();
      GNElement productForecast = productIdentification.getParent();
      
      //There will be a <ProductSchedule> that is empty and need to be deleted. This extra element
      //is generated in GridMappter
      productForecast.removeChildren(PRODUCT_SCHEDULE);
      
      productForecastEles.add(productForecast);
    }
    
    return productForecastEles;
  }
  
  private ArrayList<GNElement> getPartnerProductForecast(String globalLocationXPath, GNDocument intermediateRNXML) throws GNXMLException
  {
    ArrayList<GNElement> partnerProductForecastList = new ArrayList<GNElement>();
    List locationIdNodes = intermediateRNXML.getNodes(globalLocationXPath);
    for(int i =0; locationIdNodes != null && i < locationIdNodes.size(); i++)
    {
      GNElement locationIdEle = (GNElement)locationIdNodes.get(i);
      GNElement physicalLocationEle = locationIdEle.getParent();
      GNElement partnerDescEle = physicalLocationEle.getParent();
      GNElement forecastPartnerEle = partnerDescEle.getParent();
      GNElement partnerProductForecastEle = forecastPartnerEle.getParent();
      
      partnerProductForecastEle.removeChildren(PRODUCT_FORECAST);
      partnerProductForecastList.add(partnerProductForecastEle);
    }
    return partnerProductForecastList;
  }
  
  /**
   * Add the XML Element "ProductSchedule" into the given list of XML Element "ProductForecast"
   * @param productForecast
   * @param productInfo
   * @param productHeader
   * @param productInfoDelim
   * @throws Exception
   */
  private void addProductScheduleForProductForecast(ArrayList<GNElement> productForecast, String[] productInfo, String productHeader, String productInfoDelim) throws Exception
  {
    if( (productInfo == null || productForecast == null) || (productInfo.length != productForecast.size())) 
    {
      throw new Exception("Num product item in intermediate xml does not match with the backend document !! productInfo--> "+(productInfo != null ? productInfo.length : 0)+ " productForecast--> "+ (productForecast != null ? productForecast.size() : 0));
    }
    
    if(productInfo.length < 1)
    {
      throw new Exception("No product item info in backend document !! ");
    }
  
    //remove the additional column from productHeader
    //#Customer Item  Supplier  Site  CFG Data Measure  01/21/2009
    ArrayList<String> dateHeader = convertStrToArray(productHeader, productInfoDelim);
    String[] dateHeaderArr = removeAdditionalColumnItem(dateHeader, 5);
    
    if(dateHeaderArr == null || dateHeaderArr.length  < 1)
    {
      throw new Exception("Expecting at least 1 date header !");
    }
    
    for(int i = 0; i < productForecast.size() ; i++)
    {
      String productInfoInStr = productInfo[i].replaceAll(productInfoDelim, productInfoDelim+" ");
      
//    remove the additional column from productInfo
      ArrayList<String> productInfoList = convertStrToArray(productInfoInStr, productInfoDelim);
      
//    TODO: dun hard code the num item for deletion...
      String[] productInfoArr = removeAdditionalColumnItem(productInfoList, 5);
      
      addProductSchedule(productForecast.get(i), productInfoArr, dateHeaderArr);
    }
    
  }
  
  private ArrayList convertStrToArray(String entry, String delim)
  {
    StringTokenizer st = new StringTokenizer(entry, delim);
    ArrayList<String> strList = new ArrayList<String>();
    
    
    while(st.hasMoreTokens())
    {
      String token = (st.nextToken()).trim();
      strList.add(token);
    }
    
    return strList;
  }
  
  private String[] removeAdditionalColumnItem(ArrayList<String> columns, int numColumnToDelete)
  {
    if(columns.size() < numColumnToDelete)
    {
      return (String[])columns.toArray(new String[]{});
    }
    else
    {
      String[] str = new String[columns.size() - numColumnToDelete];
      int counter = 0;
      
      for(int i = numColumnToDelete; i < columns.size(); i++)
      {
        str[counter] = columns.get(i);
        counter++;
      }
      
      return str;
    }
  
  }
  
  /**
   * Add the XML Element "ProductSchedule" into the element "ProductForecast".
   * @param productForecast
   * @param productQuantityInfo
   * @param productDateHeader
   * @throws Exception
   */
  private void addProductSchedule(GNElement productForecast, String[] productQuantityInfo, String[] productDateHeader) throws Exception
  {
    if(productDateHeader == null || productDateHeader.length ==0)
    {
      return;
    }
    
    String nextDateInStr;
    Date nextDate = null;
    boolean isPreviousDateWeek = false;
    
    for(int i = 0 ; i < productDateHeader.length; i++)
    {
      if( (i+1) < productDateHeader.length)
      {
        nextDateInStr = productDateHeader[i+1];
        nextDate = formatDate(nextDateInStr, DATE_FORMAT);
      }
      
      String dateInStr = productDateHeader[i];
      Date date = formatDate(dateInStr, DATE_FORMAT);
      String rnDate = formatDateToString(date, RN_DATE_FORMAT);
      String forecastIntervalCode = "";
      String productQuantity = "".equals(productQuantityInfo[i]) ? 0+"" : productQuantityInfo[i];
      
      forecastIntervalCode = interpretIntervalCode(date, nextDate, isPreviousDateWeek); //determine the 'Day', 'Week'
      if(FORECAST_INTERVAL_WEEK.equals(forecastIntervalCode))
      {
          isPreviousDateWeek = true;
      }
      
      GNElement productSchedule = getProductScheduleElement(rnDate, forecastIntervalCode, productQuantity, FORECAST_QUANTITY_TYPE_CODE);
      productForecast.addElement(productSchedule);
      nextDate = null;
      
    }
  }
  
  /**
   * Check the forecast interval code and check whether we should put as "Week" or "Day" 
   * base on the given currentDate and nextDate, and the flag isPreviousWeek
   * 
   * EG: currentDate:2009-02-01, nextDate: 2009-02-08, it will return as week
   * 
   * @param currentDate
   * @param nextDate
   * @param isPreviousWeek Indicate whether the currentDate previous entry is a week or a day.
   * @return
   */
  private String interpretIntervalCode(Date currentDate, Date nextDate, boolean isPreviousWeek)
  {
    if(isPreviousWeek)//1/6/2009,  2/6/2009, 9/6/2009, 16/6/2009
    {
      return FORECAST_INTERVAL_WEEK;
    }
    
    if(nextDate == null && !isPreviousWeek)//handle 1/6/2009, 2/6/2009, and 1/6/2009 alone
    {
      return FORECAST_INTERVAL_DAY;
    }
    
    Calendar current = Calendar.getInstance();
    current.setTime(currentDate);
    int dayOfYearForCurrent = current.get(Calendar.DAY_OF_YEAR);
    
    
    Calendar next = Calendar.getInstance();
    next.setTime(nextDate);
    int dayOfYearForNext = next.get(Calendar.DAY_OF_YEAR);
    
    return ((dayOfYearForNext - dayOfYearForCurrent) >=7) ? FORECAST_INTERVAL_WEEK : FORECAST_INTERVAL_DAY;
    
  }
  
  private Date formatDate(String date, String format) throws Exception
  {
    SimpleDateFormat formatter = new SimpleDateFormat(format);
    return formatter.parse(date);
  }
  
  private String formatDateToString(Date date, String rnDateFormat) throws Exception
  {
    SimpleDateFormat formatter = new SimpleDateFormat(rnDateFormat);
    return formatter.format(date);
  }
  
  /**
   * @param dateHeader
   * @param quantity a str of quantity seperated by the given delim
   * @param delim
   * @return
   */
  private String[][] getProductScheduleInfo(String[] dateHeader, String quantityStr, String delim)
  {
    String[][] productScheduleInfo = new String[dateHeader.length][2];
    StringTokenizer st = new StringTokenizer(quantityStr, delim);
    int i = 0;
    
    while(st.hasMoreTokens())
    {
      String quantity = st.nextToken();
      productScheduleInfo[i][0] = dateHeader[i];
      productScheduleInfo[i][1] = quantity;
      
      System.out.println(i+" Date header "+productScheduleInfo[i][0]);
      System.out.println(i+" Quantity "+productScheduleInfo[i][0]);
    }
    
    return productScheduleInfo;
  }
  
  private GNElement getProductScheduleElement(String rnDateStamp, String forecastIntervalCode,
                                              String quantity, String forecastQuantityTypeCode)
  {
    GNElement productSchedule = GNXMLDocumentUtility.newElement("ProductSchedule");
    GNElement forecastProductSchedule = GNXMLDocumentUtility.newElement("ForecastProductSchedule");
    GNElement forecastPeriod = GNXMLDocumentUtility.newElement("ForecastPeriod");
    GNElement datePeriod = GNXMLDocumentUtility.newElement("DatePeriod");
    GNElement beginDate = GNXMLDocumentUtility.newElement("beginDate");
    GNElement dateStamp = GNXMLDocumentUtility.newElement("DateStamp");
    GNElement globalForecastIntervalCode = GNXMLDocumentUtility.newElement("GlobalForecastIntervalCode");
    GNElement productQuantity = GNXMLDocumentUtility.newElement("ProductQuantity");
    GNElement orderForecastQuantityTypeCode = GNXMLDocumentUtility.newElement("OrderForecastQuantityTypeCode");
    
    //init value
    dateStamp.setText(rnDateStamp);
    globalForecastIntervalCode.setText(forecastIntervalCode);
    productQuantity.setText(""+quantity);
    orderForecastQuantityTypeCode.setText(forecastQuantityTypeCode);
    
    beginDate.addElement(dateStamp);
    datePeriod.addElement(beginDate);
    forecastPeriod.addElement(datePeriod);
    forecastPeriod.addElement(globalForecastIntervalCode);
    forecastProductSchedule.addElement(forecastPeriod);
    forecastProductSchedule.addElement(productQuantity);
    productSchedule.addElement(forecastProductSchedule);
    productSchedule.addElement(orderForecastQuantityTypeCode);
    
    return productSchedule;
  }
  
  /**
   * @param gdoc
   * @throws Exception
   */
  /*
  public void setGDOC(Object gdoc)  throws Exception
  {
    GridDocument gdoc1 = (GridDocument)gdoc;
    gdoc1.setCustom2("haha");
    System.out.println("Set custom 2 haha");
    
    File udoc = new File(gdoc1.getUdocFullPath());
    writeToFile(udoc);
  }
  
  private void writeToFile(File udoc) throws Exception
  {
    System.out.println("Writing to file: "+udoc.getAbsolutePath());
    FileWriter writer = new FileWriter(udoc);
    writer.write("overrite the file");
    writer.close();
  }*/
}
