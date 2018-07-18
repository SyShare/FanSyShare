package sy.com.initproject.root.ui.novel;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;

import sy.com.initproject.root.models.GirlBean;
import sy.com.initproject.root.ui.category.adapter.BaseCategoryAdapter;
import sy.com.initproject.root.ui.category.base.BaseCategoryRecyclerFragment;
import sy.com.initproject.root.ui.preview.PictureActivity;
import viewmodel.HomeViewModel;
import viewmodel.ViewModelExtentionKt;

/**
 * 美女tab
 *
 * @author SyShare
 */
public class NovelFragment extends BaseCategoryRecyclerFragment<GirlBean, NovelAdapter.GirlViewHolder> {

    private HomeViewModel homeViewModel;


    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        homeViewModel = ViewModelExtentionKt.getViewModel(this, HomeViewModel.class);
        homeViewModel.getGirlModel().observe(this, response -> {
            if (response == null || !response.isSuccessful()) {
                updateEmpty(false);
                return;
            }
            setData(response.getData());
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                GirlBean bean = (GirlBean) mAdapter.getItem(position);
                if (bean == null || !(view instanceof ImageView)) {
                    return;
                }
                startPictureActivity(bean, view);
            }
        });
        mBinding.titleBar.titleTv.setText("美女专栏");
    }

    /**
     * 过场动画
     *
     * @param bean
     * @param transitView
     */
    private void startPictureActivity(GirlBean bean, View transitView) {
        Intent intent = PictureActivity.newIntent(getActivityContext(), bean.getUrl(), bean.getType());
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivityContext(), transitView, PictureActivity.TRANSIT_PIC);
        try {
            ActivityCompat.startActivity(getActivityContext(), intent, optionsCompat.toBundle());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            startActivity(intent);
        }
    }


    @Override
    protected void requestData() {
        homeViewModel.getBeautyList(1);
    }

    @Override
    protected void loadMoreRequest() {
        homeViewModel.getBeautyList(pageNow);
    }

    @Override
    protected BaseCategoryAdapter<GirlBean, NovelAdapter.GirlViewHolder> getRecyclerAdapter() {
        return new NovelAdapter(new ArrayList<>());
    }


}