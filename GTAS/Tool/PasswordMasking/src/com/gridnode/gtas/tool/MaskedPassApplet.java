package com.gridnode.gtas.tool;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.gridnode.pdip.framework.util.MaskedPass;
public class MaskedPassApplet   extends JApplet 
{
  private static final long serialVersionUID = -1858751488745815633L;
  private JTextField passwordField = new JTextField();
  private JTextArea display = new JTextArea();
  private JButton clearButton = new JButton("Clear");
  private JButton generateButton = new JButton("Generate");
  private GridLayout panelLayout = new GridLayout(1,2);
  private JPanel panel = new JPanel(panelLayout);

  public void init() {
    
//  Execute a job on the event-dispatching thread:
    //creating this applet's GUI.
    try {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                createGUI();
            }
        });
    } catch (Exception e) {
        System.err.println("createGUI didn't successfully complete");
    }
    
  }

  private void createGUI() {   
    getContentPane().removeAll();
    passwordField.setEditable(true);
    panel.add(passwordField);
    display.setEditable(false);
    display.setFont(new Font("Monospaced", Font.PLAIN, 10));
    getContentPane().setLayout(new BorderLayout(3,3));
    getContentPane().add(panel, BorderLayout.NORTH);
    getContentPane().add(new JScrollPane(display), BorderLayout.CENTER);
    
    JPanel buttonPanel = new JPanel(new FlowLayout());
    clearButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        display.setText("");
      }
    });
    buttonPanel.add(clearButton);
    
    generateButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        //display.setText(passwordField.getText());
        String password=MaskedPass.encode(passwordField.getText());
        System.out.println(password);
        display.setText(password);
      }
    });
    buttonPanel.add(generateButton);    
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);
   
  }
  /**
   * Get information such as the name of this applet, the author of
   * this applet, and a description of this applet.
   *
   * @return a string with the information about this applet.
   *
   */
  public String getAppletInfo() {
    return (
      "Title: Masked Password Generator\n" +
      "Author: Ong Eu Soon\n" 
    );
  }


}
