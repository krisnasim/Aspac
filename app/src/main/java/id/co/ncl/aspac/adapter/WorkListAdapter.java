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

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import id.co.ncl.aspac.R;
import id.co.ncl.aspac.model.Work;

/**
 * Created by Jonathan Simananda on 18/08/2017.
 */

public class WorkListAdapter extends BaseAdapter implements View.OnClickListener {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Work> workData;
    private Work tempValues;
    public Resources res;

    public WorkListAdapter(Activity activity, List<Work> data, Resources resLocal) {
        this.activity = activity;
        this.workData = data;
        this.res = resLocal;

        inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static class ViewHolder {
        private TextView workTitle;
        private TextView workShortDesc;
        private TextView workStatus;
        private TextView workDateTime;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public int getCount() {
        return workData.size();
    }

    @Override
    public Object getItem(int position) {
        return workData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder holder;

        if(convertView == null) {
            rowView = inflater.inflate(R.layout.row_work_fragment, parent, false);

            Log.d("GetView", "Finding all the right element after inflating custom layout");

            holder = new ViewHolder();
            holder.workTitle = (TextView) rowView.findViewById(R.id.work_title);
            holder.workShortDesc = (TextView) rowView.findViewById(R.id.work_short_desc);
            //holder.workStatus = (TextView) rowView.findViewById(R.id.work_status);
            //holder.workDateTime = (TextView) rowView.findViewById(R.id.work_datetime);

            //getting ride data for each row
            Work work = workData.get(position);
            rowView.setTag(holder);
        }
        else {
            holder = (ViewHolder) rowView.getTag();
        }

        if(workData.size() <= 0) {
            holder.workTitle.setText("No Data");
        }
        else {
            tempValues = null;
            tempValues = workData.get(position);

            //Log.d("GetView", "setting each row with data");
            //Log.d("GetViewData", tempValues.getWorkTitle());
            //Log.d("GetViewData", tempValues.getWorkDescShort());
            //Log.d("GetViewData", String.valueOf(tempValues.getWorkDateTime()));

            //Format formatter = new SimpleDateFormat("yyyy-MM-dd");
            //Format formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy");
            //Date date = tempValues.getWorkDateTime();
            //String datetoString = formatter.format(date);

            holder.workTitle.setText(tempValues.getWorkTitle());
            holder.workShortDesc.setText(tempValues.getWorkDescShort());
            //holder.workStatus.setText(tempValues.getWorkStatus());
            //holder.workDateTime.setText(datetoString);
        }

        return rowView;
    }
}
