package com.example.clientefirebasebanco;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Factura {
    private String codCliente;
    private String nroFact;
    private int vlrFactura;
    String codFactura="";
    Boolean value=true;
    FirebaseFirestore db= FirebaseFirestore.getInstance();

    public Factura() {
    }
/*
    public Factura(String nroFact) {
        this.nroFact = nroFact;
    }

 */

    public Factura(String codcliente,String nrofact,int valorPagoInt) {
        this.nroFact = nroFact;
        this.vlrFactura = valorPagoInt;
        this.nroFact = nrofact;
    }
    public int ProcesarFactura(String codcliente,String nrofact,int valorPagoInt,int saldoInt){
        //int saldoInt = Integer.parseInt(saldo);
        //int intValorRecarga = Integer.parseInt(valorPagoInt);
        int saldoFinal = saldoInt - valorPagoInt;

        new Saldo().UpdateSaldo(codcliente,saldoFinal);
        //setSaldo(saldoFinal);
        new Factura(codcliente,nrofact,valorPagoInt);
        return saldoFinal;

    }

    public int ReadFactura(String nroFact){
        //final String nroFact = getNroFact();
        codFactura=""; //variable para verificar si esta vacio no existe
        db.collection("factura")
                .whereEqualTo("nrofact",nroFact)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                codFactura = document.getId(); //capturar el id del documento
                                vlrFactura = Integer.parseInt(document.getData().get("vlrfactura").toString());
                                setVlrFactura(vlrFactura);
                            }
                            if (codFactura.isEmpty()){
                                //Log.w("factura", "no existente", task.getException());
                                Log.d("Factura", "is empty");
                            }
                        } else {
                            Log.w("academia", "Error leyendo documento.", task.getException());
                        }
                    }
                });
        return vlrFactura;
    }

    public String getCodFactura() {
        return codFactura;
    }

    public void setCodFactura(String codFactura) {
        this.codFactura = codFactura;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public String getNroFact() {
        return nroFact;
    }

    public void setNroFact(String nroFact) {
        this.nroFact = nroFact;
    }

    public int getVlrFactura() {
        return vlrFactura;
    }

    public void setVlrFactura(int vlrFactura) {
        this.vlrFactura = vlrFactura;
    }
}
