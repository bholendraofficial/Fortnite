package com.fortnitedeluxe;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import static android.view.WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL;

public class MainActivity extends AppCompatActivity {


    private VideoView videoview;
    AlertDialog.Builder alertDialog;
    private TextView tv_percentage;
    private ProgressBar seekbar_splashhorizontal;
    private Timer timer;

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please Wait...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        videoview.start();
        super.onResume();

    }

    @Override
    protected void onPause() {
        videoview.pause();
        super.onPause();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try
        {
            getSupportActionBar().hide();

        }catch (Exception ex)
        {
            Log.e("Exception",ex.getMessage());
        }

        fullBrightness();
        tv_percentage= findViewById(R.id.tv_percentage);
        seekbar_splashhorizontal= findViewById(R.id.seekbar_splashhorizontal);
        videoview= findViewById(R.id.videoview);
        String path="android.resource://"+getPackageName()+"/"+R.raw.appvideo;
        videoview.setVideoURI(Uri.parse(path));
        videoview.setKeepScreenOn(true);
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                showAlertDialog();
            }
        });
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                timerCounter();
            }
        });
        videoview.start();


    }
    public void fullBrightness()
    {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.screenBrightness = BRIGHTNESS_OVERRIDE_FULL;
        getWindow().setAttributes(params);
    }

    private void showAlertDialog()
    {
        try
        {

            alertDialog=new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle(getResources().getString(R.string.app_name));
            alertDialog.setIcon(R.mipmap.ic_launcher);
            alertDialog.setMessage("Something went wrong during the installation. Please reinstall "+getResources().getString(R.string.app_name));
            alertDialog.setPositiveButton("Reinstall", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()));
                    startActivity(intent);
                    ActivityCompat.finishAffinity(MainActivity.this);

                }
            });
            alertDialog.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.finishAffinity(MainActivity.this);

                }
            });
            alertDialog.show();

        }catch (Exception ex)
        {
            Log.e("Exception",ex.getMessage());
        }

    }

    private void timerCounter(){
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (seekbar_splashhorizontal.getProgress() >= 100) {
                            timer.cancel();
                        }
                        int current = videoview.getCurrentPosition();
                        int progress = current * 100 / videoview.getDuration();
                        seekbar_splashhorizontal.setProgress(progress);
                        tv_percentage.setText("Installing " + progress + "%");
                    }
                });
            }
        };
        timer.schedule(task, 0, 1000);
    }

}
