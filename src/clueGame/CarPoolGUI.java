package clueGame;


import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

public class CarPoolGUI extends JFrame {
	
	public CarPoolGUI() {
		setSize(new Dimension(400, 250));
		setTitle("Let's carpool");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ToFromPanel tfPanel = new ToFromPanel();
		add(tfPanel, BorderLayout.CENTER);
		
		PreferencePanel pPanel = new PreferencePanel();
		add(pPanel, BorderLayout.WEST);
	}

	public static void main(String[] args) {
		CarPoolGUI gui = new CarPoolGUI();
		gui.setVisible(true);
	}

}

