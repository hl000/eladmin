package me.zhengjie.repository;

import me.zhengjie.domain.WorkDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author HL
 * @create 2022/9/2 15:01
 */
public interface WorkDeviceRepository extends JpaRepository<WorkDevice, Integer>, JpaSpecificationExecutor<WorkDevice> {
}
