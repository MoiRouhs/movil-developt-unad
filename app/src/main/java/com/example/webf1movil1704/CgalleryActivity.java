package com.example.webf1movil1704;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
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
    String[] archivos;
    RecyclerView rv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cgallery);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.setTitle("Galería de Fotos");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        archivos = fileList();
        Log.d("CgalleryActivity", "Archivos encontrados: " + Arrays.toString(archivos));

        rv1 = findViewById(R.id.rv1);

        // Configurar el RecyclerView con GridLayoutManager para mostrar las imágenes en una cuadrícula de 3 columnas
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rv1.setLayoutManager(gridLayoutManager);

        // Configurar el adaptador para el RecyclerView
        rv1.setAdapter(new AdaptadorFotos());
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

    private class AdaptadorFotos extends RecyclerView.Adapter<AdaptadorFotos.AdaptadorFotosHolder> {

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
            TextView tv1, tvDesc;
            View deleteButton;

            public AdaptadorFotosHolder(@NonNull View itemView) {
                super(itemView);
                tv1 = itemView.findViewById(R.id.ivtext);
                iv1 = itemView.findViewById(R.id.ivfoto);
                tvDesc = itemView.findViewById(R.id.ivDesc);
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

                    // Cargar la descripción
                    String descFileName = archivos[position].replace(".jpg", "_desc.txt");
                    FileInputStream descStream = openFileInput(descFileName);
                    byte[] descBytes = new byte[descStream.available()];
                    descStream.read(descBytes);
                    descStream.close();
                    tvDesc.setText(new String(descBytes));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("CgalleryActivity", "Error loading image: " + e.getMessage());
                }
            }
        }

        private void abrirImagenDetalle(int position) {
            String imageName = archivos[position];
            String descriptionFileName = imageName.replace(".jpg", "_desc.txt");

            Intent intent = new Intent(CgalleryActivity.this, ImageDetailActivity.class);
            intent.putExtra("imageName", imageName);
            intent.putExtra("descriptionFileName", descriptionFileName);
            startActivity(intent);
        }

        private void eliminarImagen(int position) {
            String imageName = archivos[position];
            String descriptionFileName = imageName.replace(".jpg", "_desc.txt");

            // Eliminar imagen y descripción
            deleteFile(imageName);
            deleteFile(descriptionFileName);

            // Actualizar la lista de archivos y notificar al adaptador
            archivos = fileList();
            notifyDataSetChanged();

            Toast.makeText(CgalleryActivity.this, "Imagen eliminada", Toast.LENGTH_SHORT).show();
        }
    }
}
