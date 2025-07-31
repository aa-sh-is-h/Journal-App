package net.engineeringdigest.journalApp.entity;

import lombok.Data;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "journal_entries")
public class JournalEntry {
    @Id
    private String id;
    private String title;
    private String content;

}
