package id.co.ncl.aspac.fragment;

import android.content.Context;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.ncl.aspac.R;
import id.co.ncl.aspac.activity.HomeActivity;
import id.co.ncl.aspac.customClass.SparepartFormGenerator;


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
    private JSONObject previousDataKeren;
    private JSONObject dataMachine;
    private int machinePosition;

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

        SparepartFormGenerator form1 = new SparepartFormGenerator(getActivity());
        sparepart_layout.addView(form1);

//        TextView label = new TextView(ctx);
//        label.setText("Jumlah suara calon "+(x+1));
//        label.setTextSize(14);

//        int spinnerID = View.generateViewId();
//        Spinner spinnerSpr = new Spinner(ctx);
//        spinnerSpr.setId(spinnerID);
//        int spinnerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, getResources().getDisplayMetrics());
//        int spinnerHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
//        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.spinner_type_spr));
//        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
//        spinnerSpr.setAdapter(spinnerArrayAdapter);
//
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(spinnerWidth, spinnerHeight);
//        int startMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
//        lp.setMargins(startMargin, 17, 0, 0);
//        spinnerSpr.setLayoutParams(lp);
//        sparepart_layout.addView(spinnerSpr);
//
//        int qtyID = View.generateViewId();
//        int addbtnID = View.generateViewId();
//        Button addBtn = new Button(ctx);
//        addBtn.setId(addbtnID);
//        addBtn.setText("+");

//        addBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int value = Integer.parseInt(sparepart_qty.getText().toString());
//                value += 1;
//                sparepart_qty.setText(String.valueOf(value));
//            }
//        });

