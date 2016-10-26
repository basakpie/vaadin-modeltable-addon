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
            ItemField itemField = new ItemField();
            itemField.setField01("[" + i+ "]_value01");
            itemField.setField02(2000000);
            itemField.setField03(3000000L);
            itemField.setField04(1.12f);
            itemField.setField05(2.34d);
            itemField.setField06(new Date());
            itemField.setField07("[" + i+ "]_value07");
            itemField.setField08("[" + i+ "]_value08");
            itemField.setField10("[" + i+ "]_value10");
            itemField.setField13("[" + i+ "]_value13");
            itemField.setField15("[" + i+ "]_value15");
            itemField.setField16("[" + i+ "]_value16");
            DemoUI.itemFieldMap.put(i, itemField);
        }
    }

}
