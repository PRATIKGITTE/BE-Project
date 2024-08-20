/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.dao;

import com.project.db.*;

import com.project.bean.UserBean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserDao {
    
    PreparedStatement st;
    ResultSet rs;
    Boolean resultStatus=Boolean.FALSE;
    Connection con=DBConnect.getConnection();
    
    public boolean userRegistration(UserBean bean)
	   {
		try
		   {
		    String sql="Select * from tbluser where user_email=?";
				st = con.prepareStatement(sql);
				st.setString(1,bean.getEmail());
				rs=st.executeQuery();
				Boolean b=rs.next();
				
				if(b==true)
				{
				System.out.println("Record already exist");
				}
				
				else
				{
				
				String SQL="insert into tbluser(user_name, user_address, user_age, user_email, user_mob, user_pass, doc_name) values(?,?,?,?,?,?,?)"; 
				
					PreparedStatement pstmt=con.prepareStatement(SQL);
					pstmt.setString(1, bean.getUname());
					pstmt.setString(2, bean.getAddress());
					pstmt.setString(3, bean.getAge());
					pstmt.setString(4, bean.getEmail());
					pstmt.setString(5, bean.getMobNo());
					pstmt.setString(6, bean.getPassword());
					pstmt.setString(7, bean.getDoc_name());
					
					int index=pstmt.executeUpdate();
					
					if(index>0)
					{
						resultStatus=Boolean.TRUE;
					}
					else
					{
						resultStatus=Boolean.FALSE;	
					}
					
			   
	     	    }
		   }
				
				catch (SQLException e) 
				  {
					// TODO Auto-generated catch block
					e.printStackTrace();
				  }
					
		     return resultStatus;
				
		 }
	
   
     
    public  boolean loginCheck(UserBean bean)
     {
		
    	try {
			String sql="Select * from tbluser where user_email=? and user_pass=?";
			st = con.prepareStatement(sql);
			st.setString(1,bean.getEmail());
			st.setString(2,bean.getPassword());
			ResultSet rs=st.executeQuery();
			resultStatus=rs.next();
		} 
		catch (SQLException e)
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	
		return resultStatus;
		 	
		 
	}
    
    
    public  ArrayList<UserBean> userDetails(String email)
	{
		
	   ResultSet rs=null;
	   
	   ArrayList<UserBean> details = new ArrayList<UserBean>();
	   
       String sql = "Select * from tbluser where user_email='"+email+"'";
		
		try {
			
			Statement stmt=con.createStatement();
		
			rs = stmt.executeQuery(sql);
			
			while(rs.next())
			{
				UserBean bean=new UserBean();
				bean.setId(rs.getInt(1));
				bean.setUname(rs.getString(2));
				bean.setAddress(rs.getString(3));
				bean.setAge(rs.getString(4));
				bean.setEmail(rs.getString(5));
				bean.setMobNo(rs.getString(6));
				bean.setDoc_name(rs.getString(8));
				
				details.add(bean);
			}
			
		 } 
		catch (SQLException e) 
		   {
			
			 e.printStackTrace();
		   }
		return details;
		
		
	}
   
}
