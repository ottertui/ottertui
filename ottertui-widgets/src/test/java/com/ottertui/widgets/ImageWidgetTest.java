package com.ottertui.widgets;

import com.ottertui.core.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import static org.junit.jupiter.api.Assertions.*;

class ImageWidgetTest {

    private BufferedImage testImage() {
        BufferedImage img = new BufferedImage(16, 12, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 12; y++) {
            for (int x = 0; x < 16; x++) {
                img.setRGB(x, y, (x * 16) << 16 | (y * 20) << 8);
            }
        }
        return img;
    }

    @Test
    @DisplayName("fromImage creates widget with default protocol")
    void fromImageDefault() {
        ImageWidget w = ImageWidget.fromImage(testImage(), 10, 8);
        assertNotNull(w);
        assertEquals(10, w.cellWidth());
        assertEquals(8, w.cellHeight());
    }

    @Test
    @DisplayName("fromImage with explicit protocol")
    void fromImageExplicitProtocol() {
        ImageWidget w = ImageWidget.fromImage(testImage(), 20, 15,
            TerminalImage.Protocol.KITTY);
        assertEquals(TerminalImage.Protocol.KITTY, w.protocol());
    }

    @Test
    @DisplayName("fromBytes creates widget")
    void fromBytes() throws Exception {
        var baos = new ByteArrayOutputStream();
        ImageIO.write(testImage(), "PNG", baos);
        byte[] data = baos.toByteArray();

        ImageWidget w = ImageWidget.fromBytes(data, 15, 10,
            TerminalImage.Protocol.ITERM2);
        assertNotNull(w);
        assertEquals(TerminalImage.Protocol.ITERM2, w.protocol());
    }

    @Test
    @DisplayName("fromBytes without protocol auto-detects")
    void fromBytesAutoDetect() throws Exception {
        var baos = new ByteArrayOutputStream();
        ImageIO.write(testImage(), "PNG", baos);
        byte[] data = baos.toByteArray();

        ImageWidget w = ImageWidget.fromBytes(data, 10, 10);
        assertNotNull(w);
        assertNotNull(w.protocol());
    }

    @Test
    @DisplayName("fromFile loads from disk")
    void fromFile(@TempDir Path tempDir) throws Exception {
        Path file = tempDir.resolve("test.png");
        ImageIO.write(testImage(), "PNG", file.toFile());

        ImageWidget w = ImageWidget.fromFile(file.toString(), 10, 10);
        assertNotNull(w);
        assertEquals(10, w.cellWidth());
    }

    @Test
    @DisplayName("fromFile with explicit protocol")
    void fromFileWithProtocol(@TempDir Path tempDir) throws Exception {
        Path file = tempDir.resolve("test.png");
        ImageIO.write(testImage(), "PNG", file.toFile());

        ImageWidget w = ImageWidget.fromFile(file.toString(), 8, 6,
            TerminalImage.Protocol.SIXEL);
        assertEquals(TerminalImage.Protocol.SIXEL, w.protocol());
    }

    @Test
    @DisplayName("fromResource loads from classpath")
    void fromResource() throws Exception {
        ImageWidget w = ImageWidget.fromResource("/test-image.png", 1, 1,
            TerminalImage.Protocol.KITTY);
        assertNotNull(w);
        assertEquals(1, w.cellWidth());
        assertEquals(1, w.cellHeight());
    }

    @Test
    @DisplayName("fromResource without protocol auto-detects")
    void fromResourceAutoDetect() throws Exception {
        ImageWidget w = ImageWidget.fromResource("/test-image.png", 1, 1);
        assertNotNull(w);
        assertNotNull(w.protocol());
    }

    @Test
    @DisplayName("fromResource throws on non-existent resource")
    void fromResourceThrows() {
        assertThrows(IOException.class, () ->
            ImageWidget.fromResource("/nonexistent.png", 10, 10));
    }

    @Test
    @DisplayName("fromFile throws on non-existent file")
    void fromFileThrows() {
        assertThrows(Exception.class, () ->
            ImageWidget.fromFile("/nonexistent/path.png", 10, 10));
    }

    // ── Render ──

    @Test
    @DisplayName("render with Kitty protocol does not throw")
    void renderKitty() {
        ImageWidget w = ImageWidget.fromImage(testImage(), 10, 8,
            TerminalImage.Protocol.KITTY);
        Buffer b = new Buffer(80, 24);
        w.render(new Rect(0, 0, 80, 24), b);
        assertEquals('\033', b.getCell(0, 0).ch());
    }

    @Test
    @DisplayName("render with iTerm2 protocol does not throw")
    void renderITerm2() {
        ImageWidget w = ImageWidget.fromImage(testImage(), 10, 8,
            TerminalImage.Protocol.ITERM2);
        Buffer b = new Buffer(80, 24);
        w.render(new Rect(0, 0, 80, 24), b);
        assertEquals('\033', b.getCell(0, 0).ch());
    }

    @Test
    @DisplayName("render with Sixel protocol does not throw")
    void renderSixel() {
        ImageWidget w = ImageWidget.fromImage(testImage(), 10, 8,
            TerminalImage.Protocol.SIXEL);
        Buffer b = new Buffer(80, 24);
        w.render(new Rect(0, 0, 80, 24), b);
        assertEquals('\033', b.getCell(0, 0).ch());
    }

    @Test
    @DisplayName("render at offset position")
    void renderAtOffset() {
        ImageWidget w = ImageWidget.fromImage(testImage(), 5, 4,
            TerminalImage.Protocol.KITTY);
        Buffer b = new Buffer(80, 24);
        w.render(new Rect(10, 5, 20, 10), b);
        assertEquals('\033', b.getCell(10, 5).ch());
    }

    @Test
    @DisplayName("render with auto-detected protocol")
    void renderAutoDetect() {
        ImageWidget w = ImageWidget.fromImage(testImage(), 6, 4);
        Buffer b = new Buffer(80, 24);
        assertDoesNotThrow(() -> w.render(new Rect(0, 0, 80, 24), b));
    }

    @Test
    @DisplayName("render with iTerm2 protocol from bytes")
    void renderITerm2FromBytes() throws Exception {
        var baos = new ByteArrayOutputStream();
        ImageIO.write(testImage(), "PNG", baos);
        ImageWidget w = ImageWidget.fromBytes(baos.toByteArray(), 10, 8,
            TerminalImage.Protocol.ITERM2);
        Buffer b = new Buffer(80, 24);
        w.render(new Rect(0, 0, 80, 24), b);
        assertEquals('\033', b.getCell(0, 0).ch());
    }

    @Test
    @DisplayName("render with corrupt data for sixel returns early")
    void renderCorruptSixelData() {
        // Sixel needs valid image bytes; corrupt bytes cause render to return early
        byte[] corrupt = new byte[]{0x00, 0x01, 0x02};
        ImageWidget w = ImageWidget.fromBytes(corrupt, 10, 8,
            TerminalImage.Protocol.SIXEL);
        Buffer b = new Buffer(80, 24);
        // Should not throw — render returns early when ImageIO.read fails
        assertDoesNotThrow(() -> w.render(new Rect(0, 0, 80, 24), b));
    }

    @Test
    @DisplayName("cellWidth and cellHeight reflect display size")
    void displayDimensions() {
        ImageWidget w = ImageWidget.fromImage(testImage(), 30, 20,
            TerminalImage.Protocol.KITTY);
        assertEquals(30, w.cellWidth());
        assertEquals(20, w.cellHeight());
    }
}
