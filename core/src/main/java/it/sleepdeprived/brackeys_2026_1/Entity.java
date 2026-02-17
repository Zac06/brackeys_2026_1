package it.sleepdeprived.brackeys_2026_1;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;



public class Entity {
    private float x;
    private float y;
    private Array<Texture> sprite;
    private Rectangle rec;
    public Entity(float x, float y, Array<Texture> sprite, Rectangle rec){
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        this.rec = rec;
    }
    public Texture getSprite(int pos) {
        return sprite.get(pos);
    }
    public Array<Texture> getSprite(){ return sprite;}
    public void setSprite(Array<Texture> sprite) {
        this.sprite = sprite;
    }
    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
        rec.setPosition(this.x,this.y);
    }
    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
        rec.setPosition(this.x,this.y);
    }

    public Rectangle getRec() {
        return rec;
    }

    public void setRec(Rectangle rec) {
        this.rec = rec;
    }
}

