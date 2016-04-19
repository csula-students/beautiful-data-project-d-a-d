package edu.csula.datascience.examples;

import com.google.common.collect.Lists;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.List;
import java.util.Random;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Sorts.descending;

/**
 * An example to demonstrate mongodb java driver concept
 */
public class MongoExampleApp {
    public static void main (String[] args) {
        // establish database connection to MongoDB
        MongoClient mongoClient = new MongoClient("db");
        // select `bd-example` as testing database
        MongoDatabase database = mongoClient.getDatabase("bd-example");

        // select collection by name `test`
        MongoCollection<Document> collection = database.getCollection("test");

        // to create new document
        Document doc = new Document("name", "MongoDB")
            .append("type", "database")
            .append("count", 1)
            .append("info", new Document("x", 203).append("y", 102));

        // to insert document
        collection.insertOne(doc);
        System.out.println(
            String.format(
                "Inserted new document %s",
                doc.toJson()
            )
        );

        // to find a document out of collection
        Document foundDoc = collection.find().first();
        System.out.println(
            String.format(
                "Found document %s",
                foundDoc.toJson()
            )
        );

        // insert 1000 documents
        List<Document> docs = Lists.newArrayList();
        for (int i = 0; i < 100; i ++) {
            Document newDoc = new Document("name", i)
                .append("count", new Random().nextInt(100));
            docs.add(newDoc);
        }
        collection.insertMany(docs);
        System.out.println("Inserted many random count documents");

        // get many documents at once with iterator
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                System.out.println(
                    String.format(
                        "Got document %s",
                        cursor.next().toJson()
                    )
                );
            }
        }

        // query by field to find single document
        Document mongoDoc = collection.find(eq("name", "MongoDB")).first();
        System.out.println(
            String.format(
                "Found document by name `MongoDB` %s",
                mongoDoc.toJson()
            )
        );

        // query by field to find a list of documents
        collection.find(and(gt("count", 50), lt("count", 70)))
            .sort(descending("count")) // sort by count in desc order
            .projection(excludeId())
            .forEach((Block<? super Document>) d -> {
                System.out.println(d.toJson());
            });
    }
}
