package org.houxg.pixiurss.module.network;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * desc:
 * <br>
 * author: houxg
 * <br>
 * create on 2015/9/4
 */
public abstract class NetTask<T> {
    String url;
    Object tag;
    boolean isCanceled = false;
    ParseResult.ErrorListener errorListener;
    ParseResult.Listener<T> listener;

    public void cancel() {
        isCanceled = true;
    }

    public Request getRequest() {
        return new Request.Builder()
                .url(url)
                .tag(tag)
                .build();
    }

    public abstract boolean parseResponse(Response response);

    public void deliverSuccess(ParseResult<T> result) {
    }

}
