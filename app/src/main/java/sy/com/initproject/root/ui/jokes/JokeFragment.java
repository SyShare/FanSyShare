package sy.com.initproject.root.ui.jokes;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import sy.com.initproject.root.models.JokeBean;
import sy.com.initproject.root.ui.category.adapter.BaseCategoryAdapter;
import sy.com.initproject.root.ui.category.base.BaseCategoryRecyclerFragment;
import viewmodel.HomeViewModel;
import viewmodel.ViewModelExtentionKt;

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
            mAdapter.setNewData(listBaseResponse.getData());
            updateEmpty(false);
        });
    }

    @Override
    protected void requestData() {
        homeViewModel.getRecommondJokes(1);
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
