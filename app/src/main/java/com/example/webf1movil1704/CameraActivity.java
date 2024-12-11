package com.example.webf1movil1704;

import static java.lang.String.format;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileOutputStream;
import java.util.Date;
import android.widget.EditText;

public class CameraActivity extends AppCompatActivity {
    ImageView iv1;
    EditText editTextDescription;  // Campo para la descripción
    final int CAPTURE_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_camera);

        iv1 = findViewById(R.id.iv1);
        editTextDescription = findViewById(R.id.editTextDescription);  // Inicializar el EditText
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        this.setTitle("Camera");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void takePhoto(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bitmap1 = (Bitmap) extras.get("data");
            iv1.setImageBitmap(bitmap1);

            String description = editTextDescription.getText().toString();  // Obtener la descripción

            try {
                // Nombre del archivo combinado
                String fileName = createNameFile();
                FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);

                // Guardar imagen en binario
                bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                // Escribir un delimitador único
                fos.write("\n---DESCRIPTION---\n".getBytes());

                // Guardar la descripción
                fos.write(description.getBytes());
                fos.close();

                // Limpiar el cuadro de texto
                editTextDescription.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private String createNameFile() {
        String date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return "IMG_" + date + ".data";
    }

    private String createDescriptionFileName() {
        String date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return "IMG_" + date + "_desc.txt";
    }

    public void verTodo(View v) {
        Intent intent = new Intent(this, CgalleryActivity.class);
        startActivity(intent);
    }
}
