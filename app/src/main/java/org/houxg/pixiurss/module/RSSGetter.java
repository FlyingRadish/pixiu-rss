package org.houxg.pixiurss.module;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.houxg.pixiurss.App;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * desc:
 * </br>
 * author: houxg
 * </br>
 * create on 2015/9/1
 */
public class RSSGetter implements Runnable {

    String url;
    Object tag;

    public RSSGetter(String url, Object tag) {
        this.url = url;
        this.tag = tag;
    }

    public void run() {
        Request request = new Request.Builder()
                .url(url)
                .tag(tag)
                .build();
        try {
            Response response = App.getOKHttpClient().newCall(request).execute();
//            String str = response.body().string();
//            Log.i("houxg", "rcv=" + str);
            InputStream stream = response.body().byteStream();


            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            RSS2SAXParser saxParser = new RSS2SAXParser();
            parser.parse(stream, saxParser);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
