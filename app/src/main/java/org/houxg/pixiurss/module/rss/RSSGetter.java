package org.houxg.pixiurss.module.rss;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.houxg.pixiurss.module.App;
import org.houxg.pixiurss.model.RSS2Channel;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * desc:RSS批量获取网络线程
 * </br>
 * author: houxg
 * </br>
 * create on 2015/9/1
 */
public class RSSGetter extends Thread {

    String[] urls;
    Object tag;
    Request.Builder requestBuilder;
    Listener listener;
    ErrorListener errorListener;
    DeliverHandler handler;

    public interface Listener {
        void onSuccess(RSS2Channel data, int index, int total);
    }

    public interface ErrorListener {
        void onError(int index, int total);
    }

    public RSSGetter(String[] urls, Object tag) {
        this.urls = urls;
        this.tag = tag;
        requestBuilder = new Request.Builder();
        handler = new DeliverHandler(Looper.getMainLooper(), this);
    }

    public void subscribe(Listener listener, ErrorListener errorListener) {
        this.listener = listener;
        this.errorListener = errorListener;
    }

    public void cancel() {
        getClient().cancel(tag);
    }

    private OkHttpClient getClient() {
        return App.getOKHttpClient();
    }

    public void run() {
        if (urls == null || urls.length == 0) {
            return;
        }
        OkHttpClient client = getClient();
        for (int i = 0; i < urls.length; i++) {
            Request request = getRequestByUrl(urls[i]);
            try {
                Response response = client.newCall(request).execute();
                InputStream stream = response.body().byteStream();
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                RSS2SAXParser rssParser = new RSS2SAXParser();
                long last = System.currentTimeMillis();
                parser.parse(stream, rssParser);
                stream.close();
                Log.i("houxg", "time=" + (System.currentTimeMillis() - last));
                handler.obtainMessage(DeliverHandler.WHAT_SUCCESS, i, urls.length, rssParser.getResult()).sendToTarget();
            } catch (IOException | ParserConfigurationException | SAXException e) {
                e.printStackTrace();
                handler.obtainMessage(DeliverHandler.WHAT_ERROR, i, urls.length).sendToTarget();
            }
        }
        handler.destroy();
    }

    public Request getRequestByUrl(String url) {
        return requestBuilder.url(url)
                .tag(tag)
                .build();
    }

    static class DeliverHandler extends Handler {
        RSSGetter getter;

        public static final int WHAT_SUCCESS = 1;
        public static final int WHAT_ERROR = 2;

        public DeliverHandler(Looper mainLooper, RSSGetter rssGetter) {
            super(mainLooper);
            this.getter = rssGetter;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_SUCCESS:
                    if (getter.listener != null) {
                        getter.listener.onSuccess((RSS2Channel) msg.obj, msg.arg1, msg.arg2);
                    }
                    break;
                case WHAT_ERROR:
                    if (getter.errorListener != null) {
                        getter.errorListener.onError(msg.arg1, msg.arg2);
                    }
                    break;
            }
        }

        public void destroy() {
            removeCallbacks(null);
        }
    }
}
