package com.mercator;

public class WGS84 {
	/**
	 * Equatorial radius of earth is required for distance computation.
	 */
	public static final double EQUATORIALRADIUS = 6378137.0;

	/**
	 * Polar radius of earth is required for distance computation.
	 */
	public static final double POLARRADIUS = 6356752.3142;

	/**
	 * The flattening factor of the earth's ellipsoid is required for distance computation.
	 */
	public static final double INVERSEFLATTENING = 298.257223563;

}
