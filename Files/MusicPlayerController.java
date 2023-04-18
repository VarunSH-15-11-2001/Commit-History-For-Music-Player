import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MusicPlayerController implements ActionListener {
    private MusicPlayerView view;
    private MusicPlayerModel model;

    public MusicPlayerController(MusicPlayerView view, MusicPlayerModel model) {
        this.view = view;
        this.model = model;
        this.view.addListeners(this);
        this.view.setPlaylist(this.model.getPlaylist());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.addButton) {
            String title = view.getSongTitle();
            String artist = view.getSongArtist();
            int duration = view.getSongDuration();
            Song song = new Song(title, artist, duration);
            model.addSong(song);
            view.clearInputs();
        } else if (e.getSource() == view.playButton) {
            Song song = view.getSelectedSong();
            if (song != null) {
                model.playSong(song);
            }
        } else if (e.getSource() == view.pauseButton) {
            model.pauseSong();
        // } else if (e.getSource() == view.stopButton) {
        //     model.stopSong();
        } else if (e.getSource() == view.nextButton) {
            model.nextSong();
        } else if(e.getSource() == view.prevButton) {
            model.prevSong();
        }
    }
}