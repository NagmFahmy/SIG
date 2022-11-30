package view;

import com.sun.tools.javac.Main;
import controller.FileOperations;
import model.InvoiceHeader;
import model.InvoiceHeaderTable;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
public class InvoiceFrame extends JFrame{
    private FileOperations fileOperations = new FileOperations(this);
    private JPanel RightPanel;
    private JPanel LeftPanel;
    private JPanel TRightPanel;
    private JPanel BRightPanel;
    private JPanel ButtonContainer;
    private JPanel BtnLeft;
    private JPanel BtnRight;
    private JButton createInvoiceButton;
    private JButton createLineButton;
    private JButton cancelLineButton;
    private JButton saveRecordButton;
    private JLabel customerNameLabel;
    private JLabel invoiceTotalLabel;
    private JLabel invoiceDateLabel;
    private JLabel invoiceNumLabel;

    private JTextField customerNameField;
    private JTextField invoiceTotalField;
    private JTextField invoiceDateField;
    private JTextField invoiceNumField;

    private JButton deleteInvoiceButton;
    private JButton deleteLineButton;
    private JTable invoiceTable;
    private JMenu file;
    private JMenuBar mb;
    private JScrollPane jscrollPane1;
    private JScrollPane jScrollPane2;
    private JTable lineTable;
    private JMenuItem loadFileMenuItem;
    private JMenuItem saveFileMenuItem;
    private ArrayList<InvoiceHeader> invoices;
    private InvoiceHeaderTable invoicesTableModel;

    public InvoiceFrame(){
     initialize();
    }

    public void initialize() {

        RightPanel = new JPanel();
        LeftPanel = new JPanel();
        TRightPanel = new JPanel();
        BRightPanel = new JPanel();
        ButtonContainer = new JPanel();
        BtnLeft = new JPanel();
        BtnRight = new JPanel();
        mb = new JMenuBar();
        file = new JMenu("File");

        loadFileMenuItem = new JMenuItem("Load File");
        loadFileMenuItem.addActionListener(fileOperations);
        saveFileMenuItem= new JMenuItem("Save File");
        saveFileMenuItem.addActionListener(fileOperations);
        file.add(loadFileMenuItem);
        file.add(saveFileMenuItem);
        mb.add(file);


        createInvoiceButton = new JButton("Create New Invoice");
        createInvoiceButton.addActionListener(fileOperations);
        deleteInvoiceButton = new JButton("Delete Invoice");
        deleteInvoiceButton.addActionListener(fileOperations);
        saveRecordButton = new JButton("Save Record");
        saveRecordButton.addActionListener(fileOperations);
        cancelLineButton = new JButton("Cancel");
        createLineButton = new JButton("Create New Item");
        createLineButton.addActionListener(fileOperations);
        deleteLineButton = new JButton("Delete Item");
        deleteLineButton.addActionListener(fileOperations);
        LeftPanel.setPreferredSize(new Dimension(550, 600));
        LeftPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Invoice Table", TitledBorder.CENTER, TitledBorder.TOP));
        jscrollPane1 = new JScrollPane();
        jScrollPane2 = new JScrollPane();

        invoiceTable = new JTable();
        invoiceTable.getSelectionModel().addListSelectionListener(fileOperations);
        invoiceTable.setBounds(30, 40, 200, 300);
        invoiceTable.setModel(getInvoicesTableModel());
        invoiceTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {

                }
        ));
        jscrollPane1.setBackground(Color.green);
        jscrollPane1.setViewportView(invoiceTable);
        lineTable = new JTable();
        lineTable.setBackground(this.getContentPane().getBackground());
        lineTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {

                }
        ));
        jScrollPane2.setBackground(Color.green);
        jScrollPane2.setViewportView(lineTable);

        LeftPanel.add(jscrollPane1);
        TRightPanel.setLayout(new GridLayout(5, 5));

        invoiceNumLabel= new JLabel("Invoice Number");
        invoiceNumField = new JTextField();
        invoiceNumField.setEditable(false);
        invoiceDateLabel = new JLabel("Invoice Date");
        invoiceDateField = new JTextField();
