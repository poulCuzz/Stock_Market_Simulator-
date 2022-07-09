package pl.coderslab.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.*;
import pl.coderslab.services.MarketService;
import pl.coderslab.repository.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class MarketController {

    private final BuyOrdersRepository buyOrdersRepository;
    private final SalesOrdersRepository salesOrdersRepository;
    private final CompaniesRepository companiesRepository;
    private final UserRepository userRepository;
    private final SharesHeldRepository sharesHeldRepository;
    private final MarketService marketService;


    public MarketController(BuyOrdersRepository buyOrdersRepository, SalesOrdersRepository salesOrdersRepository, CompaniesRepository companiesRepository, UserRepository userRepository, SharesHeldRepository sharesHeldRepository, MarketService marketService) {
        this.buyOrdersRepository = buyOrdersRepository;
        this.salesOrdersRepository = salesOrdersRepository;
        this.companiesRepository = companiesRepository;
        this.userRepository = userRepository;
        this.sharesHeldRepository = sharesHeldRepository;
        this.marketService = marketService;
    }

    @RequestMapping("/market")
    public String showMarket(Model model){
       // *********************BestBuyOrders************************************************

        List<BuyOrders> allBestBuyOrders = marketService.getBuyOrders();
        // allBestBuyOrders.forEach(System.out::println);

        // *********************BestSalesOrders************************************************

        List<SalesOrders> allBestSalesOrders = marketService.getSalesOrders();

        model.addAttribute("buyOrders", allBestBuyOrders);
        model.addAttribute("salesOrders", allBestSalesOrders);
        return "/market/market";
    }




    @RequestMapping("/sell/{userId}/{companyId}/{volumen}/{priceLimit}")
    public String sellFromMarket(@PathVariable Long userId,
                                @PathVariable Long companyId,
                                @PathVariable int volumen,
                                @PathVariable double priceLimit, Model model){
        Companies company = companiesRepository.findById(companyId).get();
        System.out.println("*********************************************************************************************");
        System.out.println(company.getName());
        BuyOrders buyOrder = buyOrdersRepository.findFirstByUser_IdAndCompany_Id(userId, companyId);
        System.out.println(buyOrder.getId());
        buyOrder.setCompany(company);
        buyOrder.setVolumen(volumen);
        buyOrder.setPriceLimit(priceLimit);
        model.addAttribute("buyOrders", buyOrder);
        return "market/sell";
    }



    @PostMapping("/sell/{userId}/{companyName}/{volumen}/{priceLimit}")
    public String sellFromMarket(@Valid BuyOrders buyOrders, BindingResult result, @RequestParam String yourId, @RequestParam int volumen, Model model){
        if(result.hasErrors()) {
            model.addAttribute("buyOrders", buyOrders);
            System.out.println("!!masz jakiś błąd !!!");
            return "market/sell";
        }
        System.out.println(buyOrders.toString());
        Long userId = buyOrders.getUser().getId();
        Long usersCompanyId = buyOrders.getCompany().getId();
        BuyOrders usersBuyOrder = buyOrdersRepository.findFirstByUser_IdAndCompany_Id(userId, usersCompanyId);
        Companies company = companiesRepository.findById(usersCompanyId).get();
        LocalDateTime localDateTime = LocalDateTime.now();
        String dateAndTime = localDateTime.toString().replaceAll("\\..*", "").replaceFirst("T", "  ");
        double usersPriceLimit = usersBuyOrder.getPriceLimit();
        Long yourIdLong = Long.parseLong(yourId);
        if(yourIdLong == userId) {
            System.out.println("!!!nie możesz dokonywać tranzakcji z samym sobą!!!");
            model.addAttribute("buyOrders", buyOrders);
            return "market/sell";
        }
        if(sharesHeldRepository.findFirstByUserIdAndCompanyId(yourIdLong, usersCompanyId) == null) {
            System.out.println("!!!nie posiadasz tej spółki w portfelu!!!");
            return "redirect:/market";
        }else if(volumen > usersBuyOrder.getVolumen()){
            model.addAttribute("buyOrders", buyOrders);
            System.out.println("!!!nie ma tyle akcji do kupienia!! Wybierz inną liczbę");
            return "market/sell";
        }else if(volumen > sharesHeldRepository.findFirstByUserIdAndCompanyId(yourIdLong, usersCompanyId).getVolume()){
            model.addAttribute("buyOrders", buyOrders);
            System.out.println("!!!nie masz tylu akcji w portfelu!!!");
            return "market/sell";
        }
        if (buyOrders.getPriceLimit() < usersPriceLimit) {
            System.out.println("nie opłaca ci sie sprzedawać po niższej cenie!!");
            model.addAttribute("buyOrders", buyOrders);
            return "market/sell";
        }else if(buyOrders.getPriceLimit() == usersPriceLimit && volumen == usersBuyOrder.getVolumen()) {
            buyOrdersRepository.delete(usersBuyOrder);
            userRepository.findById(userId).get().setMoneyUsd(userRepository.findById(userId).get().getMoneyUsd() - usersPriceLimit * volumen);
            userRepository.findById(yourIdLong).get().setMoneyUsd(userRepository.findById(yourIdLong).get().getMoneyUsd() + usersPriceLimit * volumen);
            SharesHeld sharesHeld = new SharesHeld();
            sharesHeld.setVolume(buyOrders.getVolumen());
            sharesHeld.setUser(userRepository.findById(userId).get());
            sharesHeld.setCompany(buyOrders.getCompany());
            sharesHeld.setDateAndTime(dateAndTime);
            sharesHeld.setPurchasePrice(usersPriceLimit);
            sharesHeld.setValueAll(company.getPricePerStock() * buyOrders.getVolumen());
            sharesHeld.setPurchasePriceAll();
            sharesHeld.setProfitOrLoss();
            sharesHeldRepository.save(sharesHeld);

            SharesHeld yourSharesHeld = sharesHeldRepository.findFirstByUserIdAndCompanyId(yourIdLong, usersCompanyId);
            if(yourSharesHeld.getVolume() == volumen) {
                sharesHeldRepository.delete(yourSharesHeld);
                return "redirect:/held/list?userId=" + yourId;
            }else{
                yourSharesHeld.setVolume(yourSharesHeld.getVolume() - volumen);
                yourSharesHeld.setValueAll(yourSharesHeld.getValueAll() - buyOrders.getCompany().getPricePerStock()* yourSharesHeld.getVolume());
                sharesHeldRepository.save(yourSharesHeld);
                return "redirect:/held/list?userId=" + yourId;
            }

        }else if (buyOrders.getPriceLimit() == usersPriceLimit && volumen < usersBuyOrder.getVolumen()) {
            usersBuyOrder.setVolumen(usersBuyOrder.getVolumen() - volumen);
            userRepository.findById(userId).get().setMoneyUsd(userRepository.findById(userId).get().getMoneyUsd() - usersPriceLimit * volumen);
            userRepository.findById(yourIdLong).get().setMoneyUsd(userRepository.findById(yourIdLong).get().getMoneyUsd() + usersPriceLimit * volumen);
            SharesHeld sharesHeld = new SharesHeld();
            sharesHeld.setVolume(buyOrders.getVolumen());
            sharesHeld.setUser(userRepository.findById(userId).get());
            sharesHeld.setCompany(buyOrders.getCompany());
            sharesHeld.setDateAndTime(dateAndTime);
            sharesHeld.setPurchasePrice(usersPriceLimit);
            sharesHeld.setValueAll(company.getPricePerStock() * volumen);
            sharesHeld.setPurchasePriceAll();
            sharesHeld.setProfitOrLoss();
            sharesHeldRepository.save(sharesHeld);

            SharesHeld yourSharesHeld = sharesHeldRepository.findFirstByUserIdAndCompanyId(yourIdLong, usersCompanyId);
            if(yourSharesHeld.getVolume() == volumen) {
                sharesHeldRepository.delete(yourSharesHeld);
                return "redirect:/held/list?userId=" + yourId;
            }else{
                yourSharesHeld.setVolume(yourSharesHeld.getVolume() - volumen);
                yourSharesHeld.setValueAll(yourSharesHeld.getValueAll() - buyOrders.getCompany().getPricePerStock()* yourSharesHeld.getVolume());
                sharesHeldRepository.save(yourSharesHeld);
                return "redirect:/held/list?userId=" + yourId;
            }


        }else if(buyOrders.getPriceLimit() > usersPriceLimit) {
            User user = userRepository.findById(yourIdLong).get();
            user.setMoneyUsd(user.getMoneyUsd() - companiesRepository.findById(usersCompanyId).get().getPricePerStock()*volumen);
            SharesHeld sharesHeld = sharesHeldRepository.findFirstByUserIdAndCompanyId(yourIdLong, usersCompanyId);
            if(usersBuyOrder.getVolumen() == buyOrders.getVolumen()) {
                sharesHeldRepository.delete(sharesHeld);
                return "redirect:/market";
            }
            sharesHeld.setVolume(sharesHeld.getVolume() - volumen);
            sharesHeld.setValueAll(sharesHeld.getValueAll() - sharesHeld.getCompany().getPricePerStock()* volumen);
            sharesHeldRepository.save(sharesHeld);
            SalesOrders salesOrders = new SalesOrders();
            salesOrders.setUser(userRepository.findById(yourIdLong).get());
            salesOrders.setCompany(buyOrders.getCompany());
            salesOrders.setVolumen(buyOrders.getVolumen());
            salesOrders.setPriceLimit(buyOrders.getPriceLimit());
            salesOrdersRepository.save(salesOrders);
            return "redirect:/market";
        }
        return "redirect:/market";
    }




    @RequestMapping("/buy/{userId}/{companyId}/{volumen}/{priceLimit}")
    public String buyFromMarket(@PathVariable Long userId,
                                @PathVariable Long companyId,
                                @PathVariable int volumen,
                                @PathVariable double priceLimit, Model model){
        Companies company = companiesRepository.findById(companyId).get();
        System.out.println("*********************************************************************************************");
        System.out.println(company.getName());
        SalesOrders salesOrder = salesOrdersRepository.findFirstByUser_IdAndCompany_Id(userId, companyId);
        salesOrder.setCompany(company);
        salesOrder.setVolumen(volumen);
        salesOrder.setPriceLimit(priceLimit);
        model.addAttribute("salesOrders", salesOrder);

        return "market/buy";
    }

    @PostMapping("/buy/{userId}/{companyName}/{volumen}/{priceLimit}")
    public String buyFromMarket(@ModelAttribute("salesOrders") @Valid SalesOrders salesOrder, BindingResult result, @RequestParam String yourId, @RequestParam int volumen, Model model){
        if(result.hasErrors()) {
            model.addAttribute("salesOrders", salesOrder);
            System.out.println("!!masz jakiś błąd !!!");
            return "market/buy";
        }
        Long userId = salesOrder.getUser().getId();
        Long yourIdLong = Long.parseLong(yourId);
        User user = userRepository.findById(userId).get();
        User your = userRepository.findById(yourIdLong).get();
        Long usersCompanyId = salesOrder.getCompany().getId();
        Companies company = companiesRepository.findById(usersCompanyId).get();
        SalesOrders usersSalesOrder = salesOrdersRepository.findFirstByUser_IdAndCompany_Id(userId, usersCompanyId);
        LocalDateTime localDateTime = LocalDateTime.now();
        String dateAndTime = localDateTime.toString().replaceAll("\\..*", "").replaceFirst("T", "  ");
        double usersPriceLimit = usersSalesOrder.getPriceLimit();
        if(yourIdLong == userId) {
            System.out.println("!!!nie możesz dokonywać tranzakcji z samym sobą!!!");
            model.addAttribute("salesOrders", salesOrder);
            return "market/buy";
        }
        if(userRepository.findById(yourIdLong).get().getMoneyUsd() < salesOrder.getPriceLimit()*volumen) {
            System.out.println("!!!masz za mało pieniędzy!!!");
            model.addAttribute("salesOrders", salesOrder);
            return "market/buy";
        }else if(volumen > usersSalesOrder.getVolumen()) {
            System.out.println("!!!ni możesz kupić więcej akcji niż wystawia sprzedający!!!");
            model.addAttribute("salesOrders", salesOrder);
            return "market/buy";
        }else if(salesOrder.getPriceLimit() > usersPriceLimit){
            System.out.println("!!!nie opłaca ci się ustalać wyższej ceny!!!");
            model.addAttribute("salesOrders", salesOrder);
            return "market/buy";
        }
        if(salesOrder.getPriceLimit() == usersPriceLimit && volumen == usersSalesOrder.getVolumen()) {
            user.setMoneyUsd(user.getMoneyUsd() + salesOrder.getPriceLimit()*volumen);
            your.setMoneyUsd(your.getMoneyUsd() - salesOrder.getPriceLimit()*volumen);
            salesOrdersRepository.delete(salesOrdersRepository.findFirstByUser_IdAndCompany_Id(userId, usersCompanyId));
            SharesHeld sharesHeld = new SharesHeld();
            sharesHeld.setVolume(volumen);
            sharesHeld.setCompany(companiesRepository.findById(usersCompanyId).get());
            sharesHeld.setUser(your);
            sharesHeld.setDateAndTime(dateAndTime);
            sharesHeld.setPurchasePrice(usersPriceLimit);
            sharesHeld.setValueAll(company.getPricePerStock() * salesOrder.getVolumen());
            sharesHeld.setPurchasePriceAll();
            sharesHeld.setProfitOrLoss();
            sharesHeldRepository.save(sharesHeld);
            return "redirect:/held/list?userId=" + yourId;
        }else if(salesOrder.getPriceLimit() == usersPriceLimit && volumen < usersSalesOrder.getVolumen()) {
            user.setMoneyUsd(user.getMoneyUsd() + salesOrder.getPriceLimit()*volumen);
            your.setMoneyUsd(your.getMoneyUsd() - salesOrder.getPriceLimit()*volumen);
            SalesOrders salesOrder2 = salesOrdersRepository.findFirstByUser_IdAndCompany_Id(userId, usersCompanyId);
            salesOrder2.setVolumen(salesOrder2.getVolumen() - volumen);
            salesOrdersRepository.save(salesOrder2);
            SharesHeld sharesHeld = new SharesHeld();
            sharesHeld.setVolume(volumen);
            sharesHeld.setCompany(companiesRepository.findById(usersCompanyId).get());
            sharesHeld.setUser(your);
            sharesHeld.setDateAndTime(dateAndTime);
            sharesHeld.setPurchasePrice(usersPriceLimit);
            sharesHeld.setValueAll(company.getPricePerStock() * volumen);
            sharesHeld.setPurchasePriceAll();
            sharesHeld.setProfitOrLoss();
            sharesHeldRepository.save(sharesHeld);
            return "redirect:/held/list?userId=" + yourId;
        }else if(salesOrder.getPriceLimit() < usersPriceLimit) {
            BuyOrders buyOrders = new BuyOrders();
            buyOrders.setUser(your);
            buyOrders.setVolumen(volumen);
            buyOrders.setCompany(companiesRepository.findById(usersCompanyId).get());
            buyOrders.setPriceLimit(salesOrder.getPriceLimit());
            buyOrdersRepository.save(buyOrders);
            return "redirect:/market";
        }
        return "redirect:/market";
    }

}
