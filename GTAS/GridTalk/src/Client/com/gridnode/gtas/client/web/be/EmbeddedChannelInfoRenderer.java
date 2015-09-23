/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EmbeddedChannelInfoRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-09-30     Daniel D'Cotta      Created
 * 2003-10-10     Neo Sok Lay         Change to use IGTSearchedChannelInfoEntity
 *                                    instead of IGTChannelInfoEntity.
 */
package com.gridnode.gtas.client.web.be;

import org.w3c.dom.Node;

import com.gridnode.gtas.client.ctrl.IGTCommInfoEntity;
import com.gridnode.gtas.client.ctrl.IGTPackagingInfoEntity;
import com.gridnode.gtas.client.ctrl.IGTSearchedChannelInfoEntity;
import com.gridnode.gtas.client.ctrl.IGTSecurityInfoEntity;
import com.gridnode.gtas.client.web.renderers.*;

public class EmbeddedChannelInfoRenderer extends AbstractRenderer
{
  //private static final String CONFIGURE_IMAGE_SRC = "images/actions/configure.gif";

  private boolean _edit;

  protected final static Number[] _fields = { IGTSearchedChannelInfoEntity.NAME,
                                              IGTSearchedChannelInfoEntity.DESCRIPTION,
                                              IGTSearchedChannelInfoEntity.REF_ID, };

  protected final static Number[] _piFields = { IGTPackagingInfoEntity.NAME,
                                                IGTPackagingInfoEntity.DESCRIPTION,
                                                IGTPackagingInfoEntity.REF_ID,
                                                IGTPackagingInfoEntity.ENVELOPE, };

  protected final static Number[] _siFields = { IGTSecurityInfoEntity.NAME,
                                                IGTSecurityInfoEntity.DESCRIPTION,
                                                IGTSecurityInfoEntity.REF_ID,
                                                IGTSecurityInfoEntity.ENC_TYPE,
                                                IGTSecurityInfoEntity.ENC_LEVEL,
                                                IGTSecurityInfoEntity.SIG_TYPE,
                                                IGTSecurityInfoEntity.DIGEST_ALGORITHM, };

  protected final static Number[] _ciFields = { IGTCommInfoEntity.NAME,
                                                IGTCommInfoEntity.DESCRIPTION,
                                                IGTCommInfoEntity.REF_ID,
                                                IGTCommInfoEntity.PROTOCOL_TYPE,
                                                IGTCommInfoEntity.URL, };


  public static final ITabDef[] _tabs = {
    new TabDef("embeddedChannelInfo.tabs.general",        "general_tab"),
    new TabDef("embeddedChannelInfo.tabs.packagingInfo",  "packagingInfo_tab"),
    new TabDef("embeddedChannelInfo.tabs.securityInfo",   "securityInfo_tab"),
    new TabDef("embeddedChannelInfo.tabs.commInfo",       "commInfo_tab"),
  };

  public EmbeddedChannelInfoRenderer( RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();

      IGTSearchedChannelInfoEntity embeddedChannelInfo = (IGTSearchedChannelInfoEntity)getEntity();

      IGTPackagingInfoEntity  packagingInfo = (IGTPackagingInfoEntity)embeddedChannelInfo.getFieldValue(IGTSearchedChannelInfoEntity.PACKAGING_PROFILE);
      IGTSecurityInfoEntity   securityInfo  = (IGTSecurityInfoEntity) embeddedChannelInfo.getFieldValue(IGTSearchedChannelInfoEntity.SECURITY_PROFILE);
      IGTCommInfoEntity       commInfo      = (IGTCommInfoEntity)     embeddedChannelInfo.getFieldValue(IGTSearchedChannelInfoEntity.TPT_COMM_INFO);

      //EmbeddedChannelInfoAForm eciForm = (EmbeddedChannelInfoAForm)getActionForm();

      renderCommonFormElements(embeddedChannelInfo.getType(), _edit);
      BindingFieldPropertyRenderer bfpr = renderFields(null, embeddedChannelInfo, _fields);

      // render embedded entitis
      bfpr = renderFields(bfpr, packagingInfo, _piFields, null, "packagingInfo_");
      bfpr = renderFields(bfpr, securityInfo,  _siFields, null, "securityInfo_");
      bfpr = renderFields(bfpr, commInfo,      _ciFields, null, "commInfo_");

      // render tab pane
      renderTabs(rContext, "generalTab", _tabs);

      // remove edit button
      Node editLink = getElementById("edit_button", true);
      removeNode(editLink, true);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering embeddedChannelInfo screen", t);
    }
  }
}