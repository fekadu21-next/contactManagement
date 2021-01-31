/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ViewAllContact.java
 *
 * Created on May 27, 2010, 1:40:14 PM
 */
package com.contactReport.frame;

/**
 *
 * @author COMP5
 */
import com.contactReport.bean.Contact;
import com.contactReport.common.CommonHelper;
import com.contactReport.connection.DBConnection;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

public class ViewAllContact extends javax.swing.JFrame {

    /** Creates new form ViewAllContact */
    GridBagLayout gbl;
    List allContactlList = new ArrayList();
    int listSize = 0;
    int noOfPage = 0;
    int currentPageIndx = 1;
    int stval = 0;
    int endval = 0;
    int recordsperpage=16;
    JCheckBox chkContact;
    int chk[]=new int[10];
    
    public ViewAllContact() {


        this.setTitle("View All Contact Details");
        
        createUI();
        PrinterJob pj = PrinterJob.getPrinterJob();
        mPageFormat = pj.defaultPage();
        setVisible(true);
        initComponents();
        lblMsg.setVisible(false);
        gbl = new GridBagLayout();
        pnlBody.setLayout(gbl);   
        allContactlList = getContactList();
        loadContactList();
     //  pnlBody.setPreferredSize(new Dimension(581, 640));
     //  this.setPreferredSize(new Dimension(581, 640));
       
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        CommonHelper.setFrameInCenter(this);
        bindGroupName();
       
    }

    private void bindGroupName() {
        try {
            String sql = "select * from tblgroup";
            ArrayList groupList = DBConnection.getGroupNameList(sql);
            String[] groupName = new String[groupList.size()];
            for (int i = 0; i < groupList.size(); i++) {
                groupName[i] = (String) groupList.get(i);
                cmbGroupName.addItem(groupName[i]);
            }
        } catch (Exception ex) {
            System.out.println("Exception " + ex);
        }
    }

    private void loadContactList() {
        listSize = allContactlList.size();
        if (listSize < 1) {
            pnlBody.removeAll();
            pnlBody.setVisible(false);
            lblMsg.setVisible(true);
        } else {
            lblMsg.setVisible(false);
        }
        noOfPage = (int) listSize / recordsperpage;
        if (listSize % recordsperpage >= 1) {
            noOfPage++;
        }
        if (noOfPage <= 1) {
            btnNext.setEnabled(false);
            btnPrv.setEnabled(false);
            stval = 0;
            endval = listSize;
            contactList(allContactlList, stval, endval);
            pnlBody.setVisible(true);
        } else if (noOfPage > 1) {
            stval = 0;
            endval = recordsperpage;
            contactList(allContactlList, stval, endval);
            pnlBody.setVisible(true);
            btnNext.setEnabled(true);
            btnPrv.setEnabled(false);
        }
    }
    /**************************/
    private PageFormat mPageFormat;

