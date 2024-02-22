import sys
from PyQt5.QtWidgets import QApplication, QMainWindow, QLabel, QVBoxLayout, QWidget


class NoteTimelineWindow(QMainWindow):
    def __init__(self):
        super().__init__()

        self.setWindowTitle('GPop Editor')
        self.setGeometry(100, 100, 1400, 800)

        self.init_ui()

    def init_ui(self):
        central_widget = QWidget()
        self.setCentralWidget(central_widget)

        layout = QVBoxLayout()
        central_widget.setLayout(layout)

        label = QLabel('Hier könnte Ihre Noten-Timeline sein!')
        layout.addWidget(label)


def main():
    app = QApplication(sys.argv)
    window = NoteTimelineWindow()
    window.show()
    sys.exit(app.exec_())


if __name__ == '__main__':
    main()
