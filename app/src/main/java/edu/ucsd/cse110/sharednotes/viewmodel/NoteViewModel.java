package edu.ucsd.cse110.sharednotes.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import edu.ucsd.cse110.sharednotes.model.Note;
import edu.ucsd.cse110.sharednotes.model.NoteDatabase;
import edu.ucsd.cse110.sharednotes.model.NoteRepository;

public class NoteViewModel extends AndroidViewModel {
    private LiveData<Note> note;
    private final NoteRepository repo;
    private ScheduledFuture<?> noteFuture;
    public NoteViewModel(@NonNull Application application) {
        super(application);
        var context = application.getApplicationContext();
        var db = NoteDatabase.provide(context);
        var dao = db.getDao();
        this.repo = new NoteRepository(dao);
    }

    /* public LiveData<Note> getNote(String title) {
        // TODO: use getSynced here instead?
        // The returned live data should update whenever there is a change in
        // the database, or when the server returns a newer version of the note.
        // Polling interval: 3s.
        MutableLiveData<Long> timeValue = new MutableLiveData<>();
        var scheduler = Executors.newSingleThreadScheduledExecutor();
        this.noteFuture = scheduler.scheduleAtFixedRate(() -> {
            if (note == null) {
                note = repo.getSynced(title);
            }

        }, 0, 3, TimeUnit.MILLISECONDS);


        return note;
    }*/

    public LiveData<Note> getNote(String title) {
        // Create a MutableLiveData object to hold the current value of the note
        //MutableLiveData<Note> noteLiveData = new MutableLiveData<>();
        if (note == null) {
            note = repo.getSynced(title);
        }
        // Create a ScheduledExecutorService to schedule the update task
        /* ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        // Schedule a task to update the note every 3 seconds
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // Get the latest version of the note from the repository
                if (note == null) {
                    note = repo.getLocal(title);
                }
                LiveData<Note> updatedNote = repo.getSynced(title);

                // Update the value of noteLiveData if the updatedNote is different
                if (!updatedNote.equals(note.getValue())) {
                    note = updatedNote;
                }
            }
        }, 0, 3, TimeUnit.SECONDS); */

        // Return the noteLiveData object
        return note;
    }


    public void save(Note note) {
        // TODO: try to upload the note to the server.
        repo.upsertSynced(note);
    }
}
