package app;

import javax.xml.bind.SchemaOutputResolver;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;


public class SaleTopology {

    static String dataFile;
    static String resultFile;
    static final String TOPOLOGY = "artisan-finder-topology";
    static final String SPOUT = "artisan-spout";
    static final String BOLT = "artisan-bolt";

    public static void main(String args[]) throws Exception {

        dataFile = "./data_"+args[0]+".csv";
        resultFile = "./result_"+args[0]+".csv";

        System.out.println("********************* START TOPOLOGY WITH : " + dataFile + " *********************");
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout(SPOUT, new SaleSpout());
        topologyBuilder.setBolt(BOLT, new SaleBolt(), 1).shuffleGrouping(SPOUT);
        Config config = new Config();
        config.setDebug(true);
        LocalCluster localCluster = new LocalCluster();

        localCluster.submitTopology(TOPOLOGY, config, topologyBuilder.createTopology());
        try {
            Thread.sleep(10000);
        } catch (Exception e) {
            System.out.println("--------- MESSAGE ERROR " + e.getMessage() + " ---------");
        }
        localCluster.shutdown();

        System.out.println("--------- END ---------");
        System.exit(0);
    }


}