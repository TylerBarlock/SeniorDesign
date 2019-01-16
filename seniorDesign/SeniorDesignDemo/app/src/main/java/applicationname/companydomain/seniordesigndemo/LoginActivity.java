package applicationname.companydomain.seniordesigndemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Controllers.LoginActivityController;

public class LoginActivity extends AppCompatActivity {
    private LoginActivityController mLoginActivityController;
    private Button mLoginButton;
    private EditText mFnameEdit;
    private EditText mLnameEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginButton = (Button) findViewById(R.id.createUser);
        mFnameEdit = (EditText) findViewById(R.id.input_fname);
        mLnameEdit = (EditText) findViewById(R.id.input_lname);
        mLoginActivityController = new LoginActivityController(this);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = String.valueOf(mFnameEdit.getText());
                String lname = String.valueOf(mLnameEdit.getText());
                if(fname.length() == 0 || lname.length()==0)
                {
                    showError();
                    return;
                }
                nextScreen(fname, lname);
            }
        });
    }

    public void nextScreen(String fname, String lname){
        Intent loadUserInfoScreen = new Intent(this, UserInformationActivity.class);
        loadUserInfoScreen.putExtra("fname", fname);
        loadUserInfoScreen.putExtra("lname", lname);
        startActivity(loadUserInfoScreen);
    }

    public void showError(){
        Toast.makeText(this, "Please enter first and last name.", Toast.LENGTH_LONG).show();

    }

}
