package viewmodel

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.support.annotation.NonNull
import com.baseproject.architecture.BaseLiveDataObserver
import com.baseproject.architecture.BaseViewModel
import sy.com.initproject.root.api.ApiService
import sy.com.initproject.root.models.GirlBean
import sy.com.initproject.root.models.JokeBean
import sy.com.initproject.root.models.NewsBean
import sy.com.lib_http.bean.BaseResponse

/**
 * @Description:
 * @Data：2018/7/16-15:02
 * @author: SyShare
 */
class HomeViewModel(@NonNull application: Application) : BaseViewModel<ApiService>(application) {


    /**
     * 段子列表请求
     */
    val jokeModel by lazy { MutableLiveData<BaseResponse<List<JokeBean>>>() }

    fun getRecommondJokes(page: Int) {
        mService.getNovelList(1, page)
                .doOnSubscribe { this.add(it) }
                .subscribe(BaseLiveDataObserver(false, jokeModel))
    }


    /**
     * 新闻列表请求
     */
    val newsModel by lazy { MutableLiveData<BaseResponse<NewsBean>>() }

    fun getRecommondNews() {
        mService.newsList
                .doOnSubscribe { this.add(it) }
                .subscribe(BaseLiveDataObserver(false, newsModel))
    }

    /**
     * 美女列表请求
     */
    val girlModel by lazy { MutableLiveData<BaseResponse<List<GirlBean>>>() }

    fun getBeautyList(page: Int) {
        mService.getBeautyList(page)
                .doOnSubscribe { this.add(it) }
                .subscribe(BaseLiveDataObserver(false, girlModel))
    }
}