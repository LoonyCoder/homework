package dao;

import com.lagou.RunBoot;
import com.lagou.entity.COrder;
import com.lagou.repository.COrderRepository;
import org.apache.shardingsphere.core.strategy.keygen.SnowflakeShardingKeyGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RunBoot.class)
public class TestSharding {
    @Resource
    private COrderRepository corderRepository;


    @Test
    @Repeat(10)
    public void testShardingBOrder(){
        Random random = new Random();
        int i = random.nextInt(100000);
        COrder order = new COrder();
        order.setDel(false);
        order.setCompanyId(888);
        order.setPositionId(12345);
        order.setUserId(i);
        order.setPublishUserId(123);
        order.setResumeType(1);
        order.setStatus("AUTO");
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());

        corderRepository.save(order);
    }

    @Test

    public void testRead(){

        COrder order = corderRepository.findByUId(22412);
        System.out.println("===============================");
        System.out.println(order.toString());
        System.out.println("===============================");

        COrder order2 = corderRepository.findByUId(22412);
        System.out.println("===============================");
        System.out.println(order2.toString());
        System.out.println("===============================");


    }
}
