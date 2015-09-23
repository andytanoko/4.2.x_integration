package com.gridnode.pdip.app.workflow.expression;


import java.util.ArrayList;
import java.util.HashMap;

import de.fzi.XPath.Parser.Token;

/**
 *  This class parses the expression and returns boolean value
 */
public class ExpressionParser
{

    /**
     * If the entity in the expression is contextdata it uses ContextExpression and parses it and
     * rest all it uses EntityExpression and parses it.
     * @param expression
     * @param contextData
     * @return boolean
     * @throws Exception
     */
    public static boolean processExpression(String expression, HashMap contextData) throws Exception
    {
        expression = "(" + expression + ")";
        expression = Expression.setVariableValues(expression, contextData);
        ArrayList tokenList = ExpressionTokenizer.getXPathTokens(expression);

        return processTokens(tokenList, contextData);
    }

    private static boolean processTokens(ArrayList tokenList, HashMap contextData) throws Exception
    {
        boolean retBool = false;
        String operator = null;

        if (Expression.isOpenBracket(tokenList.get(0).toString()) && Expression.isCloseBracket(tokenList.get(tokenList.size() - 1).toString()))
        {
            for (int i = 1; i < tokenList.size() - 1; i++)
            {
                Token currTok = (Token) tokenList.get(i);

                if (Expression.isOpenBracket(currTok.toString()))
                {
                    ArrayList subList = Expression.getSubTokens(tokenList, i);

                    i += subList.size() - 1;
                    retBool = processTokens(subList, contextData);
                } else if (Expression.isLogicalOperator(currTok))
                {
                    operator = currTok.toString();
                    if (retBool && operator.equals("or"))
                        return retBool;
                } else if (Expression.isEntity(currTok))
                {
                    ArrayList subList = Expression.getSubTokens(tokenList, i + 1);

                    i += subList.size();
                    if (currTok.toString().equals("contextdata"))
                        retBool = ContextExpression.processContextTokens(currTok, subList, contextData);
                    else retBool = EntityExpression.processEntityTokens(currTok, subList, contextData);
                }
            }
            return retBool;
        }
        return false;
    }

    public static void main(String args[]) throws Exception
    {
        String expression[] = {
                "(contextdata[@keyname='${keyname}'] and contextdata[@value='${value}'])",
                "(contextdata[@id1=10 and @value='${value}'])",
                "(contextdata[(@id1=10) and (@value='${value}' and @id1>${id2})])",
                "(contextdata[@id1!=10 or @id1>${id2}])",
                "(contextdata[@id1!=10 and @id1>${id2}])"};

        HashMap map = new HashMap();

        map.put("keyname", "role");
        map.put("value", "CompanyA");
        map.put("id1", new Integer(10));
        map.put("id2", new Double("5.01"));

        String tempExpr=expression[0]+" and ("+expression[4]+" or "+expression[2]+") and "+expression[1];
        System.out.println(tempExpr + "\t" + processExpression(tempExpr,map));
        tempExpr=expression[0]+" and ("+expression[4]+" or "+expression[2]+") and "+expression[4];
        System.out.println(tempExpr + "\t" + processExpression(tempExpr,map));

    }

}
