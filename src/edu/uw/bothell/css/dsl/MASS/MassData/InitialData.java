/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uw.bothell.css.dsl.MASS.MassData;

/**
 * Created by Nicolas on 8/9/2015.
 */
import java.io.Serializable;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author Nicolas
 */
public class InitialData extends MASSPacket implements Serializable
{
    //The name of the users mass application
    private String agentsName = "";

    //places file name
    private String placesName = "";

    //The number of places in the x dimension
    private int placesX;

    //The number of places in the Y dimension
    private int placesY;

    //The number of places
    private int numberOfPlaces;

    //The number of agents
    private int numberOfAgents;

    //does place overload setDebugData()
    private boolean placeOverloadsSetDebugData;

    //does place overload getDebugData()
    private boolean placeOverloadsGetDebugData;

    //does agent overload setDebugData()
    private boolean agentOverloadsSetDebugData;

    //does agent overload getDebugData()
    private boolean agentOverloadsGetDebugData;

    //data type of place debug data, must extend Number
    private Class<? extends Number> placeDataType;

    //data type of agent debug data, must extend number
    private Class<? extends Number> agentDataType;

    /**
     * Default constructor
     * 
     * Defaults all members to java default values. No touching!
     * 
     */
    public InitialData(){}
    
    /**
     * JSON constructor
     * 
     * This constructor is strictly for instantiating InitialData packet from the
     * given JSON string, and is only called from MASSCppConnection.java. 
     * MASSCppConnection gathers the JSON string from a MASS C++ simulation and is
     * java-fied here so that communication between the JAVA GUI and the MASS C++
     * library is possible.
     * 
     * *WARNING* edits to this constructor must be reflected in multiple places 
     * though out this project including the MASS C++ debugInit() method as well as
     *  the toJsonStirng() methods. And probably other places *Danger!* 
     * 
     * @param jsonString 
     */
    public InitialData(String jsonString)
    {
        //convert JSON to GSON
        JsonObject jsonObject = new JsonParser().parse(jsonString.trim()).getAsJsonObject();
        JsonObject packet = jsonObject.getAsJsonObject("packet");
        
        //convert GSON to this object
        this.agentsName = packet.get("agentsName").getAsString();
        this.placesName = packet.get("placesName").getAsString();
        this.placesX = packet.get("placesX").getAsInt();
        this.placesY = packet.get("placesY").getAsInt();
        this.numberOfPlaces = packet.get("numberOfPlaces").getAsInt();
        this.numberOfAgents = packet.get("numberOfAgents").getAsInt();
        this.placesX = packet.get("placesX").getAsInt();
        this.placeOverloadsSetDebugData = packet.get("placeOverloadsSetDebugData").getAsBoolean();
        this.placeOverloadsGetDebugData = packet.get("placeOverloadsGetDebugData").getAsBoolean();
        this.agentOverloadsSetDebugData = packet.get("agentOverloadsSetDebugData").getAsBoolean();
        this.agentOverloadsGetDebugData = packet.get("agentOverloadsGetDebugData").getAsBoolean();
        
        //get object type of overloaded debug data for agents and places, must extend Number
        String pDataType = packet.get("placeDataType").getAsString();
        String aDataType = packet.get("agentDataType").getAsString();
        
        if(pDataType.trim().isEmpty())
        {
            this.placeDataType = null;
        }
        else
        {
            try 
            {
                placeDataType = (Class<? extends Number>) Class.forName("java.lang." + pDataType);
            } 
            catch (ClassNotFoundException | ClassCastException ex) 
            {
                System.out.println("No such class: " + pDataType);
                this.placeDataType = null;
            }
        }
        
        if(aDataType.trim().isEmpty())
        {
            this.agentDataType = null;
        }
        else
        {
            try 
            {
                agentDataType = (Class<? extends Number>) Class.forName("java.lang." + pDataType);
            } 
            catch (ClassNotFoundException | ClassCastException ex) 
            {
                System.out.println("No such class: " + aDataType);
                this.agentDataType = null;
            }
        }
        
    }


    /**
     * getApplicationName
     *
     * Gets the name of the user defines MASS application.
     *
     * @return the name of the application
     */
    public String getAgentsName()
    {
        return this.agentsName;
    }

