package com.github.xushifustudio.libduckeys.settings;

import com.github.xushifustudio.libduckeys.helper.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SettingBankNamer {

    private String mPrefix;
    private String mSuffix;
    private StringBuilder mBuilder;

    public SettingBankNamer() {
        mBuilder = new StringBuilder();
        mPrefix = "";
        mSuffix = "";
    }

    public SettingBankNamer addParam(String param) {
        mBuilder.append(param);
        mBuilder.append('\n');
        return this;
    }

    public SettingBankNamer setPrefix(String prefix) {
        mPrefix = prefix;
        return this;
    }

    public SettingBankNamer setSuffix(String suffix) {
        mSuffix = suffix;
        return this;
    }


    private String digest(String str) {
        if (str == null) {
            str = "";
        }
        try {
            MessageDigest md = MessageDigest.getInstance("sha-1");
            byte[] sum = md.digest(str.getBytes(StandardCharsets.UTF_8));
            return Hex.stringify(sum);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return String.valueOf(str.hashCode());
        }
    }


    public String name() {
        String params = mBuilder.toString();
        String sum = digest(params);
        mBuilder.setLength(0);
        mBuilder.append(mPrefix);
        mBuilder.append(sum);
        mBuilder.append(mSuffix);
        return mBuilder.toString();
    }
}
