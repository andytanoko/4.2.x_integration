/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BpssModelHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 5, 2005    Tam Wei Xiang       Created
 * May 3, 2008    Tam Wei Xiang       Notify the change of the BpssProcessSpecification
 *                                    to the nodes in the cluster other than the one
 *                                    that perform the undeploy of the Bpss.
 */
package com.gridnode.pdip.app.deploy.manager.bpss;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import com.gridnode.pdip.app.deploy.manager.GWFDeployableList;
import com.gridnode.pdip.app.deploy.manager.IConstants;
import com.gridnode.pdip.app.deploy.manager.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.deploy.manager.util.Logger;
import com.gridnode.pdip.app.deploy.manager.util.Utilities;
import com.gridnode.pdip.base.gwfbase.bpss.helpers.BpssDefinitionCache;
import com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration;
import com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecEntry;
import com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecification;
import com.gridnode.pdip.base.gwfbase.bpss.model.IBpssProcessSpecEntry;
import com.gridnode.pdip.base.gwfbase.bpss.model.IBpssProcessSpecification;
import com.gridnode.pdip.base.gwfbase.notification.ProcessDefChangeNotification;
import com.gridnode.pdip.framework.db.AbstractEntityHandler;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.domain.GWFException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.notification.Notifier;
import com.gridnode.xml.adapters.GNDocument;
import com.gridnode.xml.adapters.GNDocumentType;
import com.gridnode.xml.adapters.GNElement;
import com.gridnode.xml.adapters.GNXMLDocumentUtility;

/**
 * @author Tam Wei Xiang
 *
 * @since GT 4.0
 */
public class BpssModelHandler
{
	private final String _classname = "BpssModelHandler";
	private final String _defaultSystemId = "BPSS1.01.dtd";
	
	public BpssModelHandler()
	{
		
	}
	/**
	 * Unmarshal the xml document to ProcessSpecification obj
	 * @param file an xml document
	 * @return
	 */
	public ProcessSpecification loadModel(File processSpecFile)
		throws ApplicationException
	{
		Logger.log("[BpssModelHandler.loadModel] Loading model for processSpecFile "+ processSpecFile.getAbsolutePath());
		
		File dtdFile = this.getDTDFile();
		
		if(!validateModel(processSpecFile, dtdFile))
		{
			return null;
		}
		
		return new ProcessSpecification().buildModel(getRootElement(processSpecFile));
		
	}
	
	/**
	 * Unmarshal the inputstream to the ProcessSpecification obj.
	 * @param input
	 * @return
	 * @throws ApplicationException
	 */
	public ProcessSpecification loadModel(InputStream input)
		throws ApplicationException
	{
		try
		{
			//Due to the reader will close the stream,
			//we need to construct byteArrayInputStream. The closing operation on 
			//such a stream will not have any effect.
			byte[] b = new byte[input.available()];
			input.read(b);
			
			ByteArrayInputStream byteStream = new ByteArrayInputStream(b);
			
			InputStreamReader reader = new InputStreamReader(byteStream);
			boolean isValid = GNXMLDocumentUtility.validateDTD(reader, this.getDTDFile().getAbsolutePath());
			
			if(!isValid)
			{
				return null;
			}
			
			byteStream.reset();
			return new ProcessSpecification().buildModel(getRootElement(byteStream));
		}
		catch(Exception ex)
		{
			Logger.warn("loadModel failed", ex);
			throw new ApplicationException("[BpssModelHanler.loadModel(InputStream)] loadModel failed.", ex);
		}
	}
	
	/* Currently not supported
	public ProcessSpecification loadModel(Reader reader)
	{
		//get root element from reader ? xml util only support read from inputSteam
		return null;
	}*/
	
	/**
	 * We will unmarshal the bpss xml file represent by the filepath to 
	 * ProcessSpecification obj. 
	 * @param filepath the absolute path of the xml file.
	 * @return
	 */
	public ProcessSpecification loadModel(String filepath)
		throws SystemException
	{
		try
		{
			File processSpecFile = new File(filepath);
		  return this.loadModel(processSpecFile);
		}
		catch(Exception ex)
		{
			throw new SystemException("[BpssModel.loadModel(filepath)] Cannot load model for file "+filepath, ex);
		}
	}
	
