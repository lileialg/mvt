/*
 * Copyright 2010, 2011 mapsforge.org
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
package com.mercator;

/**
 * This class represents a map tile. All mandatory fields are final.
 * 
 */
public class Tile implements Comparable<Tile> {
	static final byte TILE_BYTES_PER_PIXEL = 2;

	/**
	 * The tile size in pixel.
	 */
	public static final short TILE_SIZE = 256;
	private final int hashCode;
	final long pixelX;
	final long pixelY;
	int renderPriority;
	final long x;
	final long y;
	final byte zoomLevel;

	/**
	 * Constructs a new tile with the specified XY number and zoom level.
	 * 
	 * @param x
	 *            the X number of the tile.
	 * @param y
	 *            the Y number of the tile.
	 * @param zoomLevel
	 *            the zoom level of the tile.
	 */
	Tile(long x, long y, byte zoomLevel) {
		this.x = x;
		this.y = y;
		this.zoomLevel = zoomLevel;
		this.hashCode = calculateHashCode();
		this.pixelX = x * TILE_SIZE;
		this.pixelY = y * TILE_SIZE;
		this.renderPriority = Integer.MAX_VALUE;
	}

	public int compareTo(Tile another) {
		return this.renderPriority - another.renderPriority;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (!(obj instanceof Tile)) {
			return false;
		} else {
			Tile other = (Tile) obj;
			if (this.x != other.x) {
				return false;
			} else if (this.y != other.y) {
				return false;
			} else if (this.zoomLevel != other.zoomLevel) {
				return false;
			} else {
				return true;
			}
		}
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}

	/**
	 * Calculate and save the hash code of this tile.
	 * 
	 * @return the hash code for this tile.
	 */
	private int calculateHashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (this.x ^ (this.x >>> 32));
		result = prime * result + (int) (this.y ^ (this.y >>> 32));
		result = prime * result + this.zoomLevel;
		return result;
	}

	@Override
	public String toString() {
		return "Tile: " + this.x + ", " + this.y + ", " + this.zoomLevel;
	}
}
