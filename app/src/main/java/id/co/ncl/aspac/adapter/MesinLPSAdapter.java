package id.co.ncl.aspac.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ncl.aspac.R;
import id.co.ncl.aspac.model.Mesin;

/**
 * Created by Jonathan Simananda on 08/11/2017.
 */

public class MesinLPSAdapter extends BaseAdapter implements View.OnClickListener {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Mesin> mesinData;
    private Mesin tempValues;
    public Resources res;

    public MesinLPSAdapter(Activity activity, List<Mesin> data, Resources resLocal) {
        this.activity = activity;
        this.mesinData = data;
        this.res = resLocal;

        inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static class ViewHolder {
        @BindView(R.id.mesin_merk_label) TextView mesin_merk_label;
        @BindView(R.id.mesin_model_label) TextView mesin_model_label;
        @BindView(R.id.mesin_no_seri_label) TextView mesin_no_seri_label;
        @BindView(R.id.mesin_status_label) TextView mesin_status_label;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public int getCount() {
        return mesinData.size();
    }

    @Override
    public Object getItem(int i) {
        return mesinData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = view;
        ViewHolder holder;

        if(view == null) {
            rowView = inflater.inflate(R.layout.mesin_lps_row_view, viewGroup, false);
            holder = new ViewHolder(rowView);
            rowView.setTag(holder);
        }
        else {
            holder = (ViewHolder) rowView.getTag();
        }

        if(mesinData.size() <= 0) {
            holder.mesin_merk_label.setText("No Data");
        }
        else {
            tempValues = null;
            tempValues = mesinData.get(i);

            //Log.d("GetView", "setting each row with data");
            //Log.d("GetViewData", tempValues.getMesinBrand());
            //Log.d("GetViewData", String.valueOf(tempValues.getMesinModel()));
            //Log.d("GetViewData", String.valueOf(tempValues.getMesinNomorSeri()));

            holder.mesin_merk_label.setText(tempValues.getMesinBrand());
            holder.mesin_model_label.setText(tempValues.getMesinModel());
            holder.mesin_no_seri_label.setText(tempValues.getMesinNomorSeri());
            holder.mesin_status_label.setText(tempValues.getMesinStatus());
        }

        return rowView;
    }
}
