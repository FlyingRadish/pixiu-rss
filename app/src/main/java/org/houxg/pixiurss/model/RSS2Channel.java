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
    RSS2Image img;
    String skipHours;
    String skipDays;
    List<RSS2Item> items;
}
