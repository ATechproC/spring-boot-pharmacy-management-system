package com.atechproc.service.image;

import com.atechproc.dto.ImageDto;
import com.atechproc.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id, Long medicineId) throws Exception;
    List<ImageDto> saveImages(List<MultipartFile> file, Long medicineId);
    void updateImage(MultipartFile file, Long medicineId);
}
