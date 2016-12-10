/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import edu.uw.bothell.css.dsl.MASS.MassData.AgentData;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;

/**
 *
 * @author Nicolas
 */
public class CellPanel extends JPanel
{
    private int x, y;

        public CellPanel(Frame frame, int x, int y) 
        {
            
            this.x = x;
            this.y = y;
            
            addMouseListener(new MouseAdapter() 
            {
                @Override
                public void mouseEntered(MouseEvent e) 
                {
                    frame.setCoord(x, y);
                }

                @Override
                public void mouseExited(MouseEvent e) 
                {
                }
                
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    int width = frame.initialData.getPlacesX();
                    int index = frame.selectedPlace = (width * y) + x;
                    
                    String placeData = frame.newestPackage.getPlaceData()[index].getThisPlaceData().toString();
                    AgentData[] agents = frame.newestPackage.getPlaceData()[index].getAgentDataOnThisPlace();
                    int numAgents = agents.length;
                    
                    frame.placesValueField.setText(placeData);
                    frame.numAgentsLabel.setText(Integer.toString(numAgents));
                    
                    DefaultListModel model = new DefaultListModel();
                    
                    for(int i = 0; i < agents.length; i++)
                    {
                        model.addElement("Agent #" + agents[i].getId());
                    }
                    
                    frame.agentList.setModel(model);
                    
                    if(agents.length > 0)
                    {
                        frame.agentList.setSelectedIndex(0);
                    }
                    frame.sendPlaceButton.setEnabled(true);
                    frame.placesValueField.setEditable(true);
                    boolean enable = (agents.length > 0);
                    frame.agentInjectButton.setEnabled(enable);
                    frame.agentValueField.setEditable(enable);
                }
            });
        }
        

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(6, 6);//6, 6
        }
}
