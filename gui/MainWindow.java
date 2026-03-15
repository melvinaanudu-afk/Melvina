package gui;

import controller.*;
import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MainWindow extends JFrame {
    private LibraryManager manager;
    private DefaultTableModel itemTableModel, userTableModel, searchTableModel;
    private JTextArea reportArea;
    private JComboBox<String> sortAlgoBox, sortCriteriaBox;

    public MainWindow() {
        manager = new LibraryManager();
        setupMainFrame();
        refreshAllData();
    }

    private void setupMainFrame() {
        setTitle("MIVA Smart Library System 2026");
        setSize(1150, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.BLACK);
        JLabel title = new JLabel("  MIVA UNIVERSITY LIBRARY", JLabel.LEFT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        header.add(title, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // Menu & Tabs
        setJMenuBar(createMenuBar());
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Inventory", createViewItemsTab());
        tabs.addTab("Circulation", createBorrowTab());
        tabs.addTab("Admin", createAdminTab());
        tabs.addTab("Search", createSearchSortTab());
        tabs.addTab("Reports", createReportsTab1());
        add(tabs, BorderLayout.CENTER);

        // Status Bar
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        statusBar.add(new JLabel("Ready | Actions in Stack: " + manager.getUndoStackSize()));
        add(statusBar, BorderLayout.SOUTH);
    }

    private JMenuBar createMenuBar() {
        JMenuBar mb = new JMenuBar();
        JMenu edit = new JMenu("Edit");
        JMenuItem undo = new JMenuItem("Undo");
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
        undo.addActionListener(e -> { manager.undoLastAction(); refreshAllData(); });
        edit.add(undo);
        mb.add(edit);
        return mb;
    }

    private JPanel createViewItemsTab() {
        JPanel p = new JPanel(new BorderLayout());
        itemTableModel = new DefaultTableModel(new String[]{"ID", "Type", "Title", "Author", "Year", "Status"}, 0);
        
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sortAlgoBox = new JComboBox<>(new String[]{"QuickSort", "SelectionSort"});
        sortCriteriaBox = new JComboBox<>(new String[]{"Title", "Author"});
        JButton btn = new JButton("Sort");
        btn.addActionListener(e -> { manager.sortInventory((String)sortCriteriaBox.getSelectedItem(), (String)sortAlgoBox.getSelectedItem()); refreshAllData(); });
        
        top.add(new JLabel("Algorithm:")); top.add(sortAlgoBox); top.add(btn);
        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(new JTable(itemTableModel)), BorderLayout.CENTER);
        return p;
    }

    // ... inside MainWindow class ...

    private JPanel createBorrowTab() {
        // Reuse your BorrowPanel which already contains the Return button logic
        BorrowController controller = new BorrowController(manager);
        
        // We pass 'this::refreshAllData' so the tables update immediately
        BorrowPanel panel = new BorrowPanel(controller, this::refreshAllData);
        
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.add(panel);
        return centerWrapper;
    }

    private JPanel createReportsTab1() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        reportArea.setBackground(new Color(245, 245, 245));

        JButton genBtn = new JButton("Generate System Report");
        genBtn.addActionListener(e -> {
            reportArea.setText(manager.generateReport());
        });

        p.add(new JLabel("Library Transaction Logs:"), BorderLayout.NORTH);
        p.add(new JScrollPane(reportArea), BorderLayout.CENTER);
        p.add(genBtn, BorderLayout.SOUTH);
        return p;
    }

    private JPanel createAdminTab() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel form = new JPanel();
        JTextField nf = new JTextField(10), ef = new JTextField(10);
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"Student", "Faculty", "Admin"});
        JButton addBtn = new JButton("Add User");
        addBtn.addActionListener(e -> { manager.addUser(nf.getText(), ef.getText(), (String)roleBox.getSelectedItem()); refreshAllData(); });
        form.add(new JLabel("Name:")); form.add(nf); form.add(ef); form.add(roleBox); form.add(addBtn);
        userTableModel = new DefaultTableModel(new String[]{"UID", "Name", "Email", "Role"}, 0);
        p.add(form, BorderLayout.NORTH);
        p.add(new JScrollPane(new JTable(userTableModel)), BorderLayout.CENTER);
        return p;
    }

    private JPanel createSearchSortTab() {
        JPanel p = new JPanel(new BorderLayout());
        JTextField q = new JTextField(15);
        JComboBox<String> m = new JComboBox<>(new String[]{"Recursive Search", "Binary Search", "Linear Search"});
        JButton b = new JButton("Search");
        searchTableModel = new DefaultTableModel(new String[]{"ID", "Title", "Status"}, 0);
        
        b.addActionListener(e -> {
            searchTableModel.setRowCount(0);
            LibraryItem res = manager.performSearch(q.getText(), (String)m.getSelectedItem());
            if (res != null) searchTableModel.addRow(new Object[]{res.getId(), res.getTitle(), res.getStatus()});
        });

        JPanel t = new JPanel(); t.add(q); t.add(m); t.add(b);
        p.add(t, BorderLayout.NORTH);
        p.add(new JScrollPane(new JTable(searchTableModel)), BorderLayout.CENTER);
        return p;
    }

    private void refreshAllData() {
        if (itemTableModel != null) {
            itemTableModel.setRowCount(0);
            for (LibraryItem i : manager.getInventory()) 
                itemTableModel.addRow(new Object[]{i.getId(), i.getType(), i.getTitle(), i.getAuthor(), i.getYear(), i.getStatus()});
        }
        if (userTableModel != null) {
            userTableModel.setRowCount(0);
            for (User u : manager.getUsers()) 
                userTableModel.addRow(new Object[]{u.getUserId(), u.getName(), u.getEmail(), u.getRole()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
    
}