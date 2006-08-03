package jcurses.event;

import java.util.ArrayList;
import java.util.List;

/**
*  This class is the basic class  for listener manager. Listener manager are used 
* in widgets classes to manage listeners on widgets's events. This can be 
* also implemented without an listener manager, but these make it easier.
* 
* @author Alexei Chmelev
 * @param <T>
*/
public abstract class ListenerManager<T, U extends Event> {
	
	private List<T> _listeners = new ArrayList<T>();
	
    
    /**
    *  The method adds a listener to the list of managed listeners.
    * 
    * @param listener The listener to be added. The listener must be of right type for 
    * listened events.
    */
	public void  addListener(T listener) {
		_listeners.add(listener);
	}
    
    
    /**
    *  The method removes a listener from the list of managed listeners.
    * 
    * @param listener The listener to be removed from the list. 
    */
	public void  removeListener(T listener) {
		_listeners.remove(listener);
	}
	
	
    /**
    *  The method handles an occured event. This method is called in a widget
    *  if an event is occured and has to be delegated to listeners.
    * 
    * @param event event to be handled
    */
	public void handleEvent(U event) {
		for (int i=0; i<_listeners.size(); i++) {
			doHandleEvent(event, _listeners.get(i));
		}
	}
	
    
    /**
    *  The method is called by <code>handleElent<code> for each registered listener.
    * In order this method has to call an method of the listener object with handled event as parameter
    * 
    * @param event event to be handled
    * @param listener listener to handle
    */
	protected abstract void doHandleEvent(U event, T listener);
	
	
    /**
    *  The method returns the number of listeners registered by this listener manager
    * 
    * @return number of registered listeners
    */
	public int countListeners() {
		return _listeners.size();
	}
	
	
	

}
