package id.co.ncl.aspac.customClass;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import id.co.ncl.aspac.R;

/**
 * Created by jonat on 21/11/2017.
 */

public class SparepartFormGenerator extends RelativeLayout {

    //all visual elements variables goes here
    private Button addButton;
    private Button minButton;
    private TextView qtyValue;
    private Spinner sparepartPicker;

    //all elements ID references goes here
    private int addButtonID;
    private int minButtonID;
    private int qtyValueID;
    private int sparepartPickerID;

    //and these are the rest of the variables
    private static int DEFAULT_WIDTH_BUTTON;
    private static int DEFAULT_HEIGHT_BUTTON;
    private static int DEFAULT_SPINNER_WIDTH;
    private static int DEFAULT_SPINNER_HEIGHT;

    public SparepartFormGenerator(Context context) {
        super(context);
        //defining pre-init before layout creation
        DEFAULT_WIDTH_BUTTON = convertIntToDP(40);
        DEFAULT_HEIGHT_BUTTON = convertIntToDP(40);
        DEFAULT_SPINNER_WIDTH = convertIntToDP(180);
        DEFAULT_SPINNER_HEIGHT = convertIntToDP(40);
        //the rest of LayoutParams should be defined here
        LayoutParams layPar = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layPar.setMargins(convertIntToDP(8), convertIntToDP(16), convertIntToDP(8), convertIntToDP(16));
        setLayoutParams(layPar);

        //setting up the elements
        //1. Sparepart Picker
        LayoutParams lpSpinner = new LayoutParams(DEFAULT_SPINNER_WIDTH, DEFAULT_SPINNER_HEIGHT);
        lpSpinner.setMargins(convertIntToDP(4), convertIntToDP(16), convertIntToDP(8), convertIntToDP(16));
        lpSpinner.addRule(ALIGN_PARENT_LEFT);
        createSparepartPicker(context);
        sparepartPickerID = View.generateViewId();
        sparepartPicker.setId(sparepartPickerID);
        sparepartPicker.setLayoutParams(lpSpinner);
        addView(sparepartPicker);
        //Log.d("sparepartPickerID", String.valueOf(sparepartPickerID));
        //2. Quantity value
        LayoutParams lpTextView = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lpTextView.setMargins(convertIntToDP(10), convertIntToDP(16), convertIntToDP(8), convertIntToDP(16));
        //Log.d("sparepartPickerID", String.valueOf(sparepartPickerID));
        lpTextView.addRule(RelativeLayout.RIGHT_OF, sparepartPickerID);
        createQtyValue(context);
        qtyValueID = View.generateViewId();
        qtyValue.setId(qtyValueID);
        qtyValue.setLayoutParams(lpTextView);
        qtyValue.setTextSize(26);
        addView(qtyValue);
        //3. Buttons
        LayoutParams lpAddButton = new LayoutParams(DEFAULT_WIDTH_BUTTON, DEFAULT_HEIGHT_BUTTON);
        lpAddButton.setMargins(convertIntToDP(4), convertIntToDP(16), convertIntToDP(8), convertIntToDP(16));
        lpAddButton.addRule(RIGHT_OF, qtyValueID);
        createAddButton(context);
        addButtonID = View.generateViewId();
        addButton.setId(addButtonID);
        addButton.setLayoutParams(lpAddButton);
        addView(addButton);
        LayoutParams lpMinButton = new LayoutParams(DEFAULT_WIDTH_BUTTON, DEFAULT_HEIGHT_BUTTON);
        lpMinButton.setMargins(convertIntToDP(4), convertIntToDP(16), convertIntToDP(8), convertIntToDP(16));
        lpMinButton.addRule(RIGHT_OF, addButtonID);
        createMinButton(context);
        minButtonID = View.generateViewId();
        minButton.setId(minButtonID);
        minButton.setLayoutParams(lpMinButton);
        addView(minButton);
    }

    public void setQtyValue(int value) {
        if(value < 0) {
            Log.d("errorSetQtyValue", "Can't assign negative values!");
            Log.d("errorSetQtyValue", "Assigning zero value instead");
            qtyValue.setText("0");
        } else {
            qtyValue.setText(String.valueOf(value));
        }
    }

    private TextView createQtyValue(Context ctx) {
        qtyValue = new TextView(ctx);
        //setUniqueID(qtyValue, qtyValueID);
        qtyValue.setText("0");
        return qtyValue;
    }

    public void setSparepartPicker(ArrayAdapter adapter) {
        sparepartPicker.setAdapter(adapter);
    }

    private Spinner createSparepartPicker(Context ctx) {
        sparepartPicker = new Spinner(ctx);
        //setUniqueID(sparepartPicker, sparepartPickerID);
        //Log.d("createSpinner", String.valueOf(sparepartPickerID));
        //sparepartPicker
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.spinner_name_spr));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        sparepartPicker.setAdapter(spinnerArrayAdapter);
        return sparepartPicker;
    }

    public void setAddButton(int dpWidth, int dpHeight, String label) {
        //convert integers into dp equivalent first
        int finalDPWidth = convertIntToDP(dpWidth);
        int finalDPHeight = convertIntToDP(dpHeight);
        //change the values
        addButton.setWidth(finalDPWidth);
        addButton.setHeight(finalDPHeight);
        addButton.setText(label);
    }

    private Button createAddButton(Context ctx){
        addButton = new Button(ctx);
        //setUniqueID(addButton, addButtonID);
        addButton.setWidth(DEFAULT_WIDTH_BUTTON);
        addButton.setHeight(DEFAULT_HEIGHT_BUTTON);
        addButton.setText("+");
        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                addValue(qtyValue);
            }
        });
        return addButton;
    }

    private void addValue(TextView quantity) {
        int value = Integer.parseInt(quantity.getText().toString());
        value += 1;
        quantity.setText(String.valueOf(value));
    }

    public void setMinButton(int dpWidth, int dpHeight, String label) {
        //convert integers into dp equivalent first
        int finalDPWidth = convertIntToDP(dpWidth);
        int finalDPHeight = convertIntToDP(dpHeight);
        //change the values
        minButton.setWidth(finalDPWidth);
        minButton.setHeight(finalDPHeight);
        minButton.setText(label);
    }

    private Button createMinButton(Context ctx) {
        minButton = new Button(ctx);
        //setUniqueID(minButton, minButtonID);
        minButton.setWidth(DEFAULT_WIDTH_BUTTON);
        minButton.setHeight(DEFAULT_HEIGHT_BUTTON);
        minButton.setText("-");
        minButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                minValue(qtyValue);
            }
        });
        return minButton;
    }

    private void minValue(TextView quantity) {
        int value = Integer.parseInt(quantity.getText().toString());
        value -= 1;
        if(value < 0) {
            quantity.setText("0");
        } else {
            quantity.setText(String.valueOf(value));
        }
    }

    private int convertIntToDP(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }

    private void setUniqueID(View view, int reference) {
        reference = View.generateViewId();
        view.setId(reference);
        //Log.d("uniqueID", String.valueOf(reference));
    }
}