package com.gridnode.gtas.client.web.archive.searchpage;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.model.dbarchive.searchpage.IFieldValueCollection;

import org.w3c.dom.*;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.gridnode.pdip.framework.util.AssertUtil;

/**
 * This is the renderer for rendering the estore search criteria page 
 * 
 */
public class EsPiSearchPageRenderer extends AbstractRenderer
{
	private boolean _edit;
	//private ActionMapping _actionMapping;
	public static final String SEARCH_IMAGE_SCR = "images/actions/view.gif";
	
	/*
	public static final Number[] PDC_FIELDS =Z {
		IGTFieldValueCollectionEntity.VALUE
	};*/
	
	protected static final Number[] _fields = new Number[]
  {
		IGTSearchEsPiDocumentEntity.PROCESS_DEF,
		IGTSearchEsPiDocumentEntity.PROCESS_STATE,
		IGTSearchEsPiDocumentEntity.PARTNER_ID,
		IGTSearchEsPiDocumentEntity.PARTNER_NAME,
		IGTSearchEsPiDocumentEntity.PROCESS_FROM_START_TIME,
		IGTSearchEsPiDocumentEntity.FROM_ST_HOUR,
		IGTSearchEsPiDocumentEntity.PROCESS_TO_START_TIME,
		IGTSearchEsPiDocumentEntity.TO_ST_HOUR,
		IGTSearchEsPiDocumentEntity.DOC_NO,    
		IGTSearchEsPiDocumentEntity.FROM_DOC_DATE,
		IGTSearchEsPiDocumentEntity.TO_DOC_DATE,
		IGTSearchEsPiDocumentEntity.FORM_MSG,
    
    //Regina Zeng 12 Oct 2006--Add user tracking ID
    IGTSearchEsPiDocumentEntity.USER_TRACKING_ID,
    IGTSearchEsPiDocumentEntity.REMARK
	};
	
	//private ActionMapping actionMapping;

	public EsPiSearchPageRenderer(ActionContext actContext, RenderingContext rContext, boolean edit)
	{
		super(rContext);
		_edit = edit;
		//_actionMapping = actContext.getMapping();
    if(!_edit)
    {
      throw new UnsupportedOperationException("View mode not supported");
    }
	}


	public void render() throws RenderingException
	{
		try
		{
			log("EsPiSearchPageRenderer start rendering ...");
			IGTSearchEsPiDocumentEntity searchEsPiDocument = (IGTSearchEsPiDocumentEntity)getEntity();
			RenderingContext rContext = getRenderingContext();
			BindingFieldPropertyRenderer bfpr = new BindingFieldPropertyRenderer(rContext);
			
			includeJavaScript(IGlobals.JS_DATE_TIME_PICKER);
			
			renderCommonFormElements(IGTEntity.ENTITY_SEARCH_ES_PI_DOCUMENT, _edit);
			//renderLabel("ok_icon","searchEsPi.edit.ok");
			renderFields(bfpr, searchEsPiDocument, _fields);
      
      Element ok = getElementById("ok",false); // Convert the ok button to do search!
      if(ok != null)
      {
        replaceTextCarefully(ok,rContext.getResourceLookup().getMessage("searchEsPi.edit.ok"));
        Element okIcon = getElementById("ok_icon",false);
        if(okIcon != null)
        {
          //Oct 2006 RZ: Changed icon from save to seach
          okIcon.setAttribute("src",SEARCH_IMAGE_SCR);
        }
      }
      
			renderDropDownControl();
      
      //Regina Zeng 13 October 2006--Remove "Cancel" button
      removeNode("cancel_button");
    }
		catch(Exception ex)
		{
			throw new RenderingException("Error in rendering the searchEsPiDocument screen.", ex);
		}
	}
	
  /*
	private String getPath(String forwardName)
	{
		ActionForward forward = _actionMapping.findForward(forwardName);
		return forward.getPath();
	} */
	
  //01122006 RZ: Made changes to handle action errors
	private void renderDropDownControl() throws RenderingException
	{
		try
		{
			FieldValueCollectionHelper helper = new FieldValueCollectionHelper(getRenderingContext());
      EsPiSearchPageAForm form = (EsPiSearchPageAForm)getActionForm();
      
			ArrayList processArray = helper.getEntity(IFieldValueCollection.PROCESS_DEF);
			renderDropDownList(processArray, "processDef_value", form.getProcessDef()); //render processDef
			
			ArrayList partnerIDs = helper.getEntity(IFieldValueCollection.PARTNER_ID);
			renderDropDownList(partnerIDs, "partnerID_value", form.getPartnerID());
			
			ProcessStateHelper processStateHelper = new ProcessStateHelper();
			ArrayList labelArray = processStateHelper.getLabelArray();
			ArrayList valueArray = processStateHelper.getValueArray();
			
			renderDropDownList(labelArray, valueArray, "processState_value", form.getProcessState());
		}
		catch (GTClientException e)
		{
			throw new RenderingException(e);
		}
	}
	
	//TEST
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
	
