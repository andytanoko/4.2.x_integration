package com.gridnode.gtas.client.web.archive.searchpage;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTFieldValueCollectionEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.pdip.framework.util.AssertUtil;

public class FieldValueCollectionHelper
{
	private IGTManager manager;
	
	public FieldValueCollectionHelper(RenderingContext rContext) throws GTClientException
	{
		HttpServletRequest request = rContext.getRequest();
		IGTSession gtasSession = StaticWebUtils.getGridTalkSession(request);
		manager = gtasSession.getManager(IGTEntity.ENTITY_FIELD_VALUE_COLLECTION);
	}
	
	public ArrayList getEntity(Long uid) throws GTClientException
	{
		IGTEntity entity = manager.getByUid(uid);
		return (ArrayList) entity.getFieldValue(IGTFieldValueCollectionEntity.VALUE);
	}
}
