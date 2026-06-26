package com.example.controller;

import com.example.dto.CompteRequest;
import com.example.dto.OperationRequest;
import com.example.dto.VirementRequest;
import com.example.model.Compte;
import com.example.service.CompteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/comptes")
public class CompteController {

    private final CompteService service;

    public CompteController(CompteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Compte> creer(@RequestBody CompteRequest requete) {
        Compte compte = service.creerCompte(requete.getNumero(), requete.getTitulaire());
        return ResponseEntity.status(HttpStatus.CREATED).body(compte);
    }

    @GetMapping("/{numero}")
    public Compte consulter(@PathVariable String numero) {
        return service.consulter(numero);
    }

    @GetMapping
    public List<Compte> lister() {
        return service.listerComptes();
    }

    @PostMapping("/{numero}/depot")
    public Compte deposer(@PathVariable String numero, @RequestBody OperationRequest requete) {
        return service.deposer(numero, requete.getMontant());
    }

    @PostMapping("/{numero}/retrait")
    public Compte retirer(@PathVariable String numero, @RequestBody OperationRequest requete) {
        return service.retirer(numero, requete.getMontant());
    }

    @PostMapping("/virement")
    public Compte virer(@RequestBody VirementRequest requete) {
        return service.virer(requete.getSource(), requete.getDestination(), requete.getMontant());
    }
}
