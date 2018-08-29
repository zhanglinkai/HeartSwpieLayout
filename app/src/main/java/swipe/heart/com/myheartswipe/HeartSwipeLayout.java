package swipe.heart.com.myheartswipe;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import swipe.heart.com.myheartswipe.HeartSwipeManager;

/**
 * Created by Administrator on 2018/8/27.
 */

public class HeartSwipeLayout extends LinearLayout {
    /**
     * 主内容view
     */
    private View contentView;
    /**
     * 滑动菜单view
     */
    private View menuView;
    /**
     * 设置当前活动的方向
     */
    private int mode;
    /**
     * 当前滑动的状态，标记向左-向右-向上-向下
     */
    private int scrollStatus;
    /**
     * 滑动callback
     */
    private ViewDragHelper mDragHelper;
    /**
     * listener
     */
    private HeartSwipeLayoutListener listener;

    public HeartSwipeLayout(Context context) {
        this(context, null);
    }

    public HeartSwipeLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeartSwipeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragHelper = ViewDragHelper.create(this, mDragHelperCallback);
        setSwipeMode(Mode.LEFT);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 2) {
            throw new NullPointerException("you only need two child view!");
        } else {
            contentView = getChildAt(0);
            menuView = getChildAt(1);
        }
    }

    public void setListener(HeartSwipeLayoutListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        switch (mode) {
            case Mode.LEFT:
                contentView.layout(contentView.getLeft(), contentView.getTop(), contentView.getLeft() + contentView.getMeasuredWidth(), contentView.getMeasuredHeight());
                menuView.layout(contentView.getMeasuredWidth() + contentView.getLeft(), contentView.getTop(), contentView.getMeasuredWidth() + contentView.getLeft() + menuView.getMeasuredWidth(), contentView.getMeasuredHeight());
                break;
            case Mode.TOP:
                contentView.layout(contentView.getLeft(), contentView.getTop(), contentView.getLeft() + contentView.getMeasuredWidth(), contentView.getMeasuredHeight());
                menuView.layout(contentView.getLeft(), contentView.getBottom(), contentView.getMeasuredWidth(), contentView.getBottom() + menuView.getMeasuredHeight());
                break;
            case Mode.RIGHT:
                contentView.layout(contentView.getLeft(), contentView.getTop(), contentView.getLeft() + contentView.getMeasuredWidth(), contentView.getMeasuredHeight());
                menuView.layout(contentView.getLeft() - menuView.getMeasuredWidth(), contentView.getTop(), contentView.getLeft(), contentView.getMeasuredHeight());
                break;
            case Mode.BOTTOM:
                contentView.layout(contentView.getLeft(), contentView.getTop(), contentView.getLeft() + contentView.getMeasuredWidth(), contentView.getMeasuredHeight());
                menuView.layout(contentView.getLeft(), contentView.getTop() - menuView.getMeasuredHeight(), contentView.getMeasuredWidth(), menuView.getMeasuredHeight());
                break;
        }
    }

    float xDistance, yDistance, xStart, yStart, xEnd, yEnd;
    boolean isccc;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                isccc = false;
                xStart = ev.getX();
                yStart = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dX = (int) (ev.getX() - xStart);
                int dY = (int) (ev.getY() - yStart);
                if (mode==Mode.LEFT||mode==Mode.RIGHT) {
                    if (Math.abs(dX) < Math.abs(dY)) {//上下滑动
                        HeartSwipeManager.newInstance().closeLayout();
                        return true;
                    }
                }else if (mode==Mode.TOP||mode==Mode.BOTTOM){
                    if (Math.abs(dX) > Math.abs(dY)) {//左右滑动滑动
                        HeartSwipeManager.newInstance().closeLayout();
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mDragHelper.cancel();
                return false;
        }
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //固定写法
        mDragHelper.processTouchEvent(ev);
        return true;
    }

    @Override
    public void computeScroll() {
        //固定写法
        //此方法用于自动滚动,比如自动回滚到默认位置.
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private ViewDragHelper.Callback mDragHelperCallback = new ViewDragHelper.Callback() {
        /**
         * 处理水平方向上的拖动
         * @param child 被拖动到view
         * @param left 移动到达的x轴的距离
         * @param dx 建议的移动的x距离
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (mode == Mode.RIGHT || mode == Mode.LEFT) {
                return left;
            } else {
                return 0;
            }
        }

        /**
         *  处理竖直方向上的拖动
         * @param child 被拖动到view
         * @param top 移动到达的y轴的距离
         * @param dy 建议的移动的y距离
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if (mode == Mode.RIGHT || mode == Mode.LEFT) {
                return 0;
            } else {
                return top;
            }
        }

        /**
         * 尝试捕获子view，一定要返回true
         * @param child 尝试捕获的view
         * @param pointerId 指示器id？
         * 这里可以决定哪个子view可以拖动
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == contentView || child == menuView;
        }

        /*
        * 这个用来控制横向移动的边界范围，单位是像素。
        * */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return menuView.getMeasuredWidth();
        }

        /*
        * 这个用来控制垂直移动的边界范围，单位是像素。
        * */
        @Override
        public int getViewVerticalDragRange(View child) {
            return menuView.getMeasuredHeight();
        }

        /*
        * 当releasedChild被释放的时候，xvel和yvel是x和y方向的加速度
        * */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            switch (mode) {
                case Mode.LEFT:
                    int scrollLeftStatus = getScrollStatus();
                    if (scrollLeftStatus == ScrollStatus.LEFT) {
                        open();
                    } else if (scrollLeftStatus == ScrollStatus.RIGHT) {
                        close();
                    }
                    break;
                case Mode.RIGHT:
                    int scrollRightStatus = getScrollStatus();
                    if (scrollRightStatus == ScrollStatus.LEFT) {
                        close();
                    } else if (scrollRightStatus == ScrollStatus.RIGHT) {
                        open();
                    }
                    break;
                case Mode.TOP:
                    int scrollTopStatus = getScrollStatus();
                    if (scrollTopStatus == ScrollStatus.TOP) {
                        open();
                    } else if (scrollTopStatus == ScrollStatus.BOTTOM) {
                        close();
                    }
                    break;
                case Mode.BOTTOM:
                    int scrollBottomStatus = getScrollStatus();
                    if (scrollBottomStatus == ScrollStatus.TOP) {
                        close();
                    } else if (scrollBottomStatus == ScrollStatus.BOTTOM) {
                        open();
                    }
                    break;
            }
        }

        /*
        * 这个是当changedView的位置发生变化时调用，我们可以在这里面控制View的显示位置和移动。
        * */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            switch (mode) {
                case Mode.LEFT:
                    if (dx > 0) {
                        setScrollStatus(ScrollStatus.RIGHT);
                    } else {
                        setScrollStatus(ScrollStatus.LEFT);
                    }
                    if (contentView == changedView) {
                        menuView.offsetLeftAndRight(dx);
                    } else {
                        contentView.offsetLeftAndRight(dx);
                    }
                    break;
                case Mode.RIGHT:
                    if (dx > 0) {
                        setScrollStatus(ScrollStatus.RIGHT);
                    } else {
                        setScrollStatus(ScrollStatus.LEFT);
                    }
                    if (contentView == changedView) {
                        menuView.offsetLeftAndRight(dx);
                    } else {
                        contentView.offsetLeftAndRight(dx);
                    }
                    break;
                case Mode.BOTTOM:
                    if (dy > 0) {
                        setScrollStatus(ScrollStatus.BOTTOM);
                    } else {
                        setScrollStatus(ScrollStatus.TOP);
                    }
                    if (contentView == changedView) {
                        menuView.offsetTopAndBottom(dy);
                    } else {
                        contentView.offsetTopAndBottom(dy);
                    }
                    break;
                case Mode.TOP:
                    if (dy > 0) {
                        setScrollStatus(ScrollStatus.BOTTOM);
                    } else {
                        setScrollStatus(ScrollStatus.TOP);
                    }
                    if (contentView == changedView) {
                        menuView.offsetTopAndBottom(dy);
                    } else {
                        contentView.offsetTopAndBottom(dy);
                    }
                    break;
            }

            invalidate();
        }

        /**
         * 当拖拽到状态改变时回调
         * @params 新的状态
         */
        @Override
        public void onViewDragStateChanged(int state) {
            switch (state) {
                case ViewDragHelper.STATE_DRAGGING:  // 正在被拖动
                    break;
                case ViewDragHelper.STATE_IDLE:  // view没有被拖拽或者 正在进行fling/snap
                    break;
                case ViewDragHelper.STATE_SETTLING: // fling完毕后被放置到一个位置
                    break;
            }
            super.onViewDragStateChanged(state);
        }
    };

    /**
     * slide close
     */
    public void close() {
        if (mDragHelper.smoothSlideViewTo(contentView, 0, 0)) {
            HeartSwipeManager.newInstance().setLayout(null);
            listener.onClose(this);
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * slide open
     */
    public void open() {
        switch (mode) {
            case Mode.LEFT:
                if (mDragHelper.smoothSlideViewTo(contentView, -menuView.getMeasuredWidth(), 0)) {
                    HeartSwipeManager.newInstance().closeLayout();
                    HeartSwipeManager.newInstance().setLayout(this);
                    listener.onOpen(this);
                    ViewCompat.postInvalidateOnAnimation(this);
                }
                break;
            case Mode.RIGHT:
                if (mDragHelper.smoothSlideViewTo(contentView, menuView.getMeasuredWidth(), 0)) {
                    HeartSwipeManager.newInstance().closeLayout();
                    HeartSwipeManager.newInstance().setLayout(this);
                    listener.onOpen(this);
                    ViewCompat.postInvalidateOnAnimation(this);
                }
                break;
            case Mode.TOP:
                if (mDragHelper.smoothSlideViewTo(contentView, 0, -menuView.getMeasuredHeight())) {
                    HeartSwipeManager.newInstance().closeLayout();
                    HeartSwipeManager.newInstance().setLayout(this);
                    listener.onOpen(this);
                    ViewCompat.postInvalidateOnAnimation(this);
                }
                break;
            case Mode.BOTTOM:
                if (mDragHelper.smoothSlideViewTo(contentView, 0, menuView.getMeasuredHeight())) {
                    HeartSwipeManager.newInstance().closeLayout();
                    HeartSwipeManager.newInstance().setLayout(this);
                    listener.onOpen(this);
                    ViewCompat.postInvalidateOnAnimation(this);
                }
                break;
        }

    }

    /*
    * 设置当前滑动的方向
    * */
    public void setSwipeMode(@Mode int mode) {
        this.mode = mode;
        switch (mode) {
            case Mode.LEFT:
                mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
                break;
            case Mode.RIGHT:
                mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_RIGHT);
                break;
            case Mode.TOP:
                mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_TOP);
                break;
            case Mode.BOTTOM:
                mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_BOTTOM);
                break;
        }
    }

    /*
    * 滑动模式
    * 左-上-右-下
    * */
    @interface Mode {
        int LEFT = 1;
        int TOP = 2;
        int RIGHT = 3;
        int BOTTOM = 4;
    }

    /*
    * 滑动状态
    * 左-上-右-下
    * */
    private @interface ScrollStatus {
        int LEFT = 5;
        int TOP = 6;
        int RIGHT = 7;
        int BOTTOM = 8;
    }

    private int getScrollStatus() {
        return scrollStatus;
    }

    private void setScrollStatus(@ScrollStatus int scrollStatus) {
        this.scrollStatus = scrollStatus;
    }

    /*static class HeartSwipeManager{
        private static HeartSwipeManager manager;
        private HeartSwipeLayout layout;
        public static HeartSwipeManager newInstance() {
            if (manager==null){
                manager=new HeartSwipeManager();
            }
            return manager;
        }
        public void setLayout(HeartSwipeLayout layout) {
            this.layout = layout;
        }
        public void closeLayout(){
            if (layout!=null){
                layout.close();
            }
        }
    }*/

    /*
        * 事件监听
        * */
    public interface HeartSwipeLayoutListener {
        //public void onStartOpen(HeartSwipeLayout layout);

        public void onOpen(HeartSwipeLayout layout);

        //public void onStartClose(HeartSwipeLayout layout);

        public void onClose(HeartSwipeLayout layout);

        /*public void onUpdate(HeartSwipeLayout layout, int leftOffset, int topOffset);

        public void onHandRelease(HeartSwipeLayout layout, float xvel, float yvel);*/
    }
}
