package com.rule.sync;

import com.alibaba.fastjson.JSON;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
public class BinlogAutoSyn {

    private static String getDataObject(List messages) {
        return JSON.toJSONString(messages);
    }

    public static void main(String[] args) {
        BinaryLogClient client = new BinaryLogClient("127.0.0.1", 3306, "root", "123456");
        client.setServerId(2);
        client.registerEventListener(event -> {
            EventData data = event.getData();
            if (data instanceof TableMapEventData) {
                TableMapEventData tableMapEventData = (TableMapEventData) data;
                log.info("Table: {} :[ {} - {} ]", tableMapEventData.getTableId(), tableMapEventData.getDatabase(), tableMapEventData.getTable());
            }
            if (data instanceof UpdateRowsEventData) {
                log.info("Update: {}", data.toString());
                UpdateRowsEventData updateRowsEventData = (UpdateRowsEventData) data;
                for (Map.Entry<Serializable[], Serializable[]> row : updateRowsEventData.getRows()) {
                    List<Serializable> entries = Arrays.asList(row.getValue());
                    log.info(String.valueOf(entries));
                    String dataObject = getDataObject(entries);
                    log.info(dataObject);
                }
            } else if (data instanceof WriteRowsEventData) {
                log.info("Insert: {}", data.toString());
                WriteRowsEventData writeRowsEventData = (WriteRowsEventData) data;
                List<Serializable[]> rows = writeRowsEventData.getRows();
                for (Serializable[] row : rows) {
                    List<Serializable> entries = Arrays.asList(row);
                    String dataObject = getDataObject(entries);
                    log.info(dataObject);
                }

            } else if (data instanceof DeleteRowsEventData) {
                log.info("Delete: {}", data.toString());
                DeleteRowsEventData writeRowsEventData = (DeleteRowsEventData) data;
                List<Serializable[]> rows = writeRowsEventData.getRows();
                for (Serializable[] row : rows) {
                    List<Serializable> entries = Arrays.asList(row);
                    String dataObject = getDataObject(entries);
                    log.info(dataObject);
                }
            }
        });

        try {
            client.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}