package sy.com.initproject.root.ui.category.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pince.frame.BaseFragment;
import com.pince.ut.NetUtil;
import com.pince.ut.ViewUtil;

import sy.com.initproject.R;
import sy.com.initproject.databinding.FragmentBaseRecyclerViewBinding;
import sy.com.initproject.root.interf.OnTabReselectListener;
import sy.com.initproject.root.ui.category.adapter.BaseCategoryAdapter;
import sy.com.initproject.root.utils.SpaceItemDecoration;
import sy.com.initproject.root.widgets.EmptyCustomView;

/**
 * @author syb
 * @date 2018/5/14
 * 首页多Tab基类
 */

public abstract class BaseCategoryRecyclerFragment<T, K extends BaseViewHolder> extends BaseFragment<FragmentBaseRecyclerViewBinding>
        implements SwipeRefreshLayout.OnRefreshListener, OnTabReselectListener {

    private static final int MINI_REFRESH_TIME = 1000;
    /**
     * 是否正在刷新
     */
    protected boolean isRefreshing;

    /**
     * 适配器
     */
    protected BaseQuickAdapter mAdapter;
    /**
     * 当前页数
     */
    private int pageNow = 1;

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

        mAdapter.setPreLoadNumber(getPreLoadNumber());
//        mAdapter.setOnLoadMoreListener(() -> getRecommendRooms(getTabType().id, pageNow, false), mBinding.recyclerView);
    }

    @Override
    protected void setViewData(Bundle savedInstanceState) {
        requestData();
    }

    @Override
    public void onRefresh() {
        isRefreshing = true;
        requestData();
    }

    @Override
    public void onTabReselect() {
        mBinding.swipeView.setRefreshing(true);
        onRefresh();
    }

    /**
     * 请求数据
     */
    protected abstract void requestData();

    /**
     * 获取控制器
     *
     * @return
     */
    protected RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        return new GridLayoutManager(getActivity(), 2);
    }


    /**
     * 加载完成，可以加载更多
     */
    public void loadComplete() {
        isRefreshing = false;
        mAdapter.loadMoreComplete();
        mAdapter.setEnableLoadMore(true);
    }

    /**
     * 加载完成，over
     */
    public void loadMoreEnd() {
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
        return 6;
    }
}
