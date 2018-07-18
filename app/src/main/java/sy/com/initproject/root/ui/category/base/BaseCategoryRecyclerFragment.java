package sy.com.initproject.root.ui.category.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pince.frame.BaseFragment;
import com.pince.ut.NetUtil;
import com.pince.ut.ViewUtil;

import java.util.List;

import sy.com.initproject.R;
import sy.com.initproject.databinding.FragmentBaseRecyclerViewBinding;
import sy.com.initproject.root.interf.OnTabReselectListener;
import sy.com.initproject.root.ui.category.adapter.BaseCategoryAdapter;
import sy.com.initproject.root.utils.SpaceItemDecoration;
import sy.com.initproject.root.widgets.EmptyCustomView;

import static sy.com.initproject.root.ui.news.Constant.PAGE_SIZE;

/**
 * @author syb
 * @date 2018/5/14
 * 首页多Tab基类
 */

public abstract class BaseCategoryRecyclerFragment<T, K extends BaseViewHolder> extends BaseFragment<FragmentBaseRecyclerViewBinding>
        implements SwipeRefreshLayout.OnRefreshListener, OnTabReselectListener {

    /**
     * 当前页数
     */
    public int pageNow = 1;
    /**
     * 是否正在刷新
     */
    protected boolean isRefreshing;
    /**
     * 适配器
     */
    protected BaseQuickAdapter mAdapter;
    /**
     * 滑动监听
     */
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                    Glide.with(getActivityContext()).resumeRequests();
                    break;
                case RecyclerView.SCROLL_STATE_DRAGGING:
                case RecyclerView.SCROLL_STATE_SETTLING:
                    Glide.with(getActivityContext()).pauseRequests();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected int requestLayoutId() {
        return R.layout.fragment_base_recycler_view;
    }

    @Override
    protected void initView(View rootView) {
        mBinding.swipeView.setColorSchemeResources(R.color.blue_50, R.color.blue_100, R.color.red_50, R.color.red_100);
        mBinding.swipeView.setOnRefreshListener(this);
        mBinding.recyclerView.setItemAnimator(null);
        mBinding.recyclerView.setHasFixedSize(true);
        mBinding.recyclerView.setLayoutManager(getRecyclerViewLayoutManager());
        if (getItemDecoration() != null) {
            mBinding.recyclerView.addItemDecoration(getItemDecoration());
        }
        mAdapter = getRecyclerAdapter();
        mBinding.recyclerView.setAdapter(mAdapter);

        if (loadMoreEnable()) {
            mAdapter.setPreLoadNumber(getPreLoadNumber());
            mAdapter.setOnLoadMoreListener(this::loadMoreRequest, mBinding.recyclerView);
        }
    }

    @Override
    protected void setViewData(Bundle savedInstanceState) {
        requestData();
    }

    @Override
    public void onRefresh() {
        pageNow = 1;
        isRefreshing = true;
        requestData();
    }

    @Override
    public void onTabReselect() {
        pageNow = 1;
        mBinding.swipeView.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.recyclerView.addOnScrollListener(mOnScrollListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        mBinding.recyclerView.removeOnScrollListener(mOnScrollListener);
    }

    /**
     * 设置数据
     *
     * @param data
     */
    protected void setData(List<T> data) {
        if (mAdapter == null) {
            return;
        }
        if (pageNow == 1) {
            mAdapter.setNewData(data);
        } else {
            mAdapter.addData(data);
        }
        updateLoadState(data);
    }

    /**
     * 设置加载状态
     *
     * @param data
     */
    private void updateLoadState(List<T> data) {
        updateEmpty(false);
        if (data.size() < getNextPageLimit()) {
            loadMoreEnd();
        } else {
            loadComplete();
            pageNow++;
        }
    }

    /**
     * 加载完成，可以加载更多
     */
    private void loadComplete() {
        isRefreshing = false;
        mAdapter.loadMoreComplete();
        mAdapter.setEnableLoadMore(true);
    }

    /**
     * 加载完成，over
     */
    private void loadMoreEnd() {
        isRefreshing = false;
        mAdapter.loadMoreEnd();
        mAdapter.setEnableLoadMore(false);
    }

    /**
     * 空状态处理
     *
     * @param isError
     */
    protected void updateEmpty(boolean isError) {
        isRefreshing = false;
        mBinding.swipeView.setRefreshing(false);
        final boolean disconnected = !NetUtil.isNetworkAvailable(getActivity());
        boolean isEmpty = mAdapter.getData().isEmpty();
        if (isError && disconnected && isEmpty) {
            mBinding.errorLayout.setErrorType(EmptyCustomView.NETWORK_ERROR);
            return;
        }

        if (isError && isEmpty) {
            mBinding.errorLayout.setErrorType(EmptyCustomView.NODATA_ENABLE_CLICK);
            return;
        }

        if (isEmpty) {
            mBinding.errorLayout.setErrorType(EmptyCustomView.NODATA);
            return;
        }
        mBinding.errorLayout.setErrorType(EmptyCustomView.HIDE_LAYOUT);
    }

    /**
     * T 代表数据类型
     * K代表ViewHolder
     *
     * @return
     */
    protected abstract BaseCategoryAdapter<T, K> getRecyclerAdapter();

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new SpaceItemDecoration(ViewUtil.dip2px(10), ViewUtil.dip2px(12), false);
    }

    /**
     * 预加载数量
     *
     * @return
     */
    public int getPreLoadNumber() {
        return 10;
    }


    /**
     * 请求数据
     */
    protected abstract void requestData();

    /**
     * 加载更多
     */
    protected abstract void loadMoreRequest();

    /**
     * 是否允许加载更多
     *
     * @return
     */
    protected boolean loadMoreEnable() {
        return true;
    }

    /**
     * 获取下页的限制
     *
     * @return
     */
    protected int getNextPageLimit() {
        return PAGE_SIZE;
    }

    /**
     * 获取控制器
     *
     * @return
     */
    protected RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        return new GridLayoutManager(getActivity(), 2);
    }
}
