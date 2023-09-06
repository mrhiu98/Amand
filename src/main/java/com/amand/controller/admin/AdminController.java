package com.amand.controller.admin;

import com.amand.Utils.SecurityUtils;
import com.amand.constant.SystemConstant;
import com.amand.dto.*;
import com.amand.form.ColorForm;
import com.amand.form.SizeForm;
import com.amand.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IColorService colorService;

    @Autowired
    private ISizeService sizeService;

    @Autowired
    private  IProductService productService;

    @GetMapping("/home")
    public ModelAndView home(HttpSession session){
        ModelAndView mav = new ModelAndView("/admin/views/home");
        String fullName = "";
        String userName = "";
        if (SecurityUtils.getPrincipal() != null){
            fullName = SecurityUtils.getPrincipal().getFullName();
            userName = SecurityUtils.getPrincipal().getUsername();
        }
        mav.addObject("userName", userName);
        mav.addObject("fullName", fullName);
        return mav;
    }

    @GetMapping("/danh-sach-san-pham")
    public ModelAndView listProduct(@RequestParam (value = "page", defaultValue = "1") int page,
                                    @RequestParam (value = "limit", defaultValue = "5") int limit){
        ModelAndView mav = new ModelAndView("admin/views/ListProduct");
        Pageable pageable = PageRequest.of(page - 1, limit);
        List<ProductDto> productDtos = productService.findAll(pageable, SystemConstant.ACTIVE_STATUS);
        mav.addObject("productDtos", productDtos);
        int totalPages = (int) Math.ceil((double) productService.getTotalItem(SystemConstant.ACTIVE_STATUS) / limit);
        mav.addObject("totalPages", totalPages);
        mav.addObject("limit", limit);
        mav.addObject("page", page);
        return mav;
    }

    @GetMapping("/chinh-sua-thong-tin-san-pham")
    public ModelAndView editProduct(@RequestParam(value = "id") Integer id, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView("admin/views/EditProduct");
        ProductDto productDto = productService.findOneById(id);
        if (productDto == null) {
            mav.setViewName("redirect:/404");
            redirectAttributes.addFlashAttribute("messageError", "Sản phẩm không tồn tại, vui lòng chọn sản phẩm khác");
            return mav;
        } else {
            mav.addObject("productDto", productDto);
        }
        List<Integer> colorIds = productDto.getColorIds();
        mav.addObject("colorIds", colorIds);

        Integer categoryId = productDto.getCategoryId();
        CategoryDto categoryDto = categoryService.findOneById(categoryId);
        mav.addObject("categoryDto", categoryDto);

        List<Integer> sizeIds = productDto.getSizeIds();
        mav.addObject("sizeIds", sizeIds);

        List<CategoryDto> categoryDtos = categoryService.findAllByStatus(SystemConstant.ACTIVE_STATUS);
        mav.addObject("categoryDtos", categoryDtos);

        List<ColorDto> colorDtos = colorService.findAllByStatus(SystemConstant.ACTIVE_STATUS);
        mav.addObject("colorDtos", colorDtos);

        List<SizeDto> sizeDtos = sizeService.findAllByStatus(SystemConstant.ACTIVE_STATUS);
        mav.addObject("sizeDtos", sizeDtos);

        return mav;
    }

    @GetMapping("/them-san-pham")
    public ModelAndView createProduct() {
         ModelAndView mav = new ModelAndView("/admin/views/CreateProduct");
         mav.addObject("categories", categoryService.findAllByStatus(SystemConstant.ACTIVE_STATUS));
         mav.addObject("colors", colorService.findAllByStatus(SystemConstant.ACTIVE_STATUS));
         mav.addObject("sizes", sizeService.findAllByStatus(SystemConstant.ACTIVE_STATUS));
        return mav;
    }

    @GetMapping("/them-the-loai")
    public ModelAndView createCategory() {
        ModelAndView mav = new ModelAndView("admin/views/CreateCategory");
        return mav;
    }

    @GetMapping("/danh-sach-the-loai-san-pham")
    public ModelAndView listCategory(@RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "limit", defaultValue = "5") int limit) {
        ModelAndView mav = new ModelAndView("admin/views/ListCategories");
        Pageable pageable = PageRequest.of(page - 1, limit);
        List<CategoryDto> categoryDtos = categoryService.findAllByStatus(pageable, SystemConstant.ACTIVE_STATUS);
        mav.addObject("categoryDtos", categoryDtos);
        int totalItem = categoryService.getTotalItem(SystemConstant.ACTIVE_STATUS);
        mav.addObject("totalPage", (int) Math.ceil( (double) totalItem / limit));
        mav.addObject("limit", limit);
        mav.addObject("page", page);
        return mav;
    }

    @GetMapping("/chinh-sua-the-loai-san-pham")
    public ModelAndView editCategory(@RequestParam(value = "id") Integer id, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView("admin/views/EditCategory");
        CategoryDto categoryDto = categoryService.findOneById(id);
        if (categoryDto == null) {
            mav.setViewName("redirect:/404");
            redirectAttributes.addFlashAttribute("messageError", "Thể loại sản phẩm không tồn tại, vui lòng " +
                    "chọn thể loại sản phẩm khác");
            return mav;
        }
        mav.addObject("categoryDto", categoryDto);
        return mav;
    }

    @GetMapping("/them-mau-sac")
    public ModelAndView createColor() {
        ModelAndView mav = new ModelAndView("admin/views/CreateColor");
        return mav;
    }

    @GetMapping("/danh-sach-mau-san-pham")
    public ModelAndView listColor(@RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(value = "limit", defaultValue = "3") int limit) {
        ModelAndView mav = new ModelAndView("admin/views/ListColor");
        Pageable pageable = PageRequest.of(page -1, limit);
        List<ColorDto> colorDtos = colorService.findAllByStatus(pageable, SystemConstant.ACTIVE_STATUS);
        mav.addObject("colorDtos", colorDtos);
        mav.addObject("page", page);
        mav.addObject("limit", limit);
        int totalItem = colorService.getTotalItem(SystemConstant.ACTIVE_STATUS);
        mav.addObject("totalPage", (int) Math.ceil((double) totalItem / limit));

        return mav;
    }

    @GetMapping("/chinh-sua-mau-san-pham")
    public ModelAndView editColor(@RequestParam(value = "id") Integer id, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView("admin/views/EditColor");
        ColorDto colorDto = colorService.findOneById(id);
        if (colorDto == null) {
            mav.setViewName("redirect:/404");
            redirectAttributes.addFlashAttribute("messageError","Màu sản phẩm không tồn tại vui lòng chọn" +
                    "màu khác");
            return mav;
        }
        mav.addObject("colorDto", colorDto);
        return mav;
    }
    @PostMapping("/chinh-sua-mau-san-pham")
    public ModelAndView editColor(@ModelAttribute ColorForm colorForm, RedirectAttributes redirectAttributes){
        ModelAndView mav = new ModelAndView("admin/views/EditColor");
        Map<String, String> resultValidate = colorService.validate(colorForm, false);
        if (CollectionUtils.isEmpty(resultValidate)) {
            colorService.save(colorForm);
            redirectAttributes.addFlashAttribute("message", "Cập nhật thành công");
            mav.setViewName("redirect:/admin/chi-tiet-mau-san-pham?id=" + colorForm.getId());
        } else {
            mav.addObject("messageError", resultValidate);
            mav.addObject("colorDto", colorForm);
        }
        return mav;
    }

    @GetMapping("/chi-tiet-mau-san-pham")
    public ModelAndView detailColor(@RequestParam(value = "id") Integer id, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView("admin/views/DetailColor");
        ColorDto colorDto = colorService.findOneById(id);
        if (colorDto == null) {
            mav.setViewName("redirect:/404");
            redirectAttributes.addFlashAttribute("messageError", "Màu sản phẩm không tồn tại");
            return mav;
        }
        mav.addObject("colorDto", colorDto);
        return mav;
    }

    @GetMapping("/them-kich-thuoc")
    public ModelAndView createSize() {
        ModelAndView mav = new ModelAndView("admin/views/CreateSize");
        return mav;
    }

    @GetMapping("/danh-sach-kich-thuoc")
    public ModelAndView listSize(@RequestParam (value = "page", defaultValue = "1") Integer page,
                                 @RequestParam (value = "limit", defaultValue = "3") Integer limit) {
        ModelAndView mav = new ModelAndView("admin/views/ListSize");
        mav.addObject("page", page);
        mav.addObject("limit", limit);
        Pageable pageable = PageRequest.of(page-1, limit);
        List<SizeDto> sizeDtos = sizeService.findAllByStatus(pageable, SystemConstant.ACTIVE_STATUS);
        mav.addObject("sizeDtos", sizeDtos);
        int totalItem = sizeService.getTotalItem(SystemConstant.ACTIVE_STATUS);
        mav.addObject("totalPages",(int) Math.ceil((double) totalItem / limit));
        return mav;
    }

    @GetMapping("/chinh-sua-kich-thuoc")
    public ModelAndView editSize(@RequestParam(value = "id") Integer id, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView("/admin/views/EditSize");
        SizeDto sizeDto = sizeService.findOneById(id);
        if (sizeDto == null) {
            mav.setViewName("redirect:/404");
            redirectAttributes.addFlashAttribute("messageError", "Kích thước sản phẩm không tồn tại");
            return mav;
        }
        mav.addObject("sizeDto", sizeDto);
        return mav;
        }

    @PostMapping("/chinh-sua-kich-thuoc")
    public ModelAndView editSize(@ModelAttribute SizeForm sizeForm, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView("admin/views/EditSize");
        Map<String, String> validate = sizeService.validate(sizeForm, false);
        if (CollectionUtils.isEmpty(validate)) {
            redirectAttributes.addFlashAttribute("message", "Cập nhật thành công");
            sizeService.save(sizeForm);
            mav.setViewName("redirect:/admin/chi-tiet-kich-thuoc-san-pham?id=" + sizeForm.getId());
        } else {
            mav.addObject("messageError", validate);
            mav.addObject("sizeDto", sizeForm);
        }

        return mav;
    }

    @GetMapping("/chi-tiet-kich-thuoc-san-pham")
    public ModelAndView detailSize(@RequestParam(value = "id") Integer id, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView("admin/views/DetailSize");
        SizeDto sizeDto = sizeService.findOneById(id);
        if (sizeDto == null) {
            mav.setViewName("redirect:/404");
            redirectAttributes.addFlashAttribute("messageError", "Kích thước sản phẩm không tồn tại");
            return mav;
        }
        mav.addObject("sizeDto", sizeDto);
        return mav;
    }



    @GetMapping("/tao-don-hang")
    public ModelAndView createOder() {
        ModelAndView mav = new ModelAndView("admin/views/CreateOder");
        return mav;
    }

    @GetMapping("/chinh-sua-don-hang")
    public ModelAndView editOder() {
        ModelAndView mav = new ModelAndView("admin/views/EditOder");
        return mav;
    }

    @GetMapping("/danh-sach-don-hang")
    public ModelAndView listOders() {
        ModelAndView mav = new ModelAndView("admin/views/ListOders");
        return mav;
    }

    @GetMapping("/tao-tai-khoan-admin")
    public ModelAndView createAdminAccount() {
        ModelAndView mav = new ModelAndView("admin/views/CreateAdminAccount");
        mav.addObject("roles", roleService.findAll());
        return mav;
    }

    @GetMapping("/danh-sach-tai-khoan-admin")
    public ModelAndView listAdminAccount(@RequestParam(value = "page", defaultValue = "1") int page,
                                         @RequestParam(value = "limit", defaultValue = "5") int limit) {
        ModelAndView mav = new ModelAndView("admin/views/ListAdminAccount");
        Pageable pageable = PageRequest.of(page - 1, limit);
        List<UserDto> userDtos = userService.findAllByRoleCodeAndStatus(SystemConstant.ROLE_CODE_IMPLOY, pageable, SystemConstant.ACTIVE_STATUS);
        int totalItem = userService.getTotalItem(SystemConstant.ACTIVE_STATUS);
        mav.addObject("totalPage", (int) Math.ceil((double) totalItem / limit));
        mav.addObject("userDtos", userDtos);
        mav.addObject("page", page);
        mav.addObject("limit", limit);
        return mav;
    }

    @GetMapping("/chinh-sua-tai-khoan-admin")
    public ModelAndView editAdminAccount(@RequestParam(value = "id", required = false) Integer id) {
        ModelAndView mav = new ModelAndView("/admin/views/EditAdminAccount");
        UserDto userDto = userService.findOneById(id);
        List<String> roleCodes = roleService.getRoleCodesByUserDto(userDto);
        mav.addObject("roleUsers", roleCodes);
        mav.addObject("userDto", userDto);
        List<RoleDto> roles = roleService.findAll();
        mav.addObject("roles", roles);
        return mav;
    }

}
