package app.voron.ph.showcaseapp.Activities;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import app.voron.ph.showcaseapp.R;

public class AboutAppActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextContent = null;
    private LinearLayout mButton1 = null;
    private LinearLayout mButton2 = null;
    private LinearLayout mButton3 = null;
    private LinearLayout mButton4 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        mTextContent = (TextView)findViewById(R.id.text_content);
        mButton1 = (LinearLayout) findViewById(R.id.button_contact);
        mButton2 = (LinearLayout) findViewById(R.id.button_email);
        mButton3 = (LinearLayout) findViewById(R.id.button_skype);
        mButton4 = (LinearLayout) findViewById(R.id.button_freelancim);
        //
        String textContent = getResources().getString(R.string.about_activity_content);
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);
        mTextContent.setText(Html.fromHtml(textContent));
    }
    //
    private void launchMailingApp(){
        try {
            String email = getResources().getString(R.string.about_activity_contact_2);
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"+email));
            startActivity(intent);
        } catch (Exception e) {
            Log.e("AboutAppActivity", e.getMessage());
        }
    }
    //
    private void launchSkype(){
        try {
            String skypeId = getResources().getString(R.string.about_activity_contact_3);
            Uri skypeUri = Uri.parse("skype:" + skypeId + "?chat");
            Intent myIntent = new Intent(Intent.ACTION_VIEW, skypeUri);
            myIntent.setComponent(new ComponentName("com.skype.raider", "com.skype.raider.Main"));
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
        }
        catch (Exception e){
            Log.e("AboutAppActivity", e.getMessage());
        }
    }
    //
    private void launchUrl(String url){
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
        catch (Exception e){
            Log.e("AboutAppActivity", e.getMessage());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_contact:
                break;
            case R.id.button_email:
                launchMailingApp();
                break;
            case R.id.button_skype:
                launchSkype();
                break;
            case R.id.button_freelancim:
                String profileUrl = getResources().getString(R.string.about_activity_contact_4);
                launchUrl(profileUrl);
                break;
        }
    }
}
