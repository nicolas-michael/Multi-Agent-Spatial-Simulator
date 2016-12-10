/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uw.bothell.css.dsl.MASS.MassData;

import java.io.Serializable;

/**
 * Created by Nicolas on 8/13/2015.
 */
public class AgentData extends MASSPacket implements Serializable
{
    private boolean isAlive;
    private int id;
    private Number debugData;
    private int index;
    private int children;

    public boolean isAlive() {
        return isAlive;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Number getDebugData() {
        return debugData;
    }

    public void setDebugData(Number debugData) {
        this.debugData = debugData;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    @Override
    public String toJSONString() {
        return "{\"isAlive\":false,\"id\":0,\"Number\":0\"index\":0\"children\":0}";
    }
}
