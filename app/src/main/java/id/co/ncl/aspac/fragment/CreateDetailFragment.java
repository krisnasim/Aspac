package id.co.ncl.aspac.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.ncl.aspac.R;
import id.co.ncl.aspac.activity.HomeActivity;
import id.co.ncl.aspac.customClass.SparepartFormGenerator;
import id.co.ncl.aspac.database.DatabaseManager;
import id.co.ncl.aspac.dao.MachineDao;
import id.co.ncl.aspac.dao.SparepartDao;
import id.co.ncl.aspac.model.Machine;
import id.co.ncl.aspac.model.Sparepart;


public class CreateDetailFragment extends Fragment {

    @BindView(R.id.add_sparepart_button) Button add_sparepart_button;
    @BindView(R.id.send_job_detail_button) Button send_job_detail_button;
    @BindView(R.id.sparepart_layout) LinearLayout sparepart_layout;
    @BindView(R.id.rtas_check_box) CheckBox rtas_check_box;
    @BindView(R.id.rtbs_check_box) CheckBox rtbs_check_box;
    @BindView(R.id.radioGroupDetail) RadioGroup radioGroupDetail;
    @BindView(R.id.job_status_ok_radio_btn) RadioButton job_status_ok_radio_btn;
    @BindView(R.id.job_status_bad_radio_btn) RadioButton job_status_bad_radio_btn;

    private Context ctx;
    private JSONArray jsonSpareparts = new JSONArray();
    private JSONObject machineStatus = new JSONObject();
    private JSONArray machineSpareparts = new JSONArray();
    private List<SparepartFormGenerator> sparepartForms = new ArrayList<>();
    private List<Sparepart> sparepartArray = new ArrayList<>();
    private JSONObject finalJSONObj;
    private JSONObject dataMachine;
    private int machinePosition;
    private String machineID;
    private Long serviceID;
    private boolean sparepartExists = false;
    private int rtbs_flag, rtas_flag, job_status;
    private SharedPreferences sharedPref;
    private String noLPS;
    private boolean special = false;

    private DatabaseManager dbManager;


