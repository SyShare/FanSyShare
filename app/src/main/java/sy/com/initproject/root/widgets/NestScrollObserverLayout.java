package sy.com.initproject.root.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import sy.com.initproject.R;

/**
 * @author SyShare
 * @date 2018/7/18
 */

public class NestScrollObserverLayout extends FrameLayout implements NestedScrollingParent {
    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();

    private int childLayoutId;
    private View childView;
    private ScrollAwareHandler scrollAwareHandler;
    private ScrollStatuCallback callback;

    public NestScrollObserverLayout(@NonNull Context context) {
        this(context, null);
    }

    public NestScrollObserverLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestScrollObserverLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NestScrollObserverLayout);
        childLayoutId = typedArray.getResourceId(R.styleable.NestScrollObserverLayout_animalChildLayout, 0);
        typedArray.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (childLayoutId > 0) {
            childView = findViewById(childLayoutId);
            if (childView != null) {
                scrollAwareHandler = new ScrollAwareHandler(childView);
            } else {
                throw new RuntimeException("animalChildLayout must be its child view!");
            }
        }
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (scrollAwareHandler != null) {
            scrollAwareHandler.onNestedScroll(dyConsumed);
        }
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
    }

    @Override
    public void onStopNestedScroll(View child) {
    }

    @Override
    public int getNestedScrollAxes() {
        return 0;
    }

    public void animateIn() {
        if (scrollAwareHandler != null) {
            scrollAwareHandler.animateIn();
        }
    }

    public void animateOut() {
        if (scrollAwareHandler != null) {
            scrollAwareHandler.animateOut();
        }
    }

    public void setSrollStatusCallback(ScrollStatuCallback callback) {
        this.callback = callback;
    }

    public interface ScrollStatuCallback {
        void onScrollStatus(@ScrollStatus int status);
    }

    @IntDef({ScrollStatus.ScrollIn, ScrollStatus.ScrollOut})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ScrollStatus {
        int ScrollIn = 0;
        int ScrollOut = 1;
    }

    private class ScrollAwareHandler {

        private boolean mIsAnimatingOut = false;
        private boolean mIsAnimatingIn = false;

        private View child;

        public ScrollAwareHandler(View v) {
            child = v;
        }

        public void onNestedScroll(int dyConsumed) {
            if (dyConsumed > 0 && !this.mIsAnimatingOut) {
                if (this.mIsAnimatingIn) {
                    ViewCompat.animate(child).cancel();
                }
                animateOut(child);
            } else if (dyConsumed < 0 && !mIsAnimatingIn) {
                if (this.mIsAnimatingOut) {
                    ViewCompat.animate(child).cancel();
                }
                animateIn(child);
            }
        }

        public void animateOut() {
            animateOut(child);
        }

        public void animateIn() {
            animateIn(child);
        }

        private void animateOut(final View button) {
            if (callback != null) {
                callback.onScrollStatus(ScrollStatus.ScrollOut);
            }
            ViewCompat.animate(button)
                    .translationY(-button.getHeight())
                    .setInterpolator(INTERPOLATOR)
                    .setListener(new ViewPropertyAnimatorListener() {
                        @Override
                        public void onAnimationStart(View view) {
                            ScrollAwareHandler.this.mIsAnimatingOut = true;
                            button.setLayerType(LAYER_TYPE_HARDWARE, null);
                        }

                        @Override
                        public void onAnimationCancel(View view) {
                            ScrollAwareHandler.this.mIsAnimatingOut = false;
                            button.setLayerType(LAYER_TYPE_NONE, null);
                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            view.setVisibility(View.GONE);
                            ScrollAwareHandler.this.mIsAnimatingOut = false;
                            button.setLayerType(LAYER_TYPE_NONE, null);
                        }
                    }).start();
        }

        private void animateIn(View button) {
            if (callback != null) {
                callback.onScrollStatus(ScrollStatus.ScrollIn);
            }
            button.setVisibility(View.VISIBLE);
            ViewCompat.animate(button)
                    .translationY(0)
                    .setInterpolator(INTERPOLATOR)
                    .setListener(new ViewPropertyAnimatorListener() {
                        @Override
                        public void onAnimationStart(View view) {
                            ScrollAwareHandler.this.mIsAnimatingIn = true;
                            button.setLayerType(LAYER_TYPE_HARDWARE, null);
                        }

                        @Override
                        public void onAnimationCancel(View view) {
                            ScrollAwareHandler.this.mIsAnimatingIn = false;
                            button.setLayerType(LAYER_TYPE_NONE, null);
                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            ScrollAwareHandler.this.mIsAnimatingIn = false;
                            button.setLayerType(LAYER_TYPE_NONE, null);
                        }
                    })
                    .start();
        }
    }
}
