package com.android.gift.room.bean;

import java.util.List;

/**
 * Created by TinyHung@outlook.com
 * 2019/7/17
 */

public class LiveRoomInfo {

    private String city;
    /**
     * albums : ["http://img.ikstatic.cn/MTU1NzQxMTk4NDQ3MiMzODIjanBn.jpg","http://img.ikstatic.cn/MTU1NTE0NDQ5MiM2MTI=.jpg","http://img.ikstatic.cn/MTU1Nzc0MDcwOTYyNSMzNjQjanBn.jpg"]
     * birth : 2019-04-05
     * current_value : 263456
     * description : 愿有人待你如初，疼你入骨，从此深情不被辜负☺️
     * emotion : 保密
     * gender : 0
     * gmutex : 0
     * hometown : 福建省&漳州市
     * id : 458049062
     * inke_verify : 1
     * level : 56
     * location : 福州市
     * next_diff : 17104
     * nick : ✨小懒宝✨
     * portrait : http://img.ikstatic.cn/MTU1ODE2NzUzNzQ4MyM5ODUjanBn.jpg
     * profession : 自由职业者
     * rank_veri : 203
     * register_at : 1493701448
     * sex : 0
     * third_platform : 0
     * veri_info : 校园频道 百变甜心
     * verified : 203
     * verified_prefix : 认证:
     * verified_reason : 校园频道 百变甜心
     * verify_list : [{"expire_at":32503651199,"expire_at_str":"","id":203,"is_selected":true,"reason":"校园频道 百变甜心","type":"channel","verified_prefix":""}]
     */

    private CreatorBean creator;
    private int end_time;
    private ExtraBean extra;
    private String gps_position;
    private int group;
    private String id;
    private String image;
    private int landscape;
    private int link;
    private String live_type;
    private String location;
    private int mode;
    private int multi;
    private String name;
    private int online_users;
    private int optimal;
    private int pub_stat;
    private int req_source;
    /**
     * annoncement :
     * cover :
     * cover_check :
     * cover_status : 0
     * id : 0
     * liveid :
     * name :
     * owner : 0
     * playid : 0
     * show_room_id : 0
     * status : 0
     * title :
     */

    private RoomBean room;
    private int room_id;
    private int rotate;
    private String share_addr;
    private int slot;
    private int start_time;
    private int status;
    private String stream_addr;
    private String sub_live_type;
    private String token;
    private int version;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public CreatorBean getCreator() {
        return creator;
    }

    public void setCreator(CreatorBean creator) {
        this.creator = creator;
    }

    public int getEnd_time() {
        return end_time;
    }

    public void setEnd_time(int end_time) {
        this.end_time = end_time;
    }

    public ExtraBean getExtra() {
        return extra;
    }

    public void setExtra(ExtraBean extra) {
        this.extra = extra;
    }

    public String getGps_position() {
        return gps_position;
    }

