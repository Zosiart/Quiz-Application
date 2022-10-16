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

import commons.GameUpdatesPacket;
import commons.MultiPlayerGame;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import server.multiplayer.WaitingRoom;
import java.util.ArrayList;
import java.util.Random;

/**
 * Configuration file
 * Beans get executed (?) when the program starts
 * Whatever the beans return are saved for later use :)
 */
@Configuration
public class Config {

    public static String defaultImagePath = "./server/src/main/resources/activity-bank-pictures/";
    public static int numberOfQuestions = 20;

    /**
     * packet that will be used
     * @return
     */
    @Bean
    public GameUpdatesPacket getGameUpdatesPacket(){
        return new GameUpdatesPacket( 0 ,"WAITINGROOM", -1);
    }

    /**
     * Returns a new random object
     * @return a new java Random object
     */
    @Bean
    public Random getRandom() {
        return new Random();
    }

    /**
     * Returns a new WaitingRoom object
     * @return new WaitingRoom
     */
    @Bean
    public WaitingRoom getWaitingRoom() {
        WaitingRoom waitingRoom = new WaitingRoom(new ArrayList<>(), new ArrayList<>(), numberOfQuestions);
        return waitingRoom;
    }
    /**
     * Returns a new multiplayer object
     * Default values are gameID 1, and empty arrays for now
     * @return new multiplayer object
     */
    @Bean
    public MultiPlayerGame getMultiplayerGame(){
        return new MultiPlayerGame(1, new ArrayList<>(),new ArrayList<>());
    }
}