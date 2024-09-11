package com.bitwormhole.libduckeys;

import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.bitwormhole.libduckeys.helper.DuckLogger;
import com.bitwormhole.libduckeys.ui.boxes.Container;
import com.bitwormhole.libduckeys.ui.boxes.Node;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class BoxContainerTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        // Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();


        Container con = new Container();
        int[] zlist = new int[]{5, 0, 6, 8, 1, 7, 7, 8};
        for (int z : zlist) {
            Node node = new Node();
            node.z = z;
            con.addChild(node);
        }

        con.sortChildren();

        List<Long> output = new ArrayList<>();
        boolean reverse = true; // false;
        Log.d(DuckLogger.TAG, "forChildren(), reverse = " + reverse);

        con.forChildren(reverse, (item) -> {
            int z = item.z;
            Log.d(DuckLogger.TAG, "z-index = " + z);
            output.add((long) z);
        });

        long first = output.get(0);
        long last = output.get(output.size() - 1);
        assert (first > last);

        // assertEquals("com.bitwormhole.libduckeys.test", appContext.getPackageName());
    }
}
