package io.kowalski.claptrap.storage.sms;
//package io.kowalski.claptrap.sms;
//
//import java.util.Map;
//import java.util.Set;
//import java.util.UUID;
//
//import javax.inject.Inject;
//
//import io.kowalski.claptrap.storage.MessageStorageService;
//
//public class SMSStorageService implements MessageStorageService<SMS, Phone> {
//
//    private final SMSBroadcastService broadcastService;
//
//    @Inject
//    public SMSStorageService(final SMSBroadcastService broadcastService) {
//        this.broadcastService = broadcastService;
//    }
//
//    @Override
//    public void store(final SMS sms) {
//        broadcastService.broadcast(sms);
//    }
//
//    @Override
//    public Map<UUID, SMS> fetchMessagesFromCollection(final Phone phone) {
//
//    }
//
//    @Override
//    public SMS fetchMessageById(final Phone phone, final UUID id) {
//    }
//
//    @Override
//    public SMS deleteMessage(final Phone phone, final UUID id) {
//    }
//
//    @Override
//    public void emptyCollection(final Phone phone) {
//    }
//
//    @Override
//    public Set<Phone> fetchCollections() {
//    }
//
//    @Override
//    public Set<Phone> fetchCollectionsWithCounts() {
//    }
//
//}
