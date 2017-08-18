package com.neu.edu.document.search.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.xml.sax.SAXException;

public class FileIndexer {

	public static final String FIELD_NAME = "filename";

	public static void main(String[] args) {
		// Input folder
		String docsPath = "inputFiles";

		// Output folder
		String indexPath = "indexedFiles";

		// Input Path Variable
		final Path docDir = Paths.get(docsPath);

		try {
			// org.apache.lucene.store.Directory instance
			Directory dir = FSDirectory.open(Paths.get(indexPath));

			// analyzer with the default stop words
			Analyzer analyzer = new StandardAnalyzer();

			// IndexWriter Configuration
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);

			// IndexWriter writes new index files to the directory
			IndexWriter writer = new IndexWriter(dir, iwc);

			// Its recursive method to iterate all files and directories
			indexDocs(writer, docDir);

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void indexDocs(final IndexWriter writer, Path path) throws IOException {
		// Directory?
		if (Files.isDirectory(path)) {
			// Iterate directory
			Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					try {
						FileHandlingParser fileParser = new FileHandlingParser();

						fileParser.checkFile(file);
						// Index this file
						System.out.println("in if part");
						indexDoc(writer, file, attrs.lastModifiedTime().toMillis());
					} catch (IOException ioe) {
						ioe.printStackTrace();
					} catch (SAXException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return FileVisitResult.CONTINUE;
				}
			});
		} else {
			// Index this file
			indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis());
		}
	}

	static void indexDoc(IndexWriter writer, Path file, long lastModified) throws IOException {
		try (InputStream stream = Files.newInputStream(file)) {

			String fileType = file.toString().substring(file.toString().length() - 3, file.toString().length());
			System.out.println("indexDoc:- " + file + " fileType is:- " + fileType);

			// Create lucene Document
			Document doc = new Document();

			if (file.toString().contains("pdf")) {

				PDFParser parser = new PDFParser(stream);
				parser.parse();
				COSDocument cd = parser.getDocument();
				PDFTextStripper stripper = new PDFTextStripper();
				String text = stripper.getText(new PDDocument(cd));
				cd.close();
				doc.add(new StringField("path", file.toString(), Field.Store.YES));
				doc.add(new LongPoint("modified", lastModified));
				doc.add(new TextField("contents", text, Store.YES));
				doc.add(new TextField(FIELD_NAME, file.toString(), Field.Store.YES));
			} else {
				doc.add(new StringField("path", file.toString(), Field.Store.YES));
				doc.add(new LongPoint("modified", lastModified));
				doc.add(new TextField("contents", new String(Files.readAllBytes(file)), Store.YES));
				doc.add(new TextField(FIELD_NAME, file.toString(), Field.Store.YES));
			}

			writer.updateDocument(new Term("path", file.toString()), doc);
		}
	}
}
