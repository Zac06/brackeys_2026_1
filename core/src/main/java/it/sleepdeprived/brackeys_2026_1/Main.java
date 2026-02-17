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

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {
    private SpriteBatch batch;
    private Font font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new Font();
        setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {
        super.render(); // this calls the active screen's render()
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public Font getFont() {
        return font;
    }
}
