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
import android.net.Uri;
import android.os.Bundle;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
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
import id.co.ncl.aspac.activity.SignatureActivity;
import id.co.ncl.aspac.adapter.MesinLPSAdapter;
import id.co.ncl.aspac.customClass.ListViewUtility;
import id.co.ncl.aspac.customClass.VolleyMultipartRequest;
import id.co.ncl.aspac.customClass.VolleySingleton;
import id.co.ncl.aspac.dao.MachineDao;
import id.co.ncl.aspac.dao.ServiceDao;
import id.co.ncl.aspac.database.DatabaseManager;
import id.co.ncl.aspac.model.Machine;
import id.co.ncl.aspac.model.Mesin;
import id.co.ncl.aspac.model.Service;

public class SpecialCreateFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject>, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @BindView(R.id.cust_data_sp)
    TextView cust_data_sp;
    @BindView(R.id.date_time_sp) TextView date_time_sp;
    @BindView(R.id.daftar_mesin_list_view_sp)
    ListView daftar_mesin_list_view_sp;
    @BindView(R.id.create_form_4_card_view_sp)
    CardView create_form_4_card_view_sp;
    @BindView(R.id.create_form_button_sp)
    Button create_form_button_sp;
    @BindView(R.id.kerusakan_input_sp)
    EditText kerusakan_input_sp;
    //@BindView(R.id.perbaikan_input) EditText perbaikan_input;
    @BindView(R.id.keterangan_input_sp) EditText keterangan_input_sp;
    @BindView(R.id.nik_pic_input_sp) EditText nik_pic_input_sp;
    @BindView(R.id.no_pic_input_sp) EditText no_pic_input_sp;
    @BindView(R.id.signature_preview_img_sp)
    ImageView signature_preview_img_sp;

    private View view;
    private MesinLPSAdapter adapter;
    private int listViewHeight;
    private JSONArray dataMachineArray;
    private JSONObject dataKeren;
    private JSONArray machineSpareparts;
    private JSONArray machineStatusArray;
    private JSONObject machineStatus;
    private JSONObject finalJSONObj;
    private String serviceLPS;
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

    @OnClick(R.id.create_form_button_sp)
    public void sendForm() {
        //set the url
        //String url = "http://103.26.208.118/api/submitdatalps";
        String url = "http://103.26.208.118/api/postRepairLPS";

        create_form_button_sp.setEnabled(false);

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
            SharedPreferences userSharedPref = getActivity().getSharedPreferences("userCred", Context.MODE_PRIVATE);
            sharedPref = getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE);
            token = "Bearer "+userSharedPref.getString("token", "empty token");
            try {
                //finalJSONObj = new JSONObject(sharedPref.getString("current_service_json", "empty"));
                finalJSONObj = new JSONObject(sharedPref.getString(cachedService.getNoLPS(), "empty"));
                //finalJSONObj.put("no_lps", cachedService.getNoLPS());
                //finalJSONObj.put("teknisi_id", 121);
                //finalJSONObj.put("name_pic", sharedPref.getString("name", "noName"));
                finalJSONObj.put("kerusakan", kerusakan_input_sp.getText().toString());
                //finalJSONObj.put("perbaikan", perbaikan_input.getText().toString());
                finalJSONObj.put("keterangan", keterangan_input_sp.getText().toString());
                finalJSONObj.put("nik_pic", nik_pic_input_sp.getText().toString());
                finalJSONObj.put("no_pic", no_pic_input_sp.getText().toString());
                //finalJSONObj.put("date_lps", date);
                finalJSONObj.put("tanggal_jam_selesai", dateTime);

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

        VolleyMultipartRequest newReq = new VolleyMultipartRequest(url, headers, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    Log.d("onResponse", "DAMN YOU DID IT! HECK YEAH");
                    Log.d("onResponse", response);
                    Toast.makeText(getActivity(), "Data berhasil disimpan!", Toast.LENGTH_SHORT).show();

                    //delete the sharedpref
                    SharedPreferences preferences = getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE);
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
                Map<String, String> params;
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

//                Log.d("params", params.get("kerusakan"));
//                //Log.d("params", params.get("perbaikan"));
//                Log.d("params", params.get("keterangan"));
//                Log.d("params", params.get("nik_pic"));
//                Log.d("params", params.get("no_pic"));
//                //Log.d("params", params.get("date_lps"));
//                Log.d("params", String.valueOf(params.get("rtbs_flag")));
//                Log.d("params", String.valueOf(params.get("rtas_flag")));
//                Log.d("params", String.valueOf(params.get("job_status")));
//                Log.d("params", String.valueOf(params.get("repair_service_id")));
//                Log.d("params", params.get("name_pic"));
//                Log.d("params", params.get("sparepart_consumed"));

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

    @OnClick(R.id.date_time_sp)
    public void setDateTime() {
        finalCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, finalCalendar.get(Calendar.YEAR), finalCalendar.get(Calendar.MONTH), finalCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @OnClick(R.id.clear_signature_button_sp)
    public void clearSignature() {
        //prepare the filled in data first
        sharedPref = getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE);
        try {
            finalJSONObj = new JSONObject(sharedPref.getString(cachedService.getNoLPS(), "empty"));
            //finalJSONObj.put("no_lps", cachedService.getNoLPS());
            finalJSONObj.put("repair_service_id", String.valueOf(cachedService.getRepairdID()));
            //finalJSONObj.put("teknisi_id", 121);
            finalJSONObj.put("kerusakan", kerusakan_input_sp.getText().toString());
            //finalJSONObj.put("perbaikan", perbaikan_input.getText().toString());
            finalJSONObj.put("keterangan", keterangan_input_sp.getText().toString());
            finalJSONObj.put("nik_pic", nik_pic_input_sp.getText().toString());
            finalJSONObj.put("no_pic", no_pic_input_sp.getText().toString());
            //finalJSONObj.put("date_lps", date);
            //finalJSONObj.put("tanggal_jam_selesai", date_time_sp.getText().toString());

            SharedPreferences.Editor editor = sharedPref.edit();
            //editor.putString("current_service_json", String.valueOf(finalJSONObj));
            editor.putString(cachedService.getNoLPS(), String.valueOf(finalJSONObj));
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(getActivity(), SignatureActivity.class);
        intent.putExtra("service_id", serviceLPS);
        intent.putExtra("special", true);
        startActivity(intent);
    }

    public SpecialCreateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null) {
            serviceLPS = args.getString("service_id");
            Log.d("receivedSerID", String.valueOf(serviceLPS));

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
        view = inflater.inflate(R.layout.fragment_special_create, container, false);
        ButterKnife.bind(this, view);
        getActivity().setTitle("Penulisan LPS");
        dbManager = DatabaseManager.getInstance();
        //setupSignature();
        if(signedBitmap != null) {
            signature_preview_img_sp.setImageBitmap(signedBitmap);
        }

        if(!serviceLPS.isEmpty()) {
            ServiceDao serDAO = new ServiceDao(dbManager);
            Service service = serDAO.get(serviceLPS);
            cachedService = service;
            setupFinalJSON(service);
            //date_time.setText(service.getDateService());
            //cust_data.setText(service.getName() + "\n" + service.getStatus() + "\n" + service.getAddress() + "\n" + service.getOfficePhoneNumber());

            Log.d("logging", "" + service.getName());
            Log.d("logging", "" + service.getRepairdID());
            Log.d("logging", "" + service.getAddress());
            Log.d("logging", "" + service.getOfficePhoneNumber());
            Log.d("logging", String.valueOf(service.getBranchID()));

            cust_data_sp.setText(service.getCustomerName() + "\n" + service.getAddress() + "\n" + service.getOfficePhoneNumber());
            //kerusakan_input.setText(service.);

            serDAO.closeConnection();

            MachineDao macDAO = new MachineDao(dbManager);
            mesinArray = macDAO.getAllByNoLPS(serviceLPS);
            Log.d("mesinarrayCount", "mesin array count is: "+mesinArray.size());
            macDAO.closeConnection();

            for(int h = 0; h < mesinArray.size(); h++) {
                Machine mac = mesinArray.get(h);
                //Log.d("mesin", mac.getBrand());
                Log.d("mesin", mac.getName());
                Log.d("mesin", String.valueOf(mac.getMachineID()));
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
                machineIDs.add(mac.getMachineID());
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
        date_time_sp.setText(format.format(finalCalendar.getTime()));
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
        String response = null;
        try {
            response = new String(error.networkResponse.data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d("errorResponse", response);
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialog.dismiss();
        try {
            //Log.d("onResponse", "DAMN YOU DID IT! HECK YEAH");
            Log.d("onResponse", response.toString(2));
            Toast.makeText(getActivity(), "Data berhasil disimpan!", Toast.LENGTH_SHORT).show();

            //delete the sharedpref
            SharedPreferences preferences = getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE);
            //preferences.edit().remove("current_service_json").apply();
            preferences.edit().remove(cachedService.getNoLPS()).apply();

            //delete the sent service
            ServiceDao serDAO = new ServiceDao(dbManager);
            MachineDao macDAO = new MachineDao(dbManager);
            //delete the machines first
            macDAO.deleteByID(cachedService.getId());
            //then delete the service
            serDAO.delete(cachedService);
            serDAO.closeConnection();

        } catch (Exception e) {
            e.printStackTrace();
        }

        //move to new fragment
        HomeActivity act = (HomeActivity) getActivity();
        Fragment newFrag = new SpecialWorkFragment();
        act.changeFragmentNoBS(newFrag);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                if(resultCode == Activity.RESULT_OK) {
                    //pairBluetoothDevice();
                }
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

//        try {
//            //disconnectBT();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

//        try {
//            disconnectBT();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
            sharedPref = getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE);

            //check if there are any previous json
            //if(sharedPref.getString("current_service_json", "empty").equals("empty")) {
            if(sharedPref.getString(service.getNoLPS(), "empty").equals("empty")) {
                Log.d("finalJSON", "NO FINAL JSON EXIST!");
                //create the new JSOn
                finalJSONObj = new JSONObject();
//                try {
//                    //finalJSONObj.put("no_lps", service.getNoLPS());
//                    //finalJSONObj.put("type_lps", service.getTypeService());
//                    //finalJSONObj.put("customer_id", String.valueOf(service.getCustomerID()));
//                    //finalJSONObj.put("customer_branch_id", String.valueOf(service.getCBID()));
//                    //finalJSONObj.put("teknisi_id", "111");
//                    //finalJSONObj.put("date_lps", date);
//                    //finalJSONObj.put("tanggal_jam_selesai", dateTime);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

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
                        //put the input data (if any) into the form
                        try {
                            if(finalJSONObj.has("kerusakan")) {
                                kerusakan_input_sp.setText(finalJSONObj.getString("kerusakan"));
                            } if(finalJSONObj.has("keterangan")) {
                                keterangan_input_sp.setText(finalJSONObj.getString("keterangan"));
                            } if(finalJSONObj.has("nik_pic")) {
                                nik_pic_input_sp.setText(finalJSONObj.getString("nik_pic"));
                            } if(finalJSONObj.has("no_pic")) {
                                no_pic_input_sp.setText(finalJSONObj.getString("no_pic"));
                            } if(finalJSONObj.has("tanggal_jam_selesai")) {
                                date_time_sp.setText(finalJSONObj.getString("tanggal_jam_selesai"));
                            }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(checkForSparepartData()) {
                        //put inner object first
//                        if(machineSpareparts.length() > 0) {
//                            machineStatus.put("sparepart_consumed", machineSpareparts);
//                        }
                        //and then, put the object into json
//                        if(finalJSONObj.has("machine")) {
//                            //if it HAS the name
//                            machineStatusArray = finalJSONObj.getJSONArray("machine");
//                            //check if we add NEW machine, or REVISE old machine data
//                            for(int g = 0; g < machineStatusArray.length(); g++) {
//                                JSONObject machineStatusOld = machineStatusArray.getJSONObject(g);
//                                //if(machineStatusOld.getInt("machine_id") == machineStatus.getInt("machine_id")) {
//                                if(machineStatusOld.getInt("temp_service_id") == machineStatus.getInt("temp_service_id")) {
//                                    Log.d("loopCountFinal", "SAME ID TEMP?");
//                                    //replace the old on with the new one, instead of ADDING
//                                    machineStatusArray.remove(g);
//                                    machineStatusArray.put(g, machineStatus);
//                                    break;
//                                } else {
//                                    Log.d("loopCountFinal", String.valueOf(g));
//                                    Log.d("lengthArray", String.valueOf(machineStatusArray.length()));
//                                    //if(g - machineStatusArray.length() == 1) {
//                                    machineStatusArray.put(machineStatus);
//                                    //}
//                                }
//                            }
//                            //then put the final array back to json
//                            Log.d("machineArray", Arrays.toString(new JSONArray[]{machineStatusArray}));
//                            finalJSONObj.put("machine", Arrays.toString(new JSONArray[]{machineStatusArray}));
//                        } else {
//                            //if it HAS NOT the name
//                            machineStatusArray = new JSONArray();
//                            machineStatusArray.put(machineStatus);
//                            finalJSONObj.put("machine", machineStatusArray);
//                        }
                        finalJSONObj.put("rtbs_flag", String.valueOf(machineStatus.get("rtbs_flag")));
                        finalJSONObj.put("rtas_flag", String.valueOf(machineStatus.get("rtas_flag")));
                        finalJSONObj.put("job_status", String.valueOf(machineStatus.get("job_status")));
                        String stringJSON = machineSpareparts.toString();
                        JsonParser jsonParser = new JsonParser();
                        JsonArray objectFromString = jsonParser.parse(stringJSON).getAsJsonArray();
                        Log.d("printGSON", objectFromString.toString());
                        String convertedString = objectFromString.toString();
                        //String testJSON = machineSpareparts.toString();
                        finalJSONObj.put("sparepart_consumed", convertedString);

                        Log.d("JSONArray", finalJSONObj.toString(2));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SharedPreferences.Editor editor = sharedPref.edit();
                //editor.putString("current_service_json", String.valueOf(finalJSONObj));
                editor.putString(service.getNoLPS(), String.valueOf(finalJSONObj));
                editor.apply();
            } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setAdapter() {
        if(mesinData.size()>0){
            Log.d("setAdapter", "Setting up mesin adapter");

            adapter = new MesinLPSAdapter(getActivity(), mesinData, getResources());
            daftar_mesin_list_view_sp.setAdapter(adapter);

            daftar_mesin_list_view_sp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //move to new fragment
                    HomeActivity act = (HomeActivity) getActivity();
                    Bundle args = new Bundle();
                    args.putString("service_id", serviceLPS);
                    args.putString("machine_id", machineIDs.get(position));
                    for(int x=0; x<machineIDs.size(); x++) {
                        //Log.d("machineID", "machine ID is: " + machineIDs.get(position));
                        Log.d("machineID", "machine ID is: " + machineIDs.get(x));
                    }
                    args.putString("no_lps", cachedService.getNoLPS());
                    args.putString("special", "special");
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
        ListViewUtility.setListViewHeightBasedOnChildren(daftar_mesin_list_view_sp);
    }

    private boolean checkforSharedPreferences() {
        boolean result = false;
        SharedPreferences userSharedPref = getActivity().getSharedPreferences("userCred", Context.MODE_PRIVATE);
        if(userSharedPref.contains("token")) {
            //sharedpref exist
            result = true;
        } else {
            //sharedpref does not exist. do nothing
        }
        return result;
    }
}
