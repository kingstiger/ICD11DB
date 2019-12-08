package piotr.api.Repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import piotr.DAOs.ICD11;
import piotr.DAOs.QICD11;

import java.util.List;

@Repository
public interface ICD11Repository extends
        MongoRepository<ICD11, String>,
        QuerydslPredicateExecutor<ICD11>  {

    List<ICD11> findAllByTitle(String title);

    List<ICD11> findAllByType(String type);

    List<ICD11> findAllByParent(String parent);
}
