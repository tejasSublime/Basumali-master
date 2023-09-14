package com.tech.Education.Cognitive.App_Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.tech.Education.Cognitive.App_Helper.AnimatedGifImageView;
import com.tech.Education.Cognitive.App_Helper.Interstitial_Google_Ads;
import com.tech.Education.Cognitive.R;
import com.tech.Education.Cognitive.UserSessionManagement.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class ResultActivity extends AppCompatActivity {
    private int score, wrongQues, rightQues, totalQues;
    private TextView jscoretxt, jpassFail;
    private ProgressBar jprogressBar;
    private Button jcorrect, jwrong;
    AdView mAdView;
    Button jnext_quiz, jplay_again;
    UserSessionManager session;
    String userId, quizId;
    AnimatedGifImageView animatedGifImageView;
    Dialog dialog;
    private String nextQuizURL = "http://winnerspaathshala.com/winners/api.php?action=GetQuizByUnlocked&userid=";
    KonfettiView konfettiView;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getSupportActionBar().setTitle("Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final KonfettiView konfettiView = (KonfettiView) findViewById(R.id.konfettiView);
        init();
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            score = bundle.getInt("score");
            wrongQues = bundle.getInt("wrong");
            rightQues = bundle.getInt("right");
            totalQues = bundle.getInt("totalQues");
            quizId = bundle.getString("quizId");
            Log.e("result", "" + score);
            Log.e("wrongQues", "" + wrongQues);
            Log.e("rightQues", "" + rightQues);
            Log.e("TotalQues", "" + totalQues);
            Log.e("quizId", "" + quizId);
        }

        double percentage = (rightQues * 100) / totalQues;
        jscoretxt.setText(String.format("%s%%", (int) percentage));
        jprogressBar.setProgress((int) percentage * 10);
        jcorrect.setText(String.format("%d correct answer", rightQues));
        jwrong.setText(String.format("%d wrong answer", wrongQues));

        int v = score * 10;
        if (v < 60) {
            jpassFail.setText("Fail");
            gifDialog(v);
            sendQuizData(percentage);
        } else {
            jpassFail.setText("Pass");
            gifDialog(v);
            sendQuizData(percentage);
        }
    }

    private void init() {

        konfettiView = (KonfettiView) findViewById(R.id.konfettiView);
        jscoretxt = (TextView) findViewById(R.id.txtProgress);
        jpassFail = (TextView) findViewById(R.id.tttt);
        jprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        jcorrect = (Button) findViewById(R.id.cc);
        jwrong = (Button) findViewById(R.id.ccc);
        jnext_quiz = (Button) findViewById(R.id.next_quiz);
        jplay_again = (Button) findViewById(R.id.play_again);
        mAdView = (AdView) findViewById(R.id.adView);

        //initialize quiz session
        session = new UserSessionManager(ResultActivity.this);

        //get User Id From Session
        HashMap<String, String> user = session.getUserDetails();
        //get user userId
        userId = user.get(UserSessionManager.KEY_USER_ID);
        Log.e("session_userid", userId);

        jnext_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Interstitial_Google_Ads().getInterstitialAd(ResultActivity.this);
                int per = score * 10;
                if (per >= 60) {

                    onNextClicked();

                } else {
                    new SweetAlertDialog(ResultActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                            .setTitleText("Quiz is Locked")
                            .setContentText("Your Test is not clear.")
                            .setCustomImage(R.drawable.oops)
                            .show();
                }
            }
        });

        jplay_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Interstitial_Google_Ads().getInterstitialAd(ResultActivity.this);
                Intent intent = new Intent(ResultActivity.this, QuizActivity.class);
                intent.putExtra("quizId", quizId);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


                //  playAgainQuiz();
            }
        });
    }

    private void gifDialog(int marks) {

        if (marks >= 60) {
            Toasty.success(this, "Congrats You are Pass!", Toast.LENGTH_SHORT, true).show();

            /*  final AlertDialog.Builder builder = new AlertDialog.Builder(ResultActivity.this);

            AdInfoModel webview = new AdInfoModel(ResultActivity.this);
            builder.setView(webview);
            webview.loadUrl("http://bestanimations.com/Holidays/Fireworks/fireworks-animation-33.gif");
            builder.setCancelable(true);

            final AlertDialog dlg = builder.create();
            dlg.show();
            final Timer t = new Timer();
            t.schedule(new TimerTask() {
                public void run() {
                    dlg.dismiss();
                    t.cancel();
                }
            }, 5000);*/

            konfettiView.build()
                    .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                    .setDirection(0.0, 359.0)
                    .setSpeed(1f, 5f)
                    .setFadeOutEnabled(true)
                    .setTimeToLive(2000L)
                    .addShapes(Shape.RECT, Shape.CIRCLE)
                    .addSizes(new Size(12, 5f))
                    .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                    .stream(300, 5000L);
        } else {
            Toasty.info(this, "Fail Better luck next time.", Toast.LENGTH_SHORT, true).show();
           /* final AlertDialog.Builder builder = new AlertDialog.Builder(ResultActivity.this);
            AdInfoModel webview = new AdInfoModel(ResultActivity.this);
            builder.setView(webview);
            webview.loadUrl("http://78.media.tumblr.com/830c500d5f32c9ca7c58d4319d4d08ef/tumblr_n8kvw10HoR1qatq71o1_400.gif");
            builder.setCancelable(true);

            final AlertDialog dlg = builder.create();
            dlg.show();
            final Timer t = new Timer();
            t.schedule(new TimerTask() {
                public void run() {
                    dlg.dismiss();
                    t.cancel();
                }
            }, 5000);*/

            new SweetAlertDialog(ResultActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText("Try Again")
                    .setContentText("Your Test is not clear.")
                    .setCustomImage(R.drawable.oops)
                    .show();
        }
    }

    private void sendQuizData(final double score) {

        String sendURL = "http://winnerspaathshala.com/winners/api.php?action=SubmitQuiz&userid=" + userId + "&quizid=" + quizId + "&score=" + score;
        Log.e("sendUrl", sendURL);
        StringRequest stringRequest = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, sendURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("sendURL", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String strSuccess = jsonObject.getString("success");
                            String message = jsonObject.getString("msg");
                            String Tag = "Points";
                            if (strSuccess.equals("1") && message.equals("Update Quiz Successfully.")) {

                                Log.e("Tag", "data is sended");
                            } else {
                                Log.e("Tag", "data is not sended");
                            }

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("MainActivityTab1", "Error: " + error.getMessage());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("userid", "" + userId);
                Log.e("quizid", "" + quizId);
                Log.e("score", "" + score);
                params.put("userid", "" + userId);
                params.put("quizid", "" + quizId);
                params.put("score", "" + score);
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(ResultActivity.this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void onNextClicked() {

        StringRequest stringRequest = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, nextQuizURL,
                response -> {
                    Log.d("nextQuizURL", response.toString());
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String strSuccess = jsonObject.getString("success");
                        String message = jsonObject.getString("msg");
                        Log.d("TAG", "onResponse: checking msg " + message + " " + strSuccess);
                        if (strSuccess.equals("1") && message.equals("Quiz Found.")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            String quizid = jsonObject1.getString("quizid");
                            Intent intent = new Intent(ResultActivity.this, QuizActivity.class);
                            intent.putExtra("quizId", quizid);
                            startActivity(intent);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> VolleyLog.d("DrawerMainTab1", "Error: " + error.getMessage())) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("userid", userId);
                params.put("userid", userId);
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(ResultActivity.this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void playAgainQuiz() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.show();
        String playAgainURL = "http://techviawebs.com/cognitive/api.php?action=playAgain&userid=1&quizid=1";
        StringRequest stringRequest = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, playAgainURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("playAgainURL", response.toString());
                        dialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String strSuccess = jsonObject.getString("success");
                            String message = jsonObject.getString("msg");

                            if (strSuccess.equals("1") && message.equals("Quiz Found.")) {

                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                String quizid = jsonObject1.getString("quizid");
                                String quiz_title = jsonObject1.getString("quiz_title");
                                String video_path = jsonObject1.getString("video_path");
                                String videoid = jsonObject1.getString("quiz_video");
                                String quizstatus_unlocked = jsonObject1.getString("quizstatus_unlocked");
                                Intent intent = new Intent(ResultActivity.this, VideoActivity.class);
                                intent.putExtra("videoid", videoid);
                                intent.putExtra("quizId", quizid);
                                startActivity(intent);
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
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("userid", userId);
                Log.e("quizid", quizId);
                params.put("userid", userId);
                params.put("quizid", quizId);
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(ResultActivity.this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdView != null) {
            mAdView.pause();
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ResultActivity.this, DrawerMain.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
