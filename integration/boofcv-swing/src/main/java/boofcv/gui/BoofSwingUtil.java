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

package boofcv.gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.prefs.Preferences;

public class BoofSwingUtil {

	public static File openFileChooseDialog(Component parent) {
		return openFileChooseDialog(parent,new File(".").getPath());
	}

	public static File openFileChooseDialog(Component parent, String defaultPath ) {
		String key = "PreviouslySelected";

		Preferences prefs = Preferences.userRoot().node(parent.getClass().getSimpleName());
		String previousPath=prefs.get(key, defaultPath);
		JFileChooser chooser = new JFileChooser(previousPath);

		File selected = null;
		int returnVal = chooser.showOpenDialog(parent);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			selected = chooser.getSelectedFile();
			prefs.put(key, selected.getParent());
		}
		return selected;
	}

	public static void invokeNowOrLater(Runnable r ) {
		if(SwingUtilities.isEventDispatchThread() ) {
			r.run();
		} else {
			SwingUtilities.invokeLater(r);
		}
	}

	public static void checkGuiThread() {
		if( !SwingUtilities.isEventDispatchThread() )
			throw new RuntimeException("Must be run in UI thread");
	}
}
