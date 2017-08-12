package com.example.test.Utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * 关闭工具类
 */

public class CloseUtils {

    /**
     * 关闭closeable对象
     * @param closeable
     */
    public static void close (Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
