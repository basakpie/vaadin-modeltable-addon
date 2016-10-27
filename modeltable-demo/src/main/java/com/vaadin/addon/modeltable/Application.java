package com.vaadin.addon.modeltable;

import com.vaadin.spring.annotation.EnableVaadin;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * Created by basakpie on 2016-10-14.
 */
@Configuration
@EnableVaadin
@SpringBootApplication
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Override
    public void run(String... strings) throws Exception {
        for(int i=0; i <= 3; i++) {
            Item item = new Item();
            item.setField01("[" + i+ "]_value01");
            item.setField02(2000000);
            item.setField03(3000000L);
            item.setField04(1.12f);
            item.setField05(2.34d);
            item.setField06(new Date());
            item.setField07(true);
            item.setField08("[" + i+ "]_value08");
            item.setField10("[" + i+ "]_value10");
            item.setField13("[" + i+ "]_value13");
            item.setField15("[" + i+ "]_value15");
            item.setField16("[" + i+ "]_value16");
            DemoUI.itemFieldMap.put(i, item);
        }
    }

}
