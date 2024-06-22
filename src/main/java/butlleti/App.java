package butlleti;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

/**
 * Xapucilla ràpida per dividir els butlletins de notes de l'alumnat 
 * 
 *
 */
public class App {
	
	// value where we can consider that this is a blank image
    // can be much higher or lower depending of what is considered as a blank page
    public static final int BLANK_THRESHOLD = 160;
//    public static final String directoriFitxersResultat = "C:\\Users\\Administrador\\Downloads\\notes\\";
//    public static final String pathNomFitxerNotes = "C:\\Users\\Administrador\\Downloads\\butlleti.pdf";
    
    public static final String PATH_OUTPUT_DIR= "/tmp/";
    public static final String PATH_INPUT_FILE= "/tmp/notes.pdf";
    
    
    public static void main( String[] args ) throws IOException, DocumentException {
    	
    	generaPdfAlumnes(PATH_INPUT_FILE, PATH_OUTPUT_DIR);
    }
    
    private static void generaPdfAlumnes(String pathSencerButlleti, String directoriResultat) throws IOException, DocumentException{
    	
    	PdfReader reader = new PdfReader(pathSencerButlleti);
    	int numberPages = reader.getNumberOfPages();
    	PdfReaderContentParser parser = new PdfReaderContentParser(reader);
    	TextExtractionStrategy strategy = parser.processContent(1, new SimpleTextExtractionStrategy());
    	String nomFitxerAnterior = xapuciExtreuNom(strategy.getResultantText(), 1);
    	
    	
    	int paginaInicialAlumne = 0;
    	for (int paginaActual = 1; paginaActual <= numberPages; paginaActual++) {
    
    		strategy = parser.processContent(paginaActual, new SimpleTextExtractionStrategy());
      
    		if(reader.getPageContent(paginaActual).length > BLANK_THRESHOLD || paginaActual==numberPages) {
    			String nomFitxer = xapuciExtreuNom(strategy.getResultantText(), paginaActual);
	    		if (!nomFitxerAnterior.equals(nomFitxer) || paginaActual==numberPages) {
	    			
	    			if(paginaActual==numberPages) {
	    				paginaActual++;
	    			}
	    			
	    			escriuNotesAlumnePdf(directoriResultat, reader, paginaInicialAlumne, paginaActual);
	    			paginaInicialAlumne = paginaActual-1;
	    		}
	    		nomFitxerAnterior = nomFitxer;
    		}
    	}
    }
    

    
    private static void escriuNotesAlumnePdf(String directoriResultat, PdfReader reader, int paginaInicialAlumne, int paginaFinalAlumne) throws IOException, DocumentException {
    	
    	paginaInicialAlumne++;
    	Document document = new Document(reader.getPageSizeWithRotation(1));
    	PdfReaderContentParser parser = new PdfReaderContentParser(reader);
    	TextExtractionStrategy strategy = parser.processContent(paginaInicialAlumne, new SimpleTextExtractionStrategy());
    	String nomFitxer = xapuciExtreuNom(strategy.getResultantText(), paginaFinalAlumne);
    	PdfCopy writer = new PdfCopy(document, new FileOutputStream(directoriResultat+nomFitxer+".pdf"));
        document.open();
      
        for(int i = paginaInicialAlumne; i < paginaFinalAlumne; i++) {
        	if(reader.getPageContent(i).length > BLANK_THRESHOLD) {
        		PdfImportedPage page = writer.getImportedPage(reader, i);
        		writer.addPage(page);
        	}
        }
        document.close();
        writer.close();
        
    }
    
    /**
     * Xapuci rendiment nefast!! Codi fet en 5 minuts, em fa mandra dedicar neurones!!!!!!
     * TODO: Si algun dia es fa seriosament no way fer-ho així 
     * @param textPaginaNotes
     * @param paginaFinalAlumne
     * @return
     */
    private static String xapuciExtreuNom(String textPaginaNotes, int paginaFinalAlumne) {
    	if(textPaginaNotes!=null && !textPaginaNotes.isBlank()) {
    		int posicioDadesAlumnes = textPaginaNotes.indexOf("Alumne");
    		int posicioSalt = textPaginaNotes.indexOf("\n", posicioDadesAlumnes);
    		int posicioSaltFinal = textPaginaNotes.indexOf("\n", posicioSalt+1);
    		return textPaginaNotes.substring(posicioSalt+1, posicioSaltFinal);
    	}
    	return "";
    }
    
    
    
//    public static void main( String[] args ) throws IOException, DocumentException {
//    	
//    	if(args!=null && args.length==2) {
//    		
//    		if(args[1].endsWith("\"")) {
//    			args[1]=args[1].substring(0, args[1].length()-1);
//    		}
//    		
//    		if(!args[1].endsWith(File.separator)) {
//    			args[1] = args[1]+File.separator;
//    		}
//    		generaPdfAlumnes(args[0], args[1]);
//    		System.out.println("Processat fitxer: "+args[0]);
//    		System.out.println("Directori de sortida: "+args[1]);
//    	}else {
//    		generaPdfAlumnes(pathNomFitxerNotes, directoriFitxersResultat);
//    	}
//    }
    
    

    

}
