package com.gridnode.gtas.server.certificate.actions;

import java.util.Collection;

import com.gridnode.gtas.events.certificate.GetCertificateListEvent;
import com.gridnode.gtas.model.certificate.CertificateEntityFieldID;
import com.gridnode.gtas.server.certificate.helpers.CertificateLogger;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityListAction;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerHome;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;
import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.base.certificate.model.ICertificate;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;


public class GetCertificateListAction extends AbstractGetEntityListAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1159980721558504079L;
	private static final String ACTION_NAME = "GetCertificateListAction";

  public GetCertificateListAction()
  {
  }

  @Override
	protected String getActionName()
	{
		return ACTION_NAME;
	}

	@Override
	protected Class getExpectedEventClass()
	{
		return GetCertificateListEvent.class;
	}

  private ICertificateManagerObj getManager() throws ServiceLookupException
  {
    return (ICertificateManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
           ICertificateManagerHome.class.getName(),
           ICertificateManagerHome.class,
           new Object[0]);
  }

  protected Collection retrieveEntityList(IDataFilter filter) throws java.lang.Exception
  {
  	 //TWX 29082006 To ensure the certificates we retrieve are non-revoked cert 
  	 if(filter == null)
  	 {
  		 filter = new DataFilterImpl();
  		 filter.addSingleFilter(null, ICertificate.REVOKEID,filter.getEqualOperator(), new Integer(0), false);
  	 }
  	 else
  	 {
  		 filter.addSingleFilter(filter.getAndConnector(), ICertificate.REVOKEID,filter.getEqualOperator(), new Integer(0), false);
  	 }
  	 
     return getManager().getCertificate(filter);
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return Certificate.convertEntitiesToMap(
             (Certificate[])entityList.toArray(new Certificate[entityList.size()]),
             CertificateEntityFieldID.getEntityFieldID(),
             null);
  }

  protected String getListIDPrefix()
  {
    return "CertificateInfoListCursor.";
  }

}


