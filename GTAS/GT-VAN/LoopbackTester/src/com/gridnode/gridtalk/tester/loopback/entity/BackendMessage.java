/**
 * 
 */
package com.gridnode.gridtalk.tester.loopback.entity;

import com.gridnode.gridtalk.tester.loopback.util.FileUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Alain
 *
 */
public class BackendMessage {
  public static final String PLACE_HOLDER_SENDER_DUNS = "###_SENDER_DUNS_###";
  public static final String PLACE_HOLDER_RECEIVER_DUNS = "###_RECEIVER_DUNS_###";
  public static final String PLACE_HOLDER_DOC_ID = "###_DOC_ID_###";
    public static final String PLACE_HOLDER_REF_DOC_ID = "###_REF_DOC_ID_###";
    public static final String PATH_KEY_DOC_ID = "doc.id";
    
    /**
     * <code>pip.code</code> in Be_DEF.path;
     */
//    public static final String PATH_KEY_PIP_CODE = "pip.code";
    
    private String _docIdPath = null;
//    private String _pipCodePath = null;
    private String _template = null;
    private String _refDocIdPath = null;
    
    private String _pipCode = null;
    private Properties _params = null;
  	private Properties _paths = null;
  	
  	private static final String TEMPLATE_FILE_EXT = "template";
  	private static final String PARAMS_FILE_EXT = "param";
  	private static final String PATHS_FILE_EXT = "path";
  	
    /**
		 * @return Returns the refDocIdPath.
		 */
		public String getRefDocIdPath()
		{
			return _refDocIdPath;
		}
		/**
		 * @param refDocIdPath The refDocIdPath to set.
		 */
		public void setRefDocIdPath(String refDocIdPath)
		{
			_refDocIdPath = refDocIdPath;
		}
		/**
     * 
     */
    public BackendMessage(String pipCode)  throws FileNotFoundException, IOException
    {
    	_pipCode = pipCode;
    	init();
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

    /**
     * @return Returns the docIdPath.
     */
    public String getDocIdPath() {
        return _docIdPath;
    }
    /**
     * @param docIdPath The docIdPath to set.
     */
    public void setDocIdPath(String docIdPath) {
        _docIdPath = docIdPath;
    }
    /**
     * @return Returns the templateFile.
     */
    public String getTemplate() {
        return _template;
    }
    /**
     * @param templateFile The templateFile to set.
     */
    public void setTemplate(String template) {
        _template = template;
    }
    /**
     * @return Returns the pipCodePath.
     */
//    public String getPipCodePath() {
//        return _pipCodePath;
//    }
    /**
     * @param pipCodePath The pipCodePath to set.
     */
//    public void setPipCodePath(String pipCodePath) {
//        _pipCodePath = pipCodePath;
//    }
		/**
		 * @return Returns the params.
		 */
		public Properties getParams()
		{
			return _params;
		}

}
