package com.example.webf1movil1704.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.example.webf1movil1704.RegistrarLoteActivity;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "db_lotes";
    public static final String TABLE_NAME = "lotes";

    public DbHelper(@Nullable Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_NAME + " (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    fecha_siembra DATE,\n" +
                "    tipo_cultivo TEXT,\n" +
                "    numero_arboles INTEGER,\n" +
                "    area_sembrada REAL,\n" +
                "    ubicacion TEXT\n" +
                ");\n");

    }

    @Override
    public void onUpgrade( SQLiteDatabase sqLiteDatabase, int i, int i1){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

