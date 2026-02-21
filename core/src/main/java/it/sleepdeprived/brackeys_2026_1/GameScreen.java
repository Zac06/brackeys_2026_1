package it.sleepdeprived.brackeys_2026_1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.BufferedReader;
import java.util.Scanner;

public class GameScreen implements Screen {
    private static final float PLAYER_SPEED = 4f;
    private static final float PLAYER_W = 28f;
    private static final float PLAYER_H = 32f;

    private static final float PLAYER_EYE_OFFSET_X = PLAYER_W / 2 - 4;
    private static final float PLAYER_EYE_OFFSET_Y = PLAYER_H / 2 + 6;
    private static final float PLAYER_ANIMATION_DELAY = 0.1f;

    private static final float BULLET_W = 16f;
    private static final float BULLET_H = 12f;
    private static final float BULLET_SPEED = 128f;


    // ── fields ────────────────────────────────────────────────────────────────
    private final Main game;
    private SpriteBatch batch;

    private OrthographicCamera camera;
    private Viewport viewport;

    // textures (kept so we can dispose them)
    //private Texture levelTex;

    // player textures, to keep in gamescreen
    private Texture stillTex;
    private Texture eyeTex;
    private Array<Texture> animationTextures;
    private Texture shotgunTex;
    private Array<Texture> bulletTextures;

    private Array<Bullet> bullets = new Array<>();

    private Player player;
    private Level level;
    private int levelNumber;

    private int uscito = 2; // controlla se il player esce dalla mappa o non
    /*
        0 --> uscito a sx (inizio)
        1 --> uscito a dx (fine)
        2 --> non uscito (player dentro alla mappa)
     */

    // ── constructor ───────────────────────────────────────────────────────────
    public GameScreen(Main game) {
        this.game = game;
        this.levelNumber = 1;
    }

    public GameScreen(Main game, int levelNumber) {
        this.game = game;
        this.levelNumber = levelNumber;
    }

    // ── Screen lifecycle ──────────────────────────────────────────────────────
    @Override
    public void show() {
        //sets up the camera
        camera = new OrthographicCamera();
        camera.zoom = 0.5f;

        //sets up rendering shenenigans
        viewport = new FitViewport(WindowProperties.WIN_WIDTH, WindowProperties.WIN_HEIGHT, camera);
        batch = new SpriteBatch();

        level = new Level(levelNumber);

        loadPlayer();

        bulletTextures = new Array<>();
        String[] animPaths = {"bullet1.png", "bullet2.png"};
        for (String path : animPaths) {
            Texture t = new Texture(Gdx.files.internal(path));
            t.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            bulletTextures.add(t);
        }
    }

    @Override
    public void render(float delta) {
        update(delta);

        UnifiedColorClearer.clear();

        batch.begin();
        level.draw(batch);
        player.draw(batch, delta);

        for (Bullet b : bullets) {
            b.draw(batch);
        }

        batch.end();
    }

    public void update(float delta) {
        player.update(delta, camera);

        camera.position.set(
            player.getX() + PLAYER_W / 2f,
            player.getY() + PLAYER_H / 2f,
            0
        );

        handleInput();
        checkCollisionPlayerMap();
        checkBulletWallCollision();

        // SHOOT
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            shoot();
        }

        // Update bullets
        for (int i = bullets.size - 1; i >= 0; i--) {
            Bullet b = bullets.get(i);
            b.update(delta);

            // Remove if outside map
            if (b.getX() < 0 || b.getX() > Level.MAP_WIDTH ||
                b.getY() < 0 || b.getY() > Level.MAP_HEIGHT) {
                bullets.removeIndex(i);
                //System.out.println("Bullet was removed!");
            }
        }

