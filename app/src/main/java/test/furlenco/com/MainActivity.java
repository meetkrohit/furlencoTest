package test.furlenco.com;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    VideoView playVideo;
    String videoUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    MediaController mediaController;
    DownLoadManager downLoadManager;
    String fileName;
    Uri videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playVideo = findViewById(R.id.playVideo);

        downLoadManager = new DownLoadManager();

        mediaController = new MediaController(this);
        mediaController.setAnchorView(playVideo);


        try {

            fileName = videoUrl.substring(videoUrl.lastIndexOf('/') + 1);
            Log.e("Video name", fileName);
            String path = getFilesDir().getAbsolutePath()+"/"+fileName;
            File file = new File(path);
            if (file.exists()){
                videoPath = Uri.parse(path);
                Log.e("Video File", videoPath.toString());
            } else {
                videoPath = Uri.parse(videoUrl);
                downLoadManager.startDownLoad(MainActivity.this, videoUrl, fileName);
            }


        } catch (Exception e) {
            Log.e("Error:", e.toString());
        }


        playVideo.setMediaController(mediaController);
        playVideo.setVideoURI(videoPath);
        playVideo.requestFocus();
        playVideo.start();




        playVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
                        playVideo.setMediaController(mediaController);
                        mediaController.setAnchorView(playVideo);
                    }
                });
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }
}
