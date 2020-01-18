package com.android.bloga.draggableframelayout;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.view.WindowManager;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DraggableLayout extends FrameLayout{

    //touch
    private int previousFingerPosition = 0;
    private int commentsFrameLayoutPosition = 0;
    private int defaultViewHeight;
    private boolean isClosing = false;
    private boolean isScrollingUp = false;
    private boolean isScrollingDown = false;
    private boolean dragViewHandlingTouch = false;

    private boolean listViewScrollingEndedTop = true;
    private boolean listViewScrollingEndedBottom = false;

    private ListView commentsListView;
    AbsListView.OnScrollListener scrollListener;
    int lastFirstVisibleItem = 0;

    private DraggableLayoutListener dragLayoutListener = null;

    public void setDragLayoutListener(DraggableLayoutListener dragLayoutListener) {
        this.dragLayoutListener = dragLayoutListener;
    }


    public interface DraggableLayoutListener{
        public void onDialogClose();
    }

    public DraggableLayout(@NonNull Context context) {
        super(context, null);
    }

    public DraggableLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DraggableLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scrollListener = new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (view.getLastVisiblePosition() == view.getAdapter().getCount() -1 &&
                        view.getChildAt(view.getChildCount() - 1).getBottom() <= view.getHeight())
                {
                    //It is scrolled all the way down here
                    setListViewScrollingEndedBottom(true);
                    setListViewScrollingEndedTop(false);
                }
                else if(view.getFirstVisiblePosition() == 0 && view.getChildCount()>0 && lastFirstVisibleItem > firstVisibleItem) { //first reached
                    setListViewScrollingEndedTop(true);
                    setListViewScrollingEndedBottom(false);
                }
                else if(lastFirstVisibleItem != firstVisibleItem){
                    setListViewScrollingEndedTop(false);
                    setListViewScrollingEndedBottom(false);
                }
                lastFirstVisibleItem = firstVisibleItem;
            }
        };


    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        //pass touch event to frame layout if listview reached its end
        if (listViewScrollingEndedTop || listViewScrollingEndedBottom) {
            setDragViewHandlingTouch(true);
            onTouchEvent(event);
            return true;
        }
        setDragViewHandlingTouch(false);
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {


        final int Y = (int) event.getRawY();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                // save default base layout height
                defaultViewHeight = getHeight();

                // Init finger and view position
                previousFingerPosition = Y;
                commentsFrameLayoutPosition = (int) this.getY();
                return super.dispatchTouchEvent(event);
//                break;

            case MotionEvent.ACTION_UP:
                // If user was doing a scroll up
                onTouchEvent(event);
                break;
            case MotionEvent.ACTION_MOVE:
                int currentPosition = (int) this.getY();

                // If we scroll up
                if (previousFingerPosition > Y) {
                    // First time android rise an event for "up" move
                    if (!isScrollingUp) {
                        isScrollingUp = true;
                    }

                    if (commentsListView.getLastVisiblePosition() == commentsListView.getAdapter().getCount() - 1 || currentPosition >0) {
                       //pass touch event to Frame layout
                        if(commentsListView.getLastVisiblePosition() == commentsListView.getAdapter().getCount() - 1) {
                            setListViewScrollingEndedBottom(true);
                        }
                        onTouchEvent(event);
                    } else {
//                        pass touch event to listview
                        if(dragViewHandlingTouch == true) {
                            this.setY(0);
                            setListViewScrollingEndedTop(false);
                            setListViewScrollingEndedBottom(false);
                            commentsListView.onTouchEvent(event);

                            MotionEvent up = MotionEvent.obtain(event);
                            up.setAction(MotionEvent.ACTION_CANCEL);
                            super.dispatchTouchEvent(up);
                            up.recycle();
                            event.setAction(MotionEvent.ACTION_DOWN);
                            super.dispatchTouchEvent(event);

                        }
                    }
                }
                // If we scroll down
                else {

                    // First time android rise an event for "down" move
                    if (!isScrollingDown) {
                        isScrollingDown = true;
                    }

                    if (commentsListView.getFirstVisiblePosition() == 0 || currentPosition <0) {
                        //pass touch event to Frame layout
                        if(commentsListView.getLastVisiblePosition() == 0){
                            setListViewScrollingEndedTop(true);
                        }
                        onTouchEvent(event);
                    } else {
//                        pass touch event to listview
                        if(dragViewHandlingTouch == true) {
                            this.setY(0);
                            setListViewScrollingEndedBottom(false);
                            setListViewScrollingEndedTop(false);
                            commentsListView.onTouchEvent(event);

                            MotionEvent up = MotionEvent.obtain(event);
                            up.setAction(MotionEvent.ACTION_CANCEL);
                            super.dispatchTouchEvent(up);
                            up.recycle();
                            event.setAction(MotionEvent.ACTION_DOWN);
                        }
                    }
                }
                previousFingerPosition = Y;
                break;
        }
        return super.dispatchTouchEvent(event);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // Get finger position on screen
        final int Y = (int) event.getRawY();


        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                break;

            case MotionEvent.ACTION_UP:
                // If user was doing a scroll up
                int currentYPosition = (int)this.getY();
                if(isScrollingUp){
                    if ((commentsFrameLayoutPosition - currentYPosition) > defaultViewHeight / 4) {
                        closeUpAndDismissDialog(currentYPosition);
                        return true;
                    }else {
                        // Reset FrameLayout position
                        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(this, "y", currentYPosition, 0);
                        positionAnimator.setDuration(200);
                        positionAnimator.start();
                    }
                    // We are not in scrolling up mode anymore
                    isScrollingUp = false;
                }

                // If user was doing a scroll down
                if(isScrollingDown){

                    if (Math.abs(commentsFrameLayoutPosition - currentYPosition) > defaultViewHeight / 4)
                    {
                        closeDownAndDismissDialog(currentYPosition);
                        return true;
                    }else {
                        // Reset commentsFrameLayout position
                        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(this, "y", currentYPosition, 0);
                        positionAnimator.setDuration(200);
                        positionAnimator.start();

                        // Reset base layout size
                        getLayoutParams().height = defaultViewHeight;
                        requestLayout();
                    }
                    // We are not in scrolling down mode anymore
                    isScrollingDown = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(!isClosing){
                    // If we scroll up
                    if(previousFingerPosition >Y){
                        this.setY(this.getY() + (Y - previousFingerPosition));
                    }
                    // If we scroll down
                    else{
                        this.setY(this.getY() + (Y - previousFingerPosition));
                    }
                }
                break;
        }
        return true;
    }

    public void closeUpAndDismissDialog(int currentPosition){
        isClosing = true;
        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(this, "y", currentPosition, -getHeight());
        positionAnimator.setDuration(300);
        positionAnimator.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animator)
            {
                if(dragLayoutListener != null) {
                    dragLayoutListener.onDialogClose();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        positionAnimator.start();
    }

    public void closeDownAndDismissDialog(int currentPosition){
        isClosing = true;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(this, "y", currentPosition, screenHeight+getHeight());
        positionAnimator.setDuration(300);
        positionAnimator.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animator)
            {
                if(dragLayoutListener != null) {
                    dragLayoutListener.onDialogClose();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        positionAnimator.start();
    }
    private  void setDragViewHandlingTouch(boolean dragviewHandlingTouch){
        this.dragViewHandlingTouch = dragviewHandlingTouch;

    }

    public void setCommentsListView(ListView commentsListView) {
        this.commentsListView = commentsListView;
        commentsListView.setOnScrollListener(scrollListener);
    }

    public void setListViewScrollingEndedTop(boolean listViewScrollingEnded){
        this.listViewScrollingEndedTop = listViewScrollingEnded;
    }

    public void setListViewScrollingEndedBottom(boolean listViewScrollingEnded){
        this.listViewScrollingEndedBottom = listViewScrollingEnded;
    }

}
