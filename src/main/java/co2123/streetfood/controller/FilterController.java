package co2123.streetfood.controller;

import co2123.streetfood.StreetfoodApplication;
import co2123.streetfood.repo.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import co2123.streetfood.model.Dish;
import co2123.streetfood.model.Vendor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FilterController {

    @Autowired
    private VendorRepository vendorRepository;

    @GetMapping("/search1")
    public String search1(@RequestParam String vendor, Model model) {
        List<Vendor> list;

        if (vendor != null && !vendor.trim().isEmpty()) {
            list = vendorRepository.findByNameContainingIgnoreCase(vendor);
        } else {
            list = (List<Vendor>) vendorRepository.findAll();
        }

        if(list.isEmpty()){
            list = (List<Vendor>) vendorRepository.findAll();
        }

        model.addAttribute("vendors", list);
        return "vendors";
    }


    @GetMapping("/search2")
    public String search2(@RequestParam String dish, Model model) {
        List<Vendor> list;

        if (dish != null && !dish.trim().isEmpty()) {
            list = vendorRepository.findByDishesNameContainingIgnoreCase(dish);
        } else {
            list = (List<Vendor>) vendorRepository.findAll();
        }

        if(list.isEmpty()){
            list = (List<Vendor>) vendorRepository.findAll();
        }

        model.addAttribute("vendors", list);
        return "vendors";
    }
}

