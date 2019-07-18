package com.android.gift.room.bean;

import java.util.List;

/**
 * Created by TinyHung@outlook.com
 * 2019/7/17
 */

public class InkeRoomInfo {

    /**
     * city :
     * creator : {"albums":["http://img.ikstatic.cn/MTU1NzQxMTk4NDQ3MiMzODIjanBn.jpg","http://img.ikstatic.cn/MTU1NTE0NDQ5MiM2MTI=.jpg","http://img.ikstatic.cn/MTU1Nzc0MDcwOTYyNSMzNjQjanBn.jpg"],"birth":"2019-04-05","current_value":"263456","description":"愿有人待你如初，疼你入骨，从此深情不被辜负☺️","emotion":"保密","gender":0,"gmutex":0,"hometown":"福建省&漳州市","id":458049062,"inke_verify":1,"level":56,"location":"福州市","next_diff":"17104","nick":"✨小懒宝✨","portrait":"http://img.ikstatic.cn/MTU1ODE2NzUzNzQ4MyM5ODUjanBn.jpg","profession":"自由职业者","rank_veri":203,"register_at":1493701448,"sex":0,"third_platform":"0","veri_info":"校园频道 百变甜心","verified":203,"verified_prefix":"认证:","verified_reason":"校园频道 百变甜心","verify_list":[{"expire_at":32503651199,"expire_at_str":"","id":203,"is_selected":true,"reason":"校园频道 百变甜心","type":"channel","verified_prefix":""}]}
     * end_time : 0
     * extra : {"label":[]}
     * gps_position : 119.270173,26.045831
     * group : 0
     * id : 1563299477033542
     * image :
     * landscape : 0
     * link : 0
     * live_type :
     * location : CN,福建省,福州市
     * mode : 0
     * multi : 1
     * name : 🤞🏻🤞🏻🤞🏻
     * online_users : 50830
     * optimal : 0
     * pub_stat : 1
     * req_source : 0
     * room : {"annoncement":"","cover":"","cover_check":"","cover_status":0,"id":0,"liveid":"","name":"","owner":0,"playid":0,"show_room_id":0,"status":0,"title":""}
     * room_id : 123456789
     * rotate : 0
     * share_addr : https://mlive2.inke.cn/app/hot/live?uid=458049062&liveid=1563299477033542&ctime=1563299477
     * slot : 1
     * start_time : 1563299582
     * status : 1
     * stream_addr : http://csysource.rtc.inke.cn/live/1563299477033542_0.flv?ikDnsOp=1001&ikHost=csy&ikOp=0&codecInfo=8192&ikLog=1&ikSyncBetaxyz=0&ikChorus=1&dpSrc=6&push_host=push.cls.inke.cn&msUid=727999310&msSmid=DuamVqMG5xcuuYAnMcSVH8Kw7WRf9sVErfBG8Vmhh7yq%2F2kg08c3Idf%2B%2Frtw505JFR5mLMInwcODBqHVv%2FFv2xNg&ikMinBuf=2900&ikMaxBuf=3600&ikSlowRate=0.9&ikFastRate=1.1
     * sub_live_type :
     * token : rec_7_3_6_2^727999310_1563327606366_1^0^111|10001
     * version : 0
     */

    private LiveRoomInfo live_info;
    private int pos;
    private String redirect_type;
    private int score;
    //当ITEM数据为空，取这里的数据
    private InkeChannel channel;
    private List<InkeWebItem> ticker;

    public LiveRoomInfo getLive_info() {
        return live_info;
    }

    public void setLive_info(LiveRoomInfo live_info) {
        this.live_info = live_info;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getRedirect_type() {
        return redirect_type;
    }

    public void setRedirect_type(String redirect_type) {
        this.redirect_type = redirect_type;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public InkeChannel getChannel() {
        return channel;
    }

    public void setChannel(InkeChannel channel) {
        this.channel = channel;
    }

    public List<InkeWebItem> getTicker() {
        return ticker;
    }

    public void setTicker(List<InkeWebItem> ticker) {
        this.ticker = ticker;
    }
}