package com.gridnode.pdip.app.workflow.expression;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import de.fzi.XPath.Parser.Token;
import de.fzi.XPath.Parser.XPathParser;

public class Expression {
    static String xpathOperators[]={">",">=","<","<=","!=","="};
    static String xpathConnectors[]={"and","or"};
    static String xpathFunctionNames[]={"contains"};
    static String openDelims[]={"[","(","{"};
    static String closeDelims[]={"]",")","}"};

    public static final int AND_CONNECTOR=1;
    public static final int OR_CONNECTOR=2;

    public static final int EQUAL_OPERATOR=3;
    public static final int NOT_EQUAL_OPERATOR=4;
    public static final int LESS_OPERATOR=5;
    public static final int LESS_EQUAL_OPERATOR=6;
    public static final int GRATER_OPERATOR=7;
    public static final int GRATER_EQUAL_OPERATOR=8;
    public static final int LIKE_OPERATOR=9;

  public static String setVariableValues(String expression,HashMap map){
    int ind=0;
    while((ind=expression.indexOf("${",ind))>-1){
        int ind2=expression.indexOf("}",ind);
        if(ind2>-1){
            String key=expression.substring(ind+2,ind2);
            Object value=map.get(key);
            expression=expression.substring(0,ind)+((value==null)?"":value.toString())+expression.substring(ind2+1);
        }
        ind+=2;
    }
    return expression;
  }

    public static int getCodeFromName(String condition){
        if("=".equals(condition)){
            return EQUAL_OPERATOR;
        } else if("!=".equals(condition)){
            return NOT_EQUAL_OPERATOR;
        } else if("<".equals(condition)){
            return LESS_OPERATOR;
        } else if("<=".equals(condition)){
            return LESS_EQUAL_OPERATOR;
        } else if(">".equals(condition)){
            return GRATER_OPERATOR;
        } else if("<=".equals(condition)){
            return GRATER_EQUAL_OPERATOR;
        } else if("like".equalsIgnoreCase(condition)){
            return LIKE_OPERATOR;
        } else if("and".equalsIgnoreCase(condition)){
            return AND_CONNECTOR;
        } else if("or".equalsIgnoreCase(condition)){
            return OR_CONNECTOR;
        } else return -1;
    }

    public static ArrayList getSubTokens(ArrayList tokenList,int index)
    {
        ArrayList subList=new ArrayList();
        Stack stack=new Stack();
        if(getCloseStr(tokenList.get(index).toString())!=null)
            stack.push(getCloseStr(tokenList.get(index).toString()));
        subList.add(tokenList.get(index++));
        while(!stack.isEmpty() && index<tokenList.size())
        {
            Token currTok=(Token)tokenList.get(index++);
            if(isOpenStr(currTok.toString()))
                stack.push(getCloseStr(currTok.toString()));
            else if(currTok.toString().equals(stack.peek()))
                stack.pop();
            subList.add(currTok);
        }
        return subList;
    }

    public static boolean isOpenStr(String str)
    {
        for(int i=0;i<openDelims.length;i++)
            if(openDelims[i].equals(str)) return true;
        return false;
    }

    public static boolean isCloseStr(String str)
    {
        for(int i=0;i<closeDelims.length;i++)
            if(closeDelims[i].equals(str)) return true;
        return false;
    }

    public static String getOpenStr(String str)
    {
        for(int i=0;i<closeDelims.length;i++)
            if(closeDelims[i].equals(str)) return openDelims[i];
        return null;
    }


    public static String getCloseStr(String str)
    {
        for(int i=0;i<openDelims.length;i++)
            if(openDelims[i].equals(str)) return closeDelims[i];
        return null;
    }


  public static boolean isValue(Token tok)
  {
    return (!isAttribute(tok) &&
            !isConditionalOperator(tok) &&
            !isLogicalOperator(tok) &&
            !isFunction(tok) &&
            !isEntity(tok));
  }

  public static boolean isAttribute(Token tok)
  {
    return (tok!=null &&
            tok.next!=null &&
            tok.kind==39 &&
            tok.next.kind==XPathParser.NCName);
  }

  public static boolean isEntity(Token tok)
  {
    return (tok!=null &&
            tok.next!=null &&
            tok.kind==XPathParser.NCName &&
            tok.next.kind==42);
  }

  public static boolean isVariable(Token tok)
  {
    return (tok!=null &&
            tok.next!=null &&
            tok.next.next!=null &&
            tok.next.next.next!=null &&
            tok.kind== 13 &&
            tok.next.toString().equals("{")&&
            tok.next.next.kind==XPathParser.NCName &&
            tok.next.next.next.toString().equals("}"));
  }


  public static boolean isFunction(Token tok)
  {
    int index;
    for(index=0;index<xpathFunctionNames.length && !xpathFunctionNames[index].equals(tok.toString()) ;index++)
    {
    }
    return !(index==xpathFunctionNames.length);
  }

  public static boolean isConditionalOperator(Token tok)
  {
    int index;
    for(index=0;index<xpathOperators.length && !xpathOperators[index].equals(tok.toString()) ;index++)
    {
    }
    return !(index==xpathOperators.length);
  }

  public static boolean isLogicalOperator(Token tok)
  {
    int index;
    for(index=0;index<xpathConnectors.length && !xpathConnectors[index].equals(tok.toString()) ;index++)
    {
    }
    return !(index==xpathConnectors.length);
  }


  public static boolean isOpenBracket(String op)
  {
    return op.trim().equals("(");
  }

  public static boolean isCloseBracket(String op)
  {
    return op.trim().equals(")");
  }

}