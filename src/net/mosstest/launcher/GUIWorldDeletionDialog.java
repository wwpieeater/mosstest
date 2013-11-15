package net.mosstest.launcher;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
		this.contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		{
			JLabel lblAreYouSure = new JLabel("Are you sure you wish to delete the world called "+worldName+"? This operation cannot be undone.");
			this.contentPanel.add(lblAreYouSure, "2, 2, right, default");
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
