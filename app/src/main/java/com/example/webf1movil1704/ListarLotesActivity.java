package com.example.webf1movil1704;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.webf1movil1704.DB.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class ListarLotesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewLotes;
    private LoteAdapter loteAdapter;
    private List<Lote> loteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_lotes);

        /* Título */
        this.setTitle("Listar lotes");

        recyclerViewLotes = findViewById(R.id.recyclerViewLotes);
        loteList = new ArrayList<>();

        cargarDatos();

        loteAdapter = new LoteAdapter(loteList);
        recyclerViewLotes.setAdapter(loteAdapter);
    }


    private void cargarDatos() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columnas = {"id", "ubicacion", "area_sembrada"};
        Cursor cursor = db.query(DbHelper.TABLE_NAME, columnas, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String ubicacion = cursor.getString(cursor.getColumnIndexOrThrow("ubicacion"));
                double areaSembrada = cursor.getDouble(cursor.getColumnIndexOrThrow("area_sembrada"));

                loteList.add(new Lote(id, ubicacion, areaSembrada));
            }
            cursor.close();
        } else {
            Log.d("ListarLotesActivity", "No se encontraron datos.");
        }
    }
}
