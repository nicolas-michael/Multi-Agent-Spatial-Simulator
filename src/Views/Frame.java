/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import edu.uw.bothell.css.dsl.MASS.MassData.Preferences;
import Connection.Connection;
import Connection.MASSCppConnection;
import Connection.MASSJavaConnection;
import edu.uw.bothell.css.dsl.MASS.MassData.*;
import static edu.uw.bothell.css.dsl.MASS.MassData.MASSRequest.RequestType.INITIAL_DATA;
import static edu.uw.bothell.css.dsl.MASS.MassData.MASSRequest.RequestType.INJECT_AGENT;
import static edu.uw.bothell.css.dsl.MASS.MassData.MASSRequest.RequestType.INJECT_PLACE;
import static edu.uw.bothell.css.dsl.MASS.MassData.MASSRequest.RequestType.UPDATE_PACKAGE;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.Thread.State;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Nicolas
 */
public class Frame extends javax.swing.JFrame {

    //static instance of this Frame
    private static Frame frameInstance;
    
    //contains data from initial connection (app name & size of places)
    protected InitialData initialData;
    
    //the newest data recieved from mass
    protected UpdatePackage newestPackage;
    
    //the thread listening for mass udates
    private Thread listenThread;
    
    //the users preferences
    private Preferences userPreferences;
    
    //file location of serialized preferences file
    private final String FILE_LOCATION = "src/Preferences/preferences.ser";
    
    //default of 2 seconds between iterations
    private volatile int timeBetweenIterations = 1500;
    
    //Connection objet, contains connection data and funcrions
    private Connection connection;
    
    //linear index of user selected place in GUI grid (linear index = (y * width) + x)
    protected int selectedPlace = -1;
    
