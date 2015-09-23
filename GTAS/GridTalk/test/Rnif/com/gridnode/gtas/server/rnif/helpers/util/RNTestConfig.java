package com.gridnode.gtas.server.rnif.helpers.util;

import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;



public class RNTestConfig extends GNConfigFile
{
  public static String command_GNRN = "GNRN_CMD";
  public static String command_GNRN_GET_PARTNER = "GNRN_GET_PARTER";
  public static String command_ViewStatus = "GNRN_ViewStatus";
  public static String command_ViewLastStatus = "GNRN_ViewLastStatus";
  public static String command_ViewActiveProcess = "GNRN_ViewActiveStatus";
  public static String general_partner = "General";

  public RNTestConfig()
  {
  }

  public RNTestConfig(Hashtable property)
  {
    super.setProperty(property);
  }

  public RNTestConfig(String filename)
  {
    super.readConfigFile(filename);
  }
  public String toString()
  {
    Enumeration en = getProperties().keys();
    String content = "";
    while(en.hasMoreElements())
    {
      String key = (String)en.nextElement();
      Object value = getProperties().get(key);
      if(value instanceof String)
        content += key + ": " + value + "\r\n";
      else if(key.equals("filecontent"))
      {
       if(value != null)
        content += "File: File loaded\r\n";
      else
        content += "File: File not loaded\r\n";
      }
      else  if(key.equals("attachmentcontents"))
      {
       if(value != null)
        content += "File: attachments loaded\r\n";
      else
        content += "File: attachments not loaded\r\n";
      }
      else if(value instanceof String[])
      {
         String[] values = (String[])value;
         for(int i = 0; i < values.length;i++)
          content += key + "[" + i + "]: " + values[i] + "\r\n";
      }

    }
    return content;
  }



  public void setCaseno(String caseno)
  {
    setProperty("caseno", caseno);
  }

  public void setFileContent(byte[] content)
  {
    setProperty("filecontent",content);
  }

  public byte[] getFileContent()
  {
    Object ob = getProperties().get("filecontent");
    if(ob == null)
     return null;
    else
    return (byte[])ob;
  }

  public String getCaseno()
  {
    return getProperty("caseno");
  }
  public void setVersion(String version)
  {
    setProperty("version", version);
  }
  public String getVersion()
  {
        return getProperty("version");
  }
  public void setRole(String role)
  {
        setProperty("role", role);
  }

  public String getRole()
  {
        return getProperty("role");
  }

  public void setSignature(boolean signature)
  {
        setBooleanProperty("signature", signature);
  }

  public boolean getSignature()
  {
        return getBooleanProperty("signature");
  }
  public void setSignature_alg(String signature_alg)
  {
        setProperty("signature_alg", signature_alg);
  }
  public String getSignature_alg()
  {
        return getProperty("signature_alg");
  }

  public void setEncrypt(boolean encrypt)
  {
        setBooleanProperty("encrypt", encrypt);
  }

  public boolean getEncrypt()
  {
        return getBooleanProperty("encrypt");
  }

  public void setNoRepudation(boolean NoRepudation)
  {
        setBooleanProperty("NoRepudation", NoRepudation);
  }

  public boolean getNoRepudation()
  {
        return getBooleanProperty("NoRepudation");
  }

  public void setEncrypt_alg(String encrypt_alg)
  {
        setProperty("encrypt_alg", encrypt_alg);
  }

  public String getEncrypt_alg()
  {
        return getProperty("encrypt_alg");
  }

  public String getGNID()
  {
        return getProperty("GNID");
  }

  public void setGNID(String GNID)
  {
        setProperty("GNID", GNID);
  }

  public String getCmd()
  {
        return getProperty("Cmd");
  }

  public void setCmd(String Cmd)
  {
        setProperty("Cmd", Cmd);
  }

  public String getGNPassword()
  {
        return getProperty("GNPassword");
  }

  public void setGNPassword(String GNPassword)
  {
        setProperty("GNPassword", GNPassword);
  }

  public String getPartnerURL()
  {
        return getProperty("PartnerURL");
  }

  public void setPartnerURL(String PartnerURL)
  {
        setProperty("PartnerURL", PartnerURL);
  }

  public String getSelfURL()
  {
        return getProperty("SelfURL");
  }

  public void setSelfURL(String SelfURL)
  {
        setProperty("SelfURL", SelfURL);
  }

  public String getURLType()
  {
        return getProperty("URLType");
  }

  public void setURLType(String URLType)
  {
        setProperty("URLType", URLType);
  }

  public void setEncrypt_len(int encrypt_len)
  {
        setIntProperty("encrypt_len", encrypt_len);
  }

  public int getEncrypt_len()
  {
        return getIntProperty("encrypt_len");
  }

  public void setSend(boolean send)
  {
        setBooleanProperty("send", send);
  }

  public boolean getSend()
  {
       return getBooleanProperty("send");
  }

  public void setReply(boolean reply)
  {
        setBooleanProperty("reply", reply);
  }

  public boolean getReply()
  {
        return getBooleanProperty("reply");
  }

