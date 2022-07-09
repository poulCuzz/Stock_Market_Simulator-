package pl.coderslab.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.coderslab.SalesOrders;
import pl.coderslab.SharesHeld;
import pl.coderslab.repository.SalesOrdersRepository;
import pl.coderslab.repository.SharesHeldRepository;

@Controller
public class TestController {

    private final SharesHeldRepository sharesHeldRepository;
    private final SalesOrdersRepository salesOrdersRepository;

    public TestController(SharesHeldRepository sharesHeldRepository, SalesOrdersRepository salesOrdersRepository) {
        this.sharesHeldRepository = sharesHeldRepository;
        this.salesOrdersRepository = salesOrdersRepository;
    }

    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        SharesHeld sharesHeld = sharesHeldRepository.findById(6L).get();
        sharesHeld.setValueAll(100);

        System.out.println("******************************************************");
        System.out.println(sharesHeld.getValueAll());


        return "OK";
    }

    @RequestMapping("/test2")
    @ResponseBody
    public String test2(){


        return "OK";
    }
}
