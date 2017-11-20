package id.co.ncl.aspac.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import id.co.ncl.aspac.R;
import id.co.ncl.aspac.activity.HomeActivity;
import id.co.ncl.aspac.customClass.SparepartCompletionView;
import id.co.ncl.aspac.model.Sparepart;


public class CreateDetailFragment extends Fragment {

    //@BindView(R.id.add_number_btn) Button add_number_btn;
    //@BindView(R.id.min_number_btn) Button min_number_btn;
    @BindView(R.id.add_sparepart_button) Button add_sparepart_button;
    //@BindView(R.id.sparepart_qty) TextView sparepart_qty;
    @BindView(R.id.send_job_detail_button) Button send_job_detail_button;
    //@BindView(R.id.input_part_select_demo) SparepartCompletionView completionView;
    @BindView(R.id.sparepart_layout) RelativeLayout sparepart_layout;

    private Sparepart[] parts;
    private JSONObject dataKeren;
    //private LinearLayout layout;
    private Context ctx;
    private ArrayAdapter<Sparepart> adapterSpare;
    private JSONArray jsonSpareparts = new JSONArray();
    private JSONArray jsonMachines = new JSONArray();

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

//        TextView label = new TextView(ctx);
//        label.setText("Jumlah suara calon "+(x+1));
//        label.setTextSize(14);
        int spinnerID = View.generateViewId();
        Spinner spinnerSpr = new Spinner(ctx);
        spinnerSpr.setId(spinnerID);
        int spinnerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, getResources().getDisplayMetrics());
        int spinnerHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.spinner_type_spr));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        spinnerSpr.setAdapter(spinnerArrayAdapter);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(spinnerWidth, spinnerHeight);
        int startMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        lp.setMargins(startMargin, 17, 0, 0);
        spinnerSpr.setLayoutParams(lp);
        sparepart_layout.addView(spinnerSpr);

        int addbtnID = View.generateViewId();
        Button addBtn = new Button(ctx);
        addBtn.setId(addbtnID);
        addBtn.setText("+");

        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(width, width);
        int startMargin2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        lp2.setMargins(startMargin2, 17, 0, 0);
        lp2.addRule(RelativeLayout.RIGHT_OF, spinnerID);
        addBtn.setLayoutParams(lp2);
        sparepart_layout.addView(addBtn);

        int qtyID = View.generateViewId();
        TextView qtyValue = new TextView(ctx);
        qtyValue.setTextSize(26);
        qtyValue.setId(qtyID);
        qtyValue.setText("0");

        RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(width, width);
        int startMargin3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, getResources().getDisplayMetrics());
        lp3.setMargins(startMargin3, 17, 0, 0);
        lp3.addRule(RelativeLayout.RIGHT_OF, addbtnID);
        qtyValue.setLayoutParams(lp3);
        sparepart_layout.addView(qtyValue);

        int minbtnID = View.generateViewId();
        Button minBtn = new Button(ctx);
        minBtn.setId(minbtnID);
        minBtn.setText("-");

        RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(width, width);
        int startMargin4 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        lp4.setMargins(startMargin4, 17, 0, 0);
        lp4.addRule(RelativeLayout.RIGHT_OF, qtyID);
        minBtn.setLayoutParams(lp4);
        sparepart_layout.addView(minBtn);

    }

    @OnClick(R.id.send_job_detail_button)
    public void goBackSir() {

        if(dataKeren.length() != 0) {
            try {
                Log.d("JSONContent", dataKeren.getString("serial_number"));

                JSONObject jsonMesin = new JSONObject();
                jsonMesin.put("machine_id", 10);
                jsonMesin.put("serial_number", dataKeren.getString("serial_number"));
                jsonMesin.put("rtbs_flag", 10);
                jsonMesin.put("rtas_flag", 10);
                jsonMesin.put("job_status", 1);
                jsonMesin.put("garansi_mesin", 123456);
                jsonMesin.put("sparepart_consumed", jsonSpareparts);
                jsonMachines.put(jsonMesin);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //move to new fragment
//        HomeActivity act = (HomeActivity) getActivity();
//        Bundle args = new Bundle();
//
//        act.goBackFragment();
    }

    public CreateDetailFragment() {
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
        View view = inflater.inflate(R.layout.fragment_create_detail, container, false);
        ButterKnife.bind(this, view);
        ctx = getActivity();

        parts = new Sparepart[]{
                new Sparepart("AASDC23", "Head counter part"),
                new Sparepart("W3CAASD", "Windle cash counter"),
                new Sparepart("AB78XYY", "Stopgap brake"),
                new Sparepart("LLOP888", "Machine bracket"),
                new Sparepart("M0N87YD", "Outer shell"),
                new Sparepart("112UUIY", "Grease")
        };

        adapterSpare = new ArrayAdapter<Sparepart>(getActivity(), android.R.layout.simple_list_item_1, parts);
        //completionView = (SparepartCompletionView) getActivity().findViewById(R.id.input_part_select_demo);
        //completionView.setAdapter(adapterSpare);

        //sparepart_qty.setText("1");

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
