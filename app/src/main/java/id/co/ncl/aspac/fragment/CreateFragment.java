package id.co.ncl.aspac.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.ncl.aspac.R;
import id.co.ncl.aspac.activity.HomeActivity;
import id.co.ncl.aspac.activity.LoginActivity;
import id.co.ncl.aspac.adapter.MesinLPSAdapter;
import id.co.ncl.aspac.customClass.CustomJSONObjectRequest;
import id.co.ncl.aspac.customClass.SparepartCompletionView;
import id.co.ncl.aspac.model.Mesin;
import id.co.ncl.aspac.customClass.ListViewUtility;
import id.co.ncl.aspac.model.Sparepart;

public class CreateFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {

    //@BindView(R.id.expandableLayout1) ExpandableRelativeLayout expandableLayout1;
    //@BindView(R.id.expandableLayout2) ExpandableRelativeLayout expandableLayout2;
    //@BindView(R.id.expandableButton1) Button expandableButton1;
    @BindView(R.id.cust_data) TextView cust_data;
    @BindView(R.id.engineer_name) TextView engineer_name;
    @BindView(R.id.date_time) TextView date_time;
    @BindView(R.id.daftar_mesin_list_view) ListView daftar_mesin_list_view;
    @BindView(R.id.create_form_4_card_view) CardView create_form_4_card_view;
    @BindView(R.id.create_form_button) Button create_form_button;
    @BindView(R.id.kerusakan_input) EditText kerusakan_input;
    @BindView(R.id.perbaikan_input) EditText perbaikan_input;

    private View view;
    private MesinLPSAdapter adapter;
    private int listViewHeight;
    private JSONArray dataMachineArray;
    private JSONObject dataKeren;
    private List<Mesin> mesinData = new ArrayList<Mesin>();
    private SharedPreferences sharedPref;
    private ProgressDialog progressDialog;

    private SparepartCompletionView completionView;
    private Sparepart[] people;
    private ArrayAdapter<Sparepart> adapterSpare;

//    @OnClick(R.id.expandableButton1)
//    public void clickMe() {
//        expandableLayout1.toggle();
//    }

//    @OnClick(R.id.expandableLayout1)
//    public void clickme() {
//        expandableButton1(view);
//    }

//    @OnClick(R.id.expandableLayout2)
//    public void clickme2() {
//        expandableButton2(view);
//    }

