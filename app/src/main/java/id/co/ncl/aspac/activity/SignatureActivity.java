package id.co.ncl.aspac.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.ncl.aspac.R;

public class SignatureActivity extends AppCompatActivity {

    @BindView(R.id.signature_pad_fs) SignaturePad signature_pad_fs;
    @BindView(R.id.save_sign_btn) Button save_sign_btn;
    @BindView(R.id.clear_sign_btn) Button clear_sign_btn;

    private long serviceID;
    private Bitmap signedBitmap;
    private ProgressDialog progressDialog;

    @OnClick(R.id.clear_sign_btn)
    public void clearSignature() {
        signature_pad_fs.clear();
    }

    @OnClick(R.id.save_sign_btn)
    public void saveSignature() {
        save_sign_btn.setEnabled(false);
        clear_sign_btn.setEnabled(false);

        progressDialog = new ProgressDialog(this, R.style.CustomDialog);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.show();

        //Convert to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        signedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("signature_image", byteArray);
        intent.putExtra("service_id", serviceID);
        progressDialog.dismiss();
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_signature);
        setTitle("Sign Here");
        ButterKnife.bind(this);

        if(getIntent().getLongExtra("service_id", 0) != 0) {
            serviceID = getIntent().getLongExtra("service_id", 0);
        }

        //setup signature listener
        signature_pad_fs.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                //this one return normal imaage with white background
                signedBitmap = signature_pad_fs.getSignatureBitmap();
                //this one return png signture with no background
                //signedBitmap = signature_pad.getTransparentSignatureBitmap();
                //this one return vector. No idea how to store this
                //String signedVcotor = signature_pad.getSignatureSvg();

                //check bitmap null or not
                Log.d("signature width: ", String.valueOf(signedBitmap.getWidth()));
                Log.d("signature height: ", String.valueOf(signedBitmap.getHeight()));
            }

            @Override
            public void onClear() {

            }
        });
    }
}
