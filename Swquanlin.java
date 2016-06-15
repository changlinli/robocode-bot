// For some reason I can't get my current package name to do this right
//package Blah;
package robocodeBot;
import robocode.*;
//import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * MyFirstRobot - a robot by (your name here)
 */
public class Swquanlin extends Robot
{

	/**
	 * MyFirstRobot's run method - Seesaw
	 */
	public void run() {

		while (true) {
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
		fire(1);
	}

    private double perpendicularHeading(int enemyLocX, int enemyLocY, int currentLocX, int currentLocY, double currentAngle) {
        int yDiff = enemyLocY - currentLocY;
        int xDiff = enemyLocX - currentLocX;
        double angleOffset = Math.atan((double) xDiff / (double) yDiff);
        return angleOffset + Math.PI / 2.0;
    }

	/**
	 * We were hit!  Turn perpendicular to the bullet,
	 * so our seesaw might avoid a future shot.
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		turnLeft(90 - e.getBearing());
	}
}
