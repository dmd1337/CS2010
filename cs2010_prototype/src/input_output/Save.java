package input_output;

import genes.Gene;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
/**
 * Class to handle exporting of Biomorphs.
 * @author Tom Connolly
 * @version 04/03/2015
 */
public class Save
{
	static FileOutputStream fop = null;
	static File file;
	
	
	public Save()
	{
	}
	public void changeSaveDestination()
	{
	}
	public void saveToPNG()
	{
	}
	public void saveToJPG()
	{
	}
	public void saveToPDF()
	{
	}
	public void serialiseBiomorph()
	{
	}
	public void saveGeneValuesToTextFile(Gene[] geneValues, String fileName){
		try {
			String content = "";
			for(Gene gene : geneValues){
				content = content + gene.getValue() + ", ";
			}
			//save file to src
			file = new File("src/" + fileName + ".txt");
			fop = new FileOutputStream(file);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			// get the content in bytes
			byte[] contentInBytes = content.getBytes();
 
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
			
			System.out.println("Genes have been saved.");
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
/*	//main method for testing
	public static void main(String[] args){
		int[] vals = {1,2,3,4,5,6,7,8,9,10,11};
		saveGeneValuesToTextFile(vals, "Biomorph 1");
	}*/
}