
package com.project.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class UserHome 
{
	JFrame frame;
	
	private JPanel panel1,panel2,panel3;
	private JLabel label1;
	private JButton homeButton,upload_Button, back_btn;
	
	
   public UserHome()
   {
 	    frame = new JFrame("UserHome");
	    frame.setSize(800,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		
		panel1=new JPanel();
		panel1.setBounds(20,50,750,80);
		
		panel2=new JPanel();
		panel2.setBounds(20,130,700,500);
		panel2.setLayout(null);
		
		label1=new JLabel("Sanskrit text recognition and translation using transfer learning");
		label1.setBounds(10,10,400,40);
		label1.setFont(new Font("Arial", Font.BOLD, 20));
		label1.setForeground(Color.BLACK);
		panel1.add(label1);
		panel1.setOpaque(false);
		
		
		
		
		
		upload_Button = new JButton("Upload Image");
		upload_Button.setBounds(280,150,200,40);
		upload_Button.setForeground(Color.BLACK);
		panel2.add(upload_Button);
		panel2.setOpaque(false);
			
		upload_Button.addActionListener(new ActionListener()
	       {
	         public void actionPerformed(ActionEvent e)	
	          {
		
	        	 ImageProcessing ur=new ImageProcessing();
		        //USI.setVisible(true);
		        frame.dispose();
	          }
	  
	       }
		);
		
		
		
	    frame.setContentPane(new JLabel(new ImageIcon("images\\3D2.jpg")));
	 	frame.add(panel1);
	 	frame.add(panel2);
	 	    
	 	frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
     }
  
       public static void main(String args[])
        {
	       new UserHome();
        }
}
