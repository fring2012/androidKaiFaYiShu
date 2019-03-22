package com.study.czq.androidKaiFaYiShu.utils;

import java.io.Closeable;
import java.io.IOException;

public class CloseableUtil {
    public static void close(Closeable closeable) {
        try {
            if (closeable != null)
                closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
