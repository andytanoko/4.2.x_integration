package com.gridnode.pdip.base.security.mime.smime.helpers;

import java.io.*;
import java.util.*;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;

import com.gridnode.pdip.base.certificate.helpers.GridCertUtilities;
import com.gridnode.pdip.base.security.exceptions.*;
import com.gridnode.pdip.base.security.helpers.*;
import com.gridnode.pdip.base.security.mime.*;
import com.gridnode.pdip.base.security.mime.smime.*;
import com.gridnode.pdip.base.security.mime.smime.exceptions.*;

/**
 * <p>Title:  * This software is the proprietary information of GridNode Pte Ltd.
 * <p>Description: Peer Data Integration Platform
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: GridNode Pte Ltd</p>
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 05 Nov 2003    Zou Qingsong        Created. *
 */

public class GNSMimeBasic implements ISMimePackager, ISMimeDePackager
{
  private Vector actions = new Vector();
  private HashMap actionScope = new HashMap();
  private SMimeFactory factory = null;

  private MimeBodyPart content = null;
  private MimeBodyPart[] attachments = null;
  private boolean expandMessage = true;
  private MimeMessage message;
  private MimeMessage message1;
  private byte[] messageDigest;
  private String digestAlgorithm = "SHA1";
  private String encoding_pkcs7 = SMimeHelper.ENCODING_BASE64;
  private String encoding_default = SMimeHelper.ENCODING_BINARY;

  static
  {
    MailcapCommandMap mc = (MailcapCommandMap)CommandMap.getDefaultCommandMap();
    mc.addMailcap("application/pkcs7-mime;; x-java-content-handler=com.gridnode.pdip.base.security.mime.smime.helpers.pkcs7_mime");
    mc.addMailcap("application/pkcs7-signature;; x-java-content-handler=com.gridnode.pdip.base.security.mime.smime.helpers.pkcs7_mime");
    CommandMap.setDefaultCommandMap(mc);
  }

  public void  setPKCS7Encoding(String encoding)
  {
    encoding_pkcs7 = encoding;
  }

  public void   setDefaultEncoding(String encoding)
  {
    encoding_default = encoding;
  }

  public String  getPKCS7Encoding()
  {
    return encoding_pkcs7;
  }

  public String   getDefaultEncoding()
  {
    return encoding_default;
  }

  public GNSMimeBasic(SMimeFactory factory)
  {
    this.factory = factory;
  }

  public SMimeFactory getFactory()
  {
    return factory;
  }

  public void clearAllActions()
  {
    actions = new Vector();
  }

  public void appendAction(String action)
  {
    removeAction(action);
    actions.add(action);
  }

  public void removeAction(String action)
  {
      actions.remove(action);
  }

  public void setActions(Vector actions)
  {
    this.actions = actions;
  }

  public Vector getActions()
  {
    return actions;
  }

  public void setActionProperty(String action, int scope)
  {
    actionScope.put(action, new Integer(scope));
    if(scope != SCOPE_NONE && actions.indexOf(action) < 0)
      appendAction(action);
    else if(scope == SCOPE_NONE && actions.indexOf(action) >= 0)
      removeAction(action);
  }

  public int getActionProperty(String action)
  {
    Object ob = actionScope.get(action);
    try
    {
      return ((Integer)ob).intValue();
    }
    catch (Exception ex)
    {
      return SCOPE_NONE;
    }
  }

  public void setSignScope(int scope)
  {
    setActionProperty(ACTION_SIGN, scope);
  }

  public boolean     hasAction(String ac)
  {
    if(actions.indexOf(ac) >= 0)
      return true;
    else
      return false;
  }

  public boolean     isSigned()
  {
    return hasAction(ACTION_SIGN);
  }

  public boolean     isEncryted()
  {
    return hasAction(ACTION_ENCRYPT);
  }

  public boolean     isCompressed()
  {
    return hasAction(ACTION_COMPRESS);
  }

  public void disableSign()
  {
    setSignScope(SCOPE_NONE);
  }

  public byte[] getMessageDigest() throws GNSMimeException
  {
//SecurityLogger.debug("to compute digest on [" + new String(messageDigest) + "], algo is"
//+ digestAlgorithm);
    return getMessageDigest(messageDigest);
  }

  public byte[] getMessageDigest(byte[] content) throws GNSMimeException
  {
    return GridCertUtilities.getMessageDigest(digestAlgorithm, content);
  }

