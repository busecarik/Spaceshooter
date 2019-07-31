package com.busecarik.spaceshooter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;

public class Jukebox {
    public enum GameEvent {
        Boost,
        GameOver,
        Crash,
        StartGame
    }

    private static final String TAG = "Jukebox";

    private SoundPool _soundPool = null;
    public boolean _sfxEnabled = true;
    private HashMap<GameEvent, Integer> _sfxMap = null;
    private Context _context = null;

    public Jukebox(final Context context) {
        _context = context;
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(_context);
        _sfxEnabled = prefs.getBoolean(Config.SOUNDS_PREF_KEY, true);
        loadIfNeeded();
    }

    public void play(final GameEvent event){
        if(!_sfxEnabled || _soundPool == null){ return; }
        final float leftVolume = Config.DEFAULT_VOLUME;
        final float rightVolume = Config.DEFAULT_VOLUME;
        final int priority = 1;
        final int loop = 0; //-1 loop forever, 0 play once
        final float rate = 1.0f;
        final Integer soundID = _sfxMap.get(event);
        if(soundID != null){
            _soundPool.play(soundID, leftVolume, rightVolume, priority, loop, rate);
        }
    }

    public void unloadSoundEffects(){
        if(_soundPool != null){
            _soundPool.release();
            _soundPool = null;
        }
    }

    private void loadIfNeeded(){
        if(_sfxEnabled){
            loadSoundEffects();
        }
    }

    private void loadSoundEffects(){
        createSoundPool();
        _sfxMap = new HashMap<>();
        loadEventSound(GameEvent.Crash, Config.CRASH);
        loadEventSound(GameEvent.Boost, Config.BOOST);
        loadEventSound(GameEvent.GameOver, Config.GAME_OVER);
        loadEventSound(GameEvent.StartGame, Config.START_GAME);
    }

    private void loadEventSound(final GameEvent event, final String fileName){
        try {
            final AssetFileDescriptor afd = _context.getAssets().openFd(fileName);
            final int soundId = _soundPool.load(afd, 1);
            _sfxMap.put(event, soundId);
        }catch(IOException e){
            Log.e(TAG, "loadEventSound: error loading sound " + e.toString());
        }
    }


    @SuppressWarnings("deprecation")
    private void createSoundPool() {
        final AudioAttributes attr = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        _soundPool = new SoundPool.Builder()
                .setAudioAttributes(attr)
                .setMaxStreams(Config.MAX_STREAMS)
                .build();
    }
}