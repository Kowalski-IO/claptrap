package io.kowalski.claptrap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CLI {
	
	public static void main(String[] args) {
		parseArguments(args);
	}
	
	private static void parseArguments(String[] args) {
		CommandLineParser parser = new DefaultParser();
		Options options = buildCLIOptions();
		CommandLine line = null;
		try {
			 line = parser.parse(options, args);
		} catch (ParseException e) {
			System.err.println( "Invalid argument(s).");
			printHelp(options);
		}
		
		if(line.hasOption("help")) {
			printHelp(options);
		}
		
		Settings settings = new Settings();
		
		if(line.hasOption("http-port")) {
			settings.setHttpPort(Integer.parseInt(line.getOptionValue("http-port")));
		}
		
		if (line.hasOption("smtp-port")) {
			settings.setSmtpPort(Integer.parseInt(line.getOptionValue("smtp-port")));
		}
		
		if (line.hasOption("box-size")) {
			settings.setInboxSize(Integer.parseInt(line.getOptionValue("box-size")));
		}
		
		if (line.hasOption("debug")) {
			settings.setDebug(true);
		}
		
		App.run(settings);
		
	}
	
	private static Options buildCLIOptions() {
		Options options = new Options();
		
		options.addOption(Option.builder("h")
				.longOpt("help")
				.desc("show this help message")
				.hasArg(false)
				.build());
		
		options.addOption(Option.builder("p")
				.longOpt("http-port")
				.desc("sets the http port for the web server")
				.hasArg(true)
				.numberOfArgs(1)
				.type(Integer.class)
				.argName("port")
				.optionalArg(false)
				.required(false)
				.build());		
		
		options.addOption(Option.builder("s")
				.longOpt("smtp-port")
				.desc("sets the port for the smtp server")
				.hasArg(true)
				.numberOfArgs(1)
				.type(Integer.class)
				.argName("port")
				.optionalArg(false)
				.required(false)
				.build());		
		
		options.addOption(Option.builder("d")
				.longOpt("debug")
				.desc("enable debugging information")
				.hasArg(false)
				.required(false)
				.build());	
		
		options.addOption(Option.builder("b")
				.longOpt("box-size")
				.desc("sets maximum number of emails claptrap holds for each server (default: 500)")
				.hasArg(true)
				.numberOfArgs(1)
				.type(Integer.class)
				.argName("email-count")
				.optionalArg(false)
				.required(false)
				.build());	


		return options;
	}
	
	private static void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("claptrap", options);
		System.exit(0);
	}

}
