package sy.com.initproject.root.api;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import sy.com.initproject.root.models.GirlBean;
import sy.com.initproject.root.models.JokeBean;
import sy.com.initproject.root.models.NewsBean;
import sy.com.lib_http.bean.BaseResponse;

/**
 * @Description:
 * @Data：2018/7/16-15:58
 * @author: SyShare
 */
public interface ApiService {


    /**
     * 热门段子
     *
     * @param type
     * @param page
     * @return
     */
    @GET("/satinApi")
    Observable<BaseResponse<List<JokeBean>>> getNovelList(@Query("type") int type, @Query("page") int page);


    /**
     * 获取新闻列表
     * @return
     */
    @GET("/journalismApi")
    Observable<BaseResponse<NewsBean>> getNewsList();


    /**
     * 获取美女列表
     * @return
     */
    @GET("/meituApi")
    Observable<BaseResponse<List<GirlBean>>> getBeautyList(@Query("page") int page);
}
