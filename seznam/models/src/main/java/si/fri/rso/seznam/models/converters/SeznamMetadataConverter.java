package si.fri.rso.seznam.models.converters;

import si.fri.rso.seznam.lib.SeznamMetadata;
import si.fri.rso.seznam.models.entities.SeznamMetadataEntity;

public class SeznamMetadataConverter {

    public static SeznamMetadata toDto(SeznamMetadataEntity entity) {

        SeznamMetadata dto = new SeznamMetadata();
        dto.setCocktailId(entity.getCocktailId());
        dto.setUser(entity.getUser());
        dto.setName(entity.getName());

        return dto;

    }

    public static SeznamMetadataEntity toEntity(SeznamMetadata dto) {

        SeznamMetadataEntity entity = new SeznamMetadataEntity();
        entity.setCocktailId(dto.getCocktailId());
        entity.setUser(dto.getUser());
        entity.setName(dto.getName());

        return entity;

    }

}
