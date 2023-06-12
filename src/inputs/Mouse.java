package inputs;

import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import game.GameController;

public class Mouse implements MouseListener {
	public static int x, y;
	public static Mouse mouse = new Mouse();
	
	public Mouse() {
		x = 0;
		y = 0;
	}
	
	public void update(int screenX, int screenY) {
		x = MouseInfo.getPointerInfo().getLocation().x - screenX;
		y = MouseInfo.getPointerInfo().getLocation().y - screenY;
	}

	public void mouseClicked(MouseEvent e) {
        if (e.getButton() ==1) GameController.handleMouseClick();
    }

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}
}
