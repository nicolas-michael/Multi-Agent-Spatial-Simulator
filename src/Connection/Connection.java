/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import edu.uw.bothell.css.dsl.MASS.MassData.MASSPacket;
import edu.uw.bothell.css.dsl.MASS.MassData.MASSRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nicolas
 */
public abstract class Connection 
{
    /**
     * String containing host and port in the format, host:port.
     * Assumed to be un-validated.
     */
    private String hostAndPort;
    
    /**
     * URI for validated host and port string. URI methods, 
     * getHost() and get() port return null and -1 respectively
     * for un-valid hosts and ports.
     */
    private URI uri;
    
    /**
     * Client socket used to connect to MASS application.
     */
    private Socket socket;
    
    /**
     * Output stream for sending objects to MASS.
     */
    private OutputStream outputStream;
    
    /**
     * Input stream for reading objects from MASS.
     */
    private InputStream inputStream;
    
    /**
     * True if host and port has been validated.
     */
    private boolean validated;
    
    /**
     * Availability of host.
     */
    private boolean available;
    
    /**
     * Connection status;
     */
    private boolean connected;
    
    /**
     * Connection constructor
     * 
     * Sets the host and port. validateHost should be called immediately
     * after construction.
     * 
     * @param hostAndPort The host and port to connect to.
     */
    public Connection(String hostAndPort)
    {
        this.hostAndPort = hostAndPort;
    }
    
    /**
     * validate 
     * 
     * Checks validity of current host and port and return true if valid.
     * This method and hostAvailable() should be called before requesting a 
     * connection.
     * 
     * @return true if valid.
     */
    public boolean validate()
    {
        try 
        {
            this.uri = new URI("ignorethisfakescheme://" + this.getHostAndPort());
        } 
        catch (URISyntaxException ex) 
        {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            this.validated = false;
        }
        
        if(uri != null)
        {
            this.validated = (this.uri.getHost() != null && this.uri.getPort() != -1);
        }
        
        return this.isValidated();
    }
    
    /**
     * hostAvailable
     * 
     * Pings the host to ensure existance.
     * 
     * @return true if host exists.
     */
    public boolean hostAvailable()
    {
        try 
        {
            available = InetAddress.getByName(uri.getHost()).isReachable(4000);
        } 
        catch (UnknownHostException ex) 
        {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            available = false;
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            available = false;
        }

        return isAvailable();
    }
    
    /**
     * connect
     * 
     * To be implemented in java and c++ connection classes
     * 
     * @return true of connection was made
     */
    public abstract boolean connect();
    
    /**
     * endConnection
     * 
     * Closes the socket and object streams. End all thread working on the 
     * socket before calling. 
     * 
     * to-do : make abstract to be implemented in java and c connection classes
     * 
     */
    public void endConnection()
    {
        try 
        {
            this.getInputStream().close();
            this.getOutputStream().close();
            this.socket.close();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.connected = false;
        this.socket = null;
        this.inputStream = null;
        this.outputStream = null;
    }
    
    /**
     * destroy
     * 
     * Ends connections, sets all data members to null/false.
     */
    public void destroy()
    {
        if(isConnected()) this.endConnection();
        
        this.setHostAndPort(null);
        this.available = false;
        this.inputStream = null;
        this.outputStream = null;
        this.socket = null;
        this.uri = null;
        this.validated = false;
    }

    /**
     * @return the hostAndPort
     */
    public String getHostAndPort() 
    {
        return hostAndPort;
    }

    /**
     * @param hostAndPort the hostAndPort to set
     */
    public void setHostAndPort(String hostAndPort) 
    {
        this.hostAndPort = hostAndPort;
    }

    /**
     * @return the outputStream
     */
    public OutputStream getOutputStream() 
    {
        return outputStream;
    }

    /**
     * @return the inputStream
     */
    public InputStream getInputStream() 
    {
        return inputStream;
    }

    /**
     * @return the validated
     */
    public boolean isValidated() 
    {
        return validated;
    }

    /**
     * @return the available
     */
    private boolean isAvailable() 
    {
        return available;
    }

    /**
     * @return the connection state
     */
    public boolean isConnected() 
    {
        return connected;
    }
    
    public abstract MASSPacket makeRequest(MASSRequest request);

    /**
     * @param connected the connected to set
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    /**
     * @return the socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * @param socket the socket to set
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * @return the uri
     */
    public URI getUri() {
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setUri(URI uri) {
        this.uri = uri;
    }

    /**
     * @param outputStream the outputStream to set
     */
    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * @param inputStream the inputStream to set
     */
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    
}
