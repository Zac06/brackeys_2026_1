package it.sleepdeprived.brackeys_2026_1;
/*
COMMENTI CHE SERVONO AL NABO CERVI

coordinate cartesiane : (x,y) (0,0) sta in baso a sx
 */

/*
Ho inserito tra i file assets/ui/
    - first_level.png sfondo del primo livello
    - gem.png da ridimensionare a piacere
    - heart.png nel caso ci servisse si puo' togliere
    - key.png che apre permette di terminare il livello
 */

/*
    siccome ho pensato a creare gli sfondi di gioco caricando dei png ho avuto un idea:
    prendere il colore dei pixel circostanti e verificare se dello colore del muro.
    chiedo a chat e mi fa che risulterebbe lento e consiglierebbe questo:
    usare la classe rectangole, e creare dei rettangoli invisibili che "cozzano" con le coordinate dei muri effettivi dello sfondo

    ci penso tranquillamente io, penso di creare una funzione che in base al livello che sarà caratterizzato da un intero
    mi crea dei rettangoli che fungono da bordi
 */



// dimensioni schermo : 1280 704
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Intersector;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private final float pxPerTile = 32;
    private SpriteBatch batch;
    private Texture level;
    private Texture player;
    private int y = 560;
    private int x = 30;
    private Array<Rectangle> borders = new Array<>();
    private Rectangle intersection = new Rectangle(); // Rettangolo temporaneo per il risultato
    private Player p;
    private String path;
    @Override
    public void create() {
        batch = new SpriteBatch();
        /*
        ================================== SCOMMENTA PER PROVARE
        player = new Texture("heart.png");
        Array<Texture> h = new Array<>();
        h.add(player);
        p = new Player(x, y, h, new Rectangle(x, y, 40, 40));
        loadMapFromText(3);<-------- INSERISCI 1,2,3 IN BASE AL LIVELLO CHE VUOI PROVARE
        level = new Texture(path.substring(0,path.length()-3)+"png");
        */

    }

    // ciao
    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        isKeyPressed();// PER MUOVERSI NEL TEST
        checkCollisionPlayerMap();// PER FAR SI CHE IL PLAYER SI FERMI NEL TEST
        batch.begin();
        batch.draw(level, 0, 0);// DISEGNA SFONDO
        batch.draw(p.getSprite(0), p.getX(), p.getY());// DISEGNA PLAYER
        batch.end();

    }

    @Override
    public void dispose() {
        batch.dispose();
        level.dispose();
    }

    public void isKeyPressed() {
        if (Gdx.input.isKeyPressed(Input.Keys.D)) p.setX(p.getX() + 6);
        if (Gdx.input.isKeyPressed(Input.Keys.A)) p.setX(p.getX() - 6);
        if (Gdx.input.isKeyPressed(Input.Keys.W)) p.setY(p.getY() + 6);
        if (Gdx.input.isKeyPressed(Input.Keys.S)) p.setY(p.getY() - 6);
    }

    //  Rectangle(x,y,width,height)

    public void loadMapFromText(int map){
        int [][] matrix = new int[22][40];// inserire delle varibili provate sopra
        path="";
        switch (map){
            case 1:
                path = "first_level.txt";
                break;
            case 2:
                path = "second_level.txt";
                break;
            case 3:
                path = "third_level.txt";
                break;
            default:
                break;
        }
        int i = 0, j = 0;
        if(Gdx.files.internal(path).exists()){
            try(BufferedReader in = new BufferedReader(Gdx.files.internal(path).reader());){
                String temp = in.readLine();
                while(temp != null){
                    Scanner temp_sc = new Scanner(temp);
                    while(temp_sc.hasNext()){
                        matrix[i][j] = Integer.parseInt(temp_sc.next());
                        j++;
                    }
                    j=0;
                    i++;
                    temp = in.readLine();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            loadMapBorders(matrix);
        }
    }
    public void loadMapBorders(int[][] mapGrid) {
        for (int row = 0; row < mapGrid.length; row++) {
            for (int col = 0; col < mapGrid[0].length; col++) {
                if (mapGrid[row][col] == 1) {
                    // Calcolo coordinate:
                    // X è facile: colonna * dimensionessddwd
                    float x = col * pxPerTile;

                    // Y è invertita: (Altezza Totale - Riga Corrente - 1) * dimensione
                    float y = (mapGrid.length - 1 - row) * pxPerTile;

                    borders.add(new Rectangle(x, y, pxPerTile, pxPerTile));
                }
            }
        }
    }
    public void checkCollisionPlayerMap() {
        int padding = 20;
        for (Rectangle wall : borders) {
            if (Intersector.intersectRectangles(p.getRec(), wall, intersection)) {
                // Se siamo qui, c'è una collisione.
                // 'intersection' ora contiene l'area sovrapposta.
                // Capiamo se la collisione è orizzontale o verticale guardando la forma dell'intersezione
                if (intersection.height > intersection.width) {
                    // L'intersezione è alta e stretta -> Collisione LATERALE (Destra/Sinistra)
                    // Se il centro del player è a sinistra del muro, spingilo a sinistra
                    if (p.getX() <= wall.x) {
                        p.setX(wall.x - p.getRec().getWidth());
                    } else {
                        p.setX(wall.x + p.getRec().getWidth());
                    }
                } else {
                    if (p.getY() <= wall.y) {
                        p.setY(wall.y - p.getRec().getHeight());
                    } else {
                        p.setY(wall.y + p.getRec().getHeight());
                    }
                }
            }
        }
    }
}
