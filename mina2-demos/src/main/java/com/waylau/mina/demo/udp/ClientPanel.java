package com.waylau.mina.demo.udp;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ClientPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JTextField textField;

	public ClientPanel(String label) {
		super();

		setPreferredSize(MemoryMonitor.PANEL_SIZE);

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.CENTER;

		c.gridwidth = GridBagConstraints.REMAINDER;
		add(new JLabel(label), c);

		c.gridwidth = 1;
		add(new JLabel("Memory Used : "));
		textField = new JTextField(10);
		textField.setEditable(false);
		add(textField, c);
	}

	public void updateTextField(final long val) {
		System.out.println("New value for textfield - " + val);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				textField.setText(String.valueOf(val));
			}
		});
	}
}
