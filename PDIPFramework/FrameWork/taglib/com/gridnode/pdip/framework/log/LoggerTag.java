package com.gridnode.pdip.framework.log;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Category;
import org.apache.log4j.Priority;

/** Abstract base class for the logging tags which log a message to a
  * log4j category.
  *
  */

public abstract class LoggerTag extends BodyTagSupport {

    private String category;
    private String message;


    public void setCategory(String category) {
        this.category = category;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Tag interface
    //-------------------------------------------------------------------------
    public int doStartTag() throws JspException  {
        if ( message != null ) {
            Category logCategory = getLoggingCategory();
            Priority priority = getPriority();
            if ( logCategory.isEnabledFor( priority ) ) {
                // Log now as doAfterBody() may not be called for an empty tag
                logCategory.log( priority, message );
            }
            return SKIP_BODY;
        }
        return EVAL_BODY_BUFFERED;
    }

    public int doAfterBody() throws JspException {
        if (message == null) {
            Category logCategory = getLoggingCategory();
            Priority priority = getPriority();
            if ( logCategory.isEnabledFor( priority ) ) {
                logCategory.log( priority, getBodyContent().getString().trim() );
            }
        }
        return SKIP_BODY;
    }

    // Implementation methods
    //-------------------------------------------------------------------------
    protected abstract Priority getPriority();

    protected Category getLoggingCategory() {
        if ( category == null ) {
            return Category.getRoot();
        }
        else {
            return Category.getInstance( category );
        }
    }
}

