package pro.hyper_passport.ticketsystem;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static String JsonURL;
    private static ArrayList<HashMap<String, Object>> myBooks;
    private static final String ELEMENT_NAME = "element_name";
    private static final String ID = "id";
    public ListView listView;
    public SupportCount supportCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_themes_problem_layout);
        listView = (ListView) findViewById(R.id.listThemesProblem);
        myBooks = new ArrayList<HashMap<String, Object>>();
        //принимаем параметр который мы послылали в manActivity
        Bundle extras = getIntent().getExtras();
        //превращаем в тип стринг для парсинга
        String json = extras.getString(JsonURL);
        //передаем в метод парсинга
        JSONURL(json);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {

                String str  = ((TextView) itemClicked.findViewById(R.id.text1)).getText().toString();
                String id_of_theme = myBooks.get(position).get("id").toString();
                Toast.makeText(getApplicationContext(),
                        id_of_theme,
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ListProblemActivity.class);
                startActivity(intent);
            }
        });
        supportCount = new SupportCount();
        supportCount.setValueThemes(myBooks.size());
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /** @param result */
    public void JSONURL(String result) {

        try {
            //создали читателя json объектов и отдали ему строку - result
            JSONObject json = new JSONObject(result);
            //дальше находим вход в наш json им является ключевое слово data
            JSONArray urls = json.getJSONArray("categories");
            //проходим циклом по всем нашим параметрам
            for (int i = 0; i < urls.length(); i++) {
                HashMap<String, Object> hm;
                hm = new HashMap<String, Object>();
                //читаем что в себе хранит параметр firstname

                hm.put(ELEMENT_NAME, urls.getJSONObject(i).getString("Element_name").toString());
                //потом supportCount используется при генерации заголовков SplitTabsView

                supportCount.setNameThemes(urls.getJSONObject(i).getString("Element_name").toString());
                //читаем что в себе хранит параметр lastname
                hm.put(ID, urls.getJSONObject(i).getString("Element_id").toString());
                myBooks.add(hm);
                //дальше добавляем полученные параметры в наш адаптер
                SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, myBooks, R.layout.element_group_list,
                        new String[] { ELEMENT_NAME, }, new int[] { R.id.text1 });
                //выводим в листвью
                Log.i(TAG," creating adapter");
                listView.setAdapter(adapter);
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing data " + e.toString());
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_myAsked) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
