package fightGame.utils;

public class MathUtils {
	/**
	 * The greatest floating point value the computer can store.. represents infinity
	 */
	public static final float INFINITY = 99999999999999999999999999999999999999f;
	
	public static int ceil(int max, int val) {
		if (val > max)
			return max;
		else
			return val;
	}
	
	public static float ceil(float max, float val) {
		if (val > max)
			return max;
		else
			return val;
	}
	
	public static double ceil(double max, double val) {
		if (val > max)
			return max;
		else 
			return val;
	}
	
	public static int floor(int min, int val) {
		if (val < min)
			return min;
		else 
			return val;
	}
	
	public static float floor(float min, float val) {
		if (val < min)
			return min;
		else 
			return val;
	}
	
	public static double floor(double min, double val) {
		if (val < min)
			return min;
		else
			return val;
	}
	
	public static int clip(int min, int max, int val) {
		if (floor(min,val) == min)
			return min;
		if (ceil(max,val) == max)
			return max;
		return val;
	}
	
	public static float clip(float min, float max, float val) {
		if (floor(min,val) == min)
			return min;
		if (ceil(max,val) == max)
			return max;
		return val;
	}
	
	public static double clip(double min, double max, double val) {
		if (floor(min,val) == min)
			return min;
		if (ceil(max,val) == max)
			return max;
		return val;
	}
	
	public static float round(float val, float decimal) {
		float scale = 1.0f/decimal;
		val *= scale;
		return Math.round(val)/scale;
	}
	
	public static double round(double val, double decimal) {
		double scale = 1.0/decimal;
		val *= scale;
		return Math.round(val)/scale;
	}
	
	/**
	 * Pads the number with zeros or rounds the number to match the specified 
	 * numeral counts
	 * @param number
	 * @param headNumberCount
	 * @param trailingNumberCount
	 * @return
	 */
	public static String pad(double number, int headNumberCount, int trailingNumberCount) {
		number = MathUtils.round(number, 1/Math.pow(10, trailingNumberCount));
		String num = ""+number;
		if (trailingNumberCount < 1)
			num = ""+(int)number;
		for (int order = 1; order < headNumberCount; order++) 
			if (number < Math.pow(10, order)) 
				num = 0+num;
		for (int order = 1; order < trailingNumberCount; order++) 
			if (number % Math.pow(10, -order) == 0)
				num+="0";
		return num;
	}
	
	/**
	 * they are considered equal if they are within +/-0.001 of each other
	 * @param val1
	 * @param val2
	 * @return
	 */
	public static boolean equals(float val1, float val2) {
		return isWithin(val1, val2, 0.001f);
	}
	
	/**
	 * Returns true if the two numbers are within some specified range
	 */
	public static boolean isWithin(float val1, float val2, float range) {
		return Math.abs(val1-val2) < range;
	}
	
	/**
	 * Returns the lower of two integers
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static int min(int num1, int num2) {
		if (num1 < num2)
			return num1;
		else //also includes if they are the same, just return either number it doesn't matter
			return num2;
	}
	
	public static float min(float num1, float num2) {
		if (num1 < num2)
			return num1;
		else
			return num2;
	}
	
	public static double min(double num1, double num2) {
		if (num1 < num2)
			return num1;
		else
			return num2;
	}
	
	/**
	 * Returns the greater of the two integer numbers
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static int max(int num1, int num2) {
		if (num1 > num2)
			return num1;
		else
			return num2;
	}
	
	public static float max(float num1, float num2) {
		if (num1 > num2)
			return num1;
		else
			return num2;
	}
	
	public static double max(double num1, double num2) {
		if (num1 > num2)
			return num1;
		else
			return num2;
	}
	
	/**
	 * Checks if the angle is between two others
	 * 
	 * The lower limit specifies the starting position and
	 * goes around the unit circle (+rad direction) to the upper limit
	 * to determine the area of acceptability for the angle
	 * 
	 * Angles are put between [0,2pi)
	 * 
	 * The angle is inclusive to the limits
	 * 
	 * @param lowerLimit 
	 * @param upperLimit
	 * @param angle
	 * @return
	 */
	public static boolean isAngleWithin(double lowerLimit, double upperLimit, double angle) {
		lowerLimit = limitAngleDomain(lowerLimit);
		upperLimit = limitAngleDomain(upperLimit);
		angle = limitAngleDomain(angle);
		
		if (angle >= lowerLimit && angle <= upperLimit)
			return true;
		
		//for when the bounds cross 0pi
		if (lowerLimit > upperLimit) {
			if (angle >= lowerLimit)
				return true;
			else if (angle <= upperLimit) 
				return true;
		}
		
		//inclusive of the entire circle
		if (lowerLimit == upperLimit) 
			return true;
		
		return false;
		
	}
	
	/**
	 * Limits the angle between [0,2pi)
	 * @param angle
	 * @return
	 */
	public static double limitAngleDomain(double angle) {
		while (angle < 0)
			angle += Math.PI * 2;
		while (angle >= Math.PI * 2)
			angle -= Math.PI * 2;
		return angle;
	}
	
