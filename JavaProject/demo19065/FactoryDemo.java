package demo19065;

import base.*;

public class FactoryDemo extends Factory {

	private DemoHub hub;

	@Override
	public Network createNetwork() {
		return new DemoNetwork();
	}

	@Override
	public Highway createHighway() {
		return new DemoHighway();
	}

	@Override
	public Hub createHub(Location location) {
		hub = new DemoHub(location);
		return hub;
	}

	@Override
	public Truck createTruck() {
		 DemoTruck newTruck = new DemoTruck();
		 try {
		 	newTruck.setReferenceHub(this.hub);
		 } catch(Exception e) {
		 	System.out.println("Problem encountered while assigning reference hub to one of the trucks");
		 }
		 return newTruck;
	}
}