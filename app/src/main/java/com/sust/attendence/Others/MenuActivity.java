package com.sust.attendence.Others;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.sust.attendence.Call.CallActivity;
import com.sust.attendence.Manage.ManageActivity;
import com.sust.attendence.R;
import com.sust.attendence.Session.UserSessionManager;

public class MenuActivity extends Activity implements OnClickListener {

    private Button manage_btn, call_btn;
    private TextView welcome_tv;
    private UserSessionManager session;
    private String welcome_text = "Welcome , ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initialize();


    }

    private void initialize() {
        // TODO Auto-generated method stub
        session = new UserSessionManager(this);
        if(session.checkLogin())
            finish();
        manage_btn = (Button) findViewById(R.id.manage_btn);
        call_btn = (Button) findViewById(R.id.call_btn);
        welcome_tv = (TextView) findViewById(R.id.welcome_tv);
        manage_btn.setOnClickListener(this);
        call_btn.setOnClickListener(this);
        welcome_tv.setText(welcome_text+session.get_name());
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.manage_btn:
                Intent manage_intent = new Intent(this, ManageActivity.class);
                startActivity(manage_intent);
                break;
            case R.id.call_btn:
                Intent call_intent = new Intent(this, CallActivity.class);
                startActivity(call_intent);
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the others_menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.others_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exit:
                finish();
                break;
            case R.id.logout:
                session.logoutUser();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
