package com.gridnode.pdip.app.workflow.impl.bpss.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;

import com.gridnode.pdip.app.workflow.exceptions.WorkflowException;
import com.gridnode.pdip.app.workflow.util.IWorkflowConstants;
import com.gridnode.pdip.app.workflow.util.Logger;
import com.gridnode.pdip.base.gwfbase.bpss.helpers.BpssDefinitionCache;
import com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration;
import com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecEntry;
import com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecification;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.util.StringUtil;
import com.gridnode.pdip.framework.util.UtilEntity;
import com.gridnode.pdip.framework.util.UtilString;

public class BpssKeyHelper {
	
	private static final String BPSS_KEY_PATH = "workflow.path.bpss.key";
	private static final String BPSS_KEY_FILE = "bpssKey.properties";
	private static final String BPSS_KEY_PREFIX = ".include.Gridnode.ID";
	
	private static final String REGISTRATION_HANDLER_CLASS = "registration.handler";
	private static final String REGISTRATION_INSTANTIATE_METHOD = "registration.instantiate.method";
	private static final String REGISTRATION_REGISTER_INFO_METHOD = "registration.register.info.method";
	private static final String REGISTRATION_INFO_CLASS = "registration.info";
	private static final String REGISTRATION_NODE_ID_METHOD = "registration.node.id.method";
	private static String GRIDNODE_ID = "";
	
	private static Properties bpssKeyProperties = new Properties();
	
	static
	{
		//Load the bpss key properties
		try
		{
			File f = FileUtil.getFile(BPSS_KEY_PATH, BPSS_KEY_FILE);
			if(f != null)
			{
				bpssKeyProperties.load(new FileInputStream(f));
				Logger.debug("BpssKeyHelper", "BpssKeyProperties: "+bpssKeyProperties);
			}
			else
			{
				Logger.log(Logger.activityCategory, "BpssKeyHelper No "+BPSS_KEY_FILE+" found.");
			}
		}
		catch(Exception ex)
		{
			Logger.err(Logger.activityCategory, "BpssKeyHelper Error in retrieving the bpss key properties", ex);
		}
		
		//Get the Gridnode ID
		try
		{
			if(bpssKeyProperties != null)
			{
				GRIDNODE_ID = initGridnodeID(bpssKeyProperties);
				Logger.debug(Logger.activityCategory, "BpssKeyHelper Obtain node id is "+GRIDNODE_ID);
			}
		}
		catch(Exception ex)
		{
			Logger.err(Logger.activityCategory, "BpssKeyHelper Error in getting the Gridnode ID", ex);
		}
	}
	
    public static String getProcessDefinitionKey(String documentType,String cacheKey) throws WorkflowException,SystemException {
        //assumption is made that document type is unique in the system,i.e accross all the packages
        BpssDefinitionCache defCache=BpssDefinitionCache.getBpssDefinitionCache(cacheKey);
        BpssBinaryCollaboration binCollaboration=defCache.getBpssBinaryCollaborationDocEnvelop(documentType);
        String processDefKey="http://"+IWorkflowConstants.BPSS_ENGINE+"/";
        BpssProcessSpecification bpssProcessSpecification=defCache.getBpssProcessSpecification();
        processDefKey+=bpssProcessSpecification.getSpecName()+"/";
        processDefKey+=bpssProcessSpecification.getSpecVersion()+"/";
        processDefKey+=bpssProcessSpecification.getSpecUUId()+"/";
        processDefKey+=BpssBinaryCollaboration.ENTITY_NAME+"/";
        processDefKey+=binCollaboration.getBinaryCollaborationName();
        return processDefKey;
    }

    public static String getProcessDefinitionKey(Long entryUId,String entryType) throws Throwable{
        String processDefKey="http://"+IWorkflowConstants.BPSS_ENGINE+"/";
        BpssProcessSpecEntry bpssProcessSpecEntry=BpssDefinitionHelper.getProcessSpecEntry(entryUId,entryType);
        BpssProcessSpecification bpssProcessSpecification=(BpssProcessSpecification)UtilEntity.getEntityByKey(bpssProcessSpecEntry.getSpecUId(),BpssProcessSpecification.ENTITY_NAME,true);
        processDefKey+=bpssProcessSpecification.getSpecName()+"/";
        processDefKey+=bpssProcessSpecification.getSpecVersion()+"/";
        processDefKey+=bpssProcessSpecification.getSpecUUId()+"/";
        processDefKey+=entryType+"/";
        processDefKey+=bpssProcessSpecEntry.getEntryName();
        return processDefKey;
    }

    public static String makeDocumentId(Long rtBinaryCollaborationUId,String businessTransActivityId) throws WorkflowException {
        try{
            String documentId="gwf://"+rtBinaryCollaborationUId+"/"+businessTransActivityId+"/"+IBpssConstants.PARTNER_CONSTANT;
            Logger.debug("[BpssKeyHelper.makeDocumentId] documentId="+documentId);
            return documentId;
        }catch(Throwable th){
            throw new WorkflowException("[BpssKeyHelper.makeDocumentId] Unable to makeDocumentId, rtBinaryCollaborationUId="+rtBinaryCollaborationUId+",businessTransActivityId="+businessTransActivityId,th);
        }
    }