  public void setReset(boolean Reset)
  {
        setBooleanProperty("Reset", Reset);
  }

  public boolean getReset()
  {
        return getBooleanProperty("Reset");
  }

  public void setPayload(boolean payload)
  {
        setBooleanProperty("payload", payload);
  }

  public boolean getPayload()
  {
        return getBooleanProperty("payload");
  }

  public void setAttachment(boolean attachment)
  {
        setBooleanProperty("attachment", attachment);
        if(!attachment)
        {
          removeProperty("attachmentnames");
          removeProperty("attachmentcontents");
        }
  }

  public String getAttachmentDes()
  {
    return getProperty("attachmentdes");
  }

  public void getAttachmentDes(String attde)
  {
    setProperty("attachmentdes", attde);
  }



  public boolean getAttachment()
  {
    return getBooleanProperty("attachment");
  }

  public String[] getAttachmentNames()
  {
      Object attachmentnames = getProperties().get("attachmentnames");
      if(attachmentnames == null)
       return null;
      if(attachmentnames instanceof String)
      {
        setAttachmentNames((String)attachmentnames);
        attachmentnames = getProperties().get("attachmentnames");
      }
      if(attachmentnames != null && attachmentnames instanceof String[])
        return (String[])attachmentnames;
      else
       return null;
  }

  public void setAttachmentNames(String names)
  {
      Vector namearr = new Vector();
      for(StringTokenizer fileST = new StringTokenizer(names, ";"); fileST.hasMoreTokens();)
        {
            String fileToSent = fileST.nextToken();
            namearr.add(fileToSent);
        }
      String[] attname = new String[namearr.size()];
      for(int i = 0; i < namearr.size();i++)
      {
        attname[i] = (String)namearr.get(i);
      }
      setProperty("attachmentnames", attname);
  }

  public boolean loadAttachments()
  {
      if(getSend() && getAttachment() && getAttachmentNames() != null && getAttachmentNames().length > 0)
      {
        String[] names = getAttachmentNames();
        byte[][] atts = new byte[names.length][];
        for(int i = 0; i < names.length;i++)
        {
          try
          {
            FileInputStream in = new FileInputStream(getWorkDirectory() + names[i]);
            byte[] contend = new byte[in.available()];
            in.read(contend);
            atts[i] = contend;
            in.close();
          }
          catch (Exception ex)
          {
              ex.printStackTrace();
          }
        }
        setProperty("attachmentcontents", atts);
        return true;
      }
      else
      return false;
  }


  public byte[][] getAttachmentContents()
  {
      Object attachments =  getProperties().get("attachmentcontents");
      if(attachments != null)
       return (byte[][])attachments;
      else
       return null;
  }

  public void setPip(String pip)
  {
   setProperty("pip", pip);
  }

  public String getPip()
  {
    return getProperty("pip");
  }

  public void setPartnerID(String partnerID)
  {
    setProperty("parterID", partnerID);
  }

  public String getPartnerID()
  {
    String p = getProperty("partnerID");
    if(p.length() == 0)
     return getProperty("parterID");
    else
      return p;
  }

  public void setDocType(String docType)
  {
    setProperty("docType", docType);
  }

  public String getDocType()
  {
    return getProperty("docType");
  }

  public void setFilename(String filename)
  {
    setProperty("filename", filename);
  }

  public void setWorkDirectory(String workDirectory)
  {
    setProperty("workdirectory", workDirectory);
  }

  public String getWorkDirectory()
  {
    return getProperty("workdirectory");
  }

  public boolean loadFile()
  {
    try
    {
      if(getSend() && getFilename().length() > 0)
      {
        FileInputStream in = new FileInputStream(getWorkDirectory() + getFilename());
        byte[] contend = new byte[in.available()];
        in.read(contend);
        setFileContent(contend);
        return true;
      }
      else
       return false;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      return false;
    }
  }

  public String getFilename()
  {
    return getProperty("filename");
  }

    public static void main(String [] args)
      throws Exception
  {
    RNTestConfig  config = new RNTestConfig();
    config.setPip("3A4");
    config.setReply(false);
    config.setVersion("RNIF2.0");
    config.setPartnerID("ORACLE");
    config.setDocType("3A4");
    config.setFilename("3A4.xml");
    config.setReset(true);
    config.setAttachment(true);
    config.setNoRepudation(false);

    config.setCaseno("L1");
    config.setRole("Responder");
//    config.setRole("Initiator");
    config.setSend(true);

    config.setEncrypt(false);
    config.setEncrypt_alg("3DES");
    config.setEncrypt_len(168);
    config.setPayload(false);

    config.setSignature(true);
    config.setSignature_alg("SHA1");
    config.setWorkDirectory("D:\\gridnode\\backend\\sender\\");

    config.setAttachmentNames("att.java;senderlite.txt;moon3.key");
    config.loadFile();
    config.loadAttachments();

    System.out.println(config);

//    config.writeConfigFile("W:\\gridnode\\backend\\sender\\L1.properties");
  }


}