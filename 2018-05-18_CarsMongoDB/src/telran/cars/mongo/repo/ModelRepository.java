package telran.cars.mongo.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import telran.cars.mongo.ModelCrud;
@Repository
public interface ModelRepository extends MongoRepository<ModelCrud, String>{

}
