package net.mosstest.launcher;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.AbstractTableModel;
import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.SpringLayout;
import net.miginfocom.swing.MigLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.CardLayout;
import javax.swing.BoxLayout;

public class GUIClientsideLauncher {

	public class SingleplayerListEntry {

		public String name;
		public String description;
		public String gamePreset;

		public SingleplayerListEntry(String name, String description,
				String gamePreset) {
			this.name = name;
			this.description = description;
			this.gamePreset = gamePreset;
		}

	}

	private JFrame frmMosstestClientLauncher;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
			System.out
					.println("[ERROR] [NONFATAL] [GUI] System Look-And-Feel could not be set; falling back to default L&F.");
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIClientsideLauncher window = new GUIClientsideLauncher(new ArrayList<SingleplayerListEntry>());
					window.frmMosstestClientLauncher.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUIClientsideLauncher(ArrayList<SingleplayerListEntry> singleplayerEntries) {
		initialize(singleplayerEntries);
	}

	/**
	 * Initialize the contents of the frame.
	 * @param singleplayerEntries 
	 */
	private void initialize(ArrayList<SingleplayerListEntry> singleplayerEntries) {
		this.frmMosstestClientLauncher = new JFrame();
		frmMosstestClientLauncher.setTitle("Mosstest Client launcher <0.0.1-initial>");
		this.frmMosstestClientLauncher.setBounds(100, 100, 800, 480);
		this.frmMosstestClientLauncher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		this.frmMosstestClientLauncher.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel singleplayerTab = new JPanel();
		tabbedPane.addTab("Singleplayer", null, singleplayerTab, null);
		singleplayerTab.setLayout(new BorderLayout(0, 0));

		JPanel singleplayerControlBtns = new JPanel();
		singleplayerTab.add(singleplayerControlBtns, BorderLayout.SOUTH);
		singleplayerControlBtns.setLayout(new GridLayout(0, 4, 0, 0));

		JButton btnPlaySingleplayer = new JButton("Play");
		singleplayerControlBtns.add(btnPlaySingleplayer);

		JButton btnNewSingleplayer = new JButton("New...");

		singleplayerControlBtns.add(btnNewSingleplayer);

		JButton btnSettingsSingleplayer = new JButton("Settings...");
		singleplayerControlBtns.add(btnSettingsSingleplayer);

		JButton btnDelete = new JButton("Delete...");
		singleplayerControlBtns.add(btnDelete);

		this.table = new JTable();
		this.table.setFillsViewportHeight(true);
		this.table.setModel(new SingleplayerListTableModel(singleplayerEntries));
		this.table.getColumnModel().getColumn(0).setPreferredWidth(90);
		this.table.getColumnModel().getColumn(1).setPreferredWidth(256);
		this.table.getColumnModel().getColumn(2).setPreferredWidth(104);
		this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//singleplayerTab.add(this.table, BorderLayout.CENTER);

		JScrollPane singleplayerScrollPane = new JScrollPane(this.table);
		singleplayerScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		singleplayerTab.add(singleplayerScrollPane, BorderLayout.CENTER);
		JPanel aboutTab = new JPanel();
		tabbedPane.addTab("About", null, aboutTab, null);
		tabbedPane.setEnabledAt(1, true);
		SpringLayout sl_aboutTab = new SpringLayout();
		aboutTab.setLayout(sl_aboutTab);
		
		JPanel communityToolsButtonPanel = new JPanel();
		sl_aboutTab.putConstraint(SpringLayout.NORTH, communityToolsButtonPanel, 405, SpringLayout.NORTH, aboutTab);
		sl_aboutTab.putConstraint(SpringLayout.WEST, communityToolsButtonPanel, 0, SpringLayout.WEST, aboutTab);
		sl_aboutTab.putConstraint(SpringLayout.EAST, communityToolsButtonPanel, 787, SpringLayout.WEST, aboutTab);
		aboutTab.add(communityToolsButtonPanel);
		communityToolsButtonPanel.setLayout(new GridLayout(0, 5, 0, 0));
		
		JButton btnReportBug = new JButton("Report a bug...");
		communityToolsButtonPanel.add(btnReportBug);
		
		JButton btnRequestNewFeature = new JButton("Request new feature...");
		communityToolsButtonPanel.add(btnRequestNewFeature);
		
		JButton btnVisitWebsite = new JButton("Visit website");
		communityToolsButtonPanel.add(btnVisitWebsite);
		
		JButton btnGithubProject = new JButton("GitHub project");
		communityToolsButtonPanel.add(btnGithubProject);
		
		JButton btnVisitForums = new JButton("Visit forums");
		communityToolsButtonPanel.add(btnVisitForums);
		
		
		
		JTextArea textArea = new JTextArea();
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
				+ "Made by hexafraction, thatnerd2, et al.\r\n"
				+ "Default game code and textures created by dolinksy296, hexafraction, et. al.\r\n"
				+ "\r\n"
				+ "Uses the following libraries:\r\n"
				+ "* Apache Commons Lang\r\n"
				+ "* Apache Commons Collections\r\n"
				+ "* JInput\r\n"
				+ "* jmonkeyengine/jme3\r\n"
				+ "* niftygui\r\n"
				+ "* Google Guava Collections\r\n"
				+ "* Apache Commons CLI (currently unused)\r\n"
				+ "* Apache Commons Configuration\r\n"
				+ "* Mozilla Rhino\r\n"
				+ "* leveldbjni\r\n"
				+ "* junit4 for testing\r\n"
				+ "\r\n"
				+ "Made with the help of Eclipse, Git, Maven, and countless services such as TravisCI and GitHub.\r\n");
		textArea.setWrapStyleWord(true);
		textArea.setBackground(new Color(192, 192, 192));
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		sl_aboutTab.putConstraint(SpringLayout.NORTH, scrollPane, 0, SpringLayout.NORTH, aboutTab);
		sl_aboutTab.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, aboutTab);
		sl_aboutTab.putConstraint(SpringLayout.SOUTH, scrollPane, 0, SpringLayout.NORTH, communityToolsButtonPanel);
		sl_aboutTab.putConstraint(SpringLayout.EAST, scrollPane, 787, SpringLayout.WEST, aboutTab);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		aboutTab.add(scrollPane);
		

	}

	class SingleplayerListTableModel extends AbstractTableModel {
		private String[] columnNames = { "World name", "Description",
				"Game preset" };
		private ArrayList<SingleplayerListEntry> entries = new ArrayList<>();

		public int getColumnCount() {
			return this.columnNames.length;
		}

		public int getRowCount() {
			return this.entries.size();
		}

		public String getColumnName(int col) {
			return this.columnNames[col];
		}

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
		public Class getColumnClass(int c) {
			return String.class;
		}

		/*
		 * Don't need to implement this method unless your table's editable.
		 */
		public boolean isCellEditable(int row, int col) {
			return false;
		}

		/**
		 * @param entries
		 */
		public SingleplayerListTableModel(
				ArrayList<SingleplayerListEntry> entries) {
			this.entries = entries;
		}

	}
}
