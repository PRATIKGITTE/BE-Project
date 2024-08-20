package com.project.view;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import com.project.algo.ConvolutionalNeuralNetwork;
import com.project.algo.GrayDemo;

import com.project.algo.Kmeans;
import com.project.algo.MatchFeatures;
import com.project.algo.MedianFilter;
import com.project.algo.ConvolutionLayer;
import com.project.algo.Thresholding;

import com.project.bean.UserBean;
import com.project.dao.UserDao;
import com.project.db.DBConnect;

public class ImageProcessing extends javax.swing.JFrame 
{
  Connection connection=null;
  PreparedStatement ps=null;
  ResultSet rs=null;
  String filePath=null;
  String filePath1=null;
  public static String FileName=null;
  String tmp="";
  String appPath1=null;
  String outImg1=null;
  String outImg2=null;
  String fileName=null;
  String fileName1=null;
  String TumorArea="";
  String stageofTumor="";
  String identification="";
  
  File source, dest;
  public static float tumorarea;
  
  String Path="C:/Users/bhise/Desktop/New folder/Project/";
	
  String appPath=Path+"src/com/project/images";
  
  public ImageProcessing() 
  {

   initComponents();
   setSize(1100,770);
   //setLocation(80,100);
   setLocationRelativeTo(null);
   setVisible(true);
   
  }

//1048576 Size limit allowed for Image storage in MySQL.
  public void showImage()
  {
	  jScrollPane2.setText("");
	  
	  try
	  {
		//display image
		  	JFileChooser chooser=new JFileChooser(new File("C:\\Users\\bhise\\Desktop\\Project\\Datset\\"));

		  	chooser.setMultiSelectionEnabled(false);
		  	chooser.setVisible(true);

		  	chooser.showOpenDialog(this);

		  	File file=chooser.getSelectedFile();
		  	FileName=file.getName();
		  	if(file!=null)
		  	{
		  		filePath=file.getPath();
			  	
		  		JOptionPane.showMessageDialog(this, "Image Uploaded Successfully!!!");
			  	
			  	path.setText("File path:-"+" "+filePath);
			  	showimage.setIcon(new ImageIcon(filePath));
			  	filename.setText(file.getName());
			  	
			  	file=chooser.getSelectedFile();
			  	BufferedImage patientImage = ImageIO.read(file);
			  	source=new File(appPath+"/Brain_Img.jpg");
		        ImageIO.write(patientImage, "jpg", source);
			  	
			  	dest = new File(appPath);
			  	
		        if (!dest.exists()) {
		            if (dest.mkdir()) {
		                System.out.println("Directory is created!");
		            } else {
		                System.out.println("Failed to create directory!");
		            }
		        }
	          

				fileName=filename.getText();
			   
			 	fileName=fileName.substring(0,fileName.indexOf("."));
			 }
		  	else
		  	{
		  		JOptionPane.showMessageDialog(this,"Please select image");
		  	}
	  }
   catch(Exception e)
   {
	   JOptionPane.showMessageDialog(this, e.getMessage());
	   e.printStackTrace();
   }
}
public void showOriginalImage()
{
	try
	  {
		 
		  	//File file=new File(appPath+"/"+filename.getText());
		  	if(source!=null)
		  	{
		  		filePath=source.getPath();
		  	}
		  	if(filePath!=null)
		  	{
		  		path.setText("File path:-"+" "+filePath);
		  		showimage.setIcon(new ImageIcon(filePath));
		  	} 
	  }
	catch(Exception e)
	{
	   JOptionPane.showMessageDialog(this, e.getMessage());
	   e.printStackTrace();
	}
}

public void showGrayImage()
{
	try
	  {    
		   //File file=new File(appPath+"/"+filename.getText());
		   BufferedImage grayImg=GrayDemo.toGray(source);
		   
		   File file=new File(appPath+"/GrayImg.jpg");
		   
		   ImageIO.write(grayImg, "jpg", file);
		   
		   JOptionPane.showMessageDialog(this, "Gray Successfully!!!");
		  	if(file!=null)
		  	{
		  		filePath=file.getPath();
		  	}
		  	if(filePath!=null)
		  	{
		  		path.setText("File path:-"+" "+filePath);
		  		showimage.setIcon(new ImageIcon(filePath));
		  	} 
	  }
	catch(Exception e)
	{
	   JOptionPane.showMessageDialog(this, e.getMessage());
	   e.printStackTrace();
	}
}

public void showFilterImage()
{
	try
	  {     
		   File file=new File(appPath+"/GrayImg.jpg");
		   
		   BufferedImage filterImg=MedianFilter.medianFilter(file);
		   
		   file=new File(appPath+"/FilteredImg.jpg");
		   
		   ImageIO.write(filterImg, "jpg", file);
		   
		   JOptionPane.showMessageDialog(this, "Filtered Successfully!!!");
		  	if(file!=null)
		  	{
		  		filePath=file.getPath();
		  	}
		  	if(filePath!=null)
		  	{
		  		path.setText("File path:-"+" "+filePath);
		  		showimage.setIcon(new ImageIcon(filePath));
		  		System.out.println("filePath1==="+filePath);
		  	} 
	  }
	catch(Exception e)
	{
	   JOptionPane.showMessageDialog(this, e.getMessage());
	   e.printStackTrace();
	}
}

public void showClusters()
{
	try
	  {   
		   File file=new File(appPath+"/FilteredImg.jpg");
		   BufferedImage filterImg=ImageIO.read(file);
		    
		   String[] dstpath={appPath+"/cluster1.jpg",appPath+"/cluster2.jpg",appPath+"/cluster3.jpg",appPath+"/cluster4.jpg"};
		   for(int i=0; i<dstpath.length;i++)
		   {
		    System.out.println("path= "+dstpath[i]);
		   }
		    
		   Kmeans kmeans=new Kmeans();
		   int k=4;
		   Kmeans.imagecluster(k,filterImg,dstpath);
		    
		   File file1=new File(appPath+"/cluster1.jpg");
		   File file2=new File(appPath+"/cluster2.jpg");
		   File file3=new File(appPath+"/cluster3.jpg");
		   File file4=new File(appPath+"/cluster4.jpg");
		   
		   String filepath1=null;
		   String filepath2=null;
		   String filepath3=null;
		   String filepath4=null;
		   
		   JOptionPane.showMessageDialog(this, "Segmentation Successfully!!!");
		   
		  	if(file1!=null && file2!=null && file3!=null && file4!=null)
		  	{
		  		filepath1=file1.getPath();
		  		filepath2=file2.getPath();
		  		filepath3=file3.getPath();
		  		filepath4=file4.getPath();
		  	}
		  	if(filepath1!=null && filepath2!=null && filepath3!=null && filepath4!=null)
		  	{
		  		//path.setText("File path:-"+" "+filePath);
		  		clustlbl1.setIcon(new ImageIcon(filepath1));
		  		clustlbl2.setIcon(new ImageIcon(filepath2));
		  		clustlbl3.setIcon(new ImageIcon(filepath3));
		  		clustlbl4.setIcon(new ImageIcon(filepath4));
		  	} 
	  }
	catch(Exception e)
	{
	   JOptionPane.showMessageDialog(this, e.getMessage());
	   e.printStackTrace();
	}
}

public void showFuzzyClusters()
{
	
	 try
	 {
       
      String cropface=appPath+"/"+"cluster4.jpg";
      
      String test_featurefile =appPath+"/"+"test_feature.txt";
	  
      ConvolutionLayer f=new ConvolutionLayer();
      f.extractAll(cropface,test_featurefile);
  	  
  	  JOptionPane.showMessageDialog(this,"Feature Extraction and Classification Completed!!!");
  	   
	  }
	  catch(Exception e)
      {
          JOptionPane.showMessageDialog(this, e.getMessage());
          e.printStackTrace();
      }   
}
public void showThresholdImage()
{
	 try
	 {
		 String result=null;
	   
	   result=ConvolutionalNeuralNetwork.classify(FileName);
    	
	     System.out.println(result);
	     
	     
	     JOptionPane.showMessageDialog(this,"Result Generated!!!");
	     String filename=FileName+".txt";
	      File myObj = new File("E:\\Translation\\"+filename);
	      if (myObj.createNewFile()) {
	        System.out.println("File created: " + myObj.getName());
	      } else {
	        System.out.println("File already exists.");
	      }
	      
	      FileWriter myWriter = new FileWriter("E:\\Translation\\"+filename);
	      myWriter.write("Translation-"+result);
	      myWriter.close();
	 }
  catch(Exception e)
  {
      JOptionPane.showMessageDialog(this, e.getMessage());
      e.printStackTrace();
  } 
}


private void initComponents() 
   {

     jLabel1 = new javax.swing.JLabel();
     path = new javax.swing.JLabel();
     filename = new javax.swing.JLabel();
     showimage = new javax.swing.JLabel();
     
     clustlbl1= new javax.swing.JLabel();
     clustlbl2= new javax.swing.JLabel();
     clustlbl3= new javax.swing.JLabel();
     clustlbl4= new javax.swing.JLabel();
     
     browse_btn = new javax.swing.JButton();
     original_btn = new javax.swing.JButton();
     gray_btn = new javax.swing.JButton(); 
     filter_btn = new javax.swing.JButton();
     kmeans_btn = new javax.swing.JButton();
     fuzzy_btn= new javax.swing.JButton();
     thresh_btn= new javax.swing.JButton();
     detect_btn= new javax.swing.JButton();
     area_btn= new javax.swing.JButton();
     stage_btn= new javax.swing.JButton();
     identification_btn= new javax.swing.JButton();
     prediction_btn= new javax.swing.JButton();
     report_btn= new javax.swing.JButton();
     back_btn= new javax.swing.JButton();
     exit_btn= new javax.swing.JButton();
     
     mainPanel= new javax.swing.JPanel();
     TextArea = new javax.swing.JTextArea();
     
     jScrollPane1 = new javax.swing.JScrollPane();
     jScrollPane2 = new javax.swing.JTextArea();	

     setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
     getContentPane().setLayout(null);
     
     setContentPane(new JLabel(new ImageIcon("images\\3D2.jpg")));

     
     jLabel1.setText("Sanskrit text recognition and translation using transfer learning");
     jLabel1.setFont(new Font("Arial", Font.BOLD, 20));
     jLabel1.setForeground(Color.BLACK);
     getContentPane().add(jLabel1);
     jLabel1.setBounds(330, 30, 600, 16);

     /*jScrollPane1.setViewportView(showimage);
     getContentPane().add(jScrollPane1);
     jScrollPane1.setBounds(330, 70, 400, 375);*/
     
     mainPanel.revalidate();
     mainPanel.add(showimage);
     getContentPane().add(mainPanel);
     mainPanel.setBounds(330, 70, 400, 450);
     
     browse_btn.setText("Select Image");
     //browse_btn.setForeground(new java.awt.Color(51, 51, 255));
     browse_btn.setForeground(Color.BLACK);
     getContentPane().add(browse_btn);
     browse_btn.setBounds(150, 70, 150, 30);
     browse_btn.addActionListener(new ActionListener() 
     {
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			// TODO Auto-generated method stub
			browse_btnActionPerformed(e);	
			
		}
	});
    
