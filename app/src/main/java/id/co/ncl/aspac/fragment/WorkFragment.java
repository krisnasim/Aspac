package id.co.ncl.aspac.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ncl.aspac.R;
import id.co.ncl.aspac.adapter.WorkListAdapter;
import id.co.ncl.aspac.model.Work;

public class WorkFragment extends Fragment {

    @BindView(R.id.work_list_listview) ListView work_list_listview;

    private ListView lv;
    private Resources res;
    private WorkListAdapter adapter;
    private SharedPreferences sharedPref;
    private List<Work> workData = new ArrayList<Work>();

    public WorkFragment() {
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
        View view = inflater.inflate(R.layout.fragment_work, container, false);
        ButterKnife.bind(this, view);
        getActivity().setTitle("Daftar Pekerjaan");

        //create date object
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(2017, 8, 31);
        Date myDate = myCalendar.getTime();
        //myDate = myCalendar.getTime();

        for(int x = 0; x < 7; x++) {
            //create new work object
            Work newWork = new Work();
            newWork.setWorkTitle("Benerin Mesin nomor "+(x+1));
            newWork.setWorkDescShort("Benerin mesin dari klien. Mesin dapat komplain tidak jalan");
            newWork.setWorkStatus("Pending");
            newWork.setWorkDateTime(myDate);

            workData.add(newWork);
        }

        setAdapter();
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
        if(workData.size()>0){
            Log.d("setAdapter", "Setting up work list adapter");

            adapter = new WorkListAdapter(getActivity(), workData, res);
            work_list_listview.setAdapter(adapter);
            work_list_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getActivity(), "Hey! You clicked on some work!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Log.d("setAdapter", "The workData array is empty!");
        }
    }
}