  //(Older version) 01122006 RZ: Added form as a arguement to renderDropDownList()
	private void renderDropDownList(ArrayList labelArray, ArrayList valueArray, String elementId, String returnValue)
	{
		if(labelArray == null || valueArray == null)
		{
			return;
		}
		AssertUtil.assertTrue(labelArray.size() == valueArray.size());
		Element parent = getElementById(elementId);
    if(returnValue!=null && !"".equals(returnValue))
    {
      int valueIndex = Integer.parseInt(returnValue);
      
      String value = null;
      String label = null;
      for (int i = 0; i < labelArray.size(); i++)
      {
        Element option = _target.createElement("option");
        if(valueIndex==0)
        {  
          value = (String)valueArray.get(2);
          label = (String)labelArray.get(2);
          option.setAttribute("selected", "selected");
          valueArray.remove(2);
          labelArray.remove(2);
          valueIndex=-1;
          i--;
        }
        else if (valueIndex==1)
        {
          value = (String)valueArray.get(5);
          label = (String)labelArray.get(5);
          option.setAttribute("selected", "selected");
          valueArray.remove(5);
          labelArray.remove(5);
          valueIndex=-1;
          i--;
        }
        else if (valueIndex==2)
        {  
          value = (String)valueArray.get(4);
          label = (String)labelArray.get(4);
          option.setAttribute("selected", "selected");
          valueArray.remove(4);
          labelArray.remove(4);
          valueIndex=-1;
          i--;
        }
        else if(valueIndex==3)
        {
          value = (String)valueArray.get(1);
          label = (String)labelArray.get(1);
          option.setAttribute("selected", "selected");
          valueArray.remove(1);
          labelArray.remove(1);
          valueIndex=-1;
          i--;
        }
        else if(valueIndex==4)
        {
          value = (String)valueArray.get(6);
          label = (String)labelArray.get(6);
          option.setAttribute("selected", "selected");
          valueArray.remove(6);
          labelArray.remove(6);
          valueIndex=-1;
          i--;
        }
        else if(valueIndex==5)
        {
          value = (String)valueArray.get(3);
          label = (String)labelArray.get(3);
          option.setAttribute("selected", "selected");
          valueArray.remove(3);
          labelArray.remove(3);
          valueIndex=-1;
          i--;
        }
        else if(valueIndex==6)
        {
          value = (String)valueArray.get(0);
          label = (String)labelArray.get(0);
          option.setAttribute("selected", "selected");
          valueArray.remove(0);
          labelArray.remove(0);
          valueIndex=-1;
          i--;
        }
        else
        {
          value = (String)valueArray.get(i);
          label = (String)labelArray.get(i);
        }       
        
        option.setAttribute("value", value);
        Text textNode = _target.createTextNode(label);
        option.appendChild(textNode);
        parent.appendChild(option);
      }
    }
    else
    {
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
	}
	
	// old version
//	private void renderDropDownList(ArrayList ar, String elementId)
//	{
//		Iterator it = ar.iterator();
//		Element parent = getElementById(elementId);
//		while (it.hasNext())
//		{
//			String value = (String) it.next();
//			Element option = _target.createElement("option");
//			option.setAttribute("value", value);
//			Text textNode = _target.createTextNode(value);
//			option.appendChild(textNode);
//			parent.appendChild(option);
//		}
//	}


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
		com.gridnode.gtas.client.web.archive.helpers.Logger.log("[EsPiSearchPageRenderer] " + message);
	}
	
	private class ProcessStateHelper
	{
		private ArrayList valueArray;
		private ArrayList labelArray;
		
		public ProcessStateHelper() throws RenderingException
		{
			try
			{
				RenderingContext rContext = getRenderingContext();
				HttpServletRequest request = rContext.getRequest();
				IGTSession gtasSession = StaticWebUtils.getGridTalkSession(request);
				IGTManager manager = gtasSession.getManager(IGTManager.MANAGER_ES_PI);
	      IGTFieldMetaInfo fmi = manager.getSharedFieldMetaInfo(IGTEntity.ENTITY_ES_PI, IGTEsPiEntity.PROCESS_STATE);
	      IEnumeratedConstraint ec = (IEnumeratedConstraint) fmi.getConstraint();
	      int size = ec.getSize();
	      valueArray = new ArrayList(size);
	      for (int i = 0; i < size; i++)
	      {
	      	valueArray.add(ec.getValue(i));
	      }
	      labelArray = new ArrayList(size);
	      
	      com.gridnode.gtas.client.web.renderers.ISimpleResourceLookup rLookup = rContext.getResourceLookup();
	      for (int i = 0; i < size; i++)
	      {
	      	String label = ec.getLabel(i);
	      	String message = rLookup.getMessage(label);
	      	labelArray.add(message);
	      }
			} catch (GTClientException e)
			{
				throw new RenderingException(e);
			}
		}
		
		public ArrayList getValueArray()
		{
			AssertUtil.assertTrue(valueArray != null);
			return valueArray;
		}
		
		public ArrayList getLabelArray()
		{
			AssertUtil.assertTrue(labelArray != null);
			return labelArray;
		}
	}
}
