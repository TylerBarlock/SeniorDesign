package applicationname.companydomain.seniordesigndemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;

import java.util.ArrayList;
import java.util.List;

import Controllers.UserInformationController;

public class UserInformationActivity extends AppCompatActivity implements WheelPicker.OnItemSelectedListener, View.OnClickListener {
    private String fname;
    private String lname;
    String LOG = "log";
    String TAG = "log";
    private WheelPicker wheelLeft;
    private WheelPicker wheelRight;
    private TextView heightf;
    private TextView heighti;
    private EditText inputWeight;
    private EditText inputAge;
    private CheckBox checkSmoker;
    private Button buttonSelect;
    private Button buttonDone;
    private String heightInches;
    private String heightFeet;
    private UserInformationController mUserInfoController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Bundle intentExtras = getIntent().getExtras();
        fname = (String) intentExtras.get("fname");
        lname = (String) intentExtras.get("lname");
        Log.e(LOG, "onCreate: " + fname);
        wheelLeft = (WheelPicker) findViewById(R.id.main_wheel_left);
        List<Integer> dataLeft = new ArrayList<>();
        for (int i = 1; i < 9; i++)
            dataLeft.add(i);
        wheelLeft.setData(dataLeft);
        wheelRight = (WheelPicker) findViewById(R.id.main_wheel_right);
        List<Integer> dataRight = new ArrayList<>();
        for (int i = 1; i < 12; i++)
            dataRight.add(i);
        wheelRight.setData(dataRight);
        wheelLeft.setOnItemSelectedListener(this);
        wheelRight.setOnItemSelectedListener(this);
        heightf = (TextView) findViewById(R.id.text_hf);
        heighti = (TextView) findViewById(R.id.text_hi);
        mUserInfoController = new UserInformationController(this);
        buttonDone = (Button) findViewById(R.id.button_done);
        inputWeight = (EditText)findViewById(R.id.input_weight);
        inputAge = (EditText)findViewById(R.id.input_age);
        checkSmoker = (CheckBox)findViewById(R.id.check_smoker);
        heightf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heightf.setVisibility(View.GONE);
                heighti.setVisibility(View.GONE);
                wheelLeft.setVisibility(View.VISIBLE);
                wheelRight.setVisibility(View.VISIBLE);
                buttonSelect.setVisibility(View.VISIBLE);
            }
        });
        heighti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heightf.setVisibility(View.GONE);
                heighti.setVisibility(View.GONE);
                wheelLeft.setVisibility(View.VISIBLE);
                wheelRight.setVisibility(View.VISIBLE);
                buttonSelect.setVisibility(View.VISIBLE);
            }
        });
        buttonSelect = (Button) findViewById(R.id.selectHeight);
        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heightf.setVisibility(View.VISIBLE);
                heighti.setVisibility(View.VISIBLE);
                wheelLeft.setVisibility(View.GONE);
                wheelRight.setVisibility(View.GONE);
                buttonSelect.setVisibility(View.GONE);
                heightf.setText(heightFeet);
                heighti.setText(heightInches);
            }
        });
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserInfoController.createUser(fname, lname,Integer.parseInt(String.valueOf(inputWeight.getText())),Integer.parseInt(String.valueOf(heightf.getText())),Integer.parseInt(String.valueOf(heighti.getText())),Integer.parseInt(String.valueOf(inputAge.getText())),checkSmoker.isChecked());
                nextScreen();
            }
        });
    }

    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {
        switch (picker.getId()) {
            case R.id.main_wheel_left:
                heightFeet = data.toString();
                break;
            case R.id.main_wheel_right:
                heightInches = data.toString();
                break;
        }
    }

    @Override
    public void onClick(View v) {

    }

    public void nextScreen(){
        Intent loadPhotoScreen = new Intent(this, PhotoActivity.class);
        startActivity(loadPhotoScreen);
    }
}
