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
public abstract class MASSPacket implements Serializable
{
    public abstract String toJSONString();
}
