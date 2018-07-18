package sy.com.initproject.root.ui.jokes;

import android.support.annotation.Nullable;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import sy.com.initproject.R;
import sy.com.initproject.databinding.JokeListItemBinding;
import sy.com.initproject.root.models.JokeBean;
import sy.com.initproject.root.ui.category.adapter.BaseCategoryAdapter;

/**
 * Description:
 * Data：2018/7/16-12:48
 *
 * @author Administrator
 */
public class JokeAdapter extends BaseCategoryAdapter<JokeBean, JokeAdapter.JokeViewHolder> {


    public JokeAdapter(@Nullable List<JokeBean> data) {
        super(R.layout.joke_list_item, data);
    }

    @Override
    protected void convert(JokeViewHolder helper, JokeBean item) {
        helper.bindViewData(item);
    }


    public class JokeViewHolder extends BaseViewHolder {

        private JokeListItemBinding binding;

        public JokeViewHolder(View view) {
            super(view);
            binding = JokeListItemBinding.bind(view);
        }

        void bindViewData(JokeBean item) {
            binding.setBean(item);
            binding.executePendingBindings();

            Glide.with(mContext)
                    .load(item.getBimageuri())
                    .apply(new RequestOptions()
                            .centerCrop()
                            .error(R.drawable.ic_place_holder)
                            .placeholder(R.drawable.ic_place_holder)
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .thumbnail(0.1f)
                    .into(binding.coverIv);
        }
    }
}
