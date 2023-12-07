package com.example.conectamovil.Vista;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.conectamovil.Controlador.InfoUsuario;
import com.example.conectamovil.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class EditarUsuarioActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imagen;
    private Button cambiarFoto, guardarCambios, buscarUsuario;
    private EditText nombresEditar, correoEditar, passEditar, buscarid;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        // Inicializar Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();

        // Referencias a las vistas
        imagen = findViewById(R.id.imgUsuario);
        cambiarFoto = findViewById(R.id.btnCambiarFoto);
        guardarCambios = findViewById(R.id.btnGuardarCambios);
        buscarUsuario = findViewById(R.id.btnBuscarUsuario);
        nombresEditar = findViewById(R.id.EditarNombres);
        correoEditar = findViewById(R.id.EditarCorreo);
        buscarid = findViewById(R.id.editTextBuscarId);

        // Inicializar la referencia a la base de datos
        databaseReference = firebaseDatabase.getReference("NuevosUsuarios");

        buscarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el ID del usuario que se va a buscar
                final String buscarID = buscarid.getText().toString().trim();

                // Buscar el usuario por ID
                buscarUsuarioPorID(buscarID);
            }
        });

        guardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los valores editados
                String nuevoNombre = nombresEditar.getText().toString().trim();
                String nuevoCorreo = correoEditar.getText().toString().trim();

                // Validar que los campos no estén vacíos
                if (!nuevoNombre.isEmpty() && !nuevoCorreo.isEmpty()) {
                    // Obtener el ID del usuario que se va a editar
                    final String buscarID = buscarid.getText().toString().trim();

                    // Actualizar los valores en la base de datos
                    editarUsuario(buscarID, nuevoNombre, nuevoCorreo);
                } else {
                    Toast.makeText(EditarUsuarioActivity.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Acciones del botón "Cambiar Foto"
        cambiarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar Foto"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            // Aquí puedes realizar acciones con la URI de la imagen seleccionada, como cargarla en tu ImageView
            imagen.setImageURI(imageUri);
        }
    }

    private void buscarUsuarioPorID(final String buscarID) {
        databaseReference.orderByChild("id").equalTo(buscarID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot listaID : snapshot.getChildren()) {
                        // Obtener la referencia del usuario que se va a editar
                        DatabaseReference usuarioRef = listaID.getRef();

                        // Obtener los datos del usuario
                        InfoUsuario usuario = listaID.getValue(InfoUsuario.class);

                        // Cargar los datos en las vistas correspondientes
                        nombresEditar.setText(usuario.getNombre());
                        correoEditar.setText(usuario.getCorreo());

                        Toast.makeText(EditarUsuarioActivity.this, "Usuario encontrado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditarUsuarioActivity.this, "No existe Usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar errores en la base de datos
            }
        });
    }

    private void editarUsuario(String buscarID, String nuevoNombre, String nuevoCorreo) {
        // Actualizar los valores en la base de datos
        databaseReference.orderByChild("id").equalTo(buscarID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot listaID : snapshot.getChildren()) {
                        // Obtener la referencia del usuario que se va a editar
                        DatabaseReference usuarioRef = listaID.getRef();

                        // Actualizar los valores
                        usuarioRef.child("nombre").setValue(nuevoNombre);
                        usuarioRef.child("correo").setValue(nuevoCorreo);

                        Toast.makeText(EditarUsuarioActivity.this, "Usuario Editado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditarUsuarioActivity.this, "No existe Usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar errores en la base de datos
            }
        });
    }
}
