/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eieruhr.model;

import java.util.EventListener;

/**
 *
 * @author Christian
 */
public interface ModelListener extends EventListener
{
    public void zustandGeaendert(ModelEvent evt);
    public void EventTimerAbgelaufen(ModelEvent evt);
}
