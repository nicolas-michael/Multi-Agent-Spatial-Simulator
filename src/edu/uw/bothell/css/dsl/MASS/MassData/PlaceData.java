/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uw.bothell.css.dsl.MASS.MassData;

import java.io.Serializable;

/**
 * Created by Nicolas on 8/10/2015.
 */
public class PlaceData extends MASSPacket implements Serializable
{
    //this places data
    private Number thisPlaceData;

    //this places index
    private int index;

    //if this place contains agents
    private boolean hasAgents = false;

    //an array of agent data residing on this place
    private AgentData[] agentDataOnThisPlace;

    public Number getThisPlaceData() {
        return thisPlaceData;
    }

    public void setThisPlaceData(Number thisPlaceData) {
        this.thisPlaceData = thisPlaceData;
    }

    public boolean isHasAgents() {
        return hasAgents;
    }

    public void setHasAgents(boolean hasAgents) {
        this.hasAgents = hasAgents;
    }

    public AgentData[] getAgentDataOnThisPlace() {
        return agentDataOnThisPlace;
    }

    public void setAgentDataOnThisPlace(AgentData[] agentDataOnThisPlace) {
        this.agentDataOnThisPlace = agentDataOnThisPlace;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toJSONString() {
        String jsonString =  "{\"Number\":0,\"index\":0,\"hasAgents\":false,\"AgentArray\":[";
        
        jsonString += agentDataOnThisPlace[0].toJSONString();
        
        for(int i = 1; i < agentDataOnThisPlace.length; i++)
        {
            jsonString += "," + agentDataOnThisPlace[i].toJSONString();
        }
        
        jsonString += "]}";
        
        return jsonString;
        
    }
}