	/**
	 * We will marshal the ProcessSpec obj into the file user specify.
	 * If such a file is not existed, a new file will be created. If it exist, it will be
	 * overwritten.
	 * @param file
	 * @param ps
	 * @throws ApplicationException
	 */
	public void saveModel(File file, ProcessSpecification ps)
		throws ApplicationException
	{
		try
		{
			GNDocumentType docType = GNXMLDocumentUtility.newDocumentType(ps.get_TagName(), ps.getSystemId());
			
			GNDocument gnDoc = GNXMLDocumentUtility.newDocument(ps.buildElement(), docType);
			
			GNXMLDocumentUtility.writeToFile(gnDoc, file.getAbsolutePath(), true, true);
		}
		catch(Exception ex)
		{
			Logger.warn("[BpssModel.saveModel(File, ProcessSpecification)]", ex);
			throw new ApplicationException("[BpssModel.saveModel(File, ProcessSpecification)] Unable to output the process specification to file.", ex);
		}
	}
	
	/**
	 * We will marshal the ProcesSpecification obj to the OutputStream that user specify. 
	 * @param output
	 * @param ps
	 * @throws ApplicationException
	 */
	public void saveModel(OutputStream output, ProcessSpecification ps)
		throws ApplicationException, IOException
	{
		try
		{
			GNDocumentType docType = GNXMLDocumentUtility.newDocumentType(ps.get_TagName(), ps.getSystemId());
			
			GNDocument gnDoc = GNXMLDocumentUtility.newDocument(ps.buildElement(), docType);
			ByteArrayOutputStream byteArrayOut = GNXMLDocumentUtility.writeToStream(gnDoc, true, true);
			
			
			output.write(byteArrayOut.toByteArray());
			output.flush();
			output.close();
			
		}
		catch(Exception ex)
		{
			Logger.warn("[BpssModel.saveModel(File, ProcessSpecification)]", ex);
			throw new ApplicationException("[BpssModel.saveModel(OutputStream, ProcessSpecification)] Unable to output the process specification to OutputStream.", ex);
		}
		finally
		{
			if(output!=null)
			{
				output.close();
			}
		}
	}
	
	/* Currently not supported
	public void saveModel(Writer writer, ProcessSpecification ps)
	{
		//xml util didn't support write gn element into reader ???
	}*/
	
	/**
	 * We will marshal the ProcessSpecification obj into the file represent by the file
	 * path.
	 * If such a file is not existed, a new file will be created. If it exist, it will be
	 * overwritten.
	 */
	public void saveModel(String filename, ProcessSpecification ps)
		throws ApplicationException
	{
		File outputFile = new File(filename);
		saveModel(outputFile, ps);
	}
	
