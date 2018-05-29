package g.rezza.moch.grupia.lib;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

import g.rezza.moch.grupia.R;

/**
 * Created by rezza on 14/03/18.
 */

public class AudioPlayer {
    private MediaPlayer mMediaPlayer;

    public void stop() {
        if (mMediaPlayer != null) {
            Log.d("AudioPlayer", "RESET MEDIA");
            mMediaPlayer.release();
            mMediaPlayer.reset();
            mMediaPlayer = null;
        }
    }

    public void play(Context c) {
        MediaPlayer mediaPlayer = MediaPlayer.create(c, R.raw.shrt);
        mediaPlayer.start(); // no need to call prepare(); create() does that for you
    }
}
