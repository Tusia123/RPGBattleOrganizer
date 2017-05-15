package com.example.marta.battleorganizer_v1.Utils;


import android.support.annotation.NonNull;
import android.util.Log;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Table contains spells
 */

@Entity
public class Spell implements Comparable<Spell> {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    @Transient
    private int leftTime = 0;
    @Transient
    private String whose;
    @Transient
    private boolean checked;

    public Spell(String name) {
        this.name = name;
        this.setLeftTime(0);
        this.setWhose(null);
    }

    public Spell(String name, int time, String whose) {
        this.name = name;
        this.setLeftTime(time);
        this.setWhose(whose);
    }

    @Generated(hash = 1395553395)
    public Spell(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 2133214849)
    public Spell() {
    }

    public String print() {
        return "id:" + this.id + " ,name:" + this.name + " time:" + this.getLeftTime() + " ,whose:" + this.getWhose();
    }

    public static Spell spellFromJsonString(String jsonString) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            Log.e("Spell: ", "", e);
        }

        if (jsonObject == null) {
            return null;
        }

        String name = jsonObject.optString(Constants.SPELL_NAME);
        int time = jsonObject.optInt(Constants.SPELL_TIME, 0);
        String whose = jsonObject.optString(Constants.SPELL_WHOSE);
        Spell spell = new Spell(name, time, whose);
        return spell;
    }

    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.SPELL_NAME, this.name);
            jsonObject.put(Constants.SPELL_TIME, this.leftTime);
            jsonObject.put(Constants.SPELL_WHOSE, this.whose);
        } catch (JSONException e) {
            Log.e(Utils.getTag(this.getClass().getSimpleName()), "json error: ", e);
        }
        return jsonObject;
    }

    public static String makeJSONStringFromList(List<Spell> list) {
        JSONArray jsonArray = new JSONArray();

        for (Spell spell : list) {
            JSONObject jsonObject = spell.toJsonObject();
            jsonArray.put(jsonObject);
        }

        return jsonArray.toString();
    }

    public static List<Spell> makeListFromJSONString(String jsonString) {
        List<Spell> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Spell spell = spellFromJsonString(jsonObject.toString());
                if (spell != null) {
                    list.add(spell);
                }
            }
        } catch (JSONException e) {
            Log.e("RPG Spell makeList", "json error: ", e);
        }
        return list;
    }

    @Override
    public int compareTo(@NonNull Spell o) {
        return this.name.compareTo(o.getName());
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLeftTime() {
        return leftTime;
    }

    public void setLeftTime(int leftTime) {
        this.leftTime = leftTime;
    }

    public String getWhose() {
        return whose;
    }

    public void setWhose(String whose) {
        this.whose = whose;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
