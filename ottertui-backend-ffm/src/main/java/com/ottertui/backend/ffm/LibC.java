package com.ottertui.backend.ffm;

import java.lang.foreign.Arena;
import java.lang.foreign.SymbolLookup;

final class LibC {

    private static final String[] FALLBACKS = {
        "/usr/lib/libSystem.B.dylib",           // macOS
        "/usr/lib64/libc.so.6",                 // Linux (some distros)
        "/lib/x86_64-linux-gnu/libc.so.6",      // Linux x86_64
        "/lib/aarch64-linux-gnu/libc.so.6",     // Linux ARM64
    };

    static final SymbolLookup LOOKUP = load();

    private static SymbolLookup load() {
        try {
            return SymbolLookup.libraryLookup("c", Arena.global());
        } catch (IllegalArgumentException e) {
            for (String path : FALLBACKS) {
                try {
                    return SymbolLookup.libraryLookup(path, Arena.global());
                } catch (IllegalArgumentException ignored) {
                }
            }
            throw new IllegalArgumentException(
                "Cannot load native C library. Tried short name 'c' and paths: "
                    + String.join(", ", FALLBACKS), e);
        }
    }

    private LibC() {}
}
