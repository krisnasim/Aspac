package id.co.ncl.aspac.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ncl.aspac.R;
import id.co.ncl.aspac.activity.HomeActivity;
import id.co.ncl.aspac.adapter.WorkListAdapter;
import id.co.ncl.aspac.customClass.CustomJSONObjectRequest;
import id.co.ncl.aspac.model.Work;

public class WorkFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {

    @BindView(R.id.work_list_listview) ListView work_list_listview;

    private ListView lv;
    private Resources res;
    private JSONArray dataGlobalArray;
    private WorkListAdapter adapter;
    private SharedPreferences sharedPref;
    private ArrayList<Work> workData = new ArrayList<>();

    public WorkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("onCreate", "I AM CREATED!");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_work, container, false);
        ButterKnife.bind(this, view);
        Log.d("onCreate", "MY VIEW IS CREATED!");
        getActivity().setTitle("Daftar Pekerjaan Rutin");

        if(savedInstanceState != null) {
            Log.d("onCreate", "SAVEDINSTANCESTATE YEA!");
            workData = savedInstanceState.getParcelableArrayList("work");
            String dataGlobalArrayString = savedInstanceState.getString("globalArray");
            try {
                dataGlobalArray = new JSONArray(dataGlobalArrayString);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            setAdapter();
        } else {
            Log.d("onCreate", "INIT DATA!");
            //get work API
            getAllWorkData();
        }

//        //create date object
//        Calendar myCalendar = Calendar.getInstance();
//        myCalendar.set(2017, 8, 31);
//        Date myDate = myCalendar.getTime();
//        //myDate = myCalendar.getTime();
//
//        for(int x = 0; x < 7; x++) {
//            //create new work object
//            Work newWork = new Work();
//            newWork.setWorkTitle("Benerin Mesin nomor "+(x+1));
//            newWork.setWorkDescShort("Benerin mesin dari klien. Mesin dapat komplain tidak jalan");
//            newWork.setWorkStatus("Pending");
//            newWork.setWorkDateTime(myDate);
//
//            workData.add(newWork);
//        }
//
//        setAdapter();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("onCreate", "SAVING INSTANCES!");
        outState.putString("globalArray", dataGlobalArray.toString());
        outState.putParcelableArrayList("work", workData);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("ErrorJSON", "Error Message: "+error);
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            //Log.d("JSONResponse", "JSON Response: "+response.toString(2));
            Log.d("onCreate", "API SUCCESS!");
            //create local JSONObj
            JSONObject jsonObj = response;
            JSONArray dataArray = jsonObj.getJSONArray("data");
            dataGlobalArray = dataArray;
            Log.d("JSONAspac", "Record size: "+dataArray.length());
            //try to loop the array
            if(workData.size() == 0) {
                for (int z = 0; z < dataArray.length(); z++) {
                    JSONObject obj = dataArray.getJSONObject(z);
                    //Log.d("JSONContent", "Starting new array of values "+z);
                    //Log.d("JSONContent", obj.getString("date_service"));
                    JSONObject custJSON = obj.getJSONObject("customer_branch");
                    //Log.d("JSONContent", custJSON.getString("branch_name"));
                    //Log.d("JSONContent", custJSON.getString("branch_status"));
                    //Log.d("JSONContent", custJSON.getString("branch_address"));
                    //Log.d("JSONContent", custJSON.getString("office_phone_number"));
                    JSONObject teknisiJSON = obj.getJSONObject("teknisi");
                    //Log.d("JSONContent", teknisiJSON.getString("username"));
                    //Log.d("JSONContent", teknisiJSON.getString("email"));
                    //Log.d("JSONContent", teknisiJSON.getString("name"));
                    JSONArray mesinsArray = obj.getJSONArray("machines");
                    for(int y = 0; y < mesinsArray.length(); y++) {
                        JSONObject mesinJSON = mesinsArray.getJSONObject(y);
                        //Log.d("JSONContent", mesinJSON.getString("brand"));
                        //Log.d("JSONContent", mesinJSON.getString("model"));
                        //Log.d("JSONContent", mesinJSON.getString("serial_number"));
                    }

                    //create date formatting
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = formatter.parse(obj.getString("date_service"));

                    //create new work object
                    Work newWork = new Work();
                    newWork.setWorkTitle("Pekerjaan Rutin "+(z+1));
                    newWork.setWorkDescShort(custJSON.getString("branch_name"));
                    newWork.setWorkStatus("Pending");
                    newWork.setWorkDateTime(date);

                    workData.add(newWork);
                }

                setAdapter();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void getAllWorkData() {
        Log.d("onCreate", "GET SOME API!");
        //set the url
        //String url = getString(R.string.list_all_post);
        String url = "http://aspac.noti-technologies.com/api/getserviceschedule";
        String token = "";
        if(checkforSharedPreferences()) {
            sharedPref = getActivity().getSharedPreferences("userCred", Context.MODE_PRIVATE);
            token = "Bearer "+sharedPref.getString("token", "empty token");
        }

        //set headers
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", token);
        //set params
//        HashMap<String, String> params = new HashMap<>();
        JSONObject param = new JSONObject();

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        CustomJSONObjectRequest jsObjRequest = new CustomJSONObjectRequest(Request.Method.GET, url, param, headers, this, this);
        //add the request to the queue
        requestQueue.add(jsObjRequest);
    }

    private void setAdapter() {
        Log.d("onCreate", "SET ADAPTERS!");
        if(workData.size()>0){
            Log.d("setAdapter", "Setting up work list adapter");

            adapter = new WorkListAdapter(getActivity(), workData, res);
            work_list_listview.setAdapter(adapter);
            work_list_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(getActivity(), "Hey! You clicked on some work!", Toast.LENGTH_SHORT).show();
                    HomeActivity act = (HomeActivity) getActivity();
                    Bundle args = new Bundle();
                    try {
                        args.putString("data", dataGlobalArray.getJSONObject(position).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Fragment newFrag = new CreateFragment();
                    newFrag.setArguments(args);
                    act.changeFragment(newFrag);
                }
            });
        }
        else {
            Log.d("setAdapter", "The workData array is empty!");
        }
    }

    private boolean checkforSharedPreferences() {
        boolean result = false;
        sharedPref = getActivity().getSharedPreferences("userCred", Context.MODE_PRIVATE);
        if(sharedPref.contains("token")) {
            //sharedpref exist
            result = true;
        } else {
            //sharedpref does not exist. do nothing
        }
        return result;
    }
}
