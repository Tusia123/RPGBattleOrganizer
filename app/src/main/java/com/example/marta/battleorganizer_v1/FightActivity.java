package com.example.marta.battleorganizer_v1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.marta.battleorganizer_v1.Utils.AdapterFightCharacters;
import com.example.marta.battleorganizer_v1.Utils.AdapterFightSpells;
import com.example.marta.battleorganizer_v1.Utils.Character;
import com.example.marta.battleorganizer_v1.Utils.Constants;
import com.example.marta.battleorganizer_v1.Utils.Spell;
import com.example.marta.battleorganizer_v1.Utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FightActivity extends Activity {

    public static final int PICK_CHARACTERS_REQUEST = 1;
    public static final int PICK_SPELLS_REQUEST = 2;
    private String TAG = Utils.getTag(this.getClass().getSimpleName());

    @BindView(R.id.tv_fight_turn_number)
    TextView mTvTurnNumber;
    @BindView(R.id.btn_fight_add_characters)
    Button mBtnAddCharacter;
    @BindView(R.id.btn_fight_add_spells)
    Button mBtnAddSpell;
    @BindView(R.id.rv_fight_character)
    RecyclerView mRvCharacters;
    @BindView(R.id.rv_fight_spells)
    RecyclerView mRvCSpells;

    private int mTurn;

    private Random generator = new Random();


    private AdapterFightCharacters mAdapterFightCharacters;
    private LinearLayoutManager mLayoutManagerCharacters;
    private List<Character> mCharacterList = new ArrayList<>();

    private AdapterFightSpells mAdapterFightSpells;
    private LinearLayoutManager mLayoutManagerSpells;
    private List<Spell> mSpellList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight);
        ButterKnife.bind(this);
        mTurn = 1;
        mTvTurnNumber.setText(String.valueOf(mTurn));

        mRvCharacters.setHasFixedSize(true);
        mAdapterFightCharacters = new AdapterFightCharacters(mCharacterList);
        mLayoutManagerCharacters = new LinearLayoutManager(this);
        mRvCharacters.setLayoutManager(mLayoutManagerCharacters);
        mRvCharacters.setItemAnimator(new DefaultItemAnimator());
        mRvCharacters.setAdapter(mAdapterFightCharacters);

        mRvCSpells.setHasFixedSize(true);
        mAdapterFightSpells = new AdapterFightSpells(mSpellList);
        mLayoutManagerSpells = new LinearLayoutManager(this);
        mRvCSpells.setLayoutManager(mLayoutManagerSpells);
        mRvCSpells.setItemAnimator(new DefaultItemAnimator());
        mRvCSpells.setAdapter(mAdapterFightSpells);

        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Character character = mCharacterList.get(0);
                mCharacterList.remove(0);
                mCharacterList.add(character);
                if (character == mAdapterFightCharacters.getLastCharacter()) {
                    nextTurn();
//                    mTurn++;
//                    mTvTurnNumber.setText(String.valueOf(mTurn));
//                    nextTurnInSpells();
                }
                mAdapterFightCharacters.setCharacters(mCharacterList);
                Log.i(TAG, "swiped");
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (viewHolder.getAdapterPosition() == 0) {
                    return super.getSwipeDirs(recyclerView, viewHolder);
                } else {
                    return 0;
                }
            }
        });
        swipeToDismissTouchHelper.attachToRecyclerView(mRvCharacters);

        mBtnAddCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCharactersFromList();
            }
        });
        mBtnAddSpell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSpellsFromList();
            }
        });
    }

    public void nextTurn(){
        mTurn++;
        mTvTurnNumber.setText(String.valueOf(mTurn));
        nextTurnInSpells();
    }

    private void nextTurnInSpells() {
        int leftTime = 0;
        List<Spell> listToRemove = new ArrayList<>();
        for (Spell spell : mSpellList) {
            leftTime = spell.getLeftTime();
            if (leftTime <= 0) {
                listToRemove.add(spell);
            } else {
                spell.setLeftTime(leftTime-1);
            }
        }
        mSpellList.removeAll(listToRemove);
        mAdapterFightSpells.notifyDataSetChanged();
    }

    private void selectCharactersFromList() {
        Intent intent = new Intent(getApplicationContext(), CharactersActivity.class);
        intent.putExtra(Constants.CHARACTER_INTENT_KEY, Constants.INTENT_PICK_UP);
        startActivityForResult(intent, PICK_CHARACTERS_REQUEST);
    }

    private void selectSpellsFromList() {
        Intent intent = new Intent(getApplicationContext(), SpellsActivity.class);
        intent.putExtra(Constants.SPELL_INTENT_KEY, Constants.INTENT_PICK_UP);
        startActivityForResult(intent, PICK_SPELLS_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CHARACTERS_REQUEST) {
            if (resultCode == RESULT_OK) {
                String jsonString = data.getStringExtra(Constants.CHARACTER_LIST_KEY);
                Log.i(TAG, "activity result!: " + jsonString);

                Character currentCharacter = null;
                if (mCharacterList.size() > 1) {
                    currentCharacter = mCharacterList.get(0);
                }

                List<Character> newCharacters = Character.makeListFromJSONString(jsonString);
                for (Character character : newCharacters) {
                    addNewCharacters(character);
                }
                setCharactersInProperOrder(currentCharacter);
                mAdapterFightCharacters.setCharacters(mCharacterList);
            }
        }
        if (requestCode == PICK_SPELLS_REQUEST) {
            if (resultCode == RESULT_OK) {
                hideKeyboard();
                String jsonString = data.getStringExtra(Constants.SPELLS_LIST_KEY);
                Log.i(TAG, "activity result!: " + jsonString);

                Spell newSpell = Spell.spellFromJsonString(jsonString);
                if (newSpell != null) {
                    mSpellList.add(newSpell);
                    mAdapterFightSpells.setSpells(mSpellList);
                }
            }
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private void setCharactersInProperOrder(Character currentCharacter) {
        Collections.sort(mCharacterList);
        mAdapterFightCharacters.setLastCharacter(mCharacterList.get(mCharacterList.size() - 1));
        if (currentCharacter != null) {
            int index = mCharacterList.indexOf(currentCharacter);
            for (int i = 0; i < index; i++) {
                Character moveCharacter = mCharacterList.get(0);
                mCharacterList.remove(0);
                mCharacterList.add(moveCharacter);
            }
        }
    }

    private void addNewCharacters(Character newCharacter) {
        //oldCount - this is how many this character already is in list
        int oldCount = 0;
        for (Character oldCharacter : mCharacterList) {
            if (oldCharacter.getName().contains(newCharacter.getName())) {
                oldCount++;
            }
        }
        int newCount = oldCount + newCharacter.getCount();

        //only one character, no number, just add one character and return
        if (newCount == 1) {
            addCharacterAndRandInitiative(newCharacter);
            return;
        }

        //update old characters(characters which has already been in list)
        //update their count
        for (Character oldCharacter : mCharacterList) {
            if (oldCharacter.getName().contains(newCharacter.getName())) {
                //old character without number, number must be added
                if (oldCount == 1) {
                    oldCharacter.setName(oldCharacter.getName() + "1");
                }
                oldCharacter.setCount(newCount);
            }
        }

        //add new characters
        for (int i = oldCount + 1; i <= newCount; i++) {
            Character anotherCharacter = newCharacter.getCopy(newCharacter.getName() + i);
            anotherCharacter.setCount(newCount);
            addCharacterAndRandInitiative(anotherCharacter);
        }

    }

    private void addCharacterAndRandInitiative(Character character) {
        int k20throw = generator.nextInt(20);
        k20throw++;
        character.setInitiative(character.getBaseInitiative() + k20throw);
        mCharacterList.add(character);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.i(TAG, "instance saved");
        savedInstanceState.putString("listInJSONForm", Character.makeJSONStringFromList(mCharacterList));
        savedInstanceState.putInt("mTurn", mTurn);
        int lastCharacterIndex = 0;
        if (mAdapterFightCharacters.getLastCharacter() != null) {
            lastCharacterIndex = mCharacterList.indexOf(mAdapterFightCharacters.getLastCharacter());
        } else if (mCharacterList.size() > 1) {
            lastCharacterIndex = mCharacterList.size() - 1;
        }
        savedInstanceState.putInt("mLastCharacter", lastCharacterIndex);
        savedInstanceState.putString("spellsList", Spell.makeJSONStringFromList(mSpellList));
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(TAG, "restore instance");
        //turn
        mTurn = savedInstanceState.getInt("mTurn");

        //characters
        String jsonList = savedInstanceState.getString("listInJSONForm");
        mTvTurnNumber.setText(String.valueOf(mTurn));
        mCharacterList = Character.makeListFromJSONString(jsonList);
        if (mCharacterList.size() > 0) {
            mAdapterFightCharacters.setLastCharacter(mCharacterList.get(savedInstanceState.getInt("mLastCharacter")));
        }
        mAdapterFightCharacters.setCharacters(mCharacterList);
        mAdapterFightCharacters.notifyDataSetChanged();

        //spells
        String strSpellList = savedInstanceState.getString("spellsList");
        mSpellList = Spell.makeListFromJSONString(strSpellList);
        mAdapterFightSpells.setSpells(mSpellList);
        mAdapterFightSpells.notifyDataSetChanged();
    }
}
