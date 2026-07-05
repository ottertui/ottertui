package com.ottertui.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class CharWidthTest {

    @Test
    @DisplayName("ASCII characters have width 1")
    void asciiWidth1() {
        assertEquals(5, CharWidth.displayWidth("Hello"));
        assertEquals(1, CharWidth.displayWidth("a"));
        assertEquals(3, CharWidth.displayWidth("abc"));
    }

    @Test
    @DisplayName("empty string has width 0")
    void emptyString() {
        assertEquals(0, CharWidth.displayWidth(""));
    }

    @Test
    @DisplayName("CJK characters have width 2")
    void cjkWidth2() {
        assertEquals(2, CharWidth.displayWidth("中"));
        assertEquals(4, CharWidth.displayWidth("中文"));
        assertEquals(6, CharWidth.displayWidth("日本語"));
    }

    @Test
    @DisplayName("Korean Hangul has width 2")
    void hangulWidth2() {
        assertEquals(2, CharWidth.displayWidth("한"));
        assertEquals(4, CharWidth.displayWidth("한글"));
    }

    @Test
    @DisplayName("tab character has width 4")
    void tabWidth4() {
        assertEquals(4, CharWidth.displayWidth("\t"));
        assertEquals(6, CharWidth.displayWidth("a\tb"));
    }

    @Test
    @DisplayName("control characters have width 0")
    void controlCharsWidth0() {
        assertEquals(0, CharWidth.displayWidth("\u0001"));
        assertEquals(0, CharWidth.displayWidth("\u001F"));
    }

    @Test
    @DisplayName("zero-width space has width 0")
    void zeroWidthSpace() {
        assertEquals(2, CharWidth.displayWidth("a\u200Bb"));
    }

    @Test
    @DisplayName("mixed ASCII and CJK")
    void mixedAsciiCjk() {
        assertEquals(4, CharWidth.displayWidth("a中b"));
    }

    @Test
    @DisplayName("emoji characters have width 2 via east-asian-wide range")
    void emojiWidth2() {
        assertEquals(2, CharWidth.displayWidth("😀"));
    }

    @Test
    @DisplayName("heart emoji has width 1")
    void heartEmojiWidth() {
        assertEquals(1, CharWidth.displayWidth("❤"));
    }

    @Test
    @DisplayName("fullwidth forms have width 2")
    void fullwidthWidth2() {
        assertEquals(2, CharWidth.displayWidth("Ａ"));
        assertEquals(4, CharWidth.displayWidth("ＡＢ"));
    }
}
