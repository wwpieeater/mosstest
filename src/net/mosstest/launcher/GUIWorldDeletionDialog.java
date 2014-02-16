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

// TODO: Auto-generated Javadoc
/**
 * The Class GUIWorldDeletionDialog.
 */
public class GUIWorldDeletionDialog extends JDialog {
	
	/** The dlg result. */
	boolean dlgResult=false;
	
	/** The content panel. */
	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			GUIWorldDeletionDialog dialog = new GUIWorldDeletionDialog("test from main"); //$NON-NLS-1$
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 *
	 * @param worldName the world name
	 */
	public GUIWorldDeletionDialog(String worldName) {
		setTitle(Messages.getString("GUIWorldDeletionDialog.DLG_TITLE")); //$NON-NLS-1$
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
				JLabel lblAreYouSure = new JLabel(Messages.getString("GUIWorldDeletionDialog.AREYOUSURE_TEXT")+worldName+Messages.getString("GUIWorldDeletionDialog.QUESTIONMARK_CANNOT_UNDO")); //$NON-NLS-1$ //$NON-NLS-2$
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
				JButton btnYes = new JButton(Messages.getString("GUIWorldDeletionDialog.YES")); //$NON-NLS-1$
				btnYes.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						GUIWorldDeletionDialog.this.dlgResult = true;
						GUIWorldDeletionDialog.this.dispose();
					}
				});
				btnYes.setActionCommand(Messages.getString("GUIWorldDeletionDialog.YES")); //$NON-NLS-1$
				buttonPane.add(btnYes);
				getRootPane().setDefaultButton(btnYes);
			}
			{
				JButton cancelButton = new JButton(Messages.getString("GUIWorldDeletionDialog.NO")); //$NON-NLS-1$
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
