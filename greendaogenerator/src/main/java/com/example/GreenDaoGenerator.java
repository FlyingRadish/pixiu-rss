package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "org.houxg.pixiurss");

        addRSS(schema);
        new DaoGenerator().generateAll(schema, "./app/src-gen");
    }

    private static Entity addRSS(Schema schema){
        Entity channel = schema.addEntity("Source");
        channel.addIdProperty().primaryKey().autoincrement().getProperty();
        channel.addStringProperty("alias");
        channel.addStringProperty("title");
        channel.addStringProperty("link").unique();

        Entity article = schema.addEntity("Article");
        article.addIdProperty().primaryKey().autoincrement().getProperty();
        article.addStringProperty("title");
        article.addStringProperty("link").unique();
        article.addLongProperty("pubTime");
        article.addStringProperty("desc");
        Property sourceId = article.addLongProperty("SourceId").getProperty();

        article.addToOne(channel, sourceId);
        channel.addToMany(article, sourceId);
        return article;
    }
}
