package com.tech.Education.Cognitive.App_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.tech.Education.Cognitive.App_Bean.QuizBean;
import com.tech.Education.Cognitive.App_Database.DbHelper;
import com.tech.Education.Cognitive.R;
import com.tech.Education.Cognitive.UserSessionManagement.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class QuizActivity extends AppCompatActivity {
    AdView mAdView;
    private String quizId, userId;
    private String quizURL = "https://winnerspaathshala.com/winners/api.php?action=FetchQuestion&userid="+userId+"&quizid="+quizId;
    UserSessionManager session;
    DbHelper db;
    List<QuizBean> quesList;
    int score = 0, rightAns = 0, wroungAns = 0;
    int qid = 0;
    QuizBean currentQ;
    MaterialProgressBar progressBar;
    TextView txtQuestion, txtOutOff;
    RadioGroup radioGroup;
    ImageView qustnImage;
    RadioButton rda, rdb, rdc, rdd;
    Button butNext, butPrivious;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //initialize quiz database

        db = new DbHelper(this);
        //initialize app component
        init();

        //clear db
        deletePriviousQuiz();
        Log.e("raw", "" + db.rowcount());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {

        //get Quiz id from intent
        Bundle bundle = getIntent().getExtras();
        quizId = bundle.getString("quizId");


        //initialize quiz session
        session = new UserSessionManager(QuizActivity.this);

        //get User Id From Session
        HashMap<String, String> user = session.getUserDetails();
        //get user userId
        userId = user.get(UserSessionManager.KEY_USER_ID);
        Log.e("session_userid", userId);

        //initialize app component
        progressBar = (MaterialProgressBar) findViewById(R.id.quiz_progress);
        txtQuestion = (TextView) findViewById(R.id.question);
        txtOutOff = (TextView) findViewById(R.id.outOff);
        qustnImage = (ImageView) findViewById(R.id.question_img);
        butNext = (Button) findViewById(R.id.nextBtn);
        butPrivious = (Button) findViewById(R.id.priviousBtn);
        rda = (RadioButton) findViewById(R.id.optOne);
        rdb = (RadioButton) findViewById(R.id.optTwo);
        rdc = (RadioButton) findViewById(R.id.optThree);
        rdd = (RadioButton) findViewById(R.id.optFour);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mAdView != null) {
            mAdView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    private void deletePriviousQuiz() {
        db.deleteAll();
        //get quiz data
        getQuizData();
    }

    private void getQuizData() {

        // final AlertDialog dialog = new SpotsDialog(Splash_Screen.this,R.style.Custom);
        // dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, quizURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("quizURL", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String strSuccess = jsonObject.getString("success");
                            String message = jsonObject.getString("msg");

                            if (strSuccess.equals("1") && message.equals("Question Found.")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonData = jsonArray.getJSONObject(i);
                                    Log.d("Insert: ", "Inserting .." + "value:" + i);

                                    db.addQuestion(new QuizBean(jsonData.getInt("question_quizid"),
                                            jsonData.getString("question_question"),
                                            jsonData.getString("question_ans"),
                                            jsonData.getString("question_option_a"),
                                            jsonData.getString("question_option_b"),
                                            jsonData.getString("question_option_c"),
                                            jsonData.getString("question_option_d"),
                                            jsonData.getString("question_image")));
                                }
                                //show quiz in UI
                                Log.e("countRaw", "" + db.rowcount());
                                quiz(db.rowcount());

                            } else {
                                Log.d("Failed: ", "Inserting ..Failed");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
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
                params.put("userid", "" + userId);
                params.put("quizid", "" + quizId);
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(QuizActivity.this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void quiz(final int totalQues) {

        quesList = db.getAllQuestions();
        currentQ = quesList.get(qid);
        if (qid == 0) {
            butPrivious.setVisibility(View.INVISIBLE);
        }
        setQuestionView();

        butNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                radioGroup = (RadioGroup) findViewById(R.id.rg);
                RadioButton answer = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                radioGroup.clearCheck();
                if (answer != null) {
                    String strAnds = answer.getText().toString();
                    String strQAnds = currentQ.getQuestion_ans();
                    Log.d("yourans", currentQ.getQuestion_ans() + " " + answer.getText());
                    if (currentQ.getQuestion_ans().equals(answer.getText())) {
                        score++;
                        rightAns++;
                        Log.d("score", "Your score" + score);
                    } else {
                        wroungAns++;
                        Log.d("score", "Your score" + score);
                    }

                    if (qid == totalQues) {
                        butNext.setText("Submit");
                    }
                    if (qid < totalQues) {
                        currentQ = quesList.get(qid);
                        setQuestionView();
                    } else {
                        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                        Bundle b = new Bundle();
                        b.putInt("score", score); //Your score
                        b.putInt("wrong", wroungAns); //Your wrong answer
                        b.putInt("right", rightAns); //Your right answer
                        b.putInt("totalQues", totalQues); //toatal questions
                        b.putString("quizId", quizId); //toatal questions
                        intent.putExtras(b); //Put your score to your next Intent
                        startActivity(intent);
                        finish();
                    }

                } else {
                    Toast.makeText(QuizActivity.this, "Please select answer first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        butPrivious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (qid > 0) {
                    butPrivious.setVisibility(View.VISIBLE);
                    qid--;
                    currentQ = quesList.get(qid);
                    setQuestionViewOnPrivious();
                } else {
                    Toast.makeText(getApplicationContext(), "Not Applicable yet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setQuestionView() {
        txtQuestion.setText(currentQ.getQuestion_question());
        rda.setText(currentQ.getQuestion_option_a());
        rdb.setText(currentQ.getQuestion_option_b());
        rdc.setText(currentQ.getQuestion_option_c());
        rdd.setText(currentQ.getQuestion_option_d());
        //IMG

        String imgUrl="https://winnerspaathshala.com/winners/uploads/question_image/" + currentQ.getQuestion_image();
        Log.e("Image",imgUrl);
        Log.d("",imgUrl);


        Glide.with(QuizActivity.this)
                .load(imgUrl)
                .into(qustnImage);


        progressBar.setProgress(qid * db.getAllQuestions().size());
        qid++;
        txtOutOff.setText(qid + "/"+db.getAllQuestions().size());
    }

    private void setQuestionViewOnPrivious() {
        txtQuestion.setText(currentQ.getQuestion_question());
        rda.setText(currentQ.getQuestion_option_a());
        rdb.setText(currentQ.getQuestion_option_b());
        rdc.setText(currentQ.getQuestion_option_c());
        rdd.setText(currentQ.getQuestion_option_d());
        //IMG

        String imgUrl="https://winnerspaathshala.com/winners/uploads/question_image/" + currentQ.getQuestion_image();
        Log.d("",imgUrl);
        Glide.with(QuizActivity.this)
                .load(imgUrl)
                .into(qustnImage);

        progressBar.setProgress(qid * 10);
        txtOutOff.setText(qid + "/10");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(QuizActivity.this, DrawerMain.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
