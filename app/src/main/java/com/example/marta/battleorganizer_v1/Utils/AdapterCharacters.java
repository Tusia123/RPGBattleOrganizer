package com.example.marta.battleorganizer_v1.Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marta.battleorganizer_v1.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;


public class AdapterCharacters extends RecyclerView.Adapter<AdapterCharacters.ViewHolder> {
    private final String TAG = Utils.getTag(AdapterCharacters.class.getSimpleName());

    private List<Character> mListCharacters;
    private Context mContext;
    private int mMode;
    private int mImageSize = 70;
    private CharacterDao mCharacterDao;
    @BindArray(R.array.character_types)
    String[] mTypeList;


    public AdapterCharacters(List<Character> list, CharacterDao characterDao, int mode) {
        Log.i(TAG, "AdapterCharacters()");
        this.mListCharacters = list;
        this.mCharacterDao = characterDao;
        this.mMode = mode;

        for (Character character : mListCharacters) {
            if (character.isChecked()) {
                character.setChecked(false);
                mCharacterDao.update(character);
            }
        }
    }

    public void setCharacters(List<Character> list) {
        if (list != null) {
            this.mListCharacters = list;
            notifyDataSetChanged();
        }
    }

    @Override
    public AdapterCharacters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder()");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_character, parent, false);
        mContext = parent.getContext();
        mImageSize = (int) mContext.getResources().getDimension(R.dimen.img_size);
        ButterKnife.bind(this, itemView);

        ViewHolder holder = new ViewHolder(itemView, mMode);
        holder.mcbCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() != null) {
                    CheckBox checkBox = (CheckBox) v;
                    int position = (int) v.getTag();
                    Log.i(TAG, "position: " + position);
                    Character character = mListCharacters.get(position);
                    character.setChecked(checkBox.isChecked());
                    mCharacterDao.update(character);
                }
            }
        });
        holder.metCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.metCount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.i(TAG, "focus changed====================================================");
                if (!hasFocus && v.getTag() != null) {
                    Log.i(TAG, "focus lost====================================================");
                    EditText editText = (EditText) v;
                    int position = (int) v.getTag();
                    Log.i(TAG, "position: " + position);
                    Character character = mListCharacters.get(position);
                    String strCount = editText.getText().toString();
                    int count;
                    if (strCount.isEmpty()) {
                        count = 1;
                    } else {
                        count = Integer.parseInt(strCount);
                        if (count < 1) {
                            count = 1;
                        }
                    }
                    character.setCount(count);
                    mCharacterDao.update(character);
                }
            }
        });
        holder.mbtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = (long) v.getTag();
                mCharacterDao.deleteByKey(id);
                for (Character characterToDelete : mListCharacters) {
                    if (characterToDelete.getId() == id) {
                        mListCharacters.remove(characterToDelete);
                        notifyDataSetChanged();
                        break;
                    }
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(AdapterCharacters.ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder()");
        Character characterItem = mListCharacters.get(position);

        holder.mcbCheck.setChecked(characterItem.isChecked());
        holder.mcbCheck.setTag(position);
        if (characterItem.getImagePath() == null) {
            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.default_character);
            holder.mivImage.setImageDrawable(drawable);
        } else {
            Picasso.with(mContext).load(characterItem
                    .getImagePath())
                    .resize(mImageSize, mImageSize)
                    .centerCrop()
                    .error(R.drawable.default_character)
                    .into(holder.mivImage);
        }
        holder.mtvName.setText(characterItem.getName());
        holder.mtvKP.setText(String.valueOf(characterItem.getKp()));
        holder.mtvKPnp.setText(String.valueOf(characterItem.getKpNp()));
        holder.mtvKPdot.setText(String.valueOf(characterItem.getKpDot()));
        holder.mtvInitiative.setText(String.valueOf(characterItem.getBaseInitiative()));
        holder.mtvType.setText(getStringFromType(characterItem.getType()));
        holder.metCount.setText(String.valueOf(characterItem.getCount()));
        holder.metCount.setTag(position);
        holder.mbtnDelete.setTag(characterItem.getId());
    }

    private String getStringFromType(int type) {
        if (type < 0)
            type = 0;
        if (type > 2)
            type = 2;
        return mTypeList[type];
    }

    @Override
    public int getItemCount() {
        return mListCharacters.size();
    }

    public List<Character> getCheckedListCharacters() {
        List<Character> checkedList = new ArrayList<>();

        for (Character character : mListCharacters) {
            if (character.isChecked()) {
                int count = character.getCount();
                if (count < 1) {
                    count = 1;
                }

                Character newCharacter = character.getCopy();
                newCharacter.setCount(count);
                checkedList.add(newCharacter);
            }
        }
        return checkedList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        String TAG = Utils.getTag(ViewHolder.class.getSimpleName());
        @BindView(R.id.one_character_checkbox)
        CheckBox mcbCheck;
        @BindView(R.id.one_character_image)
        ImageView mivImage;
        @BindView(R.id.one_character_name)
        TextView mtvName;
        @BindView(R.id.one_character_kp)
        TextView mtvKP;
        @BindView(R.id.one_character_kp_np)
        TextView mtvKPnp;
        @BindView(R.id.one_character_kp_dot)
        TextView mtvKPdot;
        @BindView(R.id.one_character_initiative)
        TextView mtvInitiative;
        @BindView(R.id.one_character_type)
        TextView mtvType;
        @BindView(R.id.one_character_delete)
        Button mbtnDelete;
        @BindView(R.id.one_character_count)
        EditText metCount;

        public ViewHolder(View view, int mode) {
            super(view);
            Log.i(TAG, "ViewHolder()");
            ButterKnife.bind(this, view);

            if (mode == Constants.INTENT_NORMAL) {
                mcbCheck.setVisibility(View.GONE);
                metCount.setVisibility(View.GONE);
            }
        }
    }
}
