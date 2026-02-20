package it.sleepdeprived.brackeys_2026_1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.io.BufferedReader;
import java.util.Scanner;

public class Level {
    public static final float PX_PER_TILE = 32f;

    private float playerStartX;
    private float playerStartY;

    public static final float MAP_WIDTH = 40 * PX_PER_TILE;
    public static final float MAP_HEIGHT = 22 * PX_PER_TILE;

    // map / collision
    private Array<Rectangle> borders = new Array<>();
    private Rectangle intersection = new Rectangle();


    private Texture levelTex;

    public Level(int levelNumber) {
        String levelPath = loadMapFromText(levelNumber);
        levelTex = new Texture(Gdx.files.internal(levelPath));
        levelTex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(levelTex, 0, 0);
    }

    /**
     * Reads the tile map for the given level number, populates {@code borders},
     * and returns the path to the matching PNG background.
     *
     * @param map 1 = first_level, 2 = second_level, 3 = third_level
     * @return path to the level PNG (e.g. "first_level.png")
     */
    private String loadMapFromText(int map) {
        borders.clear();

        String txtPath;
        switch (map) {
            case 1:
                txtPath = "first_level.txt";
                borders.add(new Rectangle(PX_PER_TILE*41,PX_PER_TILE*2,PX_PER_TILE,PX_PER_TILE*2));
                borders.add(new Rectangle(PX_PER_TILE*40,PX_PER_TILE,PX_PER_TILE,PX_PER_TILE));
                borders.add(new Rectangle(PX_PER_TILE*40,PX_PER_TILE*4,PX_PER_TILE,PX_PER_TILE));
                break;
            case 2:
                txtPath = "second_level.txt";

                borders.add(new Rectangle(PX_PER_TILE*41,PX_PER_TILE*17,PX_PER_TILE,PX_PER_TILE*3));
                borders.add(new Rectangle(PX_PER_TILE*40,PX_PER_TILE*20,PX_PER_TILE,PX_PER_TILE));
                borders.add(new Rectangle(PX_PER_TILE*40,PX_PER_TILE*16,PX_PER_TILE,PX_PER_TILE));
                break;
            case 3:
                txtPath = "third_level.txt";
                borders.add(new Rectangle(PX_PER_TILE*41,PX_PER_TILE*2,PX_PER_TILE,PX_PER_TILE*3));
                borders.add(new Rectangle(PX_PER_TILE*40,PX_PER_TILE,PX_PER_TILE,PX_PER_TILE));
                borders.add(new Rectangle(PX_PER_TILE*40,PX_PER_TILE*5,PX_PER_TILE,PX_PER_TILE));
                break;
            default:
                throw new IllegalArgumentException("Unknown map index: " + map);
        }

        switch (map){
            case 1,3:
                borders.add(new Rectangle(PX_PER_TILE*-2,PX_PER_TILE*17,PX_PER_TILE,PX_PER_TILE*3));
                borders.add(new Rectangle(PX_PER_TILE*-1,PX_PER_TILE*20,PX_PER_TILE,PX_PER_TILE));
                borders.add(new Rectangle(PX_PER_TILE*-1,PX_PER_TILE*16,PX_PER_TILE,PX_PER_TILE));
                break;
            case 2:
                borders.add(new Rectangle(PX_PER_TILE*-2,PX_PER_TILE*2,PX_PER_TILE,PX_PER_TILE*3));
                borders.add(new Rectangle(PX_PER_TILE*-1,PX_PER_TILE*5,PX_PER_TILE,PX_PER_TILE));
                borders.add(new Rectangle(PX_PER_TILE*-1,PX_PER_TILE,PX_PER_TILE,PX_PER_TILE));

                break;
        }

        int[][] matrix = new int[22][40];

        if (Gdx.files.internal(txtPath).exists()) {
            try (BufferedReader in = new BufferedReader(Gdx.files.internal(txtPath).reader())) {
                int row = 0;
                String line;
                while ((line = in.readLine()) != null && row < 22) {
                    int col = 0;
                    Scanner sc = new Scanner(line);
                    while (sc.hasNextInt()) {
                        matrix[row][col++] = sc.nextInt();
                    }
                    row++;
                }

                String coordLine = in.readLine();
                if (coordLine != null) {
                    Scanner sc = new Scanner(coordLine);
                    if (sc.hasNextInt()) {
                        playerStartX = sc.nextInt();
                    }
                    if (sc.hasNextInt()) {
                        playerStartY = sc.nextInt();
                    }
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

    public Rectangle getIntersection() {
        return intersection;
    }

    public Array<Rectangle> getBorders() {
        return borders;
    }

    public float getPlayerStartX() {
        return playerStartX;
    }

    public float getPlayerStartY() {
        return playerStartY;
    }

    public void dispose() {
        if (levelTex != null) {
            levelTex.dispose();
        }
    }
}
