package si.fri.rso.koktejli.models.converters;

import si.fri.rso.koktejli.lib.KoktejliMetadata;
import si.fri.rso.koktejli.models.entities.KoktejliMetadataEntity;

public class KoktejliMetadataConverter {

    public static KoktejliMetadata toDto(KoktejliMetadataEntity entity) {

        KoktejliMetadata dto = new KoktejliMetadata();
        dto.setImageId(entity.getId());
        dto.setCreated(entity.getCreated());
        dto.setDescription(entity.getDescription());
        dto.setTitle(entity.getTitle());
        dto.setHeight(entity.getHeight());
        dto.setWidth(entity.getWidth());
        dto.setUri(entity.getUri());

        return dto;

    }

    public static KoktejliMetadataEntity toEntity(KoktejliMetadata dto) {

        KoktejliMetadataEntity entity = new KoktejliMetadataEntity();
        entity.setCreated(dto.getCreated());
        entity.setDescription(dto.getDescription());
        entity.setTitle(dto.getTitle());
        entity.setHeight(dto.getHeight());
        entity.setWidth(dto.getWidth());
        entity.setUri(dto.getUri());

        return entity;

    }

}
