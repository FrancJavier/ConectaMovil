package com.example.conectamovil.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.conectamovil.Controlador.MqttHandler;
import com.example.conectamovil.R;

public class ChatActivity extends AppCompatActivity {

    private static final String BROKER_URL = "tcp://test.mosquitto.org:1883";
    private static final String CLIENT_ID ="ChatFrancisco";

    private MqttHandler mqttHandler;
    private EditText messageEditText;

    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mqttHandler = new MqttHandler();
        mqttHandler.connect(BROKER_URL, CLIENT_ID);

        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageEditText.getText().toString();

                if(!message.isEmpty()){

                    String topic ="ChatFrancisco";
                    publicMessage(topic, message);
                    messageEditText.setText("");
                }else{
                    Toast.makeText(ChatActivity.this, "Por favor, escriba algo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        mqttHandler.disconnect();
        super.onDestroy();
    }

    private void publicMessage(String topic, String message) {
        Toast.makeText(this, "Publicado mensaje0" + message, Toast.LENGTH_SHORT).show();
        // Publicar el mensaje
        mqttHandler.publish(topic, message);
    }

    private void subscribeTopic(String topic) {
        // Suscribirse al tema
        Toast.makeText(this, "Suscrito al topico" + topic, Toast.LENGTH_SHORT).show();
        mqttHandler.subscribe(topic);
    }
}