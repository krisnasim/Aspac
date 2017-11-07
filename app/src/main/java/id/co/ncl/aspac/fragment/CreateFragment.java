package id.co.ncl.aspac.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.ncl.aspac.R;

public class CreateFragment extends Fragment {

    @BindView(R.id.expandableLayout1) ExpandableRelativeLayout expandableLayout1;
    //@BindView(R.id.expandableLayout2) ExpandableRelativeLayout expandableLayout2;
    @BindView(R.id.expandableButton1) Button expandableButton1;

    View view;

    @OnClick(R.id.expandableButton1)
    public void clickMe() {
        expandableLayout1.toggle();
    }

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create, container, false);
        ButterKnife.bind(this, view);
        getActivity().setTitle("Penulisan LPS");

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
}
