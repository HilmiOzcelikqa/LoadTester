import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.save.SaveService;
import org.apache.jorphan.collections.HashTree;

import java.io.File;

public class JMeterRunner {
    public static void main(String[] args) {
        // Set JMeter home directory
        String jmeterHome = "src/test/ReadBody/jmeter/apache-jmeter-5.5"; // Correct path
        // Correct path
        System.setProperty("jmeter.home", jmeterHome);

        // Jmeter
        StandardJMeterEngine jmeter = new StandardJMeterEngine();

        // Load JMeter test plan (.jmx file)
        File jmxFile = new File("C:\\Users\\hilmi.ozcelik\\Desktop\\front.jmx");
        HashTree testPlanTree;

        try {
            // Load the .jmx file into a HashTree
            testPlanTree = SaveService.loadTree(jmxFile);

            // Initialize the JMeter engine
            jmeter.configure(testPlanTree);

            // Create a ResultCollector to save results
            String resultTreePath = "C:\\Users\\hilmi.ozcelik\\Desktop\\jmt\\result_tree.jtl"; // Ensure this directory exists
            ResultCollector collector = new ResultCollector();
            collector.setFilename(resultTreePath);
            testPlanTree.add(collector);

            // Run the test
            jmeter.run();

            // Optionally, print results to console
            System.out.println("Test completed. Results saved to: " + resultTreePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
