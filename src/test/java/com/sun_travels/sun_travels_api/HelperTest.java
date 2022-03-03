package com.sun_travels.sun_travels_api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HelperTest {

    @Test
    void canCapitalizeFirstLetterOfEveryWord() {
        assertThat(Helper.capitalizeFirstLetterOfEveryWord("MARINO BEACH")).isEqualTo("Marino Beach");
        assertThat(Helper.capitalizeFirstLetterOfEveryWord("maRiNo BEaCh")).isEqualTo("Marino Beach");
    }

    @Test
    void canRoundToTwoDecimals() {
        assertThat(Helper.roundToTwoDecimals(Double.parseDouble("012"))).isEqualTo(Double.parseDouble("12.00"));
        assertThat(Helper.roundToTwoDecimals(Double.parseDouble("12"))).isEqualTo(Double.parseDouble("12.00"));
        assertThat(Helper.roundToTwoDecimals(Double.parseDouble("12.0"))).isEqualTo(Double.parseDouble("12.00"));
        assertThat(Helper.roundToTwoDecimals(Double.parseDouble("12.00"))).isEqualTo(Double.parseDouble("12.00"));
        assertThat(Helper.roundToTwoDecimals(Double.parseDouble("12.00"))).isEqualTo(Double.parseDouble("12.00"));
        assertThat(Helper.roundToTwoDecimals(Double.parseDouble("12.25"))).isEqualTo(Double.parseDouble("12.25"));
        assertThat(Helper.roundToTwoDecimals(Double.parseDouble("12.252"))).isEqualTo(Double.parseDouble("12.25"));
        assertThat(Helper.roundToTwoDecimals(Double.parseDouble("12.258"))).isEqualTo(Double.parseDouble("12.26"));
    }

}