package co2123.streetfood.controller;

import co2123.streetfood.StreetfoodApplication;
import co2123.streetfood.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import co2123.streetfood.repo.VendorRepository;
import co2123.streetfood.repo.AwardRepository;
import co2123.streetfood.repo.PhotoRepository;
import co2123.streetfood.model.Tag;
import co2123.streetfood.repo.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class AddEditController {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private AwardRepository awardRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private TagRepository tagRepository;

    @RequestMapping("editVendor")
    public String editVendorForm(@RequestParam Integer id, Model model) {
        Optional<Vendor> vendorOptional = vendorRepository.findById(id);
        if (vendorOptional.isEmpty()) {
            return "redirect:/admin";
        }
        model.addAttribute("vendor", vendorOptional.get());
        return "forms/editVendor";
    }


    @RequestMapping("editedVendor")
    public String submittedEditForm(@RequestParam Integer id, @ModelAttribute Vendor vendor, Model model) {
        Optional<Vendor> vendorOptional = vendorRepository.findById(id);
        if (vendorOptional.isEmpty()) {
            return "redirect:/admin";
        }

        Vendor foundVendor = vendorOptional.get();
        foundVendor.setName(vendor.getName());
        foundVendor.setLocation(vendor.getLocation());
        foundVendor.setCuisineType(vendor.getCuisineType());
        vendorRepository.save(foundVendor);

        return "redirect:/vendor?id=" + id;
    }


    @RequestMapping("editVendorProfile")
    public String editVendorProfileForm(@RequestParam Integer id, Model model) {
        Optional<Vendor> vendorOptional = vendorRepository.findById(id);
        if (vendorOptional.isEmpty()) {
            return "redirect:/admin";
        }

        Vendor foundVendor = vendorOptional.get();
        VendorProfile profile = foundVendor.getProfile();
        model.addAttribute("profile", profile);
        model.addAttribute("vendor", id);
        return "forms/editVendorProfile";
    }


    @RequestMapping("editedVendorProfile")
    public String submittedProfileEditForm(@RequestParam Integer id, @ModelAttribute VendorProfile profile, Model model) {
        Optional<Vendor> vendorOptional = vendorRepository.findById(id);
        if (vendorOptional.isEmpty()) {
            return "redirect:/admin";
        }

        Vendor foundVendor = vendorOptional.get();
        VendorProfile existingProfile = foundVendor.getProfile();

        if (existingProfile == null) {
            foundVendor.setProfile(profile);
        } else {

            existingProfile.setBio(profile.getBio());
            existingProfile.setSocialMediaHandle(profile.getSocialMediaHandle());
            existingProfile.setWebsite(profile.getWebsite());
        }

        vendorRepository.save(foundVendor);

        return "redirect:/vendor?id=" + id;
    }



    @RequestMapping("newDish")
    public String newDishForm(@RequestParam Integer id, Model model) {
        Optional<Vendor> vendorOptional = vendorRepository.findById(id);
        if (vendorOptional.isEmpty()) {
            return "redirect:/admin";
        }
        model.addAttribute("vendor", vendorOptional.get());
        model.addAttribute("dish", new Dish());
        model.addAttribute("tags", tagRepository.findAll());  // Get tags from DB
        return "forms/newDish";
    }


    @RequestMapping("addDish")
    public String addDish(@RequestParam Integer vendorid, @RequestParam List<Integer> tagIds, @ModelAttribute Dish dish, Model model) {
        Optional<Vendor> vendorOptional = vendorRepository.findById(vendorid);
        if (vendorOptional.isEmpty()) {
            return "redirect:/admin";
        }

        Vendor foundVendor = vendorOptional.get();
        dish.setVendor(foundVendor);

        if (tagIds != null && !tagIds.isEmpty()) {
            List<Tag> selectedTags = (List<Tag>) tagRepository.findAllById(tagIds);
            dish.setTags(new ArrayList<>(selectedTags));
        }

        foundVendor.getDishes().add(dish);
        vendorRepository.save(foundVendor);

        return "redirect:/vendor?id=" + vendorid;
    }



    @RequestMapping("newReview")
    public String newReview(@RequestParam Integer vendorid, @RequestParam Integer dishid, Model model) {
        Optional<Vendor> vendorOptional = vendorRepository.findById(vendorid);
        if (vendorOptional.isEmpty()) {
            return "redirect:/admin";
        }
        Vendor foundVendor = vendorOptional.get();
        model.addAttribute("vendor", foundVendor);
        model.addAttribute("dishid", dishid);
        model.addAttribute("review", new Review());
        return "forms/newReview";
    }

    @RequestMapping("addReview")
    public String addReview(@RequestParam Integer vendorid, @RequestParam Integer dishid, @ModelAttribute Review review, Model model) {
        Optional<Vendor> vendorOptional = vendorRepository.findById(vendorid);
        if (vendorOptional.isEmpty()) {
            return "redirect:/admin";
        }

        Vendor foundVendor = vendorOptional.get();

        Dish foundDish = null;
        for (Dish dish : foundVendor.getDishes()) {
            if (dish.getId() == dishid) {
                foundDish = dish;
                break;
            }
        }

        if (foundDish == null) {
            return "redirect:/admin";
        }

        review.setReviewDate(LocalDateTime.now());
        review.setDish(foundDish);

        foundDish.getReviews().add(review);

        vendorRepository.save(foundVendor);

        return "redirect:/vendor?id=" + vendorid;
    }

    @RequestMapping("newPhoto")
    public String newPhoto(@RequestParam Integer vendorid, Model model) {
        Optional<Vendor> vendorOptional = vendorRepository.findById(vendorid);
        if (vendorOptional.isEmpty()) {
            return "redirect:/admin";
        }
        model.addAttribute("vendor", vendorOptional.get());
        model.addAttribute("photo", new Photo());
        return "forms/newPhoto";
    }

    @RequestMapping("addPhoto")
    public String addPhoto(@RequestParam Integer vendorid, @ModelAttribute Photo photo, Model model) {
        Optional<Vendor> vendorOptional = vendorRepository.findById(vendorid);
        if (vendorOptional.isEmpty()) {
            return "redirect:/admin";
        }

        Vendor foundVendor = vendorOptional.get();
        photo.setVendor(foundVendor);

        photoRepository.save(photo);

        foundVendor.getPhotos().add(photo);
        vendorRepository.save(foundVendor);

        return "redirect:/vendor?id=" + vendorid;
    }

    @RequestMapping("newAward")
    public String newAward(@RequestParam Integer vendorid, Model model) {
        Optional<Vendor> vendorOptional = vendorRepository.findById(vendorid);
        if (vendorOptional.isEmpty()) {
            return "redirect:/admin";
        }
        model.addAttribute("vendor", vendorOptional.get());
        model.addAttribute("award", new Award());
        return "forms/newAward";
    }

    @RequestMapping("addAward")
    public String addAward(@RequestParam Integer vendorid, @ModelAttribute Award award, Model model) {
        Optional<Vendor> vendorOptional = vendorRepository.findById(vendorid);
        if (vendorOptional.isEmpty()) {
            return "redirect:/admin";
        }

        Vendor foundVendor = vendorOptional.get();
        award.setVendor(foundVendor);

        awardRepository.save(award);

        foundVendor.getAwards().add(award);
        vendorRepository.save(foundVendor);
        return "redirect:/vendor?id=" + vendorid;
    }


    @RequestMapping("editDish")
    public String editDishForm(@RequestParam Integer vendorid, @RequestParam Integer dishid, Model model) {
        Optional<Vendor> vendorOptional = vendorRepository.findById(vendorid);
        if (vendorOptional.isEmpty()) {
            return "redirect:/admin";
        }

        Vendor foundVendor = vendorOptional.get();
        Dish foundDish = null;
        for (Dish dish : foundVendor.getDishes()) {
            if (dish.getId() == dishid) {
                foundDish = dish;
                break;
            }
        }

        if (foundDish == null) {
            return "redirect:/admin";
        }
        model.addAttribute("vendor", foundVendor);
        model.addAttribute("dish", foundDish);
        model.addAttribute("tags", tagRepository.findAll());  // Get all tags from DB
        return "forms/editDish";
    }
    @RequestMapping("editedDish")
    public String submittedEditDishForm(@RequestParam Integer vendorid, @RequestParam Integer dishid, @RequestParam(required = false) List<Integer> tagIds, @ModelAttribute Dish dish, Model model) {
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

        foundDish.setName(dish.getName());
        foundDish.setPrice(dish.getPrice());
        foundDish.setDescription(dish.getDescription());
        foundDish.setSpiceLevel(dish.getSpiceLevel());

        if (tagIds != null && !tagIds.isEmpty()) {
            List<Tag> selectedTags = (List<Tag>) tagRepository.findAllById(tagIds);
            foundDish.setTags(new ArrayList<>(selectedTags));
        } else {
            foundDish.setTags(new ArrayList<>());
        }

        vendorRepository.save(foundVendor);
        return "redirect:/vendor?id=" + vendorid;
    }

    @RequestMapping("editReview")
    public String editReview(@RequestParam Integer vendorId, @RequestParam Integer reviewId, Model model) {
        Optional<Vendor> vendorOptional = vendorRepository.findById(vendorId);
        if (vendorOptional.isEmpty()) {
            return "redirect:/admin";
        }

        Vendor foundVendor = vendorOptional.get();
        Review foundReview = null;
        for (Dish dish : foundVendor.getDishes()) {
            for (Review r : dish.getReviews()) {
                if (r.getId() == reviewId) {
                    foundReview = r;
                    break;
                }
            }
            if (foundReview != null) break;
        }

        if (foundReview == null) {
            return "redirect:/admin";
        }

        model.addAttribute("vendor", foundVendor);
        model.addAttribute("review", foundReview);
        return "forms/editReview";
    }

    @RequestMapping("editedReview")
    public String editedReview(@RequestParam Integer vendorId, @RequestParam Integer reviewId, @ModelAttribute Review review, Model model) {
        Optional<Vendor> vendorOptional = vendorRepository.findById(vendorId);
        if (vendorOptional.isEmpty()) {
            return "redirect:/admin";
        }

        Vendor foundVendor = vendorOptional.get();
        Review foundReview = null;
        Dish parentDish = null;
        for (Dish dish : foundVendor.getDishes()) {
            for (Review r : dish.getReviews()) {
                if (r.getId() == reviewId) {
                    foundReview = r;
                    parentDish = dish;
                    break;
                }
            }
            if (foundReview != null) break;
        }

        if (foundReview == null || parentDish == null) {
            return "redirect:/admin";
        }

        foundReview.setReviewerName(review.getReviewerName());
        foundReview.setComment(review.getComment());
        foundReview.setRating(review.getRating());

        vendorRepository.save(foundVendor);
        return "redirect:/vendor?id=" + vendorId;
    }
    @RequestMapping("editPhoto")
    public String editPhoto(@RequestParam Integer photoId, Model model) {
        Optional<Photo> photoOptional = photoRepository.findById(photoId);

        if (photoOptional.isEmpty()) {
            return "redirect:/admin";
        }

        model.addAttribute("photo", photoOptional.get());
        return "forms/editPhoto";
    }


    @RequestMapping("editedPhoto")
    public String editedPhoto(@RequestParam Integer photoId, @ModelAttribute Photo photo, Model model) {
        Optional<Photo> photoOptional = photoRepository.findById(photoId);

        if (photoOptional.isEmpty()) {
            return "redirect:/admin";
        }

        Photo foundPhoto = photoOptional.get();
        foundPhoto.setDescription(photo.getDescription());
        foundPhoto.setUrl(photo.getUrl());

        photoRepository.save(foundPhoto);

        Vendor vendor = foundPhoto.getVendor();
        if (vendor == null) {
            return "redirect:/admin";
        }

        return "redirect:/vendor?id=" + vendor.getId();
    }

    @RequestMapping("editAward")
    public String editAward(@RequestParam Integer awardId, Model model) {
        Optional<Award> awardOptional = awardRepository.findById(awardId);
        if (awardOptional.isEmpty()) {
            return "redirect:/admin";
        }
        model.addAttribute("award", awardOptional.get());
        return "forms/editAward";
    }

    @RequestMapping("editedAward")
    public String editedAward(@RequestParam Integer awardId, @ModelAttribute Award award, Model model) {
        Optional<Award> awardOptional = awardRepository.findById(awardId);
        if (awardOptional.isEmpty()) {
            return "redirect:/admin";
        }

        Award foundAward = awardOptional.get();
        foundAward.setTitle(award.getTitle());
        foundAward.setYear(award.getYear());


        awardRepository.save(foundAward);

        Vendor foundVendor = foundAward.getVendor();
        return "redirect:/vendor?id=" + foundVendor.getId();
    }

}
