package co2123.streetfood.controller;

import co2123.streetfood.StreetfoodApplication;
import co2123.streetfood.model.*;
import co2123.streetfood.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class DeleteController {


    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private AwardRepository awardRepository;

    @RequestMapping("/deleteVendor")
    public String deleteVendor(@RequestParam("id") Integer id) {
        Optional<Vendor> vendorOptional = vendorRepository.findById(id);
        if(vendorOptional.isPresent()){
            vendorRepository.delete(vendorOptional.get());
        }
        return "redirect:/admin";
    }
    @RequestMapping("/deleteDish")
    public String deleteDish(@RequestParam Integer vendorid, @RequestParam Integer dishid) {
        Optional<Vendor> vendorOptional = vendorRepository.findById(vendorid);
        if (vendorOptional.isEmpty()) {
            return "redirect:/admin";
        }

        Vendor foundVendor = vendorOptional.get();

        Dish foundDish = null;
        for (Dish d : foundVendor.getDishes()) {
            if (d.getId() == dishid) {
                foundDish = d;
                break;
            }
        }

        if (foundDish == null) {
            return "redirect:/admin";
        }

        foundVendor.getDishes().remove(foundDish);
        vendorRepository.save(foundVendor);
        return "redirect:/vendor?id=" + vendorid;
    }


    @RequestMapping("/deleteReview")
    public String deleteReview(@RequestParam Integer vendorId, @RequestParam Integer reviewId) {
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        if(reviewOptional.isEmpty()){
            return "redirect:/admin";
        }

        Review foundReview = reviewOptional.get();

        Optional<Vendor> vendorOptional = vendorRepository.findById(vendorId);
        if(vendorOptional.isEmpty()){
            return "redirect:/admin";
        }

        Vendor foundVendor = vendorOptional.get();

        // Find and remove review from dish
        for(Dish d : foundVendor.getDishes()){
            if(d.getReviews().contains(foundReview)){
                d.getReviews().remove(foundReview);
                vendorRepository.save(foundVendor);  // Save to update
                break;
            }
        }

        // Delete review from repository
        reviewRepository.delete(foundReview);

        return "redirect:/vendor?id=" + vendorId;
    }


    @RequestMapping("/deletePhoto")
    public String deletePhoto(@RequestParam Integer photoId) {
        Optional<Photo> photoOptional = photoRepository.findById(photoId);
        if(photoOptional.isEmpty()){
            return "redirect:/admin";
        }

        Photo foundPhoto = photoOptional.get();
        Vendor vendor = foundPhoto.getVendor();

        if(vendor == null){
            return "redirect:/admin";
        }

        vendor.getPhotos().remove(foundPhoto);
        vendorRepository.save(vendor);
        photoRepository.delete(foundPhoto);

        return "redirect:/vendor?id=" + vendor.getId();
    }


    @RequestMapping("/deleteAward")
    public String deleteAward(@RequestParam Integer awardId) {
        Optional<Award> awardOptional = awardRepository.findById(awardId);
        if(awardOptional.isEmpty()){
            return "redirect:/admin";
        }

        Award foundAward = awardOptional.get();
        Vendor vendor = foundAward.getVendor();

        if(vendor == null){
            return "redirect:/admin";
        }

        vendor.getAwards().remove(foundAward);
        vendorRepository.save(vendor);
        awardRepository.delete(foundAward);

        return "redirect:/vendor?id=" + vendor.getId();
    }

}
