package id.co.ncl.aspac.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.ncl.aspac.R;
import id.co.ncl.aspac.activity.HomeActivity;
import id.co.ncl.aspac.adapter.MesinLPSAdapter;
import id.co.ncl.aspac.customClass.CustomJSONObjectRequest;
import id.co.ncl.aspac.customClass.PrinterCommands;
import id.co.ncl.aspac.database.AspacSQLite;
import id.co.ncl.aspac.database.DatabaseManager;
import id.co.ncl.aspac.database.MachineDao;
import id.co.ncl.aspac.database.ServiceDao;
import id.co.ncl.aspac.model.Machine;
import id.co.ncl.aspac.model.Mesin;
import id.co.ncl.aspac.customClass.ListViewUtility;
import id.co.ncl.aspac.model.Service;

public class CreateFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {

    //@BindView(R.id.expandableLayout1) ExpandableRelativeLayout expandableLayout1;
    //@BindView(R.id.expandableLayout2) ExpandableRelativeLayout expandableLayout2;
    //@BindView(R.id.expandableButton1) Button expandableButton1;
    @BindView(R.id.cust_data) TextView cust_data;
    //@BindView(R.id.engineer_name) TextView engineer_name;
    @BindView(R.id.date_time) TextView date_time;
    @BindView(R.id.daftar_mesin_list_view) ListView daftar_mesin_list_view;
    @BindView(R.id.create_form_4_card_view) CardView create_form_4_card_view;
    @BindView(R.id.create_form_button) Button create_form_button;
    @BindView(R.id.kerusakan_input) EditText kerusakan_input;
    @BindView(R.id.perbaikan_input) EditText perbaikan_input;
    //@BindView(R.id.print_button) Button print_button;

    private View view;
    private MesinLPSAdapter adapter;
    private int listViewHeight;
    private JSONArray dataMachineArray;
    private JSONObject dataKeren;
    private JSONArray machineSpareparts;
    private JSONArray machineStatusArray;
    private JSONObject machineStatus;
    private JSONObject finalJSONObj;
    private long serviceID = 0;
    private List<String> machineIDs = new ArrayList<>();
    private List<Mesin> mesinData = new ArrayList<Mesin>();
    private List<Machine> mesinArray = new ArrayList<>();
    private SharedPreferences sharedPref;
    private ProgressDialog progressDialog;
    private Service cachedService;

    private DatabaseManager dbManager;

//    private SparepartCompletionView completionView;
//    private Spare_Part[] people;
//    private ArrayAdapter<Spare_Part> adapterSpare;

    //new variables for the printing
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private BluetoothDevice bluetoothDevice;

    private OutputStream outputStream;
    private InputStream inputStream;
    private Thread thread;
    private Thread connThread;

