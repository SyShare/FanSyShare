package sy.com.initproject.root.ui.news;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pince.frame.BaseMvpPresenter;

import java.util.List;

import sy.com.initproject.R;
import sy.com.initproject.databinding.NewsListItemBinding;
import sy.com.initproject.root.models.NewsBean;

import static sy.com.initproject.root.ui.news.Constant.MARGIN_LAY_5;
import static sy.com.initproject.root.ui.news.Constant.MARGIN_LAY_8;
import static sy.com.initproject.root.ui.news.Constant.ViewType.TYPE_LSIT;
import static sy.com.initproject.root.ui.news.Constant.ViewType.TYPE_TITLE;

/**
 * @author syb
 * @date 2018/5/10
 */

public class NewsPresenter extends BaseMvpPresenter<IMainContact.IView> implements IMainContact.IPresenter {


    @Override
    public boolean initData(Intent intent) {
        return true;
    }


    @Override
    public DelegateAdapter initRecyclerView(@NonNull RecyclerView recyclerView) {
        //初始化
        //创建VirtualLayoutManager对象
        VirtualLayoutManager layoutManager = new VirtualLayoutManager(getActivityHandler().getActivityContext());
        recyclerView.setLayoutManager(layoutManager);
        //设置回收复用池大小，（如果一屏内相同类型的 View 个数比较多，需要设置一个合适的大小，防止来回滚动时重新创建 View）
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(TYPE_LSIT, 20);
        viewPool.setMaxRecycledViews(TYPE_TITLE, 5);
        //设置适配器
        DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager, false);
        recyclerView.setAdapter(delegateAdapter);
        return delegateAdapter;
    }

    @Override
    public BaseDelegateAdapter initCommonTitle(String title) {
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        linearLayoutHelper.setMargin(0, MARGIN_LAY_8, 0, 0);
        linearLayoutHelper.setBgColor(0xff432e2a);
        return new BaseDelegateAdapter(getActivityHandler().getActivityContext(), linearLayoutHelper, R.layout.base_view_title, 1, TYPE_TITLE) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                holder.setText(R.id.tv_title, title);
            }
        };
    }

    @Override
    public BaseDelegateAdapter initListAdapter(List<NewsBean.CommonBean> teachList) {
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        linearLayoutHelper.setMargin(0, MARGIN_LAY_5, 0, MARGIN_LAY_5);
        return new BaseDelegateAdapter(getActivityHandler().getActivityContext(), linearLayoutHelper, R.layout.news_list_item, teachList.size(), TYPE_LSIT) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                NewsListItemBinding binding = DataBindingUtil.bind(holder.itemView);
                if (binding != null) {
                    bindData(binding, teachList.get(position));
                }
            }
        };
    }

    private void bindData(NewsListItemBinding binding, NewsBean.CommonBean item) {
        binding.setBean(item);
        binding.executePendingBindings();


        String url = "";
        if (item.getPicInfo() != null && !item.getPicInfo().isEmpty()) {
            url = item.getPicInfo().get(0).getUrl();
        }
        Glide.with(getActivityHandler().getActivityContext())
                .load(url)
                .apply(new RequestOptions()
                        .centerCrop()
                        .error(R.drawable.ic_place_holder)
                        .placeholder(R.drawable.ic_place_holder)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .thumbnail(0.1f)
                .into(binding.coverIv);

    }
}
