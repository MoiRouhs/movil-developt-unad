package com.example.webf1movil1704;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.webf1movil1704.DB.DbHelper;

public class RegistrarLoteActivity extends AppCompatActivity {

    private Button btnEnviar;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar_lote);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        /* Título */
        this.setTitle("Registrar Lote");

        /* Back Button */
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        btnEnviar = findViewById(R.id.enviar_registro_lote);
        /* Crear DB SQLite */
        DbHelper dbHelper = new DbHelper(RegistrarLoteActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db != null) {
            Toast.makeText(RegistrarLoteActivity.this, "Base de datos creada", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(RegistrarLoteActivity.this, "Error al crear la base de datos", Toast.LENGTH_SHORT).show();
        }

        btnEnviar.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              EditText fechaSiembra;
              EditText tipoCultivo;
              EditText numeroArboles;
              EditText areaSembrada;
              EditText ubicacion;


              fechaSiembra = (EditText)findViewById(R.id.editTextDate);
              tipoCultivo = (EditText)findViewById(R.id.tipo_cultivo);
              numeroArboles = (EditText)findViewById(R.id.numero_arboles);
              areaSembrada = (EditText)findViewById(R.id.area_sembrada);
              ubicacion = (EditText)findViewById(R.id.ubicacion);


              // Crear un objeto ContentValues para insertar datos
              ContentValues values = new ContentValues();
              values.put("fecha_siembra", String.valueOf(fechaSiembra.getText()));
              values.put("tipo_cultivo", String.valueOf(tipoCultivo.getText()));
              values.put("numero_arboles", Integer.parseInt(String.valueOf(numeroArboles.getText())));
              values.put("area_sembrada", Double.parseDouble(String.valueOf(areaSembrada.getText())));  // en hectáreas
              values.put("ubicacion", String.valueOf(ubicacion.getText()));

              if(existed(values)) {
                  // Insertar el registro en la base de datos
                  long newRowId = db.insert(DbHelper.TABLE_NAME, null, values);

                  // Verificar si se insertó correctamente
                  if (newRowId != -1) {
                      // Registro insertado correctamente
                      Toast.makeText(RegistrarLoteActivity.this, "Registro insertado correctamente. ID: " + newRowId, Toast.LENGTH_SHORT).show();
                      System.out.println("Registro insertado correctamente. ID: " + newRowId);
                      fechaSiembra.setText("");
                      tipoCultivo.setText("");
                      numeroArboles.setText("");
                      areaSembrada.setText("");
                      ubicacion.setText("");
                  } else {
                      // Error al insertar el registro
                      Toast.makeText(RegistrarLoteActivity.this, "Error al insertar el registro.", Toast.LENGTH_SHORT).show();
                      System.out.println("Error al insertar el registro.");
                  }
              }else{
                  Toast.makeText(RegistrarLoteActivity.this, "El registro ya existe", Toast.LENGTH_SHORT).show();

              }
          }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true ;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean existed(Object obj) {
        DbHelper dbHelper = new DbHelper(RegistrarLoteActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String query = "SELECT 1 FROM lotes WHERE fecha_siembra = ? AND tipo_cultivo = ? AND numero_arboles = ? AND area_sembrada = ? AND ubicacion = ? LIMIT 1;";
        Log.d("test", "Esto es un test");
        Log.d("obj", ((ContentValues) obj).getAsString("fecha_siembra"));
        Cursor cursor = db.rawQuery(query, new String[]{
                ((ContentValues) obj).getAsString("fecha_siembra"),
                ((ContentValues) obj).getAsString("tipo_cultivo"),
                String.valueOf(((ContentValues) obj).getAsInteger("numero_arboles")),
                String.valueOf(((ContentValues) obj).getAsDouble("area_sembrada")),
                ((ContentValues) obj).getAsString("ubicacion")
        });
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        Log.d("existed", String.valueOf(exists));
        return !exists;
    }

}