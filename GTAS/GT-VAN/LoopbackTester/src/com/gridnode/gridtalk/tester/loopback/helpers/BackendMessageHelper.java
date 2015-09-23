/**
 * 
 */
package com.gridnode.gridtalk.tester.loopback.helpers;

import com.gridnode.gridtalk.tester.loopback.cfg.ConfigMgr;
import com.gridnode.gridtalk.tester.loopback.dao.BackendMessageDao;
import com.gridnode.gridtalk.tester.loopback.entity.BackendMessage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * @author Alain
 * 
 */
public class BackendMessageHelper
{
	private final static String DOC_GEN_TS_PLACEHOLDER = "###_DOC_GEN_TS_###";

	private BackendMessageDao _dao = null;

	/**
	 * 
	 */
	public BackendMessageHelper()
	{
		_dao = new BackendMessageDao();
	}

	public String generateBackendMessage(String pipCode, String docId) throws IOException
	{
		return generateBackendMessage(pipCode, docId, docId);
	
	}

	public String generateBackendMessage(String pipCode, String requestDocId, String docId) throws IOException
	{
		String mn = "generateBackendMessage";
		BackendMessage msg = _dao.getBackendMessage(pipCode);
		String template = msg.getTemplate();
		String docGenTs = genTs();
//		Logger.debug(this.getClass().getSimpleName(), mn, "Replacing"+ BackendMessage.PLACE_HOLDER_DOC_ID +" with "+docId);
		template = template.replaceAll(BackendMessage.PLACE_HOLDER_DOC_ID,
																				docId);
//		Logger.debug(this.getClass().getSimpleName(), mn, template);
		
//		Logger.debug(this.getClass().getSimpleName(), mn, "Replacing"+ BackendMessage.PLACE_HOLDER_REF_DOC_ID +" with "+requestDocId);
		template = template.replaceAll(BackendMessage.PLACE_HOLDER_REF_DOC_ID,
																				requestDocId);
//		Logger.debug(this.getClass().getSimpleName(), mn, template);

//		Logger.debug(this.getClass().getSimpleName(), mn, "Replacing"+ BackendMessage.PLACE_HOLDER_RECEIVER_DUNS +" with "+ConfigMgr.getMainConfig().getDuns());
		template = template.replaceAll(BackendMessage.PLACE_HOLDER_RECEIVER_DUNS, ConfigMgr.getPartnerConfig().getDuns());
//		Logger.debug(this.getClass().getSimpleName(), mn, msgStr);

//		Logger.debug(this.getClass().getSimpleName(), mn, "Replacing"+ BackendMessage.PLACE_HOLDER_SENDER_DUNS +" with "+ConfigMgr.getRnifHubConfig().getDuns());
		template = template.replaceAll(BackendMessage.PLACE_HOLDER_SENDER_DUNS, ConfigMgr.getBackendConfig().getDuns());
//		Logger.debug(this.getClass().getSimpleName(), mn, msgStr);

		template = template.replaceAll(DOC_GEN_TS_PLACEHOLDER, docGenTs);

    // Replace variables from .param file
		Properties actionProp = msg.getParams();
		Enumeration keys = actionProp.propertyNames();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String val = actionProp.getProperty(key);
			template = template.replaceAll(key, val);
		}
		return template;
	}

	private String genTs()
	{
		SimpleDateFormat formatter = new SimpleDateFormat(
																											"yyyyMMdd'T'hhmmss'.'SSS'Z'");
		Date currentTime_1 = new Date();
		return formatter.format(currentTime_1);
	}

	public String findDocId(String content) throws IOException
	{
		String docId = null;
		BackendMessage msg = _dao.getBackendMessage("DEF");
		String docIdPath = msg.getDocIdPath();
		docId = findValue(content, docIdPath);
		return docId;
	}

//	public String findPipCode(String content) throws IOException
//	{
//		BackendMessage msg = _dao.getBackendMessage("DEF");
//		return findValue(content, msg.getPipCodePath());
//	}
//
	private String findValue(String content, String commaDelimitedTokenList)
	{
		String[] tokenList = constructTokenList(commaDelimitedTokenList);
		String value = search(content, tokenList);
		return value;
	}

	protected String[] constructTokenList(String commaDelimitedTokenList)
	{
		StringTokenizer st = new StringTokenizer(commaDelimitedTokenList, ", ");
		String[] tokenList = new String[st.countTokens()];
		int count = 0;
		while (st.hasMoreTokens())
		{
			tokenList[count++] = st.nextToken();
		}
		return tokenList;
	}

	protected String search(String content, String[] searchTokens)
	{
		int start = 0;
		int end = -1;
		for (int i = 0; i < searchTokens.length; i++)
		{
			int idx = content.indexOf(searchTokens[i], start);
			if (idx < 0) { return null; // not found
			}
			if (i % 2 == 0) // start token
			{
				start = idx + searchTokens[i].length();
			}
			else
			// end token
			{
				end = idx;
			}
		}
		return content.substring(start, end);
	}
}
