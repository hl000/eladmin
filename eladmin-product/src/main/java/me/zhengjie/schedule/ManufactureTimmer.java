package me.zhengjie.schedule;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.service.ManufactureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author HL
 * @create 2021/5/19 17:52
 */
@Slf4j
@Component
public class ManufactureTimmer {
    @Autowired
    private ManufactureService manufactureService;


    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Scheduled(cron = "${scheduled.autoManufacture-timer}")
    public void createDailyPlanByTimer() {
        String today = dateFormat.format(new Date());
        manufactureService.createManufacture(today);
    }
}
