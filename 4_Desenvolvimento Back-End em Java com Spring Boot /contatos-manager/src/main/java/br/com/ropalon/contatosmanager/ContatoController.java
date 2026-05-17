package br.com.ropalon.contatosmanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ContatoController {
    @Autowired
    private ContatoRepository repository;

    @GetMapping("/contato")
    public List<Contato> todosContatos(){
        return repository.findAll();
    }

    @PostMapping("/contato")
    public Contato salvar(@RequestBody Contato contato){
        return repository.save(contato);
    }
}