  public int getSignScope()
  {
    return getActionProperty(ACTION_SIGN);
  }

  public void setEncryptScope(int scope)
  {
    setActionProperty(ACTION_ENCRYPT, scope);
  }

  public void disableEncryption()
  {
    setEncryptScope(SCOPE_NONE);
  }

  public int getEncryptScope()
  {
    return getActionProperty(ACTION_ENCRYPT);
  }

  public void setCompressScope(int scope)
  {
    setActionProperty(ACTION_COMPRESS, scope);
  }

  public void disableCompress()
  {
    setCompressScope(SCOPE_NONE);
  }

  public int getCompressScope()
  {
    return getActionProperty(ACTION_COMPRESS);
  }

  public void setContent(MimeBodyPart content)
  {
    this.content = content;
  }

  public MimeBodyPart getContent()
  {
    return content;
  }

  public void setAttachments(MimeBodyPart[] attachments)
  {
    this.attachments = attachments;
  }

  public MimeBodyPart[] getAttachments()
  {
    return attachments;
  }

  public void  setExpandMessage(boolean isExpand)
  {
    expandMessage = isExpand;
  }

  public boolean isExpandMessage()
  {
    return expandMessage;
  }
  /*
  private boolean validActions()
  {
    try
    {
      if(actions == null || actions.size() <= 0)
        return true;
      int lastscope = SCOPE_NONE;
      for(int i = 0; i < actions.size();i++)
      {
        int scope = getActionProperty((String)actions.get(i));
        if(scope == SCOPE_ALL)
          lastscope = SCOPE_ALL;
        if(lastscope == SCOPE_ALL && scope > SCOPE_ALL)
          return false;
      }
      return true;
    }
    catch (Exception ex)
    {
        return false;
    }
  }*/

  protected MimeMultipart sign(MimeMultipart doc, int scope) throws GNSMimeException
  {
    try
    {
        SecurityLogger.log("GNSMimeBasic: sign scope[" + scope + "]");
        if(scope == SCOPE_NONE)
          return doc;
        MimeMultipart m = new MimeMultipart();
        MimeMultipart m1 = new MimeMultipart();
        MimeBodyPart toDigest = null;

        if(doc.getCount() == 1)
        {
          toDigest = (MimeBodyPart)doc.getBodyPart(0);
          m.addBodyPart(SMimeHelper.checkSetContentEncoding(SMimeHelper.createPart(sign(toDigest)), getDefaultEncoding()));
        }
        else
        {
          switch(scope)
          {
            case SCOPE_ALL:
              SecurityLogger.log("GNSMimeBasic: sign All");
              toDigest = SMimeHelper.checkSetContentEncoding(SMimeHelper.createPart(doc), getDefaultEncoding());
              m.addBodyPart(SMimeHelper.checkSetContentEncoding(SMimeHelper.createPart(sign(toDigest)), getDefaultEncoding()));
              break;
            case SCOPE_PAYLOAD:
              SecurityLogger.log("GNSMimeBasic: sign Conaint");
              toDigest = (MimeBodyPart)doc.getBodyPart(0);
              m.addBodyPart(SMimeHelper.checkSetContentEncoding(SMimeHelper.createPart(sign(toDigest)), getDefaultEncoding()));
              for(int i = 1; i < doc.getCount();i++)
              {
                m.addBodyPart(doc.getBodyPart(i));
              }
              break;
            case SCOPE_ATTACHMENT:
              SecurityLogger.log("GNSMimeBasic: sign Attachment");
              m.addBodyPart(doc.getBodyPart(0));
              if(doc.getCount() > 1)
              {
                if(doc.getCount() == 2)
                  {
                    toDigest = (MimeBodyPart)doc.getBodyPart(1);
                    m.addBodyPart(SMimeHelper.checkSetContentEncoding(SMimeHelper.createPart(sign(toDigest)), getDefaultEncoding()));
                  }
                else
                {
                  for(int i = 1; i < doc.getCount();i++)
                  {
                    m1.addBodyPart(doc.getBodyPart(i));
                  }
                  toDigest = SMimeHelper.checkSetContentEncoding(SMimeHelper.createPart(m1), getDefaultEncoding());
                  m.addBodyPart(SMimeHelper.checkSetContentEncoding(SMimeHelper.createPart(sign(toDigest)), getDefaultEncoding()));
                }
              }
              break;
         }
        }
        if(toDigest != null)
          messageDigest = SMimeHelper.getBytesFromMime(toDigest);
        return m;
    }
   catch (MessagingException ex)
   {
      throw new GNSMimeException(ex, GNSMimeException.GNSMIME_EXCEPTION_WRONG_CONTENT);
   }
   catch (IOException ex)
   {
      throw new GNSMimeException(ex, GNSMimeException.GNSMIME_EXCEPTION_WRONG_CONTENT);
   }
 }

