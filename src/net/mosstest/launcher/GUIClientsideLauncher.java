package net.mosstest.launcher;

import net.mosstest.servercore.*;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NonNls;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc

/**
 * The Class GUIClientsideLauncher.
 */
public class GUIClientsideLauncher {

    /**
     * The logger.
     */
    static Logger logger = Logger.getLogger(GUIClientsideLauncher.class);

    /**
     * The mdl.
     */
    private SingleplayerListTableModel mdl;

    static {
        System.setSecurityManager(MosstestSecurityManager.instance);
    }

    /**
     * The Class SingleplayerListEntry.
     */
    public static class SingleplayerListEntry {

        /**
         * The name.
         */
        public String name;

        /**
         * The description.
         */
        public String description;

        /**
         * The game preset.
         */
        public String gamePreset;

        /**
         * Instantiates a new singleplayer list entry.
         *
         * @param name        the name
         * @param description the description
         * @param gamePreset  the game preset
         */
        public SingleplayerListEntry(String name, String description,
                                     String gamePreset) {
            this.name = name;
            this.description = description;
            this.gamePreset = gamePreset;
        }

    }

    /**
     * The frm mosstest client launcher.
     */
    private JDialog frmMosstestClientLauncher;

    /**
     * The table.
     */
    private JTable table;

