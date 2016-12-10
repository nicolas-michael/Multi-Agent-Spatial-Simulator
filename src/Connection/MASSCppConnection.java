/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import edu.uw.bothell.css.dsl.MASS.MassData.MASSPacket;
import edu.uw.bothell.css.dsl.MASS.MassData.MASSRequest;
import edu.uw.bothell.css.dsl.MASS.MassData.InitialData;
import static edu.uw.bothell.css.dsl.MASS.MassData.MASSRequest.RequestType.UPDATE_PACKAGE;
import edu.uw.bothell.css.dsl.MASS.MassData.UpdatePackage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nicolas
 */
public class MASSCppConnection extends Connection
{
    
    OutputStreamWriter out;
    BufferedReader dIn;

    public MASSCppConnection(String host)
    {
        super(host);
    }
    
    @Override
    public MASSPacket makeRequest(MASSRequest request) 
    {
        if(!this.isConnected()) return null;
        
        try 
        {
            out.write(request.getPacket().toJSONString());
            out.flush();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(MASSJavaConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        MASSPacket data = null;
        
        char[] massData = new char[2056];
        int factor = 2056;
        try 
        {
            dIn.read(massData);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(MASSCppConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        String thisString = new String(massData);
        String trimmed = thisString.trim();
        
        int openBrackets = trimmed.length() - trimmed.replace("{", "").length();
        int closedBrackets = trimmed.length() - trimmed.replace("}", "").length();
        
        while(openBrackets != closedBrackets)
        {
            factor *= 1.5;
            massData = new char[factor];
            try 
            {
                dIn.read(massData);
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(MASSCppConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
            trimmed += new String(massData);
            trimmed = trimmed.trim();
            openBrackets = trimmed.length() - trimmed.replace("{", "").length();
            closedBrackets = trimmed.length() - trimmed.replace("}", "").length();
        }
  
        switch(request.getRequest())
        {
            case INITIAL_DATA: return new InitialData(trimmed);
            case UPDATE_PACKAGE: return new UpdatePackage(trimmed);
        }
        
        return null;
    }

    @Override
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
            this.setInputStream(this.getSocket().getInputStream());
            this.setOutputStream(this.getSocket().getOutputStream());
            this.setConnected(getSocket().isConnected());
            
            out = new OutputStreamWriter(this.getSocket().getOutputStream());
            dIn = new BufferedReader(new InputStreamReader(this.getSocket().getInputStream() , "UTF8"));
             
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
    
}
