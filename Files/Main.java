import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MusicPlayerModel model = new MusicPlayerModel();
                MusicPlayerView view = new MusicPlayerView(model);
                MusicPlayerController controller = new MusicPlayerController(view, model);
                view.setVisible(true);
            }
        });
    }
}