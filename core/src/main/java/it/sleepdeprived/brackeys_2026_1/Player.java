package it.sleepdeprived.brackeys_2026_1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Player extends Entity {

    private final Sprite stillSprite;   // single still frame
    private final Sprite eyeSprite;

    private final Sprite shotgunSprite;
    private float shotgunAngle;

    private float eyeSpriteBaseOffsetX;
    private float eyeSpriteBaseOffsetY;
    private float eyeSpriteShiftX;
    private float eyeSpriteShiftY;

    private static final float eyeSprite_SHIFT = 4f;
    private static final float SHOTGUN_OFFSET=16f;

    private boolean animating = false;  // false = show still, true = cycle frames
    private Direction direction = Direction.CENTER;

    public Player(float x, float y, float width, float height, Sprite stillSprite, Array<Sprite> animationFrames, Sprite eye, float eyeBaseOffsetX, float eyeBaseOffsetY, float frameDuration, Sprite shotgunSprite) {
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

        this.shotgunSprite = shotgunSprite;
        this.shotgunAngle=0;
        this.shotgunSprite.setSize(this.shotgunSprite.getWidth() * scaleX, this.shotgunSprite.getHeight() * scaleY);

        //this.shotgunSprite.setOriginCenter();
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
            super.draw(batch);      // draw current animation frame
        } else {
            stillSprite.draw(batch);
        }
        eyeSprite.draw(batch);

        shotgunSprite.draw(batch);
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

    public void update(float delta, OrthographicCamera camera) {

        // keep your animation update
        super.update(delta);

        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mouse);

        float centerX = getX() + getWidth() / 2f;
        float centerY = getY() + getHeight() / 2f;

        float dx = mouse.x - centerX;
        float dy = mouse.y - centerY;

        float shotgunAngleRadians=MathUtils.atan2(dy, dx);
        shotgunAngle = shotgunAngleRadians * MathUtils.radiansToDegrees;

        this.shotgunSprite.setOrigin(0, this.shotgunSprite.getHeight() / 2);
        shotgunSprite.setRotation(shotgunAngle);
        shotgunSprite.setPosition(
            (float) (centerX+SHOTGUN_OFFSET*(Math.cos(shotgunAngleRadians))),
            //centerY - shotgunSprite.getHeight() / 2f
            (float)(centerY+SHOTGUN_OFFSET*(Math.sin(shotgunAngleRadians)))
        );
    }

    public Vector2 getShootingPoint(OrthographicCamera camera, int bulletHeight){
        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mouse);

        float centerX = getX() + getWidth() / 2f;
        float centerY = getY() + getHeight() / 2f;

        float dx = mouse.x - centerX;
        float dy = mouse.y - centerY;

        float shotgunAngleRadians=MathUtils.atan2(dy, dx);
        shotgunAngle = shotgunAngleRadians * MathUtils.radiansToDegrees;

        return new Vector2(
            (float) (centerX+(SHOTGUN_OFFSET+shotgunSprite.getWidth())*(Math.cos(shotgunAngleRadians))),
            (float) (centerY+(SHOTGUN_OFFSET+shotgunSprite.getWidth())*(Math.sin(shotgunAngleRadians)))
            );
    }

    public float getShotgunAngle(){
        return shotgunAngle;
    }
}
