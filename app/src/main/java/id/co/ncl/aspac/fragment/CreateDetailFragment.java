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
import id.co.ncl.aspac.database.MachineDao;
import id.co.ncl.aspac.database.SparepartDao;
import id.co.ncl.aspac.model.Machine;
import id.co.ncl.aspac.model.Sparepart;


public class CreateDetailFragment extends Fragment {

    //@BindView(R.id.add_number_btn) Button add_number_btn;
    //@BindView(R.id.min_number_btn) Button min_number_btn;
    @BindView(R.id.add_sparepart_button) Button add_sparepart_button;
    //@BindView(R.id.sparepart_qty) TextView sparepart_qty;
    @BindView(R.id.send_job_detail_button) Button send_job_detail_button;
    //@BindView(R.id.input_part_select_demo) SparepartCompletionView completionView;
    @BindView(R.id.sparepart_layout) LinearLayout sparepart_layout;
    @BindView(R.id.rtas_check_box) CheckBox rtas_check_box;
    @BindView(R.id.rtbs_check_box) CheckBox rtbs_check_box;
    @BindView(R.id.radioGroupDetail) RadioGroup radioGroupDetail;
    @BindView(R.id.job_status_ok_radio_btn) RadioButton job_status_ok_radio_btn;
    @BindView(R.id.job_status_bad_radio_btn) RadioButton job_status_bad_radio_btn;

    //private Spare_Part[] parts;
    //private LinearLayout layout;
    private Context ctx;
    //private ArrayAdapter<Spare_Part> adapterSpare;
    private JSONArray jsonSpareparts = new JSONArray();
    //private JSONArray jsonMachines = new JSONArray();
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

    private DatabaseManager dbManager;

//    @OnClick(R.id.add_number_btn)
//    public void addNumber() {
//        int value = Integer.parseInt(sparepart_qty.getText().toString());
//        value += 1;
//        sparepart_qty.setText(String.valueOf(value));
//    }
//
//    @OnClick(R.id.min_number_btn)
//    public void minNumber() {
//        int value = Integer.parseInt(sparepart_qty.getText().toString());
//        value -= 1;
//        if(value < 0) {
//            sparepart_qty.setText("0");
//        } else {
//            sparepart_qty.setText(String.valueOf(value));
//        }
//    }

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
        Fragment newFrag = new CreateFragment();
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
            //get the arguments here
//            try {
//                dataMachine = new JSONObject(args.getString("data"));
//                previousDataKeren = new JSONObject(args.getString("dataKeren"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            machineID = args.getString("machine_id");
            serviceID = args.getLong("service_id");
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

        Log.d("machineIDDetail", machineID);

        //get the machineID first
        MachineDao macDAO = new MachineDao(dbManager);
        Machine newMachine = macDAO.get(machineID);
        Log.d("machineSpec", newMachine.getBrand());
        Log.d("machineSpec", newMachine.getModel());
        Log.d("machineSpec", String.valueOf(newMachine.getTempServiceID()));
        Log.d("machineSpec", newMachine.getMachineID());
        macDAO.closeConnection();
        //fill in the machine json
        try {
//            machineStatus.put("machine_id", Integer.valueOf(newMachine.getMachineID()));
//            machineStatus.put("serial_number", newMachine.getSerialNumber());
//            machineStatus.put("sales_number", newMachine.getSalesNumber());
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
//            for(int a = 0; a < sparepartArray.size(); a++) {
//                Sparepart sparepart = sparepartArray.get(a);
//                Log.d("SparepartCont", sparepart.getSparepartID());
//                Log.d("SparepartCont", sparepart.getCode());
//                Log.d("SparepartCont", sparepart.getName());
//            }
        } else {
            Log.d("spareExist", "SPAREPART NOT EXIST");
            //Toast.makeText(ctx, "Tidak ada sparepart untuk mesin ini!", Toast.LENGTH_SHORT).show();
        }

