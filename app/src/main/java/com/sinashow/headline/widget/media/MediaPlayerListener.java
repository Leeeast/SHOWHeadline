package com.sinashow.headline.widget.media;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public interface MediaPlayerListener {
    void onPrepared();

    void onAutoCompletion(IMediaPlayer iMediaPlayer);

    void onCompletion();

    void onBufferingUpdate(int percent);

    void onSeekComplete();

    void onError(int what, int extra);

    void onInfo(int what, int extra);

    void onVideoSizeChanged();

    void onBackFullscreen();

    void onVideoPause();

    void onVideoResume();
}
