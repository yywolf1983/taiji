package top.nones.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

public class TaichiView extends View implements View.OnClickListener {
    private Paint whitePaint;
    private Paint blackPaint;
    private Paint shadowPaint;
    private Paint borderPaint;
    private RectF rectF;
    private float rotationAngle = 0;
    private Handler handler;
    private boolean isAnimating = true;
    private static final long ANIMATION_DELAY = 16;
    private static final float ROTATION_SPEED = 0f;

    public TaichiView(Context context) {
        super(context);
        init();
    }

    public TaichiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        whitePaint.setColor(0xFFFFFFFF);
        whitePaint.setStyle(Paint.Style.FILL);

        blackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        blackPaint.setColor(0xFF1A1A1A);
        blackPaint.setStyle(Paint.Style.FILL);

        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadowPaint.setColor(0x20000000);
        shadowPaint.setStyle(Paint.Style.FILL);

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setColor(0xFFE0E0E0);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(2f);

        rectF = new RectF();
        setClickable(true);
        setOnClickListener(this);

        handler = new Handler(Looper.getMainLooper());
        startRotationAnimation();

        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    private void startRotationAnimation() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (isAnimating) {
                    rotationAngle += ROTATION_SPEED;
                    if (rotationAngle >= 360) {
                        rotationAngle -= 360;
                    }
                    invalidate();
                    handler.postDelayed(this, ANIMATION_DELAY);
                }
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAnimating = true;
        startRotationAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAnimating = false;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onClick(View v) {
        int hexagramNumber = (int) (Math.random() * 64);
        HexagramDetailActivity.start(getContext(), hexagramNumber);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int size = Math.min(width, height);
        float radius = size * 0.45f;
        float centerX = width / 2f;
        float centerY = height / 2f;

        canvas.save();
        canvas.rotate(rotationAngle, centerX, centerY);

        canvas.drawCircle(centerX, centerY, radius, blackPaint);

        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        canvas.drawArc(rectF, 270, 180, true, whitePaint);

        float bigRadius = radius / 2;
        canvas.drawCircle(centerX, centerY - bigRadius, bigRadius, whitePaint);

        canvas.drawCircle(centerX, centerY + bigRadius, bigRadius, blackPaint);

        float smallRadius = radius / 6;
        canvas.drawCircle(centerX, centerY - bigRadius, smallRadius, blackPaint);
        canvas.drawCircle(centerX, centerY + bigRadius, smallRadius, whitePaint);

        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(size, size);
    }
}