package sy.com.initproject.root.ui.novel;

import android.support.annotation.Nullable;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import sy.com.initproject.R;
import sy.com.initproject.databinding.GirlListItemBinding;
import sy.com.initproject.root.models.GirlBean;
import sy.com.initproject.root.ui.category.adapter.BaseCategoryAdapter;

/**
 * Description:
 * Data：2018/7/16-12:48
 *
 * @author Administrator
 */
public class NovelAdapter extends BaseCategoryAdapter<GirlBean, NovelAdapter.GirlViewHolder> {


    public NovelAdapter(@Nullable List<GirlBean> data) {
        super(R.layout.girl_list_item, data);
    }

    @Override
    protected void convert(GirlViewHolder helper, GirlBean item) {
        helper.bindData(item);
    }


    public class GirlViewHolder extends BaseViewHolder {

        private GirlListItemBinding binding;

        public GirlViewHolder(View view) {
            super(view);
            binding = GirlListItemBinding.bind(view);
        }

        void bindData(GirlBean item) {
            binding.setBean(item);
            binding.executePendingBindings();
            addOnClickListener(R.id.cover_iv);
            Glide.with(mContext)
                    .load(item.getUrl())
                    .apply(new RequestOptions()
                            .centerCrop()
                            .error(R.drawable.ic_place_holder)
                            .placeholder(R.drawable.ic_place_holder))
                    .thumbnail(0.1f)
                    .into(binding.coverIv);
        }
    }
}
