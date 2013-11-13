package net.mosstest.launcher;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

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
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("163px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("441px"),},
			new RowSpec[] {
				FormFactory.UNRELATED_GAP_ROWSPEC,
				RowSpec.decode("20px"),
				FormFactory.NARROW_LINE_GAP_ROWSPEC,
				RowSpec.decode("20px"),
				FormFactory.NARROW_LINE_GAP_ROWSPEC,
				RowSpec.decode("20px"),
				FormFactory.NARROW_LINE_GAP_ROWSPEC,
				RowSpec.decode("192px"),
				FormFactory.NARROW_LINE_GAP_ROWSPEC,
				RowSpec.decode("138px"),}));
		{
			JLabel lblName = new JLabel("Name:");
			this.contentPanel.add(lblName, "2, 2, right, center");
		}
		{
			this.reporterName = new JTextField();
			this.contentPanel.add(this.reporterName, "4, 2, fill, top");
			this.reporterName.setColumns(10);
		}
		{
			JLabel lblEmailoptional = new JLabel("e-mail (optional):");
			this.contentPanel.add(lblEmailoptional, "2, 4, right, center");
		}
		{
			this.email = new JTextField();
			this.contentPanel.add(this.email, "4, 4, fill, top");
			this.email.setColumns(10);
		}
		{
			JLabel lblProblemDescription = new JLabel("Problem summary:");
			this.contentPanel.add(lblProblemDescription, "2, 6, right, center");
		}
		{
			this.problemSummary = new JTextField();
			this.contentPanel.add(this.problemSummary, "4, 6, fill, top");
			this.problemSummary.setColumns(10);
		}
		{
			JLabel lblDetailedProblemDescription = new JLabel(
					"Detailed problem description:");
			this.contentPanel.add(lblDetailedProblemDescription, "2, 8, right, center");
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			this.contentPanel.add(scrollPane, "4, 8, fill, fill");
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
			this.contentPanel.add(chckbxIncludeTechnicalInformation, "2, 10, left, top");
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			this.contentPanel.add(scrollPane, "4, 10, fill, fill");
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
