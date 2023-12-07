package com.example.conectamovil.Vista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.conectamovil.Controlador.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.conectamovil.R;


public class RegistroActivity extends AppCompatActivity {

    private EditText nombres, correo, pass;
    private Button registrarse;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        nombres = findViewById(R.id.editTextnombre);
        correo = findViewById(R.id.editTextEmail);
        pass = findViewById(R.id.editTextPass);
        registrarse = findViewById(R.id.btnRegistrar);

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registro();
            }
        });
    }

    //METODO REGISTRO LOGIN
    //VALIDACIONES
    private void registro() {
        String nombreUser = nombres.getText().toString().trim();
        String correoUser = correo.getText().toString().trim();
        String passUser = pass.getText().toString().trim();

        if (TextUtils.isEmpty(nombreUser) || TextUtils.isEmpty(correoUser) || TextUtils.isEmpty(passUser)) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(correoUser, passUser)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Registro en Firebase Authentication exitoso
                            String userId = mAuth.getCurrentUser().getUid();
                            DatabaseReference nuevoUsuarioRef = databaseReference.child(userId);

                            Usuario nuevoUsuario = new Usuario(nombreUser, correoUser, passUser);
                            nuevoUsuarioRef.setValue(nuevoUsuario);

                            Toast.makeText(this, "Usuario Registrado", Toast.LENGTH_SHORT).show();
                        } else {
                            // Manejar fallos en el registro
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(this, "Error en el registro: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
