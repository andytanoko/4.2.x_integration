package com.gridnode.pdip.framework.log;

import org.apache.log4j.Priority;

/** Logs a warning message to the current log4j category.
  *
  */

public class WarnTag extends LoggerTag {
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3483785077980347241L;

		protected Priority getPriority() {
        return Priority.WARN;
    }
}

