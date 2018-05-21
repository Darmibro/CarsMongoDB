package telran.cars.mongo.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import telran.cars.mongo.RentRecordCrud;
@Repository
public interface RentRecordsRepositor extends MongoRepository<RentRecordCrud, Long>{



	List<RentRecordCrud> findByCarNumber(String carNumber);

	//List<RentRecordCrud> findByRentDataBetween(LocalDate localDateStart, LocalDate returnedDateDelet);

	

}
