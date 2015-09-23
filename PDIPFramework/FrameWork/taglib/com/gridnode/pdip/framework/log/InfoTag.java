package com.gridnode.pdip.framework.log;

import org.apache.log4j.Priority;

/** Logs an info message to the current log4j category.
  *
  */ 

public class InfoTag extends LoggerTag {

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7836966970886073996L;

		protected Priority getPriority() {
        return Priority.INFO;
    }
}
