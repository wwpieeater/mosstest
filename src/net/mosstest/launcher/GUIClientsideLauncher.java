package net.mosstest.launcher;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.AbstractTableModel;

import net.mosstest.servercore.MossWorld;

public class GUIClientsideLauncher {
	private SingleplayerListTableModel mdl;

	public static class SingleplayerListEntry {

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
					ArrayList<SingleplayerListEntry> entries = new ArrayList<SingleplayerListEntry>();
					entries.add(new SingleplayerListEntry("name1", "desc1",
							"game1"));
					entries.add(new SingleplayerListEntry(
							"name2",
							"desc2",
							"game2aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
					GUIClientsideLauncher window = new GUIClientsideLauncher(
							entries);
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
	public GUIClientsideLauncher(
			ArrayList<SingleplayerListEntry> singleplayerEntries) {
		initialize(singleplayerEntries);
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @param singleplayerEntries
	 */
	private void initialize(ArrayList<SingleplayerListEntry> singleplayerEntries) {
		this.frmMosstestClientLauncher = new JFrame();
		this.frmMosstestClientLauncher
				.setTitle("Mosstest Client launcher <0.0.1-initial>");
		this.frmMosstestClientLauncher.setBounds(100, 100, 800, 480);
		this.frmMosstestClientLauncher
				.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		this.frmMosstestClientLauncher.getContentPane().add(tabbedPane,
				BorderLayout.CENTER);

		JPanel singleplayerTab = new JPanel();
		tabbedPane.addTab("Singleplayer", null, singleplayerTab, null);
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
		singleplayerTab.add(singleplayerControlBtns, BorderLayout.SOUTH);
		singleplayerControlBtns.setLayout(new GridLayout(0, 4, 0, 0));

		JButton btnPlaySingleplayer = new JButton("Play");
		btnPlaySingleplayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				int row = GUIClientsideLauncher.this.table.getSelectedRow();
				if (row < 0) {
					JOptionPane
							.showMessageDialog(
									null,
									"No world was selected to be started. Please select an existing world in the table, or create a new world.",
									"No world selected",
									JOptionPane.WARNING_MESSAGE);
					return;
				}
				GUIClientsideLauncher.this.frmMosstestClientLauncher
						.setVisible(false);
				// below is testing code. in reality this would call a method to
				// start a world and block. This should be in a try-catch block
				// for the bug reporter to snatch up.
				try {
					throw new Exception();
					//MossWorld w=new MossWorld((String)GUIClientsideLauncher.this.table.getModel().getValueAt(row, 0), -16511);
				}
				catch(Exception e) {
					StringBuilder s=new StringBuilder("Exception uncaught in code\r\n");
					
					GUIBugReportDialog bg=new GUIBugReportDialog(MossDebugUtils.getDebugInformation(e));
					bg.setVisible(true);
				}
				GUIClientsideLauncher.this.frmMosstestClientLauncher
						.setVisible(true);
			}
		});
		singleplayerControlBtns.add(btnPlaySingleplayer);

		JButton btnNewSingleplayer = new JButton("New...");
		btnNewSingleplayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GUIWorldCreationDialog dlg = new GUIWorldCreationDialog();
				dlg.setVisible(true);
				if (dlg.dlgResult) {
					System.out.println("got value: " + dlg.nameField.getText());
					System.out.println("got desc: " + dlg.inputDesc.getText());
					System.out.println("got game: "
							+ dlg.comboBox.getSelectedIndex());
				} else
					System.out.println("cxld");
			}
		});

		singleplayerControlBtns.add(btnNewSingleplayer);

		JButton btnSettingsSingleplayer = new JButton("Settings...");
		singleplayerControlBtns.add(btnSettingsSingleplayer);

		JButton btnDelete = new JButton("Delete...");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = GUIClientsideLauncher.this.table.getSelectedRow();
				if (row < 0) {
					JOptionPane
							.showMessageDialog(
									null,
									"No world was selected to be deleted. Please select an existing world in the table.",
									"No world selected",
									JOptionPane.WARNING_MESSAGE);
					return;
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
		tabbedPane.addTab("About", null, aboutTab, null);
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

		JButton btnReportBug = new JButton("Report a bug...");
		btnReportBug.setEnabled(false);
		communityToolsButtonPanel.add(btnReportBug);

		JButton btnRequestNewFeature = new JButton("Request new feature...");
		btnRequestNewFeature.setEnabled(false);
		communityToolsButtonPanel.add(btnRequestNewFeature);

		JButton btnVisitWebsite = new JButton("Visit website");
		btnVisitWebsite.setEnabled(false);
		communityToolsButtonPanel.add(btnVisitWebsite);

		JButton btnGithubProject = new JButton("GitHub project");
		btnGithubProject.setEnabled(false);
		communityToolsButtonPanel.add(btnGithubProject);

		JButton btnVisitForums = new JButton("Visit forums");
		btnVisitForums.setEnabled(false);
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
