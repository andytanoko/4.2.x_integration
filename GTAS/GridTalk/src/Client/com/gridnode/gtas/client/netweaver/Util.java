package com.gridnode.gtas.client.netweaver;

import org.w3c.dom.*;

public class Util
{
//	public static void removeCssAttribute(Document document)
//	{
//		Element element = document.getDocumentElement();
//		removeCssAttributeHelper(element);
//	}
//	
//	public static void removeCssAttributeHelper(Element element)
//	{
//		element.removeAttribute("class");
//		element.removeAttribute("style");
//		NodeList list = element.getChildNodes();
//		int length = list.getLength();
//		for (int i = 0; i < length; i++)
//		{
//			Node node = list.item(i);
//			if (node instanceof Element)
//			{
//				removeCssAttributeHelper((Element) node);
//			}
//		}
//	}
//	
//	public static void removeAllElement(Document document, String elementName)
//	{
//		Element element = document.getDocumentElement();
//		removeAllElementHelper(element, elementName);
//	}
//	
//	public static void removeAllElementHelper(Element element, String elementName)
//	{
//		String name = element.getNodeName();
//		if (elementName.equals(name))
//		{
//			Node parent = element.getParentNode();
//			parent.removeChild(element);
//			log("[removeAllElementHelper] remove node: " + elementName);
//		} else
//		{
//			NodeList list = element.getChildNodes();
//			int length = list.getLength();
//			for (int i = 0; i < length; i++)
//			{
//				Node node = list.item(i);
//				if (node instanceof Element)
//				{
//					removeAllElementHelper((Element) node, elementName);
//				}
//			}
//		}
//	}
	
	private static void log(String message)
  {
  	com.gridnode.gtas.client.netweaver.helper.Logger.debug("[Util]" + message);
  }
	
	public static void removeAnchorTags(Document document)
	{
		Element element = document.getDocumentElement();
		removeAnchorTagsHelper(element);
	}
	
	private static void removeAnchorTagsHelper(Element element)
	{
		String name = element.getNodeName();
		if ("a".equals(name))
		{
			Node parent = element.getParentNode();
			NodeList list = element.getChildNodes();
			parent.removeChild(element);
			for (int i = 0; i < list.getLength(); i++)
			{
				Node node = list.item(i);
				parent.appendChild(node);
			}
		} else
		{
			NodeList list = element.getChildNodes();
			int length = list.getLength();
			for (int i = 0; i < length; i++)
			{
				Node node = list.item(i);
				if (node instanceof Element)
				{
					removeAnchorTagsHelper((Element) node);
				}
			}
		}
	}
	
	public static void removeAllCheckBox(Document document)
	{
		Element element = document.getDocumentElement();
		removeAllCheckBoxHelper(element);
	}
	
	private static void removeAllCheckBoxHelper(Element element)
	{
		if (isCheckBox(element))
		{
			Node parent = element.getParentNode();
			parent.removeChild(element);
		} else
		{
			NodeList list = element.getChildNodes();
			int length = list.getLength();
			for (int i = 0; i < length; i++)
			{
				Node node = list.item(i);
				if (node instanceof Element)
				{
					removeAllCheckBoxHelper((Element) node);
				}
			}
		}
	}
	
	private static boolean isCheckBox(Element element)
	{
		String name = element.getNodeName();
		String type = element.getAttribute("type");
		return "input".equals(name) && "checkbox".equals(type);
	}
	
	public static void removeAllViewImgTags(Document document)
	{
		Element element = document.getDocumentElement();
		removeAllViewImgTagsHelper(element);
	}
	
	private static void removeAllViewImgTagsHelper(Element element)
	{
		if (isViewImgTag(element))
		{
			Node parent = element.getParentNode();
			parent.removeChild(element);
		} else
		{
			NodeList list = element.getChildNodes();
			int length = list.getLength();
			for (int i = 0; i < length; i++)
			{
				Node node = list.item(i);
				if (node instanceof Element)
				{
					removeAllViewImgTagsHelper((Element) node);
				}
			}
		}
	}
	
	private static boolean isViewImgTag(Element element)
	{
		String name = element.getNodeName();
		String src = element.getAttribute("src");
		return "img".equals(name) && "images/actions/view.gif".equals(src);
	}
}
