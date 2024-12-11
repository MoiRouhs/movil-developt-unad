package com.example.webf1movil1704;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.util.Arrays;

public class ImageDetailActivity extends AppCompatActivity {
    ImageView ivImageDetail;
    TextView tvDescriptionDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        ImageView ivImageDetail = findViewById(R.id.ivImageDetail);
        TextView tvDescriptionDetail = findViewById(R.id.tvDescriptionDetail);

        String imageName = getIntent().getStringExtra("imageName");

        try {
            // Leer el archivo completo
            FileInputStream fis = openFileInput(imageName);
            byte[] fileBytes = new byte[fis.available()];
            fis.read(fileBytes);
            fis.close();

            // Convertir el archivo a String para buscar el delimitador
            String fileContent = new String(fileBytes);
            String delimiter = "\n---DESCRIPTION---\n";

            // Separar la imagen y la descripción
            int delimiterIndex = fileContent.indexOf(delimiter);
            if (delimiterIndex != -1) {
                // Obtener la parte binaria de la imagen
                byte[] imageBytes = Arrays.copyOfRange(fileBytes, 0, delimiterIndex);

                // Obtener la descripción como texto
                String description = fileContent.substring(delimiterIndex + delimiter.length());

                // Mostrar la imagen
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                ivImageDetail.setImageBitmap(bitmap);

                // Mostrar la descripción
                tvDescriptionDetail.setText(description);
            } else {
                tvDescriptionDetail.setText("Error: No se pudo leer la descripción.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            tvDescriptionDetail.setText("Error al cargar los datos.");
        }
    }

    private void loadImageAndDescription(String fileName) {
        try {
            // Leer todo el contenido del archivo
            FileInputStream fis = openFileInput(fileName);
            byte[] fileData = fis.readAllBytes();
            fis.close();

            // Buscar el delimitador para separar la imagen y la descripción
            String delimiter = "\n---DESCRIPTION---\n";
            int delimiterIndex = new String(fileData).indexOf(delimiter);

            if (delimiterIndex != -1) {
                // Extraer la imagen
                byte[] imageBytes = Arrays.copyOfRange(fileData, 0, delimiterIndex);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                ivImageDetail.setImageBitmap(bitmap);

                // Extraer la descripción
                String description = new String(fileData, delimiterIndex + delimiter.length(), fileData.length - delimiterIndex - delimiter.length());
                tvDescriptionDetail.setText(description);
            } else {
                tvDescriptionDetail.setText("No se encontró descripción.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            tvDescriptionDetail.setText("Error al cargar el archivo.");
        }
    }
}