    protected void createUI() {
        setSize(300, 300);
        center();

        // Add the menu bar.
        JMenuBar mb = new JMenuBar();
        JMenu file = new JMenu("File", true);
       /* file.add(new FilePrintAction()).setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK));
        file.add(new FilePageSetupAction()).setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK
                | Event.SHIFT_MASK));
        file.addSeparator();*/
        file.add(new FileQuitAction()).setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.CTRL_MASK));
        mb.add(file);
        setJMenuBar(mb);
      
    }

    protected void center() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension us = getSize();
        int x = (screen.width - us.width) / 2;
        int y = (screen.height - us.height) / 2;
        setLocation(x, y);
    }

    public class FilePrintAction extends AbstractAction {

        public FilePrintAction() {
            super("Print");
        }

        public void actionPerformed(ActionEvent ae) {
            PrinterJob pj = PrinterJob.getPrinterJob();
            ComponentPrintable cp = new ComponentPrintable(getContentPane().add(pnlBody));
            pj.setPrintable(cp, mPageFormat);
            if (pj.printDialog()) {
                try {
                    pj.print();
                } catch (PrinterException e) {
                    System.out.println(e);
                }
            }
        }
    }

    public class FilePageSetupAction extends AbstractAction {

        public FilePageSetupAction() {
            super("Page setup...");
        }

        public void actionPerformed(ActionEvent ae) {
            PrinterJob pj = PrinterJob.getPrinterJob();
            mPageFormat = pj.pageDialog(mPageFormat);
          
        }
    }

    public class FileQuitAction extends AbstractAction {

        public FileQuitAction() {
            super("Quit");
        }

        public void actionPerformed(ActionEvent ae) {
            //System.exit(0);
            MainWindow mainwindow = new MainWindow();
            mainwindow.setVisible(true);
            setVisible(false);
        }
    }

    /************** Get All Contact List **************/
    private List getContactList() {
        String sql = "select * from contactInformation";
        List contactList = new ArrayList();
        try {

            contactList = DBConnection.getContactList(sql);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return contactList;
    }

    ArrayList allIds = new ArrayList();
    private void contactList(List contactList, int start, int end) {
        allIds.clear();
       pnlBody.removeAll();
        int x = 0;
        int y = 0;
        int count = 0;
        int k = start;
        int j = end;

        int l=0;
        if(currentPageIndx==0)
        {
            l=16;
        }
        else
        {
            l=currentPageIndx*16;
        }
        int noOfRecord=0;
        for (int i = k; i < l; i++) {

            if (count == 2) {
                /*******************/
                x = 0;
                y = y + 1;
                GridBagConstraints lblgbc2 = new GridBagConstraints();
                lblgbc2.gridx = x;
                lblgbc2.gridy = y;
                lblgbc2.gridheight = 1;
                lblgbc2.gridwidth = 2;
                lblgbc2.weightx =0.0;
                lblgbc2.weighty=1.0;
                lblgbc2.fill = GridBagConstraints.HORIZONTAL;
                lblgbc2.anchor=GridBagConstraints.NORTH;
                String lt = " ";
                JLabel lbl2 = new JLabel(lt);
                lbl2.setPreferredSize(new Dimension(10, 25));
               //  lbl2.setBorder(new EtchedBorder());
                gbl.setConstraints(lbl2, lblgbc2);
                pnlBody.add(lbl2);
                /*******************/               
                count = 0;
                x = 0;
                y = y + 2;

             //   System.out.println("LBL2: "+lbl2.getHeight());
            }
            if(i<j)
            {
            Contact contact = (Contact) contactList.get(i);

            /*******************/
            x = x + 2;
            GridBagConstraints lblgbc1 = new GridBagConstraints();
            lblgbc1.gridx = x;
            lblgbc1.gridy = y;
            lblgbc1.weightx =1.0;
            lblgbc1.weighty=1.0;
            lblgbc1.gridheight = 1;
            lblgbc1.gridwidth = 2;
            lblgbc1.fill = GridBagConstraints.HORIZONTAL;
            lblgbc1.anchor=GridBagConstraints.NORTH;
            String labelText = "<html>";
            if (chkContactName.isSelected()) {
                labelText = labelText + "<b><u>Name:</u></b> &nbsp;&nbsp;" + contact.getContactname();
            }
            else
            {
                labelText = labelText + "";
            }
            if (chkAddress.isSelected()) {
                labelText = labelText + "<br><b><u>Address:</u></b> &nbsp;&nbsp;" + contact.getAddress();
            }
            else
            {
                labelText = labelText + "<br>";
            }
            if (chkPhone.isSelected()) {
                labelText = labelText + "<br><b><u>Phone:</u></b> &nbsp;&nbsp;" + contact.getPhone();
            }
            else
            {
                labelText = labelText + "<br>";
            }
            if (chkEmail.isSelected()) {
                labelText = labelText + "<br><b><u>Email:</u></b> &nbsp;&nbsp;" + contact.getEmail();
            }
            else
            {
                labelText = labelText + "<br><br>";
            }
            if (chkAll.isSelected()) {
                labelText =
                        "<html><b><u>Name:</u></b> &nbsp;&nbsp;" + contact.getContactname()
                        + "<br><b><u>Address:</u></b> &nbsp;&nbsp;" + contact.getAddress()
                        + "<br><b><u>Phone:</u></b> &nbsp;&nbsp;" + contact.getPhone()
                        + "<br><b><u>Email:</u></b> &nbsp;&nbsp;" + contact.getEmail()
                        + "</html>";
            }


            JLabel lblText = new JLabel(labelText);
           //  chkContact=new JCheckBox(labelText);
         /*  lblText.setName(contact.getContactid());
            lblText.setPreferredSize(new Dimension(100, 80));
            lblText.setBorder(new EtchedBorder());
            gbl.setConstraints(lblText, lblgbc1);
            pnlBody.add(lblText);*/

           
            chkContact=new JCheckBox();
            chkContact.setName(contact.getContactid());
            allIds.add(chkContact);
            chkContact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handleCheck(evt);
            }
        });
            //chkContact.add

            //chkContact.setPreferredSize(new Dimension(10, 10));
            JPanel jp = new JPanel();
           // jp.setLayout(null);
            jp.setBorder(new EtchedBorder());
            //chkContact.setBounds(50, 50, 50, 50);
            GridBagConstraints checkBoxContraints = new GridBagConstraints();
            checkBoxContraints.gridx = 0;
            checkBoxContraints.gridy = 0;
            checkBoxContraints.weightx=1.0;
            checkBoxContraints.ipadx =1;
            checkBoxContraints.anchor=GridBagConstraints.NORTH;
            jp.add(chkContact);
            //lblText.setBounds(51,0,100,400);
              GridBagConstraints labelContraints = new GridBagConstraints();
            labelContraints.gridx = 1;
            labelContraints.gridy = 0;
            labelContraints.weightx=1.0;
            labelContraints.ipadx =30;
            labelContraints.anchor=GridBagConstraints.NORTH;
           lblText.setPreferredSize(new Dimension(250, 80));
            //lblText.setBorder(new EtchedBorder());
             jp.add(lblText);
            gbl.setConstraints(jp, lblgbc1);
            pnlBody.add(jp);
         //    System.out.println("Count: "+chkContact.getName());
            //jScrollPane1.setSize(200, 200);

            }
            /*******************/           
            
            else
            {
                //Contact contact = (Contact) contactList.get(i);

            /*******************/
            x = x + 2;
            GridBagConstraints lblgbc1 = new GridBagConstraints();
            lblgbc1.gridx = x;
            lblgbc1.gridy = y;
            lblgbc1.weightx =1.0;
            lblgbc1.weighty=1.0;
            lblgbc1.gridheight = 1;
            lblgbc1.gridwidth = 2;
            lblgbc1.fill = GridBagConstraints.HORIZONTAL;
            lblgbc1.anchor=GridBagConstraints.NORTH;
            String labelText = "<html>";
            if (chkContactName.isSelected()) {
                labelText = labelText + "<b><u></u></b> &nbsp;&nbsp;" ;
            }
            else
            {
                labelText = labelText + "";
            }
            if (chkAddress.isSelected()) {
                labelText = labelText + "<br><b><u></u></b> &nbsp;&nbsp;";
            }
            else
            {
                labelText = labelText + "<br>";
            }
            if (chkPhone.isSelected()) {
                labelText = labelText + "<br><b><u></u></b> &nbsp;&nbsp;";
            }
            else
            {
                labelText = labelText + "<br>";
            }
            if (chkEmail.isSelected()) {
                labelText = labelText + "<br><b><u></u></b> &nbsp;&nbsp;";
            }
            else
            {
                labelText = labelText + "<br><br>";
            }
            if (chkAll.isSelected()) {
                labelText =
                        "<html><b><u></u></b> &nbsp;&nbsp;"
                        + "<br><b><u></u></b> &nbsp;&nbsp;"
                        + "<br><b><u></u></b> &nbsp;&nbsp;"
                        + "<br><b><u></u></b> &nbsp;&nbsp;"
                        + "</html>";
            }


            JLabel lblText = new JLabel(labelText);
           //  chkContact=new JCheckBox(labelText);          
            lblText.setPreferredSize(new Dimension(250, 80));
           // lblText.setBorder(new EtchedBorder());
            gbl.setConstraints(lblText, lblgbc1);
            pnlBody.add(lblText);
            /*chkContact=new JCheckBox(labelText);
            chkContact.setName(contact.getContactid());
            chkContact.setPreferredSize(new Dimension(100, 80));
            chkContact.setBorder(new EtchedBorder());
            gbl.setConstraints(chkContact, lblgbc1);
            pnlBody.add(chkContact);*/
         //    System.out.println("Count: "+chkContact.getName());
            //jScrollPane1.setSize(200, 200);
            }



             /*******************/
            x = x + 2;
            GridBagConstraints lblgbc3 = new GridBagConstraints();
            lblgbc3.gridx = x;
            lblgbc3.gridy = y;
            lblgbc3.gridheight = 1;
            lblgbc3.gridwidth = 2;
            lblgbc3.weightx =0.0;
            lblgbc3.weighty=1.0;
            lblgbc3.fill = GridBagConstraints.HORIZONTAL;
            lblgbc3.anchor=GridBagConstraints.NORTH;
            String lt3 = "";
            JLabel lbl3 = new JLabel(lt3);
            lbl3.setPreferredSize(new Dimension(10, 10));
            // lbl3.setBorder(new EtchedBorder());
            gbl.setConstraints(lbl3, lblgbc3);
            pnlBody.add(lbl3);
            /*******************/
            pack();
            x = x + 2;

            count++;
            noOfRecord++;
          //  System.out.println("LBL3: "+lbl3.getHeight());
          //  System.out.println("LBLText: "+lblText.getHeight());
        }

       //  System.out.println("Count: "+noOfRecord);

        if(noOfRecord<9)
        {
          //   System.out.println("Test: "+noOfRecord);
            pnlBody.setPreferredSize(new Dimension(581, 18));
        }

    }

    /************************************/
    /*********************************/
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonPanel = new javax.swing.JPanel();
        btnPrv = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        txtContactName = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        lblMsg = new javax.swing.JLabel();
        btnAll = new javax.swing.JButton();
        chkAll = new javax.swing.JCheckBox();
        chkContactName = new javax.swing.JCheckBox();
        chkAddress = new javax.swing.JCheckBox();
        chkEmail = new javax.swing.JCheckBox();
        chkPhone = new javax.swing.JCheckBox();
        cmbGroupName = new javax.swing.JComboBox();
        btnShow = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnPrint = new javax.swing.JButton();
        chkSelectAll = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        pnlBody = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnPrv.setFont(new java.awt.Font("Courier New", 0, 11)); // NOI18N
        btnPrv.setForeground(new java.awt.Color(102, 0, 102));
        btnPrv.setText("Prev");
        btnPrv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handelPrev(evt);
            }
        });

        btnNext.setFont(new java.awt.Font("Courier New", 0, 11)); // NOI18N
        btnNext.setForeground(new java.awt.Color(102, 0, 102));
        btnNext.setText("Next");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handelNext(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Courier New", 0, 11));
        jButton1.setForeground(new java.awt.Color(102, 0, 102));
        jButton1.setText("Search");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handleSearch(evt);
            }
        });

        lblMsg.setFont(new java.awt.Font("Courier New", 0, 16));
        lblMsg.setForeground(new java.awt.Color(255, 0, 0));
        lblMsg.setText("No Record Present");

        btnAll.setFont(new java.awt.Font("Courier New", 0, 11)); // NOI18N
        btnAll.setForeground(new java.awt.Color(102, 0, 102));
        btnAll.setText("Show All");
        btnAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handleAll(evt);
            }
        });

        chkAll.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        chkAll.setForeground(new java.awt.Color(0, 0, 204));
        chkAll.setSelected(true);
        chkAll.setText("All");
        chkAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handleChkAll(evt);
            }
        });

        chkContactName.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        chkContactName.setForeground(new java.awt.Color(0, 0, 204));
        chkContactName.setText("Contact Name");
        chkContactName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handleChkName(evt);
            }
        });

        chkAddress.setFont(new java.awt.Font("Verdana", 0, 11));
        chkAddress.setForeground(new java.awt.Color(0, 0, 204));
        chkAddress.setText("Address");
        chkAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handleChkAddress(evt);
            }
        });

        chkEmail.setFont(new java.awt.Font("Verdana", 0, 11));
        chkEmail.setForeground(new java.awt.Color(0, 0, 204));
        chkEmail.setText("E-mail");
        chkEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handleChkMail(evt);
            }
        });

        chkPhone.setFont(new java.awt.Font("Verdana", 0, 11));
        chkPhone.setForeground(new java.awt.Color(0, 0, 204));
        chkPhone.setText("Phone");
        chkPhone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handleChkPhone(evt);
            }
        });

        btnShow.setFont(new java.awt.Font("Courier New", 0, 11)); // NOI18N
        btnShow.setForeground(new java.awt.Color(102, 0, 102));
        btnShow.setText("Show");
        btnShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handleShow(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Verdana", 0, 11));
        jLabel1.setForeground(new java.awt.Color(0, 0, 204));
        jLabel1.setText("Select Group Name : ");

        btnPrint.setFont(new java.awt.Font("Courier New", 0, 11));
        btnPrint.setForeground(new java.awt.Color(102, 0, 102));
        btnPrint.setText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handlePrint(evt);
            }
        });

        chkSelectAll.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        chkSelectAll.setForeground(new java.awt.Color(0, 0, 204));
        chkSelectAll.setText("Select All");
        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handleChkSelectAll(evt);
            }
        });

        javax.swing.GroupLayout buttonPanelLayout = new javax.swing.GroupLayout(buttonPanel);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanelLayout.setHorizontalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbGroupName, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnShow)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtContactName, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPrint)
                .addGap(18, 18, 18))
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(buttonPanelLayout.createSequentialGroup()
                        .addComponent(chkAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chkContactName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chkAddress)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chkEmail)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chkPhone))
                    .addGroup(buttonPanelLayout.createSequentialGroup()
                        .addComponent(chkSelectAll)
                        .addGap(74, 74, 74)
                        .addComponent(lblMsg)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnPrv)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNext)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAll)
                .addContainerGap(38, Short.MAX_VALUE))
        );
        buttonPanelLayout.setVerticalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbGroupName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnShow)
                    .addComponent(txtContactName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(btnPrint))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkAll)
                    .addComponent(chkContactName)
                    .addComponent(chkAddress)
                    .addComponent(chkEmail)
                    .addComponent(chkPhone)
                    .addComponent(btnPrv)
                    .addComponent(btnNext)
                    .addComponent(btnAll))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblMsg)
                    .addComponent(chkSelectAll))
                .addGap(121, 121, 121))
        );

        javax.swing.GroupLayout pnlBodyLayout = new javax.swing.GroupLayout(pnlBody);
        pnlBody.setLayout(pnlBodyLayout);
        pnlBodyLayout.setHorizontalGroup(
            pnlBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 698, Short.MAX_VALUE)
        );
        pnlBodyLayout.setVerticalGroup(
            pnlBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 472, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(pnlBody);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void handelPrev(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_handelPrev
        // TODO add your handling code here:
        pnlBody.removeAll();
        pnlBody.setVisible(false);
        currentPageIndx = currentPageIndx - 2;
        stval = currentPageIndx * recordsperpage;
        currentPageIndx++;
        endval = currentPageIndx * recordsperpage;
        if (listSize < endval) {
            endval = listSize;
        }
        contactList(allContactlList, stval, endval);
        pnlBody.setVisible(true);
        btnPrv.setEnabled(true);
        if (currentPageIndx == 1) {
            btnPrv.setEnabled(false);
        }
        btnNext.setEnabled(true);
        pack();
    }//GEN-LAST:event_handelPrev

    private void handelNext(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_handelNext
        // TODO add your handling code here:
        pnlBody.removeAll();
        pnlBody.setVisible(false);
        stval = currentPageIndx * recordsperpage;
        currentPageIndx++;
        endval = currentPageIndx * recordsperpage;
        if (listSize < endval) {
            endval = listSize;
        }
        contactList(allContactlList, stval, endval);
        pnlBody.setVisible(true);
        btnPrv.setEnabled(true);
        if (noOfPage == currentPageIndx) {
            btnNext.setEnabled(false);
        }
        pack();
    }//GEN-LAST:event_handelNext

    private void handleSearch(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_handleSearch
        // TODO add your handling code here:    
        if (txtContactName.getText() != null) {
            String cname = "%" + txtContactName.getText() + "%";
            String sql = "select * from contactInformation where contactname like '" + cname + "' ";
            try {
                allContactlList = DBConnection.getContactList(sql);
                pnlBody.removeAll();
                loadContactList();
                pack();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }//GEN-LAST:event_handleSearch

    private void handleAll(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_handleAll
        // TODO add your handling code here:
        txtContactName.setText("");
        pnlBody.removeAll();
        allContactlList = getContactList();
        loadContactList();

    }//GEN-LAST:event_handleAll

    private void handleShow(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_handleShow
        // TODO add your handling code here:
         if (cmbGroupName.getSelectedItem().toString() != null) {
            String gname = cmbGroupName.getSelectedItem().toString();
            String sql = "select * from contactInformation where groupname ='" + gname + "'";
            try {
                allContactlList = DBConnection.getContactList(sql);
                pnlBody.removeAll();
                loadContactList();
              // this.setPreferredSize(new Dimension(581, 640));
               pack();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }//GEN-LAST:event_handleShow

    private void handlePrint(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_handlePrint
        // TODO add your handling code here:
        if(arr.size()>0)
        {
         if(chkAll.isSelected()){  
                   fieldList.clear();
                   fieldList.add("all");
                } else {                     
                     fieldList.remove("all");
                }

      /*  for(int ii=0;ii<arr.size(); ii++){
            System.out.println(arr.get(ii));
        }

         for(int i=0;i<fieldList.size(); i++){
            System.out.println(fieldList.get(i));
        }
        */
        PrintContactDetails pc=new PrintContactDetails(arr,fieldList);
        pc.setVisible(true);
        this.setVisible(false);
        }
        else
        {
            JOptionPane.showMessageDialog(null,	"Please Select Any Contact.");
        }
    }//GEN-LAST:event_handlePrint

    ArrayList fieldList=new ArrayList();
    private void handleChkAll(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_handleChkAll
        // TODO add your handling code here:
        fieldList.clear();
        chkContactName.setSelected(false);
        chkAddress.setSelected(false);
        chkEmail.setSelected(false);
        chkPhone.setSelected(false);
               if(chkAll.isSelected()){                  
                   fieldList.add("all");
                } else {                     
                     fieldList.remove("all");
                }
        
    }//GEN-LAST:event_handleChkAll

    private void handleChkName(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_handleChkName
        // TODO add your handling code here:

        if(chkContactName.isSelected()){
                   fieldList.add("name");
                } else {
                     fieldList.remove("name");
                }
    }//GEN-LAST:event_handleChkName

    private void handleChkAddress(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_handleChkAddress
        // TODO add your handling code here:
         if(chkAddress.isSelected()){
                   fieldList.add("address");
                } else {
                     fieldList.remove("address");
                }
    }//GEN-LAST:event_handleChkAddress

    private void handleChkMail(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_handleChkMail
        // TODO add your handling code here:
         if(chkEmail.isSelected()){
                   fieldList.add("mail");
                } else {
                     fieldList.remove("mail");
                }
        
    }//GEN-LAST:event_handleChkMail

    private void handleChkPhone(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_handleChkPhone
        // TODO add your handling code here:
         if(chkPhone.isSelected()){
                   fieldList.add("phone");
                } else {
                     fieldList.remove("phone");
                }
    }//GEN-LAST:event_handleChkPhone

    private void handleChkSelectAll(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_handleChkSelectAll
        // TODO add your handling code here:
        System.out.print("Size "+allIds.size());
       if(chkSelectAll.isSelected())
       {
               for(int i= 0; i<allIds.size(); i++){
                    JCheckBox chk =(JCheckBox)allIds.get(i);
                    chk.setSelected(true);
                    arr.add(chk.getName());
                }
       } 
       else
       {
            for(int i= 0; i<allIds.size(); i++){
                    JCheckBox chk =(JCheckBox)allIds.get(i);
                    chk.setSelected(false);
                    arr.remove(chk.getName());
                }
       }


    }//GEN-LAST:event_handleChkSelectAll
    ArrayList arr = new ArrayList();
      private void handleCheck(java.awt.event.ActionEvent evt) {
            Object obj =evt.getSource();
            if(obj instanceof JCheckBox){
                JCheckBox checkBox = (JCheckBox)obj;
                if(checkBox.isSelected()){
                //    System.out.print(checkBox.getName());
                    arr.add(checkBox.getName());
                } else {
                 //    System.out.print(checkBox.getName());
                     arr.remove(checkBox.getName());
                }
                
            }
          //System.out.print("Test "+chkContact.getName());

      }
    /**
     * @param args the command line arguments
     */
   public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ViewAllContact().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAll;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnPrv;
    private javax.swing.JButton btnShow;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JCheckBox chkAddress;
    private javax.swing.JCheckBox chkAll;
    private javax.swing.JCheckBox chkContactName;
    private javax.swing.JCheckBox chkEmail;
    private javax.swing.JCheckBox chkPhone;
    private javax.swing.JCheckBox chkSelectAll;
    private javax.swing.JComboBox cmbGroupName;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblMsg;
    private javax.swing.JPanel pnlBody;
    private javax.swing.JTextField txtContactName;
    // End of variables declaration//GEN-END:variables
}

/*******************************/
class ComponentPrintable implements Printable {

    private Component mComponent;

    public ComponentPrintable(Component c) {
        mComponent = c;
    }

    public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }
        Graphics2D g2 = (Graphics2D) g;
        g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        boolean wasBuffered = disableDoubleBuffering(mComponent);
        mComponent.paint(g2);
        restoreDoubleBuffering(mComponent, wasBuffered);
        return PAGE_EXISTS;
    }

    private boolean disableDoubleBuffering(Component c) {
        if (c instanceof JComponent == false) {
            return false;
        }
        JComponent jc = (JComponent) c;
        boolean wasBuffered = jc.isDoubleBuffered();
        jc.setDoubleBuffered(false);
        return wasBuffered;
    }

    private void restoreDoubleBuffering(Component c, boolean wasBuffered) {
        if (c instanceof JComponent) {
            ((JComponent) c).setDoubleBuffered(wasBuffered);
        }
    }
}

/*******************************/


/************* Get All Contact list ***************/


/****************************/
