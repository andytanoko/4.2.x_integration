/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RNFileTransformer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 1, 2005    Tam Wei Xiang       Created
 * Oct 10,2006    Tam Wei Xiang       Merge from ESTORE stream. Modified method
 *                                    extractRNContent. If we can't perform decryption,
 *                                    the content in encryption format will be returned.
 */
package com.gridnode.gtas.server.dbarchive.helpers;

import com.gridnode.gtas.server.dbarchive.model.RNFileContainer;
import com.gridnode.pdip.base.rnif.exception.RosettaNetException;
import com.gridnode.gtas.server.dbarchive.helpers.Logger;
import com.gridnode.pdip.base.rnif.helper.SecurityInfoFinder;
import com.gridnode.pdip.base.security.mime.SMimeFactory;
import com.gridnode.pdip.base.security.exceptions.SecurityServiceException;
import com.gridnode.pdip.base.security.mime.IMime;
import com.gridnode.pdip.base.security.mime.IPart;
import com.gridnode.pdip.framework.exceptions.FileAccessException;
import com.gridnode.pdip.base.rnif.helper.XMLUtil;
import com.gridnode.pdip.framework.exceptions.ApplicationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * It transform the MIME or SMIME msg to a certain form so that the msg
 * can be rendered properly by the UI part.
 * 
 * Tam Wei Xiang
 * 
 * @version
 * @since
 */
public class RNFileExtracter
{
	private static RNFileExtracter _rnFileExt;
	private SecurityInfoFinder _infoFinder;
	
	//For RNIF 2.0
	private final String _CONTENT_LOCATION = "Content-Location";
	private final String _PREAMBLE_CONTENT_LOCATION = "RN-Preamble";
	private final String _DELIVERY_CONTENT_LOCATION = "RN-Delivery-Header";
	private final String _SERVICE_HEADER_CONTENT_LOCATION = "RN-Service-Header";
	private final String _SERVICE_CONTENT_CONTENT_LOCATION = "RN-Service-Content";
	
	//For RNIF1.1
	private final int RN_VERSION_NUMBER = 0x00010100;
	private final String PREAMBLE_HEADER = "preamble-header";
	private final String SERVICE_HEADER = "service-header";
	private final String SERVICE_CONTENT = "service-content";
	
	private final String EXTRACT_TYPE_ENCRYPTED_CONTENT = "content"; //we only extract out the entire sign part and service content
	//private final String EXTRACT_TYPE_PREAMBLE_DELIVERY = "preamDeliver"; //we only extract the preamble and delivery header from audit file
	
	//private final String CERT_TYPE_ENCRYPT = "encyptCert";
	//private final String CERT_TYPE_SIGN = "signCert";
	
	private RNFileExtracter()
	{
		
	}

	
	public static RNFileExtracter getInstance()
	{
		if(_rnFileExt == null)
		{
			_rnFileExt = new RNFileExtracter();
		}
		return _rnFileExt;
	}
	
	/**
	 * It will differentiate the RNIF version of the RN file and delegate to the appropriate method
	 * to extract out the rn content.
	 * @param rnFile
	 * @return RNFileContainer which contain the rn content eg preamble, service hearder, service content (in decrypted format
	 *         or encrypted format depending whether we can decrypt the rnFile)
	 * @throws Exception
	 */
	public RNFileContainer extractRNContent(File rnFile)
				 throws Exception
	{
		try
		{
			if (isRNIF2P0(rnFile) )
			{
				Object extractedContent = extractRNFile2P0Content(rnFile,"");
				if(extractedContent == null)//Just return the content in encrypted format
				{
					extractedContent = extractRNFile2P0Content(rnFile, EXTRACT_TYPE_ENCRYPTED_CONTENT);
				}
				return (RNFileContainer)extractedContent;
			}
			else
			{
				return extractRNFile1P0Content(rnFile);
			}
			
		}
		catch(FileNotFoundException ex)
		{
			throw new FileAccessException("[RNFileExtracter.extractRNContent] File "+rnFile.getAbsolutePath()+" is not exist.",ex);
		}
		catch(Exception ex)
		{
			Logger.warn("[RNFilExtracter.extractRNContent] Error occured while extracting out RN file content",ex);
			throw new ApplicationException(ex);
		}
	}
	
