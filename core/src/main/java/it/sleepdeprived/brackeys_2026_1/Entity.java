package it.sleepdeprived.brackeys_2026_1;

import com.badlogic.gdx.graphics.Texture;
import java.util.Vector;

public class Entity {
    private int x;
    private int y;
    private Vector<Texture> sprite;
    public Entity(int x, int y, Vector<Texture> sprite){
        this.x = x;
        this.y = y;
        this.sprite = new Vector<>();
    }
    public Texture getSprite(int pos) {
        return sprite.get(pos);
    }
    public Vector<Texture> getSprite(){ return sprite;}
    public void setSprite(Vector<Texture> sprite) {
        this.sprite = sprite;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
}

