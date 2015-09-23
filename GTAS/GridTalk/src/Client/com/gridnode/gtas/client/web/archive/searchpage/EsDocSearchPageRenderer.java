package com.gridnode.gtas.client.web.archive.searchpage;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.model.dbarchive.searchpage.IFieldValueCollection;
import com.gridnode.pdip.framework.util.AssertUtil;

import org.w3c.dom.*;
import org.apache.struts.action.*;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

public class EsDocSearchPageRenderer extends AbstractRenderer
{
	//Regina Zeng 13 Oct 2006--Change "save" icon to "search" icon and Remove "Cancel" button
  public static final String SEARCH_IMAGE_SCR = "images/actions/view.gif";

	private boolean _edit;
	protected static final Number[] _fields = 
		         new Number[]
	                      {
													IGTSearchEsDocDocumentEntity.PARTNER_ID,
													IGTSearchEsDocDocumentEntity.PARTNER_NAME,
													IGTSearchEsDocDocumentEntity.FOLDER,
													IGTSearchEsDocDocumentEntity.DOC_TYPE,
													IGTSearchEsDocDocumentEntity.USER_TRACKING_ID,
													IGTSearchEsDocDocumentEntity.FROM_CREATE_DATE,
													IGTSearchEsDocDocumentEntity.FROM_CREATE_DATE_HOUR,
													IGTSearchEsDocDocumentEntity.TO_CREATE_DATE,
													IGTSearchEsDocDocumentEntity.TO_CREATE_DATE_HOUR,
													IGTSearchEsDocDocumentEntity.FROM_SENT_DATE,
													IGTSearchEsDocDocumentEntity.FROM_SENT_DATE_HOUR,
													IGTSearchEsDocDocumentEntity.TO_SENT_DATE,
													IGTSearchEsDocDocumentEntity.TO_SENT_DATE_HOUR,
													IGTSearchEsDocDocumentEntity.FROM_RECEIVED_DATE,
													IGTSearchEsDocDocumentEntity.FROM_RECEIVED_DATE_HOUR,
													IGTSearchEsDocDocumentEntity.TO_RECEIVED_DATE,
													IGTSearchEsDocDocumentEntity.TO_RECEIVED_DATE_HOUR,
													IGTSearchEsDocDocumentEntity.DOC_NO,
													IGTSearchEsDocDocumentEntity.FROM_DOC_DATE,
													IGTSearchEsDocDocumentEntity.FROM_DOC_DATE_HOUR,
													IGTSearchEsDocDocumentEntity.TO_DOC_DATE,
													IGTSearchEsDocDocumentEntity.TO_DOC_DATE_HOUR,
                          IGTSearchEsDocDocumentEntity.REMARK,
													IGTSearchEsDocDocumentEntity.FORM_MSG
	                      };
	
	public EsDocSearchPageRenderer(ActionContext actContext, RenderingContext rContext,
	 															boolean edit)
	{
		super(rContext);
		_edit = edit;
    if(!_edit)
    {
      throw new UnsupportedOperationException("View mode not supported");
    }
	}


	public void render() throws RenderingException
	{
		includeJavaScript(IGlobals.JS_DATE_TIME_PICKER);
		try
		{
			IGTSearchEsDocDocumentEntity esDoc = (IGTSearchEsDocDocumentEntity)getEntity();
			RenderingContext rContext = getRenderingContext();
			BindingFieldPropertyRenderer bfpr = new BindingFieldPropertyRenderer(rContext);
			renderFields(bfpr, esDoc, _fields);
			renderCommonFormElements(IGTEntity.ENTITY_SEARCH_ES_DOC_DOCUMENT, _edit);
			renderDropDownControl();
      
      Element ok = getElementById("ok",false); // Convert the ok button to do search!
      if(ok != null)
      {
        replaceTextCarefully(ok,rContext.getResourceLookup().getMessage("searchEsPi.edit.ok"));
        Element okIcon = getElementById("ok_icon",false);
        if(okIcon != null)
        {
          //RZ - Changed icon from save to seach
          okIcon.setAttribute("src",SEARCH_IMAGE_SCR);
        }
      }
      
			removeNode("cancel_button");
		}
		catch(Exception ex)
		{
			throw new RenderingException("[EsDocSearchPageRenderer.render] Error in rendering searchEsDoc screen.", ex);
		}
	}

