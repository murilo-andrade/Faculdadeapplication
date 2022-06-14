package br.com.sandiego.faculdadeapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;

public class DisciplinaDAO extends SQLiteOpenHelper {
    private static final String DATABASE = "BancoDisciplinas";
    private static final int VERSAO = 1;
    public DisciplinaDAO(Context context) {
        super(context, DATABASE, null, VERSAO);
    }
    public void onCreate(@NonNull SQLiteDatabase db) {
        String ddl ="CREATE TABLE Disciplina (id INTEGER PRIMARY KEY,"
                + " nome TEXT UNIQUE NOT NULL);";
        db.execSQL(ddl);
    }
    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int velha, int nova) {
        String ddl ="DROP TABLE IF EXISTS Disciplina";
        db.execSQL(ddl);
        onCreate(db);
    }

    public void salvar(@NonNull DisciplinaValue disciplinaValue) {
        ContentValues values = new ContentValues();
        values.put("nome", disciplinaValue.getNome());
        getWritableDatabase().insert("Disciplina", null, values );
    }
    public List getLista(){
        List<DisciplinaValue> disciplinas = new LinkedList<DisciplinaValue>();
        String query = "SELECT * FROM " + "Disciplina order by nome";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        DisciplinaValue disciplina;
        if (cursor.moveToFirst()) {
            do {
                disciplina = new DisciplinaValue();
                disciplina.setId(Long.parseLong(cursor.getString(0)));
                disciplina.setNome(cursor.getString(1));
                disciplinas.add(disciplina);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return disciplinas;
    }

    public void deletar(@NonNull DisciplinaValue disciplinaValue) {
        String[] args = { disciplinaValue.getId().toString() };
        getWritableDatabase().delete("Disciplina", "id=?", args);
    }

    public void dropAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        String ddl ="DROP TABLE IF EXISTS Disciplina";
        db.execSQL(ddl);
        onCreate(db);
    }

    public void alterar(@NonNull DisciplinaValue disciplinaValue) {
        ContentValues values = new ContentValues();
        values.put("nome", disciplinaValue.getNome());
        getWritableDatabase().update("Disciplina", values,
                "id=?", new String[]{disciplinaValue.getId().toString()});
    }
}