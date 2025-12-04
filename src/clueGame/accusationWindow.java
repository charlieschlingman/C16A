package clueGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class accusationWindow extends JDialog {

    private JComboBox<String> roomBox;
    private JComboBox<String> playerBox;
    private JComboBox<String> weaponBox;
    private List<Card> rooms;
    private List<Card> players;
    private List<Card> weapons;    
    public Card roomGuess;
    public Card playerGuess;
    public Card weaponGuess;
    public boolean submittedGuess;
    public boolean isOpen = false;



    public accusationWindow(List<Card> rooms, List<Card> players, List<Card> weapons) {

    	// Set default sizing, variables, and layout
        super();
        isOpen = true;
        this.rooms = rooms;
        this.players =  players;
        this.weapons = weapons;
        setTitle("Make an Accusation");
        setLayout(new BorderLayout());
        setSize(350, 180);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel gridPanel = new JPanel(new GridLayout(3, 2));

        // Add the room combo box and label
        gridPanel.add(new JLabel("Room"));
        roomBox = new JComboBox<>();
        for (Card room: rooms) {
        	String roomName = room.getCardName();
        	roomBox.addItem(roomName);
        }
        gridPanel.add(roomBox);

        // Add the Player combo box and label
        gridPanel.add(new JLabel("Player"));
        playerBox = new JComboBox<>();
        for (Card player: players) {
        	String playerName = player.getCardName();
        	playerBox.addItem(playerName);
        }
        gridPanel.add(playerBox);

        // Add the weapon combo box and label
        gridPanel.add(new JLabel("Weapon"));
        weaponBox = new JComboBox<>();
        for (Card weapon: weapons) {
        	String weaponName = weapon.getCardName();
        	weaponBox.addItem(weaponName);
        }
        gridPanel.add(weaponBox);

        add(gridPanel, BorderLayout.CENTER);

        
        // Add the buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));

        JButton submit = new JButton("Submit");
        JButton cancel = new JButton("Cancel");

        buttonPanel.add(submit);
        buttonPanel.add(cancel);

        add(buttonPanel, BorderLayout.SOUTH);


        submit.addActionListener(new submitListener());

        cancel.addActionListener(new cancelListener());
        
        
    }
    

 	private class submitListener implements ActionListener {
 		@Override
 		public void actionPerformed(ActionEvent e) {
 	        
 	        // Get the room from the room name and set it to roomGuess
 	       for (Card room: rooms) {
	        	if (room.getCardName() == roomBox.getSelectedItem()) {
	        		roomGuess = room;
	        	}
	        }
 	       
 	   // Get the player from the player name and set it to playerGuess
 	      for (Card player: players) {
	        	if (player.getCardName() == playerBox.getSelectedItem()) {
	        		playerGuess = player;
	        	}
	        }
 	  // Get the weapon from the weapon name and set it to weaponGuess
 	     for (Card weapon: weapons) {
	        	if (weapon.getCardName() == weaponBox.getSelectedItem()) {
	        		weaponGuess = weapon;
	        	}
	        }
 	     
 	    System.out.println("Room: " + roomGuess.getCardName());
	    System.out.println("Player: " + playerGuess.getCardName());
	    System.out.println("Weapon: " + weaponGuess.getCardName());
	    
	    submittedGuess = true;
 	    dispose();
 		}
 	}
 	
 	// if the cancel button is hit, exit the window
 	private class cancelListener implements ActionListener {
 		@Override
 		public void actionPerformed(ActionEvent e) {
 			submittedGuess = false;
 	        dispose();
 		}
 	}
 	
    public String getRoom() {
    	String room = (String) roomBox.getSelectedItem();
    	return room;
    }
    
    public String getPlayer() {
    	String player = (String) playerBox.getSelectedItem();
    	return player;
    }
    
    public String getWeapon() {
    	String room = (String) weaponBox.getSelectedItem();
    	return room;
    }
    
    // Make it so that whenever dispose is called, it also sets isOpen to fals
    @Override
    public void dispose() {
    	isOpen = false;
    	super.dispose();
    }
}

