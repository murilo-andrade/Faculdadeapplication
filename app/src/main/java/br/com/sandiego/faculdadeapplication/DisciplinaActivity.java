package br.com.sandiego.faculdadeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DisciplinaActivity extends AppCompatActivity {
    protected EditText editTextDisciplin;
    protected DisciplinaValue disciplinaSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disciplina);
        Button button = findViewById(R.id.salvar);
        this.editTextDisciplin = (EditText) findViewById(R.id.disciplina);
        this.editTextDisciplin.setText("Nova Disciplina");
        Intent intent = getIntent();
        disciplinaSelecionada = (DisciplinaValue) intent.getSerializableExtra("disciplinaSelecionada");
        if(disciplinaSelecionada!=null){
            button.setText("Alterar");
            editTextDisciplin.setText(disciplinaSelecionada.getNome());
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisciplinaDAO dao = new DisciplinaDAO(DisciplinaActivity.this);
                if (disciplinaSelecionada==null) {
                    DisciplinaValue disciplinaValue = new DisciplinaValue();
                    disciplinaValue.setNome(editTextDisciplin.getText().toString());
                    dao.salvar(disciplinaValue);
                }else{
                    disciplinaSelecionada.setNome(editTextDisciplin.getText().toString());
                    dao.alterar(disciplinaSelecionada);
                }
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menudisciplina, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_new) {
            Intent intent = new Intent(this, DisciplinaActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}