    @OnClick(R.id.create_form_button)
    public void sendForm() {
        //set the url
        String url = "http://aspac.noti-technologies.com/api/submitdatalps";

        create_form_button.setEnabled(false);

        progressDialog = new ProgressDialog(getActivity(), R.style.CustomDialog);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.show();

        //prepare date and datetime here
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
        String date = dateFormat.format(cal.getTime());
        String dateTime = dateTimeFormat.format(cal.getTime());

        String token = "";
        if(checkforSharedPreferences()) {
            sharedPref = getActivity().getSharedPreferences("userCred", Context.MODE_PRIVATE);
            token = "Bearer "+sharedPref.getString("token", "empty token");
        }
        //set headers
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", token);

        //array holder. must be changed with delivered from other fragment
        JSONArray jsonSpareparts1 = new JSONArray();
        JSONArray jsonSpareparts2 = new JSONArray();

        JSONArray jsonMachines = new JSONArray();
        JSONObject jsonMachine1 = new JSONObject();
        JSONObject jsonMachine2 = new JSONObject();
        JSONObject jsonSparepart1 = new JSONObject();
        JSONObject jsonSparepart2 = new JSONObject();
        try {
            jsonSparepart1.put("sparepart_id", "13");
            jsonSpareparts1.put(jsonSparepart1);
            jsonSparepart2.put("sparepart_id", "15");
            jsonSpareparts2.put(jsonSparepart2);

            jsonMachine1.put("machine_id", "11");
            jsonMachine1.put("serial_number", "aabbcc");
            jsonMachine1.put("rtbs_flag", "11");
            jsonMachine1.put("rtas_flag", "11");
            jsonMachine1.put("job_status", "11");
            jsonMachine1.put("garansi_number", "123456");
            jsonMachine1.put("sparepart_consumed",jsonSpareparts1);

            jsonMachine2.put("machine_id", "12");
            jsonMachine2.put("serial_number", "aabbcc");
            jsonMachine2.put("rtbs_flag", "12");
            jsonMachine2.put("rtas_flag", "12");
            jsonMachine2.put("job_status", "12");
            jsonMachine2.put("garansi_number", "123456");
            jsonMachine2.put("sparepart_consumed",jsonSpareparts2);

            jsonMachines.put(jsonMachine1);
            jsonMachines.put(jsonMachine2);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("type_lps", 1);
            jsonObj.put("customer_id", 8);
            jsonObj.put("customer_branch_id", 7);
            jsonObj.put("teknisi_id", 69);
            jsonObj.put("date_lps", date);
            jsonObj.put("tanggal_jam_selesai", dateTime);
            jsonObj.put("kerusakan", kerusakan_input.getText().toString());
            jsonObj.put("perbaikan", perbaikan_input.getText().toString());
            jsonObj.put("machine", jsonMachines);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        CustomJSONObjectRequest customJSONReq = new CustomJSONObjectRequest(Request.Method.POST, url, jsonObj, this, this);
        customJSONReq.setHeaders(headers);

        try {
            //Map<String, String> testH = jsObjRequest.getHeaders();
            //Log.d("headers",testH.get("Content-Type"));
            Log.d("headers", String.valueOf(customJSONReq.getHeaders()));
            Log.d("content", jsonObj.toString(2));
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestQueue.add(customJSONReq);
    }

    public CreateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null) {
            //get the arguments here
            try {
                dataKeren = new JSONObject(args.getString("data"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create, container, false);
        ButterKnife.bind(this, view);
        getActivity().setTitle("Penulisan LPS");

        if(dataKeren.length() != 0) {
            Log.d("JSONContent", "Starting new array of values");
            try {
                Log.d("JSONContent", dataKeren.getString("date_service"));
                date_time.setText(dataKeren.getString("date_service"));
                JSONObject custJSON = dataKeren.getJSONObject("customer_branch");
                //Log.d("JSONContent", custJSON.getString("branch_name"));
                //Log.d("JSONContent", custJSON.getString("branch_status"));
                //Log.d("JSONContent", custJSON.getString("branch_address"));
                //Log.d("JSONContent", custJSON.getString("office_phone_number"));
                cust_data.setText(custJSON.getString("branch_name") + "\n" + custJSON.getString("branch_status") + "\n" + custJSON.getString("branch_address") + "\n" + custJSON.getString("office_phone_number"));
                JSONObject teknisiJSON = dataKeren.getJSONObject("teknisi");
                //Log.d("JSONContent", teknisiJSON.getString("username"));
                //Log.d("JSONContent", teknisiJSON.getString("email"));
                //Log.d("JSONContent", teknisiJSON.getString("name"));
                engineer_name.setText(teknisiJSON.getString("name"));
                JSONArray mesinsArray = dataKeren.getJSONArray("machines");
                dataMachineArray = mesinsArray;
                for(int y = 0; y < mesinsArray.length(); y++) {
                    JSONObject mesinJSON = mesinsArray.getJSONObject(y);
                    //Log.d("JSONContent", mesinJSON.getString("brand"));
                    //Log.d("JSONContent", mesinJSON.getString("model"));
                    //Log.d("JSONContent", mesinJSON.getString("serial_number"));

                    //check data only if exists in mesinJSON
                    if(mesinJSON.has("machine_status")) {
                        Log.d("GODDAMN", "F*CK YEAH! DIS IS AWESOME");

                        JSONObject machineStatus = mesinJSON.getJSONObject("machine_status");
                        Log.d("JSONContent", machineStatus.getString("rtas_status"));
                        Log.d("JSONContent", machineStatus.getString("rtbs_status"));
                        Log.d("JSONContent", machineStatus.getString("machine_ok"));

                        JSONArray machineSpareparts = mesinJSON.getJSONArray("machine_spareparts");
                        for(int t = 0; t < machineSpareparts.length(); t++) {
                            JSONObject machineSparepart = machineSpareparts.getJSONObject(t);
                            Log.d("JSONContent", machineSparepart.getString("sparepart_id"));
                        }
                    }

                    //create new forum object
                    Mesin newMesin = new Mesin();
                    newMesin.setMesinBrand(mesinJSON.getString("brand"));
                    newMesin.setMesinModel(mesinJSON.getString("model"));
                    newMesin.setMesinNomorSeri(mesinJSON.getString("serial_number"));
                    //newMesin.setMesinStatus("Sehat");

                    mesinData.add(newMesin);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            setAdapter();

        } else {
            //create new forum object
            Mesin newMesin = new Mesin();
            newMesin.setMesinBrand("NCL");
            newMesin.setMesinModel("Super 3000");
            newMesin.setMesinNomorSeri("201730001127839");
            newMesin.setMesinStatus("Sehat");

            for (int x = 0; x < 6; x++) {
                mesinData.add(newMesin);
            }

            setAdapter();
        }

//        Sparepart[] parts = new Sparepart[]{
//                new Sparepart("AASDC23", "Head counter part"),
//                new Sparepart("W3CAASD", "Windle cash counter"),
//                new Sparepart("AB78XYY", "Stopgap brake"),
//                new Sparepart("LLOP888", "Machine bracket"),
//                new Sparepart("M0N87YD", "Outer shell"),
//                new Sparepart("112UUIY", "Grease")
//        };
//
//        adapterSpare = new ArrayAdapter<Sparepart>(getActivity(), android.R.layout.simple_list_item_1, parts);
//        completionView = (SparepartCompletionView) getActivity().findViewById(R.id.input_part_select);
//        completionView.setAdapter(adapterSpare);
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
        Log.d("errorResponse", String.valueOf(error));
        Log.d("errorResponse", String.valueOf(error.getLocalizedMessage()));
        Log.d("errorResponse", Arrays.toString(error.getStackTrace()));
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialog.dismiss();
        try {
            Log.d("onResponse", "DAMN YOU DID IT! HECK YEAH");
            Log.d("onResponse", response.toString(2));
            Toast.makeText(getActivity(), "Data berhasil disimpan!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //move to new fragment
        HomeActivity act = (HomeActivity) getActivity();
        Fragment newFrag = new WorkFragment();
        act.changeFragmentNoBS(newFrag);
    }

    private void setAdapter() {
        if(mesinData.size()>0){
            Log.d("setAdapter", "Setting up mesin adapter");

            adapter = new MesinLPSAdapter(getActivity(), mesinData, getResources());
            daftar_mesin_list_view.setAdapter(adapter);

            daftar_mesin_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    //Log.d("log", "mesin clicked");
//                    //make a new customDialog instead now
//                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
//                    // Get the layout inflater
//                    LayoutInflater inflater = getActivity().getLayoutInflater();
//                    mBuilder.setView(inflater.inflate(R.layout.dialog_mesin_service_list, null));
//                    mBuilder.setNeutralButton("Tutup", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                        }
//                    });
//
//                    AlertDialog dialog = mBuilder.create();
//                    dialog.show();

                    //move to new fragment
                    HomeActivity act = (HomeActivity) getActivity();
                    Bundle args = new Bundle();
                    try {
                        args.putString("data", dataMachineArray.getJSONObject(position).toString());
                        args.putString("dataKeren", dataKeren.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mesinData.clear();
                    adapter.notifyDataSetChanged();
                    Fragment newFrag = new CreateDetailFragment();
                    newFrag.setArguments(args);
                    act.changeFragmentNoBS(newFrag);
                }
            });
        }
        else {
            Log.d("setAdapter", "The mesinData array is empty!");
        }

//        daftar_mesin_list_view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//
//            public void onGlobalLayout() {
//                listViewHeight = daftar_mesin_list_view.getHeight();
//                Log.d("ListViewHeight", String.valueOf(listViewHeight));
//            }
//        });
//
//        // Set the CardView layoutParams
//        ViewGroup.LayoutParams params = create_form_4_card_view.getLayoutParams();
//        params.height = listViewHeight;
//
//        create_form_4_card_view.setLayoutParams(params);

        ListViewUtility.setListViewHeightBasedOnChildren(daftar_mesin_list_view);
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
