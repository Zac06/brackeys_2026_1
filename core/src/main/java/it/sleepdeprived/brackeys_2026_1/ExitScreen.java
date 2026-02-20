package it.sleepdeprived.brackeys_2026_1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ExitScreen implements Screen {
    private String message;
    private int levelScreenNumber;
    private int persistenceMilliseconds;
    private int elapsedMilliseconds;

    private Main game;
    private SpriteBatch batch;

    private Texture background;
    private Texture banner;
    private GlyphLayout layout;

    private OrthographicCamera camera;
    private Viewport viewport;

    public ExitScreen(Main game) {
        this.game = game;
        this.message = "";
        this.levelScreenNumber = 1;
        this.persistenceMilliseconds = 0;
        this.elapsedMilliseconds = 0;
    }

    public ExitScreen(Main game, String message, int levelScreenNumber, int persistenceMilliseconds) {
        this.game = game;
        this.message = message;
        this.levelScreenNumber = levelScreenNumber;
        this.persistenceMilliseconds = persistenceMilliseconds;
        this.elapsedMilliseconds = 0;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();

        //sets up rendering shenanigans
        viewport = new FitViewport(WindowProperties.WIN_WIDTH, WindowProperties.WIN_HEIGHT, camera);
        batch = new SpriteBatch();

        layout = new GlyphLayout();

        banner = new Texture(Gdx.files.internal("banner.png"));
        switch (levelScreenNumber) {
            case 1:
                background = new Texture(Gdx.files.internal("first_level.png"));
                break;
            case 2:
                background = new Texture(Gdx.files.internal("second_level.png"));
                break;
            case 3:
                background = new Texture(Gdx.files.internal("third_level.png"));
                break;
        }

        this.elapsedMilliseconds = 0;

    }

    @Override
    public void render(float delta) {
        elapsedMilliseconds += (int)(delta * 1000);
        if(persistenceMilliseconds>0 &&  elapsedMilliseconds>=persistenceMilliseconds) {
            game.setScreen(new LevelSelectorScreen(game, levelScreenNumber));
        }

        UnifiedColorClearer.clear();

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Draw background splash
        batch.begin();
        batch.draw(background, 0, 0, WindowProperties.WIN_WIDTH, WindowProperties.WIN_HEIGHT);
        batch.draw(banner, 0, 0, WindowProperties.WIN_WIDTH, WindowProperties.WIN_HEIGHT);

        if (message != null && !message.isEmpty()) {
            layout.setText(game.getFont().getBigFont(), message, Color.WHITE,
                WindowProperties.WIN_WIDTH, Align.center, false);

            game.getFont().getBigFont().draw(batch, layout, 0, WindowProperties.WIN_HEIGHT / 2f);
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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

    public void dispose() {
        if (batch != null) {
            batch.dispose();
        }
        if (background != null) {
            background.dispose();
        }
        if(banner != null) {
            banner.dispose();
        }

    }
}
