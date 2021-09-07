package com.dispositivos.app.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dispositivos.app.MainActivity;
import com.dispositivos.app.R;
import com.dispositivos.app.RegistroActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    EditText txt_usuario, txt_password;
    Button btn_login;
    ConstraintLayout constraintLayout;
    TextView txt_registro;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


            setContentView(R.layout.activity_login);

            txt_usuario = findViewById(R.id.txt_usuario);
            txt_password = findViewById(R.id.txt_password);
            btn_login = findViewById(R.id.btn_login);
            txt_registro = findViewById(R.id.txt_registro);

            String registro = txt_registro.getText().toString();
            SpannableString span_registro = new SpannableString(registro);
            span_registro.setSpan(new UnderlineSpan(), 0, span_registro.length(), 0);
            txt_registro.setText(span_registro);



            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                            String usuario = txt_usuario.getText().toString().trim();
                            String contraseña = txt_password.getText().toString().trim();

                            if(TextUtils.isEmpty(usuario) && TextUtils.isEmpty(contraseña)){

                                txt_usuario.setError("Campo Requerido");
                                txt_password.setError("Campo Requerido");

                                txt_usuario.requestFocus();
                                txt_password.requestFocus();

                                Toast.makeText(LoginActivity.this,"FAVOR DE NO DEJAR CAMPOS VACIOS",Toast.LENGTH_LONG).show();

                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);

                                return;
                            }
                            else{
                                login("https://btptda.com.mx/FunctionsController/login");
                            }
                        };
            });

            txt_usuario.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String email = txt_usuario.getText().toString();
                    if (!validarEmail(email)){
                        txt_usuario.setError("Email no válido");
                        txt_usuario.requestFocus();
                    }

                }
            });

            txt_registro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent (getApplicationContext(), RegistroActivity.class);
                    startActivity(intent);
                }
            });


        }


        private void login(String URL){


            StringRequest stringRequest =  new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(!response.isEmpty()){

                        Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                        String usuario = txt_usuario.getText().toString().trim();
                        intent.putExtra("usuario",usuario);
                        startActivity(intent);

                    }else{
                        Toast.makeText(LoginActivity.this,"SERVIDOR SIN RESPUESTA",Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Context context = getApplicationContext();
                    CharSequence text = "USUARIO O CONTRASEÑA INCORRECTA";
                    int duration = Toast.LENGTH_SHORT;
                    System.out.println(error);
                    Toast.makeText(context, text, duration).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> parametros = new HashMap<String,String>();
                    parametros.put("email",txt_usuario.getText().toString());
                    parametros.put("password",txt_password.getText().toString());
                    return parametros;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }



    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    }
