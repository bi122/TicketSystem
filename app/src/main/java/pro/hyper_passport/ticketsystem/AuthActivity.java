package pro.hyper_passport.ticketsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.NoCopySpan;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mihail on 02.07.2015.
 */
public class AuthActivity extends AppCompatActivity {
    public EditText login;
    public EditText pass;
    private ProgressDialog dialog;
    public TableLayout tl;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public String getLogin() {
        return login.getText().toString();
    }
    public String getPass(){
        return pass.getText().toString();
    }

    void dosomething(){
        tl.findViewById(R.id.error_row).setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_layout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Button btn = (Button) findViewById(R.id.btn_login);
        tl = (TableLayout)findViewById(R.id.tbl_layout_auth);
        tl.findViewById(R.id.error_row).setVisibility(View.GONE);
        login = (EditText) findViewById(R.id.eTxt_user);
        pass = (EditText) findViewById(R.id.eTxt_pass);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //тут указываем куда будем конектится, для примера я привел удаленный хост на котором лежит json с ответом об аутентификации
                //new AuthRequestTask().execute("http://hyper-passport.pro/auth/externalLogin/");
                new AuthRequestTask().execute("http://sa11977-21669.smrtp.ru/");
            }
        });
    }

    class AuthRequestTask extends AsyncTask<String, String, String> {
        public boolean auth_result = false;
        public  String  TAG = "AuthRequestTask";
        @Override
        protected String doInBackground(String... params) {
            try {
                Log.i(TAG, "in do background");
                //создаем запрос на сервер
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //будем передавать два параметра
                StringBuilder sb = new StringBuilder();
                try {
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    urlConnection.setChunkedStreamingMode(0);

                    OutputStreamWriter out = new OutputStreamWriter (urlConnection.getOutputStream());

                    //out.write("email=" + getLogin() + "&password=" + getPass().toString());
                    out.write("login=" + getLogin() + "&pass=" + getPass().toString());

                    out.close();
                    if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        InputStreamReader isr = new InputStreamReader(in);
                        BufferedReader br = new BufferedReader(isr);
                        String read = br.readLine();
                        //hyper-passport.pro/auth/externalLogin?email=admin@admin.org&password=111222
                        //http://hyper-passport.pro/support/categoriesJson получаем список категории
                        //hyper-passport.pro/support/itemsListJson/11136  получаем проблемы по теме категории 11136, последее это id категории тикета‏
                        while(read != null) {
                            //System.out.println(read);
                            sb.append(read);
                            read = br.readLine();
                        }
                        Log.i(TAG, br.toString());
                    }
                    else {
                        Log.i(TAG, String.valueOf(urlConnection.getResponseCode()));
                    }

                    getListProblems(sb);
                }
                finally {
                    Log.i(TAG, "in fanally");
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                System.out.println("Exp=" + e);
            }
            return null;
        }

        private void getListProblems(StringBuilder sb) throws JSONException {
            if (new ReaderJSON(sb.toString()).checkAuth()){
                //new ListProblemRequstTask().execute("http://hyper-passport.pro/support/categoriesJson");
                new ListProblemRequstTask().execute("http://www.test-client.smrtp.ru/listThemes.php");
                auth_result = true;
            }
            else{
                //new ListProblemRequstTask().execute("http://hyper-passport.pro/support/categoriesJson");
                auth_result = false;
            }
        }

        @Override
        protected void onPostExecute(String result) {

            dialog.dismiss();
            super.onPostExecute(result);
            if (auth_result == false){
                dosomething();
            }

            //new ListProblemRequstTask().execute("http://www.test-client.smrtp.ru/listThemes.php");
        }

        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(AuthActivity.this);
            dialog.setMessage("Загружаюсь...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
        }
    }

    class ListProblemRequstTask extends AsyncTask<String, String, String>{
        private String TAG = "ListProblemRequstTask";
        @Override
        protected String doInBackground(String... params) {
            try {
                Log.i(TAG, "in do background");
                //создаем запрос на сервер
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //будем передавать два параметра
                StringBuilder sb = new StringBuilder();
                try {
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoInput(true);
                    urlConnection.setChunkedStreamingMode(0);
                    if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        InputStreamReader isr = new InputStreamReader(in);
                        BufferedReader br = new BufferedReader(isr);
                        String read = br.readLine();
                        while(read != null) {
                            //System.out.println(read);
                            sb.append(read);
                            read = br.readLine();
                        }
                        Log.i(TAG, br.toString());
                    }
                    else {
                        //TODO реализовать обработку
                    }
                }
                finally {
                    Log.i(TAG, "in fanally");
                    urlConnection.disconnect();
                }

                //TODO сюда вставить разбор json auth и старт MainActivity
                //посылаем на вторую активность полученные параметры
                Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                //то что куда мы будем передавать и что, putExtra(куда, что);
                Log.i("output", sb.toString());
                intent.putExtra(MainActivity.JsonURL, sb.toString());
                startActivity(intent);

            } catch (Exception e) {
                System.out.println("Exp=" + e);
            }
            return null;
        }
    }

}
