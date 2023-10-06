package com.example.intuitech_hazi.controller;
import com.example.intuitech_hazi.service.TheMuseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TheMuseController {

    private final TheMuseService theMuseService;

    @Autowired
    public TheMuseController(TheMuseService theMuseService) {
        this.theMuseService = theMuseService;
    }

    @GetMapping("/searchJobs")
    public ResponseEntity<String> searchJobs(@RequestParam String query) {
        return theMuseService.searchJobs(query);
    }
}
