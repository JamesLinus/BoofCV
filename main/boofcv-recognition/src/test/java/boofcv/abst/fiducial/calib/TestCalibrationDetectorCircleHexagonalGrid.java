/*
 * Copyright (c) 2011-2017, Peter Abeles. All Rights Reserved.
 *
 * This file is part of BoofCV (http://boofcv.org).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package boofcv.abst.fiducial.calib;

import boofcv.abst.geo.calibration.DetectorFiducialCalibration;
import boofcv.factory.fiducial.FactoryFiducialCalibration;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayF32;
import georegression.metric.UtilAngle;
import georegression.struct.point.Point2D_F64;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.List;

import static boofcv.abst.fiducial.calib.CalibrationDetectorCircleAsymmGrid.createLayout;

/**
 * @author Peter Abeles
 */
public class TestCalibrationDetectorCircleHexagonalGrid extends GenericPlanarCalibrationDetectorChecks {

	public TestCalibrationDetectorCircleHexagonalGrid() {
		targetConfigs.add( new ConfigCircleHexagonalGrid(5, 5, 20,24) );
	}

	@Override
	public void renderTarget(Object layout, double length3D , GrayF32 image, List<Point2D_F64> points2D) {
		ConfigCircleHexagonalGrid config = (ConfigCircleHexagonalGrid)layout;

		double radiusPixels = 20;
		double centerDistancePixels = 2*radiusPixels*config.centerDistance/config.circleDiameter;
		double borderPixels = 30;

		double spaceX = centerDistancePixels/2.0;
		double spaceY = centerDistancePixels*Math.sin(UtilAngle.radian(60));

		int imageWidth = (int)(borderPixels*2 + (config.numCols-1)*spaceX + 2*radiusPixels+0.5);
		int imageHeight = (int)(borderPixels*2 + (config.numRows-1)*spaceY + 2*radiusPixels+0.5);

		double centerDistanceWorld = length3D*centerDistancePixels/(double)imageWidth;

		image.reshape( imageWidth,imageHeight );
		BufferedImage buffered = new BufferedImage(image.width,image.height, BufferedImage.TYPE_INT_BGR);

		Graphics2D g2 = buffered.createGraphics();
		g2.setColor(Color.WHITE);
		g2.fillRect(0,0,buffered.getWidth(),buffered.getHeight());
		g2.setColor(Color.BLACK);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Ellipse2D.Double ellipse = new Ellipse2D.Double();

		for (int row = 0; row < config.numRows; row++) {
			double y = borderPixels+radiusPixels+row*spaceY;
			for (int col = 0; col < config.numCols; col++) {
				double x = borderPixels+radiusPixels+col*spaceX;

				if( row%2 == 1 && col%2 == 0 )
					continue;
				if( row%2 == 0 && col%2 == 1 )
					continue;

				ellipse.setFrame(x-radiusPixels,y-radiusPixels,2*radiusPixels,2*radiusPixels);
				g2.fill(ellipse);
			}
		}

		ConvertBufferedImage.convertFrom(buffered, image);

		points2D.clear();
		points2D.addAll( createLayout(config.numRows, config.numCols, centerDistanceWorld ));
	}

	@Override
	public DetectorFiducialCalibration createDetector(Object layout) {
		return FactoryFiducialCalibration.circleAsymmGrid((ConfigCircleHexagonalGrid)layout);
	}

}
