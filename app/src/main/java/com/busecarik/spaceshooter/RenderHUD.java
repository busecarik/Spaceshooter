package com.busecarik.spaceshooter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

public class RenderHUD{
    Context _context = null;
    private Paint _paint = null;
    private Typeface _typeface = null;

    RenderHUD(Context context) {
        _context = context;
        _paint = new Paint();
        //next line is a borrowing code
        _typeface = Typeface.createFromAsset(context.getAssets(), "fonts/theme.ttf");
        _paint.setColor(Color.WHITE);
        _paint.setTextAlign(Paint.Align.LEFT);
        _paint.setTextSize(Config.TEXT_SIZE_HUD);
        //next line is a borrowing code
        _paint.setTypeface(_typeface);
    }

    void isContinue(final Canvas canvas, final int playerHealth, final int distance) {
        canvas.drawText(_context.getString(R.string.playerHealth, playerHealth) , 10, Config.TEXT_SIZE_HUD, _paint);
        canvas.drawText(_context.getString(R.string.distanceTravelled, distance), 10, Config.TEXT_SIZE_HUD *2, _paint);
    }

    void isGameOver(Canvas canvas) {
        canvas.drawText(_context.getString(R.string.game_over), Config.CENTER_X, Config.CENTER_Y, _paint);
        canvas.drawText(_context.getString(R.string.restart), Config.CENTER_X, Config.CENTER_Y + Config.TEXT_SIZE_HUD, _paint);
    }
}