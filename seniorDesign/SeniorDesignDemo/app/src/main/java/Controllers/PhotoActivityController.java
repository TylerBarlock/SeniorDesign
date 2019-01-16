package Controllers;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Util.SharedPreferenceHelper;
import applicationname.companydomain.seniordesigndemo.PhotoActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PhotoActivityController {
    private final SharedPreferenceHelper mSharedPreferenceHelper;
    private Context mContext;
    private PhotoActivity p;
    private static final String TAG = "HttpURLGET";
    private List<String> list;
    private List<String> URIlist;

    public PhotoActivityController(Context context, PhotoActivity p) {
        mContext = context;
        mSharedPreferenceHelper = new SharedPreferenceHelper(context);
        this.p = p;
        list = new ArrayList<>();
        URIlist = new ArrayList<>();
    }

    public void listFoods(List<String> s){

        findRecipes(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    String jsonData = response.body().string();
                    JSONObject returnJSON = new JSONObject(jsonData);
                    JSONArray hints = (JSONArray) returnJSON.get("hints");
                    JSONObject food1 = hints.getJSONObject(0);
                    JSONObject morefood = food1.getJSONObject("food");
                    String food = morefood.getString("label");
                    String foodURI = morefood.getString("uri");
                    URIlist.add(foodURI);
                    list.add(food);

                    Log.e(TAG, "listFoods: " + list.size() );
                    p.showList(list, p);
                    /*
                    JSONObject morefood = food.getJSONObject("food");
                    String foodURI = morefood.getString("uri");
                    JSONArray measures = food.getJSONArray("measures");
                    //Log.e(TAG, "onResponse: " + foodURI);
                    if (response.isSuccessful()) {
                        for (int i = 0; i < measures.length(); i++) {
                            JSONObject m = measures.getJSONObject(i);
                            if (m.getString("label").equals("Ounce")) {
                                //Log.e(TAG, "onResponse: " + "FOUNDDDDDDDDDDDDDDDDDDDDDDDD");
                                //Log.e(TAG, "onResponse: " + m.getString("uri"));
                                String measureURI = m.getString("uri");
                            }
                        }
                    }
                    */
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, s);
    }


    public void findRecipes(Callback callback, List<String> s) {

        String APP_KEY = "283c09f15ef61bca03b9d9d1c444f0b0";
        String APP_ID = "7162d658";
        for (String food:s) {
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        String ingredients = ("" + "," + "").replaceAll("\\s", "");
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.edamam.com/api/food-database/parser").newBuilder();
            urlBuilder.addQueryParameter("ingr", food);
            urlBuilder.addQueryParameter("app_id", "7162d658");
            urlBuilder.addQueryParameter("app_key", "283c09f15ef61bca03b9d9d1c444f0b0");
            String url = urlBuilder.build().toString();
            //Log.e(TAG, url);

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(callback);
        }
    }

    public String getURI(int position){
        return URIlist.get(position);
    }

}
