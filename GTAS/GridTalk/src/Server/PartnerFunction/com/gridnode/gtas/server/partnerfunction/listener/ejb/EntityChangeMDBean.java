/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityChangeMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 16 2002    Koh Han Sing        Created
 * Oct 17 2005    Neo Sok Lay         For JDK1.5 compliance: reflection invocation
 *                                    must explicit cast for null args.
 */
package com.gridnode.gtas.server.partnerfunction.listener.ejb;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.partnerfunction.helpers.IPartnerFunctionPathConfig;
import com.gridnode.gtas.server.partnerfunction.helpers.Logger;
import com.gridnode.gtas.server.partnerfunction.model.PartnerFunction;
import com.gridnode.gtas.server.partnerfunction.xpdl.XPDLWriter;
import com.gridnode.pdip.app.deploy.manager.ejb.IGWFDeployMgrHome;
import com.gridnode.pdip.app.deploy.manager.ejb.IGWFDeployMgrObj;
import com.gridnode.pdip.framework.db.entity.EntityEvent;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This message driven bean is used to perform specific task whenever
 * there is a create/updated/delete of a partner function
 *
 * @author Koh Han Sing
 *
 * @version 4.0
 * @since 2.0
 */
public class EntityChangeMDBean
       implements MessageDrivenBean, MessageListener
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5973337359232695684L;
	private MessageDrivenContext _mdx = null;

  public void setMessageDrivenContext(MessageDrivenContext ctx)
    throws javax.ejb.EJBException
  {
    _mdx = ctx;
  }

  public void ejbRemove()
  {
  }

  public void ejbCreate()
  {
  }

  public void onMessage(Message msg)
  {
    Logger.log("[EntityChangeMDBean.onMessage] Enter");
    try
    {
      EntityEvent event = (EntityEvent)((ObjectMessage)msg).getObject();
      IEntity entity = event.getEntity();
      int eventType = event.getEventType();

      Logger.log("[EntityChangeMDBean.onMessage] EventType = "+event.getEventTypeDesc());

      if (entity.getEntityName().equals(PartnerFunction.ENTITY_NAME))
      {
        if (eventType == EntityEvent.CREATED)
        {
          callXPDLWriter(entity);
        }
        else if (eventType == EntityEvent.UPDATED)
        {
          String xpdlFilename = entity.getFieldValue(PartnerFunction.PARTNER_FUNCTION_ID)+".xpdl";
          File xpdlFile = FileUtil.getFile(IPartnerFunctionPathConfig.PATH_XPDL, xpdlFilename);
          getDeploymentManager().markDeletedXpdl(xpdlFile);

          callXPDLWriter(entity);
        }
        else if (eventType == EntityEvent.DELETED)
        {
          String xpdlFilename = entity.getFieldValue(PartnerFunction.PARTNER_FUNCTION_ID)+".xpdl";
          File xpdlFile = FileUtil.getFile(IPartnerFunctionPathConfig.PATH_XPDL, xpdlFilename);
          getDeploymentManager().markDeletedXpdl(xpdlFile);
          FileUtil.delete(IPartnerFunctionPathConfig.PATH_XPDL, xpdlFilename);
        }
      }
      else if (eventType == EntityEvent.CREATED)
      {
        callXPDLWriter(entity);
      }
    }
    catch (EJBException ejbEx)
    {
      Logger.error(ILogErrorCodes.GT_PARTNER_FUNCTION_ENTITY_CHANGE_MDB,
                   "[EntityChangeMDBean.onMessage] EJB Exception: "+ejbEx.getMessage(), ejbEx);
    }
    catch (Throwable ex)
    {
      Logger.error(ILogErrorCodes.GT_PARTNER_FUNCTION_ENTITY_CHANGE_MDB,
                   "[EntityChangeMDBean.onMessage] Exception: "+ex.getMessage(), ex);
    }
    Logger.log("[EntityChangeMDBean.onMessage] End");
  }

  private void callXPDLWriter(IEntity entity)
    throws Throwable
  {
    String className = entity.getEntityName()+"XPDLWriter";
    try
    {
      XPDLWriter writer = (XPDLWriter)Class.forName(
        "com.gridnode.gtas.server.partnerfunction.xpdl."+className).getMethod("getInstance", (Class[])null).invoke(null, (Object[])null);
      Collection xpdlFiles = writer.writeToXPDL(entity);

      Iterator iterator = xpdlFiles.iterator();
      while (iterator.hasNext())
      {
        File aXpdlFile = (File)iterator.next();
        getDeploymentManager().deployXpdl(aXpdlFile);
      }

      Logger.log("[EntityChangeMDBean.callXPDLWriter] "+entity.getEntityName()+" XPDL deployed");
    }
    catch (ClassNotFoundException ex)
    {
      Logger.log("[EntityChangeMDBean.callXPDLWriter] ClassNotFoundException :"+className);
    }
  }

  private IGWFDeployMgrObj getDeploymentManager()
    throws ServiceLookupException
  {
    return (IGWFDeployMgrObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IGWFDeployMgrHome.class.getName(),
      IGWFDeployMgrHome.class,
      new Object[0]);
  }

  public static void main(String[] asfnisugei)
  {
    try
    {
    EntityChangeMDBean bean = new EntityChangeMDBean();
    File xpdlFile = new File("C:/views/i00017_GT_2.1_new/GTAS/GridTalk/data/sys/workflow/xpdl/MappingRules.xpdl");
    //File xpdlFile = new File("C:/views/i00017_GT_2.1_new/GTAS/GridTalk/data/sys/workflow/xpdl/UserProcedures.xpdl");
    //File xpdlFile = new File("C:/views/i00017_GT_2.1_new/GTAS/GridTalk/data/sys/workflow/xpdl/PFOB.xpdl");
    bean.getDeploymentManager().deployXpdl(xpdlFile);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

}