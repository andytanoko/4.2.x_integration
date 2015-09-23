package com.gridnode.pdip.app.workflow.adaptors;

import java.util.*;
import java.io.*;
import com.gridnode.pdip.app.workflow.impl.bpss.helpers.*;
/**
 * This is only for testing
 */
public class SendDocument {
    public Object sendRequestDocument(String documentId,String documentType,Object documentObject,Integer retryCount,String partnerKey){
        try{
            try{
                File f=new File("d:/testActions.tmp");
                if(f.exists()){
                    Properties prop=new Properties();
                    prop.load(new FileInputStream(f));
                    if(prop!=null && prop.getProperty(IBpssConstants.EXCEPTION_TIMETO_ACK)!=null){
                        String ex="Unable to send Request Document, documentType="+documentType+",documentId="+documentId ;
                        System.out.println(ex);
                        return ex;
                    }
                }
            }catch(Throwable th){
                th.printStackTrace();
            }
            System.out.println("SENDING REQUEST DOCUMENT, documentId="+documentId);
            PrintWriter pw=new PrintWriter(new FileOutputStream("d:/testReqDocument.tmp"));
            pw.println("documentId="+documentId);
            pw.println("documentType="+documentType);
            pw.println("documentObject="+documentObject);
            pw.println("retryCount="+retryCount);
            pw.println("partnerKey="+partnerKey);
            pw.close();
            return null;
        }catch(Throwable th){
            th.printStackTrace();
            return th;
        }
    }

    public String sendResponseDocument(String documentId,String documentType,Object documentObject,Integer retryCount,String partnerKey){
        try{
            System.out.println("SENDING RESPONSE DOCUMENT, documentId="+documentId);
            PrintWriter pw=new PrintWriter(new FileOutputStream("d:/testResDocument.tmp"));
            pw.println("documentId="+documentId);
            pw.println("documentType="+documentType);
            pw.println("documentObject="+documentObject);
            pw.println("retryCount="+retryCount);
            pw.println("partnerKey="+partnerKey);
            pw.close();
            return null;
        }catch(Throwable th){
            th.printStackTrace();
        }
        System.out.println("[SendDocument.sendResponseDocument] Failed to send");
        return "Unable to send Response Document, documentType="+documentType+",documentId="+documentId ;
    }

    public void sendSendSignal(String documentId,String signalType,Object reason,String documentType,Object documentObject,String partnerKey){
        try{
            System.out.println("SENDING SIGNAL , documentId="+documentId+",signalType="+signalType+",partnerKey="+partnerKey);
            PrintWriter pw=new PrintWriter(new FileOutputStream("d:/"+signalType+".tmp"));
            pw.println("documentId="+documentId);
            pw.println("signalType="+signalType);
            pw.println("documentType="+documentType);
            pw.println("reason="+reason);
            pw.println("documentObject="+documentObject);
            pw.println("partnerKey="+partnerKey);
            pw.close();
        }catch(Throwable th){
            th.printStackTrace();
        }
    }


    public String validateDocument(String documentId,String documentType,Object documentObject,String specLocation,String specElement){
        try{
            Properties prop=new Properties();
            prop.load(new FileInputStream("d:/testValidateDoc.tmp"));
            if(prop.getProperty(documentType)!=null)
                return "Invalid Document, documentObject="+documentObject;
        }catch(Exception ex){
        }
        return null;
    }

    public void notifyReceivedDocument(String documentId,String documentType,Object documentObject,Boolean isRetry,Boolean isValid){
        try{
            PrintWriter pw=new PrintWriter(new FileOutputStream("d:/testReceivedDoc.tmp"));
            pw.println("documentId="+documentId);
            pw.println("documentType="+documentType);
            pw.println("documentObject="+documentObject);
            pw.println("isRetry="+isRetry);
            pw.println("isValid="+isValid);
            pw.close();
        }catch(Throwable th){
            th.printStackTrace();
        }
    }

    public void notifyReceivedSignal(String signalType,Object reason,String documentId,String senderKey){
        try{
            PrintWriter pw=new PrintWriter(new FileOutputStream("d:/testNotifyReceivedSignal.tmp"));
            pw.println("signalType="+signalType);
            pw.println("reason="+reason);
            pw.println("documentId="+documentId);
            pw.println("senderKey="+senderKey);
            pw.close();
        }catch(Throwable th){
            th.printStackTrace();
        }
    }

}