package com.busecarik.spaceshooter;

public class Player extends BitmapEntity {
    static final String TAG = "Player";

    int _health = Config.PLAYER_HEALTH;

    Player() {
        super();
        loadBitmap(R.drawable.player_ship, Config.PLAYER_HEIGHT);
        respawn();
    }

    @Override
    void respawn() {
        _x = Config.STARTING_POSITION;
        _health = Config.STARTING_HEALTH;
        _velX = 0f;
        _velY = 0f;
    }

    @Override
    void update() {
        _velX *= Config.DRAG;
        _velY += Config.GRAVITY;
        if (_game._isBoosting) {
            _velX *= Config.ACC;
            _velY += Config.LIFT;
        }
        _velX = Utils.clamp(_velX, Config.MIN_VEL, Config.MAX_VEL);
        _velY = Utils.clamp(_velY, -Config.MAX_VEL, Config.MAX_VEL);
        _y += _velY;
        _y = Utils.clamp(_y, 0, Config.STAGE_HEIGHT - _height);
        _game._playerSpeed = _velX;
    }

    void onCollision(Entity that) {
        _health--;
    }
}
