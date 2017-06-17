package com.example.administrator.fnroad.utils;

import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Line;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.SpatialReference;

import java.util.List;

/**
 * Created by hornkyin on 16/10/17.
 */
public class ArcgisMapUtils {
    /**
     * 根据List生成Polygon
     *
     * @param shorePolygonPoints
     * @return
     */
    public static Polygon generatePolygonByPoints(List<Point> shorePolygonPoints) {
        Polygon polygon = new Polygon();
        Point startPoint = null;
        Point endPoint = null;
        // 绘制完整的多边形
        for (int i = 1; i < shorePolygonPoints.size(); i++) {
            startPoint = shorePolygonPoints.get(i - 1);
            endPoint = shorePolygonPoints.get(i);

            Line line = new Line();
            line.setStart(startPoint);
            line.setEnd(endPoint);

            polygon.addSegment(line, false);
        }

        return polygon;
    }

    /**
     * 由Polygon的所有点计算出相对的中心位置
     *
     * @param polygon
     * @return
     */
    public Point getCenterPoint(Polygon polygon) {
        //获取graphicSelected图标中心点

        double sumX = 0, sumY = 0,minX=polygon.getPoint(0).getX(),minY=polygon.getPoint(0).getY();
        for (int i = 0; i < polygon.getPointCount(); i++) {
            sumX += polygon.getPoint(i).getX();
            sumY += polygon.getPoint(i).getY();
            if(minX>polygon.getPoint(i).getX()){
                minX=polygon.getPoint(i).getX();
            }
            if(minY>polygon.getPoint(i).getY()){
                minY=polygon.getPoint(i).getY();
            }
        }

        double x = sumX / polygon.getPointCount();
        double y = sumY / polygon.getPointCount();
        return new Point(x, y);
    }

    public static Point toWgsPoint(Point clickPoint, SpatialReference spatialReference){
        Point wgsPoint = (Point) GeometryEngine.project(clickPoint, spatialReference, SpatialReference.create(4326));
        return wgsPoint;
    }

    public static Point toMapPoint(Point wgsPoint, SpatialReference spatialReference) {
        Point mapPoint = (Point) GeometryEngine.project(wgsPoint, SpatialReference.create(4326), spatialReference);
        return mapPoint;
    }

}
