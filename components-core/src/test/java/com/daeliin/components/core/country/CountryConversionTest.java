package com.daeliin.components.core.country;

import com.daeliin.components.core.fixtures.CountryFixtures;
import com.daeliin.components.core.library.CountryLibrary;
import com.daeliin.components.core.sql.BCountry;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public final class CountryConversionTest {

    private CountryConversion countryConversion = new CountryConversion();

    @Test
    public void shouldMapToNull_whenNull() {
        Country nullCountry = null;

        assertThat(countryConversion.map(nullCountry)).isNull();
    }

    @Test
    public void shouldMapCountry() {
        BCountry mappedCountry = countryConversion.map(CountryLibrary.france());

        assertThat(mappedCountry).isEqualToComparingFieldByField(CountryFixtures.france());
    }

    @Test
    public void shouldInstantiateNull_fromNull() {
        BCountry nullCountryRow = null;

        assertThat(countryConversion.instantiate(nullCountryRow)).isNull();
    }

    @Test
    public void shouldInstantiateAnCountry() {
        Country rebuiltCountry = countryConversion.instantiate(CountryFixtures.france());

        assertThat(rebuiltCountry).isEqualTo(CountryLibrary.france());
    }
}