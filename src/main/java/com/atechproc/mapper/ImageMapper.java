package com.atechproc.mapper;

import com.atechproc.dto.ImageDto;
import com.atechproc.model.Image;

import java.util.List;

public class ImageMapper {
    public static ImageDto toDto(Image image) {
        ImageDto dto = new ImageDto();
        dto.setId(image.getId());
        dto.setImageName(image.getFileName());
        dto.setDownloadUrl(image.getDownloadUrl());
        return dto;
    }

    public static List<ImageDto> toDTOs(List<Image> images) {
        return images.stream()
                .map(ImageMapper::toDto).toList();
    }
}
