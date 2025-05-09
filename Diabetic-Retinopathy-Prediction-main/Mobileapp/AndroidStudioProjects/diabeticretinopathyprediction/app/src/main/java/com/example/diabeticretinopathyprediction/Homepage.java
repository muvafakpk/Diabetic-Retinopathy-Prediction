package com.example.diabeticretinopathyprediction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diabeticretinopathyprediction.databinding.ActivityHomepageBinding;
import com.squareup.picasso.Picasso;

public class Homepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomepageBinding binding;
    Snackbar snackbar = null;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHomepage.toolbar);
        binding.appBarHomepage.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        snackbar = Snackbar.make(findViewById(android.R.id.content), "Please click BACK again to exit", Snackbar.LENGTH_SHORT);


        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_homepage);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
        View nv = navigationView.getHeaderView(0);
        ImageView im = nv.findViewById(R.id.imageView);
        TextView nme = nv.findViewById(R.id.navname);
        TextView em = nv.findViewById(R.id.navemail);

        nme.setText(sh.getString("name", ""));
        em.setText(sh.getString("email", "gfx"));
        String url = sh.getString("url", "")+sh.getString("image", "");
        Picasso.with(getApplicationContext()).load(url).transform(new CircleTransform()). into(im);
        navigationView.setItemIconTintList(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homepage, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_homepage);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_gallery) {
            startActivity(new Intent(getApplicationContext(), view_doc.class));
        }
        if (id == R.id.nav_slideshow2) {
            startActivity(new Intent(getApplicationContext(), view_appointment.class));
        }
        if (id == R.id.nav_slideshow) {
            startActivity(new Intent(getApplicationContext(), view_reply.class));
        }
        if (id == R.id.nav_predict) {
            startActivity(new Intent(getApplicationContext(), view_prediction.class));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            new AlertDialog.Builder(Homepage.this)
                    .setTitle("Logout")
                    .setMessage("Are you really want to logout?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Whatever...
                            SharedPreferences.Editor ed = sh.edit();
                            ed.putString("lid", "");
                            ed.commit();
                            finish();
                            startActivity(new Intent(getApplicationContext(), Login.class));
                        }
                    }).
                    show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (snackbar.isShown()){
            finish();
        }
        else {
            snackbar.show();
        }

    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(getApplicationContext(), Locationservice.class));
        super.onDestroy();
    }
}