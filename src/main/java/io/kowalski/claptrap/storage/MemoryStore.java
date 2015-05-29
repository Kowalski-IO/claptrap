package io.kowalski.claptrap.storage;

import io.kowalski.claptrap.smtp.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;

import org.mapdb.DB;
import org.mapdb.DBMaker;

public class MemoryStore {
	
	private final Set<String> servers = new HashSet<String>();
	private static final DB DB = DBMaker.newMemoryDB().make();
	
	private int inboxSize = 500;
	
	public final void add(final Message message, final String mapName) {
		ConcurrentNavigableMap<UUID, Object> treeMap = DB.getTreeMap(mapName);
		if (treeMap.size() >= inboxSize) {
			treeMap.clear();
		}
		treeMap.put(message.getUuid(), message);
		DB.commit();
	}
	
	public final void remove(final String uuid, final String mapName) {
		ConcurrentNavigableMap<UUID, Object> treeMap = DB.getTreeMap(mapName);
		treeMap.remove(UUID.fromString(uuid));
		DB.commit();
	}

	public final Map<UUID, Message> dump(final String mapName) {
		return DB.getTreeMap(mapName);
	}
	
	public final List<Message> dumpAsList(final String mapName) {
		return new ArrayList<Message>(dump(mapName).values());
	}
	
	public final List<Message> dumpAsSortedList(final String mapName) {
		List<Message> tempList = dumpAsList(mapName);
		Collections.sort(tempList);
		return tempList;
	}

	public final String dumpAll(final String mapName) {
		ConcurrentNavigableMap<UUID, Object> treeMap = DB.getTreeMap(mapName);
		treeMap.clear();
		DB.commit();
		return "All gone!";
	}

	public final Set<String> getServers() {
		return servers;
	}

	public final int getBoxSize() {
		return inboxSize;
	}

	public final void setInboxSize(final int inboxSize) {
		this.inboxSize = inboxSize;
	}
}
