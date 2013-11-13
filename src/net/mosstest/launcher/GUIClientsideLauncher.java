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

	private JFrame frame;
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
					GUIClientsideLauncher window = new GUIClientsideLauncher();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUIClientsideLauncher() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.frame = new JFrame();
		this.frame.setBounds(100, 100, 800, 480);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		this.frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

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
		this.table.setModel(new SingleplayerListTableModel());
		this.table.getColumnModel().getColumn(0).setPreferredWidth(90);
		this.table.getColumnModel().getColumn(1).setPreferredWidth(256);
		this.table.getColumnModel().getColumn(2).setPreferredWidth(104);
		this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		singleplayerTab.add(this.table, BorderLayout.CENTER);
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
			// Note that the data/cell address is constant,
			// no matter where the cell appears onscreen.
			if (col < 2) {
				return false;
			} else {
				return true;
			}
		}

	}
}
