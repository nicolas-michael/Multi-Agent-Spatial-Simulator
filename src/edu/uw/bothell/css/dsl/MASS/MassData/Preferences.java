package edu.uw.bothell.css.dsl.MASS.MassData;

import java.io.Serializable;
import java.net.URI;

/**
 * Intended to be expanded once the need for more preferences arises.
 * Currently just holds host and port data in a URI object so the user 
 * does not have to enter it every time.
 * 
 * @author Nicolas
 */
public class Preferences implements Serializable
{
    /**
     * The use of URI ensures that host and port data are
     * syntactically correct. Before adding a URI to preferences, the existence 
     * of the host should be confirmed. 
     * 
     */
    private URI uri;
    
    //if user wants auto connection on start, requires mass app to start first
    private boolean autoConnect;
    
    //is user using MASS java or c++ 
    private boolean java;
    
    //directory of MASS program for auto connection
    private String programDirectory;
    
    /**
     * Gets the current URI preferences
     * 
     * @return current URI containing preferred host and port info 
     */
    public URI getURI()
    {
        return uri;
    }
    
    /**
     * Sets the new URI preferences
     * 
     * @param uri the URI containing validated host and port data
     */
    public void setURI(URI uri)
    {
        this.setUri(uri);
    }
    
    public boolean autoConnect()
    {
        return this.isAutoConnect();
    }
    
    public void setAutoConnect(boolean autoConnect)
    {
        this.autoConnect = autoConnect;
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
     * @return the autoConnect
     */
    public boolean isAutoConnect() {
        return autoConnect;
    }

    /**
     * @return the java
     */
    public boolean isJava() {
        return java;
    }

    /**
     * @param java the java to set
     */
    public void setJava(boolean java) {
        this.java = java;
    }

    /**
     * @return the programDirectory
     */
    public String getProgramDirectory() {
        return programDirectory;
    }

    /**
     * @param programDirectory the programDirectory to set
     */
    public void setProgramDirectory(String programDirectory) {
        this.programDirectory = programDirectory;
    }
    
}
