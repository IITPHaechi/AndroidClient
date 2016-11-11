package iitp.project.haechi.purdueapps3.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dnay2 on 2016-11-09.
 */

public class BottomSheet extends ViewGroup {

    //필드구성
    /*

     */

    private static final int DEFAULT_PANEL_HEIGHT = 68; // dp로 기본 높이 지정
    private static final float DEFAULT_ANCHOR_POINT = 1.0f; // 기본 고정높이 지정(퍼센트)
    private static PanelState DEFAULT_SLIDE_STATE = PanelState.COLLAPSED;
    private static final int DEFAULT_SHADOW_HEIGHT = 4;
    private static final int DEFAULT_FADE_COLOR = 0x99000000; //기본 페이드 컬러
    private static final int DEFAULT_MIN_FLING_VELOCITY = 400;
    private static final boolean DEFAULT_OVERLAY_FLAG = false;
    private static final boolean DEFAULT_CLIP_PANEL_FLAG = true;
    private static final int[] DEFAULT_ATTRS = new int[]{
            android.R.attr.gravity
    };
    private int mMinFlingVelocity = DEFAULT_MIN_FLING_VELOCITY;
    private int mCoveredFadeColor = DEFAULT_FADE_COLOR;
    private static final int DEFAULT_PARALLAX_OFFSET = 0;
    private final Paint mCoveredFadePaint = new Paint();
    private Drawable mShadowDrawable;
    private int mPanelHeight = -1;
    private int mShadowHeight = -1;
    private int mParallaxOffset = -1;
    private boolean mIsSlidingUp;
    private boolean mOverlayContent = DEFAULT_OVERLAY_FLAG;
    private boolean mClipPanel = DEFAULT_CLIP_PANEL_FLAG;
    private View mDragView;
    private int mDragViewResId = -1;
    private View mScrollableView;
    private int mScrollableViewResId;
    //스크롤러블 뷰헬퍼가 들어가는 자리입니다 읍읍

    private View ilgakView; //빙산의 일각 뷰
    private View realView; //진짜로 보여줘야 하는 뷰

    //패널의 현재상태에 대한 정보
    public enum PanelState{
        EXPANDED,
        COLLAPSED,
        ANCHORED,
        HIDDEN,
        DRAGGING
    }

    private PanelState mSlideState = DEFAULT_SLIDE_STATE;
    private PanelState mLastNotDraggingSlideState = DEFAULT_SLIDE_STATE;
    private float mSlideOffset;
    private int mSlideRange;
    private float mAnchorPoint = 1.f;
    private boolean mIsUnableToDrag;
    private boolean mIsTouchEnabled;

    private float mPrevMotionY;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private boolean mIsScrollableViewHandlingTouch=false;

    private List<SlidingPaneLayout.PanelSlideListener> mPanelSlideListeners = new ArrayList<>();
    private View.OnClickListener mFadeOnClickListener;

    //뷰의 드래그헬퍼가 들어오는 자리입니다 읍읍

    private boolean mFirstLayout = true;
    private final Rect mTmpRect = new Rect();

    public interface PanelSlideListener{
        void onPanelSlide(View panel, float slideOffset);
        void onPanelStateChanged(View panel, PanelState previousState, PanelState nextState);
    }

    public static class SimplePanelSlideListener implements PanelSlideListener{
        @Override
        public void onPanelSlide(View panel, float slideOffset) {
        }

