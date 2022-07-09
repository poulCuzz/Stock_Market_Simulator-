package pl.coderslab.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.coderslab.Companies;
import pl.coderslab.repository.CompaniesRepository;

import javax.validation.Valid;

@Controller
@RequestMapping("/company")
public class CompaniesController {

    private final CompaniesRepository companiesRepository;

    public CompaniesController(CompaniesRepository companiesRepository) {
        this.companiesRepository = companiesRepository;
    }

    //***************************ADD**************************************

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addCompany(Model model) {
        model.addAttribute("company", new Companies());
        return "company/add2";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String save(@Valid Companies company, BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("company", company);
            return "company/add2";
        }
        companiesRepository.save(company);
        return "redirect:/company/list";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("companies", companiesRepository.findAll());
        return "company/list2";
    }

    //*************************DEL***************************************

    @RequestMapping(value = "/del/{id}", method = RequestMethod.GET)
    public String delCompany(@PathVariable String id) {
        Long parsedId = Long.parseLong(id);
        companiesRepository.deleteById(parsedId);
        return "redirect:/company/list";
    }

    //************************EDIT***************************************
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String EditCompany(@PathVariable String id, Model model) {
        Long parsedId = Long.parseLong(id);
        model.addAttribute("company", companiesRepository.findById(parsedId));
        return "company/add2";
    }


    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String EditCompany(@Valid Companies company,BindingResult result,  Model model) {
        if(result.hasErrors()) {
            model.addAttribute("company", company);
            return "company/add2";
        }
        companiesRepository.save(company);
        return "redirect:/company/list";
    }

}
