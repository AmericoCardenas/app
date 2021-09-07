package com.dispositivos.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    Button btn_registro;
    EditText txt_email,txt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        btn_registro = findViewById(R.id.btn_registro);

        //BTN REGISTRAR
        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txt_email.getText().toString().trim();
                String password = txt_password.getText().toString().trim();
                String nombre = "na";
                String area = "na";


                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){

                    txt_email.setError("Campo Requerido");
                    txt_password.setError("Campo Requerido");
                    txt_email.requestFocus();
                    txt_password.requestFocus();


                    Toast.makeText(getApplicationContext(),"FAVOR DE NO DEJAR CAMPOS VACIOS",Toast.LENGTH_LONG).show();

                    return;
                }
                else{
                    enviar("https://btptda.com.mx/FunctionsController/insert_usuario",nombre,email,password,area);
                }

            }
        });


    }

    public void enviar(String URL,String nombre,String email,String password,String area ){
        StringRequest stringRequest =  new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    Intent intent = new Intent (getApplicationContext(), RegistroActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"DATOS INGRESADOS CORRECTAMENTE",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                    intent.putExtra("usuario",email);
                    startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),error.networkResponse.data.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                //parametros.put("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");

                parametros.put("nombre",nombre);
                parametros.put("email",email);
                parametros.put("password",password);
                parametros.put("area",area);


                parametros.put("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}