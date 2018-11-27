package sy.com.initproject.root.ui.news;

import android.support.v7.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.pince.frame.mvp.IBasePresenter;
import com.pince.frame.mvp.IBaseView;

import java.util.List;

import sy.com.initproject.root.models.NewsBean;

/**
 * @author syb
 * @date 2018/5/10
 */

public interface IMainContact {

    interface IView extends IBaseView {

        /**
         * 跳转WebView视图
         * @param bean
         */
        void jumpWeb(NewsBean.CommonBean bean);

    }

    interface IPresenter extends IBasePresenter<IView> {
        /**
         * 初始化RecyclerView
         *
         * @param recyclerView
         * @return
         */
        DelegateAdapter initRecyclerView(RecyclerView recyclerView);

        /**
         * Banner
         *
         * @param stringList
         * @return
         */
        BaseDelegateAdapter initBanner(List<String> stringList);

        /**
         * 公共title
         *
         * @param title
         * @return
         */
         BaseDelegateAdapter initCommonTitle(String title);

        /**
         * 教育
         *
         * @param teachList
         * @return
         */
        BaseDelegateAdapter initListAdapter(List<NewsBean.CommonBean> teachList);

    }
}
