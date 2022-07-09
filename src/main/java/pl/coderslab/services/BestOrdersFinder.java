package pl.coderslab.services;

import lombok.Getter;
import lombok.Setter;
import pl.coderslab.BuyOrders;
import pl.coderslab.SalesOrders;
import pl.coderslab.repository.BuyOrdersRepository;
import pl.coderslab.repository.SalesOrdersRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class BestOrdersFinder {


    static BuyOrdersRepository buyOrdersRepository;
    static SalesOrdersRepository salesOrdersRepository;


    public BuyOrders findBestBuyOrderByCompanyId(Long companyId) {

        List<BuyOrders> list = buyOrdersRepository.findByCompanyId(companyId);
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


    public void findAllCompaniesId(){
        salesOrdersRepository.findAll().forEach(System.out::println);
        List<BuyOrders> allBuyOrders = buyOrdersRepository.findAll();
        List<Long> allCompaniesIds = new ArrayList<>();

        for (BuyOrders b: allBuyOrders) {
            allCompaniesIds.add(b.getCompany().getId());
        }
        allCompaniesIds.forEach(System.out::println);
        List<Long> sortedList = new ArrayList<>();
        sortedList = allCompaniesIds.stream().sorted().distinct().collect(Collectors.toList());
        sortedList.forEach(System.out::println);
    }

    public static void main(String[] args) {
        BestOrdersFinder bestOrdersFinder = new BestOrdersFinder();
        bestOrdersFinder.findAllCompaniesId();
    }


    public SalesOrders findBestSalesOrderByCompanyId(Long companyId) {
        List<SalesOrders> list = salesOrdersRepository.findByCompanyId(companyId);
        if (!list.isEmpty()) {
            List<SalesOrders> result= list.stream()
                    .sorted(Comparator.comparingDouble(SalesOrders::getPriceLimit))
                    .collect(Collectors.toList());
            System.out.println(result.get(0).toString());
            return result.get(0);
        }
        System.out.println("nie ma takich zleceń");
        return null;
    }



}