        //get the final JSON
        setupFinalJSON(newMachine.getTempServiceID());

//        parts = new Spare_Part[]{
//                new Spare_Part("AASDC23", "Head counter part"),
//                new Spare_Part("W3CAASD", "Windle cash counter"),
//                new Spare_Part("AB78XYY", "Stopgap brake"),
//                new Spare_Part("LLOP888", "Machine bracket"),
//                new Spare_Part("M0N87YD", "Outer shell"),
//                new Spare_Part("112UUIY", "Grease")
//        };

        //adapterSpare = new ArrayAdapter<Spare_Part>(getActivity(), android.R.layout.simple_list_item_1, parts);
        //completionView = (SparepartCompletionView) getActivity().findViewById(R.id.input_part_select_demo);
        //completionView.setAdapter(adapterSpare);

        //sparepart_qty.setText("1");

//        if(previousDataKeren.length() != 0) {
//            Log.d("JSONContent", "Starting new array of values");
//            try {
//                Log.d("JSONContent", previousDataKeren.getString("date_service"));
//
//                JSONObject custJSON = previousDataKeren.getJSONObject("customer_branch");
//                Log.d("JSONContent", custJSON.getString("branch_name"));
//                Log.d("JSONContent", custJSON.getString("branch_status"));
//                Log.d("JSONContent", custJSON.getString("branch_address"));
//                Log.d("JSONContent", custJSON.getString("office_phone_number"));
//
//                JSONObject teknisiJSON = previousDataKeren.getJSONObject("teknisi");
//                Log.d("JSONContent", teknisiJSON.getString("username"));
//                Log.d("JSONContent", teknisiJSON.getString("email"));
//                Log.d("JSONContent", teknisiJSON.getString("name"));
//
//                JSONArray mesinsArray = previousDataKeren.getJSONArray("machines");
//
//                for(int y = 0; y < mesinsArray.length(); y++) {
//                    JSONObject mesinJSON = mesinsArray.getJSONObject(y);
//                    Log.d("JSONContent", mesinJSON.getString("brand"));
//                    Log.d("JSONContent", mesinJSON.getString("model"));
//                    Log.d("JSONContent", mesinJSON.getString("serial_number"));
//
//                    if(mesinJSON.getString("brand").equals(dataMachine.getString("brand"))) {
//                        if(mesinJSON.getString("model").equals(dataMachine.getString("model"))) {
//                            if(mesinJSON.getString("serial_number").equals(dataMachine.getString("serial_number"))) {
//                                //same machine picked. remember the position
//                                machinePosition = y;
//
//                                if(mesinJSON.has("machine_status")) {
//                                    JSONObject machineStats = mesinJSON.getJSONObject("machine_status");
//                                    Log.d("JSONContent", machineStats.getString("rtas_status"));
//                                    Log.d("JSONContent", machineStats.getString("rtbs_status"));
//                                    Log.d("JSONContent", machineStats.getString("machine_ok"));
//
//                                    if(machineStats.getString("rtas_status").equals("true")) {
//                                        rtas_check_box.setChecked(true);
//                                    }
//
//                                    if(machineStats.getString("rtbs_status").equals("true")) {
//                                        rtbs_check_box.setChecked(true);
//                                    }
//
//                                    if(machineStats.getString("machine_ok").equals("true")) {
//                                        job_status_ok_radio_btn.setChecked(true);
//                                    } else {
//                                        job_status_bad_radio_btn.setChecked(true);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }

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
            finalJSONObj = new JSONObject(sharedPref.getString("current_service_json", "empty"));

            Log.d("machineIDfinalJSON", String.valueOf(machineID));

            if(finalJSONObj.has("machine")) {
                JSONArray machineJSONArray = finalJSONObj.getJSONArray("machine");
                for(int x = 0; x < machineJSONArray.length(); x++) {
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
                        JSONArray sparepartJSONArray = null;
                        if(machineJSON.has("sparepart_consumed")) {
                            sparepartJSONArray = machineJSON.getJSONArray("sparepart_consumed");
                        }
                        if(sparepartJSONArray != null) {
                            for(int c = 0; c < sparepartJSONArray.length(); c++) {
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
