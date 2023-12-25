package me.zhengjie.repository;

import me.zhengjie.domain.WorkTicket;
import me.zhengjie.domain.WorkTicketManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author HL
 * @create 2021/4/13 16:15
 */
public interface WorkTicketManagerRepository extends JpaRepository<WorkTicketManager, Integer>, JpaSpecificationExecutor<WorkTicketManager> {
}
