/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LoginRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-30     Andrew Hill         Created
 * 2002-10-??     Andrew Hill         Adjust to chromed version
 * 2002-11-07     Andrew Hill         Tweaking for demo
 * 2003-01-07     Andrew Hill         Proper use of RenderingContext
 * 2003-01-10     Andrew Hill         TimeZone rendering
 * 2003-01-15     Andrew Hill         Fixed typo in timezone error rendering causing npe
 * 2003-02-19     Andrew Hill         Render timeout msg if rqd
 * 2003-04-08     Andrew Hill         Use IGlobals script url constant
 * 2003-06-26     Andrew Hill         Extend AbstractLoginRenderer to share some common behaviour
 */
package com.gridnode.gtas.client.web.login;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.utils.DateUtils;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.web.strutsbase.MessageUtils;
 
public class LoginRenderer extends AbstractLoginRenderer implements IDocumentRenderer
{

  public LoginRenderer( RenderingContext rContext,
                        LoginAForm form,
                        String submitUrl)
  { //20030626AH - Refactor to use superclass
    super(rContext, form, submitUrl);
  }

  protected void render() throws RenderingException
  { //20030107AH - Mod to use RenderingContext
    try
    {
      super.render(); //20030626AH - Invoke common rendering for login renderers
      RenderingContext rContext = getRenderingContext();
      IURLRewriter urlRewriter = rContext.getUrlRewriter();
      LoginAForm form = _form;

      ActionErrors actionErrors = rContext.getActionErrors();
      includeJavaScript(IGlobals.JS_ENTITY_FORM_METHODS); //20030317AH, 20030408AH

      boolean isTimeout = isTimeout(rContext.getRequest()); //20030219AH

      Document target = _target;

      // First render internationalised text into dom:
      renderLabel("login_heading","login.heading",false);
      renderLabel("login_message","login.message",false);
      renderLabel("username_label","login.username",false);
      renderLabel("password_label","login.password",false);
      renderElementAttributeFromKey("login","value","login.submit");
      renderLabel("title","gridtalk.title.window",false); //20030423AH
      renderLabel("timeZone_label","login.timeZone",false); //20030110AH

      // Render the submit URL using the url rewriter to ensure context path and session encoding
      renderElementAttribute("login_form","action",urlRewriter.rewriteURL(_submitUrl));

      // Render the user-entered values from the action form
      renderElementAttribute("username","value",form.getUsername());
      renderElementAttribute("password","value",""); //nb: we clear password field explicitly
      renderElementAttribute("gmtOffset","value",form.getGmtOffset()); //20030113AH

      renderTimeZones(rContext);

      if(isTimeout)
      { //20030219AH
        renderLabelCarefully("timeout_label","login.timeout",false);
      }
      else
      { //20030219AH
        removeNode("timeout_label",false);
      }

      if(actionErrors != null)
      {
        ActionError usernameAErr = MessageUtils.getFirstError(actionErrors, "username");
        if(usernameAErr != null)
        {
          renderLabel("username_error",usernameAErr.getKey());
        }
        ActionError passwordAErr = MessageUtils.getFirstError(actionErrors, "password");
        if(passwordAErr != null)
        {
          renderLabel("password_error",passwordAErr.getKey());
        }
        ActionError loginAErr = MessageUtils.getFirstError(actionErrors, "login");
        if(loginAErr != null)
        {
          renderLabel("login_error",loginAErr.getKey());
        }
        ActionError timeZoneAErr = MessageUtils.getFirstError(actionErrors, "timeZone");
        //if(loginAErr != null) Opps....
        if(timeZoneAErr != null) //20030115AH
        { //20030110AH
          renderLabel("timeZone_error",timeZoneAErr.getKey());
        }
      }
    }
    catch(Exception e)
    {
      throw new RenderingException("Error rendering login page",e);
    }
  }

  protected void renderTimeZones(RenderingContext rContext)
    throws RenderingException
  { //20030113AH
    try
    {
//dumpZoneEn(rContext);

      final Date date = new Date();
      int curOffset = StaticUtils.primitiveIntValue( ((LoginAForm)_form).getGmtOffset() );

      //Get a list of candidate TimeZones. Later when got time to refactor we will want to cache
      //the candidate lists in ServletContext. This cache would itself be a hashtable keyed by
      //the current server side offset (so we dont use it after a dst transition) into which
      //candidate collections of timezones are placed keyed by the client offset.
      final List candidates = DateUtils.getTimeZones(date,curOffset);

      // Add default timezone near top if not in list already
      TimeZone defZone = TimeZone.getDefault();
      if(!DateUtils.timeZoneCollectionContains(candidates,defZone,true)) //20030407AH
      {
        candidates.add(0,defZone);
      }
      // Add UTC timezone at top of list if not in list already
      TimeZone utc = TimeZone.getTimeZone("UTC");
      if(!DateUtils.timeZoneCollectionContains(candidates,utc))
      {
        candidates.add(0,utc);
      }

      final ISimpleResourceLookup rLookup = rContext.getResourceLookup();
      final Locale locale = StaticWebUtils.getLocale(rContext.getRequest());
      //@todo: sorting
      LoginAForm form = _form;

      IOptionValueRetriever tzRetriever = new IOptionValueRetriever()
      {
        public String getOptionText(Object choice) throws GTClientException
        {
          TimeZone tz = (TimeZone)choice;
          boolean isDst = tz.inDaylightTime(date);
          String displayName = tz.getDisplayName(isDst,tz.SHORT,locale);
          String i18nKey = StaticUtils.replaceSubstring(tz.getID(), ":", "_"); //20030407AH
          String id = rLookup.getMessage( i18nKey );
          displayName = displayName + " - " + id;
          return displayName;
        }

        public String getOptionValue(Object choice) throws GTClientException
        {
          TimeZone tz = (TimeZone)choice;
          return tz.getID();
        }
      };
      renderSelectOptions("timeZone_value",candidates,tzRetriever,true,null);
      
      // 20031201 DDJ: GNDB00014694
      String timeZone = ((LoginAForm)_form).getTimeZone();          
      if(!renderSelectedOptions("timeZone_value", timeZone, false)) 
      {
        renderSelectedOptions("timeZone_value", defZone.getID(), false);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering TimeZones",t);
    }
  }

  protected boolean isTimeout(HttpServletRequest request)
  { //20030219AH
    return StaticUtils.primitiveBooleanValue(request.getParameter("isTimeout"));
  }

  public void dumpZoneEn(RenderingContext rContext) throws RenderingException
  {
    StringBuffer b = new StringBuffer();
    String[] zones = TimeZone.getAvailableIDs();
    for(int i=0; i < zones.length; i++)
    {
      String city = getCity(zones[i]);
      //b.append(zones[i] + "=" + city + "\n");
      b.append(zones[i] + "=" + zones[i] + "\n");
    }

    Comment comment = _target.createComment(b.toString());
    Element head = getHeadNode();
    head.appendChild(comment);
  }
  
  private String getCity(String id)
  {
    int index = id.indexOf("/",0);
    if(index == -1) return id;
    String city = id.substring(index+1,id.length());
    return StaticUtils.replaceSubstring(city,"_"," ");
  }
}