package com.example.marta.battleorganizer_v1.Utils;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marta.battleorganizer_v1.FightActivity;
import com.example.marta.battleorganizer_v1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;


public class AdapterFightCharacters extends RecyclerView.Adapter<AdapterFightCharacters.ViewHolder> {
    private final String TAG = Utils.getTag(this.getClass().getSimpleName());

    private List<Character> mListCharacters;
    private Character mLastCharacter;
    private Context mContext;
    private int mImageSize = 70;
    @BindArray(R.array.character_types)
    String[] mTypeList;

    public Character getLastCharacter() {
        return mLastCharacter;
    }

    public void setLastCharacter(Character mLastCharacter) {
        this.mLastCharacter = mLastCharacter;
    }


    public AdapterFightCharacters(List<Character> list) {
        this.mListCharacters = list;
    }

    public void setCharacters(List<Character> list) {
        mListCharacters = list;
        notifyDataSetChanged();
    }

    @Override
    public AdapterFightCharacters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder()");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_character, parent, false);
        mContext = parent.getContext();
        mImageSize = (int) mContext.getResources().getDimension(R.dimen.img_size);
        ButterKnife.bind(this, itemView);

        ViewHolder holder = new ViewHolder(itemView);
        holder.mbtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();

                if (position == mListCharacters.indexOf(mLastCharacter)) {
                    if (position > 0) {
                        mLastCharacter = mListCharacters.get(position - 1);
                    } else if(mListCharacters.size()>1 && position == 0){
                        mLastCharacter = mListCharacters.get(mListCharacters.size()-1);
                        ((FightActivity)mContext).nextTurn();
                    } else{
                        mLastCharacter = null;
                    }
                }
                mListCharacters.remove(position);
                notifyDataSetChanged();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(AdapterFightCharacters.ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder()");
        Character characterItem = mListCharacters.get(position);

        try {
            Picasso.with(mContext).load(characterItem
                    .getImagePath())
                    .resize(mImageSize, mImageSize)
                    .centerCrop()
                    .error(R.drawable.default_character)
                    .into(holder.mivImage);
        } catch (Exception e) {
            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.default_character);
            holder.mivImage.setImageDrawable(drawable);
        }
        holder.mtvName.setText(characterItem.getName());
        holder.mtvKP.setText(String.valueOf(characterItem.getKp()));
        holder.mtvKPnp.setText(String.valueOf(characterItem.getKpNp()));
        holder.mtvKPdot.setText(String.valueOf(characterItem.getKpDot()));
        holder.mtvType.setText(getStringFromType(characterItem.getType()));
        holder.mbtnDelete.setTag(position);
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

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            mcbCheck.setVisibility(View.GONE);
            mtvInitiative.setVisibility(View.GONE);
            metCount.setVisibility(View.GONE);
        }
    }
}
