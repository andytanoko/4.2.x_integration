package com.gridnode.pdip.framework.log.remote;

import org.apache.log4j.Logger;
import org.apache.log4j.Category;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;

public class RemoteLoggingEvent extends LoggingEvent implements java.io.Serializable  {
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4492903555427223778L;

	/** Hostname of machine from which this event originated. */
  public String hostName;
 
  /** Server Name */
        public String server;

  public RemoteLoggingEvent( String    fqnOfCategoryClass, Category  category,
                 Priority  priority, Object    message, Throwable throwable,String hostName,String server) {
    super( fqnOfCategoryClass,category,priority,message,throwable );
    this.hostName=hostName;
    this.server=server;

  }
  public RemoteLoggingEvent(LoggingEvent event,String hostName,String server) {
    super(event.fqnOfCategoryClass,Logger.getInstance(event.getLoggerName()),event.getLevel(),event.getMessage(),(event.getThrowableInformation()==null)?null:event.getThrowableInformation().getThrowable());
    this.hostName=hostName;
    this.server=server;
  }

}
