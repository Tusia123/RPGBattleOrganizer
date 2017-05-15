package com.example.marta.battleorganizer_v1.Utils;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.marta.battleorganizer_v1.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AdapterFightSpells extends RecyclerView.Adapter<AdapterFightSpells.ViewHolder> {
    String TAG = Utils.getTag(AdapterSpells.class.getSimpleName());

    private List<Spell> mListSpells;
    private Context mContext;


    public AdapterFightSpells(List<Spell> list) {
        Log.i(TAG, "AdapterCharacters()");
        this.mListSpells = list;
    }

    public void setSpells(List<Spell> list) {
        if (list != null) {
            this.mListSpells = list;
            notifyDataSetChanged();
        }
    }

    @Override
    public AdapterFightSpells.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder()");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_spell, parent, false);
        mContext = parent.getContext();
        ButterKnife.bind(this, itemView);

        AdapterFightSpells.ViewHolder holder = new AdapterFightSpells.ViewHolder(itemView);

        holder.mbtnAddSpell.setVisibility(View.GONE);

        holder.mbtnDeleteSpell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View deletebutton) {
                Spell spell = (Spell) deletebutton.getTag();
                mListSpells.remove(spell);
                notifyDataSetChanged();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(AdapterFightSpells.ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder()");
        Spell spellItem = mListSpells.get(position);

        holder.mtvName.setText(spellItem.getName());
        holder.mtvTime.setText(String.valueOf(spellItem.getLeftTime()));
        holder.mtvWhose.setText(spellItem.getWhose());
        holder.mbtnDeleteSpell.setTag(spellItem);
    }

    @Override
    public int getItemCount() {
        return mListSpells.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        String TAG = Utils.getTag(this.getClass().getSimpleName());
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
            Log.i(TAG, "ViewHolder()");
            ButterKnife.bind(this, view);
        }
    }
}
