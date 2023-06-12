package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import game.GameController;

public class Keyboard implements KeyListener {
	public boolean[] keyStates = new boolean[500];
	public static Keyboard keyboard = new Keyboard();
	
	private Keyboard() {
		for (int i = 0; i < keyStates.length; ++i) keyStates[i] = false;
	}
    
	public void keyTyped(KeyEvent e) {
		GameController.handleKeyType(e);
	}
    
	public void keyPressed(KeyEvent e) {
		keyStates[e.getKeyCode()] = true;
	}
    
	public void keyReleased(KeyEvent e) {
		keyStates[e.getKeyCode()] = false;
	}
}
