package com.gridnode.gtas.client.netweaver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.web.strutsbase.GTActionBase;
import com.gridnode.pdip.framework.util.AssertUtil;

public class NwInitialBodyAction extends GTActionBase
{
	public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response)
  throws Exception
  {
		HttpSession session = request.getSession();
		String nwForward = (String) session.getAttribute("nw_forward");
		ActionForward forward = mapping.findForward(nwForward);
		AssertUtil.assertTrue(forward != null);
		return forward;
  }
}
