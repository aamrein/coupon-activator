package couponactivator.cli;

import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CliParser {

    private static final String LONG_OPT_USERNAME = "user";
    private static final String LONG_OPT_PASSWORD = "password";
    private static final String LONG_OPT_CREDENTIALS = "credentials";
    private static final String LONG_OPT_TYPE_FILTER = "type-filter";
    private static final String LONG_OPT_HELP = "help";

    public static CliArgs parse(String[] args) throws IllegalArgumentException {
        Options options = getOptions();

        CommandLineParser parser = new DefaultParser();

        CliArgs cliArgs = new CliArgs();
        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption(LONG_OPT_HELP)) {
                displayHelpAndExit(options, "Help: These are the options you have. " +
                        "Please enter at minimum username and password or pass a credentials file");
            }

            readCredentials(cmd, cliArgs);

            parseFilter(cmd, cliArgs);

        } catch (ParseException e) {
            displayHelpAndExit(options, e.getMessage());
        }

        return cliArgs;
    }

    private static Options getOptions() {
        Options options = new Options();

        Option username = new Option("u", LONG_OPT_USERNAME, true, "User name for login");
        options.addOption(username);

        Option password = new Option("p", LONG_OPT_PASSWORD, true, "Password for login");
        options.addOption(password);

        Option credentials = new Option("c", LONG_OPT_CREDENTIALS, true,
                "Credentials file for login. At the first line of your file must stand your username and passowrd, " +
                        "separated with a double point (':').\n" +
                        "Example:\nmyUsername:myPassword");
        options.addOption(credentials);

        Option typeFilter = new Option("tf", LONG_OPT_TYPE_FILTER, true,
                "This filter excludes the given types from activation and deactivation.\n" +
                        "Example 1:\n-tf bonus\nThis excludes all coupon with the type BONUS.\n" +
                        "Example 2:\n-tf bonus discount\nThis excludes all coupon with the type BONUS and DISCOUNT.\n" +
                        "Known types from reverse engineering:\nBONUS, DISCOUNT");
        typeFilter.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(typeFilter);

        Option help = new Option("h", LONG_OPT_HELP, false, "Prints this help.");
        options.addOption(help);

        return options;
    }

    private static void displayHelpAndExit(Options options, String header) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar coupon-activator-X.Y.jar -u yourUserName -p yourPassword",
                "\n" + header,
                options,
                "");
        System.exit(1);
    }

    private static void readCredentials(CommandLine cmd, CliArgs cliArgs) throws ParseException {
        if (cmd.hasOption(LONG_OPT_CREDENTIALS)) {
            String path = cmd.getOptionValue(LONG_OPT_CREDENTIALS);
            File file = new File(path);
            try (BufferedReader reader = new BufferedReader(new FileReader(file))){
                String[] credentials = reader.readLine().split(":");
                if (credentials.length > 1 && credentials[0].length() > 0 && credentials[1].length() > 0) {
                    cliArgs.setUsername(credentials[0]);
                    cliArgs.setPassword(credentials[1]);
                } else {
                    throw new ParseException("Credentials file is corrupt. " +
                            "Type 'myUsername:myPassword' on the first line of your file");
                }
            } catch (IOException e) {
                throw new ParseException("Cannot read credentials file. Error is: " + e.getMessage());
            }

        } else if (cmd.hasOption(LONG_OPT_USERNAME) && cmd.hasOption(LONG_OPT_PASSWORD)) {
            cliArgs.setUsername(cmd.getOptionValue(LONG_OPT_USERNAME));
            cliArgs.setPassword(cmd.getOptionValue(LONG_OPT_PASSWORD));
        } else {
            throw new ParseException("No credentials or username and password. One of them are required.");
        }
    }

    private static void parseFilter(CommandLine cmd, CliArgs cliArgs) {
        if (cmd.hasOption(LONG_OPT_TYPE_FILTER)) {
            cliArgs.setTypeFilter(cmd.getOptionValue(LONG_OPT_TYPE_FILTER));
        }
    }
}
