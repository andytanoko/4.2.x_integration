/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MainFrame.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 26 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.registration.nodelock;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.io.*;
import java.util.StringTokenizer;

import javax.swing.*;

import com.gridnode.gtas.server.registration.product.ProductKey;


public class MainFrame extends JFrame implements ActionListener, KeyListener
{
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JTextField key1TF = new JTextField();
  JTextField key2TF = new JTextField();
  JTextField key3TF = new JTextField();
  JTextField key4TF = new JTextField();
  JTextField nodeLockTF = new JTextField();
  JButton importBN = new JButton();
  JLabel jLabel1 = new JLabel();
  JTextField nodeIdTF = new JTextField();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel5 = new JLabel();
  JTextField startDateTF = new JTextField();
  JTextField endDateTF = new JTextField();
  JTextField connectionsTF = new JTextField();
  JTextField categoryTF = new JTextField();
  JLabel jLabel6 = new JLabel();
  JLabel jLabel7 = new JLabel();
  JLabel jLabel8 = new JLabel();
  JTextField osNameTF = new JTextField();
  JTextField osVersionTF = new JTextField();
  JTextField machineNameTF = new JTextField();
  JButton okBN = new JButton();
  JButton cancelBN = new JButton();
  JLabel jLabel9 = new JLabel();
  JLabel jLabel10 = new JLabel();

  JFileChooser fc;

  String productKey1;
  String productKey2;
  String productKey3;
  String productKey4;
  int nodeId;
  ProductKey productKey;

  String startDate;
  String endDate;
  String connections;
  String category;

  String osName;
  String osVersion;
  String machineName;
  NodeInfoGenerator generator;

