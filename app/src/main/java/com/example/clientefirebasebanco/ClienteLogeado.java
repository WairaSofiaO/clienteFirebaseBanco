package com.example.clientefirebasebanco;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ClienteLogeado extends AppCompatActivity {
    TextView txtusuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_logeado);

        String usuario = getIntent().getStringExtra("rusuario");
        txtusuario.setText(usuario);
    }
}
