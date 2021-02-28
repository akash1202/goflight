package ca.cegepgim.goflight;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.FitWindowsFrameLayout;

public class SplashActivity extends AppCompatActivity{
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Animation anim;
    View splashView;
    String PREFERENCENAME="GOFLIGHT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        anim = AnimationUtils.loadAnimation(this, R.anim.initial_anim);
        sharedPreferences = getSharedPreferences(PREFERENCENAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
       // finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
       // splashView = findViewById(R.id.splashView);
    }
}