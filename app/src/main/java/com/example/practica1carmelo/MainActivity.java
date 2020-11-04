package com.example.practica1carmelo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practica1carmelo.settings.SettingsActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private final int READ_PERMISSIONS=1;
    ListaLLamadas calls=new ListaLLamadas();
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }


    public void init(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        solicitarPermisos();

        getCallLog();

        guardarArchivosIntern();
        guardarArchivosExtern();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.settings){
            viewSettingsActivity();
        }
        return true;
    }

    private void viewSettingsActivity(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);

    }

    private void solicitarPermisos(){

        int permisoReadContacts = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG);
        int permisoReadLog = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG);

        if(permisoReadContacts!= PackageManager.PERMISSION_GRANTED){
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){

                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.READ_CALL_LOG},READ_PERMISSIONS);
            }else{
                getCallLog();
            }
        }
    }


    private void getCallLog() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor managedCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,null,null,null,null);
                int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
                int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
                int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);

                while(managedCursor.moveToNext()){
                    String numberOrName="";
                    String phName = managedCursor.getString(name);
                    String phNumber = managedCursor.getString(number);

                    if(phName==null){
                        phName="Desconocido";
                    }


                    String callDate = managedCursor.getString(date);
                    Date callDayTime = new Date(Long.valueOf(callDate));
                    SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    String dateString = formater.format(callDayTime);

                    String[] totalSplit = dateString.split(" ");
                    String[] dateSplit = totalSplit[0].split("-");
                    String[] timeSplit = totalSplit[1].split(":");

                    String day = dateSplit[0];
                    String month = dateSplit[1];
                    String year = dateSplit[2];
                    String hour = timeSplit[0];
                    String min = timeSplit[1];
                    String sec = timeSplit[2];

                    calls.añadirLlamada(phName,phNumber,year,month,day,hour,min,sec);


                }
                managedCursor.close();

            }
        }).start();


    }

    public void guardarArchivosIntern(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                File f = new File(getFilesDir(), "historial.csv");
                try{
                    FileWriter fw = new FileWriter(f);
                    for(Llamada obj:calls.getLista()){
                        fw.write(obj.getNombre()+";"+
                                obj.getNum()+";"+
                                obj.getAño()+";"+
                                obj.getMes()+";"+
                                obj.getDia()+";"+
                                obj.getHora()+";"+
                                obj.getSeg());
                    };
                    fw.flush();
                    fw.close();

                }catch (IOException e){
                }

            }
        }).start();


    }

    public void guardarArchivosExtern(){

        new Thread(new Runnable() {
            @Override
            public void run() {

                calls.sorting();
                File f = new File(getExternalFilesDir(null), "llamadas.csv");
                try{
                    FileWriter fw = new FileWriter(f);
                    for(Llamada obj:calls.getLista()){
                        fw.write(obj.getNombre()+";"+
                                obj.getNum()+";"+
                                obj.getAño()+";"+
                                obj.getMes()+";"+
                                obj.getDia()+";"+
                                obj.getHora()+";"+
                                obj.getSeg());

                    };
                    fw.flush();
                    fw.close();

                }catch (IOException e){
                }

            }
        }).start();



    }


    public void cargarArchivosIntern(View view){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String nombre="";
                String año="";
                String mes="";
                String dia="";
                String hora="";
                String min="";
                String seg="";
                String num="";
                String[] partes;
                Llamada nueva;


                File f = new File(getFilesDir(),"historial.csv");
                try{
                    BufferedReader br = new BufferedReader(new FileReader(f));
                    String linea;
                    while ((linea=br.readLine()) != null){
                        partes=linea.split(";");
                        nombre=partes[0];
                        num=partes[1];
                        año=partes[2];
                        mes=partes[3];
                        dia=partes[4];
                        hora=partes[5];
                        min=partes[6];
                        seg=partes[7];
                        nueva = new Llamada(partes[0],partes[1],partes[2],partes[3],partes[4],partes[5],partes[6],partes[7]);

                    }
                    br.close();
                    TextView llamadas = (TextView) findViewById(R.id.salida);
                    llamadas.setText("");
                    for(Llamada obj:calls.getLista()){
                        llamadas.append(obj.getAño()+";"+obj.getMes()+";"+obj.getDia()+";"+obj.getHora()+";"+obj.getMin()+";"+obj.getSeg()+";"+obj.getNum()+";"+obj.getNombre()+"\n");
                    };


                }catch (IOException e){
                }
            }
        });





    }

    public void cargarArchivosExtern(View view){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String nombre="";
                String año="";
                String mes="";
                String dia="";
                String hora="";
                String min="";
                String seg="";
                String num="";
                String[] partes;
                Llamada nueva;


                File f = new File(getExternalFilesDir(null),"llamadas.csv");
                try{
                    BufferedReader br = new BufferedReader(new FileReader(f));
                    String linea;
                    while ((linea=br.readLine()) != null){
                        partes=linea.split(";");
                        nombre=partes[0];
                        num=partes[1];
                        año=partes[2];
                        mes=partes[3];
                        dia=partes[4];
                        hora=partes[5];
                        min=partes[6];
                        seg=partes[7];
                        nueva = new Llamada(partes[0],partes[1],partes[2],partes[3],partes[4],partes[5],partes[6],partes[7]);

                    }
                    br.close();
                    TextView llamadas = (TextView) findViewById(R.id.salida);
                    llamadas.setText("");
                    for(Llamada obj:calls.getLista()){
                        llamadas.append(obj.getNombre()+";"+obj.getAño()+";"+obj.getMes()+";"+obj.getDia()+";"+obj.getHora()+";"+obj.getMin()+";"+obj.getSeg()+";"+obj.getNum()+"\n");
                    };


                }catch (IOException e){
                }
            }
        });







    }

    public void eleccionConfig(){

    }






}

