package Controllers;

import android.content.Context;

import Util.SharedPreferenceHelper;

public class LoginActivityController {
    private final SharedPreferenceHelper mSharedPreferenceHelper;
    private Context mContext;

    public LoginActivityController(Context context) {
        mContext = context;
        mSharedPreferenceHelper = new SharedPreferenceHelper(context);
    }

}
