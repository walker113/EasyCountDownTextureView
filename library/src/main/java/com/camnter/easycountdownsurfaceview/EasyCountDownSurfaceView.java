package com.camnter.easycountdownsurfaceview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.os.Build;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Description：EasyCountDownSurfaceView
 * Created by：CaMnter
 * Time：2016-03-15 19:49
 */
public class EasyCountDownSurfaceView extends SurfaceView implements SurfaceHolder.Callback2 {

    private static final String LESS_THAN_TEN_FORMAT = "%02d";
    private static final String MEASURE_TEXT_WIDTH_HEIGHT_SAMPLE = "06";

    private static final int DEFAULT_COLOR_BACKGROUND = 0xff000000;
    private static final int DEFAULT_COLOR_TIME = 0xffffffff;
    private DisplayMetrics mMetrics;

    private static final int COUNT_DOWN_INTERVAL = 100;

    private long mMillisInFuture = 1000 * 60 * 6L + 1000 * 60 * 60 * 6L + 1000 * 30L;

    /**************
     * Default dp *
     **************/
    private static final float DEFAULT_BACKGROUND_PAINT_WIDTH = 0.66f;
    private static final float DEFAULT_TIME_PAINT_WIDTH = 0.88f;
    private static final float DEFAULT_ROUND_RECT_RADIUS = 5.0f;
    private static final float DEFAULT_RECT_SIDE = 26.0f;
    private static final float DEFAULT_RECT_SPACING = 10.0f;


    /**************
     * Default px *
     **************/
    private float rectSide;
    private float rectSpacing;
    private float firstTranslateX;
    private float secondTranslateX;
    private float roundRectRadius;

    private SurfaceHolder mHolder;
    private EasyThread mThread;

    private final Locale locale = Locale.getDefault();
    private final Calendar mCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+00:00"));

    private Paint timePaint;
    private Paint backgroundPaint;

    private RectF backgroundRectF;

    private Paint.FontMetricsInt timePaintfontMetrics;
    private float timePaintBaseLine;

    public EasyCountDownSurfaceView(Context context) {
        super(context);
        this.init();
    }

    public EasyCountDownSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public EasyCountDownSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EasyCountDownSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init();
    }

    private void init() {
        this.mMetrics = this.getResources().getDisplayMetrics();
        this.setTime(this.mMillisInFuture);

        this.mHolder = this.getHolder();
        this.mHolder.addCallback(this);

        // set SurfaceView transparent
        this.setZOrderOnTop(true);
        this.mHolder.setFormat(PixelFormat.TRANSPARENT);

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
    }

    private void initTimePaint() {
        this.timePaint = new Paint();
        this.timePaint.setAntiAlias(true);
        this.timePaint.setColor(DEFAULT_COLOR_TIME);
        this.timePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.timePaint.setStrokeWidth(this.dp2px(DEFAULT_TIME_PAINT_WIDTH));
        this.timePaint.setTextSize(40);
        this.timePaint.setTextAlign(Paint.Align.CENTER);
        this.timePaintfontMetrics = this.timePaint.getFontMetricsInt();
        this.timePaintBaseLine = (this.backgroundRectF.bottom + this.backgroundRectF.top - this.timePaintfontMetrics.bottom - this.timePaintfontMetrics.top) / 2;
    }

    private void initBackgroundAttribute() {
        this.rectSide = this.dp2px(DEFAULT_RECT_SIDE);
        this.rectSpacing = this.dp2px(DEFAULT_RECT_SPACING);
        this.firstTranslateX = this.rectSide + this.rectSpacing;
        this.secondTranslateX = this.rectSide * 2 + this.rectSpacing * 2;
        this.backgroundRectF = new RectF(0, 0, this.rectSide, this.rectSide);
    }


    @Override
    public void surfaceRedrawNeeded(SurfaceHolder holder) {
        System.out.println("surfaceRedrawNeeded");
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        System.out.println("surfaceCreated");
        if (this.mThread == null) {
            this.mThread = new EasyThread();
            this.mThread.start();
        } else {
            if (mMillisInFuture > 0) {
                this.mThread.isRunning = true;
            }
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        System.out.println("surfaceChanged");
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        System.out.println("surfaceDestroyed");
        if (this.mThread != null) {
            this.mThread.stopThread();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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

        public volatile boolean isRunning = false;
        public volatile boolean isCompleted = false;

        public EasyThread() {
            this.isRunning = true;
        }

        public synchronized final void stopThread() {
            System.out.println("stopThread");
            this.isRunning = false;
        }

        @Override
        public void run() {
            System.out.println("Run");
            long deltaTime;
            long tickTime = 0;
            while (!isCompleted) {
                while (isRunning) {
                    Canvas canvas = null;
                    try {
                        synchronized (this) {
                            canvas = mHolder.lockCanvas();
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
                                this.isCompleted = true;
                                this.isRunning = false;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (mHolder != null)
                                mHolder.unlockCanvasAndPost(canvas);
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
            System.out.println("hour:" + hour + " minute:" + minute + " second:" + second);
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

}