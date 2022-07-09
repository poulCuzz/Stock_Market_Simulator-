package pl.coderslab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.BuyOrders;
import pl.coderslab.SalesOrders;

import java.util.List;

@Repository
public interface SalesOrdersRepository extends JpaRepository<SalesOrders, Long> {
    List<SalesOrders> findByCompanyId(Long companyId);
    SalesOrders findFirstByUser_IdAndCompany_Id(Long userId, Long companyId);
}
