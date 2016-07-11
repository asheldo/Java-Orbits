package orbits.model;

import orbits.Simulation;

import java.util.Random;
import java.util.logging.Level;

public class Planet {

	private static int counter = 0;

	public final int index = ++counter;
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
	private int absorbed;
	private double radius;

	// private Board board;

	// Initializes a planet with a given x and y coordinate, mass, and x and y velocity components

	public Planet(double xpos, double ypos, int mass, double dx, double dy, boolean fixed) {
		this.xpos = xpos;
		this.ypos = ypos;
		setMass(mass);
		this.dx = dx;
		this.dy = dy;
		this.fixed = fixed;
	}

	public Planet(Planet p) {
		this.xpos = p.x();
		this.ypos = p.y();
		this.mass = p.getMass();
		this.radius = p.getRadius();
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
		if (Double.isNaN(x)) {
			throw new RuntimeException("move");
		}
		xpos = x;
	}
	public void setY(double y) {
		if (Double.isNaN(y)) {
			throw new RuntimeException("move");
		}
		ypos = y;
	}
	// Will be needed for determining the forces that this planet acts upon others with.

	public double getMass() {
		return mass;
	}

	/**
	 * If the distance is closer than the 2 planet radii (one diameter)
	 * double thresholdCollisionDistance = ...;
	 *
	 * So, 4 * Pi * (radius of the sphere * radius of the sphere * radius of the sphere) / 3
	 *
	 * @param mass - zero will be translated to .0000001 arbitrarily small non-zero (negatives preserved)
     */
	public void setMass(double mass) {
		if (this.mass < 0) {
			this.radius = 0;
		}
		else if (this.mass == 0 || (mass > 0)) {
			this.radius = estimateRadius(mass, getDensity(mass));
		}
		this.mass = mass == 0 ? .0000001 : mass;
	}

	private static final double fourThirdsPI = 4. * Math.PI / 3.;

	public static double estimateRadius(double mass, double density) {
		double vol = mass / density;
		double radiusEst = Math.pow(vol * fourThirdsPI, 0.33);
		return radiusEst;
	}

	/**
	 *
	 * @return Sun is .255 as dense as terrestrial planet
     */
	public static double getDensity(double mass) {
		return mass > 10000 ? .255 : 1;
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
		if (Double.isNaN(dx)) {
			throw new RuntimeException("move");
		}
		this.dx = dx;
	}

	public void setDy(double dy) {
		if (Double.isNaN(dy)) {
			throw new RuntimeException("move");
		}
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
			dx = String.format("%1.2f", this.dx);
			dy = String.format("%1.2f", this.dy);
		}
		return "\tX,Y=" + String.format("%1.2f", this.xpos) +
				"," + String.format("%1.2f", this.ypos) +
				(this.xpos == 0 && this.ypos == 0. ? "\t" : "") +
				"\tM=" + String.format("%1.2f", this.mass) +
				"\tRad=" + String.format("%1.2f", this.radius) +
				"\tDX,DY=" + dx + "," + dy +
				"\t++=" + absorbed
				+ "\n";
	}

	// Adds velocity values to position
	public void move(double dt) {
		if (Double.isNaN(dt)) {
			throw new RuntimeException("move");
		}
		if(!this.fixed){
			this.xpos += dt * this.dx;
			this.ypos += dt * this.dy;
		}
	}

	private static Random random = new Random();

	/**
	 * Distribute around circle.
	 * Also change motion to circular in same, cc direction
	 */
	public void randomizeOnCircle(double dt) {
		double rand = random.nextDouble();

		move(dt * rand); // randomize off line

		// now distribute around circle
		double randAngle = 90. * rand;
		double sin = Math.sin(randAngle);
		double cos = Math.cos(randAngle);
		double d = Math.sqrt(Math.pow(xpos, 2) + Math.pow(ypos, 2));
		double vec = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
		double xa = sin;
		double ya = cos;

		int randQuadrant = random.nextInt(4);
		if (randQuadrant == 0) {
			xpos = d * xa;
			ypos = d * ya;
			dx = -vec * ya;
			dy = vec * xa;
		}
		else if (randQuadrant == 1) {
			xpos = d * xa;
			ypos = -d * ya;
			dx = vec * ya;
			dy = vec * xa;
		}
		else if (randQuadrant == 2) {
			xpos = -d * xa;
			ypos = -d * ya;
			dx = vec * ya;
			dy = -vec * xa;
		}
		if (randQuadrant == 3) {
			xpos = -d * xa;
			ypos = d * ya;
			dx = -vec * ya;
			dy = -vec * xa;
		}
		Simulation.getInstance().logState("Rand: " + randQuadrant + " vec=" + vec, Level.INFO);
		Simulation.getInstance().logState(this.toString(), Level.INFO);
	}

	public void absorbed(Planet p2) {
		++absorbed;
	}

	/**
	 * @return estimated by mass and type
     */
	public double getRadius() {
		return radius;
	}

	public int getQuadrant(Planet origin) {
		double x = xpos - origin.xpos, y = ypos - origin.ypos;;
		if (x >= 0 && y >= 0) {
			return 0;
		}
		if (x >= 0 && y < 0) {
			return 1;
		}
		if (x < 0 && y < 0) {
			return 2;
		}
		return 3;
	}
}