package com.gridnode.pdip.app.deploy.manager.util;

import java.io.*;

import com.gridnode.pdip.app.deploy.manager.*;
import com.gridnode.pdip.framework.file.helpers.*;

import com.gridnode.pdip.framework.exceptions.FileAccessException;
import org.xml.sax.*;

public class XmlEntityResolver implements EntityResolver {
    String dtdFileName;
    File dtdFile;

    public XmlEntityResolver(String dtdFileName) throws FileAccessException {
        this.dtdFileName=dtdFileName;
        dtdFile=FileUtil.getFile(IConstants.PATH_WORKFLOW_DTD, dtdFileName);
        System.out.println("Constructor DTD File "+dtdFile);
    }

    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        try{
            System.out.println("DTD File "+dtdFile);
            InputStream in=new FileInputStream(dtdFile);
            System.out.println("After InputStream");
            return new InputSource(in);
        }catch(Exception e){
            e.printStackTrace();
            throw new SAXException("Failed to resolve dtd, dtdFileName="+dtdFileName,e);
        }
    }
}