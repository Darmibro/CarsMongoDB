package telran.cars.mongo.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import telran.cars.mongo.CarCrud;
@Repository
public interface CarRepository extends MongoRepository<CarCrud, String>{

}
