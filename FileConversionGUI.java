import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class FileConversionGUI extends JFrame {
    private JProgressBar overallProgressBar;
    private JPanel filePanel;
    private DefaultListModel<File> fileListModel;
    private JButton startButton, cancelButton, downloadButton;
    private File[] convertedFiles;

    public FileConversionGUI() {
        setTitle("File Conversion");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        JButton selectButton = new JButton("Select Files");
        selectButton.addActionListener(new SelectButtonListener());
        topPanel.add(selectButton);

        String[] conversionOptions = {"PDF to DOCX", "Image Resize"};
        JComboBox<String> conversionComboBox = new JComboBox<>(conversionOptions);
        topPanel.add(conversionComboBox);

        startButton = new JButton("Start");
        startButton.addActionListener(new StartButtonListener(conversionComboBox));
        topPanel.add(startButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new CancelButtonListener());
        cancelButton.setEnabled(false);
        topPanel.add(cancelButton);

        downloadButton = new JButton("Download");
        downloadButton.addActionListener(new DownloadButtonListener());
        downloadButton.setEnabled(false);
        topPanel.add(downloadButton);

        add(topPanel, BorderLayout.NORTH);

        fileListModel = new DefaultListModel<>();
        JList<File> fileList = new JList<>(fileListModel);
        fileList.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(new JScrollPane(fileList), BorderLayout.CENTER);

        filePanel = new JPanel();
        filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.Y_AXIS));
        add(filePanel, BorderLayout.EAST);

        overallProgressBar = new JProgressBar();
        overallProgressBar.setStringPainted(true);
        add(overallProgressBar, BorderLayout.SOUTH);
    }

    private class SelectButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(true);
            fileChooser.setFileFilter(new FileNameExtensionFilter("Files", "pdf", "jpg", "png"));
            int result = fileChooser.showOpenDialog(FileConversionGUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File[] selectedFiles = fileChooser.getSelectedFiles();
                for (File file : selectedFiles) {
                    fileListModel.addElement(file);
                }
            }
        }
    }

    private class StartButtonListener implements ActionListener {
        private JComboBox<String> conversionComboBox;

        public StartButtonListener(JComboBox<String> conversionComboBox) {
            this.conversionComboBox = conversionComboBox;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            startButton.setEnabled(false);
            cancelButton.setEnabled(true);
            overallProgressBar.setMaximum(fileListModel.size());
            overallProgressBar.setValue(0);

            convertedFiles = new File[fileListModel.size()];

            String conversionType = (String) conversionComboBox.getSelectedItem();

            for (int i = 0; i < fileListModel.size(); i++) {
                File file = fileListModel.get(i);
                JProgressBar fileProgressBar = new JProgressBar(0, 100);
                fileProgressBar.setStringPainted(true);
                filePanel.add(new JLabel(file.getName()));
                filePanel.add(fileProgressBar);
                filePanel.revalidate();

                new FileConversionTask(file, fileProgressBar, i, conversionType).execute();
            }
        }
    }

    private class CancelButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Handle cancellation logic
        }
    }

    private class DownloadButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showSaveDialog(FileConversionGUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File downloadDir = fileChooser.getSelectedFile();
                try {
                    for (File convertedFile : convertedFiles) {
                        if (convertedFile != null && convertedFile.exists()) {
                            File destinationFile = new File(downloadDir, convertedFile.getName());
                            Files.copy(convertedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        } else {
                            JOptionPane.showMessageDialog(FileConversionGUI.this, "File not found: " + convertedFile);
                        }
                    }
                    JOptionPane.showMessageDialog(FileConversionGUI.this, "Files downloaded successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(FileConversionGUI.this, "Failed to download files: " + ex.getMessage());
                }
            }
        }
    }

    private class FileConversionTask extends SwingWorker<Void, Integer> {
        private final File file;
        private final JProgressBar progressBar;
        private final int index;
        private final String conversionType;

        public FileConversionTask(File file, JProgressBar progressBar, int index, String conversionType) {
            this.file = file;
            this.progressBar = progressBar;
            this.index = index;
            this.conversionType = conversionType;
        }

        @Override
        protected Void doInBackground() throws Exception {
            if (conversionType.equals("PDF to DOCX")) {
                convertPdfToDocx(file);
            } else if (conversionType.equals("Image Resize")) {
                resizeImage(file);
            }
            publish(100);
            return null;
        }

        @Override
        protected void process(List<Integer> chunks) {
            for (Integer value : chunks) {
                progressBar.setValue(value);
            }
        }

        @Override
        protected void done() {
            overallProgressBar.setValue(overallProgressBar.getValue() + 1);
            if (overallProgressBar.getValue() == overallProgressBar.getMaximum()) {
                startButton.setEnabled(true);
                cancelButton.setEnabled(false);
                downloadButton.setEnabled(true);
            }
        }

        private void convertPdfToDocx(File pdfFile) throws Exception {
            PDDocument pdfDocument = PDDocument.load(pdfFile);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(pdfDocument);
            pdfDocument.close();

            XWPFDocument docxDocument = new XWPFDocument();
            XWPFParagraph paragraph = docxDocument.createParagraph();
            paragraph.createRun().setText(text);

            File docxFile = new File(pdfFile.getParent(), pdfFile.getName().replace(".pdf", ".docx"));
            try (FileOutputStream out = new FileOutputStream(docxFile)) {
                docxDocument.write(out);
            }
            convertedFiles[index] = docxFile;
        }

        private void resizeImage(File imageFile) throws Exception {
            BufferedImage originalImage = ImageIO.read(imageFile);
            int newWidth = originalImage.getWidth() / 2;  // Example resize: half the width
            int newHeight = originalImage.getHeight() / 2; // Example resize: half the height
            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());

            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
            g.dispose();

            File resizedFile = new File(imageFile.getParent(), "resized_" + imageFile.getName());
            ImageIO.write(resizedImage, "jpg", resizedFile);
            convertedFiles[index] = resizedFile;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FileConversionGUI().setVisible(true));
    }
}
