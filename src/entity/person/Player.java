package entity.person;

public class Player extends Person {
    
    public Player(int x, int y) {
        super(x, y, 0xFF000000);
    }

    public boolean steal() {
        return false;
    }
}
