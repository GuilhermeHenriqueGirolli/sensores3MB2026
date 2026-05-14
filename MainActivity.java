package com.example.sensores2026v2;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// Declara a classe principal da Activity, herdando de AppCompatActivity
public class MainActivity extends AppCompatActivity {

    // Gerenciador de sensores do dispositivo
    SensorManager sensorManager;
    // Objeto que representa um sensor específico
    Sensor sensor;
    // Listener que recebe eventos do sensor
    SensorEventListener sensorEventListener;
    // Referência ao TextView que exibirá o resultado
    TextView txtResultadoProg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Chama o método da classe pai para iniciar a Activity
        super.onCreate(savedInstanceState);

        // Ativa o modo Edge-to-Edge (tela ocupando toda a área)
        EdgeToEdge.enable(this);

        // Define o layout XML que será exibido na tela
        setContentView(R.layout.activity_main);

        // Ajusta automaticamente margens e preenchimentos para barras do sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Obtém as áreas ocupadas por status bar e navigation bar
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Aplica o padding para evitar sobreposição
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Acessa o serviço de sensores do Android
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Obtém o sensor de luz do dispositivo
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // Verifica se o dispositivo possui sensor de luz
        if (sensor == null) {
            // Exibe mensagem informando que não há sensor
            Toast.makeText(this, "O dispositivo não possui sensor de luz!", Toast.LENGTH_SHORT).show();
            // Encerra a Activity
            finish();
        }

        // Cria o listener que receberá os eventos do sensor
        sensorEventListener = new SensorEventListener() {

            // Chamado sempre que o sensor detecta uma mudança
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                // Obtém o valor da luminosidade (em lux)
                float value = sensorEvent.values[0];

                // Conecta o TextView do layout ao código
                txtResultadoProg = (TextView) findViewById(R.id.txtResultado);

                // Exibe o valor da luminosidade na tela
                txtResultadoProg.setText("Luminosidade: " + value + " lx");
            }

            // Chamado quando a precisão do sensor muda (não usado aqui)
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }

    //Método chamado quando a Activity volta a ficar visível para o usuário
    @Override
    protected void onResume() {
        super.onResume();

        // Registra o listener para começar a receber dados do sensor
        // SENSOR_DELAY_FASTEST = menor intervalo possível entre leituras
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    // Método chamado quando a Activity deixa de estar em destaque
    @Override
    protected void onPause() {
        super.onPause();

        // Remove o listener para economizar bateria e processamento
        sensorManager.unregisterListener(sensorEventListener);
    }
}
