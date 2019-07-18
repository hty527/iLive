package com.android.gift.room.bean;

import android.text.TextUtils;
import java.util.List;

/**
 * Created by TinyHung@outlook.com
 * 2019/7/17
 */

public class InkeRoomItem {

    public static final int ITEM_TYPE_UNKNOWN = 0;
    public static final int ITEM_TYPE_ROOM = 1;
    public static final int ITEM_TYPE_BANNER = 2;
    public static final int ITEM_TYPE_WEB = 3;

    /**
     * card_type : 0
     * cover_type : 1000
     * style : 1
     * tabkey :
     * tags : {"posa":{"end_color":"7EB8F3","icon":"http://img.ikstatic.cn/MTU0MzM5MDE0NTc5OCM5NjgjanBn.jpg","is_dynamic":0,"start_color":"2171FF","text":"福建工程学院","type":"weak"},"posb":{"text":"福州市"},"posc":{"text":"🤞🏻🤞🏻🤞🏻"}}
     */

    private CoverBean cover;
    /**
     * live_info : {"city":"","creator":{"albums":["http://img.ikstatic.cn/MTU1NzQxMTk4NDQ3MiMzODIjanBn.jpg","http://img.ikstatic.cn/MTU1NTE0NDQ5MiM2MTI=.jpg","http://img.ikstatic.cn/MTU1Nzc0MDcwOTYyNSMzNjQjanBn.jpg"],"birth":"2019-04-05","current_value":"263456","description":"愿有人待你如初，疼你入骨，从此深情不被辜负☺️","emotion":"保密","gender":0,"gmutex":0,"hometown":"福建省&漳州市","id":458049062,"inke_verify":1,"level":56,"location":"福州市","next_diff":"17104","nick":"✨小懒宝✨","portrait":"http://img.ikstatic.cn/MTU1ODE2NzUzNzQ4MyM5ODUjanBn.jpg","profession":"自由职业者","rank_veri":203,"register_at":1493701448,"sex":0,"third_platform":"0","veri_info":"校园频道 百变甜心","verified":203,"verified_prefix":"认证:","verified_reason":"校园频道 百变甜心","verify_list":[{"expire_at":32503651199,"expire_at_str":"","id":203,"is_selected":true,"reason":"校园频道 百变甜心","type":"channel","verified_prefix":""}]},"end_time":0,"extra":{"label":[]},"gps_position":"119.270173,26.045831","group":0,"id":"1563299477033542","image":"","landscape":0,"link":0,"live_type":"","location":"CN,福建省,福州市","mode":0,"multi":1,"name":"🤞🏻🤞🏻🤞🏻","online_users":50830,"optimal":0,"pub_stat":1,"req_source":0,"room":{"annoncement":"","cover":"","cover_check":"","cover_status":0,"id":0,"liveid":"","name":"","owner":0,"playid":0,"show_room_id":0,"status":0,"title":""},"room_id":123456789,"rotate":0,"share_addr":"https://mlive2.inke.cn/app/hot/live?uid=458049062&liveid=1563299477033542&ctime=1563299477","slot":1,"start_time":1563299582,"status":1,"stream_addr":"http://csysource.rtc.inke.cn/live/1563299477033542_0.flv?ikDnsOp=1001&ikHost=csy&ikOp=0&codecInfo=8192&ikLog=1&ikSyncBetaxyz=0&ikChorus=1&dpSrc=6&push_host=push.cls.inke.cn&msUid=727999310&msSmid=DuamVqMG5xcuuYAnMcSVH8Kw7WRf9sVErfBG8Vmhh7yq%2F2kg08c3Idf%2B%2Frtw505JFR5mLMInwcODBqHVv%2FFv2xNg&ikMinBuf=2900&ikMaxBuf=3600&ikSlowRate=0.9&ikFastRate=1.1","sub_live_type":"","token":"rec_7_3_6_2^727999310_1563327606366_1^0^111|10001","version":0}
     * pos : -1
     * redirect_type : live
     * score : 2
     */

