/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPackagingHandler
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 16-OCT-2002    Jagadeesh           Created.
 *
 * 04-DEC-2002    Jagadeesh           Modified - To extend from AbstractPackaignHandler.
 * 
 * 06-OCT-2003		Jagadeesh						Modified - Added Header for Package Type.
 *
 */

package com.gridnode.pdip.base.packaging.handler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.gridnode.pdip.base.packaging.exception.PackagingServiceException;
import com.gridnode.pdip.base.packaging.exceptions.ILogErrorCodes;
import com.gridnode.pdip.base.packaging.helper.IPackagingConfig;
import com.gridnode.pdip.base.packaging.helper.PackagingInfo;
import com.gridnode.pdip.base.packaging.helper.PackagingLogger;
import com.gridnode.pdip.base.transport.helpers.ITransportConstants;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;

public class DefaultPackagingHandler extends AbstractPackagingHandler
{
	private static final String CLASS_NAME = "DefaultPackagingHandler";
	private static final String TEMP_PATH = System.getProperty("java.io.tmpdir");
	//private static final FileAccess fileAccess = new FileAccess();

	public DefaultPackagingHandler()
	{
	}

	public PackagingInfo packageAndEnvelope(PackagingInfo info)
		throws PackagingServiceException
	{
		final String METHOD_NAME = "packageAndEnvelope()";
		try
		{
			//Format - Arrange the Header in Proper Order..
			/*
			 * @todo This part has to be refactored, to give a proper PackageType,
			 * (Ex: we can provide a DEFAULT_PACKAGE_TYPE, since there is a direct 
			 * mapping between PackagingType selected by User, or perhaps, a mapping 
			 * defined in properties file. )
			 *  
			 * */

			Hashtable header = getDefaultPackagedHeader(info);
			String packagingType =
				getPackagingConfiguration().getString(
					info.getEnvelopeType());
			header.put(ITransportConstants.PACKAGE_TYPE_KEY, packagingType);

			info.setEnvelopeHeader(header);
			if (info.getPayLoadToPackage() != null)
			{
				if (info.isZip() == true)
					//This special Check is required as SimpleSend Function for GT1.3 need this.
				{ //As per Kan Mun... and hence this check.
					PackagingLogger.debugLog(CLASS_NAME, METHOD_NAME, "ZipBlock Start");
					String fileName = info.getFileId(); //Get's the FileId to Zip
					String tempPath = TEMP_PATH + fileName;
					PackagingLogger.infoLog(
						CLASS_NAME,
						METHOD_NAME,
						"Folder To Create " + tempPath); 
					//boolean create = 
					createTempFolder(TEMP_PATH + fileName);
					PackagingLogger.debugLog(CLASS_NAME, METHOD_NAME, "In ZipBlock");
					createZipFile(
						tempPath + File.separator + fileName,
						info.getPayLoadToPackage());
					//Zip the File Only
					PackagingLogger.debugLog(CLASS_NAME, METHOD_NAME, "After ZipBlock");
					File tfile = new File(tempPath + File.separator + fileName);
					PackagingLogger.debugLog(
						CLASS_NAME,
						METHOD_NAME,
						"After Creating tempFile");
					File[] fileContent = new File[] { tfile };
					PackagingLogger.debugLog(CLASS_NAME, METHOD_NAME, "B4 Setting File");
					info.setPackagedPayLoad(fileContent);
					PackagingLogger.debugLog(
						CLASS_NAME,
						METHOD_NAME,
						"After Setting File");
				} //Since No Splitting is done with this function, hence there is no conditional check here.
				else
				{
					PackagingLogger.debugLog(CLASS_NAME, METHOD_NAME, "In Else Block");
					//Return without zipping. Just Send The First file, to be sync to unpackage
					//           File[] payload = info.getPayLoadToPackage();
					//           File[] fileContent = {payload[0]};
					//           info.setPackagedPayLoad(fileContent);
					info.setPackagedPayLoad(info.getPayLoadToPackage());
				}
			} //else return formatted header.
			return info;
		} catch (Exception ex)
		{
			PackagingLogger.warnLog(
				CLASS_NAME,
				METHOD_NAME,
				"Exception in formating&Packaging");
			throw new PackagingServiceException(
				"Exception in formating&Packaging",
				ex);
		}
	}

