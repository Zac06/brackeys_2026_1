package it.sleepdeprived.brackeys_2026_1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LevelSelectorScreen implements Screen {
    private final Main game;
    private SpriteBatch batch;

    private OrthographicCamera camera;
    private Viewport viewport;

    private Texture levelBtnTexture;
    private Texture levelBtnTextureHover;
    private Texture nextLevelBtnTexture;
    private Texture nextLevelBtnTextureHover;
    private Texture previousLevelBtnTexture;
    private Texture previousLevelBtnTextureHover;

    private int currentLevelNumber;

    private Stage stage;

    public LevelSelectorScreen(Main game, int currentLevelNumber) {
        this.game = game;
        this.currentLevelNumber = currentLevelNumber;
    }

    public LevelSelectorScreen(Main game) {
        this.game = game;
        this.currentLevelNumber = 1;
    }

    @Override
    public void show() {
        //sets up the camera
        camera = new OrthographicCamera();

        //sets up rendering shenenigans
        viewport = new FitViewport(1280, 704, camera);
        batch = new SpriteBatch();

        loadLevelTexture(currentLevelNumber);

        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        Button.ButtonStyle startBtnStyle=new Button.ButtonStyle();
        startBtnStyle.up=new TextureRegionDrawable(levelBtnTexture);
        startBtnStyle.over=new TextureRegionDrawable(levelBtnTextureHover);

        Button startBtn = new Button(startBtnStyle);
        startBtn.setSize(startBtn.getWidth() * 8f, startBtn.getHeight() * 8f);
        startBtn.setPosition(
            (1280 + startBtn.getWidth()) / 2f,
            (704 + startBtn.getHeight()) / 2f
        );
        startBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("ciao");
                //game.setScreen(new GameScreen(game));
            }
        });
        stage.addActor(startBtn);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(207/255f, 219/255f, 114/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
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

    private void loadLevelTexture(int levelNumber){
        if(levelNumber <= 0 || levelNumber > 3){
            throw new IllegalArgumentException("invalid level number");
        }

        levelBtnTexture=new Texture(Gdx.files.internal("levelselector/level"+levelNumber+"btn.png"));
        levelBtnTextureHover=new Texture(Gdx.files.internal("levelselector/level"+levelNumber+"btnh.png"));
    }
}
