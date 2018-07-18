package sy.com.initproject.root.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 *
 * @author SyShare
 * @date 18/7/18
 */
public class PaddingDecoration extends RecyclerView.ItemDecoration {
    private int paddingTop;
    private int paddingBottom;

    private int paddingCount = 1;

    public PaddingDecoration(Context context, int paddingTopDp, int paddingBottomDP, int paddingCount) {
        this(context, paddingTopDp, paddingBottomDP);
        this.paddingCount = paddingCount;
    }

    public PaddingDecoration(Context context, int paddingTopDp, int paddingBottomDP) {
        Resources r = context.getResources();
        paddingTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paddingTopDp, r.getDisplayMetrics());
        paddingBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paddingBottomDP, r.getDisplayMetrics());
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        int count = state.getItemCount();
        if (pos < getPaddingCount(parent.getAdapter())) {
            outRect.top = paddingTop;
            if (pos + 1 >= count) {
                outRect.bottom = paddingBottom;
            }
            return;
        } else if (pos + 1 >= count) {
            outRect.bottom = paddingBottom;
            return;
        }
    }

    private int getPaddingCount(RecyclerView.Adapter adapter) {
        if (hasHeader(adapter)) {
            return 1;
        }
        return paddingCount;
    }

    private boolean hasHeader(RecyclerView.Adapter adapter) {
        if (adapter instanceof BaseQuickAdapter) {
            BaseQuickAdapter quickAdapter = (BaseQuickAdapter) adapter;
            if (quickAdapter.getHeaderLayoutCount() > 0) {
                return true;
            }
        }
        return false;
    }

}
