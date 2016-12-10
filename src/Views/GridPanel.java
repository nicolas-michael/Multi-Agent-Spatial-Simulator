/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import edu.uw.bothell.css.dsl.MASS.MassData.UpdatePackage;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

/**
 *
 * @author Nicolas
 */
public class GridPanel extends JPanel {
    
    
    
    Color[] color = new Color[]{new Color(0x0000ff), new Color(0x0033ff), new Color(0x0066ff),
    new Color(0x0099ff), new Color(0x00ccff), new Color(0x00ffff), new Color(0x00ffcc),
    new Color(0x00ff99), new Color(0x00ff66), new Color(0x00ff33), new Color(0x00ff00),
    new Color(0x33ff00), new Color(0x66ff00), new Color(0x99ff00), new Color(0xccff00),
    new Color(0xffff00), new Color(0xffcc00), new Color(0xff9900), new Color(0xff6600),
    new Color(0xff3300), new Color(0xff0000)};

    public GridPanel(Frame frame, int width, int height, UpdatePackage newPackage) {
        

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                gbc.gridx = col;
                gbc.gridy = row;

                CellPanel cellPane = new CellPanel(frame, col, row);
                //cellPane.setCoord(col, row);
                Border border = null;
                if (row < height - 1) {
                    if (col < width - 1) {
                        border = new MatteBorder(1, 1, 0, 0, Color.GRAY);
                    } else {
                        border = new MatteBorder(1, 1, 0, 1, Color.GRAY);
                    }
                } else {
                    if (col < width - 1) {
                        border = new MatteBorder(1, 1, 1, 0, Color.GRAY);
                    } else {
                        border = new MatteBorder(1, 1, 1, 1, Color.GRAY);
                    }
                }
                cellPane.setBorder(border);
                if(newPackage != null)
                {   
                    int index = (int) ((newPackage.getPlaceData()
                            [(row * width) + col]).getThisPlaceData().doubleValue() % 20);
                    if(index < 0) index = 0;
                    
                    int indexAgent = 0;
                    if(newPackage.getPlaceData()[row*width+col].isHasAgents()){
                        indexAgent = (int) ((newPackage.getPlaceData()
                            [(row * width) + col]).getAgentDataOnThisPlace()[0].getDebugData().doubleValue());
                        if(indexAgent < 0) indexAgent = 0;
                    }
                    
                    if(newPackage.getPlaceData()[(row * width) + col].isHasAgents())
                    {
                        if(indexAgent != 20){
                            cellPane.setBackground(Color.BLACK);
                        }else
                        {
                            cellPane.setBackground(color[indexAgent]);
                        }
                    }
                    else
                    {
                        cellPane.setBackground(color[index]);
                    }
                }
               
                add(cellPane, gbc);
            }
        }
        
    }
    
    @Override
        public Dimension getPreferredSize() {
            
            return new Dimension(550, 550);
        }

    public void updateGrid(Number[] places, Number[] agents) {


    }
}
