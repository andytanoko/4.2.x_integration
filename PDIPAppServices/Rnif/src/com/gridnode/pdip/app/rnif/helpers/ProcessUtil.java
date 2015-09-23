package com.gridnode.pdip.app.rnif.helpers;

import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerHome;
import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerObj;
import com.gridnode.pdip.app.mapper.model.MappingFile;

import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class ProcessUtil
{

  public static IMappingManagerObj getMappingManager() throws ServiceLookupException
  {
    return (IMappingManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
      IMappingManagerHome.class.getName(),
      IMappingManagerHome.class,
      new Object[0]);
  }

  public static String getMappingFileName(Long mappingFileUid) throws Exception
  {
    if (mappingFileUid == null)
      return null;
    MappingFile mappingFile= getMappingManager().findMappingFile(mappingFileUid);

    String res= null;
    String subDir = null; //040309NSL 
    if (mappingFile != null)
    {
      res= mappingFile.getFilename();
      subDir = mappingFile.getSubPath(); //040309NSL 
      if (res != null)
      {
        res= res.trim();
        subDir = (subDir == null? "" : subDir.trim()); //040309NSL 
        if (res.length() > 0)
          return subDir.concat(res);  //040309NSL 
      }
    }
    return null;
  }

}