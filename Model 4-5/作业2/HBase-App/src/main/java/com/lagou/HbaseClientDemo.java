package com.lagou;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

public class HbaseClientDemo {
    Configuration conf = null;
    Connection conn = null;
    HBaseAdmin admin = null;

    @Before
    public void init() throws IOException {
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "192.168.91.139,192.168.91.143,192.168.91.144");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conn = ConnectionFactory.createConnection(conf);
    }

    @Test
    public void createTable() throws IOException {
        admin = (HBaseAdmin) conn.getAdmin();

        HTableDescriptor user_relationship = new HTableDescriptor(TableName.valueOf("user_relation"));
        user_relationship.addFamily(new HColumnDescriptor("friends"));
        admin.createTable(user_relationship);
        System.out.println("user_relation！！");
    }


    @Test
    public void delete() throws IOException {

        final Table user_relationship = conn.getTable(TableName.valueOf("user_relation"));
        Delete delete = new Delete("1111".getBytes());
        delete.addColumn("friends".getBytes(),"2222".getBytes());

        user_relationship.delete(delete);
        user_relationship.close();
        System.out.println("删除成功");

    }

    @After
    public void destroy(){
        if(admin!=null){
            try {
                admin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(conn !=null){
            try {
                conn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}