    public void setGps_position(String gps_position) {
        this.gps_position = gps_position;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getLandscape() {
        return landscape;
    }

    public void setLandscape(int landscape) {
        this.landscape = landscape;
    }

    public int getLink() {
        return link;
    }

    public void setLink(int link) {
        this.link = link;
    }

    public String getLive_type() {
        return live_type;
    }

    public void setLive_type(String live_type) {
        this.live_type = live_type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getMulti() {
        return multi;
    }

    public void setMulti(int multi) {
        this.multi = multi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOnline_users() {
        return online_users;
    }

    public void setOnline_users(int online_users) {
        this.online_users = online_users;
    }

    public int getOptimal() {
        return optimal;
    }

    public void setOptimal(int optimal) {
        this.optimal = optimal;
    }

    public int getPub_stat() {
        return pub_stat;
    }

    public void setPub_stat(int pub_stat) {
        this.pub_stat = pub_stat;
    }

    public int getReq_source() {
        return req_source;
    }

    public void setReq_source(int req_source) {
        this.req_source = req_source;
    }

    public RoomBean getRoom() {
        return room;
    }

    public void setRoom(RoomBean room) {
        this.room = room;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public int getRotate() {
        return rotate;
    }

    public void setRotate(int rotate) {
        this.rotate = rotate;
    }

    public String getShare_addr() {
        return share_addr;
    }

    public void setShare_addr(String share_addr) {
        this.share_addr = share_addr;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStream_addr() {
        return stream_addr;
    }

    public void setStream_addr(String stream_addr) {
        this.stream_addr = stream_addr;
    }

    public String getSub_live_type() {
        return sub_live_type;
    }

    public void setSub_live_type(String sub_live_type) {
        this.sub_live_type = sub_live_type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public static class CreatorBean {

        private String birth;
        private String current_value;
        private String description;
        private String emotion;
        private int gender;
        private int gmutex;
        private String hometown;
        private int id;
        private int inke_verify;
        private int level;
        private String location;
        private String next_diff;
        private String nick;
        private String portrait;
        private String profession;
        private int rank_veri;
        private int register_at;
        private int sex;
        private String third_platform;
        private String veri_info;
        private int verified;
        private String verified_prefix;
        private String verified_reason;
        private List<String> albums;
        /**
         * expire_at : 32503651199
         * expire_at_str :
         * id : 203
         * is_selected : true
         * reason : 校园频道 百变甜心
         * type : channel
         * verified_prefix :
         */

        private List<VerifyListBean> verify_list;

        public String getBirth() {
            return birth;
        }

        public void setBirth(String birth) {
            this.birth = birth;
        }

        public String getCurrent_value() {
            return current_value;
        }

        public void setCurrent_value(String current_value) {
            this.current_value = current_value;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getEmotion() {
            return emotion;
        }

        public void setEmotion(String emotion) {
            this.emotion = emotion;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getGmutex() {
            return gmutex;
        }

        public void setGmutex(int gmutex) {
            this.gmutex = gmutex;
        }

        public String getHometown() {
            return hometown;
        }

        public void setHometown(String hometown) {
            this.hometown = hometown;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getInke_verify() {
            return inke_verify;
        }

        public void setInke_verify(int inke_verify) {
            this.inke_verify = inke_verify;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getNext_diff() {
            return next_diff;
        }

        public void setNext_diff(String next_diff) {
            this.next_diff = next_diff;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public int getRank_veri() {
            return rank_veri;
        }

        public void setRank_veri(int rank_veri) {
            this.rank_veri = rank_veri;
        }

        public int getRegister_at() {
            return register_at;
        }

        public void setRegister_at(int register_at) {
            this.register_at = register_at;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getThird_platform() {
            return third_platform;
        }

        public void setThird_platform(String third_platform) {
            this.third_platform = third_platform;
        }

        public String getVeri_info() {
            return veri_info;
        }

        public void setVeri_info(String veri_info) {
            this.veri_info = veri_info;
        }

        public int getVerified() {
            return verified;
        }

        public void setVerified(int verified) {
            this.verified = verified;
        }

        public String getVerified_prefix() {
            return verified_prefix;
        }

        public void setVerified_prefix(String verified_prefix) {
            this.verified_prefix = verified_prefix;
        }

        public String getVerified_reason() {
            return verified_reason;
        }

        public void setVerified_reason(String verified_reason) {
            this.verified_reason = verified_reason;
        }

        public List<String> getAlbums() {
            return albums;
        }

        public void setAlbums(List<String> albums) {
            this.albums = albums;
        }

        public List<VerifyListBean> getVerify_list() {
            return verify_list;
        }

        public void setVerify_list(List<VerifyListBean> verify_list) {
            this.verify_list = verify_list;
        }

        @Override
        public String toString() {
            return "CreatorBean{" +
                    "birth='" + birth + '\'' +
                    ", current_value='" + current_value + '\'' +
                    ", description='" + description + '\'' +
                    ", emotion='" + emotion + '\'' +
                    ", gender=" + gender +
                    ", gmutex=" + gmutex +
                    ", hometown='" + hometown + '\'' +
                    ", id=" + id +
                    ", inke_verify=" + inke_verify +
                    ", level=" + level +
                    ", location='" + location + '\'' +
                    ", next_diff='" + next_diff + '\'' +
                    ", nick='" + nick + '\'' +
                    ", portrait='" + portrait + '\'' +
                    ", profession='" + profession + '\'' +
                    ", rank_veri=" + rank_veri +
                    ", register_at=" + register_at +
                    ", sex=" + sex +
                    ", third_platform='" + third_platform + '\'' +
                    ", veri_info='" + veri_info + '\'' +
                    ", verified=" + verified +
                    ", verified_prefix='" + verified_prefix + '\'' +
                    ", verified_reason='" + verified_reason + '\'' +
                    ", albums=" + albums +
                    ", verify_list=" + verify_list +
                    '}';
        }

        public static class VerifyListBean {
            private long expire_at;
            private String expire_at_str;
            private int id;
            private boolean is_selected;
            private String reason;
            private String type;
            private String verified_prefix;

            public long getExpire_at() {
                return expire_at;
            }

            public void setExpire_at(long expire_at) {
                this.expire_at = expire_at;
            }

            public String getExpire_at_str() {
                return expire_at_str;
            }

            public void setExpire_at_str(String expire_at_str) {
                this.expire_at_str = expire_at_str;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public boolean isIs_selected() {
                return is_selected;
            }

            public void setIs_selected(boolean is_selected) {
                this.is_selected = is_selected;
            }

            public String getReason() {
                return reason;
            }

            public void setReason(String reason) {
                this.reason = reason;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getVerified_prefix() {
                return verified_prefix;
            }

            public void setVerified_prefix(String verified_prefix) {
                this.verified_prefix = verified_prefix;
            }
        }
    }

    public static class ExtraBean {
        private List<?> label;

        public List<?> getLabel() {
            return label;
        }

        public void setLabel(List<?> label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return "ExtraBean{" +
                    "label=" + label +
                    '}';
        }
    }

    public static class RoomBean {
        private String annoncement;
        private String cover;
        private String cover_check;
        private int cover_status;
        private int id;
        private String liveid;
        private String name;
        private int owner;
        private int playid;
        private int show_room_id;
        private int status;
        private String title;

        public String getAnnoncement() {
            return annoncement;
        }

        public void setAnnoncement(String annoncement) {
            this.annoncement = annoncement;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getCover_check() {
            return cover_check;
        }

        public void setCover_check(String cover_check) {
            this.cover_check = cover_check;
        }

        public int getCover_status() {
            return cover_status;
        }

        public void setCover_status(int cover_status) {
            this.cover_status = cover_status;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLiveid() {
            return liveid;
        }

        public void setLiveid(String liveid) {
            this.liveid = liveid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getOwner() {
            return owner;
        }

        public void setOwner(int owner) {
            this.owner = owner;
        }

        public int getPlayid() {
            return playid;
        }

        public void setPlayid(int playid) {
            this.playid = playid;
        }

        public int getShow_room_id() {
            return show_room_id;
        }

        public void setShow_room_id(int show_room_id) {
            this.show_room_id = show_room_id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}