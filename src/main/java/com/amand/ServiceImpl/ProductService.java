package com.amand.ServiceImpl;

import com.amand.Utils.FileUtils;
import com.amand.constant.SystemConstant;
import com.amand.converter.ProductConverter;
import com.amand.dto.ProductDto;
import com.amand.entity.CategoryEntity;
import com.amand.entity.ProductEntity;
import com.amand.form.ProductForm;
import com.amand.repository.CategoryRepository;
import com.amand.repository.ColorRepository;
import com.amand.repository.ProductRepository;
import com.amand.repository.SizeRepository;
import com.amand.service.IProductService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class ProductService implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductConverter productConverter;

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ProductDto save(ProductForm productForm)  {
        ProductEntity productEntity = productConverter.toEntity(productForm);
        CategoryEntity categoryEntity = categoryRepository.findOneByCode(productForm.getCategoryCode());
        productEntity.setCategory(categoryEntity);
        productEntity.setColors(colorRepository.findAllByIds(productForm.getColorIds()));
        productEntity.setSizes(sizeRepository.findAllByIds(productForm.getSizeIds()));
        String image = fileUtils.uploadFile(productForm.getImage1());
        productEntity.setImage1(image);
        image = fileUtils.uploadFile(productForm.getImage2());
        productEntity.setImage2(image);
        image = fileUtils.uploadFile(productForm.getImage3());
        productEntity.setImage3(image);
        image = fileUtils.uploadFile(productForm.getImage4());
        productEntity.setImage4(image);
        productEntity = productRepository.save(productEntity);
        return productConverter.toDto(productEntity);
    }

    @Override
    public Map<String, String> validate(ProductForm productForm) {
        Map<String, String> results = new HashMap<>();
        if (Strings.isBlank(productForm.getCategoryCode())) {
            results.put("MessageCategory", "Bạn không được để trống thông tin thể loại sản phẩm");
        }
        if (Strings.isNotBlank(productForm.getName())) {
            if (StringUtils.hasLength(productRepository.findOneNameByName(productForm.getName()))) {
                results.put("MessageName", "Tên sản phẩm đã tồn tại");
            }
        } else {
            results.put("MessageName", "Bạn không được để trống thông tin tên sản phẩm");
        }

        if (productForm.getOldPrice() == null) {
            results.put("MessageOldPrice", "Bạn không được để trống thông tin giá sản phẩm");
        }

        if (productForm.getAmount() == null) {
            results.put("MessageAmount", "Bạn không được để trống thông tin số lượng sản phẩm");
        }

        if (Strings.isBlank(productForm.getSeason())) {
            results.put("MessageSeason", "Bạn không được để trống thông tin mùa sản phẩm");
        }

        if (CollectionUtils.isEmpty(productForm.getColorIds())) {
            results.put("MessageColor", "Bạn không được để trống thông tin màu sản phẩm");
        }

        if (CollectionUtils.isEmpty(productForm.getSizeIds())) {
            results.put("MessageSize", "Bạn không được để trống thông tin kích thước sản phẩm");
        }

        String errorImage1 = getErrorImage(productForm.getImage1());
        if (Strings.isNotBlank(errorImage1)) {
            results.put("MessageImage1", errorImage1);
        }

        String errorImage2 = getErrorImage(productForm.getImage2());
        if (Strings.isNotBlank(errorImage2)) {
            results.put("MessageImage2", errorImage2);
        }

        String errorImage3 = getErrorImage(productForm.getImage3());
        if (Strings.isNotBlank(errorImage3)) {
            results.put("MessageImage3", errorImage3);
        }

        String errorImage4 = getErrorImage(productForm.getImage4());
        if (Strings.isNotBlank(errorImage4)) {
            results.put("MessageImage4", errorImage4);
        }

        return results;
    }

    @Override
    public List<ProductDto> findAll(Pageable pageable) {
        List<ProductDto> productDtos = new ArrayList<>();
        List<ProductEntity> productEntities = productRepository.findAll(pageable).getContent();
        for (ProductEntity productEntity : productEntities) {
            ProductDto productDto = productConverter.toDto(productEntity);
            CategoryEntity categoryEntity = categoryRepository.findOneById(productEntity.getCategory().getId());
            productDto.setCategoryName(categoryEntity.getName());
            productDtos.add(productDto);
        }
        return productDtos;
    }

    @Override
    public int getTotalItem() {
        return (int) productRepository.count();
    }

    private String getErrorImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return "Bạn không được để trống thông tin ảnh sản phẩm";
        }
        String fileName = image.getOriginalFilename() == null ? "" : image.getOriginalFilename().toLowerCase(Locale.ROOT);
        if (!fileName.endsWith(".png") && !fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg")) {
            return "Vui lòng upfile thuộc các định dạng sau 'png', 'jpg', 'jpeg'";
        }
        if (image.getSize() > SystemConstant.IMAGE_SIZE) {
            return "Vui lòng upfile nhỏ hơn 6MB";
        }
        return "";
    }
}