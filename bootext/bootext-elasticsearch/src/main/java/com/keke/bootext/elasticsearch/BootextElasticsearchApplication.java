package com.keke.bootext.elasticsearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

@SpringBootApplication
public class BootextElasticsearchApplication implements CommandLineRunner {

    @Autowired
    private ElasticsearchOperations es;

//    @Autowired
//    private BookService bookService;

    public static void main(String args[]) {
        SpringApplication.run(BootextElasticsearchApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }

    //useful for debug


}

