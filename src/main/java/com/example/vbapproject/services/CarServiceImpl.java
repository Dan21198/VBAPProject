package com.example.vbapproject.services;

import com.example.vbapproject.exception.RecordNotFoundException;
import com.example.vbapproject.model.Car;
import com.example.vbapproject.model.Order;
import com.example.vbapproject.repository.CarRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CarServiceImpl implements CarService{
    private final CarRepository carRepository;

    @Override
    public Car create(Car newCar) {
        newCar.setName(newCar.getBrand() + " " + newCar.getModelOfCar());
        Car ret = carRepository.save(newCar);
        return ret;
    }

    @Override
    public Car getById(long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Car not found."));
        return car;
    }

    @Override
    public void update(Car car) throws RecordNotFoundException {
        Optional<Car> optionalExistingCar = carRepository.findById(car.getId());
        if (optionalExistingCar.isEmpty()) {
            throw new RecordNotFoundException("Car not found.");
        }
        Car existingCar = optionalExistingCar.get();
        Order existingOrder = existingCar.getOrder();

        existingCar.setName(car.getBrand() + " " + car.getModelOfCar());
        existingCar.setBrand(car.getBrand());
        existingCar.setYearOfProduction(car.getYearOfProduction());
        existingCar.setModelOfCar(car.getModelOfCar());
        existingCar.setKm(car.getKm());
        existingCar.setPrice(car.getPrice());

        if (car.getOrder() != null) {
            if (existingOrder != null) {
                existingOrder.setId(car.getOrder().getId());
            } else {
                existingOrder = car.getOrder();
                existingCar.setOrder(existingOrder);
            }
        } else {
            existingCar.setOrder(null);
        }

        carRepository.save(existingCar);
    }

    @Override
    public void delete(long id) throws Exception {
        boolean exists = carRepository.existsById(id);
        if(exists){
            carRepository.deleteById(id);
        }else {
            throw new RecordNotFoundException("Car not found.");
        }
    }

    @Override
    public List<Car> getAll() {
        return carRepository.findAll();
    }

    @Override
    public List<Car> getByBrand(String brand) {
        return carRepository.findByBrand(brand);
    }

    public List<Car> getCarsNotInOrder() {
        return carRepository.findByOrderIdIsNull();
    }
}
