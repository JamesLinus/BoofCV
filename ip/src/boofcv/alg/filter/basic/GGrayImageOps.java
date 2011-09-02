/*
 * Copyright 2011 Peter Abeles
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package boofcv.alg.filter.basic;

import boofcv.struct.image.ImageBase;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageSInt16;
import boofcv.struct.image.ImageUInt8;


/**
 * Weakly typed version of {@link GrayImageOps}.
 *
 * @author Peter Abeles
 */
@SuppressWarnings({"unchecked"})
public class GGrayImageOps {

	/**
	 * <p>
	 * Stretches the image's intensity:<br>
	 * O<sub>x,y</sub> = I<sub>x,y</sub>&gamma + beta<br>
	 * </p>
	 * <p>
	 * The image's intensity is clamped at 0 and max;
	 * </p>
	 *
	 * @param input  Input image. Not modified.
	 * @param output If not null, the output image.  If null a new image is declared and returned.  Modified.
	 * @return Output image.
	 */
	public static <T extends ImageBase> T stretch(T input, double gamma, double beta, double max , T output) {
		if( input instanceof ImageFloat32 ) {
			return (T)GrayImageOps.stretch((ImageFloat32)input,gamma,(float)beta,(float)max,(ImageFloat32)output);
		} else if( input instanceof ImageUInt8) {
			return (T)GrayImageOps.stretch((ImageUInt8)input,gamma,(int)beta,(int)max,(ImageUInt8)output);
		} else if( input instanceof ImageSInt16) {
			return (T)GrayImageOps.stretch((ImageSInt16)input,gamma,(int)beta,(int)max,(ImageSInt16)output);
		} else {
			throw new IllegalArgumentException("Unknown image type: "+input.getClass().getSimpleName());
		}
	}
}