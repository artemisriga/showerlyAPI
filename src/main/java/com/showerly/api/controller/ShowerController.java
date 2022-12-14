package com.showerly.api.controller;

import com.showerly.api.model.Shower;
import com.showerly.api.model.presentable.PresentableShower;
import com.showerly.api.repository.ShowerRepository;
import com.showerly.api.service.PresentableService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Api(tags = "1")
@RestController
@RequestMapping("/shower")
public class ShowerController {
    @Autowired
    private ShowerRepository showerRepository;

    @Autowired
    private PresentableService presentableService;

    @GetMapping("/token")
    public ResponseEntity<String> requestToken(@RequestParam String showerId) {
        Optional<Shower> optionalShower = this.showerRepository.findById(showerId);
        if (optionalShower.isPresent()) {
            Shower shower = this.showerRepository.getById(showerId);
            return ResponseEntity.status(200).body(shower.getToken());
        }

        Shower shower = new Shower();
        shower.setId(showerId);
        shower.setToken(UUID.randomUUID().toString());
        this.showerRepository.save(shower);

        return ResponseEntity.status(200).body(shower.getToken());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PresentableShower> getDataOfShower(@PathVariable String id) {
        Optional<Shower> optionalShower = this.showerRepository.findById(id);
        if (optionalShower.isEmpty()) {
            return ResponseEntity.status(400).build();
        }

        PresentableShower presentableShower = this.presentableService.getPresentableShower(id);
        return ResponseEntity.ok(presentableShower);
    }
}