  protected MimeMultipart encrypt(MimeMultipart doc, int scope) throws GNSMimeException
  {
    try
    {
        SecurityLogger.log("GNSMimeBasic: encrypt scope[" + scope + "]");
        if(scope == SCOPE_NONE)
          return doc;
        MimeMultipart m = new MimeMultipart();
        MimeMultipart m1 = new MimeMultipart();

        if(doc.getCount() == 1)
        {
          m.addBodyPart(encrypt(doc.getBodyPart(0)));
        }
        else
        {
          switch(scope)
          {
            case SCOPE_ALL:
              SecurityLogger.log("GNSMimeBasic: encrypt All");
              m.addBodyPart(encrypt(SMimeHelper.checkSetContentEncoding(SMimeHelper.createPart(doc), getDefaultEncoding())));
              break;
            case SCOPE_PAYLOAD:
              SecurityLogger.log("GNSMimeBasic: encrypt Containt");
              m.addBodyPart(encrypt(doc.getBodyPart(0)));
              for(int i = 1; i < doc.getCount();i++)
              {
                m.addBodyPart(doc.getBodyPart(i));
              }
              break;
            case SCOPE_ATTACHMENT:
              SecurityLogger.log("GNSMimeBasic: encrypt Attachment");
              m.addBodyPart(doc.getBodyPart(0));
              if(doc.getCount() > 1)
              {
                if(doc.getCount() == 2)
                    m.addBodyPart(encrypt(doc.getBodyPart(1)));
                else
                {
                  for(int i = 1; i < doc.getCount();i++)
                  {
                    m1.addBodyPart(doc.getBodyPart(i));
                  }
                  m.addBodyPart(encrypt(SMimeHelper.checkSetContentEncoding(SMimeHelper.createPart(m1), getDefaultEncoding())));
                }
              }
              break;
         }
        }
        return m;
    }
   catch (MessagingException ex)
   {
      throw new GNSMimeException(ex, GNSMimeException.GNSMIME_EXCEPTION_WRONG_CONTENT);
   }
  }

  protected MimeMultipart compress(MimeMultipart doc, int scope) throws GNSMimeException
  {
    try
    {
      SecurityLogger.log("GNSMimeBasic: compress scope[" + scope + "]");
      if(scope == SCOPE_NONE)
        return doc;
      MimeMultipart m = new MimeMultipart();
      MimeMultipart m1 = new MimeMultipart();

      if(doc.getCount() == 1)
      {
        m.addBodyPart(compress(doc.getBodyPart(0)));
      }
      else
      {
        switch(scope)
        {
          case SCOPE_ALL:
            SecurityLogger.log("GNSMimeBasic: compress All");
            m.addBodyPart(compress(SMimeHelper.checkSetContentEncoding(SMimeHelper.createPart(doc), getDefaultEncoding())));
            break;
          case SCOPE_PAYLOAD:
           SecurityLogger.log("GNSMimeBasic: compress Containt");
           m.addBodyPart(compress(doc.getBodyPart(0)));
            for(int i = 1; i < doc.getCount();i++)
            {
              m.addBodyPart(doc.getBodyPart(i));
            }
            break;
          case SCOPE_ATTACHMENT:
           SecurityLogger.log("GNSMimeBasic: compress Attachment");
            m.addBodyPart(doc.getBodyPart(0));
            if(doc.getCount() > 1)
            {
              if(doc.getCount() == 2)
                m.addBodyPart(compress(doc.getBodyPart(1)));
              else
              {
                for(int i = 1; i < doc.getCount();i++)
                {
                  m1.addBodyPart(doc.getBodyPart(i));
                }
                m.addBodyPart(compress(SMimeHelper.checkSetContentEncoding(SMimeHelper.createPart(m1), getDefaultEncoding())));
              }
             }
            break;
       }
      }
      return m;
    }
   catch (MessagingException ex)
   {
      throw new GNSMimeException(ex, GNSMimeException.GNSMIME_EXCEPTION_WRONG_CONTENT);
   }
  }

