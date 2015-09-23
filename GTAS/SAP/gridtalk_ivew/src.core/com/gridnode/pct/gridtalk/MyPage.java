package com.gridnode.pct.gridtalk;

import java.util.HashMap;
import java.util.Map;

import com.sap.security.api.IUser;
import com.sap.security.api.umap.IUserMappingData;
import com.sap.security.api.umap.NoLogonDataAvailableException;
import com.sapportals.htmlb.*;
import com.sapportals.htmlb.enum.*;
import com.sapportals.htmlb.event.*;
import com.sapportals.htmlb.page.*;
import com.sapportals.portal.htmlb.page.*;
import com.sapportals.portal.prt.component.*;
import com.sapportals.portal.prt.runtime.PortalRuntime;
import com.sapportals.portal.prt.service.usermapping.IUserMappingService;

public class MyPage extends PageProcessorComponent {
	private static LoginInfoBean loginInfoBean = new LoginInfoBean();
	
	public DynPage getPage() {
		//DO NOTHING
		return null;
	}
	public DynPage getPage(
		IPortalComponentRequest aRequest,
		IPortalComponentResponse aResponse) {
		getUsermapping(aRequest, aResponse);
		return new MyPageDynPage();
	}

	private void getUsermapping(
		IPortalComponentRequest request,
		IPortalComponentResponse response) {
		// obtain system 
		String systemalias = "GridNode_GridTalk";

		// get user from request 
		IUser iuser = request.getUser();

		// get usermapping service 
		IUserMappingService iums =
			(IUserMappingService) PortalRuntime
				.getRuntimeResources()
				.getService(
				IUserMappingService.KEY);
		IUserMappingData iumd = iums.getMappingData(systemalias, iuser);
		Map map = new HashMap();
		try {
			iumd.enrich(map);
		} catch (NoLogonDataAvailableException nldae) {
			throw new RuntimeException(nldae);

		}

		// In m is all data
		String username = (String) map.get("user");
		String password = (String) map.get("mappedpassword");
		loginInfoBean.setUsername(username);
		loginInfoBean.setPassword(password);
	}

	public static class MyPageDynPage extends JSPDynPage {

		public void doInitialization() {
		}

		public void doProcessAfterInput() throws PageException {
		}

		public void doProcessBeforeOutput() throws PageException {
			((IPortalComponentRequest) getRequest())
				.getServletRequest()
				.setAttribute(
				"loginInfoBean",
				loginInfoBean);
			this.setJspName("JSPDyn.jsp");
		}
	}
}
