
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class StudentRegistrationSystem extends JFrame {

    // ---------- Theme ----------
    private final Color BG = new Color(15, 23, 42);
    private final Color SIDEBAR = new Color(17, 27, 48);
    private final Color CARD = new Color(30, 41, 59);
    private final Color INPUT = new Color(39, 52, 73);
    private final Color ACCENT = new Color(45, 212, 191);
    private final Color TEXT = new Color(241, 245, 249);
    private final Color MUTED = new Color(148, 163, 184);
    private final Color BORDER = new Color(51, 65, 85);

    // ---------- Navigation & Views ----------
    private JLabel dashboardMenuItem;
    private JLabel registrationMenuItem;
    private JLabel studentsMenuItem;
    private String currentView = "DASHBOARD";

    private JLabel headerTitle;
    private JLabel headerSubtitle;
    private JPanel statsPanel;
    private JPanel workspace;
    private JPanel formCard;
    private JPanel tableCard;

    // ---------- Form Components ----------
    private JTextField nameField;
    private JTextField rollField;
    private JTextField emailField;
    private JTextField searchField;

    private JComboBox<String> courseBox;
    private JComboBox<String> divisionBox;
    private JComboBox<String> genderBox;

    private JTable studentTable;
    private DefaultTableModel tableModel;

    private JLabel totalLabel;
    private JLabel divisionALabel;
    private JLabel divisionBLabel;
    private JLabel divisionCLabel;
    private JLabel statusLabel;

    private final Set<String> registeredRollNumbers = new HashSet<>();

    public StudentRegistrationSystem() {

        setTitle("Student Registration System");
        setSize(1250, 780);
        setMinimumSize(new Dimension(1050, 680));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);
        add(createMainContent(), BorderLayout.CENTER);

        updateStatistics();
    }

    // ---------- Sidebar ----------

    private JPanel createSidebar() {

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(SIDEBAR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("  STUDENT HUB");
        logo.setFont(new Font("SansSerif", Font.BOLD, 19));
        logo.setForeground(ACCENT);
        logo.setBorder(new EmptyBorder(30, 15, 35, 10));

        sidebar.add(logo);

        JLabel menuTitle = new JLabel("  MAIN MENU");
        menuTitle.setForeground(MUTED);
        menuTitle.setFont(new Font("SansSerif", Font.BOLD, 11));
        menuTitle.setBorder(new EmptyBorder(0, 15, 12, 0));

        sidebar.add(menuTitle);

        dashboardMenuItem = createMenuItem("▣   Dashboard", "DASHBOARD", true);
        registrationMenuItem = createMenuItem("＋   Registration", "REGISTRATION", false);
        studentsMenuItem = createMenuItem("▤   Students", "STUDENTS", false);

        sidebar.add(dashboardMenuItem);
        sidebar.add(registrationMenuItem);
        sidebar.add(studentsMenuItem);

        sidebar.add(Box.createVerticalGlue());

        JPanel footer = new JPanel();
        footer.setOpaque(false);
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setBorder(new EmptyBorder(20, 20, 25, 20));

        JLabel footerTitle = new JLabel("OOP PROJECT");
        footerTitle.setForeground(TEXT);
        footerTitle.setFont(new Font("SansSerif", Font.BOLD, 13));

        JLabel footerSub = new JLabel("Java Swing Application");
        footerSub.setForeground(MUTED);
        footerSub.setFont(new Font("SansSerif", Font.PLAIN, 11));

        footer.add(footerTitle);
        footer.add(Box.createVerticalStrut(5));
        footer.add(footerSub);

        sidebar.add(footer);

        return sidebar;
    }

    private JLabel createMenuItem(String text, String viewKey, boolean active) {

        JLabel item = new JLabel(text);
        item.setFont(new Font("SansSerif", active ? Font.BOLD : Font.PLAIN, 14));
        item.setForeground(active ? ACCENT : MUTED);
        item.setOpaque(true);
        item.setBackground(active ? CARD : SIDEBAR);
        item.setBorder(new EmptyBorder(15, 20, 15, 10));
        item.setAlignmentX(Component.LEFT_ALIGNMENT);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setActiveMenu(viewKey);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!viewKey.equals(currentView)) {
                    item.setBackground(new Color(24, 36, 60));
                    item.setForeground(TEXT);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!viewKey.equals(currentView)) {
                    item.setBackground(SIDEBAR);
                    item.setForeground(MUTED);
                }
            }
        });

        return item;
    }

    private void setActiveMenu(String viewKey) {
        if (viewKey.equals(currentView) && workspace != null && workspace.getComponentCount() > 0) {
            return;
        }
        currentView = viewKey;

        updateMenuItemState(dashboardMenuItem, "DASHBOARD".equals(viewKey));
        updateMenuItemState(registrationMenuItem, "REGISTRATION".equals(viewKey));
        updateMenuItemState(studentsMenuItem, "STUDENTS".equals(viewKey));

        switchView(viewKey);
    }

    private void updateMenuItemState(JLabel item, boolean active) {
        if (item != null) {
            item.setForeground(active ? ACCENT : MUTED);
            item.setBackground(active ? CARD : SIDEBAR);
            item.setFont(new Font("SansSerif", active ? Font.BOLD : Font.PLAIN, 14));
        }
    }

    private void switchView(String viewKey) {
        if (workspace == null || formCard == null || tableCard == null) {
            return;
        }

        workspace.removeAll();

        if ("DASHBOARD".equals(viewKey)) {
            if (headerTitle != null) headerTitle.setText("Student Dashboard");
            if (headerSubtitle != null) headerSubtitle.setText("Manage student registrations in one place");
            if (statsPanel != null) statsPanel.setVisible(true);

            workspace.setLayout(new BorderLayout(20, 0));
            formCard.setPreferredSize(new Dimension(350, 0));
            formCard.setMaximumSize(new Dimension(350, Integer.MAX_VALUE));
            workspace.add(formCard, BorderLayout.WEST);
            workspace.add(tableCard, BorderLayout.CENTER);

        } else if ("REGISTRATION".equals(viewKey)) {
            if (headerTitle != null) headerTitle.setText("Student Registration");
            if (headerSubtitle != null) headerSubtitle.setText("Enter student details below to register");
            if (statsPanel != null) statsPanel.setVisible(false);

            workspace.setLayout(new BorderLayout());
            formCard.setPreferredSize(new Dimension(460, 0));

            JPanel centerBox = new JPanel(new GridBagLayout());
            centerBox.setOpaque(false);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            gbc.fill = GridBagConstraints.VERTICAL;
            centerBox.add(formCard, gbc);

            workspace.add(centerBox, BorderLayout.CENTER);

        } else if ("STUDENTS".equals(viewKey)) {
            if (headerTitle != null) headerTitle.setText("Registered Students");
            if (headerSubtitle != null) headerSubtitle.setText("View, search, and manage registered students");
            if (statsPanel != null) statsPanel.setVisible(false);

            workspace.setLayout(new BorderLayout());
            workspace.add(tableCard, BorderLayout.CENTER);
        }

        workspace.revalidate();
        workspace.repaint();
    }

    // ---------- Main Dashboard ----------

    private JPanel createMainContent() {

        JPanel main = new JPanel(new BorderLayout(0, 20));
        main.setBackground(BG);
        main.setBorder(new EmptyBorder(28, 30, 25, 30));

        main.add(createHeader(), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 20));
        content.setOpaque(false);

        content.add(createStatisticsPanel(), BorderLayout.NORTH);
        content.add(createWorkspace(), BorderLayout.CENTER);

        main.add(content, BorderLayout.CENTER);

        return main;
    }

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));

        headerTitle = new JLabel("Student Dashboard");
        headerTitle.setFont(new Font("SansSerif", Font.BOLD, 27));
        headerTitle.setForeground(TEXT);

        headerSubtitle = new JLabel(
                "Manage student registrations in one place");
        headerSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        headerSubtitle.setForeground(MUTED);

        heading.add(headerTitle);
        heading.add(Box.createVerticalStrut(7));
        heading.add(headerSubtitle);

        JLabel badge = new JLabel("  ● SYSTEM ONLINE  ");
        badge.setForeground(ACCENT);
        badge.setFont(new Font("SansSerif", Font.BOLD, 11));
        badge.setOpaque(true);
        badge.setBackground(new Color(20, 55, 62));
        badge.setBorder(new EmptyBorder(10, 8, 10, 8));

        header.add(heading, BorderLayout.WEST);
        header.add(badge, BorderLayout.EAST);

        return header;
    }

    // ---------- Statistics ----------

    private JPanel createStatisticsPanel() {

        statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setOpaque(false);
        statsPanel.setPreferredSize(new Dimension(0, 105));

        totalLabel = new JLabel("0");
        divisionALabel = new JLabel("0");
        divisionBLabel = new JLabel("0");
        divisionCLabel = new JLabel("0");

        statsPanel.add(createStatCard("TOTAL STUDENTS", totalLabel, ACCENT));
        statsPanel.add(createStatCard("DIVISION A", divisionALabel,
                new Color(96, 165, 250)));
        statsPanel.add(createStatCard("DIVISION B", divisionBLabel,
                new Color(192, 132, 252)));
        statsPanel.add(createStatCard("DIVISION C", divisionCLabel,
                new Color(251, 191, 36)));

        return statsPanel;
    }

    private JPanel createStatCard(String title, JLabel value, Color accent) {

        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(CARD);
        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(15, 18, 15, 18)));

        JLabel heading = new JLabel(title);
        heading.setForeground(MUTED);
        heading.setFont(new Font("SansSerif", Font.BOLD, 11));

        value.setForeground(accent);
        value.setFont(new Font("SansSerif", Font.BOLD, 27));

        card.add(heading, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);

        return card;
    }

    // ---------- Workspace ----------

    private JPanel createWorkspace() {

        workspace = new JPanel(new BorderLayout(20, 0));
        workspace.setOpaque(false);

        formCard = createFormPanel();
        formCard.setPreferredSize(new Dimension(350, 0));

        tableCard = createTablePanel();

        workspace.add(formCard, BorderLayout.WEST);
        workspace.add(tableCard, BorderLayout.CENTER);

        return workspace;
    }

    private JPanel createFormPanel() {

        class FormScrollPanel extends JPanel implements Scrollable {
            @Override
            public Dimension getPreferredScrollableViewportSize() {
                return getPreferredSize();
            }

            @Override
            public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
                return 16;
            }

            @Override
            public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
                return 48;
            }

            @Override
            public boolean getScrollableTracksViewportWidth() {
                return true;
            }

            @Override
            public boolean getScrollableTracksViewportHeight() {
                return false;
            }
        }

        JPanel form = new FormScrollPanel();
        form.setBackground(CARD);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(22, 22, 22, 22));

        JLabel title = new JLabel("New Registration");
        title.setFont(new Font("SansSerif", Font.BOLD, 19));
        title.setForeground(TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Enter student details below");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(title);
        form.add(Box.createVerticalStrut(6));
        form.add(subtitle);
        form.add(Box.createVerticalStrut(22));

        nameField = createTextField();
        rollField = createTextField();
        emailField = createTextField();

        courseBox = createComboBox(new String[]{
                "Information Technology",
                "Computer Engineering",
                "Mechanical Engineering",
                "Civil Engineering",
                "Electronics Engineering"
        });

        divisionBox = createComboBox(new String[]{"A", "B", "C"});
        genderBox = createComboBox(new String[]{
                "Male", "Female", "Other"
        });

        addFormField(form, "Student Name", nameField);
        addFormField(form, "Roll Number", rollField);
        addFormField(form, "Email Address", emailField);
        addFormField(form, "Course", courseBox);
        addFormField(form, "Division", divisionBox);
        addFormField(form, "Gender", genderBox);

        form.add(Box.createVerticalStrut(10));

        JButton registerButton = createButton(
                "Register Student", ACCENT, BG);

        JButton clearButton = createButton(
                "Clear Form", INPUT, TEXT);

        registerButton.addActionListener(e -> registerStudent());
        clearButton.addActionListener(e -> clearForm());

        form.add(registerButton);
        form.add(Box.createVerticalStrut(10));
        form.add(clearButton);

        statusLabel = new JLabel("Ready to register students");
        statusLabel.setForeground(MUTED);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(Box.createVerticalStrut(18));
        form.add(statusLabel);

        JScrollPane formScroll = new JScrollPane(form);
        formScroll.setBorder(new LineBorder(BORDER, 1, true));
        formScroll.setBackground(CARD);
        formScroll.getViewport().setBackground(CARD);
        formScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        formScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        formScroll.getVerticalScrollBar().setUnitIncrement(16);

        formScroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(51, 65, 85);
                this.trackColor = CARD;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                b.setMinimumSize(new Dimension(0, 0));
                b.setMaximumSize(new Dimension(0, 0));
                return b;
            }
        });

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(formScroll, BorderLayout.CENTER);

        return wrapper;
    }

    private void addFormField(JPanel panel, String label, JComponent field) {

        JLabel heading = new JLabel(label);
        heading.setFont(new Font("SansSerif", Font.BOLD, 12));
        heading.setForeground(TEXT);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        panel.add(heading);
        panel.add(Box.createVerticalStrut(7));
        panel.add(field);
        panel.add(Box.createVerticalStrut(15));
    }

    // ---------- Student Table ----------

    private JPanel createTablePanel() {

        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(CARD);
        panel.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(22, 22, 22, 22)));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Registered Students");
        title.setFont(new Font("SansSerif", Font.BOLD, 19));
        title.setForeground(TEXT);

        searchField = createTextField();
        searchField.setPreferredSize(new Dimension(200, 35));
        searchField.setToolTipText("Search students");

        header.add(title, BorderLayout.WEST);
        header.add(searchField, BorderLayout.EAST);

        String[] columns = {
                "Name", "Roll No.", "Email", "Course", "Division", "Gender"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(38);
        studentTable.setFont(new Font("SansSerif", Font.PLAIN, 12));
        studentTable.setForeground(TEXT);
        studentTable.setBackground(CARD);
        studentTable.setSelectionBackground(new Color(45, 65, 88));
        studentTable.setSelectionForeground(TEXT);
        studentTable.setGridColor(BORDER);
        studentTable.setShowVerticalLines(false);
        studentTable.setFillsViewportHeight(true);

        JTableHeader tableHeader = studentTable.getTableHeader();
        tableHeader.setBackground(INPUT);
        tableHeader.setForeground(ACCENT);
        tableHeader.setFont(new Font("SansSerif", Font.BOLD, 11));
        tableHeader.setPreferredSize(new Dimension(0, 40));
        tableHeader.setReorderingAllowed(false);

        // Header renderer: forces dark background and turquoise text under all Look & Feels
        tableHeader.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                label.setOpaque(true);
                label.setBackground(INPUT);
                label.setForeground(ACCENT);
                label.setFont(new Font("SansSerif", Font.BOLD, 11));
                label.setHorizontalAlignment(SwingConstants.LEFT);
                label.setBorder(new CompoundBorder(
                        new MatteBorder(0, 0, 1, 1, BORDER),
                        new EmptyBorder(8, 12, 8, 12)));
                return label;
            }
        });

        // Cell renderer: ensures dark background, clean padding, and readable text
        studentTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                label.setOpaque(true);
                label.setFont(new Font("SansSerif", Font.PLAIN, 12));
                label.setBorder(new EmptyBorder(0, 12, 0, 12));

                if (isSelected) {
                    label.setBackground(new Color(45, 65, 88));
                    label.setForeground(TEXT);
                } else {
                    label.setBackground(row % 2 == 0 ? CARD : new Color(26, 36, 52));
                    label.setForeground(TEXT);
                }
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(new LineBorder(BORDER, 1));
        scrollPane.getViewport().setBackground(CARD);

        JPanel tableCorner = new JPanel();
        tableCorner.setBackground(INPUT);
        scrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, tableCorner);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(51, 65, 85);
                this.trackColor = CARD;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                b.setMinimumSize(new Dimension(0, 0));
                b.setMaximumSize(new Dimension(0, 0));
                return b;
            }
        });

        // Search functionality
        searchField.getDocument().addDocumentListener(
                new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                filterStudents();
            }

            public void removeUpdate(DocumentEvent e) {
                filterStudents();
            }

            public void changedUpdate(DocumentEvent e) {
                filterStudents();
            }
        });

        JButton deleteButton = createButton(
                "Delete Selected Student",
                new Color(185, 28, 28),
                TEXT);

        deleteButton.addActionListener(e -> deleteStudent());

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);

        JLabel countLabel = new JLabel("Student records");
        countLabel.setForeground(MUTED);
        countLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        bottom.add(countLabel, BorderLayout.WEST);
        bottom.add(deleteButton, BorderLayout.EAST);

        panel.add(header, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    // ---------- Input Components ----------

    private JTextField createTextField() {

        JTextField field = new JTextField();

        field.setBackground(INPUT);
        field.setForeground(TEXT);
        field.setCaretColor(ACCENT);
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));

        field.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(8, 10, 8, 10)));

        return field;
    }

    private JComboBox<String> createComboBox(String[] options) {

        JComboBox<String> combo = new JComboBox<>(options);

        combo.setBackground(INPUT);
        combo.setForeground(TEXT);
        combo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        combo.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(2, 4, 2, 4)));

        // BasicComboBoxUI overrides system Look & Feel so popup and field match dark theme
        combo.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("▾");
                button.setFont(new Font("SansSerif", Font.BOLD, 12));
                button.setBackground(INPUT);
                button.setForeground(ACCENT);
                button.setBorder(new EmptyBorder(0, 4, 0, 8));
                button.setContentAreaFilled(false);
                button.setFocusPainted(false);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                return button;
            }

            @Override
            protected ComboPopup createPopup() {
                BasicComboPopup popup = new BasicComboPopup(comboBox) {
                    @Override
                    protected JScrollPane createScroller() {
                        JScrollPane scroller = super.createScroller();
                        scroller.setBorder(new LineBorder(BORDER, 1));
                        scroller.getViewport().setBackground(INPUT);
                        return scroller;
                    }
                };
                popup.setBorder(new LineBorder(BORDER, 1));
                if (popup.getList() != null) {
                    popup.getList().setBackground(INPUT);
                    popup.getList().setSelectionBackground(new Color(45, 65, 88));
                    popup.getList().setSelectionForeground(ACCENT);
                }
                return popup;
            }
        });

        // Custom renderer for selected value and dropdown popup items
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                label.setOpaque(true);
                label.setFont(new Font("SansSerif", Font.PLAIN, 12));
                label.setBorder(new EmptyBorder(7, 10, 7, 10));

                if (isSelected && index >= 0) {
                    label.setBackground(new Color(45, 65, 88));
                    label.setForeground(ACCENT);
                } else {
                    label.setBackground(INPUT);
                    label.setForeground(TEXT);
                }
                return label;
            }
        });

        return combo;
    }

    private JButton createButton(String text, Color background,
                                 Color foreground) {

        JButton button = new JButton(text);

        // BasicButtonUI ensures the background color is actively painted and not overridden by Windows L&F
        button.setUI(new BasicButtonUI());
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(new CompoundBorder(
                new LineBorder(background.equals(INPUT) ? BORDER : background, 1, true),
                new EmptyBorder(11, 15, 11, 15)));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(background.brighter());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(background);
            }
        });

        return button;
    }

    // ---------- Registration Logic ----------

    private void registerStudent() {

        String name = nameField.getText().trim();
        String roll = rollField.getText().trim();
        String email = emailField.getText().trim();

        String course = (String) courseBox.getSelectedItem();
        String division = (String) divisionBox.getSelectedItem();
        String gender = (String) genderBox.getSelectedItem();

        if (name.isEmpty() || roll.isEmpty() || email.isEmpty()) {

            showMessage("Please fill in all required fields.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!name.matches("[\\p{L} .'-]+")) {

            showMessage("Please enter a valid student name.",
                    "Invalid Name", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {

            showMessage("Please enter a valid email address.",
                    "Invalid Email", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (registeredRollNumbers.contains(roll.toLowerCase())) {

            showMessage("This roll number is already registered.",
                    "Duplicate Roll Number", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tableModel.addRow(new Object[]{
                name, roll, email, course, division, gender
        });

        registeredRollNumbers.add(roll.toLowerCase());

        updateStatistics();
        clearForm();

        statusLabel.setText("Student registered successfully!");

        showMessage("Student registered successfully!",
                "Registration Successful", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearForm() {

        nameField.setText("");
        rollField.setText("");
        emailField.setText("");

        courseBox.setSelectedIndex(0);
        divisionBox.setSelectedIndex(0);
        genderBox.setSelectedIndex(0);

        nameField.requestFocus();
    }

    private void deleteStudent() {

        int selectedRow = studentTable.getSelectedRow();

        if (selectedRow == -1) {

            showMessage("Please select a student to delete.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = studentTable.convertRowIndexToModel(selectedRow);

        String roll = tableModel.getValueAt(modelRow, 1).toString();

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this student?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {

            registeredRollNumbers.remove(roll.toLowerCase());
            tableModel.removeRow(modelRow);

            updateStatistics();

            statusLabel.setText("Student record deleted.");
        }
    }

    // ---------- Search ----------

    private void filterStudents() {

        TableRowSorter<DefaultTableModel> sorter =
                new TableRowSorter<>(tableModel);

        studentTable.setRowSorter(sorter);

        String search = searchField.getText().trim();

        if (search.isEmpty()) {

            sorter.setRowFilter(null);

        } else {

            sorter.setRowFilter(RowFilter.regexFilter(
                    "(?i)" + java.util.regex.Pattern.quote(search)));
        }
    }

    // ---------- Statistics ----------

    private void updateStatistics() {

        int total = tableModel == null ? 0 : tableModel.getRowCount();

        int a = 0;
        int b = 0;
        int c = 0;

        if (tableModel != null) {

            for (int i = 0; i < tableModel.getRowCount(); i++) {

                String division =
                        tableModel.getValueAt(i, 4).toString();

                switch (division) {

                    case "A":
                        a++;
                        break;

                    case "B":
                        b++;
                        break;

                    case "C":
                        c++;
                        break;
                }
            }
        }

        if (totalLabel != null) {
            totalLabel.setText(String.valueOf(total));
            divisionALabel.setText(String.valueOf(a));
            divisionBLabel.setText(String.valueOf(b));
            divisionCLabel.setText(String.valueOf(c));
        }
    }

    private void showMessage(String message, String title, int type) {

        JOptionPane.showMessageDialog(this, message, title, type);
    }

    // ---------- Main Method ----------

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            // Dark theme defaults for Swing controls
            UIManager.put("TableHeader.background", new Color(39, 52, 73));
            UIManager.put("TableHeader.foreground", new Color(45, 212, 191));
            UIManager.put("ComboBox.background", new Color(39, 52, 73));
            UIManager.put("ComboBox.foreground", new Color(241, 245, 249));
            UIManager.put("ComboBox.selectionBackground", new Color(45, 65, 88));
            UIManager.put("ComboBox.selectionForeground", new Color(45, 212, 191));

            new StudentRegistrationSystem().setVisible(true);
        });
    }
}
