package com.gridnode.pdip.framework.log.remote;

import org.apache.log4j.*;
import org.apache.log4j.helpers.PatternParser;

public class RemotePatternLayout extends PatternLayout {

	public RemotePatternLayout() {
		this(DEFAULT_CONVERSION_PATTERN);
	}

	public RemotePatternLayout(String pattern) {
	    super(pattern);
	}

	public PatternParser createPatternParser(String pattern) {
		return new RemotePatternParser( pattern == null ? DEFAULT_CONVERSION_PATTERN : pattern);
	}
}
