package com.gridnode.gtas.server.rnif.act;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rnif.helpers.IRnifConstant;
import com.gridnode.gtas.server.rnif.helpers.RnifException;
import com.gridnode.pdip.app.rnif.model.ProcessAct;
import com.gridnode.pdip.app.rnif.model.ProcessDef;

import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;

public class SendExceptionAction extends SendSignalAction
{
  private String _exception;
  private String _exceptionCode;
  public SendExceptionAction(String exceptionCode, String exceptionStr)
  {
    super();
    
    _exceptionCode= exceptionCode;
    _exception= exceptionStr;
  }

  Document writeInfo(GridDocument originalGDoc, ProcessDef def, ProcessAct pAct)
    throws RnifException
  {
    DocType docType= new DocType("Exception", IRnifConstant.EXCEPTION_MESSAGE_TYPE_2);

    Element root= new Element("Exception");

    Element child1= new Element("ExceptionDescription");
    Element child2= new Element("errorClassification");
    Element child3= new Element("GlobalMessageExceptionCode");
    child3.addContent(_exceptionCode);
    child2.addContent(child3);
    child1.addContent(child2);

    child2= new Element("errorDescription");
    child3= new Element("FreeFormText");
    child3.addContent(_exception);
    child2.addContent(child3);
    child1.addContent(child2);

    child2= new Element("offendingMessageComponent");
    child3= new Element("GlobalMessageComponentCode");
    child3.addContent("ServiceContent");
    child2.addContent(child3);
    child1.addContent(child2);

    root.addContent(child1);

    child1= new Element("GlobalExceptionTypeCode");
    child1.addContent("General Exception");
    root.addContent(child1);

    Document doc= new Document(root, docType);
    return doc;
  }

  /*       writer.write( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
   writer.write( "<!DOCTYPE Exception SYSTEM \"" +  IRnifConstant.EXCEPTION_MESSAGE_TYPE + "\">");
   writer.write( "<Exception>");
  
   writer.write( "<ExceptionDescription>");
     writer.write( "<errorClassification>");
       writer.write( "<GlobalMessageExceptionCode>");
       writer.write("UNP.SCON.VALERR");
       writer.write( "</GlobalMessageExceptionCode>");
     writer.write( "</errorClassification>");
  
     writer.write("<errorDescription>");
       writer.write( "<FreeFormText>");
       writer.write(_exception.toString());
       writer.write( "</FreeFormText>");
     writer.write("</errorDescription>");
  
  
     writer.write("<offendingMessageComponent>");
       writer.write( "<GlobalMessageComponentCode/>");
     writer.write("</offendingMessageComponent>");
   writer.write( "</ExceptionDescription>");
  
   writer.write( "<GlobalExceptionTypeCode/>");
  writer.write( "</Exception>");
  writer.close();
  */

  String getBaseDocName(String originalFileName)
  {
    // set a unique filename
    return "Except_" + originalFileName;
  }

  String getActualClassName()
  {
    return "SendExceptionAction";
  }

  String getProcessMsgType()
  {
    return IRnifConstant.EXCEPTION_MESSAGE_TYPE_2;
  }

  String getUDocDocType()
  {
    return IRnifConstant.RN2_EXCEPTION;
  }

  String getGlobalSignalCode()
  {
    return "Exception";
  }

}