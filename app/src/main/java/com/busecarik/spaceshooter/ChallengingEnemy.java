package com.busecarik.spaceshooter;

public class ChallengingEnemy extends Enemy {

    private boolean _direction = false;

    ChallengingEnemy() {
        int resID = R.drawable.tm_5;
        loadBitmap(resID, Config.ENEMY_HEIGHT);
        _bitmap = Utils.flipBitmap(_bitmap, false);
            respawn();
    }

    @Override
    void respawn() {
            _x = Config.STAGE_WIDTH + _game._rng.nextInt(Config.CHAL_ENEMY_SPAWN_OFFSET);
    }

    @Override
    void update() {
        if (_game._distanceTraveled > Config.CHAL_ENEMY_APPEARANCE) {
            _velX = -(_game._playerSpeed + Config.X_VELOCITY);
            _x += _velX;
            _velY = Config.Y_VELOCITY;
            _direction = Utils.findDirection(_y, 0, Config.STAGE_HEIGHT - Config.ENEMY_HEIGHT, _direction);
            if (_direction) {
                _y += _velY;
            } else {
                _y -= _velY;
            }
            if (right() < 0) {
                _x = Config.STAGE_WIDTH + _game._rng.nextInt(Config.ENEMY_SPAWN_OFFSET);
            }
        }
    }

}
