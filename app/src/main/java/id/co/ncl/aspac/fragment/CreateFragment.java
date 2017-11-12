package id.co.ncl.aspac.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import id.co.ncl.aspac.adapter.MesinLPSAdapter;
import id.co.ncl.aspac.model.Mesin;

public class CreateFragment extends Fragment {

    //@BindView(R.id.expandableLayout1) ExpandableRelativeLayout expandableLayout1;
    //@BindView(R.id.expandableLayout2) ExpandableRelativeLayout expandableLayout2;
    //@BindView(R.id.expandableButton1) Button expandableButton1;
    @BindView(R.id.cust_data) TextView cust_data;
    @BindView(R.id.engineer_name) TextView engineer_name;
    @BindView(R.id.date_time) TextView date_time;
    @BindView(R.id.daftar_mesin_list_view) ListView daftar_mesin_list_view;

    private View view;
    private MesinLPSAdapter adapter;
    private JSONObject dataKeren;
    private List<Mesin> mesinData = new ArrayList<Mesin>();

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
                Log.d("JSONContent", custJSON.getString("branch_name"));
                Log.d("JSONContent", custJSON.getString("branch_status"));
                Log.d("JSONContent", custJSON.getString("branch_address"));
                Log.d("JSONContent", custJSON.getString("office_phone_number"));
                cust_data.setText(custJSON.getString("branch_name") + "\n" + custJSON.getString("branch_status") + "\n" + custJSON.getString("branch_address") + "\n" + custJSON.getString("office_phone_number"));
                JSONObject teknisiJSON = dataKeren.getJSONObject("teknisi");
                Log.d("JSONContent", teknisiJSON.getString("username"));
                Log.d("JSONContent", teknisiJSON.getString("email"));
                Log.d("JSONContent", teknisiJSON.getString("name"));
                engineer_name.setText(teknisiJSON.getString("name"));
                JSONArray mesinsArray = dataKeren.getJSONArray("machines");
                for(int y = 0; y < mesinsArray.length(); y++) {
                    JSONObject mesinJSON = mesinsArray.getJSONObject(y);
                    Log.d("JSONContent", mesinJSON.getString("brand"));
                    Log.d("JSONContent", mesinJSON.getString("model"));
                    Log.d("JSONContent", mesinJSON.getString("serial_number"));

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

    public void expandableButton1(View view) {
        //expandableLayout1 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout1);
        //expandableLayout1.toggle(); // toggle expand and collapse
    }

    public void expandableButton2(View view) {
        //expandableLayout2 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout2);
        //expandableLayout2.toggle(); // toggle expand and collapse
    }

    private void setAdapter() {
        if(mesinData.size()>0){
            Log.d("setAdapter", "Setting up mesin adapter");

            adapter = new MesinLPSAdapter(getActivity(), mesinData, getResources());
            daftar_mesin_list_view.setAdapter(adapter);
            daftar_mesin_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Log.d("log", "mesin clicked");
                    //make a new customDialog instead now
                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                    // Get the layout inflater
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    mBuilder.setView(inflater.inflate(R.layout.dialog_mesin_service_list, null));
                    mBuilder.setNeutralButton("Tutup", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    AlertDialog dialog = mBuilder.create();
                    dialog.show();
                }
            });
        }
        else {
            Log.d("setAdapter", "The mesinData array is empty!");
        }
    }
}
