package demo19501;

import base.*;
import java.lang.Math;

class TruckDemo extends Truck {

	// Constructor
	public TruckDemo() {
		this.lastHub = null; // Last hub visited by the truck
		this.onHub = null; // Current hub on which is the truck
		this.nextHub = null; // Next hub to which the truck is heading

		this.onHighway = false; // Boolean to tell if the truck is on a highway currectly or not
		this.hwy = null; // Highway on which the truck is currently

		this.truckStarted = false; // Boolean to tell if the truck has started moving or not

		this.totalTime = 0; // Total time from the start of the program
		this.reachedStation = false; // Boolean to tell if the truck has reached the final station

		truckNum++;
		name = String.valueOf(truckNum);
	}

	// Name of the truck
	@Override
	public String getTruckName() {

		return "Truck19501";
	}

	// The Hub from which it last exited.
	public Hub getLastHub() {		
		return this.lastHub;
	}

	// Truck gets to know it has been added to a highway. 
	// Called in derived classs of Hub
	public void enter(Highway hwy) {
		// System.out.println("Get on the highway" + this.name);
		this.onHighway = true; // Added to a highway
		this.hwy = hwy; // Highway to be which it is added
		this.lastHub = this.onHub; // Changing the last hub to the current hub
		this.nextHub = hwy.getEnd(); // End point of the highway is our next hub to reach

	}

	private void addToHub(int nextDestX, int nextDestY) {

		Location finalDest = Network.getNearestHub(super.getDest()).getLoc();
		int finalX = finalDest.getX();
		int finalY = finalDest.getY();

		// If the hub is the final destination hub then we will not add it to the hub
		if(nextDestX == finalX && nextDestY == finalY) {
			super.setLoc(super.getDest()); // Setting location to final station
			this.lastHub = this.onHub; // Changing the last hub to the current hub
			this.reachedStation = true; // Reached the final destination station
		}
		else {
			if(!this.nextHub.add(this)) {
				return;
			}
			super.setLoc(new Location(nextDestX, nextDestY));
			this.onHub = this.nextHub;
		}
		this.hwy.remove(this);
		this.hwy = null;
		this.onHighway = false;
	}

	// called every deltaT time to update its status/position
	// If less than startTime, does nothing
	// If at a hub, tries to move on to next highway in its route
	// If on a road/highway, moves towards next Hub
	// If at dest Hub, moves towards dest
	protected void update(int deltaT) {
		totalTime += deltaT; // Increasing total time of the program

		// If less than startTime or reached final dest station, does nothing
		if(totalTime < super.getStartTime() || this.reachedStation) {
			return;
		}

		// If truck has not started yet then send it to neearest hub from the station
		if(!this.truckStarted) {
            Hub startHub = Network.getNearestHub(super.getSource()); // Finding nearest hub to the station

			// Adds truck to the start hub
			if(startHub.add(this)) {
				this.truckStarted = true; // Truck has started moving now
				super.setLoc(startHub.getLoc()); // Setting location to start hub
				this.onHub = startHub; // Now the truck is on start hub
			}
			return;
		}

		// If truck is not on a highway i.e. it is on a hub then does nothing
		if(!this.onHighway) {
			return;
		}
		
		Location loc = super.getLoc(); // Current location of the truck
		int truckX = loc.getX(); 
		int truckY = loc.getY(); 

		Location nextDest = this.nextHub.getLoc(); // Location of the next hub
		int nextDestX = nextDest.getX(); 
		int nextDestY = nextDest.getY();

		double distance = (double)(0.005 * deltaT) * hwy.getMaxSpeed();  // Distance covered in deltaT time on the highway

		if(truckX == nextDestX) {
			// Checking this condition separately because of tan 90
			if(Math.abs(nextDestY - truckY) > distance) {
				if(truckY < nextDestY) {
					super.setLoc(new Location(truckX, truckY + (int)Math.round(distance)));
				}
				else {
					super.setLoc(new Location(truckX, truckY - (int)Math.round(distance)));
				}
			}
			// Reached the hub
			else {
				this.addToHub(nextDestX, nextDestY);
			}
		}
		else {
			double angle = Math.atan(Math.abs((double) (nextDestY - truckY)/(nextDestX - truckX))); // Slope angle
			// Distance between truck's loc and next hub's loc
			double distTruckNextHub = (double) Math.sqrt((nextDestX-truckX)*(nextDestX-truckX) + (nextDestY-truckY)*(nextDestY-truckY));

			// Reached the hub
			if(distTruckNextHub < distance) {
				this.addToHub(nextDestX, nextDestY);
			}
			// Still moving on the highway
			else {
				int displaceX = (int) Math.round(distance * Math.cos(angle)); // Displacement in x direction
				int displaceY = (int) Math.round(distance * Math.sin(angle)); // Displacement in y direction

				// Integer coordinate increase
				if(Math.abs(displaceX) < 1) { displaceX = 1; }
				if(Math.abs(displaceY) < 1) { displaceY = 1; }

				displaceX = (truckX < nextDestX) ? Math.abs(displaceX) : -Math.abs(displaceX);
				displaceY = (truckY < nextDestY) ? Math.abs(displaceY) : -Math.abs(displaceY);

				super.setLoc(new Location(truckX + displaceX, truckY + displaceY)); // Moving truck on highway
			}
		}
	}

	private Hub lastHub, onHub , nextHub;
	private boolean onHighway;
	private Highway hwy;
	private boolean truckStarted;
	private int totalTime;
	private boolean reachedStation;

	// Just for testing purposes
	private static int truckNum = 0;
	private String name;
}
