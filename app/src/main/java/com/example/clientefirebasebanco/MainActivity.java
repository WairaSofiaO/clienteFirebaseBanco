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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    EditText usuario,clave;
    TextView id,registrarme;
    Button iniciarsesion;
    FirebaseFirestore db;
    String codcliente, getCodCliente,getNombres,getApellidos,getSaldo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuario = findViewById(R.id.etusuario);
        clave = findViewById(R.id.etclave);
        id = findViewById(R.id.tvid);
        iniciarsesion = findViewById(R.id.btniniciarsesion);
        registrarme = findViewById(R.id.btnregistrarme);
        usuario.getText().toString();

        /* Mandar datos a otra activity */

        /*instanciar firebase firestore*/
        db = FirebaseFirestore.getInstance();

        iniciarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Funcion buscar usuario en la base de datos clientes*/
                codcliente=""; // verifica si no existe
                db.collection("clientes")
                        /*Comparar valores usuario y clave*/
                        .whereEqualTo("usuario",usuario.getText().toString())
                        .whereEqualTo("clave",clave.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for (QueryDocumentSnapshot document : task.getResult()){
                                        //Log.d(TAG, document.getId() + " => " + document.getData());

                                        /*Variable para verificar si existe o no */
                                        codcliente = document.getId();
                                        Toast.makeText(getApplicationContext(),"Ingreso exitoso",Toast.LENGTH_SHORT).show();
                                        id.setText(codcliente);

                                        /*Variables para mandar los datos del cliente a otra actividad*/
                                        getNombres = document.getData().get("nombres").toString();
                                        getApellidos = document.getData().get("apellidos").toString();
                                        getSaldo = document.getData().get("saldo").toString();

                                        getInfoCliente(codcliente, getNombres, getApellidos, getSaldo);

                                        irLogueado(codcliente,getNombres, getApellidos, getSaldo);
                                    }
                                    if (codcliente.isEmpty()){
                                        Toast.makeText(getApplicationContext(),"Usuario y/o contraseña incorrecta",Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.w("clientes", "Error leyendo documento", task.getException());
                                    }
                                }
                            }
                        });
            }
        });
    }

    public void getInfoCliente(String codcliente, String nombres, String apellidos, String saldo) {

        int saldoInt = Integer.parseInt(saldo);
        String getUsuario = usuario.getText().toString();
        String getClave= clave.getText().toString();
        Toast.makeText(getApplicationContext(),"USUARIO: "+getUsuario,Toast.LENGTH_SHORT).show();
        Cliente infoCliente = new Cliente(codcliente, nombres, apellidos, saldoInt,getUsuario,getClave);

        /*
        info.setCodCliente(codcliente);
        info.setNombres(nombres);
        info.setApellidos(apellidos);
        info.setSaldo(SaldoInt);
        info.setUsuario(usuario.getText().toString());
        info.setClave(clave.getText().toString());
        String saldox = info.getCodCliente();
        //Toast.makeText(getApplicationContext(),saldox,Toast.LENGTH_SHORT).show();
        return info;

         */

    }

    public void  irLogueado(String codcliente,String nombres,String apellidos,String saldo){
        Intent irLog = new Intent(MainActivity.this, ClienteLogeado.class);
        irLog.putExtra("codcliente",codcliente);
        irLog.putExtra("nombres",nombres);
        irLog.putExtra("apellidos",apellidos);
        irLog.putExtra("saldo",saldo);
        //irLog.putExtra("usuario",usuario.getText().toString());

        startActivity(irLog);

    }
}