    //allows thread to continue while user has pressed play button, false to stop it
    private volatile boolean play;
   
            
    /**
     * Creates new form Frame
     */
    public Frame() 
    {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        while(!this.readPreferences())
        {
            //get users connection preferences, modal true
            SplashDialog dialog = new SplashDialog(this, true);
            dialog.setVisible(true);
        }
        
        this.init();

        listenThread = new Thread()
        {
            public void run()
            {
                while(true)
                {
                    if(play)
                    {
                        getUpdate();
                    }
                    try 
                    {
                        Thread.sleep(timeBetweenIterations);
                    }
                    catch (InterruptedException ex) 
                    {
                        Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
    }
    
    /**
     * iniSetup
     * 
     * initializes a connection if preferences set to auto connect, sets 
     * connection labels, etc.
     */
    private void init()
    {
        String host = userPreferences.getURI().getHost() + ":" 
                + userPreferences.getURI().getPort();
        
        if(userPreferences.isJava())
        {
            connection = new MASSJavaConnection(host);
        }
        else
        {
            connection = new MASSCppConnection(host);
        }
        
        //set host and port in text field
        hostField.setText(userPreferences.getURI().getHost() + ":" 
                + userPreferences.getURI().getPort());
        
        //make connection if autoconnect = true
        if(userPreferences.autoConnect())
        {
            if(!performAutoConnect())
            {
                connectionError("Auto connect failed");
                return;
            }
            errorLabel.setText("Connected");
            errorButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/connected.png")));
        }
        else
        {
            errorLabel.setText("Not Connected");
            errorButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/notconnected.png")));
        }
    }
    
    private void connectionError(String error)
    {
        //sidplay user error
        errorLabel.setText(error);
        errorButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/notconnected.png")));
        
        //null all data for fresh start
        initialData = null;
        newestPackage = null;
        play = false;
        listenThread = null;
        connection = null;
        selectedPlace = -1;
        
        //disable everything except connect button
        PlayButton.setEnabled(false);
        nextButton.setEnabled(false);
        pauseButton.setEnabled(false);
        placesValueField.setEditable(false);
        agentValueField.setEditable(false);
        sendPlaceButton.setEnabled(false);
        agentInjectButton.setEnabled(false);
        
        //clear the labels and grid
        xLabel.setText("");
        yLabel.setText("");
        agentLabel.setText("None");
        placeLabel.setText("None");
        Grid.removeAll();
        Grid.repaint();
        Grid.revalidate();
        
        //display prefered host/port
        hostField.setText(userPreferences.getURI().getHost() + ":" 
                + userPreferences.getURI().getPort());
    }
    
    private boolean readPreferences()
    {
        //serialize the preferences
        try 
        {
            FileInputStream fin = new FileInputStream(FILE_LOCATION);
            ObjectInputStream is = new ObjectInputStream(fin);
            userPreferences = (Preferences)is.readObject();
            fin.close();
            is.close();
        } 
        catch (FileNotFoundException ex) 
        {
            return false;
        } 
        catch (IOException | ClassNotFoundException ex) 
        {
            return false;
        }
        
        return true;
    }
    
    /**
     * getGridHeight
     * 
     * Returns the height of JPanel Grid. This is used to adjust the size of
     * each CellPane so as to fit inside the visible Panel and avoid users
     * resizing screen. 
     * 
     * @return the height of the current Grid JPanel
     */
    public static Dimension getGridDimension()
    {
        int width = Frame.frameInstance.Grid.getWidth();
        int height = Frame.frameInstance.Grid.getHeight();
        
        return new Dimension(width, height);
    }
    
    private boolean performAutoConnect()
    {
        //start user program
        
        //connect
        connectButtonActionPerformed(null);
        
        return connection.isConnected();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        Grid = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        pauseButton = new javax.swing.JButton();
        PlayButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        errorButton = new javax.swing.JButton();
        errorLabel = new javax.swing.JLabel();
        controlPanel = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        agentList = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        agentNameLabel = new javax.swing.JLabel();
        agentStatusLabel = new javax.swing.JLabel();
        agentIndexLabel = new javax.swing.JLabel();
        agentValueField = new javax.swing.JTextField();
        agentInjectButton = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        placesNameLabel = new javax.swing.JLabel();
        numAgentsLabel = new javax.swing.JLabel();
        placesValueField = new javax.swing.JTextField();
        sendPlaceButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        hostField = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        connectButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        xLabel = new javax.swing.JLabel();
        yLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        placeLabel = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        agentLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setFocusTraversalPolicyProvider(true);

        Grid.setBackground(new java.awt.Color(27, 27, 27));
        Grid.setForeground(new java.awt.Color(204, 204, 204));
        Grid.setLayout(new java.awt.GridBagLayout());
        getContentPane().add(Grid, java.awt.BorderLayout.CENTER);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(740, 64));
        jPanel1.setRequestFocusEnabled(false);
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(250, 77));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        pauseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/pause.png"))); // NOI18N
        pauseButton.setBorder(null);
        pauseButton.setBorderPainted(false);
        pauseButton.setContentAreaFilled(false);
        pauseButton.setEnabled(false);
        pauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(13, 5, 13, 5);
        jPanel2.add(pauseButton, gridBagConstraints);

        PlayButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/playbutton.png"))); // NOI18N
        PlayButton.setBorder(null);
        PlayButton.setBorderPainted(false);
        PlayButton.setContentAreaFilled(false);
        PlayButton.setEnabled(false);
        PlayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PlayButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(13, 5, 13, 5);
        jPanel2.add(PlayButton, gridBagConstraints);

        nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/next.png"))); // NOI18N
        nextButton.setBorder(null);
        nextButton.setBorderPainted(false);
        nextButton.setContentAreaFilled(false);
        nextButton.setEnabled(false);
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(13, 5, 13, 5);
        jPanel2.add(nextButton, gridBagConstraints);

        jPanel1.add(jPanel2, java.awt.BorderLayout.WEST);

        jPanel3.setBackground(new java.awt.Color(51, 51, 51));
        jPanel3.setForeground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanel5.setBackground(new java.awt.Color(51, 51, 51));
        jPanel5.setMinimumSize(new java.awt.Dimension(250, 64));
        jPanel5.setPreferredSize(new java.awt.Dimension(250, 64));

        errorButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/notconnected.png"))); // NOI18N
        errorButton.setToolTipText("View details");
        errorButton.setBorder(null);
        errorButton.setBorderPainted(false);
        errorButton.setContentAreaFilled(false);
        errorButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/notconnectedpressed.png"))); // NOI18N
        errorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                errorButtonActionPerformed(evt);
            }
        });

        errorLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        errorLabel.setForeground(new java.awt.Color(204, 204, 204));
        errorLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        errorLabel.setText("No connection");
        errorLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        errorLabel.setMinimumSize(new java.awt.Dimension(300, 15));
        errorLabel.setPreferredSize(new java.awt.Dimension(300, 15));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(errorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(errorButton)
                .addGap(18, 18, 18))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(errorButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(errorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 341, 0, 1);
        jPanel3.add(jPanel5, gridBagConstraints);

        jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        controlPanel.setBackground(new java.awt.Color(51, 51, 51));
        controlPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        controlPanel.setPreferredSize(new java.awt.Dimension(250, 100));
        controlPanel.setLayout(new java.awt.BorderLayout(0, 10));

        jPanel9.setBackground(new java.awt.Color(51, 51, 51));
        jPanel9.setLayout(new java.awt.BorderLayout());

        jPanel10.setBackground(new java.awt.Color(51, 51, 51));
        jPanel10.setForeground(new java.awt.Color(204, 204, 204));

        jScrollPane1.setBackground(new java.awt.Color(51, 51, 51));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jScrollPane1.setForeground(new java.awt.Color(204, 204, 204));
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        agentList.setBackground(new java.awt.Color(51, 51, 51));
        agentList.setForeground(new java.awt.Color(204, 204, 204));
        agentList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        agentList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                agentListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(agentList);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 204, 204));
        jLabel1.setText("  Agents");

        jPanel11.setBackground(new java.awt.Color(51, 51, 51));

        jPanel15.setBackground(new java.awt.Color(51, 51, 51));
        jPanel15.setMaximumSize(new java.awt.Dimension(32767, 150));
        jPanel15.setMinimumSize(new java.awt.Dimension(0, 150));
        jPanel15.setPreferredSize(new java.awt.Dimension(66, 120));

        jLabel14.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(204, 204, 204));
        jLabel14.setText("  Agent Details");

        jPanel16.setBackground(new java.awt.Color(51, 51, 51));
        jPanel16.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel16.setForeground(new java.awt.Color(204, 204, 204));
        jPanel16.setMaximumSize(new java.awt.Dimension(32767, 150));

        jLabel15.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(204, 204, 204));
        jLabel15.setText("Name : ");

        jLabel16.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(204, 204, 204));
        jLabel16.setText("Value : ");

        jLabel17.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(204, 204, 204));
        jLabel17.setText("Status : ");

        jLabel18.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(204, 204, 204));
        jLabel18.setText("Index : ");

        agentNameLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        agentNameLabel.setForeground(new java.awt.Color(204, 204, 204));

        agentStatusLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        agentStatusLabel.setForeground(new java.awt.Color(204, 204, 204));

        agentIndexLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        agentIndexLabel.setForeground(new java.awt.Color(204, 204, 204));

        agentValueField.setEditable(false);
        agentValueField.setBackground(new java.awt.Color(70, 70, 70));
        agentValueField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        agentValueField.setForeground(new java.awt.Color(204, 204, 204));
        agentValueField.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        agentValueField.setPreferredSize(new java.awt.Dimension(59, 19));

        agentInjectButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/upef.png"))); // NOI18N
        agentInjectButton.setBorder(null);
        agentInjectButton.setBorderPainted(false);
        agentInjectButton.setContentAreaFilled(false);
        agentInjectButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/upd.png"))); // NOI18N
        agentInjectButton.setEnabled(false);
        agentInjectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agentInjectButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(agentNameLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(agentStatusLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(agentIndexLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(agentValueField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(agentInjectButton, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(agentNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(agentStatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(agentInjectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel16)
                                .addComponent(agentValueField, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel17)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel18)
                    .addComponent(agentIndexLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel9.add(jPanel10, java.awt.BorderLayout.NORTH);

        controlPanel.add(jPanel9, java.awt.BorderLayout.CENTER);

        jPanel13.setBackground(new java.awt.Color(51, 51, 51));
        jPanel13.setMaximumSize(new java.awt.Dimension(32767, 150));
        jPanel13.setMinimumSize(new java.awt.Dimension(0, 150));
        jPanel13.setPreferredSize(new java.awt.Dimension(66, 120));

        jLabel10.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(204, 204, 204));
        jLabel10.setText("  Places");

        jPanel14.setBackground(new java.awt.Color(51, 51, 51));
        jPanel14.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel14.setForeground(new java.awt.Color(204, 204, 204));
        jPanel14.setMaximumSize(new java.awt.Dimension(32767, 150));

        jLabel11.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(204, 204, 204));
        jLabel11.setText("Name : ");

        jLabel12.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(204, 204, 204));
        jLabel12.setText("Value : ");

        jLabel13.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(204, 204, 204));
        jLabel13.setText("Agents :");

        placesNameLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        placesNameLabel.setForeground(new java.awt.Color(204, 204, 204));

        numAgentsLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        numAgentsLabel.setForeground(new java.awt.Color(204, 204, 204));

        placesValueField.setEditable(false);
        placesValueField.setBackground(new java.awt.Color(70, 70, 70));
        placesValueField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        placesValueField.setForeground(new java.awt.Color(204, 204, 204));
        placesValueField.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        placesValueField.setPreferredSize(new java.awt.Dimension(59, 19));

        sendPlaceButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/upef.png"))); // NOI18N
        sendPlaceButton.setBorder(null);
        sendPlaceButton.setBorderPainted(false);
        sendPlaceButton.setContentAreaFilled(false);
        sendPlaceButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/upd.png"))); // NOI18N
        sendPlaceButton.setEnabled(false);
        sendPlaceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendPlaceButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(placesNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(numAgentsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(placesValueField, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendPlaceButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11)
                    .addComponent(placesNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sendPlaceButton, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(placesValueField, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel13)
                    .addComponent(numAgentsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        controlPanel.add(jPanel13, java.awt.BorderLayout.NORTH);

        getContentPane().add(controlPanel, java.awt.BorderLayout.WEST);

        jPanel4.setBackground(new java.awt.Color(51, 51, 51));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setPreferredSize(new java.awt.Dimension(809, 50));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel6.setBackground(new java.awt.Color(51, 51, 51));
        jPanel6.setPreferredSize(new java.awt.Dimension(250, 40));
        jPanel6.setLayout(new java.awt.GridBagLayout());

        hostField.setBackground(new java.awt.Color(70, 70, 70));
        hostField.setForeground(new java.awt.Color(204, 204, 204));
        hostField.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        hostField.setMinimumSize(new java.awt.Dimension(0, 23));
        hostField.setPreferredSize(new java.awt.Dimension(240, 20));
        hostField.setSelectionColor(new java.awt.Color(51, 51, 51));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 238;
        gridBagConstraints.ipady = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(11, 0, 8, 5);
        jPanel6.add(hostField, gridBagConstraints);

        jPanel4.add(jPanel6, java.awt.BorderLayout.WEST);

        jPanel8.setBackground(new java.awt.Color(51, 51, 51));

        connectButton.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        connectButton.setForeground(new java.awt.Color(204, 204, 204));
        connectButton.setText("Connect");
        connectButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        connectButton.setContentAreaFilled(false);
        connectButton.setPreferredSize(new java.awt.Dimension(49, 20));
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 204, 204));
        jLabel2.setText("X : ");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(204, 204, 204));
        jLabel3.setText("Y : ");

        xLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        xLabel.setForeground(new java.awt.Color(204, 204, 204));
        xLabel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        xLabel.setPreferredSize(new java.awt.Dimension(2, 23));

        yLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        yLabel.setForeground(new java.awt.Color(204, 204, 204));
        yLabel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        yLabel.setPreferredSize(new java.awt.Dimension(2, 23));

        jLabel6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(204, 204, 204));
        jLabel6.setText("Places : ");

        placeLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        placeLabel.setForeground(new java.awt.Color(204, 204, 204));
        placeLabel.setText("None");

        jLabel8.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(204, 204, 204));
        jLabel8.setText("Agents : ");

        agentLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        agentLabel.setForeground(new java.awt.Color(204, 204, 204));
        agentLabel.setText("None");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(connectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel2)
                .addGap(10, 10, 10)
                .addComponent(xLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(yLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(placeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(agentLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(connectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel2))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(xLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(yLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(placeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(agentLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanel4.add(jPanel8, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel4, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void PlayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PlayButtonActionPerformed
        if(!connection.isConnected()) return;
        
        play = true;
        pauseButton.setEnabled(true);
        PlayButton.setEnabled(false);
        nextButton.setEnabled(false);
        connectButton.setEnabled(false);
        placesValueField.setEditable(false);
        agentValueField.setEditable(false);
        sendPlaceButton.setEnabled(false);
        agentInjectButton.setEnabled(false);
        
        State state = listenThread.getState();
        if(state.name().equals(State.NEW.toString()))
        {
            listenThread.start();
        }
    }//GEN-LAST:event_PlayButtonActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        play = false;
        this.getUpdate();
    }//GEN-LAST:event_nextButtonActionPerformed

    private void pauseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseButtonActionPerformed
        play = false;
        pauseButton.setEnabled(false);
        PlayButton.setEnabled(true);
        nextButton.setEnabled(true);
        connectButton.setEnabled(true);
        placesValueField.setEditable(true);
        agentValueField.setEditable(true);
        System.out.println("pause pressed");
    }//GEN-LAST:event_pauseButtonActionPerformed

    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed
        System.out.println(this.connection.isConnected());
        if(this.connection.isConnected())
        {
            //todo: tell mass we are diconnecting
            this.connection.endConnection();
            connectButton.setText("Connect");
            errorLabel.setText("Disconnected");
            errorButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/notconnected.png")));
            return;
        }
        
        this.connection.setHostAndPort(hostField.getText().trim());
        if(!this.connection.connect())
        {
            connectButton.setText("Connect");
            errorLabel.setText("Connection Error");
            errorButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/notconnected.png")));
            return;
        }
        
        MASSPacket iniPacket = this.connection.makeRequest
            (new MASSRequest(INITIAL_DATA, new InitialData()));
        
        
        if(iniPacket == null)
        {
            connectButton.setText("Connect");
            errorLabel.setText("Connection Error");
            errorButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/notconnected.png")));
            return;
        }
  
        initialData = (InitialData) iniPacket;
        placeLabel.setText(Integer.toString(initialData.getNumberOfPlaces()));
        agentLabel.setText(Integer.toString(initialData.getNumberOfAgents()));
        placesNameLabel.setText(initialData.getPlacesName());
        agentNameLabel.setText(initialData.getAgentsName());
        connectButton.setText("Disconnect");
        errorLabel.setText("Connected");
        PlayButton.setEnabled(true);
        nextButton.setEnabled(true);
        errorButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/connected.png")));
        
    }//GEN-LAST:event_connectButtonActionPerformed

    private void agentListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_agentListValueChanged
        if(!(agentList.getSelectedIndex() >= 0)) return;
        
        AgentData data = this.newestPackage.getPlaceData()
                [selectedPlace].getAgentDataOnThisPlace()[agentList.getSelectedIndex()];
        
        agentValueField.setText(data.getDebugData().toString());
        String status = (data.isAlive()) ? "Alive" : "Dead";
        agentStatusLabel.setText(status);
        agentIndexLabel.setText((data.getIndex() / initialData.getPlacesX()) + ", " 
                + (data.getIndex() % initialData.getPlacesX()));
    }//GEN-LAST:event_agentListValueChanged

    private void sendPlaceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendPlaceButtonActionPerformed
        if(placesValueField.getText().isEmpty()) return;
        
        Number number = null;
        
        try 
        {
            //create Number subclass of users choice
            Constructor con = initialData.getPlaceDataType().getConstructor(new Class[]{String.class});
            number = (Number) con.newInstance(placesValueField.getText());
        } 
        catch (NumberFormatException ex)
        {
            //user did not enter a valid number corresponding to their chosen data type
            this.placesValueField.setText("NO!");
            return;
        } 
        catch (NoSuchMethodException | SecurityException | InstantiationException 
                | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) 
        {
            this.placesValueField.setText("NO!");
            return;
        }
        
        
        PlaceData placeToInject = new PlaceData();
        placeToInject.setThisPlaceData(number);
        placeToInject.setIndex(selectedPlace);
        
        MASSRequest request = new MASSRequest(INJECT_PLACE, placeToInject);
        connection.makeRequest(request);
    }//GEN-LAST:event_sendPlaceButtonActionPerformed

    private void agentInjectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agentInjectButtonActionPerformed
        if(agentValueField.getText().isEmpty()) return;
        
        Number number = null;
        
        try 
        {
            //create Number subclass of users choice
            Constructor con = initialData.getAgentDataType().getConstructor(new Class[]{String.class});
            number = (Number) con.newInstance(agentValueField.getText());
        } 
        catch (NumberFormatException ex)
        {
            //user did not enter a valid number sorresponding to their chosen data type
            this.agentValueField.setText("NO!");
            return;
        } 
        catch (NoSuchMethodException | SecurityException | InstantiationException 
                | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) 
        {
            this.agentValueField.setText("NO!");
            return;
        }
        
        AgentData agentToInject = new AgentData();
        agentToInject.setDebugData(number);
        agentToInject.setIndex(selectedPlace);
        agentToInject.setId(newestPackage.getPlaceData()
                [selectedPlace].getAgentDataOnThisPlace()[agentList.getSelectedIndex()].getId());
        
        MASSRequest request = new MASSRequest(INJECT_AGENT, agentToInject);
        connection.makeRequest(request);
    }//GEN-LAST:event_agentInjectButtonActionPerformed

    private void errorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_errorButtonActionPerformed
        ConnectionSettingsDialog dialog = new ConnectionSettingsDialog(this, true, this.userPreferences);
        dialog.setVisible(true);
    }//GEN-LAST:event_errorButtonActionPerformed

    private void getUpdate()
    {
        MASSPacket newPacket;
        
        if(!userPreferences.isJava() && newestPackage == null)
        {
            newestPackage = new UpdatePackage(initialData);
        }
        newPacket = this.connection.makeRequest
            (new MASSRequest(UPDATE_PACKAGE, newestPackage));
        
        //todo - handle better
        if(newPacket == null)
        {
            errorLabel.setText("Connection error");
            errorButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/notconnected.png")));
            return;
        }
        
        UpdatePackage thisPackage = (UpdatePackage) newPacket;
        this.newestPackage = thisPackage;
        this.updateGrid();
    }
    
    public void setCoord(int x, int y)
    {
        xLabel.setText(Integer.toString(x));
        yLabel.setText(Integer.toString(y));
    }
    
    private void updateGrid()
    {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0;
        Grid.removeAll();
        Grid.add(new GridPanel(this, initialData.getPlacesX(), initialData.getPlacesY(), 
                                this.newestPackage));
        
        Grid.revalidate();
        Grid.repaint();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        Frame myFrame = new Frame();
        Frame.frameInstance = myFrame;
        myFrame.setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Grid;
    private javax.swing.JButton PlayButton;
    javax.swing.JLabel agentIndexLabel;
    protected javax.swing.JButton agentInjectButton;
    private javax.swing.JLabel agentLabel;
    javax.swing.JList agentList;
    javax.swing.JLabel agentNameLabel;
    javax.swing.JLabel agentStatusLabel;
    javax.swing.JTextField agentValueField;
    private javax.swing.JButton connectButton;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JButton errorButton;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JTextField hostField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton nextButton;
    javax.swing.JLabel numAgentsLabel;
    private javax.swing.JButton pauseButton;
    private javax.swing.JLabel placeLabel;
    javax.swing.JLabel placesNameLabel;
    javax.swing.JTextField placesValueField;
    protected javax.swing.JButton sendPlaceButton;
    private javax.swing.JLabel xLabel;
    private javax.swing.JLabel yLabel;
    // End of variables declaration//GEN-END:variables
}
