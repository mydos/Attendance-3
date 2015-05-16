package com.sust.attendence.Call;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.sust.attendence.R;
import com.sust.attendence.Session.UserSessionManager;

public class CallActivity extends Activity implements OnTouchListener {
    ImageView swiper_iv;
    String drawable[] = {"one", "two", "three"};
    int i = 0;
    private UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        session = new UserSessionManager(this);
        if (session.checkLogin())
            finish();
        swiper_iv = (ImageView) findViewById(R.id.swiper_iv);
        swiper_iv.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
                Toast.makeText(CallActivity.this, "top", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeRight() {
                if (i >= 2)
                    i = 2;
                if (i < 2 && i >= 0) {
                    swiper_iv.setImageResource(getResources().getIdentifier(
                            drawable[++i], "drawable", getPackageName()));
                }
                Toast.makeText(CallActivity.this, "right", Toast.LENGTH_SHORT)
                        .show();
            }

            public void onSwipeLeft() {
                if (i < 0)
                    i = 0;
                if (i <= 2 && i > 0) {
                    swiper_iv.setImageResource(getResources().getIdentifier(
                            drawable[--i], "drawable", getPackageName()));
                }
                Toast.makeText(CallActivity.this, "left", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeBottom() {
                Toast.makeText(CallActivity.this, "bottom", Toast.LENGTH_SHORT)
                        .show();
            }

            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        return false;
    }

}

class OnSwipeTouchListener implements OnTouchListener {

    protected final GestureDetector gestureDetector;

    public OnSwipeTouchListener(Context ctx) {
        gestureDetector = new GestureDetector(ctx, new GestureListener());
    }

    private final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD
                            && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                    result = true;
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD
                        && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                }
                result = true;

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        return false;
    }
}