package com.pr0jector.genpass;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.ArraySet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavoriteActivity extends AppCompatActivity implements RecyclerViewAdapter.OnItemClickListener {

    private RecyclerView myRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerViewAdapter myRecyclerViewAdapter;
       private static List<String> list = new ArrayList<>();
      private static SharedPreferences sPref;
       private ImageButton imageButton;
        private FloatingActionButton fab;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_item);
      //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       //  setSupportActionBar(toolbar);
      //  getSupportActionBar().setDisplayShowHomeEnabled(true);
        myRecyclerView = new RecyclerView(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        imageButton = (ImageButton) findViewById(R.id.closeButton);

        linearLayoutManager =
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        /*
        linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        */



        myRecyclerViewAdapter = new RecyclerViewAdapter(this,new ArrayList<String>());
        myRecyclerViewAdapter.setOnItemClickListener(this);
        myRecyclerView.setAdapter(myRecyclerViewAdapter);
        myRecyclerView.setLayoutManager(linearLayoutManager);

        setContentView(myRecyclerView);

        sPref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        Set<String> sharedString = sPref.getStringSet("sharedString", new HashSet<String>());
        list.removeAll(sharedString);

        list.addAll(sharedString); // это мы Set переводим в List
        myRecyclerViewAdapter.addAll(list); //добавляем все элементы list в адаптер


      /*  fab2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                }
        }); */

        }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                getMenuInflater().inflate(R.menu.menu_main2, menu);
                return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                int id = item.getItemId();
                return true;
        }

        public void deleteItem(View view) {
                View victim = (View) view.getParent();
                int position = myRecyclerView.getChildLayoutPosition(victim);
                myRecyclerViewAdapter.remove(position);
                saveToPrefFromAdapter();
                myRecyclerViewAdapter.notifyDataSetChanged();
        }

        public void savePasswordList(MenuItem item) {
                saveToPrefFromAdapter();
                myRecyclerViewAdapter.notifyDataSetChanged();
        }



        @Override
        public void onItemClick(RecyclerViewAdapter.ItemHolder item, int position) {
                Toast.makeText(this, "Пароль скопирован", Toast.LENGTH_SHORT).show();
                // копируем пароль
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboard.setText(item.getItemName());
        }

        public void saveToPrefFromAdapter() {
                sPref = getApplicationContext().getSharedPreferences("MyPref",MODE_PRIVATE);
                SharedPreferences.Editor editor = sPref.edit();
                Set<String> sharedString = sPref.getStringSet("sharedString", new HashSet<String>());
                sharedString.clear();


                sharedString.addAll(myRecyclerViewAdapter.getDataSet());
                editor.clear();
                editor.putStringSet("sharedString", sharedString); // кладем в едитор наш новый сет
                editor.apply();
                Toast.makeText(this, "Список паролей обновлен успешно", Toast.LENGTH_SHORT).show();
        }


        public void onResume() {
                super.onResume();
                list.clear();
        }

        public void sharePassword(MenuItem item) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = String.valueOf(myRecyclerViewAdapter.getDataSet());
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Отправить пароли с помощью");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Отправить пароли с помощью"));
        }
}
