package it.sleepdeprived.brackeys_2026_1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class Font {
    private BitmapFont buttonFont;
    private BitmapFont creditFont;
    private BitmapFont bigFont;

    // #0d171f as a libgdx Color (r, g, b, a) normalized to 0-1
    private static final Color DARK_BLUE = new Color(0x0d / 255f, 0x17 / 255f, 0x1f / 255f, 1f);

    public Font() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("bytebounce.ttf"));

        FreeTypeFontParameter buttonParams = new FreeTypeFontParameter();
        buttonParams.size = 48;
        buttonParams.color = DARK_BLUE;
        buttonFont = generator.generateFont(buttonParams);

        FreeTypeFontParameter creditParams = new FreeTypeFontParameter();
        creditParams.size = 24;
        creditParams.color = DARK_BLUE;
        creditFont = generator.generateFont(creditParams);

        FreeTypeFontParameter bigParams = new FreeTypeFontParameter();
        bigParams.size = 128;
        bigParams.color = DARK_BLUE;
        bigFont = generator.generateFont(bigParams);

        generator.dispose(); // dispose generator once all fonts are generated
    }

    public BitmapFont getButtonFont() {
        return buttonFont;
    }

    public BitmapFont getCreditFont() {
        return creditFont;
    }

    public  BitmapFont getBigFont() {
        return bigFont;
    }

    public void dispose() {
        buttonFont.dispose();
        creditFont.dispose();
    }
}
