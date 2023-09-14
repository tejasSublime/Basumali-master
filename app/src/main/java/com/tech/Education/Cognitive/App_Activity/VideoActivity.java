package com.tech.Education.Cognitive.App_Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tech.Education.Cognitive.App_Adapter.RecyclerViewDataAdapter;
import com.tech.Education.Cognitive.App_Bean.Data;
import com.tech.Education.Cognitive.App_Bean.PartBean;
import com.tech.Education.Cognitive.App_Bean.Section;
import com.tech.Education.Cognitive.App_Fragment.RecentVideoFragment;
import com.tech.Education.Cognitive.R;
import com.tech.Education.Cognitive.UserSessionManagement.UserSessionManager;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class VideoActivity extends AppCompatActivity implements UniversalVideoView.VideoViewCallback{

    UserSessionManager session;
    String quizId,video_path,quiz_unlocked,vId,userId;
    public static final String MESSAGE_PROGRESS = "message_progress";
    private static final int PERMISSION_REQUEST_CODE = 1;
    View mVideoLayout, view;
    UniversalVideoView mVideoView;
    UniversalMediaController mMediaController;

    public int mSeekPosition;
    public int cachedHeight;
    public boolean isFullscreen;
    private static final String TAG = "VideoActivity";
    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";
    private static final String VIDEO_URL = "http://techviawebs.com/cognitive/uploads/video/";

    // File url to download

    private String VideoPart="http://winnerspaathshala.com/winners/api.php?action=VideoPart&videoid=&userid=";
    private String VideoNextPart="http://techviawebs.com/cognitive/api.php?action=VideoNextPart&videoid=&userid=";
    ArrayList<PartBean> partBeans=new ArrayList<PartBean>();
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        init();

        mAdView =(AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mVideoLayout = findViewById(R.id.video_layout);
        mVideoView = (UniversalVideoView) findViewById(R.id.videoView);
        mMediaController = (UniversalMediaController) findViewById(R.id.media_controller);
        mVideoView.setMediaController(mMediaController);

        //mVideoView.setVideoViewCallback(getContext());
        //setVideoAreaSize();

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "onCompletion ");
               // Toast.makeText(getApplicationContext(), "Video complete", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Please wait", Toast.LENGTH_SHORT).show();
                getVideoNextPart(vId,userId);
            }
        });
    }

    private void init(){

        session=new UserSessionManager(VideoActivity.this);
        //get User Id From Session
        HashMap<String, String> user = session.getUserDetails();

        //get user userId
        userId = user.get(UserSessionManager.KEY_USER_ID);

        Intent intent=getIntent();
        if(intent != null){

            vId=intent.getStringExtra("videoid");
                    quizId=intent.getStringExtra("quizId");
            getVideoPart(vId,userId);

        }else {
            System.out.print("Intent is null");

        }
    }

    private void setVideoAreaSize(final String videoId, final String path) {
        mVideoLayout.post(new Runnable() {
            @Override
            public void run() {
                int width = mVideoLayout.getWidth();
                cachedHeight = (int) (width * 405f / 720f);
//                cachedHeight = (int) (width * 3f / 4f);
//                cachedHeight = (int) (width * 9f / 16f);
                ViewGroup.LayoutParams videoLayoutParams = mVideoLayout.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = cachedHeight;
                mVideoLayout.setLayoutParams(videoLayoutParams);
                mVideoView.setVideoPath(path);
                /*mVideoView.setVideoPath(VIDEO_URL+path);*/
                mVideoView.requestFocus();
                mVideoView.start();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState Position=" + mVideoView.getCurrentPosition());
        outState.putInt(SEEK_POSITION_KEY, mSeekPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        mSeekPosition = outState.getInt(SEEK_POSITION_KEY);
        Log.d(TAG, "onRestoreInstanceState Position=" + mSeekPosition);
    }


    @Override
    public void onScaleChange(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        if (isFullscreen) {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoLayout.setLayoutParams(layoutParams);
            //mBottomLayout.setVisibility(View.GONE);
            mVideoView.start();

        } else {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = this.cachedHeight;
            mVideoLayout.setLayoutParams(layoutParams);
            //mBottomLayout.setVisibility(View.VISIBLE);
        }

        //switchTitleBar(!isFullscreen);
    }

    private void switchTitleBar(boolean show) {
        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            if (show) {
                supportActionBar.show();
            } else {
                supportActionBar.hide();
            }
        }
    }

    @Override
    public void onPause(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onPause UniversalVideoView callback");
        mVideoView.start();
    }

    @Override
    public void onStart(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onStart UniversalVideoView callback");
    }

    @Override
    public void onBufferingStart(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onBufferingStart UniversalVideoView callback");
    }

    @Override
    public void onBufferingEnd(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onBufferingEnd UniversalVideoView callback");
    }

    @Override
    public void onBackPressed() {

        if (this.isFullscreen) {
            mVideoView.setFullscreen(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause ");
        if (mVideoView != null && mVideoView.isPlaying()) {
            mSeekPosition = mVideoView.getCurrentPosition();
            Log.d(TAG, "onPause mSeekPosition=" + mSeekPosition);
            mVideoView.start();
            if (mAdView != null) {
                mAdView.pause();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }

    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }


    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;
        }
    }

    private void requestPermission(){

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);

    }

    private void fragment(String videoId,String userId,String type){
        RecentVideoFragment fragment =new RecentVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("videoid",videoId); // use as per your need
        bundle.putString("userId",userId);
        bundle.putString("type",type);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // fragmentTransaction.add(R.id.content_frame, aboutUs);
        fragmentTransaction.replace(R.id.content_recent, fragment,"home");
        fragmentTransaction.commit();
    }

    private void getVideoPart(final String id,final String userId){

        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();
        RequestParams params = new RequestParams();
        params.put("videoid", id);
        params.put("userid", userId);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(VideoPart,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (statusCode == 200 && response != null) {
                    Log.i("response-", response.toString());

                    dialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        String strSuccess = jsonObject.getString("success");
                        String message = jsonObject.getString("msg");
                        String path="";
                        if (strSuccess.equals("1") && message.equals("Successfully.")) {

                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            int quizCount=0;
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1= (JSONObject) jsonArray.get(i);
                                String status=jsonObject1.getString("vp_status");
                                if(Integer.parseInt(status) == 1){
                                    quizCount++;
                                    String videoId=jsonObject1.getString("videospartid");
                                    path=jsonObject1.getString("vp_path");
                                    partBeans.add(new PartBean(jsonObject1.getString("video_title"),
                                            jsonObject1.getString("video_description"),
                                            jsonObject1.getString("video_thumbnail"),
                                            jsonObject1.getString("videospartid"),
                                            jsonObject1.getString("vp_videoid"),
                                            jsonObject1.getString("vp_path"),
                                            jsonObject1.getString("vp_duration"),
                                            jsonObject1.getString("vp_status")));
                                }
                            }

                            if(partBeans.size()-quizCount == partBeans.size()){
                                Intent intent=new Intent(VideoActivity.this,QuizActivity.class);
                                intent.putExtra("quizId",quizId);
                                startActivity(intent);
                            }else {
                                setVideoAreaSize(vId,path);
                                fragment(vId,userId,"VideoPart");
                                // fragment(vId,userId,"VideoNextPart");
                            }
                            //setVideoAreaSize(id,path);
                            //fragment(vId,userId,"VideoPart");

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        Toast.makeText(VideoActivity.this,"Parsing Error",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    dialog.dismiss();
                    Toast.makeText(VideoActivity.this,"Connection Error",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                dialog.dismiss();
                Toast.makeText(VideoActivity.this,"Connection Error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                dialog.dismiss();
                Toast.makeText(VideoActivity.this,"Connection Error", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getVideoNextPart(final String id,final String userId){

        final ProgressDialog dialog=new ProgressDialog(VideoActivity.this);
        dialog.setMessage("Please Wait...");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, VideoNextPart,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("VideoNextPart", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String strSuccess = jsonObject.getString("success");
                            String message = jsonObject.getString("msg");

                            if (strSuccess.equals("1") && message.equals("Successfully.")) {

                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                String path="";
                                String videoId="";
                                int quizCount=0;
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject1= (JSONObject) jsonArray.get(i);
                                    String status=jsonObject1.getString("vp_status");
                                    if(Integer.parseInt(status) == 1){
                                        quizCount++;
                                        videoId=jsonObject1.getString("videospartid");

                                         path=jsonObject1.getString("vp_path");
                                        partBeans.add(new PartBean(jsonObject1.getString("video_title"),
                                                jsonObject1.getString("video_description"),
                                                jsonObject1.getString("video_thumbnail"),
                                                jsonObject1.getString("videospartid"),
                                                jsonObject1.getString("vp_videoid"),
                                                jsonObject1.getString("vp_path"),
                                                jsonObject1.getString("vp_duration"),
                                                jsonObject1.getString("vp_status")));
                                    }
                                }
                                if(partBeans.size()-quizCount == partBeans.size()){
                                    Intent intent=new Intent(VideoActivity.this,QuizActivity.class);
                                    intent.putExtra("quizId",quizId);
                                    startActivity(intent);
                                }else {
                                    dialog.dismiss();
                                    setVideoAreaSize(vId,path);
                                    fragment(vId,userId,"VideoPart");
                                    // fragment(vId,userId,"VideoNextPart");
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                VolleyLog.d("DrawerMainTab1", "Error: " + error.getMessage());

            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("videoid", id);
                Log.e("userid", userId);
                params.put("videoid", id);
                params.put("userid", userId);
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(VideoActivity.this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
}