    public static String makeDocumentId(Long rtBinaryCollaborationUId,String businessTransActivityId, String processInstanceIDPrefix) throws WorkflowException {
        try{
        	processInstanceIDPrefix = processInstanceIDPrefix != null && !"".equals(processInstanceIDPrefix.trim()) ? ":"+processInstanceIDPrefix : "";
        	
            String documentId="gwf://"+rtBinaryCollaborationUId+processInstanceIDPrefix+"/"+businessTransActivityId+"/"+IBpssConstants.PARTNER_CONSTANT;
            Logger.debug("[BpssKeyHelper.makeDocumentId] documentId="+documentId);
            return documentId;
        }catch(Throwable th){
            throw new WorkflowException("[BpssKeyHelper.makeDocumentId] Unable to makeDocumentId, rtBinaryCollaborationUId="+rtBinaryCollaborationUId+",businessTransActivityId="+businessTransActivityId,th);
        }
    }
    
    public static boolean canParseDocumentId(String documentId){
        List list=UtilString.split(documentId,"/");
        return (list.size()>=4 && list.get(0).toString().equals("gwf:"));
    }

    public static String changeDocumentId(String documentId,String businessTransActivityId){
        // if the documentId is generated by our engine then it can be parsed and it will change from one BusinessTransActivity
        // to another,this is done to support fork,so change the documentId to have current BusinessTransActivity name.
        // if documentId cannot be parsed means it is generated by other BPSS system.

        String newDocumentId=documentId;
        if(canParseDocumentId(newDocumentId)){
            List list=UtilString.split(documentId,"/");
            list.set(0,list.get(0)+"/");
            list.set(2,businessTransActivityId);
            newDocumentId=UtilString.join(list,"/");
        }
        Logger.debug("[BpssKeyHelper.changeDocumentId] oldDocumentId="+documentId+",newDocumentId="+newDocumentId);
        return newDocumentId;
    }
    
    /**
     * TWX 28 Feb 2008 SVN:T1, Check whether to include the process instance prefix for this particular 
     * partnerID.
     * @param partnerID
     * @return true if prefix is needed for the given partnerID
     */
    public static boolean isIncludedProcessInstancePrefix(String partnerID)
    {
    	if(bpssKeyProperties != null)
    	{
    		return bpssKeyProperties.containsKey(""+partnerID+BPSS_KEY_PREFIX);
    	}
    	return false;
    }
    
    public static String getGridnodeID()
    {
    	return GRIDNODE_ID;
    }
    
    /**
     * TWX 28 Feb 2008: SVN:T1, Use reflection to get the Gridnode ID. This provide a hack for the workflow module not
     * depending on the "gtas.registration" during the build process.
     * @param bpssKeyProps
     * @return
     * @throws Exception
     */
    private static String initGridnodeID(Properties bpssKeyProps) throws Exception
    {
    	if(bpssKeyProps == null)
    	{
    		return "";
    	}
    	
    	String registrationHandlerClass = bpssKeyProps.getProperty(REGISTRATION_HANDLER_CLASS);
    	String registrationHandlerInstantiateMethod = bpssKeyProps.getProperty(REGISTRATION_INSTANTIATE_METHOD);
    	String registrationInfoMethod = bpssKeyProps.getProperty(REGISTRATION_REGISTER_INFO_METHOD);
    	
    	String registrationInfoClass = bpssKeyProps.getProperty(REGISTRATION_INFO_CLASS);
    	String gridNodeIDMethod = bpssKeyProps.getProperty(REGISTRATION_NODE_ID_METHOD);
    	
    	if(StringUtil.isEmpty(registrationHandlerClass) || StringUtil.isEmpty(registrationHandlerInstantiateMethod) || StringUtil.isEmpty(registrationInfoClass) ||
    			StringUtil.isEmpty(gridNodeIDMethod) || StringUtil.isEmpty(registrationInfoMethod))
    	{
    		return "";
    	}
    	else
    	{
    		Class registerHandler = Class.forName(registrationHandlerClass);
        	Method registerInstantiateMethod = registerHandler.getMethod(registrationHandlerInstantiateMethod, new Class[]{});
        	Object registerHandlerInstance = registerInstantiateMethod.invoke(null, new Object[]{});
        	Method getRegisterInfoMethod = registerHandler.getMethod(registrationInfoMethod, new Class[]{});
        	
        	
        	Object registerInfoInstance = getRegisterInfoMethod.invoke(registerHandlerInstance, new Object[]{}); 
        	Class registerInfoClass = Class.forName(registrationInfoClass);
        	Method getGridnodeIDMethod = registerInfoClass.getMethod(gridNodeIDMethod, new Class[]{});
        	
        	Integer gridnodeID = (Integer)getGridnodeIDMethod.invoke(registerInfoInstance, new Object[]{});
        	
        	
        	return gridnodeID + "";
    	}
    }
  /**
   * Base on the document id, determine if GT is participating the requesting role.
   * @param docId The document id
   * @return <b>true</b> if docId ends with <code>/SELF</code>, <b>false</b> otherwise.
   */  
  public static boolean isRequestingRole(String docId)
  {
    //NSL20070530
    return (docId != null && docId.endsWith("/"+IBpssConstants.PARTNER_CONSTANT));
  }
}