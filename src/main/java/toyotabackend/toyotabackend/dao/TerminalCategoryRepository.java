package toyotabackend.toyotabackend.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toyotabackend.toyotabackend.domain.Terminal.TerminalCategory;


@Repository
public interface TerminalCategoryRepository extends JpaRepository<TerminalCategory,Integer> {
    Page<TerminalCategory> findAllByname(String filterCategory, Pageable paging);
}
