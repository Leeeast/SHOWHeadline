package com.sinashow.headline.widget.media;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.TextureView;

/**
 * Created by lidongliang on 2017/5/17.
 */

public class VideoView extends TextureView {
    private static final String TAG = VideoView.class.getSimpleName();

    public VideoView(Context context) {
        super(context);
    }

    public VideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void scaleVideoSize(int videoWidth, int videoHeight) {
        if (videoWidth == 0 || videoHeight == 0) {
            return;
        }
        Size viewSize = new Size(getWidth(), getHeight());
        Size videoSize = new Size(videoWidth, videoHeight);
        float sx = (float) viewSize.getWidth() / videoSize.getWidth();
        float sy = (float) viewSize.getHeight() / videoSize.getHeight();
        float maxScale = Math.max(sx, sy);
        sx = maxScale / sx;
        sy = maxScale / sy;
        Matrix matrix = new Matrix();
        matrix.setScale(sx, sy, viewSize.getWidth() / 2f, viewSize.getHeight() / 2f);
        if (matrix != null) {
            setTransform(matrix);
        }
    }

}
