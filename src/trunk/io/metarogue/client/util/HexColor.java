package io.metarogue.client.util;

/**
 * MetaRogue class
 * User: andrew
 * Date: 1/12/14
 * time: 1:43 PM
 */
public class HexColor {

    static float[] white = {1f,1f,1f};

    public HexColor() {
        // Auto-generated constructor
    }

    public static float[] convert(String hex) {
        if(hex.length() == 6) {
            // Get substrings of each color (ie D0 or FF)
            String r = hex.substring(0,2);
            String g = hex.substring(2,4);
            String b = hex.substring(4,6);
            // Convert to actual mathematical bytes
            int rInt = Integer.parseInt(r, 16);
            int gInt = Integer.parseInt(g, 16);
            int bInt = Integer.parseInt(b, 16);
            // Check that shit for silliness
            if(rInt >= 0 && rInt <= 255 && gInt >= 0 && gInt <= 255 && bInt >= 0 && bInt <= 255) {
                float[] rFloat = {rInt/255, gInt/255, bInt/255};
                return rFloat;
            }
        }
        return white;
    }

}
