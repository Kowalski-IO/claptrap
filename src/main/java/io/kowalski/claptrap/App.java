package io.kowalski.claptrap;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.SparkBase.staticFileLocation;
import io.kowalski.claptrap.smtp.DefaultMessageHandlerFactory;
import io.kowalski.claptrap.storage.MemoryStore;
import io.kowalski.claptrap.transformer.JsonTransformer;

import java.util.UUID;

import org.subethamail.smtp.server.SMTPServer;

/**
 * Claptrap Fake smtp server to use for debugging purposes. Stores messages in
 * MapDB for super fast storage and general laziness.
 * 
 * @author Brandon Kowalski
 * @version 0.0.1
 */
public class App {

	private static final DefaultMessageHandlerFactory FACTORY = new DefaultMessageHandlerFactory();

	public static final MemoryStore MEM_STORE = new MemoryStore();

	public static void run(Settings settings) {
		printClaptrap();
		httpServer(settings.getHttpPort());
		mailServer(settings.getSmtpPort());
		MEM_STORE.setInboxSize(settings.getInboxSize());
	}

	private static void httpServer(final int port) {
		staticFileLocation("/public");
		
		port(port);

		get("/heartbeat", (req, res) -> {
			return "Claptrap is up!";
		}, new JsonTransformer());

		get("/servers", (req, res) -> {
			return MEM_STORE.getServers();
		}, new JsonTransformer());

		get("/emails/:mapName", (req, res) -> {
			return MEM_STORE.dumpAsSortedList(req.params(":mapName"));
		}, new JsonTransformer());

		get("/email/:mapName/:uuid", (req, res) -> {
			UUID uuid = UUID.fromString(req.params(":uuid"));
			return MEM_STORE.dump(req.params(":mapName")).get(uuid);
		}, new JsonTransformer());

		delete("/email/:mapName", (req, res) -> {
			return MEM_STORE.dumpAll(req.params(":mapName"));
		}, new JsonTransformer());

		delete("/email/:mapName/:uuid", (req, res) -> {
			UUID uuid = UUID.fromString(req.params(":uuid"));
			return MEM_STORE.dump(req.params(":mapName")).remove(uuid);
		}, new JsonTransformer());
	}

	private static void mailServer(final int port) {
		SMTPServer smtpServer = new SMTPServer(FACTORY);
		smtpServer.setSoftwareName("Claptrap SMTP");
		smtpServer.setPort(port);
		smtpServer.start();
	}

	private static void printClaptrap() {
		System.out.println("       ,");
		System.out.println("       |");
		System.out.println("    ]  |.-._");
		System.out.println("     \\|\"(0)\"| _]");
		System.out.println("     `|=\\#/=|\\/");
		System.out.println("      :  _  :");
		System.out.println("       \\/_\\/ ");
		System.out.println("        |=| ");
		System.out.println("        `-' Hello Minion!");
	}
}
