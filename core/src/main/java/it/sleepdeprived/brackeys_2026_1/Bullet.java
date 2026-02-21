package it.sleepdeprived.brackeys_2026_1;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Vector2;

public class Bullet extends Entity {

    private Vector2 direction;  // normalized
    private Vector2 velocity;   // direction * speed

    public Bullet(float x, float y,
                  float width, float height,
                  Array<Sprite> sprites,
                  Vector2 direction,
                  float speed) {

        super(x, y, width, height, sprites);

        this.direction = new Vector2(direction).nor(); //nor=normalized
        this.velocity = new Vector2(this.direction).scl(speed);     //scl=scalar

        float rotation = this.direction.angleDeg();
        for (Sprite s : sprites) {
            s.setOriginCenter();
            s.setRotation(rotation);
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        float newX = getX() + velocity.x * delta;
        float newY = getY() + velocity.y * delta;

        setPosition(newX, newY);
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getDirection() {
        return direction;
    }
}