        @Override
        public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
        }
    }





    public BottomSheet(Context context) {
        super(context, null);
    }

    public BottomSheet(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public BottomSheet(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    private void initialize(Context context, @Nullable AttributeSet attrs){

        if(isInEditMode()){
            mShadowDrawable = null;
            //mDragHelper = null;
            return;
        }

        Interpolator scrollerInterpolator = null;

        if(attrs != null){
            //attrs 사용
            TypedArray defAttrs = context.obtainStyledAttributes(attrs, DEFAULT_ATTRS);
            if(defAttrs != null){
                int gravity = defAttrs.getInt(0, Gravity.NO_GRAVITY);
                setGravity(gravity);
            }
            defAttrs.recycle();
            /*
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SlidingUpPanelLayout);

            if (ta != null) {
                mPanelHeight = ta.getDimensionPixelSize(R.styleable.SlidingUpPanelLayout_umanoPanelHeight, -1);
                mShadowHeight = ta.getDimensionPixelSize(R.styleable.SlidingUpPanelLayout_umanoShadowHeight, -1);
                mParallaxOffset = ta.getDimensionPixelSize(R.styleable.SlidingUpPanelLayout_umanoParallaxOffset, -1);

                mMinFlingVelocity = ta.getInt(R.styleable.SlidingUpPanelLayout_umanoFlingVelocity, DEFAULT_MIN_FLING_VELOCITY);
                mCoveredFadeColor = ta.getColor(R.styleable.SlidingUpPanelLayout_umanoFadeColor, DEFAULT_FADE_COLOR);

                mDragViewResId = ta.getResourceId(R.styleable.SlidingUpPanelLayout_umanoDragView, -1);
                mScrollableViewResId = ta.getResourceId(R.styleable.SlidingUpPanelLayout_umanoScrollableView, -1);

                mOverlayContent = ta.getBoolean(R.styleable.SlidingUpPanelLayout_umanoOverlay, DEFAULT_OVERLAY_FLAG);
                mClipPanel = ta.getBoolean(R.styleable.SlidingUpPanelLayout_umanoClipPanel, DEFAULT_CLIP_PANEL_FLAG);

                mAnchorPoint = ta.getFloat(R.styleable.SlidingUpPanelLayout_umanoAnchorPoint, DEFAULT_ANCHOR_POINT);

                mSlideState = PanelState.values()[ta.getInt(R.styleable.SlidingUpPanelLayout_umanoInitialState, DEFAULT_SLIDE_STATE.ordinal())];

                int interpolatorResId = ta.getResourceId(R.styleable.SlidingUpPanelLayout_umanoScrollInterpolator, -1);
                if (interpolatorResId != -1) {
                    scrollerInterpolator = AnimationUtils.loadInterpolator(context, interpolatorResId);
                }
            }

            ta.recycle();
             */

        }

        final float density = context.getResources().getDisplayMetrics().density;

        if (mPanelHeight == -1) {
            mPanelHeight = (int) (DEFAULT_PANEL_HEIGHT * density + 0.5f);
        }
        if (mShadowHeight == -1) {
            mShadowHeight = (int) (DEFAULT_SHADOW_HEIGHT * density + 0.5f);
        }
        if (mParallaxOffset == -1) {
            mParallaxOffset = (int) (DEFAULT_PARALLAX_OFFSET * density);
        }
        // If the shadow height is zero, don't show the shadow
//        if (mShadowHeight > 0) {
//            if (mIsSlidingUp) {
//                mShadowDrawable = getResources().getDrawable(R.drawable.d_above_shadow);
//            } else {
//                mShadowDrawable = getResources().getDrawable(R.drawable.d_below_shadow);
//            }
//        } else {
//            mShadowDrawable = null;
//        }

        setWillNotDraw(false);

//        mDragHelper = ViewDragHelper.create(this, 0.5f, scrollerInterpolator, new DragHelperCallback());
//        mDragHelper.setMinVelocity(mMinFlingVelocity * density);

        mIsTouchEnabled = true;


    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mDragViewResId != -1) {
//            setDragView(findViewById(mDragViewResId));
        }
        if (mScrollableViewResId != -1) {
//            setScrollableView(findViewById(mScrollableViewResId));
        }
    }

    public void setGravity(int gravity){
        if (gravity != Gravity.TOP && gravity != Gravity.BOTTOM) {
            gravity = Gravity.BOTTOM;
//            throw new IllegalArgumentException("gravity must be set to either top or bottom");
        }
        mIsSlidingUp = gravity == Gravity.BOTTOM;
        if (!mFirstLayout) {
            requestLayout();
        }
    }

    /*
    setGravity까지 따라쓰며 공부중입니다. 필드부터 아직 감이 안잡힙니다.
     */

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
