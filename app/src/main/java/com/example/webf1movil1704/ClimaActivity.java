package com.example.webf1movil1704;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ClimaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clima);

        // Obtener los datos enviados desde la actividad anterior
        String ciudad = getIntent().getStringExtra("ciudad");
        String temperatura = getIntent().getStringExtra("temperatura");
        String condiciones = getIntent().getStringExtra("condiciones");
        String tempMax = getIntent().getStringExtra("tempMax");
        String tempMin = getIntent().getStringExtra("tempMin");
        String humedad = getIntent().getStringExtra("humedad");
        String presion = getIntent().getStringExtra("presion");
        String velocidadViento = getIntent().getStringExtra("velocidadViento");

        // Asignar los datos a los TextViews
        TextView tvCiudad = findViewById(R.id.tvCiudad);
        TextView tvTemperatura = findViewById(R.id.tvTemperatura);
        TextView tvCondiciones = findViewById(R.id.tvCondiciones);
        TextView tvTempMax = findViewById(R.id.tvTempMax);
        TextView tvTempMin = findViewById(R.id.tvTempMin);
        TextView tvHumedad = findViewById(R.id.tvHumedad);
        TextView tvPresion = findViewById(R.id.tvPresion);
        TextView tvVelocidadViento = findViewById(R.id.tvVelocidadViento);

        tvCiudad.setText("Ciudad: " + ciudad);
        tvTemperatura.setText("Temperatura: " + temperatura + "°C");
        tvCondiciones.setText("Condiciones: " + condiciones);
        tvTempMax.setText("Temp. Máxima: " + tempMax + "°C");
        tvTempMin.setText("Temp. Mínima: " + tempMin + "°C");
        tvHumedad.setText("Humedad: " + humedad + "%");
        tvPresion.setText("Presión: " + presion + " hPa");
        tvVelocidadViento.setText("Viento: " + velocidadViento + " m/s");
    }
}

