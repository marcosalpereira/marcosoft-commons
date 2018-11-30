package org.marcosoft.lib;

public class SystemUtil {

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (final InterruptedException e) {
        }
    }
}
