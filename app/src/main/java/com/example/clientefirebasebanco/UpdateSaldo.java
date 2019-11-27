package com.example.clientefirebasebanco;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class UpdateSaldo {
    String codCliente;
    int saldo;
    String valorRecarga;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public UpdateSaldo() {
    }
    public UpdateSaldo(String codcliente, int saldo) {
        this.saldo = saldo;
        this.codCliente = codcliente;

        DocumentReference clienteRef = db.collection("clientes").document(codcliente);

        // Set the "isCapital" field of the city 'DC'
        clienteRef
                .update("saldo", saldo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error updating document", e);
                    }
                });

    }

    public int ProcesarRecarga(String codCliente, String saldoActual, String valorRecarga){
        int intValorRecarga = Integer.parseInt(valorRecarga);
        int intSaldoActual = Integer.parseInt(saldoActual);
        int saldoFinal = intValorRecarga + intSaldoActual;
        //Toast.makeText(getApplicationContext(), "Saldo Actualizado: " + saldoFinal, Toast.LENGTH_SHORT).show();
        new UpdateSaldo(codCliente,saldoFinal);
        return saldoFinal;
    }
    public String getCodCliente() {
        return codCliente;
    }

    public int getSaldo() {
        return saldo;
    }

    public String getValorRecarga() {
        return valorRecarga;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public void setValorRecarga(String valorRecarga) {
        this.valorRecarga = valorRecarga;
    }






}

