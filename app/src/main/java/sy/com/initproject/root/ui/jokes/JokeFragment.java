package sy.com.initproject.root.ui.jokes;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;

import java.util.ArrayList;
import java.util.List;

import sy.com.initproject.R;
import sy.com.initproject.root.api.HomeViewModel;
import sy.com.initproject.root.models.JokeBean;
import sy.com.initproject.root.ui.category.adapter.BaseCategoryAdapter;
import sy.com.initproject.root.ui.category.base.BaseCategoryRecyclerFragment;
import sy.com.lib_http.arch.ViewModelExtentionKt;

/**
 * 热门段子tab
 *
 * @author SyShare
 */
public class JokeFragment extends BaseCategoryRecyclerFragment<JokeBean, JokeAdapter.JokeViewHolder> {

    private HomeViewModel homeViewModel;


    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        homeViewModel = ViewModelExtentionKt.getViewModel(this, HomeViewModel.class);
        homeViewModel.getJokeModel().observe(this, listBaseResponse -> {
            if (listBaseResponse == null || !listBaseResponse.isSuccessful()) {
                updateEmpty(false);
                return;
            }

            setData(filter(listBaseResponse.getData()));
        });
        mBinding.titleBar.titleTv.setText("好评段子");

        mAdapter.setOnItemClickListener((adapter, view, position) -> {

            JokeBean bean = (JokeBean) mAdapter.getItem(position);
            if (bean == null
                    || TextUtils.isEmpty(bean.getVideouri())) {
                return;
            }

            ARouter.getInstance().build("/com/player")
                    .withString("videoUrl", bean.getVideouri())
                    .withString("extraTitle", bean.getName())
                    .withString("thumb", bean.getBimageuri())
                    .navigation();
        });
        dynamicAddSkinView(mBinding.titleBar.tabBar, "background", R.color.colorAccent);
    }

    private List<JokeBean> filter(List<JokeBean> datas) {
//        for (JokeBean bean : datas) {
//            if (TextUtils.isEmpty(bean.getVideouri())) {
//                datas.remove(bean);
//            }
//        }
        return datas;
    }

    @Override
    protected void requestData() {
        homeViewModel.getRecommondJokes(1);
    }

    @Override
    protected void loadMoreRequest() {
        homeViewModel.getRecommondJokes(pageNow);
    }

    @Override
    protected BaseCategoryAdapter<JokeBean, JokeAdapter.JokeViewHolder> getRecyclerAdapter() {
        return new JokeAdapter(new ArrayList<>());
    }

    @Override
    protected RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        return new LinearLayoutManager(getActivityContext());
    }
}
