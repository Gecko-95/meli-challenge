package meli.challenge.test.utils;

public class DistanceCalculator {

    private static final double LAT_V = 4.1247544;
    private static final double LON_V = -73.6791012;

    public static double distance(double lat2, double lon2) {
        if ((LAT_V == lat2) && (LON_V == lon2)) {
            return 0;
        } else {
            double theta = LON_V - lon2;
            double dist = Math.sin(Math.toRadians(LAT_V)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(LAT_V)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.85316;

            return (dist);
        }
    }
}
