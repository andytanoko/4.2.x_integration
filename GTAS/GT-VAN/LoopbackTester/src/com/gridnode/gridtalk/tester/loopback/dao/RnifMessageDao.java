/**
 * 
 */
package com.gridnode.gridtalk.tester.loopback.dao;

import com.gridnode.gridtalk.tester.loopback.entity.RnifMessageEntity;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author YiJun.Liu
 *
 */
public class RnifMessageDao implements IRnifMessageDao{
	
	public RnifMessageDao()
	{
		
	}
	
	public RnifMessageEntity getRnifMessageTemplate(String pipCode) throws MessageDaoException
	{
		String mn = "getRnifMessageTemplate";
		RnifMessageEntity rne = null;
			try
			{
				rne = new RnifMessageEntity(pipCode);
			}
			catch (FileNotFoundException e)
			{
				throw new MessageDaoException("Properties file not found. PIP Code: "+pipCode, e);
			}
			catch (IOException e)
			{
				throw new MessageDaoException("Error reading properties file. PIP Code: "+pipCode, e);
			}
			return rne;
	}
	
//	private RnifMessageEntity getGtObRnif()
//	{
//		RnifMessageEntity rne = new RnifMessageEntity();
//		rne.setPipInstIdPath(ConfigMgr.getMainConfig().getGtOutboundRnifPipInstIdPath());
//		rne.setDocIdPath(ConfigMgr.getMainConfig().getGtOutboundRnifDocIdPath());
//		return rne;
//	}
//	
//	private RnifMessageEntity getGtIbRnif()
//	{
//		RnifMessageEntity rne = new RnifMessageEntity();
//		rne.setParams(ConfigMgr.getRnifHubConfig().getActionProperties());
//		rne.setTemplate(ConfigMgr.getRnifHubConfig().getActionTemplate());
//		return rne;		
//	}
//	
//	private void logWarn(String methodName, String message, Throwable t)
//	{
//		Logger.warn(this.getClass().getSimpleName(), methodName, message, t);
//	}
//	
//	private void logError(String methodName, String message, Throwable t)
//	{
//		Logger.error(this.getClass().getSimpleName(), methodName, message, t);
//	}
}
