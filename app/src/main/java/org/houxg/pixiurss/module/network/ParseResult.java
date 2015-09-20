package org.houxg.pixiurss.module.network;

/**
 * desc:
 * <br>
 * author: houxg
 * <br>
 * create on 2015/9/4
 */
public class ParseResult<T> {

    public interface Listener<T> {
        void onSuccess(T data);
    }

    public interface ErrorListener {
        void onError();
    }

    public ParseResult(NetError error, boolean isSuccess) {
        this.isSuccess = isSuccess;
        this.error = error;
    }

    public ParseResult(T data, boolean isSuccess) {
        this.data = data;
        this.isSuccess = isSuccess;
    }

    boolean isSuccess = false;
    NetError error;
    T data;


    public static <T> ParseResult<T> onSuccess(T data) {
        ParseResult result = new ParseResult<>(data, true);
        return result;
    }

    public static ParseResult onError(NetError error) {
        ParseResult result = new ParseResult(error, false);
        return result;
    }
}
