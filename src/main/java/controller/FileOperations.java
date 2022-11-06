package controller;

import view.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.List;
import java.util.StringJoiner;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FileOperations {
    public static void LoadFileF(File file ,JTable InvoiceList) {
        URL resource = MainFrame.class.getClassLoader().getResource("DB/SalesInvoice-db.csv");
        if (resource == null) {
            throw new IllegalArgumentException("file not found!");
        } else {
            try {
                file = new File(resource.toURI());

                toCSV(InvoiceList, file);


            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        }


    }

    public static void SaveItemsToCSV(JTable InvoiceList,JTable InvoiceDetailList,int index) {
        try {
            URL resource = MainFrame.class.getClassLoader().getResource("DB/SalesInvoice-db.csv");
            if (resource == null) {
                throw new IllegalArgumentException("file not found!");
            } else {
                try {
                    File file = new File(resource.toURI());
                    try (Writer writer = new FileWriter(file)) {
                        WriteItemsTo(InvoiceList.getModel(), writer,InvoiceDetailList,index);
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }


            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void toCSV(JTable table, File file) {
        try {
            writeTo(table.getModel(), file);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void WriteItemsTo(TableModel dtm, Writer out,JTable InvoiceDetailList, int index) throws IOException {
        final String LINE_SEP = System.getProperty("line.separator");
        TableModel detailsModel = InvoiceDetailList.getModel();
        int numCols = dtm.getColumnCount();
        int numRows = dtm.getRowCount();
        int detailsRows = detailsModel.getRowCount();
        int detailsColumns = detailsModel.getColumnCount();

        StringJoiner joiner = new StringJoiner(",");
        StringJoiner details = new StringJoiner(",");
        for (int i = 0; i < numCols; i++) {
            joiner.add(dtm.getColumnName(i));
        }
        joiner.add("items");
        out.write(joiner.toString());
        out.write(LINE_SEP);

        for (int r = 0; r < detailsRows; r++) {
            details = new StringJoiner("-");
            for (int c = 0; c < detailsColumns; c++) {
                details.add(detailsModel.getValueAt(r, c).toString());
            }

        }

        for (int r = 0; r < numRows; r++) {
            joiner = new StringJoiner(",");
            for (int c = 0; c < numCols; c++) {
                joiner.add(dtm.getValueAt(r, c).toString());
            }

            if (r == index) {
                joiner.add(List.of(details).toString());
                out.write(joiner.toString());
                out.write(LINE_SEP);

            } else {
                joiner.add("[]");
                out.write(joiner.toString());
                out.write(LINE_SEP);

            }
        }
    }

        public static HashMap<Integer, Object> items;
        public static HashMap <Integer, JTable> detailTables;

        public static DefaultTableModel loadFrom(File file) throws IOException {
            return loadFrom(file, null);
        }

        public static DefaultTableModel loadFrom(File file, Vector<Object> headers) throws IOException {
            try (Reader is = new FileReader(file)) {
                return loadFrom(is, headers);
            }
        }

        public static DefaultTableModel loadFrom(Reader reader, Vector<Object> headers) {
            DefaultTableModel model = null;
            Vector<Vector<Object>> rows = new Vector<Vector<Object>>();
            items = new HashMap<Integer, Object>();
            detailTables = new HashMap<Integer,JTable>();
            Scanner s = new Scanner(reader);

            while (s.hasNextLine()) {
                rows.add(new Vector<Object>(Arrays.asList(s.nextLine().split("\\s*,\\s*"))));
            }
            AtomicInteger counter = new AtomicInteger(0);

            rows.forEach((obj) -> {
                var count = counter.getAndIncrement();
                items.put(count, obj.get(obj.size() - 1));
                detailTables.put(count,new JTable(MainFrame.InvoiceDetailsData,MainFrame.InvoiceDetailsColumns));
            });
            items.remove(0);

            if (headers == null && rows.size()> 0) {

                for (int i = 0; i < rows.size(); i++) {

                    int lastIndex = rows.get(i).size() - 1;
                    rows.get(i).remove(lastIndex);
                };

                headers = rows.remove(0);

            }
            model = new DefaultTableModel(rows, headers);

            return model;
        }

        public static void writeTo(TableModel dtm, File file) throws IOException {
            try (Writer writer = new FileWriter(file)) {
                writeTo(dtm, writer);
            }
        }

        public static void writeTo(TableModel dtm, Writer out) throws IOException {
            final String LINE_SEP = System.getProperty("line.separator");
            int numCols = dtm.getColumnCount();
            int numRows = dtm.getRowCount();

            StringJoiner joiner = new StringJoiner(",");
            for (int i = 0; i < numCols; i++) {
                joiner.add(dtm.getColumnName(i));
            }
            out.write(joiner.toString());

            out.write(LINE_SEP);

            for (int r = 0; r < numRows; r++) {
                joiner = new StringJoiner(",");
                for (int c = 0; c < numCols; c++) {
                    joiner.add(dtm.getValueAt(r, c).toString());
                }
                out.write(joiner.toString());
                out.write(LINE_SEP);
            }
        }

    }
