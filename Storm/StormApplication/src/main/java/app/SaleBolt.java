package app;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

public class SaleBolt implements IRichBolt {

    Map <String,Integer> counters;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.counters = new HashMap<>();
    }

    @Override
    public void execute(Tuple input) {
        String word = input.getStringByField("watch_on");
        if (!counters.containsKey(word)) {
            counters.put(word, 1);
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<< " + word + " >>>>>>>>>>>>>>>>>>>>>>");
        } else {
            counters.put(word, counters.get(word) + 1);
        }
    }

    @Override
    public void cleanup() {
        try {
            FileWriter fileWriter = new FileWriter(SaleTopology.resultFile);
            for (Map.Entry<String, Integer> entry : counters.entrySet()) {
                fileWriter.write(entry.getKey() + "\t" + entry.getValue()+"\n");
            }
            fileWriter.close();
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<< "+e.getMessage()+" >>>>>>>>>>>>>>>>>>>>>>");
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }

    @Override
    public Map < String, Object > getComponentConfiguration() { return null; }

}