    private byte[] readBuffer;
    private int readBufferPosition;
    private volatile boolean stopWorker;

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

//    @OnClick(R.id.print_button)
//    public void printDemo() {
//        //begin the bluetooth chain
//        FindBluetoothDevice();
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
            try {
                finalJSONObj = new JSONObject(sharedPref.getString("current_service_json", "empty"));
                finalJSONObj.put("kerusakan", kerusakan_input.getText().toString());
                finalJSONObj.put("perbaikan", perbaikan_input.getText().toString());

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("current_service_json", String.valueOf(finalJSONObj));
                editor.apply();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //set headers
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", token);

//        //array holder. must be changed with delivered from other fragment
//        JSONArray jsonSpareparts1 = new JSONArray();
//        JSONArray jsonSpareparts2 = new JSONArray();
//
//        JSONArray jsonMachines = new JSONArray();
//        JSONObject jsonMachine1 = new JSONObject();
//        JSONObject jsonMachine2 = new JSONObject();
//        JSONObject jsonSparepart1 = new JSONObject();
//        JSONObject jsonSparepart2 = new JSONObject();
//        try {
//            jsonSparepart1.put("sparepart_id", "13");
//            jsonSpareparts1.put(jsonSparepart1);
//            jsonSparepart2.put("sparepart_id", "15");
//            jsonSpareparts2.put(jsonSparepart2);
//
//            jsonMachine1.put("machine_id", "11");
//            jsonMachine1.put("serial_number", "aabbcc");
//            jsonMachine1.put("rtbs_flag", "11");
//            jsonMachine1.put("rtas_flag", "11");
//            jsonMachine1.put("job_status", "11");
//            jsonMachine1.put("garansi_number", "123456");
//            jsonMachine1.put("sparepart_consumed",jsonSpareparts1);
//
//            jsonMachine2.put("machine_id", "12");
//            jsonMachine2.put("serial_number", "aabbcc");
//            jsonMachine2.put("rtbs_flag", "12");
//            jsonMachine2.put("rtas_flag", "12");
//            jsonMachine2.put("job_status", "12");
//            jsonMachine2.put("garansi_number", "123456");
//            jsonMachine2.put("sparepart_consumed",jsonSpareparts2);
//
//            jsonMachines.put(jsonMachine1);
//            jsonMachines.put(jsonMachine2);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//        JSONObject jsonObj = new JSONObject();
//        try {
//            jsonObj.put("type_lps", 1);
//            jsonObj.put("customer_id", 8);
//            jsonObj.put("customer_branch_id", 7);
//            jsonObj.put("teknisi_id", 69);
//            jsonObj.put("date_lps", date);
//            jsonObj.put("tanggal_jam_selesai", dateTime);
//            jsonObj.put("kerusakan", kerusakan_input.getText().toString());
//            jsonObj.put("perbaikan", perbaikan_input.getText().toString());
//            jsonObj.put("machine", jsonMachines);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        CustomJSONObjectRequest customJSONReq = new CustomJSONObjectRequest(Request.Method.POST, url, finalJSONObj, this, this);
        customJSONReq.setHeaders(headers);

        try {
            //Map<String, String> testH = jsObjRequest.getHeaders();
            //Log.d("headers",testH.get("Content-Type"));
            Log.d("headers", String.valueOf(customJSONReq.getHeaders()));
            Log.d("content", finalJSONObj.toString(2));
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
//            try {
//                dataKeren = new JSONObject(args.getString("data"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            serviceID = args.getLong("service_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create, container, false);
        ButterKnife.bind(this, view);
        getActivity().setTitle("Penulisan LPS");
        dbManager = DatabaseManager.getInstance();


        if(serviceID != 0) {
//            Log.d("JSONContent", "Starting new array of values");
//            try {
//                Log.d("JSONContent", dataKeren.getString("date_service"));
//                date_time.setText(dataKeren.getString("date_service"));
//                JSONObject custJSON = dataKeren.getJSONObject("customer_branch");
//                //Log.d("JSONContent", custJSON.getString("branch_name"));
//                //Log.d("JSONContent", custJSON.getString("branch_status"));
//                //Log.d("JSONContent", custJSON.getString("branch_address"));
//                //Log.d("JSONContent", custJSON.getString("office_phone_number"));
//                cust_data.setText(custJSON.getString("branch_name") + "\n" + custJSON.getString("branch_status") + "\n" + custJSON.getString("branch_address") + "\n" + custJSON.getString("office_phone_number"));
//                JSONObject teknisiJSON = dataKeren.getJSONObject("teknisi");
//                //Log.d("JSONContent", teknisiJSON.getString("username"));
//                //Log.d("JSONContent", teknisiJSON.getString("email"));
//                //Log.d("JSONContent", teknisiJSON.getString("name"));
//                engineer_name.setText(teknisiJSON.getString("name"));
//                JSONArray mesinsArray = dataKeren.getJSONArray("machines");
//                dataMachineArray = mesinsArray;
//                for(int y = 0; y < mesinsArray.length(); y++) {
//                    JSONObject mesinJSON = mesinsArray.getJSONObject(y);
//                    //Log.d("JSONContent", mesinJSON.getString("brand"));
//                    //Log.d("JSONContent", mesinJSON.getString("model"));
//                    //Log.d("JSONContent", mesinJSON.getString("serial_number"));
//
//                    //check data only if exists in mesinJSON
//                    if(mesinJSON.has("machine_status")) {
//                        Log.d("GODDAMN", "F*CK YEAH! DIS IS AWESOME");
//
//                        JSONObject machineStatus = mesinJSON.getJSONObject("machine_status");
//                        Log.d("JSONContent", machineStatus.getString("rtas_status"));
//                        Log.d("JSONContent", machineStatus.getString("rtbs_status"));
//                        Log.d("JSONContent", machineStatus.getString("machine_ok"));
//
//                        JSONArray spareparts = mesinJSON.getJSONArray("spareparts");
//
//                        JSONArray machineSpareparts = mesinJSON.getJSONArray("machine_spareparts");
//                        for(int t = 0; t < machineSpareparts.length(); t++) {
//                            JSONObject machineSparepart = machineSpareparts.getJSONObject(t);
//                            if(machineSpareparts.length() > 0) {
//                                JSONObject sparepart = spareparts.getJSONObject(t);
//                                Log.d("JSONBaru", sparepart.getString("code"));
//                                Log.d("JSONBaru", sparepart.getString("name"));
//                                Log.d("JSONBaru", sparepart.getString("sell_price"));
//                            }
//                            Log.d("JSONContent", machineSparepart.getString("sparepart_id"));
//                        }
//                    }
//
//                    //create new forum object
//                    Mesin newMesin = new Mesin();
//                    newMesin.setMesinBrand(mesinJSON.getString("brand"));
//                    newMesin.setMesinModel(mesinJSON.getString("model"));
//                    newMesin.setMesinNomorSeri(mesinJSON.getString("serial_number"));
//                    //newMesin.setMesinStatus("Sehat");
//
//                    mesinData.add(newMesin);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

            ServiceDao serDAO = new ServiceDao(dbManager);
            Service service = serDAO.get(serviceID);
            cachedService = service;
            setupFinalJSON(service);
            date_time.setText(service.getDateService());
            cust_data.setText(service.getName() + "\n" + service.getStatus() + "\n" + service.getAddress() + "\n" + service.getOfficePhoneNumber());
            //engineer_name.setText(service.getTname());

            serDAO.closeConnection();

            MachineDao macDAO = new MachineDao(dbManager);
            mesinArray = macDAO.getAllByServiceID(serviceID);
            macDAO.closeConnection();

            for(int h = 0; h < mesinArray.size(); h++) {
                Machine mac = mesinArray.get(h);
                //create new forum object
                Mesin newMesin = new Mesin();
                newMesin.setMesinBrand(mac.getBrand());
                newMesin.setMesinModel(mac.getModel());
                newMesin.setMesinNomorSeri(mac.getSerialNumber());
                machineIDs.add(mac.getMachineID());
                mesinData.add(newMesin);
            }
            //checkForSparepartData();
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

//        Spare_Part[] parts = new Spare_Part[]{
//                new Spare_Part("AASDC23", "Head counter part"),
//                new Spare_Part("W3CAASD", "Windle cash counter"),
//                new Spare_Part("AB78XYY", "Stopgap brake"),
//                new Spare_Part("LLOP888", "Machine bracket"),
//                new Spare_Part("M0N87YD", "Outer shell"),
//                new Spare_Part("112UUIY", "Grease")
//        };
//
//        adapterSpare = new ArrayAdapter<Spare_Part>(getActivity(), android.R.layout.simple_list_item_1, parts);
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

            //delete the sharedpref
            SharedPreferences preferences = getActivity().getSharedPreferences("userCred", Context.MODE_PRIVATE);
            preferences.edit().remove("current_service_json").apply();

            //delete the sent service
            ServiceDao serDAO = new ServiceDao(dbManager);
            serDAO.delete(cachedService);
            serDAO.closeConnection();

        } catch (Exception e) {
            e.printStackTrace();
        }

        //move to new fragment
        HomeActivity act = (HomeActivity) getActivity();
        Fragment newFrag = new WorkFragment();
        act.changeFragmentNoBS(newFrag);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                if(resultCode == Activity.RESULT_OK) {
                    pairBluetoothDevice();
                }
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            disconnectBT();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
//            if(bluetoothSocket != null) {
//                bluetoothSocket.close();
//            }
//            if(bluetoothAdapter != null) {
//                bluetoothAdapter.disable();
//            }
            disconnectBT();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkForSparepartData() {
        boolean result = false;
        //check for data from sparepart fragment
        Bundle args = getArguments();
        if(args != null) {
            //check for special JSON wrapped in String variable
            if(args.containsKey("machine_status") && args.containsKey("machine_spareparts")) {
                try {
                    machineStatus = new JSONObject(args.getString("machine_status"));
                    machineSpareparts = new JSONArray(args.getString("machine_spareparts"));
                    result = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if(args.containsKey("machine_status")){
                try {
                    machineStatus = new JSONObject(args.getString("machine_status"));
                    result = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private void setupFinalJSON(Service service) {
        //start creating the JSON
        String token = "";
        if(checkforSharedPreferences()) {
            sharedPref = getActivity().getSharedPreferences("userCred", Context.MODE_PRIVATE);

            //prepare date and datetime here
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
            String date = dateFormat.format(cal.getTime());
            String dateTime = dateTimeFormat.format(cal.getTime());

            //check if there are any previous json
            if(sharedPref.getString("current_service_json", "empty").equals("empty")) {
                //create the new JSOn
                finalJSONObj = new JSONObject();
                try {
                    finalJSONObj.put("type_lps", service.getTypeService());
                    finalJSONObj.put("customer_id", service.getCustomerID());
                    finalJSONObj.put("customer_branch_id", service.getCBID());
                    finalJSONObj.put("teknisi_id", service.getTID());
                    finalJSONObj.put("date_lps", date);
                    finalJSONObj.put("tanggal_jam_selesai", dateTime);
                    //finalJSONObj.put("kerusakan", kerusakan_input.getText().toString());
                    //finalJSONObj.put("perbaikan", perbaikan_input.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("current_service_json", String.valueOf(finalJSONObj));
                editor.apply();
            } else {
                //assign the JSON
                try {
                    finalJSONObj = new JSONObject(sharedPref.getString("current_service_json", "empty"));
                    if(checkForSparepartData()) {
                        //put inner object first
                        if(machineSpareparts.length() > 0) {
                            machineStatus.put("sparepart_consumed", machineSpareparts);
                        }
                        //and then, put the object into json
                        if(finalJSONObj.has("machine")) {
                            //if it HAS the name
                            machineStatusArray = finalJSONObj.getJSONArray("machine");
                            //check if we add NEW machine, or REVISE old machine data
                            for(int g = 0; g < machineStatusArray.length(); g++) {
                                JSONObject machineStatusOld = machineStatusArray.getJSONObject(g);
                                if(machineStatusOld.getInt("machine_id") == machineStatus.getInt("machine_id")) {
                                    //replace the old on with the new one, instead of ADDING
                                    machineStatusArray.put(g, machineStatus);
                                    break;
                                } else {
                                    if(g - machineStatusArray.length() == 1) {
                                        machineStatusArray.put(machineStatus);
                                    }
                                }
                            }
                            //then put the final array back to json
                            finalJSONObj.put("machine", machineStatusArray);
                        } else {
                            //if it HAS NOT the name
                            machineStatusArray = new JSONArray();
                            machineStatusArray.put(machineStatus);
                            finalJSONObj.put("machine", machineStatusArray);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("current_service_json", String.valueOf(finalJSONObj));
                editor.apply();
            }
        }
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
                    args.putLong("service_id", serviceID);
                    args.putString("machine_id", machineIDs.get(position));
//                    try {
//                        args.putString("data", dataMachineArray.getJSONObject(position).toString());
//                        args.putString("dataKeren", dataKeren.toString());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
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

    //do find the bluetooth first
    private void FindBluetoothDevice(){
        try {
            progressDialog = new ProgressDialog(getActivity(), R.style.CustomDialog);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Melakukan koneksi...");
            progressDialog.show();

            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            //bluetoothAdapter.enable();
            if(bluetoothAdapter == null){
                Toast.makeText(getActivity(), "No Bluetooth Adapter found", Toast.LENGTH_LONG).show();
            } else {
                if(!bluetoothAdapter.isEnabled()){
                    Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBT,0);
                } else {
                    pairBluetoothDevice();
                }
            }

            //try using thread?
//            Thread thr = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//                    bluetoothAdapter.enable();
//                    if(bluetoothAdapter == null){
//                        Toast.makeText(getActivity(), "No Bluetooth Adapter found", Toast.LENGTH_LONG).show();
//                    }
//                    if(bluetoothAdapter.isEnabled()){
//                        Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                        //startActivityForResult(enableBT,0);
//                    }
//
//                    Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();
//
//                    if(pairedDevice.size()>0){
//                        for(BluetoothDevice pairedDev:pairedDevice){
//                            Log.d("printerName", pairedDev.getName());
//                            // My Bluetoth printer name is RPP-02
//                            if(pairedDev.getName().equals("RPP-02")){
//                                bluetoothDevice = pairedDev;
//                                Toast.makeText(getActivity(), "Bluetooth Printer Attached: "+pairedDev.getName(), Toast.LENGTH_LONG).show();
//                                break;
//                            }
//                        }
//                    }
//
//                    //open bluetooth printer
//                    openBluetoothPrinter();
//                }
//            });
//            thr.run();
        } catch(Exception ex){
            ex.printStackTrace();
        }

    }

    private void pairBluetoothDevice() {
        Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();

        if (pairedDevice.size() > 0) {
            for (BluetoothDevice pairedDev : pairedDevice) {
                Log.d("printerName", pairedDev.getName());
                // My Bluetoth printer name is RPP-02
                if (pairedDev.getName().equals("RPP-02")) {
                    bluetoothDevice = pairedDev;
                    Toast.makeText(getActivity(), "Bluetooth Printer Attached: " + pairedDev.getName(), Toast.LENGTH_LONG).show();
                    break;
                } else {
                    Log.d("bluetoothDevice", "item not found!");
                }
            }
            //open bluetooth printer
            openBluetoothPrinter();
        } else {
            Log.d("pairedBluetooth", "empty paired list");
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "DeviceConnected", Toast.LENGTH_SHORT).show();
                //begin to listen for data stream
                beginListenData();
            } else if(msg.what == 1) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Terjadi kesalahan dengan koneksi", Toast.LENGTH_SHORT).show();
            }
        }
    };

    // then, Open Bluetooth Printer connection
    private void openBluetoothPrinter() {
        connThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //Standard uuid from string //
                    UUID uuidSting = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
                    bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuidSting);
                    bluetoothAdapter.cancelDiscovery();
                    bluetoothSocket.connect();
                    outputStream = bluetoothSocket.getOutputStream();
                    inputStream = bluetoothSocket.getInputStream();
                    mHandler.sendEmptyMessage(0);

                }catch (Exception ex){
                    ex.printStackTrace();
                    Log.d("errorMsg", ex.getMessage());
                    mHandler.sendEmptyMessage(1);
//            try {
//                Log.e("errorFall","trying fallback...");
//
//                UUID uuidSting = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
////                final Method m = bluetoothDevice.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
////                bluetoothSocket = (BluetoothSocket) m.invoke(bluetoothDevice, 1);
//
//                bluetoothSocket =(BluetoothSocket) bluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(bluetoothDevice,1);
//                bluetoothSocket.connect();
//
//                //Log.e("","Connected");
//            }
//            catch (Exception e2) {
//                Log.e("", "Couldn't establish Bluetooth connection!");
//                e2.printStackTrace();
//                bluetoothAdapter.disable();
//                //bluetoothSocket.close();
//                //inputStream.close();
//                //outputStream.close();
//            }
                }
            }
        });
        connThread.start();
    }

    void beginListenData(){
        try{
            final Handler handler =new Handler();
            final byte delimiter=10;
            stopWorker =false;
            readBufferPosition=0;
            readBuffer = new byte[1024];

            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!Thread.currentThread().isInterrupted() && !stopWorker){
                        try{
                            int byteAvailable = inputStream.available();
                            if(byteAvailable>0){
                                byte[] packetByte = new byte[byteAvailable];
                                inputStream.read(packetByte);

                                for(int i=0; i<byteAvailable; i++){
                                    byte b = packetByte[i];
                                    if(b==delimiter){
                                        byte[] encodedByte = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer,0,
                                                encodedByte,0,
                                                encodedByte.length
                                        );
                                        final String data = new String(encodedByte,"US-ASCII");
                                        readBufferPosition=0;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                //lblPrinterName.setText(data);
                                                Log.d("dataPrint", data);
                                            }
                                        });
                                    }else{
                                        readBuffer[readBufferPosition++]=b;
                                    }
                                }
                            }
                        }catch(Exception ex){
                            Log.d("listenDataError", ex.getMessage());
                            stopWorker=true;
                        }
                    }
                }
            });
            thread.start();
            //thread.join();
            //print the data
            printData();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    // Printing Text to Bluetooth Printer //
    void printData() throws IOException {
        try{
//            String msg = textBox.getText().toString();
//            msg+="\n";
//            Log.d("message", msg);
//            byte[] msgBuffer = msg.getBytes();
//            outputStream.write(msgBuffer);

            printCustom(finalJSONObj.getString("tanggal_jam_selesai"), 0, 0);
            printNewLine();
            printCustom("Asia Pacific", 3, 1);
            printCustom("RT.2/RW.3, Kamal Muara,", 0, 1);
            printCustom("Penjaringan, North Jakarta City,", 0, 1);
            printCustom("Jakarta 14470", 0, 1);
            printCustom("Telp: (021) 56983222", 0, 1);
            printNewLine();
            printNewLine();
            printCustom(cachedService.getName(), 2, 0);
            printCustom(cachedService.getAddress(), 1, 0);
            printNewLine();

            for(int j = 0; j < mesinArray.size(); j++) {
                Machine mac = mesinArray.get(j);
                printCustom("Mesin "+(j+1), 0, 0);
                printCustom("No seri:"+mac.getSerialNumber(), 0, 0);
                printNewLine();
            }

            printNewLine();
            printCustom("Thank you", 3, 1);
            printNewLine();
            printNewLine();
            printNewLine();

//            printCustom("Font sizes", 5, 1);
//            printNewLine();
//            printCustom("Text Size 0", 0, 0);
//            printCustom("Text Size 0", 0, 1);
//            printCustom("Text Size 0", 0, 2);
//
//            printCustom("Text Size 1", 1, 0);
//            printCustom("Text Size 2", 2, 0);
//            printCustom("Text Size 3", 3, 0);
//            printCustom("Text Size 4", 4, 0); //these sizes are experimental byte I made my own
//            printCustom("Text Size 5", 5, 0); //these sizes are experimental byte I made my own
//
//            printNewLine();
//            printNewLine();
//            printCustom("Bill example", 5, 1);
//            printNewLine();
//
//            printCustom("Fair Group BD",2,1);
//            printCustom("Pepperoni Foods Ltd.",0,1);
//            //printPhoto(R.drawable.ic_icon_pos);
//            printCustom("H-123, R-123, Dhanmondi, Dhaka-1212",0,1);
//            printCustom("Hot Line: +88000 000000",0,1);
//            printCustom("Vat Reg : 0000000000,Mushak : 11",0,1);
//            String dateTime[] = getDateTime();
//            printText(leftRightAlign(dateTime[0], dateTime[1]));
//            printNewLine();
//            printText(leftRightAlign("Qty: Name" , "Price "));
//            printNewLine();
//            printCustom(new String(new char[32]).replace("\0", "."),0,1);
//            printText(leftRightAlign("Total" , "2,0000/="));
//            printNewLine();
//            printCustom("Thank you for coming & we look",0,1);
//            printCustom("forward to serve you again",0,1);
//            printNewLine();
//            printNewLine();

            //lblPrinterName.setText("Printing Text...");
            Toast.makeText(getActivity(), "Mencetak tulisan...", Toast.LENGTH_SHORT).show();
            //after finished, disconnect the printer
            //disconnectBT();
            //print_button.setEnabled(false);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    // Disconnect Printer //
    void disconnectBT() throws IOException {
        try {
            stopWorker=true;
            if(outputStream != null) {
                outputStream.close();
            }
            if(inputStream != null) {
                inputStream.close();
            }
            if(bluetoothSocket != null) {
                bluetoothSocket.close();
            }
            //bluetoothAdapter.disable();
            //lblPrinterName.setText("Printer Disconnected.");
            //Toast.makeText(getActivity(), "Printer Terputus", Toast.LENGTH_SHORT).show();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //print new line
    private void printNewLine() {
        try {
            outputStream.write(PrinterCommands.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String[] getDateTime() {
        final Calendar c = Calendar.getInstance();
        String dateTime [] = new String[2];
        dateTime[0] = c.get(Calendar.DAY_OF_MONTH) +"/"+ c.get(Calendar.MONTH) +"/"+ c.get(Calendar.YEAR);
        dateTime[1] = c.get(Calendar.HOUR_OF_DAY) +":"+ c.get(Calendar.MINUTE);
        return dateTime;
    }

    private String leftRightAlign(String str1, String str2) {
        String ans = str1 +str2;
        if(ans.length() <25){
            int n = (25 - str1.length() + str2.length());
            ans = str1 + new String(new char[n]).replace("\0", " ") + str2;
        }
        return ans;
    }

    //print text
    private void printText(String msg) {
        try {
            // Print normal text
            outputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //print custom
    private void printCustom(String msg, int size, int align) {
        //Print config "mode"
        byte[] cc = new byte[]{0x1B,0x21,0x03};  // 0- normal size text
        //byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
        byte[] bb = new byte[]{0x1B,0x21,0x08};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B,0x21,0x20}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1B,0x21,0x10}; // 3- bold with large text
        byte[] bb4 = new byte[]{0x1B,0x21,0x5}; //experimental
        byte[] bb5 = new byte[]{0x1B,0x21,0x30}; //experimental
        try {
            switch (size){
                case 0:
                    outputStream.write(cc);
                    break;
                case 1:
                    outputStream.write(bb);
                    break;
                case 2:
                    outputStream.write(bb2);
                    break;
                case 3:
                    outputStream.write(bb3);
                    break;
                case 4:
                    outputStream.write(bb4);
                    break;
                case 5:
                    outputStream.write(bb5);
                    break;
            }

            switch (align){
                case 0:
                    //left align
                    outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    outputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
            }
            outputStream.write(msg.getBytes());
            outputStream.write(PrinterCommands.LF);
            //outputStream.write(cc);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d("SocketClose", "SocketClosed");
        } catch (IOException ex) {
            Log.d("SocketClose", "CouldNotCloseSocket");
        }
    }
}
