package id.co.ncl.aspac.fragment;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import id.co.ncl.aspac.R;
import id.co.ncl.aspac.customClass.CustomJSONObjectRequest;
import id.co.ncl.aspac.database.AspacSQLite;
import id.co.ncl.aspac.database.DatabaseManager;
import id.co.ncl.aspac.database.SparepartDao;
import id.co.ncl.aspac.model.Sparepart;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import static android.content.Context.NOTIFICATION_SERVICE;

public class HomeFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {

    private AspacSQLite myDb;
    private DatabaseManager dbManager;
    private SharedPreferences sharedPref;
    private ProgressDialog progressDialog;

    public HomeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        getActivity().setTitle("Beranda");
        myDb = new AspacSQLite(getContext());
        DatabaseManager.initializeInstance(myDb);
        dbManager = DatabaseManager.getInstance();

        checkForLocalData();

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
    public void onErrorResponse(VolleyError error) {
        progressDialog.dismiss();
        Log.d("ErrorJSON", "Error Message: "+error);
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            Log.d("JSONResponse", "JSON Response: "+response.toString(2));
            Log.d("onCreate", "API SUCCESS!");
            //create local JSONObj
            JSONObject jsonObj = response;
            JSONArray sparepartArray = jsonObj.getJSONArray("spareparts");
            for(int a = 0; a < sparepartArray.length(); a++) {
                JSONObject sparepartData = sparepartArray.getJSONObject(a);
                //prepare the sparepart object
                Sparepart sparepart = new Sparepart();
                sparepart.setSparepartID(sparepartData.getString("id"));
                sparepart.setCode(sparepartData.getString("code"));
                sparepart.setName(sparepartData.getString("name"));
                sparepart.setMachineID(sparepartData.getInt("machine_id"));

                SparepartDao spaDAO = new SparepartDao(dbManager);
                long sparepartID = spaDAO.insert(sparepart);
            }
            progressDialog.dismiss();
            Log.d("sparepartDb", "Sparepart has been downloaded!");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void checkForLocalData() {
        SparepartDao spaDAO = new SparepartDao(dbManager);
        int result = spaDAO.getCount();
        spaDAO.closeConnection();

//        if(result == 472) {
        if(result > 0) {
            //all data is here. no need for API calls
            Log.d("dataLocal", "Data has been cached!");
        } else {
            //wait with dialog
            progressDialog = new ProgressDialog(getActivity(), R.style.CustomDialog);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Mohon tunggu...");
            progressDialog.show();
            //get sparepart API
            getAllSparepartData();
        }
    }

    private void getAllSparepartData() {
        Log.d("onCreate", "GET SOME API!");
        //set the url
        String url = "http://103.26.208.118/api/getallsparepart";

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
