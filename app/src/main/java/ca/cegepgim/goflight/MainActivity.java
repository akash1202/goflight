package ca.cegepgim.goflight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

//import android.support.v4.app.Fragment;

import ca.cegepgim.goflight.provider.DatabaseUtils;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new HomeFragment()).commit();
        // tv1=findViewById(R.id.textView);
        // tv1.setText(tv1.getText().toString()+FirebaseAuth.getInstance().getCurrentUser().getEmail());

       bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_book:
                        selectedFragment = new BookFragment();
                        break;
                    case R.id.nav_mytrips:
                        selectedFragment = new MyTripsFragment();
                        break;
                    case R.id.nav_checkin:
                        selectedFragment = new CheckInFragment();
                        break;
                    case R.id.nav_account:
                        selectedFragment = new AccountFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();
                return true;
            }
        });
    }

    public void replaceFragments(Fragment fragment) {
       // getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }



    public boolean isLatestVersion() {
        return getCurrentVersion() == BuildConfig.VERSION_CODE;
    }

    public int getCurrentVersion() {   //app updated is checked here using database version
        return DatabaseUtils.getPref(this, DatabaseUtils.CURRENT_VERSION, 0);
    }

    public void setToLatestVersion() {
        DatabaseUtils.setPref(this, DatabaseUtils.CURRENT_VERSION, BuildConfig.VERSION_CODE);
    }
}