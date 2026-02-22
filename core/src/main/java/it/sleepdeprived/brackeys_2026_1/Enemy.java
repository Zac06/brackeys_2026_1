package it.sleepdeprived.brackeys_2026_1;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Enemy extends Entity {
    private boolean moving;
    private Sprite stillSprite;

    public Enemy(float x, float y, float width, float height, Array<Sprite> sprites, Sprite stillSprite) {
        super(x, y, width, height, sprites);

        stillSprite.setPosition(x, y);
        stillSprite.setSize(width, height);
        this.stillSprite = stillSprite;
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (moving) {
            super.draw(batch);      // draw current animation frame
        } else {
            stillSprite.draw(batch);
        }
    }

    @Override
    public void update(float delta){
        super.update(delta);

        stillSprite.setPosition(getX(), getY());
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }
}
