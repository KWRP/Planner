package com.kwrp.planner_gui;


import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * Defines what touch action consitutes as a swipe, and performs the appropriate
 * action in response when bound to something.
 *
 * @author https://stackoverflow.com/users/177776/mirek-rusin
 *         https://stackoverflow.com/questions/4139288/android-how-to-handle-right-to-left-swipe-gestures
 */
public class OnSwipeTouchListener implements OnTouchListener {

    /**
     * An instance of GestureDetector
     */
    private final GestureDetector gestureDetector;

    /**
     * Action to undertake when the action happens
     *
     * @param ctx the context the event is read from
     */
    public OnSwipeTouchListener(Context ctx) {
        gestureDetector = new GestureDetector(ctx, new GestureListener());
    }

    /**
     * When the view receives a touch event
     *
     * @param v     the view where the touch event took place
     * @param event motion event details
     * @return gestureDetector
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }


    /**
     * Defines swipe conditions
     */
    private final class GestureListener extends SimpleOnGestureListener {

        /**
         * The swipe distance threshold
         */
        private static final int SWIPE_THRESHOLD = 100;

        /**
         * The swipe speed threshold
         */
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;


        /**
         * Defines whether or not the motion can be considered a swipe and
         * calls the appropriate action if it is.
         *
         * @param e1        first motion event to take place
         * @param e2        second motion event to take place
         * @param velocityX velocity of the movement on the X-axis
         * @param velocityY velocity of the movement on the Y-axis
         * @return true
         */
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }
}