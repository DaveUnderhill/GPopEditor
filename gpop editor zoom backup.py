import sys
import os  # Importieren Sie das Modul os
from PyQt5.QtWidgets import QApplication, QMainWindow, QVBoxLayout, QWidget
import matplotlib.pyplot as plt
from matplotlib.backends.backend_qt5agg import FigureCanvasQTAgg as FigureCanvas
import librosa
import librosa.display

class NoteTimelineWindow(QMainWindow):
    def __init__(self):
        super().__init__()

        self.setWindowTitle('GPop Editor (Version 0.01)')
        self.setGeometry(100, 100, 1400, 800)

        self.init_ui()

    def init_ui(self):
        central_widget = QWidget()
        self.setCentralWidget(central_widget)

        layout = QVBoxLayout()
        central_widget.setLayout(layout)

        # Bestimmen des absoluten Pfads zum Verzeichnis, in dem sich das Skript befindet
        script_dir = os.path.dirname(os.path.abspath(__file__))
        # Verwendung des relativen Pfads zum Laden der Audiodatei
        audio_file = os.path.join(script_dir, 'test.mp3')

        # Laden der Audiodatei
        y, sr = librosa.load(audio_file)

        # Erstellen des Zeitbereichs für die X-Achse basierend auf der Länge der Audiodatei
        x = range(len(y))

        # Erstellen einer neuen Matplotlib-Figur und Zeichnen der Wellenform
        self.fig, self.ax = plt.subplots()
        self.line, = self.ax.plot(x, y)
        self.ax.set_title('Wellenform')
        
        # Hinzufügen der Figur in das PyQt-Fenster
        self.canvas = FigureCanvas(self.fig)
        layout.addWidget(self.canvas)

        # Verbinden des Mausrad-Ereignisses
        self.canvas.mpl_connect('scroll_event', self.on_scroll)

    

    def on_scroll(self, event):
        # Position der Maus in den Achsenkoordinaten umwandeln
        x, y = event.xdata, event.ydata
        
        # Zoomfaktor berechnen
        if event.button == 'up':
            zoom_factor = 0.9
        elif event.button == 'down':
            zoom_factor = 1.1
        else:
            return
        
        # Neue Breite der Achsen definieren
        xmin, xmax = self.ax.get_xlim()
        new_width = (xmax - xmin) * zoom_factor
        
        # Neue Achsenpositionen berechnen
        new_xmin = x - (x - xmin) * (zoom_factor)
        new_xmax = new_xmin + new_width
        
        # Achsenbegrenzungen setzen
        self.ax.set_xlim(new_xmin, new_xmax)
        
        # Neuzeichnen der Wellenform
        self.canvas.draw()

def main():
    app = QApplication(sys.argv)
    window = NoteTimelineWindow()
    window.show()
    sys.exit(app.exec_())

if __name__ == '__main__':
    main()