package net.mosstest.launcher;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import java.awt.Color;
import java.awt.Window.Type;
import javax.swing.JProgressBar;
import javax.swing.Box;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.BoxLayout;
import java.awt.Cursor;
import java.awt.Dimension;

public class GUIBugReportDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField reporterName;
	private JTextField email;
	private JTextField problemSummary;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			GUIBugReportDialog dialog = new GUIBugReportDialog("test traceback");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public GUIBugReportDialog(String traceback) {
		setMinimumSize(new Dimension(640, 480));
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		setTitle("Report a bug");
		setBounds(100, 100, 640, 480);
		getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(this.contentPanel, BorderLayout.CENTER);
		this.contentPanel.setLayout(new MigLayout("", "[][grow]", "[][][][grow][grow]"));
		{
			JLabel lblName = new JLabel("Name:");
			this.contentPanel.add(lblName, "cell 0 0,alignx trailing");
		}
		{
			this.reporterName = new JTextField();
			this.contentPanel.add(this.reporterName, "cell 1 0,growx");
			this.reporterName.setColumns(10);
		}
		{
			JLabel lblEmailoptional = new JLabel("e-mail (optional):");
			this.contentPanel.add(lblEmailoptional, "cell 0 1,alignx trailing");
		}
		{
			this.email = new JTextField();
			this.contentPanel.add(this.email, "cell 1 1,growx");
			this.email.setColumns(10);
		}
		{
			JLabel lblProblemDescription = new JLabel("Problem summary:");
			this.contentPanel.add(lblProblemDescription, "cell 0 2,alignx trailing");
		}
		{
			this.problemSummary = new JTextField();
			this.contentPanel.add(this.problemSummary, "cell 1 2,growx");
			this.problemSummary.setColumns(10);
		}
		{
			JLabel lblDetailedProblemDescription = new JLabel(
					"Detailed problem description:");
			this.contentPanel.add(lblDetailedProblemDescription, "cell 0 3,alignx trailing");
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			this.contentPanel.add(scrollPane, "cell 1 3,grow");
			{
				JTextArea txtLongDesc = new JTextArea();
				txtLongDesc.setWrapStyleWord(true);
				txtLongDesc.setLineWrap(true);
				txtLongDesc
						.setText("Please describe your problem here, preferably including what you had done, what you expected to happen, what happened, and any other details about the world or game.\r\n\r\nPlease note that we cannot help with third-party games or mods.");
				scrollPane.setViewportView(txtLongDesc);
			}
		}
		{
			JCheckBox chckbxIncludeTechnicalInformation = new JCheckBox(
					"Include technical information");
			this.contentPanel.add(chckbxIncludeTechnicalInformation, "cell 0 4,alignx trailing,aligny top");
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			this.contentPanel.add(scrollPane, "cell 1 4,grow");
			{
				JTextArea textArea = new JTextArea();
				textArea.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				textArea.setBackground(new Color(204, 204, 204));
				textArea.setEditable(false);
				textArea.setWrapStyleWord(true);
				textArea.setLineWrap(true);
				textArea.setText("The following information will be included in the bug report if this box is checked: \r\n\r\n"+traceback);
				scrollPane.setViewportView(textArea);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
			{
				JProgressBar progressBar = new JProgressBar();
				buttonPane.add(progressBar);
			}
			{
				JButton okButton = new JButton("Submit");
				buttonPane.add(okButton);
				okButton.setActionCommand("OK");
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.setActionCommand("Cancel");
			}
		}
	}

}
