package com.TestProject.JuiceStock.service;

import com.TestProject.JuiceStock.dto.JuiceDTO;
import com.TestProject.JuiceStock.entity.Juice;
import com.TestProject.JuiceStock.exception.JuiceAlreadyRegisteredException;
import com.TestProject.JuiceStock.exception.JuiceNotFoundException;
import com.TestProject.JuiceStock.exception.JuiceStockExceededException;
import com.TestProject.JuiceStock.mapper.JuiceMapper;
import com.TestProject.JuiceStock.repository.JuiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class JuiceService {

    @Autowired
    private final JuiceRepository juiceRepository;
    private final JuiceMapper juiceMapper = JuiceMapper.INSTANCE;

    public JuiceDTO createJuice(JuiceDTO juiceDTO) throws JuiceAlreadyRegisteredException {
        verifyIfIsAlreadyRegistered(juiceDTO.getName());
        Juice juice = juiceMapper.toModel(juiceDTO);
        Juice savedJuice = juiceRepository.save(juice);
        return juiceMapper.toDTO(savedJuice);
    }

    public JuiceDTO findByName(String name) throws JuiceNotFoundException {
        Juice foundJuice = juiceRepository.findByName(name)
                .orElseThrow(() -> new JuiceNotFoundException(name));
        return juiceMapper.toDTO(foundJuice);
    }

    public List<JuiceDTO> listAll() {
        return juiceRepository.findAll()
                .stream()
                .map(juiceMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws JuiceNotFoundException {
        verifyIfExists(id);
        juiceRepository.deleteById(id);
    }

    private void verifyIfIsAlreadyRegistered(String name) throws JuiceAlreadyRegisteredException {
        Optional<Juice> optSavedJuice = juiceRepository.findByName(name);
        if (optSavedJuice.isPresent()) {
            throw new JuiceAlreadyRegisteredException(name);
        }
    }

    private Juice verifyIfExists(Long id) throws JuiceNotFoundException {
        return juiceRepository.findById(id)
                .orElseThrow(() -> new JuiceNotFoundException(id));
    }

    public JuiceDTO increment(Long id, int quantityToIncrement) throws JuiceNotFoundException, JuiceStockExceededException {
        Juice juiceToIncrementStock = verifyIfExists(id);
        int quantityAfterIncrement = quantityToIncrement + juiceToIncrementStock.getQuantity();
        if (quantityAfterIncrement <= juiceToIncrementStock.getMax()) {
            juiceToIncrementStock.setQuantity(juiceToIncrementStock.getQuantity() + quantityToIncrement);
            Juice incrementedJuiceStock = juiceRepository.save(juiceToIncrementStock);
            return juiceMapper.toDTO(incrementedJuiceStock);
        }
        throw new JuiceStockExceededException(id, quantityToIncrement);
    }
}
