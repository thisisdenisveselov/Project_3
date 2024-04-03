package ru.veselov.springcourse.Project3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.veselov.springcourse.Project3.models.Sensor;
import ru.veselov.springcourse.Project3.repositories.SensorsRepository;
import ru.veselov.springcourse.Project3.util.SensorNameIsTakenException;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SensorsService {

    private final SensorsRepository sensorsRepository;

    @Autowired
    public SensorsService(SensorsRepository sensorsRepository) {
        this.sensorsRepository = sensorsRepository;
    }

    public Optional<Sensor> findByName(String name) {
        return sensorsRepository.findByName(name);
    }

    @Transactional
    public void save(Sensor sensor) {
        if(findByName(sensor.getName()).isPresent())
            throw new SensorNameIsTakenException();

        sensorsRepository.save(sensor);
    }
}
