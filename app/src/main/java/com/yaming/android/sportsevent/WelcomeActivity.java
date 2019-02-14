package com.yaming.android.sportsevent;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.ArrayMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

    }

    @Override
    protected void onResume() {
        super.onResume();

        int readPhoneStatusPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);
        if (readPhoneStatusPermission == PackageManager.PERMISSION_GRANTED) {

            int cameraPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
            if (cameraPermission == PackageManager.PERMISSION_GRANTED) {

                int storagePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (storagePermission == PackageManager.PERMISSION_GRANTED) {

                    leanCloudGetDataExample(new StringThreadCallback() {
                        @Override
                        public void runCallback(String s) {

                            if (s.length() > 0) {
                                Intent intent  = new Intent(WelcomeActivity.this, ShowMessage.class);
                                intent.putExtra("description", s);
                                startActivity(intent);
                            } else {
                                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                            }
                            finish();

                        }
                    });

                } else {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 97);
                    }

                }

            } else {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 98);
                }

            }

        } else {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_PHONE_STATE)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 99);
            }

        }

    }

    private void leanCloudGetDataExample(final StringThreadCallback stringThreadCallback) {

        String appId = "sv0sbXAORCOPT37kS19eNfXD-gzGzoHsz";
        String appKey = "JlV6X88Fpclf2sRUC5dR97nU";

        String urlGet = "https://leancloud.cn:443/1.1/classes/Announcement?limit=1";

        ArrayMap<String,String> headerProperties = new ArrayMap<>();
        headerProperties.put("X-LC-Id", appId);
        headerProperties.put("X-LC-Key", appKey);

        HttpThreadHandler.get(WelcomeActivity.this, urlGet, headerProperties, new HttpThreadCallback() {
            @Override
            public void runCallback(int runStatus, ArrayMap<String, String> responseHeaderProperties, String result, String error) {
                String outPut = "";
                if (runStatus == 200) {
                    try {
                        JSONObject jsonObj = new JSONObject(result);
                        JSONArray resultArray = jsonObj.getJSONArray("results");
                        for (int i=0;i<resultArray.length();i++) {
                            JSONObject resultObj = resultArray.getJSONObject(i);
                            outPut = resultObj.getString("Description");
                            if (outPut.length() > 0) {
                                break;
                            }
                        }
                    } catch (JSONException e) { }
                }
                stringThreadCallback.runCallback(outPut);
            }
        });

    }

}
