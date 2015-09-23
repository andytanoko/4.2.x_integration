package com.gridnode.pdip.base.transport.soap;

/**
 * This Interface defines a subset of KEY-MAPPING, required by
 * GTAS to Process SOAP Messages, independent of Message
 * Source.
 *
 */

public interface ISoapHeaderKeys
{

	public static final String EVENT_ID = "EventId";

	public static final String PACKAGE_TYPE_KEY = "x-gn-package-type";

	public static final String FILE_ID = "FileId";

	public static final String DOCUMENT_TYPE = "DocumentType";

	public static final String SENDER_UUID = "SenderUUID";

	public static final String SENDER_QUERY_URL = "SenderQueryURL";

	public static final String RECEIPENT_UUID = "ReceipentUUID";

	public static final String RECEIPENT_QUERY_URL = "ReceipentQueryURL";

	public static final String RN_VERSION_KEY = "x-RN-Version";

	public static final String[] HEADER_NAMES =
		new String[] {
			EVENT_ID,
			PACKAGE_TYPE_KEY,
			FILE_ID,
			DOCUMENT_TYPE,
			SENDER_UUID,
			SENDER_QUERY_URL,
			RECEIPENT_UUID,
			RECEIPENT_QUERY_URL,
			RN_VERSION_KEY };

}