  public MimeMessage expandMessage(MimeMessage m) throws GNSMimeException
  {
     try
     {
        if(!SMimeHelper.isMultipart(m) || SMimeHelper.isSigned(m))
          return m;
        Multipart body = (Multipart)m.getContent();
        if(body.getCount() <= 1)
          return m;
        if(!(body.getBodyPart(1).getContent() instanceof Multipart))
          return m;
        Multipart body1 = (Multipart)body.getBodyPart(1).getContent();

        mime_multipart m2 = new mime_multipart(body);
        while(m2.getCount() > 1)
          m2.removeBodyPart(1);
       for(int i = 0; i < body1.getCount(); i++)
       {
          m2.addBodyPart(body1.getBodyPart(i));
       }
       return SMimeHelper.createMessage(m2);
     }
     catch (MessagingException ex)
     {
        throw new GNSMimeException(ex, GNSMimeException.GNSMIME_EXCEPTION_WRONG_CONTENT);
     }
     catch (IOException ex)
     {
        throw new GNSMimeException(ex, GNSMimeException.GNSMIME_EXCEPTION_WRONG_CONTENT);
     }
  }

  protected BodyPart dePackDocument(BodyPart part) throws GNSMimeException
  {
     try
     {
      if(SMimeHelper.isSigned((MimeBodyPart)part))
      {
         SecurityLogger.log("GNSMimeBasic: dePackDocument " + "signed message");
         appendAction(ACTION_SIGN);
         MimeBodyPart toDigest = (MimeBodyPart)verify((MimeMultipart)part.getContent());
         messageDigest = SMimeHelper.getBytesFromMime(toDigest);
         return dePackDocument(toDigest);
      }
      else if(SMimeHelper.isEncrypted((MimeBodyPart)part))
      {
         SecurityLogger.log("GNSMimeBasic: dePackDocument " + "encrypted message");
         appendAction(ACTION_ENCRYPT);
         return dePackDocument(deCrypt(part));
      }
      else if(SMimeHelper.isCompressed((MimeBodyPart)part))
      {
         SecurityLogger.log("GNSMimeBasic: dePackDocument " + "Compressed message");
         appendAction(ACTION_COMPRESS);
         return dePackDocument(deCompress(part));
      }
      else if(SMimeHelper.isMultipart((MimeBodyPart)part))
      {
         SecurityLogger.log("GNSMimeBasic: dePackDocument " + "Multipart message");
         MimeMultipart m1 = (MimeMultipart)part.getContent();
         mime_multipart m2 = new mime_multipart(m1);
         while(m2.getCount() > 0)
          m2.removeBodyPart(0);
         for(int i = 0; i < m1.getCount(); i++)
         {
            m2.addBodyPart(dePackDocument(m1.getBodyPart(i)));
         }
         return  SMimeHelper.createPart(m2);
      }
      else
        return part;
    }
   catch (MessagingException ex)
   {
      throw new GNSMimeException(ex, GNSMimeException.GNSMIME_EXCEPTION_WRONG_CONTENT);
   }
   catch (IOException ex)
   {
      throw new GNSMimeException(ex, GNSMimeException.GNSMIME_EXCEPTION_WRONG_CONTENT);
   }
  }

  public MimeMessage dePackDocument() throws GNSMimeException
  {
      SecurityLogger.log("GNSMimeBasic: dePackDocument start");
      messageDigest = null;
      MimeBodyPart part = new MimeBodyPart();
      try
      {
        if(message.getContent() instanceof Multipart)
          {
            SecurityLogger.log("GNSMimeBasic: dePackDocument mutipart");
            part = SMimeHelper.createPart((MimeMultipart)message.getContent());
            SMimeHelper.setContentType(part, message.getContentType());
          }
        else
          {
              SecurityLogger.log("GNSMimeBasic: dePackDocument not mutipart");
              part = SMimeHelper.createPart(message.getContent(), message.getContentType());
          }
        MimeBodyPart depackedPart = (MimeBodyPart)dePackDocument(part);
//        SecurityLogger.log("After depackMsg, encoding is " + SMimeHelper.getContentEncoding(depackedPart) );
//        SMimeHelper.checkSetContentEncoding(depackedPart, SMimeHelper.ENCODING_BINARY);
        byte[] messageBytes = SMimeHelper.getBytesFromMime(depackedPart);
//SecurityLogger.debug("Before createmessage");
        message1 = SMimeHelper.createMessage(messageBytes);
//        SecurityLogger.log("Message encoding is " + SMimeHelper.getContentEncoding(message1));
        if(isExpandMessage())
          {
            SecurityLogger.log("GNSMimeBasic: dePackDocument expand message");
            message1 = expandMessage(message1);
          }
        if(isSigned() || isEncryted() || isCompressed())
              SecurityLogger.log("GNSMimeBasic: dePackDocument digest (header+content) generated by depacking process already!");
        else
          {
              messageDigest = SMimeHelper.getContentBytesFromMime(message1);
              SecurityLogger.log("GNSMimeBasic: packDocument digest for content only(no header)");
          }
        SecurityLogger.log("GNSMimeBasic: dePackDocument finish");
        return message1;
      }
     catch (IOException ex)
     {
        throw new GNSMimeException(ex, GNSMimeException.GNSMIME_EXCEPTION_WRONG_CONTENT);
     }
     catch (MessagingException ex)
     {
        throw new GNSMimeException(ex, GNSMimeException.GNSMIME_EXCEPTION_WRONG_CONTENT);
     }
  }

