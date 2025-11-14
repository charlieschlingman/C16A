package clueGame;

import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class PreferencePanel extends JPanel {

	public PreferencePanel() {
		setLayout(new GridLayout(2, 1));
		setBorder(new TitledBorder(new EtchedBorder(), "Preferences"));
		
		JRadioButton musicButton = new JRadioButton("Music");
		JRadioButton noMusicButton = new JRadioButton("No Music");
		add(musicButton);
		add(noMusicButton);
		
		ButtonGroup group = new ButtonGroup();
		group.add(musicButton);
		group.add(noMusicButton);
		noMusicButton.setSelected(true);
	}
}
