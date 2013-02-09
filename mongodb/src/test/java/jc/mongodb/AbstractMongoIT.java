/**
 * Copyright 2013 the project contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package jc.mongodb;

import org.junit.After;
import org.junit.Before;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;

public abstract class AbstractMongoIT {

    protected Mongo mongo;
    protected DB db;
    protected DBCollection dbCollection;

    @Before
    public void createMongo() throws Exception {
        MongoOptions options = new MongoOptions();
        mongo = new Mongo("localhost", options);
        mongo.dropDatabase("testdb");
        db = mongo.getDB("testdb");
        dbCollection = db.getCollection("testcollection");
    }

    @After
    public void closeMongo() {
        db.dropDatabase();
        mongo.close();
    }

}
