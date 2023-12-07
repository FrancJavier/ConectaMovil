// ListaDeContactosActivity.java

package com.example.conectamovil.Vista;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.conectamovil.Controlador.InfoUsuario;
import com.example.conectamovil.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListaDeContactosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewContactos;
    private ContactoAdapter adapter;
    private ArrayList<InfoUsuario> listaDeContactos;  // Cambiado a ArrayList de InfoUsuario

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_contactos);

        recyclerViewContactos = findViewById(R.id.recyclerViewContactos);
        listaDeContactos = new ArrayList<>();
        adapter = new ContactoAdapter(listaDeContactos);

        // Configura el RecyclerView con un LinearLayoutManager
        recyclerViewContactos.setLayoutManager(new LinearLayoutManager(this));

        // Asigna el adaptador al RecyclerView
        recyclerViewContactos.setAdapter(adapter);

        // Obtiene la referencia a la base de datos Firebase y agrega el ValueEventListener como ya lo hacías
        databaseReference = FirebaseDatabase.getInstance().getReference().child("NuevosUsuarios");

        // Agrega un listener para obtener los datos de la base de datos
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Limpia la lista antes de agregar los nuevos contactos
                listaDeContactos.clear();

                // Recorre los datos de la base de datos y agrega los contactos a la lista
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Intenta obtener el valor como Usuario
                    InfoUsuario usuario = snapshot.getValue(InfoUsuario.class);

                    // Verifica si el valor obtenido no es nulo antes de agregarlo a la lista
                    if (usuario != null) {
                        listaDeContactos.add(usuario);
                    } else {
                        Log.w("ListaDeContactosActivity", "El valor de usuario es nulo para un elemento.");
                    }
                }

                // Notifica al adaptador que los datos han cambiado
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Maneja los errores de la base de datos (opcional)
            }
        });
    }
}
