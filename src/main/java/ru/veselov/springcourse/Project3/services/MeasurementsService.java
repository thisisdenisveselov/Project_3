package ru.veselov.springcourse.Project3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.veselov.springcourse.Project3.models.Measurement;
import ru.veselov.springcourse.Project3.repositories.MeasurementsRepository;
import ru.veselov.springcourse.Project3.util.SensorNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MeasurementsService {

    private final MeasurementsRepository measurementsRepository;
    private final SensorsService sensorsService;

    @Autowired
    public MeasurementsService(MeasurementsRepository measurementsRepository, SensorsService sensorsService) {
        this.measurementsRepository = measurementsRepository;
        this.sensorsService = sensorsService;
    }

    public List<Measurement> findAll() {
        return measurementsRepository.findAll();
    }

    public int findRainyDays() {
        return measurementsRepository.countMeasurementByRainingIsTrue();
    }
    @Transactional
    public void save(Measurement measurement) {
        measurement.setCreated_at(LocalDateTime.now());
        measurementsRepository.save(measurement);
    }
}
