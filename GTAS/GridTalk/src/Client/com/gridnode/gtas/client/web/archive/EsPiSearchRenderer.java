package com.gridnode.gtas.client.web.archive;

import org.w3c.dom.*;

import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IEnumeratedConstraint;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTEsPiEntity;
import com.gridnode.gtas.client.ctrl.IGTFieldMetaInfo;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.archive.helpers.DateTimeHelper;
import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.web.xml.BadDocumentException;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;
import com.gridnode.gtas.events.dbarchive.*;
import com.gridnode.pdip.framework.util.AssertUtil;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

/**
 * This is the renderer for rendering the search RESULT page of estore process instance
 * 
 */
public class EsPiSearchRenderer extends AbstractRenderer
{
	private EsPiSearchQuery searchQuery;
	private TimeZone userSelectTZ;
	
	private static final String DATE_TYPE_FROM = "from";
	private static final String DATE_TYPE_TO = "to";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String TIME_FORMAT = "HH:mm";
	
  public EsPiSearchRenderer(RenderingContext rContext, EsPiSearchQuery searchQuery, TimeZone userSelectTZ)
  {
    super(rContext);
    this.searchQuery = searchQuery;
    this.userSelectTZ = userSelectTZ;
  }
  
  public void render() throws RenderingException
  {
  	includeJavaScript(IGlobals.JS_DATE_TIME_PICKER);
  	try
  	{
	  	RenderingContext rContext = getRenderingContext();
	  	Document document = rContext.getDocumentManager().getDocument(IDocumentKeys.ES_PI_SEARCH_QUERY, true);
	  	Node sourceNode = getElementByAttributeValue("searchQueryTable", "id", document);
	  	Node targetNode = getElementById("listview_table");
	  	
	  	sourceNode = sourceNode.cloneNode(true);
      sourceNode = _target.importNode(sourceNode,true);
      
	  	targetNode.getParentNode().insertBefore(sourceNode, targetNode);
	  	
	  	//render the criteria label
	  	renderLabel("process_label", "searchPi.lv.processDef");
	  	renderLabel("processState_label", "searchPi.lv.processState");
	  	renderLabel("partnerID_label","searchPi.lv.partnerID");
	  	renderLabel("partnerName_label", "searchPi.lv.partnerName");
	  	renderLabel("fromStartTime_label", "searchPi.lv.fromStartTime");
	  	renderLabel("toStartTime_label", "searchPi.lv.toStartTime");
	  	renderLabel("docNo_label", "searchPi.lv.docNo");
	  	renderLabel("fromDocDate_label", "searchPi.lv.fromDocDate");
	  	renderLabel("toDocDate_label", "searchPi.lv.toDocDate");
      renderLabel("remark_label", "searchPi.lv.remark");
      
	  	String processStateLabel = getProcessStateLabel(searchQuery.getProcessState());
	  	replaceText("docNo_value", searchQuery.getDocNo(), true);
	  	replaceText("process_value", searchQuery.getProcess(), true);
	  	replaceText("processState_value", processStateLabel, true);
	  	replaceText("partnerID_value", searchQuery.getPartnerId(), true);
	  	replaceText("partnerName_value", searchQuery.getPartnerName(), true);
	  	replaceText("fromDocDate_value", searchQuery.getDocDateFrom(), true);
	  	replaceText("toDocDate_value", searchQuery.getDocDateTo(), true);
      replaceText("remark_value", searchQuery.getRemark(), true);
	  	
	  	//TWX 24 Mar 2006 Added in new search criteria--Process From Start Time, To Start Time
	  	String startFDate = searchQuery.getProcessStartFromDate();
	  	String startFTime = getDefaultTime(searchQuery.getProcessStartFromTime(),DATE_TYPE_FROM);
	  	
	  	String startTDate = searchQuery.getProcessStartToDate();
	  	String startTTime = getDefaultTime(searchQuery.getProcessStartToTime(), DATE_TYPE_TO);
	  	
	  	String localFromStartTime = DateTimeHelper.convertDateTimeInTimeZone(startFDate, startFTime,DATE_FORMAT+TIME_FORMAT,
	  			                                                                 userSelectTZ,"");
	  	String localToStartTime = DateTimeHelper.convertDateTimeInTimeZone(startTDate, startTTime,DATE_FORMAT+TIME_FORMAT,
	  			                                                                 userSelectTZ, "");
	  	
	  	replaceText("fromStartTime_value",localFromStartTime, true);
	  	replaceText("toStartTime_value",localToStartTime, true);
      
	  	//Regina Zeng 19 Oct 2006: Addded user tracking ID
      renderLabel("userTrackingID_label", "searchPi.lv.userTrackingID");
      replaceText("userTrackingID_value", searchQuery.getUserTrackingID(), true);  
  	} catch (BadDocumentException e)
  	{
  		throw new RenderingException(e);
  	}
  }
  
  /**
   * Acquire the default time(00:00 or 23:59) if the time we input is empty string or null
   * @param time
   * @param dateType
   * @return
   */
  private static String getDefaultTime(String time, String dateType)
	{
		if(time != null && ! "".equals(time))
		{
			return time;
		}
		else if(DATE_TYPE_FROM.equals(dateType))
		{
			return "00:00";
		}
		else if(DATE_TYPE_TO.equals(dateType))
		{
			return "23:59";
		}
		else
		{
			throw new IllegalArgumentException("[EsPiSearchRenderer.getDefaultTime] dateType "+ dateType+" is not supported.");
		}
	}

  private void log(String message)
	{
		com.gridnode.gtas.client.web.archive.helpers.Logger.debug("[EsPiSearchRenderer] " + message);
	}
  
  private String getProcessStateLabel(String value) throws RenderingException
  {
  	try
  	{
  		AssertUtil.assertTrue(value != null);
	  	RenderingContext rContext = getRenderingContext();
			HttpServletRequest request = rContext.getRequest();
			IGTSession gtasSession = StaticWebUtils.getGridTalkSession(request);
			IGTManager manager = gtasSession.getManager(IGTManager.MANAGER_ES_PI);
	    IGTFieldMetaInfo fmi = manager.getSharedFieldMetaInfo(IGTEntity.ENTITY_ES_PI, IGTEsPiEntity.PROCESS_STATE);
	    IEnumeratedConstraint ec = (IEnumeratedConstraint) fmi.getConstraint();
	    String label = ec.getLabel(value);
	    com.gridnode.gtas.client.web.renderers.ISimpleResourceLookup rLookup = rContext.getResourceLookup();
	    return rLookup.getMessage(label);
  	} catch (GTClientException e)
  	{
  		throw new RenderingException(e);
  	}
  }
}
