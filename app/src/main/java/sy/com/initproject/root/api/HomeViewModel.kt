package sy.com.initproject.root.api

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.support.annotation.NonNull
import sy.com.initproject.root.models.GirlBean
import sy.com.initproject.root.models.JokeBean
import sy.com.initproject.root.models.NewsBean
import sy.com.lib_http.arch.ApiObserver
import sy.com.lib_http.arch.BaseViewModel
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
      return  mService.getNovelList(1, page)
                .doOnSubscribe { this.add(it) }
                .subscribe(object : ApiObserver<BaseResponse<List<JokeBean>>>(){
                    override fun onSuccess(resp: BaseResponse<List<JokeBean>>) {
                        jokeModel.value = resp
                    }

                    override fun onFail(throwable: Throwable?) {
                        super.onFail(throwable)
                        jokeModel.value = null
                    }


                })
    }


    /**
     * 新闻列表请求
     */
    val newsModel by lazy { MutableLiveData<BaseResponse<NewsBean>>() }

    fun getRecommondNews() {
        mService.newsList
                .doOnSubscribe { this.add(it) }
                .subscribe(object : ApiObserver<BaseResponse<NewsBean>>(){
                    override fun onSuccess(resp: BaseResponse<NewsBean>) {
                        newsModel.value = resp
                    }

                    override fun onFail(throwable: Throwable?) {
                        super.onFail(throwable)
                        newsModel.value = null
                    }


                })
    }

    /**
     * 美女列表请求
     */
    val girlModel by lazy { MutableLiveData<BaseResponse<List<GirlBean>>>() }

    fun getBeautyList(page: Int) {
        mService.getBeautyList(page)
                .doOnSubscribe { this.add(it) }
                .subscribe(object : ApiObserver<BaseResponse<List<GirlBean>>>(){
                    override fun onSuccess(resp: BaseResponse<List<GirlBean>>) {
                        girlModel.value = resp
                    }

                    override fun onFail(throwable: Throwable?) {
                        super.onFail(throwable)
                        girlModel.value = null
                    }


                })
    }
}