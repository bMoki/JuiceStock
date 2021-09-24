package com.TestProject.JuiceStock.service;

import com.TestProject.JuiceStock.builder.JuiceDTOBuilder;
import com.TestProject.JuiceStock.dto.JuiceDTO;
import com.TestProject.JuiceStock.entity.Juice;
import com.TestProject.JuiceStock.exception.JuiceAlreadyRegisteredException;
import com.TestProject.JuiceStock.exception.JuiceNotFoundException;
import com.TestProject.JuiceStock.exception.JuiceStockExceededException;
import com.TestProject.JuiceStock.mapper.JuiceMapper;
import com.TestProject.JuiceStock.repository.JuiceRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JuiceServiceTest {

    private static final long INVALID_JUICE_ID = 1L;

    @Mock
    private JuiceRepository juiceRepository;

    private JuiceMapper juiceMapper = JuiceMapper.INSTANCE;

    @InjectMocks
    private JuiceService juiceService;

    @Test
    void whenJuiceInformedThenItShouldBeCreated() throws JuiceAlreadyRegisteredException {
        // given
        JuiceDTO expectedJuiceDTO = JuiceDTOBuilder.builder().build().toJuiceDTO();
        Juice expectedSavedJuice = juiceMapper.toModel(expectedJuiceDTO);

        // when
        when(juiceRepository.findByName(expectedJuiceDTO.getName())).thenReturn(Optional.empty());
        when(juiceRepository.save(expectedSavedJuice)).thenReturn(expectedSavedJuice);

        //then
        JuiceDTO createdJuiceDTO = juiceService.createJuice(expectedJuiceDTO);

        assertThat(createdJuiceDTO.getId(), is(equalTo(expectedJuiceDTO.getId())));
        assertThat(createdJuiceDTO.getName(), is(equalTo(expectedJuiceDTO.getName())));
        assertThat(createdJuiceDTO.getQuantity(), is(equalTo(expectedJuiceDTO.getQuantity())));
    }

    @Test
    void whenAlreadyRegisteredJuiceInformedThenAnExceptionShouldBeThrown() {
        // given
        JuiceDTO expectedJuiceDTO = JuiceDTOBuilder.builder().build().toJuiceDTO();
        Juice duplicatedJuice = juiceMapper.toModel(expectedJuiceDTO);

        // when
        when(juiceRepository.findByName(expectedJuiceDTO.getName())).thenReturn(Optional.of(duplicatedJuice));

        // then
        assertThrows(JuiceAlreadyRegisteredException.class, () -> juiceService.createJuice(expectedJuiceDTO));
    }

    @Test
    void whenValidJuiceNameIsGivenThenReturnAJuice() throws JuiceNotFoundException {
        // given
        JuiceDTO expectedFoundJuiceDTO = JuiceDTOBuilder.builder().build().toJuiceDTO();
        Juice expectedFoundJuice = juiceMapper.toModel(expectedFoundJuiceDTO);

        // when
        when(juiceRepository.findByName(expectedFoundJuice.getName())).thenReturn(Optional.of(expectedFoundJuice));

        // then
        JuiceDTO foundJuiceDTO = juiceService.findByName(expectedFoundJuiceDTO.getName());

        assertThat(foundJuiceDTO, is(equalTo(expectedFoundJuiceDTO)));
    }

    @Test
    void whenNotRegisteredJuiceNameIsGivenThenThrowAnException() {
        // given
        JuiceDTO expectedFoundJuiceDTO = JuiceDTOBuilder.builder().build().toJuiceDTO();

        // when
        when(juiceRepository.findByName(expectedFoundJuiceDTO.getName())).thenReturn(Optional.empty());

        // then
        assertThrows(JuiceNotFoundException.class, () -> juiceService.findByName(expectedFoundJuiceDTO.getName()));
    }

    @Test
    void whenListJuiceIsCalledThenReturnAListOfJuices() {
        // given
        JuiceDTO expectedFoundJuiceDTO = JuiceDTOBuilder.builder().build().toJuiceDTO();
        Juice expectedFoundJuice = juiceMapper.toModel(expectedFoundJuiceDTO);

        //when
        when(juiceRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundJuice));

        //then
        List<JuiceDTO> foundListJuicesDTO = juiceService.listAll();

        assertThat(foundListJuicesDTO, is(not(empty())));
        assertThat(foundListJuicesDTO.get(0), is(equalTo(expectedFoundJuiceDTO)));
    }

    @Test
    void whenListJuiceIsCalledThenReturnAnEmptyListOfJuices() {
        //when
        when(juiceRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        //then
        List<JuiceDTO> foundListJuicesDTO = juiceService.listAll();

        assertThat(foundListJuicesDTO, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenAJuiceShouldBeDeleted() throws JuiceNotFoundException{
        // given
        JuiceDTO expectedDeletedJuiceDTO = JuiceDTOBuilder.builder().build().toJuiceDTO();
        Juice expectedDeletedJuice = juiceMapper.toModel(expectedDeletedJuiceDTO);

        // when
        when(juiceRepository.findById(expectedDeletedJuiceDTO.getId())).thenReturn(Optional.of(expectedDeletedJuice));
        doNothing().when(juiceRepository).deleteById(expectedDeletedJuiceDTO.getId());

        // then
        juiceService.deleteById(expectedDeletedJuiceDTO.getId());

        verify(juiceRepository, times(1)).findById(expectedDeletedJuiceDTO.getId());
        verify(juiceRepository, times(1)).deleteById(expectedDeletedJuiceDTO.getId());
    }

    @Test
    void whenIncrementIsCalledThenIncrementJuiceStock() throws JuiceNotFoundException, JuiceStockExceededException {
        //given
        JuiceDTO expectedJuiceDTO = JuiceDTOBuilder.builder().build().toJuiceDTO();
        Juice expectedJuice = juiceMapper.toModel(expectedJuiceDTO);

        //when
        when(juiceRepository.findById(expectedJuiceDTO.getId())).thenReturn(Optional.of(expectedJuice));
        when(juiceRepository.save(expectedJuice)).thenReturn(expectedJuice);

        int quantityToIncrement = 10;
        int expectedQuantityAfterIncrement = expectedJuiceDTO.getQuantity() + quantityToIncrement;

        // then
        JuiceDTO incrementedJuiceDTO = juiceService.increment(expectedJuiceDTO.getId(), quantityToIncrement);

        assertThat(expectedQuantityAfterIncrement, equalTo(incrementedJuiceDTO.getQuantity()));
        assertThat(expectedQuantityAfterIncrement, lessThan(expectedJuiceDTO.getMax()));
    }

    @Test
    void whenIncrementIsGreatherThanMaxThenThrowException() {
        JuiceDTO expectedJuiceDTO = JuiceDTOBuilder.builder().build().toJuiceDTO();
        Juice expectedJuice = juiceMapper.toModel(expectedJuiceDTO);

        when(juiceRepository.findById(expectedJuiceDTO.getId())).thenReturn(Optional.of(expectedJuice));

        int quantityToIncrement = 80;
        assertThrows(JuiceStockExceededException.class, () -> juiceService.increment(expectedJuiceDTO.getId(), quantityToIncrement));
    }

    @Test
    void whenIncrementAfterSumIsGreatherThanMaxThenThrowException() {
        JuiceDTO expectedJuiceDTO = JuiceDTOBuilder.builder().build().toJuiceDTO();
        Juice expectedJuice = juiceMapper.toModel(expectedJuiceDTO);

        when(juiceRepository.findById(expectedJuiceDTO.getId())).thenReturn(Optional.of(expectedJuice));

        int quantityToIncrement = 45;
        assertThrows(JuiceStockExceededException.class, () -> juiceService.increment(expectedJuiceDTO.getId(), quantityToIncrement));
    }

    @Test
    void whenIncrementIsCalledWithInvalidIdThenThrowException() {
        int quantityToIncrement = 10;

        when(juiceRepository.findById(INVALID_JUICE_ID)).thenReturn(Optional.empty());

        assertThrows(JuiceNotFoundException.class, () -> juiceService.increment(INVALID_JUICE_ID, quantityToIncrement));
    }

}
