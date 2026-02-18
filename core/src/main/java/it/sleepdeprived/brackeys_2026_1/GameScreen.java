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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.BufferedReader;
import java.util.Scanner;

public class GameScreen implements Screen {

    // ── constants ────────────────────────────────────────────────────────────
    private static final float PX_PER_TILE = 32f;
    private static final float PLAYER_SPEED = 4f;
    private static final float PLAYER_W = 28f;
    private static final float PLAYER_H = 32f;
    private static final float PLAYER_START_X = 30f;
    private static final float PLAYER_START_Y = 560f;
    private static final float PLAYER_EYE_OFFSET_X = PLAYER_W / 2 - 4;
    private static final float PLAYER_EYE_OFFSET_Y = PLAYER_H / 2 + 6;
    private static final float PLAYER_ANIMATION_DELAY = 0.1f;

    private static final float MAP_WIDTH = 40 * PX_PER_TILE;
    private static final float MAP_HEIGHT = 22 * PX_PER_TILE;


    // ── fields ────────────────────────────────────────────────────────────────
    private final Main game;
    private SpriteBatch batch;

    private OrthographicCamera camera;
    private Viewport viewport;

    // textures (kept so we can dispose them)
    private Texture levelTex;
    private Texture stillTex;
    private Texture eyeTex;
    private Array<Texture> animationTextures;

    private Player player;

    // map / collision
    private Array<Rectangle> borders = new Array<>();
    private Rectangle intersection = new Rectangle();

    // ── constructor ───────────────────────────────────────────────────────────
    public GameScreen(Main game) {
        this.game = game;
    }

    // ── Screen lifecycle ──────────────────────────────────────────────────────
    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.zoom = 0.5f;

        viewport = new FitViewport(1280, 704, camera);
        batch = new SpriteBatch();

        // Load map (1 = first_level, 2 = second_level, 3 = third_level)
        String levelPath = loadMapFromText(1);
        levelTex = new Texture(levelPath);
        levelTex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        // Player still sprite
        stillTex = new Texture("player/still.png");
        stillTex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        Sprite stillSprite = new Sprite(stillTex);

        // Walking animation sprites
        animationTextures = new Array<>();
        Array<Sprite> animationSprites = new Array<>();
        String[] animPaths = {"player/animation1.png", "player/animation2.png"};
        for (String path : animPaths) {
            Texture t = new Texture(path);
            t.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            animationTextures.add(t);
            animationSprites.add(new Sprite(t));
        }

        // Eye sprite
        eyeTex = new Texture("player/eyes.png");
        eyeTex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        Sprite eyeSprite = new Sprite(eyeTex);

        player = new Player(
            PLAYER_START_X, PLAYER_START_Y,
            PLAYER_W, PLAYER_H,
            stillSprite,
            animationSprites,
            eyeSprite,
            PLAYER_EYE_OFFSET_X, PLAYER_EYE_OFFSET_Y,
            PLAYER_ANIMATION_DELAY
        );
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handleInput();
        checkCollisionPlayerMap();

        camera.position.set(
            player.getX() + PLAYER_W / 2f,
            player.getY() + PLAYER_H / 2f,
            0
        );

        //camera.update();
        updateCamera();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(levelTex, 0, 0);
        player.draw(batch, delta);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        levelTex.dispose();
        stillTex.dispose();
        eyeTex.dispose();
        for (Texture t : animationTextures) t.dispose();
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
        camera.position.x = Math.max(halfW, Math.min(camera.position.x, MAP_WIDTH - halfW));
        camera.position.y = Math.max(halfH, Math.min(camera.position.y, MAP_HEIGHT - halfH));

        camera.update();
    }

    // ── map loading ───────────────────────────────────────────────────────────

    /**
     * Reads the tile map for the given level number, populates {@code borders},
     * and returns the path to the matching PNG background.
     *
     * @param map 1 = first_level, 2 = second_level, 3 = third_level
     * @return path to the level PNG (e.g. "first_level.png")
     */
    private String loadMapFromText(int map) {
        String txtPath;
        switch (map) {
            case 1:
                txtPath = "first_level.txt";
                break;
            case 2:
                txtPath = "second_level.txt";
                break;
            case 3:
                txtPath = "third_level.txt";
                break;
            default:
                throw new IllegalArgumentException("Unknown map index: " + map);
        }

        int[][] matrix = new int[22][40];

        if (Gdx.files.internal(txtPath).exists()) {
            try (BufferedReader in = new BufferedReader(Gdx.files.internal(txtPath).reader())) {
                int row = 0;
                String line;
                while ((line = in.readLine()) != null) {
                    int col = 0;
                    Scanner sc = new Scanner(line);
                    while (sc.hasNextInt()) {
                        matrix[row][col++] = sc.nextInt();
                    }
                    row++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            loadMapBorders(matrix);
        }

        // Strip ".txt" → ".png"
        return txtPath.substring(0, txtPath.length() - 3) + "png";
    }

    private void loadMapBorders(int[][] mapGrid) {
        borders.clear();
        int rows = mapGrid.length;
        int cols = mapGrid[0].length;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (mapGrid[row][col] == 1) {
                    float bx = col * PX_PER_TILE;
                    float by = (rows - 1 - row) * PX_PER_TILE; // flip Y axis
                    borders.add(new Rectangle(bx, by, PX_PER_TILE, PX_PER_TILE));
                }
            }
        }
    }

    // ── collision ─────────────────────────────────────────────────────────────
    private void checkCollisionPlayerMap() {
        Rectangle playerRect = player.getHitbox();
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
}
