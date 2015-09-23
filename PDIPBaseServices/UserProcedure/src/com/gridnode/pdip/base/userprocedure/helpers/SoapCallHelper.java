package com.gridnode.pdip.base.userprocedure.helpers;

import java.lang.reflect.Method;
import java.util.*;

import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Operation;
import javax.wsdl.Port;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.xml.namespace.QName;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.utils.bytecode.ParamNameExtractor;
import org.apache.axis.utils.bytecode.ParamReader;
import org.apache.axis.wsdl.gen.Parser;
import org.apache.axis.wsdl.symbolTable.*;

import com.ibm.wsdl.extensions.soap.SOAPAddressImpl;

public class SoapCallHelper extends SoapCall
{
  Parser    wsdlParser;
  String    WSDLURL;

  public String getServiceNameFromWSDL() throws Exception
  {
    javax.wsdl.Service service = selectService(null, null);
    Port port = selectPort(service.getPorts(), null);
    return port.getName();
  }

  protected Port selectPort() throws Exception
  {
    javax.wsdl.Service service = selectService(null, null);
    return selectPort(service.getPorts(), null);
  }

  public String getServiceURLFromWSDL() throws Exception
  {
    javax.wsdl.Service service = selectService(null, null);
    Port port = selectPort(service.getPorts(), null);
    SOAPAddressImpl address = (SOAPAddressImpl)port.getExtensibilityElements().toArray()[0];
    return address.getLocationURI();
  }

  public String getSOAPActionURI(String operationName)
  {
    String action = "";
    try
    {
      Binding binding = selectPort().getBinding();
      BindingOperation bop = binding.getBindingOperation(operationName, null, null);
      List list = bop.getExtensibilityElements();
      for (Iterator i = list.iterator(); i.hasNext(); )
      {
        Object obj = i.next();
        if (obj instanceof SOAPOperation)
        {
          SOAPOperation sop = (SOAPOperation)obj;
          action = sop.getSoapActionURI();
          break;
        }
      }
    }
    catch (Exception ex)
    {
      action = "";
    }
    return action;
  }

  static public Parser createparser(String wsdlURL) throws Exception
  {
    Parser wsdlParser = new Parser();
    wsdlParser.run(wsdlURL);
    return wsdlParser;
  }

  static protected SymTabEntry getSymTabEntry(Parser wsdlParser, QName qname, Class cls)
  {
    HashMap map = wsdlParser.getSymbolTable().getHashMap();
    Iterator iterator = map.entrySet().iterator();

    while (iterator.hasNext())
    {
      Map.Entry entry = (Map.Entry) iterator.next();
      //QName key = (QName) entry.getKey();
      Vector v = (Vector) entry.getValue();

      if ((qname == null) || qname.equals(qname))
      {
        for (int i = 0; i < v.size(); ++i)
        {
          SymTabEntry symTabEntry = (SymTabEntry) v.elementAt(i);
          if (cls.isInstance(symTabEntry))
          {
              return symTabEntry;
          }
        }
      }
    }
    return null;
  }

  protected javax.wsdl.Service selectService(String serviceNS, String serviceName)
    throws Exception
  {
    QName serviceQName = (((serviceNS != null)
                && (serviceName != null))
                ? new QName(serviceNS, serviceName)
                : null);
    ServiceEntry serviceEntry = (ServiceEntry) getSymTabEntry(wsdlParser, serviceQName,
                                                              ServiceEntry.class);
    return serviceEntry.getService();
  }

  protected Port selectPort(Map ports, String portName) throws Exception
  {
    Iterator valueIterator = ports.keySet().iterator();
    while (valueIterator.hasNext())
    {
      String name = (String) valueIterator.next();

      if ((portName == null) || (portName.length() == 0))
      {
        Port port = (Port) ports.get(name);
        List list = port.getExtensibilityElements();

        for (int i = 0; (list != null) && (i < list.size()); i++)
        {
          Object obj = list.get(i);
          if (obj instanceof SOAPAddress)
          {
            return port;
          }
        }
      }
      else if ((name != null) && name.equals(portName))
      {
        return (Port) ports.get(name);
      }
    }
    return null;
  }


  private void Init(Parser wsdlParser) throws Exception
  {
    this.wsdlParser = wsdlParser;
    javax.wsdl.Service aservice = selectService(null, null);
    Port port = selectPort(aservice.getPorts(), null);
    service = new org.apache.axis.client.Service(aservice.getQName());
    call = (Call)service.createCall(new QName(port.getName()));
    call.setUseSOAPAction(true);
    call.setMaintainSession(true);
  }

  public SoapCallHelper() throws Exception
  {
    super();
  }

  public SoapCallHelper(Parser wsdlParser) throws Exception
  {
    Init(wsdlParser);
  }

