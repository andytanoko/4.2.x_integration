/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TptMappingFactory.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 29, 2005   i00107              Treat header mapping xml filename as relative
 *                                    to SystemUtil.workingDir if not absolute.
 */

package com.gridnode.pdip.base.transport.helpers;

/**
 * <p>
 * Title: PDIP : Peer Data Interchange Platform
 * </p>
 * <p>
 * Description: Transport Module - for PDIP GT(AS)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: GridNode Pte Ltd - Singapore
 * </p>
 *
 * @author unascribed
 * @version 1.0
 */

import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.util.SystemUtil;

import java.io.File;
import java.util.Map;
import java.util.HashMap;

public class TptMappingFactory
{

	private Map _mappingRegistry = new HashMap();
	private Configuration tptConfig =
		ConfigurationManager.getInstance().getConfig(ITransportConfig.CONFIG_NAME);

	private static TptMappingFactory _factory = new TptMappingFactory();

	private TptMappingFactory()
	{
	}

	public static TptMappingFactory getInstance()
	{
		return _factory;
	}

	public TptMappingRegistry getMappingRegistry(String protocol)
	{
		if (checkNullEmpty(tptConfig.getString(ITransportConfig.HEADER_MAPPING_PREFIX + protocol)))
			return null;
		if (_mappingRegistry.get(protocol) == null)
		{
			TptMappingRegistry registry = loadMappingRegistry(protocol);
			if (null == registry)
				return null;
			else
				_mappingRegistry.put(protocol, registry);
		}
		return (TptMappingRegistry) _mappingRegistry.get(protocol);
	}

	/**
	 * This mehtods Constructs TptMappingRegistry for the given protocol, by
	 * parsing the protocol specific header mapping xml document.
	 *
	 * @param protocol
	 * @return
	 */

	private TptMappingRegistry loadMappingRegistry(String protocol)
	{
		String tptProtocolMappingxml =
			tptConfig.getString(ITransportConfig.HEADER_MAPPING_PREFIX + protocol);
		if (null == tptProtocolMappingxml)
			return null;
		else
		{
			try
			{
        //NSL20051129 If mapping file is not absolute path, use it relative tp the SystemUtil.workingDir. 
        if (!new File(tptProtocolMappingxml).isAbsolute())
        {
          tptProtocolMappingxml = new File(SystemUtil.getWorkingDirPath(), tptProtocolMappingxml).getCanonicalPath();
        }
				System.out.println("[Mapping File=]["+tptProtocolMappingxml+"]");
				TptMappingRegistry registry =
					new TptMappingRegistry(tptProtocolMappingxml);
				return registry;
			}
			//catch (org.jdom.JDOMException ex)
      catch (Exception ex)
			{
				ex.printStackTrace(System.out);
				return null;
			}
		}
	}

        private  boolean checkNullEmpty(String checkString)
        {
          return (checkString == null || checkString.equals(""));
        }


	/**
	 *
	 */
	public static void main(String[] args)
	{
		/*
		 * TptMappingRegistry registry =
		 * TptMappingFactory.getInstance().getMappingRegistry("http");
		 * TptMapping mapping = registry.getTptMapping("rosetta_package");
		 * //hear goes your tpt Header... take care of it.... Map tptHeader =
		 * mapping.getMappedHeader(new HashMap());
		 */
	}

}