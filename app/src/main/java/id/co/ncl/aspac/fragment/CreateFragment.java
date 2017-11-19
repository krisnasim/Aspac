package id.co.ncl.aspac.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

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
import id.co.ncl.aspac.adapter.MesinLPSAdapter;
import id.co.ncl.aspac.customClass.SparepartCompletionView;
import id.co.ncl.aspac.model.Mesin;
import id.co.ncl.aspac.customClass.ListViewUtility;
import id.co.ncl.aspac.model.Sparepart;

public class CreateFragment extends Fragment {

    //@BindView(R.id.expandableLayout1) ExpandableRelativeLayout expandableLayout1;
    //@BindView(R.id.expandableLayout2) ExpandableRelativeLayout expandableLayout2;
    //@BindView(R.id.expandableButton1) Button expandableButton1;
    @BindView(R.id.cust_data) TextView cust_data;
    @BindView(R.id.engineer_name) TextView engineer_name;
    @BindView(R.id.date_time) TextView date_time;
    @BindView(R.id.daftar_mesin_list_view) ListView daftar_mesin_list_view;
    @BindView(R.id.create_form_4_card_view) CardView create_form_4_card_view;

    private View view;
    private MesinLPSAdapter adapter;
    private int listViewHeight;
    private JSONObject dataKeren;
    private List<Mesin> mesinData = new ArrayList<Mesin>();

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
                for(int y = 0; y < mesinsArray.length(); y++) {
                    JSONObject mesinJSON = mesinsArray.getJSONObject(y);
                    //Log.d("JSONContent", mesinJSON.getString("brand"));
                    //Log.d("JSONContent", mesinJSON.getString("model"));
                    //Log.d("JSONContent", mesinJSON.getString("serial_number"));

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
//                    try {
//                        args.putString("data", dataGlobalArray.getJSONObject(position).toString());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    Fragment newFrag = new CreateDetailFragment();
                    //newFrag.setArguments(args);
                    act.changeFragment(newFrag);
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
}
