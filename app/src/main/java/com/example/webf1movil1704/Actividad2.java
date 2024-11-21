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

public class Actividad2 extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_LOCATION = 101;

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
