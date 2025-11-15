package clueGame;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class CardControlPanel extends JPanel {

	
	public CardControlPanel() {
		setLayout(new GridLayout(1, 1));
		
		JPanel largeBorder = new JPanel();
		largeBorder.setLayout(new GridLayout(3, 1));
		largeBorder.setBorder(new TitledBorder(new EtchedBorder(), "Known Cards", TitledBorder.CENTER, TitledBorder.TOP));
		
		JPanel peoplePanel =  new JPanel();
		peoplePanel.setBorder(new TitledBorder(new EtchedBorder(), "People"));
		largeBorder.add(peoplePanel);
		
		
		JPanel roomsPanel =  new JPanel();
		roomsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
		largeBorder.add(roomsPanel);
		
		
		
		JPanel weaponPanel =  new JPanel();
		weaponPanel.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));
		largeBorder.add(weaponPanel);
		
		
		add(largeBorder);
		
	}
	
	
	
	
	public static void main(String[] args) {
		CardControlPanel panel = new CardControlPanel();
		JFrame frame = new JFrame();  // create the frame 
		frame.setContentPane(panel); // put the panel in the frame
		frame.setSize(200, 750);  // size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true); // make it visible
		
		
		
	}
	
}
