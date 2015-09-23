package com.gridnode.pdip.base.appinterface.interfaces.adaptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;

import com.gridnode.pdip.base.appinterface.data.AppParam;
import com.gridnode.pdip.base.appinterface.exception.AppExecutionException;
import com.gridnode.pdip.base.appinterface.interfaces.IJavaProcedure;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class GenericJavaAdaptor implements IJavaProcedure {

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -9188937665051607450L;

	public GenericJavaAdaptor() {
  }

  //private String _name;
  private String _className;
  private String _methodName;
  private Vector _parameters;
  private int _appType = -1;
  
  public AppParam execute(AppParam param) throws AppExecutionException {
    Class jClass=null;
    Method jMethod=null;
    Object obj=null;
    try{
        jClass=Class.forName(getClassName());
        jMethod=jClass.getMethod(getMethodName(),(Class [])getParameters().toArray(new Class[]{}));
    }catch(Exception ex){ 
        throw new AppExecutionException("[GenericJavaAdaptor.execute] Unable to load method ClassName="+getClassName()+", MethodName="+getMethodName()+", ParamTypes"+getParameters()+", Parametes="+param.getAppDoc(),ex);
    }
    try{
        obj=jMethod.invoke(jClass.newInstance(),((Vector)param.getAppDoc()).toArray(new Object[]{}));
        param.setAppDoc(obj);
        return param;
    }catch (InvocationTargetException ex) {
        throw new AppExecutionException(ex.getTargetException().getMessage(),ex.getTargetException());
    }catch (Throwable th) {
        throw new AppExecutionException("[GenericJavaAdaptor.execute] Exception ,AppParam="+param,th);
    }
  }


  public String getClassName() {
    return _className;
  }
  public String getMethodName() {
    return _methodName;
  }
  public Vector getParameters() {
    return _parameters;
  }
  public int getAppType()
  {
    return _appType;
  }

  /************** dummy setters ******************/
  public void setClassName(String className) {
    _className=className;
  }
  public void setMethodName(String methodName) {
    _methodName=methodName;
  }
  public void setParameters(Vector parameters) {
    _parameters=parameters;
  }

  public void setAppType(int appType)
  {
    _appType=appType;
    
  }
}