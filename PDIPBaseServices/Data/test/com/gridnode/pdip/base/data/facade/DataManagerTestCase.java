package com.gridnode.pdip.base.data.facade;

import java.io.*;
import java.net.*;
import java.util.*;

import com.gridnode.pdip.base.data.entities.model.*;
import com.gridnode.pdip.base.data.facade.ejb.*;
import com.gridnode.pdip.base.data.facade.helpers.*;
import com.gridnode.pdip.framework.db.entity.*;
import com.gridnode.pdip.framework.db.filter.*;
import com.gridnode.pdip.framework.file.access.*;
import com.gridnode.pdip.framework.j2ee.*;
import com.gridnode.pdip.framework.util.*;
import junit.framework.*;

public class DataManagerTestCase extends TestCase{


  Long contextUId=new Long(1000);
  String testDomain="localhost";

  String businessDocumentName="TestDocument";
  String testFileName="test"+File.separatorChar+"testDocument.txt";
  String fileSepcLocation, stringSepcLocation, byteSepcLocation, memSepcLocation;

  public static Vector locationVect;
  public static Long busUId;
  public static String sepcLocation;
  public static FileAccess fileAccess;
  public static IDataManagerObject dataManager;
  public static DataItem dataItem;

  public DataManagerTestCase(String s) {
    super(s);
  }

  protected void setUp() {
    fileAccess=new FileAccess();
  }

  public void ttestDeployBusinessDocument(){
      File testDocument=new File(testFileName);
      System.out.println("Src="+testDocument.getAbsolutePath());
      System.out.println("des="+testFileName);
      try {
      String result= fileAccess.createFile(testDomain, testFileName, testDocument, true);
      assertNotNull("Unable to deploy BusinessDocument "+testDocument.getAbsolutePath(),result);
      } catch (Exception e) {
    	  assertTrue("Unable to create BusinessDocument "+testDocument.getAbsolutePath(), false);
      }
  }

  public void testDeployTestData() throws Exception {
    try{
      locationVect=new Vector();
      String fileSepcLocation="http://File/"+testDomain+"/"+URLEncoder.encode(testFileName);
      locationVect.add(fileSepcLocation);

      StringData stringData=new StringData();
      stringData.setData("This is to test StringData");
      stringData=(StringData)UtilEntity.createEntity(stringData,false);
      stringSepcLocation= KeyConverter.getKey((Long)stringData.getKey(),StringData.ENTITY_NAME,IData.ENTITY_TYPE);
      assertNotNull("Unable to deploy StringData ",stringSepcLocation);
      locationVect.add(stringSepcLocation);

      ByteData byteData=new ByteData();
      byteData.setDataType("byte[]");
      byteData.setData(new String("This is to test ByteData").getBytes());
      byteData=(ByteData)UtilEntity.createEntity(byteData,false);
      byteSepcLocation= KeyConverter.getKey((Long)byteData.getKey(),ByteData.ENTITY_NAME,IData.ENTITY_TYPE);
      assertNotNull("Unable to deploy byteData ",byteSepcLocation);
      locationVect.add(byteSepcLocation);
    }catch(Throwable e){
        assertTrue("Unable to deploy TestData "+e.getMessage(),false);
    }
  }

  public void testCreateBusinessDocument() throws Throwable {
    try{
        for(int i=0;i<locationVect.size();i++){
            System.out.println("Location :"+locationVect.elementAt(i).toString());
            BusinessDocument businessDocument=new BusinessDocument();
            businessDocument.setConditionExpr("true");
            businessDocument.setName(businessDocumentName);
            businessDocument.setSpecElement("SpectElement");
            businessDocument.setSpecLocation(locationVect.elementAt(i).toString());
            businessDocument=(BusinessDocument)UtilEntity.createEntity(businessDocument,false);
            assertNotNull("Unable to create BusinessDocument with name "+businessDocumentName, businessDocument);
            //busUId=(Long)businessDocument.getKey();
        }
    }catch(Throwable e){
        //e.printStackTrace();
        throw e;
    }
  }

  public void testCreateDataItem() throws Throwable {
    try{
        Collection defKeyColl=getTestDefinitionKeys();
        assertTrue("Unable to get defKeys :", defKeyColl.size()>0);
        Iterator iterator=defKeyColl.iterator();
        while(iterator.hasNext()){
            String defKey=iterator.next().toString();
            dataItem=getDataManager().createDataItem(defKey,"testContextType",contextUId,true);
            assertNotNull("Unable to create DataItem with defKey:"+defKey, dataItem);
            Map map=getDataManager().getFieldDataLocationMaps((Long)dataItem.getKey());
            assertNotNull("Unable to getDataItemLocationMaps for dataitem :"+dataItem.getKey(), map);
            assertTrue("Unable to get getDataItemLocationMaps for dataitem :"+dataItem.getKey(), defKeyColl.size()>0);
        }
    }catch(Exception e){
        //e.printStackTrace();
        throw e;
    }
  }

