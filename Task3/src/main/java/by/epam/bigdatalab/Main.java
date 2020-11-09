package by.epam.bigdatalab;


import by.epam.bigdatalab.dao.connectionpool.factory.ConnectionPoolFactory;
import by.epam.bigdatalab.service.factory.ServiceFactory;
import org.apache.commons.cli.*;

import java.time.LocalDate;
import java.util.Properties;

public class Main {
    private static final String PROPERTY_START_DATE = "start";
    private static final String PROPERTY_END_DATE = "end";
    private static final String PROPERTY_PATH = "path";
    private static final String PROPERTY_HELP = "help";
    private static final String PROPERTY_SAVE = "save";
    private static final String PROPERTY_OUTPUT = "output";
    private static final String PROPERTY_SAVE_IN_DB = "db";
    private static final String PROPERTY_SAVE_IN_FILE = "file";
    private static final String FIRST_DAY_ADDITION = "-01";
    private static final String HELP_MESSAGE = "This application performs info parsing to database via Street Level Crimes API method \n" +
            "usage: Task3 [options...]\n" +
            "options:\n" +
            "-Dhelp  Displays help message\n" +
            "next three options required to be input together:\n" +
            "-Dstart=2019-05  Set 2019-05 as start date\n" +
            "-Dend=2019-06  Set 2019-06 as end date\n" +
            "-Dpath='value'  Set 'value' as path to file with list of existing coordinates\n" +
            "-Dsave='value'  Set 'value' 'db' to save in database or 'file' to save in file\n" +
            "if you set save option as 'file' you need to add path to file for saving:\n" +
            "-Doutput='value'   Set 'value' as path to file where to save data\n";


    public static void main(String[] args) throws Exception {
        String[] args1 = {"-Dstart=2019-05", "-Dend=2019-05",
                "-Dpath=E:/University_and_Work/Java_Training/BigData/Remote/Task3/src/main/resources/LondonStations.csv",
                "-Dsave=db",
                "-Doutput=E:/University_and_Work/Java_Training/BigData/Remote/Task3/src/main/resources/Crimes.txt"};


        Options options = new Options();

        Option propertyOption = Option.builder()
                .longOpt("D")
                .argName("property=value")
                .hasArgs()
                .build();

        options.addOption(propertyOption);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption("D")) {
            Properties properties = cmd.getOptionProperties("D");
            if (properties.getProperty(PROPERTY_HELP) != null) {
                System.out.println(HELP_MESSAGE);
                return;
            }

            ConnectionPoolFactory.getInstance().getConnectionPool().init();

            LocalDate start = LocalDate.parse(properties.getProperty(PROPERTY_START_DATE) + FIRST_DAY_ADDITION);
            LocalDate end = LocalDate.parse(properties.getProperty(PROPERTY_END_DATE) + FIRST_DAY_ADDITION);
            String path = properties.getProperty(PROPERTY_PATH);

            if (properties.getProperty(PROPERTY_SAVE).equals(PROPERTY_SAVE_IN_FILE)) {
                String savePath = properties.getProperty(PROPERTY_OUTPUT);

                ServiceFactory.getInstance().getPoliceAPIService().processCrimesToFile(start, end, path, savePath);

            }

            if (properties.getProperty(PROPERTY_SAVE).equals(PROPERTY_SAVE_IN_DB)) {
                ServiceFactory.getInstance().getPoliceAPIService().processCrimesToDB(start, end, path);
            }


            ConnectionPoolFactory.getInstance().getConnectionPool().dispose();
        }


    }


}
