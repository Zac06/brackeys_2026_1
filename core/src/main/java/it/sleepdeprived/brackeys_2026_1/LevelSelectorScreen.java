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

    private Texture backgroundLevelTexture;

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
        viewport = new FitViewport(WindowProperties.WIN_WIDTH, WindowProperties.WIN_HEIGHT, camera);
        batch = new SpriteBatch();

        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        reloadUI();
    }

    @Override
    public void render(float delta) {
        UnifiedColorClearer.clear();

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(backgroundLevelTexture, 0, 0, WindowProperties.WIN_WIDTH, WindowProperties.WIN_HEIGHT);
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
        if (stage != null) {
            stage.dispose();
        }
        if (batch != null) {
            batch.dispose();
        }

        if(nextLevelBtnTexture != null) {
            nextLevelBtnTexture.dispose();
        }
        if(nextLevelBtnTextureHover != null) {
            nextLevelBtnTextureHover.dispose();
        }
        if(previousLevelBtnTexture != null) {
            previousLevelBtnTexture.dispose();
        }
        if(previousLevelBtnTextureHover != null) {
            previousLevelBtnTextureHover.dispose();
        }

        if (levelBtnTexture != null) {
            levelBtnTexture.dispose();
        }
        if (levelBtnTextureHover != null) {
            levelBtnTextureHover.dispose();
        }

        if (backgroundLevelTexture != null) {
            backgroundLevelTexture.dispose();
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
        if (Gdx.input.getInputProcessor() == stage) {
            Gdx.input.setInputProcessor(null);
        }
    }

    private void loadSelectorTextures(){
        if(stage!=null){
            stage.clear();
        }

        //dispose of the previously loaded textures
        if(levelBtnTexture != null){
            levelBtnTexture.dispose();
        }
        if(levelBtnTextureHover != null){
            levelBtnTextureHover.dispose();
        }

        if(nextLevelBtnTexture != null){
            nextLevelBtnTexture.dispose();
        }
        if(previousLevelBtnTexture != null){
            previousLevelBtnTexture.dispose();
        }

        if(nextLevelBtnTextureHover != null){
            nextLevelBtnTextureHover.dispose();
        }
        if(previousLevelBtnTextureHover != null){
            previousLevelBtnTextureHover.dispose();
        }

        if(backgroundLevelTexture != null){
            backgroundLevelTexture.dispose();
        }

        switch(currentLevelNumber){
            case 1:
                backgroundLevelTexture=new Texture(Gdx.files.internal("first_level.png"));
                break;
            case 2:
                backgroundLevelTexture=new Texture(Gdx.files.internal("second_level.png"));
                break;
            case 3:
                backgroundLevelTexture=new Texture(Gdx.files.internal("third_level.png"));
                break;
            default:
                throw new IllegalArgumentException("invalid level number");
        }

        levelBtnTexture=new Texture(Gdx.files.internal("levelselector/level"+currentLevelNumber+"btn.png"));
        levelBtnTextureHover=new Texture(Gdx.files.internal("levelselector/level"+currentLevelNumber+"btnh.png"));

        if(currentLevelNumber!=3){
            nextLevelBtnTexture=new Texture(Gdx.files.internal("levelselector/nextlevelbtn.png"));
            nextLevelBtnTextureHover=new Texture(Gdx.files.internal("levelselector/nextlevelbtnh.png"));
        }

        if(currentLevelNumber!=1){
            previousLevelBtnTexture=new Texture(Gdx.files.internal("levelselector/prevlevelbtn.png"));
            previousLevelBtnTextureHover=new Texture(Gdx.files.internal("levelselector/prevlevelbtnh.png"));
        }

    }

    private void reloadUI(){
        float worldWidth  = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        float padding = 20f;

        loadSelectorTextures();

        if(currentLevelNumber!=1){
            Button.ButtonStyle previousButtonStyle = new Button.ButtonStyle();
            previousButtonStyle.up = new TextureRegionDrawable(previousLevelBtnTexture);
            previousButtonStyle.over = new TextureRegionDrawable(previousLevelBtnTextureHover);

            Button previousButton = new Button(previousButtonStyle);
            previousButton.setSize(previousButton.getWidth()*8f, previousButton.getHeight()*8f);
            previousButton.setPosition(
                padding,
                (worldHeight - previousButton.getHeight()) / 2f
            );


            previousButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    currentLevelNumber--;
                    reloadUI();
                }
            });

            stage.addActor(previousButton);
        }

        if(currentLevelNumber!=3){
            Button.ButtonStyle nextButtonStyle = new Button.ButtonStyle();
            nextButtonStyle.up = new TextureRegionDrawable(nextLevelBtnTexture);
            nextButtonStyle.over = new TextureRegionDrawable(nextLevelBtnTextureHover);

            Button nextButton = new Button(nextButtonStyle);
            nextButton.setSize(nextButton.getWidth()*8f, nextButton.getHeight()*8f);
            nextButton.setPosition(
                worldWidth - nextButton.getWidth() - padding,
                (worldHeight - nextButton.getHeight()) / 2f
            );


            nextButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    currentLevelNumber++;
                    reloadUI();
                }
            });

            stage.addActor(nextButton);
        }

        // caricamento pulsante di inizio
        Button.ButtonStyle startBtnStyle=new Button.ButtonStyle();
        startBtnStyle.up=new TextureRegionDrawable(levelBtnTexture);
        startBtnStyle.over=new TextureRegionDrawable(levelBtnTextureHover);
        Button startBtn = new Button(startBtnStyle);
        startBtn.setSize(startBtn.getWidth() * 8f, startBtn.getHeight() * 8f);
        startBtn.setPosition(
            (worldWidth  - startBtn.getWidth())  / 2f,
            (worldHeight - startBtn.getHeight()) / 2f
        );


        startBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, currentLevelNumber));
            }
        });

        stage.addActor(startBtn);
    }
}
