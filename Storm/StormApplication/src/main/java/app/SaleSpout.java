package app;

import java.io.FileReader;
import java.util.List;
import java.util.Map;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import com.opencsv.CSVReader;

public class SaleSpout implements IRichSpout {

    private SpoutOutputCollector collector;
    boolean isCompleted;

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void nextTuple() {

        if (!isCompleted) {
            try (CSVReader reader = new CSVReader(new FileReader(SaleTopology.dataFile))) {
                List<String[]> rows = reader.readAll();
                int i = 0;
                for (String[] row : rows) {
                    Values val = new Values(row[11]);
                    System.out.println("<<<<<<<<<<<<<<<<<<<<<<< OBTENIDO: " + i + " - " + val.toString()+" >>>>>>>>>>>>>>>>>>>>>>");
                    this.collector.emit(val);
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("<<<<<<<<<<<<<<<<<<<<<<< "+e.getMessage()+" >>>>>>>>>>>>>>>>>>>>>>");
            }
            isCompleted = true;
        } else {
            this.close();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("watch_on"));
    }


    @Override
    public void close() { }

    @Override
    public void activate() { }

    @Override
    public void deactivate() { }

    @Override
    public void ack(Object msgId) { }

    @Override
    public void fail(Object msgId) { }

    @Override
    public Map < String, Object > getComponentConfiguration() { return null; }

}