package tryDistiller;

import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

import java.io.File;
import java.util.List;

/**
 * Created by @kash on 3/8/2018.
 */
public class firstTry {
    public static void main(String[] args)  {
        System.out.println("I am change Distiller");
        File left = new File("F:\\Study-Ebooks\\March-2018\\Paper\\Test\\file_v1.java");
        File right = new File("F:\\Study-Ebooks\\March-2018\\Paper\\Test\\file_v2.java");

        FileDistiller distiller = ChangeDistiller.createFileDistiller(ChangeDistiller.Language.JAVA);

        try {
            distiller.extractClassifiedSourceCodeChanges(null, right);
        }
        catch(Exception e)  {
            System.out.println(e.getMessage());
        }

        List<SourceCodeChange> changes = distiller.getSourceCodeChanges();
        if(changes != null) {
            System.out.println(changes.size());
            for(SourceCodeChange change: changes)   {
                System.out.println(change.getChangedEntity().getUniqueName());
            }
        }
    }
}
