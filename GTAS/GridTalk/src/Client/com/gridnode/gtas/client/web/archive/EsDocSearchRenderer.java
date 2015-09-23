package com.gridnode.gtas.client.web.archive;

import java.util.TimeZone;

import com.gridnode.gtas.client.ctrl.IGTSearchEsDocDocumentEntity;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.archive.helpers.DateTimeHelper;
import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.web.xml.BadDocumentException;
import com.gridnode.gtas.events.dbarchive.*;
import com.gridnode.gtas.client.web.xml.*;

import org.w3c.dom.*;

/**
 * Renderer the search RESULT page for es document
 */
public class EsDocSearchRenderer extends AbstractRenderer
{
	private EsDocSearchQuery searchQuery;
	private TimeZone tz;
	
	private final String DATE_TYPE_FROM = "from";
	private final String DATE_TYPE_TO = "to";
	private final String DATE_FORMAT = "yyyy-MM-dd";
	private final String TIME_FORMAT = "HH:mm";
	
  public EsDocSearchRenderer(RenderingContext rContext, EsDocSearchQuery searchQuery,
  		                       TimeZone tz)
  {
    super(rContext);
    this.searchQuery = searchQuery;
    this.tz = tz;
  }
  
  public void render() throws RenderingException
  {
  	try
  	{
  		includeJavaScript(IGlobals.JS_DATE_TIME_PICKER);
	  	RenderingContext rContext = getRenderingContext();
	  	Document document = rContext.getDocumentManager().getDocument(IDocumentKeys.ES_DOC_SEARCH_QUERY, true);
	  	Node sourceNode = getElementByAttributeValue("searchQueryTable", "id", document);
	  	Node targetNode = getElementById("listview_table");
	  	
	  	sourceNode = sourceNode.cloneNode(true);
      sourceNode = _target.importNode(sourceNode,true);
      
	  	targetNode.getParentNode().insertBefore(sourceNode, targetNode);
	  	
	  	//render the label for the criteria fields
	  	renderLabel("partnerID_label", "searchEsDoc.lv.partnerID");
	  	renderLabel("partnerName_label", "searchEsDoc.lv.partnerName");
	  	renderLabel("folder_label", "searchEsDoc.lv.folder");
	  	renderLabel("docType_label", "searchEsDoc.lv.docType");
	  	renderLabel("userTrackingID_label", "searchEsDoc.lv.userTrackingID");
	  	renderLabel("fromCreateDate_label", "searchEsDoc.lv.fromCreateDate");
	  	renderLabel("toCreateDate_label", "searchEsDoc.lv.toCreateDate");
	  	renderLabel("fromSentDate_label", "searchEsDoc.lv.fromSentDate");
	  	renderLabel("toSentDate_label", "searchEsDoc.lv.toSentDate");
	  	renderLabel("fromReceivedDate_label", "searchEsDoc.lv.fromReceivedDate");
	  	renderLabel("toReceivedDate_label", "searchEsDoc.lv.toReceivedDate");
	  	renderLabel("docNo_label", "searchEsDoc.lv.docNo");
	  	renderLabel("fromDocDate_label", "searchEsDoc.lv.fromDocDate");
	  	renderLabel("toDocDate_label", "searchEsDoc.lv.toDocDate");
      renderLabel("remark_label","searchEsDoc.lv.remark");
	  	//end render
	  	
	  	replaceText("docNo_value", searchQuery.getDocNo(), true);
	  	replaceText("docType_value", searchQuery.getDocType(), true);
	  	replaceText("partnerID_value", searchQuery.getPartnerId(), true);
	  	replaceText("partnerName_value", searchQuery.getPartnerName(), true);
      replaceText("remark_value",searchQuery.getRemark(), true);
	  	
	  	replaceText("folder_value", searchQuery.getFolder(), true);
	  	
	  	String fromCreateDate = getDateTimeInUsrTZ(searchQuery.getFromCreateDate(), searchQuery.getFromCreateDateTime(),
	  			                                       tz, DATE_TYPE_FROM);
	  	String toCreateDate = getDateTimeInUsrTZ(searchQuery.getToCreateDate(), searchQuery.getToCreateDateTime(),
	  			                                       tz, DATE_TYPE_TO);
	  	String fromSentDate = getDateTimeInUsrTZ(searchQuery.getDocDateSentFrom(), searchQuery.getFromSentDateTime(),
                                                 tz, DATE_TYPE_FROM);
	  	String toSentDate = getDateTimeInUsrTZ(searchQuery.getDocDateSentTo(), searchQuery.getToSentDateTime(),
                                                 tz, DATE_TYPE_TO);
	  	String fromReceivedDate = getDateTimeInUsrTZ(searchQuery.getDocDateReceivedFrom(), searchQuery.getFromReceivedDateTime(),
                                                 tz, DATE_TYPE_FROM);
	  	String toReceivedDate = getDateTimeInUsrTZ(searchQuery.getDocDateReceivedTo(), searchQuery.getToReceivedDateTime(),
                                                 tz, DATE_TYPE_TO);
	  	String fromDocDate = getDateTimeInUsrTZ(searchQuery.getDocDateFrom(), searchQuery.getFromDocDateTime(),
                                                 tz, DATE_TYPE_FROM);
	  	String toDocDate = getDateTimeInUsrTZ(searchQuery.getDocDateTo(), searchQuery.getToDocDateTime(),
                                                 tz, DATE_TYPE_TO);
	  	
	  	replaceText("fromCreateDate_value", fromCreateDate, true);
	  	replaceText("toCreateDate_value", toCreateDate, true);
	  	replaceText("fromSentDate_value", fromSentDate, true);
	  	replaceText("toSentDate_value", toSentDate, true);
	  	replaceText("fromReceivedDate_value", fromReceivedDate, true);
	  	replaceText("toReceivedDate_value", toReceivedDate, true);
	  	replaceText("fromDocDate_value", fromDocDate, true);
	  	replaceText("toDocDate_value", toDocDate, true);
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
  private String getDefaultTime(String time, String dateType)
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
  
  private String getDateTimeInUsrTZ(String date, String time, TimeZone tz,
  		                              String dateType)
  {
  	time = getDefaultTime(time, dateType);
  	return DateTimeHelper.convertDateTimeInTimeZone(date, time, DATE_FORMAT+TIME_FORMAT, tz,"");
  }
}