	/**
	 * Extract the content of rn file which version is RNIF 1.0
	 * @param rnFile The actual audit file
	 * @return RNFileContainer which contain the rn content eg preamble, service hearder, service content
	 * @throws Exception
	 */
	public RNFileContainer extractRNFile1P0Content(File rnFile)
				 throws IOException, RosettaNetException, SecurityServiceException
	{
		//get the actual content from the rnFile.
    File contentFile = getRNFileContent(rnFile);
		Logger.log("[RNFileExtracter.extractRNFile1P0Content]content file location "+contentFile.getAbsolutePath());
    
    SMimeFactory factory = SMimeFactory.newInstance(null, null);
    IMime envelope = factory.generateMime(contentFile);
    
    if (envelope.getPartCount() <= 0)
    {
      throw RosettaNetException.unpMesgGenErr("Cannot read RosettaNet Message");
    }
    
    //extract out RN-Preamble
    IPart part = envelope.getPart(0);
    
    String rnPreambleHeader = extract1P1RNPart(part,PREAMBLE_HEADER,rnFile.getName());
		
    //extract out RN-Service-Header
    part = envelope.getPart(1);
    String rnServiceHeader = extract1P1RNPart(part, SERVICE_HEADER,
    																				rnFile.getName());
    
		//extract out RN-Service-Content
    part = envelope.getPart(2);
    String rnServiceContent = extract1P1RNPart(part, SERVICE_CONTENT,
    																				 rnFile.getName());
		
    RNFileContainer container = new RNFileContainer();
    container.setPreamble(rnPreambleHeader);
    container.setServiceHeader(rnServiceHeader);
    container.setServiceContent(rnServiceContent);
    return container;
	}
	
	/**
	 * This method will return the actual content(include Preamble,Service Header, Service Content) 
	 * of the rnFile. It parse away the unnessary info eg rn version, signature
	 * It is used to parse RNIF 1.1 rn file.
	 * @param rnFile The actual audit file
	 * @return a File that store the actual content(eg without signature).
	 */
	private File getRNFileContent(File rnFile) 
					throws IOException, RosettaNetException
	{
		DataInputStream dis = new DataInputStream(new FileInputStream(rnFile));
		
		// Retrieve the rn version this rnFile belong
		int rnVersion = dis.readInt();
    if (rnVersion != RN_VERSION_NUMBER )
      throw RosettaNetException.unpMesgGenErr("Invalid RN Version: " + rnVersion);

    // Retrieve length of the content. After the length it is the actual content.
    // store the actual content into a byte array.
    int contentLength = dis.readInt();
    byte[] svcMesg = new byte[contentLength];
    if (dis.read(svcMesg) != contentLength)
      throw RosettaNetException.unpMesgGenErr("Error reading RNO content");
    
    // Retrieve the signature from this rnfile if any. After the length it is the actual
    // signature content.
    byte[] signature = null;
    if(dis.available() > 0)
    {
    	int signLength = dis.readInt();
    	if(signLength > 0)
    	{
    		signature = new byte[signLength];
    		if(dis.read(signature) != signLength )
    		{
    			throw RosettaNetException.unpMesgGenErr("Error reading digital signature");
    		}
    	}
    }
    
    dis.close();
    
    //write the service message into a temporary file
    File tmpFile= XMLUtil.createTempFile(rnFile.getName(), svcMesg);
    
    return tmpFile;
	}
	