    @OnClick(R.id.add_sparepart_button)
    public void addSparepartLayout() {
        if(sparepartExists) {
            SparepartFormGenerator form1 = new SparepartFormGenerator(getActivity(), sparepartArray);
            form1.setSparepartArray(sparepartArray);
            sparepart_layout.addView(form1);
            sparepartForms.add(form1);
        } else {
            Toast.makeText(ctx, "Tidak ada sparepart untuk mesin ini!", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.send_job_detail_button)
    public void goBackSir() {
        //get the data first
        //boolean rtas;
        //boolean rtbs;
        //boolean machineOK;

        int rtas;
        int rtbs;
        int machineOK;

        if(rtas_check_box.isChecked()) {
            //rtas = true;
            rtas = 1;
        } else {
            //rtas = false;
            rtas = 0;
        }

        if(rtbs_check_box.isChecked()) {
            //rtbs = true;
            rtbs = 1;
        } else {
            //rtbs = false;
            rtbs = 0;
        }

        int choice = radioGroupDetail.getCheckedRadioButtonId();

        if(choice == job_status_ok_radio_btn.getId()) {
            //machineOK = true;
            machineOK = 1;
        } else {
            //machineOK = false;
            machineOK = 0;
        }

        try {
            machineStatus.put("rtas_flag", rtas);
            machineStatus.put("rtbs_flag", rtbs);
            machineStatus.put("job_status", machineOK);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //check for sparepart value here
        for(SparepartFormGenerator form: sparepartForms) {
            int formID = form.getId();
            Log.d("sparepartValue", String.valueOf(form.getQtyValue()));
            Log.d("sparepartName", form.getSparepartName());
            //get id based on sparepart name
            Log.d("sparepartID", form.getSparepartID());

            JSONObject machineSparepart = new JSONObject();
            try {
                machineSparepart.put("sparepart_id", Integer.valueOf(form.getSparepartID()));
                //machineSparepart.put("sparepart_name", form.getSparepartName());
                machineSparepart.put("qty", form.getQtyValue());

                machineSpareparts.put(machineSparepart);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //move to new fragment
        HomeActivity act = (HomeActivity) getActivity();
        Bundle args = new Bundle();
        args.putString("machine_status", machineStatus.toString());
        args.putString("machine_spareparts", machineSpareparts.toString());
        args.putLong("service_id", serviceID);
        Fragment newFrag;
        if(special) {
            newFrag = new SpecialCreateFragment();
        } else {
            newFrag = new CreateFragment();
        }
        newFrag.setArguments(args);
        act.changeFragmentNoBS(newFrag);
    }

    public CreateDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null) {
            machineID = args.getString("machine_id");
            serviceID = args.getLong("service_id");
            noLPS = args.getString("no_lps");
            if(args.containsKey("special")) {
                special = true;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_detail, container, false);
        ButterKnife.bind(this, view);
        getActivity().setTitle("Laporan servis mesin");
        dbManager = DatabaseManager.getInstance();
        ctx = getActivity();

        if(getArguments().getBoolean("routine")) {
            //add_sparepart_button.setEnabled(false);
            add_sparepart_button.setVisibility(View.GONE);
        }

        Log.d("machineIDDetail", machineID);

        //get the machineID first
        MachineDao macDAO = new MachineDao(dbManager);
        Machine newMachine = macDAO.getByTempID(machineID);
        //Log.d("machineSpec", newMachine.getBrand());
        Log.d("machineSpec", newMachine.getName());
        Log.d("machineSpec", String.valueOf(newMachine.getTempServiceID()));
        //Log.d("machineSpec", newMachine.getMachineID());
        macDAO.closeConnection();
        //fill in the machine json
        try {
            machineStatus.put("temp_service_id", newMachine.getTempServiceID());
            //machineStatus.put("sales_number", 123);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //get the sparepart array first
        SparepartDao spaDAO = new SparepartDao(dbManager);
        sparepartArray = spaDAO.getAllByMachineID(machineID);
        spaDAO.closeConnection();

        if(sparepartArray.size() > 0) {
            Log.d("spareExist", "SPAREPART EXIST");
            sparepartExists = true;
        } else {
            Log.d("spareExist", "SPAREPART NOT EXIST");
            //Toast.makeText(ctx, "Tidak ada sparepart untuk mesin ini!", Toast.LENGTH_SHORT).show();
        }

        //get the final JSON
        setupFinalJSON(newMachine.getTempServiceID());

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

    private void setupFinalJSON(int machineID) {
        sharedPref = getActivity().getSharedPreferences("userCred", Context.MODE_PRIVATE);

        try {
            //finalJSONObj = new JSONObject(sharedPref.getString("current_service_json", "empty"));
            finalJSONObj = new JSONObject(sharedPref.getString(noLPS, "empty"));

            Log.d("machineIDfinalJSON", String.valueOf(machineID));

            if(finalJSONObj.has("machine")) {
                Log.d("jsonPrint", finalJSONObj.toString(2));
                JSONArray machineJSONArray = new JSONArray(finalJSONObj.getJSONArray("machine"));
                for(int x = 0; x < machineJSONArray.length(); x++) {
                    Log.d("countMachineLoop", String.valueOf(x));
                    Log.d("machineLength", String.valueOf(machineJSONArray.length()));
                    JSONObject machineJSON = machineJSONArray.getJSONObject(x);
                    Log.d("tempServiceID", String.valueOf(machineJSON.getInt("temp_service_id")));
                    //Log.d("tempServiceID", String.valueOf(machineJSON.getString("brand")));
                    //Log.d("tempServiceID", String.valueOf(machineJSON.getString("model")));
                    Log.d("loop", String.valueOf(x));
                    if(machineJSON.getInt("temp_service_id") == machineID) {
                        Log.d("tempService", "GOT IT");
                        rtbs_flag = machineJSON.getInt("rtbs_flag");
                        rtas_flag = machineJSON.getInt("rtas_flag");
                        job_status = machineJSON.getInt("job_status");

                        if(rtbs_flag == 1) {
                            rtbs_check_box.setChecked(true);
                        }
                        if(rtas_flag == 1) {
                            rtas_check_box.setChecked(true);
                        }
                        if(job_status == 1) {
                            job_status_ok_radio_btn.setChecked(true);
                        } else {
                            job_status_bad_radio_btn.setChecked(true);
                        }

                        //prepare this area to be commented
                        JSONArray sparepartJSONArray = null;
                        if(machineJSON.has("sparepart_consumed")) {
                            sparepartJSONArray = machineJSON.getJSONArray("sparepart_consumed");
                            Log.d("sparepartJSON", sparepartJSONArray.toString(2));
                        }
                        if(sparepartJSONArray != null) {
                            for(int c = 0; c < sparepartJSONArray.length(); c++) {
                                Log.d("arrayLength", String.valueOf(sparepartJSONArray.length()));
                                JSONObject sparepartJSON = sparepartJSONArray.getJSONObject(c);
                                SparepartFormGenerator form1 = new SparepartFormGenerator(getActivity(), sparepartArray);
                                form1.setSparepartArray(sparepartArray);
                                //set the spinner to selected to exact name, and also add qty!
                                form1.setSparepartPickerSelected(sparepartJSON.getInt("sparepart_id"));
                                form1.setQtyValue(sparepartJSON.getInt("qty"));
                                sparepart_layout.addView(form1);
                                sparepartForms.add(form1);
                            }
                        }
                        //break;
                    }
                }
            } else if(finalJSONObj.has("rtbs_flag")){
                rtbs_flag = finalJSONObj.getInt("rtbs_flag");
                rtas_flag = finalJSONObj.getInt("rtas_flag");
                job_status = finalJSONObj.getInt("job_status");

                if(rtbs_flag == 1) {
                    rtbs_check_box.setChecked(true);
                }
                if(rtas_flag == 1) {
                    rtas_check_box.setChecked(true);
                }
                if(job_status == 1) {
                    job_status_ok_radio_btn.setChecked(true);
                } else {
                    job_status_bad_radio_btn.setChecked(true);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