	//give in y = mx + b form
	/**
	 * Returns the point of intersection on a line if it exists
	 * @param m1 the slope of the first line
	 * @param b1 the intercept of the first line
	 * @param m2 the slope of the second line
	 * @param b2 the intercept of the second line
	 * @return the point of intersection as a vector if it exists, null if they are
	 * parallel, and an infinity vector if there are infinite solutions.
	 */
	public static Vector2 intersects(double m1, double b1, double m2, double b2) {
		if (m1 == m2) {//parallel so they will never intersect or will have infinite intersections
			if (b1 != b2) //the lines are parallel, but not equivalent.. no intersection exists
				return null;
			else //the lines are equivalent
				return new Vector2(INFINITY, INFINITY);
		}
		//set the equations equal to each other to find x point of intersection
		// m1x + b1 = m2x + b2
		double x = (b2-b1)/(m1-m2);
		//just plug that x value back into either solution to get y
		double y = m1 * x + b1;
		return new Vector2((float)x,(float)y);
	}
	
	//given as an origin and an angle
	public static Vector2 intersects(Vector2 origin1, double angle1, Vector2 origin2, double angle2) {
		//get the slope and intercept of each
		double m1 = Math.tan(angle1);
		double m2 = Math.tan(angle2);
		//we have an x, y, a slope, but we need b
		//y = mx + b -> b = y - mx
		double b1 = origin1.getY() - m1 * origin1.getX();
		double b2 = origin2.getY() - m2 * origin2.getY();
		return intersects(m1,b1,m2,b2);
	}
	
	//given four endpoints
	public static Vector2 intersects(Vector2 p1, Vector2 p2, Vector2 p3, Vector2 p4) {
		float denominator = (p4.x-p3.x)*(p1.y-p2.y)-(p1.x-p2.x)*(p4.y-p3.y);
		if (denominator == 0)
			return null; //either no/infinite intersections
		float ta = ((p3.y-p4.y)*(p1.x-p3.x)+(p4.x-p3.x)*(p1.y-p3.y))/denominator;
		float tb = ((p1.y-p2.y)*(p1.x-p3.x)+(p2.x-p1.x)*(p1.y-p3.y))/denominator;
		if (ta >= 0 && ta <= 1 && tb >= 0 && tb <= 1) //intersection!
			return new Vector2(p1.x+ta*(p2.x-p1.x),p1.y+ta*(p2.y-p1.y));
		else
			return null; //intersection is outside of the bounds
	}
	
	/**
	 * Gets the angle from two input coordinates from the origin
	 * @param x horizontal distance from origin
	 * @param y vertical distance from origin
	 * @return angle it makes
	 */
	public static double getAngle(double x, double y) {
		//tan = y/x
		if (x == 0) { //avoid the divide by 0 case
			if (y > 0)
				return Math.PI/2;
			else if (y < 0)
				return 3*Math.PI/2;
			else
				return 0;
		}
		double theta = Math.atan(y/x);
		if (x < 0)  //tan is defined from -pi/2 to pi/2.. so if x is negative we need to add pi
			theta += Math.PI;
		else if (y < 0)
			theta += Math.PI * 2;
		return theta;
	}
	
	/**
	 * Gets the angle between two endpoints
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static double getAngle(double x1, double y1, double x2, double y2) {
		return getAngle(x2-x1,y2-y1);
	}
	
	/**
	 * Gets the angle between to points in Vector format
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double getAngle(Vector2 p1, Vector2 p2) {
		return getAngle(p1.x,p1.y,p2.x,p2.y);
	}
	
	/**
	 * Adjusts the angle value so that it is within the range [0,2pi)
	 * @param angle
	 * @return
	 */
	public static double normalizeAngle(double angle) {
		while (angle < 0)
			angle+=Math.PI * 2;
		angle%=(Math.PI * 2);
		return angle;
	}
	
	/**
	 * Gets the distance between two points
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static float squaredDistance(float x1, float y1, float x2, float y2) {
		float dx = x2-x1, dy = y2-y1;
		return dx*dx+dy*dy;
	}
	
	public static float distance(float x1, float y1, float x2, float y2) {
		return (float)Math.sqrt(squaredDistance(x1,y1,x2,y2));
	}
	
	/**
	 * Gets the distance between two points
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static float distance(Vector2 p1, Vector2 p2) {
		return distance(p1.x,p1.y,p2.x,p2.y);
	}
	
	/**
	 * Gets a random floating point number between two numbers from [min,max)
	 * @param botRange
	 * @param topRange
	 * @return
	 */
	public static float random(float botRange, float topRange) {
		if (botRange > topRange)
			throw new RuntimeException("second input should be greater than first");
		return (float)Math.random()*(topRange-botRange)+botRange;
	}
	
	/**
	 * Gets a random integer between two numbers from [min,max)
	 * @param botRange
	 * @param topRange
	 * @return
	 */
	public static int random(int botRange, int topRange) {
		if (botRange > topRange)
			throw new RuntimeException("second input should be greater than the first");
		return (int)(Math.random()*(topRange-botRange))+botRange;
	}
	
	/**
	 * Gets a random floating point between [0, top)
	 * @param topRange
	 * @return
	 */
	public static float random(float topRange) {
		return random(0,topRange);
	}
	
	/**
	 * Gets a random integer between [0,top)
	 * @param topRange
	 * @return
	 */
	public static int random(int topRange) {
		return random(0,topRange);
	}
	
	/**
	 * Gets the sign of an integer by returning 1 for positive, -1 for negative, and 0 for 0
	 * @param number
	 * @return
	 */
	public static int sign(int number) {
		if (number == 0)
			return 0;
		return Math.abs(number)/number;
	}
	
	public static float sign(float number) {
		if (number == 0)
			return 0;
		return Math.abs(number)/number;
	}
}
