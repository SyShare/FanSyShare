package sy.com.initproject.root.utils;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * Created by liufakai on 2017/8/18.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private boolean includeEdge = false;
    private int edgeSpace;

    /**
     * @param space       间距
     * @param edgeSpace   边缘距离
     * @param includeEdge 计算边缘距离
     */
    public SpaceItemDecoration(int space, int edgeSpace, boolean includeEdge) {
        this.space = space;
        this.edgeSpace = edgeSpace;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if ((parent.getLayoutManager() instanceof GridLayoutManager)
                || (parent.getLayoutManager() instanceof StaggeredGridLayoutManager)) {
            int spanCount;
            int column;
            if (parent.getLayoutManager() instanceof GridLayoutManager) {
                GridLayoutManager manager = (GridLayoutManager) parent.getLayoutManager();
                spanCount = manager.getSpanCount();
                column = ((GridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
            } else {
                StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) parent.getLayoutManager();
                spanCount = manager.getSpanCount();
                column = ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
            }

            RecyclerView.Adapter adapter = parent.getAdapter();
            int position = parent.getChildAdapterPosition(view); // item position

            if (includeEdge) {
                if (hasHeader(adapter)) {
                    if (position > 0) {
                        if (column == 0) {
                            outRect.left = edgeSpace;
                            outRect.right = (column + 1) * space / spanCount; // (column + 1) * ((1f / spanCount) * spacing)
                        } else if (column == 1) {
                            outRect.left = space - column * space / spanCount;
                            outRect.right = edgeSpace;
                        }
                        if (position - 1 < spanCount) { // top edge
                            outRect.top = edgeSpace;
                        }
                        outRect.bottom = space; // item bottom
                    }
                } else {
                    outRect.left = space - column * space / spanCount;
                    outRect.right = (column + 1) * space / spanCount; // (column + 1) * ((1f / spanCount) * spacing)
                    if (position < spanCount) { // top edge
                        outRect.top = edgeSpace;
                    }
                    outRect.bottom = space; // item bottom
                }
            } else {
                outRect.left = column * space / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = space - (column + 1) * space / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (hasHeader(adapter) && position > 0) {
                    if (position + 1 >= spanCount) {
                        outRect.top = space; // item top
                    }
                } else if (position >= spanCount) {
                    outRect.top = space; // item top
                }

            }
        } else if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) parent.getLayoutManager()).getOrientation() == LinearLayoutManager.VERTICAL) {
                if (includeEdge) {
                    outRect.left = edgeSpace;
                    outRect.right = edgeSpace;
                }
                outRect.top = space / 2;
                outRect.bottom = space / 2;
            } else {
                if (includeEdge) {
                    outRect.top = edgeSpace;
                    outRect.bottom = edgeSpace;
                }
                outRect.left = space / 2;
                outRect.right = space / 2;
            }
        }
    }

    protected boolean hasHeader(RecyclerView.Adapter adapter) {
        if (adapter instanceof BaseQuickAdapter) {
            BaseQuickAdapter quickAdapter = (BaseQuickAdapter) adapter;
            if (quickAdapter.getHeaderLayoutCount() > 0) {
                return true;
            }
        }
        return false;
    }

    protected int getEdgeSpace() {
        return edgeSpace;
    }
}
