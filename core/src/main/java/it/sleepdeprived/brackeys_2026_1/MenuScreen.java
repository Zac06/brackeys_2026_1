package it.sleepdeprived.brackeys_2026_1;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.*;

public class MenuScreen implements Screen {
    private final Main game;
    private SpriteBatch batch;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Texture background;
    private TextureRegionDrawable startButtonTexture;
    private TextureRegionDrawable startButtonHoverTexture;
    private Sprite title;

    private Stage stage;

    public MenuScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(WindowProperties.WIN_WIDTH, WindowProperties.WIN_HEIGHT, camera);
        batch = new SpriteBatch();
        background = new Texture("homebg.png");

        startButtonTexture = new TextureRegionDrawable(new Texture("startbutton.png"));
        startButtonTexture.setTopHeight(12);

        startButtonHoverTexture = new TextureRegionDrawable(new Texture("startbutton-hover.png"));
        startButtonHoverTexture.setTopHeight(10);

        Texture titleTexture=new Texture("title.png");
        titleTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        title = new Sprite(titleTexture);

        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.getFont().getButtonFont();
        buttonStyle.up = startButtonTexture;
        buttonStyle.over = startButtonHoverTexture;

        TextButton startButton = new TextButton("START", buttonStyle);
        startButton.setSize(startButton.getWidth() * 2f, startButton.getHeight() * 2f);
        startButton.setPosition(
            (WindowProperties.WIN_WIDTH - 200) / 2f,
            (WindowProperties.WIN_HEIGHT - 60) / 2f
        );
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("ciao");
                game.setScreen(new LevelSelectorScreen(game, 1));
            }
        });
        stage.addActor(startButton);

        // --- Credits Label ---
        Label.LabelStyle labelStyle = new Label.LabelStyle(game.getFont().getCreditFont(), Color.LIGHT_GRAY);
        Label credits = new Label("by sleepdeprived duo", labelStyle);

        credits.setAlignment(Align.right);
        credits.setPosition(WindowProperties.WIN_WIDTH - credits.getPrefWidth() - 16, 16);
        stage.addActor(credits);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Draw background splash
        batch.begin();
        batch.draw(background, 0, 0, WindowProperties.WIN_WIDTH, WindowProperties.WIN_HEIGHT);

        batch.draw(title, Math.round((WindowProperties.WIN_WIDTH - title.getWidth()) / 2f), 500);
        batch.end();

        // Draw UI on top
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        stage.dispose();
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
