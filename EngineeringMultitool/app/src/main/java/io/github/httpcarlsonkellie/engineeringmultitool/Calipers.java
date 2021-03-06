package io.github.httpcarlsonkellie.engineeringmultitool;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaimie on 9/10/2016.
 */
public class Calipers extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    double top, bottom, diff;
  //  TextView diffView = (TextView) findViewById(R.id.measurement);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calipers);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView diffView = (TextView) findViewById(R.id.measurement);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final ImageView topcaliper = (ImageView) findViewById(R.id.calipertop);
        final ImageView bottomcaliper = (ImageView) findViewById(R.id.caliperbottom);
        final ImageView space = (ImageView) findViewById(R.id.imageView2);

        DisplayMetrics metrics = new DisplayMetrics();
        //getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final double myYDpi = (double)metrics.ydpi;
        //float  mMetersToPixelsX = mXDpi / 0.0254f; // 1 inch == 0.0254 meter
        //float  mMetersToPixelsY = mYDpi / 0.0254f;

        top = topcaliper.getY();
        bottom = bottomcaliper.getY();
        diff = Math.abs(bottom - top-25);
        diff = diff/441.0;

         topcaliper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
                    float y = event.getY();
                    System.out.println("top " + top);
                    System.out.println("y" + y);
                    System.out.println("touched top");
                    top += y;
                    diff = Math.abs(bottom - top - 25);
                    diff = diff/441.0;
                    diff = Math.round(diff * 100000.0)/ 100000.0;
                    diffView.setText("" + diff + " in.");
                    topcaliper.setY((float) top);
                    return true;
                }
                return false;
            }
        });

        bottomcaliper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
                    float y = event.getY();
                    System.out.println("bottom " + bottom);
                    System.out.println("y" + y);
                        System.out.println("touched bottom");
                        bottom += y;
                        diff = Math.abs(bottom - top - 25);
                        diff = diff/441.0;
                        diff = Math.round(diff * 100000.0)/ 100000.0;
                        diffView.setText("" + diff + " in.");
                        bottomcaliper.setY((float) bottom);
                        return true;
                }
                return false;
            }
        });

//        Spinner spinner = (Spinner) findViewById(R.id.units);
//        List<String> categories = new ArrayList<String>();
//        categories.add("in. ");
//        categories.add("mm. ");
//
//        // Creating adapter for spinner
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
//
//        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // attaching data adapter to spinner
//        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_calipers) {
            Intent intent = new Intent(this, Calipers.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_ym) {
            Intent intent = new Intent(this, YoungFirst.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_material_properties) {
            Intent intent = new Intent(this, WolframMaterials.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_math) {
            Intent intent = new Intent(this, WolframMath.class);
            startActivity(intent);
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