  public SoapCallHelper(String WSDLURL) throws Exception
  {
    this.WSDLURL = WSDLURL;
    Init(createparser(WSDLURL));
  }

  public Parser getParser()
  {
    return wsdlParser;
  }

  public String getWSDLURL()
  {
    return WSDLURL;
  }

  static protected boolean isMethodFromObject(Object[] id)
  {
    Class obc = new Object().getClass();
    Method[] amethods = obc.getMethods();
    for(int i = 0; i < amethods.length;i++)
    {
      Object[] oid = methodsToDetails(amethods[i], obc, null);
      if(oid[4].equals(id[4]))
        return true;
    }
    return false;
  }

  static public String saveServiceWSDD(String serviceName, String className, String methods, String scope)
    throws Exception
  {
    StringBuffer sb = new StringBuffer();
    sb.append("<deployment xmlns=\"http://xml.apache.org/axis/wsdd/\"\r\n");
    sb.append("xmlns:java=\"http://xml.apache.org/axis/wsdd/providers/java\">\r\n");
    sb.append("<service name=\"" + serviceName + "\" provider=\"java:RPC\">\r\n");
    sb.append("<parameter name=\"className\" value=\"" + className + "\"/>\r\n");
    sb.append("<parameter name=\"scope\" value=\"" + scope + "\"/>\r\n");
    sb.append("<parameter name=\"allowedMethods\" value=\"" + methods + "\"/>\r\n");
    sb.append("</service>\r\n");
    sb.append("</deployment>\r\n");
    return sb.toString();
  }

  public Class convertType(Parameter p)
  {
    try
    {
        QName paramType = org.apache.axis.wsdl.toJava.Utils.getXSIType(p);
        return getJavaType(paramType);
    }
    catch (Exception ex)
    {
    }
    return null;
  }

  public Class getJavaType(QName name) throws Exception
  {
  /*
      TypeMappingRegistry tmr = call.getMessageContext().getTypeMappingRegistry();
      TypeMapping tm = tmr.getOrMakeTypeMapping(call.getMessageContext().getEncodingStyle());
      Class typeClass = tm.getClassForQName(name);
      if(typeClass != null)
        return typeClass;
*/
    String type = name.getLocalPart();

    HashMap map = new HashMap();
    map.put("string",String.class);
    map.put("int",Integer.class);
    map.put("float",Float.class);
    map.put("boolean",Boolean.class);
    map.put("double",Double.class);
    map.put("base64Binary",byte[].class);
    map.put("Vector",Vector.class);

    map.put("ArrayOf_xsd_string",String[].class);
    map.put("ArrayOf_xsd_int",Integer[].class);
    map.put("ArrayOf_xsd_float",Float[].class);
    map.put("ArrayOf_xsd_boolean",Boolean[].class);
    map.put("ArrayOf_xsd_double",Double[].class);
    map.put("ArrayOf_xsd_base64Binary",byte[][].class);

    map.put("ArrayOf_apachesoap_DataHandler", javax.activation.DataHandler[].class);
    map.put("ArrayOfArrayOf_xsd_string", java.lang.String[][].class);
    map.put("DataHandler", javax.activation.DataHandler.class);
    Object ob = map.get(type);
    if(ob != null)
      return (Class)ob;
    else
    {
      System.out.println("getJavaType not found for:" + type);
      return null;
    }
  }

  static public String getShortJavaString(String s)
  {
    if(s == null || s.length() <= 0)
      return "";
    String sh = "";
    for(int i = 0 ; i < s.length();i++)
    {
      if(s.charAt(i) == '[')
       sh += "[]";
    }
    if(s.indexOf(".") >= 0)
     s = s.substring(s.lastIndexOf(".") + 1);
    while(s != null && s.length() > 0 && s.charAt(0) == '[')
      s = s.substring(1);
    while(s != null && s.length() > 0 && s.charAt(s.length() -1) == ';')
      s = s.substring(0, s.length() -1);
    if(s.equals("B"))
     s = "byte";
    return s + sh;
  }

  static public String getParaTypeString(Object c)
  {
    if(c == null)
      return "";
    else
      return ((Class)c).getName();
  }

  static public String getReturnTypeString(Object c)
  {
    if(c == null)
      return "void";
    else
      return ((Class)c).getName();
  }

  static public void printMethodDetails(Vector methods)
  {
    System.out.println("Methods Details no[" + methods.size() + "]");
    for(int i = 0; i < methods.size();i++)
    {
      Object[] aserviceDetail = (Object[])methods.get(i);
      String str = getShortJavaString(getReturnTypeString(aserviceDetail[1]));
      str += " " + (String)aserviceDetail[0] + "(";
      String[] para = (String[])aserviceDetail[2];
      Object[] paraType = (Object[])aserviceDetail[3];
      String   opid = (String)aserviceDetail[4];
      if(para != null && para.length > 0)
      {
        for(int j = 0; j < para.length; j++)
        {
          if(j!=0)
           str += ",";
          str += getShortJavaString(getParaTypeString(paraType[j])) + " " + para[j];
        }
      }
      str += ") [" + opid + "]";
      System.out.println(str);
    }
  }

