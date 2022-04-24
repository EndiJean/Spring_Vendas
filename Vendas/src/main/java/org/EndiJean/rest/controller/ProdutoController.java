package org.EndiJean.rest.controller;

import org.EndiJean.entity.Produto;
import org.EndiJean.repository.ProdutoRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private ProdutoRepository produtoRepository;

    public ProdutoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @GetMapping("{id}")
    public Produto getProdutoById(@PathVariable Integer id){
        return produtoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não Encontrado"));
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Produto save(@RequestBody @Valid Produto produto){
        return produtoRepository.save(produto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        produtoRepository.findById(id).map(produto -> {
            produtoRepository.delete(produto);
        return produtoRepository;
        }).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não Encontrado"));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id,@RequestBody @Valid Produto produto ){
        produtoRepository.findById(id).map(p -> {
                    produto.setId(p.getId());
                    produtoRepository.save(produto);
                    return p;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado") );
    }

    @GetMapping()
    public List<Produto> find(Produto filtro){
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING );

        Example example = Example.of(filtro, matcher);
        return produtoRepository.findAll(example);
    }

}
