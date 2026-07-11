package com.ottertui.widgets;

import com.ottertui.core.*;

final class BrailleUtils {
    private static final char BRAILLE_BASE = '\u2800';

    private BrailleUtils() {}

    static int cellToDotX(int cellX) { return cellX * 2; }
    static int cellToDotY(int cellY) { return cellY * 4; }

    static char toBrailleChar(boolean[] dots) {
        int pattern = 0;
        for (int i = 0; i < 8 && i < dots.length; i++) {
            if (dots[i]) pattern |= (1 << i);
        }
        return (char) (BRAILLE_BASE | pattern);
    }

    @FunctionalInterface
    interface DotSampler {
        Style sample(int dx, int dy);
    }

    static void renderBrailleCells(Buffer buffer, int ox, int oy,
                                    int cellW, int cellH, DotSampler sampler) {
        for (int cy = 0; cy < cellH; cy++) {
            int baseDy = cellToDotY(cy);
            for (int cx = 0; cx < cellW; cx++) {
                int baseDx = cellToDotX(cx);
                boolean[] dots = new boolean[8];
                Style cellStyle = null;

                for (int dotRow = 0; dotRow < 4; dotRow++) {
                    int dy = baseDy + dotRow;
                    for (int dotCol = 0; dotCol < 2; dotCol++) {
                        int dx = baseDx + dotCol;
                        Style s = sampler.sample(dx, dy);
                        if (s != null) {
                            dots[dotRow * 2 + dotCol] = true;
                            cellStyle = s;
                        }
                    }
                }

                if (cellStyle != null) {
                    buffer.setCell(ox + cx, oy + cy,
                        new Cell(toBrailleChar(dots), cellStyle));
                }
            }
        }
    }
}
