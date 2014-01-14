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

import java.awt.Component;

import javax.swing.SpringLayout;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridLayout;

import javax.swing.SwingConstants;

import java.awt.Point;
import java.awt.Dialog.ModalityType;

public class GUIBugReportDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField reporterName;
	private JTextField email;
	private JTextField problemSummary;
	private JCheckBox chckbxIncludeTechnicalInformation;
	private JLabel lblName;

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
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setMinimumSize(new Dimension(640, 480));
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		setTitle(Messages.getString("GUIBugReportDialog.DLG_BUG_TITLE")); //$NON-NLS-1$
		setBounds(100, 100, 640, 480);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(this.contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		{
			JPanel namePanel = new JPanel();
			namePanel.setMaximumSize(new Dimension(32767, 20));
			contentPanel.add(namePanel);
			namePanel.setLayout(new BorderLayout(0, 0));
			
			{
				lblName = new JLabel(Messages.getString("GUIBugReportDialog.DLG_BUG_NAME")); //$NON-NLS-1$
				namePanel.add(lblName, BorderLayout.WEST);
				lblName.setHorizontalTextPosition(SwingConstants.LEFT);
				lblName.setHorizontalAlignment(SwingConstants.LEFT);
				lblName.setAlignmentY(Component.TOP_ALIGNMENT);
			}
			lblName.setLabelFor(reporterName);
			{
				this.reporterName = new JTextField();
				namePanel.add(reporterName, BorderLayout.CENTER);
				reporterName.setMaximumSize(new Dimension(2147483647, 20));
				reporterName.setAlignmentY(Component.TOP_ALIGNMENT);
				this.reporterName.setColumns(10);
			}
		}
		{
			JPanel emailPanel = new JPanel();
			emailPanel.setMaximumSize(new Dimension(32767, 20));
			contentPanel.add(emailPanel);
			emailPanel.setLayout(new BorderLayout(0, 0));
			{
				JLabel lblEmailoptional = new JLabel(Messages.getString("GUIBugReportDialog.DLG_BUG_EMAIL")); //$NON-NLS-1$
				emailPanel.add(lblEmailoptional, BorderLayout.WEST);
			}
			{
				this.email = new JTextField();
				emailPanel.add(email, BorderLayout.CENTER);
				email.setMaximumSize(new Dimension(2147483647, 20));
				this.email.setColumns(10);
			}
		}
		{
			JPanel summaryPnl = new JPanel();
			summaryPnl.setMaximumSize(new Dimension(32767, 20));
			contentPanel.add(summaryPnl);
			summaryPnl.setLayout(new BorderLayout(0, 0));
			{
				JLabel lblProblemDescription = new JLabel(Messages.getString("GUIBugReportDialog.DLG_BUG_SUMMARY")); //$NON-NLS-1$
				summaryPnl.add(lblProblemDescription, BorderLayout.WEST);
			}
			{
				this.problemSummary = new JTextField();
				summaryPnl.add(problemSummary, BorderLayout.CENTER);
				this.problemSummary.setColumns(10);
			}
		}
		{
			JPanel descPanel = new JPanel();
			descPanel.setMinimumSize(new Dimension(80, 150));
			contentPanel.add(descPanel);
			descPanel.setLayout(new BorderLayout(0, 0));
			{
				JLabel lblDetailedProblemDescription = new JLabel(
						Messages.getString("GUIBugReportDialog.DLG_BUG_DESC")); //$NON-NLS-1$
				lblDetailedProblemDescription.setVerticalAlignment(SwingConstants.TOP);
				descPanel.add(lblDetailedProblemDescription, BorderLayout.WEST);
			}
			{
				JScrollPane scrollPane = new JScrollPane();
				descPanel.add(scrollPane, BorderLayout.CENTER);
				{
					JTextArea txtLongDesc = new JTextArea();
					scrollPane.setViewportView(txtLongDesc);
					txtLongDesc.setWrapStyleWord(true);
					txtLongDesc.setLineWrap(true);
					txtLongDesc
							.setText(Messages.getString("GUIBugReportDialog.DLG_BUG_DESC_DEFAULT")); //$NON-NLS-1$
				}
			}
		}
		{
			JPanel techInfoPanel = new JPanel();
			contentPanel.add(techInfoPanel);
			techInfoPanel.setLayout(new BorderLayout(0, 0));
			{
				chckbxIncludeTechnicalInformation = new JCheckBox(
						Messages.getString("GUIBugReportDialog.DLG_CHECKBOX_INCLUDE_TECH_INFO")); //$NON-NLS-1$
				techInfoPanel.add(chckbxIncludeTechnicalInformation, BorderLayout.WEST);
				chckbxIncludeTechnicalInformation.setLocation(new Point(2, 0));
				chckbxIncludeTechnicalInformation.setVerticalAlignment(SwingConstants.TOP);
				chckbxIncludeTechnicalInformation.setHorizontalTextPosition(SwingConstants.LEFT);
				chckbxIncludeTechnicalInformation.setAlignmentX(Component.CENTER_ALIGNMENT);
				chckbxIncludeTechnicalInformation.setHorizontalAlignment(SwingConstants.LEFT);
			}
			{
				JScrollPane scrollPane = new JScrollPane();
				techInfoPanel.add(scrollPane, BorderLayout.CENTER);
				{
					JTextArea textArea = new JTextArea();
					scrollPane.setViewportView(textArea);
					textArea.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					textArea.setBackground(new Color(204, 204, 204));
					textArea.setEditable(false);
					textArea.setWrapStyleWord(true);
					textArea.setLineWrap(true);
					textArea.setText(Messages.getString("GUIBugReportDialog.NOTICE_INFO_INCLUDED")+traceback); //$NON-NLS-1$
				}
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
				JButton okButton = new JButton(Messages.getString("GUIBugReportDialog.DLG_SUBMIT")); //$NON-NLS-1$
				buttonPane.add(okButton);
				okButton.setActionCommand(Messages.getString("GUIBugReportDialog.DLG_OK")); //$NON-NLS-1$
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton(Messages.getString("GUIBugReportDialog.DLG_CXL")); //$NON-NLS-1$
				buttonPane.add(cancelButton);
				cancelButton.setActionCommand(Messages.getString("GUIBugReportDialog.DLG_CXL")); //$NON-NLS-1$
			}
		}
	}

}
