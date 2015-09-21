package org.houxg.pixiurss;

import android.test.InstrumentationTestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * desc:
 * <br>
 * author: houxg
 * <br>
 * create on 2015/9/21
 */
public class LittleTest extends InstrumentationTestCase {

    public void testListInsert() {
        List<String> base = new ArrayList<>();
        base.add("1");
        base.add("title");
        base.add("3");
        base.add("4");
        base.add("5");
        base.add("a");
        base.add("b");

        List<String> add = new ArrayList<>();
        add.add("7");
        add.add("8");
        add.add("9");
        add.add("10");
//        add.add("15");

        int pos = base.indexOf("title");
        String wrapper;
        int titleSize = 3;
        if (pos < 0) {
            wrapper = "title2";
            base.add(wrapper);
            base.addAll(add);
        } else {
//            mergeList(base, add, pos + 1, pos + 3);
        }
    }


}
