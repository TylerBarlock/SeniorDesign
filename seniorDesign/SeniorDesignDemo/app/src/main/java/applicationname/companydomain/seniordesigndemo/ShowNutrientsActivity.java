package applicationname.companydomain.seniordesigndemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import Controllers.ShowNutrientsController;

public class ShowNutrientsActivity extends AppCompatActivity {
    String TAG = "hey";
    private ArrayAdapter<String> adapter;
    private ListView nutrientsListView;
    private ShowNutrientsController mShowNutrientsController;
    private List<String> nutrientsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_nutrients);
        nutrientsList = new ArrayList<>();
        Bundle intentExtras = getIntent().getExtras();
        nutrientsListView = (ListView) findViewById(R.id.nutrientsList);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, nutrientsList);
        nutrientsListView.setAdapter(adapter);
        String URI = (String) intentExtras.get("URI");
        mShowNutrientsController = new ShowNutrientsController(this, this);
        try {
            mShowNutrientsController.getNutrients(URI);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void showNutrients(final List<String> s, final ShowNutrientsActivity sna){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Stuff that updates the UI
                adapter = new ArrayAdapter<String>(sna, android.R.layout.simple_expandable_list_item_1, s);
                nutrientsListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
