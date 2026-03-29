package com.atechproc.service.image;

import com.atechproc.dto.ImageDto;
import com.atechproc.exception.ResourceNotFoundException;
import com.atechproc.mapper.ImageMapper;
import com.atechproc.model.Image;
import com.atechproc.model.Medicine;
import com.atechproc.repository.ImageRepository;
import com.atechproc.service.medicine.IMedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;
    private final IMedicineService medicineService;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with id " + id));
    }

    @Override
    @PreAuthorize("hasAnyRole('PHARMACY_OWNER', 'PHARMACIST')")
    public void deleteImageById(Long id, Long medicineId) throws Exception {
        Medicine medicine = medicineService.getMedicineById(medicineId);
        Image image = getImageById(id);
        if(!medicine.getImages().contains(image)) {
            throw new Exception("You are not allowed to delete this image");
        }
        imageRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("hasAnyRole('PHARMACY_OWNER', 'PHARMACIST')")
    public List<ImageDto> saveImages(List<MultipartFile> files, Long medicineId) {
        Medicine medicine = medicineService.getMedicineById(medicineId);
        List<ImageDto> savedImageDto = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setMedicine(medicine);

                String buildDownloadUrl = "/api/v1/images/image/download/";
                String downloadUrl = buildDownloadUrl + image.getId();
                image.setDownloadUrl(downloadUrl);

                Image savedImage = imageRepository.save(image);

                savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());
                Image savedImage1 = imageRepository.save(savedImage);

                ImageDto imageDto = ImageMapper.toDto(savedImage1);
                savedImageDto.add(imageDto);
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDto;
    }

    @Override
    @PreAuthorize("hasAnyRole('PHARMACY_OWNER', 'PHARMACIST')")
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
