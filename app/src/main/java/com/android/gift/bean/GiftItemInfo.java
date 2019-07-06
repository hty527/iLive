package com.android.gift.bean;

/**
 * hty_Yuye@Outlook.com
 * 2019/5/12
 */

public class GiftItemInfo {

    /**
     * addtime : 0
     * begin_discount : -1
     * big_svga : http://a.197754.com/uploads/images/20181108/e04d4918c5ef6031fb86f05c4804d6ab.svga
     * commision : 1
     * desp : 向主播开炮！！！主播可以获得666666积分
     * discount : 0
     * end_discount : -1
     * gift_category : 0
     * gift_type : 3
     * id : 7650
     * is_hot : 0
     * is_wawa : 0
     * price : 666666
     * scene_api_type : 0
     * sort : 99
     * source : 0
     * src : http://a.tn990.com/uploads/images/20181108/584246c7e9dccdaac0a4d1c1ddbe05a0.png
     * state : 0
     * svga : http://a.tn990.com/uploads/images/20181108/4754d1d04cd649725341d7c7ced06c4b.svga
     * tag : 新
     * title : 打炮
     */

    private int addtime;
    private int begin_discount;
    private String big_svga;
    private double commision;
    private String desp;
    private int discount;
    private int end_discount;
    private int gift_category;
    private int gift_type;
    private int id;
    private int is_hot;
    private int is_wawa;
    private int price;
    private int scene_api_type;
    private int sort;
    private String source;
    private String src;
    private int state;
    private String svga;
    private String tag;
    private String title;
    private int drawTimes;//中奖倍数

    //选中的礼物个数
    private int count;
    //礼物发出的源房间ID
    private String source_room_id;

    public int getAddtime() {
        return addtime;
    }

    public void setAddtime(int addtime) {
        this.addtime = addtime;
    }

    public int getBegin_discount() {
        return begin_discount;
    }

    public void setBegin_discount(int begin_discount) {
        this.begin_discount = begin_discount;
    }

    public String getBig_svga() {
        return big_svga;
    }

    public void setBig_svga(String big_svga) {
        this.big_svga = big_svga;
    }

    public double getCommision() {
        return commision;
    }

    public void setCommision(double commision) {
        this.commision = commision;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getEnd_discount() {
        return end_discount;
    }

    public void setEnd_discount(int end_discount) {
        this.end_discount = end_discount;
    }

    public int getGift_category() {
        return gift_category;
    }

    public void setGift_category(int gift_category) {
        this.gift_category = gift_category;
    }

    public int getGift_type() {
        return gift_type;
    }

    public void setGift_type(int gift_type) {
        this.gift_type = gift_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(int is_hot) {
        this.is_hot = is_hot;
    }

    public int getIs_wawa() {
        return is_wawa;
    }

    public void setIs_wawa(int is_wawa) {
        this.is_wawa = is_wawa;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getScene_api_type() {
        return scene_api_type;
    }

    public void setScene_api_type(int scene_api_type) {
        this.scene_api_type = scene_api_type;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getSvga() {
        return svga;
    }

    public void setSvga(String svga) {
        this.svga = svga;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDrawTimes() {
        return drawTimes;
    }

    public void setDrawTimes(int drawTimes) {
        this.drawTimes = drawTimes;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getSource_room_id() {
        return source_room_id;
    }

    public void setSource_room_id(String source_room_id) {
        this.source_room_id = source_room_id;
    }

    @Override
    public String toString() {
        return "GiftItemInfo{" +
                "addtime=" + addtime +
                ", begin_discount=" + begin_discount +
                ", big_svga='" + big_svga + '\'' +
                ", commision=" + commision +
                ", desp='" + desp + '\'' +
                ", discount=" + discount +
                ", end_discount=" + end_discount +
                ", gift_category=" + gift_category +
                ", gift_type=" + gift_type +
                ", id=" + id +
                ", is_hot=" + is_hot +
                ", is_wawa=" + is_wawa +
                ", price=" + price +
                ", scene_api_type=" + scene_api_type +
                ", sort=" + sort +
                ", source='" + source + '\'' +
                ", src='" + src + '\'' +
                ", state=" + state +
                ", svga='" + svga + '\'' +
                ", tag='" + tag + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
