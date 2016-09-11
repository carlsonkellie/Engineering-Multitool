package io.github.httpcarlsonkellie.engineeringmultitool;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jaimie on 9/10/2016.
 */
public class YoungThird extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.young_third);

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
        double[] stresses = intent.getDoubleArrayExtra("stresses");
        double[] strains = intent.getDoubleArrayExtra("strains");

        GraphView graph = (GraphView) findViewById(R.id.graph);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
        List<SortableDataPoint> toSort = new ArrayList<SortableDataPoint>();
        for (int i = 0; i < stresses.length; i++) {
            SortableDataPoint<Double> dataPoint = new SortableDataPoint<Double>((Double) strains[i], (Double) stresses[i]);
            System.out.println("strains 2i "  + strains[i]);
            System.out.println("stresses 2i " + stresses[i]);
            toSort.add(dataPoint);
        }
        Collections.sort(toSort);

        double maxStrength = 0.0;
        double xySum = 0.0;
        double xSum = 0.0;
        double ySum = 0.0;
        double x2Sum = 0.0;
        int n = 0;

        for (int i = 0; i < toSort.size(); i++){
            SortableDataPoint<Double> pt = toSort.get(i);
            DataPoint dataPoint = new DataPoint(pt.getX().doubleValue(), pt.getY().doubleValue());
            System.out.println("pt double" + pt.getX().doubleValue());
            System.out.println("pt doubley" + pt.getY().doubleValue());
            if (pt.getY().doubleValue() >  maxStrength){
                maxStrength = pt.getY().doubleValue();
            }
            xySum += pt.getX().doubleValue() * pt.getY().doubleValue();
            xSum += pt.getX().doubleValue();
            ySum += pt.getY().doubleValue();
            x2Sum += pt.getX().doubleValue()*pt.getX().doubleValue();
            n++;
            series.appendData(dataPoint, true, 10000000, false);
        }

        graph.addSeries(series);

        TextView us2 = (TextView) findViewById(R.id.us2);
        us2.setText("" + maxStrength);

        double youngsModulus = n*xySum - xSum*ySum;
        youngsModulus /= (n*x2Sum - (xSum*xSum));
        youngsModulus = Math.round(youngsModulus*10000.0)/10000.0;
        TextView ym2 = (TextView) findViewById(R.id.ym2);
        ym2.setText("" + youngsModulus);

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