 public void testGetContextData() throws Exception {
    try{
        Collection coll=getDataManager().getContextData(contextUId);
        assertNotNull("Unable to getContextData for contextUId :"+contextUId, coll);
        assertTrue("Unable to getContextData,size is zero for contextUId :"+contextUId, coll.size()>0);
    }catch(Exception e){
        //e.printStackTrace();
        throw e;
    }
 }

 public void testSaveToMaster() throws Exception{
    DataItem dataItem=null;
    try{
        Collection coll=getDataManager().getContextData(contextUId);
        assertNotNull("Unable to getContextData for contextUId :"+contextUId, coll);
        assertTrue("Unable to getContextData,size is zero for contextUId :"+contextUId, coll.size()>0);
        Iterator iterator=coll.iterator();
        while(iterator.hasNext()){
            dataItem=(DataItem)iterator.next();
            getDataManager().saveToMaster((Long)dataItem.getKey());
        }
    }catch(Exception e){
        assertTrue("Unable to savrToMaster, for dataitem:"+dataItem+" ,exception:"+e.getMessage(),false);
    }
  }

  public void testRemoveContextData() throws Exception {
    try{
        getDataManager().removeContextData(contextUId);
    }catch(Exception e){
        //e.printStackTrace();
        throw e;
    }
  }

    public void testRemoveTestData() throws Throwable {
        for(int i=0;i<locationVect.size();i++){
            String key=locationVect.elementAt(i).toString();
            IDataFilter filter=new DataFilterImpl();
            filter.addSingleFilter(null,BusinessDocument.SPEC_LOCATION,filter.getEqualOperator(),key,false);
            UtilEntity.remove(filter,BusinessDocument.ENTITY_NAME,false);
            String type = KeyConverter.getType(key);
            if(type!=null && IData.ENTITY_TYPE.equals(type)){
                UtilEntity.remove(KeyConverter.getUID(key),KeyConverter.getEntityName(key),false);
            }
        }
    }


/*

  public void testGetDataObject(){
    try{
        assertNotNull("DataItem is null", dataItem);
        Map map=getDataManager().getFieldDataLocationMaps((Long)dataItem.getKey());
        assertNotNull("Unable to getFieldDataLocationMaps for dataitem :"+dataItem.getKey(), map);
        String dataLocation=map.get(map.keySet().iterator().next()).toString();
        Object obj=getDataManager().getObjectData(dataLocation);
        assertNotNull("Unable to getData from datalocation :"+dataLocation, obj);
    }catch(Exception e){
        e.printStackTrace();
    }
  }

  public void testSetDataObject(){
    try{
        String testString="This is to test Data";
        assertNotNull("DataItem is null", dataItem);
        Map map=getDataManager().getFieldDataLocationMaps((Long)dataItem.getKey());
        assertNotNull("Unable to getFieldDataLocationMaps for dataitem :"+dataItem.getKey(), map);
        String dataLocation=map.get(map.keySet().iterator().next()).toString();
        getDataManager().setObjectData(dataLocation,testString.getBytes());
        Object obj=getDataManager().getObjectData(dataLocation);
        assertNotNull("Unable to getData from datalocation :"+dataLocation, obj);
        String retString=new String((byte[])obj);
        assertEquals("Unable to setData,returned data"+retString,testString,retString);
    }catch(Exception e){
        e.printStackTrace();
    }
  }

*/

  public Collection getTestDefinitionKeys() throws Throwable {
    try{
        Collection defKeyColl=new Vector();
        IDataFilter filter =new DataFilterImpl();
        filter.addSingleFilter(null,BusinessDocument.NAME,filter.getEqualOperator(),businessDocumentName,false);
        Collection coll=UtilEntity.getEntityByFilter(filter,BusinessDocument.ENTITY_NAME,false);
        int i=0;
        if(coll!=null && coll.size()!=0){
            Iterator iterator=coll.iterator();
            while(iterator.hasNext()){

                IEntity entity=(IEntity)iterator.next();
                if(i==1){
                    String defKey=KeyConverter.getKey((Long)entity.getKey(),BusinessDocument.ENTITY_NAME,IData.MEM_TYPE);
                    defKeyColl.add(defKey);
                }
                String defKey=KeyConverter.getKey((Long)entity.getKey(),BusinessDocument.ENTITY_NAME,"Data");
                defKeyColl.add(defKey);
                i++;
            }
        }
        return defKeyColl;
    }catch(Throwable e){
        throw e;
    }
  }

  public IDataManagerObject getDataManager(){
    try{
        if(dataManager==null){
            IDataManagerHome home=(IDataManagerHome)ServiceLookup.getInstance(ServiceLookup.CLIENT_CONTEXT).getHome(IDataManagerHome.class);
            dataManager=home.create();
        }
    }catch(Exception e){
        e.printStackTrace();
    }
    return dataManager;
  }

  public void tearDown(){
  }


  public static Test suite() {
     return new TestSuite(DataManagerTestCase.class);
  }

  public static void main(String args[]) throws Exception{
   junit.textui.TestRunner.run (suite());
  }

}

