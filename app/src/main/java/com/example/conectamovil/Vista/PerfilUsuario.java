package com.example.conectamovil.Vista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.conectamovil.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class PerfilUsuario extends AppCompatActivity {

    private EditText nombrePerfil, correoPerfil;
    private ImageView imgUsuario;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        nombrePerfil = findViewById(R.id.PerfilNombre);
        correoPerfil = findViewById(R.id.PerfilCorreo);
        imgUsuario = findViewById(R.id.imgUsuario);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("usuarios");

        mostrarPerfil();
    }

    private void mostrarPerfil() {
        // Obtener el ID del usuario actual (puedes obtenerlo de alguna manera, por ejemplo, a través de SharedPreferences)
        final String usuarioID = obtenerUsuarioID();

        // Leer la información del usuario desde la base de datos
        databaseReference.child(usuarioID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Obtener datos del usuario
                    String nombre = snapshot.child("nombre").getValue(String.class);
                    String correo = snapshot.child("correo").getValue(String.class);

                    // Mostrar la información en las vistas
                    nombrePerfil.setText(nombre);
                    correoPerfil.setText(correo);

                    // Puedes cargar la foto usando una biblioteca como Picasso o Glide
                    // En este ejemplo, supongamos que tienes la URL de la foto almacenada en la base de datos
                    String urlFoto = snapshot.child("urlFoto").getValue(String.class);
                    if (urlFoto != null && !urlFoto.isEmpty()) {
                        Picasso.get().load(urlFoto).into(imgUsuario);
                    }
                } else {
                    Toast.makeText(PerfilUsuario.this, "No existe Usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar errores en la base de datos
            }
        });
    }

    // Método ficticio para obtener el ID del usuario actual (debes implementar tu propia lógica)
    private String obtenerUsuarioID() {
        // Implementa la lógica para obtener el ID del usuario actual
        return "ID_del_usuario_actual";
    }
}