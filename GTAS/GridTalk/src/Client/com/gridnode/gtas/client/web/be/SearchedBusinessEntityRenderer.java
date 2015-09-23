/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchedBusinessEntityRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-09-15     Daniel D'Cotta      Created
 * 2003-10-03     Neo Sok Lay         Create Integer on beState instead of
 *                                    of Short. Otherwise comparison with
 *                                    STATE_MY_BE (Integer) will fail.
 *                                    Add UUID for rendering.
 * 2003-09-30     Daniel D'Cotta      Render channels as a Elv
 * 2003-10-10     Neo Sok Lay         Change to use IGTSearchedChannelInfoEntity
 *                                    instead of IGTChannelInfoEntity.
 */
package com.gridnode.gtas.client.web.be;

import java.util.Collection;

import org.apache.struts.action.ActionForward;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;

public class SearchedBusinessEntityRenderer extends AbstractRenderer
{
  private static final String CONFIGURE_IMAGE_SRC = "images/actions/configure.gif";

  private boolean _edit;

  protected final static Number[] _fields = { IGTSearchedBusinessEntityEntity.UUID,
                                              IGTSearchedBusinessEntityEntity.ID,
                                              IGTSearchedBusinessEntityEntity.ENTERPRISE_ID,
                                              IGTSearchedBusinessEntityEntity.DESCRIPTION,
                                              IGTSearchedBusinessEntityEntity.BE_STATE, };

  protected static final Number[] wpFields = {  IGTWhitePageEntity.ADDRESS,
                                                IGTWhitePageEntity.BUSINESS_DESC,
                                                IGTWhitePageEntity.CITY,
                                                IGTWhitePageEntity.CONTACT_PERSON,
                                                IGTWhitePageEntity.COUNTRY,
                                                IGTWhitePageEntity.DUNS,
                                                IGTWhitePageEntity.EMAIL,
                                                IGTWhitePageEntity.FAX,
                                                IGTWhitePageEntity.G_SUPPLY_CHAIN_CODE,
                                                IGTWhitePageEntity.LANGUAGE,
                                                IGTWhitePageEntity.POSTCODE,
                                                IGTWhitePageEntity.PO_BOX,
                                                IGTWhitePageEntity.STATE,
                                                IGTWhitePageEntity.TEL,
                                                IGTWhitePageEntity.WEBSITE, };

  private final static Object[] _channelColumns = { IGTSearchedChannelInfoEntity.NAME,
                                                    IGTSearchedChannelInfoEntity.DESCRIPTION,
                                                    IGTSearchedChannelInfoEntity.REF_ID, };

  public static final ITabDef[] _tabs = {
    new TabDef("searchedBusinessEntity.tabs.searchedBusinessEntity",  "searchedBusinessEntity_tab"),
    new TabDef("searchedBusinessEntity.tabs.whitePage",               "whitePage_tab"),
  };

