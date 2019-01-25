package com.jal.calculator.store.ds.model.tbk;

import java.util.List;

/**
 * Created on 2019/1/25.
 *
 * @author Jie.Wu
 */
public class TBKFavoritesItemResp {

    private String event_end_time;
    private String event_start_time;
    private String item_url;
    private String nick;
    private long num_iid;
    private String pict_url;
    private String provcity;
    private String reserve_price;
    private long seller_id;
    private String shop_title;
    private long status;
    private String title;
    private String tk_rate;
    private long type;
    private long user_type;
    private long volume;
    private String zk_final_price;
    private String zk_final_price_wap;
    private List<String> small_images;

    public String getEvent_end_time() {
        return event_end_time;
    }

    public void setEvent_end_time(String event_end_time) {
        this.event_end_time = event_end_time;
    }

    public String getEvent_start_time() {
        return event_start_time;
    }

    public void setEvent_start_time(String event_start_time) {
        this.event_start_time = event_start_time;
    }

    public String getItem_url() {
        return item_url;
    }

    public void setItem_url(String item_url) {
        this.item_url = item_url;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public long getNum_iid() {
        return num_iid;
    }

    public void setNum_iid(long num_iid) {
        this.num_iid = num_iid;
    }

    public String getPict_url() {
        return pict_url;
    }

    public void setPict_url(String pict_url) {
        this.pict_url = pict_url;
    }

    public String getProvcity() {
        return provcity;
    }

    public void setProvcity(String provcity) {
        this.provcity = provcity;
    }

    public String getReserve_price() {
        return reserve_price;
    }

    public void setReserve_price(String reserve_price) {
        this.reserve_price = reserve_price;
    }

    public long getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(long seller_id) {
        this.seller_id = seller_id;
    }

    public String getShop_title() {
        return shop_title;
    }

    public void setShop_title(String shop_title) {
        this.shop_title = shop_title;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTk_rate() {
        return tk_rate;
    }

    public void setTk_rate(String tk_rate) {
        this.tk_rate = tk_rate;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public long getUser_type() {
        return user_type;
    }

    public void setUser_type(long user_type) {
        this.user_type = user_type;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public String getZk_final_price() {
        return zk_final_price;
    }

    public void setZk_final_price(String zk_final_price) {
        this.zk_final_price = zk_final_price;
    }

    public String getZk_final_price_wap() {
        return zk_final_price_wap;
    }

    public void setZk_final_price_wap(String zk_final_price_wap) {
        this.zk_final_price_wap = zk_final_price_wap;
    }

    public List<String> getSmall_images() {
        return small_images;
    }

    public void setSmall_images(List<String> small_images) {
        this.small_images = small_images;
    }
}
