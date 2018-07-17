package sy.com.initproject.root.ui.novel;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import sy.com.initproject.root.models.JokeBean;
import sy.com.initproject.root.ui.category.adapter.BaseCategoryAdapter;

/**
 * Description:
 * Data：2018/7/16-12:48
 * @author Administrator
 */
public class NovelAdapter extends BaseCategoryAdapter<JokeBean, NovelAdapter.NewsViewHolder> {


    public NovelAdapter(@Nullable List<JokeBean> data) {
        super(1, data);
    }

    @Override
    protected void convert(NewsViewHolder helper, JokeBean item) {

    }


    public class NewsViewHolder extends BaseViewHolder {

        public NewsViewHolder(View view) {
            super(view);
        }
    }
}
