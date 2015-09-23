package com.gridnode.pdip.framework.log;

import org.apache.log4j.Priority;

/** Logs an error message to the current log4j category.
  *
  */
 
public class ErrorTag extends LoggerTag {

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1405031388927134655L;

		protected Priority getPriority() {
        return Priority.ERROR;
    }
}
