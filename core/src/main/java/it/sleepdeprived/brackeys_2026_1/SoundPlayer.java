package it.sleepdeprived.brackeys_2026_1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ObjectMap;

public class SoundPlayer {

    private static final ObjectMap<String, Sound> sounds = new ObjectMap<>();

    private SoundPlayer() {} // prevent instantiation

    // Load all sounds once
    public static void init() {
        sounds.put("walk", Gdx.audio.newSound(Gdx.files.internal("sounds/walk.wav")));
        sounds.put("notpass",  Gdx.audio.newSound(Gdx.files.internal("sounds/notpass.wav")));
        sounds.put("pass",  Gdx.audio.newSound(Gdx.files.internal("sounds/pass.wav")));
        sounds.put("select",  Gdx.audio.newSound(Gdx.files.internal("sounds/select.wav")));
        sounds.put("shoot", Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.wav")));
        sounds.put("kill",  Gdx.audio.newSound(Gdx.files.internal("sounds/kill.wav")));
    }

    public static void play(String name) {
        Sound sound = sounds.get(name);
        if (sound != null) {
            sound.play(1f);
        }
    }

    public static void dispose() {
        for (Sound sound : sounds.values()) {
            sound.dispose();
        }
        sounds.clear();
    }
}
