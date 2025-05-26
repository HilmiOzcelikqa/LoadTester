import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.control.Header;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;

import java.io.FileOutputStream;

public class JMeter2 {
    public static void main(String[] args) throws Exception {
        // JMeter engine
        StandardJMeterEngine jmeter = new StandardJMeterEngine();

        // JMeter initialization (properties, log levels, locale, etc)
        JMeterUtils.loadJMeterProperties("C:\\Users\\hilmi.ozcelik\\Desktop\\apache-jmeter-5.5\\bin\\jmeter.properties");
        JMeterUtils.setJMeterHome("src/test/ReadBody/jmeter/apache-jmeter-5.5/bin/jmeter");
        JMeterUtils.initLocale();

        // JMeter Test Plan
        HashTree testPlanTree = new HashTree();

        // HTTP Sampler
        HTTPSamplerProxy httpSampler = new HTTPSamplerProxy();
        httpSampler.setDomain("https://comm3-service-test.arksigner.dev");
        httpSampler.setPort(80);
        httpSampler.setPath("/api/Mobile/GetAppointmentInfo");
        httpSampler.setMethod("POST");

        // HTTP Request Body
        Arguments arguments = new Arguments();
        arguments.addArgument("CustomerIdentityNumber", "29875374016");
        arguments.addArgument("StartDate", "2024-09-25");
        arguments.addArgument("EndDate", "2024-09-25");
        arguments.addArgument("ProcessId", "4");
        httpSampler.setArguments(arguments);

        // HTTP Headers
        HeaderManager headerManager = new HeaderManager();
        headerManager.add(new Header("ApikEy","0AF22349-1A6D-4B3B-87C6-AE28443609D2"));
        headerManager.add(new Header("clientID","65e7e4e3-0d8f-49f0-bec2-e3201481ca81"));
        httpSampler.setHeaderManager(headerManager);

        // Loop Controller
        LoopController loopController = new LoopController();
        loopController.setLoops(1);
        loopController.setFirst(true);
        loopController.initialize();

        // Thread Group
        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setNumThreads(100);
        threadGroup.setRampUp(1);
        threadGroup.setDuration(60);
        threadGroup.setSamplerController(loopController);

        // Test Plan
        org.apache.jmeter.testelement.TestPlan testPlan = new org.apache.jmeter.testelement.TestPlan("API Load Test");
        testPlanTree.add(testPlan);
        testPlanTree.add(testPlan, threadGroup);
        testPlanTree.add(threadGroup, httpSampler);

        // Save test plan to JMX file
        SaveService.saveTree(testPlanTree, new FileOutputStream("src/test/ReadBody/output/results.jtl"));

        // Run Test Plan
        jmeter.configure(testPlanTree);
        jmeter.run();

        // Summariser
        Summariser summer = null;
        String summariserName = JMeterUtils.getPropDefault("summariser.name", "summary");
        if (summariserName.length() > 0) {
            summer = new Summariser(summariserName);
        }

        // Generate Report
        // You can use JMeter's built-in reporting tool to generate a report from the results file
        // Example: JMeterUtils.runJMeter("-g", "path/to/results.jtl", "-o", "path/to/report");
    }
}