//        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
//        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(width, width);
//        int startMargin2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
//        lp2.setMargins(startMargin2, 17, 0, 0);
//        lp2.addRule(RelativeLayout.RIGHT_OF, spinnerID);
//        addBtn.setLayoutParams(lp2);
//        sparepart_layout.addView(addBtn);
//
//        TextView qtyValue = new TextView(ctx);
//        qtyValue.setTextSize(26);
//        qtyValue.setId(qtyID);
//        qtyValue.setText("0");
//
//        RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(width, width);
//        int startMargin3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, getResources().getDisplayMetrics());
//        lp3.setMargins(startMargin3, 17, 0, 0);
//        lp3.addRule(RelativeLayout.RIGHT_OF, addbtnID);
//        qtyValue.setLayoutParams(lp3);
//        sparepart_layout.addView(qtyValue);
//
//        int minbtnID = View.generateViewId();
//        Button minBtn = new Button(ctx);
//        minBtn.setId(minbtnID);
//        minBtn.setText("-");
//        minBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//        RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(width, width);
//        int startMargin4 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
//        lp4.setMargins(startMargin4, 17, 0, 0);
//        lp4.addRule(RelativeLayout.RIGHT_OF, qtyID);
//        minBtn.setLayoutParams(lp4);
//        sparepart_layout.addView(minBtn);
    }

    @OnClick(R.id.send_job_detail_button)
    public void goBackSir() {

//        if(dataKeren.length() != 0) {
//            try {
//                Log.d("JSONContent", dataKeren.getString("serial_number"));
//
//                JSONObject jsonMesin = new JSONObject();
//                jsonMesin.put("machine_id", 10);
//                jsonMesin.put("serial_number", dataKeren.getString("serial_number"));
//                jsonMesin.put("rtbs_flag", 10);
//                jsonMesin.put("rtas_flag", 10);
//                jsonMesin.put("job_status", 1);
//                jsonMesin.put("garansi_mesin", 123456);
//                jsonMesin.put("sparepart_consumed", jsonSpareparts);
//                jsonMachines.put(jsonMesin);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }

        //get the data first
        boolean rtas;
        boolean rtbs;
        boolean machineOK;

        if(rtas_check_box.isChecked()) {
            rtas = true;
        } else {
            rtas = false;
        }

        if(rtbs_check_box.isChecked()) {
            rtbs = true;
        } else {
            rtbs = false;
        }

        int choice = radioGroupDetail.getCheckedRadioButtonId();

        if(choice == job_status_ok_radio_btn.getId()) {
            machineOK = true;
        } else {
            machineOK = false;
        }

        try {
            machineStatus.put("rtas_status", rtas);
            machineStatus.put("rtbs_status", rtbs);
            machineStatus.put("machine_ok", machineOK);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //set the sparepart array
        JSONObject jsonSparepart = new JSONObject();
        try {
            jsonSparepart.put("sparepart_id", "13");
            jsonSpareparts.put(jsonSparepart);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //add machinestatus JSONObj and sparepart JSONArray to previousDataKeren
        try {
            JSONArray mesinsArray = previousDataKeren.getJSONArray("machines");
            JSONObject mesin = mesinsArray.getJSONObject(machinePosition);
            mesin.put("machine_status", machineStatus);
            mesin.put("machine_spareparts", jsonSpareparts);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //move to new fragment
        HomeActivity act = (HomeActivity) getActivity();
        Bundle args = new Bundle();
        args.putString("data", previousDataKeren.toString());
        //args.putString("dataKeren", dataKeren.toString());
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
            try {
                dataMachine = new JSONObject(args.getString("data"));
                previousDataKeren = new JSONObject(args.getString("dataKeren"));
            } catch (JSONException e) {
                e.printStackTrace();
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
        ctx = getActivity();

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

        if(previousDataKeren.length() != 0) {
            Log.d("JSONContent", "Starting new array of values");
            try {
                Log.d("JSONContent", previousDataKeren.getString("date_service"));

                JSONObject custJSON = previousDataKeren.getJSONObject("customer_branch");
                Log.d("JSONContent", custJSON.getString("branch_name"));
                Log.d("JSONContent", custJSON.getString("branch_status"));
                Log.d("JSONContent", custJSON.getString("branch_address"));
                Log.d("JSONContent", custJSON.getString("office_phone_number"));

                JSONObject teknisiJSON = previousDataKeren.getJSONObject("teknisi");
                Log.d("JSONContent", teknisiJSON.getString("username"));
                Log.d("JSONContent", teknisiJSON.getString("email"));
                Log.d("JSONContent", teknisiJSON.getString("name"));

                JSONArray mesinsArray = previousDataKeren.getJSONArray("machines");

                for(int y = 0; y < mesinsArray.length(); y++) {
                    JSONObject mesinJSON = mesinsArray.getJSONObject(y);
                    Log.d("JSONContent", mesinJSON.getString("brand"));
                    Log.d("JSONContent", mesinJSON.getString("model"));
                    Log.d("JSONContent", mesinJSON.getString("serial_number"));

                    if(mesinJSON.getString("brand").equals(dataMachine.getString("brand"))) {
                        if(mesinJSON.getString("model").equals(dataMachine.getString("model"))) {
                            if(mesinJSON.getString("serial_number").equals(dataMachine.getString("serial_number"))) {
                                //same machine picked. remember the position
                                machinePosition = y;

                                if(mesinJSON.has("machine_status")) {
                                    JSONObject machineStats = mesinJSON.getJSONObject("machine_status");
                                    Log.d("JSONContent", machineStats.getString("rtas_status"));
                                    Log.d("JSONContent", machineStats.getString("rtbs_status"));
                                    Log.d("JSONContent", machineStats.getString("machine_ok"));

                                    if(machineStats.getString("rtas_status").equals("true")) {
                                        rtas_check_box.setChecked(true);
                                    }

                                    if(machineStats.getString("rtbs_status").equals("true")) {
                                        rtbs_check_box.setChecked(true);
                                    }

                                    if(machineStats.getString("machine_ok").equals("true")) {
                                        job_status_ok_radio_btn.setChecked(true);
                                    } else {
                                        job_status_bad_radio_btn.setChecked(true);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

//        if(dataMachine.length() != 0) {
//            Log.d("JSONContent", "Machine data");
//            try {
//                Log.d("JSONContent", dataMachine.getString("brand"));
//                Log.d("JSONContent", dataMachine.getString("model"));
//                Log.d("JSONContent", dataMachine.getString("serial_number"));
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
}
