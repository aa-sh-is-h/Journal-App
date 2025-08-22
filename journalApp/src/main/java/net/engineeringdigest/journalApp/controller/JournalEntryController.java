package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.services.JournalEntryServiceImpl;
import net.engineeringdigest.journalApp.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("journal")
public class JournalEntryController {
    @Autowired
    JournalEntryServiceImpl journalEntryService;

    @Autowired
    UserServiceImpl userService;

    @GetMapping("/all")
    public List<JournalEntry> getAll(){
        return journalEntryService.getAll();
    }

    @GetMapping()
    public ResponseEntity<?> getAllEntriesOfUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User userInDb = userService.findByUserName(userName);
        List<JournalEntry> usersAllEntries = userInDb.getJournalEntries();
        if(!usersAllEntries.isEmpty()){
            return new ResponseEntity<>(usersAllEntries, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry journalEntry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        try{
            journalEntryService.saveEntry(journalEntry, userName);
            return new ResponseEntity<>(journalEntry,HttpStatus.CREATED);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @GetMapping("id/{id}")
    public ResponseEntity<?> getJournalEntryById(@PathVariable String id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());
        if(!collect.isEmpty()){
            Optional<JournalEntry> journalEntry = journalEntryService.findById(id);
            if (journalEntry.isPresent()) {
                return new ResponseEntity<>(journalEntry, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{id}")
    public ResponseEntity<?> deleteJournalEntry(@PathVariable String id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean removed = journalEntryService.deleteById(id, userName);
        if (removed){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("id/{id}")
    public ResponseEntity<?> updateJournalById(@PathVariable String id, @RequestBody JournalEntry newJournalEntry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());
        if(!collect.isEmpty()){
            Optional<JournalEntry> journalEntry = journalEntryService.findById(id);
            if(journalEntry.isPresent()){
                JournalEntry found = journalEntry.get();
                found.setTitle(newJournalEntry.getTitle());
                found.setContent(newJournalEntry.getContent());
                journalEntryService.saveEntry(found);
                return new ResponseEntity<>(found,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
