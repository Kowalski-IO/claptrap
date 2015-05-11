package io.kowalski.claptrap;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.SparkBase.staticFileLocation;
import io.kowalski.claptrap.smtp.DefaultMessageHandlerFactory;
import io.kowalski.claptrap.smtp.Message;
import io.kowalski.claptrap.storage.MemoryStore;
import io.kowalski.claptrap.transformer.JsonTransformer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.subethamail.smtp.server.SMTPServer;

/**
 * Claptrap
 * 
 * @author Brandon Kowalski Fake smtp server to use for debugging purposes.
 *         Stores messages in MapDB for super fast storage.
 */
public class Claptrap {

	private static final DefaultMessageHandlerFactory FACTORY = new DefaultMessageHandlerFactory();

	public static final Set<String> SERVER_SET = new HashSet<String>();
	public static final MemoryStore MEM_STORE = new MemoryStore();

	public static void main(String[] args) {
		printClaptrap();
		staticFileLocation("/public");
		startMailServer();

		get("/heartbeat", (req, res) -> {
			return "Potato Salad is up!";
		}, new JsonTransformer());

		get("/servers", (req, res) -> {
			return SERVER_SET;
		}, new JsonTransformer());

		get("/emails/:mapName",
				(req, res) -> {
					List<Message> tempList = new ArrayList<Message>(MEM_STORE
							.dump(req.params(":mapName")).values());

					Collections.sort(tempList);

					return tempList;
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

	private static void startMailServer() {
		SMTPServer smtpServer = new SMTPServer(FACTORY);
		smtpServer.setSoftwareName("Potato Salad Fake SMTP");
		smtpServer.setPort(2525);
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
