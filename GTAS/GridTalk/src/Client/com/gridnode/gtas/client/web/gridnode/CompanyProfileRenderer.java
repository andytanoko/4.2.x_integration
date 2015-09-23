/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CompanyProfileRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-10     Andrew Hill         Created
 * 2002-09-27     Andrew Hill         Added properties for renderCommonContent & profile entity
 * 2003-08-18     Andrew Hill         Dont render cancel button in view mode (GNDB00014928)
 */
package com.gridnode.gtas.client.web.gridnode;

import org.w3c.dom.Element;

import com.gridnode.gtas.client.ctrl.IGTCompanyProfileEntity;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.BindingFieldPropertyRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class CompanyProfileRenderer extends AbstractRenderer
{
  private boolean _edit;
  private boolean _renderCommonContent = true;
  private IGTCompanyProfileEntity _profile = null;

  protected static final Number[] fields = {
    IGTCompanyProfileEntity.COY_NAME,
    IGTCompanyProfileEntity.EMAIL,
    IGTCompanyProfileEntity.ALT_EMAIL,
    IGTCompanyProfileEntity.TEL,
    IGTCompanyProfileEntity.ALT_TEL,
    IGTCompanyProfileEntity.FAX,
    IGTCompanyProfileEntity.ADDRESS,
    IGTCompanyProfileEntity.CITY,
    IGTCompanyProfileEntity.STATE,
    IGTCompanyProfileEntity.POSTCODE,
    IGTCompanyProfileEntity.COUNTRY,
    IGTCompanyProfileEntity.LANGUAGE,
  };

  public CompanyProfileRenderer( RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  public void setRenderCommonContent(boolean flag)
  {
    _renderCommonContent = flag;
  }

  public boolean isRenderCommonContent()
  {
    return _renderCommonContent;
  }

  public void setProfileEntity(IGTCompanyProfileEntity entity)
  {
    _profile = entity;
  }

  public IGTCompanyProfileEntity getProfileEntity()
  {
    return _profile;
  }

  protected void render() throws RenderingException
  {
    try
    {
      if(_profile == null)
      {
        _profile = (IGTCompanyProfileEntity)getEntity();
      }
      if(_renderCommonContent)
      {
        Element heading = getElementById("companyProfile_heading",false);
        if(heading != null)
        {
          heading.setAttribute("id","heading");
        }
        renderCommonFormElements(IGTEntity.ENTITY_COMPANY_PROFILE,_edit);
      }
      if(_edit) renderLabel("channels_create","businessEntity.channels.create",false);
      if(!_edit) removeNode("cancel_button",false); //20030818AH
      BindingFieldPropertyRenderer bfpr = renderFields(null,_profile,fields);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering companyProfile screen",t);
    }
  }
}

