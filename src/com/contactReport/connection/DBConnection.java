package com.contactReport.connection;

import com.contactReport.bean.Contact;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class DBConnection {

    // Updated: Recommended modern driver class
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/contactdb?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";  // default XAMPP username
    private static final String PASSWORD = "";  // default is empty in XAMPP

    // Connection method
    private static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName(DRIVER);  // Optional for JDBC 4.0+, but safe to include
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to database");
        } catch (Exception e) {
            System.out.println("Not Connected: " + e);
        }
        return con;
    }

    private static Statement getStatement() throws Exception {
        Connection con = getConnection();
        if (con == null) {
            throw new Exception("Failed to connect to the database.");
        }
        return con.createStatement();
    }

    public static boolean insertRecord(String sql) throws Exception {
        Statement st = getStatement();
        st.executeUpdate(sql);
        return true;
    }

    public static String getRecord(String sql) throws Exception {
        String s = null;
        Statement st = getStatement();
        ResultSet rst = st.executeQuery(sql);
        if (rst.next()) {
            s = rst.getString(1);
        }
        return s;
    }

    public static boolean checkValue(String sql) throws Exception {
        Statement st = getStatement();
        ResultSet rst = st.executeQuery(sql);
        return rst.next();
    }

    public static ArrayList<String> getRecordsList(String sql) throws Exception {
        ArrayList<String> ar = new ArrayList<>();
        Statement st = getStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            ar.add(rs.getString(1));
        }
        return ar;
    }

    public static ArrayList<String> getGroupNameList(String sql) throws Exception {
        ArrayList<String> ar = new ArrayList<>();
        Statement st = getStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            ar.add(rs.getString(2));
        }
        return ar;
    }

    public static int updateRecordsy(String sql) {
        int i = 0;
        try {
            Statement st = getStatement();
            i = st.executeUpdate(sql);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return i;
    }

    public static ArrayList<Contact> getContactList(String sql) throws Exception {
        ArrayList<Contact> contactList = new ArrayList<>();
        Statement st = getStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            Contact contact = new Contact();
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

    public static String[] getContactNameList(String sql) throws Exception {
        String[] contactname;
        Statement st = getStatement();
        ResultSet rs = st.executeQuery(sql);
        rs.last();
        int row = rs.getRow();
        rs.beforeFirst();
        contactname = new String[row];
        int i = 0;
        while (rs.next()) {
            contactname[i] = rs.getString(1);
            i++;
        }
        return contactname;
    }
}
