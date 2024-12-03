package com.example.webf1movil1704;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.webf1movil1704.network.WeatherService;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Actividad2 extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_LOCATION = 101;
    private static final String API_KEY = "1a6999ae6747cfdef63d5c4f70c75aa1"; // Clave de la API
    private static final String CITY = "Palmira, Valle del Cauca, Colombia"; // Ciudad
    private static final String UNITS = "metric"; // Unidades métricas (°C)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_actividad2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        this.setTitle("Todas las opciones");

        // Solicitar permiso de GPS al entrar en la actividad
        solicitarPermisoGPS();
    }

    // Método para solicitar permiso de GPS
    private void solicitarPermisoGPS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Si el permiso no ha sido concedido, lo pedimos
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
        } else {
            // El permiso ya fue concedido
            Toast.makeText(this, "Permiso de GPS ya concedido", Toast.LENGTH_SHORT).show();
        }
    }

    // Manejar la respuesta de la solicitud de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido
                Toast.makeText(this, "Permiso de GPS concedido", Toast.LENGTH_SHORT).show();
            } else {
                // Permiso denegado
                Toast.makeText(this, "Permiso de GPS denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Método para consultar datos meteorológicos
    public void verMeteorologia(View v) {
        // Configuración de Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Crear el servicio de Retrofit
        WeatherService service = retrofit.create(WeatherService.class);

        // Llamar a la API
        Call<JsonObject> call = service.getWeather(CITY, API_KEY, UNITS);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject weatherData = response.body();
                    String description = weatherData.getAsJsonArray("weather")
                            .get(0)
                            .getAsJsonObject()
                            .get("description")
                            .getAsString();
                    double temp = weatherData.getAsJsonObject("main").get("temp").getAsDouble();
                    double tempMax = weatherData.getAsJsonObject("main").get("temp_max").getAsDouble();
                    double tempMin = weatherData.getAsJsonObject("main").get("temp_min").getAsDouble();
                    int humidity = weatherData.getAsJsonObject("main").get("humidity").getAsInt();
                    int pressure = weatherData.getAsJsonObject("main").get("pressure").getAsInt();
                    double windSpeed = weatherData.getAsJsonObject("wind").get("speed").getAsDouble();

                    // Preparar los datos para enviar a la actividad de clima
                    String ciudad = weatherData.get("name").getAsString();
                    String temperatura = String.valueOf(temp);
                    String condiciones = description;
                    String tempMaxString = String.valueOf(tempMax);
                    String tempMinString = String.valueOf(tempMin);
                    String humedad = String.valueOf(humidity);
                    String presion = String.valueOf(pressure);
                    String velocidadViento = String.valueOf(windSpeed);

                    // Enviar los datos a ClimaActivity
                    Intent intent = new Intent(Actividad2.this, ClimaActivity.class);
                    intent.putExtra("ciudad", ciudad);
                    intent.putExtra("temperatura", temperatura);
                    intent.putExtra("condiciones", condiciones);
                    intent.putExtra("tempMax", tempMaxString);
                    intent.putExtra("tempMin", tempMinString);
                    intent.putExtra("humedad", humedad);
                    intent.putExtra("presion", presion);
                    intent.putExtra("velocidadViento", velocidadViento);
                    startActivity(intent);
                } else {
                    Toast.makeText(Actividad2.this, "No se pudo obtener el clima.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(Actividad2.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ingreso_lotes(View v) {
        Intent intento = new Intent(this, Activity_ingreso_lotes.class);
        startActivity(intento);
    }

    public void cerrarSesion(View v) {
        Intent intento = new Intent(this, MainActivity.class);
        startActivity(intento);
    }

    public void goToCamera(View v) {
        Intent ScreenCamera = new Intent(this, CameraActivity.class);
        startActivity(ScreenCamera);
    }

    public void goToRegistrarLote(View v) {
        Intent ScreenRegistrarLote = new Intent(this, RegistrarLoteActivity.class);
        startActivity(ScreenRegistrarLote);
    }
}