     original_btn.setText("Store Image");
     original_btn.setForeground(Color.BLACK);
     getContentPane().add(original_btn);
     original_btn.setBounds(150, 110, 150, 30);
     original_btn.addActionListener(new ActionListener() 
     {
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			// TODO Auto-generated method stub
			original_btnActionPerformed(e);	
			
		}
	});
     
    gray_btn.setText("GrayScale");
    gray_btn.setForeground(Color.BLACK);
    getContentPane().add(gray_btn);
    gray_btn.setBounds(150, 150, 150, 30);
    gray_btn.addActionListener(new ActionListener() 
    {
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			// TODO Auto-generated method stub
			gray_btnActionPerformed(e);	
			
		}
	});
     
    filter_btn.setText("Filtering");
    filter_btn.setForeground(Color.BLACK);
    getContentPane().add(filter_btn);
    filter_btn.setBounds(150, 190, 150, 30);
    filter_btn.addActionListener(new ActionListener() 
    {
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			// TODO Auto-generated method stub
			filter_btnActionPerformed(e);	
			
		}
	});
    
    kmeans_btn.setText("Segmentation");
    kmeans_btn.setForeground(Color.BLACK);
    getContentPane().add(kmeans_btn);
    kmeans_btn.setBounds(150, 230, 150, 30);
    kmeans_btn.addActionListener(new ActionListener() 
    {
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			// TODO Auto-generated method stub
			kmeans_btnActionPerformed(e);	
			mainPanel.removeAll();
			mainPanel.add(clustlbl1);
			mainPanel.revalidate();
			mainPanel.add(clustlbl2);
			mainPanel.revalidate();
			mainPanel.add(clustlbl3);
			mainPanel.revalidate();
			mainPanel.add(clustlbl4);
			mainPanel.revalidate();
			
			/*jScrollPane1.repaint();
			jScrollPane1.setViewportView(clustlbl1);
			jScrollPane1.setViewportView(clustlbl2);
			jScrollPane1.setViewportView(clustlbl3);
			jScrollPane1.setViewportView(clustlbl4);*/
		}
	});
    
    
    fuzzy_btn.setText("CNN");
    fuzzy_btn.setForeground(Color.BLACK);
    getContentPane().add(fuzzy_btn);
    fuzzy_btn.setBounds(150,270, 150, 30);
    fuzzy_btn.addActionListener(new ActionListener() 
    {
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			// TODO Auto-generated method stub
			fuzzy_btnActionPerformed(e);	
			mainPanel.removeAll();
			mainPanel.updateUI();
			mainPanel.add(showimage);
			mainPanel.revalidate();
			getContentPane().add(mainPanel);
		}
	});
    
    
    thresh_btn.setText("Show Result");
    thresh_btn.setForeground(Color.BLACK);
    getContentPane().add(thresh_btn);
    thresh_btn.setBounds(150, 310, 150, 30);
    thresh_btn.addActionListener(new ActionListener() 
    {
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			// TODO Auto-generated method stub
			thresh_btnActionPerformed(e);	
			
		}
	});
    
 
   path.setFont(new Font("Arial", Font.BOLD, 16));
   path.setForeground(Color.BLACK);
   getContentPane().add(path);
   path.setBounds(20, 680, 1000, 30);
            
     pack();
     
  }  

  private void browse_btnActionPerformed(java.awt.event.ActionEvent evt) 
   { 
	   showImage();  
   } 
  private void original_btnActionPerformed(java.awt.event.ActionEvent evt) 
  { 
	  showOriginalImage();  
  } 
  private void gray_btnActionPerformed(java.awt.event.ActionEvent evt) 
  { 
	  showGrayImage();
  } 
  private void filter_btnActionPerformed(java.awt.event.ActionEvent evt) 
  { 
	  showFilterImage();
  }
  
  private void kmeans_btnActionPerformed(java.awt.event.ActionEvent evt) 
  { 
	  showClusters();
  }
  private void fuzzy_btnActionPerformed(java.awt.event.ActionEvent evt) 
  { 
	 showFuzzyClusters();
  }
  
  private void thresh_btnActionPerformed(java.awt.event.ActionEvent evt) 
  { 
	 showThresholdImage();
  }
  
 
   private javax.swing.JButton browse_btn,original_btn,gray_btn,filter_btn, kmeans_btn, fuzzy_btn,thresh_btn,detect_btn,area_btn, stage_btn,identification_btn,prediction_btn,report_btn,back_btn, exit_btn;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JLabel showimage,clustlbl1,clustlbl2,clustlbl3,clustlbl4;
   private javax.swing.JLabel path,filename;
   private javax.swing.JScrollPane jScrollPane1;
   private javax.swing.JPanel mainPanel;
   private javax.swing.JTextArea jScrollPane2,TextArea;

  

    private boolean check() 
    {
      if(filePath!=null) 
      {
       if(filePath.endsWith(".jpeg")||filePath.endsWith(".gif")||filePath.endsWith(".jpg")||filePath.endsWith(".JPEG")||filePath.endsWith(".GIF")||filePath.endsWith(".JPG")||filePath.endsWith(".png"))
        {
         return true;
        }
        return false;
       }
       return false;
    }
}