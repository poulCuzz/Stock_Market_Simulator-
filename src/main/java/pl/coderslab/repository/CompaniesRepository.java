package pl.coderslab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.Companies;

@Repository
public interface CompaniesRepository extends JpaRepository<Companies, Long> {
}
