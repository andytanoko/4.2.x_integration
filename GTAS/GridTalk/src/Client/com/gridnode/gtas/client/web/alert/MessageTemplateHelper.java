package com.gridnode.gtas.client.web.alert;

public class MessageTemplateHelper
{
	public static String getMessagePropertyError(int i)
	{
		return "messageProperties[" + i
		+ "].error.keyAndTypeAndValue.required";
	}
}
