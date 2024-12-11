package com.example.webf1movil1704;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileInputStream;
import java.util.Arrays;

public class CgalleryActivity extends AppCompatActivity {
    private String[] archivos; // Sin "final" para permitir modificaciones
    RecyclerView rv1;
    EditText etFilterDate;
    Button btnApplyFilter;
    String selectedDate = ""; // Fecha seleccionada para el filtro

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cgallery);

        // Configuración de la barra superior
        this.setTitle("Galería de Fotos");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Inicialización de vistas
        etFilterDate = findViewById(R.id.etFilterDate);
        btnApplyFilter = findViewById(R.id.btnApplyFilter);
        rv1 = findViewById(R.id.rv1);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar lista de archivos
        archivos = fileList();
        Log.d("CgalleryActivity", "Archivos encontrados: " + Arrays.toString(archivos));

        // Configurar RecyclerView
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rv1.setLayoutManager(gridLayoutManager);
        rv1.setAdapter(new AdaptadorFotos(archivos));

        // Mostrar DatePickerDialog al hacer clic en el campo de fecha
        etFilterDate.setOnClickListener(v -> showDatePickerDialog());

        // Filtrar al hacer clic en el botón
        btnApplyFilter.setOnClickListener(v -> aplicarFiltroPorFecha());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            // Formato de fecha: YYYYMMDD
            selectedDate = String.format("%04d%02d%02d", year, month + 1, dayOfMonth);
            etFilterDate.setText(String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)); // Formato legible
        }, 2023, 0, 1); // Fecha por defecto
        datePickerDialog.show();
    }

    private void aplicarFiltroPorFecha() {
        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Seleccione una fecha", Toast.LENGTH_SHORT).show();
            return;
        }

        // Filtrar archivos por la fecha seleccionada
        String[] archivosFiltrados = Arrays.stream(archivos)
                .filter(nombre -> nombre.startsWith("IMG_" + selectedDate)) // Buscar archivos que coincidan con la fecha
                .toArray(String[]::new);

        // Actualizar el RecyclerView con los archivos filtrados
        rv1.setAdapter(new AdaptadorFotos(archivosFiltrados));

        if (archivosFiltrados.length == 0) {
            Toast.makeText(this, "No se encontraron fotos para esta fecha", Toast.LENGTH_SHORT).show();
        }
    }

    private class AdaptadorFotos extends RecyclerView.Adapter<AdaptadorFotos.AdaptadorFotosHolder> {
        private String[] archivos; // Evitar conflictos de referencia

        public AdaptadorFotos(String[] archivos) {
            this.archivos = archivos;
        }

        @NonNull
        @Override
        public AdaptadorFotosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AdaptadorFotosHolder(getLayoutInflater().inflate(R.layout.layout_foto, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptadorFotosHolder holder, int position) {
            holder.imprimir(position);
        }

        @Override
        public int getItemCount() {
            return archivos.length;
        }

        class AdaptadorFotosHolder extends RecyclerView.ViewHolder {
            ImageView iv1;
            TextView tv1;
            View deleteButton;

            public AdaptadorFotosHolder(@NonNull View itemView) {
                super(itemView);
                tv1 = itemView.findViewById(R.id.ivtext);
                iv1 = itemView.findViewById(R.id.ivfoto);
                deleteButton = itemView.findViewById(R.id.deleteButton);

                // Añadir un listener para abrir la imagen en detalle al hacer clic
                itemView.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        abrirImagenDetalle(position);
                    }
                });

                // Añadir un listener para eliminar la imagen
                deleteButton.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        eliminarImagen(position);
                    }
                });
            }

            public void imprimir(int position) {
                tv1.setText("Nombre foto: " + archivos[position]);
                Log.d("CgalleryActivity", "Archivo nombre: " + archivos[position]);

                try {
                    // Cargar la imagen
                    FileInputStream fileInputStream = openFileInput(archivos[position]);
                    Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                    iv1.setImageBitmap(bitmap);
                    fileInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("CgalleryActivity", "Error loading image: " + e.getMessage());
                }
            }
        }

        private void abrirImagenDetalle(int position) {
            String imageName = archivos[position];

            Intent intent = new Intent(CgalleryActivity.this, ImageDetailActivity.class);
            intent.putExtra("imageName", imageName);
            startActivity(intent);
        }

        private void eliminarImagen(int position) {
            String imageName = archivos[position];

            // Eliminar imagen
            deleteFile(imageName);

            // Actualizar la lista de archivos y notificar al adaptador
            archivos = fileList();
            notifyDataSetChanged();

            Toast.makeText(CgalleryActivity.this, "Imagen eliminada", Toast.LENGTH_SHORT).show();
        }
    }
}
