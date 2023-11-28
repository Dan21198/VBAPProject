package com.example.vbapproject.controller;

import com.example.vbapproject.model.Car;
import com.example.vbapproject.services.CarService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:3000")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class CarsController {
    private final CarService carService;

    @PostMapping("/cars")
    @PreAuthorize("hasRole('ADMIN')")
    public Car create(@Valid @RequestBody Car newCar) {
        return carService.create(newCar);
    }

    @GetMapping("/cars")
    public List<Car> getAll() {
        return carService.getAll();
    }

    @GetMapping("/cars/brand/{brand}")
    public List<Car> getByBrand(@PathVariable("brand") String brand) {
        return carService.getByBrand(brand);
    }

    @GetMapping("/getCarById/{id}")
    public Car get(@PathVariable("id") long id) {
        return carService.getById(id);
    }

    @GetMapping("/cars/notInOrder")
    public List<Car> getCarsNotInOrder() {
        return carService.getCarsNotInOrder();
    }

    @PutMapping("/cars/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void update(@PathVariable("id") long id, @Valid @RequestBody Car car) throws Exception {
        car.setId(id);
        carService.update(car);
    }

    @DeleteMapping("/cars/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable("id") long id) throws Exception {
        carService.delete(id);
    }
}
