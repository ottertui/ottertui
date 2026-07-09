package com.ottertui.widgets;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Pure Java Sixel image encoder.
 *
 * <p>Sixel encodes images using 6-pixel vertical strips. Each printable
 * ASCII character (0x3F–0x7E) represents 6 pixels, where bit 0 (LSB) is
 * the top pixel and bit 5 is the bottom pixel.</p>
 *
 * <p>Format (per band): {@code #<idx>;<r>;<g>;<b>} defines a palette entry,
 * then sixel characters render with that color. {@code $} returns to
 * line-start, {@code -} advances to the next 6-pixel band.</p>
 */
final class SixelEncoder {

    private SixelEncoder() {}

    /** Sixel character base: '?' = 0x3F = 63 */
    private static final int SIXEL_BASE = 63;

    /**
     * Encode a BufferedImage to a Sixel DCS escape sequence.
     *
     * @param image      source image (should already be scaled to target pixel dimensions)
     * @param cellWidth  display width in terminal columns
     * @param cellHeight display height in terminal rows
     */
    static String encode(BufferedImage image, int cellWidth, int cellHeight) {
        int pxWidth = image.getWidth();
        int pxHeight = image.getHeight();
        int bands = (pxHeight + 5) / 6;

        StringBuilder sb = new StringBuilder();
        sb.append("\033Pq"); // DCS Sixel begin
        sb.append("\"1;1;").append(cellWidth).append(";").append(cellHeight);

        // Quantize image colors into a palette up to 256 entries
        PaletteQuantizer quantizer = new PaletteQuantizer(image);

        for (int band = 0; band < bands; band++) {
            // Map pixel colors to palette indices for this band
            // Build runs of same-color sixels per column

            for (int x = 0; x < pxWidth; x++) {
                int lastColorIdx = -1;
                int sixelAccum = 0;
                int pixelCount = 0;

                for (int py = 0; py < 6; py++) {
                    int y = band * 6 + py;
                    if (y >= pxHeight) break;

                    int rgb = image.getRGB(x, y) & 0xFFFFFF;
                    int colorIdx = quantizer.lookup(rgb);

                    if (pixelCount > 0 && colorIdx != lastColorIdx) {
                        // Flush accumulated sixels
                        sb.append(colorDef(quantizer, lastColorIdx));
                        sb.append(sixelChar(sixelAccum));
                        sixelAccum = 0;
                        pixelCount = 0;
                    }
                    lastColorIdx = colorIdx;
                    sixelAccum |= (1 << py);
                    pixelCount++;
                }

                if (pixelCount > 0) {
                    sb.append(colorDef(quantizer, lastColorIdx));
                    sb.append(sixelChar(sixelAccum));
                }
            }

            if (band < bands - 1) {
                sb.append('-'); // next band
            }
        }

        sb.append("\033\\"); // ST (String Terminator)
        return sb.toString();
    }

    private static String colorDef(PaletteQuantizer q, int idx) {
        return q.colorDef(idx);
    }

    private static char sixelChar(int bits) {
        return (char) (SIXEL_BASE + (bits & 0x3F));
    }

    // --- Palette quantizer ---

    private static class PaletteQuantizer {
        private final List<Integer> palette = new ArrayList<>();
        private final Map<Integer, Integer> rgbToIndex = new LinkedHashMap<>();
        private final Map<Integer, String> colorDefs = new LinkedHashMap<>();
        private int lastDefinedIdx = -1;

        PaletteQuantizer(BufferedImage image) {
            buildPalette(image);
        }

        private void buildPalette(BufferedImage image) {
            int w = image.getWidth();
            int h = image.getHeight();
            // Simple frequency-based quantization: collect unique colors,
            // keep most frequent up to 256
            Map<Integer, Integer> freq = new LinkedHashMap<>();
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int rgb = image.getRGB(x, y) & 0xFFFFFF;
                    freq.merge(rgb, 1, Integer::sum);
                }
            }

            // Sort by frequency descending, keep top 255 (index 0 = default black)
            freq.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(255)
                .forEach(e -> {
                    int idx = palette.size() + 1; // 1-based palette (0 = undefined)
                    palette.add(e.getKey());
                    rgbToIndex.put(e.getKey(), idx);
                });
        }

        int lookup(int rgb) {
            Integer idx = rgbToIndex.get(rgb);
            if (idx != null) return idx;
            // Map to nearest existing color
            int bestIdx = 1;
            int bestDist = Integer.MAX_VALUE;
            for (int i = 0; i < palette.size(); i++) {
                int dist = colorDist(rgb, palette.get(i));
                if (dist < bestDist) {
                    bestDist = dist;
                    bestIdx = i + 1;
                }
            }
            return bestIdx;
        }

        String colorDef(int idx) {
            if (idx == lastDefinedIdx) return "";
            String def = colorDefs.computeIfAbsent(idx, i -> {
                int c = palette.get(i - 1);
                int r = (c >> 16) & 0xFF;
                int g = (c >> 8) & 0xFF;
                int b = c & 0xFF;
                // Scale 0-255 to 0-100 for sixel
                return "#" + i + ";2;"
                    + (r * 100 / 255) + ";"
                    + (g * 100 / 255) + ";"
                    + (b * 100 / 255);
            });
            lastDefinedIdx = idx;
            return def;
        }

        private static int colorDist(int a, int b) {
            int dr = ((a >> 16) & 0xFF) - ((b >> 16) & 0xFF);
            int dg = ((a >> 8) & 0xFF) - ((b >> 8) & 0xFF);
            int db = (a & 0xFF) - (b & 0xFF);
            return dr * dr + dg * dg + db * db;
        }
    }
}
