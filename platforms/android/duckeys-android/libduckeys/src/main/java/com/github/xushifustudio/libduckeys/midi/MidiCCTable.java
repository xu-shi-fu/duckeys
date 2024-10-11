package com.github.xushifustudio.libduckeys.midi;


import android.content.Context;

import com.github.xushifustudio.libduckeys.R;


public final class MidiCCTable {


    private MidiCCTable() {
    }

    //////////////////////////////////////////////////////////////////
    // public


    public static class Item {
        public int index;
        public byte cc;
        public String label;
        public String description;
    }

    public static Item find(Context ctx, byte cc) {
        Item[] all = listAll(ctx);
        Item item1 = null;
        final int index = cc;
        if ((0 <= index) && (index < all.length)) {
            item1 = all[index];
        }
        return cloneItem(item1);
    }

    //////////////////////////////////////////////////////////////////
    // private

    private static Item[] cache;

    private static Item[] listAll(Context ctx) {
        Item[] all = cache;
        if (all == null) {
            all = loadAll(ctx);
            cache = all;
        }
        return all;
    }

    private static Item[] loadAll(Context ctx) {
        int[] src = ids();
        Item[] dst = new Item[src.length];
        for (int i = 0; i < src.length; i++) {
            int id = src[i];
            String desc = ctx.getString(id);
            String label = "CC" + i;
            Item item = new Item();
            item.cc = (byte) i;
            item.index = i;
            item.label = label;
            item.description = desc;
            dst[i] = item;
        }
        return dst;
    }


    private static int[] ids() {
        int[] list = new int[]{
                /* 0x */
                R.string.midi_cc_0,
                R.string.midi_cc_1,
                R.string.midi_cc_2,
                R.string.midi_cc_3,
                R.string.midi_cc_4,
                R.string.midi_cc_5,
                R.string.midi_cc_6,
                R.string.midi_cc_7,
                R.string.midi_cc_8,
                R.string.midi_cc_9,

                /* 1x */
                R.string.midi_cc_10,
                R.string.midi_cc_11,
                R.string.midi_cc_12,
                R.string.midi_cc_13,
                R.string.midi_cc_14,
                R.string.midi_cc_15,
                R.string.midi_cc_16,
                R.string.midi_cc_17,
                R.string.midi_cc_18,
                R.string.midi_cc_19,

                /* 2x */
                R.string.midi_cc_20,
                R.string.midi_cc_21,
                R.string.midi_cc_22,
                R.string.midi_cc_23,
                R.string.midi_cc_24,
                R.string.midi_cc_25,
                R.string.midi_cc_26,
                R.string.midi_cc_27,
                R.string.midi_cc_28,
                R.string.midi_cc_29,

                /* 3x */
                R.string.midi_cc_30,
                R.string.midi_cc_31,
                R.string.midi_cc_32,
                R.string.midi_cc_33,
                R.string.midi_cc_34,
                R.string.midi_cc_35,
                R.string.midi_cc_36,
                R.string.midi_cc_37,
                R.string.midi_cc_38,
                R.string.midi_cc_39,

                /* 4x */
                R.string.midi_cc_40,
                R.string.midi_cc_41,
                R.string.midi_cc_42,
                R.string.midi_cc_43,
                R.string.midi_cc_44,
                R.string.midi_cc_45,
                R.string.midi_cc_46,
                R.string.midi_cc_47,
                R.string.midi_cc_48,
                R.string.midi_cc_49,

                /* 5x */
                R.string.midi_cc_50,
                R.string.midi_cc_51,
                R.string.midi_cc_52,
                R.string.midi_cc_53,
                R.string.midi_cc_54,
                R.string.midi_cc_55,
                R.string.midi_cc_56,
                R.string.midi_cc_57,
                R.string.midi_cc_58,
                R.string.midi_cc_59,

                /* 6x */
                R.string.midi_cc_60,
                R.string.midi_cc_61,
                R.string.midi_cc_62,
                R.string.midi_cc_63,
                R.string.midi_cc_64,
                R.string.midi_cc_65,
                R.string.midi_cc_66,
                R.string.midi_cc_67,
                R.string.midi_cc_68,
                R.string.midi_cc_69,

                /* 7x */
                R.string.midi_cc_70,
                R.string.midi_cc_71,
                R.string.midi_cc_72,
                R.string.midi_cc_73,
                R.string.midi_cc_74,
                R.string.midi_cc_75,
                R.string.midi_cc_76,
                R.string.midi_cc_77,
                R.string.midi_cc_78,
                R.string.midi_cc_79,

                /* 8x */
                R.string.midi_cc_80,
                R.string.midi_cc_81,
                R.string.midi_cc_82,
                R.string.midi_cc_83,
                R.string.midi_cc_84,
                R.string.midi_cc_85,
                R.string.midi_cc_86,
                R.string.midi_cc_87,
                R.string.midi_cc_88,
                R.string.midi_cc_89,

                /* 9x */
                R.string.midi_cc_90,
                R.string.midi_cc_91,
                R.string.midi_cc_92,
                R.string.midi_cc_93,
                R.string.midi_cc_94,
                R.string.midi_cc_95,
                R.string.midi_cc_96,
                R.string.midi_cc_97,
                R.string.midi_cc_98,
                R.string.midi_cc_99,

                /* 10x */
                R.string.midi_cc_100,
                R.string.midi_cc_101,
                R.string.midi_cc_102,
                R.string.midi_cc_103,
                R.string.midi_cc_104,
                R.string.midi_cc_105,
                R.string.midi_cc_106,
                R.string.midi_cc_107,
                R.string.midi_cc_108,
                R.string.midi_cc_109,

                /* 11x */
                R.string.midi_cc_110,
                R.string.midi_cc_111,
                R.string.midi_cc_112,
                R.string.midi_cc_113,
                R.string.midi_cc_114,
                R.string.midi_cc_115,
                R.string.midi_cc_116,
                R.string.midi_cc_117,
                R.string.midi_cc_118,
                R.string.midi_cc_119,

                /* 12x */
                R.string.midi_cc_12x,
                R.string.midi_cc_12x,
                R.string.midi_cc_12x,
                R.string.midi_cc_12x,
                R.string.midi_cc_12x,
                R.string.midi_cc_12x,
                R.string.midi_cc_12x,
                R.string.midi_cc_12x,
        };
        if (list.length != 128) {
            throw new RuntimeException("MidiCCTable.ids.length != 128");
        }
        return list;
    }

    private static Item cloneItem(Item src) {
        Item dst = new Item();
        if (src != null) {
            dst.index = src.index;
            dst.cc = src.cc;
            dst.label = src.label;
            dst.description = src.description;
        }
        return dst;
    }
}
