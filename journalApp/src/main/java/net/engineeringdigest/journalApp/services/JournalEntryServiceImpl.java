package net.engineeringdigest.journalApp.services;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryServiceImpl implements JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserServiceImpl userService;

    @Override
    public List<JournalEntry> getAll(){
        return journalEntryRepository.findAll();
    }

    @Override
    public Optional<JournalEntry> findById(String id){
        return journalEntryRepository.findById(id);
    }

    @Override
    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName){
        try {
            User userInDb = userService.findByUserName(userName);
            JournalEntry savedEntry = journalEntryRepository.save(journalEntry);

            userInDb.getJournalEntries().add(savedEntry);
            userService.saveExistingUser(userInDb);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveEntry(JournalEntry journalEntry){
        journalEntryRepository.save(journalEntry);
    }

    @Override
    @Transactional
    public boolean deleteById(String id, String userName){
        boolean removed = false;
        try {
            User userInDb = userService.findByUserName(userName);
            removed = userInDb.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if(removed) {
                userService.saveExistingUser(userInDb);
                journalEntryRepository.deleteById(id);
            }
            return removed;
        } catch (Exception e) {
            throw new RuntimeException("Some Error While Deleting the Entry",e);
        }

    }

}
