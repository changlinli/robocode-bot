// For some reason I can't get my current package name to do this right
//package Blah;

//package robocodeBot;
package sample;
import robocode.*;
//import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * MyFirstRobot - a robot by (your name here)
 */
public class Swquanlin extends Robot
{
	static double scanSize;
	
	/**
	 * MyFirstRobot's run method - Seesaw
	 */
	public void run() {
		scanSize = 360;
		
		while (true) {
			setAdjustRadarForGunTurn(true);
			setAdjustRadarForRobotTurn(true);
			turnRadarRight(scanSize);
			setAdjustRadarForGunTurn(false);
			setAdjustRadarForRobotTurn(false);
			
			ahead(100); // Move ahead 100
			turnGunRight(360); // Spin gun around
			back(100); // Move back 100
			turnGunRight(360); // Spin gun around
		}
	}

	/**
	 * Fire when we see a robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
        e.getBearing();
		fire(1);
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
}
