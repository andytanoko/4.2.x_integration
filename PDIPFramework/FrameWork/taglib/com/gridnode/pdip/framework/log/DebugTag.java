package com.gridnode.pdip.framework.log;

import org.apache.log4j.Priority;

/** Logs a debug message to the current log4j category.
  *
  */

public class DebugTag extends LoggerTag {
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6860733595240614949L;

		protected Priority getPriority() {
        return Priority.DEBUG;
    }
}
