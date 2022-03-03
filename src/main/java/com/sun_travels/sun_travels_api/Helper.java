package com.sun_travels.sun_travels_api;

import com.sun_travels.sun_travels_api.dtos.SearchAvailabilityQuery;
import com.sun_travels.sun_travels_api.enums.ContractType;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

public class Helper {
    private static final DecimalFormat df = new DecimalFormat("0.00");

    private Helper() { }

    /**
     * Converts every character to lowercase and capitalize the first letter of every word
     * @param text String that needs to be converted
     * @return Converted string
     */
    public static String capitalizeFirstLetterOfEveryWord( String text ) {
        char[] charArray = text.toLowerCase().toCharArray();
        boolean foundSpace = true;
        for(int i = 0; i < charArray.length; i++) {
            if(Character.isLetter(charArray[i])) {
                if(foundSpace) {
                    charArray[i] = Character.toUpperCase(charArray[i]);
                    foundSpace = false;
                }
            }
            else { foundSpace = true; }
        }
        return String.valueOf(charArray);
    }

    /**
     * @param options List of results of the search query
     * @return If exists same room types of the same hotel, the index of the GENERAL contract which the room type belongs to, otherwise -1
     */
    public static int getGeneralIndex( List<SearchAvailabilityQuery> options ) {
        for(int i = 0; i < options.size(); i++) {
            for(int j = (i+1); j < options.size(); j++) {
                if(Objects.equals(options.get(i).getHotelId(), options.get(j).getHotelId()) && Objects.equals(options.get(i).getRoomType(), options.get(j).getRoomType())) {
                    if(options.get(i).getContractType() == ContractType.GENERAL) { return i; }
                    else { return j; }
                }
            }
        }
        return -1;
    }

    public static double roundToTwoDecimals( double value ) {
        return Double.parseDouble(df.format(value));
    }
}