	/**
	 * This method is moved out from ProcessSpecification
	 * Method signature has been changed accept ProcessSpecfication obj.
	 * @param bpssModel
	 * @return
	 * @throws GWFException
	 */
	public int deploy(ProcessSpecification bpssModel)
  	throws GWFException
  {
		Logger.log("[BpssModelHandler.deploy] Deploying ProcessSpecfication ... ");
		Hashtable incomplete = new Hashtable();
		//Ready a list of entities to deploy to
		java.util.Vector all = new java.util.Vector(9);
		//Create 9 empty lists
		for (int i = 0; i < 9; i++)
		{
			GWFDeployableList list = new GWFDeployableList();
			all.add(list);
		}
		//Populate the lists
		((GWFDeployableList)all.get(0)).addAll(java.util.Arrays.asList(bpssModel.getBinaryCollaboration()));
		((GWFDeployableList)all.get(1)).addAll(java.util.Arrays.asList(bpssModel.getBusinessDocument()));
		((GWFDeployableList)all.get(2)).addAll(java.util.Arrays.asList(bpssModel.getBusinessTransaction()));
		((GWFDeployableList)all.get(3)).addAll(java.util.Arrays.asList(bpssModel.getDocumentation()));
		((GWFDeployableList)all.get(4)).addAll(java.util.Arrays.asList(bpssModel.getInclude()));
		((GWFDeployableList)all.get(5)).addAll(java.util.Arrays.asList(bpssModel.getMultiPartyCollaboration()));
		((GWFDeployableList)all.get(6)).addAll(java.util.Arrays.asList(bpssModel.getSubstitutionSet()));
		((GWFDeployableList)all.get(7)).addAll(java.util.Arrays.asList(bpssModel.getPackage()));
		((GWFDeployableList)all.get(8)).addAll(java.util.Arrays.asList(bpssModel.getProcessSpecification()));
		int finalResult = 0;
		try
		{
			//Query for existing specs
			IDataFilter filter = new DataFilterImpl();
			filter.addSingleFilter(null, IBpssProcessSpecification.UUID,
			                       filter.getEqualOperator(), bpssModel.getUuid(), false);
			filter.addSingleFilter(filter.getAndConnector(),
			                       IBpssProcessSpecification.VERSION,
			                       filter.getEqualOperator(), bpssModel.getVersion(),
			                       false);
			AbstractEntityHandler handler = EntityHandlerFactory.getHandlerFor(
                               BpssProcessSpecification.ENTITY_NAME,
                               true);
			Collection bpssProcSpecColl = handler.getEntityByFilter(filter);
			if (bpssProcSpecColl.isEmpty())
			{
				//new spec, so it can be deployed
				BpssProcessSpecification bpssProcSpec = new BpssProcessSpecification();
				//bpssProcSpec.setSpecName(this.getName().substring(1,this.getName().length()));
				bpssProcSpec.setSpecName(bpssModel.getName());
				bpssProcSpec.setSpecUUId(bpssModel.getUuid());
				bpssProcSpec.setSpecVersion(bpssModel.getVersion());
				bpssProcSpec.setSpecTimestamp(new java.sql.Time(System.currentTimeMillis()).toString());
				bpssProcSpec = (BpssProcessSpecification)handler.createEntity(
				                                                              bpssProcSpec);

				//Deploy only those lists that are not empty
				for (int i = 0; i < 9; i++)
				{
					System.out.println(all.get(i));
					if ((GWFDeployableList)all.get(i) != null)
					{
						GWFDeployableList data = (GWFDeployableList)all.get(i);
						int result = data.deploy(bpssProcSpec.getUId(), incomplete);
						finalResult = Utilities.combineResult(finalResult, result);
					}
				}
				Utilities.parseAgain(incomplete, bpssProcSpec.getUId());
				Utilities.parseMultiPartyCollaboration(incomplete,
				                                       bpssProcSpec.getUId());
			}
			else if (bpssProcSpecColl.size() > 1)
			{
				throw new GWFException();
			}
			else
			{
				Iterator it = bpssProcSpecColl.iterator();
				BpssProcessSpecification matchSpec = (BpssProcessSpecification)it.next();
				if (matchSpec == null)
				{
					throw new GWFException();
				}
				//update existing specs
				// TODO: Check if any of the specifications is currently instantiated in runtime
				// if instantiated, return with GWF_NOTHING_PERFORMED
				//Otherwise undeploy the other tables before deploying new specs
				undeploy(bpssModel);
				BpssProcessSpecification bpssProcSpec = new BpssProcessSpecification();
				//bpssProcSpec.setSpecName(this.getName().substring(1,this.getName().length()));
				bpssProcSpec.setSpecName(bpssModel.getName());
				bpssProcSpec.setSpecUUId(bpssModel.getUuid());
				bpssProcSpec.setSpecVersion(bpssModel.getVersion());
				bpssProcSpec.setSpecTimestamp(new java.sql.Date(System.currentTimeMillis()).toString());
				bpssProcSpec = (BpssProcessSpecification)handler.createEntity(
				                                                              bpssProcSpec);

				//Deploy only those lists that are not empty
				for (int i = 0; i < 9; i++)
				{
					if ((GWFDeployableList)all.get(i) != null)
					{
						GWFDeployableList data = (GWFDeployableList)all.get(i);
						int result = data.deploy(bpssProcSpec.getUId(), incomplete);
						finalResult = Utilities.combineResult(finalResult, result);
					}
				}
				Utilities.parseAgain(incomplete, bpssProcSpec.getUId());
				Utilities.parseMultiPartyCollaboration(incomplete,
				                                       bpssProcSpec.getUId());
			}
		}
		catch (Throwable th)
		{
			Logger.warn("[ProcessSpecification.deploy] Error, Unable to deploy " + bpssModel.getName()+", Uuid="+bpssModel.getUuid()+", Version="+bpssModel.getVersion() ,th); 
		}
		return finalResult;
  }
	
