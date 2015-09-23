/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SendBeRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-30     Andrew Hill         Created
 * 2002-10-11     Andrew Hill         Use the be's canSend() method in filter
 * 2003-01-15     Andrew Hill         Modified Fields (use gns now)
 */
package com.gridnode.gtas.client.web.be;

import org.apache.struts.action.*;
import org.apache.commons.beanutils.PropertyUtils;

import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.utils.*;
import com.gridnode.gtas.client.web.strutsbase.*;
import com.gridnode.gtas.client.web.StaticWebUtils;

import org.w3c.dom.*;

public class SendBeRenderer extends AbstractRenderer implements IFilter
{
  //20030115AH - Use static fake constraints to save object creations
  private static final FakeForeignEntityConstraint gnConstraint
      = new FakeForeignEntityConstraint(IGTEntity.ENTITY_GRIDNODE,"uid","id",new String[]{"name"});
  private static final FakeForeignEntityConstraint beConstraint
      = new FakeForeignEntityConstraint( IGTEntity.ENTITY_BUSINESS_ENTITY,"uid","id");
  //...


  public SendBeRenderer( RenderingContext rContext )
  {
    super(rContext);
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      ActionErrors errors = rContext.getActionErrors();
      IGTSession gtasSession = StaticWebUtils.getGridTalkSession(rContext.getRequest());
      if(gtasSession == null) throw new NullPointerException("gtasSession is null"); //20030416AH
      SendBeAForm form = (SendBeAForm)rContext.getOperationContext().getActionForm();
      BindingFieldPropertyRenderer bfpr = new BindingFieldPropertyRenderer(rContext);
      renderCommonFormElements("sendBe",true);
      //20030115AH
      renderFerField(bfpr,form,gtasSession,"gnUids",gnConstraint,errors);
      renderFerField(bfpr,form,gtasSession,"beUids",beConstraint,errors);
      //...
      renderLabel("beUids_create","sendBe.beUids.create");

      //20030325 Modify ok button
      Element ok = getElementById("ok",false);
      if(ok != null)
      {
        //ok.setAttribute("href","javascript:submitWithMethod('complete',true);");
        ok.setAttribute("onclick","submitWithMethod('complete',true); return false;"); // 20040116 DDJ: Fix for "Action[/sendBeDispatchAction] does not contain method named save"
        renderLabelCarefully(ok,"businessEntity.listview.send");
      }
      //...
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering sendBusinessEntity screen",t);
    }
  }

  private void renderFerField(BindingFieldPropertyRenderer bfpr,
                              ActionForm form,
                              IGTSession gtasSession,
                              String fieldName,
                              FakeForeignEntityConstraint constraint,
                              ActionErrors errors)
    throws RenderingException
  { //20030115AH - Render a fer field the hard way...
    try
    {
      Object value = null;
      try
      {
        value = PropertyUtils.getProperty(form, fieldName);
      }
      catch(Throwable t)
      {
        throw new RenderingException("Unable to read value of '" + fieldName + "' from form",t);
      }
      bfpr.setBindings(fieldName);
      bfpr.setConstraint(constraint);
      bfpr.setLabelKey("sendBe." + fieldName);
      bfpr.setValue(value);
      bfpr.setVisible(true);
      bfpr.setEditable(true);
      bfpr.setMandatory(true);
      bfpr.setSession(gtasSession);
      bfpr.setConstraintFilter(this);
      bfpr.setErrorKey( MessageUtils.getFirstErrorKey(errors,fieldName) );
      bfpr.render(_target);
      bfpr.reset();
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering sendBe FER field '" + fieldName + "'",t);
    }
  }

  public boolean allows(Object object, Object context) throws GTClientException
  {
    if(object instanceof IGTBusinessEntityEntity)
    {
      return ((IGTBusinessEntityEntity)object).canSend();
    }
    else if(object instanceof IGTGridNodeEntity)
    { //20030115
      return(IGTGridNodeEntity.STATE_ACTIVE.equals(
        ((IGTGridNodeEntity)object).getFieldValue(IGTGridNodeEntity.STATE)) );
    }
    else
    {
      return true;
    }
  }

  /*
    <a id="ok" href="javascript:submitWithMethod('complete',true);">
          lbComplete
        </a>
  */
}

