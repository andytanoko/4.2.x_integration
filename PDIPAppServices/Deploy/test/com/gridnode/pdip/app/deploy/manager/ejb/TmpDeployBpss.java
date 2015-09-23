package com.gridnode.pdip.app.deploy.manager.ejb;

import java.io.*;

import com.gridnode.pdip.framework.j2ee.*;
import junit.framework.*;

import com.gridnode.pdip.framework.log.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.xml.parsers.*;
import org.apache.xml.serialize.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class TmpDeployBpss
{
   // static String bpssFileNames[] = {"Test002-BpssCollaboration.xml"};
   static String bpssFileNames[] = {"3A4Test116.xml"};
   static String xpdlFileNames[] = {"PF4B2REF.xpdl"};


    public static void deploy() throws Exception {
        DeployBpssTestCase tc=new DeployBpssTestCase(DeployBpssTestCase.class.getName());
        tc.setUp();
        tc.setFileNames(bpssFileNames);
        tc.testBpssDeploy();
    }

    public static void unDeploy() throws Exception {
        UnDeployBpssTestCase tc=new UnDeployBpssTestCase(UnDeployBpssTestCase.class.getName());
        tc.setUp();
        tc.setFileNames(bpssFileNames);
        tc.testUnDeployBpss();
    }

    public static void deployXpdl() throws Exception {
        DeployXpdlTestCase tc=new DeployXpdlTestCase(DeployXpdlTestCase.class.getName());
        tc.setUp();
        tc.xpdlFileNames=xpdlFileNames;
        tc.testXpdlDeploy();
    }



    public static void main(String args[]) throws Exception {
        deployXpdl();
        //deploy();
        //unDeploy();
    }

}
