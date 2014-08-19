package com.mnt.candiez.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

public class ImageUtils {
	public static Bitmap getRoundedCornerBitmap(Bitmap input) {
		Bitmap bitmap = input;

		Bitmap bitmapRounded = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
		Canvas canvas = new Canvas(bitmapRounded);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
		canvas.drawRoundRect((new RectF(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight())), 40, 40, paint);
		return bitmapRounded;
	}
}
