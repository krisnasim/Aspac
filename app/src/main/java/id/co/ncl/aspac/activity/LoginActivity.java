package id.co.ncl.aspac.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.ncl.aspac.R;
import id.co.ncl.aspac.customClass.CustomJSONObjectRequest;

public class LoginActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject> {

    @BindView(R.id.loginButton) Button loginButton;
    @BindView(R.id.input_username) EditText input_username;
    @BindView(R.id.input_pwd) EditText input_pwd;

    private ProgressDialog progressDialog;

    @OnClick(R.id.loginButton)
    public void login() {
        //set the url
        String url = "http://aspac.noti-technologies.com/api/login";

        loginButton.setEnabled(false);

        progressDialog = new ProgressDialog(LoginActivity.this, R.style.CustomDialog);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.show();

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("username", input_username.getText().toString());
            jsonObj.put("password", input_pwd.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        CustomJSONObjectRequest customJSONReq = new CustomJSONObjectRequest(Request.Method.POST, url, jsonObj, this, this);
        customJSONReq.setHeaders(headers);

        try {
            Log.d("headers", String.valueOf(customJSONReq.getHeaders()));
            Log.d("content", jsonObj.toString(2));
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestQueue.add(customJSONReq);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        checkforSharedPreferences();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.dismiss();
        Log.d("errorResponse", String.valueOf(error));
        Log.d("errorResponse", String.valueOf(error.getLocalizedMessage()));
        Log.d("errorResponse", Arrays.toString(error.getStackTrace()));
        onLoginFailed();
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialog.dismiss();
        try {
            Log.d("onResponse", response.toString(2));
            JSONObject jsonObj = response;

            SharedPreferences sharedPref = getSharedPreferences("userCred", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("token", jsonObj.getString("token"));
            editor.putString("name", jsonObj.getString("name"));
            editor.apply();

            Toast.makeText(this, "Welcome, "+ jsonObj.getString("name") +"!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Username atau password yang anda masukan tidak cocok. Mohon cek kembali user dan password saat login", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    private void checkforSharedPreferences() {
        SharedPreferences sharedPref = this.getSharedPreferences("userCred", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "empty token");
        //Log.d("tokenPrint", token);
        if(token.matches("empty token")) {
            //well, do nothing. no user is logged in
        } else {
            //no login required. GO AWAY FROM HERE
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
