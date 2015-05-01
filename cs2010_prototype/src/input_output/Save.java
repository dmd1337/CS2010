package input_output;
import genes.Gene;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import biomorphHandling.*;
/**
 * Class to handle exporting of Biomorphs.
 * @author Tom Connolly
 * @version 04/03/2015
 */
public class Save
{
	static FileOutputStream fop = null;
	static File file;
	static String anchorDestination = "src/biomorphTextFiles/";

	public Save(Gene[] geneValues, String fileName)
	{
		try
		{
			String content = "";
			for (Gene gene : geneValues)
			{
				content = content + gene.getValue() + ",";
			}
			File dir = new File(anchorDestination);
			if(!(dir.exists())){
				dir.mkdir();
			}
			// save file to src
			file = new File(anchorDestination + fileName + ".txt");
			fop = new FileOutputStream(file);
			// if file doesnt exists, then create it
			if (!file.exists())
			{
				file.createNewFile();
			}
			// get the content in bytes
			byte[] contentInBytes = content.getBytes();
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
			System.out.println("Genes have been saved.");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (fop != null)
				{
					fop.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args){
		BiomorphCreator bc = new BiomorphCreator();
		Biomorph biomorph = bc.createBiomorph();
		Save save = new Save(biomorph.getGenes(), "biomorph");
	}
	
	
}