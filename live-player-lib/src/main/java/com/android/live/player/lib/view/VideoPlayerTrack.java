package com.android.live.player.lib.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import com.android.live.player.lib.R;
import com.android.live.player.lib.base.BaseVideoPlayer;
import com.android.live.player.lib.controller.PlayerVideoController;

/**
 * hty_Yuye@Outlook.com
 * 2019/4/21
 * Default Video Player Track
 */

public class VideoPlayerTrack extends BaseVideoPlayer<PlayerVideoController> {

    public VideoPlayerTrack(@NonNull Context context) {
        super(context);
    }

    public VideoPlayerTrack(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.view_live_media_player_layout;
    }

    public VideoPlayerTrack(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
