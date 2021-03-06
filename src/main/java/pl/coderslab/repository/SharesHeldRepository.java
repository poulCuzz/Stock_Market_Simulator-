package pl.coderslab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.SharesHeld;

import java.util.List;

@Repository
public interface SharesHeldRepository extends JpaRepository<SharesHeld, Long> {

    List<SharesHeld> findAllByUserId(Long userId);
  //  SharesHeld findFirstByUser_IdAndAndDateAndTime (Long userId, String dateAndTime);
    SharesHeld findFirstByUserIdAndCompanyId (Long userId, Long companyId);
    List<SharesHeld> findAllByCompany_Id (Long companyId);
}
