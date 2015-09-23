package com.gridnode.pdip.framework.log.remote;

import org.apache.log4j.helpers.FormattingInfo;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.helpers.PatternParser;
import org.apache.log4j.spi.LoggingEvent;

public class RemotePatternParser extends PatternParser{

	protected static final char HOSTNAME_CHAR  = 'h';
	protected static final char SERVER_CHAR    = 's';

	public RemotePatternParser(String pattern) {
		super(pattern);
	}



	public void finalizeConverter(char c) {
		PatternConverter pc = null;
		switch( c )
		{

			case HOSTNAME_CHAR:
				pc = new HostnamePatternConverter( formattingInfo );
				currentLiteral.setLength(0);
				addConverter( pc );
			   break;
			case SERVER_CHAR:
				pc = new ServerPatternConverter( formattingInfo );
				currentLiteral.setLength(0);
				addConverter( pc );
			   break;
			default:
				super.finalizeConverter( c );
      }
	}

	private static abstract class RemotePatternConverter extends PatternConverter {
		RemotePatternConverter(FormattingInfo formattingInfo) {
			super(formattingInfo);
		}

		public String convert(LoggingEvent event) {
			String result = null;
			RemoteLoggingEvent rEvent = null;

			if ( event instanceof RemoteLoggingEvent )
			{
				rEvent = (RemoteLoggingEvent) event;
				result = convert( rEvent );
			}
			return result;
		}

		public abstract String convert( RemoteLoggingEvent event );
	}

	private static class HostnamePatternConverter extends RemotePatternConverter{

		HostnamePatternConverter( FormattingInfo formatInfo ){
			super( formatInfo );
		}

		public String convert( RemoteLoggingEvent event ){
			return event.hostName;
		}
	}

	private static class ServerPatternConverter extends RemotePatternConverter{

		ServerPatternConverter( FormattingInfo formatInfo ){
			super( formatInfo );
		}

		public String convert( RemoteLoggingEvent event ){
			return event.server;
		}
	}

}
