package br.com.sandiego.faculdadeapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //protected ListView lista;
    protected DisciplinaValue disciplinaValue;
    protected ArrayAdapter<DisciplinaValue> adapter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int layout = android.R.layout.simple_list_item_1;

        DisciplinaDAO dao = new DisciplinaDAO(this);
        ArrayList<DisciplinaValue> disciplinas = new ArrayList(dao.getLista());
        dao.close();
        ArrayAdapter<DisciplinaValue> adapter =
                new ArrayAdapter<DisciplinaValue>(this,layout, disciplinas);


        ListView lista = findViewById(R.id.listView);
        lista.setAdapter(adapter);

        lista.setOnCreateContextMenuListener(this);
        registerForContextMenu(lista);

/*
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View view,
                                    int posicao, long id) {
                Toast.makeText(MainActivity.this, "Clicou " + posicao,
                        Toast.LENGTH_SHORT).show();
            }
        });

        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapter,
                                           View view, int posicao, long id) {
                Toast.makeText(MainActivity.this, adapter.getItemAtPosition(posicao).toString(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        DisciplinaDAO dao = new DisciplinaDAO(this);
        ArrayList<DisciplinaValue> disciplinas = new ArrayList(dao.getLista());
        dao.close();
        int layout = android.R.layout.simple_list_item_1;
        ArrayAdapter<DisciplinaValue> adapter =
                (ArrayAdapter<DisciplinaValue>) new ArrayAdapter<>(this,layout, disciplinas);
        ListView lista = findViewById(R.id.listView);
        lista.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menudisciplina, menu);
        return true;
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_disciplina, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull final MenuItem item) {
        int layout = android.R.layout.simple_list_item_1;
        DisciplinaDAO dao = new DisciplinaDAO(this);
        ArrayList<DisciplinaValue> disciplinas = new ArrayList(dao.getLista());
        dao.close();
        ArrayAdapter<DisciplinaValue> adapter =
                new ArrayAdapter<DisciplinaValue>(this,layout, disciplinas);

        disciplinaValue = (DisciplinaValue)
                adapter.getItem(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
        int id = item.getItemId();
        if (id == R.id.action_new) {
            Intent intent = new Intent(this, DisciplinaActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_update) {
            Intent intent = new Intent(this, DisciplinaActivity.class);
            intent.putExtra("disciplinaSelecionada", disciplinaValue);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_delete) {
//            DisciplinaDAO dao = new DisciplinaDAO(MainActivity.this);
            dao.deletar(disciplinaValue);
            dao.close();
            this.onResume();
            return true;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_new) {
            Intent intent = new Intent(this, DisciplinaActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int codigo,int reultado,Intent it) {
        super.onActivityResult(codigo, reultado, it);
        this.adapter.notifyDataSetChanged();
    }
}