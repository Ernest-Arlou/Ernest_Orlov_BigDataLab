package by.epam.bigdatalab;


import by.epam.bigdatalab.dao.connectionpool.factory.ConnectionPoolFactory;
import by.epam.bigdatalab.service.factory.ServiceFactory;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.*;


import java.time.LocalDate;
import java.util.Properties;

public class Main {
    private static final String PROPERTY_START_DATE = "start";
    private static final String PROPERTY_END_DATE = "end";
    private static final String PROPERTY_PATH = "path";
    private static final String PROPERTY_HELP = "help";
    private static final String PROPERTY_TEST = "test";
    private static final String FIRST_DAY_ADDITION = "-01";
    private static final String HELP_MESSAGE = "This application performs info parsing to database via Street Level Crimes API method \n" +
            "usage: Task3 [options...]\n" +
            "options:\n" +
            "-Dhelp  Displays help message\n" +
            "-Dtest  Test run for lat=52.629729&lng=-1.131592&date=2019-05\n" +
            "next three options required to be input together:\n" +
            "-Dstart=2019-05  Set 2019-05 as start date\n" +
            "-Dend=2019-06  Set 2019-06 as end date\n" +
            "-Dpath='value'  Set 'value' as path to file with list of existing coordinates\n";


    public static void main(String[] args) throws Exception {
        String[] args1 = {"-Dtest=test", "-Dstart=2019-05", "-Dend=2019-06", "-Dpath=E:/University_and_Work/Java_Training/BigData/Remote/Task3/src/main/resources/LondonStations.csv"};
//        String[] args = {"-Dstart=2019-05", "-Dend=2019-06", "-Dpath=E:/University_and_Work/Java_Training/BigData/Remote/Task3/src/main/resources/LondonStations.csv"};


        Options options = new Options();

        Option propertyOption = Option.builder()
                .longOpt("D")
                .argName("property=value")
                .hasArgs()
                .build();

        options.addOption(propertyOption);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args1);

        if (cmd.hasOption("D")) {
            Properties properties = cmd.getOptionProperties("D");
            if (properties.getProperty(PROPERTY_HELP) != null) {
                System.out.println(HELP_MESSAGE);
                return;
            }

            if (properties.getProperty(PROPERTY_TEST) != null) {
                ConnectionPoolFactory.getInstance().getConnectionPool().init();

                ServiceFactory.getInstance().getPoliceAPIService().test();

                ConnectionPoolFactory.getInstance().getConnectionPool().dispose();
                return;
            }

            LocalDate start = LocalDate.parse(properties.getProperty(PROPERTY_START_DATE) + FIRST_DAY_ADDITION);
            LocalDate end = LocalDate.parse(properties.getProperty(PROPERTY_END_DATE) + FIRST_DAY_ADDITION);
            String path = properties.getProperty(PROPERTY_PATH);

            ConnectionPoolFactory.getInstance().getConnectionPool().init();

            ServiceFactory.getInstance().getPoliceAPIService().processCrimes(start, end, path);

            ConnectionPoolFactory.getInstance().getConnectionPool().dispose();
        }


    }


}