  public MimeMessage packDocument() throws GNSMimeException
  {
      SecurityLogger.log("GNSMimeBasic: packDocument start");
      messageDigest = null;
      MimeMultipart envelope = new MimeMultipart();
      try
      {
        envelope.addBodyPart(SMimeHelper.checkSetContentEncoding(content, getDefaultEncoding()));
        if(attachments != null && attachments.length > 0)
        {
          for(int i = 0; i < attachments.length;i++)
          {
            envelope.addBodyPart(SMimeHelper.checkSetContentEncoding(attachments[i], getDefaultEncoding()));
          }
        }

//      SMimeHelper.createMessage(envelope);
      SecurityLogger.log("GNSMimeBasic: packDocument actions size[" + actions.size() + "]" + actions);

      if(actions != null && actions.size() > 0)
      {
        for(int i = 0; i < actions.size();i++)
          {
            String action = (String)actions.get(i);
            SecurityLogger.log("GNSMimeBasic: packDocument action[" + i + "]" + action);
            if(ACTION_COMPRESS.equals(action))
              {
                try
                {
                  envelope = compress(envelope, getActionProperty(action));
                }
                catch (Exception ex)
                {
                  throw new GNSMimeException(ex, GNSMimeException.GNSMIME_EXCEPTION_COMPRESS);
                }
              }
            else if(ACTION_ENCRYPT.equals(action))
              {
                try
                {
                  envelope = encrypt(envelope, getActionProperty(action));
                }
                catch (Exception ex)
                {
                  throw new GNSMimeException(ex, GNSMimeException.GNSMIME_EXCEPTION_ENCRYPT);
                }
              }
            else if(ACTION_SIGN.equals(action))
              {
                try
                {
                  envelope = sign(envelope, getActionProperty(action));
                }
                catch (Exception ex)
                {
                  throw new GNSMimeException(ex, GNSMimeException.GNSMIME_EXCEPTION_SIGN);
                }
              }
//            SMimeHelper.createMessage(envelope);
          }
      }
      }
      catch (MessagingException ex)
      {
          throw new GNSMimeException(ex, GNSMimeException.GNSMIME_EXCEPTION_WRONG_CONTENT);
      }
      try
      {
        if(envelope.getCount() == 1)
            {
              if(envelope.getBodyPart(0).getContent() instanceof Multipart)
                {
                  SecurityLogger.log("GNSMimeBasic: packDocument result unwarp one multipart");
                  message1 = SMimeHelper.createMessage((Multipart)envelope.getBodyPart(0).getContent());
                }
              else
              {
                  SecurityLogger.log("GNSMimeBasic: packDocument result unwarp one bodypart");
                  message1 = SMimeHelper.createMessage((MimeBodyPart)envelope.getBodyPart(0));
              }
            }
        else
            {
              SecurityLogger.log("GNSMimeBasic: packDocument result multiple bodyparts");
              message1 = SMimeHelper.createMessage(envelope);
            }
        message1.saveChanges();
        if(!isSigned())
        {
          if(!isEncryted() && !isCompressed())
            {
              messageDigest = SMimeHelper.getContentBytesFromMime(content);
              SecurityLogger.log("GNSMimeBasic: packDocument digest for content only(no header)");
            }
            else
            {
              messageDigest = SMimeHelper.getBytesFromMime(content);
              SecurityLogger.log("GNSMimeBasic: packDocument digest for content + header");
            }
        }
        else
        {
          SecurityLogger.log("GNSMimeBasic: packDocument digest generated by signing process already!");
        }
      }
      catch (MessagingException ex)
      {
          throw new GNSMimeException(ex, GNSMimeException.GNSMIME_EXCEPTION_WRONG_CONTENT);
      }
      catch (IOException ex)
      {
          throw new GNSMimeException(ex, GNSMimeException.GNSMIME_EXCEPTION_WRONG_CONTENT);
      }
      SecurityLogger.log("GNSMimeBasic: packDocument finish");
      return message1;
  }

