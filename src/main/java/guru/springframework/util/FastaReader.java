package guru.springframework.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.springframework.stereotype.Component;

/**
 *
 * @author rohanrampuria
 */
@Component
public class FastaReader {

    /**
     * @param args the command line arguments
     */
    /*public static void main(String[] args) {
        String filename="/Users/rohanrampuria/Downloads/uniprot-yeast.fasta";
        FastaReader fr = new FastaReader();
        fr.getproteinCount(filename);
    }*/
    public int getproteinCount (String filename){
        File file = new File(filename);
        int proteinCount =0;
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String eachLine = null;
            while((eachLine=br.readLine()) != null){
                if(eachLine.startsWith(">")){
                    proteinCount++;
                }
            }  
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return proteinCount;
    }
}
