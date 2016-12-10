/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import edu.uw.bothell.css.dsl.MASS.MassData.InitialData;
import edu.uw.bothell.css.dsl.MASS.MassData.MASSPacket;
import edu.uw.bothell.css.dsl.MASS.MassData.MASSRequest;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nicolas
 */
public class MASSJavaConnection extends Connection
{
    
    /**
     * MassConnection constructor
     * 
     * Sets host, must be validated.
     * 
     * @param host 
     */
    public MASSJavaConnection(String host)
    {
        super(host);
    }
    
    /**
     * Connects to MASS Java via Socket
     * 
     * @return boolean true if connection successful, false otherwise
     */
    public boolean connect()
    {
        //if host is not valid or not available return false
        if(!validate() || !hostAvailable())
        { 
            this.setConnected(false);
            return false;
        }
        
        //attempt connection to validated and available host
        try 
        {
            this.setSocket(new Socket(getUri().getHost(), getUri().getPort()));
            this.setConnected(getSocket().isConnected());
            this.setInputStream(new ObjectInputStream(getSocket().getInputStream()));
            this.setOutputStream(new ObjectOutputStream(getSocket().getOutputStream()));
        } 
        catch (IOException ex) 
        {
            this.setConnected(false);
            this.setSocket(null);
            this.setInputStream(null);
            this.setOutputStream(null);
        }
        
        return isConnected();
    }
    
    /**
     * initializeMassConnection
     * 
     * Requests data transfer with mass. If accepted MASS will return
     * an MASSPacket object containing requested data.
     * 
     * @param request contains the type of request and any supporting data.
     * @return MASSPacket object containing requested data, null if no connection, 
     * or socket exceptions.
     */
    public MASSPacket makeRequest(MASSRequest request)
    {
        MASSPacket data = null;
        if(!this.isConnected()) return null;
        
        try 
        {
            ((ObjectOutputStream)this.getOutputStream()).writeObject(request);
            this.getOutputStream().flush();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(MASSJavaConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        try 
        {
            data = (MASSPacket) ((ObjectInputStream)this.getInputStream()).readObject();
        } 
        catch (IOException | ClassNotFoundException ex) 
        {
            Logger.getLogger(MASSJavaConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        return data;
    }
}
