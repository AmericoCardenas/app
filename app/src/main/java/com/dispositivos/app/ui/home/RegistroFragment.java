package com.dispositivos.app.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dispositivos.app.MainActivity;
import com.dispositivos.app.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class RegistroFragment extends DialogFragment {

    private RegistroViewModel registroViewModel;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        registroViewModel = new ViewModelProvider(this).get(RegistroViewModel.class);
        View fragment_registro = inflater.inflate(R.layout.fragment_registro, container, false);

        EditText txt_email = fragment_registro.findViewById(R.id.txt_email);
        EditText txt_password = fragment_registro.findViewById(R.id.txt_contraseña);
        Button btn_registro = fragment_registro.findViewById(R.id.btn_registro);

        registroViewModel = new ViewModelProvider(this).get(RegistroViewModel.class);

        registroViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        super.onCreate(savedInstanceState);

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


                    Toast.makeText(getActivity(),"FAVOR DE NO DEJAR CAMPOS VACIOS",Toast.LENGTH_LONG).show();

                    return;
                }
                else{
                    enviar("https://btptda.com.mx/FunctionsController/insert_usuario",nombre,email,password,area);
                }

            }
        });


        return fragment_registro;
    }

    private void enviar(String URL,String nombre,String email,String password,String area ){
        StringRequest stringRequest =  new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    Intent intent = new Intent (getContext(), MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(),"DATOS INGRESADOS CORRECTAMENTE",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent (getContext(), MainActivity.class);
                    intent.putExtra("usuario",email);
                    startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),error.networkResponse.data.toString(),Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }




}