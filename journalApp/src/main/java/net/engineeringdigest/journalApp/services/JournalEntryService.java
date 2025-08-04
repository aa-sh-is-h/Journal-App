package net.engineeringdigest.journalApp.services;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    public List<JournalEntry> getAll(){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(String id){
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName){
        try {
            JournalEntry savedEntry = journalEntryRepository.save(journalEntry);
            User userInDb = userService.findByUserName(userName);
            userInDb.getJournalEntries().add(savedEntry);
            userService.saveEntry(userInDb);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveEntry(JournalEntry journalEntry){
        journalEntryRepository.save(journalEntry);
    }

    public void deleteById(String id, String userName){
        User userInDb = userService.findByUserName(userName);
        userInDb.getJournalEntries().removeIf(x -> x.getId().equals(id));
        userService.saveEntry(userInDb);
        journalEntryRepository.deleteById(id);
    }

}
