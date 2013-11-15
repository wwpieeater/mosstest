package net.mosstest.launcher;

import java.awt.BorderLayout;
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
import java.awt.Dialog.ModalityType;
import javax.swing.DefaultComboBoxModel;
import com.jme3.material.RenderState.TestFunction;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GUIWorldCreationDialog extends JDialog {
	boolean dlgResult= false;
	private final JPanel contentPanel = new JPanel();
	JComboBox<String> comboBox;
	JTextField inputDesc;
	JTextField nameField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			GUIWorldCreationDialog dialog = new GUIWorldCreationDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public GUIWorldCreationDialog() {
		setTitle("Create new singleplayer world...");
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setBounds(100, 100, 450, 159);
		getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(this.contentPanel, BorderLayout.CENTER);
		this.contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		{
			JLabel lblWorldName = new JLabel("World name:");
			this.contentPanel.add(lblWorldName, "2, 2, right, default");
		}
		{
			this.nameField = new JTextField();
			this.contentPanel.add(this.nameField, "4, 2, fill, default");
			this.nameField.setColumns(10);
		}
		{
			JLabel lblDescription = new JLabel("Description:");
			this.contentPanel.add(lblDescription, "2, 4, right, default");
		}
		{
			this.inputDesc = new JTextField();
			this.contentPanel.add(this.inputDesc, "4, 4, fill, default");
			this.inputDesc.setColumns(10);
		}
		{
			JLabel lblGameProfile = new JLabel("Game profile:");
			this.contentPanel.add(lblGameProfile, "2, 6, right, default");
		}
		{
			this.comboBox = new JComboBox<String>();
			this.contentPanel.add(this.comboBox, "4, 6, fill, default");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						GUIWorldCreationDialog.this.dlgResult = true;
						GUIWorldCreationDialog.this.dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						GUIWorldCreationDialog.this.dlgResult = false;
						GUIWorldCreationDialog.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