	public PackagingInfo unPackage(PackagingInfo info)
		throws PackagingServiceException
	{
		try
		{
			info.setDefaultUnPackagedHeader(getDefaultUnPackagedHeader(info));
            /**
             * @todo to be removed when Jagadeesh has implemented the actual one
             * 031105NSL: temporary hack to pass all headers back in additionalHeader
             */
            info.setAdditionalHeader(info.getUnPackagedHeader());
            
			if (info.getPayLoadToUnPackage() != null)
			{
				if (info.isZip() == true)
				{
					byte[] payLoad = info.getPayLoadToUnPackage();
					//this is decrypted data to unpackage.
					String fileId = info.getFileId();
					PackagingLogger.infoLog(
						CLASS_NAME,
						"unPackage()",
						"fileId " + fileId);
					File[] unpackageFiles = unZipFile(payLoad, fileId);
					info.setUnPackagedPayLoad(unpackageFiles);
				} else
				{
					PackagingLogger.infoLog(
						"DefaultPackagingHandler",
						"unPackage()",
						"Not UnZipping As Per Profile ");
					byte[] payLoad = info.getPayLoadToUnPackage();
					String fileId = info.getFileId();
					File[] unpackageFiles = new File[1];
					String tempFolder = TEMP_PATH + fileId;
					//boolean createFolder = 
					createTempFolder(tempFolder);
					String actualPath = new File(tempFolder, fileId).getCanonicalPath();
					FileOutputStream fos = null;
					try
					{
						fos = new FileOutputStream(actualPath);
						fos.write(payLoad);
						fos.flush();
					} catch (IOException e)
					{
						PackagingLogger.infoLog(
							"DefaultPackagingHandler",
							"unPackage()",
							"IOException while writing to File" + actualPath);
						throw new PackagingServiceException(
							"IOException while writing to File" + actualPath,
							e);
					} finally
					{
						if (fos != null)
							fos.close();
					}
					PackagingLogger.infoLog(
						"DefaultPackagingHandler",
						"unPackage()",
						"File Written With FileID " + actualPath);
					unpackageFiles[0] = new File(actualPath);
					info.setUnPackagedPayLoad(unpackageFiles);
				}
			}
			return info;
		} catch (Exception ex)
		{
			PackagingLogger.warnLog(
				"DefaultPackagingHandler",
				"unPackage()",
				"Exception in formating&Packaging");
			throw new PackagingServiceException(
				"Exception in formating&Packaging",
				ex);
		}
	}

	public static void createZipFile(String fileName, File[] files)
	{
		try
		{
			BufferedInputStream origin = null;
			FileOutputStream dest = new FileOutputStream(fileName);
			PackagingLogger.infoLog(
				CLASS_NAME,
				"creatZipFile",
				"ZipString File Name " + fileName);
			CheckedOutputStream checksum =
				new CheckedOutputStream(dest, new Adler32());
			ZipOutputStream out =
				new ZipOutputStream(new BufferedOutputStream(checksum));
			out.setMethod(ZipOutputStream.DEFLATED);
			out.setLevel(9);
			byte data[] = new byte[BUFFER];

			for (int i = 0; i < files.length; i++)
			{
				PackagingLogger.infoLog(
					CLASS_NAME,
					"createZipFile()",
					"Adding: " + files[i]);
				FileInputStream fi = new FileInputStream(files[i]);
				origin = new BufferedInputStream(fi, BUFFER);
				ZipEntry entry = new ZipEntry(files[i].getName());
				entry.setSize(files[i].length());
				entry.setTime(files[i].lastModified());

				PackagingLogger.infoLog(
					CLASS_NAME,
					"creatZipFile",
					"File Name of " + i + " is " + files[i].getName());
				out.putNextEntry(entry);
				int count;
				while ((count = origin.read(data, 0, BUFFER)) != -1)
				{
					out.write(data, 0, count);
				}
				origin.close();
			}
			out.close();
			PackagingLogger.infoLog(
				CLASS_NAME,
				"creatZipFile",
				"After Creating The Zip");
		} catch (Exception e)
		{
			e.printStackTrace();
			PackagingLogger.errorLog(
			  ILogErrorCodes.PACKAGING_ZIP_FILE_CREATE,
				CLASS_NAME,
				"createZipFile",
				"Error while creating zip file while packaging: "+e.getMessage(),
				e);
		}

	}

