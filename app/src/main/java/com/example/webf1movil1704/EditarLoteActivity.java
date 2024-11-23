package com.example.webf1movil1704;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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

public class EditarLoteActivity extends AppCompatActivity {

    private Button btnActualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_lote);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int loteId = getIntent().getIntExtra("id", -1);
        /* Título */
        this.setTitle("Editar lote ID: " + loteId );

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if(loteId != -1){
            cargarDetalleLote(loteId);
        };
        btnActualizar = findViewById(R.id.actualizar_registro_lote);
        btnActualizar.setOnClickListener(view -> guardarCambios(loteId));
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

    private void cargarDetalleLote(int id) {
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

        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DbHelper.TABLE_NAME + " WHERE id = ?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            ubicacion.setText(cursor.getString(cursor.getColumnIndexOrThrow("ubicacion")));
            tipoCultivo.setText(cursor.getString(cursor.getColumnIndexOrThrow("tipo_cultivo")));
            fechaSiembra.setText(cursor.getString(cursor.getColumnIndexOrThrow("fecha_siembra")));
            numeroArboles.setText(cursor.getString(cursor.getColumnIndexOrThrow("numero_arboles")));
            areaSembrada.setText(cursor.getString(cursor.getColumnIndexOrThrow("area_sembrada")));
        }
        cursor.close();
    }

    private void guardarCambios(int id) {
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

        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        int actualizar = db.update(DbHelper.TABLE_NAME, values, "id = ?", new String[]{String.valueOf(id)});
        if (actualizar > 0) {
            Toast.makeText(EditarLoteActivity.this, "Cambios guardados correctamente", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EditarLoteActivity.this, DetalleLoteActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(EditarLoteActivity.this, "Error al guardar los cambios", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
}