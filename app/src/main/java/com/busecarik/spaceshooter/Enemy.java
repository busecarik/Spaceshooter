package com.busecarik.spaceshooter;

public class Enemy extends BitmapEntity {

    Enemy() {
        int resID = R.drawable.tm_1;
        switch (_game._rng.nextInt(4)) {
            case 0:
                resID = R.drawable.tm_1;
                break;
            case 1:
                resID = R.drawable.tm_2;
                break;
            case 2:
                resID = R.drawable.tm_3;
                break;
            case 3:
                resID = R.drawable.tm_4;
                break;
        }
        loadBitmap(resID, Config.ENEMY_HEIGHT);
        _bitmap = Utils.flipBitmap(_bitmap, false);
        respawn();
    }

    @Override
    void respawn() {
       _x = Config.STAGE_WIDTH + _game._rng.nextInt(Config.ENEMY_SPAWN_OFFSET);
       _y = _game._rng.nextInt(Config.STAGE_HEIGHT - Config.ENEMY_HEIGHT);
    }

    @Override
    void update() {
        if (_game._distanceTraveled > Config.ENEMY_APPEARANCE) {
            _velX = -_game._playerSpeed;
            _x += _velX;
            if (right() < 0) {
                _x = Config.STAGE_WIDTH + _game._rng.nextInt(Config.ENEMY_SPAWN_OFFSET);
            }
        }
    }

    @Override
    void onCollision(Entity that) {
        _x = Config.STAGE_WIDTH + _game._rng.nextInt(Config.ENEMY_SPAWN_OFFSET);
    }
}
