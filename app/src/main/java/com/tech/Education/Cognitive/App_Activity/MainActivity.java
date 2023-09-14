package com.tech.Education.Cognitive.App_Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tech.Education.Cognitive.App_Adapter.RecyclerViewDataAdapter;
import com.tech.Education.Cognitive.App_Bean.Data;
import com.tech.Education.Cognitive.App_Bean.Section;
import com.tech.Education.Cognitive.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ProgressDialog pDialog;
    List<Data> allSampleData;
    private String TEST_URL="http://techviawebs.com/cognitive/api.php?action=GetVideo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        allSampleData = new ArrayList<Data>();

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle("G PlayStore");
        }

        testWebService(TEST_URL);
    }

    public void showProgressDialog() {

        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        if (!pDialog.isShowing())
            pDialog.show();
    }

    public void dismissProgressDialog() {
        if (pDialog != null)
            pDialog.dismiss();

    }


    public void testWebService(String url)
    {
        showProgressDialog();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dismissProgressDialog();

                if (statusCode == 200 && response != null) {
                    Log.i("response-", response.toString());


                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        String strSuccess = jsonObject.getString("success");
                        String message = jsonObject.getString("msg");
                        JSONArray dataArary= jsonObject.getJSONArray("data");

                        for(int i=0;i<dataArary.length();i++)
                        {
                            JSONObject sectionObj= (JSONObject) dataArary.get(i);

                            String title= sectionObj.getString("category_name");


                            List<Section> sections= new ArrayList<Section>();


                            JSONArray sectionsArray=sectionObj.getJSONArray("videos");

                            for(int j=0;j<sectionsArray.length();j++)
                            {

                                JSONObject obj= (JSONObject) sectionsArray.get(j);


                                Section section = new Section();

                                section.setId(obj.getString("videoid"));
                                section.setName(obj.getString("video_title"));
                                section.setImage(obj.getString("video_thumbnail"));
                                section.setPath(obj.getString("video_path"));
                                section.setDuration(obj.getString("video_duration"));
                                section.setCategory(obj.getString("video_category"));
                                sections.add(section);
                            }


                            Data data= new Data();
                            data.setTitle(title);
                            data.setSection(sections);

                            allSampleData.add(data);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this,"Parsing Error",Toast.LENGTH_SHORT).show();
                    }

                    // setting data to RecyclerView

                    if(allSampleData!=null) {


                        RecyclerView my_recycler_view = (RecyclerView) findViewById(R.id.my_recycler_view);

                        my_recycler_view.setHasFixedSize(true);

                        RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(MainActivity.this, allSampleData);

                        my_recycler_view.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));

                        my_recycler_view.setAdapter(adapter);
                    }
                }
                else {
                    Toast.makeText(MainActivity.this,"Connection Error",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                dismissProgressDialog();
                Toast.makeText(MainActivity.this,"Connection Error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                dismissProgressDialog();
                Toast.makeText(MainActivity.this,"Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
