package id.co.ncl.aspac.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.ncl.aspac.R;
import id.co.ncl.aspac.activity.HomeActivity;
import id.co.ncl.aspac.customClass.SparepartCompletionView;
import id.co.ncl.aspac.model.Sparepart;


public class CreateDetailFragment extends Fragment {

    @BindView(R.id.send_job_detail_button) Button send_job_detail_button;
    @BindView(R.id.input_part_select_demo) SparepartCompletionView completionView;

    private Sparepart[] parts;
    private ArrayAdapter<Sparepart> adapterSpare;

    @OnClick(R.id.send_job_detail_button)
    public void goBackSir() {
        //move to new fragment
        HomeActivity act = (HomeActivity) getActivity();
        Bundle args = new Bundle();

        act.goBackFragment();
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
        completionView.setAdapter(adapterSpare);

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
