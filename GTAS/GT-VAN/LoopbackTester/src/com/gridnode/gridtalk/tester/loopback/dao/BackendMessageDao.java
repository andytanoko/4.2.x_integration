/**
 * 
 */
package com.gridnode.gridtalk.tester.loopback.dao;

import com.gridnode.gridtalk.tester.loopback.entity.BackendMessage;
import com.gridnode.gridtalk.tester.loopback.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Alain
 *
 */
public class BackendMessageDao {

    /**
     * 
     */
    public BackendMessageDao() {
    }
    
    public BackendMessage getBackendMessage(String pipCode) throws IOException
    {
        BackendMessage msg = new BackendMessage(pipCode);
        msg.setDocIdPath(getDocIdPath(pipCode));
//        msg.setPipCodePath(getPipCodePath());
        msg.setTemplate(getTemplate(pipCode));
        return msg;
    }
    
    private String getDocIdPath(String pipCode) throws IOException
    {
        File pathFile = FileUtil.getFile(FileUtil.TYPE_DATA, "Be_"+pipCode+".path");
        Properties p = new Properties();
        p.load(new FileInputStream(pathFile));
        return p.getProperty(BackendMessage.PATH_KEY_DOC_ID);        
    }
    
//    private String getPipCodePath() throws FileNotFoundException, IOException
//    {
//        File pathFile = FileUtil.getFile(FileUtil.TYPE_DATA, "Be_DEF.path");
//        Properties p = new Properties();
//        p.load(new FileInputStream(pathFile));
//        return p.getProperty(BackendMessage.PATH_KEY_PIP_CODE);                
//    }
    
    private String getTemplate(String pipCode) throws IOException
    {
        File templateFile = FileUtil.getFile(FileUtil.TYPE_DATA, "Be_"+pipCode+".template");
        return getContent(templateFile);
    }
    private  String getContent(File f) throws IOException
    {
      FileInputStream is = new FileInputStream(f);
      byte[] buff = new byte[1024];
      int readLen = -1;
      StringBuffer content = new StringBuffer();
      while ((readLen=is.read(buff))>0)
      {
        content.append(new String(buff, 0, readLen));
      }    
      return content.toString();
    }
}
