package com.mercator;

/*
 * Copyright 2010, 2011, 2012 mapsforge.org
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
//package org.mapsforge.map.writer.model;

/**
 * A static class that implements spherical mercator projection.
 */
public final class MercatorProjection {

	private MercatorProjection() {
	}

	/**
	 * Convert a longitude coordinate (in degrees) to a horizontal distance in
	 * meters from the zero meridian.
	 * 
	 * @param longitude
	 *            in degrees
	 * @return longitude in meters in spherical mercator projection
	 */
	public static double longitudeToMetersX(double longitude) {
		return WGS84.EQUATORIALRADIUS * java.lang.Math.toRadians(longitude);
	}

	/**
	 * Convert a meter measure to a longitude.
	 * 
	 * @param x
	 *            in meters
	 * @return longitude in degrees in spherical mercator projection
	 */
	public static double metersXToLongitude(double x) {
		return java.lang.Math.toDegrees(x / WGS84.EQUATORIALRADIUS);
	}

	/**
	 * Convert a meter measure to a latitude.
	 * 
	 * @param y
	 *            in meters
	 * @return latitude in degrees in spherical mercator projection
	 */
	public static double metersYToLatitude(double y) {
		return java.lang.Math.toDegrees(java.lang.Math.atan(java.lang.Math
				.sinh(y / WGS84.EQUATORIALRADIUS)));
	}

	/**
	 * Convert a latitude coordinate (in degrees) to a vertical distance in
	 * meters from the equator.
	 * 
	 * @param latitude
	 *            in degrees
	 * @return latitude in meters in spherical mercator projection
	 */
	public static double latitudeToMetersY(double latitude) {
		return WGS84.EQUATORIALRADIUS
				* java.lang.Math.log(java.lang.Math.tan(java.lang.Math.PI / 4
						+ 0.5 * java.lang.Math.toRadians(latitude)));
	}

	/**
	 * Calculate the distance on the ground that is represented by a single
	 * pixel on the map.
	 * 
	 * @param latitude
	 *            the latitude coordinate at which the resolution should be
	 *            calculated.
	 * @param zoom
	 *            the zoom level at which the resolution should be calculated.
	 * @return the ground resolution at the given latitude and zoom level.
	 */
	public static double calculateGroundResolution(double latitude, byte zoom) {
		return Math.cos(latitude * Math.PI / 180) * 40075016.686
				/ ((long) Tile.TILE_SIZE << zoom);
	}

	/**
	 * Convert a latitude coordinate (in degrees) to a pixel Y coordinate at a
	 * certain zoom level.
	 * 
	 * @param latitude
	 *            the latitude coordinate that should be converted.
	 * @param zoom
	 *            the zoom level at which the coordinate should be converted.
	 * @return the pixel Y coordinate of the latitude value.
	 */
	public static double latitudeToPixelY(double latitude, byte zoom) {
		double sinLatitude = Math.sin(latitude * Math.PI / 180);
		return (0.5 - Math.log((1 + sinLatitude) / (1 - sinLatitude))
				/ (4 * Math.PI))
				* ((long) Tile.TILE_SIZE << zoom);
	}

	/**
	 * Convert a latitude coordinate (in degrees) to a tile Y number at a
	 * certain zoom level.
	 * 
	 * @param latitude
	 *            the latitude coordinate that should be converted.
	 * @param zoom
	 *            the zoom level at which the coordinate should be converted.
	 * @return the tile Y number of the latitude value.
	 */
	public static long latitudeToTileY(double latitude, byte zoom) {
		return pixelYToTileY(latitudeToPixelY(latitude, zoom), zoom);
	}

	/**
	 * Convert a longitude coordinate (in degrees) to a pixel X coordinate at a
	 * certain zoom level.
	 * 
	 * @param longitude
	 *            the longitude coordinate that should be converted.
	 * @param zoom
	 *            the zoom level at which the coordinate should be converted.
	 * @return the pixel X coordinate of the longitude value.
	 */
	public static double longitudeToPixelX(double longitude, byte zoom) {
		return (longitude + 180) / 360 * ((long) Tile.TILE_SIZE << zoom);
	}

	/**
	 * Convert a longitude coordinate (in degrees) to the tile X number at a
	 * certain zoom level.
	 * 
	 * @param longitude
	 *            the longitude coordinate that should be converted.
	 * @param zoom
	 *            the zoom level at which the coordinate should be converted.
	 * @return the tile X number of the longitude value.
	 */
	public static long longitudeToTileX(double longitude, byte zoom) {
		return pixelXToTileX(longitudeToPixelX(longitude, zoom), zoom);
	}

	/**
	 * Convert a pixel X coordinate at a certain zoom level to a longitude
	 * coordinate.
	 * 
	 * @param pixelX
	 *            the pixel X coordinate that should be converted.
	 * @param zoom
	 *            the zoom level at which the coordinate should be converted.
	 * @return the longitude value of the pixel X coordinate.
	 */
	public static double pixelXToLongitude(double pixelX, byte zoom) {
		return 360 * ((pixelX / ((long) Tile.TILE_SIZE << zoom)) - 0.5);
	}

	/**
	 * Convert a pixel X coordinate to the tile X number.
	 * 
	 * @param pixelX
	 *            the pixel X coordinate that should be converted.
	 * @param zoom
	 *            the zoom level at which the coordinate should be converted.
	 * @return the tile X number.
	 */
	public static long pixelXToTileX(double pixelX, byte zoom) {
		return (long) Math.min(Math.max(pixelX / Tile.TILE_SIZE, 0), Math.pow(
				2, zoom) - 1);
	}

