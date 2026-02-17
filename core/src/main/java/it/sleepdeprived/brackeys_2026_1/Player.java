package it.sleepdeprived.brackeys_2026_1;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;


import java.util.Vector;

public class Player extends Entity{
    public Player(float x, float y, Array<Texture> sprite, Rectangle rec){
        super(x,y,sprite,rec);
    }
}