    private InkeRoomInfo data;


    //Banner类型
    private String itemCategory;
    private int itemType;
    //广告
    //BANNER、宽
    private String width="726";
    //BANNER、高
    private String height="200";
    private List<BannerInfo> banners;

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public List<BannerInfo> getBanners() {
        return banners;
    }

    public void setBanners(List<BannerInfo> banners) {
        this.banners = banners;
    }

    public CoverBean getCover() {
        return cover;
    }

    public void setCover(CoverBean cover) {
        this.cover = cover;
    }

    public InkeRoomInfo getData() {
        return data;
    }

    public void setData(InkeRoomInfo data) {
        this.data = data;
    }


    public int getItemType() {
        //自定义Banner
        if(!TextUtils.isEmpty(itemCategory)){
            if(itemCategory.equals("2")){
                itemType=ITEM_TYPE_BANNER;
            }
            return itemType;
        }
        if(null!=getData()){
            if("live".equals(getData().getRedirect_type())){
                itemType=ITEM_TYPE_ROOM;
            }else if("web".equals(getData().getRedirect_type())){
                itemType=ITEM_TYPE_WEB;
            }
        }
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public static class CoverBean {
        private int card_type;
        private int cover_type;
        private int style;
        private String tabkey;
        /**
         * posa : {"end_color":"7EB8F3","icon":"http://img.ikstatic.cn/MTU0MzM5MDE0NTc5OCM5NjgjanBn.jpg","is_dynamic":0,"start_color":"2171FF","text":"福建工程学院","type":"weak"}
         * posb : {"text":"福州市"}
         * posc : {"text":"🤞🏻🤞🏻🤞🏻"}
         */

        private TagsBean tags;

        public int getCard_type() {
            return card_type;
        }

        public void setCard_type(int card_type) {
            this.card_type = card_type;
        }

        public int getCover_type() {
            return cover_type;
        }

        public void setCover_type(int cover_type) {
            this.cover_type = cover_type;
        }

        public int getStyle() {
            return style;
        }

        public void setStyle(int style) {
            this.style = style;
        }

        public String getTabkey() {
            return tabkey;
        }

        public void setTabkey(String tabkey) {
            this.tabkey = tabkey;
        }

        public TagsBean getTags() {
            return tags;
        }

        public void setTags(TagsBean tags) {
            this.tags = tags;
        }

        public static class TagsBean {
            /**
             * end_color : 7EB8F3
             * icon : http://img.ikstatic.cn/MTU0MzM5MDE0NTc5OCM5NjgjanBn.jpg
             * is_dynamic : 0
             * start_color : 2171FF
             * text : 福建工程学院
             * type : weak
             */

            private PosaBean posa;
            /**
             * text : 福州市
             */

            private PosbBean posb;
            /**
             * text : 🤞🏻🤞🏻🤞🏻
             */

            private PoscBean posc;

            public PosaBean getPosa() {
                return posa;
            }

            public void setPosa(PosaBean posa) {
                this.posa = posa;
            }

            public PosbBean getPosb() {
                return posb;
            }

            public void setPosb(PosbBean posb) {
                this.posb = posb;
            }

            public PoscBean getPosc() {
                return posc;
            }

            public void setPosc(PoscBean posc) {
                this.posc = posc;
            }

            public static class PosaBean {
                private String end_color;
                private String icon;
                private int is_dynamic;
                private String start_color;
                private String text;
                private String type;

                public String getEnd_color() {
                    return end_color;
                }

                public void setEnd_color(String end_color) {
                    this.end_color = end_color;
                }

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }

                public int getIs_dynamic() {
                    return is_dynamic;
                }

                public void setIs_dynamic(int is_dynamic) {
                    this.is_dynamic = is_dynamic;
                }

                public String getStart_color() {
                    return start_color;
                }

                public void setStart_color(String start_color) {
                    this.start_color = start_color;
                }

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }

            public static class PosbBean {
                private String text;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }
            }

            public static class PoscBean {
                private String text;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }
            }
        }
    }
}