package org.houxg.pixiurss.model;

import java.util.List;

/**
 * desc:
 * <br/>
 * author: houxg
 * <br/>
 * create on: 2015/8/23
 */
public class RSS2Channel {
    String title;
    String link;
    String desc;

    String lang;
    String copyRight;
    String pubDate;
    String lastBuildDate;
    String category;
    int ttl;
    String skipHours;
    String skipDays;

    RSS2Image img;
    List<RSS2Item> items;

    public List<RSS2Item> getItems() {
        return items;
    }

    public void setItems(List<RSS2Item> items) {
        this.items = items;
    }

    public RSS2Image getImg() {
        return img;
    }

    public void setImg(RSS2Image img) {
        this.img = img;
    }

    public String getSkipDays() {
        return skipDays;
    }

    public void setSkipDays(String skipDays) {
        this.skipDays = skipDays;
    }

    public String getSkipHours() {
        return skipHours;
    }

    public void setSkipHours(String skipHours) {
        this.skipHours = skipHours;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getCopyRight() {
        return copyRight;
    }

    public void setCopyRight(String copyRight) {
        this.copyRight = copyRight;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
