/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JmsDestinationRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 3 Jan 05				SC									Created
 * 16 Jan 05			SC									Modify to support viewing jms destination
 */
package com.gridnode.gtas.client.web.alert;

import java.util.Collection;

import org.apache.struts.action.ActionError;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTBusinessEntityEntity;
import com.gridnode.gtas.client.ctrl.IGTChannelInfoEntity;
import com.gridnode.gtas.client.ctrl.IGTDomainIdentifierEntity;
import com.gridnode.gtas.client.ctrl.IGTJmsDestinationEntity;
import com.gridnode.gtas.client.ctrl.IGTLookupPropertiesEntity;
import com.gridnode.gtas.client.ctrl.IGTWhitePageEntity;
import com.gridnode.gtas.client.utils.IFilter;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.web.strutsbase.MessageUtils;
import com.gridnode.pdip.framework.util.AssertUtil;

/**
 * Note: We only render CONNECTION_PASSWORD field in edit mode but not in view mode.
 *
 */
public class JmsDestinationRenderer extends AbstractRenderer
{
  private boolean edit;
	
	/* jms destination fields (without lookup properties field and CONNECTION_PASSWORD) */
  protected static final Number[] FIELDS = 
  {
  	IGTJmsDestinationEntity.NAME,
  	IGTJmsDestinationEntity.TYPE,
  	IGTJmsDestinationEntity.JNDI_NAME,
  	IGTJmsDestinationEntity.DELIVERY_MODE,
  	IGTJmsDestinationEntity.PRIORITY,
  	IGTJmsDestinationEntity.CONNECTION_FACTORY_JNDI,
  	IGTJmsDestinationEntity.CONNECTION_USER,
  	IGTJmsDestinationEntity.RETRY_INTERVAL,
  	IGTJmsDestinationEntity.MAXIMUM_RETRIES
  };
  
  protected static final Number[] PASSWORD_FIELD =
  {
  	IGTJmsDestinationEntity.CONNECTION_PASSWORD
  };

  private static Number[] LOOKUP_PROPERTIES_FIELDS =
  {
  	IGTLookupPropertiesEntity.NAME,
  	IGTLookupPropertiesEntity.VALUE
  };
  
  public JmsDestinationRenderer( RenderingContext rContext, boolean edit)
  {
    super(rContext);
    this.edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
//      RenderingContext rContext = getRenderingContext(); 

      IGTJmsDestinationEntity jd = (IGTJmsDestinationEntity) getEntity();
      JmsDestinationAForm form = (JmsDestinationAForm) getActionForm();
      
      if (edit)
      {
      	includeJavaScript(IGlobals.JS_PROPERTY);
      }
      
      renderCommonFormElements(jd.getType(), edit);
      
      BindingFieldPropertyRenderer bfpr = renderFields(null, jd, FIELDS, null, form, "");
      
      if (edit)
      {
      	renderFields(bfpr, jd, PASSWORD_FIELD, null, form, "");
      }
      renderLookupProperties();
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering jms destination screen", t);
    }
  }

  protected void renderLookupProperties() throws Throwable
  {
    RenderingContext rContext = getRenderingContext();
    IGTJmsDestinationEntity jd = (IGTJmsDestinationEntity) getEntity();
    JmsDestinationAForm form = (JmsDestinationAForm) rContext.getOperationContext().getActionForm();

//    renderLabel("domainIdentifier.type_label",         "domainIdentifier.type");
//    renderLabel("domainIdentifier.value_label",        "domainIdentifier.value");

//    renderLabelCarefully("addLookupProperties",        "lookupProperties.addLookupProperties",     false);
//    renderLabelCarefully("removeLookupProperties",     "lookupProperties.removeLookupProperties",  false);

    /* remove one lookup properties row from xhtml template */
    Node lpRow = getElementById("lp_row");
    Node lpRowParent = lpRow.getParentNode();
    lpRowParent.removeChild(lpRow);
    
    Node controlRow = getElementById("control_row");
    
    Collection lookupPropertiesCollection = (Collection) jd.getFieldValue(IGTJmsDestinationEntity.VIRTUAL_LOOKUP_PROPERTIES);
    
    IGTLookupPropertiesEntity[] lpEntities = (IGTLookupPropertiesEntity[])
    		lookupPropertiesCollection.toArray(new IGTLookupPropertiesEntity[lookupPropertiesCollection.size()]);
    LookupPropertiesAForm[] lpForms = form.getLookupProperties();
    for(int i = 0; i < lpForms.length; i++)
    {
      try
      {
        Node clonedRow = lpRow.cloneNode(true);
        lpRowParent.insertBefore(clonedRow, controlRow);
        
        // Render lookupProperties
        IGTLookupPropertiesEntity lpEntity = lpEntities[i];
        LookupPropertiesAForm lpForm = lpForms[i];

        renderFields(null, lpEntity, LOOKUP_PROPERTIES_FIELDS, lpForm, "");

        // Make id unique for each row
        String idPrefix = "lookupProperties[" + i + "].";

        if (edit)
        {
	        Element selectCol = getElementById("selected_value");
					selectCol.setAttribute("id", idPrefix + "selected_value");
					selectCol.setAttribute("name", idPrefix + "selected");
	
					if (lpForm.isSelected())
					{
						selectCol.setAttribute("checked", "checked");
					}
        }
        
        Element nameCol = getElementById("lp_name_value");
        nameCol.setAttribute("id", idPrefix + "lp_name_value");
        nameCol.setAttribute("name", idPrefix + "lp_name");
        
        Element valueCol = getElementById("lp_value_value");
        valueCol.setAttribute("id", idPrefix + "lp_value_value");
        valueCol.setAttribute("name", idPrefix + "lp_value");

        /* Manually display error */
        if (edit)
        {
	        Element errorCol = getElementById("lp_error");
	        errorCol.setAttribute("id", idPrefix + "lp_error");
	        
	        ActionError error = MessageUtils.getFirstError(rContext.getActionErrors(), idPrefix + "error.nameAndValue.required"); 
	        if(error != null)
	        {
	          //String errorMsg = rContext.getResourceLookup().getMessage(error.getKey());
	          String errorMsg = rContext.getResourceLookup().getMessage("lp.error.nameAndValue.required");
	          if(errorMsg == null) errorMsg = "Please insert error message for lp.error.nameAndValue.required";
	          replaceMultilineText(errorCol, errorMsg);
	          errorCol.setAttribute("class", "errortext");
	        }
        }
      }
      catch(Throwable t)
      {
        throw new RenderingException("Error rendering lookupProperties, i=" + i, t);
      }
    }
  }
}

