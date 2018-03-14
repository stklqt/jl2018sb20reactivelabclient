package de.virtual7.reactivelabclient.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mihai.dobrescu
 */
@RestController
@RequestMapping("/client")
public class ClientEventController {

    Logger logger = LoggerFactory.getLogger(ClientEventController.class);


    @GetMapping("/events")
    public String getTrackingEvents() {

        return "OK";
    }

    @GetMapping("/groupedEvents")
    public String getTrackingEventsGrouped() {

        return "OK"; //TODO: return something meaningful :)
    }


}
