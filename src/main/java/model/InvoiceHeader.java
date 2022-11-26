package model;

import java.util.ArrayList;
import java.util.Date;

public class InvoiceHeader {
    private int invoiceNum;
    private String invoiceDate;
    private String customerName;
    private ArrayList<InvoiceLine> invoiceLines;

    public InvoiceHeader() {
    }

    public InvoiceHeader(int num, String date, String customer) {
        this.invoiceNum = num;
        this.invoiceDate = date;
        this.customerName = customer;
    }

    public int getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(int invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public ArrayList<InvoiceLine> getInvoiceLines() {
        return invoiceLines;
    }

    public void setInvoiceLines(ArrayList<InvoiceLine> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }

    public double getInvoiceTotal() {
        double total = 0.0;
        for (InvoiceLine line : getLines()) {
            total += line.getLineTotal();
        }
        return total;
    }

    public ArrayList<InvoiceLine> getLines() {
        if (invoiceLines == null) {
            invoiceLines = new ArrayList<>();
        }
        return invoiceLines;
    }


    @Override
    public String toString() {
        return "Invoice{" + "num=" + invoiceNum + ", date=" + invoiceDate + ", customer=" + customerName + '}';
    }

    public String getAsCSV() {
        return invoiceNum + "," + invoiceDate + "," + customerName;
    }

}
