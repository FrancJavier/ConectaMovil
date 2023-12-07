package com.example.conectamovil.Vista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.conectamovil.Controlador.InfoUsuario;
import com.example.conectamovil.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PrincipalActivity extends AppCompatActivity {

    private Button Buscar, Editar, Agregar, Perfil, Eliminar, Lista,Chat;
    private EditText buscarid, nombres, correo;
    private  int contador=0; //(Generar ID)
    private FirebaseDatabase firebaseDatabase;

  //  private FirebaseAuth RegistroGeneral;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        firebaseDatabase = FirebaseDatabase.getInstance();
        // RegistroGeneral = FirebaseAuth.getInstance();
        Buscar = findViewById(R.id.btnBuscarUsuario);
        Editar = findViewById(R.id.btnActualizarUsuario);
        Agregar = findViewById(R.id.btnAgregarUsuario);
        Perfil = findViewById(R.id.btnPerfilUsuario);
        Eliminar = findViewById(R.id.btnEliminarUsuario);
        Lista = findViewById(R.id.btnListaUsuarios);
        nombres = findViewById(R.id.editTextNombre);
        correo = findViewById(R.id.editTextCorreo);
        buscarid = findViewById(R.id.editTextBuscarId);
        Chat = findViewById(R.id.btnChatUsuario);

        //NUEVA BASE DE DATOS
        databaseReference = firebaseDatabase.getReference("NuevosUsuarios");

        //METODO PARA BUSCAR USUARIO YA SEA POR ID, NOMBRE ETC
        Buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String buscarID = buscarid.getText().toString().trim();
                //Podemos buscar un usuario por id, nombre, ciudad etc
                databaseReference.orderByChild("id").equalTo(buscarID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //Buscamos si existe la ID
                        if (snapshot.exists()) {
                            for (DataSnapshot listaID : snapshot.getChildren()) {
                                InfoUsuario infoUsuario = listaID.getValue(InfoUsuario.class);
                                nombres.setText(infoUsuario.getNombre());
                                correo.setText(infoUsuario.getCorreo());
                            }
                        } else {
                            Toast.makeText(PrincipalActivity.this, "No existe Usuario", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


        //METODO PARA EDITAR USUARIO
        Editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para iniciar la EditarUsuarioActivity desde PrincipalActivity
                Intent intent = new Intent(PrincipalActivity.this, EditarUsuarioActivity.class);

                // Iniciar la actividad
                startActivity(intent);
            }
        });

        /*
        Editar.setOnClickListener(new View.OnClickListener() {

*/

        //METODO PARA AGREGAR USUARIO
        Agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los datos del EditText ingresados
                String nombreObtenido = nombres.getText().toString().trim();
                String correoObtenido = correo.getText().toString().trim();

                // Validar que el nombre no esté vacío
                if (nombreObtenido.isEmpty()) {
                    Toast.makeText(PrincipalActivity.this, "Ingrese un nombre válido", Toast.LENGTH_SHORT).show();
                    return; // Salir del método si el nombre está vacío
                }

                // Validar que el nombre contenga solo letras
                if (!nombreObtenido.matches("[a-zA-Z]+")) {
                    Toast.makeText(PrincipalActivity.this, "El nombre debe contener solo letras", Toast.LENGTH_SHORT).show();
                    return; // Salir del método si el nombre contiene números u otros caracteres
                }

                // Validar que el correo no esté vacío y tenga un formato válido
                if (correoObtenido.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(correoObtenido).matches()) {
                    Toast.makeText(PrincipalActivity.this, "Ingrese un correo válido", Toast.LENGTH_SHORT).show();
                    return; // Salir del método si el correo está vacío o no tiene un formato válido
                }

                contador++;

                InfoUsuario usuarioObtenido = new InfoUsuario(String.valueOf(contador), nombreObtenido, correoObtenido);

                // Añadir los datos a la base de datos
                databaseReference.child(String.valueOf(contador)).setValue(usuarioObtenido);

                // Mostrar mensaje de usuario agregado
                Toast.makeText(PrincipalActivity.this, "Usuario Agregado", Toast.LENGTH_SHORT).show();
            }
        });




        //METODO PERFIL USUARIO
        Perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el ID del usuario para ver el perfil
                final String usuarioID = buscarid.getText().toString().trim();

                // Verificar que el ID del usuario no esté vacío
                if (!usuarioID.isEmpty()) {
                    // Llamar al método para mostrar el perfil del usuario
                    mostrarPerfilUsuario(usuarioID);
                } else {
                    Toast.makeText(PrincipalActivity.this, "Ingresa el ID del usuario", Toast.LENGTH_SHORT).show();
                }
            }

            // Método para mostrar el perfil del usuario
            private void mostrarPerfilUsuario(String usuarioID) {
                // Realizar una consulta a la base de datos para obtener los detalles del usuario
                databaseReference.orderByChild("id").equalTo(usuarioID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot usuarioSnapshot : snapshot.getChildren()) {
                                // Obtener el usuario de la base de datos
                                InfoUsuario perfilusuario = usuarioSnapshot.getValue(InfoUsuario.class);

                                // Mostrar los detalles del usuario en tu interfaz de usuario
                                if (perfilusuario != null) {
                                    // Aquí puedes actualizar tu interfaz de usuario con los detalles del usuario
                                    // Por ejemplo, puedes establecer el texto en TextViews, cargar imágenes, etc.
                                    nombres.setText(perfilusuario.getNombre());
                                    correo.setText(perfilusuario.getCorreo());

                                    // Puedes agregar más líneas aquí para mostrar otros detalles del usuario si es necesario
                                }
                            }
                        } else {
                            // Manejar el caso en que no se encuentre el usuario
                            Toast.makeText(PrincipalActivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Manejar errores en la base de datos
                        Toast.makeText(PrincipalActivity.this, "Error al acceder a la base de datos", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        //METODO PARA ELIMINAR UN USUARIO
        Eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el ID del usuario que se va a eliminar
                final String buscarID = buscarid.getText().toString().trim();

                // Verificar que el ID no esté vacío
                if (!buscarID.isEmpty()) {
                    // Buscar el usuario en la base de datos
                    databaseReference.orderByChild("id").equalTo(buscarID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot listaID : snapshot.getChildren()) {
                                    // Obtener la referencia del usuario que se va a eliminar
                                    DatabaseReference usuarioRef = listaID.getRef();

                                    // Eliminar el usuario
                                    usuarioRef.removeValue()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(PrincipalActivity.this, "Usuario Eliminado", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(PrincipalActivity.this, "Error al eliminar usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            } else {
                                Toast.makeText(PrincipalActivity.this, "No existe Usuario con ID: " + buscarID, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(PrincipalActivity.this, "Error al acceder a la base de datos", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(PrincipalActivity.this, "Ingresa un ID para eliminar", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //METODO LISTAR LOS USUARIOS
        Lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrincipalActivity.this, ListaDeContactosActivity.class);
                startActivity(intent);

            }
        });

        Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (PrincipalActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });
    }
}




