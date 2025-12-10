package co2123.streetfood.controller;

import co2123.streetfood.model.Vendor;
import co2123.streetfood.repo.VendorRepository;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class VendorValidator implements Validator {

    private final VendorRepository vendorRepository;

    public VendorValidator(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Vendor.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Vendor vendor = (Vendor) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "", "Your vendor needs a name!");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "location", "", "Your vendor needs a location!");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cuisineType", "", "Your vendor needs a cuisine type!");

        if (vendor.getCuisineType() != null && vendor.getCuisineType().length() > 50) {
            errors.rejectValue("cuisineType", "", "Cuisine type must be 50 characters or fewer.");
        }

        if (vendor.getName() != null && !vendor.getName().trim().isEmpty()) {
            Vendor existingVendor = vendorRepository.findByName(vendor.getName());


            if (existingVendor != null) {
                if (vendor.getId() == 0 || existingVendor.getId() != vendor.getId()) {
                    errors.rejectValue("name", "", "Vendor name already exists.");
                }
            }
        }
    }
}
