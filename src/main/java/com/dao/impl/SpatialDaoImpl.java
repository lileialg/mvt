package com.dao.impl;

import java.util.List;
import java.util.Map;

import no.ecc.vectortile.VectorTileEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dao.SpatialDao;
import com.mercator.TileUtils;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

@Repository
public class SpatialDaoImpl implements SpatialDao {

	@Autowired
	protected JdbcTemplate jdbc;

	@Override
	public byte[] getContents(String type, int x, int y, int z) {
		
		String sql = null;
		
		if (type.equals("link")){
			sql = "select link_pid,name,st_astext(geom) as geom from beijing_link "
				+ "where " + "st_intersects(geom,st_geomfromtext(?,4326)) "
						+ "";
		}else if (type.equals("poi")){
			sql = "select pid,st_astext(geom) as geom from poi5 "
					+ "where " + "st_intersects(geom,st_geomfromtext(?,4326)) "
							+ "";
		}
		

		try {

			String tile = TileUtils.parseXyz2Bound(x, y, z);
			
			List<Map<String, Object>> results = jdbc.queryForList(sql, tile);

			VectorTileEncoder vte = new VectorTileEncoder(4096, 16, false);

			for (Map<String, Object> m : results) {
				String wkt = (String) m.get("geom");

				Geometry geom = new WKTReader().read(wkt);
				
				TileUtils.convert2Piexl(x, y, z, geom);
				
				m.remove("geom");
				
				vte.addFeature(type, m, geom);

			}
			
			return vte.encode();

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

}
