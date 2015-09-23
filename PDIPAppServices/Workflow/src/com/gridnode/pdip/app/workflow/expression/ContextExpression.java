package com.gridnode.pdip.app.workflow.expression;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.gridnode.pdip.base.gwfbase.util.Logger;

import de.fzi.XPath.Parser.Token;
 
public class ContextExpression extends Expression{

    public static boolean processContextTokens(Token currTok,ArrayList tokenList,HashMap contextData){
        //int index=0;
        String entityName=currTok.toString();
        if(entityName.equals("contextdata")){
            return processTokens(tokenList,contextData);
        } else throw new RuntimeException("[ContextExpression.processContextTokens] it can handle only contextdata ,entityName="+entityName);
    }

    public static boolean processTokens(ArrayList tokenList,HashMap contextData){
        String f=tokenList.get(0).toString();
        String l=tokenList.get(tokenList.size()-1).toString();
        if(isOpenStr(f) && isCloseStr(l) && getCloseStr(f)!=null && l.equals(getCloseStr(f)))
        {
          boolean retBool=false;
          List operandList=new ArrayList();
          int conditionOperator=-1;
          for(int i=1;i<tokenList.size()-1;i++)
          {
            Token currTok=(Token)tokenList.get(i);
            if(isOpenBracket(currTok.toString()))
            {
              ArrayList subList=getSubTokens(tokenList,i);
              i+=subList.size()-1;
              retBool=processTokens(subList,contextData);
            } else if(isLogicalOperator(currTok))
            {
              String operator=currTok.toString();
              if( (!retBool && operator.equals("and")) ||
                  (retBool && operator.equals("or"))  )
                return retBool;
            }else if(isConditionalOperator(currTok))
            {
                conditionOperator=getCodeFromName(currTok.toString());
            }else if(isAttribute(currTok))
            {
              currTok=(Token)tokenList.get(++i);
              operandList.add(contextData.get(currTok.toString()));
              if(operandList.size()==2)
              {
                retBool=checkCondition(conditionOperator,operandList.get(0),operandList.get(1));
                operandList.clear();
                conditionOperator=-1;
              }
            } else if(isValue(currTok))
            {
              currTok=(Token)tokenList.get(i);
              operandList.add(stripQuotes(currTok.toString()));
              if(operandList.size()==2)
              {
                retBool=checkCondition(conditionOperator,operandList.get(0),operandList.get(1));
                operandList.clear();
                conditionOperator=-1;
              }
            }
          }
          return retBool;
        }
        return false;
    }

    protected static boolean checkCondition(int condition,Object obj1,Object obj2){
        switch(condition){
            case EQUAL_OPERATOR:
                    return (compare(obj1,obj2)==0);
            case NOT_EQUAL_OPERATOR:
                    return (compare(obj1,obj2)!=0);
            case LESS_OPERATOR:
                    return (compare(obj1,obj2)==-1);
            case LESS_EQUAL_OPERATOR:
                    return (compare(obj1,obj2)<=0);
            case GRATER_OPERATOR:
                    return (compare(obj1,obj2)==1);
            case GRATER_EQUAL_OPERATOR:
                    return (compare(obj1,obj2)>=0);
            default:return false;
        }
    }

    protected static int compare(Object obj1, Object obj2){
        Logger.debug("[ContextExpression.compareObjects] Comparing obj1="+obj1+",obj2="+obj2);
        if(obj1==obj2)
            return 0;
        if(obj1==null)
            return 1;
        if(obj2==null)
            return -1;
        if(String.class.isInstance(obj1) && String.class.isInstance(obj2)){
            return obj1.toString().compareTo((String)obj2);
        }
        if(Number.class.isInstance(obj1) || Number.class.isInstance(obj2)){
            return new Double(obj1.toString()).compareTo(new Double(obj2.toString()));
        }
        if(Date.class.isInstance(obj1) || Date.class.isInstance(obj2)){
            if(String.class.isInstance(obj1))
                obj1=java.sql.Timestamp.valueOf(obj1.toString());
            if(String.class.isInstance(obj2))
                obj2=java.sql.Timestamp.valueOf(obj2.toString());
            if(Date.class.isInstance(obj1) && Date.class.isInstance(obj2))
                return ((Date)obj1).compareTo((Date)obj2);
        }
        throw new RuntimeException("[ContextExpression.compareObjects] Cannot compare on this type :"+obj1.getClass().getName()+", "+obj2.getClass().getName());
    }

    public static String stripQuotes(String str){
        if(str!=null && str.length()>1 && str.startsWith("'") &&str.endsWith("'")){
           return str.substring(1,str.lastIndexOf("'"));
        } else if(str!=null && str.length()>1 && str.startsWith("\"") &&str.endsWith("\"")){
           return str.substring(1,str.lastIndexOf("\""));
        }
        return str;
    }

    public static void main(String args[]){
        String expression[]={
                    "(contextdata[@keyname='${keyname}'] and contextdata[@value='${value}'])",
                    "(contextdata[@id1=10 and @value='${value}'])",
                    "(contextdata[(@id1=10) and (@value='${value}' and @id1>${id2})])",
                    "(contextdata[@id1!=10 or @id1>${id2}])",
                    "(contextdata[@id1!=10 and @id1>${id2}])"} ;

        HashMap map=new HashMap();
        map.put("keyname","role");
        map.put("value","CompanyA");
        map.put("id1",new Integer(10));
        map.put("id2",new Double("5.01"));
        for(int i=0;i<expression.length;i++){
            expression[i]=setVariableValues(expression[i],map);
            ArrayList list=ExpressionTokenizer.getXPathTokens(expression[i]);
            boolean bool=processContextTokens((Token)list.get(1),new ArrayList(list.subList(2,list.size()-1)),map);
            System.out.println(expression[i]+"\t"+bool);
        }
    }
}