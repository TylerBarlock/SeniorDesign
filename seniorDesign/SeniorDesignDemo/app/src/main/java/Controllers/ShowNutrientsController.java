package Controllers;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import Util.SharedPreferenceHelper;
import applicationname.companydomain.seniordesigndemo.ShowNutrientsActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ShowNutrientsController {
    private final SharedPreferenceHelper mSharedPreferenceHelper;
    private Context mContext;
    private List<String> nuts;
    private String TAG = "hey";
    private ShowNutrientsActivity mShowNutrientsActivity;
    public ShowNutrientsController(Context context,ShowNutrientsActivity sna) {
        mContext = context;
        mSharedPreferenceHelper = new SharedPreferenceHelper(context);
        nuts = new ArrayList<>();
        mShowNutrientsActivity = sna;
    }

    public void getNutrients(String URI) throws JSONException {
        findNutrients(URI, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                Log.e(TAG, "onNotResponse: " + jsonData);
                try {
                    JSONObject returnJSON = new JSONObject(jsonData);
                    JSONObject nutrients = returnJSON.getJSONObject("totalNutrients");
                    JSONObject cal = nutrients.getJSONObject("ENERC_KCAL");
                    double calorie = cal.getDouble("quantity");
                    JSONObject CHOCDF = nutrients.getJSONObject("CHOCDF");
                    double carb = CHOCDF.getDouble("quantity");
                    JSONObject s= nutrients.getJSONObject("SUGAR");
                    double sugar = s.getDouble("quantity");
                    calorie = Math.floor(calorie*100)/100;
                    carb = Math.floor(carb*100)/100;
                    sugar = Math.floor(sugar*100)/100;
                    String calories = String.valueOf(calorie);
                    String carbs = String.valueOf(carb);
                    String sugars = String.valueOf(sugar);
                    nuts.add("calories per oz: " + calories);
                    nuts.add("carbs per oz: " + carbs);
                    nuts.add("sugars per oz: " + sugars);
                    mShowNutrientsActivity.showNutrients(nuts, mShowNutrientsActivity);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
}


    public void findNutrients(String foodURI, Callback callback) throws JSONException {
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        JSONObject obj = new JSONObject()
                .put("quantity", 1)
                .put("measureURI","http://www.edamam.com/ontologies/edamam.owl#Measure_ounce")
                .put("foodURI", foodURI);

        JSONArray ja = new JSONArray()
                .put(obj);

        String json = new JSONObject()
                .put("ingredients", ja).toString();
        Log.e(TAG, "findNutrients: " + json );
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
/*
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addPart("ingredients", )
                .build();
                */
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.edamam.com/api/food-database/nutrients").newBuilder();
        urlBuilder.addQueryParameter("app_id","7162d658");
        urlBuilder.addQueryParameter("app_key","283c09f15ef61bca03b9d9d1c444f0b0");
        String url = urlBuilder.build().toString();
        Log.e(TAG,url);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

}
