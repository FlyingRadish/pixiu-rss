package org.houxg.pixiurss.module.rss;

import org.joda.time.DateTime;

/**
 * desc:
 * <br>
 * author: houxg
 * <br>
 * create on 2015/9/17
 */
public class RSSTimeParser {

    int todayMonthIndex = 0;

    public RSSTimeParser() {
        DateTime time = new DateTime();
        todayMonthIndex = time.getMonthOfYear();
    }

    private final static String[] months = new String[]{
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec"
    };

    /**
     * step 0:split by space
     * step 1:split by : for time
     * step 2:parse number by Integer.valueof()
     * step 3:from today's month, check every month-string
     *
     * @param rssTime rss pubDate string
     * @return timestamp
     */
    public long parse(String rssTime) {
        long start = System.currentTimeMillis();
        String[] items = rssTime.split(" ");
        int dayOfMonth = Integer.valueOf(items[1]);
        int year = Integer.valueOf(items[3]);
        String[] times = items[4].split(":");
        int hour = Integer.valueOf(times[0]);
        int min = Integer.valueOf(times[1]);
        int sec = Integer.valueOf(times[2]);
        String monthStr = items[2];
        int month = -1;
        int index = todayMonthIndex;
        int i;
        for (i = 0; i < 12; i++) {
            index = (index - 1);
            if (index < 0) {
                index = 11;
            }
            if (months[index].equals(monthStr)) {
                month = index + 1;
                break;
            }
        }
        if (month < 0) {
            return -1;
        }
        DateTime time = new DateTime(year, month, dayOfMonth, hour, min, sec);
        long rslt = time.getMillis();
//        Log.i("time-parse", "take=" + (System.currentTimeMillis() - start) + ", loop=" + i + ", year=" + year + ", month=" + month + ", day=" + dayOfMonth + ", hour=" + hour + ", min=" + min + ", sec=" + sec);
        return rslt;
    }
}
