package com.github.xushifustudio.libduckeys.midi;

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

    public Note getRoot() {
        return root;
    }

    public ChordPattern getPattern() {
        return pattern;
    }
}
