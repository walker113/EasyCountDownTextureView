package com.camnter.easycountdowntextureview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.TextureView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Description：EasyCountDownTextureView
 * Created by：CaMnter
 * Time：2016-03-16 13:45
 */
public class EasyCountDownTextureView extends TextureView implements TextureView.SurfaceTextureListener {

    private static final String LESS_THAN_TEN_FORMAT = "%02d";

    private static final int DEFAULT_COLOR_BACKGROUND = 0xff000000;
    private static final int DEFAULT_COLOR_TIME = 0xffffffff;
    private DisplayMetrics mMetrics;

    private static final int COUNT_DOWN_INTERVAL = 1000;

    private long mMillisInFuture = 1000 * 60 * 6L + 1000 * 60 * 60 * 6L + 1000 * 30L;

    /**************
     * Default dp *
     **************/
    private static final float DEFAULT_BACKGROUND_PAINT_WIDTH = 0.66f;
    private static final float DEFAULT_TIME_PAINT_WIDTH = 0.77f;
    private static final float DEFAULT_ROUND_RECT_RADIUS = 2.66f;
    private static final float DEFAULT_RECT_WIDTH = 18.0f;
    private static final float DEFAULT_RECT_HEIGHT = 17.0f;
    private static final float DEFAULT_RECT_SPACING = 6.0f;

    /**************
     * Default px *
     **************/
    private float rectWidth;
    private float rectHeight;
    private float rectSpacing;
    private float firstTranslateX;
    private float secondTranslateX;
    private float roundRectRadius;

    private EasyThread mThread;

    private final Locale locale = Locale.getDefault();
    private final Calendar mCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+00:00"));

    private Paint timePaint;
    private float timePaintBaseLine;
    private Paint.FontMetricsInt timePaintFontMetrics;

    private Paint backgroundPaint;
    private RectF backgroundRectF;

    public EasyCountDownTextureView(Context context) {
        super(context);
        this.init();
    }