//        invoiceDateField.setActionCommand("updateDate");
//        invoiceDateField.addActionListener(fileOperations);
        customerNameLabel = new JLabel("Customer Name");
        customerNameField = new JTextField();
        invoiceTotalLabel = new JLabel("Total Amount");
        invoiceTotalField = new JTextField();
        invoiceTotalField.setEditable(false);

        TRightPanel.add(invoiceNumLabel);
        TRightPanel.add(invoiceNumField);
        TRightPanel.add(invoiceDateLabel);
        TRightPanel.add(invoiceDateField);
        TRightPanel.add(customerNameLabel);
        TRightPanel.add(customerNameField);
        TRightPanel.add(invoiceTotalLabel);
        TRightPanel.add(invoiceTotalField);

        BRightPanel.add(jScrollPane2);
        RightPanel.add(TRightPanel,BorderLayout.NORTH);
        RightPanel.add(BRightPanel,BorderLayout.SOUTH);
        RightPanel.setBorder(BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), "Invoice Details", TitledBorder.CENTER, TitledBorder.TOP));
        RightPanel.setBackground(Color.WHITE);


        LeftPanel.setBackground(Color.WHITE);
        ButtonContainer.setBackground(Color.WHITE);
        ButtonContainer.setLayout(new FlowLayout());

        Border blackline = BorderFactory.createLineBorder(Color.black);
        setJMenuBar(mb);

        RightPanel.setPreferredSize(new Dimension(550, 600));
        RightPanel.setBorder(blackline);
        ButtonContainer.add(BtnLeft, BorderLayout.WEST);
        ButtonContainer.add(BtnRight, BorderLayout.SOUTH);
        BtnLeft.setLayout(new FlowLayout());
        BtnLeft.setPreferredSize(new Dimension(550, 50));
        BtnLeft.add(createInvoiceButton, BorderLayout.CENTER);
        BtnLeft.add(deleteInvoiceButton, BorderLayout.CENTER);
        BtnLeft.setBackground(Color.WHITE);

        BtnRight.setLayout(new FlowLayout());
        BtnRight.setBackground(Color.WHITE);
        BtnRight.setPreferredSize(new Dimension(550, 50));
        BtnRight.add(createLineButton, BorderLayout.CENTER);
        BtnRight.add(saveRecordButton, BorderLayout.CENTER);
        BtnRight.add(deleteLineButton, BorderLayout.CENTER);


        getContentPane().add(LeftPanel, BorderLayout.WEST);
        getContentPane().add(RightPanel, BorderLayout.EAST);
        getContentPane().add(ButtonContainer, BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack();
    }

    public FileOperations getFileOperations() {
        return fileOperations;
    }
    public ArrayList<InvoiceHeader> getInvoices() {
        if (invoices == null) invoices = new ArrayList<>();
        return invoices;
    }

    public void setInvoices(ArrayList<InvoiceHeader> invoices) {
        this.invoices = invoices;
    }

    public InvoiceHeaderTable getInvoicesTableModel() {
        if (invoicesTableModel == null) {
            invoicesTableModel = new InvoiceHeaderTable(getInvoices());
        }
        return invoicesTableModel;
    }

    public void setInvoicesTableModel(InvoiceHeaderTable invoicesTableModel) {
        this.invoicesTableModel = invoicesTableModel;
    }

    public JLabel getCustomerNameLabel() {
        return customerNameLabel;
    }

    public JLabel getInvoiceDateLabel() {
        return invoiceDateLabel;
    }

    public JLabel getInvoiceNumLabel() {
        return invoiceNumLabel;
    }
    public JTextField getCustomerNameField() {
        return customerNameField;
    }

    public JTextField getInvoiceDateField() {
        return invoiceDateField;
    }

    public JTextField getInvoiceNumField() {
        return invoiceNumField;
    }

    public JTable getInvoiceTable() {
        return invoiceTable;
    }

    public JTextField getInvoiceTotalLabel() {
        return invoiceTotalField;
    }
    public JTextField getInvoiceTotalField() {
        return invoiceTotalField;
    }


    public JTable getLineTable() {
        return lineTable;
    }


    public int getNextInvoiceNum() {
        int num = 0;

        for (InvoiceHeader invoice : getInvoices()) {
            if (invoice.getInvoiceNum() > num)
                num = invoice.getInvoiceNum();
        }

        return ++num;
    }


    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InvoiceFrame().setVisible(true);
            }
        });
    }
}
