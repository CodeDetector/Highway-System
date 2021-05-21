package demo19027;

import java.util.*;
import base.*;

public class TruckDemo extends Truck {
	TruckDemo() {
		onHighway = false;
		lastHub = null;
		hubs = new ArrayList<>();
		waitingOnHub = false;
	}

	@Override
	protected synchronized void update(int deltaT) {

		totalTime = totalTime + deltaT;

		if (this.getStartTime() > totalTime) {
			return;
		} else {

			int currY = this.getLoc().getY();
			int currX = this.getLoc().getX();

			if (currY == this.getSource().getY() && currX == this.getSource().getX()) {
				Hub closeHub = Network.getNearestHub(this.getSource());
				if (closeHub.add(this)) {
					this.waitingOnHub = true;
					this.onHighway = false;
					this.setLoc(closeHub.getLoc());
				}
			} else {

				Hub destHub = Network.getNearestHub(this.getDest());
				if (currX == destHub.getLoc().getX() && currY == destHub.getLoc().getY()) {
					this.setLoc(this.getDest());
					this.waitingOnHub = false;
					this.onHighway = false;
					this.lastHub = destHub;

				} else if (WaitingonHighway) {
					Hub endHub = this.currHighway.getEnd();
					if (endHub.add(this)) {
						this.waitingOnHub = true;
						this.onHighway = false;
						this.WaitingonHighway = false;
						this.currHighway.remove(this);
						hubs.add(endHub);
					} else {
						this.onHighway = false;
						this.WaitingonHighway = true;
					}

					this.setLoc(endHub.getLoc());

				} else if (onHighway) {


					int Y1 = currHighway.getStart().getLoc().getY();
					int X1 = currHighway.getStart().getLoc().getX();
					int Y2 = currHighway.getEnd().getLoc().getY();
					int X2 = currHighway.getEnd().getLoc().getX();

					double length = Math.sqrt((Y2 - Y1) * (Y2 - Y1) + (X2 - X1) * (X2 - X1)); 

					int destX, destY;

					int distTravelled = this.speed * deltaT / 100;

					destX = currX + (int) (distTravelled * (X2 - X1) / length);
					destY = currY + (int) (distTravelled * (Y2 - Y1) / length);

					double highwayLength = Math.sqrt(Math.pow(Y1 - Y2, 2) + Math.pow(X1 - X2, 2));
					this.totalDistancetoTravel += distTravelled;

					if (totalDistancetoTravel >= highwayLength) {

						Hub endHub = this.currHighway.getEnd();

						if (endHub.add(this))
					    {
							this.onHighway = false;
							this.waitingOnHub = true;
							hubs.add(endHub);
							this.currHighway.remove(this);
							this.WaitingonHighway = false;
						} 
						else {
							this.onHighway = false; 
							this.WaitingonHighway=true;
						}

						this.setLoc(endHub.getLoc()); 

					} else {
						Location destinationLoc = new Location(destX, destY); // destinationLocation
						this.setLoc(destinationLoc); // set Location
						this.WaitingonHighway = false;
						this.waitingOnHub = false;
						this.onHighway = true;
					}

				}

			}
		}

	}

	// getting last hub
	public Hub getLastHub() {
		return lastHub;
	}

	// truck name
	public String getTruckName() {
		return "Truck19027";
	}

	// enter function
	public void enter(Highway currHighway) {
		this.totalDistancetoTravel = 0;  // totalDistancetoTravel =  covered by truck initially is zero
		this.onHighway = true;
		this.currHighway = currHighway;  
		this.speed = currHighway.getMaxSpeed(); // running at maximum speed
		this.lastHub = currHighway.getStart(); //last hub is hub from where truck came from
		this.waitingOnHub = false;  // initially false
		this.WaitingonHighway = false;  // initially false

	}

	// new members
	private Hub lastHub; // last hub visited
	private ArrayList<Hub> hubs;
	private static int totalTime = 0;
	private Highway currHighway;
	private boolean onHighway; // on highway is true when truck is moving on highway.
	private boolean waitingOnHub; // waitingOnHub is true when hub is waiting.
	private boolean WaitingonHighway; // WaitingonHighway is true when highway currHighway is waiting.
	private int speed; // speed
	private int totalDistancetoTravel = 0; // totalDistancetoTravel = 

}