    public EasyCountDownTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public EasyCountDownTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EasyCountDownTextureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init();
    }

    private void init() {
        this.mMetrics = this.getResources().getDisplayMetrics();
        this.setTime(this.mMillisInFuture);

        this.setSurfaceTextureListener(this);
        this.setOpaque(false);

        this.initBackgroundPaint();
        this.initBackgroundAttribute();
        this.initTimePaint();

        this.roundRectRadius = this.dp2px(DEFAULT_ROUND_RECT_RADIUS);
    }

    private void initBackgroundPaint() {
        this.backgroundPaint = new Paint();
        this.backgroundPaint.setAntiAlias(true);
        this.backgroundPaint.setColor(DEFAULT_COLOR_BACKGROUND);
        this.backgroundPaint.setStyle(Paint.Style.FILL);
        this.backgroundPaint.setStrokeWidth(this.dp2px(DEFAULT_BACKGROUND_PAINT_WIDTH));
        this.backgroundPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    private void initTimePaint() {
        this.timePaint = new Paint();
        this.timePaint.setAntiAlias(true);
        this.timePaint.setColor(DEFAULT_COLOR_TIME);
        this.timePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.timePaint.setStrokeWidth(this.dp2px(DEFAULT_TIME_PAINT_WIDTH));
        this.timePaint.setTextSize(this.sp2px(13));
        this.timePaint.setTextAlign(Paint.Align.CENTER);
        this.timePaint.setStrokeCap(Paint.Cap.ROUND);
        this.timePaintFontMetrics = this.timePaint.getFontMetricsInt();
        this.timePaintBaseLine = (this.backgroundRectF.bottom + this.backgroundRectF.top - this.timePaintFontMetrics.bottom - this.timePaintFontMetrics.top) / 2;
    }

    private void initBackgroundAttribute() {
        this.rectWidth = this.dp2px(DEFAULT_RECT_WIDTH);
        this.rectHeight = this.dp2px(DEFAULT_RECT_HEIGHT);
        this.rectSpacing = this.dp2px(DEFAULT_RECT_SPACING);
        this.firstTranslateX = this.rectWidth + this.rectSpacing;
        this.secondTranslateX = this.rectWidth * 2 + this.rectSpacing * 2;
        this.backgroundRectF = new RectF(0, 0, this.rectWidth, this.rectHeight);
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if (this.mThread == null) {
            this.mThread = new EasyThread();
            this.mThread.start();
        } else {
            if (mMillisInFuture > 0) {
                this.mThread.running = true;
            }
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }


    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if (this.mThread != null) {
            this.mThread.stopThread();
        }
        return true;
    }


    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    /**
     * Start count down by date
     *
     * @param date date
     */
    public void setTime(Date date) {
    }

    /**
     * Start count down by timeMillis
     *
     * @param timeMillis timeMillis
     */
    public void setTime(long timeMillis) {
        this.mMillisInFuture = timeMillis;
        this.mCalendar.setTimeInMillis(this.mMillisInFuture);
    }

    private class EasyThread extends Thread {

        public volatile boolean running = false;
        public volatile boolean completed = false;

        public EasyThread() {
            this.running = true;
        }

        public synchronized final void stopThread() {
            System.out.println("stopThread");
            this.running = false;
        }

        @Override
        public void run() {
            System.out.println("Run");
            long deltaTime;
            long tickTime = 0;
            while (!completed) {
                while (running) {
                    Canvas canvas = null;
                    try {
                        synchronized (EasyCountDownTextureView.this) {
                            canvas = EasyCountDownTextureView.this.lockCanvas();
                            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                            this.drawTimeAndBackground(
                                    canvas,
                                    String.format(locale, LESS_THAN_TEN_FORMAT, mCalendar.get(Calendar.HOUR_OF_DAY)),
                                    String.format(locale, LESS_THAN_TEN_FORMAT, mCalendar.get(Calendar.MINUTE)),
                                    String.format(locale, LESS_THAN_TEN_FORMAT, mCalendar.get(Calendar.SECOND))
                            );
                            // refresh time
                            mMillisInFuture -= 1000;
                            mCalendar.setTimeInMillis(mMillisInFuture);

                            if (mMillisInFuture < 0) {
                                this.completed = true;
                                this.running = false;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            EasyCountDownTextureView.this.unlockCanvasAndPost(canvas);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    deltaTime = SystemClock.uptimeMillis() - tickTime;
                    if (deltaTime < COUNT_DOWN_INTERVAL) {
                        try {
                            Thread.sleep(COUNT_DOWN_INTERVAL - deltaTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    tickTime = SystemClock.uptimeMillis();
                }
            }
            System.out.println("Run end");
        }

        private void drawTimeAndBackground(Canvas canvas, String hour, String minute, String second) {

            canvas.drawRoundRect(backgroundRectF, roundRectRadius, roundRectRadius, backgroundPaint);
            canvas.drawText(hour, backgroundRectF.centerX(), timePaintBaseLine, timePaint);

            canvas.save();
            canvas.translate(firstTranslateX, 0);
            canvas.drawRoundRect(backgroundRectF, roundRectRadius, roundRectRadius, backgroundPaint);
            canvas.drawText(minute, backgroundRectF.centerX(), timePaintBaseLine, timePaint);
            canvas.restore();

            canvas.save();
            canvas.translate(secondTranslateX, 0);
            canvas.drawRoundRect(backgroundRectF, roundRectRadius, roundRectRadius, backgroundPaint);
            canvas.drawText(second, backgroundRectF.centerX(), timePaintBaseLine, timePaint);
            canvas.restore();
        }
    }

    /**
     * Dp to px
     *
     * @param dp dp
     * @return px
     */
    private float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, this.mMetrics);
    }

    private float sp2px(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, this.mMetrics);
    }

}