  public BodyPart compress(BodyPart content) throws GNSMimeException
  {
    try
    {
      MimeBodyPart body = SMimeHelper.createPart(compress(SMimeHelper.getBytesFromMime((MimeBodyPart)content)), SMimeFactory.PKCS7_MIME_TYPE);
      body = (MimeBodyPart)SMimeHelper.setParameter(body, "smime-type", pkcs7_mime.COMPRESSED_SMIME_TYPE_VALUE);
      body = (MimeBodyPart)SMimeHelper.setParameter(body, "name", "smime.p7z");
      body = (MimeBodyPart)SMimeHelper.checkSetContentEncoding(body, getPKCS7Encoding());
      body.setFileName("smime.p7z");
      return body;
    }
   catch (MessagingException ex)
   {
      throw new GNSMimeException(ex, GNSMimeException.GNSMIME_EXCEPTION_WRONG_CONTENT);
   }
   catch (IOException ex)
   {
      throw new GNSMimeException(ex, GNSMimeException.GNSMIME_EXCEPTION_WRONG_CONTENT);
   }
  }


  public BodyPart deCompress(BodyPart content) throws GNSMimeException
  {
    try
    {
      byte[] me = deCompress(SMimeHelper.getContentBytesFromMime((MimeBodyPart)content));
      if(!isSigned())
        messageDigest = me;
      return SMimeHelper.createPart(me);
    }
     catch (MessagingException ex)
     {
        throw new GNSMimeException(ex, GNSMimeException.GNSMIME_EXCEPTION_WRONG_CONTENT);
     }
     catch (IOException ex)
     {
        throw new GNSMimeException(ex, GNSMimeException.GNSMIME_EXCEPTION_WRONG_CONTENT);
     }
  }

  public BodyPart deCrypt(BodyPart content) throws GNSMimeException
  {
    try
    {
      byte[] me = decrypt(SMimeHelper.getContentBytesFromMime((MimeBodyPart)content));
      if(!isSigned())
        messageDigest = me;
      return SMimeHelper.createPart(me);
    }
     catch (MessagingException ex)
     {
        throw new GNSMimeException(ex, GNSMimeException.GNSMIME_EXCEPTION_WRONG_CONTENT);
     }
     catch (IOException ex)
     {
        throw new GNSMimeException(ex, GNSMimeException.GNSMIME_EXCEPTION_WRONG_CONTENT);
     }
  }

  public BodyPart verify(Multipart content) throws GNSMimeException
  {
    try
    {
      BodyPart or = content.getBodyPart(0);
      BodyPart sig = content.getBodyPart(1);
      if(verify(SMimeHelper.getBytesFromMime((MimeBodyPart)or), SMimeHelper.getContentBytesFromMime((MimeBodyPart)sig)) != null)
        return or;
      else
        return null;
    }
   catch (MessagingException ex)
   {
      throw new GNSMimeException(ex, GNSMimeException.GNSMIME_EXCEPTION_WRONG_CONTENT);
   }
   catch (IOException ex)
   {
      throw new GNSMimeException(ex, GNSMimeException.GNSMIME_EXCEPTION_WRONG_CONTENT);
   }
  }

  public byte[] compress(byte[] content) throws GNSMimeException
  {
    try
    {
//SecurityLogger.debug("To compress: content is ");
//byte[] temp = new byte[300];
//System.arraycopy(content,0,temp,0,300);
//SecurityLogger.debug(new String(temp));
      return getFactory().compress(content);
//      return new SecurityServiceBean().compress(getFactory().getCompressMethod(), getFactory().getCompressLevel(), content);
    }
    catch (Exception ex)
    {
      throw new GNSMimeException(ex,GNSMimeException.GNSMIME_EXCEPTION_COMPRESS);
    }
  }

  public byte[] encrypt(byte[] content) throws GNSMimeException
  {
    try
    {
      return getFactory().encrypt(content);
    }
    catch (Exception ex)
    {
      throw new GNSMimeException(ex,GNSMimeException.GNSMIME_EXCEPTION_ENCRYPT);
    }
  }

