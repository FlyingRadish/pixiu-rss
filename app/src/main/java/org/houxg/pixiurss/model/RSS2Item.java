package org.houxg.pixiurss.model;

import org.houxg.pixiurss.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * desc:
 * <br/>
 * author: houxg
 * <br/>
 * create on: 2015/8/22
 */
public class RSS2Item {

    Article article;

    String guid;
    String comments;
    String channelTitle;

    public RSS2Item() {
        article = new Article();
    }

    public static RSS2Item fromDao(Article dao) {
        RSS2Item item = null;
        if (dao != null) {
            item = new RSS2Item();
            item.article = dao;
            item.channelTitle = dao.getSource().getTitle();
        }
        return item;
    }

    public static List<RSS2Item> fromDaos(List<Article> daos) {
        List<RSS2Item> output = new ArrayList<>();
        if (daos != null) {
            for (Article article : daos) {
                RSS2Item item = RSS2Item.fromDao(article);
                if (item != null) {
                    output.add(item);
                }
            }
        }
        return output;
    }

    public Article toDao(){
        return article;
    }

    public static List<Article> toDaos(List<RSS2Item> items){
        List<Article> output = new ArrayList<>();
        if (items != null) {
            for (RSS2Item item : items) {
                if (item != null) {
                    output.add(item.toDao());
                }
            }
        }
        return output;
    }

    public void setTitle(String title) {
        article.setTitle(title);
    }

    public void setLink(String link) {
        article.setLink(link);
    }

    public void setDesc(String desc) {
        article.setDesc(desc);
    }

    public void setPubDate(long pubDate) {
        article.setPubTime(pubDate);
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
        return article.getTitle();
    }

    public String getLink() {
        return article.getLink();
    }

    public String getDesc() {
        return article.getDesc();
    }

    public long getPubDate() {
        return article.getPubTime();
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
                "article=" + article +
                ", guid='" + guid + '\'' +
                ", comments='" + comments + '\'' +
                ", channelTitle='" + channelTitle + '\'' +
                '}';
    }
}
