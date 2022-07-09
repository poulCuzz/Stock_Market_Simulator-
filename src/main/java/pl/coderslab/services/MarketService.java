package pl.coderslab.services;

import org.springframework.stereotype.Service;
import pl.coderslab.BuyOrders;
import pl.coderslab.SalesOrders;
import pl.coderslab.repository.BuyOrdersRepository;
import pl.coderslab.repository.SalesOrdersRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarketService {

    private final BuyOrdersRepository buyOrdersRepository;
    private final SalesOrdersRepository salesOrdersRepository;

    public MarketService(BuyOrdersRepository buyOrdersRepository, SalesOrdersRepository salesOrdersRepository) {
        this.buyOrdersRepository = buyOrdersRepository;
        this.salesOrdersRepository = salesOrdersRepository;
    }

    public List<BuyOrders> getBuyOrders() {
        List<BuyOrders> allBuyOrders = buyOrdersRepository.findAll();
        List<Long> allCompaniesIds = new ArrayList<>();
        for (BuyOrders b: allBuyOrders) {
            allCompaniesIds.add(b.getCompany().getId());
        }
        List<Long> sortedList = new ArrayList<>();
        sortedList = allCompaniesIds.stream()
                .sorted()
                .distinct()
                .collect(Collectors.toList());
        //  sortedList.forEach(System.out::println);
        List<BuyOrders> allBestBuyOrders = new ArrayList<>();
        for (int i = 0; i < sortedList.size(); i++) {
            List<BuyOrders> list = buyOrdersRepository.findByCompanyId(sortedList.get(i));
            if(!list.isEmpty()){
                List<BuyOrders> resultList = list.stream()
                        .sorted(Comparator.comparingDouble(BuyOrders::getPriceLimit).reversed())
                        .collect(Collectors.toList());
                BuyOrders result = resultList.get(0);
                // System.out.println(result);
                allBestBuyOrders.add(result);
            }else {
                System.out.println("nie ma takich zleceń");
            }
        }
        return allBestBuyOrders;
    }


    public List<SalesOrders> getSalesOrders() {
        List<SalesOrders> allSalesOrders = salesOrdersRepository.findAll();
        List<Long> allCompaniesIds2 = new ArrayList<>();
        for (SalesOrders b: allSalesOrders) {
            allCompaniesIds2.add(b.getCompany().getId());
        }
        List<Long> sortedList2 = new ArrayList<>();
        sortedList2 = allCompaniesIds2.stream()
                .sorted()
                .distinct()
                .collect(Collectors.toList());
        //  sortedList2.forEach(System.out::println);
        List<SalesOrders> allBestSalesOrders = new ArrayList<>();
        for (int i = 0; i < sortedList2.size(); i++) {
            List<SalesOrders> list2 = salesOrdersRepository.findByCompanyId(sortedList2.get(i));
            if(!list2.isEmpty()){
                List<SalesOrders> resultList2 = list2.stream()
                        .sorted(Comparator.comparingDouble(SalesOrders::getPriceLimit))
                        .collect(Collectors.toList());
                SalesOrders result2 = resultList2.get(0);
                // System.out.println(result2);
                allBestSalesOrders.add(result2);
            }else {
                System.out.println("nie ma takich zleceń");
            }
        }
        return allBestSalesOrders;
    }
}
