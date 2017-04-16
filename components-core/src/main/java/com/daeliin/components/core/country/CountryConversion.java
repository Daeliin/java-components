package com.daeliin.components.core.country;

import com.daeliin.components.core.sql.BCountry;
import com.daeliin.components.domain.resource.Conversion;

import java.sql.Timestamp;

public final class CountryConversion implements Conversion<Country, BCountry> {

    @Override
    public Country instantiate(BCountry bCountry) {
        if (bCountry == null) {
            return null;
        }

        return new Country(
                bCountry.getUuid(),
                bCountry.getCreationDate().toLocalDateTime(),
                bCountry.getCode(),
                bCountry.getName());
    }

    @Override
    public BCountry map(Country country) {
        if (country == null) {
            return null;
        }

        return new BCountry(
                country.code,
                Timestamp.valueOf(country.creationDate()),
                country.name,
                country.uuid());
    }
}
