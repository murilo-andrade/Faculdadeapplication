package br.com.sandiego.faculdadeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SplashScreen extends AppCompatActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    private TextView textView=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        // ActionBar actionBar = this.getActionBar();
        // actionBar.hide();
        textView = (TextView) this.findViewById(R.id.textView);
        textView.setText("Aguarde Carregando...");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new JSONParse().execute();
            }
        }, SPLASH_TIME_OUT);
    }

    public class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog= new ProgressDialog(SplashScreen.this);
            pDialog.setMessage("Obtendo Dados");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject json;
            DisciplinaDAO disciplinaDAO = new DisciplinaDAO(SplashScreen.this);
            disciplinaDAO.dropAll();
            JSONArray link;
            json = Json();
            try {
                // Getting JSON Array
                link = json.getJSONArray("Lista");
                Log.e("DEBUG","Resposta Lista: "+link);
                Log.e("DEBUG","Tamanho array: "+link.length());
                for (int i = 0; i < link.length(); i++) {
                    // String c = (String) link.get(i);
                    JSONObject c = link.getJSONObject(i);
                    DisciplinaValue disciplinaValue = new DisciplinaValue();
                    //disciplinaValue.setNome(c);
                    disciplinaValue.setNome(c.getString("disciplina"));
                    disciplinaDAO.salvar(disciplinaValue);
                    disciplinaDAO.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return json;
        }

        protected void onPostExecute(JSONObject json) {
            try {
                pDialog.dismiss();
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                try {
                    finalize();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                // MainActivity.this.onResume();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private JSONObject Json() {
            JSONObject json;
            String resp;
            try {
                // Create connection to send GCM Message request.
                // URL url1 = new URL("https://www.npai.uneb.br/"+"index2.php");
                URL url1 = new URL("https://www.sandiego.com.br/"+"index-json.php");
                HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                // Read GCM response.
                InputStream inputStream = conn.getInputStream();
                resp = IOUtils.toString(inputStream);
                // Log.e("DEBUG","Resposta do servidor: "+resp);
                json = new JSONObject(resp);
                // Log.e("DEBUG","Resposta JSON: "+json);
                return json;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }



}