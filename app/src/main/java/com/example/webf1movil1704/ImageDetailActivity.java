package com.example.webf1movil1704;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;

public class ImageDetailActivity extends AppCompatActivity {

    private ImageView ivDetailImage;
    private TextView tvDetailDescription;
    private Button btnDeleteImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        ivDetailImage = findViewById(R.id.ivDetailImage);
        tvDetailDescription = findViewById(R.id.tvDetailDescription);
        btnDeleteImage = findViewById(R.id.btnDeleteImage);

        // Obtener el nombre del archivo desde el Intent
        String fileName = getIntent().getStringExtra("fileName");

        // Leer el archivo de texto combinado
        try {
            FileInputStream fileInputStream = openFileInput(fileName);
            byte[] fileContent = new byte[fileInputStream.available()];
            fileInputStream.read(fileContent);
            fileInputStream.close();

            // Convertir el contenido del archivo en un String
            String content = new String(fileContent);

            // Extraer la imagen (Base64) y la descripción
            String[] parts = content.split("\n\nDescription:");
            String encodedImage = parts[0].replace("ImageBase64:", "").trim();
            String description = parts.length > 1 ? parts[1].trim() : "";

            // Decodificar la imagen de Base64
            byte[] decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            ivDetailImage.setImageBitmap(decodedImage);

            // Mostrar la descripción
            tvDetailDescription.setText(description);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al cargar la imagen y descripción", Toast.LENGTH_SHORT).show();
        }

        // Eliminar la imagen
        btnDeleteImage.setOnClickListener(v -> {
            deleteFile(fileName);
            Toast.makeText(this, "Imagen eliminada", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
