package com.example.vbapproject.services;

import com.example.vbapproject.model.Car;
import com.example.vbapproject.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CarServiceImplTest {
    private CarRepository carRepository;
    private CarService carService;

    @BeforeEach
    public void setUp() {
        carRepository = mock(CarRepository.class);
        carService = new CarServiceImpl(carRepository);
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