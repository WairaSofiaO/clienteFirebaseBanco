package com.example.clientefirebasebanco;

import android.util.Log;
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

public class Saldo {
    private String codCliente;
    private int saldo;
    private String valorRecarga;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Saldo() {
    }
    public Saldo(String codCliente, int saldo) {
        this.codCliente = codCliente;
        this.saldo = saldo;
    }

    public void UpdateSaldo(final String codCliente, final int saldoFinal) {
        //final String codCliente = getCodCliente();

        DocumentReference clienteRef = db.collection("clientes").document(codCliente);

        clienteRef
                .update("saldo", saldo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully updated!");
                        new Saldo(codCliente,saldoFinal);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error updating document", e);
                    }
                });

    }
    public int ProcesarRecarga(String codcliente, String saldo, String valorRecarga){
        //String codCliente = this.getCodCliente();
        //int saldoActual = this.getSaldo();
        int saldoInt = Integer.parseInt(saldo);
        int intValorRecarga = Integer.parseInt(valorRecarga);
        int saldoFinal = intValorRecarga + saldoInt;

        new Saldo().UpdateSaldo(codcliente,saldoFinal);

        setSaldo(saldoFinal);
        return saldoFinal;
    }
    public void ReadSaldo(){
        final String codCliente = getCodCliente();
        db.collection("clientes")
                /*Comparar valores usuario y clave*/
                .whereEqualTo("codcliente",codCliente)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                String saldo= document.getData().get("saldo").toString();
                                /*Volver mi saldo en la variable setSaldo*/
                                int saldoInt = Integer.parseInt(saldo);
                                new Saldo(codCliente,saldoInt);
                            }
                        }
                    }
                });
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

