package com.tech.Education.Cognitive.App_Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tech.Education.Cognitive.App_Adapter.RecentVideoAdapter;
import com.tech.Education.Cognitive.App_Bean.PartBean;
import com.tech.Education.Cognitive.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecentVideoFragment extends Fragment {

    View view;
    ListView jlistView;
    private String videoid,userId,type;

    //new api
    private String VideoPart="http://techviawebs.com/cognitive/api.php?action=VideoPart&videoid=1&userid=1";

    ArrayList<PartBean> partList=new ArrayList<PartBean>();
    public RecentVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_recent_video, container, false);
        jlistView=(ListView) view.findViewById(R.id.my_listview);
        Bundle mBundle = new Bundle();
        if(mBundle != null){
            mBundle = getArguments();
            videoid=mBundle.getString("videoid");
            userId=mBundle.getString("userId");
            type=mBundle.getString("type");
        }

        getPartVideo();

        return view;
    }

    private void getPartVideo(){



        StringRequest strReq = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, VideoPart, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("partVideoURL", response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String strSuccess = jsonObject.getString("success");

                    if (strSuccess.equals("1")) {
                        partList.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                            String status=jsonObject1.getString("vp_status");
                            if(Integer.parseInt(status) == 0){
                                partList.add(new PartBean(jsonObject1.getString("video_title"),
                                        jsonObject1.getString("video_description"),
                                        jsonObject1.getString("video_thumbnail"),
                                        jsonObject1.getString("videospartid"),
                                        jsonObject1.getString("vp_videoid"),
                                        jsonObject1.getString("vp_path"),
                                        jsonObject1.getString("vp_duration"),
                                        jsonObject1.getString("vp_status")));
                            }
                        }
                        RecentVideoAdapter adapter = new RecentVideoAdapter(getActivity(), partList);
                        jlistView.setAdapter(adapter);
                       // Log.e("DataBaseSize",""+db.getVideoCount());
                        adapter.notifyDataSetChanged();
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
                Log.e("videoid", videoid);
                Log.e("userid", userId);
                params.put("videoid", videoid);
                params.put("userid", userId);
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //Adding request to the queue
        requestQueue.add(strReq);

    }

   /* private void recentVideo() {

        StringRequest strReq = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, recentVideoURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RecentVideo", response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String strSuccess = jsonObject.getString("success");

                    if (strSuccess.equals("1")) {
                        morevideoList.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                            morevideoList.add(new Section(jsonObject1.getString("videoid"),
                                    jsonObject1.getString("video_title"),
                                    jsonObject1.getString("video_thumbnail"),
                                    jsonObject1.getString("video_path"),
                                    jsonObject1.getString("video_duration"),
                                    jsonObject1.getString("video_category"),
                                    jsonObject1.getString("video_description")));
                        }
                        RecentVideoAdapter adapter = new RecentVideoAdapter(getActivity(), morevideoList);
                        jlistView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

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
                Log.e("categoryname", categoryName);
                params.put("categoryname", categoryName);
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //Adding request to the queue
        requestQueue.add(strReq);
    }*/
}
