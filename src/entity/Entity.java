package entity;

public class Entity {
    protected int x, y, w, h;

    public Entity(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public int getX() {
        return x;
    }

     public int getY() {
        return y;
     }

     public int getWidth() {
        return w;
     }

     public int getHeight() {
        return h;
     }
}
