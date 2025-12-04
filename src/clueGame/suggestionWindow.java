package clueGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class suggestionWindow extends JDialog {
	
	
	private JComboBox<String> roomBox;
    private JComboBox<String> playerBox;
    private JComboBox<String> weaponBox;
    public Card roomSuggestion;
    public Card playerSuggestion;
    public Card weaponSuggestion;
    public Board theBoard;
    public boolean isOpen = false;
	
	public suggestionWindow(Player player, Board theBoard) {

		// Set the default variables, sizing, and format
        super();
        isOpen = true;
        this.theBoard = theBoard;
        setTitle("Make an Suggestion");
        setLayout(new BorderLayout());
        setSize(350, 180);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel gridPanel = new JPanel(new GridLayout(3, 2));

        
        // Add the room section, only setting the right side to the current room and setting that to the roomSuggestion
        gridPanel.add(new JLabel("Room"));
        
        BoardCell currentCell = theBoard.getCell(player.getRow(), player.getCol());
        gridPanel.add(new JLabel(theBoard.getRoom(currentCell).getName()));
        
        for (Card room: theBoard.getRoomCards()) {
        	if (room.getCardName() == theBoard.getRoom(currentCell).getName()) {
        		roomSuggestion = room;
        	}
        }
        

        // Same logic as accusationWindow logic for player
        gridPanel.add(new JLabel("Player"));
        playerBox = new JComboBox<>();
        for (Card person: theBoard.getPersonCards()) {
        	String personName = person.getCardName();
        	playerBox.addItem(personName);
        }
        gridPanel.add(playerBox);

        // Same logic as accusationWindow logic for weapon
        gridPanel.add(new JLabel("Weapon"));
        weaponBox = new JComboBox<>();
        for (Card weapon: theBoard.getWeaponCards()) {
        	String weaponName = weapon.getCardName();
        	weaponBox.addItem(weaponName);
        }
        gridPanel.add(weaponBox);

        add(gridPanel, BorderLayout.CENTER);


        // Same button logic as accusation Window
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));

        JButton submit = new JButton("Submit");
        JButton cancel = new JButton("Cancel");

        buttonPanel.add(submit);
        buttonPanel.add(cancel);

        add(buttonPanel, BorderLayout.SOUTH);


        submit.addActionListener(new submitListener());

        cancel.addActionListener(new cancelListener());
        
        
    }
    

	// Once again, same logic as accusationWindow, just without room update as that was done in the constructor
 	private class submitListener implements ActionListener {
 		@Override
 		public void actionPerformed(ActionEvent e) {
 	        

 	       
 	      for (Card player: theBoard.getPersonCards()) {
	        	if (player.getCardName() == playerBox.getSelectedItem()) {
	        		playerSuggestion = player;
	        	}
	        }
 	      
 	     for (Card weapon: theBoard.getWeaponCards()) {
	        	if (weapon.getCardName() == weaponBox.getSelectedItem()) {
	        		weaponSuggestion = weapon;
	        	}
	        }
 	     
 	    System.out.println("Room: " + roomSuggestion.getCardName());
	    System.out.println("Player: " + playerSuggestion.getCardName());
	    System.out.println("Weapon: " + weaponSuggestion.getCardName());
	    
 	    dispose();
 		}
 	}
 	
 	// Same logic for rest of the functions as accusationWindow
 	private class cancelListener implements ActionListener {
 		@Override
 		public void actionPerformed(ActionEvent e) {
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
    
    @Override
    public void dispose() {
    	isOpen = false;
    	super.dispose();
    }
}
