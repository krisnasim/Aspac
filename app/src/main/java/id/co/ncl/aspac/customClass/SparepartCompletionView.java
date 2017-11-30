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
import id.co.ncl.aspac.model.Spare_Part;

/**
 * Created by jonat on 15/11/2017.
 */

public class SparepartCompletionView extends TokenCompleteTextView<Spare_Part> {
    public SparepartCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View getViewForObject(Spare_Part object) {
        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = l.inflate(R.layout.sparepart_token, (ViewGroup) getParent(), false);
        TextView nameText = (TextView) view.findViewById(R.id.name);
        nameText.setText(object.getSparepartName()+" - "+object.getSparepartDesc());

        return view;
    }

    @Override
    protected Spare_Part defaultObject(String completionText) {
        //simple guessing on 3rd character
//        int index = completionText.indexOf(3);
//        if (index == 3) {
//            return new Spare_Part(completionText, completionText.replace(" ", ""));
//        } else {
//            return new Spare_Part(completionText.substring(0, index), completionText);
//        }
        return new Spare_Part(completionText, "Random item");
    }
}
