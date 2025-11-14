package clueGame;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ToFromPanel extends JPanel {

	public ToFromPanel() {
		setLayout(new GridLayout(2, 2));
		
		JLabel fromLabel = new JLabel("From");
		JLabel toLabel = new JLabel("To");
		
		add(fromLabel);
		add(toLabel);
		
		JComboBox<String> fromCombo = new JComboBox<String>();
		fromCombo.addItem("Golden");
		fromCombo.addItem("Boulder");
		fromCombo.addItem("Denver");
		add(fromCombo);
		
		JComboBox<String> toCombo = new JComboBox<String>();
		toCombo.addItem("Golden");
		toCombo.addItem("Boulder");
		toCombo.addItem("Denver");
		add(toCombo);
		
		setBorder(new TitledBorder(new EtchedBorder(), "Location"));
	}
}
