package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.services.JournalEntryServiceImpl;
import net.engineeringdigest.journalApp.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("journal")
public class JournalEntryController {
    @Autowired
    JournalEntryServiceImpl journalEntryService;

    @Autowired
    UserServiceImpl userService;

    @GetMapping("/all")
    public List<JournalEntry> getAll(){
        List<JournalEntry> all = journalEntryService.getAll();
        return all;
    }

    @GetMapping("id/{id}")
    public ResponseEntity<?> getJournalEntryById(@PathVariable String id){
        Optional<JournalEntry> one = journalEntryService.findById(id);
        if(one.isPresent()){
            return new ResponseEntity<>(one,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{userName}")
    public ResponseEntity<?> getAllEntriesOfUser(@PathVariable String userName){
        User userInDb = userService.findByUserName(userName);
        List<JournalEntry> usersAllEntries = userInDb.getJournalEntries();
        if(!usersAllEntries.isEmpty()){
            return new ResponseEntity<>(usersAllEntries, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{userName}")
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry journalEntry, @PathVariable String userName){
        try{
            journalEntryService.saveEntry(journalEntry, userName);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("id/{userName}/{id}")
    public ResponseEntity<?> deleteJournalEntry(@PathVariable String id, @PathVariable String userName){
        journalEntryService.deleteById(id,userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("id/{userName}/{id}")
    public ResponseEntity<?> updateJournalById(@PathVariable String id, @RequestBody JournalEntry newJournalEntry, @PathVariable String userName){
        JournalEntry found = journalEntryService.findById(id).orElse(null);
        if(found != null){
            found.setTitle(newJournalEntry.getTitle());
            found.setContent(newJournalEntry.getContent());
            journalEntryService.saveEntry(found);
            return new ResponseEntity<>(found,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