	public static File[] unZipFile(byte[] payLoad, String fileName)
	{
		Vector fileNames = new Vector();
		try
		{
			PackagingLogger.infoLog(
				"DefaultPackagingHandler",
				"unZipFile()",
				"In unZip Begin");
			String tempFolder = TEMP_PATH + fileName;
			//boolean createFolder = 
			createTempFolder(tempFolder);
			String actualPath = tempFolder + fileName;
			File unzipFile = getFileFromBytes(payLoad, actualPath);
			final int BUFFER = IPackagingHandler.BUFFER;
			BufferedOutputStream dest = null;
			FileInputStream fis = new FileInputStream(unzipFile);
			CheckedInputStream checksum = new CheckedInputStream(fis, new Adler32());
			ZipInputStream zis =
				new ZipInputStream(new BufferedInputStream(checksum));
			ZipEntry entry;

			while ((entry = zis.getNextEntry()) != null)
			{
				int count;
				byte data[] = new byte[BUFFER];
				fileNames.add(entry.getName());
				PackagingLogger.infoLog(
					"DefaultPackagingHandler",
					"unZipFile",
					"File Name is" + entry.getName());
				FileOutputStream fos = new FileOutputStream(entry.getName());
				dest = new BufferedOutputStream(fos, BUFFER);
				while ((count = zis.read(data, 0, BUFFER)) != -1)
				{
					dest.write(data, 0, count);
				}
				dest.flush();
				dest.close();
			}
			zis.close();
			File[] mpayLoad = new File[fileNames.size()];
			for (int i = 0; i < fileNames.size(); i++)
			{
				String actName = (String) fileNames.get(i);
				PackagingLogger.infoLog(
					CLASS_NAME,
					"unZipFile()",
					"Actual Files Added Are " + actName);
				mpayLoad[i] = new File(actName);
			}
			PackagingLogger.infoLog(
				"DefaultPackagingHandler",
				"unZipFile",
				"End Of UNZIP Success-->");
			return mpayLoad;
		} catch (Exception e)
		{
			PackagingLogger.warnLog(
				"DefaultPackagingHandler",
				"unZip()",
				"Cannot UnPackagePayLoad",
				e);
		}
		return null;
	}

	public static File getFileFromBytes(byte[] byteContent, String fileName)
		throws IOException
	{
		PackagingLogger.infoLog(
			CLASS_NAME,
			"getFileFromBytes()",
			"Begin of getting File");
		File f = new File(fileName);
		//    fileAccess.createFile(new )
		OutputStream ous = new FileOutputStream(f);
		ous.write(byteContent);
		ous.flush();
		PackagingLogger.infoLog(
			CLASS_NAME,
			"getFileFromBytes()",
			"End of getting File");
		return f;
	}

	private static boolean createTempFolder(String folderName)
	{
		return new File(folderName).mkdir();
		//    fileAccess.createFolder(TEMP_PATH+folderName);
	}

	private Configuration getPackagingConfiguration()
	{
		return ConfigurationManager.getInstance().getConfig(
			IPackagingConfig.CONFIG_NAME);
	}

}