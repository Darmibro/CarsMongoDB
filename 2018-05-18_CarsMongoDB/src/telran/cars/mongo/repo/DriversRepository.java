package telran.cars.mongo.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import telran.cars.mongo.DriverCrud;
@Repository
public interface DriversRepository extends MongoRepository<DriverCrud, Long>{

}
