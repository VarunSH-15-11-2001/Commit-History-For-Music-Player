import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.sound.sampled.*;

import java.io.File;
import java.io.IOException;


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
    // public JButton stopButton = new JButton("Stop");

    public JButton nextButton = new JButton("Next Song");
    public JButton prevButton = new JButton("Prev Song");
    private JList<Song> playlist = new JList<Song>();
    private DefaultListModel<Song> playlistModel = new DefaultListModel<>();
    int here=0,before,next;

    private JFrame frame;
    private Clip clip;
    ArrayList <Clip> clipPlayList = new ArrayList<Clip>();

    private JProgressBar progressBar;
    GridBagConstraints gbc = new GridBagConstraints();

    Clip currSong;

    File dir = new File("/Users/varunshankarhoskere/Downloads/Junk");


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
        // buttonPanel.add(stopButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(prevButton);
        add(buttonPanel, BorderLayout.CENTER);
        // add(statusLabel,BorderLayout.SOUTH);
        playlist.setModel(playlistModel);
        playlistModel.addAll(model.getPlaylist());
        pack();
        setLocationRelativeTo(null);
        setResizable(true);
        
        String songs = "\n";
        createPlaylist(songs);

    }

    public void songMaker() {
        {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter(
                "Audio Files", "wav", "mp3", "au", "aif"));
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                titleLabel.setText(selectedFile.getAbsolutePath());
                
                if (clip != null) {
                    clip.stop();
                    clip.close();
                }

                
                try {
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                        selectedFile);
                    clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                } catch (UnsupportedAudioFileException ex) {
                    ex.printStackTrace();
                    
                } catch (IOException ex) {
                    ex.printStackTrace();
                    
                } catch (LineUnavailableException ex) {
                    ex.printStackTrace();
                    
                }

                clip.start();
            }
        }
        
    }

    public void createPlaylist(String songs) {
        File dir = new File("/Users/varunshankarhoskere/Downloads/Junk");
        for (File file : dir.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".wav")) {
                String title = file.getName().substring(0, file.getName().lastIndexOf('.'));
                Song song = new Song(title, "unknown artist", 0);
                playlistModel.addElement(song);
                // System.out.println(song);
                
                try {
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
                    clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                } catch (UnsupportedAudioFileException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (LineUnavailableException ex) {
                    ex.printStackTrace();
                }
                clipPlayList.add(clip);
            }
        }

        playlist.setModel(playlistModel);   

        playlist.revalidate();
        for (int i = 0; i < playlistModel.size(); i++) {
            Song song = playlistModel.getElementAt(i);
            songs = songs + song.getTitle() + "\n";
        }

        
        JTextArea textArea = new JTextArea(songs);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.SOUTH);

    }
    

    public void addListeners(ActionListener listener) {
        addButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                songMaker();
            }
        });    
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currSong = clipPlayList.get(here);
                currSong.start();
            }
        });
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currSong.stop();
            }
        });
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currSong.stop();
                here+=1;
                currSong = clipPlayList.get(here);
                currSong.start();
            }
        });

        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currSong.stop();
                here-=1;
                currSong = clipPlayList.get(here);
                currSong.start();
            }
        });
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
        
        // public void setStopButtonEnabled(boolean enabled) {
        //     stopButton.setEnabled(enabled);
        // }
        
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
            // setStopButtonEnabled(playing);
        }
    }        