package top.nones.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class TaichiView extends View implements View.OnClickListener {
    private Paint whitePaint;
    private Paint blackPaint;
    private RectF rectF;

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
        blackPaint.setColor(0xFF333333);
        blackPaint.setStyle(Paint.Style.FILL);

        rectF = new RectF();
        setClickable(true);
        setOnClickListener(this);
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

        canvas.drawColor(0xFFFFFFFF);

        // 背景圆
        canvas.drawCircle(centerX, centerY, radius, blackPaint);

        // 右半白
        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        canvas.drawArc(rectF, 270, 180, true, whitePaint);

        // 上大白圆、下大黑圆
        float bigRadius = radius / 2;
        canvas.drawCircle(centerX, centerY - bigRadius, bigRadius, whitePaint);
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
