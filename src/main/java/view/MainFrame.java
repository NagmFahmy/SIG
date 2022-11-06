package view;

import com.sun.tools.javac.Main;
import controller.FileOperations;

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
import java.util.logging.Level;
import java.util.logging.Logger;
public class MainFrame extends JFrame{

    static File file;
    static JTable InvoiceList;
    static JTable InvoiceDetailList;
    static DefaultTableModel InvoiceListModel;
    static DefaultTableModel ListMainHeaders;
    static DefaultTableModel ListDetailsHeaders;
    static JPanel BRightPanel;
    static JPanel RightPanel;
    static JMenuItem LoadFile;
    static int index = 0;
    public static String[] InvoiceDetailsColumns = {"No.", "Item Name", "Item Price", "Count", "Item Total"};
    public static String[][] InvoiceDetailsData = {{"", "", "", "", ""}};

    public static void intialize() {


        JFrame MainFrame = new JFrame();
        MainFrame.getContentPane().setLayout(new BorderLayout());
        JPanel RightPanel = new JPanel();
        JPanel LeftPanel = new JPanel();
        JPanel TRightPanel = new JPanel();
        JPanel BRightPanel = new JPanel();
        JPanel ButtonContainer = new JPanel();
        JPanel BtnLeft = new JPanel();
        JPanel BtnRight = new JPanel();
        JMenuBar mb = new JMenuBar();
        JMenu File = new JMenu("File");

        LoadFile = new JMenuItem("Load File");

        JMenuItem SaveFile = new JMenuItem("Save File");
        File.add(LoadFile);
        File.add(SaveFile);
        mb.add(File);


        JButton CreateNewInvoice = new JButton("Create New Invoice");
        JButton DeleteInvoice = new JButton("Delete Selected Invoice");
        JButton SaveRecord = new JButton("Save");
        JButton CancelSelection = new JButton("Cancel");
        //LeftPanel.setLayout(new BorderLayout());
        LeftPanel.setPreferredSize(new Dimension(550, 600));
        LeftPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Invoice Table", TitledBorder.CENTER, TitledBorder.TOP));
        JScrollPane s = new JScrollPane();
        String[] InvoiceListColumns = {"No.", "Invoice Date", "Customer Name", "Total Amount"};
        String[][] InvoiceListData = {{"", "", "", ""}};

        InvoiceList = new JTable(InvoiceListData, InvoiceListColumns);
        InvoiceDetailList = new JTable(InvoiceDetailsData,InvoiceListColumns);
        InvoiceList.setBounds(30, 40, 200, 300);


        ListMainHeaders = new DefaultTableModel(InvoiceListData, InvoiceListColumns);
        LoadFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                URL resource = getClass().getClassLoader().getResource("DB/SalesInvoice-db.csv");
                if (resource == null) {
                    throw new IllegalArgumentException("file not found!");
                } else {
                    try {
                        file = new File(resource.toURI());
                        EventQueue.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    InvoiceListModel = FileOperations.loadFrom(file);
                                    if (InvoiceListModel.getColumnCount() != 0) {
                                        InvoiceList.setModel(InvoiceListModel);
                                    } else {
                                        InvoiceList.setModel(ListMainHeaders);
                                        InvoiceListModel = ListMainHeaders;
                                    }
                                } catch (IOException ex) {
                                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        });


                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }

                }
            }

        });
        CreateNewInvoice.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (InvoiceListModel == null) {
                    InvoiceListModel = ListMainHeaders;
                    InvoiceList.setModel(ListMainHeaders);
                }
                InvoiceListModel.addRow(InvoiceListData[0]);
            }
        });


        DeleteInvoice.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (InvoiceList.getSelectedRow() != -1) {
                    FileOperations.toCSV(InvoiceList,file);
                    FileOperations.SaveItemsToCSV(InvoiceList,InvoiceDetailList,index);
                    // remove selected row from the model
                    InvoiceListModel.removeRow(InvoiceList.getSelectedRow());
                    JOptionPane.showMessageDialog(null, "Selected row deleted successfully");
                }
            }
        });


        s.setBackground(Color.green);
        LeftPanel.add(new JScrollPane(InvoiceList));
        TRightPanel.setLayout(new GridLayout(5, 5));

        JLabel id_label = new JLabel("Invoice Number");
        JTextField id_textf = new JTextField();
        id_textf.setEditable(false);
        JLabel date_label = new JLabel("Invoice Date");
        JTextField date_textf = new JTextField();
        JLabel name_label = new JLabel("Customer Name");
        JTextField name_textf = new JTextField();
        JLabel amount_label = new JLabel("Total Amount");
        JTextField amount_textf = new JTextField();
        amount_textf.setEditable(false);

        TRightPanel.add(id_label);
        TRightPanel.add(id_textf);
        TRightPanel.add(date_label);
        TRightPanel.add(date_textf);
        TRightPanel.add(name_label);
        TRightPanel.add(name_textf);
        TRightPanel.add(amount_label);
        TRightPanel.add(amount_textf);

        BRightPanel.add(new JScrollPane(InvoiceDetailList));
        RightPanel.add(TRightPanel,BorderLayout.NORTH);
        RightPanel.add(BRightPanel,BorderLayout.SOUTH);
        RightPanel.setBorder(BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), "Invoice Details", TitledBorder.CENTER, TitledBorder.TOP));
        RightPanel.setBackground(Color.WHITE);

        InvoiceList.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                index = InvoiceList.getSelectedRow();
                id_textf.setText(InvoiceListModel.getValueAt(index,0 ).toString());
                date_textf.setText(InvoiceListModel.getValueAt(index, 1).toString());
                name_textf.setText(InvoiceListModel.getValueAt(index, 2).toString());
                amount_textf.setText(InvoiceListModel.getValueAt(index, 3).toString());
                if(index != -1){
                String str2 = FileOperations.items.get(index + 1).toString();
                String str3 = str2.replace("[","");
                String itemIndex = str3.replace("]", "");
                String[] items = itemIndex.split("-");
                Object[][] newArray;
                if(itemIndex != "" && items.length > 0){
                  newArray = arr(items, 1, 5);
//                  InvoiceDetailList = LoadFromFile.detailTables.get(index);
                  ListDetailsHeaders = new DefaultTableModel(newArray, InvoiceDetailsColumns);
                  InvoiceDetailList.setModel(ListDetailsHeaders);
//                  InvoiceDetailList.setVisible(true);
                }
                else {
                    System.out.println("Else");
                    ListDetailsHeaders = new DefaultTableModel(InvoiceDetailsData, InvoiceDetailsColumns);
                    InvoiceDetailList.setModel(ListDetailsHeaders);
                }

            }

            }
        });


        SaveRecord.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int i = InvoiceDetailList.getSelectedRow();
                if (i >= 0) {
                    InvoiceListModel.setValueAt(id_textf.getText(), i, 0);
                    InvoiceListModel.setValueAt(date_textf.getText(), i, 1);
                    InvoiceListModel.setValueAt(name_textf.getText(), i, 2);
                    InvoiceListModel.setValueAt(amount_textf.getText(), i, 3);
                    FileOperations.SaveItemsToCSV(InvoiceList,InvoiceDetailList,index);

                } else {
                    System.out.println("UpdateError");

                }
            }


        });
        SaveFile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                FileOperations.LoadFileF(file,InvoiceList);
            }
        });

        LeftPanel.setBackground(Color.WHITE);
        ButtonContainer.setBackground(Color.WHITE);
        ButtonContainer.setLayout(new FlowLayout());

        Border blackline = BorderFactory.createLineBorder(Color.black);
        MainFrame.setJMenuBar(mb);

        RightPanel.setPreferredSize(new Dimension(550, 600));
        RightPanel.setBorder(blackline);
        ButtonContainer.add(BtnLeft, BorderLayout.WEST);
        ButtonContainer.add(BtnRight, BorderLayout.SOUTH);
        BtnLeft.setLayout(new FlowLayout());
        BtnLeft.setPreferredSize(new Dimension(550, 50));
        BtnLeft.add(CreateNewInvoice, BorderLayout.CENTER);
        BtnLeft.add(DeleteInvoice, BorderLayout.CENTER);
        BtnLeft.setBackground(Color.WHITE);

        BtnRight.setLayout(new FlowLayout());
        BtnRight.setBackground(Color.WHITE);
        BtnRight.setPreferredSize(new Dimension(550, 50));
        BtnRight.add(SaveRecord, BorderLayout.CENTER);
        BtnRight.add(CancelSelection, BorderLayout.CENTER);


        MainFrame.getContentPane().add(LeftPanel, BorderLayout.WEST);
        MainFrame.getContentPane().add(RightPanel, BorderLayout.EAST);
        MainFrame.getContentPane().add(ButtonContainer, BorderLayout.SOUTH);

        MainFrame.pack();
        MainFrame.setVisible(true);
        MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }



    static Object[][] arr(String[] a, int r1, int c1) {
        int i = 0;
        Object[][] two = new Object[r1][c1];
        Object[][] array = new Object[r1][c1];
        for (int y = 0; y < r1; y++) {
            for (int x = 0; x < c1; x++) {
                two[y][x] = a[i];
                i++;
                array = two;
            }
        }
        return array;
    }

    public static void main(String args[]) {
        MainFrame.intialize();
    }
}
