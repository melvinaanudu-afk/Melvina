package gui;

import controller.*;
import model.LibraryItem;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class searchSortPanel extends JPanel {
    private LibraryManager manager;
    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> algoBox;
    private JTextField searchField;
    private JLabel statusLabel;
    
    private boolean isSortedByTitle = false;

    public searchSortPanel(LibraryManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        refreshTable();
    }

    private void initComponents() {

        JPanel controls = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        algoBox = new JComboBox<>(new String[]{"Merge Sort (Title)", "Selection Sort (Year)"});
        JButton sortBtn = new JButton("Apply Sort");
      
        searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search Title");
        statusLabel = new JLabel("Status: Unsorted");


        gbc.gridx = 0; gbc.gridy = 0; controls.add(new JLabel("Algorithm:"), gbc);
        gbc.gridx = 1; controls.add(algoBox, gbc);
        gbc.gridx = 2; controls.add(sortBtn, gbc);
        gbc.gridx = 3; controls.add(new JLabel(" | Search:"), gbc);
        gbc.gridx = 4; controls.add(searchField, gbc);
        gbc.gridx = 5; controls.add(searchBtn, gbc);
        gbc.gridx = 6; controls.add(statusLabel, gbc);

        add(controls, BorderLayout.NORTH);


        String[] columns = {"ID", "Type", "Title", "Author", "Year", "Status"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

      
        sortBtn.addActionListener(e -> {
            String selection = (String) algoBox.getSelectedItem();
            if ("Merge Sort (Title)".equals(selection)) {
                SearchEngine.mergeSort(manager.getInventory());
                isSortedByTitle = true;
                statusLabel.setText("Status: Sorted by Title");
            } else {
              
                SearchEngine.selectionSortByYear(manager.getInventory());
                isSortedByTitle = false;
                statusLabel.setText("Status: Sorted by Year");
            }
            refreshTable();
        });

        searchBtn.addActionListener(e -> {
            String query = searchField.getText().trim();
            if (query.isEmpty()) return;

            if (isSortedByTitle) {
         
                int index = SearchEngine.binarySearchByTitle(manager.getInventory(), query);
                highlightResult(index);
            } else {
         
                int index = manager.recursiveSearch(query, 0);
                highlightResult(index);
            }
        });
    }

    private void highlightResult(int index) {
        if (index != -1) {
            table.setRowSelectionInterval(index, index);
            table.scrollRectToVisible(table.getCellRect(index, 0, true));
        } else {
            JOptionPane.showMessageDialog(this, "Item not found in catalog.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void refreshTable() {
        model.setRowCount(0);
        for (LibraryItem item : manager.getInventory()) {
            model.addRow(new Object[]{
                item.getId(), item.getType(), item.getTitle(), 
                item.getAuthor(), item.getYear(), item.getStatus()
            });
        }
        }
        
        }
