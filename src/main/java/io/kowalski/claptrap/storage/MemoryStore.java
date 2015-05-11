package io.kowalski.claptrap.storage;

import io.kowalski.claptrap.smtp.Message;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;

import org.mapdb.DB;
import org.mapdb.DBMaker;

public class MemoryStore {
	
	public static final DB db = DBMaker.newMemoryDB().make();
	
	public final void add(final Message message, final String mapName) {
		ConcurrentNavigableMap<UUID, Object> treeMap = db.getTreeMap(mapName);
		if (treeMap.size() >= 500) {
			treeMap.clear();
		}
		treeMap.put(message.getUuid(), message);
		db.commit();
	}
	
	public final void remove(final String uuid, final String mapName) {
		ConcurrentNavigableMap<UUID, Object> treeMap = db.getTreeMap(mapName);
		treeMap.remove(UUID.fromString(uuid));
		db.commit();
	}

	public final Map<UUID, Message> dump(final String mapName) {
		return db.getTreeMap(mapName);
	}

	public final String dumpAll(final String mapName) {
		ConcurrentNavigableMap<UUID, Object> treeMap = db.getTreeMap(mapName);
		treeMap.clear();
		db.commit();
		return "All gone!";
	}
}
