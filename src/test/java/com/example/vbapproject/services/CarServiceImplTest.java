package com.example.vbapproject.services;

import com.example.vbapproject.exception.RecordNotFoundException;
import com.example.vbapproject.model.Car;
import com.example.vbapproject.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CarServiceImplTest {
    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCarById_WhenCarExists() {
        long carId = 1L;
        Car existingCar = new Car();
        existingCar.setId(carId);
        existingCar.setBrand("Toyota");
        existingCar.setModelOfCar("Camry");

        when(carRepository.findById(carId)).thenReturn(Optional.of(existingCar));

        Car fetchedCar = carService.getById(carId);

        assertNotNull(fetchedCar);
        assertEquals(existingCar.getBrand(), fetchedCar.getBrand());
        assertEquals(existingCar.getModelOfCar(), fetchedCar.getModelOfCar());
    }

    @Test
    void testGetCarById_WhenCarDoesNotExist() {
        long carId = 1L;

        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> carService.getById(carId));
    }

    @Test
    public void testGetCarsNotInOrder() {
        Car car1 = new Car();
        car1.setId(1L);
        car1.setName("Honda Civic");

        Car car2 = new Car();
        car2.setId(2L);
        car2.setName("Toyota Camry");

        List<Car> expectedCars = Arrays.asList(car1, car2);

        when(carRepository.findByOrderIdIsNull()).thenReturn(expectedCars);

        List<Car> result = carService.getCarsNotInOrder();

        assertEquals(expectedCars, result);
    }
}