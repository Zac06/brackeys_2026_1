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

//  Rectangle(x,y,width,height)

// dimensioni schermo : 1280 704
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture level;
    private Texture player;
    private int y = 560;
    private int x = 30;
    @Override
    public void create() {
        batch = new SpriteBatch();
        level = new Texture("first_level.png");
        player = new Texture("heart.png");
    }
    // ciao
    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        isKeyPressed();
        batch.begin();
        batch.draw(level,0,0);
        batch.draw(player,x,y);
        batch.end();

    }

    @Override
    public void dispose() {
        batch.dispose();
        level.dispose();
    }

    public void isKeyPressed(){
        if(Gdx.input.isKeyPressed(Input.Keys.D)) x+=2;
        if(Gdx.input.isKeyPressed(Input.Keys.A)) x-=2;
        if(Gdx.input.isKeyPressed(Input.Keys.W)) y+=2;
        if(Gdx.input.isKeyPressed(Input.Keys.S)) y-=2;
    }
}
