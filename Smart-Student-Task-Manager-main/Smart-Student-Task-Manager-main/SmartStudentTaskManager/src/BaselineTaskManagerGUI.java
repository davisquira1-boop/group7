import baseline.Task;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class BaselineTaskManagerGUI {

    private final BaselineTaskController controller = new BaselineTaskController(4096);
    private JFrame frame;
    private JTextArea outputArea;

    // table model for master schedule
    private DefaultTableModel masterModel;
    private JTable masterTable;
    private DefaultListModel<String> unlockedListModel;

    public void createAndShowGUI() {
        frame = new JFrame("Smart Student Task Manager — Baseline");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);

        // Toolbar
        JToolBar toolbar = new JToolBar();
        JButton sampleBtn = new JButton("Populate Sample");
        JButton refreshBtn = new JButton("Refresh");
        JButton clearBtn = new JButton("Clear Output");
        JButton exitBtn = new JButton("Exit");
        toolbar.add(sampleBtn);
        toolbar.add(refreshBtn);
        toolbar.addSeparator();
        toolbar.add(clearBtn);
        toolbar.add(exitBtn);

        // Left controls panel
        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controls.setBorder(BorderFactory.createTitledBorder("Controls"));

        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField idField = new JTextField(4);
        JTextField nameField = new JTextField(12);
        JTextField prField = new JTextField(3);
        JTextField dlField = new JTextField(3);
        JButton addBtn = new JButton("Add Task");
        addPanel.add(new JLabel("ID")); addPanel.add(idField);
        addPanel.add(new JLabel("Name")); addPanel.add(nameField);
        addPanel.add(new JLabel("Pr")); addPanel.add(prField);
        addPanel.add(new JLabel("DL")); addPanel.add(dlField);
        addPanel.add(addBtn);
        controls.add(addPanel);

        JPanel depPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField depFrom = new JTextField(3);
        JTextField depTo = new JTextField(3);
        JButton addDepBtn = new JButton("Add Dep");
        depPanel.add(new JLabel("From")); depPanel.add(depFrom);
        depPanel.add(new JLabel("To")); depPanel.add(depTo);
        depPanel.add(addDepBtn);
        controls.add(depPanel);

        JPanel donePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField doneField = new JTextField(4);
        JButton doneBtn = new JButton("Mark Done");
        donePanel.add(new JLabel("Done ID:")); donePanel.add(doneField); donePanel.add(doneBtn);
        controls.add(donePanel);

        JButton unlockedBtn = new JButton("Show Unlocked");
        JButton masterBtn = new JButton("Show Master Schedule");
        JButton allBtn = new JButton("Show All Tasks");
        controls.add(unlockedBtn);
        controls.add(masterBtn);
        controls.add(allBtn);

        // Center: master schedule table
        String[] cols = {"ID", "Name", "Priority", "Deadline", "Done"};
        masterModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        masterTable = new JTable(masterModel);
        masterTable.setFillsViewportHeight(true);
        masterTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        masterTable.setRowHeight(22);
        // renderer to gray out done tasks
        masterTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                Object v = table.getValueAt(row, 4);
                boolean done = v != null && Boolean.valueOf(String.valueOf(v));
                c.setForeground(done ? Color.GRAY : Color.BLACK);
                return c;
            }
        });

        JScrollPane tableScroll = new JScrollPane(masterTable);

        // Right: unlocked list and output
        JPanel right = new JPanel(new BorderLayout());
        right.setBorder(BorderFactory.createTitledBorder("Unlocked / Details"));
        unlockedListModel = new DefaultListModel<>();
        JList<String> unlockedList = new JList<>(unlockedListModel);
        unlockedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane unlockedScroll = new JScrollPane(unlockedList);

        outputArea = new JTextArea(); outputArea.setEditable(false); outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane outScroll = new JScrollPane(outputArea);

        JSplitPane rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, unlockedScroll, outScroll);
        rightSplit.setResizeWeight(0.4);
        right.add(rightSplit, BorderLayout.CENTER);

        // Main split pane
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, controls, tableScroll);
        mainSplit.setResizeWeight(0.25);
        JSplitPane outerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mainSplit, right);
        outerSplit.setResizeWeight(0.7);

        frame.getContentPane().add(toolbar, BorderLayout.NORTH);
        frame.getContentPane().add(outerSplit, BorderLayout.CENTER);

        // Action wiring
        sampleBtn.addActionListener(e -> { populateSample(); refreshAll(); appendText("Sample data populated."); });
        refreshBtn.addActionListener(e -> { refreshAll(); appendText("Refreshed."); });
        clearBtn.addActionListener(e -> outputArea.setText(""));
        exitBtn.addActionListener(e -> System.exit(0));

        addBtn.addActionListener(e -> {
            try { int id = Integer.parseInt(idField.getText().trim());
                  String name = nameField.getText().trim();
                  int p = Integer.parseInt(prField.getText().trim());
                  int d = Integer.parseInt(dlField.getText().trim());
                  appendText(controller.addTask(id, name, p, d));
                  refreshAll();
            } catch (Exception ex) { appendText("Invalid add input."); }
        });

        addDepBtn.addActionListener(e -> {
            try { int f = Integer.parseInt(depFrom.getText().trim()); int t = Integer.parseInt(depTo.getText().trim());
                  appendText(controller.addDependency(f,t)); refreshAll();
            } catch (Exception ex) { appendText("Invalid dependency input."); }
        });

        doneBtn.addActionListener(e -> {
            try { int id = Integer.parseInt(doneField.getText().trim()); appendText(controller.markDone(id)); refreshAll(); }
            catch (Exception ex) { appendText("Invalid done id."); }
        });

        unlockedBtn.addActionListener(e -> {
            unlockedListModel.clear();
            for (Task t : controller.getUnlockedTasks()) unlockedListModel.addElement(t.getId()+": "+t.getName()+" (Pr:"+t.getPriority()+" DL:"+t.getDeadline()+")");
            appendText("Unlocked tasks listed.");
        });

        masterBtn.addActionListener(e -> { refreshMasterTable(); appendText("Master schedule updated."); });
        allBtn.addActionListener(e -> appendText(controller.showAll()));

        // double-click unlocked list to mark done
        unlockedList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    String sel = unlockedList.getSelectedValue();
                    if (sel != null) {
                        int id = Integer.parseInt(sel.split(":")[0]);
                        appendText(controller.markDone(id)); refreshAll();
                    }
                }
            }
        });

        // table context: right-click to mark done
        masterTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                    int row = masterTable.rowAtPoint(evt.getPoint());
                    if (row >= 0) {
                        int id = Integer.parseInt(String.valueOf(masterTable.getValueAt(row,0)));
                        appendText(controller.markDone(id)); refreshAll();
                    }
                }
            }
        });

        // show window
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        refreshAll();
    }

    private void refreshAll() { refreshMasterTable(); unlockedListModel.clear(); }

    private void refreshMasterTable() {
        SwingUtilities.invokeLater(() -> {
            masterModel.setRowCount(0);
            for (Task t : controller.getMasterSchedule()) {
                masterModel.addRow(new Object[]{t.getId(), t.getName(), t.getPriority(), t.getDeadline(), t.isDone()});
            }
        });
    }

    private void appendText(String s) { SwingUtilities.invokeLater(() -> { outputArea.append(s+"\n"); outputArea.setCaretPosition(outputArea.getDocument().getLength()); }); }

    private void populateSample() {
        controller.addTask(1, "Do Math Homework", 1, 2);
        controller.addTask(2, "Read Chapter 4", 3, 5);
        controller.addTask(3, "Write Essay Draft", 2, 7);
        controller.addTask(4, "Submit Final Essay", 1, 10);
        controller.addDependency(3,4);
        controller.addDependency(2,3);
    }

    private String captureSystemOut(Runnable task) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        try { System.setOut(ps); task.run(); ps.flush(); return baos.toString(); }
        finally { System.setOut(originalOut); }
    }
}
