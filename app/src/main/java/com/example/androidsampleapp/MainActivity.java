package com.example.androidsampleapp;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.arthenica.mobileffmpeg.FFprobe;
import com.arthenica.mobileffmpeg.MediaInformation;
import com.example.androidsampleapp.databinding.ActivityMainBinding;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            boolean isFromNotification = b.getBoolean("isFromNotification");
            Log.e("isFromNotification", isFromNotification + "");
        }

        // todo need to set this files in mobile first for testing
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES), "ILM");
        mediaStorageDir.mkdirs();
        File image1 = new File(mediaStorageDir, "Image_1.jpeg");
        File image2 = new File(mediaStorageDir, "Image_2.jpg");
        File image3 = new File(mediaStorageDir, "Image_3.jpeg");
//        File audio = new File(mediaStorageDir, "audio_2.mp3");
        File video1 = new File(mediaStorageDir, "005.mp4");
//        File video1 = new File(mediaStorageDir, "001.mp4");
        // todo check up to this files

        File video1Ts = new File(mediaStorageDir, "v1.ts");
        File imageVideoTs = new File(mediaStorageDir, "v2.ts");
        File imageVideo2Ts = new File(mediaStorageDir, "v3.ts");
//        File videoFTs = new File(mediaStorageDir, "vFinal.ts"); // working
        File videoFTs = new File(mediaStorageDir, "vFinal.mp4"); // working
        File videoFTs2 = new File(mediaStorageDir, "vFinal2.ts");

        Log.e("isExists", image1.exists() + " && " +
                image2.exists() + " && " + image3.exists() + " && " + video1.exists());

        binding.buttonPickFile.setOnClickListener(view -> {
            if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, 11)) {
                if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 12)) {

                    int rc = FFmpeg.execute(
                            "-y -i " + image2.getPath() +
                                    " -i " + video1.getPath() +
                                    " -s 1280x720 -filter_complex \"[0:v][1:v] overlay=445:30\" -pix_fmt yuv420p -c:v libx264 -c:a copy " +
                                    video1Ts.getPath());

                    if (rc == RETURN_CODE_SUCCESS) {
                        Log.i(Config.TAG, "Command execution completed successfully.");

                        MediaInformation v1Info = FFprobe.getMediaInformation(video1Ts.getPath());
                        Log.e("video duration==", v1Info.getDuration() + "");

                        int rc1 = FFmpeg.execute(
                                "-y -loop 1 -i " + image1.getPath() +
                                        " -c:v libx264 -t 5 -strict experimental -pix_fmt yuv420p " +
                                        imageVideoTs.getPath());

                        if (rc1 == RETURN_CODE_SUCCESS) {
                            Log.i(Config.TAG, "1 Command execution completed successfully.");

                            int rc2 = FFmpeg.execute(
                                    "-y -loop 1 -i " + image3.getPath() +
                                            " -c:v libx264 -t 5 -strict experimental -pix_fmt yuv420p " +
                                            imageVideo2Ts.getPath());

                            if (rc2 == RETURN_CODE_SUCCESS) {
                                Log.i(Config.TAG, "2 Command execution completed successfully.");

                                new Handler().postDelayed(() -> {
                                    int rc3 = FFmpeg.execute("-y -i \"concat:" + imageVideoTs.getPath()
                                            + "|" + video1Ts.getPath() + "|" + imageVideo2Ts.getPath() +
                                            "\" -c copy " +
                                            videoFTs.getPath());

                                    if (rc3 == RETURN_CODE_SUCCESS) {
                                        Log.i(Config.TAG, "3 Command execution completed successfully.");
                                       /* int end = 0, start = 5;
                                        try {
                                            String d = v1Info.getDuration();
                                            double d11 = Double.parseDouble(d);
                                            end = (int) d11 + start;
                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                        }
                                        int rc4 = FFmpeg.execute(
                                                "-y -i " + videoFTs.getPath() + " -i " + audio.getPath() +
                                                        " -af \"volume=enable='between(t," + start + "," + end + ")':volume=0.2\"" +
                                                        " -c:v copy -c:a aac -strict experimental -shortest " +
                                                        videoFTs2.getPath());

                                        if (rc4 == RETURN_CODE_SUCCESS) {
                                            Log.i(Config.TAG, "4 Command execution completed successfully.");
                                        } else if (rc4 == RETURN_CODE_CANCEL) {
                                            Log.i(Config.TAG, "4 Command execution cancelled by user.");
                                        } else {
                                            Log.i(Config.TAG, String.format("4 Command execution failed with rc=%d and the output below.", rc4));
                                        }*/

                                    } else if (rc3 == RETURN_CODE_CANCEL) {
                                        Log.i(Config.TAG, "3 Command execution cancelled by user.");
                                    } else {
                                        Log.i(Config.TAG, String.format("3 Command execution failed with rc=%d and the output below.", rc3));
                                    }
                                }, 5000);

                            } else if (rc2 == RETURN_CODE_CANCEL) {
                                Log.i(Config.TAG, "2 Command execution cancelled by user.");
                            } else {
                                Log.i(Config.TAG, String.format("2 Command execution failed with rc=%d and the output below.", rc2));
                            }

                        } else if (rc1 == RETURN_CODE_CANCEL) {
                            Log.i(Config.TAG, "1 Command execution cancelled by user.");
                        } else {
                            Log.i(Config.TAG, String.format("1 Command execution failed with rc=%d and the output below.", rc1));
                        }

                    } else if (rc == RETURN_CODE_CANCEL) {
                        Log.i(Config.TAG, "Command execution cancelled by user.");
                    } else {
                        Log.i(Config.TAG, String.format("Command execution failed with rc=%d and the output below.", rc));
                    }
                }
            }
        });
    }

    public boolean checkPermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            return false;
        } else {
//            Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}