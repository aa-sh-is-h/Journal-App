package net.engineeringdigest.journalApp.services;

import net.engineeringdigest.journalApp.entity.JournalEntry;

import java.util.List;
import java.util.Optional;

public interface JournalEntryService {
    List<JournalEntry> getAll();
    Optional<JournalEntry> findById(String id);
    void saveEntry(JournalEntry journalEntry, String userName);
    void saveEntry(JournalEntry journalEntry);
    void deleteById(String id, String userName);
}
