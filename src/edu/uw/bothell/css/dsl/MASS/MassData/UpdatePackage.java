/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uw.bothell.css.dsl.MASS.MassData;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Nicolas on 8/10/2015.
 */
public class UpdatePackage extends MASSPacket implements Serializable
{
    private PlaceData[] placeData;

    public PlaceData[] getPlaceData() {
        return placeData;
    }

    public void setPlaceData(PlaceData[] placeData) {
        this.placeData = placeData;
    }
    
    public UpdatePackage(){}
    
    public UpdatePackage(InitialData data){}
    
    public UpdatePackage(String jsonString)
    {
        JsonObject jsonObject = new JsonParser().parse(jsonString.trim()).getAsJsonObject();
        JsonObject packet = jsonObject.getAsJsonObject("packet");
        JsonArray jsonArr = packet.get("PlaceArray").getAsJsonArray();
        Gson googleJson = new Gson();
        
        ArrayList<LinkedTreeMap> list = googleJson.fromJson(jsonArr, ArrayList.class);
        PlaceData[] placeDataArr = new PlaceData[list.size()];
        
        for(int i = 0; i < list.size(); i++)
        {
            LinkedTreeMap<String, Object> map = list.get(i);
            PlaceData newPlace = new PlaceData();
            
            newPlace.setThisPlaceData((Double)map.get("Number"));
            newPlace.setIndex(((Double)map.get("index")).intValue());
            newPlace.setHasAgents(((Double)map.get("hasAgents")).intValue() == 1);
            
            placeDataArr[i] = newPlace;
            
            ArrayList<LinkedTreeMap> arr = (ArrayList) map.get("AgentArray");
            AgentData[] agents =new AgentData[arr.size()];
            
            for(int j = 0; j < arr.size(); i++)
            {
                LinkedTreeMap<String, Object> agentMap = arr.get(j);
                AgentData agent = new AgentData();
                
                agent.setIsAlive((Double)agentMap.get("isAlive") == 1.);
                agent.setId(((Double)agentMap.get("id")).intValue());
                agent.setDebugData((Double)agentMap.get("Number"));
                agent.setIndex(((Double)agentMap.get("index")).intValue());
                agent.setChildren(((Double)agentMap.get("children")).intValue());
                
                agents[j] = agent;
            }
            
            placeDataArr[i].setAgentDataOnThisPlace(agents);
        }
        
        placeData = placeDataArr;
    }

    @Override
    public String toJSONString() 
    {
        
        String jsonString = "{\"packet\":{\"PlaceArray\":[";
        jsonString += "]}, \"request\":1}";
        
        return jsonString;
    }
}