  public byte[] sign(byte[] content) throws GNSMimeException
  {
    try
    {
      return getFactory().sign(content);

    }
    catch (Exception ex)
    {
      SecurityLogger.warn("message verify failed in public byte[] sign(byte[] content)", ex);
      throw new GNSMimeException(ex,GNSMimeException.GNSMIME_EXCEPTION_SIGN);
    }

  }


  public byte[] deCompress(byte[] content) throws GNSMimeException
  {
    try
    {
        return getFactory().deCompress(content);
//      return new SecurityServiceBean().deCompress(content);

    }
    catch (Exception ex)
    {
      throw new GNSMimeException(ex,GNSMimeException.GNSMIME_EXCEPTION_DECOMPRESS);
    }
  }


  public byte[] decrypt(byte[] content) throws GNSMimeException
  {
    try
    {
      return getFactory().decrypt(content);
    }
    catch (SecurityServiceException ex)
    {
      throw new GNSMimeException(ex,GNSMimeException.GNSMIME_EXCEPTION_DECRYPT);
    }
  }

  public byte[] verify(byte[] content, byte[] signature) throws GNSMimeException
  {
    try
    {
//      SecurityLogger.log("verify message[" + new String(content)+ "]");
      getFactory().verify(content, signature);
      return content;
    }
    catch (Exception ex)
    {
      throw new GNSMimeException(ex,GNSMimeException.GNSMIME_EXCEPTION_VERIFY);
    }
  }

  public BodyPart encrypt(BodyPart content) throws GNSMimeException
  {
    try
    {
      MimeBodyPart body = SMimeHelper.createPart(encrypt(SMimeHelper.getBytesFromMime((MimeBodyPart)content)), SMimeFactory.PKCS7_MIME_TYPE);
      body = (MimeBodyPart)SMimeHelper.setParameter(body, "smime-type", pkcs7_mime.ENCRYPTED_SMIME_TYPE_VALUE);
      body = (MimeBodyPart)SMimeHelper.setParameter(body, "name", "smime.p7m");
      body = (MimeBodyPart)SMimeHelper.checkSetContentEncoding(body, getPKCS7Encoding());
      body.setFileName("smime.p7m");
      return body;
   }
   catch (MessagingException ex)
   {
      throw new GNSMimeException(ex, GNSMimeException.GNSMIME_EXCEPTION_WRONG_CONTENT);
   }
   catch (IOException ex)
   {
      throw new GNSMimeException(ex, GNSMimeException.GNSMIME_EXCEPTION_WRONG_CONTENT);
   }
  }

  public Multipart sign(BodyPart content) throws GNSMimeException
  {
    try
    {
      MimeBodyPart body = SMimeHelper.createPart(sign(SMimeHelper.getBytesFromMime((MimeBodyPart)content)), SMimeFactory.PKCS7_SIGNATURE_CONTENT_TYPE);
//      verify(SMimeHelper.getBytesFromMime((MimeBodyPart)content), SMimeHelper.getContentBytesFromMime((MimeBodyPart)body));
//      SecurityLogger.log("pos1 in Multipart sign(BodyPart content)");
      body = (MimeBodyPart)SMimeHelper.setParameter(body, "smime-type", pkcs7_mime.SIGNED_SMIME_TYPE_VALUE);
      body = (MimeBodyPart)SMimeHelper.setParameter(body, "name", "smime.p7s");
      body = (MimeBodyPart)SMimeHelper.checkSetContentEncoding(body, SMimeHelper.ENCODING_BASE64);
      body.setFileName("smime.p7s");
      MimeMultipart mul = new MimeMultipart();

      mul.addBodyPart(content);
      mul.addBodyPart(body);
      mul.setSubType("signed");
      mul = SMimeHelper.setParameter(mul, "protocol", SMimeFactory.PKCS7_SIGNATURE_CONTENT_TYPE);

      String digetsAlgo = getFactory().getDigestAlgorithm();
      if(digetsAlgo == null)
      {
        digetsAlgo = SMimeFactory.DEFAULT_DIGEST_ALGO;
      }
      else
      {
        digetsAlgo = SMimeFactory.getExternalDigestAlgo(getFactory().getDigestAlgorithm());
      }
      mul = SMimeHelper.setParameter(mul, "micalg", digetsAlgo);
      SecurityLogger.log("message verified in Multipart sign(BodyPart content)");
      return mul;
   }
   catch (MessagingException ex)
   {
      throw new GNSMimeException(ex, GNSMimeException.GNSMIME_EXCEPTION_WRONG_CONTENT);
   }
   catch (IOException ex)
   {
      throw new GNSMimeException(ex, GNSMimeException.GNSMIME_EXCEPTION_WRONG_CONTENT);
   }
  }

