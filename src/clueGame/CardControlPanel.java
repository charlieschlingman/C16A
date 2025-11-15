package clueGame;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class CardControlPanel extends JPanel {

	private JPanel peopleHandPanel, peopleSeenPanel;
	private JPanel roomsHandPanel, roomsSeenPanel;
	private JPanel weaponsHandPanel, weaponsSeenPanel;


	public CardControlPanel() {

		setLayout(new BorderLayout());

		// Main Panel
		JPanel borderPanel = new JPanel();
		borderPanel.setLayout(new BoxLayout(borderPanel, BoxLayout.Y_AXIS));
		borderPanel.setBorder(new TitledBorder(new EtchedBorder(), "Known Cards", TitledBorder.CENTER, TitledBorder.TOP));

		// People
		JPanel peoplePanel = createCategoryPanel("People");
		peopleHandPanel = new JPanel(new GridLayout(0, 1));
		peopleSeenPanel = new JPanel(new GridLayout(0, 1));
		fillCategoryPanel(peoplePanel, peopleHandPanel, peopleSeenPanel);
		borderPanel.add(peoplePanel);

		// Rooms
		JPanel roomsPanel = createCategoryPanel("Rooms");
		roomsHandPanel = new JPanel(new GridLayout(0, 1));
		roomsSeenPanel = new JPanel(new GridLayout(0, 1));
		fillCategoryPanel(roomsPanel, roomsHandPanel, roomsSeenPanel);
		borderPanel.add(roomsPanel);

		// Weapons
		JPanel weaponsPanel = createCategoryPanel("Weapons");
		weaponsHandPanel = new JPanel(new GridLayout(0, 1));
		weaponsSeenPanel = new JPanel(new GridLayout(0, 1));
		fillCategoryPanel(weaponsPanel, weaponsHandPanel, weaponsSeenPanel);
		borderPanel.add(weaponsPanel);

		add(borderPanel, BorderLayout.NORTH);

		loadTestData();
	}

	// Create panels for each category
	private JPanel createCategoryPanel(String title) {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setBorder(new TitledBorder(new EtchedBorder(), title));
		p.setAlignmentX(LEFT_ALIGNMENT);
		return p;
	}

	// Fill categories with in-hand and seen fields
	private void fillCategoryPanel(JPanel outer, JPanel hand, JPanel seen) {
		JLabel inHandLabel = new JLabel("In Hand:");
		inHandLabel.setAlignmentX(LEFT_ALIGNMENT);
		outer.add(inHandLabel);

		hand.setAlignmentX(LEFT_ALIGNMENT);
		outer.add(hand);

		JLabel seenLabel = new JLabel("Seen:");
		seenLabel.setAlignmentX(LEFT_ALIGNMENT);
		outer.add(seenLabel);

		seen.setAlignmentX(LEFT_ALIGNMENT);
		outer.add(seen);

		// Bottom spacing
		outer.add(Box.createRigidArea(new Dimension(0, 8))); 
	}

	// Add a card to the panel
	private void addCard(JPanel panel, String text, Color color) {
		JTextField field = new JTextField(text);
		field.setEditable(false);
		field.setBackground(color);
		field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
		field.setPreferredSize(new Dimension(180, 20));
		panel.add(field);
	}


	// Test Data
	private void loadTestData() {
		// People – Hand
		addCard(peopleHandPanel, "Colonel Mustard", Color.WHITE);

		// People – Seen
		addCard(peopleSeenPanel, "Mrs. White", Color.YELLOW);
		addCard(peopleSeenPanel, "Miss Scarlett", Color.WHITE);
		addCard(peopleSeenPanel, "Mrs. Peacock", Color.GREEN);
		addCard(peopleSeenPanel, "Reverend Green", new Color(185, 235, 255));

		// Rooms – Hand
		// nothing added

		// Rooms – Seen
		addCard(roomsSeenPanel, "Hall", Color.YELLOW);
		addCard(roomsSeenPanel, "Ballroom", Color.WHITE);
		addCard(roomsSeenPanel, "Kitchen", Color.WHITE);
		addCard(roomsSeenPanel, "Billiard Room", Color.GREEN);
		addCard(roomsSeenPanel, "Conservatory", new Color(185, 235, 255));
		addCard(roomsSeenPanel, "Lounge", new Color(185, 235, 255));
		addCard(roomsSeenPanel, "Library", Color.WHITE);
		addCard(roomsSeenPanel, "Dining Room", new Color(230, 190, 255));

		// Weapons – Hand
		addCard(weaponsHandPanel, "Wrench", Color.WHITE);
		addCard(weaponsHandPanel, "Rope", Color.WHITE);

		// Weapons – Seen
		addCard(weaponsSeenPanel, "Lead Pipe", Color.YELLOW);
		addCard(weaponsSeenPanel, "Dagger", new Color(230, 190, 255));
		addCard(weaponsSeenPanel, "Revolver", new Color(185, 235, 255));
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
