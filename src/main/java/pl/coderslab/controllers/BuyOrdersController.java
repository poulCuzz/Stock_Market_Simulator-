package pl.coderslab.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.coderslab.BuyOrders;
import pl.coderslab.repository.BuyOrdersRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/buyorder")
public class BuyOrdersController {

    private final BuyOrdersRepository buyOrdersRepository;

    public BuyOrdersController(BuyOrdersRepository buyOrdersRepository) {
        this.buyOrdersRepository = buyOrdersRepository;
    }

    @RequestMapping("/find")
    @ResponseBody
    public BuyOrders findBestByCompanyId() {
        List<BuyOrders> list = buyOrdersRepository.findByCompanyId(1L);
        if(!list.isEmpty()){
            List<BuyOrders> result = list.stream()
                    .sorted(Comparator.comparingDouble(BuyOrders::getPriceLimit).reversed())
                    .collect(Collectors.toList());
            result.forEach(System.out::println);
            return result.get(0);
        }
        System.out.println("nie ma takich zleceń");
        return null;

    }

    @RequestMapping("/add")
    @ResponseBody
    public String add() {
        BuyOrders buyOrders = new BuyOrders();
//        buyOrders.setCompany();
//        buyOrders.setPriceLimit();
//        buyOrders.setUser();
//        buyOrders.setVolumen();
        buyOrdersRepository.save(buyOrders);
        return "buyOrder added";
    }
}

