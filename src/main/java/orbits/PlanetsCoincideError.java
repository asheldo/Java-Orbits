package orbits;

import orbits.Planet;

public class PlanetsCoincideError extends Exception {

	/* Who knows what the heck this thing-a-ma-bobber does?! I just put it in
	 here to make the squiggly lines go away :) 
									 |
									 v     */
	private static final long serialVersionUID = 1L;
	// It's b/c Exception is Serializable -^  ;)

	Planet p;
	
	public PlanetsCoincideError(Planet p) {
		this.p = p;
	}
	
	public String getMessage()
	{
		// throws a custom error message in your face if you try to... well... does anyone even read the error messages?
		return "\n\t               You are trying to add a planet that coincides with a planet that already exists at (" + p.x() + ", " + p.y() +")";
	}
	
}
