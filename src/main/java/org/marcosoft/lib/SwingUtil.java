package org.marcosoft.lib;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

public class SwingUtil {
    public static void center(Window window) {
        final Toolkit tk = Toolkit.getDefaultToolkit();
        final Dimension screenSize = tk.getScreenSize();
        final int screenWidth = screenSize.width;
        final int screenHeight = screenSize.height;

        window.setLocation(screenWidth / 2 - window.getWidth() / 2,
                screenHeight / 2 - window.getHeight() / 2);
    }
}
