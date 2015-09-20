package org.houxg.pixiurss.model;

import org.houxg.pixiurss.Source;

import java.util.ArrayList;
import java.util.List;

/**
 * desc:
 * <br/>
 * author: houxg
 * <br/>
 * create on: 2015/8/23
 */
public class RSS2Channel {
    Source source;
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

    public RSS2Channel() {
        source = new Source();
    }

    public static RSS2Channel fromDao(Source dao) {
        RSS2Channel channel = null;
        if (dao != null) {
            channel = new RSS2Channel();
            channel.source = dao;
        }
        return channel;
    }

    public static List<RSS2Channel> fromDaos(List<Source> daos) {
        List<RSS2Channel> output = new ArrayList<>();
        if (daos != null) {
            for (Source source : daos) {
                RSS2Channel channel = RSS2Channel.fromDao(source);
                if (channel != null) {
                    output.add(channel);
                }
            }
        }
        return output;
    }

    public static List<Source> toDaos(List<RSS2Channel> channels) {
        List<Source> output = new ArrayList<>();
        if (channels != null) {
            for (RSS2Channel channel : channels) {
                if (channel != null) {
                    output.add(channel.toDao());
                }
            }
        }
        return output;
    }

    public Source toDao() {
        return source;
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
        return source.getLink();
    }

    public void setLink(String link) {
        source.setLink(link);
    }

    public String getTitle() {
        return source.getTitle();
    }

    public void setTitle(String title) {
        source.setTitle(title);
    }

    public String getAlias() {
        return source.getAlias();
    }

    public void setAlias(String alias) {
        source.setAlias(alias);
    }
}