	/**
	 * This method is moved out from ProcessSpecification. Method signature has been changed
	 * to accept ProcessSpecification obj
	 * @param bpssModel
	 * @return
	 */
	public int undeploy(ProcessSpecification bpssModel)
		throws GWFException
	{
		Logger.log("[BpssModelHandler.undeploy] Undeploy ProcessSpecification....");
		int finalResult = 0;
    try
    {
      //Query for existing specs
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(null, IBpssProcessSpecification.UUID,
                             filter.getEqualOperator(), bpssModel.getUuid(), false);
      filter.addSingleFilter(filter.getAndConnector(),
                             IBpssProcessSpecification.VERSION,
                             filter.getEqualOperator(), bpssModel.getVersion(),
                             false);
      AbstractEntityHandler handler = EntityHandlerFactory.getHandlerFor(
                                          BpssProcessSpecification.ENTITY_NAME,
                                          true);
      Collection bpssProcSpecColl = handler.getEntityByFilter(filter);
      if (bpssProcSpecColl.isEmpty())
      {
        //new spec has not been deployed before, so cannot undeploy
        return bpssModel.GWF_NOTHING_PERFORMED;
      }
      else if (bpssProcSpecColl.size() > 1)
      {
        throw new GWFException("More than one bpssProcSpec exists");
      }
      else
      {
        Iterator it = bpssProcSpecColl.iterator();
        BpssProcessSpecification matchSpec = (BpssProcessSpecification)it.next();
        BpssDefinitionCache.removeBpssDefinitionCache(matchSpec.getSpecName(),matchSpec.getSpecVersion(),matchSpec.getSpecUUId());
        
        //TWX 03052008 notify the change on the BPSSProcessSpecification
        notifyBpssUndeploy(matchSpec);
        
        filter = new DataFilterImpl();
        filter.addSingleFilter(null, IBpssProcessSpecEntry.SPEC_UID,
                               filter.getEqualOperator(),
                               new Long(matchSpec.getUId()), false);
        AbstractEntityHandler entryHandler = EntityHandlerFactory.getHandlerFor(
                                                 BpssProcessSpecEntry.ENTITY_NAME,
                                                 true);
        Collection bpssProcSpecEntryColl = entryHandler.getEntityByFilter(
                                               filter);
        it = bpssProcSpecEntryColl.iterator();
        while (it.hasNext())
        {
          BpssProcessSpecEntry deployedSpecEntry = (BpssProcessSpecEntry)it.next();
          //never compare deployedSpecEntry with the one in the xml, just remove
          AbstractEntityHandler entityHandler = EntityHandlerFactory.getHandlerFor(deployedSpecEntry.getEntryType(),
                                                                                   true);
          
          //Mahesh 20041203 : leave entry for BpssBinaryCollaboration so that we can use this point to
          //        runtime processes of previous vierion definitions          
          if(!deployedSpecEntry.getEntryType().equals(BpssBinaryCollaboration.ENTITY_NAME))
          { 
            entityHandler.remove(new Long(deployedSpecEntry.getEntryUId()));
            entryHandler.remove(new Long(deployedSpecEntry.getUId()));         
          } 
          else
          {  
            Logger.debug("[ProcessSpecification.undeploy] Not deleting BpssProcessSpecEntry for BpssBinaryCollaboration, UId="+deployedSpecEntry.getUId()+", BpssBinaryCollaborationUId="+deployedSpecEntry.getEntryUId());
            try
            {
              entityHandler.remove(new Long(deployedSpecEntry.getEntryUId()));
            }
            catch(Throwable th)
            {
              Logger.debug("[ProcessSpecification.undeploy] Warning!  while deleting BpssBinaryCollaboration, Error: "+th.getMessage());
            }
          }
        }
        //Mahesh 20041203 : leave this record and update version to DELETED so that we can use this point to
        //        runtime processes of previous vierion definitions
        matchSpec.setSpecVersion("DELETED");
        handler.update(matchSpec);
      }
    }
    catch (Throwable th)
    {
      //TODO handle RemoteException, ParseException
      Logger.warn("[ProcessSpecification.undeploy] error, Unable to deploy " + bpssModel.getName()+", Uuid="+bpssModel.getUuid()+", Version="+bpssModel.getVersion(),th);
      throw new GWFException(th);
    }
    return finalResult;
	}
	
	/**
	 * Delete the bpss_process_spec record given the processSpec's uuid and version
	 * @param uuid
	 * @param version
	 * @return
	 * @throws ApplicationException
	 */
	public int undeploy(String uuid, String version)
		throws ApplicationException
	{
		try
		{
			Logger.log("[BpssModelHandler.undeploy] Undeployig process specification with uuid="+uuid+" version="+version);
			ProcessSpecification _bpssModel=new ProcessSpecification();
			_bpssModel.setUuid(uuid);
			_bpssModel.setVersion(version);
 
			return undeploy(_bpssModel);
		}
		catch(Exception ex)
		{
			throw new ApplicationException("[BpssModelHandler.undeploy] unable to undeploy process specification with uuid="+uuid+" and version="+version);
		}
    
	}
	
