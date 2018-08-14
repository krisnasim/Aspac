package id.co.ncl.aspac.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
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
import id.co.ncl.aspac.activity.SignatureActivity;
import id.co.ncl.aspac.adapter.MesinLPSAdapter;
import id.co.ncl.aspac.customClass.PrinterCommands;
import id.co.ncl.aspac.customClass.VolleyMultipartRequest;
import id.co.ncl.aspac.customClass.VolleySingleton;
import id.co.ncl.aspac.database.DatabaseManager;
import id.co.ncl.aspac.dao.MachineDao;
import id.co.ncl.aspac.dao.ServiceDao;
import id.co.ncl.aspac.model.Machine;
import id.co.ncl.aspac.model.Mesin;
import id.co.ncl.aspac.customClass.ListViewUtility;
import id.co.ncl.aspac.model.Service;

public class CreateFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject>, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @BindView(R.id.cust_data) TextView cust_data;
    @BindView(R.id.date_time) TextView date_time;
    @BindView(R.id.daftar_mesin_list_view) ListView daftar_mesin_list_view;
    @BindView(R.id.create_form_4_card_view) CardView create_form_4_card_view;
    @BindView(R.id.create_form_button) Button create_form_button;
    @BindView(R.id.kerusakan_input) EditText kerusakan_input;
    //@BindView(R.id.perbaikan_input) EditText perbaikan_input;
    @BindView(R.id.keterangan_input) EditText keterangan_input;
    @BindView(R.id.nik_pic_input) EditText nik_pic_input;
    @BindView(R.id.no_pic_input) EditText no_pic_input;
    @BindView(R.id.signature_preview_img) ImageView signature_preview_img;

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
    private Bitmap signedBitmap;
    private Calendar finalCalendar;

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

    @OnClick(R.id.create_form_button)
    public void sendForm() {
        //set the url
        //String url = "http://103.26.208.118/api/submitdatalps";
        String url = "http://103.26.208.118/api/postRoutineLPS";

        create_form_button.setEnabled(false);

        progressDialog = new ProgressDialog(getActivity(), R.style.CustomDialog);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.show();

        //final check bitmap null or not
        //Log.d("FINAL sig width: ", String.valueOf(signedBitmap.getWidth()));
        //Log.d("FINAL sig height: ", String.valueOf(signedBitmap.getHeight()));

        //prepare date and datetime here
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
        final String date = dateFormat.format(cal.getTime());
        final String dateTime = dateTimeFormat.format(cal.getTime());

        String token = "";
        if(checkforSharedPreferences()) {
            sharedPref = getActivity().getSharedPreferences("userCred", Context.MODE_PRIVATE);
            token = "Bearer "+sharedPref.getString("token", "empty token");
            try {
                //finalJSONObj = new JSONObject(sharedPref.getString("current_service_json", "empty"));
                finalJSONObj = new JSONObject(sharedPref.getString(cachedService.getNoLPS(), "empty"));
                finalJSONObj.put("no_lps", cachedService.getNoLPS());
                //finalJSONObj.put("teknisi_id", 121);
                finalJSONObj.put("kerusakan", kerusakan_input.getText().toString());
                //finalJSONObj.put("perbaikan", perbaikan_input.getText().toString());
                finalJSONObj.put("keterangan", keterangan_input.getText().toString());
                finalJSONObj.put("nik_pic", nik_pic_input.getText().toString());
                finalJSONObj.put("no_pic", no_pic_input.getText().toString());
                //finalJSONObj.put("date_lps", date);
                //finalJSONObj.put("tanggal_jam_selesai", dateTime);

                Log.d("StringfyJSON", String.valueOf(finalJSONObj));

                SharedPreferences.Editor editor = sharedPref.edit();
                //editor.putString("current_service_json", String.valueOf(finalJSONObj));
                editor.putString(cachedService.getNoLPS(), String.valueOf(finalJSONObj));
                editor.apply();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //set headers
        Map<String, String> headers = new HashMap<>();
        //headers.put("Content-Type", "multipart/form-data");
        headers.put("Authorization", token);

        VolleyMultipartRequest newReq = new VolleyMultipartRequest(url, headers, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                progressDialog.dismiss();
                try {
                    Log.d("onResponse", "DAMN YOU DID IT! HECK YEAH");
                    Log.d("onResponse", response.toString());
                    Toast.makeText(getActivity(), "Data berhasil disimpan!", Toast.LENGTH_SHORT).show();

                    //delete the sharedpref
                    SharedPreferences preferences = getActivity().getSharedPreferences("userCred", Context.MODE_PRIVATE);
                    //preferences.edit().remove("current_service_json").apply();
                    preferences.edit().remove(cachedService.getNoLPS()).apply();

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
        }, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //convert JSON into Map
                Type type = new TypeToken<Map<String, Object>>(){}.getType();
                Gson gson = new Gson();

                try {
                    Log.d("checkFinalJSON", finalJSONObj.toString(2));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("StringfyJSON", String.valueOf(finalJSONObj));

                params = gson.fromJson(String.valueOf(finalJSONObj), type);

                //Log.d("params", params.get("kerusakan"));
                //Log.d("params", params.get("perbaikan"));
                //Log.d("params", params.get("keterangan"));
                //Log.d("params", params.get("nik_pic"));
                //Log.d("params", params.get("no_pic"));
                //Log.d("params", params.get("date_lps"));
                //Log.d("params", params.get("tanggal_jam_selesai"));

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                signedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                params.put("signature_image", new DataPart("signature.jpg", byteArray, "image/jpeg"));

                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(newReq);
    }

    @OnClick(R.id.date_time)
    public void setDateTime() {
        finalCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, finalCalendar.get(Calendar.YEAR), finalCalendar.get(Calendar.MONTH), finalCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @OnClick(R.id.clear_signature_button)
    public void clearSignature() {
        //prepare the filled in data first
        sharedPref = getActivity().getSharedPreferences("userCred", Context.MODE_PRIVATE);
        try {
            finalJSONObj = new JSONObject(sharedPref.getString(cachedService.getNoLPS(), "empty"));
            finalJSONObj.put("no_lps", cachedService.getNoLPS());
            //finalJSONObj.put("teknisi_id", 121);
            finalJSONObj.put("kerusakan", kerusakan_input.getText().toString());
            //finalJSONObj.put("perbaikan", perbaikan_input.getText().toString());
            finalJSONObj.put("keterangan", keterangan_input.getText().toString());
            finalJSONObj.put("nik_pic", nik_pic_input.getText().toString());
            finalJSONObj.put("no_pic", no_pic_input.getText().toString());
            //finalJSONObj.put("date_lps", date);
            finalJSONObj.put("tanggal_jam_selesai", date_time.getText().toString());

            SharedPreferences.Editor editor = sharedPref.edit();
            //editor.putString("current_service_json", String.valueOf(finalJSONObj));
            editor.putString(cachedService.getNoLPS(), String.valueOf(finalJSONObj));
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(getActivity(), SignatureActivity.class);
        intent.putExtra("service_id", serviceID);
        startActivity(intent);
    }

    public CreateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null) {
            serviceID = args.getLong("service_id");
            Log.d("receivedSerID", String.valueOf(serviceID));

            //check if signature image exist in arguments
            if(args.getByteArray("signature_image") != null) {
                byte[] byteArray = args.getByteArray("signature_image");
                signedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                //check bitmap null or not
                Log.d("got signature width: ", String.valueOf(signedBitmap.getWidth()));
                Log.d("got signature height: ", String.valueOf(signedBitmap.getHeight()));

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
        dbManager = DatabaseManager.getInstance();
        //setupSignature();
        if(signedBitmap != null) {
            signature_preview_img.setImageBitmap(signedBitmap);
        }

        if(serviceID != 0) {
            ServiceDao serDAO = new ServiceDao(dbManager);
            Service service = serDAO.get(serviceID);
            cachedService = service;
            setupFinalJSON(service);
            //date_time.setText(service.getDateService());
            //cust_data.setText(service.getName() + "\n" + service.getStatus() + "\n" + service.getAddress() + "\n" + service.getOfficePhoneNumber());
            cust_data.setText(service.getCustomerName() + "\n" + service.getAddress() + "\n" + service.getOfficePhoneNumber());
            //kerusakan_input.setText(service.);

            serDAO.closeConnection();

            MachineDao macDAO = new MachineDao(dbManager);
            mesinArray = macDAO.getAllByServiceID(serviceID);
            macDAO.closeConnection();

            for(int h = 0; h < mesinArray.size(); h++) {
                Machine mac = mesinArray.get(h);
                //Log.d("mesin", mac.getBrand());
                Log.d("mesin", mac.getName());
                Log.d("mesin", String.valueOf(mac.getTempServiceID()));
                //Log.d("mesin", String.valueOf(mac.getTempServiceID()));
                Log.d("mesin", String.valueOf(mac.getId()));
                //create new forum object
                Mesin newMesin = new Mesin();
                //newMesin.setMesinBrand(mac.getBrand());
                newMesin.setMesinModel(mac.getName());
                newMesin.setMesinNomorSeri(mac.getSerialNumber());
                //Log.d("machineIDs", mac.getBrand());
                //Log.d("machineTempIDs", String.valueOf(mac.getTempServiceID()));
                //machineIDs.add(String.valueOf(mac.getTempServiceID()));
                machineIDs.add(String.valueOf(mac.getTempServiceID()));
                mesinData.add(newMesin);
            }
            //checkForSparepartData();
            setAdapter();
        } else {
            //create new forum object
            Mesin newMesin = new Mesin();
            //newMesin.setMesinBrand("NCL");
            newMesin.setMesinModel("Super 3000");
            newMesin.setMesinNomorSeri("201730001127839");
            newMesin.setMesinStatus("Sehat");

            for (int x = 0; x < 6; x++) {
                mesinData.add(newMesin);
            }

            setAdapter();
        }
        return view;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        finalCalendar.set(i, i1, i2);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, finalCalendar.get(Calendar.HOUR_OF_DAY), finalCalendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        finalCalendar.set(Calendar.HOUR_OF_DAY, i);
        finalCalendar.set(Calendar.MINUTE, i1);
        //set the date and time
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy, HH:mm");
        date_time.setText(format.format(finalCalendar.getTime()));
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
            //Log.d("onResponse", "DAMN YOU DID IT! HECK YEAH");
            Log.d("onResponse", response.toString(2));
            Toast.makeText(getActivity(), "Data berhasil disimpan!", Toast.LENGTH_SHORT).show();

            //delete the sharedpref
            SharedPreferences preferences = getActivity().getSharedPreferences("userCred", Context.MODE_PRIVATE);
            //preferences.edit().remove("current_service_json").apply();
            preferences.edit().remove(cachedService.getNoLPS()).apply();

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
            disconnectBT();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupSignature() {
//        signature_pad.setOnSignedListener(new SignaturePad.OnSignedListener() {
//            @Override
//            public void onStartSigning() {
//
//            }
//
//            @Override
//            public void onSigned() {
//                //this one return normal imaage with white background
//                signedBitmap = signature_pad.getSignatureBitmap();
//                //this one return png signture with no background
//                //signedBitmap = signature_pad.getTransparentSignatureBitmap();
//                //this one return vector. No idea how to store this
//                //String signedVcotor = signature_pad.getSignatureSvg();
//
//                //check bitmap null or not
//                Log.d("signature width: ", String.valueOf(signedBitmap.getWidth()));
//                Log.d("signature height: ", String.valueOf(signedBitmap.getHeight()));
//            }
//
//            @Override
//            public void onClear() {
//
//            }
//        });
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
                    Log.d("sparepartArray", args.getString("machine_spareparts"));
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

            //check if there are any previous json
            //if(sharedPref.getString("current_service_json", "empty").equals("empty")) {
            if(sharedPref.getString(service.getNoLPS(), "empty").equals("empty")) {
                Log.d("finalJSON", "NO FINAL JSON EXIST!");
                //create the new JSOn
                finalJSONObj = new JSONObject();
                try {
                    finalJSONObj.put("no_lps", service.getNoLPS());
                    //finalJSONObj.put("type_lps", service.getTypeService());
                    finalJSONObj.put("customer_id", String.valueOf(service.getCustomerID()));
                    finalJSONObj.put("customer_branch_id", String.valueOf(service.getCBID()));
                    finalJSONObj.put("teknisi_id", "111");
                    //finalJSONObj.put("date_lps", date);
                    //finalJSONObj.put("tanggal_jam_selesai", dateTime);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SharedPreferences.Editor editor = sharedPref.edit();
                //editor.putString("current_service_json", String.valueOf(finalJSONObj));
                editor.putString(service.getNoLPS(), String.valueOf(finalJSONObj));
                editor.apply();
            } else {
                Log.d("finalJSON", "THERE IS FINAL JSON EXIST!");
                //assign the JSON
                try {
                    //finalJSONObj = new JSONObject(sharedPref.getString("current_service_json", "empty"));
                    finalJSONObj = new JSONObject(sharedPref.getString(service.getNoLPS(), "empty"));

                    //put the input data (if any) into the form
                    try {
                        kerusakan_input.setText(finalJSONObj.getString("kerusakan"));
                        keterangan_input.setText(finalJSONObj.getString("keterangan"));
                        nik_pic_input.setText(finalJSONObj.getString("nik_pic"));
                        no_pic_input.setText(finalJSONObj.getString("no_pic"));
                        date_time.setText(finalJSONObj.getString("tanggal_jam_selesai"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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
                                //if(machineStatusOld.getInt("machine_id") == machineStatus.getInt("machine_id")) {
                                if(machineStatusOld.getInt("temp_service_id") == machineStatus.getInt("temp_service_id")) {
                                    Log.d("loopCountFinal", "SAME ID TEMP?");
                                    //replace the old on with the new one, instead of ADDING
                                    machineStatusArray.remove(g);
                                    machineStatusArray.put(g, machineStatus);
                                    break;
                                } else {
                                    Log.d("loopCountFinal", String.valueOf(g));
                                    Log.d("lengthArray", String.valueOf(machineStatusArray.length()));
                                    //if(g - machineStatusArray.length() == 1) {
                                        machineStatusArray.put(machineStatus);
                                    //}
                                }
                            }
                            //then put the final array back to json
                            Log.d("machineArray", Arrays.toString(new JSONArray[]{machineStatusArray}));
                            finalJSONObj.put("machine", Arrays.toString(new JSONArray[]{machineStatusArray}));
                        } else {
                            //if it HAS NOT the name
                            machineStatusArray = new JSONArray();
                            machineStatusArray.put(machineStatus);
                            finalJSONObj.put("machine", machineStatusArray);
                        }
                        Log.d("JSONArray", finalJSONObj.toString(2));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SharedPreferences.Editor editor = sharedPref.edit();
                //editor.putString("current_service_json", String.valueOf(finalJSONObj));
                editor.putString(service.getNoLPS(), String.valueOf(finalJSONObj));
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
                    //move to new fragment
                    HomeActivity act = (HomeActivity) getActivity();
                    Bundle args = new Bundle();
                    args.putLong("service_id", serviceID);
                    args.putString("machine_id", machineIDs.get(position));
                    args.putString("no_lps", cachedService.getNoLPS());
                    args.putBoolean("routine", true);
                    mesinData.clear();
                    adapter.notifyDataSetChanged();
                    Fragment newFrag = new CreateDetailFragment();
                    newFrag.setArguments(args);
                    //act.changeFragmentNoBS(newFrag);
                    act.changeFragment(newFrag);
                }
            });
        }
        else {
            Log.d("setAdapter", "The mesinData array is empty!");
        }
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

            Toast.makeText(getActivity(), "Mencetak tulisan...", Toast.LENGTH_SHORT).show();
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
