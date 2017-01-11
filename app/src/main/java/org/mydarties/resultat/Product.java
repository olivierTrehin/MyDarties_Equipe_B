package org.mydarties.resultat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Olivier Tréhin on 29/11/2016.
 */

public class Product implements Serializable {
    private String name;
    private int realTurnover;
    private int objTurnover;

    private int realSales;
    private int objSales;

    private int realMargin;
    private int objMargin;

    public Product(String name, int realTurnover, int objTurnover, int realSales, int objSales, int realMargin, int objMargin){
        this.name = name;
        this.realTurnover = realTurnover;
        this.objTurnover = objTurnover;
        this.realSales = realSales;
        this.objSales = objSales;
        this.realMargin = realMargin;
        this.objMargin = objMargin;
    }

    public Product(String name){
        this.name = name;
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRealTurnover() {
        return this.realTurnover;
    }

    public void setRealTurnover(int realTurnover) {
        this.realTurnover = realTurnover;
    }

    public int getObjTurnover() {
        return this.objTurnover;
    }

    public void setObjTurnover(int objTurnover) {
        this.objTurnover = objTurnover;
    }

    public int getRealSales() {
        return this.realSales;
    }

    public void setRealSales(int realSales) {
        this.realSales = realSales;
    }

    public int getObjSales() {
        return this.objSales;
    }

    public void setObjSales(int objSales) {
        this.objSales = objSales;
    }

    public int getRealMargin() {
        return this.realMargin;
    }

    public void setRealMargin(int realMargin) {
        this.realMargin = realMargin;
    }

    public int getObjMargin() {
        return this.objMargin;
    }

    public void setObjMargin(int objMargin) {
        this.objMargin = objMargin;
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("name", name);
            obj.put("realTurnover", realTurnover);
            obj.put("objTurnover", objTurnover);
            obj.put("realSales", realSales);
            obj.put("objSales", objSales);
            obj.put("realMargin", realMargin);
            obj.put("objMargin", objMargin);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

}
