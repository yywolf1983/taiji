package top.nones.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class YarrowView extends View {
    private Paint paint;
    private Paint highlightPaint;
    private Paint selectedPaint;
    private Paint bgPaint;
    
    private List<Stick> sticks = new ArrayList<>();
    private Random random = new Random();
    private Handler handler = new Handler();
    
    private int state = 0;
    private int totalSticks = 49;
    private int leftCount = 0;
    private int rightCount = 0;
    private int heldCount = 0;
    
    private AnimationCallback callback;
    
    private int viewWidth = 400;
    private int viewHeight = 400;
    
    public interface AnimationCallback {
        void onAnimationComplete(int state);
    }
    
    public void setCallback(AnimationCallback callback) {
        this.callback = callback;
    }
    
    public YarrowView(Context context) {
        super(context);
        init();
    }
    
    public YarrowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    private void init() {
        paint = new Paint();
        paint.setColor(0xFF8B6B3D);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);
        
        highlightPaint = new Paint();
        highlightPaint.setColor(0xFFC9A45C);
        highlightPaint.setStrokeWidth(6);
        highlightPaint.setAntiAlias(true);
        
        selectedPaint = new Paint();
        selectedPaint.setColor(0xFFD4915A);
        selectedPaint.setStrokeWidth(6);
        selectedPaint.setAntiAlias(true);
        
        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        
        resetSticks();
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        updateStickPositions();
    }
    
    private void updateStickPositions() {
        float centerX = viewWidth / 2f;
        float centerY = viewHeight / 2f;
        
        for (Stick s : sticks) {
            if (!s.held && !s.removed) {
                s.currentX = centerX;
                s.currentY = centerY;
                s.targetX = centerX;
                s.targetY = centerY;
            }
        }
    }
    
    public void resetSticks() {
        sticks.clear();
        state = 0;
        totalSticks = 49;
        leftCount = 0;
        rightCount = 0;
        heldCount = 0;
        
        for (int i = 0; i < 49; i++) {
            sticks.add(new Stick(i));
        }
        updateStickPositions();
        invalidate();
    }
    
    public void resetSticks(int count) {
        sticks.clear();
        state = 0;
        totalSticks = count;
        leftCount = 0;
        rightCount = 0;
        heldCount = 0;
        
        for (int i = 0; i < count; i++) {
            sticks.add(new Stick(i));
        }
        updateStickPositions();
        invalidate();
    }
    
    public int getTotalSticks() { return totalSticks; }
    public int getLeftCount() { return leftCount; }
    public int getRightCount() { return rightCount; }
    
    public void splitSticks(int left) {
        leftCount = left;
        rightCount = totalSticks - left;
        state = 1;
        
        float centerX = viewWidth / 2f;
        float centerY = viewHeight / 2f;
        float leftX = centerX - 100;
        float rightX = centerX + 100;
        
        for (int i = 0; i < sticks.size(); i++) {
            Stick s = sticks.get(i);
            if (i < left) {
                s.setTarget(leftX, centerY + (i % 15) * 12 - 80);
            } else {
                s.setTarget(rightX, centerY + ((i - left) % 15) * 12 - 80);
            }
            s.startMove();
        }
        
        startAnimationLoop();
    }
    
    public void takeOneFromRight() {
        state = 2;
        heldCount = 1;
        
        float centerX = viewWidth / 2f;
        float heldY = viewHeight - 100;
        
        for (int i = sticks.size() - 1; i >= 0; i--) {
            Stick s = sticks.get(i);
            if (!s.held && !s.removed) {
                s.held = true;
                s.selected = true;
                s.setTarget(centerX, heldY);
                s.startMove();
                break;
            }
        }
        
        startAnimationLoop();
    }
    
    public void countLeft(int remainder) {
        state = 3;
        
        int removed = leftCount - remainder;
        leftCount = remainder;
        
        float centerX = viewWidth / 2f;
        float removedY = viewHeight - 160;
        
        int count = 0;
        for (int i = 0; i < sticks.size(); i++) {
            Stick s = sticks.get(i);
            if (!s.held && !s.removed) {
                if (count < removed) {
                    s.removed = true;
                    s.setTarget(centerX + (count % 5 - 2) * 30, removedY);
                    s.startMove();
                    count++;
                }
            }
        }
        
        startAnimationLoop();
    }
    
    public void countRight(int remainder) {
        state = 4;
        
        int removed = rightCount - remainder;
        rightCount = remainder;
        
        float centerX = viewWidth / 2f;
        float removedY = viewHeight - 160;
        
        int count = 0;
        for (int i = 0; i < sticks.size(); i++) {
            Stick s = sticks.get(i);
            if (!s.held && !s.removed) {
                if (count < removed) {
                    s.removed = true;
                    s.setTarget(centerX + (count % 5 - 2) * 30, removedY);
                    s.startMove();
                    count++;
                }
            }
        }
        
        startAnimationLoop();
    }
    
    public void complete() {
        state = 5;
        
        for (Stick s : sticks) {
            if (s.held || s.removed) {
                s.opacity = 0;
            }
        }
        
        totalSticks = leftCount + rightCount;
        leftCount = 0;
        rightCount = 0;
        heldCount = 0;
        
        float centerX = viewWidth / 2f;
        float centerY = viewHeight / 2f;
        
        List<Stick> remaining = new ArrayList<>();
        for (Stick s : sticks) {
            if (!s.held && !s.removed) {
                s.held = false;
                s.removed = false;
                s.selected = false;
                s.opacity = 1f;
                s.setTarget(centerX, centerY);
                s.startMove();
                remaining.add(s);
            }
        }
        sticks = remaining;
        
        startAnimationLoop();
    }
    
    private void startAnimationLoop() {
        animateFrame();
    }
    
    private void animateFrame() {
        boolean needsUpdate = false;
        
        for (Stick s : sticks) {
            if (s.isMoving) {
                needsUpdate = true;
                s.update();
            }
        }
        
        if (needsUpdate) {
            invalidate();
            handler.postDelayed(this::animateFrame, 16);
        } else {
            if (callback != null) {
                callback.onAnimationComplete(state);
            }
        }
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        drawBackground(canvas);
        
        float centerX = viewWidth / 2f;
        float centerY = viewHeight / 2f;
        
        for (Stick s : sticks) {
            if (s.opacity <= 0) continue;
            
            Paint currentPaint = paint;
            if (s.selected) {
                currentPaint = selectedPaint;
            } else if (s.held) {
                currentPaint = highlightPaint;
            } else if (s.removed) {
                currentPaint = highlightPaint;
            }
            
            currentPaint.setAlpha((int)(s.opacity * 255));
            
            drawStick(canvas, s.currentX, s.currentY, currentPaint, s.scale);
        }
        
        paint.setAlpha(255);
        paint.setColor(0xFF6A6560);
        paint.setTextSize(16);
        paint.setTextAlign(Paint.Align.CENTER);
        
        canvas.drawText("左: " + leftCount, centerX - 100, centerY - 100, paint);
        canvas.drawText("右: " + rightCount, centerX + 100, centerY - 100, paint);
        canvas.drawText("夹: " + heldCount, centerX, viewHeight - 80, paint);
        
        String stateText = "";
        switch (state) {
            case 0: stateText = "准备"; break;
            case 1: stateText = "分两堆"; break;
            case 2: stateText = "取一"; break;
            case 3: stateText = "左数"; break;
            case 4: stateText = "右数"; break;
            case 5: stateText = "完成"; break;
        }
        canvas.drawText("状态: " + stateText, centerX, viewHeight - 25, paint);
    }
    
    private void drawBackground(Canvas canvas) {
        bgPaint.setColor(0xFF181B24);
        bgPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, viewWidth, viewHeight, bgPaint);
        
        bgPaint.setColor(0x208B6B3D);
        bgPaint.setStrokeWidth(2);
        
        float centerX = viewWidth / 2f;
        float centerY = viewHeight / 2f;
        
        canvas.drawLine(centerX, centerY - 120, centerX, centerY + 120, bgPaint);
        
        bgPaint.setColor(0x108B6B3D);
        canvas.drawRect(centerX - 150, centerY - 120, centerX - 50, centerY + 120, bgPaint);
        canvas.drawRect(centerX + 50, centerY - 120, centerX + 150, centerY + 120, bgPaint);
        
        bgPaint.setColor(0x15C9A45C);
        canvas.drawRect(centerX - 35, viewHeight - 120, centerX + 35, viewHeight - 80, bgPaint);
    }
    
    private void drawStick(Canvas canvas, float x, float y, Paint p, float scale) {
        float length = 65 * scale;
        float width = 5 * scale;
        
        canvas.save();
        canvas.translate(x, y);
        canvas.scale(scale, scale);
        
        p.setStrokeWidth(width);
        canvas.drawLine(0, -length / 2, 0, length / 2, p);
        
        p.setColor(0xFF5A3A1A);
        p.setStrokeWidth(width * 1.3f);
        canvas.drawPoint(0, -length / 2 + 7, p);
        canvas.drawPoint(0, length / 2 - 7, p);
        
        p.setColor(0xFF8B6B3D);
        p.setStrokeWidth(width * 0.8f);
        
        canvas.restore();
    }
    
    private class Stick {
        int id;
        float currentX;
        float currentY;
        float targetX;
        float targetY;
        
        boolean isMoving = false;
        long moveStartTime = 0;
        float scale = 1f;
        float opacity = 1f;
        
        boolean held = false;
        boolean removed = false;
        boolean selected = false;
        
        Stick(int id) {
            this.id = id;
            this.currentX = viewWidth / 2f;
            this.currentY = viewHeight / 2f;
            this.targetX = this.currentX;
            this.targetY = this.currentY;
        }
        
        void setTarget(float x, float y) {
            this.targetX = x;
            this.targetY = y;
        }
        
        void startMove() {
            isMoving = true;
            moveStartTime = System.currentTimeMillis();
            scale = 1f;
        }
        
        void update() {
            long now = System.currentTimeMillis();
            float elapsed = (now - moveStartTime) / 500f;
            float progress = Math.min(1f, elapsed);
            
            float easeProgress = easeOutCubic(progress);
            
            currentX += (targetX - currentX) * easeProgress;
            currentY += (targetY - currentY) * easeProgress;
            
            if (held) {
                scale = 0.75f + (0.25f * progress);
            }
            
            if (removed && progress > 0.5f) {
                opacity = 1f - (progress - 0.5f) * 2f;
            }
            
            if (progress >= 1f) {
                isMoving = false;
                currentX = targetX;
                currentY = targetY;
            }
        }
        
        float easeOutCubic(float t) {
            return 1 - (float) Math.pow(1 - t, 3);
        }
    }
}