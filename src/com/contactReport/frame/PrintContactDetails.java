/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PrintContactDetails.java
 *
 * Created on Jul 2, 2010, 11:43:30 AM
 */
package com.contactReport.frame;

import com.contactReport.bean.Contact;
import com.contactReport.common.CommonHelper;
import com.contactReport.connection.DBConnection;
import com.lowagie.text.Cell;
import com.lowagie.text.Chapter;
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
import java.awt.print.Paper;
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
import javax.swing.border.EtchedBorder;

import com.lowagie.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import com.lowagie.text.Document;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Section;
import com.lowagie.text.Table;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPrintPage;
import java.awt.Color;
import java.awt.Font;
import java.awt.print.Book;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import javax.swing.JOptionPane;
//import com.sun.pdfview.PDfp
/**
 *
 * @author COMP5
 */
public class PrintContactDetails extends javax.swing.JFrame {

    /** Creates new form PrintContactDetails */
    GridBagLayout gbl;
    List allContactlList = new ArrayList();
    int listSize = 0;
    int noOfPage = 0;
    int currentPageIndx = 1;
    int stval = 0;
    int endval = 0;
    int recordsperpage = 16;
    ArrayList fieldsList = new ArrayList();

    public PrintContactDetails(ArrayList contactList, ArrayList fieldList) {
        this.setTitle("Print Contact Details");
        fieldsList = fieldList;
        createUI();
        PrinterJob pj = PrinterJob.getPrinterJob();
        mPageFormat = pj.defaultPage();
        /* Paper p=new Paper();
        p.setImageableArea(0, 0, 8.27, 11.69);
        mPageFormat.setPaper(p);*/
        setVisible(true);
        initComponents();
        //   lblMsg.setVisible(false);
        gbl = new GridBagLayout();
        pnlBody.setLayout(gbl);
        allContactlList = getContactList(contactList);
        loadContactList();
        //pnlBody.setPreferredSize(new Dimension(500, 640));
        // this.setPreferredSize(new Dimension(581, 640));

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
                //  cmbGroupName.addItem(groupName[i]);
            }
        } catch (Exception ex) {
            System.out.println("Exception " + ex);
        }
    }
    private PageFormat mPageFormat;

    protected void createUI() {
        setSize(300, 300);
        center();

        // Add the menu bar.
        JMenuBar mb = new JMenuBar();
        JMenu file = new JMenu("File", true);
         file.add(new FileSaveAction()).setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
         file.addSeparator();
        file.add(new FilePrintAction()).setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK));
        file.add(new FilePageSetupAction()).setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK
                | Event.SHIFT_MASK));
        file.addSeparator();
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

     public class FileSaveAction extends AbstractAction {

        public FileSaveAction() {
            super("Save");
        }

        public void actionPerformed(ActionEvent ae) {

             int s = JOptionPane.showConfirmDialog(null, "Do you want to save the file?", "Confirm",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (s == 0) {
            createPdf();
            JOptionPane.showMessageDialog(null,	"The PDF Report File is Save Successfully !!");
        }
            

        }
    }

    public class FilePrintAction extends AbstractAction {

        public FilePrintAction() {
            super("Print");
        }

        public void actionPerformed(ActionEvent ae) {
           /* PrinterJob pj = PrinterJob.getPrinterJob();
            ComponentPrintable cp = new ComponentPrintable(getContentPane().add(pnlBody));
            pj.setPrintable(cp, mPageFormat);
            if (pj.printDialog()) {
                try {
                    pj.print();
                } catch (PrinterException e) {
                    System.out.println(e);
                }
            }*/

            createPdf();
            try{
            File f=new File("report.pdf");
            FileInputStream fis = new FileInputStream(f);
            FileChannel fc = fis.getChannel();
            ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            PDFFile pdfFile = new PDFFile(bb); // Create PDF Print Page
           PDFPrintPage pages = new PDFPrintPage(pdfFile);

            // Create Print Job
            PrinterJob pjob = PrinterJob.getPrinterJob();
            PageFormat pf = PrinterJob.getPrinterJob().defaultPage();
            pjob.setJobName(f.getName());
            Book book = new Book();
            book.append(pages, pf, pdfFile.getNumPages());
            pjob.setPageable(book);
        // Send print job to default printer
             if (pjob.printDialog()) {
                try {
                     pjob.print();
                } catch (PrinterException e) {
                    System.out.println(e);
                }
           
            }
            }
            catch(Exception ex)
            {
                System.out.println(ex);
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

            //mPageFormat.setPaper(new Paper());

        }
    }

    public class FileQuitAction extends AbstractAction {

        public FileQuitAction() {
            super("Quit");
        }

        public void actionPerformed(ActionEvent ae) {
          // System.exit(0);
           ViewAllContact viewAllContact = new ViewAllContact();
           viewAllContact.setVisible(true);
           setVisible(false);
          //  createPdf();
            
        }
    }

    /************** Get All Contact List **************/
    private List getContactList(ArrayList clist) {
        List contactList = new ArrayList();
        for (int i = 0; i < clist.size(); i++) {

            String sql = "select * from contactInformation where contactid=" + clist.get(i) + "";
            try {
                ArrayList cList = DBConnection.getContactList(sql);
                Contact contact = (Contact) cList.get(0);
                contactList.add(contact);
            } catch (Exception ex) {
                System.out.println(ex);
            }

        }
        return contactList;
    }

    private void contactList(List contactList, int start, int end) {

        int x = 0;
        int y = 0;
        int count = 0;
        int k = start;
        int j = end;
        int l = 0;
        if (currentPageIndx == 0) {
            l = 16;
        } else {
            l = currentPageIndx * 16;
        }
        int noOfRecord = 0;
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
                lblgbc2.weightx = 0.0;
                lblgbc2.weighty = 1.0;
                lblgbc2.fill = GridBagConstraints.HORIZONTAL;
                lblgbc2.anchor = GridBagConstraints.NORTH;
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

                //  System.out.println("LBL2: "+lbl2.getHeight());
            }

            if (i < j) {
                Contact contact = (Contact) contactList.get(i);

                /*******************/
                x = x + 2;
                GridBagConstraints lblgbc1 = new GridBagConstraints();
                lblgbc1.gridx = x;
                lblgbc1.gridy = y;
                // lblgbc1.weightx =1.0;
                // lblgbc1.weighty=1.0;
                //lblgbc1.gridheight = 1;
                //  lblgbc1.gridwidth = 2;
                lblgbc1.fill = GridBagConstraints.HORIZONTAL;
                lblgbc1.anchor = GridBagConstraints.NORTH;
                String labelText = "<html>";
                int f = 0;
                for (f = 0; f < fieldsList.size(); f++) {
                    if (fieldsList.get(f).equals("name")) {
                        labelText = labelText + "<b>&nbsp;&nbsp;<u>Name:</u></b> &nbsp;&nbsp;" + contact.getContactname();
                        break;
                    }
                    /*  else
                    {
                    labelText = labelText + "";
                    }*/
                }

                for (f = 0; f < fieldsList.size(); f++) {
                    if (fieldsList.get(f).equals("address")) {
                        labelText = labelText + "<br>&nbsp;&nbsp;<b><u>Address:</u></b> &nbsp;&nbsp;" + contact.getAddress();
                        break;
                    }
                    /*  else
                    {
                    labelText = labelText + "<br>";
                    }*/
                }

                for (f = 0; f < fieldsList.size(); f++) {
                    if (fieldsList.get(f).equals("phone")) {
                        labelText = labelText + "<br>&nbsp;&nbsp;<b><u>Phone:</u></b> &nbsp;&nbsp;" + contact.getPhone();
                        break;
                    }
                    /*   else
                    {
                    labelText = labelText + "<br>";
                    }*/
                }

                for (f = 0; f < fieldsList.size(); f++) {
                    if (fieldsList.get(f).equals("mail")) {
                        labelText = labelText + "<br>&nbsp;&nbsp;<b><u>Email:</u></b> &nbsp;&nbsp;" + contact.getEmail();
                        break;
                    }
                    /*  else
                    {
                    labelText = labelText + "<br><br>";
                    }*/
                }

                for (f = 0; f < fieldsList.size(); f++) {
                    if (fieldsList.get(f).equals("all")) {
                        labelText =
                                "<html>&nbsp;&nbsp;<b><u>Name:</u></b> &nbsp;&nbsp;" + contact.getContactname()
                                + "<br>&nbsp;&nbsp;<b><u>Address:</u></b> &nbsp;&nbsp;" + contact.getAddress()
                                + "<br>&nbsp;&nbsp;<b><u>Phone:</u></b> &nbsp;&nbsp;" + contact.getPhone()
                                + "<br>&nbsp;&nbsp;<b><u>Email:</u></b> &nbsp;&nbsp;" + contact.getEmail()
                                + "</html>";
                        break;
                    }
                }

                //  System.out.print("Text "+labelText);
                JLabel lblText = new JLabel(labelText);
                // JCheckBox chkContact=new JCheckBox(labelText);
                lblText.setPreferredSize(new Dimension(240, 80));
                lblText.setBorder(new EtchedBorder());
                gbl.setConstraints(lblText, lblgbc1);
                pnlBody.add(lblText);
                //jScrollPane1.setSize(200, 200);
                /*******************/
            } else {
                //Contact contact = (Contact) contactList.get(i);

                /*******************/
                x = x + 2;
                GridBagConstraints lblgbc1 = new GridBagConstraints();
                lblgbc1.gridx = x;
                lblgbc1.gridy = y;
                lblgbc1.weightx = 1.0;
                lblgbc1.weighty = 1.0;
                lblgbc1.gridheight = 1;
                lblgbc1.gridwidth = 2;
                lblgbc1.fill = GridBagConstraints.HORIZONTAL;
                lblgbc1.anchor = GridBagConstraints.NORTH;
                String labelText = "<html>";
                int f = 0;
                for (f = 0; f < fieldsList.size(); f++) {
                    if (fieldsList.get(f).equals("name")) {
                        labelText = labelText + "<b><u></u></b> &nbsp;&nbsp;";
                    } else {
                        labelText = labelText + " ";
                    }
                }
                for (f = 0; f < fieldsList.size(); f++) {
                    if (fieldsList.get(f).equals("address")) {
                        labelText = labelText + "<br><b><u></u></b> &nbsp;&nbsp;";
                    } else {
                        labelText = labelText + "<br>";
                    }
                }
                for (f = 0; f < fieldsList.size(); f++) {
                    if (fieldsList.get(f).equals("phone")) {
                        labelText = labelText + "<br><b><u></u></b> &nbsp;&nbsp;";
                    } else {
                        labelText = labelText + "<br>";
                    }
                }
                for (f = 0; f < fieldsList.size(); f++) {
                    if (fieldsList.get(f).equals("mail")) {
                        labelText = labelText + "<br><b><u></u></b> &nbsp;&nbsp;";
                    } else {
                        labelText = labelText + "<br><br>";
                    }
                }
                for (f = 0; f < fieldsList.size(); f++) {
                    if (fieldsList.get(f).equals("all")) {
                        labelText =
                                "<html>&nbsp;&nbsp;&nbsp;&nbsp;<b><u></u></b> &nbsp;&nbsp;"
                                + "<br>&nbsp;&nbsp;&nbsp;&nbsp;<b><u></u></b> &nbsp;&nbsp;"
                                + "<br>&nbsp;&nbsp;&nbsp;&nbsp;<b><u></u></b> &nbsp;&nbsp;"
                                + "<br>&nbsp;&nbsp;&nbsp;&nbsp;<b><u></u></b> &nbsp;&nbsp;"
                                + "</html>";
                    } else {
                        labelText = labelText + " ";
                    }
                }

                // System.out.println(labelText);

                JLabel lblText = new JLabel(labelText);
                //  chkContact=new JCheckBox(labelText);
                lblText.setPreferredSize(new Dimension(240, 80));
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
            lblgbc3.weightx = 0.0;
            lblgbc3.weighty = 1.0;
            lblgbc3.fill = GridBagConstraints.HORIZONTAL;
            lblgbc3.anchor = GridBagConstraints.NORTH;
            String lt3 = " ";
            JLabel lbl3 = new JLabel(lt3);
            lbl3.setPreferredSize(new Dimension(18, 10));
            // lbl3.setBorder(new EtchedBorder());
            gbl.setConstraints(lbl3, lblgbc3);
            pnlBody.add(lbl3);
            /*******************/
            pack();
            x = x + 2;

            count++;
            noOfRecord++;
            //   System.out.println("LBL3: "+lbl3.getHeight());
            //   System.out.println("LBLText: "+lblText.getHeight());

        }

        System.out.println("Count: " + noOfRecord);
        /*
        if(noOfRecord<9)
        {
        System.out.println("Test: "+noOfRecord);
        pnlBody.setPreferredSize(new Dimension(581, (noOfRecord/2)*90));
        }
         */
    }

    private void loadContactList() {
        listSize = allContactlList.size();
        if (listSize < 1) {
            pnlBody.removeAll();
            pnlBody.setVisible(false);
            //  lblMsg.setVisible(true);
        } else {
            // lblMsg.setVisible(false);
        }
        noOfPage = (int) listSize / recordsperpage;
        if (listSize % recordsperpage >= 1) {
            noOfPage++;
        }
        if (noOfPage <= 1) {
            //  btnNext.setEnabled(false);
            //  btnPrv.setEnabled(false);
            stval = 0;
            endval = listSize;
            contactList(allContactlList, stval, endval);
            pnlBody.setVisible(true);
        } else if (noOfPage > 1) {
            stval = 0;
            endval = recordsperpage;
            contactList(allContactlList, stval, endval);
            pnlBody.setVisible(true);
            //  btnNext.setEnabled(true);
            //  btnPrv.setEnabled(false);
        }
    }

    private void createPdf()
    {
        try {
                Document document = new Document(PageSize.A4, 0, 0, 0, 0);
                PdfWriter.getInstance(document, new FileOutputStream("report.pdf"));
                document.open();
                document.setMargins(0, 0, 0, 0);

               // Paragraph title1 = new Paragraph();
               // Chapter chapter1 = new Chapter(1);
               // Section section1 = chapter1.addSection(title1);
               // chapter1.setNumberDepth(0);
               // Paragraph title11 = new Paragraph("This is Section 1 in Chapter 1",
              //  FontFactory.getFont(FontFactory.HELVETICA,16,Font.BOLD,new Color(255, 0, 0)));
              //  Section section1 = chapter1.addSection(title11);
               // Paragraph someSectionText = new Paragraph("This text comes as part of section 1 of chapter 1.");
               // section1.add(someSectionText);
               // someSectionText = new Paragraph("Following is a 3 X 2 table.");
              //  section1.add(someSectionText);
                List contactList=allContactlList;
                int i=contactList.size()/2;
                Font f1=new Font("Verdana", Font.PLAIN, 12);
                Table t = new Table(2,i);
               // t.setBorderColor(new Color(220, 255, 100));
                t.setPadding(2);
                t.setSpacing(10);
                t.setBorderWidth(0);

                for(int j=0;j<contactList.size();j++)
                {
                    Contact contact=(Contact)contactList.get(j);
                    String contactText="Name : "+contact.getContactname()+"\n"
                            +"Address : "+contact.getAddress()+"\n"
                            +"Phone : "+contact.getPhone()+"\n"
                            +"E-mail : "+contact.getEmail();
                    
                Cell c1 = new Cell(contactText);
               c1.setMaxLines(4);
                //c1.setWidth("10");
                //c1.setc
                //c1.setBorder(1);
                t.addCell(c1);
                }
                
                
                //section1.add(t);
                document.add(t);



                document.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        pnlBody = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlBody.setBackground(new java.awt.Color(255, 255, 255));

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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 583, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 462, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    /**
     * @param args the command line arguments
     */
    /*  public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {
    public void run() {
    new PrintContactDetails().setVisible(true);
    }
    });
    }*/
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnlBody;
    // End of variables declaration//GEN-END:variables
}
