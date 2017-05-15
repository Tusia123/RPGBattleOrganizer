package com.example.marta.battleorganizer_v1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.marta.battleorganizer_v1.Utils.AdapterCharacters;
import com.example.marta.battleorganizer_v1.Utils.Character;
import com.example.marta.battleorganizer_v1.Utils.CharacterDao;
import com.example.marta.battleorganizer_v1.Utils.Constants;
import com.example.marta.battleorganizer_v1.Utils.DaoHelper;
import com.example.marta.battleorganizer_v1.Utils.DaoSession;
import com.example.marta.battleorganizer_v1.Utils.Utils;
import com.squareup.picasso.Picasso;


import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CharactersActivity extends Activity {
    private static int RESULT_LOAD_IMAGE = 1;
    private String TAG = Utils.getTag(CharactersActivity.class.getSimpleName());

    @BindView(R.id.btnAddListOfCharacters)
    Button mBtnAddListOfCharacters;

    @BindView(R.id.character_main_layout)
    LinearLayout mMainLayout;
    @BindView(R.id.cbCheckNewCharacter)
    CheckBox mCharacterCheck;
    @BindView(R.id.ivImageNewCharacter)
    ImageView mCharacterImage;
    private Uri mCharacterImageUri;
    private Bitmap mBitmap;
    @BindView(R.id.etNameNewCharacter)
    EditText mCharacterName;
    @BindView(R.id.etKpNewCharacter)
    EditText mCharacterKp;
    @BindView(R.id.etKpNpNewCharacter)
    EditText mCharacterKpNp;
    @BindView(R.id.etKpDotNewCharacter)
    EditText mCharacterKpDot;
    @BindView(R.id.etInitNewCharacter)
    EditText mCharacterInit;
    @BindView(R.id.spTypeChooser)
    Spinner mCharacterType;
    @BindView(R.id.character_recycle_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.btn_character_add)
    Button mAddCharacterButton;
    @BindView(R.id.vCountNewCharacter)
    View mCountCharacters;
    @BindView(R.id.include_character_descript)
    View mCharacterDescript;
    private LinearLayoutManager mLayoutManager;
    private AdapterCharacters mAdapterCharacters;
    private List<Character> mCharacterList = new ArrayList<>();
    private CharacterDao mCharacterDao;
    private int mMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mMode = intent.getIntExtra(Constants.CHARACTER_INTENT_KEY, Constants.INTENT_NORMAL);

        DaoSession daoSession = ((DaoHelper) getApplication()).getDaoSession();
        mCharacterDao = daoSession.getCharacterDao();

        setUpView();
    }

    private void returnData() {
        if (mMode == Constants.INTENT_PICK_UP) {
            List<Character> checkedCharacters = mAdapterCharacters.getCheckedListCharacters();
            Intent intentResult = new Intent();
            intentResult.putExtra(Constants.CHARACTER_LIST_KEY, Character.makeJSONStringFromList(checkedCharacters));
            setResult(Activity.RESULT_OK, intentResult);
            finish();
        }
    }

    //to lose focus when count changed
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view != null && view instanceof EditText) {
                Rect r = new Rect();
                view.getGlobalVisibleRect(r);
                int rawX = (int) ev.getRawX();
                int rawY = (int) ev.getRawY();
                if (!r.contains(rawX, rawY)) {
                    view.clearFocus();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public List<Character> getCharactersList() {
        Query<Character> characterQuery;
        characterQuery = mCharacterDao.queryBuilder().build();
        return characterQuery.list();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable("BitmapImage", mBitmap);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mBitmap = savedInstanceState.getParcelable("BitmapImage");
        mCharacterImage.setImageBitmap(mBitmap);
        mCharacterList = getCharactersList();
        mAdapterCharacters.setCharacters(mCharacterList);
    }

    private void setUpView() {
        setContentView(R.layout.activity_characters);
        ButterKnife.bind(this);


        if (mMode == Constants.INTENT_NORMAL) {
            mCharacterCheck.setVisibility(View.GONE);
            mBtnAddListOfCharacters.setVisibility(View.GONE);
            mCountCharacters.setVisibility(View.GONE);

            CheckBox cbCheckDescript = ButterKnife.findById(mCharacterDescript, R.id.cb_one_char_derscipt);
            cbCheckDescript.setVisibility(View.GONE);
            TextView tvCountDescript = ButterKnife.findById(mCharacterDescript, R.id.count_one_char_derscipt);
            tvCountDescript.setVisibility(View.GONE);
        }

        if (mBitmap != null) {
            mCharacterImage.setImageBitmap(mBitmap);
        }

        mCharacterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        mMainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return true;
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mCharacterList = getCharactersList();
        mAdapterCharacters = new AdapterCharacters(mCharacterList, mCharacterDao, mMode);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapterCharacters);
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.character_types, R.layout.spinner_text);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCharacterType.setAdapter(typeAdapter);
        mAddCharacterButton.setOnClickListener(new SaveListener());

        mBtnAddListOfCharacters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnData();
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private void addCharacter(Character character) {
        try {
            mCharacterDao.insert(character);
            mCharacterList.add(character);
            mAdapterCharacters.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e(TAG, "error, character not inserted: ", e);
        }
        Log.i(TAG, "character inserted");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult");
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Log.i(TAG, "load image");

            mCharacterImageUri = data.getData();

            try {
                Picasso.with(getApplicationContext()).load(mCharacterImageUri)
                        .resize(Constants.IMAGE_SIZE, Constants.IMAGE_SIZE)
                        .centerCrop()
                        .error(R.drawable.default_character)
                        .into(mCharacterImage);
            } catch (Exception e) {
                Log.e(TAG, "error: ", e);
                Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.default_character);
                mCharacterImage.setImageDrawable(drawable);
            }
        }
    }

    private class SaveListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String imageUri = null;
            String name = "";
            int kp = 0;
            int kpNp = 0;
            int kpDot = 0;
            int baseInitiative = 0;
            int type = 0;

            if (mCharacterImageUri != null) {
                imageUri = mCharacterImageUri.toString();
            }
            if (mCharacterName != null) {
                name = mCharacterName.getText().toString();
            }
            if (mCharacterKp != null) {
                try {
                    kp = Integer.parseInt(mCharacterKp.getText().toString());
                } catch (NullPointerException | NumberFormatException e) {
                    kp = 0;
                }
            }
            if (mCharacterKpNp != null) {
                try {
                    kpNp = Integer.parseInt(mCharacterKpNp.getText().toString());
                } catch (NullPointerException | NumberFormatException e) {
                    kpNp = 0;
                }
            }
            if (mCharacterKpDot != null) {
                try {
                    kpDot = Integer.parseInt(mCharacterKpDot.getText().toString());
                } catch (NullPointerException | NumberFormatException e) {
                    kpDot = 0;
                }
            }
            if (mCharacterInit != null) {
                try {
                    baseInitiative = Integer.parseInt(mCharacterInit.getText().toString());
                } catch (NullPointerException | NumberFormatException e) {
                    baseInitiative = 0;
                }
            }

            type = mCharacterType.getSelectedItemPosition();
            Character character = new Character(
                    imageUri,
                    name,
                    kp,
                    kpNp,
                    kpDot,
                    baseInitiative,
                    type
            );
            addCharacter(character);
            mRecyclerView.scrollToPosition(mCharacterList.size() - 1);
            hideKeyboard();

            mCharacterName.setText(null);
            mCharacterKp.setText(null);
            mCharacterKpNp.setText(null);
            mCharacterKpDot.setText(null);
            mCharacterInit.setText(null);

            mCharacterName.clearFocus();
            mCharacterKp.clearFocus();
            mCharacterKpNp.clearFocus();
            mCharacterKpDot.clearFocus();
            mCharacterInit.clearFocus();
        }
    }

}