	/**
	 * Validate the processSpecFile.
	 * @param processSpecFile
	 * @param dtdFile
	 * @return
	 * @throws ApplicationException
	 */
	private boolean validateModel(File processSpecFile, File dtdFile)
		throws ApplicationException
	{
		try
		{
			return GNXMLDocumentUtility.validateDTD(processSpecFile.getAbsolutePath(), dtdFile.getAbsolutePath());
		}
		catch(Exception ex)
		{
			Logger.warn("Cannot validate processSpecFile "+ processSpecFile.getAbsolutePath());
			throw new ApplicationException("["+_classname+".validateModel] unable to validate processSpecFile.", ex);
		}
	}
	
  private void notifyBpssUndeploy(BpssProcessSpecification processSpec)
  {
    try
    {
      ProcessDefChangeNotification notification = new ProcessDefChangeNotification(processSpec);
      Notifier.getInstance().broadcast(notification);
    }
    catch(Exception ex)
    {
      Logger.error(ILogErrorCodes.DEPLOY_MODEL_DEPLOY, "Error in notifying the ProcessDefChangeNotification", ex);
    }
  }
  
	/**
	 * Retrieve the Bpss DTD file.
	 * @return
	 * @throws ApplicationException
	 */
	private File getDTDFile()
		throws ApplicationException
	{
		try
		{
			File dtd = FileUtil.getFile(IConstants.PATH_WORKFLOW_DTD, _defaultSystemId);
			if(dtd ==null)
			{
				Exception e = new Exception("DTD file with filename "+ _defaultSystemId+" cannot be found.");
				throw new ApplicationException(e);
			}
			return dtd;
		}
		catch(Exception ex)
		{
			Logger.warn("Cannot get DTD file.", ex);
			throw new ApplicationException("[BpssModelHandler] Unable to get DTD file with filename "+ _defaultSystemId, ex);
		}
	}
	
	/**
	 * Get the root element from the Bpss file or get from an inputStream.
	 * @param processSpec
	 * @return
	 * @throws ApplicationException
	 */
	private GNElement getRootElement(Object processSpec)
		throws ApplicationException
	{
		try
		{
			if(processSpec == null)
			{
				return null;
			}
			
			if(processSpec instanceof File)
			{
				return GNXMLDocumentUtility.getRoot((File)processSpec);
			}
			else if(processSpec instanceof InputStream)
			{
				return GNXMLDocumentUtility.getRoot((InputStream)processSpec);
			}
			return null;
		}
		catch(Exception ex)
		{
			Logger.warn("Cannot get root element.", ex);
			throw new ApplicationException("[BpssModelHandler.getRootElement] Unable to get root element.", ex);
		}
	}
	
	/*
	public static void main(String[] args)
		throws Exception
	{
		FileInputStream input  = new FileInputStream(new File("C:/3C321204.bpss"));
		FileOutputStream output = new FileOutputStream(new File("c:/bpss.xml"));
		
		
		BpssModelHandler bpss = new BpssModelHandler();
		
		System.out.println("***********Testing for loadModel(String), saveModel(OutputStream)");
		ProcessSpecification ps = bpss.loadModel("C:/3C321204.bpss");
		bpss.saveModel(output, ps);
		bpss.loadModel(new FileInputStream(new File("c:/bpss.xml")));
		
		System.out.println("***********Testing for loadModel(File), saveModel(String)");
		ps = bpss.loadModel(new File("C:/3C321204.bpss"));
		bpss.saveModel("c:/bpss1.xml", ps);
		bpss.loadModel("c:/bpss1.xml");
		
		System.out.println("***********Testing for loadModel(InputStream), saveModel(String)");
		ps = bpss.loadModel(input);
		bpss.saveModel(new File("c:/bpss2.xml"),ps);
		bpss.loadModel("c:/bpss2.xml");
		
		input  = new FileInputStream(new File("C:/3C321204.bpss"));
		ps = bpss.loadModel(input);
		ByteArrayOutputStream outByte = new ByteArrayOutputStream();
		bpss.saveModel(outByte, ps);
		System.out.println(outByte.toString());
	}
	*/
}
