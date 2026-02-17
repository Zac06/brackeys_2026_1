package it.sleepdeprived.brackeys_2026_1;

import com.badlogic.gdx.graphics.Texture;

public class Enemy {
    private int x;
    private int y;
    private Texture sprite;
    public Enemy(int x, int y, Texture sprite){
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }
    public Texture getSprite() {
        return sprite;
    }
    public void setSprite(Texture sprite) {
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
