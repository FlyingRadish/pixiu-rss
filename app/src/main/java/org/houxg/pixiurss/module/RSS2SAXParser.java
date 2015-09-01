package org.houxg.pixiurss.module;

import android.util.Log;

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

    List<RSS2Item> items;
    StringBuilder stringBuilder;
    RSS2Item item;

    boolean isInItem = false;
    boolean isInChannel = false;
    boolean isInImage = false;


    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        items = new ArrayList<>();
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

        } else if (RSS2Protocol.CHANNEL_IMAGE.equals(localName)) {
            isInImage = true;
        } else if (RSS2Protocol.CHANNEL_ITEM.equals(localName)) {
            item = new RSS2Item();
            isInItem = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        String character = stringBuilder.toString();
        if (RSS2Protocol.CHANNEL.equals(localName)) {

        } else if (RSS2Protocol.CHANNEL_TITLE.equals(localName)) {

        } else if (RSS2Protocol.CHANNEL_LINK.equals(localName)) {

        } else if (RSS2Protocol.CHANNEL_IMAGE.equals(localName)) {
            isInImage = false;
        } else if (RSS2Protocol.CHANNEL_ITEM.equals(localName)) {
            items.add(item);
            Log.i("houxg", item.toString());
            isInItem = false;
        }
        if(isInImage) {
            if (RSS2Protocol.IMAGE_URL.equals(localName)) {

            } else if (RSS2Protocol.IMAGE_LINK.equals(localName)) {

            } else if (RSS2Protocol.IMAGE_TITLE.equals(localName)) {

            }
        }

        if (isInItem) {
            if (RSS2Protocol.ITEM_LINK.equals(localName)) {
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