    /**
     * Launch the application.
     *
     * @param args the arguments
     * @throws InvocationTargetException the invocation target exception
     * @throws InterruptedException      the interrupted exception
     */
    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        logger.info(Messages.getString("MOSS_CLIENT_STARTING"));
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e1) {
            logger.warn(Messages
                    .getString("GUIClientsideLauncher.WARN_SET_LAF")); //$NON-NLS-1$
        }

        EventQueue.invokeAndWait(new Runnable() {
            public void run() {
                    ArrayList<SingleplayerListEntry> entries = new ArrayList<>();

                GUIClientsideLauncher window = new GUIClientsideLauncher(
                        entries);
                    window.frmMosstestClientLauncher.setVisible(true);

            }
        });
    }

    /**
     * Create the application.
     *
     * @param singleplayerEntries the singleplayer entries
     */
    public GUIClientsideLauncher(
            ArrayList<SingleplayerListEntry> singleplayerEntries) {
        initialize(singleplayerEntries);
    }

    /**
     * Initialize the contents of the frame.
     *
     * @param singleplayerEntries the singleplayer entries
     */
    @SuppressWarnings("StringConcatenation")
    private void initialize(ArrayList<SingleplayerListEntry> singleplayerEntries) {
        this.frmMosstestClientLauncher = new JDialog();
        this.frmMosstestClientLauncher.setModal(true);
        this.frmMosstestClientLauncher.setModalityType(ModalityType.APPLICATION_MODAL);
        this.frmMosstestClientLauncher.setTitle(Messages
                .getString("GUIClientsideLauncher.DLG_TITLE")); //$NON-NLS-1$
        this.frmMosstestClientLauncher.setBounds(100, 100, 800, 480);
        this.frmMosstestClientLauncher
                .setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        this.frmMosstestClientLauncher.getContentPane().add(tabbedPane,
                BorderLayout.CENTER);

        JPanel singleplayerTab = new JPanel();
        tabbedPane
                .addTab(Messages
                        .getString("GUIClientsideLauncher.DLG_SINGLEPLAYER"), null, singleplayerTab, null); //$NON-NLS-1$
        singleplayerTab.setLayout(new BorderLayout(0, 0));
        this.table = new JTable();
        this.table.setFillsViewportHeight(true);
        this.mdl = new SingleplayerListTableModel(singleplayerEntries);
        this.table.setModel(this.mdl);
        this.table.getColumnModel().getColumn(0).setPreferredWidth(90);
        this.table.getColumnModel().getColumn(1).setPreferredWidth(256);
        this.table.getColumnModel().getColumn(2).setPreferredWidth(104);
        this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JPanel singleplayerControlBtns = new JPanel();
        singleplayerTab.add(singleplayerControlBtns, BorderLayout.PAGE_END);
        singleplayerControlBtns.setLayout(new GridLayout(0, 4, 0, 0));

        JButton btnPlaySingleplayer = new JButton(
                Messages.getString("GUIClientsideLauncher.DLG_PLAY")); //$NON-NLS-1$
        btnPlaySingleplayer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                int row = GUIClientsideLauncher.this.table.getSelectedRow();
                if (row < 0) {

                    logger.warn(Messages.getString("GAMEPLAY_NO_WORLD"));
                    JOptionPane.showMessageDialog(
                            null,
                            Messages.getString("GUIClientsideLauncher.ERR_NO_WORLD_SELECTED"), //$NON-NLS-1$
                            Messages.getString("GUIClientsideLauncher.ERR_NO_WORLD_SELECTED_TITLE"), //$NON-NLS-1$
                            JOptionPane.WARNING_MESSAGE);

                    return;
                }
                GUIClientsideLauncher.this.frmMosstestClientLauncher
                        .setVisible(false);
                // below is testing code. in reality this would call a method to
                // start a world and block. This should be in a try-catch block
                // for the bug reporter to snatch up.
                try {

                    MossWorld w = new MossWorld((String) GUIClientsideLauncher.this.table.getModel().getValueAt(row, 0), -16511);
                } catch (MossWorldLoadException | MapDatabaseException | IOException e) {
                    logger.error(Messages.getString("GAME_UNCAUGHT_EXCEPT"));

                    String fname = MossDebugUtils.writeStracktrace(e);
                    logger.error(MessageFormat.format(Messages.getString("STACKTRACE_WRITTEN"), fname));
                    GUIBugReportDialog bg = new GUIBugReportDialog(
                            MossDebugUtils.getDebugInformation(e));
                    bg.setVisible(true);
                }
                GUIClientsideLauncher.this.frmMosstestClientLauncher
                        .setVisible(true);
            }
        });
        singleplayerControlBtns.add(btnPlaySingleplayer);

        JButton btnNewSingleplayer = new JButton(
                Messages.getString("GUIClientsideLauncher.DLG_NEW")); //$NON-NLS-1$
        btnNewSingleplayer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUIWorldCreationDialog dlg = new GUIWorldCreationDialog();
                dlg.setVisible(true);
                if (dlg.dlgResult) {
                    // TODO actually create world

                } else
                    logger.info(Messages.getString("WORLD_CREATION_CXLD"));
            }
        });

        singleplayerControlBtns.add(btnNewSingleplayer);

        JButton btnSettingsSingleplayer = new JButton(
                Messages.getString("GUIClientsideLauncher.DLG_SETTINGS")); //$NON-NLS-1$
        singleplayerControlBtns.add(btnSettingsSingleplayer);

        JButton btnDelete = new JButton(
                Messages.getString("GUIClientsideLauncher.DLG_DELETE")); //$NON-NLS-1$
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = GUIClientsideLauncher.this.table.getSelectedRow();
                if (row < 0) {

                    logger.warn(Messages.getString("NO_WORLD_TO_DELETE"));
                    JOptionPane.showMessageDialog(
                            null,
                            Messages.getString("GUIClientsideLauncher.DLG_NO_WORLD_TO_DELETE"), //$NON-NLS-1$
                            Messages.getString("GUIClientsideLauncher.ERR_NO_WORLD_SELECTED_TITLE"), //$NON-NLS-1$
                            JOptionPane.WARNING_MESSAGE);

                }

            }
        });
        singleplayerControlBtns.add(btnDelete);

        // singleplayerTab.add(this.table, BorderLayout.CENTER);

        JScrollPane singleplayerScrollPane = new JScrollPane(this.table);
        singleplayerScrollPane
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        singleplayerTab.add(singleplayerScrollPane, BorderLayout.CENTER);
        JPanel aboutTab = new JPanel();
        tabbedPane
                .addTab(Messages.getString("GUIClientsideLauncher.DLG_ABOUT"), null, aboutTab, null); //$NON-NLS-1$
        tabbedPane.setEnabledAt(1, true);
        SpringLayout sl_aboutTab = new SpringLayout();
        aboutTab.setLayout(sl_aboutTab);

        JPanel communityToolsButtonPanel = new JPanel();
        sl_aboutTab.putConstraint(SpringLayout.NORTH,
                communityToolsButtonPanel, 405, SpringLayout.NORTH, aboutTab);
        sl_aboutTab.putConstraint(SpringLayout.WEST, communityToolsButtonPanel,
                0, SpringLayout.WEST, aboutTab);
        sl_aboutTab.putConstraint(SpringLayout.EAST, communityToolsButtonPanel,
                787, SpringLayout.WEST, aboutTab);
        aboutTab.add(communityToolsButtonPanel);
        communityToolsButtonPanel.setLayout(new GridLayout(0, 5, 0, 0));

        JButton btnReportBug = new JButton(
                Messages.getString("GUIClientsideLauncher.23")); //$NON-NLS-1$
        btnReportBug.setEnabled(false);
        communityToolsButtonPanel.add(btnReportBug);

        JButton btnRequestNewFeature = new JButton(
                Messages.getString("GUIClientsideLauncher.24")); //$NON-NLS-1$
        btnRequestNewFeature.setEnabled(false);
        communityToolsButtonPanel.add(btnRequestNewFeature);

        JButton btnVisitWebsite = new JButton(
                Messages.getString("GUIClientsideLauncher.25")); //$NON-NLS-1$
        btnVisitWebsite.setEnabled(false);
        communityToolsButtonPanel.add(btnVisitWebsite);

        JButton btnGithubProject = new JButton(
                Messages.getString("GUIClientsideLauncher.26")); //$NON-NLS-1$
        btnGithubProject.setEnabled(false);
        communityToolsButtonPanel.add(btnGithubProject);

        JButton btnVisitForums = new JButton(
                Messages.getString("GUIClientsideLauncher.27")); //$NON-NLS-1$
        btnVisitForums.setEnabled(false);
        communityToolsButtonPanel.add(btnVisitForums);

        @NonNls JTextArea textArea = new JTextArea();
        aboutTab.add(textArea);
        textArea.setText("  __  __  ____   _____ _____ _______ ______  _____ _______ \r\n"
                + " |  \\/  |/ __ \\ / ____/ ____|__   __|  ____|/ ____|__   __|\r\n"
                + " | \\  / | |  | | (___| (___    | |  | |__  | (___    | |   \r\n"
                + " | |\\/| | |  | |\\___ \\\\___ \\   | |  |  __|  \\___ \\   | |   \r\n"
                + " | |  | | |__| |____) |___) |  | |  | |____ ____) |  | |   \r\n"
                + " |_|  |_|\\____/|_____/_____/   |_|  |______|_____/   |_|   \r\n"
                + "                                                           \r\n"
                + "  0.0.1-initial                                              \r\n"
                + ""
                + Messages
                .getString("GUIClientsideLauncher.NOTICE_PRIMARY_AUTHORS") //$NON-NLS-1$
                + Messages
                .getString("GUIClientsideLauncher.NOTICE_DEFAULT_GAME_CODE") //$NON-NLS-1$
                + Messages.getString("GUIClientsideLauncher.SYS_NEWLINE") //$NON-NLS-1$
                + Messages.getString("GUIClientsideLauncher.USES_LIBS") //$NON-NLS-1$
                + "* Apache Commons Lang\r\n"
                + "* Apache Commons Collections\r\n"
                + "* JInput\r\n"
                + "* jmonkeyengine/jme3\r\n"
                + "* niftygui\r\n"
                + "* Google Guava Collections\r\n"
                + "* Apache Commons CLI\r\n"
                + "* Apache Commons Configuration\r\n"
                + "* Mozilla Rhino\r\n"
                + "* leveldbjni\r\n"
                + "* junit4 for testing\r\n"
                + Messages.getString("GUIClientsideLauncher.SYS_NEWLINE") //$NON-NLS-1$
                + Messages.getString("GUIClientsideLauncher.NOTICE_TOOLS_USED")); //$NON-NLS-1$
        textArea.setWrapStyleWord(true);
        textArea.setBackground(new Color(192, 192, 192));
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        sl_aboutTab.putConstraint(SpringLayout.NORTH, scrollPane, 0,
                SpringLayout.NORTH, aboutTab);
        sl_aboutTab.putConstraint(SpringLayout.WEST, scrollPane, 0,
                SpringLayout.WEST, aboutTab);
        sl_aboutTab.putConstraint(SpringLayout.SOUTH, scrollPane, 0,
                SpringLayout.NORTH, communityToolsButtonPanel);
        sl_aboutTab.putConstraint(SpringLayout.EAST, scrollPane, 787,
                SpringLayout.WEST, aboutTab);
        scrollPane
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane
                .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        aboutTab.add(scrollPane);
        frmMosstestClientLauncher.setVisible(true);
    }

    /**
     * The Class SingleplayerListTableModel.
     */
    class SingleplayerListTableModel extends AbstractTableModel {

        /**
         * The column names.
         */
        private String[] columnNames = {
                Messages.getString("GUIClientsideLauncher.COL_WORLD_NAME"), Messages.getString("GUIClientsideLauncher.COL_WORLD_DESC"), //$NON-NLS-1$ //$NON-NLS-2$
                Messages.getString("GUIClientsideLauncher.COL_GAME_PRESET")}; //$NON-NLS-1$

        /**
         * The entries.
         */
        private ArrayList<SingleplayerListEntry> entries = new ArrayList<>();

        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#getColumnCount()
         */
        public int getColumnCount() {
            return this.columnNames.length;
        }

        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#getRowCount()
         */
        public int getRowCount() {
            return this.entries.size();
        }

        /* (non-Javadoc)
         * @see javax.swing.table.AbstractTableModel#getColumnName(int)
         */
        public String getColumnName(int col) {
            return this.columnNames[col];
        }

        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#getValueAt(int, int)
         */
        public Object getValueAt(int row, int col) {
            SingleplayerListEntry entry = this.entries.get(row);
            switch (col) {
                case 0:
                    return entry.name;
                case 1:
                    return entry.description;
                case 2:
                    return entry.gamePreset;
                default:
                    return null;
            }
        }

        /*
         * All entries are strings *at the moment*.
         */
        /* (non-Javadoc)
         * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
		 */
        public Class<?> getColumnClass(int c) {
            return String.class;
        }

        /*
         * Don't need to implement this method unless your table's editable.
         */
		/* (non-Javadoc)
		 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
		 */
        public boolean isCellEditable(int row, int col) {
            return false;
        }

        /**
         * Instantiates a new singleplayer list table model.
         *
         * @param entries the entries
         */
        public SingleplayerListTableModel(
                ArrayList<SingleplayerListEntry> entries) {
            this.entries = entries;
        }

    }
}
