package com.example.clientefirebasebanco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ClienteLogeado extends AppCompatActivity {
    EditText valorrecarga, valorpago,nrofactura;
    TextView tvnombres,tvsaldo;
    Button recargar, pagar;
    String codcliente,nombres, apellidos,saldo, codFactura,valorPago;
    int saldoInt;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_logeado);

        tvnombres = findViewById(R.id.tvnombre);
        tvsaldo = findViewById(R.id.tvsaldo);
        valorrecarga = findViewById(R.id.etvalorrecarga);
        nrofactura = findViewById(R.id.etnrofactura);
        valorpago = findViewById(R.id.etvalorpago);
        recargar = findViewById(R.id.btnrecargar);
        pagar = findViewById(R.id.btnpagar);


        final Bundle bundle = getIntent().getExtras();
        if(bundle.getString("codcliente")!= null){
            codcliente = bundle.getString("codcliente");
            nombres = bundle.getString("nombres");
            apellidos = bundle.getString("apellidos");
            saldo = bundle.getString("saldo");

            tvsaldo.setText(saldo);
            tvnombres.setText(nombres +" "+ apellidos);
        }
        /*Instanciar un objeto Saldo*/
        saldoInt = Integer.parseInt(saldo);
        final Saldo modelSaldo = new Saldo(codcliente,saldoInt);
        /* *************** */

        /*Fecha y hora del sistema*/
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        final String fecha = dateFormat.format(date);
        DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        final String hora = hourFormat.format(date);
        //Toast.makeText(getApplicationContext(),fecha+" hora "+hora,Toast.LENGTH_SHORT).show();


        /*Instanciar firestore*/
        db = FirebaseFirestore.getInstance();
        //Toast.makeText(getApplicationContext(),id.getText().toString(),Toast.LENGTH_SHORT).show();

        recargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Añadir una recarga a la coleccion Recarga
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

                                /*Actualizar saldo*/
                                actualizarSaldo(codcliente, saldo,valorrecarga.getText().toString());

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
                //modelSaldo.ReadSaldo();
                //(nrofactura.getText().toString());
                BuscarFactura(nrofactura.getText().toString());
                }
        });


    }

    public void actualizarSaldoPago(String codcliente, String saldo, String ValorDebito) {
        int saldoInt= Integer.parseInt(saldo);

        Saldo pago = new Saldo(codcliente,saldoInt);
        int saldoFinal = pago.ProcesarPago(codcliente,saldo,ValorDebito);
        Toast.makeText(getApplicationContext(),"Saldo Actualizado a: "+saldoFinal,Toast.LENGTH_SHORT).show();
        String stringSaldoFinal= Integer.toString(saldoFinal);
        tvsaldo.setText(stringSaldoFinal);
    }

    public void BuscarFactura(String nrofact){
        codFactura=""; //variable para verificar si esta vacio no existe
        db.collection("factura")
                .whereEqualTo("nrofact",nrofact)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                codFactura = document.getId(); //capturar el id del documento
                                valorPago = document.getData().get("vlrfactura").toString();
                                codcliente = document.getData().get("codcliente").toString();

                                valorpago.setText(valorPago);

                                //Toast.makeText(getApplicationContext(),"la factura si existe",Toast.LENGTH_SHORT).show();

                                ProcesarFactura(codcliente,valorPago);

                            }
                            if (codFactura.isEmpty()){
                                Toast.makeText(getApplicationContext(),"Nro de factura no existente",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.w("factura", "Error leyendo documento.", task.getException());
                        }
                    }
                });
    }
    public void ProcesarFactura(String codCliente, String valorPago){
        int valorPagoInt= Integer.parseInt(valorPago);
        String nrofact = nrofactura.getText().toString();
        int saldoInt= Integer.parseInt(tvsaldo.getText().toString());

        if (saldoInt>=valorPagoInt){
            Factura factura = new Factura(codCliente,nrofact,valorPagoInt);

            int saldoFinal = factura.ProcesarFactura(codcliente,nrofact,valorPagoInt,saldoInt);
            Toast.makeText(getApplicationContext(),"Pago realizado",Toast.LENGTH_SHORT).show();
            String stringSaldoFinal= Integer.toString(saldoFinal);
            tvsaldo.setText(stringSaldoFinal);
        }else{
            Toast.makeText(getApplicationContext(),"Saldo insuficiente",Toast.LENGTH_SHORT).show();
        }



    }

    public void actualizarSaldo(String codcliente, String saldo, String valorRecarga) {
        //Saldo recarga = new Saldo();
        int saldoInt= Integer.parseInt(saldo);
        Saldo recarga = new Saldo(codcliente,saldoInt);

        int saldoFinal = recarga.ProcesarRecarga(codcliente,saldo,valorRecarga);
        Toast.makeText(getApplicationContext(),"Saldo Actualizado a: "+saldoFinal,Toast.LENGTH_SHORT).show();
        String stringSaldoFinal= Integer.toString(saldoFinal);
        tvsaldo.setText(stringSaldoFinal);

    }
}