  public static String[] getParameterNamesFromDebugInfo(SoapLocalClassLoader loader, Method method)
  {
    try
    {
      int numParams = method.getParameterTypes().length;
      if(numParams == 0)
          return null;
      String names[];
      ParamReader pr = new ParamReader(loader.loadClassBytes(method.getDeclaringClass()));
      names = pr.getParameterNames(method);
      return names;
    }
    catch (Exception ex)
    {
    }
    return null;
  }

  public static String[] getParameterNamesFromDebugInfo(Method method)
  {
    //ParamNameExtractor extra = new ParamNameExtractor();
    return ParamNameExtractor.getParameterNamesFromDebugInfo(method);
  }

  public String getResponseString() throws AxisFault
  {
    String ret = "";
    Object ob = getResponse();
    if(ob == null)
      return "";
    if(ob instanceof String[])
    {
      String[] str = (String[])ob;
      StringBuffer buf = new StringBuffer();
      buf.append("Array[]\r\n");
      for(int i = 0; i < str.length;i++)
      {
        buf.append("" + i + " [" + str[i] + "]\r\n")  ;
      }
      ret = buf.toString();
    }
    else if(ob instanceof byte[])
    {
        ret = new String((byte[])ob);
    }
      else
        ret = ob.toString();
    return ret;
  }

  static public String[] getDefaulParameterNameList(int no)
  {
    String[] paran = new String[no];
    for(int i = 0 ; i < no; i++)
      paran[i] = "arg" + i;
    return paran;
  }

  static protected Vector filterPublicMethod(Class aclass, Vector methods)
  {
    Method[] amethods = aclass.getMethods();
    if(amethods == null || amethods.length == 0 || methods == null || methods.size() == 0)
      return new Vector();
    Vector filterMethods = new Vector();
    for(int i = 0; i< amethods.length; i++)
    {
      if(amethods[i].getModifiers() == 1)
          filterMethods.add(methods.get(i));
    }
    return filterMethods;
    /*
    methods = filterMethods;
    filterMethods = new Vector();
    for(int i = 0; i< methods.size(); i++)
    {
      //if(!isMethodFromObject((Object[])methods.get(i)))
          filterMethods.add(methods.get(i));
    }
    return filterMethods;
    */
  }

  static public Vector getMethodDetailsFromClass(Class aclass, SoapLocalClassLoader loader)
  {
    Vector methods = new Vector();
    Method[] amethods = aclass.getMethods();
    if(amethods != null)
    {
      for(int i = 0 ; i < amethods.length;i++)
      {
        if(amethods[i].getDeclaringClass() != Object.class && amethods[i].getModifiers() == 1)
          methods.add(methodsToDetails(amethods[i], aclass, loader));
      }
    }
    return methods;
    //return filterPublicMethod(aclass, methods);
  }

  static public Vector getMethodDetailsFromClass(Class aclass)
  {
    return getMethodDetailsFromClass(aclass, null);
  }

  static public Vector getMethodDetailsFromWSDL(String wsdlURL) throws Exception
  {
    return new SoapCallHelper(wsdlURL).getMethodDetailsFromWSDL();
  }

  public Vector getMethodDetailsFromWSDL() throws Exception
  /**
   * 0 name
   * 1 Class return type
   * 2 String[] para names
   * 3 Class[] para types
   * 4 String id   *
   * 5 Boolean self
   */
  {
    Binding binding = selectPort().getBinding();
    SymbolTable symbolTable = wsdlParser.getSymbolTable();
    BindingEntry bEntry = symbolTable.getBindingEntry(binding.getQName());
    Iterator i = bEntry.getParameters().keySet().iterator();
    Vector methods = new Vector();
    while (i.hasNext())
    {
      Operation o = (Operation) i.next();
      Object[] md = new Object[6];


      md[0] = o.getName();
      md[5] = new Boolean(true);
      Parameters parameters = (Parameters) bEntry.getParameters().get(o);
      md[1] = convertType(parameters.returnParam);

      Vector parlist = parameters.list;
      String[] parNameV = new String[parlist.size()];
      Class[]  parTypeV = new Class[parlist.size()];
      for(int j = 0;j < parlist.size();j++)
      {
        Parameter para = (Parameter)parlist.get(j);
        parNameV[j] = para.getName();
        parTypeV[j] = convertType(para);
      }
      md[2] = parNameV;
      md[3] = parTypeV;
      serialMethodsID(md);
      methods.add(md);
    }
    return methods;
  }

