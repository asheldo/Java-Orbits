package orbits.model;

public class Planet {

	// Store X and Y coordinates of the planet in 2D space

	private double xpos;
	private double ypos;


	// Stores a mass value, required to determine the accelerations of the planet

	private double mass;


	// Stores x and y components of the velocity vector, which are added to the
	// planet's position to create motion, and change as forces act upon a planet

	private double dx;
	private double dy;
	private boolean fixed;

	// private Board board;

	// Initializes a planet with a given x and y coordinate, mass, and x and y velocity components

	public Planet(double xpos, double ypos, int mass, double dx, double dy, boolean fixed) {

		this.xpos = xpos;
		this.ypos = ypos;
		this.mass = mass;
		this.dx = dx;
		this.dy = dy;
		this.fixed = fixed;
	}

	public Planet(Planet p) {
		this.xpos = p.x();
		this.ypos = p.y();
		this.mass = p.getMass();
		this.dx = p.getDx();
		this.dy = p.getDy();

		this.fixed = p.getFixed();
		// this.setBoard(p.getBoard());
	}

/*
	public double[] getCoords() {
		double xTranslate = getBoard().getWidth()/2 + xpos - 5;
		double yTranslate = getBoard().getHeight()/2 - ypos - 5;
		return  new double[] {xTranslate, yTranslate};
	}
*/

/*
	public void setCoords(double x, double y) {
		this.xpos = x;
		this.ypos = y;
	}
*/

	// These return raw x and y coordinates, they are short because it's short and readable
	// for when I'm doing the math.
	public double x() {
		return xpos;
	}
	
	public double y() {
		return ypos;
	}

	public void setX(double x) {
		xpos = x;
	}
	public void setY(double y) {
		ypos = y;
	}
	// Will be needed for determining the forces that this planet acts upon others with.

	public double getMass() {
		return this.mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}
	
	public boolean getFixed() {
		return this.fixed;
	}
	
	public double getDx() {
		return dx;
	}
	
	public double getDy() {
		return dy;
	}

	public void setDx(double dx) {

		this.dx = dx;
	}

	public void setDy(double dy) {

		this.dy = dy;
	}
	
	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

	// toString
	public String toString() {
		String dx = "";
		String dy = "";
		if(this.fixed){
			dx = "Fixed";
			dy = "Fixed";
		} else {
			dx = String.valueOf(this.dx);
			dy = String.valueOf(this.dy);
		}
		return "\tX: " + this.xpos + "\tY: " + this.ypos + "\tMass: " + this.mass + "\tX velocity: " + dx + "\tY velocity: " + dy
				+ "\n";
	}

	// Adds velocity values to position
	public void move(double dt) {
		if(!this.fixed){
			this.xpos += dt * this.dx;
			this.ypos += dt * this.dy;
		}
	}

}