  //01122006 RZ: Made changes to handle action errors
	private void renderDropDownControl() throws RenderingException
	{
		try
		{
			FieldValueCollectionHelper helper = new FieldValueCollectionHelper(getRenderingContext());
      EsDocSearchPageAForm form = (EsDocSearchPageAForm)getActionForm();
      
			ArrayList docTypeArray = helper.getEntity(IFieldValueCollection.DOC_TYPE);
			renderDropDownList(docTypeArray, "docType_value", form.getDocType());
			
			ArrayList partnerIDArray = helper.getEntity(IFieldValueCollection.PARTNER_ID);
			renderDropDownList(partnerIDArray, "partnerID_value", form.getPartnerID());
			
      /* 
			FolderHelper folderHlp = new FolderHelper();
			ArrayList valueList = folderHlp.getValueArray();
			ArrayList labelList = folderHlp.getLabelArray();
			renderDropDownList(labelList,valueList,"folder_value");*/ 
		}
		catch (GTClientException e)
		{
			throw new RenderingException("Error Rendering drop down control ...", e);
		}
	}
	
	private void printArrayList(ArrayList ar)
	{
		for (int i = 0; i < ar.size(); i++)
		{
			log(i + ": " + ar.get(i));
		}
	}
	
  //01122006 RZ: Added form as a arguement to renderDropDownList()
	private void renderDropDownList(ArrayList ar, String elementId, String returnValue)
  {
    if(ar == null)
    {
      return;
    }
    
    Iterator it = ar.iterator();
    Element parent = getElementById(elementId);
    String value = null;
    while (it.hasNext())
    {
      value = (String)it.next();
      Element option = _target.createElement("option");
      
      if(value != null && value.equals(returnValue))
      {
        option.setAttribute("selected", "selected");
      }
      option.setAttribute("value", value);
      Text textNode = _target.createTextNode(value);
      option.appendChild(textNode);
      parent.appendChild(option);
    }
  }
	
	private void renderDropDownList(ArrayList labelArray, ArrayList valueArray, String elementId)
	{
		if(labelArray == null || valueArray == null)
		{
			return;
		}
		AssertUtil.assertTrue(labelArray.size() == valueArray.size());
		Element parent = getElementById(elementId);
		for (int i = 0; i < labelArray.size(); i++)
		{
			String value = (String) valueArray.get(i);
			String label = (String) labelArray.get(i);
			Element option = _target.createElement("option");
			option.setAttribute("value", value);
			Text textNode = _target.createTextNode(label);
			option.appendChild(textNode);
			parent.appendChild(option);
		}
	}

//	private IGTEntity getFieldValueCollectionEntity() throws GTClientException
//	{
//		IGTSession gtasSession = StaticWebUtils
//				.getGridTalkSession(getRenderingContext().getRequest());
//		IGTManager manager = gtasSession.getManager(IGTEntity.ENTITY_FIELD_VALUE_COLLECTION);
//		int uid = isPiSearch() ? 1 : 2;
//		return manager.getByUid(uid);
//	}
	
	private void log(String message)
	{
		com.gridnode.gtas.client.web.archive.helpers.Logger.log("[EsDocSearchPageRenderer] " + message);
	}

	/*
	 * An helper class to load the enum value for folder from field meta info
	 */
private class FolderHelper
	{
		private ArrayList _valueArray;
		private ArrayList _labelArray;
		
		public FolderHelper() throws RenderingException
		{
			try
			{
     
        
				RenderingContext rContext = getRenderingContext();
				HttpServletRequest request = rContext.getRequest();
				IGTSession gtasSession = StaticWebUtils.getGridTalkSession(request);
				IGTManager manager = gtasSession.getManager(IGTManager.MANAGER_ES_DOC);
	      IGTFieldMetaInfo fmi = manager.getSharedFieldMetaInfo(IGTEntity.ENTITY_ES_DOC, IGTEsDocEntity.FOLDER);
	      IEnumeratedConstraint ec = (IEnumeratedConstraint) fmi.getConstraint();
	      int size = ec.getSize();
	      _valueArray = new ArrayList(size);
	      for (int i = 0; i < size; i++)
	      {
	      	_valueArray.add(ec.getValue(i));
	      }
	      _labelArray = new ArrayList(size);
	      
	      com.gridnode.gtas.client.web.renderers.ISimpleResourceLookup rLookup = rContext.getResourceLookup();
	      for (int i = 0; i < size; i++)
	      {
	      	String label = ec.getLabel(i);
	      	String message = rLookup.getMessage(label);
	      	_labelArray.add(message);
	      }
			} catch (GTClientException e)
			{
				throw new RenderingException(e);
			}
		}
		
		public ArrayList getValueArray()
		{
			AssertUtil.assertTrue(_valueArray != null);
			return _valueArray;
		}
		
		public ArrayList getLabelArray()
		{
			AssertUtil.assertTrue(_labelArray != null);
			return _labelArray;
		}
	}
}
