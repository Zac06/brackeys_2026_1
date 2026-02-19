package it.sleepdeprived.brackeys_2026_1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class UnifiedColorClearer {
    private static float red=0;
    private static float green=0;
    private static float blue=0;
    private static float alpha=0;

    public static void setColor(int intRed, int intGreen, int intBlue, float floatAlpha) {
        red=intRed/255.0f;
        green=intGreen/255.0f;
        blue=intBlue/255.0f;
        alpha=floatAlpha;
    }

    public static void clear(){
        Gdx.gl.glClearColor(red, green, blue, alpha);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

}
