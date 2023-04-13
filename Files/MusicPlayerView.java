import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.io.File;

public class MusicPlayerView extends JFrame implements ChangeListener {

    private JLabel titleLabel = new JLabel("Title:");
    private JLabel artistLabel = new JLabel("Artist:");
    private JLabel durationLabel = new JLabel("Duration (s):");
    private JTextField titleField = new JTextField(20);
    private JTextField artistField = new JTextField(20);
    private JTextField durationField = new JTextField(5);
    public JButton addButton = new JButton("Add");
    public JButton playButton = new JButton("Play");
    public JButton pauseButton = new JButton("Pause");
    public JButton stopButton = new JButton("Stop");

    public JButton nextButton = new JButton("Next Song");
    public JButton prevButton = new JButton("Prev Song");

    private JList<Song> playlist = new JList<Song>();
    private DefaultListModel<Song> playlistModel = new DefaultListModel<>();



    private MusicPlayerModel model;
    public MusicPlayerView(MusicPlayerModel model) {
        this.model = model;
        this.model.addChangeListener(this);
        setTitle("Music Player");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(titleLabel);
        inputPanel.add(titleField);
        inputPanel.add(artistLabel);
        inputPanel.add(artistField);
        inputPanel.add(durationLabel);
        inputPanel.add(durationField);
        add(inputPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(playButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(prevButton);
        add(buttonPanel, BorderLayout.CENTER);
        playlist.setModel(playlistModel);
        playlistModel.addAll(model.getPlaylist());
        add(new JScrollPane(playlist), BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        setResizable(true);

        createPlaylist();
    }

    public void createPlaylist() {
        File dir = new File("/Users/varunshankarhoskere/Desktop/Academics/PES/Sem6/OOAD/Project/Java-project/Songs");
        for (File file : dir.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".mp3")) {
                String title = file.getName().substring(0, file.getName().lastIndexOf('.'));
                Song song = new Song(title, "unknown artist", 0);
                playlistModel.addElement(song);
            }
        }
    }    

    public void addListeners(ActionListener listener) {
        addButton.addActionListener(listener);
        playButton.addActionListener(listener);
        pauseButton.addActionListener(listener);
        stopButton.addActionListener(listener);
    }

    public void clearInputs() {
        titleField.setText("");
        artistField.setText("");
        durationField.setText("");
    }
    public String getSongTitle() {
        return titleField.getText();
        }
        public String getSongArtist() {
            return artistField.getText();
        }
        
        public int getSongDuration() {
            int duration = 0;
            try {
                duration = Integer.parseInt(durationField.getText());
            } catch (NumberFormatException e) {
                duration = 0;
            }
            return duration;
        }
        
        public void setPlaylist(ArrayList<Song> songs) {
            playlistModel.clear();
            for (Song song : songs) {
                playlistModel.addElement(song);
            }
        }
        
        public Song getSelectedSong() {
            return playlist.getSelectedValue();
        }
        
        public void setSelectedSong(Song song) {
            playlist.setSelectedValue(song, true);
        }
        
        public void setPlayButtonEnabled(boolean enabled) {
            playButton.setEnabled(enabled);
        }
        
        public void setPauseButtonEnabled(boolean enabled) {
            pauseButton.setEnabled(enabled);
        }
        
        public void setStopButtonEnabled(boolean enabled) {
            stopButton.setEnabled(enabled);
        }
        
        @Override
        public void stateChanged(ChangeEvent e) {
            Song currentSong = model.getCurrentSong();
            boolean playing = model.isPlaying();
            boolean paused = model.isPaused();
            if (currentSong != null) {
                setSelectedSong(currentSong);
                titleField.setText(currentSong.getTitle());
                artistField.setText(currentSong.getArtist());
                durationField.setText(Integer.toString(currentSong.getDuration()));
            }
            setPlayButtonEnabled(!playing);
            setPauseButtonEnabled(playing && !paused);
            setStopButtonEnabled(playing);
        }
    }        