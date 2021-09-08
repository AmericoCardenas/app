package com.dispositivos.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
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
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {

    Button btn_registro;
    EditText txt_email,txt_password,txt_vcontraseña,txt_contraseña,txt_nombre,txt_telefono,txt_domicilio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        btn_registro = findViewById(R.id.btn_registro);
        txt_email = findViewById(R.id.txt_email);

        txt_contraseña = findViewById(R.id.txt_contraseña);
        txt_vcontraseña = findViewById(R.id.txt_vcontraseña);
        txt_nombre = findViewById(R.id.txt_nombre);
        txt_telefono = findViewById(R.id.txt_telefono);
        txt_domicilio = findViewById(R.id.txt_direccion);

        //BTN REGISTRAR
        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = txt_email.getText().toString().trim();
                String password = txt_contraseña.getText().toString().trim();
                String nombre = txt_nombre.getText().toString().trim();
                String telefono = txt_telefono.getText().toString().trim();
                String domicilio = txt_domicilio.getText().toString().trim();
                String estatus = "SIN CONFIRMACION";


                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(nombre)
                        || TextUtils.isEmpty(telefono) || TextUtils.isEmpty(domicilio)){

                    txt_email.setError("Campo Requerido");
                    txt_password.setError("Campo Requerido");
                    txt_nombre.setError("Campo Requerido");
                    txt_telefono.setError("Campo Requerido");
                    txt_domicilio.setError("Campo Requerido");

                    txt_email.requestFocus();
                    txt_password.requestFocus();
                    txt_nombre.requestFocus();
                    txt_telefono.requestFocus();
                    txt_domicilio.requestFocus();


                    Toast.makeText(getApplicationContext(),"FAVOR DE NO DEJAR CAMPOS VACIOS",Toast.LENGTH_LONG).show();

                    return;
                }
                else{
                    enviar("https://btptda.com.mx/FunctionsController/insert_userapp",email,password,nombre,telefono,domicilio,estatus);
                }

            }
        });

        txt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String email = txt_email.getText().toString();
                if (!validarEmail(email)){
                    txt_email.setError("Email no válido");
                    txt_email.requestFocus();
                }

            }
        });

        txt_vcontraseña.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String contraseña = txt_contraseña.getText().toString();
                String vcontraseña = txt_vcontraseña.getText().toString();

                if (!vcontraseña.equals(contraseña)){
                    txt_vcontraseña.setError("Contraseña Distinta");
                    txt_vcontraseña.requestFocus();
                }

            }
        });




    }

    public void enviar(String URL,String email,String password,String nombre,String telefono, String domicilio, String estatus ){
        StringRequest stringRequest =  new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    Intent intent = new Intent (getApplicationContext(), RegistroActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"USUARIO REGISTRADO CORRECTAMENTE",Toast.LENGTH_SHORT).show();
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


                parametros.put("email",email);
                parametros.put("password",password);
                parametros.put("nombre",nombre);
                parametros.put("telefono",telefono);
                parametros.put("direccion",domicilio);
                parametros.put("estatus",estatus);


                parametros.put("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}