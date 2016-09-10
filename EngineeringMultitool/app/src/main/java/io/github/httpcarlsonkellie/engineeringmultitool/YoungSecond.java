package io.github.httpcarlsonkellie.engineeringmultitool;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Jaimie on 9/10/2016.
 */

public class YoungSecond extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SensorEventListener {

    public void onAccuracyChanged(Sensor sensor, int accuracy){
            //nada
    }

    double m1, m2, a, l;
    double dl = 0;
    private SensorManager sensorManager;
    private Sensor accel;
    private float timestamp;
    private float accelCurrent;
    private float accelLast;
    private double accelz = 0;
    double g = 9.8;
    double az = 0;

//    class MyTimerTask extends TimerTask{
//        @Override
//        public void run(){
//            runOnUiThread(new Runnable(){
//                @Override
//                public void run() {
//                    setStressAndStrain();
//            }});
//        }
//    }

    public void setStressAndStrain(){
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        Sensor linaccel = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(this, gravity, 10);
        sensorManager.registerListener(this, linaccel, 10);
    }

    public void onSensorChanged(SensorEvent event){
        System.out.println("sensor changed!!");

        if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            System.out.println("we have gravity sensor");
            g = event.values[2];
        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            System.out.println("we have acceleration sensor");
            az = event.values[2] - g;
        }



//        double ag = event.values[2]; //accelerration with gravity
//
//        accelLast = accelCurrent;
//        accelCurrent = event.values[2];
//        double delta = accelCurrent - accelLast;
//        accelz = accelz*0.9 + delta; //this is a low ccut filter apparently
//        //acceleration without gravity

        System.out.println("ACCELERATION" + az);
        double F = (double) (m1+m2)*(az + g)/1000.0;
        System.out.println("FORCE"  + F);
        double stress = F/a; //calculate stress in N/mm^2
        double dT = 0;
        if (timestamp != 0){
            dT = (event.timestamp - timestamp)/1000000000.0;
        }
        System.out.println("DT" + dT);
        timestamp = event.timestamp;
        dl += 0.5*az*dT*dT*1000.0; //this integration purportedly has bad accuracy; Kalman filter?
        System.out.println("DL" + dl);
        System.out.println("L" + l);
        double strain = (double) (l - dl)/(l);

        System.out.println("STRESS" + stress);
        System.out.println("STRAIN" + strain);

        TextView stressText = (TextView) findViewById(R.id.stress2);
        TextView strainText = (TextView) findViewById(R.id.strain2);

        String sigma = " " + (double) Math.round(stress*10000.0)/10000.0;
        String epsilon = " " + (double) Math.round(strain*10000.0)/10000.0;
        stressText.setText(sigma);
        strainText.setText(epsilon);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.young_second);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        Intent intent = getIntent();
        m1 = intent.getDoubleExtra("m1", 0);
        m2 = intent.getDoubleExtra("m2", 0);
        a = intent.getDoubleExtra("a", 0);
        l = intent.getDoubleExtra("l", 0);
        dl = 0;
        timestamp = 0;

//        Timer timer = new Timer();
//        MyTimerTask timerTask = new MyTimerTask();
//        timer.schedule(timerTask, 100);

        setStressAndStrain();
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

    public void stop(View view){
        Intent intent = new Intent(this, YoungThird.class);
        startActivity(intent);
    }
}
