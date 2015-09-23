/**
 * 
 */
package com.gridnode.gridtalk.tester.loopback.entity;

import com.gridnode.gridtalk.tester.loopback.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Alain Ah Ming
 *
 */
public class RnifMessageEntity {
//	public static final int DIRECTION_GT_OUTBOUND = 0;
//	public static final int DIRECTION_GT_INBOUND = 1;
	/**
	 * OC2
	 */
	public static final String PIP_0C2 = "0C2";
	public static final String PIP_ACK = "ACK";
	
	public static final String PATH_DOC_ID = "doc.id";
	
	/**
	 * pip.id in <PIP_Code>.path file
	 */
	public static final String PATH_PIP_INSTANCE_ID = "pip.id";
	public static final String PATH_RECEIVER_DUNS = "receiver.duns";
	
	/**
	 * sender.duns in <PIP_Code>.path file
	 */
	public static final String PATH_SENDER_DUNS = "sender.duns";
	public static final String PATH_MESSAGE_TRACKING_ID = "message.tracking.id";
	public static final String PATH_INITIATOR_DUNS = "initiator.duns";
	
	/**
	 * pip.code in <PIP_Code>.path file
	 */
	public static final String PATH_PIP_CODE = "pip.code";
	
	private static final String TEMPLATE_FILE_EXT = "template";
	private static final String PARAMS_FILE_EXT = "param";
	private static final String PATHS_FILE_EXT = "path";
	
	private String _pipCode = null;
//	private String _template = null;
	private Properties _params = null;
//	private String _senderDuns = null;
//	private String _receiverDuns = null;
//	private String _pipInstIdPath = null;
//	private String _docIdPath = null;
	private File _templateFile = null;
	private Properties _paths = null;
	
	public RnifMessageEntity()
	{
		
	}

	public RnifMessageEntity(String pipCode) throws FileNotFoundException, IOException
	{
		_pipCode = pipCode;
		_templateFile = FileUtil.getFile(FileUtil.TYPE_DATA, _pipCode+"."+TEMPLATE_FILE_EXT);
		init();
	}
	
	/**
	 * @return Returns the templateFile.
	 */
	public File getTemplateFile()
	{
		return _templateFile;
	}

	private void init() throws FileNotFoundException, IOException
	{
		_params = new Properties();
		_params.load(new FileInputStream(FileUtil.getFile(FileUtil.TYPE_DATA, _pipCode+"."+PARAMS_FILE_EXT)));
		initPaths();
	}
	
	private void initPaths() throws FileNotFoundException, IOException
	{
		_paths = new Properties();
		_paths.load(new FileInputStream(FileUtil.getFile(FileUtil.TYPE_DATA, _pipCode+"."+PATHS_FILE_EXT)));
	}
	
//	public String getDocIdPath() {
//		return _docIdPath;
//	}
//
//	public void setDocIdPath(String docIdPath) {
//		_docIdPath = docIdPath;
//	}
//
	public Properties getParams() {
		return _params;
	}
//
//	public void setParams(Properties params) {
//		_params = params;
//	}

	public String getPath(String key) {
		return _paths.getProperty(key);
	}

//	public void setPipInstIdPath(String pipInstIdPath) {
//		_pipInstIdPath = pipInstIdPath;
//	}

//	public String getReceiverDuns() {
//		return _receiverDuns;
//	}
//
//	public void setReceiverDuns(String receiverDuns) {
//		_receiverDuns = receiverDuns;
//	}
//
//	public String getSenderDuns() {
//		return _senderDuns;
//	}
//
//	public void setSenderDuns(String senderDuns) {
//		_senderDuns = senderDuns;
//	}

//	public String getTemplate() {
//		return _template;
//	}

//	public void setTemplate(String template) {
//		_template = template;
//	}

}
