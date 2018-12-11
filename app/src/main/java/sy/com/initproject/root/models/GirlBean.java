package sy.com.initproject.root.models;

import java.io.Serializable;

/**
 * @date：2018/7/18
 * @author: SyShare
 */
public class GirlBean implements Serializable{


    /**
     * createdAt : 2017-11-23T08:32:29.546Z
     * publishedAt : 2017-11-24T11:08:03.624Z
     * type : 美图
     * url : http://7xi8d6.com1.z0.glb.clouddn.com/20171123083218_5mhRLg_sakura.gun_23_11_2017_8_32_9_312.jpeg
     */

    private String createdAt;
    private String publishedAt;
    private String type;
    private String url;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPublishedAt() {
        return "发布于： " + publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
