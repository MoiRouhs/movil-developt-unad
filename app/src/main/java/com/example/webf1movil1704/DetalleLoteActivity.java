package com.example.webf1movil1704;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.webf1movil1704.DB.DbHelper;

public class DetalleLoteActivity extends AppCompatActivity {

    private TextView textViewDetalle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_lote);

        textViewDetalle = findViewById(R.id.textViewDetalle);

        int loteId = getIntent().getIntExtra("id", -1);
        if (loteId != -1) {
            cargarDetalleLote(loteId);
        }
    }

    private void cargarDetalleLote(int id) {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DbHelper.TABLE_NAME + " WHERE id = ?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            String detalle = "ID: " + cursor.getInt(cursor.getColumnIndexOrThrow("id")) + "\n" +
                    "Fecha de siembra: " + cursor.getString(cursor.getColumnIndexOrThrow("fecha_siembra")) + "\n" +
                    "Tipo de cultivo: " + cursor.getString(cursor.getColumnIndexOrThrow("tipo_cultivo")) + "\n" +
                    "Número de árboles: " + cursor.getInt(cursor.getColumnIndexOrThrow("numero_arboles")) + "\n" +
                    "Área sembrada: " + cursor.getDouble(cursor.getColumnIndexOrThrow("area_sembrada")) + " ha\n" +
                    "Ubicación: " + cursor.getString(cursor.getColumnIndexOrThrow("ubicacion"));

            textViewDetalle.setText(detalle);
        }
        cursor.close();
    }
}
