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

package boofcv.examples.segmentation;

import boofcv.alg.filter.binary.GThresholdImageOps;
import boofcv.alg.misc.ImageStatistics;
import boofcv.gui.ListDisplayPanel;
import boofcv.gui.binary.VisualizeBinaryData;
import boofcv.gui.image.ShowImages;
import boofcv.io.UtilIO;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.GrayU8;

import java.awt.image.BufferedImage;

/**
 * Demonstration of different techniques for automatic thresholding an image to create a binary image.  The binary
 * image can then be used for shape analysis and other applications.  Global methods apply the same threshold
 * to the entire image.  Local methods compute a local threshold around each pixel and can handle uneven
 * lighting, but produce noisy results in regions with uniform lighting.
 *
 * @see boofcv.examples.imageprocessing.ExampleBinaryOps
 *
 * @author Peter Abeles
 */
public class ExampleThresholding {

	public static void threshold( String imageName ) {
		BufferedImage image = UtilImageIO.loadImage(imageName);

		// convert into a usable format
		GrayF32 input = ConvertBufferedImage.convertFromSingle(image, null, GrayF32.class);
		GrayU8 binary = new GrayU8(input.width,input.height);

		// Display multiple images in the same window
		ListDisplayPanel gui = new ListDisplayPanel();

		// Global Methods
		GThresholdImageOps.threshold(input, binary, ImageStatistics.mean(input), true);
		gui.addImage(VisualizeBinaryData.renderBinary(binary, false, null),"Global: Mean");
		GThresholdImageOps.threshold(input, binary, GThresholdImageOps.computeOtsu(input, 0, 255), true);
		gui.addImage(VisualizeBinaryData.renderBinary(binary, false, null),"Global: Otsu");
		GThresholdImageOps.threshold(input, binary, GThresholdImageOps.computeEntropy(input, 0, 255), true);
		gui.addImage(VisualizeBinaryData.renderBinary(binary, false, null),"Global: Entropy");

		// Local method
		GThresholdImageOps.localMean(input, binary, 28, 1.0, true, null, null);
		gui.addImage(VisualizeBinaryData.renderBinary(binary, false, null),"Local: Square");
		GThresholdImageOps.localBlockMinMax(input, binary, 10, 1.0, true, 15 );
		gui.addImage(VisualizeBinaryData.renderBinary(binary, false, null),"Local: Block Min-Max");
		GThresholdImageOps.localGaussian(input, binary, 42, 1.0, true, null, null);
		gui.addImage(VisualizeBinaryData.renderBinary(binary, false, null),"Local: Gaussian");
		GThresholdImageOps.localSauvola(input, binary, 5, 0.30f, true);
		gui.addImage(VisualizeBinaryData.renderBinary(binary, false, null),"Local: Sauvola");

		// Sauvola is tuned for text image.  Change radius to make it run better in others.

		// Show the image image for reference
		gui.addImage(ConvertBufferedImage.convertTo(input,null),"Input Image");

		String fileName =  imageName.substring(imageName.lastIndexOf('/')+1);
		ShowImages.showWindow(gui,fileName);
	}

	public static void main(String[] args) {
		// example in which global thresholding works best
		threshold(UtilIO.pathExample("particles01.jpg"));
		// example in which adaptive/local thresholding works best
		threshold(UtilIO.pathExample("segment/uneven_lighting_squares.jpg"));
		// hand written text with non-uniform stained background
		threshold(UtilIO.pathExample("segment/stained_handwriting.jpg"));
	}
}
