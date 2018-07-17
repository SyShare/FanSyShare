package sy.com.initproject.root.models;

import java.util.List;

/**
 * @description:
 * @data：2018/7/17
 * @author: SyShare
 */
public class NewsBean {

    private List<CommonBean> tech;
    private List<CommonBean> auto;
    private List<CommonBean> money;
    private List<CommonBean> sports;
    private List<CommonBean> dy;
    private List<CommonBean> war;
    private List<CommonBean> ent;
    private List<CommonBean> toutiao;

    public List<CommonBean> getTech() {
        return tech;
    }

    public void setTech(List<CommonBean> tech) {
        this.tech = tech;
    }

    public List<CommonBean> getAuto() {
        return auto;
    }

    public void setAuto(List<CommonBean> auto) {
        this.auto = auto;
    }

    public List<CommonBean> getMoney() {
        return money;
    }

    public void setMoney(List<CommonBean> money) {
        this.money = money;
    }

    public List<CommonBean> getSports() {
        return sports;
    }

    public void setSports(List<CommonBean> sports) {
        this.sports = sports;
    }

    public List<CommonBean> getDy() {
        return dy;
    }

    public void setDy(List<CommonBean> dy) {
        this.dy = dy;
    }

    public List<CommonBean> getWar() {
        return war;
    }

    public void setWar(List<CommonBean> war) {
        this.war = war;
    }

    public List<CommonBean> getEnt() {
        return ent;
    }

    public void setEnt(List<CommonBean> ent) {
        this.ent = ent;
    }

    public List<CommonBean> getToutiao() {
        return toutiao;
    }

    public void setToutiao(List<CommonBean> toutiao) {
        this.toutiao = toutiao;
    }

    public static class CommonBean {
        /**
         * liveInfo : null
         * tcount : 26
         * picInfo : [{"ref":null,"width":null,"url":"http://cms-bucket.nosdn.127.net/2018/07/17/7472225402bb42fdb8fb92a2b3731bd2.png","height":null}]
         * docid : DMU4IU0T00097U7R
         * videoInfo : null
         * channel : tech
         * link : https://3g.163.com/all/special/S1489643035418.html
         * source : 参考消息网
         * title : 在柏林试水3个月后，ofo宣布暂时退出德国市场
         * type : special
         * imgsrcFrom : null
         * imgsrc3gtype : 1
         * unlikeReason : 重复、旧闻/6,内容质量差/6
         * digest : 参考消息网7月17日报道德媒称，中国共享单车ofo小黄车在柏
         * typeid : S1489643035418
         * addata : null
         * tag : 专题
         * category : 科技
         * ptime : 2018-07-17 14:52:46
         */

        private Object liveInfo;
        private int tcount;
        private String docid;
        private Object videoInfo;
        private String channel;
        private String link;
        private String source;
        private String title;
        private String type;
        private Object imgsrcFrom;
        private int imgsrc3gtype;
        private String unlikeReason;
        private String digest;
        private String typeid;
        private Object addata;
        private String tag;
        private String category;
        private String ptime;
        private List<PicInfoBean> picInfo;

        public Object getLiveInfo() {
            return liveInfo;
        }

        public void setLiveInfo(Object liveInfo) {
            this.liveInfo = liveInfo;
        }

        public int getTcount() {
            return tcount;
        }

        public void setTcount(int tcount) {
            this.tcount = tcount;
        }

        public String getDocid() {
            return docid;
        }

        public void setDocid(String docid) {
            this.docid = docid;
        }

        public Object getVideoInfo() {
            return videoInfo;
        }

        public void setVideoInfo(Object videoInfo) {
            this.videoInfo = videoInfo;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getSource() {
            return "来源" + source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Object getImgsrcFrom() {
            return imgsrcFrom;
        }

        public void setImgsrcFrom(Object imgsrcFrom) {
            this.imgsrcFrom = imgsrcFrom;
        }

        public int getImgsrc3gtype() {
            return imgsrc3gtype;
        }

        public void setImgsrc3gtype(int imgsrc3gtype) {
            this.imgsrc3gtype = imgsrc3gtype;
        }

        public String getUnlikeReason() {
            return unlikeReason;
        }

        public void setUnlikeReason(String unlikeReason) {
            this.unlikeReason = unlikeReason;
        }

        public String getDigest() {
            return digest;
        }

        public void setDigest(String digest) {
            this.digest = digest;
        }

        public String getTypeid() {
            return typeid;
        }

        public void setTypeid(String typeid) {
            this.typeid = typeid;
        }

        public Object getAddata() {
            return addata;
        }

        public void setAddata(Object addata) {
            this.addata = addata;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getPtime() {
            return ptime;
        }

        public void setPtime(String ptime) {
            this.ptime = ptime;
        }

        public List<PicInfoBean> getPicInfo() {
            return picInfo;
        }

        public void setPicInfo(List<PicInfoBean> picInfo) {
            this.picInfo = picInfo;
        }

        public static class PicInfoBean {
            /**
             * ref : null
             * width : null
             * url : http://cms-bucket.nosdn.127.net/2018/07/17/7472225402bb42fdb8fb92a2b3731bd2.png
             * height : null
             */

            private Object ref;
            private Object width;
            private String url;
            private Object height;

            public Object getRef() {
                return ref;
            }

            public void setRef(Object ref) {
                this.ref = ref;
            }

            public Object getWidth() {
                return width;
            }

            public void setWidth(Object width) {
                this.width = width;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public Object getHeight() {
                return height;
            }

            public void setHeight(Object height) {
                this.height = height;
            }
        }
    }


}
