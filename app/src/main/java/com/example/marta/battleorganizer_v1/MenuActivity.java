package com.example.marta.battleorganizer_v1;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.marta.battleorganizer_v1.Utils.Utils;

public class MenuActivity extends Activity {
    public static final String TAG = Utils.getTag(MenuActivity.class.getSimpleName());
    private static final int PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpView();
    }

    private void setUpView() {
        setContentView(R.layout.activity_menu);
        Button btnFight = (Button) findViewById(R.id.btnFight);
        btnFight.setOnClickListener(new BeginBattleClickListener());
        Button btnCharacters = (Button) findViewById(R.id.btnCharacters);
        btnCharacters.setOnClickListener(new AddCharacterClickListener());
        Button btnSpells = (Button) findViewById(R.id.btnSpells);
        btnSpells.setOnClickListener(new AddSpellClickListener());
        Button btnExit = (Button) findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new ExitClickListener());

        checkPermissions();

    }

    private void checkPermissions() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                ) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // contacts-related task you need to do.

            } else {
                finish();
            }
        }
    }


    private class BeginBattleClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), FightActivity.class);
            startActivity(intent);

        }
    }

    private class AddCharacterClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), CharactersActivity.class);
            startActivity(intent);

        }
    }

    private class AddSpellClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), SpellsActivity.class);
            startActivity(intent);
        }
    }

    private class ExitClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
    }
}
