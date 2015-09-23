package com.gridnode.gtas.client.web.archive.doc.temp;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTEsPiEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.web.xml.BadDocumentException;
import com.gridnode.gtas.client.web.xml.*;
import com.gridnode.gtas.client.web.archive.helpers.*;
import com.gridnode.gtas.client.web.archive.docforpi.*;

import org.w3c.dom.*;

public class EsPiDetailRenderer extends AbstractRenderer
{
	public static final boolean CLONE_REQUIRED = true;
	public static final String ID_VALUE = "EsPiDetail_table";
	
	/**
	 * Uid of ProcessInstanceMetaInfo.
	 */
	private Long uid;
	
  public EsPiDetailRenderer(RenderingContext rContext, Long uid)
  {
    super(rContext);
    this.uid = uid;
  }
  
  public void render() throws RenderingException
  {
  	try
  	{
	  	RenderingContext rContext = getRenderingContext();
	  	Document document = rContext.getDocumentManager().getDocument(IDocumentKeys.ES_PI_DETAIL, CLONE_REQUIRED);
	  	Node sourceNode = getElementByAttributeValue(ID_VALUE, "id", document);
	  	Node targetNode = getElementById("listview_table");
	  	sourceNode = sourceNode.cloneNode(true);
      sourceNode = _target.importNode(sourceNode,true);
	  	targetNode.getParentNode().insertBefore(sourceNode, targetNode);
      BindingFieldPropertyRenderer bfpr = new BindingFieldPropertyRenderer(getRenderingContext());
      
      /*
      //Regina Zeng 20 Oct 2006--Render the criteria label
      IGTEsPiEntity detailEntity = getEsPiEntity();
      replaceText("docDate_value", detailEntity.getFieldString(IGTEsPiEntity.DOCUMENT_DATE), true);
      replaceText("processInstanceRemark_value", detailEntity.getFieldString(IGTEsPiEntity.REMARK), true);
      */
      
	  	renderTextUsingBFPR(bfpr); //the framework itself will auto add the CSS class 'fieldlabel' into the field which display as bold
	  	                         //maybe set the class field to " " ? 
      
      //01122006 RZ: Hardcoding at this part, override the onload attribute to disable the pop up
      Element xhtmlBody = getElementById("xhtml_body", false);
      if(xhtmlBody!=null)
      {
        xhtmlBody.setAttribute("onload", " void ieTableHeaderStyleHack('listview_form');");
      }
  	} catch (BadDocumentException e)
  	{
  		throw new RenderingException(e);
  	} catch (GTClientException e)
  	{
  		throw new RenderingException(e);
  	}
  }
  
  //TEST
  private void log(String message)
  {
  	Logger.debug("[EsPiDetailRenderer]" + message);
  }
  
  private IGTEsPiEntity getEsPiEntity() throws GTClientException
  {
  	IGTSession gtasSession = StaticWebUtils.getGridTalkSession(getRenderingContext().getRequest());
  	IGTManager manager = gtasSession.getManager(IGTEntity.ENTITY_ES_PI);
  	return (IGTEsPiEntity) manager.getByUid(uid);
  }  
  
  //TEST
  //Regina Zeng 19 Oct 2006--Add doc date and process instance remark
  private void renderTextUsingBFPR(BindingFieldPropertyRenderer bfpr) throws RenderingException, GTClientException
  {  	
  	Number[] fields = 
  	{
  			IGTEsPiEntity.PROCESS_INSTANCE_ID,
  			IGTEsPiEntity.DOCUMENT_NUMBER,
  			IGTEsPiEntity.PARTNER_ID,
  			IGTEsPiEntity.PROCESS_STATE,
  			IGTEsPiEntity.PROCESS_START_DATE,
  			IGTEsPiEntity.PROCESS_END_DATE,
        IGTEsPiEntity.USER_TRACKING_ID,
        IGTEsPiEntity.DOCUMENT_DATE,
        IGTEsPiEntity.REMARK,
        IGTEsPiEntity.FAILED_REASON,
        IGTEsPiEntity.DETAIL_REASON,
        IGTEsPiEntity.RETRY_NUMBER
  	};
  	IGTEsPiEntity entity = getEsPiEntity();
  	EsPiAForm form = getEsPiAForm(entity);
  	renderFields(bfpr, entity, fields, form, "");
    
    if(! form.isProcessInstanceFailed())
    {
      removeNode("failedReason_detail");
      removeNode("detailReason_detail");
    }
  }
  
  private EsPiAForm getEsPiAForm(IGTEsPiEntity entity) throws GTClientException
  {
  	EsPiAForm form = new EsPiAForm();
  	form.setProcessInstanceID(entity.getFieldString(IGTEsPiEntity.PROCESS_INSTANCE_ID));
		form.setDocNumber(entity.getFieldString(IGTEsPiEntity.DOCUMENT_NUMBER));
		form.setPartnerID(entity.getFieldString(IGTEsPiEntity.PARTNER_ID));
		form.setProcessState(entity.getFieldString(IGTEsPiEntity.PROCESS_STATE));
		form.setProcessStartDate(entity.getFieldString(IGTEsPiEntity.PROCESS_START_DATE));
		form.setProcessEndDate(entity.getFieldString(IGTEsPiEntity.PROCESS_END_DATE));
    form.setUserTrackingID(entity.getFieldString(IGTEsPiEntity.USER_TRACKING_ID));
    form.setDocDateGenerated(entity.getFieldString(IGTEsPiEntity.DOCUMENT_DATE));
    form.setRemark((entity.getFieldString(IGTEsPiEntity.REMARK)));
    form.setFailedReason(entity.getFieldString(IGTEsPiEntity.FAILED_REASON));
    form.setDetailReason(entity.getFieldString(IGTEsPiEntity.DETAIL_REASON));
    form.setRetryNumber(entity.getFieldString(IGTEsPiEntity.RETRY_NUMBER));
    
    System.out.println("EsPiDetail Renderer "+form.getFailedReason());
    return form;
  }
}
