package controller;

import model.InvoiceHeader;
import model.InvoiceHeaderTable;
import model.InvoiceLine;
import model.InvoiceLineTable;
import view.InvoiceDialog;
import view.InvoiceFrame;
import view.LineDialog;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.StringJoiner;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FileOperations implements ActionListener, ListSelectionListener {
    private InvoiceFrame frame;
    private InvoiceDialog invoiceDialog;
    private LineDialog lineDialog;

    public FileOperations(InvoiceFrame frame) {
        this.frame = frame;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
    String actionCommand = e.getActionCommand();
        System.out.println("Action: " + actionCommand);
        switch (actionCommand) {
        case "Load File":
            loadFile();
            break;
        case "Save File":
            saveFile();
            break;
        case "Create New Invoice":
            createNewInvoice();
            break;
        case "Delete Invoice":
            deleteInvoice();
            break;
        case "Create New Item":
            createNewItem();
            break;
        case "Delete Item":
            deleteItem();
            break;
        case "createInvoiceCancel":
            createInvoiceCancel();
            break;
        case "createInvoiceOK":
            createInvoiceOK();
            break;
        case "createLineOK":
            createLineOK();
            break;
        case "Save Record":
            updateRecord();
            break;
        case "createLineCancel":
            createLineCancel();
            break;

        }

}

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedIndex = frame.getInvoiceTable().getSelectedRow();
        if (selectedIndex != -1) {
            System.out.println("You have selected row: " + selectedIndex);
            InvoiceHeader currentInvoice = frame.getInvoices().get(selectedIndex);
            frame.getInvoiceNumField().setText("" + currentInvoice.getInvoiceNum());
            frame.getInvoiceDateField().setText(currentInvoice.getInvoiceDate());
            frame.getCustomerNameField().setText(currentInvoice.getCustomerName());
            frame.getInvoiceTotalField().setText("" + currentInvoice.getInvoiceTotal());
            InvoiceLineTable linesTableModel = new InvoiceLineTable(currentInvoice.getLines());
            frame.getLineTable().setModel(linesTableModel);
            linesTableModel.fireTableDataChanged();
        }
    }

    private void loadFile() {
        JFileChooser fc = new JFileChooser();
        try {
            int result = fc.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fc.getSelectedFile();
                Path headerPath = Paths.get(headerFile.getAbsolutePath());
                List<String> headerLines = Files.readAllLines(headerPath);
                System.out.println("Invoices have been read");

                ArrayList<InvoiceHeader> invoicesArray = new ArrayList<>();
                for (String headerLine : headerLines) {
                    try {
                        String[] headerParts = headerLine.split(",");
                        int invoiceNum = Integer.parseInt(headerParts[0]);
                        String invoiceDate = headerParts[1];
                        String customerName = headerParts[2];

                        InvoiceHeader invoice = new InvoiceHeader(invoiceNum, invoiceDate, customerName);
                        invoicesArray.add(invoice);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Error in line format", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                System.out.println("Check point");
                result = fc.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File lineFile = fc.getSelectedFile();
                    Path linePath = Paths.get(lineFile.getAbsolutePath());
                    List<String> lineLines = Files.readAllLines(linePath);
                    System.out.println("Lines have been read");
                    for (String lineLine : lineLines) {
                        try {
                            String lineParts[] = lineLine.split(",");
                            int invoiceNum = Integer.parseInt(lineParts[0]);
                            String itemName = lineParts[1];
                            double itemPrice = Double.parseDouble(lineParts[2]);
                            int count = Integer.parseInt(lineParts[3]);
                            InvoiceHeader inv = null;
                            for (InvoiceHeader invoice : invoicesArray) {
                                if (invoice.getInvoiceNum() == invoiceNum) {
                                    inv = invoice;
                                    break;
                                }
                            }

                            InvoiceLine line = new InvoiceLine(itemName, itemPrice, count, inv);
                            inv.getLines().add(line);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame, "Error in line format", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    System.out.println("Check point");
                }
                frame.setInvoices(invoicesArray);
                InvoiceHeaderTable invoicesTableModel = new InvoiceHeaderTable(invoicesArray);
                frame.setInvoicesTableModel(invoicesTableModel);
                frame.getInvoiceTable().setModel(invoicesTableModel);
                frame.getInvoicesTableModel().fireTableDataChanged();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Cannot read file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveFile() {
        ArrayList<InvoiceHeader> invoices = frame.getInvoices();
        String headers = "";
        String lines = "";
        for (InvoiceHeader invoice : invoices) {
            String invCSV = invoice.getAsCSV();
            headers += invCSV;
            headers += "\n";

            for (InvoiceLine line : invoice.getLines()) {
                String lineCSV = line.getAsCSV();
                lines += lineCSV;
                lines += "\n";
            }
        }
        System.out.println("Check point");
        try {
            JFileChooser fc = new JFileChooser();
            int result = fc.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fc.getSelectedFile();
                FileWriter hfw = new FileWriter(headerFile);
                hfw.write(headers);
                hfw.flush();
                hfw.close();
                result = fc.showSaveDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File lineFile = fc.getSelectedFile();
                    FileWriter lfw = new FileWriter(lineFile);
                    lfw.write(lines);
                    lfw.flush();
                    lfw.close();
                }
            }
        } catch (Exception ex) {

        }
    }

    private void createNewInvoice() {
        invoiceDialog = new InvoiceDialog(frame);
        invoiceDialog.setVisible(true);
    }

    private void deleteInvoice() {
        int selectedRow = frame.getInvoiceTable().getSelectedRow();
        if (selectedRow != -1) {
            frame.getInvoices().remove(selectedRow);
            frame.getInvoicesTableModel().fireTableDataChanged();
        }
    }

    private void createNewItem() {
        lineDialog = new LineDialog(frame);
        lineDialog.setVisible(true);
    }

    private void deleteItem() {
        int selectedRow = frame.getLineTable().getSelectedRow();

        if (selectedRow != -1) {
            InvoiceLineTable linesTableModel = (InvoiceLineTable) frame.getLineTable().getModel();
            linesTableModel.getLines().remove(selectedRow);
            linesTableModel.fireTableDataChanged();
            frame.getInvoicesTableModel().fireTableDataChanged();
        }
    }

    private void createInvoiceCancel() {
        invoiceDialog.setVisible(false);
        invoiceDialog.dispose();
        invoiceDialog = null;
    }

    private void createInvoiceOK() {
        String date = invoiceDialog.getInvDateField().getText();
        String customer = invoiceDialog.getCustNameField().getText();
        int num = frame.getNextInvoiceNum();
        try {
            String[] dateParts = date.split("-");
            if (dateParts.length < 3) {
                JOptionPane.showMessageDialog(frame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                int day = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);
                if (day > 31 || month > 12) {
                    JOptionPane.showMessageDialog(frame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    InvoiceHeader invoice = new InvoiceHeader(num, date, customer);
                    frame.getInvoices().add(invoice);
                    frame.getInvoicesTableModel().fireTableDataChanged();
                    invoiceDialog.setVisible(false);
                    invoiceDialog.dispose();
                    invoiceDialog = null;
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void createLineOK() {
        String item = lineDialog.getItemNameField().getText();
        String countStr = lineDialog.getItemCountField().getText();
        String priceStr = lineDialog.getItemPriceField().getText();
        int count = Integer.parseInt(countStr);
        double price = Double.parseDouble(priceStr);
        int selectedInvoice = frame.getInvoiceTable().getSelectedRow();
        if (selectedInvoice != -1) {
            InvoiceHeader invoice = frame.getInvoices().get(selectedInvoice);
            InvoiceLine line = new InvoiceLine(item, price, count, invoice);
            invoice.getLines().add(line);
            InvoiceLineTable linesTableModel = (InvoiceLineTable) frame.getLineTable().getModel();
            //linesTableModel.getLines().add(line);
            linesTableModel.fireTableDataChanged();
            frame.getInvoicesTableModel().fireTableDataChanged();
        }
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
    }

    private void createLineCancel() {
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
    }
    private void updateRecord(){
        int selectedIndex = frame.getInvoiceTable().getSelectedRow();
        System.out.println(" index: "+selectedIndex+ " has been updated successfully" );
        if (selectedIndex != -1) {
            System.out.println("You have selected row: " + selectedIndex);
            InvoiceHeader currentInvoice = frame.getInvoices().get(selectedIndex);
            currentInvoice.setInvoiceDate(frame.getInvoiceDateField().getText());
            currentInvoice.setCustomerName(frame.getCustomerNameField().getText());

           frame.getInvoicesTableModel().setValueAt(frame.getInvoiceDateField().getText(),1,selectedIndex);
           frame.getInvoicesTableModel().setValueAt(frame.getCustomerNameField().getText(),2,selectedIndex);
           frame.getInvoicesTableModel().fireTableDataChanged();
           saveFile();
        }
    }

}