    /**
     * getPlacesX
     *
     * Gets the number of places in the x dimension
     *
     * @return the number of places in the x dimension
     */
    public int getPlacesX()
    {
        return this.placesX;
    }

    /**
     * getPlacesY
     *
     * Gets the number of places in the y dimension
     *
     * @return the number of places in the y dimension
     */
    public int getPlacesY()
    {
        return this.placesY;
    }

    /**
     * getNumberOfPlaces
     *
     * Gets the number of places
     *
     * @return the number of places
     */
    public int getNumberOfPlaces()
    {
        return this.numberOfPlaces;
    }

    /**
     * getNumberOfAgents
     *
     * Gets the number of agents
     *
     * @return the number of agents
     */
    public int getNumberOfAgents()
    {
        return this.numberOfAgents;
    }

    public void setPlacesX(int placesX) {
        this.placesX = placesX;
    }
    
    public void setPlacesY(int placesY) {
        this.placesY = placesY;
    }

    public void setNumberOfPlaces(int numberOfPlaces) {
        this.numberOfPlaces = numberOfPlaces;
    }

    public void setNumberOfAgents(int numberOfAgents) {
        this.numberOfAgents = numberOfAgents;
    }

    public void setAgentsName(String agentsName) {
        this.agentsName = agentsName;
    }

    public String getPlacesName() {
        return placesName;
    }

    public void setPlacesName(String placesName) {
        this.placesName = placesName;
    }

    public boolean placeOverloadsSetDebugData() {
        return placeOverloadsSetDebugData;
    }

    public void placeOverloadsSetDebugData(boolean placeOverloadsSetDebugData) {
        this.placeOverloadsSetDebugData = placeOverloadsSetDebugData;
    }

    public boolean placeOverloadsGetDebugData() {
        return placeOverloadsGetDebugData;
    }

    public void placeOverloadsGetDebugData(boolean placeOverloadsGetDebugData) {
        this.placeOverloadsGetDebugData = placeOverloadsGetDebugData;
    }

    public boolean agentOverloadsSetDebugData() {
        return agentOverloadsSetDebugData;
    }

    public void agentOverloadsSetDebugData(boolean agentOverloadsSetDebugData) {
        this.agentOverloadsSetDebugData = agentOverloadsSetDebugData;
    }

    public boolean agentOverloadsGetDebugData() {
        return agentOverloadsGetDebugData;
    }

    public void agentOverloadsGetDebugData(boolean agentOverloadsGetDebugData) {
        this.agentOverloadsGetDebugData = agentOverloadsGetDebugData;
    }

    public Class<? extends Number> getPlaceDataType() {
        return placeDataType;
    }

    public void setPlaceDataType(Class<? extends Number> placeDataType) {
        this.placeDataType = placeDataType;
    }

    public Class<? extends Number> getAgentDataType() {
        return agentDataType;
    }

    public void setAgentDataType(Class<? extends Number> agentDataType) {
        this.agentDataType = agentDataType;
    }
    
    /**
     * toJSONString overridden method
     * 
     * This method is to only be called by MASSCppConnection.java. This is a valid
     * JSON representation of this object with default values. This string is sent
     * to MASS c++ library and serves as a skeleton that is to be filled in by MASS
     * C++ and returned here via the InitialData(jsonString) constructor to be 
     * re java-fied. 
     * 
     * *WARNING* all edits here must be reflected in the InitialData(jsonString)
     * constructor as well as the debugInit() method in MASS.cpp.
     * 
     * @return 
     */
    @Override
    public String toJSONString()
    {
        return "{\"packet\":{\"agentsName\":\"\",\"placesName\":\"\",\"placesX\":0,\"placesY\":0,\"numberOfPlaces\":0,\"numberOfAgents\":0,\"placeOverloadsSetDebugData\":false,\"placeOverloadsGetDebugData\":false,\"agentOverloadsSetDebugData\":false,\"agentOverloadsGetDebugData\":false,\"placeDataType\":\"\",\"agentDataType\":\"\"},\"request\":0}";
    }
    
}
