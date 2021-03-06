package io.github.httpcarlsonkellie.engineeringmultitool;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Jaimie on 9/10/2016.
 */
public class MaterialsAnswer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

    String appid = "H2GX8L-A7P3U9LTTQ";
    String query, answer;


    public String toURL(String query) throws MalformedURLException {
        String query2 = query.replaceAll(" ", "%20");
        System.out.println("query =" + query);
        return "http://api.wolframalpha.com/v2/query?" + "input=" + query2 + "&appid=" + appid;
    }

    class MyTask extends AsyncTask<String, Void, Void> {
        Exception exception;

        protected Void doInBackground(String... urls) {
            try {
                URL url = new URL(toURL(query));
                try {
                    URLConnection connection = url.openConnection();
                    connection.connect();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String htmlSource = "";
                    String line = "";
                    FileOutputStream outputStream;
                    String filename = "content_materials_answer.xml";
                    while ((line = bufferedReader.readLine()) != null){
                        htmlSource += line;
                    }
                    String h[] = htmlSource.split("<pod | </pod>");
                    answer = "";
                    for (int i = 0; i < h.length; i++){
                        System.out.println("hi = " + h[i]);
                        String[] words = h[i].split(" ");
                        if (words.length >= 1) {
                            System.out.println("words 0 " + words[0]);
                            if (words[0].equals("title='Result'") || words[0].equals(" title='Result'") || words[0].equals("title='Result' ") ) {
                                String[] answers = h[i].split("<plaintext>|</plaintext>");
                                System.out.println("answers0" + answers[0]);
                                System.out.println("answers1" + answers[1]);
                                answer = answers[1];
                                return null;
                            }
                        }
                    }

//                    try {
//                        XMLReader reader = XMLReaderFactory.createXMLReader();
//                        reader.parse(htmlSource);
//                        String s = (String) reader.getProperty("Result");
//                        System.out.println("S" + s);
//                    } catch (SAXException s){
//                        System.out.println("exception");
//                        //lol i dont care
                    //lol i do care it ended up throwing this exception
//                    }

//                    try {
//                        Document xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(htmlSource)));
//                        xmlDocument.getDocumentElement().normalize();
//                    } catch (ParserConfigurationException e){
//                        //caught whatever
//                    } catch (SAXException s){
//                        //caught whatever
//                    }

                } catch (IOException io){
                    System.out.println("io error");
                }

            } catch (MalformedURLException m){
                System.out.println("error, bad query");
            }
            return null;
        }

        protected void onPostExecute(Void feed) {
            TextView ans = (TextView) findViewById(R.id.materialsanswer);
            ans.setText(answer);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.materials_answer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        query = intent.getStringExtra("query");
        TextView question = (TextView) findViewById(R.id.materialstitle);
        question.setText(query);

        MyTask task = new MyTask();
        task.execute();


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
