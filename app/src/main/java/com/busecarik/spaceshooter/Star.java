package com.busecarik.spaceshooter;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Star extends Entity {

    private int _colorR;
    private int _colorG;
    private int _colorB ;
    private float _radius;

    Star() {
        _x = _game._rng.nextInt(Config.STAGE_WIDTH);
        _y = _game._rng.nextInt(Config.STAGE_HEIGHT);
        _colorR = _game._rng.nextInt(Config.COLOR_RANGE);
        _colorG = _game._rng.nextInt(Config.COLOR_RANGE);
        _colorB = _game._rng.nextInt(Config.COLOR_RANGE);
        _radius = _game._rng.nextInt(Config.RADIUS_VALUE) + Config.STAR_VALUE;
        _velX = -_radius;
        _width = _radius * Config.STAR_VALUE;
        _height = _radius * Config.STAR_VALUE;
    }

    @Override
    void update() {
        _velX = -_radius;
        _x += _velX;
        _x = Utils.wrap(_x, 0, Config.STAGE_WIDTH + _width);
    }

    @Override
    void render(Canvas canvas, Paint paint) {
        //next line is borrowing code
        paint.setARGB(255, _colorR, _colorG, _colorB);
        canvas.drawCircle(_x+_radius, _y+_radius, _radius, paint);
    }
}

