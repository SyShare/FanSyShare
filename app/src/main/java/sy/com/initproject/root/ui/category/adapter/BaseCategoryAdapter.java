package sy.com.initproject.root.ui.category.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author syb
 * @date 2018/5/14
 */

public abstract class BaseCategoryAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {


    public BaseCategoryAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    @Override
    protected abstract void convert(K helper, T item);
}
