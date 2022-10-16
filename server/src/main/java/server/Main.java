/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.Scanner;

/**
 * Main for server application
 */
@SpringBootApplication
@EntityScan(basePackages = { "commons", "server" })
public class Main {

    /**
     * Runs when application is started
     * @param args
     */
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner userInput = new Scanner(System.in);
                while(true) {
                    String input = userInput.nextLine();

                    switch(input){
                        case "q":
                        case "quit":
                            System.exit(0);
                            break;
                    }
                }
            }
        }).start();
        SpringApplication.run(Main.class, args);
    }


    //used for uploading activities from JSON file to the
    //database
    //IF UNCOMMENTED, THEN ALL ACTIVITIES FROM THE FILE
    //WILL BE UPLOADED TO CURRENT DATABASE
    //ONLY USE IF USING THE IN-MEMORY TESTING DB
    //DON'T USE IF THE CURRENT DB IS NOT IN-MEMORY
    /*
    @Bean
    CommandLineRunner runner(ActivityRepository activityRepo) {
        return args -> {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<List<Activity>> mapType = new TypeReference<>() {
            };
            InputStream inputStream = TypeReference
                    .class
                    .getClassLoader()
                    .getResourceAsStream(
                            "json/activities.json"
            );

            try {
                List<Activity> activityList = mapper.readValue(inputStream, mapType);
                activityRepo.saveAll(activityList);
                System.out.println("Activity list saved successfully");
            }
            catch(IOException e) {
                System.out.println(e.getMessage());
            }
        };
    }
    */

}