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
    EditText usuario,clave,registrarme;
    TextView usu;
    Button iniciarsesion;
    FirebaseFirestore db;
    String idauto, nombres, apellidos, saldo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuario = findViewById(R.id.etusuario);
        clave = findViewById(R.id.etclave);
        usu = findViewById(R.id.usu);
        iniciarsesion = findViewById(R.id.btniniciarsesion);
        registrarme = findViewById(R.id.btnregistrarme);

        //instanciar firebase firestore
        db = FirebaseFirestore.getInstance();

        //boton iniciar sesion
        iniciarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Funcion buscar usuario en la base de datos clientes
                idauto=""; // verifica si no existe
                db.collection("clientes")
                        /*Comparar valores usuario y clave*/
                        .whereEqualTo("usuario",usuario.getText().toString())
                        .whereEqualTo( "clave",clave.getText().toString())
                        /* ********** */
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for (QueryDocumentSnapshot document : task.getResult()){
                                        //Log.d(TAG, document.getId() + " => " + document.getData());
                                        idauto= document.getId();
                                        usu.setText(document.getData().get("usuario").toString());
                                        //clave.setText(document.getData().get("clave").toString());

                                        /*
                                        String eusuario = usu.getText().toString(); //Variable que yo voy a mandar a la otra actividad
                                        Intent conectado = new Intent(getApplicationContext(),clientelogeado.class);
                                        conectado.putExtra("rusuario",eusuario); //El metodo PutExtra manda la variable eusuario y la recibe la variable rusuario
                                        startActivity(conectado); //Esto Lounchea la actividad Main2Activity
                                        */
                                        Toast.makeText(getApplicationContext(),"Usuario y contraseña encontrado",Toast.LENGTH_SHORT).show();
                                        //Toast.makeText(getApplicationContext(),"Usuario:"+usuario,Toast.LENGTH_SHORT).show();

                                        //startActivity(new Intent(getApplicationContext(),ClienteLogeado.class));





                                    }


                                    if (idauto.isEmpty()){
                                        Toast.makeText(getApplicationContext(),"Usuario y/o contraseña incorrectas",Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.w("clientes", "Error leyendo documento", task.getException());
                                    }
                                }
                            }
                        });

            }
        });
    }

}