	/**
	 * Extract the content of rn file which version is RNIF 2.0
	 * @param rnFile The actual audit file
	 * @param extractType Based on the extractType, we will extract differenct part of the rn file content.
	 * 										If invoker pass in empty string, then it will extract preamble,delivery,service header and
	 * 										service content.
	 * @return RNFileContainer which contain the rn content eg preamble, delively header, service hearder, service content.
	 *         return NULL if we have problem in decryption
	 * @throws Exception
	 */
	private Object extractRNFile2P0Content(File rnFile, String extractType) throws Exception
	{
		try
		{
			SMimeFactory factory = SMimeFactory.newInstance(null, null); //SecurityServiceException
			IMime signEnvelope = factory.generateMime(rnFile);
			IMime envelope = null;
			RNFileContainer contain = new RNFileContainer();

			
			if(signEnvelope.isSigned())
			{
				IPart part = signEnvelope.getPart(0);
				if(!part.isMultipart())
				{
					throw RosettaNetException.unpMesgSignErr(
          	"signEnvelope's part(0) must be a multipart mime!"); 
				}
				envelope = part.getMultipart();

			}
			else
			{
				envelope = signEnvelope;
			}
			
			if (envelope.getPartCount() <= 0)
	    {
	      throw RosettaNetException.unpMesgGenErr("Cannot read RosettaNet Message");
	    }
			
			//extract out RN-Preamble Header
			IPart part = envelope.getPart(0);
			String rnPreambleHeader = extractRNPart(part,_PREAMBLE_CONTENT_LOCATION,rnFile.getName());
			
			//extract out RN-Delivery-Header
			part = envelope.getPart(1);
			String rnDeliveryHeader = extractRNPart(part,_DELIVERY_CONTENT_LOCATION,rnFile.getName());
			
			//extract out RN-Service-Header
	    IPart sheader, scontent;
	    ArrayList attIPartList= new ArrayList();
	    int count= envelope.getPartCount();
	    if (count == 3)
	    {
	      // decrypt
	      part= envelope.getPart(2);
	      
	      if (part.isEncrypted())  //service header and service content is encrypted.
	      {
	      	//invoker want to extract out the service content in encrypted format (those in outbound we can't decrypt)
		      if(extractType.compareTo(EXTRACT_TYPE_ENCRYPTED_CONTENT)==0)
		      {
		      	contain.setServiceContent(part.getContentString());
		      	Logger.log("[RNFileExtracter.extractRNFile2P0Content] service content is "+ part.getContentString());
		      	contain.setPreamble(rnPreambleHeader);
		      	contain.setDeliveryHeader(rnDeliveryHeader);
		      	return contain;
		      }
	      	
	      	
	        IMime payloadcontainer = null;
	        try
	        {
	          payloadcontainer = (IMime) factory.decrypt(part);
	        }
	        catch(Exception e)
	        {
	          Logger.warn("[RNFileExtracter.extractRNFile2P0Content]Decrypt failed", e);
	          return null;
	          //throw RosettaNetException.unpMesgDcryptErr("[RNFileExtracter.extractRNFile2P0Content] Error decrypting: " + e.getMessage());
	        }
	        sheader= payloadcontainer.getPart(0);
	        scontent= payloadcontainer.getPart(1);
	        
	        //attachment pending
	        int iPartCount= payloadcontainer.getPartCount();
	        if (iPartCount > 2)
	        {
	          for (int i= 2; i < iPartCount; i++)
	          {
	            attIPartList.add(payloadcontainer.getPart(i));
	          }
	        }
	      }
	      else
	      {
	        sheader= null;
	        scontent= null;
	        throw RosettaNetException.unpMesgDcryptErr("[RNFileExtracter.extractRNFile2P0Content] Expected an encrypted message.");
	        // alternative is to proceed without encryption
	      }
	    }
	    else //must be 4 parts
      {
	    	sheader= envelope.getPart(2);
	    	scontent= envelope.getPart(3);

	    	//invoker want to extract out the service content which in encrypted format
	      if(extractType.compareTo(EXTRACT_TYPE_ENCRYPTED_CONTENT)==0)
	      {
	      	contain.setPreamble(rnPreambleHeader);
	      	contain.setDeliveryHeader(rnDeliveryHeader);
	      	contain.setServiceHeader(extractRNPart(sheader, _SERVICE_HEADER_CONTENT_LOCATION,
							                     rnFile.getName()));
	      	contain.setServiceContent(scontent.getContentString());
	      	Logger.log("[RNFileExtracter.extractRNFile2P0Content] service content is "+ scontent.toString());
	      	
	      	return contain;
	      }
	    	
	    	
	    	if (scontent.isEncrypted())
	    	{		
	    		
	    		IMime payloadcontainer = null;
	    		try
	    		{
	    			payloadcontainer = (IMime) factory.decrypt(scontent);
	    		}
	    		catch(Exception e)
	    		{
	    			Logger.warn("[RNFileExtracter.extractRNFile2P0Content]Decrypt failed", e);
	    			return null;
	          //throw RosettaNetException.unpMesgDcryptErr("[RNFileExtracter.extractRNFile2P0Content] Error decrypting: " + e.getMessage());
	    		}
	    		scontent= payloadcontainer.getPart(0);
        
	    		//pending
	    		int iPartCount= payloadcontainer.getPartCount();
	    		if (iPartCount > 1)
	    		{
	    			for (int i= 1; i < iPartCount; i++)
	    			{
	    				attIPartList.add(payloadcontainer.getPart(i));
	    			}
	    		}
	    	}
	    	else
	    	{
	    		//Logger.debug("Service content not encrypted. (4 parts)");
	    		//pending
	    		for (int i= 4; i < count; i++)
	    		{
	    			attIPartList.add(envelope.getPart(i));
	    		}
	    	}
      }
	    
	    //extract out RN-Service-Header
	    String rnServiceHeader = extractRNPart(sheader, _SERVICE_HEADER_CONTENT_LOCATION,
	    																				rnFile.getName());
	    
			//extract out RN-Service-Content
	    String rnServiceContent = extractRNPart(scontent, _SERVICE_CONTENT_CONTENT_LOCATION,
	    																				 rnFile.getName());
	    
	    //RNFileContainer contain = new RNFileContainer();
	    contain.setPreamble(rnPreambleHeader);
	    contain.setDeliveryHeader(rnDeliveryHeader);
	    contain.setServiceHeader(rnServiceHeader);
	    contain.setServiceContent(rnServiceContent);
	    
	    return contain;
		}
		catch(SecurityServiceException ex)
		{
			throw new ApplicationException(ex);
		}
		catch(RosettaNetException ex)
		{
			throw new ApplicationException(ex);
		}		
	}
	
