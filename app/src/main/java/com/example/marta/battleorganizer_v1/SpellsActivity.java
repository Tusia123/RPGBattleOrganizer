package com.example.marta.battleorganizer_v1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.marta.battleorganizer_v1.Utils.AdapterSpells;
import com.example.marta.battleorganizer_v1.Utils.Constants;
import com.example.marta.battleorganizer_v1.Utils.DaoHelper;
import com.example.marta.battleorganizer_v1.Utils.DaoSession;
import com.example.marta.battleorganizer_v1.Utils.Spell;
import com.example.marta.battleorganizer_v1.Utils.SpellDao;
import com.example.marta.battleorganizer_v1.Utils.Utils;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpellsActivity extends Activity {

    private String TAG = Utils.getTag(SpellsActivity.class.getSimpleName());

    @BindView(R.id.et_name_spell)
    EditText mSpellName;
    @BindView(R.id.btn_spell_add)
    Button mSpellAdd;
    @BindView(R.id.spell_recycle_view)
    RecyclerView mRecycleSpells;

    private LinearLayoutManager mLayoutManager;
    private AdapterSpells mAdapterSpells;
    private List<Spell> mSpellsList = new ArrayList<>();
    private SpellDao mSpellDao;
    private int mMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mMode = intent.getIntExtra(Constants.SPELL_INTENT_KEY, Constants.INTENT_NORMAL);
        DaoSession daoSession = ((DaoHelper) getApplication()).getDaoSession();
        mSpellDao = daoSession.getSpellDao();

        setUpView();
    }

    public List<Spell> getSpellsList() {
        Query<Spell> spellQuery;
        spellQuery = mSpellDao.queryBuilder().build();
        return spellQuery.list();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mSpellsList = getSpellsList();
        mAdapterSpells.setSpells(mSpellsList);
    }

    private void setUpView() {
        setContentView(R.layout.activity_spells);
        ButterKnife.bind(this);

        mRecycleSpells.setHasFixedSize(true);
        mSpellsList = getSpellsList();
        mAdapterSpells = new AdapterSpells(mSpellsList, mSpellDao, mMode);
        mLayoutManager = new LinearLayoutManager(this);
        mRecycleSpells.setLayoutManager(mLayoutManager);
        mRecycleSpells.setItemAnimator(new DefaultItemAnimator());
        mRecycleSpells.setAdapter(mAdapterSpells);
        mSpellAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSpell();
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private int addSpell(Spell spell) {
        int index = 0;
        try {
            mSpellDao.insert(spell);
            mSpellsList.add(spell);
            Collections.sort(mSpellsList);
            index = mSpellsList.indexOf(spell);
            mAdapterSpells.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e(TAG, "error, spell not inserted: ", e);
        }
        Log.i(TAG, "spell inserted");
        return index;
    }

    private void saveSpell() {
        String name = "";
        if (mSpellName != null) {
            name = mSpellName.getText().toString();
        }
        Spell spell = new Spell(name);
        int index = addSpell(spell);
        mRecycleSpells.scrollToPosition(index);
        hideKeyboard();
        mSpellName.setText(null);
        mSpellName.clearFocus();
    }
}
