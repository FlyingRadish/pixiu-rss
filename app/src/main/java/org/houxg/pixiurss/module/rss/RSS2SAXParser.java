package org.houxg.pixiurss.module.rss;

import android.util.Log;

import org.houxg.pixiurss.model.RSS2Channel;
import org.houxg.pixiurss.model.RSS2Item;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * desc:RSS解析器
 * <br/>
 * author: houxg
 * <br/>
 * create on: 2015/8/22
 */
public class RSS2SAXParser extends DefaultHandler {

    RSS2Channel channel;
    StringBuilder stringBuilder;
    RSS2Item item;

    String channelTitle = "";

    String parentTag = "";

    public RSS2Channel getResult() {
        return channel;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        channel = new RSS2Channel();
        channel.setItems(new ArrayList<RSS2Item>());
        stringBuilder = new StringBuilder();
    }


    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        stringBuilder.append(ch, start, length);   //针对同一标签可能多次调用characters方法，调用采用追加的方式
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        stringBuilder.delete(0, stringBuilder.length());    //清空Builder

        if (RSS2Protocol.CHANNEL.equals(localName)) {
            parentTag = localName;
        } else if (RSS2Protocol.CHANNEL_IMAGE.equals(localName)) {
            parentTag = localName;
        } else if (RSS2Protocol.CHANNEL_ITEM.equals(localName)) {
            Log.i("houxg", "uri=" + uri + ", qName=" + qName);
            parentTag = localName;
            item = new RSS2Item();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        String character = stringBuilder.toString();

        Log.i("houxg", "qName=" + qName);
        if (RSS2Protocol.CHANNEL.equals(parentTag)) {
            if (RSS2Protocol.CHANNEL_TITLE.equals(localName)) {
                channelTitle = character;
                channel.setTitle(channelTitle);
                Log.i("houxg", "channelTitle=" + channelTitle);
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
                channel.getItems().add(item);
                Log.i("houxg", item.toString());
            } else if (RSS2Protocol.ITEM_LINK.equals(localName)) {
                item.setLink(character);
            } else if (RSS2Protocol.ITEM_PUBDATE.equals(localName)) {
                item.setPubDate(character);
            } else if (RSS2Protocol.ITEM_TITLE.equals(localName)) {
                item.setTitle(character);
            } else if (RSS2Protocol.ITEM_DESC.equals(localName)) {
                item.setDesc(character);
            } else if (RSS2Protocol.ITEM_COMMENTS.equals(localName)) {
                item.setComments(character);
            }
        }
    }
}
