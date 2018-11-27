package sy.com.initproject.root.ui.news;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.banner.Banner;
import com.banner.IndicatorView.CircleIndicatorView;
import com.banner.Transformer;
import com.banner.interfaces.ViewPagerHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pince.frame.BaseMvpPresenter;

import java.util.List;

import sy.com.initproject.R;
import sy.com.initproject.databinding.NewsListItemBinding;
import sy.com.initproject.root.models.NewsBean;
import sy.com.initproject.root.ui.web.WebActivity;

import static sy.com.initproject.root.ui.news.Constant.MARGIN_LAY_1;
import static sy.com.initproject.root.ui.news.Constant.MARGIN_LAY_5;
import static sy.com.initproject.root.ui.news.Constant.MARGIN_LAY_8;
import static sy.com.initproject.root.ui.news.Constant.ViewType.TYPE_BANNER;
import static sy.com.initproject.root.ui.news.Constant.ViewType.TYPE_LIST;
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
        viewPool.setMaxRecycledViews(TYPE_LIST, 20);
        viewPool.setMaxRecycledViews(TYPE_TITLE, 5);
        //设置适配器
        DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager, false);
        recyclerView.setAdapter(delegateAdapter);
        return delegateAdapter;
    }

    @Override
    public BaseDelegateAdapter initBanner(List<String> stringList) {
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        linearLayoutHelper.setMargin(0, MARGIN_LAY_1, 0, 0);
        linearLayoutHelper.setBgColor(0xff432e2a);
        return new BaseDelegateAdapter(getActivityHandler().getActivityContext(), linearLayoutHelper, R.layout.base_view_banner, 1, TYPE_BANNER) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                Banner banner = holder.getView(R.id.banner);
                banner.setData(stringList)
                        .isAutoPlay(true)
                        .setBannerAnimation(Transformer.Default)
                        .setIndicatorFillMode(CircleIndicatorView.FillMode.NONE)
                        .setViewPagerHolder(CustomViewPagerHolder::new)
                        .start();
            }
        };
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
        return new BaseDelegateAdapter(getActivityHandler().getActivityContext(), linearLayoutHelper, R.layout.news_list_item, teachList.size(), TYPE_LIST) {
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
        binding.getRoot().setOnClickListener(v -> {
            if (view != null) {
                view.jumpWeb(item);
            }
        });
    }


    private static class CustomViewPagerHolder implements ViewPagerHolder<String> {

        private ImageView mImageView;

        @Override
        public View onCreateView(Context context) {
            // 返回ViewPager 页面展示的布局
            View view = LayoutInflater.from(context).inflate(R.layout.item_home_banner, null);
            mImageView = view.findViewById(R.id.viewPager_item_image);
            return view;
        }

        @Override
        public void onBind(Context context, final int position, String data) {
            if (TextUtils.isEmpty(data)) {
                return;
            }
            if (mImageView != null) {
                String url = data;

                Glide.with(context)
                        .load(url)
                        .apply(new RequestOptions()
                                .centerCrop()
                                .error(R.drawable.ic_place_holder)
                                .placeholder(R.drawable.ic_place_holder)
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                        .thumbnail(0.1f)
                        .into(mImageView);

                mImageView.setOnClickListener(v -> {
                            if (position % 2 == 0) {
                                WebActivity.open(context, "https://blog.csdn.net/syb001chen", "欢迎访问");
                            } else {
                                WebActivity.open(context, "https://github.com/SyShare", "欢迎访问");
                            }
                        }
                );
            }
        }
    }
}
