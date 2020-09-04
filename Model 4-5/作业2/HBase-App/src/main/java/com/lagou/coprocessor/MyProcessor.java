package com.lagou.coprocessor;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CoprocessorEnvironment;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.filter.ByteArrayComparable;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class MyProcessor extends BaseRegionObserver {


    @Override
    public void preDelete(ObserverContext<RegionCoprocessorEnvironment> e, Delete delete, WALEdit edit, Durability durability) throws IOException {
        JsonElement je = new JsonParser().parse(delete.toJSON());
        String uid = je.getAsJsonObject().get("families").getAsJsonObject().get("friends").getAsJsonArray().get(0).getAsJsonObject().get("qualifier").getAsString();

        Delete delete2 = new Delete(uid.getBytes());
        delete2.addColumn("friends".getBytes(),delete.getRow());
        final HTableInterface table = e.getEnvironment().getTable(TableName.valueOf("user_relation"));
        table.delete(delete2);
        table.close();

    }
}
