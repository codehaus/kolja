package com.baulsupp.kolja.util.event;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PropertyChangeListenerList extends ListenerList<PropertyChangeListener> {
  public void firePropertyChangeEvent(Object source, String property, Object oldValue, Object newValue) {
    PropertyChangeEvent evt = new PropertyChangeEvent(source, property, oldValue, newValue);

    for (PropertyChangeListener l : listeners) {
      l.propertyChange(evt);
    }
  }

  public void add(PropertyChangeListener listener) {
    listeners.add(listener);
  }
}
