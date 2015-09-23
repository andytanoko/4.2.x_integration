package com.gridnode.pdip.app.workflow.expression;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

import de.fzi.XPath.Parser.Token;
import de.fzi.XPath.Parser.XPathParser;
import de.fzi.XPath.Parser.XPathParserTokenManager;
 
public class ExpressionTokenizer {

    public static boolean isValidExpression(String expression)
    {
        try
        {
            Reader stream = new StringReader(expression);
            XPathParser xpp = new XPathParser(stream);
            xpp.disable_tracing();
            xpp.XPath();
            return true;
        }
        catch(Exception e)
        {
        }
        return false;
    }

    public static ArrayList getXPathTokens(String xPathExpression)
    {
        ArrayList tokenList=new ArrayList();
        XPathParser xpp = new XPathParser(new StringReader(xPathExpression));
        xpp.disable_tracing();
        Token t=null;
        //int ind=0;
        while((t=xpp.getNextToken())!=null){
            tokenList.add(t);
            if(t.next!=null && t.next.kind==XPathParserTokenManager.EOF) break;
        }
        return tokenList;
    }

}