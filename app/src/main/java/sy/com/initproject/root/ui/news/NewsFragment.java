package sy.com.initproject.root.ui.news;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.pince.frame.BaseMvpFragment;
import com.pince.ut.NetUtil;

import java.util.LinkedList;
import java.util.List;

import sy.com.initproject.R;
import sy.com.initproject.databinding.FragmentBaseRecyclerViewBinding;
import sy.com.initproject.root.MainApplication;
import sy.com.initproject.root.interf.OnTabReselectListener;
import sy.com.initproject.root.models.NewsBean;
import sy.com.initproject.root.ui.web.WebActivity;
import sy.com.initproject.root.utils.PaddingDecoration;
import sy.com.initproject.root.widgets.EmptyCustomView;
import sy.com.lib_http.bean.BaseResponse;
import viewmodel.HomeViewModel;
import viewmodel.ViewModelExtentionKt;

/**
 * 热门段子tab
 *
 * @author SyShare
 */
public class NewsFragment extends BaseMvpFragment<NewsPresenter, FragmentBaseRecyclerViewBinding>
        implements SwipeRefreshLayout.OnRefreshListener, OnTabReselectListener, IMainContact.IView {


    private HomeViewModel homeViewModel;
    private boolean isRefreshing = false;
    /**
     * 复合Adapter 其他继承的adapter最终都需要设置给ta
     */
    private DelegateAdapter delegateAdapter;

    /**
     * 子Adapter集合
     */
    private List<DelegateAdapter.Adapter> mAdapters = new LinkedList<>();

    @Override
    protected int requestLayoutId() {
        return R.layout.fragment_base_recycler_view;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mBinding.swipeView.setColorSchemeResources(R.color.blue_50, R.color.blue_100, R.color.red_50, R.color.red_100);
        mBinding.swipeView.setOnRefreshListener(this);
        delegateAdapter = presenter.initRecyclerView(mBinding.recyclerView);
        mBinding.recyclerView.addItemDecoration(new PaddingDecoration(getActivityContext(), 56, 64));
        mBinding.recyclerView.setAdapter(delegateAdapter);
        mBinding.titleBar.titleTv.setText("新闻资讯");
    }

    @Override
    protected void setViewData(Bundle savedInstanceState) {
        homeViewModel = ViewModelExtentionKt.getViewModel(this, HomeViewModel.class);
        homeViewModel.getNewsModel().observe(this, new Observer<BaseResponse<NewsBean>>() {
            @Override
            public void onChanged(@Nullable BaseResponse<NewsBean> newsBeanBaseResponse) {
                if (newsBeanBaseResponse != null) {
                    updateHomeData(newsBeanBaseResponse.getData());
                } else {
                    updateEmpty(false);
                }
            }
        });
        homeViewModel.getDataErrorObserver().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                updateEmpty(true);
            }
        });
        requestData();
    }

    @Override
    public void onRefresh() {
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
    private void requestData() {
        if (isRefreshing) {
            return;
        }
        isRefreshing = true;
        homeViewModel.getRecommondNews();
    }

    /**
     * 更新数据
     *
     * @param response
     */
    private void updateHomeData(NewsBean response) {
        mAdapters.clear();
        initBanner();
        initTeachAdapter(response.getTech());
        initCarsAdapter(response.getAuto());
        initMoneyAdapter(response.getMoney());
        initSportsAdapter(response.getSports());
        initSpaceAdapter(response.getDy());
        initWarsAdapter(response.getWar());
        initEntAdapter(response.getEnt());
        //设置适配器
        delegateAdapter.setAdapters(mAdapters);
        delegateAdapter.notifyDataSetChanged();
        updateEmpty(false);
    }


    private void initBanner(){
        mAdapters.add(presenter.initBanner(MainApplication.bannerList));
    }

    private void initEntAdapter(List<NewsBean.CommonBean> ent) {
        if (ent == null
                || ent.isEmpty()) {
            return;
        }
        mAdapters.add(presenter.initCommonTitle("娱乐"));
        mAdapters.add(presenter.initListAdapter(ent));
    }

    private void initWarsAdapter(List<NewsBean.CommonBean> war) {
        if (war == null
                || war.isEmpty()) {
            return;
        }
        mAdapters.add(presenter.initCommonTitle("军事"));
        mAdapters.add(presenter.initListAdapter(war));
    }

    private void initSpaceAdapter(List<NewsBean.CommonBean> dy) {
        if (dy == null
                || dy.isEmpty()) {
            return;
        }
        mAdapters.add(presenter.initCommonTitle("太空"));
        mAdapters.add(presenter.initListAdapter(dy));
    }

    private void initSportsAdapter(List<NewsBean.CommonBean> sports) {
        if (sports == null
                || sports.isEmpty()) {
            return;
        }
        mAdapters.add(presenter.initCommonTitle("体育"));
        mAdapters.add(presenter.initListAdapter(sports));
    }

    private void initMoneyAdapter(List<NewsBean.CommonBean> money) {
        if (money == null
                || money.isEmpty()) {
            return;
        }
        mAdapters.add(presenter.initCommonTitle("财经"));
        mAdapters.add(presenter.initListAdapter(money));
    }

    private void initCarsAdapter(List<NewsBean.CommonBean> auto) {
        if (auto == null
                || auto.isEmpty()) {
            return;
        }
        mAdapters.add(presenter.initCommonTitle("资讯"));
        mAdapters.add(presenter.initListAdapter(auto));
    }

    private void initTeachAdapter(List<NewsBean.CommonBean> tech) {
        if (tech == null
                || tech.isEmpty()) {
            return;
        }
        mAdapters.add(presenter.initCommonTitle("教育"));
        mAdapters.add(presenter.initListAdapter(tech));
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
        boolean isEmpty = delegateAdapter.getItemCount() == 0;
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

    @Override
    public void jumpWeb(NewsBean.CommonBean bean) {
        WebActivity.open(getActivityContext(), bean.getLink(), bean.getTitle());
    }
}
