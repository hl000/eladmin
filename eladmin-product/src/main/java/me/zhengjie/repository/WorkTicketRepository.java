package me.zhengjie.repository;

import me.zhengjie.domain.WorkTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author HL
 * @create 2021/4/13 16:15
 */
public interface WorkTicketRepository extends JpaRepository<WorkTicket, Long>, JpaSpecificationExecutor<WorkTicket> {
}
