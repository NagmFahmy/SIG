package view;

import javax.swing.*;
import java.awt.*;

public class LineDialog extends JDialog {
    private JTextField itemNameField;
    private JTextField itemCountField;
    private JTextField itemPriceField;
    private JLabel itemNameLbl;
    private JLabel itemCountLbl;
    private JLabel itemPriceLbl;
    private JButton okBtn;
    private JButton cancelBtn;

    public LineDialog(InvoiceFrame frame) {
        itemNameField = new JTextField(20);
        itemNameLbl = new JLabel("Item Name");

        itemCountField = new JTextField(20);
        itemCountLbl = new JLabel("Item Count");

        itemPriceField = new JTextField(20);
        itemPriceLbl = new JLabel("Item Price");

        okBtn = new JButton("OK");
        cancelBtn = new JButton("Cancel");

        okBtn.setActionCommand("createLineOK");
        cancelBtn.setActionCommand("createLineCancel");

        okBtn.addActionListener(frame.getFileOperations());
        cancelBtn.addActionListener(frame.getFileOperations());
        setLayout(new GridLayout(4, 2));

        add(itemNameLbl);
        add(itemNameField);
        add(itemCountLbl);
        add(itemCountField);
        add(itemPriceLbl);
        add(itemPriceField);
        add(okBtn);
        add(cancelBtn);

        pack();
    }

    public JTextField getItemNameField() {
        return itemNameField;
    }

    public JTextField getItemCountField() {
        return itemCountField;
    }

    public JTextField getItemPriceField() {
        return itemPriceField;
    }
}
