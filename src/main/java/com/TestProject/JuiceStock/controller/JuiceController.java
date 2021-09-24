package com.TestProject.JuiceStock.controller;

import com.TestProject.JuiceStock.dto.JuiceDTO;
import com.TestProject.JuiceStock.dto.QuantityDTO;
import com.TestProject.JuiceStock.exception.JuiceAlreadyRegisteredException;
import com.TestProject.JuiceStock.exception.JuiceNotFoundException;
import com.TestProject.JuiceStock.exception.JuiceStockExceededException;
import com.TestProject.JuiceStock.service.JuiceService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/juices")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class JuiceController implements JuiceControllerDocs {

    @Autowired
    private final JuiceService juiceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JuiceDTO createJuice(@RequestBody @Valid JuiceDTO juiceDTO) throws JuiceAlreadyRegisteredException {
        return juiceService.createJuice(juiceDTO);
    }

    @GetMapping("/{name}")
    public JuiceDTO findByName(@PathVariable String name) throws JuiceNotFoundException {
        return juiceService.findByName(name);
    }

    @GetMapping
    public List<JuiceDTO> listJuices() {
        return juiceService.listAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws JuiceNotFoundException {
        juiceService.deleteById(id);
    }

    @PatchMapping("/{id}/increment")
    public JuiceDTO increment(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) throws JuiceNotFoundException, JuiceStockExceededException {
        return juiceService.increment(id, quantityDTO.getQuantity());
    }
}
