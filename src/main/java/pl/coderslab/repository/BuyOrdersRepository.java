package pl.coderslab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.BuyOrders;

import java.util.List;

@Repository
public interface BuyOrdersRepository extends JpaRepository<BuyOrders, Long> {
    List<BuyOrders> findByCompanyId(Long companyId);
    BuyOrders findFirstByUser_IdAndCompany_Id (Long userId, Long companyId);
}
