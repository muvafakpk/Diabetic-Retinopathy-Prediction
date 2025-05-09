package com.example.diabeticretinopathyprediction;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    EditText ed, ed2, ed3, ed4, ed6;
    Button btn;
    ImageView im;
    SharedPreferences sh;
    ProgressDialog pd;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ed = findViewById(R.id.editTextTextPersonName2);
        ed2 = findViewById(R.id.editTextTextPersonName3);
        ed3 = findViewById(R.id.editTextTextPersonName4);
        ed4= findViewById(R.id.editTextPhone);
        ed6 = findViewById(R.id.editTextTextPassword3);
        im = findViewById(R.id.imageView4);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });
        btn = findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Emailpattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String cp = ed.getText().toString();
                String na = ed2.getText().toString();
                String email = ed3.getText().toString();
                String phn = ed4.getText().toString();
                String ps = ed6.getText().toString();


                if (!cp.equalsIgnoreCase(ps)){
                    ed.setError("password doesnt match");
                    return;
                }
                if (na.equalsIgnoreCase("")){
                    ed2.setError("required");
                    return;
                }
                if (!email.matches(Emailpattern) && email.length()>0){
                    ed3.setError("enter valid email");
                    return;
                } if (phn.length()!=10){
                    ed4.setError("enter a valid phone number");
                    return;
                } if (ps.equalsIgnoreCase("")){
                    ed6.setError("required");
                    return;
                }
                if (bitmap == null){
                    Toast.makeText(register.this, "Choose image", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    uploadBitmap(na, email, phn, ps);
                }

            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            Uri imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                im.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //converting to bitarray
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    private void uploadBitmap(final String name, final String email, final String phone, String p) {


        pd = new ProgressDialog(register.this);
        pd.setMessage("Uploading....");
        pd.show();
        String url = sh.getString("url", "") + "/android_register";
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            pd.dismiss();


                            JSONObject obj = new JSONObject(new String(response.data));

                            if(obj.getString("status").equals("ok")){
                                Toast.makeText(getApplicationContext(), "Registration success", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), Login.class);
                                startActivity(i);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Email Already Exists" ,Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                SharedPreferences o = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                params.put("na", name);//passing to python
                params.put("em", email);//passing to python
                params.put("phon", phone);
                params.put("lat", gpstracker.lati);
                params.put("log", gpstracker.longi);
                params.put("p", p);
                return params;
            }


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }
}
