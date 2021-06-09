package eieruhr;


import eieruhr.model.Model;
import eieruhr.model.ModelEvent;
import eieruhr.model.ModelListener;
import eieruhr.view.Ansicht;
import java.awt.BorderLayout;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Flow;
import javax.swing.JComponent;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Christian
 */
public class Eieruhr extends JComponent implements  ModelListener, Flow.Subscriber<Integer>
{
  private Flow.Subscription subscription;
  private CopyOnWriteArrayList<ModelListener> listenerListe;
  private Model model;
  private Ansicht view;  
  
  public Eieruhr()
  {
    model = new Model();
    view = new Ansicht();
    model.addSubscription(this);
    view.setVisible(true);
    this.setLayout(new BorderLayout());
    this.add(view, BorderLayout.CENTER);
    listenerListe = new CopyOnWriteArrayList<>();
    this.addModelListener(this);
  }

  public void setZahl(int zahl)
  {
    this.firePropertyChange("zahl", model.getVal(), zahl);
    model.setVal(zahl);
  }
  
  public int getZahl()
  {
    return model.getVal();
  }

  public void start()
  {
    model.start();
  }
  
  public void stop()
  {
    model.stop();
  }
  
  
  @Override
  public void onSubscribe(Flow.Subscription subscription)
  {
    this.subscription = subscription;
    subscription.request(1);
  }

  @Override
  public void onError(Throwable thrwbl)
  {
  }

  @Override
  public void onComplete()
  {
  }

  @Override
  public void onNext(Integer item)
  {
    if(item > 0)
    fireModelEvent(new ModelEvent(item));
    if(item == 0)
    fireZeroEvent(new ModelEvent(item));
    
    subscription.request(1);
  }

  @Override
  public void zustandGeaendert(ModelEvent evt)
  {
    view.getZaehler().setText(evt.getSource().toString());
  }
  
  @Override
  public void EventTimerAbgelaufen(ModelEvent evt)
  {
    this.stop();
    view.getZaehler().setText(evt.getSource().toString());
  }
    
  public synchronized void addModelListener(ModelListener horcher)
  {
    listenerListe.add(horcher);
  }
  
  public synchronized void removeModelListener(ModelListener horcher)
  {
    listenerListe.remove(horcher);
  }
  
  public void fireModelEvent(ModelEvent evt)
  {
    listenerListe.forEach(listener -> listener.zustandGeaendert(evt));
  }
  
  public void fireZeroEvent(ModelEvent evt)
  {
    listenerListe.forEach(listener -> listener.EventTimerAbgelaufen(evt));
  }

}