  public String[] listAllMethodsFromWSDL() throws Exception
  {
    Vector methods = getMethodDetailsFromWSDL();
    String[] methodsarray = new String[methods.size()];
    for(int j = 0; j < methods.size(); j++)
    {
      methodsarray[j] = (String)((Object[])methods.get(j))[0];
    }
    return methodsarray;
  }

  static public String methodDetailsToString(Object[] aserviceDetail)
  {
    String str = getShortJavaString(getReturnTypeString(aserviceDetail[1]));
    str += " " + (String)aserviceDetail[0] + "(";
    String[] para = (String[])aserviceDetail[2];
    Object[] paraType = (Object[])aserviceDetail[3];
    //String   opid = (String)aserviceDetail[4];
    if(paraType != null && paraType.length > 0)
    {
      for(int j = 0; j < paraType.length; j++)
      {
        if(j!=0)
         str += ",";
        str += getShortJavaString(getParaTypeString(paraType[j])) + " " + para[j];
      }
    }
    str += ")";
    return str;
  }

  static public int deserialMethodsID(Vector operations, String id)
  {
    for(int i = 0; i < operations.size(); i++)
      if(id.equals(((Object[])operations.get(i))[4]))
        return i;
    return -1;
  }

  static public Object[] methodsToDetails(Method amethod, Class aClass, SoapLocalClassLoader loader)
    /**
   * 0 name
   * 1 Class return type
   * 2 String[] para names
   * 3 Class[] para types
   * 4 String id   *
   * 5 Boolean self
   */
  {
    Object[] md = new Object[6];
    md[0] = amethod.getName();
    md[1] = amethod.getReturnType();
    md[3] = amethod.getParameterTypes();
    int parano = 0;
    if(md[3] != null)
      parano = ((Class[])md[3]).length;
    if(parano > 0)
    {
      if(loader != null)
        md[2] = getParameterNamesFromDebugInfo(loader, amethod);
      else
        md[2] = getParameterNamesFromDebugInfo(amethod);
      if(md[2] == null)
        md[2] = getDefaulParameterNameList(parano);
    }
    serialMethodsID(md);
    Boolean selfMethod = new Boolean(amethod.getDeclaringClass().equals(aClass));
    md[5] = selfMethod;
    return md;
  }

  static public String serialMethodsID(Object[] aserviceDetail)
  {
    String oname = (String)aserviceDetail[0];
    //Class returntype = null;
    String   typastr = oname + ",";
    if(aserviceDetail[1] != null)
       typastr += ((Class)aserviceDetail[1]).getName();
    else
       typastr += "void";
    //String[] para = (String[])aserviceDetail[2];
    Class[] paraType = null;
    if(aserviceDetail[3] != null)
      paraType = (Class[])aserviceDetail[3];
    if(paraType != null && paraType.length >= 1)
    {
      for(int i = 0; i < paraType.length;i++)
      {
        typastr += "," + paraType[i];
      }
    }
    typastr = "" + typastr.hashCode();
    aserviceDetail[4] = typastr;
    return  typastr;
  }

  public void setOperationName(String operationName)
  {
    call.setOperationName(operationName);
    String action = getSOAPActionURI(operationName);
    Logger.debug("[SoapCallHelper.setOperationName] SOAPActionURI ="+action);
    call.setSOAPActionURI(action);
  }

//  public static void main(String[] args) throws Exception
//  {
//    String url ="http://localhost/WebServicesProvider/services/BackendService?wsdl";
//    SoapCallHelper call = new SoapCallHelper(url);
//    //FileInputStream in = new FileInputStream("C:\\gtas\\catalina\\webapps\\axis\\WEB-INF\\jwsClasses\\EchoHeaders.class");
//    //byte[] data = new byte[in.available()];
//    //in.read(data);
//    printMethodDetails(call.getMethodDetailsFromWSDL());
// }

  public static void main(String[] arhsguejfjg)
  {
    try
    {
      java.io.File aFile = new java.io.File("C:/temp/WebServices/BackendService.xml");
      System.out.println("WOWOWOWOWOW "+aFile.toURL().toString());
      SoapCallHelper helper = new SoapCallHelper("file:/C:/temp/WebServices/BackendService.xml");
      String action = helper.getSOAPActionURI("backendImport3");
      System.out.println("SoapAction ="+action+"=");


//      Collection methods = helper.getMethodDetailsFromWSDL();
//      for (Iterator i = methods.iterator(); i.hasNext(); )
//      {
//        Object[] details = (Object[])i.next();
//        for (int j = 0; j < details.length; j++)
//        {
//          System.out.println(details[j]);
//        }
//        System.out.println("***************************");
//      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }


}