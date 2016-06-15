// For some reason I can't get my current package name to do this right
//package Blah;

package robocodeBot;
//package sample;
import robocode.*;
//import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * MyFirstRobot - a robot by (your name here)
 */
public class Swquanlin extends Robot
{
	static double scanSize;
	static double lastScannedTime;
	
	static double scannedIntervalMs = 1000;
	/**
	 * MyFirstRobot's run method - Seesaw
	 */
	public void run() {
		scanSize = 360;
		
		while (true) {
			turnGunRight(turnSpeed);
			turnSpeed += 1;
		
			// do a 360 scan once data is stale
            //double currentTime = System.currentTimeMillis();	
            //if (currentTime - lastScannedTime > scannedIntervalMs) {
                //setAdjustRadarForGunTurn(true);
                //setAdjustRadarForRobotTurn(true);
                //turnRadarRight(scanSize);
                //lastScannedTime = System.currentTimeMillis();
                //setAdjustRadarForGunTurn(false);
                //setAdjustRadarForRobotTurn(false);
            //}
			ahead(1000); // Move ahead 100
			turnGunRight(360); // Spin gun around
			//back(100); // Move back 100
			turnGunRight(360); // Spin gun around
		}
	}

	/**
	 * Fire when we see a robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
        //e.getBearing();
		//fire(1);
		//double currentTime = System.currentTimeMillis();

		turnSpeed = 0;
		Position enemyPos = getPosition(e);
		double mySpeed = getVelocity();
		double enemyHeading = e.getHeading();
		double enemySpeed = e.getVelocity();
		shootLinearExtrapolated(mySpeed, enemyPos, enemyHeading, enemySpeed);
		
		// wiggle the scanner
		//if (currentTime - lastScannedTime > scannedIntervalMs) {
			//setAdjustRadarForGunTurn(true);
			//setAdjustRadarForRobotTurn(true);
			//turnRadarRight(45);
			//turnRadarLeft(90);
			//lastScannedTime = System.currentTimeMillis();
			//setAdjustRadarForGunTurn(false);
			//setAdjustRadarForRobotTurn(false);
		//}
	}

    private double perpendicularHeadingRadians(double enemyLocX, double enemyLocY, double currentLocX, double currentLocY) {
        double yDiff = enemyLocY - currentLocY;
        double xDiff = enemyLocX - currentLocX;
        double angleOffset = Math.atan2(yDiff, xDiff);
        return angleOffset + Math.PI / 2.0;
    }

    private void turnPerpendicular(int enemyLocX, int enemyLocY) {
        double currentX = getX();
        double currentY = getY();
        double currentBearing = Math.toRadians(getHeading());
        double bearingOffset = perpendicularHeadingRadians(enemyLocX, enemyLocY, currentX, currentY);
        turnLeft(Math.toDegrees(currentBearing + bearingOffset));
    }


	/**
	 * We were hit!  Turn perpendicular to the bullet,
	 * so our seesaw might avoid a future shot.
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		turnLeft(90 - e.getBearing());
	}

    public void onHitWall(HitWallEvent e) {
        turnLeft(90);
    }

	private double getRotationDelta(double scanAngle, double enemyHeading, double enemySpeed){
		double gunAngle = getGunHeading();
		double delta = scanAngle-gunAngle;
		if (gunAngle > 180)
			delta = 360 - gunAngle;
		else if (gunAngle < -180)
			delta = -360 - gunAngle;
		return delta;
	}
	
	private double getAngle(Position pos1, Position pos2){
		double dX = pos1.x - pos2.x;
		double dY = pos1.y - pos2.y;
		double angleBetween = Math.atan(dX/dY);
		return angleBetween;
	}
	
	private Position extrapolatePosition(Position pos, double heading, double speed, double time) {
		Position newPos = new Position();
		double dY = Math.cos(heading/180 * Math.PI) * speed * time;
		double dX = Math.sin(heading/180 * Math.PI) * speed * time;
		newPos.x = pos.x + dX;
		newPos.y = pos.y + dY;
		return newPos;
	}
	
	
	private class Position{
		public double x;
		public double y;
	}

	private Position getPosition(ScannedRobotEvent e){
		double distance = e.getDistance();
		double angle = getRadarHeading();
		double dX = distance * Math.cos(angle/180 * Math.PI);
		double dY = distance * Math.sin(angle/180 * Math.PI);
		Position pos = new Position();
		pos.x = getX() + dX;
		pos.y = getY() + dY;
		return pos;
	}

	private void shootLinearExtrapolated(double mySpeed, Position enemyPos, double enemyHeading, double enemySpeed){
		double scanAngle = getRadarHeading();
		double delta = getRotationDelta(scanAngle, enemyHeading, enemySpeed);
		double time = delta/gunRotationRate;
		Position myPos = new Position(); myPos.x = getX(); myPos.y = getY();
		Position extrapolatedEnemyPos = extrapolatePosition(enemyPos, enemyHeading, enemySpeed, time);
		Position myExtrapolatedPos = extrapolatePosition(myPos, getHeading(), mySpeed, time);
		double extrapolatedAngle = getAngle(myExtrapolatedPos, extrapolatedEnemyPos);
		turnGunRight(extrapolatedAngle);
		fire(5);
	}

	private int gunRotationRate = 20;
	private int radarRotationRate = 45;
	private int turnSpeed = 10;
}
