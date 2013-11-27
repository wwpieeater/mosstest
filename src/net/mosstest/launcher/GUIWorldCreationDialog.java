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

import java.awt.Dialog.ModalityType;

import javax.swing.DefaultComboBoxModel;

import com.jme3.material.RenderState.TestFunction;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.Dimension;

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
		setTitle(Messages.getString("GUIWorldCreationDialog.DLG_TITLE")); //$NON-NLS-1$
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setBounds(100, 100, 450, 142);
		getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(this.contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(3, 1, 0, 0));
		{
			JPanel namePanel = new JPanel();
			namePanel.setMinimumSize(new Dimension(10, 20));
			namePanel.setMaximumSize(new Dimension(32767, 20));
			contentPanel.add(namePanel);
			namePanel.setLayout(new BorderLayout(0, 0));
			
			{
				JLabel lblWorldName = new JLabel(Messages.getString("GUIWorldCreationDialog.WORLD_NAME")); //$NON-NLS-1$
				namePanel.add(lblWorldName, BorderLayout.WEST);
			}
			{
				this.nameField = new JTextField();
				nameField.setMaximumSize(new Dimension(2147483647, 20));
				namePanel.add(nameField, BorderLayout.CENTER);
				this.nameField.setColumns(10);
			}
		}
		{
			JPanel descPnl = new JPanel();
			descPnl.setMinimumSize(new Dimension(10, 20));
			descPnl.setMaximumSize(new Dimension(32767, 20));
			contentPanel.add(descPnl);
			descPnl.setLayout(new BorderLayout(0, 0));
			{
				JLabel lblDescription = new JLabel(Messages.getString("GUIWorldCreationDialog.WORLD_DESC")); //$NON-NLS-1$
				descPnl.add(lblDescription, BorderLayout.WEST);
			}
			{
				this.inputDesc = new JTextField();
				descPnl.add(inputDesc, BorderLayout.CENTER);
				this.inputDesc.setColumns(10);
			}
		}
		{
			JPanel profPnl = new JPanel();
			profPnl.setMinimumSize(new Dimension(10, 20));
			profPnl.setMaximumSize(new Dimension(32767, 20));
			contentPanel.add(profPnl);
			profPnl.setLayout(new BorderLayout(0, 0));
			{
				JLabel lblGameProfile = new JLabel(Messages.getString("GUIWorldCreationDialog.GAME_PROFILE")); //$NON-NLS-1$
				profPnl.add(lblGameProfile, BorderLayout.WEST);
			}
			{
				this.comboBox = new JComboBox<String>();
				comboBox.setMaximumSize(new Dimension(32767, 23));
				profPnl.add(comboBox, BorderLayout.CENTER);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setMaximumSize(new Dimension(32767, 23));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton(Messages.getString("GUIWorldCreationDialog.OK")); //$NON-NLS-1$
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						GUIWorldCreationDialog.this.dlgResult = true;
						GUIWorldCreationDialog.this.dispose();
					}
				});
				okButton.setActionCommand(Messages.getString("GUIWorldCreationDialog.OK")); //$NON-NLS-1$
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton(Messages.getString("GUIWorldCreationDialog.CXL")); //$NON-NLS-1$
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						GUIWorldCreationDialog.this.dlgResult = false;
						GUIWorldCreationDialog.this.dispose();
					}
				});
				cancelButton.setActionCommand(Messages.getString("GUIWorldCreationDialog.CXL")); //$NON-NLS-1$
				buttonPane.add(cancelButton);
			}
		}
	}

}
