package com.ottertui.core;

public final class AnsiUtil {

    private AnsiUtil() {}

    public static String cursorTo(int row, int col) {
        return "\033[" + row + ";" + col + "H";
    }

    public static String styleToSgr(Style style) {
        var sb = new StringBuilder("\033[0");

        int fg = colorToFgCode(style.foreground());
        if (fg > 0) sb.append(';').append(fg);

        int bg = colorToBgCode(style.background());
        if (bg > 0) sb.append(';').append(bg);

        if (style.modifiers().contains(Modifier.BOLD))       sb.append(";1");
        if (style.modifiers().contains(Modifier.DIM))         sb.append(";2");
        if (style.modifiers().contains(Modifier.ITALIC))      sb.append(";3");
        if (style.modifiers().contains(Modifier.UNDERLINE))   sb.append(";4");
        if (style.modifiers().contains(Modifier.REVERSED))    sb.append(";7");
        if (style.modifiers().contains(Modifier.CROSSED_OUT)) sb.append(";9");

        sb.append('m');
        return sb.toString();
    }

    private static String colorToFgStr(Color c) {
        if (c instanceof Color.Rgb rgb) {
            return "38;2;" + rgb.r() + ";" + rgb.g() + ";" + rgb.b();
        }
        if (c instanceof Color.Indexed idx) {
            return "38;5;" + idx.index();
        }
        int code = colorToFgCode(c);
        return code > 0 ? String.valueOf(code) : "";
    }

    private static int colorToFgCode(Color c) {
        if (c instanceof Color.Rgb rgb) {
            return -1;
        }
        if (c instanceof Color.Indexed idx) {
            return -1;
        }
        if (c == Color.RESET)       return -1;
        if (c == Color.BLACK)       return 30;
        if (c == Color.RED)         return 31;
        if (c == Color.GREEN)       return 32;
        if (c == Color.YELLOW)      return 33;
        if (c == Color.BLUE)        return 34;
        if (c == Color.MAGENTA)     return 35;
        if (c == Color.CYAN)        return 36;
        if (c == Color.WHITE)       return 37;
        if (c == Color.GRAY)        return 90;
        if (c == Color.DARK_GRAY)   return 90;
        if (c == Color.LIGHT_RED)   return 91;
        if (c == Color.LIGHT_GREEN) return 92;
        if (c == Color.LIGHT_YELLOW) return 93;
        if (c == Color.LIGHT_BLUE)  return 94;
        if (c == Color.LIGHT_MAGENTA) return 95;
        if (c == Color.LIGHT_CYAN)  return 96;
        return -1;
    }

    private static int colorToBgCode(Color c) {
        if (c instanceof Color.Rgb)  return -1;
        if (c instanceof Color.Indexed) return -1;
        int fg = colorToFgCode(c);
        return fg > 0 ? fg + 10 : -1;
    }
}