	/**
	 * Convert a tile X number to a pixel X coordinate.
	 * 
	 * @param tileX
	 *            the tile X number that should be converted
	 * @return the pixel X coordinate
	 */
	public static double tileXToPixelX(long tileX) {
		return tileX * Tile.TILE_SIZE;
	}

	/**
	 * Convert a tile Y number to a pixel Y coordinate.
	 * 
	 * @param tileY
	 *            the tile Y number that should be converted
	 * @return the pixel Y coordinate
	 */
	public static double tileYToPixelY(long tileY) {
		return tileY * Tile.TILE_SIZE;
	}

	/**
	 * Convert a pixel Y coordinate at a certain zoom level to a latitude
	 * coordinate.
	 * 
	 * @param pixelY
	 *            the pixel Y coordinate that should be converted.
	 * @param zoom
	 *            the zoom level at which the coordinate should be converted.
	 * @return the latitude value of the pixel Y coordinate.
	 */
	public static double pixelYToLatitude(double pixelY, byte zoom) {
		double y = 0.5 - (pixelY / ((long) Tile.TILE_SIZE << zoom));
		return 90 - 360 * Math.atan(Math.exp(-y * 2 * Math.PI)) / Math.PI;
	}

	/**
	 * Converts a pixel Y coordinate to the tile Y number.
	 * 
	 * @param pixelY
	 *            the pixel Y coordinate that should be converted.
	 * @param zoom
	 *            the zoom level at which the coordinate should be converted.
	 * @return the tile Y number.
	 */
	public static long pixelYToTileY(double pixelY, byte zoom) {
		return (long) Math.min(Math.max(pixelY / Tile.TILE_SIZE, 0), Math.pow(
				2, zoom) - 1);
	}

	/**
	 * Convert a tile X number at a certain zoom level to a longitude
	 * coordinate.
	 * 
	 * @param tileX
	 *            the tile X number that should be converted.
	 * @param zoom
	 *            the zoom level at which the number should be converted.
	 * @return the longitude value of the tile X number.
	 */
	public static double tileXToLongitude(long tileX, byte zoom) {
		return pixelXToLongitude(tileX * Tile.TILE_SIZE, zoom);
	}

	/**
	 * Convert a tile Y number at a certain zoom level to a latitude coordinate.
	 * 
	 * @param tileY
	 *            the tile Y number that should be converted.
	 * @param zoom
	 *            the zoom level at which the number should be converted.
	 * @return the latitude value of the tile Y number.
	 */
	public static double tileYToLatitude(long tileY, byte zoom) {
		return pixelYToLatitude(tileY * Tile.TILE_SIZE, zoom);
	}

	/**
	 * Computes the amount of latitude degrees for a given distance in pixel at
	 * a given zoom level.
	 * 
	 * @param deltaPixel
	 *            the delta in pixel
	 * @param lat
	 *            the latitude
	 * @param zoom
	 *            the zoom level
	 * @return the delta in degrees
	 */
	public static double deltaLat(double deltaPixel, double lat, byte zoom) {
		double pixelY = latitudeToPixelY(lat, zoom);
		double lat2 = pixelYToLatitude(pixelY + deltaPixel, zoom);

		return Math.abs(lat2 - lat);
	}

	public static void main(String[] args) {

		/*
		 * double py = MercatorProjection.latitudeToPixelY(39.36827914916013,
		 * (byte)10);
		 * 
		 * System.out.println(py);
		 * 
		 * System.out.println((int)(py % 256));
		 * 
		 * py = MercatorProjection.latitudeToPixelY(39.639537564366, (byte)10);
		 * 
		 * System.out.println((int)(py % 256));
		 * 
		 * System.out.println(py);
		 * 
		 * long tilex = MercatorProjection.latitudeToTileY(0, (byte)18);
		 * 
		 * System.out.println(tilex);
		 * 
		 * double lat = MercatorProjection.tileYToLatitude(389, (byte)10);
		 * 
		 * System.out.println(lat);
		 * 
		 * lat = MercatorProjection.tileYToLatitude(390, (byte)10);
		 * 
		 * System.out.println(lat);
		 * 
		 * 
		 * 
		 * tilex = MercatorProjection.longitudeToTileX(179, (byte)25);
		 * 
		 * System.out.println(tilex);
		 */

//		double lat1 = MercatorProjection.tileYToLatitude(384, (byte) 10);
//
//		System.out.println(lat1);
//
//		double py = MercatorProjection.tileYToPixelY(384l);
//
//		System.out.println(py);
//		
//		lat1 = MercatorProjection.tileYToLatitude(383, (byte) 10);
//
//		System.out.println(lat1);
		
//		System.out.println(MercatorProjection.latitudeToPixelY(40.97989806962013, (byte)10));
//
//		System.out.println(MercatorProjection.latitudeToPixelY(40.99989806962013, (byte)10));
//		
//		System.out.println(MercatorProjection.latitudeToPixelY(40.95989806962013, (byte)10));
//	
//		System.out.println(MercatorProjection.latitudeToTileY(40.97989806962013, (byte)10));
//
//		System.out.println(MercatorProjection.latitudeToTileY(40.99989806962013, (byte)10));
//		
//		System.out.println(MercatorProjection.latitudeToTileY(40.95989806962013, (byte)10));
		
		/*double lon = MercatorProjection.tileXToLongitude(844, (byte)10);
		
		System.out.println(lon);
		
		lon = MercatorProjection.tileXToLongitude(845, (byte)10);
		
		System.out.println(lon);
		
		System.out.println(MercatorProjection.longitudeToTileX(116.79875, (byte)10));*/
		
//		116.21672630310059,40.00286504288028
		
		System.out.println(MercatorProjection.longitudeToPixelX(116.2167263, (byte)14));
		
		
		System.out.println(MercatorProjection.pixelXToLongitude(3451174.9999638754, (byte)14));
	}
}