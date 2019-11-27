package com.example.clientefirebasebanco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;

public class ClienteLogeado extends AppCompatActivity {
    EditText valorrecarga, valorpago,codfactura;
    TextView tvnombres,tvsaldo;
    Button recargar, pagar;
    String codcliente,nombres, apellidos,saldo;
    FirebaseFirestore db;
    UpdateSaldo updateSaldo = new UpdateSaldo();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_logeado);
        Recargas recargas = new Recargas();

        tvnombres = findViewById(R.id.tvnombre);
        tvsaldo = findViewById(R.id.tvsaldo);
        valorrecarga = findViewById(R.id.etvalorrecarga);
        valorpago = findViewById(R.id.etvalorpago);
        recargar = findViewById(R.id.btnrecargar);
        pagar = findViewById(R.id.btnpagar);

        /*Fecha y hora del sistema*/
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        final String fecha = dateFormat.format(date);
        DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        final String hora = hourFormat.format(date);
        //Toast.makeText(getApplicationContext(),fecha+" hora "+hora,Toast.LENGTH_SHORT).show();

        /*Recibir parametros*/
        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("codcliente")!= null){
            codcliente = bundle.getString("codcliente");
            nombres = bundle.getString("nombres");
            apellidos = bundle.getString("apellidos");
            saldo = bundle.getString("saldo");

            tvsaldo.setText(saldo);
            tvnombres.setText(nombres);
        }

        /*Instanciar firestore*/
        db = FirebaseFirestore.getInstance();
        //Toast.makeText(getApplicationContext(),id.getText().toString(),Toast.LENGTH_SHORT).show();

        recargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Añadir una recarga a la coleccion Regarga
                Map<String, Object> recarga = new HashMap<>();
                recarga.put("fecha",fecha);
                recarga.put("hora", hora);
                recarga.put("valor", valorrecarga.getText().toString());

                // Add a new document with a generated ID
                db.collection("recarga")
                        .add(recarga)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                Toast.makeText(getApplicationContext(),"Recarga Exitosa",Toast.LENGTH_SHORT).show();
                                /*Actulizar saldo*/
                                int saldoFinal =updateSaldo.ProcesarRecarga(codcliente,saldo,valorrecarga.getText().toString());
                                tvsaldo.setText(saldoFinal);


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Log.w(TAG, "Error adding document", e);
                                Toast.makeText(getApplicationContext(),"Error de conexion",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        pagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }
    }
