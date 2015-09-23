/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AuditFileRenderer.java
 *
 ****************************************************************************
 * Date           Author              		Changes
 ****************************************************************************
 * 12 Oct 2005			Sumedh Chalermkanjana		Created. 
 */
package com.gridnode.gtas.client.web.archive.docforpi;

import com.gridnode.gtas.client.ctrl.IGTAuditFileEntity;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.BindingFieldPropertyRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;
import com.gridnode.pdip.framework.util.AssertUtil;

public class AuditFileRenderer extends AbstractRenderer
{
//	private static final String EVIL_HARDCODED_CERT_FILENAME = "certificate.cer";
	
  private boolean _edit;
    
  private static final Number _commonFields[] = 
  {
  	IGTAuditFileEntity.FILENAME,
  	IGTAuditFileEntity.DOC_NO,
  	IGTAuditFileEntity.DOC_TYPE,
  	IGTAuditFileEntity.PARTNER_ID,
  	IGTAuditFileEntity.PARTNER_DUNS,
  	IGTAuditFileEntity.PARTNER_NAME,
  	IGTAuditFileEntity.DATE_CREATED,
  	IGTAuditFileEntity.PREAMBLE,
  	IGTAuditFileEntity.DELIVERY_HEADER,
  	IGTAuditFileEntity.SERVICE_HEADER,
  	IGTAuditFileEntity.SERVICE_CONTENT
  };
  
  public AuditFileRenderer(RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
    AssertUtil.assertTrue(edit == false);
  }

  protected void render() throws RenderingException
  {
    try
    {
      IGTAuditFileEntity instance = (IGTAuditFileEntity) getEntity();
      AuditFileAForm form = (AuditFileAForm) getActionForm();   
      
      renderCommonFormElements(instance.getType(), _edit);
      
      BindingFieldPropertyRenderer bfpr = renderFields(null,instance,_commonFields,null,form,null);
      removeNode("edit_button", true);
      //renderCertificateLink(instance);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering audit file screen", t);
    }
  }

  private void log(String message)
  {
  	com.gridnode.gtas.client.web.archive.helpers.Logger.debug("[AuditFileRenderer] " + message);
  }
}  
  
//  TEST
//  private void renderCertificateField(RenderingContext rContext,
//                                AuditFileAForm form,
//                                IGTAuditFileEntity entity)
//    throws RenderingException
//  { 
//    try
//    {
//    	//TEST
//    	String certFilename = entity.getFieldString(IGTAuditFileEntity.CERTIFICATE);
//    	
//      FormFileElement ffe = new FormFileElement("0",certFilename);
//      FormFileElement[] formFileElements = { ffe };
//
//      //TEST
////      String dlhKey = StaticWebUtils.getDlhKey(entity, rContext.getOperationContext(), IGTAuditFileEntity.CERTIFICATE);
//      MultifilesRenderer mfr = new MultifilesRenderer(rContext);
//      
//      //TEST
////      mfr.setDlhKey(dlhKey);
//      mfr.setDownloadable(true);
//      mfr.setEntity(entity);
//      mfr.setFieldId(IGTAuditFileEntity.CERTIFICATE);
//      mfr.setFormFileElements(formFileElements);
//      mfr.setViewOnly(true);
//      mfr.setInsertId("certificate_value");
//      mfr.setCollection(false);
//
//      mfr.render(_target);
//
////      renderLabel("certFile_label","certificate.certFile",false);
//
//    }
//    catch(Throwable t)
//    {
//      throw new RenderingException("Error rendering field for CERTIFICATE",t);
//    }
//  }
  
  /**
   * SUMEDH:
   * I really don't know why bfpr doesn't render certificate field properly ( this field has fieldmetainfo of  type=file ) 
   * - it just renders filename without hyperlink.
   * For now I just render file download link manually. 
   */
  /*
  private void renderCertificateLink(IGTAuditFileEntity entity) throws RenderingException
  {
  	try
  	{
	  	String filename = entity.getFieldString(IGTAuditFileEntity.CERTIFICATE);
	  	String folder = FileUtil.getPath("dbarchive.path.estore.cert.directory");
	  	Element anchor = _target.createElement("a");
	  	anchor.setAttribute("target", "_new");
	  	String hrefValue = "/gridtalk/downloadAction.do?domain=gtas&filePath=" + folder + filename;
	  	anchor.setAttribute("href", hrefValue);
	  	Text text = _target.createTextNode(filename);
	  	anchor.appendChild(text);
	  	Element parent = getElementById("certificate_value");
	  	removeAllChildren(parent);
	  	parent.appendChild(anchor);
  	} catch (FileAccessException e)
  	{
  		throw new RenderingException(e);
  	} catch (GTClientException e)
  	{
  		throw new RenderingException(e); 
  	}
  }*/