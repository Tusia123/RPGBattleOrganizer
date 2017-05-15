package com.example.marta.battleorganizer_v1.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marta.battleorganizer_v1.R;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class AdapterSpells extends RecyclerView.Adapter<AdapterSpells.ViewHolder> {
    private final String TAG = Utils.getTag(AdapterSpells.class.getSimpleName());

    private List<Spell> mListSpells;
    private Context mContext;
    private int mMode;
    private SpellDao mSpellDao;


    public AdapterSpells(List<Spell> list, SpellDao spellDao, int mode) {
        Log.i(TAG, "AdapterCharacters()");
        this.mListSpells = list;
        this.mSpellDao = spellDao;
        this.mMode = mode;

        Collections.sort(this.mListSpells);


        for (Spell spell : mListSpells) {
            spell.setChecked(false);
        }
    }

    public void setSpells(List<Spell> list) {
        if (list != null) {
            this.mListSpells = list;
            Collections.sort(this.mListSpells);
            notifyDataSetChanged();
        }
    }

    @Override
    public AdapterSpells.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder()");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_spell, parent, false);
        mContext = parent.getContext();
        ButterKnife.bind(this, itemView);

        ViewHolder holder = new ViewHolder(itemView);

        if (mMode == Constants.INTENT_PICK_UP) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "item click!");

                    int position = (int) v.getTag();
                    for (Spell spell : mListSpells) {
                        spell.setChecked(false);
                    }
                    mListSpells.get(position).setChecked(true);
                    notifyDataSetChanged();
                }
            });
        }

        holder.mbtnDeleteSpell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = (long) v.getTag();
                mSpellDao.deleteByKey(id);
                for (Spell spellToDelete : mListSpells) {
                    if (spellToDelete.getId() == id) {
                        mListSpells.remove(spellToDelete);
                        notifyDataSetChanged();
                        break;
                    }
                }
            }
        });

        holder.mbtnAddSpell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btnAdd) {
                View v = (View) btnAdd.getParent();
                TextView etName = (TextView) v.findViewById(R.id.one_spell_name);
                EditText etTime = (EditText) v.findViewById(R.id.one_spell_time);
                EditText etWhose = (EditText) v.findViewById(R.id.one_spell_whose);

                String name = "";
                int time = 0;
                String whose = "";

                if (etName != null) {
                    name = etName.getText().toString();
                }
                if (etTime != null) {
                    try {
                        time = Integer.parseInt(etTime.getText().toString());
                    } catch (NullPointerException | NumberFormatException e) {
                        time = 0;
                    }
                }
                if (etName != null) {
                    whose = etWhose.getText().toString();
                }

                if (time == 0) {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.error_no_time_given),
                            Toast.LENGTH_LONG).show();
                    if (etTime != null) {
                        etTime.requestFocus();
                    }
                    return;
                }

                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(btnAdd.getWindowToken(), 0);

                Spell spell = new Spell(name, time, whose);
                returnData(spell);
            }
        });
        return holder;
    }

    private void returnData(Spell spell) {
        Intent intent = new Intent();
        intent.putExtra(Constants.SPELLS_LIST_KEY, spell.toJsonObject().toString());
        ((Activity) mContext).setResult(Activity.RESULT_OK, intent);
        ((Activity) mContext).finish();
    }

    @Override
    public void onBindViewHolder(AdapterSpells.ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder()" + " position: " + position);
        Spell spellItem = mListSpells.get(position);

        holder.itemView.setTag(position);
        holder.mtvName.setText(spellItem.getName());
        holder.mbtnDeleteSpell.setTag(spellItem.getId());

        if (spellItem.isChecked()) {
            holder.mtvTime.setVisibility(View.VISIBLE);
            holder.mtvTime.requestFocus();
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            holder.mtvWhose.setVisibility(View.VISIBLE);
            holder.mbtnAddSpell.setVisibility(View.VISIBLE);
            holder.mbtnDeleteSpell.setVisibility(View.GONE);
        } else {
            holder.mtvTime.setVisibility(View.GONE);
            holder.mtvWhose.setVisibility(View.GONE);
            holder.mbtnAddSpell.setVisibility(View.GONE);
            holder.mbtnDeleteSpell.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mListSpells.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        String TAG = Utils.getTag(ViewHolder.class.getSimpleName());
        @BindView(R.id.one_spell_name)
        TextView mtvName;
        @BindView(R.id.one_spell_time)
        TextView mtvTime;
        @BindView(R.id.one_spell_whose)
        TextView mtvWhose;
        @BindView(R.id.one_spell_delete)
        Button mbtnDeleteSpell;
        @BindView(R.id.one_spell_add)
        Button mbtnAddSpell;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
