package top.nones.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CoinView extends View {
    private Paint paint;
    private boolean isYang = true;
    private float rotation = 0f;
    private float scale = 1f;
    private float translateY = 0f;
    private float alpha = 1f;
    
    public CoinView(Context context) {
        super(context);
        init();
    }
    
    public CoinView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
    }
    
    public void setYang(boolean yang) {
        this.isYang = yang;
        invalidate();
    }
    
    public void setRotation(float rotation) {
        this.rotation = rotation;
        invalidate();
    }
    
    public void setScale(float scale) {
        this.scale = scale;
        invalidate();
    }
    
    public void setTranslateY(float translateY) {
        this.translateY = translateY;
        invalidate();
    }
    
    public void setAlpha(float alpha) {
        this.alpha = alpha;
        invalidate();
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;
        
        canvas.save();
        canvas.translate(centerX, centerY);
        canvas.translate(0, translateY);
        canvas.scale(scale, scale);
        canvas.rotate(rotation);
        
        float radius = Math.min(width, height) / 2 - 4;
        
        drawCoin(canvas, radius);
        
        canvas.restore();
    }
    
    private void drawCoin(Canvas canvas, float radius) {
        paint.setStyle(Paint.Style.FILL);
        
        paint.setColor(0xFF9A7B2E);
        canvas.drawCircle(0, 0, radius, paint);
        
        paint.setColor(0xFFC9A45C);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(radius * 0.08f);
        canvas.drawCircle(0, 0, radius * 0.9f, paint);
        
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xFF9A7B2E);
        canvas.drawCircle(0, 0, radius, paint);
        
        paint.setColor(0xFFF5F0E8);
        canvas.drawCircle(0, 0, radius * 0.35f, paint);
        
        paint.setColor(0xFF9A7B2E);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(radius * 0.05f);
        canvas.drawCircle(0, 0, radius * 0.4f, paint);
        
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xFF6B4E2A);
        paint.setAlpha((int)(alpha * 255));
        if (isYang) {
            drawYangFace(canvas, radius);
        } else {
            drawYinFace(canvas, radius);
        }
        paint.setAlpha(255);
    }
    
    private void drawYangFace(Canvas canvas, float radius) {
        paint.setColor(0xFF181B24);
        paint.setTextSize(radius * 0.25f);
        paint.setTextAlign(Paint.Align.CENTER);
        
        canvas.drawText("开", 0, -radius * 0.25f, paint);
        
        paint.setTextSize(radius * 0.2f);
        canvas.drawText("元", -radius * 0.2f, radius * 0.1f, paint);
        canvas.drawText("通", radius * 0.2f, radius * 0.1f, paint);
        
        paint.setTextSize(radius * 0.25f);
        canvas.drawText("宝", 0, radius * 0.35f, paint);
    }
    
    private void drawYinFace(Canvas canvas, float radius) {
        paint.setColor(0xFF2D3345);
        paint.setTextSize(radius * 0.18f);
        paint.setTextAlign(Paint.Align.CENTER);
        
        canvas.drawText("背", 0, 0, paint);
        
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        
        float patternRadius = radius * 0.5f;
        for (int i = 0; i < 8; i++) {
            float angle = (float) (Math.PI * 2 * i / 8);
            float x = (float) Math.cos(angle) * patternRadius;
            float y = (float) Math.sin(angle) * patternRadius;
            canvas.drawCircle(x, y, radius * 0.05f, paint);
        }
        
        paint.setStyle(Paint.Style.FILL);
    }
}