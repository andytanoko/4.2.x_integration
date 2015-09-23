<%@ page import="java.util.*,
                java.io.*,
                java.net.*,
                org.apache.log4j.*,
                com.gridnode.pdip.framework.log.*,
                com.gridnode.pdip.framework.config.*,
                com.gridnode.pdip.framework.log.remote.*,
                org.apache.log4j.helpers.*,
                org.apache.log4j.spi.LoggingEvent"

%>

<%!
	boolean isLogConfigured=false;
	static{
		configureLog();
	}

	private Vector readLoggingEvent(InputStream iStream){
            Vector eventsVector=new Vector();
            RemoteLoggingEvent event=null;
            try{
                ObjectInputStream ois=new ObjectInputStream(iStream);
                int ch=-1;
                while(true){
                    try{
                        eventsVector.add(ois.readObject());
                    }catch(Exception e){
                        if(e.toString().indexOf("EOFException")>-1) break;
                        else LogLog.warn("Error in reading LoggingEvent ",e);
                    }
                }
            }catch(Exception e){
                LogLog.warn(" Error in readLoggingEvent",e);
                e.printStackTrace();
            }
            return eventsVector;
	}

	private void processLoggingEvents(Vector eventsVector){
            try{
                Category cat=Category.getInstance(ILog.LOG_CATEGORY_SERVER);
                while(eventsVector.size()>0){
                    try{
                        cat.callAppenders((LoggingEvent)eventsVector.remove(0));
                    }catch(Exception e){
                    }
                }
            }catch(Exception e){
                LogLog.warn(" Error in processEvent ",e);
            }
	}

	private static void  configureLog(){
			LogLog.warn("Before Configure Log ");
            try{
                if(!Category.getRoot().equals(Category.getInstance(ILog.LOG_CATEGORY_SERVER))){
                    Properties prop=ConfigManager.getInstance(Log.LOG_CONFIG).getProperties();
                    PropertyConfigurator.configure(prop);
					LogLog.warn("After Configure Log ");
				}
            }catch(Exception e){
                LogLog.warn(" Error in configuring log ",e);
            }
	}
%>
<%
	Vector eventsVector=readLoggingEvent(request.getInputStream());
	if(eventsVector.size()>0)
            processLoggingEvents(eventsVector);
%>