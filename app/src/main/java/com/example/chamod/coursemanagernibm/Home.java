package com.example.chamod.coursemanagernibm;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gc.materialdesign.views.ButtonRectangle;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class Home extends AppCompatActivity {

    ButtonRectangle btnLogin,btnSignUp;
    MaterialEditText txtUsername,txtPassword;
    String url="";
    String token;
    SweetAlertDialog pDialog;
    UserProfile profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        pDialog = new SweetAlertDialog(Home.this, SweetAlertDialog.PROGRESS_TYPE);
        btnLogin = findViewById(R.id.login);
        btnSignUp = findViewById(R.id.signUp);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        profile = new UserProfile();

        //create database or user existing one to store token (SQLite)
        final SQLiteDatabase database = openOrCreateDatabase("users.db",MODE_PRIVATE,null);
        database.execSQL("create table if not exists tokens (id INTEGER(2),token VARCHAR(1500))");

        //Check for user token
        try{
            Cursor cursor = database.rawQuery("select token from tokens where id = 1 ",null);
            cursor.moveToFirst();
            token = cursor.getString(cursor.getColumnIndex("token"));
            if(!token.equals(null)){
                profile.setToken(token);
                Intent intent = new Intent(Home.this,Dashboard.class);// New activity
                intent.putExtra("token",profile.getToken());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish(); // Call once you redirect to another activity
            }
        }catch(CursorIndexOutOfBoundsException e){
                e.printStackTrace();
        }




        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                captureUserInputs();
                // sweet alert spinner
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Authenticating...");
                pDialog.setCancelable(false);
                pDialog.show();

                    // Instantiate the RequestQueue.
                    RequestQueue queue = Volley.newRequestQueue(Home.this);
                    url = "https://nibm-api.herokuapp.com/web/api/authenticate";
                    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response) {
                                    // response
                                    pDialog.hide();
                                    try {

                                        JSONObject jsonResponse = new JSONObject(response);
                                        Log.e("response :",String.valueOf(jsonResponse.getBoolean("success")));

                                        if(jsonResponse.getBoolean("success")){

                                            database.execSQL("insert into tokens (id, token) values(1,'"+jsonResponse.getString("token")+"')");
                                            Toast.makeText(Home.this, "Welcome", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(Home.this,Dashboard.class);// New activity
                                            intent.putExtra("token",jsonResponse.getString("token"));
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish(); // Call once you redirect to another activity
                                        }else{
                                            new SweetAlertDialog(Home.this, SweetAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Oops...")
                                                    .setContentText("Authentication failed... try again!")
                                                    .show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }catch(SQLException e){
                                        e.printStackTrace();
                                        Log.e("error :",e.toString());
                                    }

                                }
                            },
                            new Response.ErrorListener()
                            {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // error
                                    Log.d("Error.Response", error.toString());
                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams()
                        {
                            Map<String, String>  params = new HashMap<String, String>();
                            params.put("password", profile.getPassword());
                            params.put("email", profile.getEmail());
                            return params;
                        }
                    };
                    queue.add(postRequest);

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,SignUp.class));
            }
        });
    }

    public void captureUserInputs(){

        profile.setEmail(txtUsername.getText().toString());
        profile.setPassword(txtPassword.getText().toString());
    }


}
