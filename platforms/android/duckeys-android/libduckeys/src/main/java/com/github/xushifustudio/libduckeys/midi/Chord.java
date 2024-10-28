package com.github.xushifustudio.libduckeys.midi;

/**
 * Chord 表示一个和弦，例如：“Fm7”
 */
public class Chord {

    private final Note root;
    private final ChordPattern pattern;

    public Chord(Note _root, ChordPattern _pattern) {

        if (_root == null) {
            _root = Note.forNote(12 * 5);
        }
        if (_pattern == null) {
            throw new RuntimeException("param: ChordPattern is null");
        }

        this.root = _root;
        this.pattern = _pattern;
    }

    public static boolean equal(Chord a, Chord b) {
        if (a == null || b == null) {
            return false;
        }
        boolean b1 = Note.equal(a.root, b.root);
        boolean b2 = ChordPattern.equal(a.pattern, b.pattern);
        return (b1 && b2);
    }

    public Note getRoot() {
        return root;
    }

    public ChordPattern getPattern() {
        return pattern;
    }
}
