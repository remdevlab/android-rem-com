package org.remdev.android.common.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import timber.log.Timber;

@SuppressWarnings({"WeakerAccess", "unused"})
public class TTSController {

    public static final float DEFAULT_SPEECH_RATE = 0.8f;
    private static TTSController instance;

    public static final int STATE_OK = TextToSpeech.SUCCESS;
    public static final int STATE_ERROR = TextToSpeech.ERROR;

    private Set<ControllerStateListener> listeners = new HashSet<>();
    private Set<SpeechProgressListener> speechProgressListeners = new HashSet<>();

    private boolean initialized = false;
    private float currentRate = DEFAULT_SPEECH_RATE;

    private TextToSpeech textToSpeech;

    private TTSController(final Context context, ControllerStateListener stateListener) {
        this(context, null, stateListener);
    }

    private TTSController(final Context context, final Locale locale, ControllerStateListener stateListener) {
        if (stateListener != null) {
            listeners.add(stateListener);
        }
        this.textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.ERROR) {
                    Timber.d("Could not initialize Text-To-Speech engine");
                    initialized = false;
                } else {
                    currentRate = DEFAULT_SPEECH_RATE;
                    textToSpeech.setSpeechRate(currentRate);
                    initialized = true;
                    if (locale != null) {
                        textToSpeech.setLanguage(locale);
                    }

                    //make a copy to enable the removing from collection (avoid ConcurrentModificationEx)
                    Collection<ControllerStateListener> copy = new HashSet<>(listeners);
                    for (ControllerStateListener stateListener : copy) {
                        stateListener.onInitialized(status);
                    }
                    addListenersNotifier();
                }
            }
        });
    }

    @SuppressLint("ObsoleteSdkInt")
    private void addListenersNotifier() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            addListenerNotifierAboveIceCream();
        } else {
            //noinspection deprecation
            textToSpeech.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {
                @Override
                public void onUtteranceCompleted(String utteranceId) {
                    Collection<SpeechProgressListener> copy = new HashSet<>(speechProgressListeners);
                    for (SpeechProgressListener speechProgressListener : copy) {
                        speechProgressListener.onDone(utteranceId);
                    }
                }
            });
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void addListenerNotifierAboveIceCream() {
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                //make a copy to enable the removing from collection (avoid ConcurrentModificationEx)
                Collection<SpeechProgressListener> copy = new HashSet<>(speechProgressListeners);
                for (SpeechProgressListener speechProgressListener : copy) {
                    speechProgressListener.onStart(utteranceId);
                }
            }

            @Override
            public void onDone(String utteranceId) {
                //make a copy to enable the removing from collection (avoid ConcurrentModificationEx)
                Collection<SpeechProgressListener> copy = new HashSet<>(speechProgressListeners);
                for (SpeechProgressListener speechProgressListener : copy) {
                    speechProgressListener.onDone(utteranceId);
                }
            }

            @Override
            public void onError(String utteranceId) {
                //make a copy to enable the removing from collection (avoid ConcurrentModificationEx)
                Collection<SpeechProgressListener> copy = new HashSet<>(speechProgressListeners);
                for (SpeechProgressListener speechProgressListener : copy) {
                    speechProgressListener.onError(utteranceId);
                }
            }
        });
    }

    public boolean isReady() {
        return initialized;
    }

    public boolean isSpeaking() {
        return textToSpeech.isSpeaking();
    }

    public void stop() {
        textToSpeech.stop();
    }

    public void speak(String text, int queueFlush) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, queueFlush, null, null);
        } else {
            //noinspection deprecation
            textToSpeech.speak(text, queueFlush, null);
        }
    }


    public void sayLater(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null, null);
        } else {
            //noinspection deprecation
            textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);
        }
    }

    public void sayNow(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            //noinspection deprecation
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public void speak(String text, int queueFlush, final @NonNull SpeechProgressListener listener, final String speechId) {
        SpeechProgressListener callbackWrapper = new SpeechProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                listener.onStart(utteranceId);
            }

            @Override
            public void onDone(String utteranceId) {
                if (utteranceId != null && speechId != null && utteranceId.equals(speechId)) {
                    listener.onDone(utteranceId);
                    removeListener(this);
                }
            }

            @Override
            public void onError(String utteranceId) {
                if (utteranceId != null && speechId != null && utteranceId.equals(speechId)) {
                    listener.onError(utteranceId);
                    removeListener(this);
                }
            }
        };
        addProgressListener(callbackWrapper);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, queueFlush, null, speechId);
        } else {
            HashMap<String, String> params = new HashMap<>();
            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, speechId);
            //noinspection deprecation
            textToSpeech.speak(text, queueFlush, params);
        }
    }

    public void addListener(ControllerStateListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public void removeListener(ControllerStateListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    public void addProgressListener(SpeechProgressListener listener) {
        if (listener != null) {
            speechProgressListeners.add(listener);
        }
    }

    public void removeListener(SpeechProgressListener listener) {
        if (listener != null) {
            speechProgressListeners.remove(listener);
        }
    }

    public static TTSController getInstance() {
        return getInstance(null);
    }

    public static TTSController getInstance(ControllerStateListener stateListener) {
        if (instance == null) {
            throw new IllegalStateException(String.format(
                    "TTS needs to be initialized before usage. Please call %s#init(Context) before calling this method", TTSController.class.getSimpleName()));
        } else if (stateListener != null) {
            stateListener.onInitialized(instance.initialized ? TextToSpeech.SUCCESS : TextToSpeech.ERROR);
        }
        return instance;
    }

    public static void init(Context context) {
        init(context, null);
    }

    public static void init(Context context, Locale locale) {
        init(context, locale, null);
    }

    public static void init(Context context, Locale locale, ControllerStateListener stateListener) {
        if (instance == null) {
            synchronized (TTSController.class) {
                if (instance == null) {
                    instance = new TTSController(context, locale, stateListener);
                }
            }
        } else if (stateListener != null) {
            stateListener.onInitialized(instance.initialized ? TextToSpeech.SUCCESS : TextToSpeech.ERROR);
        }
    }

    public interface ControllerStateListener {
        void onInitialized(int status);
    }

    public interface SpeechProgressListener {

        void onStart(String utteranceId);

        void onDone(String utteranceId);

        void onError(String utteranceId);
    }

    public static class SpeechProgressAdapter implements SpeechProgressListener {
        @Override
        public void onStart(String utteranceId) {}

        @Override
        public void onDone(String utteranceId) {}

        @Override
        public void onError(String utteranceId) {}
    }
}
