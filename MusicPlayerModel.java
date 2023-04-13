import java.util.ArrayList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MusicPlayerModel {
    private ArrayList<Song> playlist = new ArrayList<Song>();
    private Song currentSong;
    private boolean playing = false;
    private boolean paused = false;
    private ArrayList<ChangeListener> changeListeners = new ArrayList<ChangeListener>();

    public void addSong(Song song) {
        playlist.add(song);
        notifyChangeListeners();
    }

    public ArrayList<Song> getPlaylist() {
        return playlist;
    }

    public void playSong(Song song) {
        currentSong = song;
        playing = true;
        paused = false;
        notifyChangeListeners();
    }

    public void pauseSong() {
        paused = true;
        notifyChangeListeners();
    }

    public void stopSong() {
        currentSong = null;
        playing = false;
        paused = false;
        notifyChangeListeners();
    }

    public Song getCurrentSong() {
        return currentSong;
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean isPaused() {
        return paused;
    }

    public void addChangeListener(ChangeListener listener) {
        changeListeners.add(listener);
    }

    private void notifyChangeListeners() {
        for (ChangeListener listener : changeListeners) {
            listener.stateChanged(new ChangeEvent(this));
        }
    }
}