	private String extractRNPart(IPart rnFilePart, String contentLocHeader,
																			 String filename)
					throws SecurityServiceException
	{
		try
		{
			
			if(!contentLocHeader.equals(rnFilePart.getHeader(_CONTENT_LOCATION)[0]))
			{
				Logger.log("[RNFileExtracter.extractRNPart] Invalid Content-location header for "+
						contentLocHeader+" of filename "+filename);
				
				return "Invalid Content-location header for "+contentLocHeader+" of filename "+filename;
			}
			String content = (String)rnFilePart.getContent(IPart.OUTPUT_STRING);
			//byte [] buffer = new byte[inputStream.available()]; //IOException
			//inputStream.read(buffer);
			return content;
		}
		catch(SecurityServiceException ex)
		{
			Logger.log("[RNFileExtracter.extractRNPart] Cannot read content "+contentLocHeader +" of file "+filename);
			throw new SecurityServiceException(ex);
		}
	}
	
	private String extract1P1RNPart(IPart part, String contentType, String filename)
					throws RosettaNetException, SecurityServiceException
	{
		String type = part.getContentType();
		if(type.indexOf(contentType) < 0)
		{
			throw RosettaNetException.unpPrmbReadErr("[RNFileExtracter.extract1P1RNPart]Invalid "+ contentType +" rnsubtype:" + type+" for file "+filename);
		}
		return (String)part.getContent();
	}
	
	/**
	 * For the audit file in outbound, we can't perform decryption on it. however we will bring over
	 * the udoc file as service content. The service header will be generated using rnPAckInfo.
	 * The preamble, delivery header will be generated using this method.
	 * @param rnFile
	 * @return
	 */
	public void addPreambleDeliveryHeader(RNFileContainer container,File rnFile)
		throws ApplicationException
	{
		try
		{
			SMimeFactory factory = SMimeFactory.newInstance(null, null); //SecurityServiceException
			IMime signEnvelope = factory.generateMime(rnFile);
			IMime envelope = null;
			
			if(signEnvelope.isSigned())
			{
				IPart part = signEnvelope.getPart(0);
				if(!part.isMultipart())
				{
					throw RosettaNetException.unpMesgSignErr(
          	"signEnvelope's part(0) must be a multipart mime!"); 
				}
				envelope = part.getMultipart();
				
			}
			else
			{
				envelope = signEnvelope;
			}
			
			if (envelope.getPartCount() <= 0)
	    {
	      throw RosettaNetException.unpMesgGenErr("Cannot read RosettaNet Message");
	    }
			
			//extract out RN-Preamble Header
			IPart part = envelope.getPart(0);
			String rnPreambleHeader = extractRNPart(part,_PREAMBLE_CONTENT_LOCATION,rnFile.getName());
			
			//extract out RN-Delivery-Header
			part = envelope.getPart(1);
			String rnDeliveryHeader = extractRNPart(part,_DELIVERY_CONTENT_LOCATION,rnFile.getName());
			
			container.setPreamble(rnPreambleHeader);
			container.setDeliveryHeader(rnDeliveryHeader);
			
		}
		
		catch(Exception ex)
		{
			throw new ApplicationException(ex);
		}
	}
	
	
	private boolean isRNIF2P0(File rnFile)
		throws Exception
	{
		DataInputStream dis = new DataInputStream(new FileInputStream(rnFile));
		int rnVersion = dis.readInt();
		
		if (rnVersion != RN_VERSION_NUMBER )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
