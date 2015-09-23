/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntitySerializeVisitor.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 16 2003    Koh Han Sing        Created
 * May 17 2004    Neo Sok Lay         Handle subpaths in filenames
 * Oct 27 2005    Neo Sok Lay         Load mapping from SystemUtil.workingDir
 */
package com.gridnode.pdip.base.exportconfig.helpers;

import com.gridnode.pdip.base.exportconfig.exception.ExportConfigException;
import com.gridnode.pdip.base.exportconfig.model.ExportEntityList;
import com.gridnode.pdip.framework.db.ObjectXmlSerializer;
import com.gridnode.pdip.framework.db.entity.AbstractEntityVisitor;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;
import com.gridnode.pdip.framework.util.SystemUtil;

import java.io.File;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import org.exolab.castor.mapping.Mapping;

/**
 * This class will serialize a collection of entities into a XML file.
 *
 * @author Koh Han Sing
 *
 * @version GT 2.3.3
 * @since 2.1 I1
 */
public class EntitySerializeVisitor extends AbstractEntityVisitor
{
  private String _tempDir;

  public EntitySerializeVisitor() throws Exception
  {
    _tempDir = getNewTempDirName();
  }

  private static synchronized String getNewTempDirName()
  {
    return String.valueOf(System.currentTimeMillis());
  }

  public void visit(IEntity entity) throws Exception
  {
    Logger.debug("[EntitySerializeVisitor.visit] Start");
    Mapping mapping = getMapping(entity.getEntityName());

    String outputFilename = entity.getEntityName()+"-"+entity.getKey()+".xml";
    File serializedFile = FileUtil.createNewLocalFile(IPathConfig.PATH_TEMP,
                                                      _tempDir+"/",
                                                      outputFilename);
    ObjectXmlSerializer serializer = new ObjectXmlSerializer();
    serializer.serialize(entity, serializedFile.getAbsolutePath(), mapping);

    retrieveFilesUsedByEntity(entity);

    Logger.debug("[EntitySerializeVisitor.visit] End");
  }

  public void visit(Collection entities) throws Exception
  {
    Logger.debug("[EntitySerializeVisitor.visit] Start");
    IEntity entity = (IEntity)entities.iterator().next();

    Mapping mapping = getMapping(entity.getEntityName());

    ExportEntityList list = new ExportEntityList(entities);
    String outputFilename = entity.getEntityName()+".xml";
    File serializedFile = FileUtil.createNewLocalFile(IPathConfig.PATH_TEMP,
                                                      _tempDir+"/",
                                                      outputFilename);
    ObjectXmlSerializer serializer = new ObjectXmlSerializer();
    serializer.serialize(list, serializedFile.getAbsolutePath(), mapping);

    for (Iterator i = entities.iterator(); i.hasNext(); )
    {
      retrieveFilesUsedByEntity((IEntity)i.next());
    }

    Logger.debug("[EntitySerializeVisitor.visit] End");
  }

  /**
   * Zip up what is in the temp directory.
   *
   * @returns the File object of the zip file
   */
  public File zip() throws Exception
  {
    File zipFile = FileUtil.createNewLocalFile(IPathConfig.PATH_TEMP, "", _tempDir+".zip");
    File tempDir = FileUtil.getFile(IPathConfig.PATH_TEMP, _tempDir);
    ZipHelper.zip(tempDir, zipFile);
    try
    {
      FileUtil.deleteFolder(IPathConfig.PATH_TEMP, _tempDir);
    }
    catch (Exception ex)
    {
      Logger.debug("[EntitySerializeVisitor.zip] Unable to delete temp dir "+
                    _tempDir);
    }
    return zipFile;
  }

  public static void main(String[] shit)
  {
    try
    {
      EntitySerializeVisitor test = new EntitySerializeVisitor();
      test.zip();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
    System.exit(0);
  }

  public String getTempDirName()
  {
    return _tempDir;
  }

  private Mapping getMapping(String entityName) throws Exception
  {
    String mappingFile =
      ExportConfigLoader.getInstance().getMapping(entityName);
    if (mappingFile == null)
    {
      throw new ExportConfigException("Uable to load mapping for entity "+entityName);
    }

    File mapFile = new File(SystemUtil.getWorkingDirPath(), mappingFile);
    Logger.debug("[EntitySerializeVisitor.getMapping] mapFile URL ="+mapFile.toURL());
    if (!mapFile.exists())
    {
      throw new ExportConfigException("map file "+mappingFile+
        " for entity "+entityName+" does not exists");
    }

    Mapping mapping = new Mapping();
    mapping.loadMapping(mapFile.toURL());

    return mapping;
  }

  private void retrieveFilesUsedByEntity(IEntity entity) throws Exception
  {
    Logger.debug("[EntitySerializeVisitor.retrieveFilesUsedByEntity] Start");
    Hashtable fileTable = EntityRelationshipHelper.retrieveFilesUsedByEntity(entity);
    Enumeration eum = fileTable.keys();
    while (eum.hasMoreElements())
    {
      String pathKey = eum.nextElement().toString();
      String pathOfFile = FileUtil.getPath(pathKey);
      Object filenameObj = fileTable.get(pathKey);
      if (filenameObj != null)
      {
        String destFnPrefix = _tempDir+IMetaInfoConstants.FSLASH+pathOfFile;
        Collection filesnames = (Collection)filenameObj;
        for (Iterator i = filesnames.iterator(); i.hasNext(); )
        {
          String filename = i.next().toString();
          FileUtil.copy(pathKey, filename, IPathConfig.PATH_TEMP, destFnPrefix+filename);
          //FileUtil.copy(pathKey, "", filename,
          //              IPathConfig.PATH_TEMP, _tempDir+"/"+pathOfFile, filename);
        }
      }
    }
    Logger.debug("[EntitySerializeVisitor.retrieveFilesUsedByEntity] End");
  }

}