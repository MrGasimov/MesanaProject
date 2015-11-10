/** A contract between a SunEvent source and
  *   listener classes
  */
public interface SunListener {
	 /** Called whenever the sun changes position
	    *   in a SunEvent source object 
	    */
	  public void sunMoved(SunEvent e);
}
