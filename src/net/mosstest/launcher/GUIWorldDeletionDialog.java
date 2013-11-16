package net.mosstest.launcher;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class GUIWorldDeletionDialog extends JDialog {
	boolean dlgResult=false;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			GUIWorldDeletionDialog dialog = new GUIWorldDeletionDialog("test from main");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public GUIWorldDeletionDialog(String worldName) {
		setTitle("Delete singleplayer world...");
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setBounds(100, 100, 600, 105);
		getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(this.contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel);
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			
			{
				JLabel lblAreYouSure = new JLabel("Are you sure you wish to delete the world called "+worldName+"? This operation cannot be undone.");
				panel.add(lblAreYouSure);
				lblAreYouSure.setHorizontalTextPosition(SwingConstants.LEFT);
				lblAreYouSure.setHorizontalAlignment(SwingConstants.LEFT);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnYes = new JButton("Yes");
				btnYes.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						GUIWorldDeletionDialog.this.dlgResult = true;
						GUIWorldDeletionDialog.this.dispose();
					}
				});
				btnYes.setActionCommand("Yes");
				buttonPane.add(btnYes);
				getRootPane().setDefaultButton(btnYes);
			}
			{
				JButton cancelButton = new JButton("No");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						GUIWorldDeletionDialog.this.dlgResult = false;
						GUIWorldDeletionDialog.this.dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
	}

}
