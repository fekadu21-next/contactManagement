/*
 * MyConnection.java
 *
 * Created on 15 April, 2010, 12:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.contactReport.connection;

import com.contactReport.bean.Contact;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;


/**
 *
 * @author Sudipta
 */
public class DBConnection {
    
    /** Creates a new instance of MyConnection */
    public DBConnection() {
    }
    
    
    
    private static Connection getConnection() {
		Connection con = null;
		String DRIVER = "com.mysql.jdbc.Driver";
		try {
			Class.forName(DRIVER).newInstance();
			String url = "jdbc:mysql://localhost:3306/contactreport";
			con = DriverManager.getConnection(url);
		} catch (Exception e) {
			 System.out.println("Not Connected  "+e);
		}
		return con;
	}

	// declare a method to create a Statement

	private static Statement getStatement() throws Exception {
		Statement st = null;
		Connection con = getConnection();
		st = con.createStatement();
		return st;
	}

	// declare a method to insert record

	public static boolean insertRecord(String sql) throws Exception {
		Statement st = getStatement();
		st.executeUpdate(sql);

		return true;
	}


        
        
        public static String getRecord(String sql) throws Exception {
		String s = null;
		ResultSet rst = null;
		Statement st = getStatement();
		rst = st.executeQuery(sql);
		if (rst.next()) {
			s = rst.getString(1);
		}
		return s;

	}
	// declare a method to check a record is present or not

	public static boolean checkValue(String sql) throws Exception {
		boolean b = false;
		ResultSet rst = null;
		Statement st = getStatement();
		rst = st.executeQuery(sql);
		if (rst.next()) {
			b = true;
		}
		return b;

	}

        
        
        
        public static ArrayList getRecordsList(String sql) throws Exception {
		ArrayList ar = new ArrayList();
		ResultSet rs = null;
		Statement st = getStatement();
		rs = st.executeQuery(sql);
		while (rs.next()) {			
                    String str=rs.getString(1);
			ar.add(str);
		}

		return ar;
	}

        public static ArrayList getGroupNameList(String sql) throws Exception {
		ArrayList ar = new ArrayList();
		ResultSet rs = null;
		Statement st = getStatement();
		rs = st.executeQuery(sql);
		while (rs.next()) {
                    String str=rs.getString(2);
			ar.add(str);
		}

		return ar;
	}
        
        
        
        
        
         public static  int updateRecordsy(String sql){
          int i=0;
           try{
          Statement st = getStatement();
           i= st.executeUpdate(sql);
           }
           catch (Exception ex)
           {
                JOptionPane.showMessageDialog(null, ex);
           }
            return  i;
        }



         public static ArrayList getContactList(String sql) throws Exception {
		ArrayList contactList = new ArrayList();
		ResultSet rs = null;
		Statement st = getStatement();
		rs = st.executeQuery(sql);
		while (rs.next()) {
                        Contact contact=new Contact();
                        contact.setContactid(rs.getString(1));
			contact.setContactname(rs.getString(2));
                        contact.setPhone(rs.getString(3));
                        contact.setAddress(rs.getString(4));
                        contact.setEmail(rs.getString(5));
                        contact.setGroupname(rs.getString(6));
                        contactList.add(contact);

		}

		return contactList;
	}

         public static String[] getContactNameList(String sql)throws Exception
         {
           //  System.out.println(sql);
             String [] contactname=null;
             ResultSet rs = null;
		Statement st = getStatement();
		rs = st.executeQuery(sql);
                rs.last();
                int row=rs.getRow();
                rs.beforeFirst();
              //   System.out.println(row);
                contactname = new String[row];
                int i=0;
                while(rs.next()) {
                      contactname[i]=rs.getString(1);
                    //System.out.println(rs.getString(1));
                        i++;
                       }
             return  contactname;
         }
        
	// declare a method to retrieve all user record


    
}
