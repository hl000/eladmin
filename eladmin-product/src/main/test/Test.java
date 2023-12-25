import me.zhengjie.domain.WorkTicket;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author HL
 * @create 2021/9/23 11:54
 */
public class Test {


    @org.junit.Test
    public void timeTest() {
         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//        System.out.println(System.currentTimeMillis());
//
//        System.out.println(new Date());
//
//        System.out.println(new Date(System.currentTimeMillis()));

        List<WorkTicket> workTicketList = new ArrayList<>();

        String number = String.format("%03d", workTicketList.size()+1);
        System.out.println(simpleDateFormat.format(new Date()) + number);

        System.out.println("111".compareTo("21"));
    }
}
