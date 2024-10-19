package com.github.xushifustudio.libduckeys.ui.box2;

import java.util.Map;

public interface B2Style extends B2PropertyHandler {


    // property-names ////////////////////////////////////////////////////////////////


    // colors
    String color = "color"; // default, main, normal, foreground
    String text_color = "text-color";
    String background_color = "background-color";


    // borders
    String border = "border";
    String border_color = "border-color";
    String border_width = "border-width";
    String border_style = "border-style";

    String border_top = "border-top";
    String border_top_color = "border-top-color";
    String border_top_style = "border-top-style";
    String border_top_width = "border-top-width";

    String border_left = "border-left";
    String border_left_color = "border-left-color";
    String border_left_style = "border-left-style";
    String border_left_width = "border-left-width";

    String border_right = "border-right";
    String border_right_color = "border-right-color";
    String border_right_style = "border-right-style";
    String border_right_width = "border-right-width";

    String border_bottom = "border-bottom";
    String border_bottom_color = "border-bottom-color";
    String border_bottom_style = "border-bottom-style";
    String border_bottom_width = "border-bottom-width";


    String border_radius = "border-radius";
    String border_radius_x = "border-radius-x";
    String border_radius_y = "border-radius-y";


    // margins
    String margin = "margin";
    String margin_top = "margin-top";
    String margin_left = "margin-left";
    String margin_right = "margin-right";
    String margin_bottom = "margin-bottom";

    // paddings
    String padding = "padding";
    String padding_top = "padding-top";
    String padding_left = "padding-left";
    String padding_right = "padding-right";
    String padding_bottom = "padding-bottom";


    // font
    String font_size = "font-size";

    // align
    String align = "align";


    // methods ////////////////////////////////////////////////////////////////

    Map<String, B2PropertyHolder> fetchAll(Map<String, B2PropertyHolder> dest);

    B2PropertyHolder get(String name);

    B2PropertyHolder get(String name, B2State state);

    void put(B2PropertyHolder h);

    long revision();

    // 返回：revision
    long update();
}
