package tryDistiller;

import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by @kash on 3/9/2018.
 */
public class FileExplorer {
    FileDistiller distiller;
    List<SourceCodeChange> changes;

    FileExplorer(FileDistiller distiller)   {
        this.distiller = distiller;
        changes = new ArrayList<>();
    }

    public static void main(String[] args)  {
        String path1 = "F:\\Study-Ebooks\\March-2018\\Paper\\Test\\proj1";
        String path2 = "F:\\Study-Ebooks\\March-2018\\Paper\\Test\\proj2";
        File proj1 = new File(path1);
        File proj2 = new File(path2);
        FileDistiller distiller = ChangeDistiller.createFileDistiller(ChangeDistiller.Language.JAVA);
        FileExplorer explorer = new FileExplorer(distiller);
        explorer.explore(0, path1, proj1, path2, proj2);
        explorer.printChanges();
    }

    void explore(int level, String path1, File file1, String path2, File file2) {
        if(file1.isDirectory() && file2.isDirectory())  {
            File[] list1 = file1.listFiles();
            File[] list2 = file2.listFiles();
            for(int i = 0; i < list1.length; i++)   {
                explore(level + 1, path1 + "/" + list1[i].getName(), list1[i],
                        path2 + "/" + list2[i].getName(), list2[i]);
            }
        }
        else {
            if(file1.getName().endsWith(".java") && file2.getName().endsWith(".java"))  {
                System.out.println("Working on [" + file1.getName() + "]");
                distiller.extractClassifiedSourceCodeChanges(file1, file2);
                List<SourceCodeChange> changeList = distiller.getSourceCodeChanges();
                changes.addAll(changeList);
            }
        }
    }

    void printChanges() {
        System.out.println("Number of changes = " + changes.size());
        for(int i = 0; i < changes.size(); i++) {
            System.out.println(changes.get(i).getLabel());
        }
    }
}