  public void setMessage(MimeMessage message)
  {
    this.message = message;
  }

  public MimeMessage getMessage()
  {
    return message;
  }

  public MimeMessage getPackedMessage()
  {
    return message1;
  }

  public MimeMessage getDePackedMessage()
  {
    return message1;
  }

  public String getDigestAlgorithm()
  {
    return digestAlgorithm;
  }

  public void setDigestAlgorithm(String digestAlgorithm)
  {
    this.digestAlgorithm = digestAlgorithm;
  }

  public static void main(String[] args) throws Exception
  {

    MailcapCommandMap mc = (MailcapCommandMap)CommandMap.getDefaultCommandMap();
    mc.addMailcap("application/xml;; x-java-content-handler=com.gridnode.pdip.base.security.mime.smime.helpers.pkcs7_mime");
    mc.addMailcap("application/edifact;;x-java-content-handler=com.gridnode.pdip.base.security.mime.smime.helpers.pkcs7_mime");
    mc.addMailcap("application/edi-x12;;x-java-content-handler=com.gridnode.pdip.base.security.mime.smime.helpers.pkcs7_mime");
    mc.addMailcap("application/edi-consent;;x-java-content-handler=com.gridnode.pdip.base.security.mime.smime.helpers.pkcs7_mime");
    mc.addMailcap("message/disposition-notification;;x-java-content-handler=com.gridnode.pdip.base.security.mime.smime.helpers.pkcs7_mime");
    CommandMap.setDefaultCommandMap(mc);
    byte[] content = SMimeHelper.getBytesFromFile("c:\\data1.dat");
    MimeBodyPart msg = SMimeHelper.createPart(content);
    SMimeFactory fac = SMimeFactory.newInstance(null,null,"RC2/CBC/PKCS5Padding","SHA1",40);
    ISMimePackager smime = SMimeFactory2.getSMimePackager("AS2",fac);
    smime.setPKCS7Encoding(SMimeHelper.ENCODING_BINARY);
    fac.setCompressLevel(9);
    smime.setSignScope(GNSMimeBasic.SCOPE_ALL);

//    MimeBodyPart msg = new MimeBodyPart();
//    msg.setText("Hello world!");
//    SMimeHelper.setContentType(msg, "text/plain");

//    MimeBodyPart msg1 = new MimeBodyPart();
//    msg1.setText("Hello world 1!");
//    SMimeHelper.setContentType(msg1, "text/plain");
//
//    MimeBodyPart msg2 = new MimeBodyPart();
//    msg2.setText("Hello world 2!");
//    SMimeHelper.setContentType(msg2, "text/plain");

    smime.setContent(msg);
//    smime.setAttachments(new MimeBodyPart[]{msg1, msg2});
    MimeMessage enc = smime.packDocument();
//
    System.out.println(new String("----orig message start----"));
      System.out.println(new String(SMimeHelper.getBytesFromMime(enc)));
    System.out.println(new String("----orig message end----"));



//    seu.verify(smime.getPackedMessage());
//    ISMimeDePackager smime1 =  SMimeFactory2.getSMimeDePackager("AS2",fac);
//    System.out.println(new String( smime1.deCompress(enc)));
//    System.out.println(new String(smime1.deCompress(enc)));
//    smime1.setExpandMessage(false);
//    MimeMessage me = SMimeHelper.createMessage(SMimeHelper.getBytesFromFile("c:\\1.dat"));
//    byte[] con = SMimeHelper.getContentBytesFromMime(me);
//
//    SMimeHelper.createFile(new File("c:\\a.zip") ,con);
//    smime1.setMessage(me);

//    MimeMessage me = SMimeHelper.createMessage(SMimeHelper.getBytesFromFile("c:\\2.txt"));
//    smime1.setMessage(me);
//    MimeMessage me1 = smime1.dePackDocument();
//    System.out.println(new String("----de message start----"));
//      System.out.println(new String( SMimeHelper.getContentBytesFromMime(me1)));
//    System.out.println(new String("----de message end----"));
 }

}