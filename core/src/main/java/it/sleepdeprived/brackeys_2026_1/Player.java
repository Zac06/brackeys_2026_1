package it.sleepdeprived.brackeys_2026_1;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Player extends Entity {

    private final Sprite stillSprite;   // single still frame
    private final Sprite eyeSprite;

    private float eyeSpriteBaseOffsetX;
    private float eyeSpriteBaseOffsetY;
    private float eyeSpriteShiftX;
    private float eyeSpriteShiftY;

    private static final float eyeSprite_SHIFT = 4f;

    private boolean animating = false;  // false = show still, true = cycle frames
    private Direction direction = Direction.CENTER;

    public Player(float x, float y, float width, float height, Sprite stillSprite, Array<Sprite> animationFrames, Sprite eye, float eyeBaseOffsetX, float eyeBaseOffsetY, float frameDuration) {
        super(x, y, width, height, animationFrames);

        this.eyeSprite = eye;
        this.eyeSpriteBaseOffsetX = eyeBaseOffsetX;
        this.eyeSpriteBaseOffsetY = eyeBaseOffsetY;

        setFrameDuration(frameDuration);

        // scale still sprite to player size
        stillSprite.setPosition(x, y);
        stillSprite.setSize(width, height);
        this.stillSprite = stillSprite;

        // scale eye proportionally to the player dimensions
        // using the ratio of eye texture size to still texture size
        float scaleX = width / stillSprite.getRegionWidth();
        float scaleY = height / stillSprite.getRegionHeight();
        this.eyeSprite.setSize(eye.getRegionWidth() * scaleX, eye.getRegionHeight() * scaleY);

        updateEyeSpritePosition();
    }

    // --- Animation state ---

    public boolean isAnimating() {
        return animating;
    }

    public void setAnimating(boolean animating) {
        if (this.animating == animating) return;
        this.animating = animating;
        if (animating) {
            setCurrentFrame(0);
            resetFrameTimer();
        }
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        switch (direction) {
            case NORTH:
                eyeSpriteShiftX = 0;
                eyeSpriteShiftY = eyeSprite_SHIFT;
                break;
            case NORTH_EAST:
                eyeSpriteShiftX = eyeSprite_SHIFT;
                eyeSpriteShiftY = eyeSprite_SHIFT;
                break;
            case EAST:
                eyeSpriteShiftX = eyeSprite_SHIFT;
                eyeSpriteShiftY = 0;
                break;
            case SOUTH_EAST:
                eyeSpriteShiftX = eyeSprite_SHIFT;
                eyeSpriteShiftY = -eyeSprite_SHIFT;
                break;
            case SOUTH:
                eyeSpriteShiftX = 0;
                eyeSpriteShiftY = -eyeSprite_SHIFT;
                break;
            case SOUTH_WEST:
                eyeSpriteShiftX = -eyeSprite_SHIFT;
                eyeSpriteShiftY = -eyeSprite_SHIFT;
                break;
            case WEST:
                eyeSpriteShiftX = -eyeSprite_SHIFT;
                eyeSpriteShiftY = 0;
                break;
            case NORTH_WEST:
                eyeSpriteShiftX = -eyeSprite_SHIFT;
                eyeSpriteShiftY = eyeSprite_SHIFT;
                break;
            case CENTER:
            default:
                eyeSpriteShiftX = 0;
                eyeSpriteShiftY = 0;
                break;
        }
        updateEyeSpritePosition();
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        stillSprite.setPosition(x, y);
        updateEyeSpritePosition();
    }

    public void setXPosition(float x) {
        setPosition(x, getY());
    }

    public void setYPosition(float y) {
        setPosition(getX(), y);
    }

    private void updateEyeSpritePosition() {
        eyeSprite.setPosition(
            getX() + eyeSpriteBaseOffsetX + eyeSpriteShiftX,
            getY() + eyeSpriteBaseOffsetY + eyeSpriteShiftY
        );
    }


    public void draw(SpriteBatch batch, float delta) {
        if (animating) {
            update(delta);          // advance animation frame in Entity
            super.draw(batch);      // draw current animation frame
        } else {
            stillSprite.draw(batch);
        }
        eyeSprite.draw(batch);
    }

    public Direction getDirection() {
        return direction;
    }

    public Sprite getStillSprite() {
        return stillSprite;
    }

    public void setEyeSpriteBaseOffset(float x, float y) {
        this.eyeSpriteBaseOffsetX = x;
        this.eyeSpriteBaseOffsetY = y;
        updateEyeSpritePosition();
    }
}
