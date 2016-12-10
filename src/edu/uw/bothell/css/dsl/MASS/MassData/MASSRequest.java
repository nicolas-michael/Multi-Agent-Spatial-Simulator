/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uw.bothell.css.dsl.MASS.MassData;

import java.io.Serializable;

/**
 *
 * @author Nicolas
 */
public class MASSRequest implements Serializable
{
    public enum RequestType
    {
        INITIAL_DATA, UPDATE_PACKAGE, 
        INJECT_PLACE, INJECT_AGENT, TERMINATE
    }
    
    private MASSPacket packet;
    
    private final RequestType request;
    
    public MASSRequest(RequestType request, MASSPacket packet)
    {
        this.request = request;
        this.packet = packet;
    }
    
    public RequestType getRequest()
    {
        return this.request;
    }
    
     /**
     * @return the packet
     */
    public MASSPacket getPacket() 
    {
        return this.packet;
    }
    
}
