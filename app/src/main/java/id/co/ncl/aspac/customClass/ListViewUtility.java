package id.co.ncl.aspac.customClass;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by jonat on 15/11/2017.
 */

public class ListViewUtility {
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int oneHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
            oneHeight = listItem.getMeasuredHeight();
            Log.d("loopCount", String.valueOf(i));
            Log.d("measuredHeight", String.valueOf(listItem.getMeasuredHeight()));
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (totalHeight/3) + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        //params.height = oneHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}