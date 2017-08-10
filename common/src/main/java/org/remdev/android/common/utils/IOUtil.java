package org.remdev.android.common.utils;

import org.remdev.timlog.LogFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Alexandr.Salin on 5/4/16.
 */
public class IOUtil {
    private static final String TAG = IOUtil.class.getSimpleName();

    /*
   * Close a stream safely
   * @param closeable implementation of Closeable interface
   */
    public static void closeStream(Closeable closeable) {

        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                LogFactory.create(TAG).e(e.toString(), e);
            }
        }
    }

    /*
 * Read a stream and returns as a String
 * @param in InputStream
 */
    public static String readFileAsString(InputStream in) {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new BufferedInputStream(in)));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            LogFactory.create(TAG).e(e.toString(), e);
        } finally {
            IOUtil.closeStream(br);
        }
        return sb.toString();
    }
}
