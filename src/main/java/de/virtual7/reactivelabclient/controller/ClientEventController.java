package de.virtual7.reactivelabclient.controller;

import de.virtual7.reactivelabclient.event.TrackingEvent;
import de.virtual7.reactivelabclient.service.TrackingEventClientService;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mihai.dobrescu
 */
@RestController
@RequestMapping("/client")
public class ClientEventController {

    Logger logger = LoggerFactory.getLogger(ClientEventController.class);

    @Autowired
    private TrackingEventClientService trackingEventClientService;

    @GetMapping("/events")
    public String getTrackingEvents() {
        WebClient webClient = WebClient.create("http://localhost:8080");

        Flux<TrackingEvent> flux = webClient.get()
                .uri("/events/latest")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(TrackingEvent.class);

        flux
                .onBackpressureDrop(item -> logger.info("Dropping {}", item))
                .subscribe(new BaseSubscriber<TrackingEvent>() {

                    int count = 0;
                    List<TrackingEvent> buffer = new LinkedList<TrackingEvent>();

                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        logger.info("Subscribing with 10");
                        request(10);
                    }

                    @Override
                    protected void hookOnNext(TrackingEvent value) {
                        count++;
                        buffer.add(value);
                        logger.info(value.toString());
                        if (count == 10) {
                        //    trackingEventClientService.saveBulkEvents(buffer);
                            buffer.clear();
                            request(10);
                            count = 0;
                        }
                    }

                });

        return "OK";
    }

    @GetMapping("/groupedEvents")
    public String getTrackingEventsGrouped() {
        WebClient webClient = WebClient.create("http://localhost:8080");

        Flux<TrackingEvent> flux = webClient.get()
                .uri("/events/top")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(TrackingEvent.class);


        flux.groupBy(TrackingEvent::getEventID)
                .map(group -> group.publishOn(Schedulers.newParallel("newParallel1", 4)))
                .subscribe(event -> {
                    processGroupFlux(event);
                });

        return "OK"; //TODO: return something meaningful :)
    }

    private void processGroupFlux(Flux<TrackingEvent> eventFlux) {

        eventFlux.collectList()
                .subscribe(event -> {
                    logger.info("Event list size is {} on thread {} ", event.size(), Thread.currentThread().getName());
                });

    }

}