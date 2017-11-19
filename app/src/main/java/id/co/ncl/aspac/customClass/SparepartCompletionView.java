package id.co.ncl.aspac.customClass;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokenautocomplete.TokenCompleteTextView;

import id.co.ncl.aspac.R;
import id.co.ncl.aspac.model.Sparepart;

/**
 * Created by jonat on 15/11/2017.
 */

public class SparepartCompletionView extends TokenCompleteTextView<Sparepart> {
    public SparepartCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View getViewForObject(Sparepart object) {
        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = l.inflate(R.layout.sparepart_token, (ViewGroup) getParent(), false);
        TextView nameText = (TextView) view.findViewById(R.id.name);
        nameText.setText(object.getSparepartName()+" - "+object.getSparepartDesc());

        return view;
    }

    @Override
    protected Sparepart defaultObject(String completionText) {
        //simple guessing on 3rd character
//        int index = completionText.indexOf(3);
//        if (index == 3) {
//            return new Sparepart(completionText, completionText.replace(" ", ""));
//        } else {
//            return new Sparepart(completionText.substring(0, index), completionText);
//        }
        return new Sparepart(completionText, "Random item");
    }
}
