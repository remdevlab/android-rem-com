package org.remdev.android.common.loader;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import org.remdev.android.common.utils.BundleUtils;
import org.remdev.android.common.utils.IOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class RandomTextLoader extends UniversalLoader<String> {

    private static final String ARG_WORDS_LIMIT = RandomTextLoader.class.getName() + ".ARG_WORDS_LIMIT";
    private static final int ID = generateId();

    public <R extends FragmentActivity & UniversalLoader.LoadingCallback<String>>
    RandomTextLoader(R compatActivityWithCallback) {
        super(compatActivityWithCallback, ID);
    }


    @Override
    protected String doLoad(Bundle data) {
        int limit = data.getInt(ARG_WORDS_LIMIT);
        URL url;
        try {
            url = new URL("https://loripsum.net/generate.php?p=100");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.getDoOutput();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            String text = IOUtil.readFileAsString(inputStream);
            connection.disconnect();
            text = getWords(text, limit);
            return text;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getWords(String text, int limit) {
        text = text.replaceAll("<p>", "")
                .replaceAll("</p>", "")
                .replaceAll("[\n]+", " ")
                .replaceAll("[ ]+", " ");
        String[] split = text.split(" ");
        StringBuilder data = new StringBuilder();
        int min = Math.min(limit, split.length);
        for (int i = 0; i < min; i++) {
            data.append(split[i]).append(' ');
        }
        return data.toString();
    }

    public void loadText(int wordsLimit) {
        triggerLoad(wordsLimit, false);
    }

    public void reloadText(int wordsLimit) {
        triggerLoad(wordsLimit, true);
    }

    private void triggerLoad(int wordsLimit, boolean reload) {
        Bundle args = BundleUtils.compose(
                BundleUtils.of(ARG_WORDS_LIMIT, wordsLimit)
        );
        executeLoad(args, reload);
    }
}
