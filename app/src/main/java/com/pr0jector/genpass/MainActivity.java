package com.pr0jector.genpass;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.w3c.dom.Text;

import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

  //  private EditText OutputPassword;
    private TextView nTextValue;
    private Button setAddFavoriteButton;
    private CheckBox checkboxL; // Буквы в нижнем регистре
    private CheckBox checkboxU; // Верхний + нижний
    private CheckBox checkboxN; // Верхний + нижний + цифры
    private CheckBox checkboxM; // Верхний + нижний + цифры + Спецсимволы
    private static SharedPreferences sPref;
    private static SharedPreferences.Editor editor;
    private static Context context;
    private CheckBox checkBoxPlus;
   private EditText OutputPasswordPlus;
    private TextView textView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      //  OutputPassword = (EditText) findViewById(R.id.editebleOutputPassword);
        OutputPasswordPlus = (EditText) findViewById(R.id.outputPasswordPlus);
        checkBoxPlus = (CheckBox) findViewById(R.id.checkBoxPlus);
        textView4 = (TextView)findViewById(R.id.textView4);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final SeekBar seekbar = (SeekBar) findViewById(R.id.seekBar);
        seekbar.setOnSeekBarChangeListener((SeekBar.OnSeekBarChangeListener) this);

        nTextValue = (TextView) findViewById(R.id.textNumber);
        setAddFavoriteButton = (Button) findViewById(R.id.addFavoriteButton);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.about_app) {
            return true;
        }

        if (id == R.id.exit_app) {
            return true;
        }

        if (id == R.id.favorite) {
            //Intent intObj = new Intent(this, OutputPasswordActviity.class);
            // startActivity(intObj);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
        checkboxL = (CheckBox) findViewById(R.id.checkBoxL);
        checkboxU = (CheckBox) findViewById(R.id.checkBoxU);
        checkboxN = (CheckBox) findViewById(R.id.checkBoxN);
        checkboxM = (CheckBox) findViewById(R.id.checkBoxM);
        StringBuffer characters = new StringBuffer();
        String lol = "";

        String low = "abcdefghijklmnopqrstuvwxyz";
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String number = "1234567890";
        String symbol = "!#$%&'()*+,-./:;<=>?@[]^_`{|}~";

        if (checkboxL.isChecked()) { characters.append(low); lol = characters.toString(); }
        if (checkboxU.isChecked()) { characters.append(upper); lol = characters.toString(); }
        if (checkboxM.isChecked()) { characters.append(symbol); lol = characters.toString(); }
        if (checkboxN.isChecked()) { characters.append(number); lol = characters.toString(); }

        int charactersLength = lol.length();
        StringBuffer buffer = new StringBuffer();


        for (int i=0; i < Integer.valueOf(nTextValue.getText().toString()); i++){
            double index = Math.random() * charactersLength;
            buffer.append(lol.charAt((int) index));
        }
        textView4.setText(buffer.toString());
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        nTextValue.setText(String.valueOf(seekBar.getProgress()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void setAddFavoriteButton(View v) { // Клик по кнопке, обрабатывает тост и добавляет пароль в ресайклВью
        switch (v.getId()) {
            case R.id.addFavoriteButton:
                Snackbar.make(v, "Пароль сохранен в избранное", Snackbar.LENGTH_LONG)
                        .setAction("Ok", null).show();

            if (checkBoxPlus.isChecked() != true) {
                savePassword();

            } else {
                Snackbar.make(v, "Пароль сохранен в избранное", Snackbar.LENGTH_LONG)
                        .setAction("Ok", null).show();
                savePasswordPlus();
            }
                break;
            default:
                break;
        }
    }


    void savePassword() {
        sPref =  getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = sPref.edit();
        Set<String> sharedString = sPref.getStringSet("sharedString", new HashSet<String>());
        sharedString.add(String.valueOf(textView4.getText()));
        editor.clear();
        editor.putStringSet("sharedString", sharedString);
        editor.apply();
        // Toast.makeText(this, "Пароль сохранен в избранное", Toast.LENGTH_SHORT).show();
    }

    void savePasswordPlus() { // Сейв с описанием
        sPref =  getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = sPref.edit();
        Set<String> sharedString = sPref.getStringSet("sharedString", new HashSet<String>());
        sharedString.add(String.valueOf(textView4.getText()) + "\n" + "Описание:" + " " + String.valueOf(OutputPasswordPlus.getText()));
        editor.clear();
        editor.putStringSet("sharedString", sharedString);
        editor.apply();
        // Toast.makeText(this, "Пароль сохранен в избранное", Toast.LENGTH_SHORT).show();
    }

    public void favoriteActivity(MenuItem item) { // Открываем экран - избранное
        Intent intObj = new Intent(this, FavoriteActivity.class);
        startActivity(intObj);
    }


    public void ExitFromApp(MenuItem item) {
        new AlertDialog.Builder(this)
                .setMessage("Вы действительно хотите выйти?")
                .setCancelable(false)
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                }).setNegativeButton("Нет", null).show();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Вы действительно хотите покинуть программу?")
                .setCancelable(false)
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                }).setNegativeButton("Нет", null).show();
    }


    public void markCheckBox(View view) {
        if (checkBoxPlus.isChecked() != true) { OutputPasswordPlus.setVisibility(View.INVISIBLE); } else {
            OutputPasswordPlus.setVisibility(View.VISIBLE);

        }
    }
}
