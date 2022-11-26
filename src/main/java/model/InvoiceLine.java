package model;

public class InvoiceLine {
    private String itemName;
    private Double itemPrice;
    private int count;

    private InvoiceHeader invoice;

    public InvoiceLine() {
    }

    public InvoiceLine(String item, double price, int count, InvoiceHeader invoice) {
        this.itemName = item;
        this.itemPrice = price;
        this.count = count;
        this.invoice = invoice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getLineTotal() {
        return itemPrice * count;
    }

    @Override
    public String toString() {
        return "Line{" + "num=" + invoice.getInvoiceNum() + ", item=" + itemName + ", price=" + itemPrice + ", count=" + count + '}';
    }

    public InvoiceHeader getInvoice() {
        return invoice;
    }

    public String getAsCSV() {
        return invoice.getInvoiceNum() + "," + itemName + "," + itemPrice + "," + count;
    }

}
