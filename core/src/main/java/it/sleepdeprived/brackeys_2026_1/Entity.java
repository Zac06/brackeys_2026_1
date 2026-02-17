package it.sleepdeprived.brackeys_2026_1;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Entity {
    private Array<Sprite> sprites;
    private int currentFrame;
    private Rectangle hitbox;

    private float frameTimer = 0f;
    private float frameDuration = 0.1f;

    public Entity(float x, float y, float width, float height, Array<Sprite> sprites) {
        this.sprites = sprites;
        this.currentFrame = 0;
        this.hitbox = new Rectangle(x, y, width, height);

        for (Sprite s : sprites) {
            s.setPosition(x, y);
            s.setSize(width, height);
        }
    }

    /**
     * Advances the animation frame based on elapsed time. Call once per frame before draw().
     */
    public void update(float delta) {
        if (sprites.size <= 1) return;
        frameTimer += delta;
        if (frameTimer >= frameDuration) {
            frameTimer -= frameDuration;
            currentFrame = (currentFrame + 1) % sprites.size;
        }
    }

    public void draw(SpriteBatch batch) {
        sprites.get(currentFrame).draw(batch);
    }

    public void draw(SpriteBatch batch, int frame) {
        sprites.get(frame).draw(batch);
    }

    public float getX() {
        return hitbox.x;
    }

    public void setX(float x) {
        hitbox.x = x;
        for (Sprite s : sprites) s.setX(x);
    }

    public float getY() {
        return hitbox.y;
    }

    public void setY(float y) {
        hitbox.y = y;
        for (Sprite s : sprites) s.setY(y);
    }

    public void setPosition(float x, float y) {
        hitbox.setPosition(x, y);
        for (Sprite s : sprites) s.setPosition(x, y);
    }

    public float getWidth() {
        return hitbox.width;
    }

    public void setWidth(float width) {
        hitbox.width = width;
        for (Sprite s : sprites) s.setSize(width, s.getHeight());
    }

    public float getHeight() {
        return hitbox.height;
    }

    public void setHeight(float height) {
        hitbox.height = height;
        for (Sprite s : sprites) s.setSize(s.getWidth(), height);
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int frame) {
        if (frame >= 0 && frame < sprites.size) currentFrame = frame;
    }

    public float getFrameDuration() {
        return frameDuration;
    }

    public void setFrameDuration(float frameDuration) {
        this.frameDuration = frameDuration;
    }

    public void resetFrameTimer() {
        frameTimer = 0f;
    }

    public Sprite getSprite(int index) {
        return sprites.get(index);
    }

    public Array<Sprite> getSprites() {
        return sprites;
    }

    public void setSprites(Array<Sprite> sprites) {
        this.sprites = sprites;
        this.currentFrame = 0;
        this.frameTimer = 0f;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }
}
