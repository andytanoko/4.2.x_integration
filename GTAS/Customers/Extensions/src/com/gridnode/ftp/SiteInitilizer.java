package com.gridnode.ftp;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: GridNode Pte Ltd</p>
 * @author not attributable
 * @version 1.0
 */
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SiteInitilizer
{ 

  public SiteInitilizer()
  {
  }

  public static Map initilizeSites() throws Exception
  {

    System.out.println(ISiteConfig.CONFIG_LOC + ISiteConfig.CONFIG_NAME);
    InputStream input = new FileInputStream(new File(
    	ISiteConfig.CONFIG_LOC + ISiteConfig.CONFIG_NAME));

    /*Thread.currentThread(
        ).getContextClassLoader().getResourceAsStream(
        ISiteConfig.CONFIG_LOC + ISiteConfig.CONFIG_NAME);
    */

    if (input == null)
      return null;
    else
    	return setSiteConfigs(input);
  }

  private static Map setSiteConfigs(InputStream input) throws Exception
  {
    myXML myxml =  new myXML(new BufferedReader(new InputStreamReader(input)));
    return convertXMLToSite(myxml);
  }

  private static Map convertXMLToSite(myXML myxml)
  {
    Map siteConfig = new HashMap();
    String provider = myxml.Attribute.find(ISiteConfig.PROVIDER);
    siteConfig.put(ISiteConfig.PROVIDER,provider);
    for (int i=0;i<myxml.size();i++)
    {
      myXML siteElement = myxml.getElement(i);
      String siteName = siteElement.Attribute.find(ISiteConfig.NAME);
      Site site = new Site(siteName);
      myXML folderElement = siteElement.findElement(ISiteConfig.FOLDER);
      site = setSiteKeyValues(folderElement,site);
      myXML commElement = siteElement.findElement(ISiteConfig.COMMUNICATION);
      site = setSiteKeyValues(commElement,site);
      myXML dbElement = siteElement.findElement(ISiteConfig.DATABASE);
      site = setSiteKeyValues(dbElement,site);
      siteConfig.put(siteName,site);
    }
    return siteConfig;
  }

  private static Site setSiteKeyValues(myXML fldElement,Site site)
  {
    for (int j=0;j<fldElement.size();j++)
    {
      myXML subElements = fldElement.getElement(j);
      site.setProperty(subElements.getTag(),subElements.getValue());
      System.out.println(subElements.getTag()+"   "+subElements.getValue());
    }
    return site;
  }



  public static void main(String args[]) throws Exception
  {
    System.out.println(initilizeSites());
  }

}