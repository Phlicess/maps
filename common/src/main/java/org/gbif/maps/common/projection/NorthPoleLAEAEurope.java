package org.gbif.maps.common.projection;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * Our implementation of this arctic projections indicates that it clip at the equator through choice.
 * This class is threadsafe.
 * @see https://epsg.io/3575
 */
class NorthPoleLAEAEurope extends WGS84Stereographic {
  static final String EPSG_CODE = "EPSG:3575";

  // A tranform to convert from WGS84 coordinates into 3575 pixel space
  private static final MathTransform TRANSFORM;
  static {
    try {
      TRANSFORM = CRS.findMathTransform(CRS.decode("EPSG:4326"), CRS.decode("EPSG:3575"), true);
    } catch (FactoryException e) {
      throw new IllegalStateException("Unable to decode EPSG projections", e);
    }
  }

  @Override
  MathTransform getTransform() {
    return TRANSFORM;
  }

  NorthPoleLAEAEurope(int tileSize) {
    super(tileSize);
  }

  @Override
  public boolean isPlottable(double latitude, double longitude) {
    // clipped to equator and above by deliberate choice, even though the projection would support more
    return latitude >= 0 && longitude>=-180 && longitude<=180;
  }
}
