import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.logging.Handler;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.sound.sampled.*;
import javax.swing.border.EmptyBorder;

import java.io.File;
import java.io.IOException;

import java.util.Timer;
import java.util.TimerTask;


public class MusicPlayerView extends JFrame implements ChangeListener {

    private JLabel titleLabel = new JLabel("Set Timer Duration:");
    private JLabel artistLabel = new JLabel("Artist:");
    private JLabel durationLabel = new JLabel("Duration (s):");

    private JTextField titleField = new JTextField(20);

    private JTextField artistField = new JTextField(20);
    private JTextField durationField = new JTextField(5);
    public JButton addButton = new JButton("Choose file");
    public JButton playButton = new JButton("Play");
    public JButton pauseButton = new JButton("Pause");
    public JButton setTimer = new JButton("Set Timer");
    public JButton setTheme = new JButton("Set Theme");
    private JPanel contentPane;

    public JButton nextButton = new JButton("Next Song");
    public JButton prevButton = new JButton("Prev Song");
    private JList<Song> playlist = new JList<Song>();
    private DefaultListModel<Song> playlistModel = new DefaultListModel<>();
    int here=0,before,next;

    private JFrame frame;
    private Clip clip;
    ArrayList <Clip> clipPlayList = new ArrayList<Clip>();

    private JProgressBar progressBar = new JProgressBar();;
    GridBagConstraints gbc = new GridBagConstraints();

    Clip currSong;

    File dir = new File("/Users/varunshankarhoskere/Downloads/Junk");

    private Timer timer;

    private JLabel statusLabel;

    private Handler handler;
    private Runnable runnable;

    String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    
    JList<String> fontList = new JList<>(fontNames);

    JScrollPane scrollPane = new JScrollPane(fontList);


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
        // inputPanel.add(artistLabel);
        // inputPanel.add(artistField);
        // inputPanel.add(durationLabel);
        // inputPanel.add(durationField);
        add(inputPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(playButton);
        buttonPanel.add(pauseButton);
        // buttonPanel.add(stopButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(prevButton);
        buttonPanel.add(setTimer);
        buttonPanel.add(setTheme);
        add(buttonPanel, BorderLayout.CENTER);
        
        playlist.setModel(playlistModel);
        playlistModel.addAll(model.getPlaylist());
        pack();
        setLocationRelativeTo(null);
        setResizable(true);
        
        String songs = "\n";
        createPlaylist(songs);
        showThemes();
    }
    public void stopPlayingTimer(int time) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                currSong.stop();
                timer.cancel();
            }
        }, time * 1000);
    }

    public void showThemes() {
        JScrollPane scrollPane = new JScrollPane(fontList);
        fontList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(scrollPane, BorderLayout.EAST);
    }
    
    public void applyTheme(String fontName) {
        Font font = new Font(fontName,Font.PLAIN,13);
        addButton.setFont(font);
        playButton.setFont(font);
        pauseButton.setFont(font);
        nextButton.setFont(font);
        prevButton.setFont(font);
        setTimer.setFont(font);
        setTheme.setFont(font);
    }

    public void initContentPane() {
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new GridBagLayout());
        
        add(contentPane, BorderLayout.CENTER);

        progressBar = new JProgressBar();
    }

    public void chooseSong() {
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
                chooseSong();
            }
        });    
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initContentPane();
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
                currSong.setFramePosition(0);
                here+=1;
                currSong = clipPlayList.get(here);
                currSong.start();
            }
        });

        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currSong.stop();
                currSong.setFramePosition(0);
                here-=1;
                currSong = clipPlayList.get(here);
                currSong.start();
            }
        });

        setTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopPlayingTimer(Integer.parseInt(titleField.getText()));
                System.out.println(titleField.getText());
            }
        });

        setTheme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String font = (String) fontList.getSelectedValue();
                applyTheme(font);
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