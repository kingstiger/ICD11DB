package piotr.api.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import piotr.DAOs.MainCategories;
import piotr.DAOs.QMainCategories;

@Repository
public interface MainCategoriesRepository extends
        MongoRepository<MainCategories, String>,
        QuerydslPredicateExecutor<MainCategories> {


}
