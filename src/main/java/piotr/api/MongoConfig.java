package piotr.api;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    private MongoClientURI uri = new MongoClientURI(
            "mongodb+srv://first_user:mlogo45%2E@cluster0-2hypk.mongodb.net/test?retryWrites=true&w=majority");

    private MongoClient mongoClient = MongoClients.create(new ConnectionString("mongodb+srv://first_user:mlogo45%2E@cluster0-2hypk.mongodb.net/mydatabase?retryWrites=true&w=majority"));
    private MongoDatabase database = mongoClient.getDatabase("mydatabase");

    @Override
    public MongoClient mongoClient() {
        return mongoClient;
    }

    @Override
    protected String getDatabaseName() {
        return database.getName();
    }

}
