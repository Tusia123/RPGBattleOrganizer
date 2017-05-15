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
 * Table contains heroes, monsters and enemies
 */

@Entity
public class Character implements Comparable<Character> {
    @Id(autoincrement = true)
    private Long id;
    private String imagePath;
    private String name;
    private int kp;
    private int kpNp;
    private int kpDot;
    private int baseInitiative;
    private int type;
    private boolean isChecked;
    @Transient
    private int initiative = 0;
    @Transient
    private int count = 0;


    public Character(String imagePath, String name, int kp, int kpNp, int kpDot, int baseInit, int type) {
        this.imagePath = imagePath;
        this.name = name;
        this.kp = kp;
        this.kpNp = kpNp;
        this.kpDot = kpDot;
        this.baseInitiative = baseInit;
        this.type = type;

        this.isChecked = false;
    }

    public Character(String imagePath, String name, int kp, int kpNp, int kpDot, int baseInit, int init, int type, int count) {
        this.imagePath = imagePath;
        this.name = name;
        this.kp = kp;
        this.kpNp = kpNp;
        this.kpDot = kpDot;
        this.baseInitiative = baseInit;
        this.initiative = init;
        this.type = type;
        this.count = count;

        this.isChecked = false;
    }

    @Generated(hash = 1352422567)
    public Character(Long id, String imagePath, String name, int kp, int kpNp, int kpDot, int baseInitiative,
                     int type, boolean isChecked) {
        this.id = id;
        this.imagePath = imagePath;
        this.name = name;
        this.kp = kp;
        this.kpNp = kpNp;
        this.kpDot = kpDot;
        this.baseInitiative = baseInitiative;
        this.type = type;
        this.isChecked = isChecked;
    }

    @Generated(hash = 1853959157)
    public Character() {
    }

    public String print() {
        return "id:" + this.id + " ,name:" + this.name + " " + this.kp + " " + this.kpNp + " "
                + this.kpDot + " , init: " + this.initiative;
    }

    public static String makeJSONStringFromList(List<Character> list) {
        JSONArray jsonArray = new JSONArray();

        for (Character character : list) {
            JSONObject jsonObject = character.toJsonObject();
            jsonArray.put(jsonObject);
        }
        return jsonArray.toString();
    }

    public static List<Character> makeListFromJSONString(String jsonString) {
        List<Character> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Character character = characterFromJsonObject(jsonObject);
                if (character != null) {
                    list.add(character);
                }
            }
        } catch (JSONException e) {
            Log.e("RPG Character makeList", "json error: ", e);
        }
        return list;
    }

    public static Character characterFromJsonObject(JSONObject jsonObject) {
        String imagePath = jsonObject.optString(Constants.CHARACTER_IMAGE);
        String name = jsonObject.optString(Constants.CHARACTER_NAME);
        int kp = jsonObject.optInt(Constants.CHARACTER_KP, 0);
        int kpNp = jsonObject.optInt(Constants.CHARACTER_KPNP, 0);
        int kpDot = jsonObject.optInt(Constants.CHARACTER_KPDOT, 0);
        int baseInit = jsonObject.optInt(Constants.CHARACTER_BASE_INIT, 0);
        int init = jsonObject.optInt(Constants.CHARACTER_INIT, 0);
        int type = jsonObject.optInt(Constants.CHARACTER_TYPE, 0);
        int count = jsonObject.optInt(Constants.CHARACTER_COUNT, 1);
        Character character = new Character(imagePath, name, kp, kpNp, kpDot, baseInit, init, type, count);
        return character;
    }

    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.CHARACTER_ID, this.id);
            jsonObject.put(Constants.CHARACTER_IMAGE, this.imagePath);
            jsonObject.put(Constants.CHARACTER_NAME, this.name);
            jsonObject.put(Constants.CHARACTER_KP, this.kp);
            jsonObject.put(Constants.CHARACTER_KPNP, this.kpNp);
            jsonObject.put(Constants.CHARACTER_KPDOT, this.kpDot);
            jsonObject.put(Constants.CHARACTER_BASE_INIT, this.baseInitiative);
            jsonObject.put(Constants.CHARACTER_INIT, this.initiative);
            jsonObject.put(Constants.CHARACTER_TYPE, this.type);
            jsonObject.put(Constants.CHARACTER_COUNT, this.count);
        } catch (JSONException e) {
            Log.e(Utils.getTag(this.getClass().getSimpleName()), "json error: ", e);
        }
        return jsonObject;
    }

    public Character getCopy() {
        Character character = new Character(
                imagePath,
                name,
                kp,
                kpNp,
                kpDot,
                baseInitiative,
                type
        );
        return character;
    }

    public Character getCopy(String name) {
        Character character = new Character(
                imagePath,
                name,
                kp,
                kpNp,
                kpDot,
                baseInitiative,
                type
        );
        return character;
    }

    @Override
    public int compareTo(@NonNull Character o) {
        return o.initiative - this.initiative;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getKp() {
        return kp;
    }

    public void setKp(int kp) {
        this.kp = kp;
    }

    public int getKpNp() {
        return kpNp;
    }

    public void setKpNp(int kpNp) {
        this.kpNp = kpNp;
    }

    public int getKpDot() {
        return kpDot;
    }

    public void setKpDot(int kpDot) {
        this.kpDot = kpDot;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getBaseInitiative() {
        return this.baseInitiative;
    }

    public void setBaseInitiative(int baseInitiative) {
        this.baseInitiative = baseInitiative;
    }

    public int getInitiative() {
        return initiative;
    }

    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean getIsChecked() {
        return this.isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
