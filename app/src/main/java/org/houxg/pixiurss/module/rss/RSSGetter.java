package org.houxg.pixiurss.module.rss;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.houxg.pixiurss.model.RSS2Channel;
import org.houxg.pixiurss.model.RSS2Item;
import org.houxg.pixiurss.module.App;
import org.houxg.pixiurss.utils.CancelRunnable;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
public class RSSGetter implements CancelRunnable {

    private final static String TAG = "RSSGetter";

    String[] urls;
    Object tag;
    Request.Builder requestBuilder;
    Listener listener;
    ErrorListener errorListener;
    DeliverHandler handler;
    RSS2SAXParser rssParser;
    boolean isRunning = false;

    boolean isCancel = false;

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

    @Override
    public void cancel() {
        isRunning = false;
        getClient().cancel(tag);
        isCancel = true;
        if (rssParser != null) {
            rssParser.cancel();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    private OkHttpClient getClient() {
        return App.getOKHttpClient();
    }

    public void run() {
        isRunning = true;
        Log.i(TAG, "start");
        if (urls == null || urls.length == 0) {
            Log.i(TAG, "no urls");
            isRunning = false;
            return;
        }
        OkHttpClient client = getClient();
        rssParser = new RSS2SAXParser();
        for (int i = 0; i < urls.length; i++) {
            Log.i(TAG, "get rss, url=" + urls[i]);
            InputStream stream = null;
            try {
                Request request = getRequestByUrl(urls[i]);
                Response response = client.newCall(request).execute();
                stream = response.body().byteStream();
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                long last = System.currentTimeMillis();
                parser.parse(stream, rssParser);
                stream.close();
                Log.i(TAG, "parse time=" + (System.currentTimeMillis() - last));
                if (!isCancel) {
                    handler.obtainMessage(DeliverHandler.WHAT_SUCCESS, i, urls.length, new Wrapper(urls[i], rssParser.getResultChannel(), rssParser.getItemList())).sendToTarget();
                } else {
                    break;
                }
            } catch (IllegalArgumentException | IOException | ParserConfigurationException | SAXException e) {
                e.printStackTrace();
                try {
                    if (stream != null) {
                        stream.close();
                    }
                } catch (Exception ignored) {
                }
                if (!isCancel) {
                    handler.obtainMessage(DeliverHandler.WHAT_ERROR, i, urls.length).sendToTarget();
                } else {
                    break;
                }
            }
        }
        Log.i(TAG, "rss get end");
        handler.destroy();
        isRunning = false;
    }

    public Request getRequestByUrl(String url) {
        return requestBuilder.url(url)
                .tag(tag)
                .build();
    }

    public interface Listener {
        void onSuccess(String url, RSS2Channel data, List<RSS2Item> itemList, int index, int total);
    }

    public interface ErrorListener {
        void onError(int index, int total);
    }

    static class DeliverHandler extends Handler {
        public static final int WHAT_SUCCESS = 1;
        public static final int WHAT_ERROR = 2;
        RSSGetter getter;

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
                        Wrapper wrapper = (Wrapper) msg.obj;
                        getter.listener.onSuccess(wrapper.url, wrapper.channel, wrapper.itemList, msg.arg1, msg.arg2);
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

    static class Wrapper {
        RSS2Channel channel;
        List<RSS2Item> itemList;
        String url;

        public Wrapper(String url, RSS2Channel channel, List<RSS2Item> itemList) {
            this.channel = channel;
            this.itemList = itemList;
            this.url = url;
        }
    }
}
