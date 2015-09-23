package com.gridnode.pdip.app.workflow.adaptors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.gridnode.pdip.app.workflow.exceptions.WorkflowException;
import com.gridnode.pdip.app.workflow.util.Logger; 
import com.gridnode.pdip.base.appinterface.data.AppDefinitionDoc;
import com.gridnode.pdip.base.appinterface.data.IAppConstants;
import com.gridnode.pdip.base.appinterface.exception.AppExecutionException;
import com.gridnode.pdip.base.appinterface.facade.ejb.IAppManagerHome;
import com.gridnode.pdip.base.appinterface.facade.ejb.IAppManagerObj;
import com.gridnode.pdip.base.appinterface.interfaces.IJavaProcedure;
import com.gridnode.pdip.base.appinterface.interfaces.adaptor.GenericJavaAdaptor;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.util.UtilString;

public class AppAdaptor {

    public static Object callApp(Map propMap,HashMap dataMap,Long rtActivityUId) throws AppExecutionException,WorkflowException{
        propMap=changeKeyNames(propMap);
        String appType=(String)propMap.get("adapter.apptype");
        if(appType==null || Integer.parseInt(appType)==IAppConstants.JAVA_PROC){
            return callJavaProcedure(propMap,dataMap,rtActivityUId);
        } 
        else throw new WorkflowException("Unknown App Type propMap="+propMap);
    }

    private static Map changeKeyNames(Map propMap){
        if(propMap.get("adaptor.keyschanged")!=null)
            return propMap;
        Map map=new HashMap();
        for(Iterator iterator=propMap.keySet().iterator();iterator.hasNext();){
            String key=(String)iterator.next();
            String newKey=key;
            if(newKey!=null){
                if(newKey.equals("appType"))
                    newKey="adapter.apptype";
                else if(newKey.equals("appName"))
                    newKey="adapter.appname";
                else if(newKey.equals("AdapterClass"))
                    newKey="adapter.classname";
                else if(newKey.equals("AdapterMethod"))
                    newKey="adapter.method";
            }
            map.put(newKey,propMap.get(key));
        }
        map.put("adaptor.keyschanged","new");
        return map;
    }

    public static Object callJavaProcedure(Map adaptorInfo,HashMap dataMap,Long rtActivityUId) throws AppExecutionException,WorkflowException{
        try{
            adaptorInfo=changeKeyNames(adaptorInfo);
            String appName=(String)adaptorInfo.get("adapter.appname");
            if(appName==null)
                appName=GenericJavaAdaptor.class.getName();
            String className=(String)adaptorInfo.get("adapter.classname");
            String methodName=(String)adaptorInfo.get("adapter.method");
            List parameterNames=UtilString.split((String)adaptorInfo.get("adaptor.parameternames"),",");
            List parameterTypes=UtilString.split((String)adaptorInfo.get("adaptor.parametertypes"),",");

            AppDefinitionDoc doc = new AppDefinitionDoc();
            doc.setAppName(appName);
            doc.setParam(IJavaProcedure.PARAM_CLASSNAME, className);
            doc.setParam(IJavaProcedure.PARAM_METHODNAME, methodName);
            Vector params=new Vector();
            int i=0;
            for(Iterator iterator=parameterNames.iterator();iterator.hasNext();i++){
                parameterTypes.set(i,Class.forName((String)parameterTypes.get(i)));
                String paramName=(String)iterator.next();
                params.add(dataMap.get(paramName)); 
            }
            doc.setParam(IJavaProcedure.PARAM_PARAMTYPES, new Vector(parameterTypes));
            IAppManagerObj appManager = getAppManager();
            Object result=appManager.executeApp(doc,IJavaProcedure.JAVA_PROC,params);
            return result;
        }catch(AppExecutionException ex){
            throw ex;
        }catch(Throwable th){
            Logger.warn("[AppAdaptor.callJavaProcedure] Exception ",th);
            throw new WorkflowException("Exception in calling Java procedure: "+ th.getMessage(),th);
        }
    }
    
    private static IAppManagerObj getAppManager()  throws ServiceLookupException 
    {
      return (IAppManagerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(IAppManagerHome.class.getName(),IAppManagerHome.class,new Object[]{});
    }
}