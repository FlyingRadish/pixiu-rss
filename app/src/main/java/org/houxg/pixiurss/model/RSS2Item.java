package org.houxg.pixiurss.model;

/**
 * desc:
 * <br/>
 * author: houxg
 * <br/>
 * create on: 2015/8/22
 */
public class RSS2Item {
    String title;
    String link;
    String desc;
    String pubDate;
    String guid;
    String comments;
    String channelTitle;

    public void setTitle(String title) {
        this.title = title == null ? "" : title;
    }

    public void setLink(String link) {
        this.link = link == null ? "" : link;
    }

    public void setDesc(String desc) {
        this.desc = desc == null ? "" : desc;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate == null ? "" : pubDate;
    }

    public void setGuid(String guid) {
        this.guid = guid == null ? "" : guid;
    }

    public void setComments(String comments) {
        this.comments = comments == null ? "" : comments;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDesc() {
        return desc;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getGuid() {
        return guid;
    }

    public String getComments() {
        return comments;
    }

    @Override
    public String toString() {
        return "RSS2Item{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", desc='" + desc + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", guid='" + guid + '\'' +
                ", comments='" + comments + '\'' +
                ", channelTitle='" + channelTitle + '\'' +
                '}';
    }
}
