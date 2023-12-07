package com.example.conectamovil.Vista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.example.conectamovil.R;

public class MainActivity extends AppCompatActivity {

    private Button ingresar, registrar;
    private EditText email, pass;
    private FirebaseAuth autenticacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autenticacion = FirebaseAuth.getInstance();

        email = findViewById(R.id.CorreoMain);
        pass = findViewById(R.id.PassMain);
        ingresar = findViewById(R.id.btnIngresarMain);
        registrar = findViewById(R.id.btnRegistrarMain);

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IniciarSesion();
            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });
    }

    // Método para validar el inicio de sesión
    private void IniciarSesion() {
        String emailObtenido = email.getText().toString();
        String passwordObtenido = pass.getText().toString();

        if (emailObtenido.isEmpty() || passwordObtenido.isEmpty()) {
            Toast.makeText(MainActivity.this, "Faltan campos por llenar", Toast.LENGTH_LONG).show();
            return;
        }

        autenticacion.signInWithEmailAndPassword(emailObtenido, passwordObtenido)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(MainActivity.this, PrincipalActivity.class);
                            startActivity(intent);
                            Toast.makeText(MainActivity.this, "", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "La contraseña o correo es incorrecto", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
