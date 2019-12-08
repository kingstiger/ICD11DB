package piotr.api.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import piotr.DAOs.mainCategories;
import piotr.DAOs.QmainCategories;

@Repository
public interface MainCategoriesRepository extends
        MongoRepository<mainCategories, String>,
        QuerydslPredicateExecutor<mainCategories> {


}
