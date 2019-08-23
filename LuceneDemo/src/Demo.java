import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.lang.ProcessBuilder;

public class Demo {
	private static Thread lock = new Thread();
	  public static void main(String[] args) throws IOException, ParseException{
	        // 0. Specify the analyzer to tokenize our text.
	        //    The same analyzer should be used for indexing and searching
	        StandardAnalyzer analyzer = new StandardAnalyzer();

	        // 1. Create the index
	        BufferedReader namereader;
			List<String> nameslist = new ArrayList<String>();
			try {
				namereader = new BufferedReader(new FileReader(
						"/Users/bennypham/Desktop/Winter Quarter/CS172/Final Project/finalproject-ir/LuceneDemo/names.txt"));
				String line = namereader.readLine();
				while (line != null) {
					nameslist.add(line);
					line = namereader.readLine();
				}
				namereader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
	        BufferedReader textreader;
			List<String> textlist = new ArrayList<String>();
			try {
				textreader = new BufferedReader(new FileReader(
						"/Users/bennypham/Desktop/Winter Quarter/CS172/Final Project/finalproject-ir/LuceneDemo/text.txt"));
				String line = textreader.readLine();
				while (line != null) {
					textlist.add(line);
					line = textreader.readLine();
				}
				textreader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			BufferedReader latitudereader;
			List<String> latitudelist = new ArrayList<String>();
			try {
				latitudereader = new BufferedReader(new FileReader(
						"/Users/bennypham/Desktop/Winter Quarter/CS172/Final Project/finalproject-ir/LuceneDemo/latitude.txt"));
				String line = latitudereader.readLine();
				while (line != null) {
					latitudelist.add(line);
					line = latitudereader.readLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			BufferedReader longitudereader;
			List<String> longitudelist = new ArrayList<String>();
			try {
				longitudereader = new BufferedReader(new FileReader(
						"/Users/bennypham/Desktop/Winter Quarter/CS172/Final Project/finalproject-ir/LuceneDemo/longitude.txt"));
				String line = longitudereader.readLine();
				while (line != null) {
					longitudelist.add(line);
					line = longitudereader.readLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			BufferedReader urlreader;
			List<String> urllist = new ArrayList<String>();
			try {
				urlreader = new BufferedReader(new FileReader(
						"/Users/bennypham/Desktop/Winter Quarter/CS172/Final Project/finalproject-ir/LuceneDemo/tweet_url.txt"));
				String line = urlreader.readLine();
				while (line != null) {
					urllist.add(line);
					line = urlreader.readLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			BufferedReader titlereader;
			List<String> titlelist = new ArrayList<String>();
			try {
				titlereader = new BufferedReader(new FileReader(
						"/Users/bennypham/Desktop/Winter Quarter/CS172/Final Project/finalproject-ir/LuceneDemo/title.txt"));
				String line = titlereader.readLine();
				while (line != null) {
					titlelist.add(line);
					line = titlereader.readLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
	        Directory index = new RAMDirectory();

	        IndexWriterConfig config = new IndexWriterConfig(analyzer);

	        IndexWriter w = new IndexWriter(index, config);
	        
			for (int i = 0; i < nameslist.size(); i++) {
				
				String current_name = nameslist.get(i);
				String current_text = textlist.get(i);
				String current_longitude = longitudelist.get(i);
				String current_latitude = latitudelist.get(i);
				String current_url = urllist.get(i);
				String current_title = titlelist.get(i);
				addDoc(w, current_name, current_text, current_longitude, current_latitude, current_url, current_title);
			}
	        w.close();
	        
	        // 2. Query
	        // the "title" arg specifies the default field to use
	        // when no field is explicitly specified in the query.
	        String querystr = "";
	        while (querystr == null || (querystr.length() == 0)) {
	        	
	        	try {
	        		querystr = JOptionPane.showInputDialog("Enter your query:");
	        	}
	        	catch (NullPointerException ex) {
	        		querystr = "";
	        	}

	        }
	        Query q = new QueryParser("text", analyzer).parse(querystr);

	        // 3. Search query
	        IndexReader reader = DirectoryReader.open(index);
	        int max_docs = reader.numDocs();
	        Scanner intscan = new Scanner(System.in);
	        int hitsPerPage = 0;
	        while (hitsPerPage == 0) {
	        	String string_int = JOptionPane.showInputDialog("How many hits would you like to see? (max: " + max_docs
		        		+ ") \n Enter an integer:");
		        try {
		        	Integer.parseInt(string_int);
		        	hitsPerPage = Integer.parseInt(string_int);
		        }
		        catch (NumberFormatException ex) {
		        	hitsPerPage = 0;
		        }
	        }
	        
	        IndexSearcher searcher = new IndexSearcher(reader);
	        TopDocs docs = searcher.search(q, hitsPerPage);
	        ScoreDoc[] hits = docs.scoreDocs;
	        
	        // 4. Display results and write to text file
	        int num_found = 0;
	        System.out.println("Found " + hits.length + " hits.");
	        for(int i=0;i<hits.length;++i) {
	            int docId = hits[i].doc;
	            Document d = searcher.doc(docId);
	            System.out.println((i + 1) + ". " + d.get("TwitterHandle") + "\t \t" + d.get("text") +
	            		"\t Longitude: " + d.get("Longitude") + " Latitude: " + d.get("Latitude") +
	            		"Title: " + d.get("Title")+ " URL: " + d.get("URL"));
	            double user_longitude = -118.668404;//-117.3215109;
	            double user_latitude = 33.704538;//33.9799503;
	            double tweet_longitude = Double.parseDouble(d.get("Longitude"));
	            double tweet_latitude = Double.parseDouble(d.get("Latitude"));
	            double calculate_distance = distance(user_latitude, user_longitude,
	            		tweet_latitude, tweet_longitude, 'M');
	            double miles_cap = 10000.0;
	            
	            if (calculate_distance < miles_cap) {
	            	String string_to_write = "Twitter Username: " + d.get("TwitterHandle") +
	            			" || " + d.get("text") + " || Linked: " + d.get("Title") + " @ " + d.get("URL");
	            	File myFile = new File("input_file_text.txt");
	            	myFile.createNewFile();
	            	File myFile2 = new File("input_file_lat.txt");
	            	myFile2.createNewFile();
	            	File myFile3 = new File("input_file_long.txt");
	            	myFile3.createNewFile();
	            	BufferedWriter writer = new BufferedWriter(new FileWriter("input_file_text.txt", true));
	            	writer.write(string_to_write);
	            	writer.newLine();
	            	writer.close();
	            	BufferedWriter latwriter = new BufferedWriter(new FileWriter("input_file_lat.txt", true));
	            	String lat_to_write = d.get("Latitude");
	            	latwriter.write(lat_to_write);
	            	latwriter.newLine();
	            	latwriter.close();
	            	BufferedWriter longwriter = new BufferedWriter(new FileWriter("input_file_long.txt", true));
	            	String long_to_write = d.get("Longitude");
	            	longwriter.write(long_to_write);
	            	longwriter.newLine();
	            	longwriter.close();
	            	num_found++;
	            }
	            
	        }
	        // Reader can only be closed when there is no need to access the documents any more.
	        reader.close();
	        System.out.println("We found " + num_found + " tweets near you!");
	        
	        // ASSUMES THAT USER USES MAC TO ACCESS COMMAND PROMPT
	        String cmd =  "/usr/bin/python3 /Users/bennypham/Desktop/Winter Quarter/CS172/Final Project/finalproject-ir/LuceneDemo/manage.py runserver";
	       Process p = Runtime.getRuntime().exec("ps -u ");
	    	//Runtime.getRuntime().exec(System.getenv("windir") + "/System" + "tasklist.exe");
	       BufferedReader checkProcesses = new BufferedReader(new InputStreamReader(p.getInputStream()));
	       String line;
	       String pidinfo = "";
	       while ((line = checkProcesses.readLine()) != null) {
	    	   pidinfo += line;
	       }
	       checkProcesses.close();
	       if (!pidinfo.contains("python3")) {
	    	   Process proc = Runtime.getRuntime().exec("/usr/bin/open -a Terminal /usr/bin/python3 /Users/bennypham/Desktop/Winter Quarter/CS172/Final Project/finalproject-ir/LuceneDemo/manage.py runserver");
	    	   //Process proc = Runtime.getRuntime().exec("cmd.exe /c start C:\\Users\\chian\\Anaconda3\\python.exe C:\\Users\\chian\\eclipse-workspace\\LuceneDemo\\manage.py runserver");
	       }try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	       	openWebpage("http://localhost:8000/locations/");
	    }

	    private static void addDoc(IndexWriter w, String TwitterHandle, String text, String Longitude, String Latitude,
	    		String URL, String Title) throws IOException {
	        Document doc = new Document();
	        doc.add(new StringField("TwitterHandle", TwitterHandle, Field.Store.YES));
	        doc.add(new StringField("Longitude", Longitude, Field.Store.YES));
	        doc.add(new StringField("Latitude", Latitude, Field.Store.YES));
	        doc.add(new StringField("URL", URL, Field.Store.YES));
	        doc.add(new TextField("text", text, Field.Store.YES));
	        doc.add(new StringField("Title", Title, Field.Store.YES));
	        w.addDocument(doc);
	    }
	    
	    private static double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
	        double theta = lon1 - lon2;
	        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
	        dist = Math.acos(dist);
	        dist = rad2deg(dist);
	        dist = dist * 60 * 1.1515;
	        if (unit == 'K') {
	          dist = dist * 1.609344;
	        } else if (unit == 'N') {
	          dist = dist * 0.8684;
	          }
	        return (dist);
	      }

	      /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	      /*::  This function converts decimal degrees to radians             :*/
	      /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	      private static double deg2rad(double deg) {
	        return (deg * Math.PI / 180.0);
	      }

	      /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	      /*::  This function converts radians to decimal degrees             :*/
	      /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	      private static double rad2deg(double rad) {
	        return (rad * 180.0 / Math.PI);
	      }
	      
	      public static void openWebpage(String urlString) {
	    	    try {
	    	        Desktop.getDesktop().browse(new URL(urlString).toURI());
	    	    } catch (Exception e) {
	    	        e.printStackTrace();
	    	    }
	    	}
}
