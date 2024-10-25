package com.github.xushifustudio.libduckeys.midi;

public interface NotePattern {

    int TYPE_MODE = 1; // 音阶（调式）
    int TYPE_CHORD = 2; // 和弦

    /**
     * 返回类型（调式|和弦）
     */
    int type();

    /**
     * 返回调式|和弦的名称
     */
    String name();

    /**
     * 返回这一组音符的个数
     */
    int count();

    /**
     * 返回指定位置上，偏移的半音数（index=0：表示根音(chord)|1音(mode)）
     */
    int offset(int index);

    /***
     * 返回指定位置上，相对于 base 的音符
     * */
    Note getNote(int index, Note base);

}
