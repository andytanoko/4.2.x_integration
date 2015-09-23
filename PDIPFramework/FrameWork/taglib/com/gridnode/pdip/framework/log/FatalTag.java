package com.gridnode.pdip.framework.log;

import org.apache.log4j.Priority;

/** Logs a fatal message to the current log4j category.
  *
  */ 

public class FatalTag extends LoggerTag {

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -201974143503442852L;

		protected Priority getPriority() {
        return Priority.FATAL;
    }
}

