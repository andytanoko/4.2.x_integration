<%@ taglib uri="prt:taglib:tagLib" prefix="hbj" %>

<%@ page import="com.gridnode.pct.gridtalk.*" %>
<%@ page import="com.sapportals.htmlb.*" %>

<jsp:useBean id="loginInfoBean" scope="request" class="com.gridnode.pct.gridtalk.LoginInfoBean" />

<%!
	private IPortalComponentRequest componentRequest;
	private LoginInfoBean loginInfoBean;
	
	private void setLink(Link link, String nwForward)
	{
		String username = loginInfoBean.getUsername();
		String password = loginInfoBean.getPassword();
		
		String url = "http://soklay:8080/gridtalk/login.do?username=" + username
				+ "&password=" + password + "&nw_forward=" + nwForward;
		link.setReference(url);
	}
%>

<%
	this.componentRequest = componentRequest;
	this.loginInfoBean = loginInfoBean;
	final String IMAGE_URL = componentRequest.getPublicResourcePath() + "/images/logo.gif";
%>

<hbj:content 
	id="myContext">
	<hbj:page 
		title="GridTalk Iview">
		<hbj:form 
			id="myFormId">
			<hbj:image
				id="logo_image"
				alt="logo image"
				src="<%=IMAGE_URL%>" />
			<br/>
			<hbj:textView
				text="GridTalk Java Iview"
				design="HEADER2" />
				
			<br />
			<br />
			<hbj:textView
				text="Please select the following links to enter the respective areas."
				design="STANDARD" />
			<br />
			<hbj:link 
				id="documentsLink" 
				text="Documents" 
				target="_blank">
				<%
					setLink(documentsLink, "documents");
				%>

			</hbj:link>
			<br />
			<hbj:link 
				id="processLink" 
				text="Process" 
				target="_blank">
				<%
					setLink(processLink, "process");
				%>

			</hbj:link>
			<br />
			<hbj:link 
				id="registryLink" 
				text="Registry" 
				target="_blank">
				<%
					setLink(registryLink, "registry");
				%>

			</hbj:link>
			<br />
			<hbj:link 
				id="partnerLink" 
				text="Partner" 
				target="_blank">
				<%
					setLink(partnerLink, "partner");
				%>

			</hbj:link>
			<br />
			<hbj:link 
				id="processInstancesLink" 
				text="Process Instances" 
				target="_blank">
				<%
					setLink(processInstancesLink, "processInstances");
				%>

			</hbj:link>
		</hbj:form>
	</hbj:page>
</hbj:content>