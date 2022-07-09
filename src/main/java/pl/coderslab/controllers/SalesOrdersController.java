package pl.coderslab.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.BuyOrders;
import pl.coderslab.SalesOrders;
import pl.coderslab.SharesHeld;
import pl.coderslab.User;
import pl.coderslab.repository.CompaniesRepository;
import pl.coderslab.repository.SalesOrdersRepository;
import pl.coderslab.repository.SharesHeldRepository;
import pl.coderslab.repository.UserRepository;
import pl.coderslab.services.MarketService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/sellorder")
public class SalesOrdersController {

    private final SalesOrdersRepository salesOrdersRepository;
    private final CompaniesRepository companiesRepository;
    private final UserRepository userRepository;
    private final SharesHeldRepository sharesHeldRepository;
    private final MarketService marketService;

    public SalesOrdersController(SalesOrdersRepository salesOrdersRepository, CompaniesRepository companiesRepository, UserRepository userRepository, SharesHeldRepository sharesHeldRepository, MarketService marketService) {
        this.salesOrdersRepository = salesOrdersRepository;
        this.companiesRepository = companiesRepository;
        this.userRepository = userRepository;
        this.sharesHeldRepository = sharesHeldRepository;
        this.marketService = marketService;
    }

    //dodawanie do SalesOrders i usuwanie z SharesHeld

    @RequestMapping("/add/{userId}/{companyId}")
    public String addSaleOrder(@PathVariable Long userId,
                               @PathVariable Long companyId, Model model){
        SharesHeld sharesHeld = sharesHeldRepository.findFirstByUserIdAndCompanyId(userId, companyId);
        BuyOrders buyOrder = new BuyOrders();
        List<BuyOrders> list = marketService.getBuyOrders();
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getCompany().equals(sharesHeld.getCompany())) {
                buyOrder = list.get(i);
            }
        }

        if(companiesRepository.findById(companyId).get().equals(buyOrder.getCompany())) {
            BuyOrders buyOrders = new BuyOrders();
            buyOrders.setCompany(companiesRepository.findById(companyId).get());
            buyOrders.setUser(buyOrder.getUser());
            buyOrders.setPriceLimit(buyOrder.getPriceLimit());
            buyOrders.setVolumen(buyOrder.getVolumen());
            model.addAttribute("buyOrders", buyOrders);
            return "redirect:/sell/" + buyOrder.getUser().getId() + "/" + buyOrder.getCompany().getId() + "/" + buyOrder.getVolumen() + "/" + buyOrder.getPriceLimit();
        }

        SalesOrders salesOrders = new SalesOrders();
        salesOrders.setCompany(companiesRepository.findById(companyId).get());
        salesOrders.setUser(userRepository.findById(userId).get());
        model.addAttribute("salesOrder", salesOrders);
        return "salesOrder/add";
    }

    @RequestMapping(value= ("/add/{userId}/{companyId}"), method = RequestMethod.POST)
    public String addSaleOrder(@Valid SalesOrders salesOrders, BindingResult result, @RequestParam int volumen, Model model){
        if(result.hasErrors()) {
            model.addAttribute("salesOrder", salesOrders);
            System.out.println("!!masz jakiś błąd !!!");
            return "salesOrder/add";
        }
        Long userId = salesOrders.getUser().getId();
        Long companyId = salesOrders.getCompany().getId();
        User user = userRepository.findById(userId).get();
        SharesHeld sharesHeld = sharesHeldRepository.findFirstByUserIdAndCompanyId(userId, companyId);
        System.out.println("************************************************************************");
        System.out.println(volumen);
        System.out.println(sharesHeld.toString());
        if (volumen > sharesHeld.getVolume()) {
            System.out.println("!!!ustawiłeś za duży volumen, nie masz tylu akcji!!!");
            model.addAttribute("salesOrder", salesOrders);
            return "salesOrder/add";
        }else if(volumen == sharesHeld.getVolume()) {
            sharesHeldRepository.delete(sharesHeld);
            salesOrdersRepository.save(salesOrders);
            return "redirect:/held/list?userId=" + userId;
        }else if(volumen < sharesHeld.getVolume()){
            sharesHeld.setVolume(sharesHeld.getVolume()-volumen);
            sharesHeld.setValueAll(companiesRepository.findById(companyId).get().getPricePerStock()*(sharesHeld.getVolume()-volumen));
            sharesHeldRepository.save(sharesHeld);
            salesOrdersRepository.save(salesOrders);
            return "redirect:/market";
        }else {
            System.out.println("!!!!Coś poszło nie tak!!!!");
            return "redirect:/held/list?userId=" + userId;
        }

    }
}

