package com.gridnode.pdip.framework.log;

import java.util.Enumeration;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

public class DumpTag extends TagSupport {
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8282835646265432042L;
		private int scope;


    public void setScope(String sc) throws JspException {
        if (sc.equalsIgnoreCase("session")) {
            this.scope = PageContext.SESSION_SCOPE;
        }
        else if (sc.equalsIgnoreCase("request")) {
            this.scope = PageContext.REQUEST_SCOPE;
        }
        else if (sc.equalsIgnoreCase("application")) {
            this.scope = PageContext.APPLICATION_SCOPE;
        }
        else if (sc.equalsIgnoreCase("page")) {
            this.scope = PageContext.PAGE_SCOPE;
        }
        else {
            throw new JspException(
                "Scope must be page, request, session or application."
            );
        }
    }

    public int doEndTag() throws JspException {
        try {
            Enumeration names = pageContext.getAttributeNamesInScope(scope);
            pageContext.getOut().write("<dl>");
            while(names.hasMoreElements()) {
                String name = (String) names.nextElement();
                Object value = pageContext.getAttribute(name, scope);

                pageContext.getOut().write("<dt><code>"+name+"</code></dt>");
                pageContext.getOut().write("<dd><code>"+value+"</code></dd>");
            }
            pageContext.getOut().write("</dl>");
        }
        catch (Exception e) {
            throw new JspException("Exception: "+e.getMessage());
        }
        return EVAL_PAGE;
    }
}