  public SearchedBusinessEntityRenderer( RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();

      IGTSearchedBusinessEntityEntity searchedBusinessEntity = (IGTSearchedBusinessEntityEntity)getEntity();
      IGTWhitePageEntity whitePage = (IGTWhitePageEntity)searchedBusinessEntity.getFieldValue(IGTSearchedBusinessEntityEntity.WHITE_PAGE);
      //SearchedBusinessEntityAForm beForm = (SearchedBusinessEntityAForm)getActionForm();

      renderCommonFormElements(searchedBusinessEntity.getType(), _edit);
      BindingFieldPropertyRenderer bfpr = renderFields(null, searchedBusinessEntity, _fields);

      // render channels
      Collection channels = (Collection)searchedBusinessEntity.getFieldValue(IGTSearchedBusinessEntityEntity.CHANNELS);
      //String[] channelNames = new String[channels.size()];
      //int j = 0;
      //Iterator i = channels.iterator();
      //while(i.hasNext())
      //{
      //  IGTChannelInfoEntity channel = (IGTChannelInfoEntity)i.next();
      //  channelNames[j++] = channel.getFieldString(channel.NAME);
      //}
      //Element channelElement = getElementById("channels_value");
      //removeAllChildren(channelElement);
      //renderListItems(channelElement, channelNames);

      if(channels != null) // 20030930 DDJ: Render channels as a Elv
      {
        renderFieldElv(rContext, searchedBusinessEntity, IGTSearchedBusinessEntityEntity.CHANNELS, channels, _channelColumns);
      }

      // render embeded white page entity
      bfpr = renderFields(bfpr, whitePage, wpFields);

      // render tab pane
      renderTabs(rContext, "searchedBusinessEntityTab", _tabs);

      Node editLink = getElementById("edit_button", true);

      // insert configure button
      Integer beState = new Integer(searchedBusinessEntity.getFieldString(IGTSearchedBusinessEntityEntity.BE_STATE));
      if(!IGTSearchedBusinessEntityEntity.STATE_MY_BE.equals(beState))
      {
        OperationContext opCon = rContext.getOperationContext();
        Long searchId = (Long)opCon.getAttribute(SearchRegistryQueryDispatchAction.OPCON_ATTRIB_SEARCH_ID);
        String uuid = (String)opCon.getAttribute(SearchRegistryQueryDispatchAction.OPCON_ATTRIB_UUID);
        ActionForward configureForward = rContext.getMapping().findForward("configure");
        if(configureForward == null) throw new NullPointerException("configureForward is null");
        String configureUrl = configureForward.getPath();
        configureUrl = StaticWebUtils.addParameterToURL(configureUrl, "searchId", searchId.toString());
        configureUrl = StaticWebUtils.addParameterToURL(configureUrl, "uuid", uuid);
        configureUrl = rContext.getUrlRewriter().rewriteURL(configureUrl, false);
        Element configureLink = createButtonLink( "configure",
                                                  "searchedBusinessEntity.view.configure",
                                                  CONFIGURE_IMAGE_SRC,
                                                  "jalan('" + configureUrl + "');",
                                                  true);

        editLink.getParentNode().insertBefore(configureLink, editLink);
      }

      // remove edit button
      removeNode(editLink, true);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering searchedBusinessEntity screen",t);
    }
  }

  private void renderFieldElv(RenderingContext rContext,
                              IGTEntity entity,
                              Number fieldId,
                              Collection value,
                              Object[] columns)
    throws RenderingException
  {
    try
    {
      IGTSession gtasSession = StaticWebUtils.getGridTalkSession(rContext.getRequest());
      IGTFieldMetaInfo fmi = entity.getFieldMetaInfo(fieldId);
      String fieldName = fmi.getFieldName();
      IEntityConstraint constraint = (IEntityConstraint)fmi.getConstraint();
      String entityType = constraint.getEntityType();
      IGTManager manager = gtasSession.getManager(entityType);
      ColumnEntityAdapter adapter = new ColumnEntityAdapter(columns, manager, entityType);

      ListViewOptionsImpl listOptions = new ListViewOptionsImpl();
      listOptions.setColumnAdapter(adapter);
      listOptions.setCreateURL(null);
      listOptions.setDeleteURL(null);
      listOptions.setAllowsEdit(false);
      listOptions.setAllowsSelection(false);
      listOptions.setHeadingLabelKey(entity.getType() + "." + fieldName);
      listOptions.setViewURL("divertViewEmbeddedChannelInfo");
      listOptions.setUpdateURL(null);

      ElvRenderer elvRenderer = new ElvRenderer(rContext,
                                                fieldName + "_details",
                                                listOptions,
                                                value);
      EmbeddedChannelInfoListRenderer ecilr = new EmbeddedChannelInfoListRenderer(rContext);
      elvRenderer.setRenderer(ecilr);
      elvRenderer.setEmbeddedList(true);
      elvRenderer.setAllowsOrdering(false);
      elvRenderer.setIndexModeDefault(true);
      elvRenderer.setTableName(fieldName);
      elvRenderer.render(_target);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Unable to render the elv for fieldId" + fieldId, t);
    }
  }
}