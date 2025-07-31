package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.services.JournalEntryService;
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
    JournalEntryService journalEntryService;

    @GetMapping("all")
    public ResponseEntity<?> getAllEntries(){
        List<JournalEntry> all = journalEntryService.getAll();
        if(!all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("id/{id}")
    public ResponseEntity<?> getJournalEntryById(@PathVariable String id){
        Optional<JournalEntry> one = journalEntryService.findById(id);
        if(one.isPresent()){
            return new ResponseEntity<>(one,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry journalEntry){
        try{
            journalEntryService.saveEntry(journalEntry);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("id/{id}")
    public ResponseEntity<?> deleteJournalEntry(@PathVariable String id){
        Optional<JournalEntry> found = journalEntryService.findById(id);
        if(found.isPresent()){
            journalEntryService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("id/{id}")
    public ResponseEntity<?> updateJournalById(@PathVariable String id, @RequestBody JournalEntry journalEntry){
        Optional<JournalEntry> found = journalEntryService.findById(id);
        if(found.isPresent()){
            JournalEntry old = found.get();
            old.setTitle(journalEntry.getTitle());
            old.setContent(journalEntry.getContent());
            journalEntryService.saveEntry(old);
            return new ResponseEntity<>(old,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
