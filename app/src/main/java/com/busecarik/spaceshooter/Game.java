package com.busecarik.spaceshooter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;


public class Game extends SurfaceView implements Runnable {

    public static final String TAG = "Game";

    private Thread _gameThread = null;
    private volatile boolean _isRunning = false;
    private SurfaceHolder _holder = null;
    private Paint _paint;
    private Canvas _canvas;

    private ArrayList<Entity> _stars = new ArrayList<>();
    private ArrayList<Entity> _enemies = new ArrayList<>();
    private Player _player = null;
    private ChallengingEnemy _challengingEnemies = null;
    Random _rng = new Random();
    private Jukebox _jukebox = null;
    private SharedPreferences _prefs = null;
    private SharedPreferences.Editor _editor = null;
    private RenderHUD _hud = null;

    volatile boolean _isBoosting = false;
    int _distanceTraveled = 0;
    int _maxDistanceTraveled = 0;
    float _playerSpeed = 0f;
    private boolean _gameOver = true;
    private boolean _collision = false;
    private long _timer = System.currentTimeMillis();

    public Game(Context context) {
        super(context);

        Entity._game = this;
        _holder = getHolder();
        _holder.setFixedSize(Config.STAGE_WIDTH, Config.STAGE_HEIGHT);
        _paint = new Paint();
        _jukebox = new Jukebox(context);
        _hud = new RenderHUD(context);
        _prefs = context.getSharedPreferences(Config.PREFS, Context.MODE_PRIVATE);
        _editor = _prefs.edit();
        for (int i = 0; i <Config.STAR_COUNT; i++) {
            _stars.add(new Star());
        }
        for (int i = 0; i <Config.ENEMY_COUNT; i++) {
            _enemies.add(new Enemy());
        }
        _challengingEnemies = new ChallengingEnemy();
        _player = new Player();
        restart();
    }

    private void restart() {
        _collision = false;
        onGameEvent(Jukebox.GameEvent.StartGame);
        for(Entity e : _stars) {
            e.respawn();
        }
        for(Entity e : _enemies) {
            e.respawn();
        }
        _challengingEnemies.respawn();
        _player.respawn();
        _distanceTraveled = 0;
        _maxDistanceTraveled = _prefs.getInt(Config.LONGEST_DIST, 0);
        _gameOver = false;
    }

    @Override
    public void run() {
        onGameEvent(Jukebox.GameEvent.StartGame);
        while (_isRunning) {
            update();
            render();
        }
    }

    private void update() {
        if (_gameOver) { return; }
        _player.update();
        for(Entity e : _stars) {
            e.update();
        }
        for(Entity e : _enemies) {
            e.update();
        }
        _challengingEnemies.update();
        _distanceTraveled += _playerSpeed;

        checkCollisions();
        checkGameOver();
    }

    private void checkGameOver() {
        if (_player._health < 1) {
            _gameOver = true;
            _collision = false;
            if (_distanceTraveled > _maxDistanceTraveled) {
                _maxDistanceTraveled = _distanceTraveled;
                _editor.putInt(Config.LONGEST_DIST, _maxDistanceTraveled);
                _editor.apply();
            }
            onGameEvent(Jukebox.GameEvent.GameOver);
        }
    }

    private void colliding(Entity that) {
        _timer = System.currentTimeMillis();
        _player.onCollision(that);
        that.onCollision(_player);
        onGameEvent(Jukebox.GameEvent.Crash);
        _collision = true;
    }

    private void checkCollisions() {
        if (System.currentTimeMillis() >= _timer + Config.COOL_DOWN) {
            Entity temp = null;
            for (int i = 0; i < _enemies.size(); i++) {
                temp = _enemies.get(i);
                if (_player.isColliding(temp)) {
                    colliding(temp);
                }
            }
            if (_player.isColliding(_challengingEnemies)) {
                colliding(_challengingEnemies);
            }
        }
    }

    public void onGameEvent(final Jukebox.GameEvent event) {
        _jukebox.play(event);
    }

    private void checkTimer() {
        if (System.currentTimeMillis() >= _timer + Config.COOL_DOWN && _collision) {
            _collision = false;
        }
    }

    private void setAlphaValue() {
        checkTimer();
        if (_collision) {
            _paint.setAlpha(Config.TRANSPARENT_VALUE);
        } else {
            _paint.setAlpha(Config.ALPHA_VALUE);
        }
    }

    private void render() {
        if (!acquireAndLockCanvas())
            return;

        _canvas.drawColor(Color.BLACK);
        for(Entity e : _stars) {
            e.render(_canvas, _paint);
        }
        for(Entity e : _enemies) {
            e.render(_canvas, _paint);
        }
        _challengingEnemies.render(_canvas, _paint);
        setAlphaValue();
        _player.render(_canvas, _paint);
        if (!_gameOver) {
            _hud.isContinue(_canvas, _player._health, _distanceTraveled);
        } else {
            _hud.isGameOver(_canvas);
        }
        _holder.unlockCanvasAndPost(_canvas);
    }

    private boolean acquireAndLockCanvas() {
        if(!_holder.getSurface().isValid()) {
            return false;
        }
        _canvas = _holder.lockCanvas();
        return (_canvas != null);
    }

    protected void onPause() {
        Log.d(TAG, "onPause");
        _isRunning = false;
        try {
            _gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d(TAG, Log.getStackTraceString(e.getCause()));
        }
    }

    protected void onResume() {
        Log.d(TAG, "onResume");
        _isRunning = true;
        _gameThread = new Thread(this);
        _gameThread.start();
    }

    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        _gameThread = null;
        for (Entity e : _stars) {
            e.destroy();
        }
        for (Entity e : _enemies) {
            e.destroy();
        }
        _jukebox.unloadSoundEffects();
        Entity._game = null;
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        switch(event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_UP: //finger lifted
                _isBoosting = false;
                if (_gameOver) {
                    onGameEvent(Jukebox.GameEvent.Boost);
                    restart();
                }
                break;
            case MotionEvent.ACTION_DOWN: //finger pressed
                _isBoosting = true;
                break;
        }
        return true;
    }

}
