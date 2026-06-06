package top.nones.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class TaichiView extends View implements View.OnClickListener {
    private Paint whitePaint;
    private Paint blackPaint;
    private Paint shadowPaint;
    private Paint borderPaint;
    private RectF rectF;
    private float rotationAngle = 0;

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

        // 启用软件渲染以支持阴影
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    public void onClick(View v) {
        // index 范围 0-63，对应64卦
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

        // 绘制背景圆（黑色）
        canvas.drawCircle(centerX, centerY, radius, blackPaint);

        // 右半白（从270度开始，画180度的白色半圆）
        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        canvas.drawArc(rectF, 270, 180, true, whitePaint);

        // 上大白圆
        float bigRadius = radius / 2;
        canvas.drawCircle(centerX, centerY - bigRadius, bigRadius, whitePaint);

        // 下大黑圆
        canvas.drawCircle(centerX, centerY + bigRadius, bigRadius, blackPaint);

        // 鱼眼
        float smallRadius = radius / 6;
        canvas.drawCircle(centerX, centerY - bigRadius, smallRadius, blackPaint);
        canvas.drawCircle(centerX, centerY + bigRadius, smallRadius, whitePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(size, size);
    }
}