  public MainFrame()
  {
    try
    {
      jbInit();
      this.setVisible(true);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  public void actionPerformed(ActionEvent event)
  {
    String command = event.getActionCommand();
    if (command.equals(importBN.getActionCommand()))
    {
      importNodeLockFile();
    }
    if (command.equals(okBN.getActionCommand()))
    {
      generateLicenseFile();
    }
    if (command.equals(cancelBN.getActionCommand()))
    {
      clearFields();
    }
    checkFields();
  }

  public void keyTyped(KeyEvent event)
  {
    checkFields();
  }

  public void keyReleased(KeyEvent event)
  {
    checkFields();
  }

  public void keyPressed(KeyEvent event)
  {
    checkFields();
  }

  private void checkFields()
  {
    if (!key1TF.getText().equals("") && !key2TF.getText().equals("") &&
        !key3TF.getText().equals("") && !key4TF.getText().equals("") &&
        !nodeLockTF.getText().equals("") && !nodeIdTF.getText().equals(""))
    {
      okBN.setEnabled(true);
    }
    else
    {
      okBN.setEnabled(false);
    }
  }

  protected void processWindowEvent(WindowEvent e)
  {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING)
    {
      System.exit(0);
    }
  }

  private void jbInit() throws Exception
  {
    this.getContentPane().setLayout(gridBagLayout1);
    this.getContentPane().setFocusTraversalPolicy(new LayoutFocusTraversalPolicy());
    //key1TF.setNextFocusableComponent(key2TF);
    //key2TF.setNextFocusableComponent(key3TF);
    //key3TF.setNextFocusableComponent(key4TF);
    key1TF.addKeyListener(this);
    key2TF.addKeyListener(this);
    key3TF.addKeyListener(this);
    key4TF.addKeyListener(this);
    nodeIdTF.addActionListener(this);
    nodeIdTF.addKeyListener(this);
    importBN.setActionCommand("Import");
    importBN.addActionListener(this);
    importBN.setText("Import");
    jLabel1.setText("Node ID");
    jLabel2.setText("Start Date");
    jLabel3.setText("End Date");
    jLabel4.setText("Connections");
    jLabel5.setToolTipText("");
    jLabel5.setText("Category");
    jLabel6.setText("OS Name");
    jLabel7.setText("OS Version");
    jLabel8.setText("Machine Name");
    okBN.setEnabled(false);
    okBN.setText("Ok");
    okBN.addActionListener(this);
    cancelBN.setText("Cancel");
    cancelBN.addActionListener(this);
    jLabel9.setText("Product Key");
    jLabel10.setText("Node Lock File");
    nodeLockTF.setEditable(false);
    nodeLockTF.addKeyListener(this);
    startDateTF.setEditable(false);
    endDateTF.setEditable(false);
    connectionsTF.setEditable(false);
    categoryTF.setEditable(false);
    osNameTF.setEditable(false);
    osVersionTF.setEditable(false);
    machineNameTF.setEditable(false);
    this.setResizable(false);
    this.setTitle("GTAS License File Generator");
    this.getContentPane().add(key1TF, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 10), 60, 0));
    this.getContentPane().add(key2TF, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 10), 60, 0));
    this.getContentPane().add(key3TF, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 10), 60, 0));
    this.getContentPane().add(key4TF, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 10), 60, 0));
    this.getContentPane().add(nodeLockTF, new GridBagConstraints(1, 1, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 130, 0));
    this.getContentPane().add(importBN, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    this.getContentPane().add(nodeIdTF, new GridBagConstraints(1, 2, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    this.getContentPane().add(startDateTF, new GridBagConstraints(1, 3, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    this.getContentPane().add(endDateTF, new GridBagConstraints(1, 4, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    this.getContentPane().add(connectionsTF, new GridBagConstraints(1, 5, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    this.getContentPane().add(categoryTF, new GridBagConstraints(1, 6, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    this.getContentPane().add(osNameTF, new GridBagConstraints(1, 7, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    this.getContentPane().add(osVersionTF, new GridBagConstraints(1, 8, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    this.getContentPane().add(machineNameTF, new GridBagConstraints(1, 9, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    this.getContentPane().add(okBN, new GridBagConstraints(3, 10, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    this.getContentPane().add(cancelBN, new GridBagConstraints(4, 10, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    this.getContentPane().add(jLabel9, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    this.getContentPane().add(jLabel10, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    this.getContentPane().add(jLabel1, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    this.getContentPane().add(jLabel2, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    this.getContentPane().add(jLabel3, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    this.getContentPane().add(jLabel4, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    this.getContentPane().add(jLabel5, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    this.getContentPane().add(jLabel6, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    this.getContentPane().add(jLabel7, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    this.getContentPane().add(jLabel8, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    this.setSize(450, 400);

  }

  private void importNodeLockFile()
  {
    if (fc == null)
    {
      fc = new JFileChooser();
      fc.addActionListener(this);
    }
    int value = fc.showOpenDialog(this);
    if (value == fc.APPROVE_OPTION)
    {
      nodeLockTF.setText(fc.getSelectedFile().getAbsolutePath());
    }
  }

  private void generateLicenseFile()
  {
    try
    {
      productKey1 = key1TF.getText();
      productKey2 = key2TF.getText();
      productKey3 = key3TF.getText();
      productKey4 = key4TF.getText();
      nodeId = Integer.parseInt(nodeIdTF.getText());
      machineNameTF.setText("1");
      productKey = ProductKey.getProductKey(productKey1+productKey2+productKey3+productKey4, nodeId);
      machineNameTF.setText("2");

      readNodeLockFile(nodeLockTF.getText());
      machineNameTF.setText("3");

      startDate = ""+productKey.getStartDay()+"/"+productKey.getStartMth()+"/"+productKey.getStartYear();
      machineNameTF.setText("4");
      startDateTF.setText(startDate);
      machineNameTF.setText("5");
      endDate = ""+productKey.getEndDay()+"/"+productKey.getEndMth()+"/"+productKey.getEndYear();
      machineNameTF.setText("6");
      endDateTF.setText(endDate);
      machineNameTF.setText("7");

      connections = ""+productKey.getNumConnections();
      machineNameTF.setText("8");
      connectionsTF.setText(connections);
      machineNameTF.setText("9");
      category = productKey.getCategory();
      machineNameTF.setText("10");
      categoryTF.setText(category);
      machineNameTF.setText("11");

      osNameTF.setText(osName);
      machineNameTF.setText("12");
      osVersionTF.setText(osVersion);
      machineNameTF.setText(machineName);

      LicenseFileGenerator.generateFile(productKey, productKey1, productKey2,
                                        productKey3, productKey4, osName,
                                        osVersion, machineName, nodeId);

    }
    catch (Exception ex)
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PrintStream ps = new PrintStream(baos);
      ex.printStackTrace(ps);
      JOptionPane.showMessageDialog(this, "" + baos.toString(), "funny error", 0);
      ex.printStackTrace();
    }
  }

  private void readNodeLockFile(String fullPathFilename)
    throws Exception
  {
    if (generator == null)
    {
      generator = new NodeInfoGenerator();
    }
    generator.readNodeLockFile(fullPathFilename);
    osName = generator.getOsName();
    osVersion = generator.getOsVersion();
    machineName = generator.getMachineName();
  }

  private void clearFields()
  {
    key1TF.setText("");
    key2TF.setText("");
    key3TF.setText("");
    key4TF.setText("");

    nodeIdTF.setText("");
    nodeLockTF.setText("");

    startDateTF.setText("");
    endDateTF.setText("");
    connectionsTF.setText("");
    categoryTF.setText("");
    osNameTF.setText("");
    osVersionTF.setText("");
    machineNameTF.setText("");
  }

  public static void main(String[] args)
  {
    if (args.length == 0)
    {
      gui();
    }
    else
    {
      String inputFile = args[0];
      cmdLine(inputFile);
    }
  }

  private static void gui()
  {
    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    MainFrame frame = new MainFrame();
  }

  private static void cmdLine(String inputFile)
  {
    try
    {
      FileInputStream fis = new FileInputStream(inputFile);
      InputStreamReader isReader = new InputStreamReader(fis);
      BufferedReader reader = new BufferedReader(isReader);
      String aLine = reader.readLine();
      while (aLine != null)
      {
        String productKeyStr = null;
        int nodeId = -1;
        String nodeInfoFile = null;

        StringTokenizer st = new StringTokenizer(aLine, ",");
        if (st.hasMoreTokens())
          productKeyStr = st.nextToken();
        if (st.hasMoreTokens())
        {
          String nodeIdStr = st.nextToken();
          if (!nodeIdStr.equals(""))
          {
            nodeId = Integer.parseInt(nodeIdStr);
          }
        }
        if (st.hasMoreTokens())
          nodeInfoFile = st.nextToken();

        if (productKeyStr != null && nodeId != -1 && nodeInfoFile != null)
        {
          generateLicenseFile(productKeyStr, nodeId, nodeInfoFile);
        }

        aLine = reader.readLine();
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  private static void generateLicenseFile(String productKeyStr, int nodeId, String nodeInfoFile)
    throws Exception
  {
    ProductKey productKey = ProductKey.getProductKey(productKeyStr, nodeId);
    String key1 = productKeyStr.substring(0, 5);
    String key2 = productKeyStr.substring(5, 11);
    String key3 = productKeyStr.substring(11, 16);
    String key4 = productKeyStr.substring(16);

    NodeInfoGenerator generator = new NodeInfoGenerator();
    generator.readNodeLockFile(nodeInfoFile);

    LicenseFileGenerator.generateFile(productKey, key1, key2, key3, key4,
                                      generator.getOsName(),
                                      generator.getOsVersion(),
                                      generator.getMachineName(),
                                      nodeId);

  }
}