package org.houxg.pixiurss.module.rss;

import android.util.Log;

import org.houxg.pixiurss.model.RSS2Channel;
import org.houxg.pixiurss.model.RSS2Item;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * desc:RSS解析器
 * <br/>
 * author: houxg
 * <br/>
 * create on: 2015/8/22
 */
public class RSS2SAXParser extends DefaultHandler {

    StringBuilder stringBuilder;
    String channelTitle = "";

    RSS2Channel channel;
    RSS2Item item;
    List<RSS2Item> itemList;

    String parentTag = "";
    boolean isCancel = false;
    RSSTimeParser timeParser;

    public RSS2SAXParser() {
        timeParser = new RSSTimeParser();
    }

    public RSS2Channel getResultChannel() {
        return channel;
    }

    public List<RSS2Item> getItemList() {
        return itemList;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        checkCancel();
        channel = new RSS2Channel();
        itemList = new ArrayList<>();
        stringBuilder = new StringBuilder();
        parentTag = "";
        channelTitle = "";
        Log.i("rss-parser", "start doc");
    }


    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        checkCancel();
        stringBuilder.append(ch, start, length);   //针对同一标签可能多次调用characters方法，调用采用追加的方式
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        checkCancel();
        stringBuilder.delete(0, stringBuilder.length());    //清空Builder

        if (RSS2Protocol.CHANNEL.equals(localName)) {
            parentTag = localName;
        } else if (RSS2Protocol.CHANNEL_IMAGE.equals(localName)) {
            parentTag = localName;
        } else if (RSS2Protocol.CHANNEL_ITEM.equals(localName)) {
            parentTag = localName;
            item = new RSS2Item();
        }
//        try {
//            Thread.sleep(50);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        checkCancel();
        String character = stringBuilder.toString();

        if (RSS2Protocol.CHANNEL.equals(parentTag)) {
            if (RSS2Protocol.CHANNEL_TITLE.equals(localName)) {
                channelTitle = character;
                channel.setTitle(channelTitle);
            } else if (RSS2Protocol.CHANNEL_LINK.equals(localName)) {
                channel.setLink(character);
            }
        }

        if (RSS2Protocol.CHANNEL_IMAGE.equals(parentTag)) {
            if (RSS2Protocol.IMAGE_URL.equals(localName)) {

            } else if (RSS2Protocol.IMAGE_LINK.equals(localName)) {

            } else if (RSS2Protocol.IMAGE_TITLE.equals(localName)) {

            }
        }

        if (RSS2Protocol.CHANNEL_ITEM.equals(parentTag)) {
            if (RSS2Protocol.CHANNEL_ITEM.equals(localName)) {
                item.setChannelTitle(channelTitle);
                itemList.add(item);
//                Log.i("houxg", item.toString());
            } else if (RSS2Protocol.ITEM_LINK.equals(localName)) {
                item.setLink(character);
            } else if (RSS2Protocol.ITEM_PUBDATE.equals(localName)) {
                long time = timeParser.parse(character);
                item.setPubDate(time);
            } else if (RSS2Protocol.ITEM_TITLE.equals(localName)) {
                item.setTitle(character);
            } else if (RSS2Protocol.ITEM_DESC.equals(localName)) {
                item.setDesc(character);
            } else if (RSS2Protocol.ITEM_COMMENTS.equals(localName)) {
                item.setComments(character);
            }
        }
    }

    void cancel() {
        isCancel = true;
    }

    void checkCancel() throws ParseCancelException {
        if (isCancel) {
            throw new ParseCancelException();
        }
    }

    static class ParseCancelException extends SAXException {
    }
}