        updateCamera();
        batch.setProjectionMatrix(camera.combined);
    }

    private void shoot() {

        // Get mouse world position
        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mouse);

        Vector2 shootingPoint=player.getShootingPoint(camera);

        float centerX = player.getX() + player.getWidth() / 2f;
        float centerY = player.getY() + player.getHeight() / 2f;

        Vector2 direction = new Vector2(
            mouse.x - centerX,
            mouse.y - centerY
        );

        Array<Sprite> sprites = new Array<>();
        for(Texture t : bulletTextures){
            Sprite bulletSprite = new Sprite(t);
            bulletSprite.setSize(BULLET_W, BULLET_H);
            sprites.add(bulletSprite);
        }

        Bullet bullet = new Bullet(
            shootingPoint.x,
            shootingPoint.y,
            BULLET_W,
            BULLET_H,
            sprites,
            direction,
            BULLET_SPEED
        );
        bullets.add(bullet);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        if (batch != null) {
            batch.dispose();
        }
        if (level != null) {
            level.dispose();
        }

        if (stillTex != null) {
            stillTex.dispose();
        }
        if (eyeTex != null) {
            eyeTex.dispose();
        }

        if (shotgunTex != null) {
            shotgunTex.dispose();
        }

        for (Texture t : animationTextures) {
            if (t != null) {
                t.dispose();
            }
        }

        for (Texture t : bulletTextures) {
            if (t != null) {
                t.dispose();
            }
        }
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    // ── input ─────────────────────────────────────────────────────────────────
    private void handleInput() {
        float dx = 0, dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.D)) dx += PLAYER_SPEED;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) dx -= PLAYER_SPEED;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) dy += PLAYER_SPEED;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) dy -= PLAYER_SPEED;

        boolean moving = dx != 0 || dy != 0;
        if (moving) {
            player.setPosition(player.getX() + dx, player.getY() + dy);
            player.setDirection(directionFromDelta(dx, dy));
        } else {
            player.setDirection(Direction.CENTER);
        }
        player.setAnimating(moving);
    }

    private Direction directionFromDelta(float dx, float dy) {
        if (dx > 0 && dy > 0) return Direction.NORTH_EAST;
        if (dx > 0 && dy < 0) return Direction.SOUTH_EAST;
        if (dx < 0 && dy > 0) return Direction.NORTH_WEST;
        if (dx < 0 && dy < 0) return Direction.SOUTH_WEST;
        if (dx > 0) return Direction.EAST;
        if (dx < 0) return Direction.WEST;
        if (dy > 0) return Direction.NORTH;
        return Direction.SOUTH;
    }

    private void updateCamera() {
        float targetX = player.getX() + PLAYER_W / 2f;
        float targetY = player.getY() + PLAYER_H / 2f;

        float halfW = viewport.getWorldWidth() * camera.zoom / 2f;
        float halfH = viewport.getWorldHeight() * camera.zoom / 2f;

        camera.position.set(targetX, targetY, 0);

        // ── clamp camera to map borders ──
        camera.position.x = Math.max(halfW, Math.min(camera.position.x, Level.MAP_WIDTH - halfW));
        camera.position.y = Math.max(halfH, Math.min(camera.position.y, Level.MAP_HEIGHT - halfH));

        camera.update();
    }

    private void loadPlayer() {
        stillTex = new Texture(Gdx.files.internal("player/still.png"));
        stillTex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        Sprite stillSprite = new Sprite(stillTex);

        // Walking animation sprites
        animationTextures = new Array<>();
        Array<Sprite> animationSprites = new Array<>();
        String[] animPaths = {"player/animation1.png", "player/animation2.png"};
        for (String path : animPaths) {
            Texture t = new Texture(Gdx.files.internal(path));
            t.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            animationTextures.add(t);
            animationSprites.add(new Sprite(t));
        }

        // Eye sprite
        eyeTex = new Texture(Gdx.files.internal("player/eyes.png"));
        eyeTex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        Sprite eyeSprite = new Sprite(eyeTex);

        // shotgun sprite
        shotgunTex = new Texture(Gdx.files.internal("shotgun.png"));
        shotgunTex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        Sprite shotgunSprite = new Sprite(shotgunTex);

        player = new Player(
            level.getPlayerStartX(), level.getPlayerStartY(),
            PLAYER_W, PLAYER_H,
            stillSprite,
            animationSprites,
            eyeSprite,
            PLAYER_EYE_OFFSET_X, PLAYER_EYE_OFFSET_Y,
            PLAYER_ANIMATION_DELAY,
            shotgunSprite
        );
    }

    // ── collision ─────────────────────────────────────────────────────────────
    private void checkCollisionPlayerMap() {
        Array<Rectangle> borders = level.getBorders();
        Rectangle intersection = level.getIntersection();

        Rectangle playerRect = player.getHitbox();

        if (player.getX() < -30) {
            uscito = 0;
            game.setScreen(new ExitScreen(game, "Wrong exit! :(", levelNumber, 2000));
        } else if (player.getX() > WindowProperties.WIN_WIDTH) {
            uscito = 1;
            game.setScreen(new ExitScreen(game, "You passed the level! :)", levelNumber, 2000));
        } else {
            uscito = 2;
        }

        for (Rectangle wall : borders) {
            if (Intersector.intersectRectangles(playerRect, wall, intersection)) {
                if (intersection.height > intersection.width) {
                    // Lateral collision (left / right)
                    if (player.getX() <= wall.x) {
                        player.setPosition(wall.x - playerRect.width, player.getY());
                    } else {
                        player.setPosition(wall.x + wall.width, player.getY());
                    }
                } else {
                    // Vertical collision (up / down)
                    if (player.getY() <= wall.y) {
                        player.setPosition(player.getX(), wall.y - playerRect.height);
                    } else {
                        player.setPosition(player.getX(), wall.y + wall.height);
                    }
                }
            }
        }
    }

    public void checkBulletWallCollision(){
        Array<Rectangle> borders = level.getBorders();
        Rectangle intersection = level.getIntersection();

        for (Rectangle wall : borders) {
            for(int i=0; i<bullets.size; i++){
                Rectangle bulletRect = bullets.get(i).getHitbox();

                if (Intersector.intersectRectangles(bulletRect, wall, intersection)) {
                    bullets.removeIndex(i);
                }
            }
        }
    }

    public int getUscito() {
        return this.uscito;
    }
}
